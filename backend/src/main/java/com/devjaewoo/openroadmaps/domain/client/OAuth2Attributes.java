package com.devjaewoo.openroadmaps.domain.client;

import lombok.*;

import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuth2Attributes {
    private String registrationId;
    private String name;
    private String email;
    private String picture;
    private String oAuthId;
    private Map<String, Object> attributes;

    public static OAuth2Attributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if(Objects.equals(registrationId, "google")) {
            return ofGoogle(registrationId, userNameAttributeName, attributes);
        }
        else if(Objects.equals(registrationId, "github")) {
            return ofGithub(registrationId, userNameAttributeName, attributes);
        }
        else {
            throw new IllegalArgumentException("지원하지 않는 Registration입니다.");
        }
    }

    public static OAuth2Attributes ofGoogle(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .registrationId(registrationId)
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .oAuthId((String) attributes.get(userNameAttributeName))
                .attributes(attributes)
                .build();
    }

    public static OAuth2Attributes ofGithub(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .registrationId(registrationId)
                .name((String) attributes.get("login"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("avatar_url"))
                .oAuthId(attributes.get(userNameAttributeName).toString())
                .attributes(attributes)
                .build();
    }

    public Client toClient() {
        String googleOAuthId = registrationId.equals("google") ? oAuthId : null;
        String githubOAuthId = registrationId.equals("github") ? oAuthId : null;
        return Client.createOAuth(name, email, picture, googleOAuthId, githubOAuthId);
    }
}
