package com.devjaewoo.openroadmaps.domain.client;

import com.devjaewoo.openroadmaps.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ClientDto register(ClientDto.Register request) {

        // 이메일 중복 여부 체크
        if(clientRepository.existsByEmail(request.email().toLowerCase())) {
            throw new RestApiException(ClientErrorCode.DUPLICATE_EMAIL);
        }

        // 비밀번호 암호화
        String password = passwordEncoder.encode(request.password());

        // Client 객체 생성 및 저장
        Client client = Client.create("", request.email().toLowerCase(), password);
        clientRepository.save(client);
        client.setName("User#" + client.getId());

        return new ClientDto(client);
    }

    @Transactional
    public ClientDto registerOAuth(OAuth2Attributes attributes) {
        String registrationId = attributes.getRegistrationId();
        Client client;

        // 이미 같은 키로 등록된 사용자가 있다면 해당 사용자 정보 반한
        if (registrationId.equals("google")) {
            client = clientRepository.findByGoogleOAuthId(attributes.getOAuthId()).orElse(null);
        } else if (registrationId.equals("github")) {
            client = clientRepository.findByGithubOAuthId(attributes.getOAuthId()).orElse(null);
        } else {
            throw new RestApiException(ClientErrorCode.UNSUPPORTED_REGISTRATION);
        }

        // 사용자가 없다면 신규 사용자 생성, 기본 이름은 User#id
        if(client == null) {
            client = clientRepository.save(attributes.toClient());
            client.setName("User#" + client.getId());
        }

        return new ClientDto(client);
    }
}
