# Feature Specification: Exportaciones y Trazabilidad Logistica

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

Las exportaciones y la trazabilidad logistica extienden la confianza del producto mas alla de la finca. Una vez existe una solicitud aprobada o una venta confirmada, el sistema puede registrar un proceso logistico con destino, cantidad, fechas estimadas y eventos de avance.

Esta feature es principalmente Post-MVP. Se documenta para dejar claro el modelo futuro y evitar que el MVP de productos y solicitudes cierre puertas a la trazabilidad comercial.

El objetivo Post-MVP es permitir que exportadores creen exportaciones asociadas a productos o solicitudes aprobadas, registren eventos logisticos y expongan un seguimiento publico controlado mediante identificador no secuencial.

---

## 2. Actores

| ID | Actor | Tipo | Descripcion |
| --- | --- | --- | --- |
| ACT-01 | Exportador | Primario | Crea exportaciones y actualiza estados logisticos. |
| ACT-02 | Productor | Primario | Consulta exportaciones relacionadas con sus productos. |
| ACT-03 | Comprador Internacional | Primario | Consulta seguimiento publico o privado de su exportacion. |
| ACT-04 | Administrador | Primario | Audita y corrige estados logisticos. |
| ACT-05 | Sistema de Solicitudes | Secundario | Provee solicitud aprobada como origen del proceso. |
| ACT-06 | Sistema de Productos | Secundario | Provee producto exportable asociado. |

---

## 3. Entidades y modelo de datos

### 3.1 Exportacion (`ExportShipment`)

Representa un proceso de exportacion asociado a producto o solicitud.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `tracking_public_id` | `String` | Identificador publico no secuencial. Unico. | Si |
| `product_id` | `UUID (FK)` | Producto exportado. | No |
| `purchase_request_id` | `UUID (FK)` | Solicitud aprobada que origina el proceso. | No |
| `lot_id` | `UUID (FK)` | Lote trazable asociado. | Si |
| `destino_pais` | `String` | ISO 3166-1 alpha-2 recomendado. | Si |
| `destino_ciudad` | `String` | Maximo 120 caracteres. | No |
| `cantidad` | `Decimal` | Mayor que 0. | Si |
| `unidad_medida` | `Enum` | Valores definidos en `ExportUnit`. | Si |
| `fecha_estimada_salida` | `Date` | Opcional. | No |
| `fecha_estimada_entrega` | `Date` | Debe ser posterior a salida si ambas existen. | No |
| `estado_actual` | `Enum` | Valores definidos en `LogisticStatus`. Default `PREPARACION`. | Si |
| `creado_por` | `UUID (FK)` | Exportador o admin. | Si |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. Inmutable. | Si |
| `actualizado_en` | `Timestamp` | Actualizado automaticamente. UTC. | Si |

### 3.2 Evento Logistico (`LogisticEvent`)

Representa un cambio de estado o hito de la exportacion.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `export_shipment_id` | `UUID (FK)` | Exportacion asociada. | Si |
| `estado_logistico` | `Enum` | Valores definidos en `LogisticStatus`. | Si |
| `fecha_evento` | `Timestamp` | No debe ser futura salvo eventos planificados marcados como estimados. | Si |
| `ubicacion` | `String` | Maximo 180 caracteres. | No |
| `observaciones` | `String` | Maximo 1000 caracteres. | No |
| `visible_para_comprador` | `Boolean` | Default `true`. | Si |
| `registrado_por` | `UUID (FK)` | Usuario que registra el evento. | Si |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. | Si |

### 3.3 Enumeraciones

#### `LogisticStatus`

- `PREPARACION`
- `EMPAQUE`
- `PUERTO`
- `EMBARCADO`
- `EN_TRANSITO`
- `ENTREGADO`
- `CANCELADO`

#### `ExportUnit`

- `KG`
- `TON`
- `LB`
- `CAJA`
- `SACO`
- `UNIDAD`

### 3.4 Relaciones

```text
Product (0..1) ---- (N) ExportShipment
PurchaseRequest (0..1) ---- (N) ExportShipment
Lot (1) ---- (N) ExportShipment
ExportShipment (1) ---- (N) LogisticEvent
ExportShipment (1) ---- (N) AuditLog
```

