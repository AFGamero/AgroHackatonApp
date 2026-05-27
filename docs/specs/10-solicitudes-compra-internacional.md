# Feature Specification: Solicitudes de Compra Internacional

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

Las solicitudes de compra internacional convierten la trazabilidad publica en una oportunidad comercial. Un comprador puede consultar un pasaporte digital, validar origen, certificaciones y evidencias, y dejar una intencion de compra asociada a un lote o producto.

Esta feature define el flujo MVP para registrar solicitudes comerciales desde superficies publicas y permitir que usuarios internos las consulten y gestionen de forma inicial. El proceso completo de negociacion, contratos, facturacion y logistica queda para Post-MVP.

El objetivo del MVP es permitir que un comprador internacional envie una solicitud con datos de contacto, pais, cantidad e interes comercial, asociada a un lote o producto trazable, dejando estado inicial `RECIBIDA` y auditoria basica.

---

## 2. Actores

| ID | Actor | Tipo | Descripcion |
| --- | --- | --- | --- |
| ACT-01 | Comprador Internacional | Primario | Envia solicitudes de compra desde pasaporte o catalogo. |
| ACT-02 | Productor | Primario | Consulta solicitudes asociadas a sus lotes o productos. |
| ACT-03 | Exportador | Primario | Revisa y gestiona solicitudes comerciales. |
| ACT-04 | Administrador | Primario | Audita solicitudes y corrige estados por soporte. |
| ACT-05 | Sistema de Pasaporte Digital | Secundario | Provee lote publico desde donde nace la solicitud. |
| ACT-06 | Sistema de Productos | Secundario | Provee producto comercializable cuando exista catalogo. |

> En el MVP, la solicitud puede asociarse directamente a `lot_id` aunque aun no exista un modulo completo de productos exportables.

---

## 3. Entidades y modelo de datos

### 3.1 Solicitud de Compra (`PurchaseRequest`)

Representa una intencion comercial enviada por un comprador.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `public_id` | `String` | Identificador publico no secuencial para seguimiento futuro. Unico. | Si |
| `lot_id` | `UUID (FK)` | Requerido si no existe `product_id`. | No |
| `product_id` | `UUID (FK)` | Opcional en MVP. Requerido si la solicitud nace de catalogo de productos. | No |
| `passport_id` | `UUID (FK)` | Opcional. Referencia al pasaporte desde donde se envio. | No |
| `pais` | `String` | ISO 3166-1 alpha-2 recomendado. | Si |
| `cantidad_solicitada` | `Decimal` | Mayor que 0. Maximo 3 decimales. | Si |
| `unidad_medida` | `Enum` | Valores definidos en `PurchaseUnit`. | Si |
| `nombre_contacto` | `String` | Minimo 2 caracteres, maximo 120. | Si |
| `empresa` | `String` | Maximo 160 caracteres. | No |
| `correo` | `String` | Formato email valido. | Si |
| `telefono` | `String` | Maximo 40 caracteres. | No |
| `mensaje` | `String` | Maximo 2000 caracteres. | No |
| `estado` | `Enum` | Valores definidos en `PurchaseRequestStatus`. Default `RECIBIDA`. | Si |
| `origen` | `Enum` | Valores definidos en `PurchaseRequestSource`. | Si |
| `ip_origen_hash` | `String` | Opcional para mitigacion de abuso sin guardar IP plana. | No |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. Inmutable. | Si |
| `actualizado_en` | `Timestamp` | Actualizado automaticamente. UTC. | Si |

### 3.2 Evento de Solicitud (`PurchaseRequestEvent`)

Representa cambios de estado o notas internas.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `purchase_request_id` | `UUID (FK)` | Solicitud asociada. | Si |
| `estado_anterior` | `Enum` | Nulo si es evento inicial. | No |
| `estado_nuevo` | `Enum` | Estado resultante. | Si |
| `observacion` | `String` | Maximo 1000 caracteres. | No |
| `creado_por` | `UUID (FK)` | Usuario interno que registra el evento. Nulo si es sistema. | No |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. | Si |

### 3.3 Enumeraciones

#### `PurchaseRequestStatus`

- `RECIBIDA`
- `EN_REVISION`
- `APROBADA`
- `RECHAZADA`
- `EN_NEGOCIACION`
- `CERRADA`

