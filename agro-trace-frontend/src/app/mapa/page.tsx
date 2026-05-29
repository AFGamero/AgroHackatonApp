import Link from "next/link";
import { MapPin, Search, Filter, Layers } from "lucide-react";

export default function MapaPage() {
  return (
    <main className="flex-1 bg-[#FFFAF3]">
      <div className="relative">
        <div className="absolute top-0 left-0 right-0 z-10 p-4">
          <div className="max-w-[1280px] mx-auto flex flex-col sm:flex-row gap-3">
            <div className="relative flex-1">
              <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
              <input
                type="text"
                placeholder="Buscar productos en el mapa..."
                className="w-full pl-12 pr-4 py-3 rounded-xl bg-white shadow-lg border-0 outline-none focus:ring-2 focus:ring-[#6D9E13]/30"
              />
            </div>
            <div className="flex gap-2">
              <button className="flex items-center gap-2 px-4 py-3 bg-white rounded-xl shadow-lg text-sm font-medium text-gray-700 hover:text-[#6D9E13] transition-colors">
                <Filter className="w-4 h-4" />
                Filtros
              </button>
              <button className="flex items-center gap-2 px-4 py-3 bg-white rounded-xl shadow-lg text-sm font-medium text-gray-700 hover:text-[#6D9E13] transition-colors">
                <Layers className="w-4 h-4" />
                Capas
              </button>
            </div>
          </div>
        </div>

        <div className="w-full h-[calc(100vh-64px)] bg-gradient-to-br from-[#DEDB8D]/40 to-[#E3F2FD]/40 flex items-center justify-center">
          <div className="text-center p-8">
            <div className="w-24 h-24 rounded-2xl bg-white shadow-lg flex items-center justify-center mx-auto mb-6">
              <MapPin className="w-12 h-12 text-[#6D9E13]" />
            </div>
            <h2 className="font-heading font-bold text-2xl text-gray-800 mb-2">Mapa Interactivo</h2>
            <p className="text-gray-500 max-w-md mx-auto mb-6">
              Visualiza fincas y productos del Magdalena en tiempo real. Cada punto muestra certificaciones y trazabilidad.
            </p>
            <Link
              href="/tienda"
              className="inline-flex items-center gap-2 px-6 py-3 bg-[#6D9E13] text-white font-semibold rounded-lg hover:bg-[#4A7010] transition-colors"
            >
              Ver Catalogo de Productos
            </Link>
          </div>
        </div>
      </div>
    </main>
  );
}
