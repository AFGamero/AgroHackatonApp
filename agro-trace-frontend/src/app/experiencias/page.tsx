"use client";

import { useState, useRef, useEffect, useCallback } from "react";
import { Bot, Sparkles } from "lucide-react";
import { ChatMessage } from "@/components/chatbot/ChatMessage";
import { ChatInput } from "@/components/chatbot/ChatInput";
import { ChatSuggestions } from "@/components/chatbot/ChatSuggestions";
import { sendMessage, type ChatMessage as ChatMsg } from "@/services/chatbot/ChatbotService";

export default function ChatbotPage() {
  const [messages, setMessages] = useState<ChatMsg[]>([
    {
      role: "assistant",
      content:
        "Bienvenido a NebbiBot! Soy tu asistente virtual especializado en certificaciones Fairtrade y Rainforest Alliance. Puedo guiarte en el proceso de certificacion, explicarte los requisitos y beneficios, y orientarte sobre como usar la plataforma Nebbi. En que puedo ayudarte hoy?",
    },
  ]);
  const [isLoading, setIsLoading] = useState(false);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const scrollToBottom = useCallback(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, []);

  useEffect(() => {
    scrollToBottom();
  }, [messages, scrollToBottom]);

  const handleSend = useCallback(
    async (text: string) => {
      const userMsg: ChatMsg = { role: "user", content: text };
      setMessages((prev) => [...prev, userMsg]);
      setIsLoading(true);

      const response = await sendMessage(text, messages.slice(-10));

      const botMsg: ChatMsg = { role: "assistant", content: response.reply };
      setMessages((prev) => [...prev, botMsg]);
      setIsLoading(false);
    },
    [messages]
  );

  return (
    <div className="flex-1 flex flex-col bg-[#FFFAF3]">
      <div className="bg-gradient-to-r from-[#6D9E13] to-[#4A7010] text-white">
        <div className="max-w-[860px] mx-auto px-6 py-8">
          <div className="flex items-center gap-4">
            <div className="w-14 h-14 rounded-2xl bg-white/20 flex items-center justify-center">
              <Bot className="w-8 h-8" />
            </div>
            <div>
              <h1 className="font-heading font-bold text-2xl md:text-3xl">NebbiBot</h1>
              <p className="text-sm text-white/70 flex items-center gap-1.5 mt-1">
                <Sparkles className="w-3.5 h-3.5" />
                Asistente virtual con IA - Guia de certificaciones Fairtrade y Rainforest Alliance
              </p>
            </div>
          </div>

          <div className="flex flex-wrap gap-2 mt-6">
            {[
              { label: "Que es Fairtrade?", icon: "?" },
              { label: "Requisitos Rainforest Alliance", icon: "?" },
              { label: "Como registro mi certificacion", icon: "?" },
              { label: "Estados de certificacion", icon: "?" },
              { label: "Beneficios de certificarme", icon: "?" },
              { label: "Como verificar una certificacion", icon: "?" },
            ].map((item) => (
              <button
                key={item.label}
                onClick={() => handleSend(item.label)}
                className="flex items-center gap-1.5 px-3 py-1.5 text-xs font-medium rounded-full bg-white/15 text-white hover:bg-white/25 transition-colors"
              >
                {item.label}
              </button>
            ))}
          </div>
        </div>
      </div>

      <div className="flex-1 max-w-[860px] mx-auto w-full px-6 py-6 flex flex-col min-h-0">
        <div className="flex-1 overflow-y-auto space-y-3 mb-4">
          {messages.map((msg, i) => (
            <ChatMessage key={i} role={msg.role} content={msg.content} />
          ))}
          {isLoading && (
            <div className="flex justify-start">
              <div className="bg-gray-100 rounded-2xl rounded-bl-md px-4 py-3">
                <div className="flex gap-1.5">
                  <span className="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style={{ animationDelay: "0ms" }} />
                  <span className="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style={{ animationDelay: "150ms" }} />
                  <span className="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style={{ animationDelay: "300ms" }} />
                </div>
              </div>
            </div>
          )}
          {messages.length <= 1 && (
            <ChatSuggestions onSelect={handleSend} />
          )}
          <div ref={messagesEndRef} />
        </div>

        <div className="bg-white rounded-2xl shadow-lg border border-gray-200">
          <ChatInput onSend={handleSend} disabled={isLoading} />
        </div>

        <p className="text-center text-[10px] text-gray-400 mt-3 flex items-center justify-center gap-1">
          <Sparkles className="w-3 h-3" />
          Potenciado por IA - NebbiBot puede cometer errores. Verifica la informacion importante.
        </p>
      </div>
    </div>
  );
}
