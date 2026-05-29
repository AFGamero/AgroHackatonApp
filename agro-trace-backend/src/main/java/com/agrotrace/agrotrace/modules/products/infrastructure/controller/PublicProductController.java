package com.agrotrace.agrotrace.modules.products.infrastructure.controller;

import com.agrotrace.agrotrace.modules.products.application.dto.PublicProductDTO;
import com.agrotrace.agrotrace.modules.products.application.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/public/products")
@RequiredArgsConstructor
@Tag(name = "Public Products", description = "Catalogo publico de productos")
public class PublicProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Catalogo publico de productos")
    public ResponseEntity<Page<PublicProductDTO>> getCatalog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(productService.getPublicCatalog(page, size, search));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle publico de producto")
    public ResponseEntity<PublicProductDTO> getDetail(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getPublicProduct(id));
    }
}
