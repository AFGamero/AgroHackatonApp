import Link from "next/link";
import Image from "next/image";
import { Search, MapPin, Star, Shield, ChevronRight, Filter, Leaf } from "lucide-react";

const products = [
  {
    id: 1,
    name: "Guineo Verde del Magdalena",
    image: "/nebbi.png",
    price: "$45.000",
    unit: "por caja (22kg)",
    location: "Santa Marta, Magdalena",
    rating: 4.8,
    sales: 234,
    certifications: ["fairtrade", "rainforest"],
    badge: "Mas vendido",
  },
  {
    id: 2,
    name: "Cafe Organico Sierra Nevada",
    image: "/nebbi.png",
    price: "$85.000",
    unit: "por kilo",
    location: "Minca, Magdalena",
    rating: 4.9,
    sales: 189,
    certifications: ["fairtrade", "organic"],
    badge: "Premium",
  },
  {
    id: 3,
    name: "Cacao Fino de Aroma",
    image: "/nebbi.png",
    price: "$65.000",
    unit: "por kilo",
    location: "Fundacion, Magdalena",
    rating: 4.7,
    sales: 156,
    certifications: ["rainforest"],
    badge: "Exportacion",
  },
  {
    id: 4,
    name: "Naranja Valencia Fresca",
    image: "/nebbi.png",
    price: "$30.000",
    unit: "por canastilla (15kg)",
    location: "Cienaga, Magdalena",
    rating: 4.6,
    sales: 312,
    certifications: [],
    badge: "Oferta",
  },
  {
    id: 5,
    name: "Aguacate Hass Premium",
    image: "/nebbi.png",
    price: "$55.000",
    unit: "por caja (10kg)",
    location: "Aracataca, Magdalena",
    rating: 4.8,
    sales: 198,
    certifications: ["fairtrade"],
    badge: null,
  },
  {
    id: 6,
    name: "Miel de Abejas Organica",
    image: "/nebbi.png",
    price: "$40.000",
    unit: "por frasco (500ml)",
    location: "Sierra Nevada, Magdalena",
    rating: 5.0,
    sales: 89,
    certifications: ["organic"],
    badge: "Artesanal",
  },
  {
    id: 7,
    name: "Yuca Fresca del Campo",
    image: "/nebbi.png",
    price: "$25.000",
    unit: "por bulto (25kg)",
    location: "Pivijay, Magdalena",
    rating: 4.5,
    sales: 423,
    certifications: [],
    badge: "Oferta",
  },
  {
    id: 8,
    name: "Tomate Chonto Maduro",
    image: "/nebbi.png",
    price: "$35.000",
    unit: "por canastilla (12kg)",
    location: "El Reten, Magdalena",
    rating: 4.4,
    sales: 267,
    certifications: [],
    badge: null,
  },
];

const categories = [
  { name: "Frutas", icon: "🍌", count: 45 },
  { name: "Verduras", icon: "🥬", count: 32 },
  { name: "Cafe y Cacao", icon: "☕", count: 28 },
  { name: "Granos", icon: "🌽", count: 19 },
  { name: "Miel", icon: "🍯", count: 12 },
  { name: "Lacteos", icon: "🥛", count: 8 },
];

const certificationBadge = (cert: string) => {
  const config: Record<string, { bg: string; text: string; label: string }> = {
    fairtrade: { bg: "#FFF3E0", text: "#E65100", label: "Fairtrade" },
    rainforest: { bg: "#E8F5E9", text: "#2E7D32", label: "Rainforest" },
    organic: { bg: "#E3F2FD", text: "#1565C0", label: "Organico" },
  };
  const c = config[cert];
  if (!c) return null;
  return (
    <span
      className="text-[10px] px-1.5 py-0.5 rounded font-medium border"
      style={{ backgroundColor: c.bg, color: c.text, borderColor: c.bg }}
    >
      {c.label}
    </span>
  );
};

