# Feature Specification: Experiencias Turisticas

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

Las experiencias turisticas conectan el origen agricola del producto con visitantes, aliados comerciales y compradores que quieren conocer el territorio. En AgroTrace Magdalena, una experiencia se asocia a una finca y puede aparecer en catalogos publicos o en el pasaporte digital de lotes relacionados.

Esta feature define la gestion backend de experiencias turisticas: creacion, consulta, publicacion, actualizacion, fotografias y exposicion publica. La reserva transaccional queda fuera del MVP, pero el catalogo debe entregar informacion suficiente para contacto o coordinacion posterior.

El objetivo del MVP es permitir que un productor, operador turistico o administrador registre experiencias asociadas a fincas activas, controle su publicacion y permita que turistas consulten un catalogo publico filtrable.

---

## 2. Actores

| ID | Actor | Tipo | Descripcion |
| --- | --- | --- | --- |
| ACT-01 | Productor | Primario | Publica experiencias asociadas a sus fincas. |
| ACT-02 | Operador Turistico | Primario | Gestiona experiencias rurales autorizadas sobre fincas. |
| ACT-03 | Administrador | Primario | Audita, publica, despublica o corrige experiencias. |
| ACT-04 | Turista | Primario | Consulta catalogo publico y detalle de experiencias. |
| ACT-05 | Sistema de Fincas | Secundario | Valida existencia, propiedad y estado de la finca. |
| ACT-06 | Sistema de Pasaporte Digital | Secundario | Puede consumir experiencias asociadas a la finca del lote. |

> En el MVP, si no existe un rol independiente de operador turistico, el rol `PRODUCTOR` puede crear experiencias sobre sus propias fincas y `ADMIN` puede gestionar todas.

---

## 3. Entidades y modelo de datos

### 3.1 Experiencia Turistica (`TourismExperience`)

Representa una actividad turistica asociada a una finca.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `farm_id` | `UUID (FK)` | Debe apuntar a una finca `ACTIVA`. | Si |
| `creada_por` | `UUID (FK)` | Usuario productor, operador o admin. | Si |
| `nombre` | `String` | Minimo 3 caracteres, maximo 140. | Si |
| `descripcion` | `String` | Maximo 2000 caracteres. | Si |
| `duracion_minutos` | `Integer` | Mayor que 0. | Si |
| `precio` | `Decimal` | Mayor o igual que 0. Maximo 2 decimales. | Si |
| `moneda` | `String` | ISO 4217. Default `COP`. | Si |
| `capacidad_maxima` | `Integer` | Mayor que 0. | Si |
| `idiomas` | `String[]` | Opcional. Ejemplo: `ES`, `EN`. | No |
| `incluye` | `String[]` | Lista corta de beneficios incluidos. | No |
| `recomendaciones` | `String` | Maximo 1000 caracteres. | No |
| `contacto_publico` | `String` | Telefono, correo o canal publico autorizado. | No |
| `estado` | `Enum` | Valores definidos en `TourismExperienceStatus`. Default `BORRADOR`. | Si |
| `publicada_en` | `Timestamp` | UTC. Requerido cuando estado es `PUBLICADA`. | No |
| `despublicada_en` | `Timestamp` | UTC. Requerido cuando estado es `DESPUBLICADA`. | No |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. Inmutable. | Si |
| `actualizado_en` | `Timestamp` | Actualizado automaticamente. UTC. | Si |

### 3.2 Fotografia de Experiencia (`TourismExperiencePhoto`)

Representa una imagen asociada a la experiencia.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. Inmutable. | Si |
| `experience_id` | `UUID (FK)` | Experiencia propietaria de la fotografia. | Si |
| `url` | `String` | URL valida del archivo almacenado. | Si |
| `descripcion` | `String` | Maximo 255 caracteres. | No |
| `es_portada` | `Boolean` | Default `false`. Solo una portada por experiencia. | Si |
| `orden` | `Integer` | Mayor o igual que 0. | Si |
| `creado_en` | `Timestamp` | Generado automaticamente. UTC. | Si |

### 3.3 Disponibilidad declarativa (`TourismAvailability`)

Representa reglas simples de disponibilidad para el MVP.

| Atributo | Tipo | Restricciones | Requerido |
| --- | --- | --- | :---: |
| `id` | `UUID` | Generado automaticamente. | Si |
| `experience_id` | `UUID (FK)` | Experiencia asociada. | Si |
| `dias_semana` | `String[]` | Valores `LUNES` a `DOMINGO`. | Si |
| `hora_inicio` | `Time` | Hora local. | No |
| `hora_fin` | `Time` | Debe ser posterior a `hora_inicio` si ambas existen. | No |
| `nota` | `String` | Maximo 500 caracteres. | No |

### 3.4 Enumeraciones

