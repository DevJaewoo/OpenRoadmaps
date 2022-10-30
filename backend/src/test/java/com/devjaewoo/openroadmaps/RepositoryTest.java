package com.devjaewoo.openroadmaps;

import com.devjaewoo.openroadmaps.global.config.JpaRepositoryConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@DataJpaTest
@Import(JpaRepositoryConfig.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepositoryTest {
}
