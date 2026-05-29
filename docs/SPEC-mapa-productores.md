# Especificación: Mapa Interactivo de Productores - AgroTrace Magdalena

## 1. Overview

**Nombre del feature:** Mapa Interactivo de Productores
**Estado:** Pendiente de desarrollo
**Resumen:** Mapa interactivo que muestra ubicaciones de fincas, lugares de producción agrícola y zonas de agroturismo en el Magdalena, con iconos de productos y popups con información detallada de trazabilidad, certificaciones Rainforest Alliance y solicitud de recorridos turísticos.

---

## 2. Stack Tecnológico

### Librería de Mapa
** Recomendada: Leaflet + React-Leaflet**
- Bundle size pequeño
- Sin API key requerida (usa OpenStreetMap)
- Fácil de customizar
- Funciona bien con Next.js 15

**Alternativa: Mapbox GL JS** (requiere API key)

### Dependencias a instalar
```bash
npm install leaflet react-leaflet
npm install -D @types/leaflet
```

### Proveedor de tiles
OpenStreetMap (gratuito) o MapTiler (con estilo personalizado)

---

## 3. Iconos de Productos Agrícolas

Se requieren iconos para los siguientes productos típicos del Magdalena:

### Iconos necesarios

| Producto | Descripción | Color sugerido |
|----------|-------------|----------------|
| **Café** | Grano de café | `#6D9E13` (verde) |
| **Cacao** | Muestra de cacao | `#8B4513` (marrón) |
| **Plátano** | Racimo de plátanos | `#FFD700` (amarillo) |
| **Arroz** | Granjas de arroz | `#F5F5DC` (beige) |
| **Palma aceitera** | Palmera | `#228B22` (verde oscuro) |
| **Mango** | Fruto de mango | `#FF8C00` (naranja) |
| **Ganadería** | Vaca/finca ganadera | `#A0522D` (siena) |
| **Agroturismo** | Hoja + persona / granja abierta | `#00BCD4` (cyan) |

### Fuente de iconos
- **Flaticon.com** - Iconos gratuitos de alta calidad
- **Iconos 8** - Estilo consistente
- **SVG personalizados** - Para mejor calidad y control

### Icono de Agroturismo

El icono de agroturismo debe reflejar una finca o zona agrícola abierta al turismo:
- **Concepto**: Hoja verde con silueta de persona, o una granja con puerta abierta
- **Color**: `#00BCD4` (cyan) para diferenciarlo de los productores agrícolas
- **Tamaño**: 44x44px (ligeramente más grande que los productores para destacar)

---

### Especificación técnica de iconos
- Formato: SVG o PNG con transparencia
- Tamaño base: 40x40px para marcador individual
- Marcador cluster: 50x50px
- Fondo circular con borde blanco

---

## 4. Diseño del Popup de Productor

### Estructura del Popup

```
┌─────────────────────────────────────────────┐
│  [ICONO PRODUCTO]  Nombre de la Finca       │
│                    📍 Ubicación exacta       │
├─────────────────────────────────────────────┤
│  INFORMACIÓN DEL REPRESENTANTE               │
│  👤 Nombre: Juan Pérez                        │
│  📞 Tel: +57 300 123 4567                    │
│  📧 Email: juan@finca.com                    │
├─────────────────────────────────────────────┤
│  PRODUCTOS PRODUCIDOS                        │
│  ☕ Café Arábica    🌿 Orgánico              │
│  ☕ Café Robusta    🔄 Convencional          │
├─────────────────────────────────────────────┤
│  CERTIFICACIÓN RAINFOREST ALLIANCE           │
│  🏆 Estado: CERTIFICADO                       │
│  📅 Válido hasta: Diciembre 2025             │
│  📊 Área certificada: 45 hectáreas            │
│  🔗 ID Certificación: RA-2024-00123          │
├─────────────────────────────────────────────┤
│  TRAZABILIDAD                                │
│  📦 Lotes este año: 12                        │
│  🌱 Metodología: Tradicional                  │
│  📈 Rendimiento: 1.2 ton/hectárea            │
├─────────────────────────────────────────────┤
│  [Ver detalles]  [Contactar]  [Cómo llegar] │
└─────────────────────────────────────────────┘
```

### Campos de datos del Productor

