# Feature Specification: Fincas

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

La finca representa el espacio agricola donde se originan los productos trazados por AgroTrace Magdalena. Es el recurso base para crear lotes, asociar experiencias turisticas, registrar certificaciones a nivel de finca y construir pasaportes digitales confiables.

Esta feature define la gestion backend de fincas asociadas a productores activos. Una finca debe conservar integridad historica: aunque se desactive, los lotes, evidencias y pasaportes ya publicados deben mantener su referencia al origen.

El objetivo del MVP es permitir que un productor activo registre, consulte y mantenga sus fincas, con datos minimos de ubicacion, area, descripcion y fotografias opcionales, dejando reglas claras para propiedad, estado y exposicion publica.

---

## 2. Actores

| ID | Actor | Tipo | Descripcion |
| --- | --- | --- | --- |
| ACT-01 | Productor | Primario | Usuario con perfil productor activo. Crea y administra sus fincas. |
| ACT-02 | Administrador | Primario | Consulta, audita y puede cambiar estado de fincas por razones operativas. |
| ACT-03 | Sistema de Lotes | Secundario | Modulo interno que valida finca activa antes de crear lotes. |
| ACT-04 | Sistema de Turismo | Secundario | Modulo interno que asocia experiencias turisticas a fincas. |
| ACT-05 | Sistema de Pasaporte Digital | Secundario | Consume informacion publica de la finca para mostrar origen del lote. |
| ACT-06 | Turista o Comprador | Secundario | Consulta datos publicos de la finca desde pasaportes o catalogos. |

> Un productor solo puede crear y modificar fincas asociadas a su propio perfil productor.

---

## 3. Entidades y modelo de datos

### 3.1 Finca (`Farm`)

Representa una unidad agricola propiedad o administrada por un productor.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `producer_id` | `UUID (FK)` | Debe apuntar a un productor `ACTIVO`. | Si |
| `nombre` | `String` | Minimo 3 caracteres, maximo 120. No puede ser solo espacios. | Si |
| `descripcion` | `String` | Maximo 1000 caracteres. | No |
| `departamento` | `String` | Default recomendado: `Magdalena`. Catalogo soportado. | Si |
| `municipio` | `String` | Debe pertenecer al catalogo de municipios soportados. | Si |
| `vereda` | `String` | Maximo 120 caracteres. | No |
| `direccion` | `String` | Maximo 255 caracteres. | No |
| `latitud` | `Decimal` | Valor entre -90 y 90. Opcional en MVP. | No |
| `longitud` | `Decimal` | Valor entre -180 y 180. Opcional en MVP. | No |
| `area_hectareas` | `Decimal` | Valor mayor que 0. Maximo 2 decimales. | Si |
| `altitud_msnm` | `Integer` | Valor mayor o igual a 0 si se ingresa. | No |
| `historia` | `String` | Maximo 1500 caracteres. Texto publico opcional. | No |
| `estado` | `Enum` | Valores definidos en `FarmStatus`. Default `ACTIVA`. | Si |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. Inmutable. | Si |
| `actualizado_en` | `Timestamp` | Actualizado automaticamente. UTC. | Si |

### 3.2 Fotografia de Finca (`FarmPhoto`)

Representa una imagen asociada a la finca.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `farm_id` | `UUID (FK)` | Finca propietaria de la fotografia. | Si |
| `url` | `String` | URL del archivo almacenado. | Si |
| `descripcion` | `String` | Maximo 255 caracteres. | No |
| `es_portada` | `Boolean` | Default `false`. Solo una portada por finca. | Si |
| `visible_publicamente` | `Boolean` | Default `true`. Controla exposicion en pasaporte. | Si |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. | Si |

### 3.3 Enumeraciones

#### `FarmStatus`

- `ACTIVA`
- `INACTIVA`
- `PENDIENTE_REVISION`

### 3.4 Relaciones

```text
ProducerProfile (1) ---- (N) Farm
Farm (1) ---- (N) FarmPhoto
Farm (1) ---- (N) Lot
Farm (1) ---- (N) Certification
Farm (1) ---- (N) TourismExperience
Farm (1) ---- (N) AuditLog
```

### 3.5 Datos publicos vs privados

| Campo | Uso interno | Visible en pasaporte digital |
| --- | :---: | :---: |
| `nombre` | Si | Si |
| `descripcion` | Si | Si |
| `departamento` | Si | Si |
| `municipio` | Si | Si |
| `vereda` | Si | Si |
| `direccion` | Si | No por defecto |
| `latitud` / `longitud` | Si | Opcional, segun configuracion futura |
| `area_hectareas` | Si | Si |
| `historia` | Si | Si |
| `fotografias visibles` | Si | Si |

