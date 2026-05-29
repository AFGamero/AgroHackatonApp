# Feature Specification: Certificaciones

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

Las certificaciones fortalecen la confianza comercial y turistica sobre los productos trazados en Nebbi. Un comprador internacional necesita verificar si una finca o lote cuenta con certificaciones como Fairtrade o Rainforest Alliance, y si estas se encuentran vigentes o pendientes de validacion.

Esta feature define el registro, consulta, validacion y exposicion publica de certificaciones asociadas a fincas o lotes. La certificacion puede ser cargada inicialmente por un productor y posteriormente validada por un certificador o administrador.

El objetivo del MVP es permitir registrar certificaciones con documento soporte, asociarlas a una finca o lote, controlar su vigencia y exponer solo certificaciones vigentes o validadas en el pasaporte digital.

---

## 2. Actores

| ID | Actor | Tipo | Descripcion |
| --- | --- | --- | --- |
| ACT-01 | Productor | Primario | Registra certificaciones asociadas a sus fincas o lotes. |
| ACT-02 | Certificador | Primario | Revisa documentos soporte y valida o rechaza certificaciones. |
| ACT-03 | Administrador | Primario | Puede auditar, corregir estado y supervisar certificaciones. |
| ACT-04 | Sistema de Pasaporte Digital | Secundario | Consume certificaciones publicables para mostrar en pasaporte. |
| ACT-05 | Comprador Internacional | Secundario | Consulta certificaciones publicas para evaluar confianza del producto. |
| ACT-06 | Turista | Secundario | Consulta certificaciones publicas desde QR o pasaporte. |

> En el MVP, si no existe rol certificador implementado, el rol `ADMIN` puede realizar validacion manual.

---

## 3. Entidades y modelo de datos

### 3.1 Certificacion (`Certification`)

Representa una certificacion asociada a una finca o lote.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `tipo` | `Enum` | Valores definidos en `CertificationType`. | Si |
| `alcance` | `Enum` | Valores definidos en `CertificationScope`. | Si |
| `farm_id` | `UUID (FK)` | Requerido si `alcance = FINCA`. | No |
| `lot_id` | `UUID (FK)` | Requerido si `alcance = LOTE`. | No |
| `entidad_certificadora` | `String` | Maximo 160 caracteres. | Si |
| `codigo_certificacion` | `String` | Maximo 80 caracteres. | No |
| `fecha_emision` | `Date` | No puede ser futura. | Si |
| `fecha_vencimiento` | `Date` | Debe ser posterior a `fecha_emision`. | Si |
| `documento_url` | `String` | URL valida del soporte documental. | Si |
| `estado` | `Enum` | Valores definidos en `CertificationStatus`. Default `PENDIENTE_VALIDACION`. | Si |
| `visible_publicamente` | `Boolean` | Default `false`; true solo si cumple reglas de publicacion. | Si |
| `registrado_por` | `UUID (FK)` | Usuario que registra la certificacion. | Si |
| `validado_por` | `UUID (FK)` | Usuario certificador o admin que valida/rechaza. | No |
| `validado_en` | `Timestamp` | UTC. Requerido cuando estado cambia a validado o rechazado. | No |
| `motivo_rechazo` | `String` | Maximo 500 caracteres. Requerido si estado `RECHAZADA`. | No |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. Inmutable. | Si |
| `actualizado_en` | `Timestamp` | Actualizado automaticamente. UTC. | Si |

### 3.2 Enumeraciones

#### `CertificationType`

- `FAIRTRADE`
- `RAINFOREST_ALLIANCE`
- `ORGANICA`
- `GLOBAL_GAP`
- `OTRA`

#### `CertificationScope`

- `FINCA`
- `LOTE`

#### `CertificationStatus`

- `PENDIENTE_VALIDACION`
- `VALIDADA`
- `RECHAZADA`
- `VENCIDA`
- `REVOCADA`

### 3.3 Relaciones

