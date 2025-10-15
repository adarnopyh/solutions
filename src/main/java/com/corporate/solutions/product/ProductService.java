package com.corporate.solutions.product;

import com.corporate.solutions.audit.AuditService;
import com.corporate.solutions.product.dto.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;
    private final AuditService auditService;

    public List<Product> findAll() {
        return repo.findAll();
    }

    public Product findByIdOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    }

    @Transactional
    public Product create(Product entity) {
        Product saved = repo.save(entity);
        auditService.recordCreate(saved);
        return saved;
    }

    @Transactional
    public Product update(Long id, ProductRequest req, com.corporate.solutions.product.ProductMapper mapper) {
        Product p = findByIdOrThrow(id);

        Product before = Product.builder()
                .id(p.getId())
                .title(p.getTitle())
                .quantity(p.getQuantity())
                .price(p.getPrice())
                .build();

        mapper.updateEntity(p, req);
        Product saved = repo.save(p);
        auditService.recordUpdate(before, saved);
        return saved;
    }

    @Transactional
    public void delete(Long id) {
        Product p = findByIdOrThrow(id);
        repo.deleteById(id);
        auditService.recordDelete(p);
    }
}