# Feature Specification: Pasaporte Digital y QR

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

El pasaporte digital es la pieza publica que convierte la trazabilidad interna de AgroTrace Magdalena en una experiencia verificable para turistas, compradores internacionales y aliados comerciales. Cada pasaporte consolida informacion publica del productor, finca, lote, estados de cultivo, evidencias visibles y certificaciones vigentes.

El QR es el mecanismo de acceso rapido al pasaporte digital. Puede imprimirse en productos, hoteles, fincas, material turistico o documentos comerciales.

El objetivo del MVP es permitir que un productor genere un pasaporte digital publico para un lote propio, que el sistema cree una URL publica no secuencial, genere un codigo QR asociado y exponga una respuesta publica sin datos sensibles.

---

## 2. Actores

| ID | Actor | Tipo | Descripcion |
| --- | --- | --- | --- |
| ACT-01 | Productor | Primario | Genera, publica, consulta y despublica pasaportes de sus lotes. |
| ACT-02 | Administrador | Primario | Audita pasaportes, puede despublicar contenido por soporte o cumplimiento. |
| ACT-03 | Turista | Primario | Escanea QR y consulta el pasaporte publico del lote. |
| ACT-04 | Comprador Internacional | Primario | Consulta origen, certificaciones y evidencias del lote desde el pasaporte. |
| ACT-05 | Sistema | Secundario | Genera identificadores publicos, consolida datos y crea QR. |
| ACT-06 | Sistema de Trazabilidad | Secundario | Provee estados de cultivo y evidencias visibles. |
| ACT-07 | Sistema de Certificaciones | Secundario | Provee certificaciones validadas, vigentes y visibles. |

> La consulta del pasaporte es publica y no requiere autenticacion. La generacion, publicacion o despublicacion requiere usuario autenticado y permisos sobre el lote.

---

## 3. Entidades y modelo de datos

### 3.1 Pasaporte Digital (`DigitalPassport`)

Representa la publicacion publica consolidada de un lote.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `lot_id` | `UUID (FK)` | Debe apuntar a un lote propio. Unico para pasaporte activo. | Si |
| `public_id` | `String` | Identificador publico no secuencial. Unico. | Si |
| `slug` | `String` | Opcional, derivado de cultivo o nombre. No se usa como identificador confiable. | No |
| `url_publica` | `String` | URL absoluta o path publico del pasaporte. | Si |
| `estado` | `Enum` | Valores definidos en `PassportStatus`. Default `BORRADOR`. | Si |
| `publicado_en` | `Timestamp` | UTC. Requerido cuando estado es `PUBLICADO`. | No |
| `despublicado_en` | `Timestamp` | UTC. Requerido cuando estado es `DESPUBLICADO`. | No |
| `motivo_despublicacion` | `String` | Maximo 500 caracteres. Requerido si despublica admin. | No |
| `generado_por` | `UUID (FK)` | Usuario que genera el pasaporte. | Si |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. Inmutable. | Si |
| `actualizado_en` | `Timestamp` | Actualizado automaticamente. UTC. | Si |

### 3.2 Codigo QR (`QrCode`)

Representa el QR asociado a un pasaporte digital.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `passport_id` | `UUID (FK)` | Pasaporte asociado. Unico para QR activo. | Si |
| `contenido` | `String` | URL publica codificada en el QR. | Si |
| `formato` | `Enum` | Valores definidos en `QrFormat`. Default `PNG`. | Si |
| `archivo_url` | `String` | URL o path del QR generado. | Si |
| `checksum` | `String` | Hash del contenido para detectar cambios. | Si |
| `estado` | `Enum` | Valores definidos en `QrStatus`. Default `ACTIVO`. | Si |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. Inmutable. | Si |

### 3.3 Vista publica consolidada (`PassportPublicView`)

No necesariamente se persiste. Es el contrato de lectura construido desde productor, finca, lote, trazabilidad, evidencias y certificaciones.

| Seccion | Fuente | Regla publica |
| --- | --- | --- |
| Productor | `ProducerProfile` | Solo campos publicos. |
| Finca | `Farm` | Sin direccion exacta por defecto. |
| Lote | `Lot` | Datos productivos principales. |
| Trazabilidad | `CropStatusEvent` | Historial ordenado por fecha. |
| Evidencias | `Evidence` | Solo `visible_publicamente = true`. |
| Certificaciones | `Certification` | Solo `VALIDADA`, vigente y visible. |
| QR | `QrCode` | URL del QR activo. |

### 3.4 Enumeraciones

#### `PassportStatus`

