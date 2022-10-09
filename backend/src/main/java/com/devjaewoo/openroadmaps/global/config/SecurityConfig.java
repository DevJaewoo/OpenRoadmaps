package com.devjaewoo.openroadmaps.global.config;

import com.devjaewoo.openroadmaps.domain.client.Role;
import com.devjaewoo.openroadmaps.global.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/api/**").hasAnyRole(Role.CLIENT.name())
                .anyRequest().authenticated()
                .and().oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);

        return http.build();
    }
}
