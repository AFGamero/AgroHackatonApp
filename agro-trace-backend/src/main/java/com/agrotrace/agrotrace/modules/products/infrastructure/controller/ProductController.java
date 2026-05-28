package com.agrotrace.agrotrace.modules.products.infrastructure.controller;

import com.agrotrace.agrotrace.modules.products.application.dto.ProductCatalogDTO;
import com.agrotrace.agrotrace.modules.products.application.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Catalogo publico de productos")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Catalogo publico de productos agricolas")
    public ResponseEntity<List<ProductCatalogDTO>> getCatalog(@RequestParam(required = false) String search) {
        return ResponseEntity.ok(productService.getPublicCatalog(search));
    }
}
