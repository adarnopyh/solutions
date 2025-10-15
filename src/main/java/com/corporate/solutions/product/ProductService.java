package com.corporate.solutions.product;

import com.corporate.solutions.product.dto.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository repo;

    public List<Product> findAll() {
        return repo.findAll();
    }

    public Product findByIdOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    }

    public Product create(Product entity) {
        return repo.save(entity);
    }

    public Product update(Long id, ProductRequest req, com.corporate.solutions.product.ProductMapper mapper) {
        Product p = findByIdOrThrow(id);
        mapper.updateEntity(p, req);
        return repo.save(p);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("Product not found: " + id);
        repo.deleteById(id);
    }
}