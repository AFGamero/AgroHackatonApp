# Feature Specification: Spring Security Implementation

**Version**: 1.1.0  
**Actualizado**: 28/05/2026  
**Estado**: Pendiente (MVP: BCrypt + JWT implementados, SecurityFilterChain no activado)  
**Depende de**: 03-autenticacion-sesiones.md, 10-control-acceso-endpoints.md

---

## 1. Contexto

En el MVP la logica de autenticacion esta completa:
- **BCrypt**: Contraseñas hasheadas al registrar y verificadas al login. Bean `PasswordEncoder` en `PasswordEncoderConfig.java`. ✅
- **JWT**: Tokens generados y validados. `JwtTokenProvider.java` implementado. ✅
- **Sesiones**: `Session` entity + `SessionRepository` para refresh tokens. ✅

Lo que falta: **Spring Security Filter Chain** para proteger endpoints por rol.

---

## 2. Dependencia requerida

Agregar en `build.gradle.kts`:

```kotlin
implementation("org.springframework.boot:spring-boot-starter-security")
```

---

## 3. SecurityConfig (SecurityFilterChain)

Archivo: `config/SecurityConfig.java`

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### Reglas de acceso:

| Ruta | Acceso |
|------|--------|
| `/auth/**` | Público |
| `/public/**` | Público |
| `/swagger-ui/**`, `/v3/api-docs/**` | Público |
| `/h2-console/**` | Público (solo dev) |
| `/actuator/health` | Público |
| `/admin/**` | `ROLE_ADMIN` |
| Todo lo demas | Autenticado |

---

## 4. JwtAuthenticationFilter

Archivo ya existe en `config/JwtAuthenticationFilter.java` (creado pero no activo). Al usar Spring Security, extiende `OncePerRequestFilter`:

```java
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        String token = extractToken(request);
        if (StringUtils.hasText(token) && !jwtTokenProvider.isTokenExpired(token)) {
            Claims claims = jwtTokenProvider.validateToken(token);
            if (!"refresh".equals(claims.get("type", String.class))) {
                UUID userId = UUID.fromString(claims.getSubject());
                String role = claims.get("role", String.class);
                var auth = new UsernamePasswordAuthenticationToken(
                    userId, null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
```

---

## 5. PasswordEncoder (BCrypt)

Ya implementado en MVP:

```java
// PasswordEncoderConfig.java
@Configuration
public class PasswordEncoderConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

```java
// AuthService.java - ya usa BCrypt
user.setPasswordHash(passwordEncoder.encode(dto.password()));
passwordEncoder.matches(dto.password(), user.getPasswordHash());
```

**Status: ✅ COMPLETADO**

---

## 6. AuditorAware integrado

Cuando Spring Security este activo, `AuditConfig.java` debe extraer el `userId` del `SecurityContext`:

```java
@Bean
public AuditorAware<UUID> auditorProvider() {
    return () -> {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return Optional.of((UUID) auth.getPrincipal());
    };
}
```

---

## 7. Plan de activacion

| Paso | Archivo | Accion |
|------|---------|--------|
| 1 | `build.gradle.kts` | Agregar `spring-boot-starter-security` |
| 2 | `AuthService.java` | Usar `PasswordEncoder` en login y register |
| 3 | `SecurityConfig.java` | Crear con reglas de acceso |
| 4 | `AuditConfig.java` | Extraer userId del SecurityContext |
| 5 | `application.yml` | Agregar `jwt.secret` en Base64 |
| 6 | Probar | Login, refresh, acceso a endpoints protegidos |

---

## Historial de cambios

| Version | Fecha | Autor | Descripcion |
| --- | --- | --- | --- |
| 1.0.0 | 28/05/2026 | Equipo AgroTrace | Spec inicial con configuracion pendiente de Spring Security. |
