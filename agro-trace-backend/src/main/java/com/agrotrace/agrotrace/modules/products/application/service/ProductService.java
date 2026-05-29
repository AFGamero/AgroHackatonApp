package com.agrotrace.agrotrace.modules.products.application.service;

import com.agrotrace.agrotrace.modules.farms.domain.repository.FarmRepository;
import com.agrotrace.agrotrace.modules.lots.domain.repository.LotRepository;
import com.agrotrace.agrotrace.modules.passports.domain.repository.DigitalPassportRepository;
import com.agrotrace.agrotrace.modules.producers.domain.repository.ProducerRepository;
import com.agrotrace.agrotrace.modules.products.application.dto.*;
import com.agrotrace.agrotrace.modules.products.domain.model.Product;
import com.agrotrace.agrotrace.modules.products.domain.model.ProductPhoto;
import com.agrotrace.agrotrace.modules.products.domain.repository.ProductPhotoRepository;
import com.agrotrace.agrotrace.modules.products.domain.repository.ProductRepository;
import com.agrotrace.agrotrace.shared.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductPhotoRepository photoRepository;
    private final LotRepository lotRepository;
    private final FarmRepository farmRepository;
    private final ProducerRepository producerRepository;
    private final DigitalPassportRepository passportRepository;

    @Transactional
    public ProductResponseDTO createProduct(UUID userId, CreateProductDTO dto) {
        if (!lotRepository.existsById(dto.lotId())) {
            throw new BusinessException("LOT_NOT_FOUND", "Lote no encontrado", 404);
        }

        Product product = new Product();
        product.setLotId(dto.lotId());
        product.setPublicId("prd_" + UUID.randomUUID().toString().substring(0, 10));
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setCrop(dto.crop());
        product.setVariety(dto.variety());
        product.setQuantityAvailable(dto.quantityAvailable());
        product.setUnit(dto.unit());
        product.setReferencePrice(dto.referencePrice());
        product.setCurrency(dto.currency() != null ? dto.currency() : "COP");
        product.setAvailabilityDate(dto.availabilityDate());
        product.setCreatedBy(userId);

        return toResponseDTO(productRepository.save(product));
    }

    public ProductResponseDTO getProduct(UUID id) {
        return productRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new BusinessException("PRODUCT_NOT_FOUND", "Producto no encontrado", 404));
    }

    public List<ProductResponseDTO> getMyProducts(UUID userId) {
        return productRepository.findByCreatedBy(userId).stream()
                .map(this::toResponseDTO).toList();
    }

    @Transactional
    public ProductResponseDTO publishProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("PRODUCT_NOT_FOUND", "Producto no encontrado", 404));
        product.setStatus("PUBLICADO");
        product.setPublishedAt(LocalDateTime.now());
        return toResponseDTO(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDTO unpublishProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("PRODUCT_NOT_FOUND", "Producto no encontrado", 404));
        product.setStatus("BORRADOR");
        product.setPublishedAt(null);
        return toResponseDTO(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDTO markSoldOut(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("PRODUCT_NOT_FOUND", "Producto no encontrado", 404));
        product.setStatus("AGOTADO");
        return toResponseDTO(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDTO addPhoto(UUID productId, String url, String description, Boolean isCover) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("PRODUCT_NOT_FOUND", "Producto no encontrado", 404));

        if (Boolean.TRUE.equals(isCover)) {
            photoRepository.findByProductId(productId).stream()
                    .filter(ProductPhoto::getIsCover)
                    .forEach(p -> { p.setIsCover(false); photoRepository.save(p); });
        }

        ProductPhoto photo = ProductPhoto.builder()
                .product(product)
                .url(url)
                .description(description)
                .isCover(isCover != null && isCover)
                .sortOrder(photoRepository.findByProductId(productId).size())
                .build();
        photoRepository.save(photo);

        return toResponseDTO(product);
    }

    @Transactional
    public void removePhoto(UUID photoId) {
        if (!photoRepository.existsById(photoId)) {
            throw new BusinessException("PHOTO_NOT_FOUND", "Foto no encontrada", 404);
        }
        photoRepository.deleteById(photoId);
    }

    public Page<PublicProductDTO> getPublicCatalog(int page, int size, String search) {
        var pageable = PageRequest.of(page, size);
        Page<Product> products;

        if (search != null && !search.isBlank()) {
            products = productRepository.findPublishedWithSearch(search, pageable);
        } else {
            products = productRepository.findAllPublished(pageable);
        }

        return products.map(this::toPublicDTO);
    }

    public PublicProductDTO getPublicProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("PRODUCT_NOT_FOUND", "Producto no encontrado", 404));
        return toPublicDTO(product);
    }

    private ProductResponseDTO toResponseDTO(Product p) {
        var photos = photoRepository.findByProductId(p.getId()).stream()
                .map(ph -> new ProductPhotoDTO(ph.getId(), ph.getUrl(), ph.getDescription(), ph.getIsCover(), ph.getSortOrder()))
                .toList();
        return new ProductResponseDTO(p.getId(), p.getLotId(), p.getPublicId(), p.getName(),
                p.getDescription(), p.getCrop(), p.getVariety(), p.getQuantityAvailable(),
                p.getUnit(), p.getReferencePrice(), p.getCurrency(), p.getAvailabilityDate(),
                p.getStatus(), p.getPublishedAt(), p.getCreatedBy(), p.getCreatedAt(), photos);
    }

    private PublicProductDTO toPublicDTO(Product p) {
        var lot = lotRepository.findById(p.getLotId()).orElse(null);
        var farm = lot != null ? farmRepository.findById(lot.getFarmId()).orElse(null) : null;
        var passport = passportRepository.findByLotId(p.getLotId()).orElse(null);

        String coverImage = photoRepository.findByProductId(p.getId()).stream()
                .filter(ph -> Boolean.TRUE.equals(ph.getIsCover()))
                .findFirst()
                .map(ProductPhoto::getUrl)
                .orElse(null);

        List<String> images = photoRepository.findByProductId(p.getId()).stream()
                .map(ProductPhoto::getUrl).toList();

        return new PublicProductDTO(
                p.getPublicId(), p.getName(), p.getDescription(), p.getCrop(), p.getVariety(),
                p.getQuantityAvailable(), p.getUnit(), p.getReferencePrice(), p.getCurrency(),
                lot != null ? lot.getCurrentStatus() : null,
                coverImage, images,
                farm != null ? farm.getName() : "",
                farm != null && farm.getLocation() != null ? farm.getLocation() : "",
                passport != null ? passport.getPublicUrl() : null
        );
    }
}