#### `PurchaseRequestSource`

- `PASAPORTE_DIGITAL`
- `CATALOGO_PRODUCTOS`
- `FORMULARIO_PUBLICO`
- `ADMIN`

#### `PurchaseUnit`

- `KG`
- `TON`
- `LB`
- `CAJA`
- `SACO`
- `UNIDAD`

### 3.4 Relaciones

```text
Lot (1) ---- (N) PurchaseRequest
DigitalPassport (1) ---- (N) PurchaseRequest
Product (1) ---- (N) PurchaseRequest [opcional MVP]
PurchaseRequest (1) ---- (N) PurchaseRequestEvent
PurchaseRequest (1) ---- (N) AuditLog
```

### 3.5 Datos publicos vs internos

| Campo | Captura publica | Visible para productor/exportador | Publico despues de enviada |
| --- | :---: | :---: | :---: |
| `nombre_contacto` | Si | Si | No |
| `empresa` | Si | Si | No |
| `correo` | Si | Si | No |
| `telefono` | Si | Si | No |
| `pais` | Si | Si | No |
| `cantidad_solicitada` | Si | Si | No |
| `mensaje` | Si | Si | No |
| `estado` | No | Si | Solo si hay seguimiento futuro |

---

## 4. User stories y escenarios de aceptacion

### US-01 - Enviar solicitud desde pasaporte digital `P1`

**Como** comprador internacional,  
**quiero** enviar una solicitud de compra desde un pasaporte digital,  
**para que** el productor o exportador pueda contactarme.

**Por que P1**: Es el flujo comercial principal del MVP.

**Test independiente**: Un comprador envia datos validos desde un pasaporte publicado. El test es exitoso si se crea una solicitud en estado `RECIBIDA`.

#### Escenario 1 - Solicitud valida

```gherkin
Given  que existe un pasaporte PUBLICADO asociado a un lote activo
When   el comprador envia pais, cantidad, unidad, nombre y correo validos
Then   el sistema crea PurchaseRequest
And    la asocia al lote y pasaporte
And    asigna estado RECIBIDA
And    retorna confirmacion sin exponer datos internos
```

#### Escenario 2 - Pasaporte no publicado

```gherkin
Given  que el pasaporte no existe o no esta PUBLICADO
When   el comprador intenta enviar una solicitud
Then   el sistema rechaza la solicitud
And    no persiste datos de contacto
```

### US-02 - Validar datos comerciales `P1`

**Como** sistema,  
**quiero** validar datos minimos de contacto y cantidad,  
**para que** las solicitudes recibidas sean accionables.

**Test independiente**: Requests con correo invalido, cantidad cero o unidad no soportada son rechazados.

```gherkin
Given  que el comprador esta diligenciando una solicitud
When   envia correo invalido o cantidad menor o igual a cero
Then   el sistema responde error de validacion
And    no crea la solicitud
```

### US-03 - Consultar solicitudes propias `P1`

**Como** productor o exportador,  
**quiero** consultar solicitudes asociadas a mis lotes o productos,  
**para que** pueda responder oportunidades comerciales.

**Por que P1**: Sin consulta interna, las solicitudes publicas no son accionables.

**Test independiente**: Un productor autenticado consulta solicitudes. El test es exitoso si solo recibe solicitudes asociadas a sus lotes.

```gherkin
Given  que existen solicitudes para lotes de varios productores
When   un productor consulta sus solicitudes
Then   el sistema retorna solo las asociadas a sus lotes
And    incluye datos de contacto y estado
```

### US-04 - Ver detalle de solicitud `P1`

**Como** usuario interno autorizado,  
**quiero** ver el detalle de una solicitud,  
**para que** pueda evaluar el interes comercial.

**Test independiente**: El propietario del lote consulta una solicitud. El test es exitoso si recibe detalle completo y eventos.

```gherkin
Given  que existe una solicitud asociada a un lote propio
When   el productor consulta el detalle
Then   el sistema retorna contacto, cantidad, pais, mensaje, estado y eventos
```

### US-05 - Actualizar estado de solicitud `P2`

**Como** exportador o administrador,  
**quiero** actualizar el estado de una solicitud,  
**para que** el proceso comercial tenga seguimiento.

**Por que P2**: Es gestion interna util, pero el MVP minimo puede iniciar con captura y consulta.

