# Casos de Uso Backend

## Contexto

Este documento define los casos de uso iniciales para el backend de Nebbi. La prioridad es construir el MVP necesario para registrar trazabilidad agricola por lote, generar un pasaporte digital publico mediante QR, publicar experiencias turisticas y recibir solicitudes de compra internacional.

## Convenciones

- Prioridad `MVP`: debe implementarse en la primera version.
- Prioridad `Post-MVP`: queda documentado, pero no bloquea la primera entrega.
- Los casos de uso estan pensados para una API backend modular.
- Las respuestas publicas no deben exponer datos sensibles del productor, comprador o usuarios internos.
- Todo cambio relevante sobre un lote debe dejar historial auditable.

## Actores

| Actor | Descripcion |
| --- | --- |
| Productor | Registra fincas, lotes, estados de cultivo, evidencias y certificaciones. |
| Operador Turistico | Publica experiencias rurales asociadas a fincas. |
| Exportador | Gestiona productos, solicitudes comerciales y exportaciones. |
| Comprador Internacional | Consulta trazabilidad y envia solicitudes de compra. |
| Turista | Consulta pasaportes digitales y experiencias mediante QR o catalogo publico. |
| Certificador | Valida certificaciones y revisa evidencias. |
| Administrador | Supervisa usuarios, contenido y operacion general del sistema. |
| Sistema | Ejecuta procesos automaticos como generacion de pasaportes y QR. |

## Estados y Catalogos Base

### Estados de Cultivo

- `SIEMBRA`
- `CRECIMIENTO`
- `FLORACION`
- `FRUCTIFICACION`
- `MADURACION`
- `COSECHA`

### Tipos de Evidencia

- `FOTO`
- `VIDEO`
- `COMENTARIO`

### Tipos de Certificacion

- `FAIRTRADE`
- `RAINFOREST_ALLIANCE`

### Estados de Solicitud de Compra

- `RECIBIDA`
- `EN_REVISION`
- `APROBADA`
- `RECHAZADA`
- `EN_NEGOCIACION`

### Estados Logisticos

- `PREPARACION`
- `EMPAQUE`
- `PUERTO`
- `EMBARCADO`
- `EN_TRANSITO`
- `ENTREGADO`

## CU-001 Registrar Productor

**Prioridad:** MVP  
**Actor principal:** Productor  
**Objetivo:** Crear un perfil de productor dentro del sistema.

### Datos de Entrada

- Nombre completo.
- Tipo y numero de documento.
- Correo electronico.
- Telefono.
- Organizacion o asociacion.

### Flujo Principal

1. El productor envia sus datos de registro.
2. El sistema valida que el correo y documento no existan previamente.
3. El sistema crea el productor.
4. El sistema retorna el identificador del productor registrado.

### Reglas de Negocio

- El correo debe ser unico.
- El documento debe ser unico.
- El productor queda activo por defecto.
- En el MVP, el registro puede manejarse sin autenticacion completa si todavia no existe modulo de usuarios.

### Criterios de Aceptacion

- Dado un correo nuevo y documento nuevo, el sistema registra el productor.
- Dado un correo ya registrado, el sistema rechaza la solicitud.
- Dado un documento ya registrado, el sistema rechaza la solicitud.

## CU-002 Registrar Finca

**Prioridad:** MVP  
**Actor principal:** Productor  
**Objetivo:** Registrar una finca agricola asociada a un productor.

### Datos de Entrada

- Identificador del productor.
- Nombre de la finca.
- Ubicacion textual.
- Coordenadas GPS.
- Area.
- Fotografias.
- Descripcion.

### Flujo Principal

1. El productor envia los datos de la finca.
2. El sistema valida que el productor exista.
3. El sistema crea la finca asociada al productor.
4. El sistema retorna la finca creada.

### Reglas de Negocio

- Una finca siempre debe pertenecer a un productor.
- El area debe ser mayor que cero.
- Las coordenadas GPS son opcionales en MVP, pero recomendadas.

### Criterios de Aceptacion

- Dado un productor existente, el sistema permite crear una finca.
- Dado un productor inexistente, el sistema rechaza la creacion.
- Una finca creada queda disponible para asociar lotes y experiencias turisticas.

## CU-003 Crear Lote

**Prioridad:** MVP  
**Actor principal:** Productor  
**Objetivo:** Crear una unidad productiva dentro de una finca.

### Datos de Entrada

