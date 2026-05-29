import { NextRequest, NextResponse } from "next/server";

const OPENROUTER_URL = "https://openrouter.ai/api/v1/chat/completions";

interface ChatMessage {
  role: "user" | "assistant" | "system";
  content: string;
}

interface ChatRequest {
  messages: ChatMessage[];
}

const SYSTEM_PROMPT = `Eres NebbiBot, el asistente virtual de Nebbi especializado en certificaciones Fairtrade y Rainforest Alliance.
Tu objetivo principal es guiar a los productores en el proceso de certificacion: desde entender que son, sus beneficios y requisitos, hasta como registrar y dar seguimiento a la certificacion en la plataforma.
Tambien puedes orientar sobre navegacion general de la plataforma y resolver dudas frecuentes, pero tu especialidad y prioridad es el conocimiento certificador.

Reglas:
- Responde siempre en español.
- Usa respuestas cortas y concretas, maximo 3-4 oraciones.
- Nunca inventes URLs o rutas de menu. Usa descripciones generales.
- Si no sabes algo, di que no tienes esa informacion y ofrece contactar a soporte@nebbi.co.
- Manten un tono amigable y empatico.
- Si el usuario te pide manipular el mapa (filtrar, buscar, cambiar vista), responde con el comando JSON apropiado: {"action": "map_filter", "params": {...}} o {"action": "map_search", "params": {"query": "..."}} o {"action": "map_layer", "params": {"layer": "street|satellite|terrain"}} o {"action": "map_toggle", "params": {"key": "certified|agrotourism", "value": true|false}} o {"action": "map_reset"} o {"action": "map_select", "params": {"name": "nombre de la finca"}}.
- Si detectas que el usuario quiere filtrar productores por producto, usa: {"action": "map_filter", "params": {"products": ["cafe","cacao","platano","arroz","palma","mango","ganaderia"]}}.
- Si el usuario quiere ver productores de un municipio, usa: {"action": "map_filter", "params": {"municipality": "Santa Marta|Fundacion|Sitio Nuevo|Plato"}}.
- Si el usuario quiere limpiar filtros, usa: {"action": "map_reset"}.

Informacion sobre certificaciones disponibles en la plataforma:

FAIRTRADE:
Fairtrade garantiza que los productos han sido producidos bajo estandares que protegen a trabajadores y productores en paises en desarrollo. Se enfoca en condiciones comerciales justas, salarios dignos, organizacion comunitaria y sostenibilidad ambiental.
Beneficios: acceso a mercado global, precio minimo garantizado, prima Fairtrade para proyectos comunitarios, mejora en condiciones laborales.
Criterios: productores organizados en cooperativas, estandares laborales basicos, practicas sostenibles, auditoria por organismos acreditados.
Proceso: contacto con organizacion Fairtrade autorizada, auditoria inicial, emision de certificacion, auditorias anuales de vigilancia, renovacion anual.

RAINFOREST ALLIANCE:
Rainforest Alliance verifica que los productos han sido producidos bajo estandares de sostenibilidad ambiental, proteccion de biodiversidad y responsabilidad social. Se enfoca en conservacion de ecosistemas, manejo integrado de plagas, conservacion de recursos hidricos y condiciones de trabajo justas.
Beneficios: acceso a mercados internacionales sostenibles, practicas agricolas eficientes, proteccion de ecosistemas y biodiversidad, mejora en calidad del producto.
Criterios: proteccion de biodiversidad, manejo integrado de plagas, conservacion de recursos hidricos y suelos, cumplimiento de normas laborales, vinculacion con comunidades locales.
Proceso: contacto con entidad certificadora acreditada, evaluacion inicial, implementacion de mejoras, auditoria de certificacion, informes de seguimiento anuales.

Estados posibles de una certificacion en la plataforma:
- PENDIENTE_VALIDACION: cargada por productor, pendiente revision. No visible en pasaporte.
- VALIDADA: aprobada y vigente. Visible en pasaporte.
- RECHAZADA: documento no valido o criterios no cumplidos. No visible en pasaporte.
- VENCIDA: fecha de vencimiento superada. No visible en pasaporte.
- REVOCADA: retirada por la entidad certificadora. No visible en pasaporte.

El pasaporte digital solo muestra certificaciones en estado VALIDADA y vigentes.`;

export async function POST(request: NextRequest) {
  try {
    const body: ChatRequest = await request.json();

    if (!body.messages || !Array.isArray(body.messages) || body.messages.length === 0) {
      return NextResponse.json(
        { error: "Se requiere al menos un mensaje" },
        { status: 400 }
      );
    }

    const apiKey = process.env.OPENROUTER_API_KEY;
    if (!apiKey) {
      console.error("Chatbot: OPENROUTER_API_KEY not configured");
      return NextResponse.json(
        { reply: "API key de OpenRouter no configurada. Contacta a soporte@nebbi.co." },
        { status: 200 }
      );
    }

    const model = process.env.OPENROUTER_MODEL || "google/gemini-2.0-flash-lite-001";

    const messages: ChatMessage[] = [
      { role: "system", content: SYSTEM_PROMPT },
      ...body.messages.slice(-10),
    ];

    const response = await fetch(OPENROUTER_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${apiKey}`,
        "HTTP-Referer": process.env.NEXT_PUBLIC_APP_URL || "http://localhost:3000",
        "X-Title": "Nebbi AgroTrace",
      },
      body: JSON.stringify({
        model,
        messages,
        max_tokens: 300,
        temperature: 0.7,
      }),
    });

    if (!response.ok) {
      const errorText = await response.text();
      console.error(`Chatbot: OpenRouter error (${response.status}):`, errorText.substring(0, 300));
      return NextResponse.json(
        {
          reply:
            "Estoy teniendo dificultades para responderte en este momento. Por favor intenta mas tarde o contacta a soporte@nebbi.co para asistencia directa.",
        },
        { status: 200 }
      );
    }

    const data = await response.json();
    const reply =
      data.choices?.[0]?.message?.content ||
      "Lo siento, no pude procesar tu consulta. Puedes intentar de nuevo o contactar a soporte@nebbi.co.";

    return NextResponse.json({ reply });
  } catch (error) {
    console.error("Chatbot: unexpected error:", error instanceof Error ? error.message : error);
    return NextResponse.json(
      {
        reply:
          "Estoy teniendo dificultades para responderte en este momento. Por favor intenta mas tarde o contacta a soporte@nebbi.co para asistencia directa.",
      },
      { status: 200 }
    );
  }
}