#### `TourismExperienceStatus`

- `BORRADOR`
- `PUBLICADA`
- `DESPUBLICADA`
- `INACTIVA`

### 3.5 Relaciones

```text
Farm (1) ---- (N) TourismExperience
TourismExperience (1) ---- (N) TourismExperiencePhoto
TourismExperience (1) ---- (N) TourismAvailability
TourismExperience (1) ---- (N) AuditLog
User (1) ---- (N) TourismExperience [via creada_por]
```

### 3.6 Datos publicos vs privados

| Campo | Uso interno | Visible en catalogo publico |
| --- | :---: | :---: |
| `nombre` | Si | Si |
| `descripcion` | Si | Si |
| `duracion_minutos` | Si | Si |
| `precio` / `moneda` | Si | Si |
| `capacidad_maxima` | Si | Si |
| `contacto_publico` | Si | Si, si fue autorizado |
| `creada_por` | Si | No |
| Auditoria | Si | No |

---

## 4. User stories y escenarios de aceptacion

### US-01 - Crear experiencia turistica `P1`

**Como** productor u operador turistico,  
**quiero** registrar una experiencia asociada a una finca,  
**para que** pueda publicarla en el catalogo turistico.

**Por que P1**: Es el punto de entrada para activar turismo rural en el MVP.

**Test independiente**: Un productor con finca activa envia datos validos. El test es exitoso si se crea una experiencia en estado `BORRADOR`.

#### Escenario 1 - Creacion correcta

```gherkin
Given  que existe una finca ACTIVA propia
When   el productor envia nombre, descripcion, duracion, precio y capacidad validos
Then   el sistema crea la experiencia
And    la asocia a la finca
And    la deja en estado BORRADOR
```

#### Escenario 2 - Finca inexistente o inactiva

```gherkin
Given  que la finca no existe o esta INACTIVA
When   el actor intenta crear una experiencia
Then   el sistema rechaza la solicitud
And    no persiste la experiencia
```

### US-02 - Adjuntar fotografias `P1`

**Como** gestor de una experiencia,  
**quiero** adjuntar fotografias,  
**para que** el catalogo publico tenga soporte visual.

**Por que P1**: El turismo requiere imagenes para que el catalogo sea usable.

**Test independiente**: Una experiencia propia recibe una fotografia valida. El test es exitoso si queda asociada y puede marcarse como portada.

```gherkin
Given  que existe una experiencia propia
When   el gestor adjunta una fotografia valida
Then   el sistema crea TourismExperiencePhoto
And    permite marcarla como portada
```

### US-03 - Publicar experiencia `P1`

**Como** gestor autorizado,  
**quiero** publicar una experiencia,  
**para que** aparezca en el catalogo publico.

**Por que P1**: Solo las experiencias publicadas deben exponerse al publico.

**Test independiente**: Una experiencia en borrador con datos minimos se publica. El test es exitoso si aparece en `GET /public/tourism/experiences`.

#### Escenario 1 - Publicacion correcta

```gherkin
Given  que existe una experiencia BORRADOR con finca activa
And    tiene nombre, descripcion, duracion, precio y capacidad validos
When   el gestor solicita publicarla
Then   el sistema cambia estado a PUBLICADA
And    registra publicada_en
And    la incluye en el catalogo publico
```

#### Escenario 2 - Datos minimos incompletos

```gherkin
Given  que existe una experiencia BORRADOR sin descripcion
When   el gestor solicita publicarla
Then   el sistema rechaza la publicacion
And    informa los campos requeridos faltantes
```

### US-04 - Consultar catalogo publico `P1`

**Como** turista,  
**quiero** consultar experiencias publicadas,  
**para que** pueda descubrir actividades rurales disponibles.

**Por que P1**: Es el flujo publico principal.

**Test independiente**: El catalogo publico retorna solo experiencias `PUBLICADA` y permite filtros basicos.

```gherkin
Given  que existen experiencias publicadas y borradores
When   el turista consulta el catalogo publico
Then   el sistema retorna solo experiencias PUBLICADA
And    permite filtrar por municipio, finca, cultivo relacionado o rango de precio
```

### US-05 - Consultar detalle publico `P1`

**Como** turista,  
**quiero** ver el detalle de una experiencia,  
**para que** pueda decidir si contacta al operador.

**Test independiente**: Una experiencia publicada puede consultarse por ID publico. El test es exitoso si no expone campos internos.

```gherkin
Given  que existe una experiencia PUBLICADA
When   el turista consulta su detalle publico
Then   el sistema retorna descripcion, precio, duracion, capacidad, disponibilidad, fotos y finca publica
And    no retorna auditoria ni identificadores internos sensibles
```

### US-06 - Actualizar experiencia `P2`

