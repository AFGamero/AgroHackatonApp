# Feature Specification: Registro de Usuarios

**Version**: 1.0.0  
**Creado**: 27/05/2026  
**Actualizado**: 27/05/2026  
**Estado**: Borrador  
**Autor**: Equipo AgroTrace  
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

El registro de usuarios es la base de acceso para AgroTrace Magdalena. Sin usuarios autenticados no es posible administrar productores, fincas, lotes, certificaciones, experiencias turisticas ni solicitudes comerciales con trazabilidad de autoria.

Esta feature habilita el registro inicial de usuarios, la verificacion de correo electronico, el inicio de sesion y la creacion del perfil operativo segun el tipo de actor dentro del sistema.

El objetivo del MVP es permitir que un usuario cree una cuenta, confirme su correo, inicie sesion y quede asociado a un rol funcional inicial para operar dentro de la plataforma.

---

## 2. Actores

| ID | Actor | Tipo | Descripcion |
| --- | --- | --- | --- |
| ACT-01 | Visitante | Primario | Persona no autenticada que inicia el registro. |
| ACT-02 | Productor | Primario | Usuario que registra fincas, lotes, estados de cultivo y evidencias. |
| ACT-03 | Operador Turistico | Primario | Usuario que publica experiencias turisticas asociadas a fincas. |
| ACT-04 | Exportador | Primario | Usuario que gestiona solicitudes comerciales y procesos de exportacion. |
| ACT-05 | Comprador Internacional | Primario | Usuario o empresa interesada en consultar trazabilidad y solicitar compras. |
| ACT-06 | Certificador | Secundario | Usuario que valida certificaciones y revisa evidencias. |
| ACT-07 | Administrador | Primario | Usuario con permisos para gestionar usuarios, estados y roles. |
| ACT-08 | Sistema | Secundario | Servicio que genera tokens, valida credenciales y registra auditoria. |

> Un usuario sin correo verificado no debe acceder a funcionalidades protegidas del sistema, salvo endpoints necesarios para reenviar verificacion o completar el proceso de activacion.

---

## 3. Entidades y modelo de datos

### 3.1 Usuario (`User`)

Representa la identidad autenticable dentro del sistema.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente por el sistema. Inmutable. | Si |
| `nombre` | `String` | Minimo 2 caracteres, maximo 120. No puede ser solo espacios. | Si |
| `correo` | `String` | Formato email valido. Unico, normalizado a minusculas. | Si |
| `telefono` | `String` | Formato internacional E.164. Opcional pero validado si se ingresa. | No |
| `password_hash` | `String` | Hash seguro generado por backend. Nunca se retorna por API. | Si |
| `rol` | `Enum` | Valores definidos en `UserRole`. | Si |
| `estado` | `Enum` | Valores definidos en `UserStatus`. Default: `PENDIENTE_VERIFICACION`. | Si |
| `correo_verificado_en` | `Timestamp` | `null` hasta confirmar correo. UTC. | No |
| `ultimo_login_en` | `Timestamp` | Actualizado al iniciar sesion correctamente. UTC. | No |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. Inmutable. | Si |
| `actualizado_en` | `Timestamp` | Actualizado automaticamente en cada modificacion. UTC. | Si |

### 3.2 Perfil de Productor (`ProducerProfile`)

Representa los datos operativos del actor productor.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `user_id` | `UUID (FK)` | Debe apuntar a un usuario con rol `PRODUCTOR`. Unico. | Si |
| `tipo_documento` | `String` | Catalogo: `CC`, `NIT`, `CE`, `PASAPORTE`. | Si |
| `numero_documento` | `String` | Unico por tipo de documento. Maximo 40 caracteres. | Si |
| `organizacion` | `String` | Maximo 160 caracteres. | No |
| `descripcion` | `String` | Maximo 600 caracteres. | No |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. | Si |
| `actualizado_en` | `Timestamp` | Actualizado automaticamente. UTC. | Si |

