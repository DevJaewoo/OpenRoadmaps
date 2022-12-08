package com.devjaewoo.openroadmaps.domain.client.service;

import com.devjaewoo.openroadmaps.domain.client.repository.ClientRepository;
import com.devjaewoo.openroadmaps.domain.client.dto.ClientDto;
import com.devjaewoo.openroadmaps.domain.client.dto.ClientErrorCode;
import com.devjaewoo.openroadmaps.domain.client.dto.OAuth2Attributes;
import com.devjaewoo.openroadmaps.domain.client.dto.SessionClient;
import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import com.devjaewoo.openroadmaps.global.config.SessionConfig;
import com.devjaewoo.openroadmaps.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Set;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClientService {

    private final HttpSession httpSession;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ClientDto register(ClientDto.Register request) {

        // 강제 로그아웃
        logout();

        // 이메일 중복 여부 체크
        if(clientRepository.existsByEmail(request.email().toLowerCase())) {
            throw new RestApiException(ClientErrorCode.DUPLICATE_EMAIL);
        }

        // 비밀번호 암호화
        String password = passwordEncoder.encode(request.password());

        // Client 객체 생성 및 저장
        Client client = Client.create("", request.email().toLowerCase(), password);
        clientRepository.save(client);
        client.setName("user" + client.getId());

        // SessionAttribute에 Client 정보 저장
        if(httpSession != null) {
            httpSession.setAttribute(SessionConfig.CLIENT_INFO, new SessionClient(client));
        }

        // Client 권한 부여
        grantAuthority(client);

        return ClientDto.from(client);
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

        // 사용자가 없다면 신규 사용자 생성, 기본 이름은 user#id
        if(client == null) {
            client = clientRepository.save(attributes.toClient());
            client.setName("user" + client.getId());
        }

        // SessionAttribute에 Client 정보 저장
        if(httpSession != null) {
            httpSession.setAttribute(SessionConfig.CLIENT_INFO, new SessionClient(client));
        }

        return ClientDto.from(client);
    }

    public ClientDto login(ClientDto.Register request) {

        // 강제 로그아웃
        logout();

        Client client = clientRepository.findByEmailAndPasswordIsNotNull(request.email().toLowerCase())
                .orElseThrow(() -> new RestApiException(ClientErrorCode.INCORRECT_EMAIL));

        if(!client.isEnabled()) {
            throw new RestApiException(ClientErrorCode.DISABLED_CLIENT);
        }

        if(!passwordEncoder.matches(request.password(), client.getPassword())) {
            throw new RestApiException(ClientErrorCode.INCORRECT_PASSWORD);
        }

        // SessionAttribute에 Client 정보 저장
        if(httpSession != null) {
            httpSession.setAttribute(SessionConfig.CLIENT_INFO, new SessionClient(client));
        }

        // Client 권한 부여
        grantAuthority(client);

        return ClientDto.from(client);
    }

    public void logout() {
        if(httpSession != null) {
            httpSession.invalidate();
            SecurityContextHolder.clearContext();
        }
    }

    private void grantAuthority(Client client) {
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(client.getRole().key));
        User principal = new User(client.getName(), client.getPassword(), authorities);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    public ClientDto findClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ClientErrorCode.CLIENT_NOT_FOUND));

        return ClientDto.from(client);
    }
}