- Identificador de la finca.
- Codigo del lote.
- Nombre.
- Area.
- Cultivo.
- Variedad.
- Fecha de siembra.

### Flujo Principal

1. El productor selecciona una finca.
2. El productor registra los datos del lote.
3. El sistema valida que la finca exista.
4. El sistema valida que el codigo del lote no este repetido dentro de la finca.
5. El sistema crea el lote.
6. El sistema retorna el lote creado.

### Reglas de Negocio

- El codigo del lote debe ser unico por finca.
- El area debe ser mayor que cero.
- La fecha de siembra no debe ser futura.
- El lote debe iniciar sin pasaporte publicado hasta que se genere el pasaporte digital.

### Criterios de Aceptacion

- Dada una finca existente, el sistema permite crear lotes.
- Dado un codigo repetido dentro de la misma finca, el sistema rechaza la creacion.
- Dado un lote creado, el sistema permite registrar estados de cultivo y evidencias.

## CU-004 Actualizar Estado del Cultivo

**Prioridad:** MVP  
**Actor principal:** Productor  
**Objetivo:** Registrar el avance productivo de un lote.

### Datos de Entrada

- Identificador del lote.
- Estado del cultivo.
- Fecha del evento.
- Observaciones.

### Flujo Principal

1. El productor selecciona un lote.
2. El productor registra el nuevo estado del cultivo.
3. El sistema valida que el lote exista.
4. El sistema registra el estado en el historial del lote.
5. El sistema actualiza el estado actual del lote.

### Reglas de Negocio

- El estado debe pertenecer al catalogo de estados de cultivo.
- Cada actualizacion debe quedar registrada como evento historico.
- La fecha del evento no debe ser futura.
- El estado actual del lote corresponde al ultimo evento valido registrado.

### Criterios de Aceptacion

- Dado un lote existente, el sistema permite registrar un estado.
- Dado un estado no valido, el sistema rechaza la solicitud.
- Al consultar el lote, el sistema muestra su estado actual.
- Al consultar la trazabilidad, el sistema muestra el historial completo de estados.

## CU-005 Adjuntar Evidencias

**Prioridad:** MVP  
**Actor principal:** Productor  
**Objetivo:** Adjuntar evidencias al historial de un lote.

### Datos de Entrada

- Identificador del lote.
- Tipo de evidencia.
- Archivo o comentario.
- Descripcion.
- Fecha de captura o registro.

### Flujo Principal

1. El productor selecciona un lote.
2. El productor adjunta una fotografia, video o comentario.
3. El sistema valida que el lote exista.
4. El sistema almacena la evidencia.
5. El sistema asocia la evidencia al lote.

### Reglas de Negocio

- La evidencia debe pertenecer a un lote.
- En el MVP, la evidencia fotografica es obligatoria para soportar trazabilidad visual.
- Los archivos deben almacenarse en un servicio externo o carpeta configurada para almacenamiento.
- La URL publica de evidencia solo debe exponerse si esta marcada como visible.

### Criterios de Aceptacion

- Dado un lote existente, el sistema permite adjuntar evidencias.
- Dado un lote inexistente, el sistema rechaza la evidencia.
- Las evidencias visibles aparecen en el pasaporte digital del lote.

## CU-006 Registrar Certificacion

**Prioridad:** MVP  
**Actor principal:** Productor / Certificador  
**Objetivo:** Asociar certificaciones a una finca o lote.

### Datos de Entrada

- Tipo de certificacion.
- Entidad certificadora.
- Numero o codigo de certificacion.
- Fecha de emision.
- Fecha de vencimiento.
- Alcance: finca o lote.
- Documento soporte.

### Flujo Principal

1. El actor registra los datos de certificacion.
2. El sistema valida que el alcance exista.
3. El sistema valida el tipo de certificacion.
4. El sistema crea la certificacion.
5. El sistema asocia la certificacion a la finca o lote.

### Reglas de Negocio

- Solo se aceptan tipos de certificacion definidos en el catalogo.
- La fecha de vencimiento debe ser posterior a la fecha de emision.
- Una certificacion puede estar en estado pendiente si requiere validacion posterior.
- Las certificaciones vencidas deben mostrarse como no vigentes.

### Criterios de Aceptacion

- Dado un lote o finca existente, el sistema permite registrar certificaciones.
- Dado un tipo no soportado, el sistema rechaza la certificacion.
- El pasaporte digital muestra las certificaciones vigentes asociadas al lote.

