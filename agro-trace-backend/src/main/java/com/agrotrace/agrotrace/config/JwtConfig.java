package com.agrotrace.agrotrace.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.jwt")
@Getter
@Setter
public class JwtConfig {
    private String secret = "change-me-in-production";
    private long expiration = 900000L;
    private long refreshExpiration = 604800000L;
}
