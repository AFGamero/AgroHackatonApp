# Feature Specification: Autenticacion y Sesiones

**Version**: 1.0.0  
**Creado**: 27/05/2026  
**Actualizado**: 27/05/2026  
**Estado**: Borrador  
**Autor**: Equipo Nebbi  
**Revisor**: Pendiente

---

## Indice

1. [Contexto y objetivo](#1-contexto-y-objetivo)
2. [Actores](#2-actores)
3. [Entidades y modelo de datos](#3-entidades-y-modelo-de-datos)
4. [User stories y escenarios de aceptacion](#4-user-stories-y-escenarios-de-aceptacion)
5. [Edge cases](#5-edge-cases)
6. [Requerimientos funcionales](#6-requerimientos-funcionales)
7. [Requerimientos no funcionales](#7-requerimientos-no-funcionales)
8. [Criterios de exito](#8-criterios-de-exito)
9. [Contratos API sugeridos](#9-contratos-api-sugeridos)
10. [Fuera de alcance](#10-fuera-de-alcance)

---

## 1. Contexto y objetivo

La autenticacion y gestion de sesiones protege los recursos privados de Nebbi. Esta feature define como un usuario activo obtiene tokens, renueva su sesion, cierra sesion y accede a endpoints protegidos segun su rol.

El objetivo del MVP es implementar un esquema seguro y simple basado en `access_token` JWT de corta duracion y `refresh_token` persistido como hash, con capacidad de revocacion cuando el usuario cierra sesion, cambia de estado o se detecta uso invalido.

Esta spec complementa `02-registro-usuarios.md`: el registro crea la identidad, mientras que autenticacion y sesiones definen el acceso continuo al backend.

---

## 2. Actores

| ID | Actor | Tipo | Descripcion |
| --- | --- | --- | --- |
| ACT-01 | Usuario Activo | Primario | Usuario con correo verificado y estado `ACTIVO`. Puede iniciar sesion y renovar tokens. |
| ACT-02 | Usuario Pendiente | Secundario | Usuario registrado sin correo verificado. No puede iniciar sesion completa. |
| ACT-03 | Usuario Inactivo o Bloqueado | Secundario | Usuario con acceso suspendido. No puede iniciar sesion ni renovar tokens. |
| ACT-04 | Administrador | Primario | Usuario que puede desactivar usuarios y forzar invalidacion de sesiones. |
| ACT-05 | Cliente API | Secundario | Frontend, app movil o consumidor autorizado que envia tokens al backend. |
| ACT-06 | Sistema | Secundario | Servicio que emite, valida, rota y revoca tokens. |

---

## 3. Entidades y modelo de datos

### 3.1 Sesion (`Session`)

Representa una sesion autenticada asociada a un usuario y a un refresh token activo o revocado.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `user_id` | `UUID (FK)` | Usuario propietario de la sesion. | Si |
| `refresh_token_hash` | `String` | Hash del refresh token vigente. Nunca se guarda token plano. | Si |
| `user_agent` | `String` | Maximo 500 caracteres. Informativo. | No |
| `ip_address` | `String` | IPv4 o IPv6. Informativo. | No |
| `estado` | `Enum` | Valores definidos en `SessionStatus`. Default: `ACTIVA`. | Si |
| `expira_en` | `Timestamp` | Fecha de expiracion del refresh token. UTC. | Si |
| `revocada_en` | `Timestamp` | `null` mientras la sesion este activa. UTC. | No |
| `motivo_revocacion` | `String` | Maximo 160 caracteres. Requerido si se revoca manualmente. | No |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. | Si |
| `actualizado_en` | `Timestamp` | Actualizado automaticamente. UTC. | Si |

### 3.2 Token JWT (`AccessToken`)

No se persiste como entidad. Es un token firmado y de corta duracion usado para autorizar requests protegidos.

| Claim | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `sub` | `UUID` | ID del usuario autenticado. | Si |
| `sid` | `UUID` | ID de la sesion activa. | Si |
| `email` | `String` | Correo normalizado del usuario. | Si |
| `role` | `Enum` | Rol actual del usuario. | Si |
| `status` | `Enum` | Estado actual al emitir el token. | Si |
| `iat` | `Unix timestamp` | Emitido automaticamente. | Si |
| `exp` | `Unix timestamp` | Expiracion corta. | Si |

### 3.3 Relaciones

```text
User (1) ---- (N) Session
Session (1) ---- (N) AuditLog
```

### 3.4 Enumeraciones

#### `SessionStatus`

- `ACTIVA`
- `REVOCADA`
- `EXPIRADA`

#### `RevocationReason`

- `LOGOUT`
- `USER_DISABLED`
- `TOKEN_ROTATION`
- `SECURITY_REVOKE`
- `ADMIN_REVOKE`

---

## 4. User stories y escenarios de aceptacion

### US-01 - Iniciar sesion y emitir tokens `P1`

**Como** usuario activo,  
**quiero** iniciar sesion con correo y contrasena,  
**para que** pueda acceder a funcionalidades privadas de Nebbi.

**Por que P1**: Sin login no es posible proteger endpoints ni asociar acciones a usuarios.

**Test independiente**: Un usuario `ACTIVO` con correo verificado envia credenciales validas. El test es exitoso si recibe `access_token`, `refresh_token` y se crea una sesion activa.

#### Escenario 1 - Login exitoso

```gherkin
Given  que existe un usuario ACTIVO con correo verificado
When   envia correo y contrasena correctos
Then   el sistema crea una sesion ACTIVA
And    emite un access_token JWT de corta duracion
And    emite un refresh_token de larga duracion
And    guarda solo el hash del refresh_token
And    registra auditoria de login exitoso
```

#### Escenario 2 - Usuario pendiente de verificacion

```gherkin
Given  que existe un usuario en estado PENDIENTE_VERIFICACION
When   intenta iniciar sesion con credenciales correctas
Then   el sistema rechaza el login
And    no crea sesion
And    indica que debe verificar su correo
```

#### Escenario 3 - Credenciales invalidas

```gherkin
Given  que existe un usuario registrado
When   envia correo o contrasena incorrecta
Then   el sistema rechaza el login
And    no informa cual credencial fallo
And    no crea sesion
```

### US-02 - Validar access token en endpoints protegidos `P1`

**Como** cliente API,  
**quiero** enviar el access token en requests privados,  
**para que** el backend autorice o rechace el acceso segun identidad, estado y rol.

**Por que P1**: Todos los modulos privados dependen de autorizacion consistente.

**Test independiente**: Un request protegido con JWT valido accede al endpoint. Un request sin token o con token invalido recibe `401`.

#### Escenario 1 - Token valido

```gherkin
Given  que existe un access_token firmado y no expirado
And    el usuario asociado esta ACTIVO
And    la sesion asociada esta ACTIVA
When   el cliente llama un endpoint protegido
Then   el backend permite la ejecucion
And    expone el usuario autenticado al controlador o caso de uso
```

#### Escenario 2 - Token expirado

```gherkin
Given  que el access_token esta expirado
When   el cliente llama un endpoint protegido
Then   el backend rechaza la solicitud con 401
And    indica que el token expiro
```

#### Escenario 3 - Usuario desactivado despues de emitir token

```gherkin
Given  que existe un access_token aun no expirado
And    el usuario fue cambiado a INACTIVO
When   el cliente llama un endpoint protegido
Then   el backend rechaza la solicitud
And    no permite operar aunque la firma del token sea valida
```

### US-03 - Renovar access token con refresh token `P1`

**Como** usuario autenticado,  
**quiero** renovar mi access token sin volver a ingresar credenciales,  
**para que** pueda mantener una sesion activa de forma segura.

**Por que P1**: Permite usar access tokens cortos sin degradar la experiencia de uso.

**Test independiente**: Un refresh token valido genera nuevos tokens y rota el refresh token anterior.

#### Escenario 1 - Renovacion exitosa

```gherkin
Given  que existe una sesion ACTIVA con refresh_token vigente
When   el cliente solicita renovacion con ese refresh_token
Then   el sistema valida el hash del token
And    revoca el refresh_token anterior por TOKEN_ROTATION
And    crea o actualiza una sesion con nuevo refresh_token
And    emite un nuevo access_token
And    registra auditoria de renovacion
```

#### Escenario 2 - Refresh token expirado

```gherkin
Given  que el refresh_token esta expirado
When   el cliente intenta renovar la sesion
Then   el sistema rechaza la solicitud
And    marca la sesion como EXPIRADA si aun no lo estaba
And    requiere iniciar sesion nuevamente
```

#### Escenario 3 - Refresh token reutilizado

```gherkin
Given  que un refresh_token ya fue rotado o revocado
When   el cliente intenta usarlo nuevamente
Then   el sistema rechaza la solicitud
And    revoca las sesiones activas relacionadas si aplica politica de seguridad
And    registra evento de posible reutilizacion de token
```

### US-04 - Cerrar sesion `P1`

**Como** usuario autenticado,  
**quiero** cerrar mi sesion actual,  
**para que** el refresh token asociado quede revocado.

**Por que P1**: Es el mecanismo basico para terminar acceso desde el cliente actual.

**Test independiente**: El usuario llama logout con sesion activa. El test es exitoso si el refresh token queda revocado y no puede renovarse.

#### Escenario 1 - Logout exitoso

```gherkin
Given  que existe una sesion ACTIVA
When   el usuario solicita cerrar sesion
Then   el sistema cambia la sesion a REVOCADA
And    registra revocada_en y motivo LOGOUT
And    impide renovar tokens con ese refresh_token
And    registra auditoria de logout
```

#### Escenario 2 - Logout con sesion ya revocada

```gherkin
Given  que la sesion ya esta REVOCADA
When   el usuario solicita cerrar sesion nuevamente
Then   el sistema responde de forma idempotente
And    no reactiva ni modifica tokens
```

### US-05 - Autorizar por rol `P1`

**Como** backend,  
**quiero** validar roles antes de ejecutar casos de uso protegidos,  
**para que** cada actor solo acceda a las operaciones permitidas.

**Por que P1**: Productores, administradores, exportadores y compradores tienen permisos distintos desde el inicio del MVP.

**Test independiente**: Un usuario con rol incorrecto llama un endpoint restringido. El test es exitoso si recibe `403`.

#### Escenario 1 - Rol autorizado

```gherkin
Given  que el usuario autenticado tiene rol PRODUCTOR
When   solicita crear una finca propia
Then   el backend autoriza la accion
```

#### Escenario 2 - Rol no autorizado

```gherkin
Given  que el usuario autenticado tiene rol COMPRADOR_INTERNACIONAL
When   intenta crear una finca
Then   el backend rechaza la solicitud con 403
And    no ejecuta el caso de uso
```

#### Escenario 3 - Administrador accede a recurso administrativo

```gherkin
Given  que el usuario autenticado tiene rol ADMIN
When   solicita cambiar el estado de otro usuario
Then   el backend autoriza la accion
And    registra auditoria administrativa
```

### US-06 - Revocar sesiones por cambio de estado de usuario `P2`

**Como** administrador,  
**quiero** invalidar sesiones cuando un usuario se desactiva o bloquea,  
**para que** no siga usando tokens emitidos previamente.

**Por que P2**: Mejora seguridad operacional y evita acceso residual.

**Test independiente**: Un administrador desactiva un usuario con sesiones activas. El test es exitoso si ninguna sesion puede renovarse ni autorizar endpoints protegidos.

#### Escenario 1 - Desactivar usuario con sesiones activas

```gherkin
Given  que existe un usuario ACTIVO con sesiones ACTIVA
When   el administrador cambia su estado a INACTIVO
Then   el sistema revoca sus sesiones activas
And    registra motivo USER_DISABLED
And    rechaza futuros refresh tokens de ese usuario
```

#### Escenario 2 - Bloquear usuario por seguridad

```gherkin
Given  que existe un usuario ACTIVO
When   el administrador cambia su estado a BLOQUEADO
Then   el sistema revoca todas sus sesiones activas
And    impide login hasta nuevo cambio de estado
```

---

## 5. Edge cases

| # | Caso | Comportamiento esperado |
| --- | --- | --- |
| 1 | Access token valido pero sesion revocada | El backend rechaza la solicitud si valida sesion en endpoints protegidos sensibles. |
| 2 | Usuario cambia de rol despues de emitir JWT | El backend debe consultar rol actual o invalidar sesiones para evitar permisos antiguos. |
| 3 | Refresh token robado y reutilizado | Se rechaza por rotacion y se registra evento de seguridad. |
| 4 | Login simultaneo desde varios dispositivos | Se permite crear varias sesiones activas, salvo que una politica futura limite cantidad. |
| 5 | Logout repetido | Operacion idempotente; no debe fallar por sesion ya revocada. |
| 6 | Usuario desactivado con access token vigente | El backend rechaza por estado actual del usuario. |
| 7 | Reloj del cliente desfasado | La validacion de expiracion se basa en hora del servidor. |
| 8 | Token firmado con clave anterior | Se rechaza salvo que exista estrategia explicita de rotacion de claves. |
| 9 | Refresh token enviado como access token | Se rechaza por formato, firma o tipo de token invalido. |
| 10 | Intentos repetidos de login fallidos | Se registran metricas; bloqueo automatico queda fuera de alcance inicial. |

---

## 6. Requerimientos funcionales

| ID | Descripcion | US relacionada | Prioridad |
| --- | --- | --- | --- |
| FR-001 | El sistema DEBE permitir login con correo y contrasena para usuarios `ACTIVO` y verificados. | US-01 | P1 |
| FR-002 | El sistema DEBE rechazar login de usuarios pendientes, inactivos o bloqueados. | US-01 | P1 |
| FR-003 | El sistema DEBE emitir access token JWT con expiracion corta. | US-01 | P1 |
| FR-004 | El sistema DEBE emitir refresh token y almacenar solo su hash. | US-01 | P1 |
| FR-005 | El sistema DEBE crear una sesion activa por login exitoso. | US-01 | P1 |
| FR-006 | El sistema DEBE validar firma, expiracion, usuario y sesion en endpoints protegidos. | US-02 | P1 |
| FR-007 | El sistema DEBE permitir renovar access token con refresh token vigente. | US-03 | P1 |
| FR-008 | El sistema DEBE rotar refresh tokens en cada renovacion. | US-03 | P1 |
| FR-009 | El sistema DEBE revocar refresh tokens usados en logout. | US-04 | P1 |
| FR-010 | El sistema DEBE soportar autorizacion por rol para endpoints protegidos. | US-05 | P1 |
| FR-011 | El sistema DEBE retornar `401` para autenticacion ausente o invalida. | US-02 | P1 |
| FR-012 | El sistema DEBE retornar `403` para usuarios autenticados sin permisos suficientes. | US-05 | P1 |
| FR-013 | El sistema DEBE revocar sesiones activas cuando un usuario pasa a `INACTIVO` o `BLOQUEADO`. | US-06 | P2 |
| FR-014 | El sistema DEBE registrar auditoria de login, refresh, logout y revocacion administrativa. | Todas | P2 |

---

## 7. Requerimientos no funcionales

### 7.1 Rendimiento

| ID | Descripcion |
| --- | --- |
| RNF-001 | El login debe completarse en menos de 500 ms p95 sin dependencias externas sincronas. |
| RNF-002 | La renovacion de tokens debe completarse en menos de 300 ms p95. |
| RNF-003 | La validacion de access token no debe agregar mas de 50 ms p95 por request protegido. |

### 7.2 Seguridad

| ID | Descripcion |
| --- | --- |
| RNF-004 | Los access tokens deben firmarse con secreto o llave privada gestionada por variables de entorno seguras. |
| RNF-005 | Los access tokens deben tener expiracion corta recomendada entre 5 y 15 minutos. |
| RNF-006 | Los refresh tokens deben tener expiracion configurable recomendada entre 7 y 30 dias. |
| RNF-007 | Los refresh tokens no deben almacenarse en texto plano. |
| RNF-008 | Los logs y auditoria no deben contener tokens completos ni hashes sensibles. |
| RNF-009 | Los errores de login deben ser genericos para no permitir enumeracion de usuarios. |
| RNF-010 | El backend debe comparar hashes de tokens con funcion segura contra timing attacks cuando aplique. |

### 7.3 Consistencia de datos

| ID | Descripcion |
| --- | --- |
| RNF-011 | La creacion de sesion y persistencia de refresh token debe ser atomica. |
| RNF-012 | La rotacion de refresh token debe ser atomica para evitar que dos renovaciones paralelas dejen tokens activos inconsistentes. |
| RNF-013 | La revocacion administrativa de sesiones debe afectar todas las sesiones activas del usuario. |
| RNF-014 | Los endpoints protegidos deben usar una fuente consistente para validar estado actual del usuario. |

### 7.4 Observabilidad

| ID | Descripcion |
| --- | --- |
| RNF-015 | El sistema debe registrar login exitoso, login fallido agregado por metrica, refresh, logout y revocacion. |
| RNF-016 | Los eventos de posible reutilizacion de refresh token deben marcarse como eventos de seguridad. |
| RNF-017 | Las metricas deben permitir medir cantidad de sesiones activas y refresh tokens revocados. |

---

## 8. Criterios de exito

| ID | Criterio | Forma de medicion |
| --- | --- | --- |
| SC-001 | Un usuario activo puede iniciar sesion y recibe access token, refresh token y datos minimos. | Test de integracion de `POST /auth/login`. |
| SC-002 | Un usuario pendiente, inactivo o bloqueado no puede iniciar sesion. | Tests de integracion por estado de usuario. |
| SC-003 | Un access token expirado no autoriza endpoints protegidos. | Test de guard/autorizacion. |
| SC-004 | Un refresh token valido permite renovar tokens y rota el token anterior. | Test de integracion de `POST /auth/refresh`. |
| SC-005 | Un refresh token revocado no puede reutilizarse. | Test de reutilizacion posterior a logout o refresh. |
| SC-006 | Un usuario con rol incorrecto recibe `403` en endpoints restringidos. | Test de autorizacion por rol. |
| SC-007 | Al desactivar un usuario se revocan sus sesiones activas. | Test de integracion administracion + auth. |

---

## 9. Contratos API sugeridos

### 9.1 Login

`POST /auth/login`

#### Request

```json
{
  "correo": "ana@example.com",
  "password": "PasswordSeguro123!"
}
```

#### Response `200`

```json
{
  "access_token": "jwt",
  "refresh_token": "refresh-token",
  "expires_in": 900,
  "user": {
    "id": "uuid",
    "nombre": "Ana Gomez",
    "correo": "ana@example.com",
    "rol": "PRODUCTOR",
    "estado": "ACTIVO"
  }
}
```

### 9.2 Renovar tokens

`POST /auth/refresh`

#### Request

```json
{
  "refresh_token": "refresh-token"
}
```

#### Response `200`

```json
{
  "access_token": "new-jwt",
  "refresh_token": "new-refresh-token",
  "expires_in": 900
}
```

### 9.3 Cerrar sesion actual

`POST /auth/logout`

#### Headers

```http
Authorization: Bearer access-token
```

#### Request

```json
{
  "refresh_token": "refresh-token"
}
```

#### Response `200`

```json
{
  "message": "Sesion cerrada correctamente."
}
```

### 9.4 Consultar usuario autenticado

`GET /auth/me`

#### Headers

```http
Authorization: Bearer access-token
```

#### Response `200`

```json
{
  "id": "uuid",
  "nombre": "Ana Gomez",
  "correo": "ana@example.com",
  "rol": "PRODUCTOR",
  "estado": "ACTIVO"
}
```

### 9.5 Revocar sesiones de usuario

`POST /admin/users/{userId}/sessions/revoke`

#### Headers

```http
Authorization: Bearer admin-access-token
```

#### Request

```json
{
  "motivo": "USER_DISABLED"
}
```

#### Response `200`

```json
{
  "user_id": "uuid",
  "revoked_sessions": 3
}
```

---

## 10. Fuera de alcance

Los siguientes puntos quedan excluidos de esta feature inicial y deben especificarse por separado:

- Recuperacion de contrasena.
- Autenticacion multifactor.
- Autenticacion con Google, Microsoft u otros proveedores externos.
- Gestion visual de sesiones por dispositivo.
- Limite configurable de sesiones simultaneas por usuario.
- Rotacion automatica de llaves JWT con `kid`.
- Bloqueo automatico por fuerza bruta.
- Integracion con proveedores externos de identidad.

---

## Historial de cambios

| Version | Fecha | Autor | Descripcion |
| --- | --- | --- | --- |
| 1.0.0 | 27/05/2026 | Equipo Nebbi | Version inicial de la especificacion de autenticacion y sesiones. |