## CU-007 Generar Pasaporte Digital

**Prioridad:** MVP  
**Actor principal:** Sistema  
**Objetivo:** Crear una vista publica consolidada con la trazabilidad del lote.

### Datos de Entrada

- Identificador del lote.

### Flujo Principal

1. El sistema recibe la solicitud de generacion del pasaporte.
2. El sistema valida que el lote exista.
3. El sistema consolida informacion del productor, finca, lote, estados, evidencias y certificaciones.
4. El sistema genera una URL publica unica.
5. El sistema marca el pasaporte como publicado.

### Reglas de Negocio

- Cada lote puede tener un unico pasaporte digital activo.
- El pasaporte debe usar un identificador publico no secuencial.
- El pasaporte no debe exponer documento, telefono privado o datos sensibles del productor.
- El pasaporte debe poder consultarse sin autenticacion.

### Criterios de Aceptacion

- Dado un lote existente, el sistema genera una URL publica de pasaporte.
- Dado un lote inexistente, el sistema rechaza la generacion.
- La URL publica permite consultar el resumen de trazabilidad del lote.

## CU-008 Generar Codigo QR

**Prioridad:** MVP  
**Actor principal:** Sistema  
**Objetivo:** Generar un codigo QR asociado al pasaporte digital del lote.

### Datos de Entrada

- Identificador del pasaporte digital.

### Flujo Principal

1. El sistema consulta el pasaporte digital.
2. El sistema obtiene la URL publica.
3. El sistema genera el codigo QR.
4. El sistema almacena la imagen o representacion del QR.
5. El sistema retorna la URL o archivo descargable del QR.

### Reglas de Negocio

- El QR siempre debe apuntar a la URL publica del pasaporte.
- Si el pasaporte no existe, el QR no puede generarse.
- Si ya existe un QR activo, el sistema puede retornarlo sin regenerarlo.

### Criterios de Aceptacion

- Dado un pasaporte existente, el sistema genera un QR valido.
- Dado un pasaporte inexistente, el sistema rechaza la generacion.
- Al escanear el QR, se abre el pasaporte digital publico.

## CU-009 Escanear QR y Consultar Pasaporte

**Prioridad:** MVP  
**Actor principal:** Turista / Comprador Internacional  
**Objetivo:** Consultar publicamente la trazabilidad de un lote.

### Datos de Entrada

- Identificador publico del pasaporte.

### Flujo Principal

1. El usuario escanea el QR.
2. El navegador abre la URL publica del pasaporte.
3. El sistema consulta el pasaporte por su identificador publico.
4. El sistema retorna la informacion publica consolidada.

### Reglas de Negocio

- La consulta publica no requiere autenticacion.
- El sistema solo debe retornar pasaportes publicados.
- No se deben exponer datos internos, sensibles o privados.

### Criterios de Aceptacion

- Dado un pasaporte publicado, el sistema retorna la informacion publica.
- Dado un pasaporte no publicado o inexistente, el sistema retorna un error controlado.
- El usuario puede ver productor, finca, cultivo, estado actual, certificaciones y evidencias publicas.

## CU-010 Registrar Experiencia Turistica

**Prioridad:** MVP  
**Actor principal:** Operador Turistico  
**Objetivo:** Publicar una experiencia turistica asociada a una finca.

### Datos de Entrada

- Identificador de finca.
- Nombre de la experiencia.
- Descripcion.
- Duracion.
- Precio.
- Capacidad.
- Fotografias.
- Disponibilidad.

### Flujo Principal

1. El operador registra los datos de la experiencia.
2. El sistema valida que la finca exista.
3. El sistema crea la experiencia turistica.
4. El sistema la deja disponible en el catalogo si esta marcada como publicada.

### Reglas de Negocio

- Una experiencia turistica debe pertenecer a una finca.
- La capacidad debe ser mayor que cero.
- El precio no puede ser negativo.
- Solo las experiencias publicadas aparecen en el catalogo publico.

### Criterios de Aceptacion

- Dada una finca existente, el sistema permite crear una experiencia.
- Dada una finca inexistente, el sistema rechaza la creacion.
- Las experiencias publicadas aparecen en el catalogo publico.

## CU-011 Consultar Experiencias

**Prioridad:** MVP  
**Actor principal:** Turista  
**Objetivo:** Explorar experiencias turisticas disponibles.

### Datos de Entrada

