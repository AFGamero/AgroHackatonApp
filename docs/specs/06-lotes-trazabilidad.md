# Feature Specification: Lotes y Trazabilidad Agricola

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

El lote es la unidad productiva principal de AgroTrace Magdalena. Sobre un lote se registran el cultivo, variedad, fecha de siembra, estados de avance, evidencias y certificaciones que luego alimentan el pasaporte digital verificable mediante QR.

Esta feature define la gestion backend de lotes y trazabilidad agricola inicial. La trazabilidad no debe depender solo del estado actual: cada cambio relevante debe quedar registrado como evento historico para permitir consulta publica, auditoria y confianza comercial.

El objetivo del MVP es permitir que un productor activo cree lotes dentro de sus fincas activas, registre estados de cultivo, adjunte evidencias y consulte el historial completo del lote.

---

## 2. Actores

| ID | Actor | Tipo | Descripcion |
| --- | --- | --- | --- |
| ACT-01 | Productor | Primario | Crea lotes, actualiza estados de cultivo y adjunta evidencias de sus lotes. |
| ACT-02 | Administrador | Primario | Consulta lotes, audita cambios y puede intervenir estados por soporte. |
| ACT-03 | Sistema de Pasaporte Digital | Secundario | Consume informacion consolidada del lote para vista publica y QR. |
| ACT-04 | Sistema de Certificaciones | Secundario | Asocia certificaciones a lotes activos o historicos. |
| ACT-05 | Comprador Internacional | Secundario | Consulta trazabilidad publica del lote desde pasaporte digital. |
| ACT-06 | Turista | Secundario | Consulta historia, evidencias y estado del cultivo desde QR o pasaporte. |

> Un productor solo puede gestionar lotes pertenecientes a fincas asociadas a su propio perfil productor.

---

## 3. Entidades y modelo de datos

### 3.1 Lote (`Lot`)

Representa una unidad productiva dentro de una finca.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `farm_id` | `UUID (FK)` | Debe apuntar a una finca `ACTIVA` del productor. | Si |
| `codigo` | `String` | Maximo 40 caracteres. Unico por finca. Normalizado. | Si |
| `nombre` | `String` | Minimo 3 caracteres, maximo 120. | Si |
| `area_hectareas` | `Decimal` | Mayor que 0. Maximo 2 decimales. | Si |
| `cultivo` | `String` | Maximo 80 caracteres. Ejemplo: cacao, cafe, banano. | Si |
| `variedad` | `String` | Maximo 120 caracteres. | No |
| `fecha_siembra` | `Date` | No puede ser futura. | Si |
| `estado_actual` | `Enum` | Ultimo estado valido registrado. Puede iniciar como `SIN_ESTADO`. | Si |
| `descripcion` | `String` | Maximo 800 caracteres. | No |
| `estado` | `Enum` | Valores definidos en `LotStatus`. Default `ACTIVO`. | Si |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. Inmutable. | Si |
| `actualizado_en` | `Timestamp` | Actualizado automaticamente. UTC. | Si |

### 3.2 Evento de Estado de Cultivo (`CropStatusEvent`)

Representa un cambio historico en el desarrollo del cultivo.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `lot_id` | `UUID (FK)` | Lote asociado. | Si |
| `estado_cultivo` | `Enum` | Valores definidos en `CropStatus`. | Si |
| `fecha_evento` | `Date` | No puede ser futura. | Si |
| `observaciones` | `String` | Maximo 1000 caracteres. | No |
| `registrado_por` | `UUID (FK)` | Usuario que registro el evento. | Si |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. Inmutable. | Si |

### 3.3 Evidencia (`Evidence`)

Representa una prueba visual o textual asociada al lote o a un evento de cultivo.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `lot_id` | `UUID (FK)` | Lote asociado. | Si |
| `crop_status_event_id` | `UUID (FK)` | Evento asociado. Opcional. | No |
| `tipo` | `Enum` | Valores definidos en `EvidenceType`. | Si |
| `url` | `String` | Requerido para `FOTO` y `VIDEO`. URL valida. | No |
| `comentario` | `String` | Requerido para `COMENTARIO`. Maximo 1000 caracteres. | No |
| `descripcion` | `String` | Maximo 255 caracteres. | No |
| `visible_publicamente` | `Boolean` | Default `true`. Controla exposicion en pasaporte. | Si |
| `registrado_por` | `UUID (FK)` | Usuario que registro la evidencia. | Si |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. Inmutable. | Si |

