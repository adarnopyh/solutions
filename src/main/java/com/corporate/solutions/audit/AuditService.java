package com.corporate.solutions.audit;

import com.corporate.solutions.product.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final ProductAuditRepository repo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void recordCreate(Product newEntity) {
        save(newEntity.getId(), "CREATE", null, toJson(newEntity));
    }

    @Transactional
    public void recordUpdate(Product before, Product after) {
        save(after.getId(), "UPDATE", toJson(before), toJson(after));
    }

    @Transactional
    public void recordDelete(Product before) {
        save(before.getId(), "DELETE", toJson(before), null);
    }

    private void save(Long productId, String action, String oldJson, String newJson) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null && auth.isAuthenticated()) ? auth.getName() : "system";

        ProductAudit entry = ProductAudit.builder()
                .productId(productId)
                .action(action)
                .oldValueJson(oldJson)
                .newValueJson(newJson)
                .performedBy(username)
                .performedAt(Instant.now())
                .build();
        repo.save(entry);
    }

    private String toJson(Product p) {
        if (p == null) return null;
        try {
            // Create a lean snapshot to avoid lazy issues
            Product snapshot = Product.builder()
                    .id(p.getId())
                    .title(p.getTitle())
                    .quantity(p.getQuantity())
                    .price(p.getPrice())
                    .build();
            return objectMapper.writeValueAsString(snapshot);
        } catch (JsonProcessingException e) {
            return "{\"error\":\"json-serialize-failed\"}";
        }
    }
}
