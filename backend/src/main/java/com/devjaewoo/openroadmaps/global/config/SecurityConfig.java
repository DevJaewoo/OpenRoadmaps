package com.devjaewoo.openroadmaps.global.config;

import com.devjaewoo.openroadmaps.domain.client.Role;
import com.devjaewoo.openroadmaps.global.handler.ForbiddenHandler;
import com.devjaewoo.openroadmaps.global.handler.OAuthSuccessHandler;
import com.devjaewoo.openroadmaps.global.handler.UnauthorizedHandler;
import com.devjaewoo.openroadmaps.global.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UnauthorizedHandler unauthorizedHandler;
    private final ForbiddenHandler forbiddenHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuthSuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .accessDeniedHandler(forbiddenHandler).and()
                .authorizeRequests()
                    .antMatchers("/login/**").permitAll()
                    .antMatchers("/oauth/**").permitAll()
                    .antMatchers("/api/*/client/register").permitAll()
                    .antMatchers("/api/*/client/login").permitAll()
                    .antMatchers("/api/**").hasAnyRole(Role.CLIENT.name())
                    .antMatchers("/admin/**").hasAnyRole(Role.ADMIN.name())
                    .anyRequest().authenticated().and()
                .oauth2Login()
                    .authorizationEndpoint().baseUri("/oauth/authorization").and()
                    .redirectionEndpoint().baseUri("/login/oauth2/code/**").and()
                    .userInfoEndpoint().userService(customOAuth2UserService).and()
                    .successHandler(oAuth2SuccessHandler);

        return http.build();
    }
}
