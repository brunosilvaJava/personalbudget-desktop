package com.bts.personalbudgetdesktop.service;

import com.bts.personalbudgetdesktop.exception.ValidationException;
import com.bts.personalbudgetdesktop.mapper.FinancialMovementMapper;
import com.bts.personalbudgetdesktop.model.FinancialMovement;
import com.bts.personalbudgetdesktop.model.FinancialMovementDTO;
import com.bts.personalbudgetdesktop.model.FinancialMovementStatus;
import com.bts.personalbudgetdesktop.model.OperationType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class FinancialMovementService {

    private final Set<FinancialMovement> financialMovementList;
    private final FinancialMovementMapper financialMovementMapper;

    public FinancialMovementService() {
        financialMovementList = new HashSet<>();

        financialMovementList.add(new FinancialMovement(UUID.randomUUID(), OperationType.CREDIT, "Pagamento Parcela",
                BigDecimal.valueOf(1000), BigDecimal.valueOf(500), LocalDateTime.now(), LocalDateTime.now().plusDays(30),
                LocalDateTime.now().plusDays(15), FinancialMovementStatus.PENDING, true));
        financialMovementList.add(new FinancialMovement(UUID.randomUUID(), OperationType.DEBIT, "Empréstimo",
                BigDecimal.valueOf(20000), BigDecimal.valueOf(20000), LocalDateTime.now(), LocalDateTime.now().plusDays(60),
                LocalDateTime.now().plusDays(60), FinancialMovementStatus.PAID_OUT, false));
        financialMovementMapper = FinancialMovementMapper.INSTANCE;
    }

    public void save(final FinancialMovementDTO financialMovementDTO) {
        FinancialMovement financialMovement = financialMovementMapper.dtoToModel(financialMovementDTO);
        financialMovementList.remove(financialMovement);
        financialMovementList.add(financialMovement);
    }

    public void delete(final UUID financialMovementCode) {
        financialMovementList.removeIf(financialMovement -> financialMovement.code().equals(financialMovementCode));
    }

    public Optional<FinancialMovementDTO> findByCode(final UUID code) {
        return financialMovementList.stream()
                .filter(financialMovement -> financialMovement.code().equals(code))
                .map(financialMovementMapper::modelToDto)
                .findFirst();
    }

    public Set<FinancialMovementDTO> findAll() {
        return financialMovementMapper.modelListToDtoList(financialMovementList);
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
        if (financialMovementDTO.flagActive() == null) {
            errors.put("flagActive", "O status de atividade (ativo/inativo) é obrigatório.");
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