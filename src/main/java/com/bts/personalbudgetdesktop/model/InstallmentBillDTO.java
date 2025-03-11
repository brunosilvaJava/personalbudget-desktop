package com.bts.personalbudgetdesktop.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

    public record InstallmentBillDTO(
            UUID code,
            OperationType operationType,
            String description,
            BigDecimal amount,
            InstallmentBillStatus status,
            LocalDate purchaseDate,
            Integer installmentCount,
            Boolean active
    ) {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final InstallmentBillDTO installmentBillDTO = (InstallmentBillDTO) o;
            return Objects.equals(code, installmentBillDTO.code());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(code);
        }

    }