- `BORRADOR`
- `PUBLICADO`
- `DESPUBLICADO`

#### `QrFormat`

- `PNG`
- `SVG`

#### `QrStatus`

- `ACTIVO`
- `REVOCADO`

### 3.5 Relaciones

```text
Lot (1) ---- (0..1) DigitalPassport
DigitalPassport (1) ---- (0..1) QrCode
DigitalPassport (1) ---- (N) AuditLog
```

---

## 4. User stories y escenarios de aceptacion

### US-01 - Generar pasaporte digital de lote `P1`

**Como** productor,  
**quiero** generar un pasaporte digital para un lote propio,  
**para que** el origen y trazabilidad del producto puedan consultarse publicamente.

**Por que P1**: Es el objetivo central del MVP.

**Test independiente**: Un productor genera pasaporte para un lote propio activo. El test es exitoso si se crea `DigitalPassport` con `public_id` unico.

#### Escenario 1 - Generacion exitosa

```gherkin
Given  que existe un productor ACTIVO autenticado
And    tiene un lote ACTIVO dentro de una finca propia
When   solicita generar pasaporte digital para el lote
Then   el sistema crea un DigitalPassport
And    genera un public_id no secuencial y unico
And    construye url_publica
And    deja el pasaporte en estado BORRADOR o PUBLICADO segun configuracion MVP
And    registra auditoria de generacion
```

#### Escenario 2 - Lote ajeno

```gherkin
Given  que existe un lote de otro productor
When   el productor autenticado intenta generar pasaporte
Then   el sistema rechaza la solicitud con 403 o 404 segun politica de seguridad
And    no crea pasaporte
```

#### Escenario 3 - Pasaporte ya existe

```gherkin
Given  que un lote ya tiene un pasaporte activo
When   el productor solicita generar otro pasaporte
Then   el sistema retorna el pasaporte existente
And    no crea duplicados activos para el mismo lote
```

### US-02 - Publicar pasaporte digital `P1`

**Como** productor,  
**quiero** publicar el pasaporte de un lote,  
**para que** turistas y compradores puedan consultarlo mediante URL o QR.

**Por que P1**: El pasaporte solo cumple su funcion si puede ser consultado publicamente.

**Test independiente**: Un pasaporte en borrador se publica. El test es exitoso si `GET /public/passports/{publicId}` retorna informacion consolidada.

#### Escenario 1 - Publicacion exitosa

```gherkin
Given  que existe un pasaporte BORRADOR de un lote propio
When   el productor solicita publicarlo
Then   el sistema cambia estado a PUBLICADO
And    establece publicado_en
And    registra auditoria de publicacion
```

#### Escenario 2 - Lote sin datos minimos

```gherkin
Given  que existe un pasaporte BORRADOR
And    el lote no tiene estado de cultivo registrado
When   el productor intenta publicarlo
Then   el sistema puede permitir publicacion con advertencia o bloquear segun configuracion MVP
And    si bloquea, indica que debe registrar al menos un estado de cultivo
```

#### Escenario 3 - Finca o lote inactivo

```gherkin
Given  que el lote o finca asociada esta INACTIVA
When   el productor intenta publicar el pasaporte
Then   el sistema bloquea la publicacion nueva
And    conserva pasaportes publicados previamente como historicos si aplica
```

### US-03 - Generar codigo QR `P1`

**Como** productor,  
**quiero** generar un QR asociado al pasaporte digital,  
**para que** cualquier persona pueda acceder al pasaporte desde un producto o punto turistico.

**Por que P1**: El QR es el puente entre mundo fisico y pasaporte digital.

**Test independiente**: Un pasaporte existente genera QR. El test es exitoso si el QR codifica la URL publica correcta.

#### Escenario 1 - QR generado correctamente

```gherkin
Given  que existe un pasaporte digital para un lote propio
When   el productor solicita generar QR
Then   el sistema genera un codigo QR con url_publica como contenido
And    almacena archivo_url
And    guarda checksum del contenido
And    registra auditoria de generacion de QR
```

#### Escenario 2 - QR ya existe

```gherkin
Given  que el pasaporte ya tiene un QR ACTIVO
When   el productor solicita generar QR
Then   el sistema retorna el QR existente
And    no genera duplicado innecesario
```

#### Escenario 3 - Cambio de URL publica

```gherkin
Given  que existe un QR ACTIVO
And    cambia la url_publica del pasaporte por configuracion del sistema
When   el productor solicita regenerar QR
Then   el sistema revoca el QR anterior
And    genera un nuevo QR con checksum actualizado
```