- Filtros opcionales por ubicacion, finca, cultivo o rango de precio.

### Flujo Principal

1. El turista solicita el catalogo de experiencias.
2. El sistema consulta experiencias publicadas.
3. El sistema aplica filtros si existen.
4. El sistema retorna el listado.

### Reglas de Negocio

- Solo se muestran experiencias publicadas.
- El catalogo debe ser publico.
- El resultado debe incluir informacion suficiente para contactar o reservar en fases posteriores.

### Criterios de Aceptacion

- El sistema retorna experiencias publicadas.
- El sistema no retorna experiencias inactivas o no publicadas.
- Los filtros enviados reducen el resultado correctamente.

## CU-012 Reservar Experiencia

**Prioridad:** Post-MVP  
**Actor principal:** Turista  
**Objetivo:** Reservar una actividad turistica.

### Datos de Entrada

- Identificador de experiencia.
- Nombre del visitante.
- Correo.
- Telefono.
- Fecha solicitada.
- Numero de personas.

### Flujo Principal

1. El turista selecciona una experiencia.
2. El turista envia los datos de reserva.
3. El sistema valida disponibilidad.
4. El sistema registra la reserva.
5. El sistema retorna confirmacion.

### Reglas de Negocio

- La cantidad de personas no debe superar la capacidad disponible.
- La fecha solicitada debe ser futura.
- La experiencia debe estar publicada.

### Criterios de Aceptacion

- Dada disponibilidad suficiente, el sistema registra la reserva.
- Dada capacidad insuficiente, el sistema rechaza la reserva.

## CU-013 Publicar Producto

**Prioridad:** MVP  
**Actor principal:** Productor / Exportador  
**Objetivo:** Publicar productos disponibles para comercializacion.

### Datos de Entrada

- Nombre.
- Precio.
- Cantidad disponible.
- Unidad de medida.
- Identificador de lote asociado.

### Flujo Principal

1. El actor registra los datos del producto.
2. El sistema valida el lote asociado.
3. El sistema crea el producto.
4. El sistema lo deja disponible en catalogo si esta publicado.

### Reglas de Negocio

- Un producto comercializable debe estar asociado a un lote.
- El precio no puede ser negativo.
- La cantidad disponible debe ser mayor que cero.

### Criterios de Aceptacion

- Dado un lote existente, el sistema permite publicar un producto.
- Dado un lote inexistente, el sistema rechaza la publicacion.

## CU-014 Consultar Catalogo de Productos

**Prioridad:** Post-MVP  
**Actor principal:** Comprador Internacional  
**Objetivo:** Explorar productos exportables.

### Datos de Entrada

- Filtros opcionales por cultivo, certificacion, ubicacion o disponibilidad.

### Flujo Principal

1. El comprador solicita el catalogo de productos.
2. El sistema consulta productos publicados.
3. El sistema aplica filtros.
4. El sistema retorna el listado.

### Reglas de Negocio

- Solo se muestran productos publicados.
- El catalogo debe incluir acceso al pasaporte digital del lote asociado.

### Criterios de Aceptacion

- El sistema retorna productos publicados.
- Cada producto incluye informacion basica y referencia de trazabilidad.

## CU-015 Solicitar Compra Internacional

**Prioridad:** MVP  
**Actor principal:** Comprador Internacional  
**Objetivo:** Registrar una intencion de compra desde el pasaporte o catalogo.

### Datos de Entrada

- Producto o lote de interes.
- Pais.
- Cantidad solicitada.
- Unidad de medida.
- Nombre de contacto.
- Empresa.
- Correo.
- Telefono.
- Mensaje.

### Flujo Principal

1. El comprador consulta un lote o producto.
2. El comprador envia una solicitud de compra.
3. El sistema valida los datos de contacto y cantidad.
4. El sistema registra la solicitud en estado `RECIBIDA`.
5. El sistema retorna confirmacion.

### Reglas de Negocio

- La cantidad solicitada debe ser mayor que cero.
- El correo de contacto es obligatorio.
- La solicitud puede asociarse directamente a un lote en el MVP aunque no exista modulo completo de productos.
- El estado inicial siempre debe ser `RECIBIDA`.

### Criterios de Aceptacion

- Dado un lote o producto valido, el sistema registra la solicitud.
- Dado un correo invalido, el sistema rechaza la solicitud.
- Dada una cantidad menor o igual a cero, el sistema rechaza la solicitud.

## CU-016 Gestionar Solicitudes

