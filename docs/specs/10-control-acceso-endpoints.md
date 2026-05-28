# Feature Specification: Control de Acceso a Endpoints (Autorización)

**Version**: 1.0.0  
**Creado**: 28/05/2026  
**Actualizado**: 28/05/2026  
**Estado**: Borrador  
**Autor**: Equipo AgroTrace  

---

## Indice

1. [Contexto y objetivo](#1-contexto-y-objetivo)
2. [Actores y roles](#2-actores-y-roles)
3. [Matriz de acceso por endpoint](#3-matriz-de-acceso-por-endpoint)
4. [Endpoints públicos](#4-endpoints-públicos)
5. [Endpoints por rol](#5-endpoints-por-rol)
6. [Endpoints técnicos](#6-endpoints-técnicos)
7. [Reglas de negocio](#7-reglas-de-negocio)

---

## 1. Contexto y objetivo

La plataforma AgroTrace Magdalena expone endpoints REST que deben diferenciarse según requieran o no autenticación. Esta spec define qué endpoints son públicos (accesibles sin token JWT) y cuáles requieren login y rol específico.

Las reglas aquí definidas se implementan en Spring Security mediante un `SecurityFilterChain`.

---

## 2. Actores y roles

| Rol | Descripción |
|-----|-------------|
| `PUBLICO` | Sin autenticación. Turista, comprador anónimo, cualquier persona con QR. |
| `PRODUCTOR` | Productor agrícola autenticado. Gestiona fincas, lotes, evidencias. |
| `ADMIN` | Administrador de la plataforma. Audita, gestiona usuarios. |
| `OPERADOR_TURISTICO` | Gestiona experiencias turísticas. |
| `CERTIFICADOR` | Entidad certificadora. Registra certificaciones. |
| `COMPRADOR_INTERNACIONAL` | Comprador registrado. Envía solicitudes de compra. |
| `EXPORTADOR` | Exportador registrado. |

---

## 3. Matriz de acceso por endpoint

| Método | Endpoint | Público | Productor | Admin | Op Turístico | Certificador | Comprador |
|--------|----------|:-------:|:---------:|:-----:|:------------:|:------------:|:---------:|
| **Autenticación** |
| `POST` | `/auth/register` | ✅ | - | - | - | - | - |
| `POST` | `/auth/login` | ✅ | - | - | - | - | - |
| `POST` | `/auth/refresh` | - | ✅ | ✅ | ✅ | ✅ | ✅ |
| `POST` | `/auth/logout` | - | ✅ | ✅ | ✅ | ✅ | ✅ |
| **Usuarios** |
| `GET` | `/users/{id}` | - | ✅* | ✅ | ✅* | ✅* | ✅* |
| `PATCH` | `/users/{id}` | - | ✅* | ✅ | ✅* | ✅* | ✅* |
| **Productores** |
| `POST` | `/producers` | - | ✅ | ✅ | - | - | - |
| `GET` | `/producers/{id}` | - | ✅* | ✅ | - | ✅ | ✅ |
| `PATCH` | `/producers/{id}` | - | ✅* | ✅ | - | - | - |
| **Fincas** |
| `POST` | `/farms` | - | ✅ | - | - | - | - |
| `GET` | `/farms` | - | ✅* | ✅ | - | - | - |
| `GET` | `/farms/{id}` | - | ✅* | ✅ | ✅ | ✅ | - |
| `PATCH` | `/farms/{id}` | - | ✅* | - | - | - | - |
| `POST` | `/farms/{id}/photos` | - | ✅* | - | - | - | - |
| **Lotes** |
| `POST` | `/farms/{id}/lots` | - | ✅* | - | - | - | - |
| `GET` | `/farms/{id}/lots` | - | ✅* | ✅ | - | - | - |
| `GET` | `/lots/{id}` | - | ✅* | ✅ | ✅ | ✅ | - |
| `PATCH` | `/lots/{id}` | - | ✅* | - | - | - | - |
| **Estados de Certificación** |
| `POST` | `/lots/{id}/certification-status` | - | ✅* | - | - | ✅ | - |
| `GET` | `/lots/{id}/certification-status` | - | ✅* | ✅ | - | ✅ | - |
| **Evidencias** |
| `POST` | `/lots/{id}/evidence` | - | ✅* | - | - | - | - |
| `GET` | `/lots/{id}/evidence` | - | ✅* | ✅ | ✅ | ✅ | - |
| **Certificaciones** |
| `POST` | `/certifications` | - | - | - | - | ✅ | - |
| `GET` | `/certifications` | - | ✅* | ✅ | - | ✅ | - |
| `GET` | `/certifications/{id}` | - | ✅* | ✅ | - | ✅ | - |
| `PATCH` | `/certifications/{id}` | - | - | - | - | ✅ | - |
| **Pasaporte Digital y QR** |
| `GET` | `/passports/{id}` | - | ✅* | ✅ | - | - | - |
| `GET` | `/public/passports/{id}` | ✅ | - | - | - | - | - |
| `POST` | `/passports` | - | ✅* | - | - | - | - |
| `PATCH` | `/passports/{id}/publish` | - | ✅* | - | - | - | - |
| `GET` | `/qr/{id}` | - | ✅* | ✅ | - | - | - |
| **Trazabilidad** |
| `GET` | `/lots/{id}/traceability` | - | ✅* | ✅ | - | ✅ | - |
| `GET` | `/public/lots/{id}/traceability` | ✅ | - | - | - | - | - |
| **Turismo** |
| `POST` | `/tourism/experiences` | - | - | - | ✅ | - | - |
| `GET` | `/tourism/experiences` | - | ✅* | ✅ | ✅ | - | - |
| `GET` | `/public/experiences` | ✅ | - | - | - | - | - |
| `GET` | `/tourism/experiences/{id}` | - | ✅* | ✅ | ✅ | - | - |
| `PATCH` | `/tourism/experiences/{id}` | - | - | - | ✅* | - | - |
| **Solicitudes de Compra** |
| `POST` | `/public/purchase-requests` | ✅ | - | - | - | - | - |
| `GET` | `/purchase-requests` | - | ✅* | ✅ | - | - | ✅* |
| `GET` | `/purchase-requests/{id}` | - | ✅* | ✅ | - | - | ✅* |
| `PATCH` | `/purchase-requests/{id}/status` | - | ✅* | - | - | - | - |
| **Admin** |
| `GET` | `/admin/users` | - | - | ✅ | - | - | - |
| `PATCH` | `/admin/users/{id}/status` | - | - | ✅ | - | - | - |
| `GET` | `/admin/audit-logs` | - | - | ✅ | - | - | - |

> ✅* = Solo sobre recursos propios. Un productor no puede modificar lotes de otro productor.

---

## 4. Endpoints Públicos

Estos endpoints no requieren token JWT. Son accedidos por turistas, compradores anónimos y cualquier persona con un código QR.

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/auth/register` | Registro de nuevo usuario |
| `POST` | `/auth/login` | Inicio de sesión, retorna tokens |
| `GET` | `/public/passports/{id}` | Pasaporte digital público (QR) |
| `GET` | `/public/lots/{id}/traceability` | Trazabilidad pública del lote |
| `GET` | `/public/experiences` | Catálogo público de experiencias turísticas |
| `POST` | `/public/purchase-requests` | Solicitud de compra desde el pasaporte público |

---

## 5. Endpoints por Rol

### 5.1 Productor

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/producers` | Registrar perfil de productor |
| `POST` | `/farms` | Crear finca |
| `GET` | `/farms` | Listar mis fincas |
| `POST` | `/farms/{id}/photos` | Agregar foto a finca |
| `POST` | `/farms/{id}/lots` | Crear lote en finca |
| `GET` | `/farms/{id}/lots` | Listar lotes de finca |
| `POST` | `/lots/{id}/certification-status` | Registrar estado de certificación |
| `POST` | `/lots/{id}/evidence` | Adjuntar evidencia |
| `POST` | `/passports` | Generar pasaporte digital |
| `PATCH` | `/passports/{id}/publish` | Publicar pasaporte |
| `GET` | `/lots/{id}/traceability` | Consultar trazabilidad privada |
| `GET` | `/purchase-requests` | Ver solicitudes de compra recibidas |

### 5.2 Administrador

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/admin/users` | Listar todos los usuarios |
| `PATCH` | `/admin/users/{id}/status` | Cambiar estado de usuario |
| `GET` | `/admin/audit-logs` | Consultar auditoría |

### 5.3 Operador Turístico

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/tourism/experiences` | Crear experiencia turística |
| `PATCH` | `/tourism/experiences/{id}` | Editar experiencia turística |

### 5.4 Certificador

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/certifications` | Registrar certificación formal |
| `PATCH` | `/certifications/{id}` | Actualizar certificación |
| `POST` | `/lots/{id}/certification-status` | Registrar estado de certificación |

### 5.5 Comprador Internacional

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/purchase-requests` | Ver mis solicitudes de compra |

---

## 6. Endpoints Técnicos

Endpoints de infraestructura que no requieren autenticación:

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/swagger-ui/**` | Swagger UI |
| `GET` | `/v3/api-docs/**` | OpenAPI docs |
| `GET` | `/h2-console/**` | H2 Console (solo dev) |
| `GET` | `/actuator/health` | Health check |

---

## 7. Reglas de Negocio

| ID | Regla |
|----|-------|
| R-01 | Todo endpoint no listado como público requiere token JWT válido. |
| R-02 | Token expirado → renovar con refresh token en `/auth/refresh`. |
| R-03 | Productor solo accede a recursos de sus propias fincas y lotes. |
| R-04 | Operador turístico solo edita sus propias experiencias. |
| R-05 | Comprador solo ve sus propias solicitudes de compra. |
| R-06 | La trazabilidad pública no expone datos de usuarios ni evidencias ocultas. |
| R-07 | Las solicitudes de compra públicas no requieren login, se asocian por email. |
| R-08 | Swagger UI y H2 Console solo disponibles en perfil `dev` o `h2`. |

---

## Historial de cambios

| Version | Fecha | Autor | Descripcion |
| --- | --- | --- | --- |
| 1.0.0 | 28/05/2026 | Equipo AgroTrace | Version inicial. Matriz completa de endpoints públicos y privados. |