---

## 4. User stories y escenarios de aceptacion

### US-01 - Registrar finca `P1`

**Como** productor activo,  
**quiero** registrar una finca con sus datos basicos,  
**para que** pueda crear lotes y demostrar el origen agricola del producto.

**Por que P1**: Sin finca no existe unidad territorial para asociar lotes ni trazabilidad de origen.

**Test independiente**: Un productor con perfil activo envia datos validos. El test es exitoso si la finca queda creada en estado `ACTIVA`.

#### Escenario 1 - Registro exitoso

```gherkin
Given  que existe un usuario autenticado con rol PRODUCTOR
And    tiene perfil productor ACTIVO
When   envia nombre, departamento, municipio, area_hectareas y datos opcionales
Then   el sistema crea la finca en estado ACTIVA
And    la asocia al perfil productor autenticado
And    registra auditoria de creacion
```

#### Escenario 2 - Productor sin perfil completo

```gherkin
Given  que existe un usuario autenticado con rol PRODUCTOR
And    no tiene perfil productor creado
When   intenta registrar una finca
Then   el sistema rechaza la solicitud
And    indica que debe completar su perfil productor
```

#### Escenario 3 - Area invalida

```gherkin
Given  que el productor esta registrando una finca
When   envia area_hectareas igual a 0 o negativa
Then   el sistema rechaza la solicitud
And    indica que el area debe ser mayor que 0
```

### US-02 - Adjuntar fotografias de finca `P1`

**Como** productor activo,  
**quiero** asociar fotografias a mi finca,  
**para que** el pasaporte digital y futuras experiencias turisticas tengan evidencia visual del origen.

**Por que P1**: Las imagenes mejoran confianza y contexto en la trazabilidad publica.

**Test independiente**: El productor adjunta una URL de fotografia a una finca propia. El test es exitoso si queda asociada y visible segun configuracion.

#### Escenario 1 - Fotografia agregada correctamente

```gherkin
Given  que existe una finca ACTIVA del productor autenticado
When   adjunta una fotografia con URL valida
Then   el sistema crea el registro FarmPhoto
And    lo asocia a la finca
And    registra auditoria de carga de fotografia
```

#### Escenario 2 - Finca de otro productor

```gherkin
Given  que existe una finca de otro productor
When   el productor autenticado intenta adjuntar una fotografia
Then   el sistema rechaza la solicitud con 403
And    no crea la fotografia
```

#### Escenario 3 - Portada unica

```gherkin
Given  que una finca ya tiene una fotografia marcada como portada
When   el productor agrega otra fotografia como portada
Then   el sistema desmarca la portada anterior
And    deja solo la nueva fotografia como portada
```

### US-03 - Consultar mis fincas `P1`

**Como** productor,  
**quiero** listar mis fincas,  
**para que** pueda administrarlas y crear lotes dentro de ellas.

**Por que P1**: La gestion de lotes depende de seleccionar una finca propia.

**Test independiente**: Un productor autenticado consulta sus fincas. El test es exitoso si solo recibe fincas de su perfil productor.

#### Escenario 1 - Listado propio

```gherkin
Given  que el productor autenticado tiene tres fincas registradas
When   consulta su listado de fincas
Then   el sistema retorna solo sus fincas
And    incluye id, nombre, municipio, area_hectareas, estado y portada si existe
```

#### Escenario 2 - Productor sin fincas

```gherkin
Given  que el productor autenticado no tiene fincas registradas
When   consulta su listado de fincas
Then   el sistema retorna una lista vacia
```

### US-04 - Actualizar finca `P2`

**Como** productor,  
**quiero** actualizar datos descriptivos de mi finca,  
**para que** la informacion territorial y publica se mantenga vigente.

**Por que P2**: Es necesario para mantenimiento de datos, pero no bloquea la primera trazabilidad.

**Test independiente**: El productor actualiza descripcion e historia. El test es exitoso si los cambios se reflejan en la consulta de detalle.

#### Escenario 1 - Actualizacion exitosa

```gherkin
Given  que existe una finca ACTIVA del productor autenticado
When   actualiza descripcion, vereda, direccion, coordenadas, historia o altitud
Then   el sistema persiste los cambios
And    actualiza actualizado_en
And    registra auditoria con campos modificados
```

#### Escenario 2 - Cambio de area con lotes existentes

```gherkin
Given  que existe una finca con lotes registrados
When   el productor intenta reducir area_hectareas por debajo del area total de sus lotes
Then   el sistema rechaza la solicitud
And    indica que el area de finca no puede ser menor que la suma de areas de lotes
```

