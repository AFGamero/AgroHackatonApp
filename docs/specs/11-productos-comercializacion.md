# Feature Specification: Productos y Comercializacion

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

Los productos comercializables conectan los lotes trazados con compradores nacionales o internacionales. En el MVP, un producto puede representar una oferta simple asociada a un lote: cultivo, cantidad disponible, precio de referencia, unidad de medida y visibilidad publica.

Esta feature define la publicacion y consulta de productos disponibles para comercializacion. El producto no reemplaza la trazabilidad del lote; la complementa con informacion comercial basica y debe enlazar al pasaporte digital cuando exista.

El objetivo del MVP es permitir que productores o exportadores publiquen productos asociados a lotes activos y que compradores puedan consultarlos desde un catalogo publico o enviar solicitudes de compra relacionadas.

---

## 2. Actores

| ID | Actor | Tipo | Descripcion |
| --- | --- | --- | --- |
| ACT-01 | Productor | Primario | Publica productos asociados a sus lotes. |
| ACT-02 | Exportador | Primario | Publica o gestiona productos comercializables. |
| ACT-03 | Comprador Internacional | Primario | Consulta productos y puede enviar solicitudes de compra. |
| ACT-04 | Administrador | Primario | Audita publicaciones y puede despublicar productos. |
| ACT-05 | Sistema de Lotes | Secundario | Valida que el lote exista, este activo y pertenezca al actor. |
| ACT-06 | Sistema de Pasaporte Digital | Secundario | Provee URL publica de trazabilidad asociada al lote. |

---

## 3. Entidades y modelo de datos

### 3.1 Producto (`Product`)

Representa una oferta comercial asociada a un lote.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `lot_id` | `UUID (FK)` | Debe apuntar a un lote activo. | Si |
| `public_id` | `String` | Identificador publico no secuencial. Unico. | Si |
| `nombre` | `String` | Minimo 3 caracteres, maximo 140. | Si |
| `descripcion` | `String` | Maximo 1500 caracteres. | No |
| `cultivo` | `String` | Puede derivarse del lote, pero se persiste para consulta comercial. | Si |
| `variedad` | `String` | Opcional, puede derivarse del lote. | No |
| `cantidad_disponible` | `Decimal` | Mayor que 0. Maximo 3 decimales. | Si |
| `unidad_medida` | `Enum` | Valores definidos en `ProductUnit`. | Si |
| `precio_referencia` | `Decimal` | Mayor o igual que 0. Opcional si se negocia bajo cotizacion. | No |
| `moneda` | `String` | ISO 4217. Default `COP`. | No |
| `fecha_disponibilidad` | `Date` | No debe ser anterior a la fecha actual si representa disponibilidad futura. | No |
| `estado` | `Enum` | Valores definidos en `ProductStatus`. Default `BORRADOR`. | Si |
| `publicado_en` | `Timestamp` | UTC. Requerido cuando estado es `PUBLICADO`. | No |
| `creado_por` | `UUID (FK)` | Usuario productor, exportador o admin. | Si |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. Inmutable. | Si |
| `actualizado_en` | `Timestamp` | Actualizado automaticamente. UTC. | Si |

### 3.2 Fotografia de Producto (`ProductPhoto`)

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. | Si |
| `product_id` | `UUID (FK)` | Producto propietario. | Si |
| `url` | `String` | URL valida del archivo. | Si |
| `descripcion` | `String` | Maximo 255 caracteres. | No |
| `es_portada` | `Boolean` | Default `false`. Solo una portada por producto. | Si |
| `orden` | `Integer` | Mayor o igual a 0. | Si |

### 3.3 Enumeraciones

#### `ProductStatus`

- `BORRADOR`
- `PUBLICADO`
- `DESPUBLICADO`
- `AGOTADO`
- `INACTIVO`

#### `ProductUnit`

- `KG`
- `TON`
- `LB`
- `CAJA`
- `SACO`
- `UNIDAD`

### 3.4 Relaciones

```text
Lot (1) ---- (N) Product
Product (1) ---- (N) ProductPhoto
Product (1) ---- (N) PurchaseRequest
Product (1) ---- (N) AuditLog
DigitalPassport (0..1) ---- (N) Product [via lot_id]
```

### 3.5 Datos publicos vs internos

| Campo | Uso interno | Visible en catalogo publico |
| --- | :---: | :---: |
| `nombre` | Si | Si |
| `descripcion` | Si | Si |
| `cultivo` / `variedad` | Si | Si |
| `cantidad_disponible` | Si | Si |
| `precio_referencia` | Si | Si, si esta definido |
| `lot_id` | Si | No directamente |
| `public_id` | Si | Si |
| `creado_por` | Si | No |
| Auditoria | Si | No |