**Como** gestor autorizado,  
**quiero** actualizar datos editables de una experiencia,  
**para que** la informacion publica se mantenga vigente.

**Test independiente**: Una experiencia propia actualiza precio y descripcion sin cambiar finca. El test es exitoso si el cambio queda persistido y auditado.

```gherkin
Given  que existe una experiencia propia
When   el gestor actualiza descripcion o precio
Then   el sistema persiste los cambios
And    registra auditoria
```

### US-07 - Despublicar experiencia `P2`

**Como** gestor autorizado,  
**quiero** despublicar una experiencia,  
**para que** deje de aparecer en el catalogo publico.

**Test independiente**: Una experiencia publicada se despublica. El test es exitoso si deja de aparecer en respuestas publicas.

```gherkin
Given  que existe una experiencia PUBLICADA
When   el gestor solicita despublicarla
Then   el sistema cambia estado a DESPUBLICADA
And    registra despublicada_en
And    la excluye del catalogo publico
```

---

## 5. Edge cases

| ID | Caso | Resultado esperado |
| --- | --- | --- |
| 1 | Precio igual a cero | Permitido para experiencias gratuitas. |
| 2 | Precio negativo | Rechazado. |
| 3 | Capacidad igual a cero | Rechazado. |
| 4 | Finca inactiva con experiencia publicada | La experiencia debe ocultarse o bloquear futuras publicaciones. |
| 5 | Usuario intenta crear experiencia en finca ajena | Rechazado con `403`. |
| 6 | Dos fotos marcadas como portada | Solo una queda como portada. |
| 7 | Experiencia sin fotografias | Puede existir en borrador; publicacion puede permitirse por configuracion MVP. |
| 8 | Filtro publico sin resultados | Respuesta `200` con lista vacia. |
| 9 | Contacto publico no autorizado | No se expone en catalogo. |
| 10 | Actualizacion concurrente de estado | Se debe evitar perdida de actualizaciones con control transaccional. |

---

## 6. Requerimientos funcionales

| ID | Descripcion | US relacionada | Prioridad |
| --- | --- | --- | --- |
| FR-001 | El sistema DEBE permitir crear experiencias asociadas a fincas activas. | US-01 | P1 |
| FR-002 | El sistema DEBE validar propiedad o permisos sobre la finca. | US-01 | P1 |
| FR-003 | El sistema DEBE validar precio mayor o igual a cero. | US-01 | P1 |
| FR-004 | El sistema DEBE validar capacidad mayor que cero. | US-01 | P1 |
| FR-005 | El sistema DEBE permitir adjuntar fotografias a experiencias. | US-02 | P1 |
| FR-006 | El sistema DEBE permitir publicar experiencias completas. | US-03 | P1 |
| FR-007 | El sistema DEBE exponer catalogo publico solo con experiencias publicadas. | US-04 | P1 |
| FR-008 | El sistema DEBE soportar filtros publicos por municipio, finca y rango de precio. | US-04 | P1 |
| FR-009 | El sistema DEBE exponer detalle publico sin campos internos. | US-05 | P1 |
| FR-010 | El sistema DEBE permitir actualizar datos editables por gestor autorizado. | US-06 | P2 |
| FR-011 | El sistema DEBE permitir despublicar experiencias. | US-07 | P2 |
| FR-012 | El sistema DEBE registrar auditoria de creacion, publicacion, cambios y despublicacion. | Todas | P2 |

---

## 7. Requerimientos no funcionales

| ID | Descripcion |
| --- | --- |
| RNF-001 | La consulta del catalogo publico debe responder en menos de 700 ms p95 para 1000 experiencias. |
| RNF-002 | Las respuestas publicas deben ser paginadas. |
| RNF-003 | Las imagenes deben almacenarse fuera de la base de datos como URL o referencia de objeto. |
| RNF-004 | Los endpoints publicos no deben requerir autenticacion. |
| RNF-005 | Los endpoints de gestion deben requerir JWT valido. |
| RNF-006 | La exposicion publica no debe incluir datos privados de usuarios ni auditoria. |
| RNF-007 | Los cambios de estado deben ser atomicos y auditables. |
| RNF-008 | Los filtros publicos deben tener indices o estrategia de consulta eficiente. |

---

## 8. Criterios de exito

| ID | Criterio | Verificacion |
| --- | --- | --- |
| SC-001 | Un productor puede crear experiencia sobre finca propia activa. | Test de integracion `POST /tourism/experiences`. |
| SC-002 | No se puede crear experiencia sobre finca ajena o inactiva. | Test de autorizacion y estado. |
| SC-003 | Una experiencia publicada aparece en catalogo publico. | Test de `GET /public/tourism/experiences`. |
| SC-004 | Experiencias borrador, despublicadas o inactivas no aparecen publicamente. | Test de visibilidad publica. |
| SC-005 | Los filtros publicos reducen resultados correctamente. | Test parametrizado por filtros. |
| SC-006 | El detalle publico no expone campos internos. | Test de contrato de respuesta. |

