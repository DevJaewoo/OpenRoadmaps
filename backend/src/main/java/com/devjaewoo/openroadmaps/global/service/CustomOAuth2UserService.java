package com.devjaewoo.openroadmaps.global.service;

import com.devjaewoo.openroadmaps.domain.client.Client;
import com.devjaewoo.openroadmaps.domain.client.ClientRepository;
import com.devjaewoo.openroadmaps.domain.client.OAuth2Attributes;
import com.devjaewoo.openroadmaps.domain.client.SessionClient;
import com.devjaewoo.openroadmaps.global.config.SessionConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final ClientRepository clientRepository;
    private final HttpSession httpSession;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Long clientId = Optional.ofNullable((SessionClient) httpSession.getAttribute(SessionConfig.CLIENT_INFO))
                .map(SessionClient::getId)
                .orElse(null);

        log.info("Save OAuth2 user {}", oAuth2User.getAttributes());
        log.info("Current client ID: {}", clientId);

        Client client = saveOrUpdate(attributes, clientId);
        httpSession.setAttribute(SessionConfig.CLIENT_INFO, new SessionClient(client));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(client.getRole().key)), attributes.getAttributes(), userNameAttributeName);
    }

    private Client saveOrUpdate(OAuth2Attributes attributes, Long clientId) {
        String registrationId = attributes.getRegistrationId();
        Client client = null;

        if(clientId != null) {
            client = clientRepository.findById(clientId).orElse(null);
        }

        if(client != null) {
            // 현재 로그인된 사용자가 존재하면 해당 사용자의 OAuth 키만 업데이트
            if (registrationId.equals("google")) {
                client.setGoogleOAuthId(attributes.getOAuthId());
            } else if (registrationId.equals("github")) {
                client.setGithubOAuthId(attributes.getOAuthId());
            } else {
                throw new IllegalArgumentException("지원하지 않는 Registration입니다.");
            }
        }
        else {
            // 이미 같은 키로 등록된 사용자가 있다면 해당 사용자 정보 반한, 없을 시 신규 등록
            if (registrationId.equals("google")) {
                client = clientRepository.findByGoogleOAuthId(attributes.getOAuthId()).orElse(attributes.toClient());
            } else if (registrationId.equals("github")) {
                client = clientRepository.findByGithubOAuthId(attributes.getOAuthId()).orElse(attributes.toClient());
            } else {
                throw new IllegalArgumentException("지원하지 않는 Registration입니다.");
            }
        }

        return clientRepository.save(client);
    }
}
