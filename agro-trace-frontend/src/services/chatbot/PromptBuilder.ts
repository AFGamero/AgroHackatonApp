interface ChatMessage {
  role: "user" | "assistant" | "system";
  content: string;
}

export class PromptBuilder {
  private systemPrompt: string;

  constructor() {
    this.systemPrompt = this.buildSystemPrompt();
  }

  private buildSystemPrompt(): string {
    return `Eres NebbiBot, el asistente virtual de Nebbi especializado en certificaciones Fairtrade y Rainforest Alliance.
Tu objetivo principal es guiar a los productores en el proceso de certificacion.
Reglas: responde en espanol, se conciso (3-4 oraciones maximo), tono amigable y empatico.
Si no sabes algo, di que no tienes esa informacion y ofrece contactar a soporte@nebbi.co.`;
  }

  buildMessages(userMessage: string, history: ChatMessage[]): ChatMessage[] {
    return [
      { role: "system", content: this.systemPrompt },
      ...history.slice(-10),
      { role: "user", content: userMessage },
    ];
  }
}

export const promptBuilder = new PromptBuilder();
