'use client';

import { useState, useCallback, useEffect, Suspense } from 'react';
import { useSearchParams } from 'next/navigation';
import dynamic from 'next/dynamic';
import { Layers, Filter } from 'lucide-react';
import SearchBar from './components/SearchBar';
import FilterSidebar from './components/FilterSidebar';
import LayerControl from './components/LayerControl';
import { FilterState } from './components/types';
import { mockProducers } from '@/data/producers';
import { mockAgrotourism } from '@/data/agrotourism';
import type { MapAction } from '@/services/chatbot/map-intents';

const MapView = dynamic(() => import('./components/MapView'), {
  ssr: false,
  loading: () => (
    <div className="w-full h-full flex items-center justify-center bg-gradient-to-br from-[#DEDB8D]/40 to-[#E3F2FD]/40">
      <div className="text-center">
        <div className="w-16 h-16 border-4 border-[#6D9E13] border-t-transparent rounded-full animate-spin mx-auto mb-4" />
        <p className="text-gray-500">Cargando mapa...</p>
      </div>
    </div>
  ),
});

const defaultFilters: FilterState = {
  products: [],
  certified: 'all',
  municipality: 'Todos',
  searchQuery: '',
};

export default function MapaPage() {
  return (
    <Suspense fallback={
      <div className="flex-1 flex items-center justify-center bg-[#FFFAF3]">
        <div className="w-16 h-16 border-4 border-[#6D9E13] border-t-transparent rounded-full animate-spin" />
      </div>
    }>
      <MapaPageInner />
    </Suspense>
  );
}

