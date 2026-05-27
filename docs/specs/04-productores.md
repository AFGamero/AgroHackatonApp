# Feature Specification: Productores

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

El productor es el actor central de AgroTrace Magdalena. La trazabilidad agricola empieza cuando un usuario con rol `PRODUCTOR` completa su perfil operativo y queda habilitado para registrar fincas, lotes, estados de cultivo, evidencias y certificaciones.

Esta feature define la gestion del perfil productor como entidad de dominio separada de la cuenta autenticable. El usuario pertenece al modulo de identidad; el productor contiene informacion agricola, documental y publica necesaria para operar trazabilidad.

El objetivo del MVP es permitir que un usuario activo con rol `PRODUCTOR` cree y mantenga su perfil productor, que el backend valide unicidad documental y que otros modulos puedan exigir perfil completo antes de crear recursos productivos.

---

## 2. Actores

| ID | Actor | Tipo | Descripcion |
| --- | --- | --- | --- |
| ACT-01 | Productor | Primario | Usuario con rol `PRODUCTOR` que completa y actualiza su perfil operativo. |
| ACT-02 | Administrador | Primario | Usuario con rol `ADMIN` que consulta y gestiona productores. |
| ACT-03 | Sistema de Fincas | Secundario | Modulo interno que valida que el productor exista antes de crear fincas. |
| ACT-04 | Sistema de Pasaporte Digital | Secundario | Modulo interno que consume informacion publica del productor. |
| ACT-05 | Turista o Comprador | Secundario | Usuario publico que consulta informacion no sensible del productor en el pasaporte digital. |

> Un usuario autenticado sin rol `PRODUCTOR` no puede crear ni modificar un perfil productor propio.

---

## 3. Entidades y modelo de datos

### 3.1 Productor (`ProducerProfile`)

Representa el perfil agricola y documental de un usuario productor.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `user_id` | `UUID (FK)` | Debe apuntar a un usuario `ACTIVO` con rol `PRODUCTOR`. Unico. | Si |
| `tipo_documento` | `Enum` | Valores definidos en `DocumentType`. | Si |
| `numero_documento` | `String` | Maximo 40 caracteres. Unico junto con `tipo_documento`. | Si |
| `organizacion` | `String` | Maximo 160 caracteres. Asociacion, cooperativa o empresa. | No |
| `nombre_publico` | `String` | Maximo 120 caracteres. Default: nombre del usuario. | Si |
| `telefono_publico` | `String` | Formato E.164 si se ingresa. Visible solo si `mostrar_contacto` es true. | No |
| `descripcion` | `String` | Maximo 600 caracteres. Puede mostrarse en pasaporte digital. | No |
| `historia` | `String` | Maximo 1500 caracteres. Relato publico del productor. | No |
| `municipio` | `String` | Catalogo de municipios soportados del Magdalena. | No |
| `mostrar_contacto` | `Boolean` | Default `false`. Controla exposicion publica de contacto. | Si |
| `estado` | `Enum` | Valores definidos en `ProducerStatus`. Default `ACTIVO`. | Si |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. Inmutable. | Si |
| `actualizado_en` | `Timestamp` | Actualizado automaticamente. UTC. | Si |

### 3.2 Enumeraciones

#### `DocumentType`

- `CC`
- `NIT`
- `CE`
- `PASAPORTE`

#### `ProducerStatus`

- `ACTIVO`
- `INACTIVO`
- `PENDIENTE_REVISION`

### 3.3 Relaciones

```text
User (1) ---- (0..1) ProducerProfile
ProducerProfile (1) ---- (N) Farm
ProducerProfile (1) ---- (N) Product
ProducerProfile (1) ---- (N) AuditLog
```

### 3.4 Datos publicos vs privados

| Campo | Uso interno | Visible en pasaporte digital |
| --- | :---: | :---: |
| `tipo_documento` | Si | No |
| `numero_documento` | Si | No |
| `organizacion` | Si | Si |
| `nombre_publico` | Si | Si |
| `telefono_publico` | Si | Solo si `mostrar_contacto = true` |
| `descripcion` | Si | Si |
| `historia` | Si | Si |
| `municipio` | Si | Si |

