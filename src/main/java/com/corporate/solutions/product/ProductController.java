package com.corporate.solutions.product;

import com.corporate.solutions.product.dto.ProductRequest;
import com.corporate.solutions.product.dto.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Products")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;
    private final com.corporate.solutions.product.ProductMapper mapper;

    @Operation(summary = "List products")
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<ProductResponse> list() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    @Operation(summary = "Get product by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ProductResponse get(@PathVariable Long id) {
        return mapper.toResponse(service.findByIdOrThrow(id));
    }

    @Operation(summary = "Create product (ADMIN)")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse create(@Valid @RequestBody ProductRequest req) {
        Product saved = service.create(mapper.toEntity(req));
        return mapper.toResponse(saved);
    }

    @Operation(summary = "Update product (ADMIN)")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest req) {
        Product saved = service.update(id, req, mapper);
        return mapper.toResponse(saved);
    }

    @Operation(summary = "Delete product (ADMIN)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}