---

## 9. Contratos API sugeridos

### 9.1 Crear experiencia

```http
POST /tourism/experiences
Authorization: Bearer <access_token>
Content-Type: application/json
```

```json
{
  "farm_id": "7ca4d74b-2e42-4a18-9d8f-0f97a0a1f101",
  "nombre": "Ruta del cacao y la biodiversidad",
  "descripcion": "Recorrido guiado por cultivo, cosecha y transformacion artesanal.",
  "duracion_minutos": 180,
  "precio": 85000,
  "moneda": "COP",
  "capacidad_maxima": 12,
  "idiomas": ["ES"],
  "incluye": ["Guia local", "Degustacion", "Recorrido por finca"],
  "contacto_publico": "+57 300 000 0000"
}
```

```json
{
  "id": "c3645d1d-37b2-44a4-8d7a-99a7167e4378",
  "farm_id": "7ca4d74b-2e42-4a18-9d8f-0f97a0a1f101",
  "nombre": "Ruta del cacao y la biodiversidad",
  "estado": "BORRADOR",
  "creado_en": "2026-05-27T09:30:00Z"
}
```

### 9.2 Adjuntar fotografia

```http
POST /tourism/experiences/{experienceId}/photos
Authorization: Bearer <access_token>
Content-Type: application/json
```

```json
{
  "url": "https://storage.example.com/tourism/ruta-cacao.jpg",
  "descripcion": "Visitantes en el cultivo de cacao",
  "es_portada": true,
  "orden": 0
}
```

### 9.3 Publicar experiencia

```http
POST /tourism/experiences/{experienceId}/publish
Authorization: Bearer <access_token>
```

```json
{
  "id": "c3645d1d-37b2-44a4-8d7a-99a7167e4378",
  "estado": "PUBLICADA",
  "publicada_en": "2026-05-27T09:45:00Z"
}
```

### 9.4 Catalogo publico

```http
GET /public/tourism/experiences?municipio=Santa%20Marta&precio_max=100000&page=1&page_size=20
```

```json
{
  "items": [
    {
      "id": "c3645d1d-37b2-44a4-8d7a-99a7167e4378",
      "nombre": "Ruta del cacao y la biodiversidad",
      "descripcion": "Recorrido guiado por cultivo, cosecha y transformacion artesanal.",
      "duracion_minutos": 180,
      "precio": 85000,
      "moneda": "COP",
      "capacidad_maxima": 12,
      "municipio": "Santa Marta",
      "finca": {
        "nombre": "Finca La Esperanza"
      },
      "portada_url": "https://storage.example.com/tourism/ruta-cacao.jpg"
    }
  ],
  "page": 1,
  "page_size": 20,
  "total": 1
}
```

### 9.5 Detalle publico

```http
GET /public/tourism/experiences/{experienceId}
```

```json
{
  "id": "c3645d1d-37b2-44a4-8d7a-99a7167e4378",
  "nombre": "Ruta del cacao y la biodiversidad",
  "descripcion": "Recorrido guiado por cultivo, cosecha y transformacion artesanal.",
  "duracion_minutos": 180,
  "precio": 85000,
  "moneda": "COP",
  "capacidad_maxima": 12,
  "idiomas": ["ES"],
  "incluye": ["Guia local", "Degustacion", "Recorrido por finca"],
  "contacto_publico": "+57 300 000 0000",
  "finca": {
    "nombre": "Finca La Esperanza",
    "municipio": "Santa Marta",
    "departamento": "Magdalena"
  },
  "fotografias": [
    {
      "url": "https://storage.example.com/tourism/ruta-cacao.jpg",
      "descripcion": "Visitantes en el cultivo de cacao",
      "es_portada": true
    }
  ],
  "disponibilidad": [
    {
      "dias_semana": ["SABADO", "DOMINGO"],
      "hora_inicio": "08:00",
      "hora_fin": "12:00",
      "nota": "Sujeto a confirmacion previa."
    }
  ]
}
```

### 9.6 Despublicar experiencia

```http
POST /tourism/experiences/{experienceId}/unpublish
Authorization: Bearer <access_token>
Content-Type: application/json
```

```json
{
  "motivo": "Actualizacion temporal de disponibilidad."
}
```

---

## 10. Fuera de alcance

- Motor de reservas con pago en linea.
- Calendario transaccional con cupos por fecha.
- Integracion con pasarelas de pago.
- Confirmaciones por correo o WhatsApp.
- Comisiones, liquidaciones o facturacion.
- Reviews, calificaciones o comentarios publicos.
- Marketplace avanzado de operadores turisticos.
