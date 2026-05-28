# Feature Specification: Spring Security Implementation

**Version**: 1.0.0  
**Creado**: 28/05/2026  
**Estado**: Pendiente (Post-MVP)  
**Depende de**: 03-autenticacion-sesiones.md, 10-control-acceso-endpoints.md  

---

## Indice

1. [Contexto](#1-contexto)
2. [Dependencia requerida](#2-dependencia-requerida)
3. [SecurityConfig (SecurityFilterChain)](#3-securityconfig)
4. [JwtAuthenticationFilter](#4-jwtauthenticationfilter)
5. [PasswordEncoder (BCrypt)](#5-passwordencoder)
6. [AuditorAware integrado](#6-auditoraware-integrado)
7. [Plan de activacion](#7-plan-de-activacion)

---

## 1. Contexto

Actualmente la logica de autenticacion (login, register, refresh, logout) esta implementada en `AuthService` y `AuthController`. Sin embargo, **Spring Security NO esta activado** (no se uso `spring-boot-starter-security`). Esto significa que:

- Los tokens JWT se generan y validan, pero los endpoints **no estan protegidos**.
- No hay filtro que verifique el token en cada request.
- Las contraseñas se comparan en texto plano (sin BCrypt).

Esta spec documenta lo que falta para activar Spring Security en un futuro sprint.

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

Al activar Spring Security:

1. **Registro**: Encriptar con `passwordEncoder.encode(password)`.
2. **Login**: Validar con `passwordEncoder.matches(raw, hashed)`.
3. **Bean** en `SecurityConfig`:
   ```java
   @Bean
   public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }
   ```

### Cambios en AuthService:

```java
// Antes (texto plano)
user.setPasswordHash(dto.password());

// Despues (BCrypt)
user.setPasswordHash(passwordEncoder.encode(dto.password()));
```

```java
// Antes
if (!dto.password().equals(user.getPasswordHash())) { ... }

// Despues
if (!passwordEncoder.matches(dto.password(), user.getPasswordHash())) { ... }
```

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
