package com.bts.personalbudgetdesktop.service;

import com.bts.personalbudgetdesktop.exception.ValidationException;
import com.bts.personalbudgetdesktop.mapper.InstallmentBillMapper;
import com.bts.personalbudgetdesktop.model.InstallmentBill;
import com.bts.personalbudgetdesktop.model.InstallmentBillDTO;
import com.bts.personalbudgetdesktop.model.InstallmentBillStatus;
import com.bts.personalbudgetdesktop.model.OperationType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class InstallmentBillService {

    private final Set<InstallmentBill> installmentBillList;
    private final InstallmentBillMapper installmentBillMapper;

    public InstallmentBillService() {
        installmentBillList = new HashSet<>();
        installmentBillList.add(new InstallmentBill(UUID.randomUUID(), OperationType.CREDIT, "Compra Parcelada",
                BigDecimal.valueOf(2000), InstallmentBillStatus.PENDING, LocalDate.now(), 10, true));
        installmentBillList.add(new InstallmentBill(UUID.randomUUID(), OperationType.DEBIT, "Financiamento",
                BigDecimal.valueOf(50000), InstallmentBillStatus.DONE , LocalDate.now(), 60, false));
        installmentBillMapper = InstallmentBillMapper.INSTANCE;
    }

    public void save(final InstallmentBillDTO installmentBillDTO) {
        InstallmentBill installmentBill = installmentBillMapper.dtoToModel(installmentBillDTO);
        installmentBillList.remove(installmentBill);
        installmentBillList.add(installmentBill);
    }

    public void delete(final UUID installmentBillCode) {
        installmentBillList.removeIf(installmentBill -> installmentBill.code().equals(installmentBillCode));
    }

    public Optional<InstallmentBillDTO> findByCode(final UUID code) {
        return installmentBillList.stream()
                .filter(installmentBill -> installmentBill.code().equals(code))
                .map(installmentBillMapper::modelToDto)
                .findFirst();
    }

    public Set<InstallmentBillDTO> findAll() {
        return installmentBillMapper.modelListToDtoList(installmentBillList);
    }

    public void validateFields(InstallmentBillDTO installmentBillDTO) {
        Map<String, String> errors = new HashMap<>();

        if (installmentBillDTO.operationType() == null) {
            errors.put("operationType", "O tipo de operação é obrigatório.");
        }
        if (installmentBillDTO.description() == null || installmentBillDTO.description().trim().isEmpty()) {
            errors.put("description", "A descrição é obrigatória.");
        }
        if (installmentBillDTO.amount() == null || installmentBillDTO.amount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.put("amount", "O valor deve ser maior que zero.");
        }
        if (installmentBillDTO.status() == null) {
            errors.put("status", "O status é obrigatório.");
        }
        if (installmentBillDTO.purchaseDate() == null) {
            errors.put("purchaseDate", "A data de compra é obrigatória.");
        }
        if (installmentBillDTO.installmentCount() == null || installmentBillDTO.installmentCount() <= 0) {
            errors.put("installmentCount", "O número de parcelas deve ser maior que zero.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
