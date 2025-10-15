package com.corporate.solutions.audit;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "product_audit", indexes = {
        @Index(name = "idx_product_audit_product_id", columnList = "product_id"),
        @Index(name = "idx_product_audit_performed_at", columnList = "performed_at")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ProductAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "action", nullable = false, length = 16)
    private String action; // CREATE, UPDATE, DELETE

    @Column(name = "old_value", columnDefinition = "text")
    private String oldValueJson;

    @Column(name = "new_value", columnDefinition = "text")
    private String newValueJson;

    @Column(name = "performed_by", nullable = false, length = 128)
    private String performedBy;

    @Column(name = "performed_at", nullable = false)
    private Instant performedAt;
}