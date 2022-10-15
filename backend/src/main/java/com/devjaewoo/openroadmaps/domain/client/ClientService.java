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
        if(clientRepository.existsByEmail(request.email())) {
            throw new RestApiException(ClientErrorCode.DUPLICATE_EMAIL);
        }

        // 비밀번호 암호화
        String password = passwordEncoder.encode(request.password());

        // Client 객체 생성 및 저장
        Client client = Client.create(request.email(), request.email(), password);
        clientRepository.save(client);

        return new ClientDto(client);
    }
}
