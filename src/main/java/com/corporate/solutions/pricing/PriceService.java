package com.corporate.solutions.pricing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PriceService {

    // e.g. 0.21 for 21% VAT, configurable in application.yaml
    @Value("${app.vat:0.21}")
    private BigDecimal vat;

    /**
     * (quantity * price) * (1 + VAT), rounded to 2 decimals HALF_UP.
     */
    public BigDecimal totalWithVat(int quantity, BigDecimal price) {
        if (quantity < 0) throw new IllegalArgumentException("quantity must be >= 0");
        if (price == null || price.signum() < 0) throw new IllegalArgumentException("price must be >= 0");
        BigDecimal base = price.multiply(BigDecimal.valueOf(quantity));
        BigDecimal multiplier = BigDecimal.ONE.add(vat);
        return base.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }
}
