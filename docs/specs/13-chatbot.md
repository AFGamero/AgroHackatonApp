# Feature Specification: Chatbot de Guia para Certificaciones Fairtrade y Rainforest Alliance

**Version**: 1.0.0  
**Creado**: 28/05/2026  
**Actualizado**: 28/05/2026  
**Estado**: Borrador  
**Autor**: Equipo Nebbi  
**Revisor**: Pendiente

---

## Indice

1. [Contexto y objetivo](#1-contexto-y-objetivo)
2. [Actores](#2-actores)
3. [Funcionalidad principal: Guia de certificacion](#3-funcionalidad-principal-guia-de-certificacion)
4. [Conocimiento de certificaciones](#4-conocimiento-de-certificaciones)
5. [Guias por actor](#5-guias-por-actor)
6. [Diseño de conversacion](#6-diseño-de-conversacion)
7. [Arquitectura tecnica](#7-arquitectura-tecnica)
8. [Modelos y prompt engineering](#8-modelos-y-prompt-engineering)
9. [Requerimientos no funcionales](#9-requerimientos-no-funcionales)
10. [Criterios de exito](#10-criterios-de-exito)
11. [Prototipo MVP](#11-prototipo-mvp)
12. [Fuera de alcance](#12-fuera-de-alcance)

---

## 1. Contexto y objetivo

Nebbi es una plataforma que conecta productores agricolas con mercados internacionales, turistas y compradores a traves de un sistema de trazabilidad verificado por QR. El nucleo de la propuesta de valor de Nebbi gira en torno a las certificaciones Fairtrade y Rainforest Alliance, las cuales garantizan la calidad, sostenibilidad y origen de los productos.

Esta feature define un chatbot inteligente cuyo enfoque principal es servir como guia experta para el proceso de certificacion Fairtrade y Rainforest Alliance. El chatbot orienta a los productores en cada etapa del camino hacia la certificacion: desde entender los requisitos y beneficios, pasando por la preparacion de la documentacion, hasta el registro y validacion en la plataforma. Tambien asiste a compradores y turistas que desean entender el valor de las certificaciones y como verificarlas en el pasaporte digital.

Como funcionalidad complementaria, el chatbot ofrece orientacion general sobre navegacion de la plataforma y resolucion de dudas frecuentes. Sin embargo, su prioridad y especializacion es el conocimiento certificador.

---

## 2. Actores

| ID | Actor | Rol en el chatbot |
| --- | --- | --- |
| ACT-01 | Productor | Recibe guia experta sobre el proceso completo de certificacion Fairtrade y Rainforest Alliance: requisitos, beneficios, preparacion de documentacion, registro en la plataforma y seguimiento del estado de validacion. Tambien orientacion complementaria sobre registro de finca, lote y evidencias. |
| ACT-02 | Exportador | Recibe guia sobre como verificar la validez de las certificaciones en los pasaportes digitales, interpretar los estados de certificacion y como comunicarse con productores certificados. |
| ACT-03 | Operador Turistico | Recibe guia basica sobre publicacion de experiencias turisticas y como las certificaciones agregan valor a las fincas que ofrecen turismo. |
| ACT-04 | Comprador Internacional | Recibe guia sobre como verificar certificaciones Fairtrade y Rainforest Alliance en el pasaporte digital, entender los estados de certificacion y la trazabilidad del producto. |
| ACT-05 | Turista | Recibe guia basica sobre escaneo de QR, consulta de pasaporte digital y como interpretar la informacion de certificaciones que aparece. |
| ACT-06 | Certificador | Recibe guia detallada sobre el proceso de validacion de certificaciones: criterios de aprobacion, revision de documentos y evidencias, y gestion de estados. |
| ACT-07 | Administrador | Recibe guia sobre supervision de certificaciones, usuarios y operacion general del sistema. |

---

## 3. Funcionalidad principal: Guia de certificacion

El chatbot de Nebbi esta diseñado para acompanar a los productores en cada etapa del proceso de certificacion Fairtrade y Rainforest Alliance. Esta es su funcionalidad primaria y la razon de su existencia.

### 3.1 Etapas de la guia de certificacion

El chatbot cubre el ciclo completo de certificacion en cinco etapas:

| Etapa | Descripcion | Preguntas tipicas |
| --- | --- | --- |
| 1. Descubrimiento | Explica que son las certificaciones, sus beneficios y diferencias. Ayuda al productor a decidir cual le conviene. | Que es Fairtrade? Que beneficios tiene Rainforest Alliance? Cual me conviene segun mi cultivo? |
| 2. Requisitos | Detalla los criterios que debe cumplir la finca o lote para cada certificacion. | Que documentos necesito? Como preparo mi finca para la auditoria? Que criterios laborales debo cumplir? |
| 3. Preparacion | Orienta al productor sobre como preparar la documentacion, contactar entidades certificadoras y preparar la finca para evaluacion. | Donde encuentro una entidad certificadora acreditada? Como organizo mis evidencias? |
| 4. Registro en Nebbi | Guia paso a paso para registrar la certificacion en la plataforma: seccion, campos, carga de documentos y asociacion a finca o lote. | Donde subo mi certificado Fairtrade? Como asocio la certificacion a mi lote? Que formato debe tener el documento? |
| 5. Seguimiento y renovacion | Explica los estados de validacion, que significan, como resolver rechazos y cuando renovar certificaciones vencidas. | Por que mi certificacion aparece como PENDIENTE_VALIDACION? Que hago si fue RECHAZADA? Cuando debo renovar? |

### 3.2 Guia para compradores y exportadores

El chatbot tambien asiste a compradores internacionales y exportadores en la verificacion de certificaciones:

- **Verificar validez**: Explica como confirmar que una certificacion Fairtrade o Rainforest Alliance mostrada en el pasaporte digital es autentica y vigente.
- **Interpretar estados**: Describe el significado de cada estado de certificacion y cuales garantizan confiabilidad.
- **Buscar productos certificados**: Orienta sobre como filtrar productos en la plataforma por tipo de certificacion.

### 3.3 Guia complementaria de plataforma

De forma secundaria, el chatbot ofrece orientacion general sobre navegacion de la plataforma y procesos operativos. Esta funcionalidad cubre:

- Registro de fincas, lotes y evidencias.
- Publicacion de productos y experiencias turisticas.
- Solicitudes de compra y comunicacion entre actores.
- Escaneo de QR y consulta de pasaporte digital.

---

## 5. Guias por actor

A continuacion se detallan los escenarios que cubre el chatbot para cada actor, con enfasis en certificaciones.

### 5.1 Productor

| Escenario | Que resuelve el chatbot |
| --- | --- |
| Entender certificaciones | Explica que son Fairtrade y Rainforest Alliance, sus diferencias, beneficios y cual conviene segun el cultivo y mercado objetivo. |
| Conocer requisitos | Detalla los criterios especificos que debe cumplir la finca o lote para cada certificacion, incluyendo aspectos laborales, ambientales y organizativos. |
| Preparar documentacion | Orienta sobre los documentos necesarios, como contactar entidades certificadoras acreditadas y como preparar la finca para la auditoria. |
| Registrar certificacion en Nebbi | Explica el paso a paso: ir a Certificaciones en la finca o lote, seleccionar tipo (Fairtrade o Rainforest Alliance), cargar documento soporte y asociar. |
| Entender estados de validacion | Explica que significa cada estado (PENDIENTE_VALIDACION, VALIDADA, RECHAZADA, VENCIDA, REVOCADA) y que acciones tomar en cada caso. |
| Resolver rechazo | Explica por que una certificacion puede ser rechazada, como corregir los problemas y como volver a enviarla a validacion. |
| Renovar certificacion | Orienta sobre fechas de vencimiento, proceso de renovacion y como mantener la certificacion vigente. |
| Registro de finca y lote | Explica como registrar fincas y lotes en la plataforma. |
| Evidencias y cultivos | Explica como adjuntar evidencias y actualizar estados de cultivo. |
| Publicar producto | Explica como asociar un lote a un producto comercializable y definir precio. |

### 5.2 Exportador

| Escenario | Que resuelve el chatbot |
| --- | --- |
| Verificar certificaciones | Explica como confirmar la autenticidad y vigencia de certificaciones Fairtrade y Rainforest Alliance en los pasaportes digitales. |
| Filtrar productos certificados | Explica como buscar y filtrar productos en el catalogo por tipo de certificacion. |
| Entender estados | Explica el significado de cada estado de certificacion y cuales son confiables para hacer negocios. |
| Buscar productores | Explica como explorar el catalogo de productos por ubicacion, certificacion o cultivo. |
| Consultar trazabilidad | Explica como escanear el QR de un producto para ver trazabilidad completa. |

### 5.3 Operador Turistico

| Escenario | Que resuelve el chatbot |
| --- | --- |
| Publicar experiencia | Explica como crear una experiencia turistica desde la seccion de Turismo. |
| Asociar experiencia a finca | Explica como vincular la experiencia turistica a una finca existente. |
| Valor de certificaciones | Explica como las certificaciones Fairtrade y Rainforest Alliance agregan valor a las experiencias turisticas ofrecidas. |

### 5.4 Comprador Internacional

| Escenario | Que resuelve el chatbot |
| --- | --- |
| Verificar certificaciones | Explica como verificar la autenticidad de las certificaciones Fairtrade y Rainforest Alliance en el pasaporte digital del producto. |
| Interpretar pasaporte | Explica como leer la informacion del pasaporte digital: datos del productor, finca, certificaciones vigentes y evidencias. |
| Consultar trazabilidad | Explica como escanear un QR o ingresar un codigo de pasaporte para ver la trazabilidad completa. |
| Enviar solicitud de compra | Explica el formulario de solicitud, datos obligatorios y el flujo posterior. |

### 5.5 Turista

| Escenario | Que resuelve el chatbot |
| --- | --- |
| Escanear QR | Explica como usar la camara para escanear el codigo QR y abrir el pasaporte digital. |
| Consultar pasaporte | Explica la informacion que aparece en el pasaporte, incluyendo certificaciones Fairtrade y Rainforest Alliance. |
| Explorar experiencias | Explica como navegar el catalogo de experiencias turisticas disponibles. |

### 5.6 Certificador

| Escenario | Que resuelve el chatbot |
| --- | --- |
| Revisar certificaciones pendientes | Explica donde encontrar las certificaciones que requieren validacion y como filtrarlas. |
| Criterios de validacion | Detalla los criterios especificos que debe verificar para Fairtrade y para Rainforest Alliance. |
| Aprobar o rechazar | Explica el proceso de aprobacion o rechazo, como documentar la decision y notificar al productor. |
| Consultar evidencias | Explica como acceder a las evidencias fotograficas y documentales asociadas a cada certificacion. |

### 5.7 Administrador

| Escenario | Que resuelve el chatbot |
| --- | --- |
| Supervisar certificaciones | Explica como acceder al panel de administracion y revisar el estado de todas las certificaciones. |
| Supervisar usuarios | Explica como gestionar usuarios y sus roles en la plataforma. |
| Revisar contenido | Explica como aprobar o rechazar contenido publicado por los diferentes actores. |

---

## 4. Conocimiento de certificaciones

El chatbot debe tener conocimiento profundo sobre las siguientes certificaciones, segun la informacion detallada en `07-certificaciones.md`.

### 4.1 Fairtrade

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

**En Nebbi:**
El productor puede registrar su certificacion Fairtrade desde la seccion de Certificaciones de su finca o lote, cargando el documento soporte que evidencia la certificacion otorgada por la entidad certificadora.

### 4.2 Rainforest Alliance

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

**En Nebbi:**
El productor puede registrar su certificacion Rainforest Alliance desde la seccion de Certificaciones de su finca o lote, cargando el documento oficial emitido por la entidad certificadora acreditada.

### 4.1 Informacion compartida por el chatbot sobre certificaciones

El chatbot debe poder explicar:

- La diferencia conceptual entre ambas certificaciones y sus enfoques.
- Que certificacion conviene segun el tipo de producto y mercado objetivo.
- Que datos necesita el productor para registrar una certificacion en la plataforma.
- Que significa que una certificacion este pendiente de validacion.
- Por que una certificacion puede ser rechazada y como corregir el problema.
- Que aparece en el pasaporte digital del producto respecto a certificaciones.
- Como verificar la vigencia de una certificacion a traves de la plataforma.

### 4.2 Estados de certificacion y significado

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

El prompt del sistema se estructura en cuatro secciones, con enfasis en el conocimiento certificador:

**Seccion 1: Identidad y rol**

```
Eres NebbiBot, el asistente virtual de Nebbi especializado en certificaciones Fairtrade y Rainforest Alliance.
Tu objetivo principal es guiar a los productores en el proceso de certificacion: desde entender que son, sus beneficios y requisitos, hasta como registrar y dar seguimiento a la certificacion en la plataforma.
Tambien puedes orientar sobre navegacion general de la plataforma y resolver dudas frecuentes, pero tu especialidad y prioridad es el conocimiento certificador.
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
NebbiBot: [respuesta anterior]
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
| SC-001 | Un productor puede preguntar como iniciar el proceso de certificacion Fairtrade y recibir una guia completa con los pasos, requisitos y beneficios. | Test del chatbot con pregunta sobre proceso Fairtrade. |
| SC-002 | Un productor puede preguntar sobre las diferencias entre Fairtrade y Rainforest Alliance y recibir una explicacion clara que le ayude a decidir. | Test del chatbot con pregunta comparativa. |
| SC-003 | Un productor puede preguntar por que su certificacion fue rechazada y recibir orientacion sobre como corregir el problema. | Test del chatbot con pregunta sobre estado RECHAZADA. |
| SC-004 | Un comprador internacional puede preguntar como verificar la autenticidad de una certificacion en el pasaporte digital y recibir instrucciones precisas. | Test del chatbot con pregunta sobre verificacion. |
| SC-005 | Un certificador puede preguntar sobre los criterios de validacion de Rainforest Alliance y recibir una lista detallada. | Test del chatbot con pregunta sobre criterios de validacion. |
| SC-006 | Las respuestas del chatbot sobre certificaciones se muestran en menos de 5 segundos. | Medicion de tiempo de respuesta en entorno de prueba. |
| SC-007 | El chatbot responde de forma empática y orientada a la accion, guiando al usuario hacia el siguiente paso del proceso certificador. | Revision manual de respuestas. |
| SC-008 | Si el chatbot no puede responder una pregunta sobre certificaciones, ofrece escalar a atencion humana. | Test de scenarios fuera de conocimiento. |

---

## 11. Prototipo MVP

### 11.1 Objetivo del prototipo

Demonstrar la viabilidad del chatbot como guia experta en certificaciones Fairtrade y Rainforest Alliance, integrado en la aplicacion web usando un modelo gratuito de OpenRouter. El prototipo priorizara el conocimiento certificador y las conversaciones relacionadas con el proceso de certificacion.

### 11.2 Funcionalidades del prototipo

| Funcionalidad | Descripcion |
| --- | --- |
| Ventana de chat flotante | Icono en esquina inferior derecha que despliega conversacion. |
| Entrada de texto libre | El usuario puede escribir cualquier pregunta. |
| Guia de certificacion | El chatbot prioriza preguntas sobre el proceso de certificacion Fairtrade y Rainforest Alliance. |
| Conocimiento certificador | El prompt incluye informacion exhaustiva sobre ambas certificaciones, sus criterios, beneficios, requisitos y estados. |
| Respuesta del modelo | Respuestas generadas por mistral-7b-instruct via OpenRouter. |
| Historial de conversacion | Se muestran los mensajes previos en la misma sesion. |
| Orientacion complementaria | El chatbot tambien puede orientar sobre navegacion general de la plataforma. |

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
Por favor intenta mas tarde o contacta a soporte@nebbi.co para asistencia directa.
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
- Capacitacion de modelos personalizados con datos de Nebbi.
- Resolucion automatica de casos de soporte.

---

## Historial de cambios

| Version | Fecha | Autor | Descripcion |
| --- | --- | --- | --- |
| 1.0.0 | 28/05/2026 | Equipo Nebbi | Version inicial de la especificacion del chatbot. |