function MapaPageInner() {
  const searchParams = useSearchParams();
  const [filters, setFilters] = useState<FilterState>(defaultFilters);
  const [isLayerOpen, setIsLayerOpen] = useState(false);
  const [selectedProducer, setSelectedProducer] = useState<string | null>(null);
  const [selectedAgrotourism, setSelectedAgrotourism] = useState<string | null>(null);
  const [activeLayer, setActiveLayer] = useState<'street' | 'satellite' | 'terrain'>('street');
  const [showOnlyCertified, setShowOnlyCertified] = useState(false);
  const [showAgrotourism, setShowAgrotourism] = useState(true);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const searchQueryFromUrl = searchParams.get('search');

  useEffect(() => {
    if (searchQueryFromUrl) {
      setFilters((prev) => ({ ...prev, searchQuery: searchQueryFromUrl }));
    }
  }, [searchQueryFromUrl]);

  const handleSearchChange = useCallback((query: string) => {
    setFilters((prev) => ({ ...prev, searchQuery: query }));
  }, []);

  const handleFiltersChange = useCallback((newFilters: FilterState) => {
    setFilters(newFilters);
  }, []);

  const handleProducerSelect = useCallback((producerId: string) => {
    setSelectedProducer(producerId);
    setSelectedAgrotourism(null);
  }, []);

  const handleAgrotourismSelect = useCallback((agrotourismId: string) => {
    setSelectedAgrotourism(agrotourismId);
    setSelectedProducer(null);
  }, []);

  const handleLayerChange = useCallback((layer: 'street' | 'satellite' | 'terrain') => {
    setActiveLayer(layer);
  }, []);

  const handleShowCertifiedChange = useCallback((show: boolean) => {
    setShowOnlyCertified(show);
  }, []);

  useEffect(() => {
    if (filters.searchQuery.trim().length > 0) {
      const matchingProducer = mockProducers.find(
        (p) => p.name.toLowerCase().includes(filters.searchQuery.toLowerCase())
      );
      if (matchingProducer) {
        setSelectedProducer(matchingProducer.id);
      } else {
        setSelectedProducer(null);
      }
    } else {
      setSelectedProducer(null);
    }
  }, [filters.searchQuery]);

  useEffect(() => {
    const handleMapAction = (e: Event) => {
      const action = (e as CustomEvent<MapAction>).detail;
      switch (action.type) {
        case "filter":
          setFilters((prev) => ({
            ...prev,
            products: action.filters.products ?? prev.products,
            municipality: action.filters.municipality ?? prev.municipality,
            certified: action.filters.certified ?? prev.certified,
          }));
          break;
        case "search": {
          const query = action.query;
          setFilters((prev) => ({ ...prev, searchQuery: query }));
          setTimeout(() => {
            const match = mockProducers.find(
              (p) => p.name.toLowerCase().includes(query.toLowerCase())
            );
            if (match) {
              setSelectedProducer(match.id);
              setSelectedAgrotourism(null);
            }
          }, 100);
          break;
        }
        case "select_producer": {
          const match = mockProducers.find(
            (p) => p.name.toLowerCase().includes(action.name.toLowerCase())
          );
          if (match) {
            setFilters((prev) => ({ ...prev, searchQuery: match.name }));
            setSelectedProducer(match.id);
            setSelectedAgrotourism(null);
          }
          break;
        }
        case "select_agrotourism": {
          const match = mockAgrotourism.find(
            (ap) => ap.farmName.toLowerCase().includes(action.name.toLowerCase())
          );
          if (match) {
            setSelectedAgrotourism(match.id);
            setSelectedProducer(null);
          }
          break;
        }
        case "layer":
          setActiveLayer(action.layer);
          break;
        case "toggle_certified":
          setShowOnlyCertified(action.value);
          break;
        case "toggle_agrotourism":
          setShowAgrotourism(action.value);
          break;
        case "reset":
          setFilters(defaultFilters);
          setShowOnlyCertified(false);
          setShowAgrotourism(true);
          setSelectedProducer(null);
          setSelectedAgrotourism(null);
          break;
        case "fly_to_municipality": {
          const municipalityProducers = mockProducers.filter(
            (p) => p.municipality.toLowerCase().includes(action.municipality.toLowerCase())
          );
          if (municipalityProducers.length > 0) {
            setFilters((prev) => ({
              ...prev,
              municipality: action.municipality,
              products: [],
              certified: "all",
              searchQuery: "",
            }));
            setSelectedProducer(municipalityProducers[0].id);
            setSelectedAgrotourism(null);
          }
          break;
        }
      }
    };

    window.addEventListener("nebbi-map-action", handleMapAction);
    return () => window.removeEventListener("nebbi-map-action", handleMapAction);
  }, []);

  return (
    <div className="flex-1 flex bg-[#FFFAF3] relative overflow-hidden">
      {/* Desktop sidebar */}
      <div className="hidden lg:block shrink-0">
        <FilterSidebar
          filters={filters}
          onFiltersChange={handleFiltersChange}
        />
      </div>

      {/* Mobile sidebar overlay */}
      {isSidebarOpen && (
        <>
          <div
            className="fixed inset-0 bg-black/50 z-40 lg:hidden"
            onClick={() => setIsSidebarOpen(false)}
          />
          <div className="fixed left-0 top-0 z-50 h-full w-80 lg:hidden overflow-y-auto shadow-2xl" style={{ maxWidth: "85vw" }}>
            <FilterSidebar
              filters={filters}
              onFiltersChange={handleFiltersChange}
            />
          </div>
        </>
      )}

      <div className="flex-1 flex flex-col min-h-0">
        <div className="relative bg-[#FFFAF3] px-4 py-3 shrink-0">
          <div className="max-w-[1280px] mx-auto flex items-center gap-3">
            <button
              onClick={() => setIsSidebarOpen(true)}
              className="lg:hidden flex items-center gap-2 px-4 py-3 rounded-xl shadow-lg text-sm font-medium bg-white text-gray-700 hover:text-[#6D9E13] transition-colors shrink-0"
            >
              <Filter className="w-4 h-4" />
              <span className="hidden sm:inline">Filtros</span>
            </button>
            <div className="flex-1">
              <SearchBar
                value={filters.searchQuery}
                onChange={handleSearchChange}
                placeholder="Buscar fincas, municipios, productos..."
              />
            </div>
            <div className="flex gap-2 shrink-0 relative">
              <button
                onClick={() => setIsLayerOpen(!isLayerOpen)}
                className={`flex items-center gap-2 px-4 py-3 rounded-xl shadow-lg text-sm font-medium transition-colors ${
                  isLayerOpen
                    ? 'bg-[#6D9E13] text-white'
                    : 'bg-white text-gray-700 hover:text-[#6D9E13]'
                }`}
              >
                <Layers className="w-4 h-4" />
                <span className="hidden sm:inline">Capas</span>
              </button>

              <LayerControl
                isOpen={isLayerOpen}
                onClose={() => setIsLayerOpen(false)}
                activeLayer={activeLayer}
                onLayerChange={handleLayerChange}
                showCertified={showOnlyCertified}
                onShowCertifiedChange={handleShowCertifiedChange}
                showAgrotourism={showAgrotourism}
                onShowAgrotourismChange={setShowAgrotourism}
              />
            </div>
          </div>
        </div>

        <div className="flex-1 min-h-0 relative overflow-hidden">
          <MapView
            producers={mockProducers}
            agrotourismPoints={mockAgrotourism}
            filters={filters}
            selectedProducerId={selectedProducer}
            selectedAgrotourismId={selectedAgrotourism}
            onProducerClick={handleProducerSelect}
            onAgrotourismClick={handleAgrotourismSelect}
            activeLayer={activeLayer}
            showOnlyCertified={showOnlyCertified}
            showAgrotourism={showAgrotourism}
          />
        </div>
      </div>
    </div>
  );
}
