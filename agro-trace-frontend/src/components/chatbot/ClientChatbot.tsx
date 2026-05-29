"use client";

import { usePathname } from "next/navigation";
import { ChatbotWidget } from "./ChatbotWidget";
import { useCallback } from "react";
import type { MapAction } from "@/services/chatbot/map-intents";

export function ClientChatbot() {
  const pathname = usePathname();

  const handleMapAction = useCallback((action: MapAction) => {
    if (typeof window !== "undefined") {
      window.dispatchEvent(new CustomEvent("nebbi-map-action", { detail: action }));
    }
  }, []);

  if (pathname === "/experiencias") {
    return null;
  }

  return <ChatbotWidget onMapAction={pathname === "/mapa" ? handleMapAction : undefined} />;
}
