package com.corporate.solutions.pricing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PriceServiceTest {

    private static PriceService svc;

    @BeforeAll
    static void setup() {
        svc = new PriceService();
        // Inject VAT = 21% for the unit test
        ReflectionTestUtils.setField(svc, "vat", new BigDecimal("0.21"));
    }

    @Test
    void totalWithVat_basic() {
        // (2 * 10.00) * 1.21 = 24.20
        assertThat(svc.totalWithVat(2, new BigDecimal("10.00")))
                .isEqualByComparingTo("24.20");
    }

    @Test
    void totalWithVat_rounding() {
        // (3 * 1.115) * 1.21 = 4.047? -> 4.05 after rounding
        assertThat(svc.totalWithVat(3, new BigDecimal("1.115")))
                .isEqualByComparingTo("4.05");
    }

    @Test
    void totalWithVat_invalid() {
        assertThrows(IllegalArgumentException.class,
                () -> svc.totalWithVat(-1, new BigDecimal("1.00")));
    }
}
