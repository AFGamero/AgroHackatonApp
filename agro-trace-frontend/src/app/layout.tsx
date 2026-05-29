import type { Metadata } from "next";
import Header from "@/components/layout/header";
import Footer from "@/components/layout/footer";
import "./globals.css";

export const metadata: Metadata = {
  title: "Nebbi | Trazabilidad Agricola del Magdalena",
  description:
    "Plataforma digital de trazabilidad agricola, turismo rural y comercializacion internacional para productores del departamento del Magdalena, Colombia.",
  keywords: [
    "trazabilidad agricola",
    "productos agricolas",
    "Magdalena",
    "Colombia",
    "banano",
    "guineo",
    "exportacion",
    "turismo rural",
  ],
  authors: [{ name: "Equipo Nebbi" }],
  openGraph: {
    title: "Nebbi | Trazabilidad Agricola del Magdalena",
    description:
      "Plataforma digital de trazabilidad agricola para productores del Magdalena.",
    locale: "es_CO",
    type: "website",
  },
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="es">
      <head>
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossOrigin="anonymous" />
        <link
          href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700;800&family=Inter:wght@400;500;600;700&display=swap"
          rel="stylesheet"
        />
      </head>
      <body className="min-h-screen flex flex-col bg-gray-50">
        <Header />
        <main className="flex-1">{children}</main>
        <Footer />
      </body>
    </html>
  );
}