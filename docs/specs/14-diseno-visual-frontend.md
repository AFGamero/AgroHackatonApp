# Specification: Diseño Visual y Frontend

**Version**: 1.0.0
**Creado**: 28/05/2026
**Actualizado**: 28/05/2026
**Estado**: Borrador
**Autor**: Equipo AgroTrace
**Revisor**: Pendiente

---

## Indice

1. [Identidad de marca](#1-identidad-de-marca)
2. [Paleta de colores](#2-paleta-de-colores)
3. [Tipografía](#3-tipografía)
4. [Iconografía](#4-iconografía)
5. [Componentes UI](#5-componentes-ui)
6. [Layout y espaciado](#6-layout-y-espaciado)
7. [Responsive design](#7-responsive-design)
8. [Accesibilidad](#8-accesibilidad)
9. [Estilos de componentes por sección](#9-estilos-de-componentes-por-sección)
10. [Assets necesarios](#10-assets-necesarios)
11. [Historial de cambios](#11-historial-de-cambios)

---

## 1. Identidad de marca

### 1.1 Nombre de la aplicación

El nombre oficial de la plataforma es **AgroTrace Magdalena** según documentado en `00-descripcion-proyecto.md`. Este nombre esta sujeto a confirmacion final por parte del equipo.

### 1.2 Producto representativo

El producto insignia y representativo de la marca es el **guineo verde** (banano verde / plátano). Este producto simboliza:

- La-region del Magdalena y su tradición agrícola
- El producto principal de exportación de la region
- La conexión entre productores locales y mercados internacionales

### 1.3 Concepto visual

La identidad visual transmite:

- **Confianza y trazabilidad**: Los colores transmiten frescura, naturaleza y confianza.
- **Conexión campo-mercado**: La iconografía conecta al usuario con el origen agrícola.
- **Modernidad con raices**: Diseño limpio y contemporáneo que respeta la tradición agrícola.

---

## 2. Paleta de colores

### 2.1 Colores primarios

| Nombre | Hex | Uso |
| --- | --- | --- |
| Verde Agro | `#2D8B4E` | Color principal de marca, botones primarios, highlights |
| Verde Oscuro | `#1B5E34` | Hover states, headers, elementos de énfasis |
| Verde Claro | `#E8F5E9` | Fondos sutiles, badges de éxito, áreas destacadas |
| Azul Profundo | `#1565C0` | Color secundario, enlaces, elementos interactivos |
| Azul Claro | `#E3F2FD` | Fondos de sección, cards secundarias |

### 2.2 Colores semánticos

| Nombre | Hex | Uso |
| --- | --- | --- |
| Éxito | `#4CAF50` | Estados exitosos, confirmaciones, certificados activos |
| Warning | `#FF9800` | Alertas, certificaciones por vencer |
| Error | `#F44336` | Estados de error, rechazos, validación fallida |
| Info | `#2196F3` | Información, tooltips, hints |

### 2.3 Colores neutros

| Nombre | Hex | Uso |
| --- | --- | --- |
| Blanco | `#FFFFFF` | Fondos principales, cards |
| Gris 50 | `#FAFAFA` | Fondos de página |
| Gris 100 | `#F5F5F5` | Bordes sutiles, separadores |
| Gris 300 | `#E0E0E0` | Bordes, inputs deshabilitados |
| Gris 500 | `#9E9E9E` | Texto secundario, placeholders |
| Gris 700 | `#616161` | Texto body secundario |
| Gris 900 | `#212121` | Texto principal, headers |

### 2.4 Aplicación de colores

```
Fondo de página:        Gris 50 (#FAFAFA)
Fondo de cards:         Blanco (#FFFFFF)
Header/Navigation:      Verde Agro (#2D8B4E)
Botones primarios:      Verde Agro (#2D8B4E)
Botones primarios hover: Verde Oscuro (#1B5E34)
Enlaces:                 Azul Profundo (#1565C0)
Texto principal:         Gris 900 (#212121)
Texto secundario:       Gris 700 (#616161)
Texto terciario:        Gris 500 (#9E9E9E)
Bordes:                 Gris 300 (#E0E0E0)
Certificación activa:   Éxito (#4CAF50)
Alerta:                 Warning (#FF9800)
Error:                  Error (#F44336)
```

---

## 3. Tipografía

### 3.1 Familia tipográfica

**Font principal**: Inter (Google Fonts)
**Font secundaria**: Para títulos y énfasis: DM Sans

```
font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
font-family: 'DM Sans', 'Inter', sans-serif;  // Para títulos
```

### 3.2 Escala tipográfica

| Token | Size | Weight | Line-height | Uso |
| --- | --- | --- | --- | --- |
| `text-xs` | 12px | 400 | 16px | Labels pequeños, timestamps |
| `text-sm` | 14px | 400 | 20px | Texto secundario, captions |
| `text-base` | 16px | 400 | 24px | Texto body principal |
| `text-lg` | 18px | 500 | 28px | Texto destacado, body grande |
| `text-xl` | 20px | 600 | 28px | Subtítulos |
| `text-2xl` | 24px | 700 | 32px | Títulos de sección |
| `text-3xl` | 30px | 700 | 36px | Títulos de página |
| `text-4xl` | 36px | 800 | 40px | Hero titles |
| `text-5xl` | 48px | 800 | 48px | Títulos principales de landing |

### 3.3 Guía de uso

- **Headers**: Usar DM Sans bold para títulos principales
- **Body text**: Usar Inter regular para párrafos
- **Botones**: Usar Inter semibold, texto en mayúsculas para CTAs principales
- **Etiquetas**: Usar Inter medium, 12-14px

---

## 4. Iconografía

### 4.1 Estilo de iconos

Los iconos serán pictogramas de la agricultura y la cadena de valor:

- **Estilo**: Outline fino, consistente con el diseño minimalista
- **Trazo**: 1.5px - 2px
- **Esquinas**: Redondeadas (8px de radio para iconos grandes)
- **Tamaño base**: 24x24px para UI, 48x48px para illustration blocks

### 4.2 Categorías de iconos

#### Productos agrícolas (productos representativos)

| Icono | Uso |
| --- | --- |
| Guineo/Plátano | Producto insignia, secciones de producto destacado |
| Naranja | Frutas cítricas, certificaciones |
| Café | Productos de exportación, cultivos |
| Cacao | Productos premium, certificados |
| Aguacate | Productos de exportación |
| Tomate | Vegetales, producción local |
| Yuca | Cultivos regionales |
| Maíz | Granos, producción básica |

#### Procesos y logística

| Icono | Uso |
| --- | --- |
| Camión | Logística, exportación, transporte |
| Barco | Envíos internacionales, marina |
| Avión | Transporte aéreo, velocidad |
| Casa de campo | Fincas, productores |
| Campo/Cultivo | Terrenos agrícolas, trazabilidad |
| Hoja/Árbol | Sostenibilidad, certificaciones verdes |

#### Documentos y verificación

| Icono | Uso |
| --- | --- |
| Checklist | Tareas, estados de cultivo |
| Diploma/Certificado | Certificaciones, validaciones |
| QR | Pasaportes digitales, escaneo |
| Documento | Registros, evidencias |
| Cámara | Evidencias fotográficas |
| Mapa/Pin | Ubicaciones, fincas |

#### Comercial y usuarios

| Icono | Uso |
| --- | --- |
| Handshake | Acuerdos, exportadores, compras |
| Globe/World | Compradores internacionales, global |
| User/Users | Perfiles, actores |
| Store/Tienda | Catálogos, productos |
| Shopping Cart | Compras, solicitudes |
| Message/Burbuja | Comunicación, chatbot |

#### Estados y navegación

| Icono | Uso |
| --- | --- |
| Check/Checks | Completado, válido |
| Clock | Tiempo, fechas, historia |
| Chevron Right | Navegación, expansión |
| Menu/Hamburger | Navegación móvil |
| Home | Inicio, retorno |
| Settings | Configuración, administración |

### 4.3 Implementación de iconos

Se usará una librería de iconos vectoriales. Opciones recomendadas:

1. **Lucide React** - Iconos outline consistentes, licencia MIT
2. **Heroicons** - Iconos de Tailwind, estilo outline
3. **Phosphor Icons** - Variantes fill/outline, buena cobertura agrícola

Para el MVP se usará **Lucide React** por su consistencia y cobertura.

### 4.4 Icono de marca para el producto insignia

El guineo verde tendrá un icono personalizado que aparecerá en:

- Header de la aplicación
- Pantalla de inicio / landing
- Estados de producto
- Certificaciones asociadas

Este icono será un pictograma estilizado de un racimo de guineos verdes.

---

## 5. Componentes UI

### 5.1 Buttons

#### Primary Button

```
Background: Verde Agro (#2D8B4E)
Text: Blanco (#FFFFFF)
Padding: 12px 24px
Border-radius: 8px
Font: Inter semibold, 14px, uppercase
Hover: Verde Oscuro (#1B5E34)
Active: Scale 0.98
Disabled: opacity 0.5, cursor not-allowed
```

#### Secondary Button

```
Background: Transparente
Border: 1px solid Verde Agro (#2D8B4E)
Text: Verde Agro (#2D8B4E)
Padding: 12px 24px
Border-radius: 8px
Font: Inter semibold, 14px
Hover: Background Verde Claro (#E8F5E9)
```

#### Ghost Button

```
Background: Transparente
Text: Azul Profundo (#1565C0)
Padding: 8px 16px
Font: Inter medium, 14px
Hover: underline
```

#### Danger Button

```
Background: Error (#F44336)
Text: Blanco (#FFFFFF)
Padding: 12px 24px
Border-radius: 8px
Font: Inter semibold, 14px
```

### 5.2 Inputs

#### Text Input

```
Background: Blanco (#FFFFFF)
Border: 1px solid Gris 300 (#E0E0E0)
Border-radius: 8px
Padding: 12px 16px
Font: Inter regular, 16px
Placeholder: Gris 500 (#9E9E9E)
Focus: border Verde Agro (#2D8B4E), box-shadow 0 0 0 3px rgba(45,139,78,0.1)
Error: border Error (#F44336), mensaje debajo en rojo
Disabled: background Gris 100 (#F5F5F5)
```

#### Select

```
Mismo estilo que Text Input
Flecha dropdown: Gris 500 (#9E9E9E)
```

#### Checkbox / Radio

```
Unchecked: border Gris 300 (#E0E0E0), background blanco
Checked: background Verde Agro (#2D8B4E), checkmark blanco
Label: Inter regular, 14px, Gris 900 (#212121)
```

### 5.3 Cards

#### Card básica

```
Background: Blanco (#FFFFFF)
Border: 1px solid Gris 200 (#EEEEEE)
Border-radius: 12px
Padding: 24px
Shadow: 0 1px 3px rgba(0,0,0,0.08)
Hover (si clickeable): shadow más pronunciada, border Verde Agro
```

#### Card de producto

```
Header: imagen del producto, aspect ratio 4:3
Body: nombre, precio, certificaciones
Footer: botón de acción
```

#### Card de pasaporte digital

```
Header: código QR
Body: información del lote, trazabilidad
Estados: badges de certificación en la esquina
```

### 5.4 Badges y Tags

#### Badge de certificación

```
Fairtrade: background #FFF3E0, text #E65100, border #FFCC80
Rainforest: background #E8F5E9, text #2E7D32, border #A5D6A7
Organic: background #E3F2FD, text #1565C0, border #90CAF9
Default: background Gris 100, text Gris 700, border Gris 300
```

#### Status Badge

```
Activo: background Verde Claro (#E8F5E9), text Verde Oscuro
Pendiente: background #FFF8E1, text #F57F17
Vencido: background #FFEBEE, text #C62828
```

### 5.5 Navigation

#### Header principal

```
Background: Verde Agro (#2D8B4E)
Height: 64px
Logo: izquierda, icono de guineo + nombre
Nav links: centro, texto blanco
User menu: derecha, avatar + dropdown
```

#### Sidebar

```
Width: 240px (expandido), 64px (colapsado)
Background: Blanco (#FFFFFF)
Border-right: 1px solid Gris 200 (#EEEEEE)
Items: icono + label, padding 12px 16px
Active: background Verde Claro (#E8F5E9), texto Verde Agro
Hover: background Gris 50 (#FAFAFA)
```

#### Breadcrumbs

```
Separator: Chevron Right, Gris 500
Current page: texto Gris 900, no link
Previous pages: texto Azul Profundo (#1565C0), hover underline
```

### 5.6 Feedback components

#### Toast notifications

```
Success: background Verde Claro, border-left Verde Agro
Error: background #FFEBEE, border-left Error
Warning: background #FFF8E1, border-left Warning
Info: background Azul Claro, border-left Azul Profundo
Position: bottom-right, stack往上
Duration: 5s auto-dismiss
```

#### Loading states

```
Spinner: circulo giratorio, color Verde Agro
Skeleton: background Gris 200, animation shimmer
Progress bar: height 4px, background Gris 200, fill Verde Agro
```

#### Empty states

```
Ilustración centered
Título: text-xl, Gris 900
Descripción: text-base, Gris 500
CTA button si aplica
```

---

## 6. Layout y espaciado

### 6.1 Sistema de grid

```
Container max-width: 1280px
Columns: 12
Gutter: 24px
Margins: 24px (mobile), 48px (tablet), 64px (desktop)
```

### 6.2 Espaciado

El sistema de espaciado usa múltiplos de 4px:

| Token | Valor |
| --- | --- |
| `space-1` | 4px |
| `space-2` | 8px |
| `space-3` | 12px |
| `space-4` | 16px |
| `space-5` | 20px |
| `space-6` | 24px |
| `space-8` | 32px |
| `space-10` | 40px |
| `space-12` | 48px |
| `space-16` | 64px |
| `space-20` | 80px |
| `space-24` | 96px |

### 6.3 Border radius

| Token | Valor | Uso |
| --- | --- | --- |
| `radius-sm` | 4px | Inputs, badges pequeños |
| `radius-md` | 8px | Buttons, cards |
| `radius-lg` | 12px | Cards grandes, modals |
| `radius-xl` | 16px | Imágenes destacadas |
| `radius-full` | 9999px | Avatars, pills |

---

## 7. Responsive design

### 7.1 Breakpoints

| Breakpoint | Width | Dispositivo |
| --- | --- | --- |
| `sm` | 640px | Móviles grandes |
| `md` | 768px | Tablets |
| `lg` | 1024px | Laptops |
| `xl` | 1280px | Desktops |
| `2xl` | 1536px | Pantallas grandes |

### 7.2 Estrategia responsive

- **Mobile-first**: Diseñar para móvil primero, luego масштабировать arriba
- **Grid adaptativo**: 1 columna en móvil, 2 en tablet, hasta 12 en desktop
- **Navigation**: Sidebar en desktop → bottom nav en móvil
- **Cards**: Stack vertical en móvil → grid en desktop
- **Tablas**: Scroll horizontal en móvil → columnas visibles en desktop

### 7.3 Tamaño de touch targets

```
Mínimo: 44x44px para elementos interactivos en móvil
Padding mínimo: 16px para botones táctiles
Espacio entre elementos: mínimo 8px
```

---

## 8. Accesibilidad

### 8.1 Contraste de colores

- Texto sobre fondo: mínimo 4.5:1 para texto normal, 3:1 para texto grande
- Elementos UI sobre fondo: mínimo 3:1
- Verificar especialmente combinaciones:
  - Texto blanco sobre Verde Agro: ✓ 5.2:1
  - Texto Verde Agro sobre Blanco: ✓ 5.1:1
  - Texto Gris 500 sobre Blanco: ✗ 2.4:1 (no usar para texto importante)

### 8.2 Focus visible

```
Outline: 2px solid Azul Profundo (#1565C0)
Offset: 2px
No usar outline: none sin alternativa visible
```

### 8.3 ARIA labels

- Todos los botones e inputs deben tener labels accesibles
- Iconos que no tengan texto adyacente necesitan `aria-label`
- Landmarks semánticos: `<header>`, `<nav>`, `<main>`, `<footer>`
- Encabezados jerárquicos: h1 → h2 → h3 sin saltos

### 8.4 Navegación por teclado

- Tab para avanzar, Shift+Tab para retroceder
- Enter para activar botones y links
- Escape para cerrar modales y dropdowns
- Flechas para navegar en menús y selects

---

## 9. Estilos de componentes por sección

### 9.1 Página de inicio (Landing)

```
Hero: fondo con gradiente Verde Agro → Verde Oscuro
Título: text-4xl, blanco, DM Sans bold
Subtítulo: text-lg, blanco/80
CTA buttons: Primary (blanco) + Secondary (outline blanco)
Imagen hero: foto de campo o producto (guineo)
```

### 9.2 Mascota Nebbi - Jaguar Representativo

#### 9.2.1 Identidad de Nebbi

Nebbi es la mascota oficial de AgroTrace Magdalena, representada como un jaguar verde con azul en honor a las tierras y el mar de la costa colombiana.

| Atributo | Descripcion |
| --- | --- |
| Nombre | Nebbi |
| Significado | "Jaguar" en la lengua de los Koguis, pueblo indígena de la Sierra Nevada de Santa Marta |
| Colores | Verde Agro (#2D8B4E) y Azul Profundo (#1565C0), representando la naturaleza y el mar colombiano |
| Personalidad | Amigable, confiable, conectado con la tierra, guardián de la trazabilidad |
| Uso principal | Imagen del chatbot, elemento de marca, representación visual de la plataforma |

#### 9.2.2 Representación visual

Nebbi aparece como un jaguar estilizado con las siguientes caracteristicas:

- **Forma**: Cuerpo de jaguar estilizado, posicion vertical o caminando
- **Colores principales**:
  - Pelaje base: Verde Agro (#2D8B4E)
  - Manchas/patrones: Azul Profundo (#1565C0)
  - Detalles: Amarillo dorado para ojos y accents
- **Estilo**: Ilustracion flat design, simplificada y moderna
- **Expresion**: Facial amigable, no amenazante, conectando con usuarios

#### 9.2.3 Aplicaciones de Nebbi

| Aplicacion | Descripcion |
| --- | --- |
| Chatbot | Nebbi aparece como la identidad visual del chatbot, reemplazando el icono generico de chat |
| Loader/Splash | Animacion de Nebbi durante cargas |
| Empty states | Ilustraciones con Nebbi para estados sin contenido |
| Landing page | Elemento decorativo cerca del hero |
| About/Team | Seccion que menciona a Nebbi como parte de la identidad |

#### 9.2.4 Chatbot Nebbi

El chatbot de asistencia (definido en `13-chatbot.md`) usa a Nebbi como su identidad visual:

```
Launcher del chatbot: Icono de Nebbi en lugar de icono de chat generico
Ventana abierta:
  - Header: Icono de Nebbi + nombre "Nebbi" + badge de estado online
  - Mensajes del bot: Avatar de Nebbi junto a cada respuesta
  - Typing indicator: Animacion de Nebbi esperando
  - Input area: Diseño consistente con la identidad
```

### 9.3 Mapa de ubicación de productos

La seccion de mapa permite a los usuarios buscar productos agricolas de la costa colombiana visualmente en un mapa interactivo.

#### 9.3.1 Descripcion general

El mapa muestra puntos de ubicacion de productores y fincas en el departamento del Magdalena. Cada punto representa un lugar donde se produce un producto agricultural especifico.

#### 9.3.2 Barra de busqueda

```
Ubicacion: Parte superior del mapa
Placeholder: "Buscar productos (guineo, banano, cafe...)"
Autocompletado: Sugiere productos mientras el usuario escribe
Filtros: Dropdown para filtrar por tipo de producto
```

#### 9.3.3 Iconos distintivos por producto

Cada producto tiene un icono representativo en el mapa:

| Producto | Icono | Descripcion visual |
| --- | --- | --- |
| Guineo Verde | Racimo de guineos | Icono estilizado de racimo de guineos |
| Banano | Banano individual | Icono de banano maduro |
| Cafe | Grano de cafe | Icono de grano de cafe con hoja |
| Cacao | Cacao en mazorca | Icono de mazorca de cacao |
| Aguacate | Aguacate entero | Icono de aguacate partido |
| Naranja | Naranja con hoja | Icono de naranja con hoja |
| Yuca | Yuca entera | Icono de raiz de yuca |
| Maiz | Mazorca de maiz | Icono de mazorca con hojas |
| Tomate | Tomate rojo | Icono de tomate |
| Mango | Mango entero | Icono de mango verde |

#### 9.3.4 Interaccion con puntos del mapa

```
Click en punto:
  - Aparece popup/modal con informacion del lugar
  - Informacion mostrada:
    - Nombre de la finca o productor
    - Ubicacion (municipio, vereda)
    - Fotografia de referencia
    - Productos disponibles
    - Certificaciones (Fairtrade, Rainforest Alliance)
    - Informacion de contacto (correo, telefono)
    - Boton "Solicitar producto"
```

#### 9.3.5 Popup de informacion del punto

```
Diseno:
  - Width: 320px
  - Background: Blanco (#FFFFFF)
  - Border-radius: 12px
  - Shadow: 0 4px 16px rgba(0,0,0,0.15)
  
Contenido:
  - Imagen: 100% width, aspect ratio 16:9, border-radius top
  - Nombre: text-lg, DM Sans bold, Gris 900
  - Ubicacion: text-sm, Gris 500, icono de ubicacion
  - Badges de certificacion: pequenos, al lado del nombre
  - Lista de productos: chips/badges pequenos
  - Informacion de contacto: icono + texto
  - Boton "Solicitar producto": Primary button, full width
```

#### 9.3.6 Boton "Solicitar producto"

```
Accion: Abre formulario de solicitud de compra o contacto directo
Ubicacion: Dentro del popup del punto en el mapa
Comportamiento:
  - Si el usuario no esta autenticado: Redirige a login/registro
  - Si el usuario esta autenticado: Abre modal con formulario de solicitud
```

#### 9.3.7 Filtros y controles del mapa

```
Capas:
  - Toggle para mostrar/ocultar puntos por producto
  - Toggle para mostrar/ocultar certificaciones
  
Controles:
  - Zoom in/out
  - Centrar en Magdalena
  - Buscar mi ubicacion
  
Leyenda:
  - Muestra todos los iconos de productos disponibles
  - Indica cantidad de puntos por producto
```

### 9.4 Seccion Tienda

La seccion tienda ofrece dos vistas complementarias para explorar productos: busqueda tradicional y busqueda por mapa.

#### 9.4.1 Descripcion general

```
URL: /tienda o /store
Tabs de navegacion:
  - "Catalogo" - Lista tradicional de productos
  - "Mapa" - Vista de mapa con puntos de productos
```

#### 9.4.2 Pestana Catalogo (Lista tradicional)

```
Layout:
  - Sidebar izquierda: Filtros (250px)
  - Contenido principal: Grid de productos

Filtros disponibles:
  - Tipo de producto (dropdown)
  - Certificaciones (checkboxes)
  - Rango de precio (slider)
  - Ubicacion (dropdown municipio)
  - Disponibilidad (toggle)

Grid de productos:
  - 4 columnas en desktop
  - 2 columnas en tablet
  - 1 columna en movil
  - Cards con imagen, nombre, precio, ubicacion, certificaciones
  - Hover: sombra elevada
  - Click: navega a detalle del producto
```

#### 9.4.3 Pestana Mapa

```
Layout:
  - Mapa interactivo ocupa toda la pantalla o area principal
  - Barra de busqueda superpuesta en la parte superior
  - Panel lateral izquierdo colapsable con filtros
  - Leyenda de iconos en esquina inferior

Funcionalidades:
  - Misma experiencia descrita en seccion 9.3
  - Click en punto abre popup con informacion
  - Boton "Solicitar producto" en cada popup
  - Filtros aplican tanto a puntos visibles como a resultados
```

#### 9.4.4 Card de producto en catalogo

```
Diseno:
  - Background: Blanco (#FFFFFF)
  - Border: 1px solid Gris 200 (#EEEEEE)
  - Border-radius: 12px
  - Overflow: hidden para imagen

Contenido:
  - Imagen: aspect ratio 4:3, object-fit cover
  - Body (padding 16px):
    - Nombre producto: text-lg, bold
    - Precio: text-base, Verde Agro, bold
    - Unidad: text-sm, Gris 500
    - Ubicacion: text-sm, icono + texto
    - Badges certificacion: row horizontal
  - Footer: Boton "Ver detalle" o "Solicitar"
```

---

## 10. Assets necesarios

### Imágenes

- Logo de la aplicación (guineo estilizado)
- Ilustraciones para empty states
- Imágenes de cultivos y productos (stock)
- Foto de perfil placeholder
- mascot Nebbi (ilustraciones en diferentes poses y tamanos)

### Iconos personalizados

- Guineo/Plátano (producto insignia)
- Logo mark para favicon
- Iconos de productos agricolas para el mapa (guineo, banano, cafe, cacao, aguacate, naranja, yuca, maiz, tomate, mango)
- Avatar de Nebbi para el chatbot

### mascot Nebbi - Especificaciones de arte

```
Colores:
  - Pelaje principal: Verde Agro (#2D8B4E)
  - Manchas/patrones: Azul Profundo (#1565C0)
  - Ojos/detalles: Amarillo dorado (#FFC107)
  
Variantes necesarias:
  - Icono circular (para chatbot launcher): 56x56px
  - Avatar para mensajes: 40x40px
  - Ilustracion completa: 200x200px para empty states
  - Mini badge: 24x24px para headers
```

---

## 11. Historial de cambios

| Version | Fecha | Autor | Descripcion |
| --- | --- | --- | --- |
| 1.0.0 | 28/05/2026 | Equipo AgroTrace | Version inicial del diseño visual y frontend spec |
| 1.1.0 | 28/05/2026 | Equipo AgroTrace | Agregada mascota Nebbi, mapa de productos y seccion tienda |