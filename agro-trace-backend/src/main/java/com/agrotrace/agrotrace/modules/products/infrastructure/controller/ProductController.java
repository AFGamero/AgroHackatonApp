package com.agrotrace.agrotrace.modules.products.infrastructure.controller;

import com.agrotrace.agrotrace.modules.products.application.dto.CreateProductDTO;
import com.agrotrace.agrotrace.modules.products.application.dto.ProductResponseDTO;
import com.agrotrace.agrotrace.modules.products.application.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Productos comercializables")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Crear producto")
    public ResponseEntity<ProductResponseDTO> create(@RequestParam UUID userId, @Valid @RequestBody CreateProductDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(userId, dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping("/my")
    @Operation(summary = "Listar mis productos")
    public ResponseEntity<List<ProductResponseDTO>> getMyProducts(@RequestParam UUID userId) {
        return ResponseEntity.ok(productService.getMyProducts(userId));
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "Publicar producto")
    public ResponseEntity<ProductResponseDTO> publish(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.publishProduct(id));
    }

    @PostMapping("/{id}/unpublish")
    @Operation(summary = "Despublicar producto")
    public ResponseEntity<ProductResponseDTO> unpublish(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.unpublishProduct(id));
    }

    @PostMapping("/{id}/mark-sold-out")
    @Operation(summary = "Marcar como agotado")
    public ResponseEntity<ProductResponseDTO> markSoldOut(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.markSoldOut(id));
    }

    @PostMapping("/{id}/photos")
    @Operation(summary = "Agregar foto al producto")
    public ResponseEntity<ProductResponseDTO> addPhoto(
            @PathVariable UUID id, @RequestParam String url,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "false") Boolean isCover) {
        return ResponseEntity.ok(productService.addPhoto(id, url, description, isCover));
    }

    @DeleteMapping("/{id}/photos/{photoId}")
    @Operation(summary = "Eliminar foto del producto")
    public ResponseEntity<Void> removePhoto(@PathVariable UUID photoId) {
        productService.removePhoto(photoId);
        return ResponseEntity.noContent().build();
    }
}