#### Escenario 3 - Coordenadas invalidas

```gherkin
Given  que el productor actualiza coordenadas de la finca
When   envia latitud fuera de -90 a 90 o longitud fuera de -180 a 180
Then   el sistema rechaza la solicitud
And    indica que las coordenadas no son validas
```

### US-05 - Consultar detalle de finca propia `P1`

**Como** productor,  
**quiero** consultar el detalle de una finca propia,  
**para que** pueda revisar sus datos antes de crear lotes o experiencias.

**Por que P1**: El detalle de finca es base para flujos posteriores.

**Test independiente**: Un productor consulta una finca propia por ID. El test es exitoso si recibe detalle completo y fotografias.

#### Escenario 1 - Consulta exitosa

```gherkin
Given  que existe una finca del productor autenticado
When   consulta el detalle por id
Then   el sistema retorna datos completos de la finca
And    incluye fotografias asociadas
```

#### Escenario 2 - Consulta de finca ajena

```gherkin
Given  que existe una finca de otro productor
When   el productor autenticado consulta ese id
Then   el sistema rechaza la solicitud con 403 o 404 segun politica de seguridad
```

### US-06 - Consultar informacion publica de finca `P1`

**Como** turista o comprador,  
**quiero** consultar informacion publica de la finca desde un pasaporte digital,  
**para que** pueda conocer el origen territorial del producto.

**Por que P1**: El pasaporte digital debe mostrar origen sin exponer datos sensibles.

**Test independiente**: El sistema de pasaporte consulta datos publicos de una finca asociada a lote publicado. El test es exitoso si no retorna direccion privada ni campos internos.

#### Escenario 1 - Finca publica en pasaporte

```gherkin
Given  que existe una finca ACTIVA asociada a un lote publicado
When   el pasaporte digital solicita sus datos publicos
Then   el sistema retorna nombre, descripcion, departamento, municipio, vereda, area_hectareas, historia y fotografias visibles
And    no retorna direccion exacta por defecto
```

#### Escenario 2 - Fotografias ocultas

```gherkin
Given  que una finca tiene fotografias con visible_publicamente en false
When   se consulta informacion publica de la finca
Then   el sistema excluye esas fotografias de la respuesta publica
```

### US-07 - Desactivar finca `P2`

**Como** productor o administrador,  
**quiero** desactivar una finca que ya no estara disponible,  
**para que** no se creen nuevos lotes ni experiencias sobre ella sin borrar su historial.

**Por que P2**: Permite control operativo manteniendo integridad historica.

**Test independiente**: Una finca desactivada no permite crear nuevos lotes, pero sus lotes historicos siguen consultables.

#### Escenario 1 - Desactivacion exitosa

```gherkin
Given  que existe una finca ACTIVA del productor autenticado
When   solicita cambiar su estado a INACTIVA
Then   el sistema actualiza el estado
And    bloquea la creacion de nuevos lotes en esa finca
And    conserva referencias historicas
And    registra auditoria de cambio de estado
```

#### Escenario 2 - Reactivacion de finca

```gherkin
Given  que existe una finca INACTIVA del productor autenticado
When   solicita cambiar su estado a ACTIVA
Then   el sistema permite nuevamente crear lotes
And    registra auditoria de reactivacion
```

---

## 5. Edge cases

| # | Caso | Comportamiento esperado |
| --- | --- | --- |
| 1 | Productor intenta crear finca sin perfil productor | Se rechaza con error indicando completar perfil productor. |
| 2 | Productor inactivo intenta crear finca | Se rechaza porque el perfil productor no esta activo. |
| 3 | Nombre duplicado de finca para el mismo productor | Se permite con advertencia futura; en backend MVP no es bloqueo duro salvo decision contraria. |
| 4 | Area de finca menor que suma de lotes existentes | Se rechaza la actualizacion de area. |
| 5 | Coordenadas parciales | Si se envia latitud, debe enviarse longitud; si se envia longitud, debe enviarse latitud. |
| 6 | Fotografia marcada como portada duplicada | El backend garantiza una sola portada por finca. |
| 7 | Finca desactivada con lotes publicados | Los pasaportes existentes siguen resolviendo la finca como origen historico. |
| 8 | Eliminacion fisica solicitada | No se permite eliminacion fisica si hay relaciones; se usa desactivacion logica. |
| 9 | Municipio fuera del catalogo soportado | Se rechaza la solicitud. |
| 10 | Texto malicioso en descripcion o historia | El backend sanitiza y el frontend debe renderizar como texto. |

---

## 6. Requerimientos funcionales

