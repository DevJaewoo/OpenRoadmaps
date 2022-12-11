package com.devjaewoo.openroadmaps.global.config;

import com.devjaewoo.openroadmaps.domain.blog.dto.PostOrder;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.ConnectionType;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.Recommend;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapOrder;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringConfig implements WebMvcConfigurer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new RoadmapOrder.RoadmapOrderConverter());
        registry.addConverter(new PostOrder.PostOrderConverter());
        registry.addConverter(new Accessibility.AccessibilityConverter());
        registry.addConverter(new ConnectionType.ConnectionTypeConverter());
        registry.addConverter(new Recommend.RecommendConverter());
    }
}
