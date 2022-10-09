package com.devjaewoo.openroadmaps.domain.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmailAndPassword(String email, String password);
    Optional<Client> findByGoogleOAuthId(String googleOAuthId);
    Optional<Client> findByGithubOAuthId(String githubOAuthId);
}
