package com.livanov.continuousarchitecture;

import com.livanov.continuousarchitecture.persistence.DummyDataGenerator;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class ContinuousArchitectureApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContinuousArchitectureApplication.class, args);
    }

    @Bean
    ApplicationRunner runner(DummyDataGenerator generator) {
        return args -> generator.generate();
    }

    @Configuration
    @EnableWebSecurity
    public static class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .build();
        }
    }
}
