package com.devjaewoo.openroadmaps.domain.client;

import com.devjaewoo.openroadmaps.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock ClientRepository clientRepository;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks
    ClientService clientService;

    @Nested
    @DisplayName("회원가입")
    class Register {

        @Nested
        @DisplayName("이메일/비밀번호")
        class EmailPassword {

            @Test
            @DisplayName("성공")
            public void success() {
                //given
                String email = "DevJaewoo@email.com";
                ClientDto.Register request = new ClientDto.Register(email, "12345678");

                // clientRepository Mock 설정
                given(clientRepository.existsByEmail(any())).willReturn(false);
                given(clientRepository.save(any(Client.class))).will(invocation -> {
                    Client argument = invocation.getArgument(0, Client.class);
                    argument.setId(1L);
                    return argument;
                });

                given(passwordEncoder.encode(any())).willReturn("$encoded_password$");

                //when
                ClientDto clientDto = clientService.register(request);

                //then
                assertThat(clientDto.id()).isGreaterThan(0);
                assertThat(clientDto.name()).isEqualTo("User#1");
                assertThat(clientDto.email()).isEqualTo(email.toLowerCase());
            }

            @Test
            @DisplayName("이메일 중복")
            public void duplicateEmail() {
                //given
                ClientDto.Register request = new ClientDto.Register("asd@email.com", "12345678");
                given(clientRepository.existsByEmail(any())).willReturn(true);

                //when
                Executable executable = () -> clientService.register(request);

                //then
                assertThrows(RestApiException.class, executable);
            }
        }

        @Nested
        @DisplayName("OAuth")
        class OAuth {

            @Test
            @DisplayName("성공")
            public void success() {
                //given
                String email = "devjaewoo@gmail.com";

                Map<String, Object> attributes = new HashMap<>();
                attributes.put("sub", "1234");
                attributes.put("name", "DevJaewoo");
                attributes.put("email", email);
                attributes.put("picture", "https://picture.com");

                OAuth2Attributes oAuth2Attributes = OAuth2Attributes.ofGoogle("google", "sub", attributes);

                // clientRepository Mock 설정
                given(clientRepository.save(any(Client.class))).will(invocation -> {
                    Client argument = invocation.getArgument(0, Client.class);
                    argument.setId(1L);
                    return argument;
                });

                //when
                ClientDto clientDto = clientService.registerOAuth(oAuth2Attributes);

                //then
                assertThat(clientDto.id()).isGreaterThan(0);
                assertThat(clientDto.name()).isEqualTo("User#1");
                assertThat(clientDto.email()).isEqualTo(email);
            }

            @Test
            @DisplayName("지원되지 않는 Registration")
            public void unsupportedRegistration() {
                //given
                OAuth2Attributes oAuth2Attributes = OAuth2Attributes.ofGoogle("unsupported", "null", new HashMap<>());

                //when
                Executable executable = () -> clientService.registerOAuth(oAuth2Attributes);

                //then
                assertThrows(RestApiException.class, executable);
            }
        }
    }

}