---

## 4. User stories y escenarios de aceptacion

### US-01 - Crear exportacion `P2`

**Como** exportador,  
**quiero** crear una exportacion desde una solicitud aprobada o producto,  
**para que** el despacho tenga seguimiento logistico.

```gherkin
Given  que existe una solicitud APROBADA o producto exportable
When   el exportador registra destino, cantidad y unidad validos
Then   el sistema crea ExportShipment
And    genera tracking_public_id
And    crea primer evento logistico PREPARACION
```

### US-02 - Actualizar estado logistico `P2`

**Como** exportador,  
**quiero** registrar avances logisticos,  
**para que** comprador y productor conozcan el estado del envio.

```gherkin
Given  que existe una exportacion activa
When   el exportador registra estado EMBARCADO con fecha valida
Then   el sistema crea LogisticEvent
And    actualiza estado_actual de la exportacion
```

### US-03 - Consultar trazabilidad logistica interna `P2`

**Como** productor o exportador,  
**quiero** consultar el historial logistico,  
**para que** pueda dar soporte al comprador.

```gherkin
Given  que existe una exportacion con eventos logisticos
When   un usuario autorizado consulta el detalle
Then   el sistema retorna datos de exportacion y eventos completos
```

### US-04 - Consultar seguimiento publico `P2`

**Como** comprador internacional,  
**quiero** consultar el seguimiento con un codigo publico,  
**para que** pueda verificar el avance del envio.

```gherkin
Given  que existe una exportacion con tracking_public_id valido
When   el comprador consulta el seguimiento publico
Then   el sistema retorna estado_actual y eventos visibles
And    no expone datos internos del exportador o productor
```

### US-05 - Cancelar exportacion `P3`

**Como** exportador o administrador,  
**quiero** cancelar una exportacion,  
**para que** el seguimiento refleje que el proceso no continuara.

```gherkin
Given  que existe una exportacion no entregada
When   el usuario autorizado solicita cancelacion con motivo
Then   el sistema cambia estado_actual a CANCELADO
And    registra evento logistico visible segun configuracion
```

---

## 5. Edge cases

| ID | Caso | Resultado esperado |
| --- | --- | --- |
| 1 | Cantidad menor o igual a cero | Rechazado. |
| 2 | Solicitud no aprobada | No permite crear exportacion desde esa solicitud. |
| 3 | Fecha estimada de entrega anterior a salida | Rechazado. |
| 4 | Estado logistico no soportado | Rechazado. |
| 5 | Evento con fecha futura no marcado como estimado | Rechazado. |
| 6 | Exportacion entregada recibe nuevo estado operativo | Rechazado salvo correccion administrativa. |
| 7 | Consulta con tracking invalido | Error controlado sin revelar existencia de IDs internos. |
| 8 | Evento interno no visible | No aparece en seguimiento publico. |
| 9 | Cancelacion despues de entregado | Requiere flujo administrativo excepcional. |
| 10 | Eventos fuera de orden | Historial se ordena por `fecha_evento` y `creado_en`. |

---

## 6. Requerimientos funcionales

| ID | Descripcion | US relacionada | Prioridad |
| --- | --- | --- | --- |
| FR-001 | El sistema DEBE permitir crear exportaciones desde solicitud aprobada o producto valido. | US-01 | P2 |
| FR-002 | El sistema DEBE generar tracking publico no secuencial. | US-01 | P2 |
| FR-003 | El sistema DEBE crear evento inicial `PREPARACION`. | US-01 | P2 |
| FR-004 | El sistema DEBE registrar eventos logisticos validos. | US-02 | P2 |
| FR-005 | El sistema DEBE actualizar `estado_actual` con el ultimo evento valido. | US-02 | P2 |
| FR-006 | El sistema DEBE permitir consulta interna de exportaciones autorizadas. | US-03 | P2 |
| FR-007 | El sistema DEBE exponer seguimiento publico por tracking. | US-04 | P2 |
| FR-008 | El sistema DEBE ocultar eventos no visibles en seguimiento publico. | US-04 | P2 |
| FR-009 | El sistema DEBE permitir cancelacion con motivo. | US-05 | P3 |
| FR-010 | El sistema DEBE registrar auditoria de creacion, eventos y cancelacion. | Todas | P2 |