### 3.3 Perfil Comercial (`CommercialProfile`)

Representa informacion adicional para compradores internacionales y exportadores.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `user_id` | `UUID (FK)` | Debe apuntar a usuario `COMPRADOR_INTERNACIONAL` o `EXPORTADOR`. Unico. | Si |
| `empresa` | `String` | Maximo 160 caracteres. | Si |
| `pais` | `String` | Codigo ISO 3166-1 alpha-2 o catalogo interno. | Si |
| `cargo` | `String` | Maximo 120 caracteres. | No |
| `sitio_web` | `String` | URL valida si se ingresa. | No |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. | Si |
| `actualizado_en` | `Timestamp` | Actualizado automaticamente. UTC. | Si |

### 3.4 Token de Verificacion (`EmailVerificationToken`)

Representa el token temporal para confirmar correo electronico.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `user_id` | `UUID (FK)` | Usuario propietario del token. | Si |
| `token_hash` | `String` | Hash del token. El token plano no se guarda. | Si |
| `expira_en` | `Timestamp` | Debe ser futuro al momento de creacion. UTC. | Si |
| `usado_en` | `Timestamp` | `null` hasta ser utilizado. UTC. | No |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. | Si |

### 3.5 Relaciones

```text
User (1) ---- (0..1) ProducerProfile
User (1) ---- (0..1) CommercialProfile
User (1) ---- (N) EmailVerificationToken
User (1) ---- (N) AuditLog
```

### 3.6 Enumeraciones

#### `UserRole`

- `PRODUCTOR`
- `OPERADOR_TURISTICO`
- `EXPORTADOR`
- `COMPRADOR_INTERNACIONAL`
- `CERTIFICADOR`
- `ADMIN`

#### `UserStatus`

- `PENDIENTE_VERIFICACION`
- `ACTIVO`
- `INACTIVO`
- `BLOQUEADO`

---

## 4. User stories y escenarios de aceptacion

### US-01 - Registro inicial de usuario `P1`

**Como** visitante,  
**quiero** crear una cuenta con correo, contrasena y tipo de actor,  
**para que** pueda acceder a AgroTrace y completar mi perfil operativo.

**Por que P1**: Sin cuenta de usuario no existe autoria ni permisos para operar trazabilidad agricola.

**Test independiente**: El visitante envia datos validos al endpoint de registro. El test es exitoso si se crea un usuario en estado `PENDIENTE_VERIFICACION` y se genera un token de verificacion.

#### Escenario 1 - Registro exitoso

```gherkin
Given  que no existe ningun usuario con el correo "productor@test.com"
When   el visitante envia nombre, correo, telefono opcional, contrasena y rol PRODUCTOR
Then   el sistema crea el usuario en estado PENDIENTE_VERIFICACION
And    almacena la contrasena como hash seguro
And    genera un token de verificacion con expiracion
And    registra auditoria de creacion de usuario
And    retorna un mensaje indicando que debe verificar su correo
```

#### Escenario 2 - Correo duplicado

```gherkin
Given  que ya existe un usuario con correo "productor@test.com"
When   el visitante intenta registrarse con el mismo correo
Then   el sistema no crea un nuevo usuario
And    retorna error de conflicto indicando que el correo ya esta registrado
```

#### Escenario 3 - Contrasena invalida

```gherkin
Given  que el visitante esta registrando una cuenta
When   envia una contrasena que no cumple la politica minima
Then   el sistema rechaza la solicitud
And    informa los requisitos de contrasena incumplidos
```

### US-02 - Verificar correo electronico `P1`

**Como** usuario registrado,  
**quiero** confirmar mi correo electronico,  
**para que** mi cuenta quede activa y pueda iniciar sesion.

**Por que P1**: La verificacion reduce cuentas falsas y protege acciones posteriores de trazabilidad.

