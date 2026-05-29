"use client";

interface ChatSuggestionsProps {
  onSelect: (message: string) => void;
}

const suggestions = [
  "Que es Fairtrade?",
  "Como obtengo la certificacion Rainforest Alliance?",
  "Ver fincas de cafe",
  "Ver solo certificados",
  "Mostrar agroturismo",
  "Ir a Santa Marta",
];

export function ChatSuggestions({ onSelect }: ChatSuggestionsProps) {
  return (
    <div className="flex flex-wrap gap-2 px-4 py-3">
      {suggestions.map((suggestion) => (
        <button
          key={suggestion}
          onClick={() => onSelect(suggestion)}
          className="px-3 py-1.5 text-xs font-medium rounded-full border border-[#6D9E13]/30 text-[#6D9E13] bg-white hover:bg-[#DEDB8D]/30 transition-colors"
        >
          {suggestion}
        </button>
      ))}
    </div>
  );
}
