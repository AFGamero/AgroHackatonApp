"use client";

import { useState, useEffect, useCallback } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { QrCode, Download, X } from "lucide-react";

interface QrModalProps {
  isOpen: boolean;
  onClose: () => void;
  url: string;
  producerName: string;
}

export function QrModal({ isOpen, onClose, url, producerName }: QrModalProps) {
  const [qrDataUrl, setQrDataUrl] = useState("");

  useEffect(() => {
    if (isOpen && url) {
      import("qrcode")
        .then((QRCode) =>
          QRCode.toDataURL(url, { width: 320, margin: 2, color: { dark: "#4A7010", light: "#FFFFFF" } })
        )
        .then(setQrDataUrl)
        .catch(console.error);
    }
  }, [isOpen, url]);

  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === "Escape") onClose();
    };
    if (isOpen) {
      document.addEventListener("keydown", handleKeyDown);
      document.body.style.overflow = "hidden";
    }
    return () => {
      document.removeEventListener("keydown", handleKeyDown);
      document.body.style.overflow = "";
    };
  }, [isOpen, onClose]);

  const handleDownload = useCallback(() => {
    if (!qrDataUrl) return;
    const link = document.createElement("a");
    link.download = `qr-nebbi-${producerName.replace(/\s+/g, "-").toLowerCase()}.png`;
    link.href = qrDataUrl;
    link.click();
  }, [qrDataUrl, producerName]);

  return (
    <AnimatePresence>
      {isOpen && (
        <div className="fixed inset-0 z-[9999] flex items-center justify-center">
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={{ duration: 0.15 }}
            className="absolute inset-0 bg-black/50 backdrop-blur-sm"
            onClick={onClose}
          />
          <motion.div
            initial={{ opacity: 0, scale: 0.95, y: 10 }}
            animate={{ opacity: 1, scale: 1, y: 0 }}
            exit={{ opacity: 0, scale: 0.95, y: 10 }}
            transition={{ duration: 0.2, ease: "easeOut" }}
            className="relative z-10 bg-white rounded-2xl shadow-xl p-6 mx-4 max-w-sm w-full"
          >
            <div className="flex items-center justify-between mb-4">
              <div className="flex items-center gap-2">
                <QrCode className="w-5 h-5 text-[#6D9E13]" />
                <h3 className="font-heading font-bold text-lg text-gray-900">Codigo QR</h3>
              </div>
              <button
                onClick={onClose}
                className="w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100 transition-colors"
              >
                <X className="w-4 h-4 text-gray-500" />
              </button>
            </div>

            <div className="bg-[#FFFAF3] rounded-xl p-4 flex items-center justify-center mb-3">
              {qrDataUrl ? (
                <img src={qrDataUrl} alt={`QR para ${producerName}`} className="w-56 h-56 rounded-lg" />
              ) : (
                <div className="w-56 h-56 flex items-center justify-center">
                  <div className="w-8 h-8 border-2 border-[#6D9E13] border-t-transparent rounded-full animate-spin" />
                </div>
              )}
            </div>

            <p className="text-sm font-medium text-gray-800 text-center mb-1">{producerName}</p>
            <p className="text-xs text-gray-400 text-center mb-4 truncate px-2">
              {url.length > 50 ? url.substring(0, 50) + "..." : url}
            </p>

            <button
              onClick={handleDownload}
              disabled={!qrDataUrl}
              className="w-full flex items-center justify-center gap-2 bg-[#6D9E13] text-white text-sm font-medium py-2.5 rounded-xl hover:bg-[#4A7010] transition-colors disabled:opacity-50"
            >
              <Download className="w-4 h-4" />
              Descargar QR
            </button>
          </motion.div>
        </div>
      )}
    </AnimatePresence>
  );
}

interface QrButtonProps {
  url: string;
  producerName: string;
  variant?: "default" | "outline" | "icon";
  className?: string;
}

export function QrButton({ url, producerName, variant = "default", className = "" }: QrButtonProps) {
  const [isOpen, setIsOpen] = useState(false);

  const variantStyles = {
    default:
      "flex-1 flex items-center justify-center gap-1.5 bg-white border border-[#6D9E13] text-[#6D9E13] text-xs font-medium py-2 rounded-lg hover:bg-green-50 transition-colors",
    outline:
      "flex items-center gap-1.5 px-3 py-1.5 border border-gray-200 text-gray-600 text-xs font-medium rounded-lg hover:border-[#6D9E13] hover:text-[#6D9E13] transition-colors",
    icon: "w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100 transition-colors text-gray-500 hover:text-[#6D9E13]",
  };

  return (
    <>
      <button onClick={() => setIsOpen(true)} className={`${variantStyles[variant]} ${className}`}>
        <QrCode className={variant === "icon" ? "w-4 h-4" : "w-3.5 h-3.5"} />
        {variant !== "icon" && <span>QR</span>}
      </button>
      <QrModal isOpen={isOpen} onClose={() => setIsOpen(false)} url={url} producerName={producerName} />
    </>
  );
}
