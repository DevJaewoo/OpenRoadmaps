package com.devjaewoo.openroadmaps.global.config;

import com.devjaewoo.openroadmaps.domain.roadmap.dto.ConnectionType;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.Recommend;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapSearch;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.persistence.EntityManager;

@Configuration
public class SpringConfig implements WebMvcConfigurer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new RoadmapSearch.OrderRequestConverter());
        registry.addConverter(new ConnectionType.ConnectionTypeConverter());
        registry.addConverter(new Accessibility.AccessibiltyConverter());
        registry.addConverter(new Recommend.RecommendConverter());
    }
}
