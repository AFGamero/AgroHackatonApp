package com.agrotrace.agrotrace.modules.products.application.service;

import com.agrotrace.agrotrace.modules.farms.domain.repository.FarmRepository;
import com.agrotrace.agrotrace.modules.lots.domain.model.Lot;
import com.agrotrace.agrotrace.modules.lots.domain.repository.LotRepository;
import com.agrotrace.agrotrace.modules.products.application.dto.ProductCatalogDTO;
import com.agrotrace.agrotrace.modules.producers.domain.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final LotRepository lotRepository;
    private final FarmRepository farmRepository;
    private final ProducerRepository producerRepository;

    public List<ProductCatalogDTO> getPublicCatalog(String search) {
        return lotRepository.findAll().stream()
                .filter(lot -> search == null || search.isEmpty() ||
                        lot.getCrop().toLowerCase().contains(search.toLowerCase()) ||
                        lot.getName().toLowerCase().contains(search.toLowerCase()))
                .map(lot -> {
                    var farm = farmRepository.findById(lot.getFarmId()).orElse(null);
                    String farmName = farm != null ? farm.getName() : "";
                    String producerOrg = "";
                    if (farm != null) {
                        var producer = producerRepository.findById(farm.getProducerId()).orElse(null);
                        producerOrg = producer != null && producer.getOrganization() != null ? producer.getOrganization() : "";
                    }
                    return new ProductCatalogDTO(
                            lot.getId().toString(), lot.getName(), lot.getCrop(),
                            lot.getVariety(), lot.getAreaHectares(),
                            lot.getCurrentStatus(), farmName, producerOrg);
                }).toList();
    }
}
