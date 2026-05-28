package com.agrotrace.agrotrace.modules.passports.application.service;

import com.agrotrace.agrotrace.modules.lots.domain.repository.LotRepository;
import com.agrotrace.agrotrace.modules.passports.application.dto.PassportResponseDTO;
import com.agrotrace.agrotrace.modules.passports.domain.model.DigitalPassport;
import com.agrotrace.agrotrace.modules.passports.domain.repository.DigitalPassportRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PassportService {

    private final DigitalPassportRepository passportRepository;
    private final LotRepository lotRepository;

    @Transactional
    public PassportResponseDTO generatePassport(UUID lotId, String baseUrl) {
        if (!lotRepository.existsById(lotId)) {
            throw new BusinessException("LOT_NOT_FOUND", "Lote no encontrado", 404);
        }

        passportRepository.findByLotId(lotId).ifPresent(p -> {
            throw new BusinessException("PASSPORT_EXISTS", "El lote ya tiene un pasaporte digital");
        });

        String publicIdentifier = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String publicUrl = baseUrl + "/public/passports/" + publicIdentifier;

        DigitalPassport passport = DigitalPassport.builder()
                .lotId(lotId)
                .publicIdentifier(publicIdentifier)
                .publicUrl(publicUrl)
                .build();

        passport = passportRepository.save(passport);
        return toDTO(passport);
    }

    public PassportResponseDTO getPassportByLot(UUID lotId) {
        return passportRepository.findByLotId(lotId)
                .map(this::toDTO)
                .orElseThrow(() -> new BusinessException("PASSPORT_NOT_FOUND", "Pasaporte no encontrado", 404));
    }

    public PassportResponseDTO getPassportByPublicId(String publicIdentifier) {
        return passportRepository.findByPublicIdentifier(publicIdentifier)
                .map(this::toDTO)
                .orElseThrow(() -> new BusinessException("PASSPORT_NOT_FOUND", "Pasaporte no encontrado", 404));
    }

    @Transactional
    public PassportResponseDTO publishPassport(UUID lotId) {
        DigitalPassport passport = passportRepository.findByLotId(lotId)
                .orElseThrow(() -> new BusinessException("PASSPORT_NOT_FOUND", "Pasaporte no encontrado", 404));

        passport.setIsPublished(true);
        passport.setPublishedAt(LocalDateTime.now());
        passport = passportRepository.save(passport);
        return toDTO(passport);
    }

    private PassportResponseDTO toDTO(DigitalPassport p) {
        return new PassportResponseDTO(p.getId(), p.getLotId(), p.getPublicIdentifier(), p.getPublicUrl(), p.getIsPublished(), p.getPublishedAt());
    }
}
