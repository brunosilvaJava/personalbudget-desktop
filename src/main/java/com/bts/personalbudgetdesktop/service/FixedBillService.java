package com.bts.personalbudgetdesktop.service;

import com.bts.personalbudgetdesktop.client.personalbudgetapi.PersonalBudgetApiClient;
import com.bts.personalbudgetdesktop.exception.ValidationException;
import com.bts.personalbudgetdesktop.mapper.FixedBillMapper;
import com.bts.personalbudgetdesktop.model.FixedBill;
import com.bts.personalbudgetdesktop.model.FixedBillDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FixedBillService {

    private final PersonalBudgetApiClient personalBudgetApiClient;
    private final FixedBillMapper fixedBillMapper;

    public FixedBillService() {
        personalBudgetApiClient = new PersonalBudgetApiClient();
        fixedBillMapper = FixedBillMapper.INSTANCE;
    }

    public void save(final FixedBillDTO fixedBillDTO) {
        FixedBill fixedBill = fixedBillMapper.dtoToModel(fixedBillDTO);
        personalBudgetApiClient.saveFixedBill(fixedBill);
    }

    public void delete(final UUID fixedBillCode) {
        personalBudgetApiClient.delete(fixedBillCode);
    }

    public FixedBillDTO findByCode(final UUID code) {
        FixedBill fixedBill = personalBudgetApiClient.find(code).orElseThrow();
        return fixedBillMapper.modelToDto(fixedBill);
    }

    public List<FixedBillDTO> findAll() {
        return fixedBillMapper.modelListToDtoList(personalBudgetApiClient.findFixedBills());
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
