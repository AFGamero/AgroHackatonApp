import Link from "next/link";
import { Badge } from "@/components/ui/badge";
import { NebbiMascot, ProductIcon, CertificationBadge } from "@/components/branding";
import {
  MapPin,
  CheckCircle,
  QrCode,
  ShieldCheck,
  Leaf,
  Globe,
  ArrowRight,
  Package,
  TreePine,
  Coffee,
  Apple,
} from "lucide-react";

const features = [
  {
    icon: <MapPin className="w-8 h-8" />,
    title: "Trazabilidad Total",
    description: "Registra fincas, lotes y estados de cultivo. Cada producto tiene un historial verificable.",
  },
  {
    icon: <QrCode className="w-8 h-8" />,
    title: "Pasaporte Digital",
    description: "Genera codigos QR unicos para cada lote. Compradores y turistas escanean y verifican origen.",
  },
  {
    icon: <ShieldCheck className="w-8 h-8" />,
    title: "Certificaciones",
    description: "Registra y verifica certificaciones Fairtrade y Rainforest Alliance en el pasaporte digital.",
  },
  {
    icon: <Leaf className="w-8 h-8" />,
    title: "Turismo Rural",
    description: "Publica experiencias turisticas asociadas a fincas. Conecta el campo con visitantes.",
  },
  {
    icon: <Globe className="w-8 h-8" />,
    title: "Comercio Internacional",
    description: "Recibe solicitudes de compra de compradores internacionales interesados en tus productos.",
  },
  {
    icon: <CheckCircle className="w-8 h-8" />,
    title: "Evidencias Visuales",
    description: "Adjunta fotografas y documentos a cada lote. Construye confianza con preuves.",
  },
];

const certifications = [
  { name: "Fairtrade", type: "fairtrade" as const, description: "Comercio justo y condiciones dignas" },
  { name: "Rainforest Alliance", type: "rainforest" as const, description: "Sostenibilidad y conservacion" },
];

const productIcons = [
  { name: "Guineo", icon: "guineo", color: "bg-[#2D8B4E]" },
  { name: "Cafe", icon: "cafe", color: "bg-[#6F4E37]" },
  { name: "Cacao", icon: "cacao", color: "bg-[#4A2C2A]" },
  { name: "Naranja", icon: "naranja", color: "bg-[#FF8C00]" },
];