```text
Farm (1) ---- (N) Certification
Lot (1) ---- (N) Certification
User (1) ---- (N) Certification [via registrado_por]
User (1) ---- (N) Certification [via validado_por]
Certification (1) ---- (N) AuditLog
```

### 3.4 Reglas de alcance

| Alcance | Requiere | Prohibe |
| --- | --- | --- |
| `FINCA` | `farm_id` | `lot_id` |
| `LOTE` | `lot_id` | `farm_id` |

### 3.5 Datos publicos vs privados

| Campo | Uso interno | Visible en pasaporte digital |
| --- | :---: | :---: |
| `tipo` | Si | Si |
| `entidad_certificadora` | Si | Si |
| `codigo_certificacion` | Si | Si, si existe |
| `fecha_emision` | Si | Si |
| `fecha_vencimiento` | Si | Si |
| `estado` | Si | Si |
| `documento_url` | Si | Opcional, solo si se define como publico |
| `motivo_rechazo` | Si | No |
| `registrado_por` / `validado_por` | Si | No |

---

## 4. User stories y escenarios de aceptacion

### US-01 - Registrar certificacion de finca `P1`

**Como** productor,  
**quiero** registrar una certificacion asociada a una finca propia,  
**para que** pueda demostrar cumplimiento de estandares en el origen agricola.

**Por que P1**: Las certificaciones a nivel de finca son necesarias para confianza comercial y pasaporte digital.

**Test independiente**: Un productor registra una certificacion Fairtrade sobre una finca propia. El test es exitoso si queda en estado `PENDIENTE_VALIDACION`.

#### Escenario 1 - Registro exitoso

```gherkin
Given  que existe una finca ACTIVA del productor autenticado
When   registra tipo, entidad_certificadora, fechas y documento_url con alcance FINCA
Then   el sistema crea la certificacion en estado PENDIENTE_VALIDACION
And    la asocia a la finca
And    visible_publicamente queda en false
And    registra auditoria de creacion
```

#### Escenario 2 - Finca ajena

```gherkin
Given  que existe una finca de otro productor
When   el productor autenticado intenta registrar una certificacion sobre esa finca
Then   el sistema rechaza la solicitud con 403 o 404 segun politica de seguridad
And    no crea la certificacion
```

#### Escenario 3 - Fechas invalidas

```gherkin
Given  que el productor registra una certificacion
When   fecha_vencimiento es anterior o igual a fecha_emision
Then   el sistema rechaza la solicitud
And    indica que la fecha de vencimiento debe ser posterior a la fecha de emision
```

### US-02 - Registrar certificacion de lote `P1`

**Como** productor,  
**quiero** registrar una certificacion asociada a un lote propio,  
**para que** el pasaporte digital muestre certificaciones especificas del producto.

**Por que P1**: El lote es la unidad principal de trazabilidad y exportacion.

**Test independiente**: Un productor registra Rainforest Alliance sobre un lote propio. El test es exitoso si queda asociada al lote.

#### Escenario 1 - Registro exitoso

```gherkin
Given  que existe un lote ACTIVO de una finca propia
When   registra una certificacion con alcance LOTE
Then   el sistema crea la certificacion
And    la asocia al lote
And    registra auditoria de creacion
```

#### Escenario 2 - Alcance inconsistente

```gherkin
Given  que el productor registra una certificacion con alcance LOTE
When   envia farm_id en lugar de lot_id
Then   el sistema rechaza la solicitud
And    indica que alcance LOTE requiere lot_id
```

#### Escenario 3 - Lote inactivo

```gherkin
Given  que existe un lote INACTIVO
When   el productor intenta registrar una nueva certificacion
Then   el sistema rechaza la solicitud
And    indica que el lote no esta activo
```

### US-03 - Validar certificacion `P1`

**Como** certificador o administrador,  
**quiero** validar una certificacion registrada,  
**para que** pueda aparecer como confiable y vigente en el pasaporte digital.

