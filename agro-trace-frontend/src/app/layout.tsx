import type { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'AgroTrace Magdalena',
  description: 'Plataforma de trazabilidad agrícola y comercio internacional',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="es">
      <body>{children}</body>
    </html>
  )
}