### 3.4 Enumeraciones

#### `CropStatus`

- `SIEMBRA`
- `CRECIMIENTO`
- `FLORACION`
- `FRUCTIFICACION`
- `MADURACION`
- `COSECHA`

#### `LotStatus`

- `ACTIVO`
- `INACTIVO`
- `ARCHIVADO`

#### `EvidenceType`

- `FOTO`
- `VIDEO`
- `COMENTARIO`

### 3.5 Relaciones

```text
Farm (1) ---- (N) Lot
Lot (1) ---- (N) CropStatusEvent
Lot (1) ---- (N) Evidence
CropStatusEvent (1) ---- (N) Evidence
Lot (1) ---- (N) Certification
Lot (1) ---- (0..1) DigitalPassport
Lot (1) ---- (N) AuditLog
```

### 3.6 Datos publicos vs privados

| Campo | Uso interno | Visible en pasaporte digital |
| --- | :---: | :---: |
| `codigo` | Si | Si |
| `nombre` | Si | Si |
| `area_hectareas` | Si | Si |
| `cultivo` | Si | Si |
| `variedad` | Si | Si |
| `fecha_siembra` | Si | Si |
| `estado_actual` | Si | Si |
| `historial de estados` | Si | Si |
| `evidencias visibles` | Si | Si |
| `registrado_por` | Si | No |

---

## 4. User stories y escenarios de aceptacion

### US-01 - Crear lote `P1`

**Como** productor activo,  
**quiero** crear un lote dentro de una finca propia,  
**para que** pueda iniciar trazabilidad agricola sobre una unidad productiva especifica.

**Por que P1**: Sin lote no existe unidad sobre la cual generar pasaporte digital ni QR.

**Test independiente**: Un productor con finca activa envia datos validos. El test es exitoso si el lote queda creado y asociado a la finca.

#### Escenario 1 - Creacion exitosa

```gherkin
Given  que existe un productor ACTIVO autenticado
And    tiene una finca ACTIVA
When   envia codigo, nombre, area_hectareas, cultivo, variedad opcional y fecha_siembra
Then   el sistema crea el lote en estado ACTIVO
And    lo asocia a la finca indicada
And    establece estado_actual como SIN_ESTADO
And    registra auditoria de creacion
```

#### Escenario 2 - Codigo duplicado en la misma finca

```gherkin
Given  que existe un lote con codigo LOTE-001 dentro de una finca
When   el productor intenta crear otro lote con codigo LOTE-001 en la misma finca
Then   el sistema rechaza la solicitud con error de conflicto
And    no crea un nuevo lote
```

#### Escenario 3 - Area excede capacidad de finca

```gherkin
Given  que una finca tiene area total de 10 hectareas
And    ya existen lotes que suman 9 hectareas
When   el productor intenta crear un lote de 2 hectareas
Then   el sistema rechaza la solicitud
And    indica que el area total de lotes no puede superar el area de la finca
```

### US-02 - Listar lotes de una finca `P1`

**Como** productor,  
**quiero** listar los lotes de una finca propia,  
**para que** pueda administrarlos y actualizar su trazabilidad.

**Por que P1**: El productor necesita seleccionar el lote correcto antes de registrar estados o evidencias.

**Test independiente**: Un productor consulta lotes de una finca propia. El test es exitoso si solo recibe lotes de esa finca.

#### Escenario 1 - Listado exitoso

```gherkin
Given  que existe una finca propia con lotes registrados
When   el productor consulta sus lotes
Then   el sistema retorna codigo, nombre, cultivo, variedad, area, estado_actual y estado
```

#### Escenario 2 - Finca ajena

```gherkin
Given  que existe una finca de otro productor
When   el productor autenticado intenta listar sus lotes
Then   el sistema rechaza la solicitud con 403 o 404 segun politica de seguridad
```

### US-03 - Registrar estado de cultivo `P1`

**Como** productor,  
**quiero** registrar el avance del cultivo de un lote,  
**para que** el historial agricola quede documentado y consultable.

**Por que P1**: Los estados de cultivo son el centro de la trazabilidad agricola.

**Test independiente**: El productor registra `CRECIMIENTO` en un lote propio. El test es exitoso si se crea un evento y el lote actualiza `estado_actual`.

#### Escenario 1 - Estado registrado correctamente

```gherkin
Given  que existe un lote ACTIVO de una finca propia
When   el productor registra estado_cultivo CRECIMIENTO con fecha_evento valida
Then   el sistema crea un CropStatusEvent
And    actualiza estado_actual del lote a CRECIMIENTO
And    registra auditoria de cambio de estado
```