**Por que P1**: Una certificacion cargada por productor no debe publicarse como validada sin revision.

**Test independiente**: Un admin valida una certificacion pendiente y vigente. El test es exitoso si cambia a `VALIDADA` y se vuelve publicable.

#### Escenario 1 - Validacion exitosa

```gherkin
Given  que existe una certificacion PENDIENTE_VALIDACION
And    fecha_vencimiento es futura
When   el certificador la valida
Then   el sistema cambia estado a VALIDADA
And    establece validado_por y validado_en
And    establece visible_publicamente en true
And    registra auditoria de validacion
```

#### Escenario 2 - Certificacion vencida al validar

```gherkin
Given  que existe una certificacion PENDIENTE_VALIDACION
And    fecha_vencimiento ya paso
When   el certificador intenta validarla
Then   el sistema rechaza la validacion
And    puede marcarla como VENCIDA
And    no la hace visible publicamente
```

#### Escenario 3 - Usuario sin permiso

```gherkin
Given  que existe una certificacion pendiente
When   un usuario PRODUCTOR intenta validarla
Then   el sistema rechaza la solicitud con 403
```

### US-04 - Rechazar certificacion `P1`

**Como** certificador o administrador,  
**quiero** rechazar una certificacion con motivo,  
**para que** el productor pueda corregir o cargar un soporte valido.

**Por que P1**: La validacion requiere salida negativa controlada y auditable.

**Test independiente**: Un admin rechaza una certificacion pendiente con motivo. El test es exitoso si queda `RECHAZADA` y no publicable.

#### Escenario 1 - Rechazo exitoso

```gherkin
Given  que existe una certificacion PENDIENTE_VALIDACION
When   el certificador la rechaza con motivo
Then   el sistema cambia estado a RECHAZADA
And    guarda motivo_rechazo
And    establece validado_por y validado_en
And    mantiene visible_publicamente en false
And    registra auditoria de rechazo
```

#### Escenario 2 - Rechazo sin motivo

```gherkin
Given  que existe una certificacion pendiente
When   el certificador intenta rechazarla sin motivo
Then   el sistema rechaza la solicitud
And    indica que motivo_rechazo es obligatorio
```

### US-05 - Consultar certificaciones privadas `P1`

**Como** productor,  
**quiero** consultar las certificaciones de mis fincas y lotes,  
**para que** pueda ver su estado de validacion y vigencia.

**Por que P1**: El productor necesita monitorear certificaciones pendientes, validadas o rechazadas.

**Test independiente**: Un productor consulta certificaciones de una finca propia. El test es exitoso si recibe todas sus certificaciones, incluyendo rechazadas y pendientes.

#### Escenario 1 - Consulta privada de finca

```gherkin
Given  que existe una finca propia con certificaciones registradas
When   el productor consulta sus certificaciones
Then   el sistema retorna certificaciones pendientes, validadas, rechazadas, vencidas y revocadas
And    incluye motivo_rechazo si existe
```

#### Escenario 2 - Consulta privada de lote

```gherkin
Given  que existe un lote propio con certificaciones
When   el productor consulta sus certificaciones
Then   el sistema retorna certificaciones asociadas al lote
```

### US-06 - Consultar certificaciones publicas para pasaporte `P1`

**Como** turista o comprador,  
**quiero** ver certificaciones publicas de una finca o lote,  
**para que** pueda verificar confianza y estandares del producto.

**Por que P1**: El pasaporte digital debe mostrar solo certificaciones publicables y vigentes.

**Test independiente**: El pasaporte consulta certificaciones de un lote. El test es exitoso si solo aparecen certificaciones `VALIDADA`, vigentes y visibles.

#### Escenario 1 - Certificaciones publicas disponibles

```gherkin
Given  que existe un lote con certificaciones VALIDADA vigentes y visibles
When   se consulta informacion publica para pasaporte
Then   el sistema retorna tipo, entidad_certificadora, codigo, fecha_emision, fecha_vencimiento y estado
And    no retorna motivo_rechazo ni usuarios internos
```