### US-04 - Consultar pasaporte publico `P1`

**Como** turista o comprador,  
**quiero** consultar un pasaporte digital publico,  
**para que** pueda verificar origen, historia, trazabilidad y certificaciones del lote.

**Por que P1**: Es la experiencia publica principal del MVP.

**Test independiente**: Un usuario sin autenticacion consulta `GET /public/passports/{publicId}`. El test es exitoso si recibe datos consolidados sin campos sensibles.

#### Escenario 1 - Consulta publica exitosa

```gherkin
Given  que existe un pasaporte PUBLICADO
When   un usuario publico consulta por public_id
Then   el sistema retorna productor publico, finca publica, lote, trazabilidad, evidencias visibles y certificaciones publicables
And    no exige autenticacion
And    no retorna campos sensibles
```

#### Escenario 2 - Pasaporte no publicado

```gherkin
Given  que existe un pasaporte BORRADOR
When   un usuario publico intenta consultarlo
Then   el sistema retorna error controlado de no encontrado o no disponible
And    no expone informacion parcial
```

#### Escenario 3 - Public ID inexistente

```gherkin
Given  que no existe pasaporte con el public_id recibido
When   se consulta la URL publica
Then   el sistema retorna 404
```

### US-05 - Despublicar pasaporte `P2`

**Como** productor o administrador,  
**quiero** despublicar un pasaporte,  
**para que** deje de estar disponible publicamente sin borrar su historial.

**Por que P2**: Permite control de contenido y cumplimiento.

**Test independiente**: Un pasaporte publicado se despublica. El test es exitoso si la consulta publica deja de retornar datos.

#### Escenario 1 - Despublicacion por productor

```gherkin
Given  que existe un pasaporte PUBLICADO de un lote propio
When   el productor solicita despublicarlo
Then   el sistema cambia estado a DESPUBLICADO
And    establece despublicado_en
And    registra auditoria
```

#### Escenario 2 - Despublicacion administrativa

```gherkin
Given  que existe un pasaporte PUBLICADO
And    el administrador esta autenticado
When   lo despublica con motivo
Then   el sistema cambia estado a DESPUBLICADO
And    guarda motivo_despublicacion
And    registra auditoria administrativa
```

### US-06 - Consultar QR del pasaporte `P1`

**Como** productor,  
**quiero** descargar o consultar el QR de un pasaporte,  
**para que** pueda imprimirlo o compartirlo.

**Por que P1**: El QR debe estar disponible para uso fisico y comercial.

**Test independiente**: El productor consulta QR de un pasaporte propio. El test es exitoso si recibe `archivo_url` y contenido.

#### Escenario 1 - QR disponible

```gherkin
Given  que existe un pasaporte propio con QR ACTIVO
When   el productor consulta el QR
Then   el sistema retorna archivo_url, formato y contenido
```

#### Escenario 2 - QR inexistente

```gherkin
Given  que existe un pasaporte sin QR
When   el productor consulta el QR
Then   el sistema retorna que no existe QR
And    puede sugerir generar uno
```

---

## 5. Edge cases

| # | Caso | Comportamiento esperado |
| --- | --- | --- |
| 1 | Dos solicitudes generan pasaporte para el mismo lote simultaneamente | Constraint unico por `lot_id` evita duplicados; una solicitud retorna el existente o conflicto controlado. |
| 2 | Public ID colisiona | El sistema reintenta generacion hasta obtener identificador unico. |
| 3 | Lote sin eventos de cultivo | Puede publicarse con advertencia o bloquearse segun configuracion; la regla debe ser consistente. |
| 4 | Evidencias ocultas | Nunca aparecen en respuesta publica. |
| 5 | Certificacion vencida | No aparece como certificacion publica aunque siga asociada al lote. |
| 6 | Productor oculta contacto | El pasaporte no retorna telefono del productor. |
| 7 | Pasaporte despublicado con QR impreso | La URL responde no disponible o pagina controlada sin datos sensibles. |
| 8 | QR generado antes de publicar | Puede permitirse, pero al escanear no debe exponer datos hasta que el pasaporte este PUBLICADO. |
| 9 | Cambio de dominio publico | Debe regenerarse QR o mantener redireccion estable. En MVP se recomienda URL estable por configuracion. |
| 10 | Consulta publica con public_id malformado | Retorna 404 o 400 controlado sin filtrar errores internos. |

---

## 6. Requerimientos funcionales