---

## 7. Requerimientos no funcionales

| ID | Descripcion |
| --- | --- |
| RNF-001 | La consulta publica de tracking debe responder en menos de 700 ms p95. |
| RNF-002 | Los identificadores publicos no deben ser secuenciales ni adivinables. |
| RNF-003 | Los eventos logisticos y actualizacion de estado deben ser atomicos. |
| RNF-004 | El seguimiento publico no debe exponer correos, telefonos ni datos internos. |
| RNF-005 | Las consultas internas deben exigir JWT y autorizacion por rol o propiedad. |
| RNF-006 | El historial logistico debe ser inmutable salvo correcciones auditadas. |

---

## 8. Criterios de exito

| ID | Criterio | Verificacion |
| --- | --- | --- |
| SC-001 | Exportador crea exportacion desde solicitud aprobada. | Test de integracion `POST /exports`. |
| SC-002 | Se crea evento inicial `PREPARACION`. | Test transaccional. |
| SC-003 | Registrar evento logistico actualiza estado actual. | Test de estado. |
| SC-004 | Tracking publico retorna solo eventos visibles. | Test de contrato publico. |
| SC-005 | Solicitud no aprobada no permite exportacion. | Test de regla de negocio. |
| SC-006 | Exportacion entregada no acepta nuevos eventos operativos. | Test de ciclo de vida. |

---

## 9. Contratos API sugeridos

### 9.1 Crear exportacion

```http
POST /exports
Authorization: Bearer <access_token>
Content-Type: application/json
```

```json
{
  "purchase_request_id": "e35d1b8e-6de8-43be-b282-a0bbef34076c",
  "product_id": "d1f0400a-84b9-4048-8c77-8e40e3c2f341",
  "lot_id": "8fb53b5e-f4f4-4486-989c-a3a67c72e2aa",
  "destino_pais": "US",
  "destino_ciudad": "Miami",
  "cantidad": 2.5,
  "unidad_medida": "TON",
  "fecha_estimada_salida": "2026-06-20",
  "fecha_estimada_entrega": "2026-07-05"
}
```

```json
{
  "id": "736b150c-5d53-4b87-8d3a-1310f4201e02",
  "tracking_public_id": "trk_5e12ac90d8a1",
  "estado_actual": "PREPARACION",
  "creado_en": "2026-05-27T12:00:00Z"
}
```

### 9.2 Registrar evento logistico

```http
POST /exports/{exportId}/events
Authorization: Bearer <access_token>
Content-Type: application/json
```

```json
{
  "estado_logistico": "EMBARCADO",
  "fecha_evento": "2026-06-22T15:30:00Z",
  "ubicacion": "Puerto de Santa Marta",
  "observaciones": "Contenedor embarcado correctamente.",
  "visible_para_comprador": true
}
```

```json
{
  "id": "99708d47-c2f4-4d4b-a2b6-80c43b5fa311",
  "estado_logistico": "EMBARCADO",
  "exportacion_estado_actual": "EMBARCADO",
  "creado_en": "2026-06-22T15:35:00Z"
}
```

### 9.3 Seguimiento publico

```http
GET /public/exports/tracking/trk_5e12ac90d8a1
```

```json
{
  "tracking_public_id": "trk_5e12ac90d8a1",
  "estado_actual": "EMBARCADO",
  "destino_pais": "US",
  "destino_ciudad": "Miami",
  "eventos": [
    {
      "estado_logistico": "PREPARACION",
      "fecha_evento": "2026-05-27T12:00:00Z",
      "ubicacion": null,
      "observaciones": "Exportacion creada."
    },
    {
      "estado_logistico": "EMBARCADO",
      "fecha_evento": "2026-06-22T15:30:00Z",
      "ubicacion": "Puerto de Santa Marta",
      "observaciones": "Contenedor embarcado correctamente."
    }
  ]
}
```

---

## 10. Fuera de alcance

- Integracion con navieras o operadores logisticos.
- Documentos aduaneros.
- Incoterms, seguros y costos logisticos.
- Tracking automatico por GPS o contenedor.
- Notificaciones automaticas al comprador.
- Gestion de reclamaciones o devoluciones.