**Test independiente**: El usuario confirma un token valido. El test es exitoso si el usuario pasa a estado `ACTIVO`.

#### Escenario 1 - Verificacion exitosa

```gherkin
Given  que existe un usuario en estado PENDIENTE_VERIFICACION
And    tiene un token de verificacion vigente y no utilizado
When   el usuario confirma el token
Then   el sistema marca el token como usado
And    actualiza correo_verificado_en
And    cambia el estado del usuario a ACTIVO
And    registra auditoria de verificacion de correo
```

#### Escenario 2 - Token expirado

```gherkin
Given  que existe un token de verificacion expirado
When   el usuario intenta confirmar su correo con ese token
Then   el sistema rechaza la solicitud
And    mantiene el usuario en estado PENDIENTE_VERIFICACION
And    indica que debe solicitar un nuevo enlace de verificacion
```

#### Escenario 3 - Token reutilizado

```gherkin
Given  que existe un token ya utilizado
When   el usuario intenta confirmar el correo con ese token
Then   el sistema rechaza la solicitud
And    no modifica el estado actual del usuario
```

### US-03 - Iniciar sesion `P1`

**Como** usuario activo,  
**quiero** iniciar sesion con correo y contrasena,  
**para que** pueda acceder a endpoints protegidos del sistema.

**Por que P1**: La autenticacion es requisito para operar cualquier recurso privado del backend.

**Test independiente**: El usuario activo envia credenciales validas. El test es exitoso si recibe `access_token`, `refresh_token` y datos basicos del usuario.

#### Escenario 1 - Login exitoso

```gherkin
Given  que existe un usuario ACTIVO con correo verificado
When   envia correo y contrasena correctos
Then   el sistema retorna access_token y refresh_token
And    retorna id, nombre, correo, rol y estado del usuario
And    actualiza ultimo_login_en
And    registra auditoria de inicio de sesion
```

#### Escenario 2 - Cuenta sin verificar

```gherkin
Given  que existe un usuario en estado PENDIENTE_VERIFICACION
When   intenta iniciar sesion con credenciales correctas
Then   el sistema rechaza el login
And    indica que debe verificar su correo antes de continuar
```

#### Escenario 3 - Credenciales invalidas

```gherkin
Given  que existe un usuario registrado
When   envia una contrasena incorrecta
Then   el sistema rechaza el login
And    no informa si fallo el correo o la contrasena por separado
```

### US-04 - Completar perfil de productor `P1`

**Como** productor activo,  
**quiero** completar mi perfil con documento y organizacion,  
**para que** pueda registrar fincas y lotes con trazabilidad de propietario.

**Por que P1**: El MVP necesita productores con informacion minima antes de crear fincas.

**Test independiente**: Un usuario con rol `PRODUCTOR` envia datos validos de perfil. El test es exitoso si se crea `ProducerProfile` asociado a su usuario.

#### Escenario 1 - Perfil creado correctamente

```gherkin
Given  que existe un usuario ACTIVO con rol PRODUCTOR
And    no tiene perfil de productor
When   envia tipo_documento, numero_documento y organizacion opcional
Then   el sistema crea el perfil de productor
And    lo asocia al usuario autenticado
And    registra auditoria de creacion de perfil
```

#### Escenario 2 - Documento duplicado

```gherkin
Given  que ya existe un perfil con tipo_documento CC y numero_documento 123456
When   otro productor intenta registrar el mismo documento
Then   el sistema rechaza la solicitud
And    indica que el documento ya esta asociado a otro productor
```

#### Escenario 3 - Rol no permitido

```gherkin
Given  que existe un usuario ACTIVO con rol COMPRADOR_INTERNACIONAL
When   intenta crear un perfil de productor
Then   el sistema rechaza la solicitud por permisos insuficientes
```

### US-05 - Completar perfil comercial `P2`