#### Escenario 2 - Certificaciones no publicables

```gherkin
Given  que existen certificaciones PENDIENTE_VALIDACION, RECHAZADA o VENCIDA
When   se consulta informacion publica
Then   el sistema las excluye de la respuesta publica
```

### US-07 - Marcar certificaciones vencidas `P2`

**Como** sistema,  
**quiero** marcar como vencidas las certificaciones cuya fecha_vencimiento ya paso,  
**para que** no aparezcan como vigentes en pasaportes digitales.

**Por que P2**: La vigencia puede calcularse en consulta, pero un job mejora consistencia operativa.

**Test independiente**: Una certificacion validada con fecha vencida se procesa. El test es exitoso si queda `VENCIDA` y no visible.

#### Escenario 1 - Vencimiento automatico

```gherkin
Given  que existe una certificacion VALIDADA con fecha_vencimiento pasada
When   el sistema ejecuta el proceso de vencimiento
Then   cambia estado a VENCIDA
And    establece visible_publicamente en false
And    registra auditoria del cambio automatico
```

---

## 5. Edge cases

| # | Caso | Comportamiento esperado |
| --- | --- | --- |
| 1 | Certificacion con `farm_id` y `lot_id` al mismo tiempo | Se rechaza por alcance ambiguo. |
| 2 | Certificacion sin `farm_id` ni `lot_id` | Se rechaza porque debe tener alcance concreto. |
| 3 | Fecha de emision futura | Se rechaza la solicitud. |
| 4 | Fecha de vencimiento igual a fecha de emision | Se rechaza; vencimiento debe ser posterior. |
| 5 | Productor intenta validar su propia certificacion | Se rechaza salvo que tenga rol certificador/admin. |
| 6 | Certificacion validada luego es editada por productor | Debe volver a `PENDIENTE_VALIDACION` si se modifican campos criticos o bloquearse edicion. |
| 7 | Documento soporte privado | El documento no se expone publicamente salvo configuracion explicita. |
| 8 | Certificacion vencida pero estado aun `VALIDADA` | La consulta publica debe excluirla aunque el job aun no la haya marcado. |
| 9 | Lote inactivo con certificacion validada | Puede seguir visible como informacion historica si el pasaporte esta publicado y la certificacion sigue vigente. |
| 10 | Duplicado de codigo de certificacion | No se bloquea globalmente en MVP; puede advertirse si coincide tipo, entidad y alcance. |

---

## 6. Requerimientos funcionales

| ID | Descripcion | US relacionada | Prioridad |
| --- | --- | --- | --- |
| FR-001 | El sistema DEBE permitir registrar certificaciones asociadas a fincas propias. | US-01 | P1 |
| FR-002 | El sistema DEBE permitir registrar certificaciones asociadas a lotes propios. | US-02 | P1 |
| FR-003 | El sistema DEBE validar consistencia entre `alcance`, `farm_id` y `lot_id`. | US-01, US-02 | P1 |
| FR-004 | El sistema DEBE crear certificaciones nuevas en estado `PENDIENTE_VALIDACION`. | US-01, US-02 | P1 |
| FR-005 | El sistema DEBE validar que fecha_vencimiento sea posterior a fecha_emision. | US-01, US-02 | P1 |
| FR-006 | El sistema DEBE permitir validar certificaciones a usuarios `CERTIFICADOR` o `ADMIN`. | US-03 | P1 |
| FR-007 | El sistema DEBE permitir rechazar certificaciones con motivo obligatorio. | US-04 | P1 |
| FR-008 | El sistema DEBE permitir consultar certificaciones privadas de fincas y lotes propios. | US-05 | P1 |
| FR-009 | El sistema DEBE exponer publicamente solo certificaciones validadas, vigentes y visibles. | US-06 | P1 |
| FR-010 | El sistema DEBE excluir certificaciones vencidas de respuestas publicas aunque su estado no haya sido actualizado. | US-06 | P1 |
| FR-011 | El sistema DEBE soportar proceso para marcar certificaciones vencidas. | US-07 | P2 |
| FR-012 | El sistema DEBE registrar auditoria de creacion, validacion, rechazo, vencimiento y revocacion. | Todas | P2 |