**Prioridad:** Post-MVP  
**Actor principal:** Exportador  
**Objetivo:** Revisar y actualizar solicitudes de compra.

### Datos de Entrada

- Identificador de solicitud.
- Nuevo estado.
- Observaciones.

### Flujo Principal

1. El exportador consulta las solicitudes recibidas.
2. El exportador selecciona una solicitud.
3. El exportador actualiza su estado.
4. El sistema registra el cambio.

### Reglas de Negocio

- Solo se permiten estados definidos en el catalogo.
- Todo cambio de estado debe quedar auditado.

### Criterios de Aceptacion

- Dada una solicitud existente, el sistema permite actualizar su estado.
- Dado un estado no valido, el sistema rechaza el cambio.

## CU-017 Registrar Exportacion

**Prioridad:** Post-MVP  
**Actor principal:** Exportador  
**Objetivo:** Crear un proceso de exportacion asociado a un producto o solicitud aprobada.

### Datos de Entrada

- Producto.
- Solicitud de compra.
- Destino.
- Cantidad.
- Fecha estimada.

### Flujo Principal

1. El exportador selecciona una solicitud aprobada.
2. El exportador registra los datos de exportacion.
3. El sistema crea la exportacion.
4. El sistema inicia el estado logistico en `PREPARACION`.

### Reglas de Negocio

- La cantidad debe ser mayor que cero.
- La exportacion debe tener destino.
- En Post-MVP, una exportacion deberia originarse desde una solicitud aprobada.

### Criterios de Aceptacion

- Dada una solicitud aprobada, el sistema permite crear una exportacion.
- El primer estado logistico queda registrado automaticamente.

## CU-018 Actualizar Estado Logistico

**Prioridad:** Post-MVP  
**Actor principal:** Exportador  
**Objetivo:** Registrar el avance logistico de una exportacion.

### Datos de Entrada

- Identificador de exportacion.
- Estado logistico.
- Fecha del evento.
- Observaciones.

### Flujo Principal

1. El exportador selecciona una exportacion.
2. El exportador registra el nuevo estado logistico.
3. El sistema valida el estado.
4. El sistema registra el evento en el historial logistico.

### Reglas de Negocio

- El estado debe pertenecer al catalogo logistico.
- Todo cambio debe quedar en historial.
- La fecha del evento no debe ser futura.

### Criterios de Aceptacion

- Dada una exportacion existente, el sistema registra el estado logistico.
- El historial logistico queda disponible para consulta.

## CU-019 Consultar Trazabilidad Logistica

**Prioridad:** Post-MVP  
**Actor principal:** Comprador Internacional  
**Objetivo:** Consultar el estado de envio de una exportacion.

### Datos de Entrada

- Identificador publico o codigo de seguimiento.

### Flujo Principal

1. El comprador consulta el seguimiento.
2. El sistema busca la exportacion.
3. El sistema retorna el estado actual y el historial logistico.

### Reglas de Negocio

- La consulta debe usar un identificador publico no secuencial.
- No se deben exponer datos internos del exportador o comprador.

### Criterios de Aceptacion

- Dado un codigo valido, el sistema retorna el historial logistico.
- Dado un codigo invalido, el sistema retorna un error controlado.

## Resumen MVP

Los casos de uso que deben implementarse primero son:

1. `CU-001` Registrar Productor.
2. `CU-002` Registrar Finca.
3. `CU-003` Crear Lote.
4. `CU-004` Actualizar Estado del Cultivo.
5. `CU-005` Adjuntar Evidencias.
6. `CU-006` Registrar Certificacion.
7. `CU-007` Generar Pasaporte Digital.
8. `CU-008` Generar Codigo QR.
9. `CU-009` Escanear QR y Consultar Pasaporte.
10. `CU-010` Registrar Experiencia Turistica.
11. `CU-011` Consultar Experiencias.
12. `CU-015` Solicitar Compra Internacional.

## Modulos Backend Derivados

- `producers`: productores.
- `farms`: fincas.
- `lots`: lotes, estados de cultivo e historial.
- `evidence`: evidencias y archivos.
- `certifications`: certificaciones.
- `passports`: pasaportes digitales y consulta publica.
- `qr`: generacion y almacenamiento de codigos QR.
- `tourism`: experiencias turisticas.
- `purchase-requests`: solicitudes de compra internacional.
- `exports`: exportaciones y trazabilidad logistica para Post-MVP.
