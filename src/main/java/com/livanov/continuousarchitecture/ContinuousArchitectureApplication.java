package com.livanov.continuousarchitecture;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDate;

@SpringBootApplication
public class ContinuousArchitectureApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContinuousArchitectureApplication.class, args);
    }

    @Bean
    ApplicationRunner runner(PeopleRepository peopleRepository) {
        return args -> {
            peopleRepository.save(new Person("Lyubo", LocalDate.of(1919, 2, 26)));
            peopleRepository.save(new Person("John", LocalDate.of(2020, 4, 22)));
            peopleRepository.save(new Person("Steve", LocalDate.of(2001, 12, 7)));
        };
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