**Test independiente**: Un exportador cambia `RECIBIDA` a `EN_REVISION`. El test es exitoso si se crea evento historico.

```gherkin
Given  que existe una solicitud RECIBIDA
When   el exportador cambia estado a EN_REVISION con observacion
Then   el sistema actualiza el estado
And    crea PurchaseRequestEvent
And    registra auditoria
```

### US-06 - Mitigar spam basico `P2`

**Como** sistema,  
**quiero** aplicar controles simples de abuso,  
**para que** el formulario publico no sea explotado facilmente.

**Test independiente**: Multiples solicitudes repetidas en ventana corta pueden ser rechazadas o marcadas para revision.

```gherkin
Given  que una misma combinacion de correo y pasaporte envia muchas solicitudes
When   supera el limite configurado
Then   el sistema bloquea temporalmente o marca la solicitud para revision
```

---

## 5. Edge cases

| ID | Caso | Resultado esperado |
| --- | --- | --- |
| 1 | Cantidad igual a cero | Rechazado. |
| 2 | Unidad no soportada | Rechazado. |
| 3 | Correo invalido | Rechazado. |
| 4 | Lote inactivo pero pasaporte historico publicado | Decision MVP: permitir solicitud solo si el pasaporte esta publicado y el producto sigue disponible; si no hay disponibilidad, rechazar. |
| 5 | Solicitud duplicada del mismo correo sobre el mismo lote | Permitida con rate limit o marcada para revision. |
| 6 | Producto inexistente pero lot_id valido | Permitido en MVP si la solicitud nace del pasaporte. |
| 7 | Mensaje con contenido excesivo | Rechazado por longitud maxima. |
| 8 | Usuario interno consulta solicitud de otro productor | Rechazado con `403`. |
| 9 | Cambio a estado no definido | Rechazado. |
| 10 | Actualizacion concurrente de estado | Debe preservar historial y evitar perdida de eventos. |

---

## 6. Requerimientos funcionales

| ID | Descripcion | US relacionada | Prioridad |
| --- | --- | --- | --- |
| FR-001 | El sistema DEBE permitir crear solicitudes desde pasaportes publicados. | US-01 | P1 |
| FR-002 | El sistema DEBE asociar la solicitud a lote y, si aplica, pasaporte o producto. | US-01 | P1 |
| FR-003 | El sistema DEBE crear solicitudes nuevas en estado `RECIBIDA`. | US-01 | P1 |
| FR-004 | El sistema DEBE validar correo obligatorio y formato valido. | US-02 | P1 |
| FR-005 | El sistema DEBE validar cantidad mayor que cero. | US-02 | P1 |
| FR-006 | El sistema DEBE validar unidad de medida soportada. | US-02 | P1 |
| FR-007 | El sistema DEBE permitir consulta autenticada de solicitudes propias. | US-03 | P1 |
| FR-008 | El sistema DEBE permitir ver detalle de solicitudes autorizadas. | US-04 | P1 |
| FR-009 | El sistema DEBE permitir actualizar estado con historial. | US-05 | P2 |
| FR-010 | El sistema DEBE registrar eventos de cambio de estado. | US-05 | P2 |
| FR-011 | El sistema DEBE aplicar controles basicos de abuso en endpoints publicos. | US-06 | P2 |
| FR-012 | El sistema DEBE registrar auditoria de creacion, consulta sensible y cambios de estado. | Todas | P2 |

---

## 7. Requerimientos no funcionales

| ID | Descripcion |
| --- | --- |
| RNF-001 | El endpoint publico de creacion debe responder en menos de 700 ms p95 sin integraciones externas. |
| RNF-002 | Las consultas internas deben estar paginadas. |
| RNF-003 | Los datos de contacto deben protegerse como datos personales. |
| RNF-004 | Las respuestas publicas no deben devolver correos, telefonos ni identificadores internos. |
| RNF-005 | Los endpoints internos deben requerir JWT valido y autorizacion por propiedad o rol. |
| RNF-006 | Los cambios de estado deben ser atomicos junto con el evento historico. |
| RNF-007 | Debe existir trazabilidad de origen de la solicitud. |
| RNF-008 | Deben registrarse metricas de solicitudes recibidas por pais, cultivo y estado. |

---

## 8. Criterios de exito