---

## 4. User stories y escenarios de aceptacion

### US-01 - Publicar producto comercializable `P1`

**Como** productor o exportador,  
**quiero** crear un producto asociado a un lote,  
**para que** compradores puedan consultar disponibilidad comercial.

**Test independiente**: Un productor con lote activo crea un producto con cantidad valida. El test es exitoso si queda en estado `BORRADOR` o `PUBLICADO` segun endpoint usado.

```gherkin
Given  que existe un lote ACTIVO propio
When   el actor envia nombre, cantidad, unidad y datos comerciales validos
Then   el sistema crea el producto
And    lo asocia al lote
And    genera public_id no secuencial
```

### US-02 - Validar disponibilidad y precio `P1`

**Como** sistema,  
**quiero** validar cantidad, unidad y precio,  
**para que** el catalogo no publique ofertas invalidas.

```gherkin
Given  que el actor crea o actualiza un producto
When   envia cantidad menor o igual a cero, unidad no soportada o precio negativo
Then   el sistema rechaza la solicitud
And    no publica cambios invalidos
```

### US-03 - Publicar y despublicar producto `P1`

**Como** gestor autorizado,  
**quiero** controlar la visibilidad de un producto,  
**para que** solo ofertas vigentes aparezcan publicamente.

```gherkin
Given  que existe un producto BORRADOR con datos minimos validos
When   el gestor lo publica
Then   el sistema cambia estado a PUBLICADO
And    registra publicado_en
And    aparece en catalogo publico
```

### US-04 - Consultar catalogo publico de productos `P2`

**Como** comprador internacional,  
**quiero** explorar productos publicados,  
**para que** pueda encontrar oportunidades de compra con trazabilidad.

```gherkin
Given  que existen productos publicados y borradores
When   el comprador consulta el catalogo publico
Then   el sistema retorna solo productos PUBLICADO
And    incluye informacion basica y enlace al pasaporte digital si existe
```

### US-05 - Consultar detalle publico de producto `P2`

**Como** comprador internacional,  
**quiero** ver el detalle de un producto,  
**para que** pueda revisar disponibilidad y trazabilidad antes de solicitar compra.

```gherkin
Given  que existe un producto PUBLICADO
When   el comprador consulta el detalle publico
Then   el sistema retorna datos comerciales, lote publico, finca publica y pasaporte si existe
And    no retorna datos internos del productor
```

### US-06 - Marcar producto como agotado `P2`

**Como** productor o exportador,  
**quiero** marcar un producto como agotado,  
**para que** deje de recibir solicitudes nuevas.

```gherkin
Given  que existe un producto PUBLICADO
When   el gestor lo marca como AGOTADO
Then   el sistema actualiza el estado
And    lo excluye del catalogo publico por defecto
```

---

## 5. Edge cases

| ID | Caso | Resultado esperado |
| --- | --- | --- |
| 1 | Cantidad disponible igual a cero | Rechazado al crear; para producto existente debe usarse `AGOTADO`. |
| 2 | Precio no definido | Permitido si el producto se negocia bajo solicitud. |
| 3 | Precio negativo | Rechazado. |
| 4 | Lote sin pasaporte digital | Producto puede existir; catalogo indica trazabilidad no publicada o bloquea publicacion segun configuracion. |
| 5 | Lote inactivo | No permite crear o publicar nuevos productos. |
| 6 | Producto publicado y luego lote desactivado | Debe despublicarse o quedar oculto del catalogo publico. |
| 7 | Dos portadas de producto | Solo una foto queda como portada. |
| 8 | Usuario intenta publicar producto de lote ajeno | Rechazado con `403`. |
| 9 | Producto agotado recibe solicitud | Rechazado o redirigido a contacto manual segun configuracion. |
| 10 | Actualizacion concurrente de cantidad | Debe preservarse consistencia con bloqueo optimista o transaccion. |

---

## 6. Requerimientos funcionales