**Como** comprador internacional o exportador,  
**quiero** completar los datos de mi empresa y pais,  
**para que** pueda operar solicitudes comerciales dentro de la plataforma.

**Por que P2**: Facilita trazabilidad comercial, pero la solicitud publica de compra puede existir antes de una cuenta completa.

**Test independiente**: Un usuario con rol comercial envia empresa y pais validos. El test es exitoso si se crea `CommercialProfile`.

#### Escenario 1 - Perfil comercial creado correctamente

```gherkin
Given  que existe un usuario ACTIVO con rol COMPRADOR_INTERNACIONAL
And    no tiene perfil comercial
When   envia empresa, pais, cargo opcional y sitio_web opcional
Then   el sistema crea el perfil comercial
And    lo asocia al usuario autenticado
And    registra auditoria de creacion de perfil
```

#### Escenario 2 - Pais invalido

```gherkin
Given  que existe un usuario ACTIVO con rol EXPORTADOR
When   envia un pais que no pertenece al catalogo soportado
Then   el sistema rechaza la solicitud
And    indica que el pais no es valido
```

### US-06 - Desactivar o reactivar usuario `P2`

**Como** administrador,  
**quiero** cambiar el estado de un usuario,  
**para que** pueda bloquear acceso sin eliminar informacion historica.

**Por que P2**: La administracion de acceso es necesaria para operacion real, pero no bloquea el primer registro del MVP.

**Test independiente**: Un administrador desactiva un usuario activo. El test es exitoso si el usuario no puede iniciar sesion y sus datos historicos permanecen.

#### Escenario 1 - Desactivar usuario activo

```gherkin
Given  que existe un usuario ACTIVO
And    el administrador esta autenticado con rol ADMIN
When   cambia el estado del usuario a INACTIVO
Then   el sistema actualiza el estado
And    invalida sesiones activas si existe soporte de sesiones
And    registra auditoria con usuario administrador, timestamp y motivo
```

#### Escenario 2 - Reactivar usuario inactivo

```gherkin
Given  que existe un usuario INACTIVO con correo verificado
And    el administrador esta autenticado con rol ADMIN
When   cambia el estado del usuario a ACTIVO
Then   el sistema permite que el usuario vuelva a iniciar sesion
And    registra auditoria de reactivacion
```

#### Escenario 3 - Usuario inactivo intenta iniciar sesion

```gherkin
Given  que existe un usuario INACTIVO
When   intenta iniciar sesion con credenciales correctas
Then   el sistema rechaza el login
And    indica que la cuenta no esta activa
```

---

## 5. Edge cases

| # | Caso | Comportamiento esperado |
| --- | --- | --- |
| 1 | Correo con mayusculas o espacios laterales | El backend normaliza a minusculas y elimina espacios antes de validar unicidad. |
| 2 | Registro simultaneo con el mismo correo | La base de datos debe bloquear duplicados mediante indice unico. Solo una solicitud crea usuario. |
| 3 | Token de verificacion filtrado despues de uso | El token usado no puede reutilizarse. |
| 4 | Token de verificacion expirado | Se rechaza la verificacion y se permite solicitar un nuevo token. |
| 5 | Usuario activo intenta verificar correo de nuevo | El sistema responde de forma idempotente sin generar cambios sensibles. |
| 6 | Cambio de rol con datos relacionados | No se permite cambiar rol si existen recursos dependientes incompatibles sin proceso administrativo explicito. |
| 7 | Perfil duplicado para el mismo usuario | El backend rechaza crear un segundo perfil del mismo tipo para el mismo usuario. |
| 8 | Usuario desactivado con fincas o lotes creados | No se elimina informacion historica ni se rompen relaciones. Solo se bloquea acceso. |
| 9 | Contrasena enviada en logs o auditoria | Nunca se registra contrasena ni hash en logs, auditoria o respuestas API. |
| 10 | Login con correo inexistente | El backend retorna mensaje generico de credenciales invalidas. |

---

