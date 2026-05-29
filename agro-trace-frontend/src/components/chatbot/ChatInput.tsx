"use client";

import { useState, useRef, type KeyboardEvent } from "react";
import { Send } from "lucide-react";

interface ChatInputProps {
  onSend: (message: string) => void;
  disabled?: boolean;
}

export function ChatInput({ onSend, disabled }: ChatInputProps) {
  const [value, setValue] = useState("");
  const inputRef = useRef<HTMLInputElement>(null);

  const handleSend = () => {
    const trimmed = value.trim();
    if (!trimmed || disabled) return;
    onSend(trimmed);
    setValue("");
  };

  const handleKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  return (
    <div className="flex items-center gap-2 p-3 bg-white">
      <input
        ref={inputRef}
        type="text"
        value={value}
        onChange={(e) => setValue(e.target.value)}
        onKeyDown={handleKeyDown}
        placeholder="Escribe tu mensaje..."
        disabled={disabled}
        className="flex-1 px-4 py-2.5 rounded-xl bg-gray-50 border-0 outline-none focus:ring-2 focus:ring-[#6D9E13]/30 text-sm disabled:opacity-50"
      />
      <button
        onClick={handleSend}
        disabled={!value.trim() || disabled}
        className="w-10 h-10 flex items-center justify-center rounded-xl bg-[#6D9E13] text-white hover:bg-[#4A7010] transition-colors disabled:opacity-40 disabled:cursor-not-allowed shrink-0"
        aria-label="Enviar mensaje"
      >
        <Send className="w-4 h-4" />
      </button>
    </div>
  );
}
