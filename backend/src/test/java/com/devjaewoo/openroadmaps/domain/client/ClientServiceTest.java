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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock ClientRepository clientRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock HttpSession httpSession;

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
                assertThrows(RestApiException.class, executable, ClientErrorCode.DUPLICATE_EMAIL.message);
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
                assertThrows(RestApiException.class, executable, ClientErrorCode.UNSUPPORTED_REGISTRATION.message);
            }
        }
    }

    @Nested
    @DisplayName("로그인")
    class Login {

        @Test
        @DisplayName("성공")
        public void success() {
            //given
            String email = "DevJaewoo@email.com";
            ClientDto.Register request = new ClientDto.Register(email, "12345678");
            Client client = Client.create("name", email, "password");

            given(clientRepository.findByEmailAndPasswordIsNotNull(any())).willReturn(Optional.of(client));
            given(passwordEncoder.matches(any(), any())).willReturn(true);

            //when
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
            ClientDto clientDto = clientService.login(request);

            //then
            assertThat(clientDto.email()).isEqualTo(email.toLowerCase());
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        }

        @Test
        @DisplayName("미등록된 이메일")
        public void incorrectEmail() {
            //given
            String email = "DevJaewoo@email.com";
            ClientDto.Register request = new ClientDto.Register(email, "12345678");

            given(clientRepository.findByEmailAndPasswordIsNotNull(any())).willReturn(Optional.empty());

            //when
            Executable executable = () -> clientService.login(request);

            //then
            assertThrows(RestApiException.class, executable, ClientErrorCode.INCORRECT_EMAIL.message);
        }

        @Test
        @DisplayName("비밀번호 불일치")
        public void incorrectPassword() {
            //given
            String email = "DevJaewoo@email.com";
            ClientDto.Register request = new ClientDto.Register(email, "12345678");
            Client client = Client.create("name", email, "password");

            given(clientRepository.findByEmailAndPasswordIsNotNull(any())).willReturn(Optional.of(client));
            given(passwordEncoder.matches(any(), any())).willReturn(false);

            //when
            Executable executable = () -> clientService.login(request);

            //then
            assertThrows(RestApiException.class, executable, ClientErrorCode.INCORRECT_PASSWORD.message);
        }

        @Test
        @DisplayName("비활성화된 사용자에 로그인 시도")
        public void disabledClient() {
            //given
            String email = "DevJaewoo@email.com";
            ClientDto.Register request = new ClientDto.Register(email, "12345678");
            Client client = Client.create("name", email, "password");
            client.setEnabled(false);

            given(clientRepository.findByEmailAndPasswordIsNotNull(any())).willReturn(Optional.of(client));
            // given(passwordEncoder.matches(any(), any())).willReturn(false); unnecessary code라고 함

            //when
            Executable executable = () -> clientService.login(request);

            //then
            assertThrows(RestApiException.class, executable, ClientErrorCode.INCORRECT_PASSWORD.message);
        }
    }

    @Nested
    @DisplayName("로그아웃")
    class Logout {

        @Test
        @DisplayName("성공")
        public void success() {
            //given
            String email = "DevJaewoo@email.com";
            ClientDto.Register request = new ClientDto.Register(email, "12345678");
            Client client = Client.create("name", email, "password");

            given(clientRepository.findByEmailAndPasswordIsNotNull(any())).willReturn(Optional.of(client));
            given(passwordEncoder.matches(any(), any())).willReturn(true);

            clientService.login(request);

            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();

            //when
            clientService.logout();

            //then
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        }
    }
}