| ID | Criterio | Verificacion |
| --- | --- | --- |
| SC-001 | Un comprador puede enviar solicitud desde pasaporte publicado. | Test de integracion `POST /public/purchase-requests`. |
| SC-002 | No se aceptan solicitudes con correo invalido o cantidad no positiva. | Tests de validacion. |
| SC-003 | La solicitud queda en estado `RECIBIDA`. | Test de persistencia. |
| SC-004 | Productor solo ve solicitudes de sus lotes. | Test de autorizacion por propiedad. |
| SC-005 | El detalle interno incluye contacto y eventos. | Test de contrato interno. |
| SC-006 | Un cambio de estado crea evento historico. | Test transaccional de estado. |

---

## 9. Contratos API sugeridos

### 9.1 Crear solicitud publica desde pasaporte

```http
POST /public/purchase-requests
Content-Type: application/json
```

```json
{
  "passport_public_id": "psp_9f3ad7c2b01e",
  "pais": "US",
  "cantidad_solicitada": 2.5,
  "unidad_medida": "TON",
  "nombre_contacto": "Maria Johnson",
  "empresa": "Caribbean Specialty Imports",
  "correo": "maria@example.com",
  "telefono": "+1 555 0100",
  "mensaje": "Estamos interesados en mango certificado para importacion trimestral."
}
```

```json
{
  "public_id": "prq_2a9d84f0b632",
  "estado": "RECIBIDA",
  "mensaje": "Solicitud recibida correctamente."
}
```

### 9.2 Listar solicitudes internas

```http
GET /purchase-requests?estado=RECIBIDA&page=1&page_size=20
Authorization: Bearer <access_token>
```

```json
{
  "items": [
    {
      "id": "e35d1b8e-6de8-43be-b282-a0bbef34076c",
      "public_id": "prq_2a9d84f0b632",
      "pais": "US",
      "cantidad_solicitada": 2.5,
      "unidad_medida": "TON",
      "nombre_contacto": "Maria Johnson",
      "empresa": "Caribbean Specialty Imports",
      "correo": "maria@example.com",
      "estado": "RECIBIDA",
      "lote": {
        "codigo": "MNG-001",
        "cultivo": "Mango"
      },
      "creado_en": "2026-05-27T10:10:00Z"
    }
  ],
  "page": 1,
  "page_size": 20,
  "total": 1
}
```

### 9.3 Detalle de solicitud

```http
GET /purchase-requests/{requestId}
Authorization: Bearer <access_token>
```

```json
{
  "id": "e35d1b8e-6de8-43be-b282-a0bbef34076c",
  "public_id": "prq_2a9d84f0b632",
  "pais": "US",
  "cantidad_solicitada": 2.5,
  "unidad_medida": "TON",
  "nombre_contacto": "Maria Johnson",
  "empresa": "Caribbean Specialty Imports",
  "correo": "maria@example.com",
  "telefono": "+1 555 0100",
  "mensaje": "Estamos interesados en mango certificado para importacion trimestral.",
  "estado": "RECIBIDA",
  "origen": "PASAPORTE_DIGITAL",
  "lote": {
    "id": "8fb53b5e-f4f4-4486-989c-a3a67c72e2aa",
    "codigo": "MNG-001",
    "cultivo": "Mango"
  },
  "eventos": [
    {
      "estado_anterior": null,
      "estado_nuevo": "RECIBIDA",
      "observacion": "Solicitud creada desde pasaporte digital.",
      "creado_en": "2026-05-27T10:10:00Z"
    }
  ]
}
```

### 9.4 Actualizar estado

```http
PATCH /purchase-requests/{requestId}/status
Authorization: Bearer <access_token>
Content-Type: application/json
```

```json
{
  "estado": "EN_REVISION",
  "observacion": "Solicitud asignada a equipo comercial."
}
```

```json
{
  "id": "e35d1b8e-6de8-43be-b282-a0bbef34076c",
  "estado": "EN_REVISION",
  "actualizado_en": "2026-05-27T10:25:00Z"
}
```

---

## 10. Fuera de alcance

- Negociacion comercial completa.
- Contratos, proformas, facturacion o pagos.
- Gestion documental de exportacion.
- Seguimiento logistico internacional.
- Inventario disponible por cosecha.
- SLA comercial o asignacion automatica de agentes.
- Portal publico de seguimiento para comprador.
