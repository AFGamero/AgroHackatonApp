import Link from "next/link";
import { Users, MapPin, Star, Shield, Package, Sprout, ArrowRight } from "lucide-react";

const productores = [
  {
    id: 1,
    name: "Cooperativa Agricola del Magdalena",
    location: "Santa Marta",
    products: ["Guineo", "Cafe", "Cacao"],
    certifications: ["fairtrade", "rainforest"],
    rating: 4.9,
    description: "Cooperativa con mas de 20 anos de experiencia exportando guineo verde y cafe organico.",
  },
  {
    id: 2,
    name: "Finca El Paraiso",
    location: "Minca",
    products: ["Cafe", "Miel"],
    certifications: ["organic"],
    rating: 4.8,
    description: "Finca familiar especializada en cafe de altura cultivado bajo sombra en la Sierra Nevada.",
  },
  {
    id: 3,
    name: "Agroindustrias del Caribe",
    location: "Fundacion",
    products: ["Cacao", "Naranja", "Aguacate"],
    certifications: ["rainforest", "fairtrade"],
    rating: 4.7,
    description: "Empresa dedicada a la produccion y exportacion de cacao fino de aroma.",
  },
  {
    id: 4,
    name: "Hacienda Santa Elena",
    location: "Cienaga",
    products: ["Guineo", "Tomate", "Yuca"],
    certifications: [],
    rating: 4.5,
    description: "Productores tradicionales de la region bananera con productos frescos de temporada.",
  },
  {
    id: 5,
    name: "Asociacion de Caficultores Sierra Nevada",
    location: "Aracataca",
    products: ["Cafe"],
    certifications: ["fairtrade", "rainforest", "organic"],
    rating: 5.0,
    description: "Asociacion que agrupa a 45 familias caficultoras de la Sierra Nevada de Santa Marta.",
  },
  {
    id: 6,
    name: "Finca Los Mangos",
    location: "Pivijay",
    products: ["Yuca", "Guineo", "Maiz"],
    certifications: ["fairtrade"],
    rating: 4.4,
    description: "Finca diversificada con cultivos de pancoger y productos para el mercado local y nacional.",
  },
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

export default function ProductoresPage() {
  return (
    <main className="flex-1 bg-[#FFFAF3]">
      <section className="bg-gradient-to-br from-[#6D9E13] to-[#4A7010] py-10 text-white">
        <div className="max-w-[1280px] mx-auto px-6">
          <h1 className="font-heading font-bold text-3xl md:text-4xl mb-3">Productores del Magdalena</h1>
          <p className="text-white/80 max-w-lg">
            Conoce a los productores que cultivan los mejores productos agricolas de la region. Directo del campo a tu mesa.
          </p>
        </div>
      </section>

      <section className="max-w-[1280px] mx-auto px-6 py-10">
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {productores.map((producer) => (
            <div
              key={producer.id}
              className="bg-white rounded-xl border border-gray-100 p-6 hover:shadow-lg hover:border-[#6D9E13]/30 transition-all"
            >
              <div className="flex items-start justify-between mb-3">
                <div className="w-12 h-12 rounded-full bg-[#DEDB8D] flex items-center justify-center">
                  <Users className="w-6 h-6 text-[#6D9E13]" />
                </div>
                <div className="flex items-center gap-1">
                  <Star className="w-4 h-4 fill-yellow-500 text-yellow-500" />
                  <span className="text-sm font-medium text-gray-700">{producer.rating}</span>
                </div>
              </div>

              <h3 className="font-heading font-bold text-lg text-gray-900 mb-1">{producer.name}</h3>
              <div className="flex items-center gap-1 text-sm text-gray-500 mb-3">
                <MapPin className="w-3.5 h-3.5" />
                {producer.location}
              </div>

              <p className="text-sm text-gray-600 mb-4 leading-relaxed line-clamp-2">
                {producer.description}
              </p>

              <div className="flex gap-1 mb-3 flex-wrap">
                {producer.certifications.map((cert) => (
                  <span key={cert}>{certificationBadge(cert)}</span>
                ))}
              </div>

              <div className="flex flex-wrap gap-1.5 mb-4">
                {producer.products.map((p) => (
                  <span
                    key={p}
                    className="px-2 py-0.5 bg-[#DEDB8D]/40 text-[#4A7010] text-xs font-medium rounded-full"
                  >
                    {p}
                  </span>
                ))}
              </div>

              <Link
                href="/tienda"
                className="inline-flex items-center gap-1 text-sm font-medium text-[#6D9E13] hover:text-[#4A7010] transition-colors"
              >
                Ver productos
                <ArrowRight className="w-4 h-4" />
              </Link>
            </div>
          ))}
        </div>
      </section>
    </main>
  );
}
