package com.devjaewoo.openroadmaps.domain.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
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
            String email = "client@gmail.com";
            String password = "password";
            Client client = Client.create("client", email, password);
            clientRepository.save(client);

            //when
            Client result = clientRepository.findByEmailAndPassword(email, password).orElse(null);
            Client result1 = clientRepository.findByEmailAndPassword(email, "").orElse(null);
            Client result2 = clientRepository.findByEmailAndPassword("", password).orElse(null);
            Client result3 = clientRepository.findByEmailAndPassword("", "").orElse(null);

            //then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isGreaterThan(0);
            assertThat(Arrays.asList(result1, result2, result3)).containsOnlyNulls();
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