---

## 7. Requerimientos no funcionales

### 7.1 Rendimiento

| ID | Descripcion |
| --- | --- |
| RNF-001 | El registro de certificacion debe completarse en menos de 600 ms p95 sin carga binaria directa. |
| RNF-002 | La consulta publica de certificaciones para pasaporte debe completarse en menos de 250 ms p95. |
| RNF-003 | La consulta privada por finca o lote debe completarse en menos de 400 ms p95 para hasta 100 certificaciones. |

### 7.2 Seguridad

| ID | Descripcion |
| --- | --- |
| RNF-004 | Solo productores autenticados pueden registrar certificaciones sobre recursos propios. |
| RNF-005 | Solo `CERTIFICADOR` o `ADMIN` pueden validar, rechazar, revocar o corregir certificaciones. |
| RNF-006 | Motivos de rechazo y usuarios internos no deben aparecer en respuestas publicas. |
| RNF-007 | URLs de documentos soporte deben validarse y no deben permitir esquemas inseguros. |

### 7.3 Consistencia de datos

| ID | Descripcion |
| --- | --- |
| RNF-008 | La certificacion debe asociarse exactamente a una finca o a un lote, nunca a ambos. |
| RNF-009 | La validacion debe ser atomica: cambio de estado, `validado_por`, `validado_en` y visibilidad se actualizan juntos. |
| RNF-010 | La consulta publica debe recalcular vigencia con fecha actual del servidor. |
| RNF-011 | La revocacion o vencimiento no debe eliminar registros historicos. |

### 7.4 Observabilidad

| ID | Descripcion |
| --- | --- |
| RNF-012 | Toda operacion de escritura sobre certificaciones debe registrar auditoria con usuario actor, timestamp y resultado. |
| RNF-013 | Los rechazos por documentos vencidos o inconsistentes deben ser medibles. |
| RNF-014 | Debe poder medirse cantidad de certificaciones por estado y tipo. |

---

## 8. Criterios de exito

| ID | Criterio | Forma de medicion |
| --- | --- | --- |
| SC-001 | Un productor puede registrar certificacion sobre finca propia. | Test de integracion de `POST /farms/{farmId}/certifications`. |
| SC-002 | Un productor puede registrar certificacion sobre lote propio. | Test de integracion de `POST /lots/{lotId}/certifications`. |
| SC-003 | Una certificacion pendiente puede ser validada por `ADMIN` o `CERTIFICADOR`. | Test de autorizacion y cambio de estado. |
| SC-004 | Una certificacion rechazada exige motivo y no aparece publicamente. | Test de validacion y serializacion publica. |
| SC-005 | La respuesta publica solo incluye certificaciones validadas, vigentes y visibles. | Test de `GET /public/.../certifications`. |
| SC-006 | Certificaciones vencidas no aparecen en pasaporte aunque sigan con estado `VALIDADA`. | Test de vigencia por fecha del servidor. |
| SC-007 | Productores no pueden registrar certificaciones sobre recursos ajenos. | Test de aislamiento por propietario. |

---

## 9. Contratos API sugeridos

### 9.1 Registrar certificacion de finca

`POST /farms/{farmId}/certifications`

#### Headers

```http
Authorization: Bearer access-token
```

#### Request

```json
{
  "tipo": "FAIRTRADE",
  "entidad_certificadora": "Fairtrade International",
  "codigo_certificacion": "FT-CO-2026-001",
  "fecha_emision": "2026-01-15",
  "fecha_vencimiento": "2027-01-15",
  "documento_url": "https://storage.example.com/certifications/fairtrade.pdf"
}
```

