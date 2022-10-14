package com.devjaewoo.openroadmaps.domain.client;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_email", columnNames = {"email"}),
        @UniqueConstraint(name = "unique_google", columnNames = {"google_oauth_id"}),
        @UniqueConstraint(name = "unique_github", columnNames = {"github_oauth_id"}),
})
@ToString
public class Client {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    private String name;

    private String email;
    private String password;
    private boolean isEmailVerified;

    @Column(name = "google_oauth_id")
    private String googleOAuthId;
    @Column(name = "github_oauth_id")
    private String githubOAuthId;

    private String picture;
    private int reputation;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isEnabled;

    public static Client create(String name, String email, String password) {
        Client client = new Client();
        client.name = name;
        client.email = email;
        client.password = password;
        client.role = Role.CLIENT;

        return client;
    }
    public static Client createOAuth(String name, String email, String picture, String googleOAuthId, String githubOAuthId) {
        Client client = new Client();
        client.name = name;
        client.email = email;
        client.picture = picture;
        client.googleOAuthId = googleOAuthId;
        client.githubOAuthId = githubOAuthId;
        client.role = Role.CLIENT;

        return client;
    }
}
