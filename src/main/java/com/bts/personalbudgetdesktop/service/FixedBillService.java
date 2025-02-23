package com.bts.personalbudgetdesktop.service;

import com.bts.personalbudgetdesktop.exception.ValidationException;
import com.bts.personalbudgetdesktop.mapper.FixedBillMapper;
import com.bts.personalbudgetdesktop.model.FixedBill;
import com.bts.personalbudgetdesktop.model.FixedBillDTO;
import com.bts.personalbudgetdesktop.model.OperationType;
import com.bts.personalbudgetdesktop.model.recurrence.MonthlyRecurrence;
import com.bts.personalbudgetdesktop.model.recurrence.YearlyRecurrence;
import java.math.BigDecimal;
import java.time.MonthDay;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class FixedBillService {

    private final Set<FixedBill> fixedBillList;
    private final FixedBillMapper fixedBillMapper;

    public FixedBillService() {
        fixedBillList = new HashSet<>();
        fixedBillList.add(new FixedBill(OperationType.CREDIT, "Salário", BigDecimal.valueOf(10000), new MonthlyRecurrence(1)));
        fixedBillList.add(new FixedBill(OperationType.DEBIT, "Presente", BigDecimal.valueOf(850), new YearlyRecurrence(MonthDay.of(12, 24))));
        fixedBillMapper = FixedBillMapper.INSTANCE;
    }

    public void save(final FixedBillDTO fixedBillDTO) {
        validateFields(fixedBillDTO);

        FixedBill fixedBill = fixedBillMapper.dtoToModel(fixedBillDTO);

        fixedBillList.remove(fixedBill);
        fixedBillList.add(fixedBill);
    }

    public void delete(final UUID fixedBillCode) {
        fixedBillList.removeIf(fixedBill -> fixedBill.code().equals(fixedBillCode));
    }

    public Optional<FixedBillDTO> findByCode(final UUID code) {
        return fixedBillList.stream()
                .filter(fixedBillDTO -> fixedBillDTO.code().equals(code))
                .map(fixedBillMapper::modelToDto)
                .findFirst();
    }

    public Set<FixedBillDTO> findAll() {
        return fixedBillMapper.modelListToDtoList(fixedBillList);
    }

    public void validateFields(FixedBillDTO fixedBillDTO) {
        Map<String, String> errors = new HashMap<>();

        if (fixedBillDTO.operationType() == null) {
            errors.put("operationType", "O tipo de operação é obrigatório.");
        }

        if (fixedBillDTO.description() == null || fixedBillDTO.description().trim().isEmpty()) {
            errors.put("description", "A descrição é obrigatória.");
        }
        if (fixedBillDTO.amount() == null || fixedBillDTO.amount().trim().isEmpty()) {
            errors.put("amount", "O valor é obrigatório.");
        }
        if (fixedBillDTO.recurrenceType() == null) {
            errors.put("recurrenceType", "O tipo de recorrência é obrigatório.");
        }
        if (fixedBillDTO.days() == null || fixedBillDTO.days().isEmpty()) {
            errors.put("days", "Os dias são obrigatórios.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

}
