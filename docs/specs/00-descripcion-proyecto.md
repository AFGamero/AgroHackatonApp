# AgroTrace Magdalena

## Descripcion del Proyecto

AgroTrace Magdalena es una plataforma digital de trazabilidad agricola, turismo rural y comercializacion internacional enfocada en productores del departamento del Magdalena, Colombia.

El objetivo principal del proyecto es permitir que un producto agricola tenga un pasaporte digital verificable mediante codigo QR, conectando en una sola plataforma al productor, al turista y al comprador internacional.

La solucion permite registrar productores, fincas, lotes de cultivo, estados del proceso agricola, evidencias fotograficas, certificaciones, experiencias turisticas y solicitudes de compra internacional. Cada lote puede contar con un pasaporte digital publico, donde cualquier persona puede consultar su origen, historia, estado actual, certificaciones y evidencias asociadas.

## Problema que Resuelve

Actualmente muchos productos agricolas exportados carecen de una trazabilidad clara y accesible para compradores, turistas y aliados comerciales. Esto reduce la confianza en el origen del producto, dificulta la validacion de certificaciones y limita la visibilidad de los pequenos productores.

Ademas, existe una desconexion entre los productos agricolas del territorio y las experiencias turisticas asociadas a su origen. AgroTrace Magdalena busca unir estos dos mundos mediante una experiencia digital verificable, interactiva y orientada a la confianza.

## Propuesta de Valor

- Los productores pueden registrar sus fincas, lotes y avances del cultivo.
- Los turistas pueden escanear un QR y conocer la historia del producto y su origen.
- Los compradores internacionales pueden verificar trazabilidad, certificaciones y evidencias.
- Los operadores turisticos pueden publicar experiencias rurales asociadas a fincas.
- Los exportadores pueden recibir solicitudes de compra y gestionar procesos comerciales.

## Objetivo Principal del MVP

Permitir que un producto agricola del Magdalena tenga un pasaporte digital verificable mediante QR, conectando productor, turista y comprador internacional a traves de una unica plataforma de trazabilidad.

## Alcance del MVP

1. Registro de productores.
2. Registro de fincas.
3. Gestion de lotes.
4. Actualizacion de estados de cultivo.
5. Carga de evidencias fotograficas.
6. Registro de certificaciones.
7. Generacion de pasaporte digital.
8. Generacion de codigo QR.
9. Visualizacion publica del lote.
10. Catalogo de experiencias turisticas.
11. Solicitud de compra internacional.

## Stack Tecnologico Sugerido

- Frontend: Next.js, TypeScript, TailwindCSS, Shadcn/UI.
- Backend: NestJS, TypeScript.
- Base de datos: PostgreSQL.
- Almacenamiento: Supabase Storage o S3.
- Autenticacion: JWT y refresh tokens.
- Mapas: Leaflet o Mapbox.
- QR: paquete `qrcode` de npm.
- Infraestructura: Docker y Docker Compose.

## Arquitectura Inicial

El sistema se plantea como un monolito modular orientado al backend, con separacion clara por dominios funcionales. Esta arquitectura permite desarrollar rapidamente el MVP, mantener el codigo organizado y preparar una posible evolucion futura hacia microservicios si el crecimiento del producto lo requiere.