#### Escenario 2 - Estado no soportado

```gherkin
Given  que existe un lote ACTIVO
When   el productor envia un estado_cultivo que no pertenece al catalogo
Then   el sistema rechaza la solicitud
And    no actualiza estado_actual
```

#### Escenario 3 - Fecha futura

```gherkin
Given  que existe un lote ACTIVO
When   el productor registra un estado con fecha_evento futura
Then   el sistema rechaza la solicitud
And    indica que la fecha del evento no puede ser futura
```

### US-04 - Adjuntar evidencia al lote `P1`

**Como** productor,  
**quiero** adjuntar evidencias fotograficas, videos o comentarios,  
**para que** el estado del cultivo tenga respaldo visual o descriptivo.

**Por que P1**: Las evidencias aumentan confianza en el pasaporte digital y en solicitudes comerciales.

**Test independiente**: El productor adjunta una foto visible a un lote propio. El test es exitoso si aparece en el historial publico del lote.

#### Escenario 1 - Foto agregada correctamente

```gherkin
Given  que existe un lote ACTIVO de una finca propia
When   el productor adjunta evidencia tipo FOTO con URL valida
Then   el sistema crea la evidencia
And    la asocia al lote
And    la marca visible_publicamente segun el request
And    registra auditoria de evidencia agregada
```

#### Escenario 2 - Comentario sin texto

```gherkin
Given  que el productor registra evidencia tipo COMENTARIO
When   no envia comentario
Then   el sistema rechaza la solicitud
And    indica que comentario es requerido para evidencia tipo COMENTARIO
```

#### Escenario 3 - Foto sin URL

```gherkin
Given  que el productor registra evidencia tipo FOTO
When   no envia url
Then   el sistema rechaza la solicitud
And    indica que url es requerida para evidencias de archivo
```

### US-05 - Consultar historial de trazabilidad `P1`

**Como** productor,  
**quiero** consultar el historial completo de un lote,  
**para que** pueda revisar estados, evidencias y secuencia productiva.

**Por que P1**: El historial es necesario para auditoria interna y para alimentar el pasaporte.

**Test independiente**: Un productor consulta el historial de un lote propio. El test es exitoso si recibe estados ordenados y evidencias relacionadas.

#### Escenario 1 - Historial completo

```gherkin
Given  que existe un lote con estados y evidencias
When   el productor consulta su trazabilidad
Then   el sistema retorna datos del lote
And    retorna eventos de estado ordenados por fecha_evento ascendente
And    retorna evidencias asociadas
```

#### Escenario 2 - Lote sin eventos

```gherkin
Given  que existe un lote recien creado sin eventos
When   el productor consulta su trazabilidad
Then   el sistema retorna el lote
And    retorna historial vacio
And    estado_actual permanece SIN_ESTADO
```

### US-06 - Consultar trazabilidad publica del lote `P1`

**Como** turista o comprador,  
**quiero** consultar la trazabilidad publica de un lote,  
**para que** pueda verificar origen, cultivo, estado actual y evidencias visibles.

**Por que P1**: Esta informacion alimenta el pasaporte digital verificable por QR.

**Test independiente**: El pasaporte digital solicita la trazabilidad publica de un lote publicado. El test es exitoso si solo retorna informacion permitida y evidencias visibles.

#### Escenario 1 - Trazabilidad publica disponible

```gherkin
Given  que existe un lote asociado a un pasaporte publicado
When   se consulta su trazabilidad publica
Then   el sistema retorna codigo, nombre, cultivo, variedad, fecha_siembra, estado_actual e historial
And    retorna solo evidencias con visible_publicamente en true
And    no retorna IDs internos de usuarios ni datos privados
```

#### Escenario 2 - Evidencias ocultas

```gherkin
Given  que un lote tiene evidencias visibles y no visibles
When   se consulta la trazabilidad publica
Then   el sistema excluye evidencias no visibles
```

### US-07 - Actualizar lote `P2`

**Como** productor,  
**quiero** actualizar datos descriptivos de un lote,  
**para que** pueda corregir o mantener informacion basica sin perder historial.

**Por que P2**: Mejora mantenimiento de datos, pero el MVP puede iniciar con creacion y eventos.

**Test independiente**: El productor actualiza descripcion y variedad. El test es exitoso si se persisten cambios y no se borra historial.

#### Escenario 1 - Actualizacion descriptiva