| ID | Descripcion | US relacionada | Prioridad |
| --- | --- | --- | --- |
| FR-001 | El sistema DEBE permitir generar un pasaporte digital para un lote propio. | US-01 | P1 |
| FR-002 | El sistema DEBE garantizar un unico pasaporte activo por lote. | US-01 | P1 |
| FR-003 | El sistema DEBE generar `public_id` no secuencial y unico. | US-01 | P1 |
| FR-004 | El sistema DEBE permitir publicar pasaportes para consulta publica. | US-02 | P1 |
| FR-005 | El sistema DEBE generar QR asociado a la URL publica del pasaporte. | US-03 | P1 |
| FR-006 | El sistema DEBE evitar duplicar QR activo para un mismo pasaporte. | US-03 | P1 |
| FR-007 | El sistema DEBE permitir consultar pasaportes publicados sin autenticacion. | US-04 | P1 |
| FR-008 | El sistema DEBE excluir datos sensibles de productor, finca, usuarios internos y auditoria. | US-04 | P1 |
| FR-009 | El sistema DEBE incluir solo evidencias visibles publicamente. | US-04 | P1 |
| FR-010 | El sistema DEBE incluir solo certificaciones validadas, vigentes y visibles. | US-04 | P1 |
| FR-011 | El sistema DEBE permitir despublicar pasaportes sin eliminacion fisica. | US-05 | P2 |
| FR-012 | El sistema DEBE permitir consultar o descargar informacion del QR de un pasaporte propio. | US-06 | P1 |
| FR-013 | El sistema DEBE registrar auditoria de generacion, publicacion, despublicacion y generacion de QR. | Todas | P2 |

---

## 7. Requerimientos no funcionales

### 7.1 Rendimiento

| ID | Descripcion |
| --- | --- |
| RNF-001 | La generacion de pasaporte debe completarse en menos de 700 ms p95 sin generar QR sincrono obligatorio. |
| RNF-002 | La consulta publica de pasaporte debe completarse en menos de 700 ms p95 para lotes con hasta 500 eventos y evidencias. |
| RNF-003 | La generacion de QR debe completarse en menos de 500 ms p95 si se genera localmente. |

### 7.2 Seguridad y privacidad

| ID | Descripcion |
| --- | --- |
| RNF-004 | La URL publica debe usar identificador no secuencial para reducir enumeracion. |
| RNF-005 | La consulta publica no debe exponer IDs de usuarios internos, documentos, telefonos privados ni direcciones exactas por defecto. |
| RNF-006 | Los endpoints de generacion, publicacion y despublicacion deben requerir autenticacion. |
| RNF-007 | Productores solo pueden operar pasaportes de lotes propios. |
| RNF-008 | Administradores pueden despublicar pasaportes por cumplimiento o soporte. |

### 7.3 Consistencia de datos

| ID | Descripcion |
| --- | --- |
| RNF-009 | La creacion de pasaporte y asignacion de `public_id` debe ser atomica. |
| RNF-010 | La generacion de QR debe codificar exactamente `url_publica`. |
| RNF-011 | La consulta publica debe construir datos desde fuentes vigentes o una vista materializada consistente. |
| RNF-012 | Despublicar un pasaporte no debe eliminar lote, evidencias, certificaciones ni QR historico. |

### 7.4 Observabilidad

| ID | Descripcion |
| --- | --- |
| RNF-013 | Debe registrarse auditoria de cambios de estado y generacion de QR. |
| RNF-014 | Deben medirse consultas publicas por pasaporte para analitica futura. |
| RNF-015 | Deben registrarse errores de consolidacion de pasaporte sin exponer detalles al usuario publico. |

---

## 8. Criterios de exito

| ID | Criterio | Forma de medicion |
| --- | --- | --- |
| SC-001 | Un productor puede generar pasaporte para un lote propio. | Test de integracion de `POST /lots/{lotId}/passport`. |
| SC-002 | No se crean dos pasaportes activos para el mismo lote. | Test de concurrencia + constraint de base de datos. |
| SC-003 | Un pasaporte publicado puede consultarse sin autenticacion. | Test de `GET /public/passports/{publicId}`. |
| SC-004 | La respuesta publica no expone documentos, usuario interno ni direccion exacta. | Test de serializacion publica. |
| SC-005 | El QR codifica la URL publica correcta. | Test de generacion y lectura de contenido QR. |
| SC-006 | Pasaportes borrador o despublicados no exponen datos publicos. | Test de estado de pasaporte. |
| SC-007 | Certificaciones vencidas o no validadas no aparecen en el pasaporte. | Test de consolidacion publica. |

---

## 9. Contratos API sugeridos

### 9.1 Generar pasaporte de lote

`POST /lots/{lotId}/passport`

