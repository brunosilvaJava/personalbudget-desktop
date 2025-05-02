package com.bts.personalbudgetdesktop.service;

import com.bts.personalbudgetdesktop.client.personalbudgetapi.PersonalBudgetApiClient;
import com.bts.personalbudgetdesktop.exception.ValidationException;
import com.bts.personalbudgetdesktop.mapper.FixedBillMapper;
import com.bts.personalbudgetdesktop.mapper.InstallmentBillMapper;
import com.bts.personalbudgetdesktop.model.FixedBill;
import com.bts.personalbudgetdesktop.model.FixedBillDTO;
import com.bts.personalbudgetdesktop.model.InstallmentBill;
import com.bts.personalbudgetdesktop.model.InstallmentBillDTO;
import com.bts.personalbudgetdesktop.model.InstallmentBillStatus;
import com.bts.personalbudgetdesktop.model.OperationType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class InstallmentBillService {

    private final PersonalBudgetApiClient personalBudgetApiClient;
    private final InstallmentBillMapper installmentBillMapper;

    public InstallmentBillService() {
        personalBudgetApiClient = new PersonalBudgetApiClient();
        installmentBillMapper = InstallmentBillMapper.INSTANCE;
    }

    public void save(final InstallmentBillDTO installmentBillDTO) {
        InstallmentBill installmentBill = installmentBillMapper.dtoToModel(installmentBillDTO);
        personalBudgetApiClient.saveInstallmentBill(installmentBill);
    }

    public void delete(final UUID installmentBillCode) {
        personalBudgetApiClient.deleteInstallmentBill(installmentBillCode);
    }

    public InstallmentBillDTO findByCode(final UUID code) {
        InstallmentBill installmentBill = personalBudgetApiClient.findInstallmentBill(code).orElseThrow();
        return installmentBillMapper.modelToDto(installmentBill);
    }

    public List<InstallmentBillDTO> findAll() {
        return installmentBillMapper.modelListToDtoList(personalBudgetApiClient.findInstallmentBills());
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