```gherkin
Given  que existe un lote ACTIVO propio
When   el productor actualiza nombre, variedad o descripcion
Then   el sistema persiste los cambios
And    conserva todo el historial de estados y evidencias
And    registra auditoria con campos modificados
```

#### Escenario 2 - Reducir area con restricciones

```gherkin
Given  que existe un lote asociado a certificaciones o pasaporte publicado
When   el productor intenta modificar area_hectareas
Then   el sistema permite el cambio solo si no viola reglas de consistencia definidas
And    registra auditoria del cambio de area
```

### US-08 - Desactivar lote `P2`

**Como** productor o administrador,  
**quiero** desactivar un lote que ya no esta en produccion,  
**para que** no reciba nuevos eventos sin borrar la trazabilidad historica.

**Por que P2**: Permite control del ciclo de vida productivo.

**Test independiente**: Un lote desactivado no acepta nuevos estados, pero su historial sigue consultable.

#### Escenario 1 - Desactivacion exitosa

```gherkin
Given  que existe un lote ACTIVO propio
When   el productor cambia su estado a INACTIVO
Then   el sistema actualiza el estado del lote
And    bloquea nuevos estados de cultivo y evidencias operativas
And    conserva historial y pasaporte si existe
```

#### Escenario 2 - Registrar evento en lote inactivo

```gherkin
Given  que existe un lote INACTIVO
When   el productor intenta registrar un nuevo estado de cultivo
Then   el sistema rechaza la solicitud
And    indica que el lote no esta activo
```

---

## 5. Edge cases

| # | Caso | Comportamiento esperado |
| --- | --- | --- |
| 1 | Dos lotes con mismo codigo en una finca | Se rechaza por indice unico compuesto `farm_id` + `codigo`. |
| 2 | Mismo codigo en fincas distintas | Se permite porque la unicidad es por finca. |
| 3 | Area total de lotes supera area de finca | Se rechaza creacion o actualizacion del lote. |
| 4 | Fecha de siembra futura | Se rechaza la creacion o actualizacion. |
| 5 | Evento de estado con fecha anterior a fecha de siembra | Se rechaza salvo decision explicita de registrar correcciones historicas. |
| 6 | Evidencia tipo FOTO sin URL | Se rechaza por validacion de tipo. |
| 7 | Evidencia tipo COMENTARIO con URL pero sin comentario | Se rechaza si falta comentario requerido. |
| 8 | Productor intenta gestionar lote de finca ajena | Se rechaza con 403 o 404 segun politica de seguridad. |
| 9 | Lote inactivo con pasaporte publicado | El pasaporte sigue visible como informacion historica si esta publicado. |
| 10 | Estados registrados fuera de orden cronologico | El historial se ordena por `fecha_evento`; `estado_actual` debe calcularse con el evento mas reciente valido. |
| 11 | Dos estados con misma fecha | Se ordenan por `fecha_evento` y `creado_en`; el ultimo creado puede definir estado actual. |
| 12 | Archivo malicioso en evidencia | La carga binaria queda fuera de alcance; URLs deben validarse y renderizarse de forma segura. |

---

## 6. Requerimientos funcionales

| ID | Descripcion | US relacionada | Prioridad |
| --- | --- | --- | --- |
| FR-001 | El sistema DEBE permitir crear lotes dentro de fincas activas propias. | US-01 | P1 |
| FR-002 | El sistema DEBE validar codigo unico por finca. | US-01 | P1 |
| FR-003 | El sistema DEBE validar que el area total de lotes no supere el area de la finca. | US-01 | P1 |
| FR-004 | El sistema DEBE listar lotes por finca propia. | US-02 | P1 |
| FR-005 | El sistema DEBE registrar eventos de estado de cultivo sobre lotes activos. | US-03 | P1 |
| FR-006 | El sistema DEBE actualizar `estado_actual` al registrar un evento valido. | US-03 | P1 |
| FR-007 | El sistema DEBE rechazar estados de cultivo fuera del catalogo. | US-03 | P1 |
| FR-008 | El sistema DEBE permitir adjuntar evidencias a lotes propios. | US-04 | P1 |
| FR-009 | El sistema DEBE validar campos requeridos segun tipo de evidencia. | US-04 | P1 |
| FR-010 | El sistema DEBE permitir consultar historial privado completo de trazabilidad. | US-05 | P1 |
| FR-011 | El sistema DEBE exponer trazabilidad publica sin datos privados ni evidencias ocultas. | US-06 | P1 |
| FR-012 | El sistema DEBE permitir actualizar datos descriptivos del lote sin borrar historial. | US-07 | P2 |
| FR-013 | El sistema DEBE permitir desactivar lotes sin eliminacion fisica. | US-08 | P2 |
| FR-014 | El sistema DEBE registrar auditoria de creacion, actualizacion, eventos, evidencias y cambios de estado. | Todas | P2 |