---

## 4. User stories y escenarios de aceptacion

### US-01 - Crear perfil productor `P1`

**Como** usuario con rol `PRODUCTOR`,  
**quiero** crear mi perfil productor,  
**para que** pueda registrar fincas y operar trazabilidad agricola.

**Por que P1**: Sin perfil productor no existe propietario agricola para asociar fincas, lotes y pasaportes digitales.

**Test independiente**: Un usuario `ACTIVO` con rol `PRODUCTOR` envia datos validos. El test es exitoso si se crea un `ProducerProfile` unico asociado al usuario.

#### Escenario 1 - Creacion exitosa

```gherkin
Given  que existe un usuario ACTIVO con rol PRODUCTOR
And    no tiene perfil productor creado
When   envia tipo_documento, numero_documento, nombre_publico y datos opcionales
Then   el sistema crea el perfil productor en estado ACTIVO
And    asocia el perfil al usuario autenticado
And    registra auditoria de creacion
```

#### Escenario 2 - Usuario ya tiene perfil

```gherkin
Given  que existe un usuario PRODUCTOR con perfil productor creado
When   intenta crear otro perfil productor
Then   el sistema rechaza la solicitud
And    indica que el usuario ya tiene un perfil productor asociado
```

#### Escenario 3 - Usuario con rol incorrecto

```gherkin
Given  que existe un usuario ACTIVO con rol COMPRADOR_INTERNACIONAL
When   intenta crear un perfil productor
Then   el sistema rechaza la solicitud con 403
And    no crea registros de productor
```

### US-02 - Validar documento unico `P1`

**Como** backend,  
**quiero** validar que el documento de productor no este duplicado,  
**para que** un mismo productor no quede representado por perfiles inconsistentes.

**Por que P1**: La identidad documental es necesaria para trazabilidad y confianza comercial.

**Test independiente**: Dos usuarios intentan registrar el mismo tipo y numero de documento. El test es exitoso si solo uno queda persistido.

#### Escenario 1 - Documento disponible

```gherkin
Given  que no existe productor con tipo_documento CC y numero_documento 123456
When   el productor registra ese documento
Then   el sistema permite crear el perfil
```

#### Escenario 2 - Documento duplicado

```gherkin
Given  que ya existe productor con tipo_documento CC y numero_documento 123456
When   otro usuario intenta registrar el mismo documento
Then   el sistema rechaza la solicitud con error de conflicto
And    no expone informacion privada del productor existente
```

#### Escenario 3 - Documento con espacios o formato inconsistente

```gherkin
Given  que el productor envia numero_documento con espacios laterales
When   el backend valida la solicitud
Then   normaliza el valor antes de validar unicidad
And    persiste el documento normalizado
```

### US-03 - Consultar perfil propio `P1`

**Como** productor autenticado,  
**quiero** consultar mi perfil productor,  
**para que** pueda verificar mis datos antes de crear fincas o publicar informacion.

**Por que P1**: Los siguientes modulos necesitan saber si el perfil esta completo.

**Test independiente**: Un productor autenticado consulta `GET /producers/me`. El test es exitoso si recibe su perfil o un estado claro indicando que debe crearlo.

#### Escenario 1 - Perfil existente

```gherkin
Given  que existe un usuario PRODUCTOR autenticado con perfil creado
When   consulta su perfil
Then   el sistema retorna los datos del perfil productor
And    no retorna campos sensibles de autenticacion
```

#### Escenario 2 - Perfil no creado

```gherkin
Given  que existe un usuario PRODUCTOR autenticado sin perfil
When   consulta su perfil
Then   el sistema responde que el perfil esta pendiente de creacion
And    no crea ningun perfil automaticamente
```

### US-04 - Actualizar perfil productor `P2`

**Como** productor,  
**quiero** actualizar informacion descriptiva de mi perfil,  
**para que** mi historia y datos publicos se mantengan actualizados.

**Por que P2**: Es importante para mantener calidad de datos, pero no bloquea la primera creacion.

**Test independiente**: Un productor actualiza `historia` y `mostrar_contacto`. El test es exitoso si el pasaporte digital consume los nuevos datos publicos.

