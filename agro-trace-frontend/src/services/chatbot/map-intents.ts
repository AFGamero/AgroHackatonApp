import { FilterState, ProductType } from "@/app/mapa/components/types";

export type MapAction =
  | { type: "filter"; filters: Partial<FilterState> }
  | { type: "search"; query: string }
  | { type: "select_producer"; name: string }
  | { type: "select_agrotourism"; name: string }
  | { type: "layer"; layer: "street" | "satellite" | "terrain" }
  | { type: "toggle_certified"; value: boolean }
  | { type: "toggle_agrotourism"; value: boolean }
  | { type: "reset" }
  | { type: "fly_to_municipality"; municipality: string };

const PRODUCT_KEYWORDS: Record<string, ProductType> = {
  cafe: "cafe",
  cafetal: "cafe",
  cafetera: "cafe",
  cafetero: "cafe",
  cacao: "cacao",
  cacaotero: "cacao",
  cacaotal: "cacao",
  chocolate: "cacao",
  platano: "platano",
  platanos: "platano",
  platanera: "platano",
  arroz: "arroz",
  arrocera: "arroz",
  arrocero: "arroz",
  palma: "palma",
  palmera: "palma",
  aceitera: "palma",
  mango: "mango",
  mangos: "mango",
  mangifera: "mango",
  ganaderia: "ganaderia",
  ganadera: "ganaderia",
  ganado: "ganaderia",
  leche: "ganaderia",
};

const MUNICIPALITY_KEYWORDS: Record<string, string> = {
  "santa marta": "Santa Marta",
  santa: "Santa Marta",
  fundacion: "Fundación",
  fundación: "Fundación",
  "sitio nuevo": "Sitio Nuevo",
  sitionuevo: "Sitio Nuevo",
  plato: "Plato",
};

interface ParsedResult {
  action: MapAction | null;
  reply?: string;
}

export function parseMapIntent(message: string): ParsedResult {
  const lower = message.toLowerCase().trim();

  if (
    lower.includes("limpiar") ||
    lower.includes("reset") ||
    lower.includes("quitar filtro") ||
    lower.includes("mostrar todo") ||
    lower.includes("ver todo") ||
    lower.includes("sin filtro") ||
    (lower.includes("quitar") && (lower.includes("filtro") || lower.includes("certificado")))
  ) {
    return {
      action: { type: "reset" },
      reply: "He limpiado todos los filtros del mapa para que veas todos los productores.",
    };
  }

  if (
    lower.includes("satelite") ||
    lower.includes("satélite") ||
    lower.includes("satelital") ||
    lower.includes("vista sat")
  ) {
    return {
      action: { type: "layer", layer: "satellite" },
      reply: "He cambiado la vista del mapa a modo satélite.",
    };
  }

  if (
    lower.includes("terreno") ||
    lower.includes("topografico") ||
    lower.includes("topográfico") ||
    lower.includes("topografia")
  ) {
    return {
      action: { type: "layer", layer: "terrain" },
      reply: "He cambiado la vista del mapa a modo terreno.",
    };
  }

  if (
    lower.includes("calle") ||
    lower.includes("mapa normal") ||
    lower.includes("estandar") ||
    lower.includes("estándar") ||
    lower.includes("vista normal")
  ) {
    return {
      action: { type: "layer", layer: "street" },
      reply: "He cambiado la vista del mapa a modo calle.",
    };
  }

  const raKeywords = [
    "certificado",
    "certificados",
    "certificadas",
    "certificada",
    "rainforest",
    "rain forest",
    "solo ra",
    "solo rainforest",
  ];
  if (raKeywords.some((kw) => lower.includes(kw)) && !lower.includes("no certificado")) {
    return {
      action: { type: "toggle_certified", value: true },
      reply: "He filtrado el mapa para mostrar solo los productores con certificación Rainforest Alliance.",
    };
  }

  if (lower.includes("no certificado") || lower.includes("sin certificar") || lower.includes("sin certificacion")) {
    return {
      action: { type: "toggle_certified", value: false },
      reply: "He quitado el filtro de certificación. Ahora ves todos los productores.",
    };
  }

  if (
    lower.includes("mostrar agroturismo") ||
    lower.includes("ver agroturismo") ||
    lower.includes("muestra agroturismo") ||
    lower.includes("activar agroturismo")
  ) {
    return {
      action: { type: "toggle_agrotourism", value: true },
      reply: "He mostrado los puntos de agroturismo en el mapa.",
    };
  }

  if (
    lower.includes("ocultar agroturismo") ||
    lower.includes("esconder agroturismo") ||
    lower.includes("quitar agroturismo") ||
    lower.includes("desactivar agroturismo") ||
    lower.includes("sin agroturismo")
  ) {
    return {
      action: { type: "toggle_agrotourism", value: false },
      reply: "He ocultado los puntos de agroturismo del mapa.",
    };
  }

  const detectedProducts: ProductType[] = [];
  for (const [keyword, productType] of Object.entries(PRODUCT_KEYWORDS)) {
    if (lower.includes(keyword) && !detectedProducts.includes(productType)) {
      detectedProducts.push(productType);
    }
  }

  let detectedMunicipality: string | undefined;
  for (const [keyword, municipality] of Object.entries(MUNICIPALITY_KEYWORDS)) {
    if (lower.includes(keyword)) {
      detectedMunicipality = municipality;
      break;
    }
  }

  if (detectedProducts.length > 0 && detectedMunicipality) {
    return {
      action: {
        type: "filter",
        filters: { products: detectedProducts, municipality: detectedMunicipality, certified: "all" },
      },
      reply: `He filtrado el mapa para mostrar solo ${detectedProducts.map((p) => translateProduct(p)).join(", ")} en ${detectedMunicipality}.`,
    };
  }

  if (detectedProducts.length > 0) {
    return {
      action: {
        type: "filter",
        filters: { products: detectedProducts, certified: "all", municipality: "Todos" },
      },
      reply: `He filtrado el mapa para mostrar solo productores de ${detectedProducts.map((p) => translateProduct(p)).join(", ")}.`,
    };
  }

  if (detectedMunicipality) {
    return {
      action: { type: "fly_to_municipality", municipality: detectedMunicipality },
      reply: `He centrado el mapa en ${detectedMunicipality} y filtrado sus productores.`,
    };
  }

  const searchMatch = lower.match(
    /(?:busca|buscar|busco|encuentra|encuentro|ll[eé]vame a|llevar a|ir a|ve a|mu[eé]strame|mostrar)\s+(?:la\s+)?(?:finca|hacienda|granja|cooperativa|plantacion|plantación)\s+(.+)/i
  );
  if (searchMatch) {
    const name = searchMatch[1].trim();
    return {
      action: { type: "search", query: name },
      reply: `Buscando "${name}" en el mapa...`,
    };
  }

  return { action: null };
}

function translateProduct(type: ProductType): string {
  const labels: Record<ProductType, string> = {
    cafe: "café",
    cacao: "cacao",
    platano: "plátano",
    arroz: "arroz",
    palma: "palma aceitera",
    mango: "mango",
    ganaderia: "ganadería",
  };
  return labels[type] || type;
}