```typescript
interface Producer {
  // Identificación
  id: string;
  name: string;
  type: 'finca' | 'granja' | 'planta' | 'cooperativa';

  // Ubicación
  coordinates: { lat: number; lng: number };
  address: string;
  municipality: string; // ej: "Santa Marta, Magdalena"
  region: string;

  // Representante
  representative: {
    name: string;
    role: string;
    phone: string;
    email: string;
  };

  // Productos
  products: Array<{
    type: ProductType;
    variety?: string; // ej: "Arábica", "Castillo"
    certification: 'organic' | 'conventional' | 'mixed';
    annualProduction: string;
  }>;

  // Rainforest Alliance
  rainforestAlliance: {
    certified: boolean;
    certificationId: string;
    validUntil: string;
    certifiedArea: string; // hectares
    auditDate: string;
    score?: number; // puntaje de auditoría
  };

  // Trazabilidad
  traceability: {
    lotsThisYear: number;
    methodology: 'traditional' | 'mixed' | 'technified';
    yieldPerHectare: string;
    harvestSeasons: string[];
    lastInspection: string;
  };

  // Multimedia
  photos?: string[];
  certifications?: string[];
}

---

## 4bis. Diseño del Popup de Agroturismo

### Estructura del Popup de Agroturismo

```
┌─────────────────────────────────────────────┐
│  🌿 AGROTURISMO                              │
│  [ICONO AGROTURISMO]  Nombre de la Finca     │
│                       📍 Ubicación exacta     │
├─────────────────────────────────────────────┤
│  INFORMACIÓN DEL TOUR                        │
│  🏡 Tipo: Finca cafetera / Cacaotal / Mixta  │
│  ⏱️ Duración: 3-4 horas                       │
│  👥 Capacidad: Hasta 15 personas              │
│  💰 Precio: Desde $50.000 COP por persona     │
├─────────────────────────────────────────────┤
│  ACTIVIDADES DISPONIBLES                     │
│  🌱 Recorrido por cultivos                    │
│  ☕ Cata de café / chocolate                  │
│  🧑‍🌾 Participación en cosecha                  │
│  📸 Fotografía de paisaje                     │
├─────────────────────────────────────────────┤
│  SERVICIOS                                   │
│  🍽️ Almuerzo típico incluido                 │
│  🚗 Transporte (opcional)                     │
│  🛖 Hospedaje rural (opcional)                │
├─────────────────────────────────────────────┤
│  CONTACTO Y RESERVA                          │
│  👤 Guía: Carlos Martínez                     │
│  📞 Tel: +57 300 123 4567                    │
│  📧 Email: turismo@finca.com                  │
│  📅 Disponible: Lunes a Sábado               │
├─────────────────────────────────────────────┤
│  [Solicitar recorrido]  [Ver más info]       │
└─────────────────────────────────────────────┘
```

### Campos de datos del Punto de Agroturismo

```typescript
interface AgrotourismPoint {
  id: string;
  farmName: string;
  type: 'finca_cafetera' | 'cacaotal' | 'mixta' | 'ganadera' | 'frutal';
  
  // Ubicación
  coordinates: { lat: number; lng: number };
  address: string;
  municipality: string;
  region: string;

  // Información del tour
  tour: {
    duration: string;        // ej: "3-4 horas", "Medio día"
    capacity: number;        // máximo de personas
    pricePerPerson: number;  // en COP
    currency: string;        // default "COP"
    availability: string[];  // días de la semana
    description: string;     // descripción breve del recorrido
  };

  // Actividades
  activities: Array<{
    name: string;
    icon: string;
    description: string;
  }>;

  // Servicios incluidos
  services: {
    lunch: boolean;
    transportation: boolean;
    accommodation: boolean;
    guide: boolean;
    others: string[];
  };

  // Guía de contacto
  guide: {
    name: string;
    phone: string;
    email: string;
  };

  // Media
  photos: string[];
  rating?: number; // 1-5 estrellas
  reviews?: number; // cantidad de reseñas

