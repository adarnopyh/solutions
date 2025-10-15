package com.corporate.solutions.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface ProductAuditRepository extends JpaRepository<ProductAudit, Long> {

    Page<ProductAudit> findAllByPerformedAtBetween(Instant from, Instant to, Pageable pageable);
}
