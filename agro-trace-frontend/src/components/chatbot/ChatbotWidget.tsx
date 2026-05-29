"use client";

import { useState, useRef, useEffect, useCallback } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { X, Bot } from "lucide-react";
import { ChatMessage } from "./ChatMessage";
import { ChatInput } from "./ChatInput";
import { ChatSuggestions } from "./ChatSuggestions";
import { sendMessage, type ChatMessage as ChatMsg, type ChatbotResponse } from "@/services/chatbot/ChatbotService";
import type { MapAction } from "@/services/chatbot/map-intents";

interface ChatbotWidgetProps {
  onMapAction?: (action: MapAction) => void;
}

export function ChatbotWidget({ onMapAction }: ChatbotWidgetProps) {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState<ChatMsg[]>([
    {
      role: "assistant",
      content:
        "Hola, soy NebbiBot! Soy tu guia experto en certificaciones Fairtrade y Rainforest Alliance. Preguntame lo que necesites sobre certificaciones, trazabilidad o navegacion de la plataforma.",
    },
  ]);
  const [isLoading, setIsLoading] = useState(false);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const scrollToBottom = useCallback(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, []);

  useEffect(() => {
    if (isOpen) scrollToBottom();
  }, [messages, isOpen, scrollToBottom]);

  const handleSend = useCallback(
    async (text: string) => {
      const userMsg: ChatMsg = { role: "user", content: text };
      setMessages((prev) => [...prev, userMsg]);
      setIsLoading(true);

      const history = messages.filter((m) => m.role !== "assistant" || m.content.length < 500);

      const response: ChatbotResponse = await sendMessage(text, history);

      if (response.mapAction && onMapAction) {
        onMapAction(response.mapAction);
      }

      const botMsg: ChatMsg = { role: "assistant", content: response.reply };
      setMessages((prev) => [...prev, botMsg]);
      setIsLoading(false);
    },
    [messages, onMapAction]
  );

  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === "Escape" && isOpen) {
        setIsOpen(false);
      }
    };
    document.addEventListener("keydown", handleKeyDown);
    return () => document.removeEventListener("keydown", handleKeyDown);
  }, [isOpen]);

  return (
    <>
      <button
        onClick={() => setIsOpen(!isOpen)}
        className={`fixed bottom-6 right-6 z-50 w-14 h-14 rounded-full shadow-xl flex items-center justify-center transition-all duration-300 hover:scale-110 active:scale-95 ${
          isOpen
            ? "bg-gray-200 text-gray-600 hover:bg-gray-300"
            : "bg-[#6D9E13] text-white hover:bg-[#4A7010]"
        }`}
        aria-label={isOpen ? "Cerrar chat" : "Abrir chat"}
      >
        {isOpen ? <X className="w-6 h-6" /> : <Bot className="w-7 h-7" />}
      </button>

      <AnimatePresence>
        {isOpen && (
          <motion.div
            initial={{ opacity: 0, y: 20, scale: 0.95 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            exit={{ opacity: 0, y: 20, scale: 0.95 }}
            transition={{ duration: 0.2, ease: "easeOut" }}
            className="fixed bottom-24 right-6 z-50 w-[370px] max-w-[calc(100vw-3rem)] h-[520px] max-h-[calc(100vh-8rem)] bg-white rounded-2xl shadow-2xl flex flex-col overflow-hidden border border-gray-200"
          >
            <div className="flex items-center gap-3 px-4 py-3 bg-gradient-to-r from-[#6D9E13] to-[#4A7010] text-white shrink-0">
              <div className="w-9 h-9 rounded-full bg-white/20 flex items-center justify-center">
                <Bot className="w-5 h-5" />
              </div>
              <div>
                <h3 className="font-heading font-bold text-sm">NebbiBot</h3>
                <p className="text-[10px] text-white/70">Asistente virtual</p>
              </div>
            </div>

            <div className="flex-1 overflow-y-auto px-4 py-3 space-y-3 bg-[#FFFAF3]">
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
              <div ref={messagesEndRef} />
            </div>

            {messages.length <= 1 && (
              <ChatSuggestions onSelect={handleSend} />
            )}

            <div className="border-t border-gray-100">
              <ChatInput onSend={handleSend} disabled={isLoading} />
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </>
  );
}
