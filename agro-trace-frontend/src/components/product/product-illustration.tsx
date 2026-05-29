"use client";

import type { ReactNode } from "react";

interface ProductIllustrationProps {
  category: string;
  className?: string;
}

const categoryConfigs: Record<string, { gradient: string; svg: ReactNode }> = {
  "Plátano": {
    gradient: "from-yellow-100 via-yellow-50 to-green-50",
    svg: (
      <svg viewBox="0 0 120 120" fill="none" className="w-full h-full">
        <ellipse cx="60" cy="55" rx="22" ry="38" fill="#F5D442" transform="rotate(-15 60 55)" />
        <ellipse cx="60" cy="55" rx="22" ry="38" fill="#E5C030" transform="rotate(-15 60 55)" opacity="0.3" />
        <path d="M46 22c3-4 10-6 14-3 3 2 4 7 2 11" stroke="#7A9C20" strokeWidth="3" fill="none" />
        <path d="M52 18c2-3 8-4 11-2" stroke="#7A9C20" strokeWidth="2" fill="none" />
        <circle cx="40" cy="50" r="2" fill="#B8900A" opacity="0.5" />
        <circle cx="68" cy="40" r="1.5" fill="#B8900A" opacity="0.5" />
        <circle cx="55" cy="70" r="1" fill="#B8900A" opacity="0.5" />
      </svg>
    ),
  },
  "Café": {
    gradient: "from-amber-100 via-orange-50 to-red-50",
    svg: (
      <svg viewBox="0 0 120 120" fill="none" className="w-full h-full">
        <circle cx="60" cy="70" r="35" fill="#6F4E37" />
        <circle cx="60" cy="70" r="30" fill="#8B6914" />
        <circle cx="60" cy="70" r="26" fill="#A0522D" />
        <ellipse cx="60" cy="55" rx="16" ry="12" fill="#D2A679" />
        <ellipse cx="60" cy="55" rx="10" ry="7" fill="#E8CCAC" />
        <path d="M75 30c3 5 4 12 2 18-3 8-10 14-12 22" stroke="#5C3A1E" strokeWidth="3" fill="none" strokeLinecap="round" />
        <ellipse cx="58" cy="45" rx="2" ry="1.5" fill="#8B4513" />
      </svg>
    ),
  },
  "Cacao": {
    gradient: "from-amber-100 via-yellow-50 to-orange-50",
    svg: (
      <svg viewBox="0 0 120 120" fill="none" className="w-full h-full">
        <ellipse cx="60" cy="65" rx="32" ry="36" fill="#8B4513" />
        <ellipse cx="60" cy="65" rx="28" ry="32" fill="#A0522D" />
        <ellipse cx="58" cy="58" rx="14" ry="18" fill="#D2691E" />
        <ellipse cx="58" cy="55" rx="10" ry="12" fill="#CD853F" />
        <line x1="48" y1="58" x2="68" y2="58" stroke="#6B3410" strokeWidth="1.5" />
        <line x1="50" y1="52" x2="66" y2="52" stroke="#6B3410" strokeWidth="1" />
        <path d="M60 25c0-5-2-10-5-13" stroke="#5C3A1E" strokeWidth="2.5" fill="none" strokeLinecap="round" />
        <path d="M48 28c-2-4-3-9-2-13" stroke="#5C3A1E" strokeWidth="2" fill="none" strokeLinecap="round" />
      </svg>
    ),
  },
  "Mango": {
    gradient: "from-orange-100 via-yellow-50 to-green-50",
    svg: (
      <svg viewBox="0 0 120 120" fill="none" className="w-full h-full">
        <ellipse cx="58" cy="60" rx="24" ry="32" fill="#FF8C00" transform="rotate(-20 58 60)" />
        <ellipse cx="58" cy="60" rx="22" ry="30" fill="#FFA500" transform="rotate(-20 58 60)" />
        <ellipse cx="58" cy="55" rx="14" ry="20" fill="#FFB347" transform="rotate(-20 58 65)" opacity="0.6" />
        <path d="M44 38c2-5 6-9 12-10" stroke="#5C8A0E" strokeWidth="3" fill="none" strokeLinecap="round" />
        <circle cx="50" cy="52" r="1.5" fill="#CC7000" opacity="0.4" />
        <circle cx="62" cy="50" r="1" fill="#CC7000" opacity="0.4" />
      </svg>
    ),
  },
  "Arroz": {
    gradient: "from-green-50 via-lime-50 to-yellow-50",
    svg: (
      <svg viewBox="0 0 120 120" fill="none" className="w-full h-full">
        <path d="M30 90l10-45 5 40 10-42 5 38 10-44 5 42 10-40" stroke="#7A9C20" strokeWidth="2" fill="none" />
        <ellipse cx="55" cy="50" rx="4" ry="7" fill="#F5F5DC" transform="rotate(15 55 50)" />
        <ellipse cx="45" cy="48" rx="3.5" ry="6.5" fill="#FAFAD2" transform="rotate(10 45 48)" />
        <ellipse cx="65" cy="52" rx="3.5" ry="6" fill="#FFF8DC" transform="rotate(20 65 52)" />
        <ellipse cx="35" cy="45" rx="3" ry="5.5" fill="#F5F5DC" transform="rotate(5 35 45)" />
        <ellipse cx="75" cy="55" rx="3" ry="5" fill="#FAFACC" transform="rotate(25 75 55)" />
        <ellipse cx="50" cy="55" rx="3" ry="5" fill="#FFF8DC" transform="rotate(-5 50 55)" />
        <ellipse cx="60" cy="42" rx="3.5" ry="6" fill="#F5F5DC" transform="rotate(12 60 42)" />
        <ellipse cx="40" cy="60" rx="3" ry="5" fill="#FAFAD2" transform="rotate(-10 40 60)" />
        <ellipse cx="70" cy="45" rx="3" ry="5.5" fill="#FFF8DC" transform="rotate(18 70 45)" />
      </svg>
    ),
  },
  "Palma Aceitera": {
    gradient: "from-green-100 via-emerald-50 to-yellow-50",
    svg: (
      <svg viewBox="0 0 120 120" fill="none" className="w-full h-full">
        <rect x="56" y="45" width="8" height="45" rx="3" fill="#8B7355" />
        <circle cx="60" cy="35" r="18" fill="#228B22" />
        <circle cx="60" cy="30" r="14" fill="#2E8B2E" />
        <circle cx="55" cy="22" r="8" fill="#DAA520" opacity="0.8" />
        <circle cx="65" cy="25" r="7" fill="#DAA520" opacity="0.7" />
        <circle cx="50" cy="28" r="6" fill="#DAA520" opacity="0.6" />
        <circle cx="70" cy="32" r="5" fill="#DAA520" opacity="0.5" />
        <path d="M40 50l-15-5 3-3 15 5" fill="#228B22" />
        <path d="M80 50l15-5-3-3-15 5" fill="#228B22" />
        <path d="M38 55l-18 2 2-4 17-1" fill="#2E8B2E" />
        <path d="M82 55l18 2-2-4-17-1" fill="#2E8B2E" />
      </svg>
    ),
  },
  "Ganadería": {
    gradient: "from-stone-100 via-amber-50 to-green-50",
    svg: (
      <svg viewBox="0 0 120 120" fill="none" className="w-full h-full">
        <ellipse cx="60" cy="80" rx="30" ry="10" fill="#7A9C20" opacity="0.3" />
        <ellipse cx="60" cy="60" rx="22" ry="28" fill="#F5DEB3" />
        <ellipse cx="50" cy="50" rx="12" ry="14" fill="#FAEBD7" />
        <ellipse cx="50" cy="48" rx="7" ry="9" fill="#FFF8DC" />
        <ellipse cx="50" cy="48" rx="2.5" ry="5" fill="#FFB6C1" />
        <circle cx="42" cy="45" r="2.5" fill="#333" />
        <circle cx="52" cy="44" r="2.5" fill="#333" />
        <circle cx="42" cy="45" r="1" fill="white" />
        <circle cx="52" cy="44" r="1" fill="white" />
        <path d="M38 55c-2 4-3 8-2 10" stroke="#8B7355" strokeWidth="2" fill="none" />
        <path d="M44 54c-1 3-2 6-1 8" stroke="#8B7355" strokeWidth="2" fill="none" />
        <circle cx="35" cy="45" r="4" fill="#DEB887" />
        <circle cx="60" cy="42" r="3" fill="#DEB887" />
        <rect x="72" y="40" width="18" height="1.5" rx="1" fill="#8B7355" />
        <rect x="76" y="38" width="10" height="1.5" rx="1" fill="#8B7355" />
        <rect x="82" y="42" width="12" height="1" rx="0.5" fill="#8B7355" />
      </svg>
    ),
  },
};

export function ProductIllustration({ category, className = "" }: ProductIllustrationProps) {
  const config = categoryConfigs[category] || categoryConfigs["Plátano"];

  return (
    <div
      className={`w-full h-full bg-gradient-to-br ${config.gradient} p-4 flex items-center justify-center ${className}`}
    >
      <div className="w-[70%] h-[70%] flex items-center justify-center">
        {config.svg}
      </div>
    </div>
  );
}
