package com.devjaewoo.openroadmaps.domain.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class ClientRepositoryTest {

    @Autowired private ClientRepository clientRepository;

    @Nested
    @DisplayName("Client 조회")
    class TestSelect {

        @Test
        @DisplayName("ID")
        public void TestId() {
            //given
            Client client = Client.create("client", "email", "password");
            clientRepository.save(client);

            //when
            Client result = clientRepository.findById(client.getId()).orElse(null);

            //then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(client.getId());
        }

        @Test
        @DisplayName("Email/Password")
        public void TestEmailPassword() {
            //given
            String email1 = "client1@gmail.com";
            Client client1 = Client.create("client", email1, "password");

            String email2 = "client2@gmail.com";
            Client client2 = Client.create("client", email2, null);

            clientRepository.save(client1);
            clientRepository.save(client2);

            //when
            Client result1 = clientRepository.findByEmailAndPasswordIsNotNull(email1).orElse(null);
            Client result2 = clientRepository.findByEmailAndPasswordIsNotNull(email2).orElse(null);
            Client result3 = clientRepository.findByEmailAndPasswordIsNotNull("").orElse(null);

            //then
            assertThat(result1).isNotNull();
            assertThat(result1.getId()).isGreaterThan(0);
            assertThat(result2).isNull();
            assertThat(result3).isNull();
        }

        @Test
        @DisplayName("OAuthID")
        public void TestOAuthClient() {
            //given
            String googleId = "googleId";
            String githubId = "githubId";
            Client client = Client.createOAuth("oauth", "email", "picture", googleId, githubId);
            clientRepository.save(client);

            //when
            Client result1 = clientRepository.findByGoogleOAuthId(googleId).orElse(null);
            Client result2 = clientRepository.findByGithubOAuthId(githubId).orElse(null);

            //then
            assertThat(result1).isNotNull();
            assertThat(result1.getGoogleOAuthId()).isEqualTo(googleId);
            assertThat(result2).isNotNull();
            assertThat(result2.getGithubOAuthId()).isEqualTo(githubId);
        }
    }
}