  // Estado del punto
  active: boolean;
  verifiedAt?: string;
}
```

---

## 5. Interacciones del Mapa

### Comportamiento de Iconos

| Acción | Comportamiento |
|--------|----------------|
| **Hover** | Escala 1.1x, sombra aumenta, tooltip con nombre |
| **Click** | Abre popup con información completa |
| **Drag** | Mueve el mapa |
| **Scroll** | Zoom in/out |

### Clustering de Marcadores
- Cuando hay >10 marcadores cercanos, agrupar en cluster
- Cluster muestra cantidad de productores
- Click en cluster hace zoom o abre lista

### Filtros (Sidebar o Modal)

```
┌──────────────────┐
│ FILTROS          │
├──────────────────┤
│ Producto:        │
│ ☐ Café           │
│ ☐ Cacao          │
│ ☐ Plátano        │
│ ☐ Arroz          │
│ ☐ Palma          │
├──────────────────┤
│ Certificación:   │
│ ☐ Certificado    │
│ ☐ En proceso     │
│ ☐ Sin cert.      │
├──────────────────┤
│ Municipio:       │
│ [Santa Marta  ▼] │
├──────────────────┤
│ Agroturismo:     │
│ ☐ Mostrar puntos │
├──────────────────┤
│ [Aplicar filtros]│
└──────────────────┘
```

### Búsqueda
- Input en la parte superior
- Busca por: nombre finca, municipio, producto, representante
- Autocompletado con sugerencias

---

## 6. Capas del Mapa

### Tipos de visualización

| Capa | Descripción |
|------|-------------|
| **Mapa base** | OpenStreetMap o MapTiler estilo terreno |
| **Productores** | Marcadores de fincas (defecto: activa) |
| **Agroturismo** | Marcadores de zonas abiertas al turismo agrícola con recorridos disponibles |
| **Certificación RA** | Mapa de calor o polígonos de áreas certificadas |
| **Rutas** | Líneas de rutas logísticas (futuro) |
| **Clima** | Capas meteorológicas (futuro) |

### Toggle de capas
- Botón en toolbar para abrir panel de capas
- Checkboxes para activar/desactivar

---

## 7. Responsive Design

### Desktop (>1024px)
- Mapa ocupa 100% del viewport
- Sidebar de filtros colapsable a la izquierda
- Popup: 400px width

### Tablet (768px - 1024px)
- Filtros en drawer lateral
- Popup: 350px width

### Mobile (<768px)
- Filtros en modal inferior (bottom sheet)
- Popup: 100% width, altura máxima 70vh
- Botón flotante para abrir filtros

---

## 8. Estados del Popup

### Estado: Cargando
- Skeleton loader con animación
- Mismo tamaño que contenido final

### Estado: Error
- Mensaje: "No se pudo cargar la información"
- Botón reintentar

### Estado: Vacío (sin datos)
- Mensaje: "Este producer aún no tiene información completa"
- Solo datos básicos visibles

---

## 9. Accesibilidad

- Navegación por teclado en lista de productores
- ARIA labels en todos los controles
- Alt text en fotos
- Contraste mínimo 4.5:1
- Focus visible en todos los elementos interactivos

---

## 10. Datos de Ejemplo (Mock Data)

```typescript
const mockProducers: Producer[] = [
  {
    id: 'finca-001',
    name: 'Finca El Paraíso',
    type: 'finca',
    coordinates: { lat: 11.2445, lng: -74.2095 },
    address: 'Vereda El Jardín, Km 12 vía Santa Marta-Palermo',
    municipality: 'Santa Marta',
    region: 'Magdalena',
    representative: {
      name: 'Carlos Martínez',
      role: 'Propietario y administrador',
      phone: '+57 300 123 4567',
      email: 'carlos.martinez@fincaelparaiso.com'
    },
    products: [
      { type: 'cafe', variety: 'Castillo', certification: 'organic', annualProduction: '25 ton' },
      { type: 'cacao', variety: ' TCS-01', certification: 'conventional', annualProduction: '8 ton' }
    ],
    rainforestAlliance: {
      certified: true,
      certificationId: 'RA-2024-00847',
      validUntil: '2025-12-31',
      certifiedArea: '45 hectáreas',
      auditDate: '2024-03-15',
      score: 87
    },
    traceability: {
      lotsThisYear: 12,
      methodology: 'technified',
      yieldPerHectare: '1.8 ton/ha',
      harvestSeasons: ['Mar-Jun', 'Sep-Dic'],
      lastInspection: '2024-03-15'
    }
  },
  // ... más productores de ejemplo
];
```

### Mock Data de Agroturismo

```typescript
const mockAgrotourism: AgrotourismPoint[] = [
  {
    id: 'agrotur-001',
    farmName: 'Finca Cafetera El Edén',
    type: 'finca_cafetera',
    coordinates: { lat: 11.3200, lng: -74.0500 },
    address: 'Vereda San Pedro, Km 8 vía Minca',
    municipality: 'Santa Marta',
    region: 'Magdalena',
    tour: {
      duration: '4 horas',
      capacity: 15,
      pricePerPerson: 65000,
      currency: 'COP',
      availability: ['Lunes', 'Martes', 'Jueves', 'Viernes', 'Sábado'],
      description: 'Recorrido completo por cultivos de café, desde la semilla hasta la taza.'
    },
    activities: [
      { name: 'Recorrido por cafetales', icon: 'plant', description: 'Caminata guiada por los cultivos' },
      { name: 'Cata de café', icon: 'coffee', description: 'Degustación de variedades locales' },
      { name: 'Participación en cosecha', icon: 'harvest', description: 'Recolecta café con agricultores locales' },
      { name: 'Fotografía de paisaje', icon: 'camera', description: 'Vistas panorámicas de la sierra' }
    ],
    services: {
      lunch: true,
      transportation: false,
      accommodation: false,
      guide: true,
      others: ['Seguro de viaje incluido']
    },
    guide: {
      name: 'María Fernanda López',
      phone: '+57 310 987 6543',
      email: 'turismo@fincaeleden.com'
    },
    photos: ['eden-1.jpg', 'eden-2.jpg'],
    rating: 4.8,
    reviews: 34,
    active: true
  }
];
```

---

## 11. Tareas de Desarrollo

### Fase 1: Setup
- [ ] Instalar leaflet y react-leaflet
- [ ] Crear componente MapContainer base
- [ ] Configurar provider de tiles

### Fase 2: Marcadores
- [ ] Crear componente ProducerMarker
- [ ] Crear componente AgrotourismMarker (icono diferenciado, mayor tamaño)
- [ ] Definir iconos para cada producto y para agroturismo
- [ ] Implementar clustering (agrupando productores y agroturismo por separado)

### Fase 3: Popup
- [ ] Crear ProducerPopup component
- [ ] Crear AgrotourismPopup component con diseño diferenciado
- [ ] Diseñar layout responsive
- [ ] Implementar estados (loading, error)

### Fase 4: Filtros y Búsqueda
- [ ] Crear Sidebar de filtros
- [ ] Implementar búsqueda con autocompletado
- [ ] Conectar filtros con datos

### Fase 5: Data
- [ ] Crear mock data de productores
- [ ] Definir interfaces TypeScript
- [ ] Preparar API endpoint (futuro)

---

## 12. Archivos a crear/modificar

```
agro-trace-frontend/src/
├── app/mapa/
│   ├── page.tsx                 # Página principal del mapa
│   └── components/
│       ├── MapView.tsx          # Componente principal del mapa
│       ├── ProducerMarker.tsx   # Marcador individual
│       ├── ProducerPopup.tsx    # Popup con información del productor
│       ├── AgrotourismPopup.tsx  # Popup con info de agroturismo
│       ├── FilterSidebar.tsx    # Panel de filtros
│       ├── SearchBar.tsx        # Barra de búsqueda
│       ├── LayerControl.tsx      # Control de capas
│       └── types.ts             # Interfaces TypeScript
├── data/
│   └── producers.ts             # Mock data
└── app/globals.css              # Añadir estilos del mapa
```

---

## 13. Preguntas/Pendientes para el usuario

1. **Iconos:** ¿De qué productos agrícolas specifically quieres iconos? ¿Prefieres que use iconos de Flaticon o necesitas que te pida iconos específicos?

2. **Datos:** ¿Ya existe una API/backend con datos de productores o usamos mock data inicialmente?

3. **Mapa base:** ¿Prefieres OpenStreetMap (gratuito) o Mapbox con estilo personalizado?

4. **Ubicaciones:** ¿Tienes las coordenadas de las fincas o generamos datos de ejemplo?

5. **Polígonos RA:** ¿Las áreas certificadas Rainforest Alliance son puntos o polígonos?