| ID | Descripcion | US relacionada | Prioridad |
| --- | --- | --- | --- |
| FR-001 | El sistema DEBE permitir crear fincas a productores con perfil `ACTIVO`. | US-01 | P1 |
| FR-002 | El sistema DEBE rechazar creacion de fincas si el usuario no tiene perfil productor. | US-01 | P1 |
| FR-003 | El sistema DEBE validar que `area_hectareas` sea mayor que 0. | US-01 | P1 |
| FR-004 | El sistema DEBE permitir adjuntar fotografias a fincas propias. | US-02 | P1 |
| FR-005 | El sistema DEBE garantizar una sola fotografia de portada por finca. | US-02 | P1 |
| FR-006 | El sistema DEBE listar solo las fincas del productor autenticado. | US-03 | P1 |
| FR-007 | El sistema DEBE permitir consultar el detalle de una finca propia. | US-05 | P1 |
| FR-008 | El sistema DEBE permitir actualizar campos descriptivos y de ubicacion de una finca propia. | US-04 | P2 |
| FR-009 | El sistema DEBE impedir que el area de finca sea menor que el area total de sus lotes. | US-04 | P2 |
| FR-010 | El sistema DEBE exponer una vista publica de finca sin direccion exacta por defecto. | US-06 | P1 |
| FR-011 | El sistema DEBE permitir desactivar y reactivar fincas sin eliminacion fisica. | US-07 | P2 |
| FR-012 | El sistema DEBE impedir crear nuevos lotes o experiencias sobre fincas `INACTIVA`. | US-07 | P2 |
| FR-013 | El sistema DEBE registrar auditoria de creacion, actualizacion, fotografias y cambios de estado. | Todas | P2 |

---

## 7. Requerimientos no funcionales

### 7.1 Rendimiento

| ID | Descripcion |
| --- | --- |
| RNF-001 | La creacion de finca debe completarse en menos de 600 ms p95 sin carga binaria sincrona. |
| RNF-002 | El listado de fincas propias debe completarse en menos de 400 ms p95 para hasta 500 fincas por productor. |
| RNF-003 | La consulta publica de finca para pasaporte debe completarse en menos de 250 ms p95. |

### 7.2 Seguridad

| ID | Descripcion |
| --- | --- |
| RNF-004 | Solo productores autenticados pueden crear o modificar sus propias fincas. |
| RNF-005 | Productores no pueden consultar detalle privado de fincas ajenas. |
| RNF-006 | La respuesta publica no debe exponer direccion exacta ni campos internos por defecto. |
| RNF-007 | Todos los campos de texto deben validarse y sanitizarse. |

### 7.3 Consistencia de datos

| ID | Descripcion |
| --- | --- |
| RNF-008 | Toda finca debe estar asociada a un `ProducerProfile` existente. |
| RNF-009 | La desactivacion de finca no debe eliminar lotes, evidencias, certificaciones ni pasaportes asociados. |
| RNF-010 | La actualizacion de portada debe ser atomica para evitar multiples portadas. |
| RNF-011 | La validacion de area contra lotes debe usar una lectura consistente de la suma de areas. |

### 7.4 Observabilidad

| ID | Descripcion |
| --- | --- |
| RNF-012 | Toda operacion de escritura sobre finca debe registrar auditoria con usuario actor, timestamp y resultado. |
| RNF-013 | Los rechazos por permisos o estado inactivo deben ser medibles para diagnosticar problemas de flujo. |

---

## 8. Criterios de exito

| ID | Criterio | Forma de medicion |
| --- | --- | --- |
| SC-001 | Un productor activo puede registrar una finca valida. | Test de integracion de `POST /farms`. |
| SC-002 | Un usuario sin perfil productor no puede registrar fincas. | Test de autorizacion y precondicion de dominio. |
| SC-003 | Un productor solo lista y consulta sus propias fincas privadas. | Test de aislamiento por propietario. |
| SC-004 | La vista publica de finca no retorna direccion exacta por defecto. | Test de serializacion publica. |
| SC-005 | Solo una fotografia puede quedar como portada por finca. | Test de integracion de fotografias. |
| SC-006 | Una finca inactiva no permite crear nuevos lotes. | Test de integracion con modulo de lotes. |

---

## 9. Contratos API sugeridos

### 9.1 Crear finca

`POST /farms`

#### Headers

```http
Authorization: Bearer access-token
```

#### Request

```json
{
  "nombre": "Finca Sierra Verde",
  "descripcion": "Finca productora de cacao y cafe.",
  "departamento": "Magdalena",
  "municipio": "Santa Marta",
  "vereda": "Minca",
  "direccion": "Via principal Minca",
  "latitud": 11.143,
  "longitud": -74.119,
  "area_hectareas": 12.5,
  "altitud_msnm": 650,
  "historia": "Finca familiar dedicada a agricultura sostenible."
}
```