export default function TiendaPage() {
  return (
    <main className="flex-1 bg-[#FFFAF3]">
      {/* Search Hero */}
      <section className="bg-gradient-to-br from-[#6D9E13] to-[#4A7010] py-8">
        <div className="max-w-[1280px] mx-auto px-6">
          <div className="relative max-w-2xl mx-auto">
            <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
            <input
              type="text"
              placeholder="Buscar productos (guineo, cafe, cacao...)"
              className="w-full pl-12 pr-4 py-4 rounded-xl bg-white text-gray-900 placeholder-gray-400 border-0 outline-none focus:ring-2 focus:ring-[#FFFAF3]/30 text-lg"
            />
          </div>
          <div className="flex justify-center gap-3 mt-4 flex-wrap">
            {["Guineo", "Cafe", "Cacao", "Naranja", "Aguacate", "Miel"].map((tag) => (
              <Link
                key={tag}
                href="#"
                className="px-4 py-1.5 bg-white/10 text-white text-sm rounded-full hover:bg-white/20 transition-colors"
              >
                {tag}
              </Link>
            ))}
          </div>
        </div>
      </section>

      {/* Categories */}
      <section className="max-w-[1280px] mx-auto px-6 -mt-6 relative z-10">
        <div className="bg-white rounded-2xl shadow-lg border border-gray-100 p-6 grid grid-cols-3 md:grid-cols-6 gap-3">
          {categories.map((cat) => (
            <Link
              key={cat.name}
              href="#"
              className="flex flex-col items-center gap-1.5 p-3 rounded-xl hover:bg-[#DEDB8D]/30 transition-colors group"
            >
              <span className="text-2xl">{cat.icon}</span>
              <span className="text-xs font-medium text-gray-700 text-center group-hover:text-[#4A7010]">
                {cat.name}
              </span>
              <span className="text-[10px] text-gray-400">{cat.count}</span>
            </Link>
          ))}
        </div>
      </section>

      {/* Product Grid */}
      <section className="max-w-[1280px] mx-auto px-6 py-10">
        <div className="flex items-center justify-between mb-6">
          <div>
            <h2 className="font-heading font-bold text-2xl text-gray-900">Productos del Magdalena</h2>
            <p className="text-sm text-gray-500 mt-0.5">Productos frescos directamente del campo</p>
          </div>
          <button className="hidden sm:flex items-center gap-2 px-4 py-2 text-sm font-medium text-gray-600 border border-gray-200 rounded-lg hover:border-[#6D9E13] hover:text-[#6D9E13] transition-colors">
            <Filter className="w-4 h-4" />
            Filtrar
          </button>
        </div>

        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 md:gap-6">
          {products.map((product) => (
            <Link
              key={product.id}
              href="#"
              className="group bg-white rounded-xl border border-gray-100 overflow-hidden hover:shadow-lg hover:border-[#6D9E13]/30 transition-all"
            >
              <div className="relative aspect-square bg-gradient-to-br from-[#DEDB8D]/30 to-[#E3F2FD]/30 p-6 flex items-center justify-center">
                <Image
                  src={product.image}
                  alt={product.name}
                  width={160}
                  height={160}
                  className="object-contain group-hover:scale-105 transition-transform"
                />
                {product.badge && (
                  <span className="absolute top-3 left-3 bg-[#6D9E13] text-white text-[10px] font-semibold px-2 py-0.5 rounded-full">
                    {product.badge}
                  </span>
                )}
                <button className="absolute top-3 right-3 w-8 h-8 bg-white/80 rounded-full flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
                  <Star className="w-4 h-4 text-gray-400" />
                </button>
              </div>

              <div className="p-4">
                <div className="flex gap-1 mb-1.5 flex-wrap">
                  {product.certifications.map((cert) => (
                    <span key={cert}>{certificationBadge(cert)}</span>
                  ))}
                </div>
                <h3 className="text-sm font-medium text-gray-800 line-clamp-2 leading-snug mb-1">
                  {product.name}
                </h3>
                <div className="flex items-center gap-1 text-yellow-500 mb-1.5">
                  <Star className="w-3.5 h-3.5 fill-current" />
                  <span className="text-xs font-medium text-gray-600">{product.rating}</span>
                  <span className="text-xs text-gray-400">({product.sales})</span>
                </div>
                <div className="flex items-end justify-between">
                  <div>
                    <p className="text-lg font-heading font-bold text-gray-900">
                      {product.price}
                    </p>
                    <p className="text-[11px] text-gray-400">{product.unit}</p>
                  </div>
                  <div className="flex items-center gap-1 text-[11px] text-gray-400">
                    <MapPin className="w-3 h-3" />
                    <span className="truncate max-w-[80px]">{product.location}</span>
                  </div>
                </div>
              </div>
            </Link>
          ))}
        </div>
      </section>
    </main>
  );
}