#### Escenario 1 - Actualizacion exitosa

```gherkin
Given  que existe un productor autenticado con perfil ACTIVO
When   actualiza nombre_publico, organizacion, descripcion, historia o mostrar_contacto
Then   el sistema persiste los cambios
And    actualiza actualizado_en
And    registra auditoria con campos modificados
```

#### Escenario 2 - Intento de cambiar documento

```gherkin
Given  que existe un perfil productor con documento registrado
When   el productor intenta cambiar tipo_documento o numero_documento
Then   el sistema rechaza el cambio en el flujo normal
And    indica que el cambio documental requiere proceso administrativo
```

#### Escenario 3 - Texto demasiado largo

```gherkin
Given  que el productor actualiza su historia
When   envia mas de 1500 caracteres
Then   el sistema rechaza la solicitud
And    indica el limite maximo permitido
```

### US-05 - Consultar informacion publica del productor `P1`

**Como** turista o comprador,  
**quiero** ver informacion publica del productor en el pasaporte digital,  
**para que** pueda conocer el origen humano y territorial del producto.

**Por que P1**: El pasaporte digital necesita mostrar datos confiables sin exponer informacion sensible.

**Test independiente**: El pasaporte digital solicita el perfil publico de un productor. El test es exitoso si solo retorna campos permitidos.

#### Escenario 1 - Perfil publico disponible

```gherkin
Given  que existe un productor ACTIVO asociado a un lote publicado
When   el pasaporte digital consulta su informacion publica
Then   el sistema retorna nombre_publico, organizacion, descripcion, historia y municipio
And    excluye documento y campos privados
```

#### Escenario 2 - Contacto oculto

```gherkin
Given  que el productor tiene mostrar_contacto en false
When   se consulta su informacion publica
Then   el sistema no retorna telefono_publico
```

#### Escenario 3 - Contacto visible

```gherkin
Given  que el productor tiene mostrar_contacto en true
And    tiene telefono_publico valido
When   se consulta su informacion publica
Then   el sistema retorna telefono_publico
```

### US-06 - Desactivar productor `P2`

**Como** administrador,  
**quiero** desactivar un perfil productor,  
**para que** deje de crear nuevos recursos sin romper la trazabilidad historica.

**Por que P2**: Permite control operativo sin eliminar datos relacionados.

**Test independiente**: Un administrador desactiva un productor. El test es exitoso si no puede crear nuevas fincas, pero sus pasaportes historicos siguen resolviendo datos publicos permitidos.

#### Escenario 1 - Desactivacion exitosa

```gherkin
Given  que existe un productor ACTIVO
And    el administrador esta autenticado
When   cambia su estado a INACTIVO
Then   el sistema bloquea creacion de nuevas fincas para ese productor
And    mantiene referencias historicas
And    registra auditoria de desactivacion
```

#### Escenario 2 - Productor inactivo intenta crear finca

```gherkin
Given  que existe un productor INACTIVO
When   intenta crear una finca
Then   el sistema rechaza la operacion
And    indica que el perfil productor no esta activo
```

---

## 5. Edge cases

| # | Caso | Comportamiento esperado |
| --- | --- | --- |
| 1 | Dos requests crean perfil para el mismo usuario al mismo tiempo | La base de datos permite solo un perfil por `user_id`. Una solicitud falla con conflicto. |
| 2 | Dos productores registran el mismo documento simultaneamente | Indice unico compuesto bloquea duplicado por `tipo_documento` y `numero_documento`. |
| 3 | Productor cambia correo en modulo de usuarios | El perfil productor no cambia; la relacion se mantiene por `user_id`. |
| 4 | Productor inactivo con lotes publicados | Los pasaportes historicos siguen visibles si los lotes estan publicados. |
| 5 | `mostrar_contacto = true` sin telefono publico | El backend no retorna telefono y puede advertir que falta telefono publico. |
| 6 | Municipio fuera del Magdalena | El backend rechaza el valor si el catalogo MVP solo soporta municipios del Magdalena. |
| 7 | Documento enviado con guiones o puntos | El backend aplica normalizacion definida antes de validar unicidad. |
| 8 | Administrador intenta eliminar productor con fincas | No se permite eliminacion fisica; solo cambio de estado. |
| 9 | Perfil productor incompleto intenta crear finca | El modulo de fincas rechaza hasta completar campos minimos requeridos. |
| 10 | Informacion publica con texto malicioso | El backend sanitiza y el frontend debe renderizar como texto, no HTML. |