## 6. Requerimientos funcionales

| ID | Descripcion | US relacionada | Prioridad |
| --- | --- | --- | --- |
| FR-001 | El sistema DEBE permitir registrar usuarios con nombre, correo, contrasena, telefono opcional y rol solicitado. | US-01 | P1 |
| FR-002 | El sistema DEBE validar unicidad de correo normalizado antes de persistir. | US-01 | P1 |
| FR-003 | El sistema DEBE almacenar contrasenas usando hashing seguro y sal gestionada por el algoritmo. | US-01 | P1 |
| FR-004 | El sistema DEBE crear usuarios nuevos en estado `PENDIENTE_VERIFICACION`. | US-01 | P1 |
| FR-005 | El sistema DEBE generar token de verificacion con expiracion y guardar solo su hash. | US-01, US-02 | P1 |
| FR-006 | El sistema DEBE activar usuarios cuando confirmen un token vigente y no utilizado. | US-02 | P1 |
| FR-007 | El sistema DEBE permitir login solo a usuarios `ACTIVO` con correo verificado. | US-03 | P1 |
| FR-008 | El sistema DEBE emitir `access_token` y `refresh_token` al autenticar correctamente. | US-03 | P1 |
| FR-009 | El sistema DEBE permitir a productores crear su perfil de productor una unica vez. | US-04 | P1 |
| FR-010 | El sistema DEBE impedir que usuarios sin rol `PRODUCTOR` creen `ProducerProfile`. | US-04 | P1 |
| FR-011 | El sistema DEBE permitir perfiles comerciales para usuarios `EXPORTADOR` y `COMPRADOR_INTERNACIONAL`. | US-05 | P2 |
| FR-012 | El sistema DEBE permitir a administradores desactivar y reactivar usuarios. | US-06 | P2 |
| FR-013 | El sistema NO DEBE permitir eliminacion fisica de usuarios con informacion relacionada. | US-06 | P1 |
| FR-014 | El sistema DEBE registrar auditoria de registro, verificacion, login exitoso, creacion de perfiles y cambios de estado. | Todas | P2 |

---

## 7. Requerimientos no funcionales

### 7.1 Rendimiento

| ID | Descripcion |
| --- | --- |
| RNF-001 | La operacion de registro debe completarse en menos de 800 ms p95 sin envio sincrono de correo externo. |
| RNF-002 | La operacion de login debe completarse en menos de 500 ms p95. |
| RNF-003 | La verificacion de token debe completarse en menos de 500 ms p95. |

### 7.2 Seguridad

| ID | Descripcion |
| --- | --- |
| RNF-004 | Las contrasenas deben almacenarse con `argon2id` o `bcrypt` con costo configurado por ambiente. |
| RNF-005 | Los endpoints de perfil y administracion deben requerir token JWT valido. |
| RNF-006 | Los endpoints administrativos deben validar rol `ADMIN`. |
| RNF-007 | El backend debe sanitizar y validar todos los campos de texto. |
| RNF-008 | Los errores de login no deben revelar si el correo existe. |
| RNF-009 | Los tokens de verificacion y refresh tokens no deben guardarse en texto plano. |

### 7.3 Consistencia de datos

| ID | Descripcion |
| --- | --- |
| RNF-010 | La creacion de usuario y token de verificacion debe ser atomica. |
| RNF-011 | La unicidad de correo debe estar reforzada por indice unico en base de datos. |
| RNF-012 | La unicidad de documento de productor debe estar reforzada por indice unico compuesto. |
| RNF-013 | Los cambios de estado de usuario no deben eliminar ni modificar recursos historicos relacionados. |

### 7.4 Observabilidad

| ID | Descripcion |
| --- | --- |
| RNF-014 | Toda operacion de escritura debe registrar usuario actor cuando exista, timestamp y resultado. |
| RNF-015 | Los logs no deben contener contrasenas, tokens planos, refresh tokens ni hashes sensibles. |
| RNF-016 | Los fallos repetidos de login deben ser medibles para activar controles de abuso en una fase posterior. |