---

## 7. Requerimientos no funcionales

### 7.1 Rendimiento

| ID | Descripcion |
| --- | --- |
| RNF-001 | La creacion de lote debe completarse en menos de 600 ms p95. |
| RNF-002 | El registro de estado de cultivo debe completarse en menos de 500 ms p95. |
| RNF-003 | La consulta de trazabilidad privada debe completarse en menos de 700 ms p95 para hasta 500 eventos y evidencias. |
| RNF-004 | La consulta publica para pasaporte debe completarse en menos de 400 ms p95. |

### 7.2 Seguridad

| ID | Descripcion |
| --- | --- |
| RNF-005 | Solo productores autenticados pueden crear y modificar lotes propios. |
| RNF-006 | Administradores pueden consultar lotes para soporte y auditoria. |
| RNF-007 | La trazabilidad publica no debe exponer `registrado_por`, datos internos ni evidencias ocultas. |
| RNF-008 | Todos los campos de texto deben validarse y sanitizarse. |

### 7.3 Consistencia de datos

| ID | Descripcion |
| --- | --- |
| RNF-009 | La unicidad de codigo por finca debe reforzarse con indice unico. |
| RNF-010 | La creacion de evento y actualizacion de `estado_actual` debe ser atomica. |
| RNF-011 | La creacion de evidencia asociada a evento debe validar que evento y lote correspondan. |
| RNF-012 | La desactivacion de lote no debe eliminar eventos, evidencias, certificaciones ni pasaportes. |
| RNF-013 | El calculo de area total de lotes debe evitar condiciones de carrera en creaciones paralelas. |

### 7.4 Observabilidad

| ID | Descripcion |
| --- | --- |
| RNF-014 | Toda operacion de escritura sobre lotes, eventos y evidencias debe registrar auditoria. |
| RNF-015 | Los rechazos por area, propiedad o estado inactivo deben ser medibles. |
| RNF-016 | Debe poder medirse cantidad de eventos y evidencias por lote para monitorear crecimiento. |

---

## 8. Criterios de exito

| ID | Criterio | Forma de medicion |
| --- | --- | --- |
| SC-001 | Un productor puede crear un lote valido dentro de una finca propia activa. | Test de integracion de `POST /farms/{farmId}/lots`. |
| SC-002 | No se pueden duplicar codigos de lote dentro de una misma finca. | Test de integracion + constraint de base de datos. |
| SC-003 | No se puede superar el area disponible de la finca. | Test de regla de dominio sobre areas. |
| SC-004 | Registrar un estado crea historial y actualiza `estado_actual`. | Test de integracion de `POST /lots/{lotId}/status-events`. |
| SC-005 | Las evidencias visibles aparecen en trazabilidad publica y las ocultas no. | Test de serializacion publica. |
| SC-006 | Un productor no puede gestionar lotes de fincas ajenas. | Test de aislamiento por propietario. |
| SC-007 | Un lote inactivo no acepta nuevos estados de cultivo. | Test de estado de ciclo de vida. |

---

## 9. Contratos API sugeridos

### 9.1 Crear lote

`POST /farms/{farmId}/lots`

#### Headers

```http
Authorization: Bearer access-token
```

#### Request

```json
{
  "codigo": "LOTE-001",
  "nombre": "Cacao Norte",
  "area_hectareas": 2.5,
  "cultivo": "Cacao",
  "variedad": "Criollo",
  "fecha_siembra": "2026-02-15",
  "descripcion": "Lote principal de cacao criollo."
}
```

#### Response `201`

```json
{
  "id": "uuid",
  "farm_id": "uuid",
  "codigo": "LOTE-001",
  "nombre": "Cacao Norte",
  "cultivo": "Cacao",
  "variedad": "Criollo",
  "area_hectareas": 2.5,
  "estado_actual": "SIN_ESTADO",
  "estado": "ACTIVO"
}
```

### 9.2 Listar lotes de finca

`GET /farms/{farmId}/lots`

#### Headers

```http
Authorization: Bearer access-token
```

#### Response `200`

