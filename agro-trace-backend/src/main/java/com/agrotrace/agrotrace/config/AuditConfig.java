package com.agrotrace.agrotrace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class AuditConfig {

    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return new AuditorAware<UUID>() {
            @Override
            public Optional<UUID> getCurrentAuditor() {
                return Optional.of(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            }
        };
    }
}