---

## 6. Requerimientos funcionales

| ID | Descripcion | US relacionada | Prioridad |
| --- | --- | --- | --- |
| FR-001 | El sistema DEBE permitir crear un perfil productor a usuarios `ACTIVO` con rol `PRODUCTOR`. | US-01 | P1 |
| FR-002 | El sistema DEBE impedir mas de un perfil productor por usuario. | US-01 | P1 |
| FR-003 | El sistema DEBE validar unicidad de `tipo_documento` + `numero_documento`. | US-02 | P1 |
| FR-004 | El sistema DEBE normalizar documento antes de persistir y validar unicidad. | US-02 | P1 |
| FR-005 | El sistema DEBE permitir consultar el perfil propio del productor autenticado. | US-03 | P1 |
| FR-006 | El sistema DEBE permitir actualizar campos descriptivos y publicos del perfil productor. | US-04 | P2 |
| FR-007 | El sistema NO DEBE permitir cambio documental desde el flujo normal de edicion. | US-04 | P1 |
| FR-008 | El sistema DEBE exponer una vista publica del productor sin documento ni datos sensibles. | US-05 | P1 |
| FR-009 | El sistema DEBE respetar `mostrar_contacto` antes de exponer telefono publico. | US-05 | P1 |
| FR-010 | El sistema DEBE permitir a administradores desactivar perfiles productores. | US-06 | P2 |
| FR-011 | El sistema NO DEBE permitir eliminacion fisica de productores con recursos relacionados. | US-06 | P1 |
| FR-012 | El sistema DEBE registrar auditoria de creacion, actualizacion y cambio de estado. | Todas | P2 |

---

## 7. Requerimientos no funcionales

### 7.1 Rendimiento

| ID | Descripcion |
| --- | --- |
| RNF-001 | La creacion de perfil productor debe completarse en menos de 500 ms p95. |
| RNF-002 | La consulta del perfil propio debe completarse en menos de 300 ms p95. |
| RNF-003 | La consulta publica del productor para pasaporte digital debe completarse en menos de 250 ms p95. |

### 7.2 Seguridad

| ID | Descripcion |
| --- | --- |
| RNF-004 | Solo usuarios autenticados con rol `PRODUCTOR` pueden crear o modificar su perfil productor. |
| RNF-005 | Solo administradores pueden consultar perfiles privados de terceros o cambiar estados. |
| RNF-006 | Documento y datos privados nunca deben aparecer en respuestas publicas. |
| RNF-007 | Todos los campos de texto deben validarse y sanitizarse. |

### 7.3 Consistencia de datos

| ID | Descripcion |
| --- | --- |
| RNF-008 | La unicidad por `user_id` debe reforzarse con indice unico. |
| RNF-009 | La unicidad documental debe reforzarse con indice unico compuesto. |
| RNF-010 | La desactivacion de productor no debe eliminar fincas, lotes, evidencias ni pasaportes historicos. |
| RNF-011 | Los modulos de fincas y lotes deben validar que el productor este activo antes de crear nuevos recursos. |

### 7.4 Observabilidad

| ID | Descripcion |
| --- | --- |
| RNF-012 | Toda operacion de escritura sobre productor debe registrar auditoria con usuario actor, timestamp y resultado. |
| RNF-013 | Los errores por conflicto documental deben ser medibles para detectar registros duplicados frecuentes. |

---

## 8. Criterios de exito