| ID | Descripcion | US relacionada | Prioridad |
| --- | --- | --- | --- |
| FR-001 | El sistema DEBE permitir crear productos asociados a lotes activos. | US-01 | P1 |
| FR-002 | El sistema DEBE validar propiedad o permisos sobre el lote. | US-01 | P1 |
| FR-003 | El sistema DEBE validar cantidad disponible mayor que cero. | US-02 | P1 |
| FR-004 | El sistema DEBE validar unidad de medida soportada. | US-02 | P1 |
| FR-005 | El sistema DEBE rechazar precios negativos. | US-02 | P1 |
| FR-006 | El sistema DEBE permitir publicar y despublicar productos. | US-03 | P1 |
| FR-007 | El sistema DEBE exponer catalogo publico solo con productos publicados. | US-04 | P2 |
| FR-008 | El sistema DEBE incluir enlace al pasaporte digital cuando exista. | US-04, US-05 | P2 |
| FR-009 | El sistema DEBE permitir marcar productos como agotados. | US-06 | P2 |
| FR-010 | El sistema DEBE registrar auditoria de creacion, publicacion, cambios y agotamiento. | Todas | P2 |

---

## 7. Requerimientos no funcionales

| ID | Descripcion |
| --- | --- |
| RNF-001 | Las consultas publicas deben ser paginadas. |
| RNF-002 | El catalogo publico debe responder en menos de 700 ms p95 para 1000 productos. |
| RNF-003 | Los endpoints internos deben requerir JWT valido. |
| RNF-004 | Las respuestas publicas no deben exponer datos sensibles del productor. |
| RNF-005 | Los cambios de estado y disponibilidad deben ser auditables. |
| RNF-006 | La publicacion debe ser atomica frente a validaciones del lote. |

---

## 8. Criterios de exito

| ID | Criterio | Verificacion |
| --- | --- | --- |
| SC-001 | Un productor puede crear producto de lote propio activo. | Test de integracion `POST /products`. |
| SC-002 | No se puede crear producto con cantidad no positiva. | Test de validacion. |
| SC-003 | No se puede publicar producto de lote ajeno. | Test de autorizacion. |
| SC-004 | Producto publicado aparece en catalogo publico. | Test de `GET /public/products`. |
| SC-005 | Producto despublicado o agotado no aparece por defecto. | Test de visibilidad publica. |
| SC-006 | El detalle publico incluye trazabilidad si hay pasaporte publicado. | Test de contrato publico. |

---

## 9. Contratos API sugeridos

### 9.1 Crear producto

```http
POST /products
Authorization: Bearer <access_token>
Content-Type: application/json
```

```json
{
  "lot_id": "8fb53b5e-f4f4-4486-989c-a3a67c72e2aa",
  "nombre": "Mango Tommy trazado",
  "descripcion": "Mango fresco con trazabilidad desde finca del Magdalena.",
  "cultivo": "Mango",
  "variedad": "Tommy Atkins",
  "cantidad_disponible": 5,
  "unidad_medida": "TON",
  "precio_referencia": 1200,
  "moneda": "USD",
  "fecha_disponibilidad": "2026-06-15"
}
```

```json
{
  "id": "d1f0400a-84b9-4048-8c77-8e40e3c2f341",
  "public_id": "prd_8fa03d2c1b71",
  "nombre": "Mango Tommy trazado",
  "estado": "BORRADOR",
  "creado_en": "2026-05-27T11:00:00Z"
}
```

### 9.2 Publicar producto

```http
POST /products/{productId}/publish
Authorization: Bearer <access_token>
```

```json
{
  "id": "d1f0400a-84b9-4048-8c77-8e40e3c2f341",
  "estado": "PUBLICADO",
  "publicado_en": "2026-05-27T11:10:00Z"
}
```

### 9.3 Catalogo publico

```http
GET /public/products?cultivo=Mango&pais_destino=US&page=1&page_size=20
```

```json
{
  "items": [
    {
      "public_id": "prd_8fa03d2c1b71",
      "nombre": "Mango Tommy trazado",
      "cultivo": "Mango",
      "variedad": "Tommy Atkins",
      "cantidad_disponible": 5,
      "unidad_medida": "TON",
      "precio_referencia": 1200,
      "moneda": "USD",
      "pasaporte_url": "/public/passports/psp_9f3ad7c2b01e"
    }
  ],
  "page": 1,
  "page_size": 20,
  "total": 1
}
```

### 9.4 Marcar agotado

```http
POST /products/{productId}/mark-sold-out
Authorization: Bearer <access_token>
```

```json
{
  "id": "d1f0400a-84b9-4048-8c77-8e40e3c2f341",
  "estado": "AGOTADO",
  "actualizado_en": "2026-05-27T11:25:00Z"
}
```

---

## 10. Fuera de alcance

- Inventario transaccional por orden.
- Integracion con pagos o facturacion.
- Cotizaciones automaticas.
- Tarifas de envio o incoterms.
- Marketplace multi-vendedor completo.
- Reservas de producto o bloqueo de inventario.
