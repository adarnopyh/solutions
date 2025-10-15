package com.corporate.solutions.audit;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/audit/products")
public class ProductAuditController {

    private final ProductAuditRepository repo;

    public ProductAuditController(ProductAuditRepository repo) {
        this.repo = repo;
    }

    @Operation(summary = "List product audit entries (ADMIN). Filter by from/to Instant. Supports pagination.")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<ProductAudit> list(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            Pageable pageable
    ) {
        Instant fromSafe = (from != null) ? from : Instant.EPOCH;
        Instant toSafe = (to != null) ? to : Instant.now();
        return repo.findAllByPerformedAtBetween(fromSafe, toSafe, pageable);
    }
}
