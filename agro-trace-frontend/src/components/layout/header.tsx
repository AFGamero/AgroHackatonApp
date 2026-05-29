"use client";

import Link from "next/link";
import { useState } from "react";
import { NebbiLogo } from "@/components/branding";

const navLinks = [
  { href: "/", label: "Inicio" },
  { href: "/tienda", label: "Tienda" },
  { href: "/experiencias", label: "Experiencias" },
  { href: "/pasaporte", label: "Pasaporte Digital" },
];

export function Header() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  return (
    <header className="bg-[#2D8B4E] h-16 sticky top-0 z-40">
      <div className="max-w-[1280px] mx-auto h-full px-6 flex items-center justify-between">
        <Link href="/" className="flex items-center gap-2">
          <NebbiLogo size={36} />
        </Link>

        <nav className="hidden md:flex items-center gap-6">
          {navLinks.map((link) => (
            <Link
              key={link.href}
              href={link.href}
              className="text-white/90 hover:text-white text-sm font-medium transition-colors"
            >
              {link.label}
            </Link>
          ))}
        </nav>

        <div className="flex items-center gap-3">
          <Link
            href="/auth/login"
            className="hidden md:inline-flex items-center px-4 py-2 text-sm font-medium text-white border border-white/30 rounded-lg hover:bg-white/10 transition-colors"
          >
            Iniciar Sesion
          </Link>
          <Link
            href="/auth/register"
            className="inline-flex items-center px-4 py-2 text-sm font-semibold text-[#2D8B4E] bg-white rounded-lg hover:bg-[#E8F5E9] transition-colors"
          >
            Registrarse
          </Link>

          <button
            className="md:hidden p-2 text-white"
            onClick={() => setIsMenuOpen(!isMenuOpen)}
            aria-label="Menu"
          >
            <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              {isMenuOpen ? (
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              ) : (
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
              )}
            </svg>
          </button>
        </div>
      </div>

      {isMenuOpen && (
        <div className="md:hidden bg-[#1B5E34] border-t border-white/10">
          <nav className="px-6 py-4 flex flex-col gap-3">
            {navLinks.map((link) => (
              <Link
                key={link.href}
                href={link.href}
                className="text-white/90 hover:text-white text-sm font-medium py-2"
                onClick={() => setIsMenuOpen(false)}
              >
                {link.label}
              </Link>
            ))}
            <Link
              href="/auth/login"
              className="text-white/90 hover:text-white text-sm font-medium py-2 border-t border-white/10 mt-2 pt-4"
              onClick={() => setIsMenuOpen(false)}
            >
              Iniciar Sesion
            </Link>
          </nav>
        </div>
      )}
    </header>
  );
}

export default Header;