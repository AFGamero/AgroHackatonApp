package com.agrotrace.agrotrace.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class JwtConfig {

    @Value("${app.jwt.secret:YWdyb3RyYWNlbWFnZGFsZW5hMjAyNnNlY3JldGtleTEyMw==}")
    private String secret;

    @Value("${app.jwt.expiration:900000}")
    private long expiration;

    @Value("${app.jwt.refresh-expiration:604800000}")
    private long refreshExpiration;
}
