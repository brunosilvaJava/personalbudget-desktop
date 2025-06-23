package com.bts.personalbudgetdesktop.service;

import com.bts.personalbudgetdesktop.client.personalbudgetapi.PersonalBudgetApiClient;
import com.bts.personalbudgetdesktop.exception.ValidationException;
import com.bts.personalbudgetdesktop.mapper.FinancialMovementMapper;
import com.bts.personalbudgetdesktop.mapper.FixedBillMapper;
import com.bts.personalbudgetdesktop.model.FinancialMovement;
import com.bts.personalbudgetdesktop.model.FinancialMovementDTO;
import com.bts.personalbudgetdesktop.model.FinancialMovementStatus;
import com.bts.personalbudgetdesktop.model.FixedBill;
import com.bts.personalbudgetdesktop.model.FixedBillDTO;
import com.bts.personalbudgetdesktop.model.OperationType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class FinancialMovementService {

    private final PersonalBudgetApiClient personalBudgetApiClient;
    private final FinancialMovementMapper financialMovementMapper;

    public FinancialMovementService() {
        personalBudgetApiClient = new PersonalBudgetApiClient();
        financialMovementMapper = FinancialMovementMapper.INSTANCE;
    }

    public void save(final FinancialMovementDTO financialMovementDTO) {
        FinancialMovement financialMovement = financialMovementMapper.dtoToModel(financialMovementDTO);
        personalBudgetApiClient.saveFinancialMovement(financialMovement);
    }

    public void delete(final UUID financialMovementCode) {
        personalBudgetApiClient.deleteFinancialMovement(financialMovementCode);
    }

    public FinancialMovementDTO findByCode(final UUID code) {
        FinancialMovement financialMovement = personalBudgetApiClient.findFinancialMovement(code).orElseThrow();
        return financialMovementMapper.modelToDto(financialMovement);
    }

    public List<FinancialMovementDTO> findAll(LocalDate startDate, LocalDate endDate, List<String> status,
                                              String operationType, String description) {
        return financialMovementMapper.modelListToDtoList(
            personalBudgetApiClient.findFinancialMovements(startDate, endDate, status, operationType, description)
        );
    }

    public void validateFields(FinancialMovementDTO financialMovementDTO) {
        Map<String, String> errors = new HashMap<>();

        if (financialMovementDTO.operationType() == null) {
            errors.put("operationType", "O tipo de operação é obrigatório.");
        }
        if (financialMovementDTO.description() == null || financialMovementDTO.description().trim().isEmpty()) {
            errors.put("description", "A descrição é obrigatória.");
        }
        if (financialMovementDTO.amount() == null || financialMovementDTO.amount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.put("amount", "O valor deve ser maior que zero.");
        }
        if (financialMovementDTO.amountPaid() == null || financialMovementDTO.amountPaid().compareTo(BigDecimal.ZERO) < 0) {
            errors.put("amountPaid", "O valor pago não pode ser negativo.");
        }
        if (financialMovementDTO.status() == null) {
            errors.put("status", "O status é obrigatório.");
        }
        if (financialMovementDTO.movementDate() == null) {
            errors.put("movementDate", "A data do movimento é obrigatória.");
        }
        if (financialMovementDTO.dueDate() == null) {
            errors.put("dueDate", "A data de vencimento é obrigatória.");
        }
        if (financialMovementDTO.payDate() == null && financialMovementDTO.status() == FinancialMovementStatus.PAID_OUT) {
            errors.put("payDate", "A data de pagamento é obrigatória quando o status é Pago.");
        }
        if (financialMovementDTO.movementDate() != null && financialMovementDTO.dueDate() != null &&
                financialMovementDTO.movementDate().getDayOfYear() > financialMovementDTO.dueDate().getDayOfYear()) {
            errors.put("movementDate", "A data de movimentação não pode ser maior que a data de vencimento");
        }
        if (financialMovementDTO.movementDate() != null && financialMovementDTO.payDate() != null &&
                financialMovementDTO.movementDate().getDayOfYear() > financialMovementDTO.payDate().getDayOfYear()) {
            errors.put("movementDate", "A data de movimentação não pode ser maior que a data de pagamento");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}