#### Headers

```http
Authorization: Bearer access-token
```

#### Response `201`

```json
{
  "id": "uuid",
  "lot_id": "uuid",
  "public_id": "agrt_8f4d2a9c",
  "url_publica": "https://agrotrace.example.com/p/agrt_8f4d2a9c",
  "estado": "BORRADOR"
}
```

### 9.2 Publicar pasaporte

`PATCH /passports/{passportId}/publish`

#### Headers

```http
Authorization: Bearer access-token
```

#### Response `200`

```json
{
  "id": "uuid",
  "estado": "PUBLICADO",
  "publicado_en": "2026-05-27T08:00:00.000Z",
  "url_publica": "https://agrotrace.example.com/p/agrt_8f4d2a9c"
}
```

### 9.3 Generar QR

`POST /passports/{passportId}/qr`

#### Headers

```http
Authorization: Bearer access-token
```

#### Request

```json
{
  "formato": "PNG"
}
```

#### Response `201`

```json
{
  "id": "uuid",
  "passport_id": "uuid",
  "contenido": "https://agrotrace.example.com/p/agrt_8f4d2a9c",
  "formato": "PNG",
  "archivo_url": "https://storage.example.com/qr/agrt_8f4d2a9c.png",
  "estado": "ACTIVO"
}
```

### 9.4 Consultar QR

`GET /passports/{passportId}/qr`

#### Headers

```http
Authorization: Bearer access-token
```

#### Response `200`

```json
{
  "id": "uuid",
  "contenido": "https://agrotrace.example.com/p/agrt_8f4d2a9c",
  "formato": "PNG",
  "archivo_url": "https://storage.example.com/qr/agrt_8f4d2a9c.png",
  "estado": "ACTIVO"
}
```

### 9.5 Consultar pasaporte publico

`GET /public/passports/{publicId}`

#### Response `200`

```json
{
  "public_id": "agrt_8f4d2a9c",
  "productor": {
    "nombre_publico": "Ana Gomez",
    "organizacion": "Cooperativa Sierra Verde",
    "descripcion": "Productora de cacao en el Magdalena."
  },
  "finca": {
    "nombre": "Finca Sierra Verde",
    "departamento": "Magdalena",
    "municipio": "Santa Marta",
    "vereda": "Minca",
    "area_hectareas": 12.5
  },
  "lote": {
    "codigo": "LOTE-001",
    "nombre": "Cacao Norte",
    "cultivo": "Cacao",
    "variedad": "Criollo",
    "fecha_siembra": "2026-02-15",
    "estado_actual": "CRECIMIENTO"
  },
  "trazabilidad": [
    {
      "estado_cultivo": "CRECIMIENTO",
      "fecha_evento": "2026-05-20",
      "observaciones": "El cultivo presenta buen desarrollo vegetativo."
    }
  ],
  "evidencias": [
    {
      "tipo": "FOTO",
      "url": "https://storage.example.com/evidence/photo.jpg",
      "descripcion": "Fotografia del crecimiento del cultivo"
    }
  ],
  "certificaciones": [
    {
      "tipo": "RAINFOREST_ALLIANCE",
      "entidad_certificadora": "Rainforest Alliance",
      "fecha_vencimiento": "2027-02-01",
      "estado": "VALIDADA"
    }
  ]
}
```

### 9.6 Despublicar pasaporte

`PATCH /passports/{passportId}/unpublish`

#### Headers

```http
Authorization: Bearer access-token
```

#### Request

```json
{
  "motivo_despublicacion": "Solicitud del productor"
}
```

#### Response `200`

```json
{
  "id": "uuid",
  "estado": "DESPUBLICADO",
  "despublicado_en": "2026-05-27T08:30:00.000Z"
}
```

---

## 10. Fuera de alcance

Los siguientes puntos quedan excluidos de esta feature inicial y deben especificarse por separado:

- Diseno frontend del pasaporte digital.
- Analitica avanzada de escaneos QR.
- Geolocalizacion del escaneo.
- Redireccion dinamica por campanas.
- Personalizacion visual del QR por marca.
- Versionado historico completo del pasaporte.
- Firma criptografica o blockchain del pasaporte.
- Descarga en PDF del pasaporte.
- Multiidioma del pasaporte.
- Cache distribuida o CDN para vista publica.

---

## Historial de cambios

| Version | Fecha | Autor | Descripcion |
| --- | --- | --- | --- |
| 1.0.0 | 27/05/2026 | Equipo AgroTrace | Version inicial de la especificacion de pasaporte digital y QR. |