#### Response `201`

```json
{
  "id": "uuid",
  "alcance": "FINCA",
  "farm_id": "uuid",
  "tipo": "FAIRTRADE",
  "estado": "PENDIENTE_VALIDACION",
  "visible_publicamente": false
}
```

### 9.2 Registrar certificacion de lote

`POST /lots/{lotId}/certifications`

#### Headers

```http
Authorization: Bearer access-token
```

#### Request

```json
{
  "tipo": "RAINFOREST_ALLIANCE",
  "entidad_certificadora": "Rainforest Alliance",
  "codigo_certificacion": "RA-2026-991",
  "fecha_emision": "2026-02-01",
  "fecha_vencimiento": "2027-02-01",
  "documento_url": "https://storage.example.com/certifications/rainforest.pdf"
}
```

#### Response `201`

```json
{
  "id": "uuid",
  "alcance": "LOTE",
  "lot_id": "uuid",
  "tipo": "RAINFOREST_ALLIANCE",
  "estado": "PENDIENTE_VALIDACION",
  "visible_publicamente": false
}
```

### 9.3 Validar certificacion

`PATCH /admin/certifications/{certificationId}/validate`

#### Headers

```http
Authorization: Bearer admin-or-certifier-access-token
```

#### Response `200`

```json
{
  "id": "uuid",
  "estado": "VALIDADA",
  "visible_publicamente": true,
  "validado_en": "2026-05-27T08:00:00.000Z"
}
```

### 9.4 Rechazar certificacion

`PATCH /admin/certifications/{certificationId}/reject`

#### Headers

```http
Authorization: Bearer admin-or-certifier-access-token
```

#### Request

```json
{
  "motivo_rechazo": "El documento soporte no corresponde al lote indicado."
}
```

#### Response `200`

```json
{
  "id": "uuid",
  "estado": "RECHAZADA",
  "visible_publicamente": false,
  "motivo_rechazo": "El documento soporte no corresponde al lote indicado."
}
```

### 9.5 Listar certificaciones privadas de finca

`GET /farms/{farmId}/certifications`

#### Headers

```http
Authorization: Bearer access-token
```

#### Response `200`

```json
[
  {
    "id": "uuid",
    "tipo": "FAIRTRADE",
    "entidad_certificadora": "Fairtrade International",
    "codigo_certificacion": "FT-CO-2026-001",
    "fecha_emision": "2026-01-15",
    "fecha_vencimiento": "2027-01-15",
    "estado": "VALIDADA",
    "visible_publicamente": true
  }
]
```

### 9.6 Listar certificaciones publicas de lote

`GET /public/lots/{lotId}/certifications`

#### Response `200`

```json
[
  {
    "tipo": "RAINFOREST_ALLIANCE",
    "entidad_certificadora": "Rainforest Alliance",
    "codigo_certificacion": "RA-2026-991",
    "fecha_emision": "2026-02-01",
    "fecha_vencimiento": "2027-02-01",
    "estado": "VALIDADA"
  }
]
```

---

## 10. Fuera de alcance

Los siguientes puntos quedan excluidos de esta feature inicial y deben especificarse por separado:

- Integracion directa con APIs de Fairtrade o Rainforest Alliance.
- Validacion automatica de documentos PDF.
- Firma digital o blockchain para certificados.
- Flujo completo de solicitud formal a entidades certificadoras.
- Gestion de renovaciones automaticas.
- Alertas por vencimiento proximamente.
- Versionado completo de documentos soporte.
- Carga binaria directa del documento soporte.
- Certificaciones por producto comercial publicado.

---

## Historial de cambios

| Version | Fecha | Autor | Descripcion |
| --- | --- | --- | --- |
| 1.0.0 | 27/05/2026 | Equipo Nebbi | Version inicial de la especificacion de certificaciones. |
