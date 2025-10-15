package com.corporate.solutions.product;

import com.corporate.solutions.pricing.PriceService;
import com.corporate.solutions.product.dto.ProductRequest;
import com.corporate.solutions.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final PriceService priceService;

    public Product toEntity(ProductRequest req) {
        return Product.builder()
                .title(req.getTitle())
                .quantity(req.getQuantity())
                .price(req.getPrice())
                .build();
    }

    public void updateEntity(Product entity, ProductRequest req) {
        entity.setTitle(req.getTitle());
        entity.setQuantity(req.getQuantity());
        entity.setPrice(req.getPrice());
    }

    public ProductResponse toResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .quantity(p.getQuantity())
                .price(p.getPrice())
                .totalWithVat(priceService.totalWithVat(p.getQuantity(), p.getPrice()))
                .build();
    }
}