package com.devjaewoo.openroadmaps.domain.client.repository;

import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByEmail(String email);
    boolean existsByName(String name);
    Optional<Client> findByName(String name);
    Optional<Client> findByEmailAndPasswordIsNotNull(String email);
    Optional<Client> findByGoogleOAuthId(String googleOAuthId);
    Optional<Client> findByGithubOAuthId(String githubOAuthId);
}