---

## 8. Criterios de exito

| ID | Criterio | Forma de medicion |
| --- | --- | --- |
| SC-001 | Un visitante puede crear una cuenta valida y queda en estado `PENDIENTE_VERIFICACION`. | Test de integracion de `POST /auth/register`. |
| SC-002 | Un usuario puede verificar correo con token valido y queda `ACTIVO`. | Test de integracion de `POST /auth/verify-email`. |
| SC-003 | Un usuario activo puede iniciar sesion y recibir tokens validos. | Test de integracion de `POST /auth/login`. |
| SC-004 | Un usuario no verificado no puede iniciar sesion. | Test de integracion de bloqueo por estado. |
| SC-005 | Un productor activo puede crear su perfil productor una sola vez. | Test de integracion de `POST /users/me/producer-profile`. |
| SC-006 | No existen usuarios duplicados por correo normalizado. | Prueba de concurrencia + constraint de base de datos. |
| SC-007 | Ninguna respuesta API retorna `password_hash` ni tokens persistidos. | Test de serializacion de respuestas. |

---

## 9. Contratos API sugeridos

### 9.1 Registrar usuario

`POST /auth/register`

#### Request

```json
{
  "nombre": "Ana Gomez",
  "correo": "ana@example.com",
  "telefono": "+573001112233",
  "password": "PasswordSeguro123!",
  "rol": "PRODUCTOR"
}
```

#### Response `201`

```json
{
  "id": "uuid",
  "correo": "ana@example.com",
  "estado": "PENDIENTE_VERIFICACION",
  "message": "Usuario registrado. Verifica tu correo para activar la cuenta."
}
```

### 9.2 Verificar correo

`POST /auth/verify-email`

#### Request

```json
{
  "token": "verification-token"
}
```

#### Response `200`

```json
{
  "estado": "ACTIVO",
  "message": "Correo verificado correctamente."
}
```

### 9.3 Iniciar sesion

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
  "user": {
    "id": "uuid",
    "nombre": "Ana Gomez",
    "correo": "ana@example.com",
    "rol": "PRODUCTOR",
    "estado": "ACTIVO"
  }
}
```

### 9.4 Crear perfil de productor

`POST /users/me/producer-profile`

#### Request

```json
{
  "tipo_documento": "CC",
  "numero_documento": "123456789",
  "organizacion": "Asociacion Sierra Verde",
  "descripcion": "Productora de cacao en el Magdalena."
}
```

#### Response `201`

```json
{
  "id": "uuid",
  "user_id": "uuid",
  "tipo_documento": "CC",
  "numero_documento": "123456789",
  "organizacion": "Asociacion Sierra Verde"
}
```

### 9.5 Cambiar estado de usuario

`PATCH /admin/users/{userId}/status`

#### Request

```json
{
  "estado": "INACTIVO",
  "motivo": "Solicitud del usuario"
}
```

#### Response `200`

```json
{
  "id": "uuid",
  "estado": "INACTIVO"
}
```

---

## 10. Fuera de alcance

Los siguientes puntos quedan excluidos de esta feature inicial y deben especificarse por separado:

- Recuperacion de contrasena.
- Autenticacion con proveedores externos como Google o Microsoft.
- Autenticacion multifactor.
- Invitaciones administrativas.
- Aprobacion manual de roles sensibles.
- Gestion granular de permisos por modulo.
- Sesiones por dispositivo y cierre remoto de sesiones.
- Rate limiting avanzado y bloqueo automatico por intentos fallidos.

---

## Historial de cambios

| Version | Fecha | Autor | Descripcion |
| --- | --- | --- | --- |
| 1.0.0 | 27/05/2026 | Equipo AgroTrace | Version inicial de la especificacion de registro de usuarios. |