```json
[
  {
    "id": "uuid",
    "codigo": "LOTE-001",
    "nombre": "Cacao Norte",
    "cultivo": "Cacao",
    "variedad": "Criollo",
    "area_hectareas": 2.5,
    "estado_actual": "CRECIMIENTO",
    "estado": "ACTIVO"
  }
]
```

### 9.3 Registrar estado de cultivo

`POST /lots/{lotId}/status-events`

#### Headers

```http
Authorization: Bearer access-token
```

#### Request

```json
{
  "estado_cultivo": "CRECIMIENTO",
  "fecha_evento": "2026-05-20",
  "observaciones": "El cultivo presenta buen desarrollo vegetativo."
}
```

#### Response `201`

```json
{
  "id": "uuid",
  "lot_id": "uuid",
  "estado_cultivo": "CRECIMIENTO",
  "fecha_evento": "2026-05-20",
  "observaciones": "El cultivo presenta buen desarrollo vegetativo."
}
```

### 9.4 Adjuntar evidencia

`POST /lots/{lotId}/evidence`

#### Headers

```http
Authorization: Bearer access-token
```

#### Request

```json
{
  "crop_status_event_id": "uuid",
  "tipo": "FOTO",
  "url": "https://storage.example.com/evidence/photo.jpg",
  "descripcion": "Fotografia del crecimiento del cultivo",
  "visible_publicamente": true
}
```

#### Response `201`

```json
{
  "id": "uuid",
  "lot_id": "uuid",
  "crop_status_event_id": "uuid",
  "tipo": "FOTO",
  "url": "https://storage.example.com/evidence/photo.jpg",
  "visible_publicamente": true
}
```

### 9.5 Consultar trazabilidad privada

`GET /lots/{lotId}/traceability`

#### Headers

```http
Authorization: Bearer access-token
```

#### Response `200`

```json
{
  "lot": {
    "id": "uuid",
    "codigo": "LOTE-001",
    "nombre": "Cacao Norte",
    "cultivo": "Cacao",
    "variedad": "Criollo",
    "fecha_siembra": "2026-02-15",
    "estado_actual": "CRECIMIENTO"
  },
  "status_events": [
    {
      "id": "uuid",
      "estado_cultivo": "CRECIMIENTO",
      "fecha_evento": "2026-05-20",
      "observaciones": "El cultivo presenta buen desarrollo vegetativo."
    }
  ],
  "evidence": [
    {
      "id": "uuid",
      "tipo": "FOTO",
      "url": "https://storage.example.com/evidence/photo.jpg",
      "descripcion": "Fotografia del crecimiento del cultivo",
      "visible_publicamente": true
    }
  ]
}
```

### 9.6 Consultar trazabilidad publica

`GET /public/lots/{lotId}/traceability`

#### Response `200`

```json
{
  "codigo": "LOTE-001",
  "nombre": "Cacao Norte",
  "cultivo": "Cacao",
  "variedad": "Criollo",
  "fecha_siembra": "2026-02-15",
  "estado_actual": "CRECIMIENTO",
  "status_events": [
    {
      "estado_cultivo": "CRECIMIENTO",
      "fecha_evento": "2026-05-20",
      "observaciones": "El cultivo presenta buen desarrollo vegetativo."
    }
  ],
  "evidence": [
    {
      "tipo": "FOTO",
      "url": "https://storage.example.com/evidence/photo.jpg",
      "descripcion": "Fotografia del crecimiento del cultivo"
    }
  ]
}
```

### 9.7 Cambiar estado del lote

`PATCH /lots/{lotId}/status`

#### Headers

```http
Authorization: Bearer access-token
```

#### Request

```json
{
  "estado": "INACTIVO",
  "motivo": "Fin del ciclo productivo"
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

- Carga binaria directa de archivos.
- Procesamiento de imagenes o videos.
- Validacion automatica de evidencias con IA.
- Certificaciones de lote, cubiertas en una spec separada.
- Pasaporte digital y QR, cubiertos en una spec separada.
- Exportacion logistica del producto.
- Georreferenciacion precisa del perimetro del lote.
- Registro de labores agronomicas detalladas como fertilizacion, riego o control fitosanitario.
- Flujo de aprobacion externa de evidencias.

---

## Historial de cambios

| Version | Fecha | Autor | Descripcion |
| --- | --- | --- | --- |
| 1.0.0 | 27/05/2026 | Equipo AgroTrace | Version inicial de la especificacion de lotes y trazabilidad agricola. |