export default function HomePage() {
  return (
    <main className="flex-1">
      <section className="relative bg-gradient-to-br from-[#2D8B4E] to-[#1B5E34] text-white overflow-hidden">
        <div className="absolute inset-0 opacity-10">
          <div className="absolute top-10 left-10 w-32 h-32 rounded-full bg-white/20" />
          <div className="absolute bottom-20 right-20 w-48 h-48 rounded-full bg-white/10" />
          <div className="absolute top-1/2 left-1/3 w-24 h-24 rounded-full bg-white/15" />
        </div>
        
        <div className="max-w-[1280px] mx-auto px-6 py-20 md:py-32 grid md:grid-cols-2 gap-12 items-center relative z-10">
          <div>
            <Badge variant="success" className="mb-6">
              Producto representativo: Guineo Verde del Magdalena
            </Badge>
            <h1 className="font-heading font-extrabold text-4xl md:text-5xl leading-tight mb-6">
              Trazabilidad Agricola desde el Origen
            </h1>
            <p className="text-lg text-white/80 mb-8 max-w-lg">
              Nebbi conecta productores con compradores internacionales mediante Pasaportes Digitales verificables por QR. Certificaciones Fairtrade y Rainforest Alliance garantizan calidad y origen.
            </p>
            <div className="flex flex-col sm:flex-row gap-4">
              <Link
                href="/auth/register"
                className="inline-flex items-center justify-center gap-2 px-8 py-4 bg-white text-[#2D8B4E] font-semibold rounded-lg hover:bg-[#E8F5E9] transition-colors"
              >
                Comenzar Ahora
                <ArrowRight className="w-5 h-5" />
              </Link>
              <Link
                href="/tienda"
                className="inline-flex items-center justify-center px-8 py-4 border-2 border-white text-white font-semibold rounded-lg hover:bg-white/10 transition-colors"
              >
                Explorar Productos
              </Link>
            </div>
          </div>
          
          <div className="hidden md:flex justify-center">
            <NebbiMascot size={280} />
          </div>
        </div>
        
        <div className="absolute bottom-0 left-0 right-0 h-16 bg-gradient-to-t from-gray-50 to-transparent" />
      </section>

      <section className="py-16 md:py-24 bg-gray-50">
        <div className="max-w-[1280px] mx-auto px-6">
          <div className="text-center mb-16">
            <h2 className="font-heading font-bold text-3xl md:text-4xl text-gray-900 mb-4">
              Propuesta de Valor
            </h2>
            <p className="text-lg text-gray-600 max-w-2xl mx-auto">
              Nebbi ofrece a cada actor del ecosistema agricola las herramientas para conectar, verificar y comercializar con transparencia.
            </p>
          </div>
          
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
            {features.map((feature, index) => (
              <div key={index} className="card p-8 hover:shadow-lg hover:border-[#2D8B4E] transition-all">
                <div className="w-14 h-14 rounded-xl bg-[#E8F5E9] flex items-center justify-center text-[#2D8B4E] mb-6">
                  {feature.icon}
                </div>
                <h3 className="font-heading font-bold text-xl text-gray-900 mb-3">
                  {feature.title}
                </h3>
                <p className="text-gray-600">
                  {feature.description}
                </p>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="py-16 md:py-24 bg-white">
        <div className="max-w-[1280px] mx-auto px-6">
          <div className="grid md:grid-cols-2 gap-12 items-center">
            <div>
              <h2 className="font-heading font-bold text-3xl md:text-4xl text-gray-900 mb-6">
                Explora Productos en el Mapa
              </h2>
              <p className="text-lg text-gray-600 mb-8">
                Visualiza productos agricolas de la costa colombiana en un mapa interactivo. Cada punto representa una finca con sus productos y certificaciones. Haz clic para ver detalles.
              </p>
              <ul className="space-y-4 mb-8">
                <li className="flex items-center gap-3">
                  <CheckCircle className="w-5 h-5 text-[#2D8B4E] flex-shrink-0" />
                  <span className="text-gray-700">Iconos distintivos por tipo de producto</span>
                </li>
                <li className="flex items-center gap-3">
                  <CheckCircle className="w-5 h-5 text-[#2D8B4E] flex-shrink-0" />
                  <span className="text-gray-700">Informacion de certificaciones al presionar</span>
                </li>
                <li className="flex items-center gap-3">
                  <CheckCircle className="w-5 h-5 text-[#2D8B4E] flex-shrink-0" />
                  <span className="text-gray-700">Solicita productos directamente desde el mapa</span>
                </li>
              </ul>
              <Link
                href="/tienda"
                className="inline-flex items-center gap-2 px-6 py-3 bg-[#2D8B4E] text-white font-semibold rounded-lg hover:bg-[#1B5E34] transition-colors"
              >
                <MapPin className="w-5 h-5" />
                Ver Mapa de Productos
              </Link>
            </div>
            
            <div className="relative bg-gradient-to-br from-[#E8F5E9] to-[#E3F2FD] rounded-2xl p-8 aspect-[4/3] flex items-center justify-center overflow-hidden">
              <div className="absolute inset-0 opacity-30">
                <div className="absolute top-4 left-4 w-16 h-16 rounded-full bg-[#2D8B4E]/20" />
                <div className="absolute bottom-8 right-8 w-20 h-20 rounded-full bg-[#1565C0]/20" />
                <div className="absolute top-1/2 left-1/2 w-12 h-12 rounded-full bg-[#2D8B4E]/10" />
              </div>
              <div className="relative text-center">
                <div className="flex justify-center gap-4 mb-6">
                  {productIcons.map((product, i) => (
                    <div
                      key={i}
                      className={`w-14 h-14 ${product.color} rounded-full flex items-center justify-center text-white shadow-lg`}
                    >
                      <ProductIcon product={product.icon} size={28} />
                    </div>
                  ))}
                </div>
                <p className="text-[#1B5E34] font-semibold text-lg">Magdalena, Colombia</p>
                <p className="text-[#2D8B4E] text-sm mt-2">+15 fincas activas</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className="py-16 md:py-24 bg-[#1B5E34] text-white">
        <div className="max-w-[1280px] mx-auto px-6 text-center">
          <h2 className="font-heading font-bold text-3xl md:text-4xl mb-6">
            Certificaciones que Generan Confianza
          </h2>
          <p className="text-lg text-white/80 max-w-2xl mx-auto mb-12">
            Registra y muestra las certificaciones Fairtrade y Rainforest Alliance en el pasaporte digital de cada producto. Compradores internacionales verifican origen y calidad.
          </p>
          
          <div className="grid md:grid-cols-2 gap-8 max-w-3xl mx-auto">
            {certifications.map((cert) => (
              <div key={cert.name} className="bg-white/10 backdrop-blur rounded-xl p-8 text-center">
                <CertificationBadge type={cert.type} />
                <h3 className="font-heading font-bold text-2xl mt-4 mb-2">{cert.name}</h3>
                <p className="text-white/70">{cert.description}</p>
              </div>
            ))}
          </div>
          
          <div className="mt-12">
            <Link
              href="/auth/register"
              className="inline-flex items-center gap-2 px-8 py-4 bg-white text-[#1B5E34] font-semibold rounded-lg hover:bg-[#E8F5E9] transition-colors"
            >
              <ShieldCheck className="w-5 h-5" />
              Registrar Certificaciones
            </Link>
          </div>
        </div>
      </section>

      <section className="py-16 md:py-24 bg-gray-50">
        <div className="max-w-[1280px] mx-auto px-6">
          <div className="text-center mb-16">
            <h2 className="font-heading font-bold text-3xl md:text-4xl text-gray-900 mb-4">
              Como Funciona
            </h2>
            <p className="text-lg text-gray-600">
              De la finca al comprador internacional, todo queda documentado y verificable.
            </p>
          </div>
          
          <div className="grid md:grid-cols-4 gap-8">
            {[
              { step: "1", title: "Registra tu Finca", desc: "Agrega ubicacion, fotos y datos basicos" },
              { step: "2", title: "Crea Lotes", desc: "Asocia cultivos, variedades y fechas" },
              { step: "3", title: "Agrega Evidencias", desc: "Sube fotos y documentos del proceso" },
              { step: "4", title: "Genera Pasaporte", desc: "Obtén un QR unico para cada lote" },
            ].map((item, index) => (
              <div key={index} className="text-center">
                <div className="w-16 h-16 rounded-full bg-[#2D8B4E] text-white font-heading font-bold text-2xl flex items-center justify-center mx-auto mb-4">
                  {item.step}
                </div>
                <h3 className="font-heading font-bold text-lg text-gray-900 mb-2">{item.title}</h3>
                <p className="text-gray-600 text-sm">{item.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="py-16 md:py-24 bg-white">
        <div className="max-w-[1280px] mx-auto px-6">
          <div className="bg-gradient-to-br from-[#2D8B4E] to-[#1B5E34] rounded-2xl p-12 text-center text-white">
            <h2 className="font-heading font-bold text-3xl md:text-4xl mb-6">
              Conecta con el Mundo
            </h2>
            <p className="text-lg text-white/80 max-w-2xl mx-auto mb-8">
              Buyers internacionales escanean el QR del producto, ven la trazabilidad completa, certificaciones y pueden enviarte una solicitud de compra directamente.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Link
                href="/auth/register"
                className="inline-flex items-center justify-center gap-2 px-8 py-4 bg-white text-[#2D8B4E] font-semibold rounded-lg hover:bg-[#E8F5E9] transition-colors"
              >
                <Package className="w-5 h-5" />
                Crear Cuenta Gratis
              </Link>
              <Link
                href="/pasaporte"
                className="inline-flex items-center justify-center gap-2 px-8 py-4 border-2 border-white text-white font-semibold rounded-lg hover:bg-white/10 transition-colors"
              >
                <QrCode className="w-5 h-5" />
                Ver Ejemplo de Pasaporte
              </Link>
            </div>
          </div>
        </div>
      </section>
    </main>
  );
}