#### Response `201`

```json
{
  "id": "uuid",
  "producer_id": "uuid",
  "nombre": "Finca Sierra Verde",
  "municipio": "Santa Marta",
  "area_hectareas": 12.5,
  "estado": "ACTIVA"
}
```

### 9.2 Listar mis fincas

`GET /farms`

#### Headers

```http
Authorization: Bearer access-token
```

#### Response `200`

```json
[
  {
    "id": "uuid",
    "nombre": "Finca Sierra Verde",
    "municipio": "Santa Marta",
    "area_hectareas": 12.5,
    "estado": "ACTIVA",
    "portada_url": "https://storage.example.com/farms/photo.jpg"
  }
]
```

### 9.3 Consultar detalle de finca

`GET /farms/{farmId}`

#### Headers

```http
Authorization: Bearer access-token
```

#### Response `200`

```json
{
  "id": "uuid",
  "nombre": "Finca Sierra Verde",
  "descripcion": "Finca productora de cacao y cafe.",
  "departamento": "Magdalena",
  "municipio": "Santa Marta",
  "vereda": "Minca",
  "direccion": "Via principal Minca",
  "latitud": 11.143,
  "longitud": -74.119,
  "area_hectareas": 12.5,
  "altitud_msnm": 650,
  "historia": "Finca familiar dedicada a agricultura sostenible.",
  "estado": "ACTIVA",
  "fotografias": []
}
```

### 9.4 Actualizar finca

`PATCH /farms/{farmId}`

#### Headers

```http
Authorization: Bearer access-token
```

#### Request

```json
{
  "descripcion": "Finca productora de cacao certificado.",
  "historia": "Tres generaciones dedicadas al cultivo sostenible.",
  "area_hectareas": 13.2
}
```

#### Response `200`

```json
{
  "id": "uuid",
  "descripcion": "Finca productora de cacao certificado.",
  "area_hectareas": 13.2,
  "actualizado_en": "2026-05-27T08:00:00.000Z"
}
```

### 9.5 Adjuntar fotografia

`POST /farms/{farmId}/photos`

#### Headers

```http
Authorization: Bearer access-token
```

#### Request

```json
{
  "url": "https://storage.example.com/farms/photo.jpg",
  "descripcion": "Vista principal de la finca",
  "es_portada": true,
  "visible_publicamente": true
}
```

#### Response `201`

```json
{
  "id": "uuid",
  "farm_id": "uuid",
  "url": "https://storage.example.com/farms/photo.jpg",
  "es_portada": true,
  "visible_publicamente": true
}
```

### 9.6 Consultar informacion publica de finca

`GET /public/farms/{farmId}`

#### Response `200`

```json
{
  "id": "uuid",
  "nombre": "Finca Sierra Verde",
  "descripcion": "Finca productora de cacao certificado.",
  "departamento": "Magdalena",
  "municipio": "Santa Marta",
  "vereda": "Minca",
  "area_hectareas": 12.5,
  "historia": "Tres generaciones dedicadas al cultivo sostenible.",
  "fotografias": [
    {
      "url": "https://storage.example.com/farms/photo.jpg",
      "descripcion": "Vista principal de la finca"
    }
  ]
}
```

### 9.7 Cambiar estado de finca

`PATCH /farms/{farmId}/status`

#### Headers

```http
Authorization: Bearer access-token
```

#### Request

```json
{
  "estado": "INACTIVA",
  "motivo": "Finca no disponible temporalmente"
}
```

#### Response `200`

```json
{
  "id": "uuid",
  "estado": "INACTIVA"
}
```

---

## 10. Fuera de alcance

Los siguientes puntos quedan excluidos de esta feature inicial y deben especificarse por separado:

- Carga binaria directa de imagenes.
- Integracion con mapas o geocodificacion externa.
- Validacion oficial de direccion.
- Poligonos geograficos o delimitacion cartografica de finca.
- Gestion de multiples propietarios por finca.
- Transferencia de propiedad de finca entre productores.
- Importacion masiva de fincas.
- Certificaciones a nivel de finca, cubiertas en una spec separada.
- Experiencias turisticas asociadas a finca, cubiertas en una spec separada.

---

## Historial de cambios

| Version | Fecha | Autor | Descripcion |
| --- | --- | --- | --- |
| 1.0.0 | 27/05/2026 | Equipo AgroTrace | Version inicial de la especificacion de fincas. |