| ID | Criterio | Forma de medicion |
| --- | --- | --- |
| SC-001 | Un usuario `PRODUCTOR` activo puede crear su perfil productor una sola vez. | Test de integracion de `POST /producers/me`. |
| SC-002 | Dos perfiles no pueden compartir el mismo documento normalizado. | Test de integracion + constraint de base de datos. |
| SC-003 | Usuarios sin rol `PRODUCTOR` reciben `403` al crear perfil productor. | Test de autorizacion por rol. |
| SC-004 | El perfil publico no retorna documento ni datos sensibles. | Test de serializacion publica. |
| SC-005 | Un productor inactivo no puede crear nuevas fincas. | Test de integracion con modulo de fincas. |
| SC-006 | La actualizacion de campos descriptivos queda auditada. | Verificacion de registros en `AuditLog`. |

---

## 9. Contratos API sugeridos

### 9.1 Crear perfil productor

`POST /producers/me`

#### Headers

```http
Authorization: Bearer access-token
```

#### Request

```json
{
  "tipo_documento": "CC",
  "numero_documento": "123456789",
  "organizacion": "Asociacion Sierra Verde",
  "nombre_publico": "Ana Gomez",
  "telefono_publico": "+573001112233",
  "descripcion": "Productora de cacao en el Magdalena.",
  "historia": "Familia productora con tradicion agricola en la Sierra Nevada.",
  "municipio": "Santa Marta",
  "mostrar_contacto": false
}
```

#### Response `201`

```json
{
  "id": "uuid",
  "user_id": "uuid",
  "tipo_documento": "CC",
  "numero_documento": "123456789",
  "organizacion": "Asociacion Sierra Verde",
  "nombre_publico": "Ana Gomez",
  "estado": "ACTIVO"
}
```

### 9.2 Consultar perfil propio

`GET /producers/me`

#### Headers

```http
Authorization: Bearer access-token
```

#### Response `200`

```json
{
  "id": "uuid",
  "tipo_documento": "CC",
  "numero_documento": "123456789",
  "organizacion": "Asociacion Sierra Verde",
  "nombre_publico": "Ana Gomez",
  "telefono_publico": "+573001112233",
  "descripcion": "Productora de cacao en el Magdalena.",
  "historia": "Familia productora con tradicion agricola en la Sierra Nevada.",
  "municipio": "Santa Marta",
  "mostrar_contacto": false,
  "estado": "ACTIVO"
}
```

### 9.3 Actualizar perfil productor

`PATCH /producers/me`

#### Headers

```http
Authorization: Bearer access-token
```

#### Request

```json
{
  "organizacion": "Cooperativa Sierra Verde",
  "descripcion": "Productora de cacao y cafe en el Magdalena.",
  "historia": "Tres generaciones dedicadas al cultivo sostenible.",
  "mostrar_contacto": true
}
```

#### Response `200`

```json
{
  "id": "uuid",
  "organizacion": "Cooperativa Sierra Verde",
  "descripcion": "Productora de cacao y cafe en el Magdalena.",
  "mostrar_contacto": true,
  "actualizado_en": "2026-05-27T08:00:00.000Z"
}
```

### 9.4 Consultar perfil publico

`GET /public/producers/{producerId}`

#### Response `200`

```json
{
  "id": "uuid",
  "nombre_publico": "Ana Gomez",
  "organizacion": "Cooperativa Sierra Verde",
  "descripcion": "Productora de cacao y cafe en el Magdalena.",
  "historia": "Tres generaciones dedicadas al cultivo sostenible.",
  "municipio": "Santa Marta"
}
```

### 9.5 Cambiar estado de productor

`PATCH /admin/producers/{producerId}/status`

#### Headers

```http
Authorization: Bearer admin-access-token
```

#### Request

```json
{
  "estado": "INACTIVO",
  "motivo": "Revision administrativa"
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

- Verificacion documental automatica con servicios externos.
- Aprobacion manual avanzada de productores.
- Cambio documental por flujo administrativo completo.
- Gestion de multiples productores asociados a un mismo usuario.
- Gestion de representantes legales.
- Calificaciones o reputacion publica del productor.
- Geolocalizacion detallada del productor independiente de sus fincas.
- Importacion masiva de productores.

---

## Historial de cambios

| Version | Fecha | Autor | Descripcion |
| --- | --- | --- | --- |
| 1.0.0 | 27/05/2026 | Equipo AgroTrace | Version inicial de la especificacion de productores. |
