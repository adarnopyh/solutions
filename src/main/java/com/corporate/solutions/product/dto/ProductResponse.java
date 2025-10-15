package com.corporate.solutions.product.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ProductResponse {
    Long id;
    String title;
    Integer quantity;
    BigDecimal price;
    BigDecimal totalWithVat; // computed in backend
}
