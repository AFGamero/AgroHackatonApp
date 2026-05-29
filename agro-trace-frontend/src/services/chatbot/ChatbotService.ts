import { parseMapIntent, MapAction } from "./map-intents";

interface ChatMessage {
  role: "user" | "assistant";
  content: string;
}

interface ChatbotResponse {
  reply: string;
  mapAction?: MapAction | null;
}

export async function sendMessage(
  userMessage: string,
  history: ChatMessage[]
): Promise<ChatbotResponse> {
  const intent = parseMapIntent(userMessage);
  if (intent.action) {
    return {
      reply: intent.reply || "He actualizado el mapa segun tu solicitud.",
      mapAction: intent.action,
    };
  }

  try {
    const messages = [
      ...history.map((m) => ({ role: m.role, content: m.content })),
      { role: "user" as const, content: userMessage },
    ];

    const response = await fetch("/api/chatbot", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ messages }),
    });

    if (!response.ok) {
      throw new Error("API error");
    }

    const data = await response.json();
    return { reply: data.reply || "No pude procesar tu consulta.", mapAction: null };
  } catch {
    return {
      reply:
        "Estoy teniendo dificultades para responderte en este momento. Por favor intenta mas tarde o contacta a soporte@nebbi.co para asistencia directa.",
      mapAction: null,
    };
  }
}

export type { ChatMessage, ChatbotResponse };
