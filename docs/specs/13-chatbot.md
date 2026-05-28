# Feature Specification: Chatbot de Asistencia

**Version**: 1.0.0  
**Creado**: 28/05/2026  
**Actualizado**: 28/05/2026  
**Estado**: Borrador  
**Autor**: Equipo AgroTrace  
**Revisor**: Pendiente

---

## Indice

1. [Contexto y objetivo](#1-contexto-y-objetivo)
2. [Actores](#2-actores)
3. [Funcionalidades principales](#3-funcionalidades-principales)
4. [Guias por actor](#4-guias-por-actor)
5. [Conocimiento de certificaciones](#5-conocimiento-de-certificaciones)
6. [Diseño de conversacion](#6-diseño-de-conversacion)
7. [Arquitectura tecnica](#7-arquitectura-tecnica)
8. [Modelos y prompt engineering](#8-modelos-y-prompt-engineering)
9. [Requerimientos no funcionales](#9-requerimientos-no-funcionales)
10. [Criterios de exito](#10-criterios-de-exito)
11. [Prototipo MVP](#11-prototipo-mvp)
12. [Fuera de alcance](#12-fuera-de-alcance)

---

## 1. Contexto y objetivo

AgroTrace Magdalena es una plataforma con multiples actores y funcionalidades: productores que registran fincas y lotes, exportadores que gestionan compras internacionales, turistas que escanean codigos QR, y certificadores que validan origenes. La navegacion y comprension de todas estas capacidades puede resultar compleja para usuarios nuevos.

Esta feature define un chatbot inteligente integrado en la aplicacion web que funciona como guia conversacional para todos los actores de la plataforma. El objetivo es reducir la curva de aprendizaje, responder preguntas frecuentes, guiar procesos y facilitar la interaccion con la plataforma sin necesidad de consultar documentacion externa.

El chatbot no reemplaza atencion humana ni ejecuta transacciones en nombre del usuario. Actua como orientador contextual que interpreta la pregunta y dirige al usuario hacia la funcionalidad adecuada o explica conceptos relevantes sobre certificaciones, trazabilidad y la cadena de valor.

---

## 2. Actores

| ID | Actor | Rol en el chatbot |
| --- | --- | --- |
| ACT-01 | Productor | Recibe guias sobre registro de finca, lote, estado de cultivo, evidencias, certificaciones y como contactarse con exportadores. |
| ACT-02 | Exportador | Recibe guias sobre comunicacion con productores, origen de productos, adquisicion de productos y conexion con otros agentes. |
| ACT-03 | Operador Turistico | Recibe guias sobre publicacion de experiencias turisticas y promocion de fincas. |
| ACT-04 | Comprador Internacional | Recibe guias sobre consulta de trazabilidad, envio de solicitudes de compra y seguimiento logistico. |
| ACT-05 | Turista | Recibe guias sobre escaneo de QR, consulta de pasaporte digital y exploracion de experiencias. |
| ACT-06 | Certificador | Recibe guias sobre proceso de validacion de certificaciones y revision de evidencias. |
| ACT-07 | Administrador | Recibe guias sobre supervision de usuarios, contenido y operacion general del sistema. |

---

## 3. Funcionalidades principales

### 3.1 Guia contextual de navegacion

El chatbot responde preguntas sobre como llegar a una funcionalidad especifica dentro de la plataforma. Cada respuesta incluye un direcionamineto claro hacia la seccion o accion correspondiente.

**Ejemplos de preguntas:**

- Donde puedo registrar mi finca?
- Como creo un lote para mi cultivo?
- Donde subo las fotos de mi cosecha?
- Como puedo ver las solicitudes de compra que me han llegado?

### 3.2 Guia de procesos

El chatbot explica los pasos necesarios para completar procesos dentro de la plataforma. Las explicaciones son concisas y orientadas a la accion.

**Ejemplos:**

- Como registro una certificacion Fairtrade?
- Que debo hacer para que mi producto aparezca en el catalogo?
- Como funciona el proceso de validacion de una certificacion?

### 3.3 Informacion sobre certificaciones

El chatbot tiene conocimiento profundo sobre Fairtrade y Rainforest Alliance, incluyendo criterios, beneficios, proceso de obtencion y como se reflejan en el pasaporte digital.

**Ejemplos:**

- Que es la certificacion Fairtrade y que beneficios me da?
- Como obtener Rainforest Alliance para mi finca?
- Cuanto tiempo dura el proceso de validacion de una certificacion?
- Que datos aparecen en el pasaporte digital sobre mis certificaciones?

### 3.4 Guia de comunicacion entre actores

El chatbot facilita la conexion entre actores de la cadena de valor. Orienta sobre como producers y exportadores pueden comunicarse.

**Ejemplos:**

- Como puede contactarme un exportador conmigo?
- Donde veo los mensajes o solicitudes de compradores internacionales?
- Como puedo ofrecer mis productos a exportadores?

### 3.5 Soporte para errores comunes

El chatbot detecta errores frecuentes y orienta al usuario sobre como corregirlos. No ejecuta correcciones automaticas sino que guia verbalmente.

---

## 4. Guias por actor

### 4.1 Productor

| Escenario | Que resuelve el chatbot |
| --- | --- |
| Registro de finca | Explica que debe ir a la seccion Mis Fincas, seleccionar Nueva Finca y completar los campos de nombre, ubicacion, coordenadas y descripcion. |
| Registro de lote | Explica que desde la finca debe seleccionar Crear Lote, ingresar codigo, nombre, area, cultivo, variedad y fecha de siembra. |
| Actualizar estado de cultivo | Explica que debe seleccionar el lote, ir a Estados de Cultivo, registrar el nuevo estado con observaciones y fecha del evento. |
| Adjuntar evidencias | Explica que desde el lote debe ir a Evidencias, seleccionar el tipo, adjuntar archivo o comentario y guardar. |
| Registrar certificacion | Explica el proceso de certificacion, diferencia entre Fairtrade y Rainforest Alliance, y que documentos necesita. |
| Publicar producto | Explica como asociar un lote a un producto comercializable, definir precio, cantidad y unidad de medida. |
| Contactar exportadores | Explica que sus productos publicados aparecen en el catalogo de exportadores y que estos pueden enviarle solicitudes de compra. |
| Consultar solicitudes | Explica donde ver las solicitudes de compra recibidas y como responder a cada una. |

### 4.2 Exportador

| Escenario | Que resuelve el chatbot |
| --- | --- |
| Buscar productores | Explica como explorar el catalogo de productos por ubicacion, certificacion o cultivo. |
| Consultar origen de productos | Explica como escanear el QR de un producto o buscar por codigo de lote para ver trazabilidad completa. |
| Adquirir productos | Explica el flujo de enviar solicitud de compra, que datos debe ingresar y que esperar como respuesta. |
| Conectarse con agentes | Explica como funcionan las solicitudes de compra y la comunicacion con productores a traves de la plataforma. |
| Ver certificaciones | Explica como las certificaciones Fairtrade y Rainforest Alliance aparecen en el pasaporte digital y como verificarlas. |
| Gestionar solicitudes | Explica como revisar solicitudes recibidas, cambiar su estado y negociar con el productor. |

### 4.3 Operador Turistico

| Escenario | Que resuelve el chatbot |
| --- | --- |
| Publicar experiencia | Explica como crear una experiencia turistica desde la seccion de Turismo, ingresando nombre, descripcion, duracion, precio y fotografias. |
| Asociar experiencia a finca | Explica como vincular la experiencia turistica a una finca existente en la plataforma. |
| Gestionar disponibilidad | Explica como actualizar la disponibilidad y capacidad de las experiencias publicadas. |

### 4.4 Comprador Internacional

| Escenario | Que resuelve el chatbot |
| --- | --- |
| Consultar trazabilidad | Explica como escanear un QR o ingresar un codigo de pasaporte para ver la trazabilidad completa del producto. |
| Enviar solicitud de compra | Explica el formulario de solicitud, que datos son obligatorios y que sucede al enviarla. |
| Seguimiento logistico | Explica como consultar el estado de envio de una exportacion en la seccion de seguimiento. |

### 4.5 Turista

| Escenario | Que resuelve el chatbot |
| --- | --- |
| Escanear QR | Explica como usar la camara del telefono para escanear el codigo QR y abrir el pasaporte digital. |
| Consultar pasaporte | Explica que information aparecera en el pasaporte, incluyendo datos del productor, finca, cultivo y certificaciones. |
| Explorar experiencias | Explica como navegar el catalogo de experiencias turisticas disponibles. |

### 4.6 Certificador

| Escenario | Que resuelve el chatbot |
| --- | --- |
| Revisar certificaciones pendientes | Explica donde encontrar las certificaciones que requieren validacion. |
| Validar certificacion | Explica los criterios que debe revisar y como aprobar o rechazar una certificacion. |
| Consultar evidencias | Explica como acceder a las evidencias fotograficas y documentales asociadas a cada certificacion. |

### 4.7 Administrador

| Escenario | Que resuelve el chatbot |
| --- | --- |
| Supervisar usuarios | Explica como acceder al panel de administracion y gestionar usuarios del sistema. |
| Revisar contenido | Explica como aprobar o rechazar contenido publicado por los diferentes actores. |
| Supervisar operaciones | Explica como consultar metricas y estados generales de la plataforma. |

---

## 5. Conocimiento de certificaciones

El chatbot debe tener conocimiento profundo sobre las siguientes certificaciones, segun la informacion detallada en `07-certificaciones.md`.

### 5.1 Fairtrade

**Descripcion general:**
Fairtrade es un sistema de certificacion que garantiza que los productos han sido producidos bajo estandares que protegen a los trabajadores y productores en paises en desarrollo. Se enfoca en condiciones comerciales justas, salarios dignos, organizacion comunitaria y sostenibilidad ambiental.

**Beneficios para el productor:**

- Acceso a un mercado global que premia productos con impacto social.
- Precio minimo garantizado que protege contra fluctuaciones del mercado.
- Prima de Fairtrade que se invierte en proyectos comunitarios.
- Mejora en las condiciones laborales y proteccion ambiental.

**Criterios principales:**

- Los productores deben estar organizados en cooperativas u organizaciones.
- Se deben cumplir estandares laborales basicos.
- Se deben observar practicas sostenibles de produccion.
- El proceso de certificacion requiere auditoria por parte de organismos acreditados.

**Proceso de obtencion:**

1. El productor se contacto con una organizacion Fairtrade autorizada.
2. Se realiza una auditoria inicial de la finca o lote.
3. Si cumple los criterios, se emite la certificacion.
4. Se realizan auditorias anuales de vigilancia.
5. La certificacion tiene validez anual y debe renovarse.

**En AgroTrace:**
El productor puede registrar su certificacion Fairtrade desde la seccion de Certificaciones de su finca o lote, cargando el documento soporte que evidencia la certificacion otorgada por la entidad certificadora.

### 5.2 Rainforest Alliance

**Descripcion general:**
Rainforest Alliance es una certificacion que verifica que los productos han sido producidos bajo estandares de sostenibilidad ambiental, proteccion de la biodiversidad y responsabilidad social. Se enfoca en la conservacion de ecosistemas, manejo integrado de plagas, conservacion de recursos hidricos y condiciones de trabajo justas.

**Beneficios para el productor:**

- Acceso a mercados internacionales que valoran la sostenibilidad.
- Implementacion de practicas agricolas mas eficientes y sostenibles.
- Proteccion de ecosistemas y biodiversidad en las fincas.
- Mejora en la calidad del producto y resiliencia frente al cambio climatico.

**Criterios principales:**

- Proteccion de la biodiversidad en areas naturales y de conservacion.
- Manejo integrado de plagas y تقليل de plaguicidas sinteticos.
- Conservacion de recursos hidricos y suelos.
- Cumplimiento de normas laborales locales e internacionales.
- Vinculacion con comunidades locales y respeto a derechos.

**Proceso de obtencion:**

1. El productor se pone en contacto con una entidad certificadora acreditada por Rainforest Alliance.
2. Se realiza una evaluacion inicial de la finca.
3. Se implementan las mejoras necesarias para cumplir estandares.
4. Se realiza una auditoria de certificacion.
5. Se emiten informes de seguimiento anuales.
6. La certificacion tiene vigencia variable segun el alcance.

**En AgroTrace:**
El productor puede registrar su certificacion Rainforest Alliance desde la seccion de Certificaciones de su finca o lote, cargando el documento oficial emitido por la entidad certificadora acreditada.

### 5.3 Informacion compartida por el chatbot sobre certificaciones

El chatbot debe poder explicar:

- La diferencia conceptual entre ambas certificaciones y sus enfoques.
- Que certificacion conviene segun el tipo de producto y mercado objetivo.
- Que datos necesita el productor para registrar una certificacion en la plataforma.
- Que significa que una certificacion este pendiente de validacion.
- Por que una certificacion puede ser rechazada y como corregir el problema.
- Que aparece en el pasaporte digital del producto respecto a certificaciones.
- Como verificar la vigencia de una certificacion a traves de la plataforma.

### 5.4 Estados de certificacion y significado

| Estado | Significado | Visible en pasaporte |
| --- | --- | --- |
| `PENDIENTE_VALIDACION` | Cargada por el productor pero no validada por certificador. | No |
| `VALIDADA` | Aprobada y vigente segun fecha de vencimiento. | Si |
| `RECHAZADA` | No cumplio criterios o documento no valido. | No |
| `VENCIDA` | Fecha de vencimiento pasada. | No |
| `REVOCADA` | Retirada por la entidad certificadora. | No |

---

## 6. Diseño de conversacion

### 6.1 Principios de conversacion

| Principio | Descripcion |
| --- | --- |
| Concision | Respuestas cortas y directas que respondan la pregunta del usuario. |
| Orientacion a accion | Cada respuesta incluye el paso siguiente o la accion recomendada. |
| Empatia | El chatbot reconoce el contexto del usuario antes de responder. |
| Contexto | El chatbot mantiene memoria de la conversacion para referencias previas. |
| Escalamiento | Cuando no puede responder, ofrece contactar a un operador humano. |

### 6.2 Tipos de respuesta

| Tipo | Cuando se usa | Ejemplo |
| --- | --- | --- |
| Instruccion directa | El usuario pregunta como hacer algo. | Ve a Mis Fincas, selecciona Nueva Finca y completa los datos solicitados. |
| Explicacion conceptual | El usuario pregunta que es algo. | Fairtrade es una certificacion que garantiza condiciones comerciales justas para productores... |
| Confirmacion de proceso | El usuario necesita confirmar un flujo. | Para registrar una certificacion necesitas: 1) документы soporte, 2) datos de la entidad certificadora... |
| Redireccion | El usuario necesita ir a una seccion especifica. | Para ver tus solicitudes de compra ve a la seccion Solicitudes en el menu principal. |
| Despedida | El usuario termina la conversacion. | Estoy aqui si necesitas mas ayuda. Cuida tu cosecha! |

### 6.3 Manejo de frases no claras

Cuando el chatbot no entiende la pregunta del usuario:

1. Pide clarificacion de forma empática: No estoy seguro de entender. Podrias dar mas detalles sobre lo que necesitas?
2. Ofrece opciones comunes: Quizas te refieres a alguno de estos temas: registrar finca, consultar certificaciones, o enviar una solicitud?
3. Si persiste la incomprension, ofrece escalar: Prefiero conectarte con un operador que pueda ayudarte mejor con tu consulta.

### 6.4 Integracion con la interfaz

- El chatbot aparece como un icono flotante en la esquina inferior derecha de la aplicacion.
- Al hacer clic se despliega una ventana de conversacion con historial de mensajes.
- El usuario puede escribir texto libre o seleccionar de opciones predefinidas para guiada inicial.
- Las respuestas del chatbot pueden incluir enlaces a secciones de la aplicacion.
- El chatbot mantiene contexto durante la sesion del usuario.

---

## 7. Arquitectura tecnica

### 7.1 Integracion en la arquitectura

```
┌──────────────────────────────────────────────────────────┐
│                     Aplicacion Web                        │
│                                                          │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐  │
│  │   Frontend   │    │   Chatbot    │    │    API       │  │
│  │   (Next.js)  │<-->│  Component   │<-->│   Backend    │  │
│  └─────────────┘    └──────┬──────┘    └──────┬──────┘  │
│                            │                   │         │
│                     ┌──────▼──────┐            │         │
│                     │   OpenRouter│<───────────┘         │
│                     │   API       │                      │
│                     └─────────────┘                      │
└──────────────────────────────────────────────────────────┘
```

### 7.2 Flujo de datos

1. El usuario escribe un mensaje en el componente de chatbot del frontend.
2. El frontend envia el mensaje al backend a traves de un endpoint protegido.
3. El backend construye el prompt con el contexto del usuario y el historial de conversacion.
4. El backend envia la solicitud al modelo de OpenRouter.
5. El backend recibe la respuesta del modelo y la retorna al frontend.
6. El frontend muestra la respuesta en la ventana de conversacion.

### 7.3 Componentes necesarios

| Componente | Responsabilidad |
| --- | --- |
| `ChatbotWidget` | Componente visual del chatbot en el frontend. Maneja UI, historial y entrada de texto. |
| `ChatbotService` | Servicio en el backend que recibe mensajes, construye prompts y comunica con OpenRouter. |
| `PromptBuilder` | Construye el prompt del sistema con contexto, instrucciones y conocimiento de certificaciones. |
| `ConversationManager` | Maneja el historial de conversacion por sesion de usuario. |

---

## 8. Modelos y prompt engineering

### 8.1 Proveedor de modelo

Se usara OpenRouter como proveedor de modelos generativos gratuitos para el prototipo. OpenRouter permite acceder a multiples modelos gratuitos con una API compatible con OpenAI.

### 8.2 Modelo recomendado para prototipo

| Modelo | Proveedor | Costo | Idioma | Adequado para |
| --- | --- | --- | --- | --- |
| `mistralai/mistral-7b-instruct` | Mistral | Gratis | Multi | Guiado conversacional, rapido |
| `openchat/openchat-7b` | OpenChat | Gratis | Multi |chatbots, instrucciones |
| `meta-llama/llama-3-8b-instruct` | Meta | Gratis | Multi |chatbots, contexto amplio |

Para el prototipo se usara `mistralai/mistral-7b-instruct` por su balance entre calidad y velocidad.

### 8.3 Estructura del prompt

El prompt del sistema se estructura en tres secciones:

**Seccion 1: Identidad y rol**

```
Eres AgroBot, el asistente virtual de AgroTrace Magdalena.
Tu objetivo es guiar a los usuarios de la plataforma para que puedan navegar,
entender funcionalidades y resolver dudas sobre certificaciones y procesos.
```

**Seccion 2: Reglas de conversacion**

```
Reglas:
- Responde siempre en español.
- Usa respuestas cortas y concretas, maximo 3-4 oraciones.
- Nunca inventes URLs o rutas de menu. Usa descripciones generales.
- Si no sabes algo, di que no tienes esa informacion y ofrece escalar.
- Mantén un tono amigable y empatico.
```

**Seccion 3: Contexto del usuario**

```
El usuario actual tiene el rol de [ROL] y esta en la seccion [SECCION].
Su pregunta es: [PREGUNTA]
```

**Seccion 4: Conocimiento de certificaciones**

```
Informacion sobre certificaciones disponibles en la plataforma:

FAIRTRADE:
[resumen de criterios, beneficios y proceso]

RAINFOREST ALLIANCE:
[resumen de criterios, beneficios y proceso]

Estados posibles de una certificacion en la plataforma:
- PENDIENTE_VALIDACION: cargada por productor, pendiente revision
- VALIDADA: aprobada y vigente
- RECHAZADA: documento no valido o criteria no cumplidos
- VENCIDA: fecha de vencimiento superada
- REVOCADA: retirada por la entidad certificadora

El pasaporte digital solo muestra certificaciones en estado VALIDADA y vigentes.
```

### 8.4 Historial de conversacion

El prompt incluyara los ultimos 5 mensajes de la conversacion para mantener contexto:

```
Historial de conversacion:
Usuario: [mensaje anterior]
AgroBot: [respuesta anterior]
Usuario: [mensaje actual]
```

---

## 9. Requerimientos no funcionales

### 9.1 Rendimiento

| ID | Descripcion | Meta |
| --- | --- | --- |
| RNF-001 | Tiempo de respuesta del chatbot para consultas simples | < 3 segundos |
| RNF-002 | Tiempo de respuesta del chatbot para consultas sobre certificaciones | < 5 segundos |
| RNF-003 | El chatbot debe manejar hasta 100 conversaciones concurrentes | Sin degradacion |

### 9.2 Seguridad

| ID | Descripcion |
| --- | --- |
| RNF-004 | El chatbot no debe exponer datos sensibles del usuario mas alla de su rol. |
| RNF-005 | El historial de conversacion se almacena cifrado en el backend. |
| RNF-006 | La comunicacion con OpenRouter usa HTTPS. |
| RNF-007 | Las respuestas del modelo se sanitizan para evitar inyeccion de contenido. |

### 9.3 Privacidad

| ID | Descripcion |
| --- | --- |
| RNF-008 | El usuario puede eliminar su historial de conversacion. |
| RNF-009 | No se almacenan conversaciones de usuarios no autenticados. |
| RNF-010 | Los datos de las conversaciones no se usan para entrenar modelos. |

### 9.4 Disponibilidad

| ID | Descripcion |
| --- | --- |
| RNF-011 | El chatbot debe tener disponibilidad del 99% durante horario comercial. |
| RNF-012 | Si OpenRouter no esta disponible, el chatbot responde con mensaje de indisponibilidad. |

---

## 10. Criterios de exito

| ID | Criterio | Forma de medicion |
| --- | --- | --- |
| SC-001 | Un productor puede preguntar como registrar una finca y recibir instrucciones claras. | Test del chatbot con pregunta sobre registro de finca. |
| SC-002 | Un exportador puede preguntar sobre certificaciones Fairtrade y recibir una explicacion precisa. | Test del chatbot con pregunta sobre Fairtrade. |
| SC-003 | Un turista puede preguntar que es un pasaporte digital y recibir una explicacion comprensible. | Test del chatbot con pregunta sobre pasaporte. |
| SC-004 | Las respuestas del chatbot se muestran en menos de 5 segundos. | Medicion de tiempo de respuesta en entorno de prueba. |
| SC-005 | El chatbot responde de forma empática y orientado a la accion. | Revision manual de respuestas. |
| SC-006 | Si el chatbot no puede responder, ofrece escalar a atencion humana. | Test de scenarios fuera de conocimiento. |

---

## 11. Prototipo MVP

### 11.1 Objetivo del prototipo

Demonstrar la viabilidad del chatbot integrado en la aplicacion web usando un modelo gratuito de OpenRouter. El prototipo permitira conversaciones basicas con los flujos de guiado definidos.

### 11.2 Funcionalidades del prototipo

| Funcionalidad | Descripcion |
| --- | --- |
| Ventana de chat flotante | Icono en esquina inferior derecha que despliega conversacion. |
| Entrada de texto libre | El usuario puede escribir cualquier pregunta. |
| Respuestas predefinidas | Botones de opcion rapida para guiada inicial: Productor, Exportador, Turista, otro. |
| Respuesta del modelo | Respuestas generadas por mistral-7b-instruct via OpenRouter. |
| Historial de conversacion | Se muestran los mensajes previos en la misma sesion. |
| knowledge de certificaciones | El prompt incluye informacion sobre Fairtrade y Rainforest Alliance. |

### 11.3 Componentes a implementar

| Componente | Ubicacion | Descripcion |
| --- | --- | --- |
| `ChatbotWidget` | `src/components/chatbot/ChatbotWidget.tsx` | Componente React del chatbot con ventana de conversacion. |
| `ChatbotService` | `src/services/chatbot/ChatbotService.ts` | Servicio backend para comunicar con OpenRouter. |
| `PromptBuilder` | `src/services/chatbot/PromptBuilder.ts` | Construye prompts del sistema. |
| API route | `src/app/api/chatbot/route.ts` | Endpoint API para enviar mensajes al chatbot. |
| Configuracion | `.env.local` | Variables para API key de OpenRouter. |

### 11.4 Configuracion de OpenRouter

```env
OPENROUTER_API_KEY=your_api_key_here
OPENROUTER_MODEL=mistralai/mistral-7b-instruct
```

### 11.5 Flujo de implementacion

1. Crear componente ChatbotWidget con UI basica de ventana de chat.
2. Implementar API route que reciba mensajes y los envie a OpenRouter.
3. Crear PromptBuilder con el prompt del sistema incluyendo informacion de certificaciones.
4. Implementar ChatbotService en el backend para orquestar la comunicacion.
5. Integrar ChatbotWidget en el layout principal de la aplicacion.
6. Probar conversaciones con usuarios de cada rol.

### 11.6 Fallback cuando el modelo no responde

Si OpenRouter no responde o el modelo falla, el chatbot retornara:

```
Estoy teniendo dificultades para responderte en este momento.
Por favor intenta mas tarde o contacta a soporte@agrotrace.co para asistencia directa.
```

---

## 12. Fuera de alcance

Los siguientes puntos quedan excluidos del prototipo inicial y deben especificarse por separado:

- Chatbot con voz o audio.
- Integracion con centros de atencion humana en vivo.
- Historial persistente de conversacion entre sesiones.
- Analytics de conversaciones para mejora del modelo.
- Chatbot multilingue con traduccion automatica.
- Recomendaciones personalizadas basadas en comportamiento del usuario.
- Notificaciones proactivas del chatbot hacia el usuario.
- Integracion con sistemas de CRM o helpdesk externos.
- Capacitacion de modelos personalizados con datos de AgroTrace.
- Resolucion automatica de casos de soporte.

---

## Historial de cambios

| Version | Fecha | Autor | Descripcion |
| --- | --- | --- | --- |
| 1.0.0 | 28/05/2026 | Equipo AgroTrace | Version inicial de la especificacion del chatbot. |