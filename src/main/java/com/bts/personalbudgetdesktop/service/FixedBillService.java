package com.bts.personalbudgetdesktop.service;

import com.bts.personalbudgetdesktop.model.FixedBill;
import com.bts.personalbudgetdesktop.model.OperationType;
import com.bts.personalbudgetdesktop.model.RecurrenceType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FixedBillService {

    protected final List<FixedBill> fixedBillList;

    public FixedBillService() {
        fixedBillList = new ArrayList<>();
        fixedBillList.add(new FixedBill(OperationType.CREDIT, "Salary", BigDecimal.valueOf(10000), RecurrenceType.MONTHLY, Set.of(1)));
        fixedBillList.add(new FixedBill(OperationType.DEBIT, "Rent", BigDecimal.valueOf(850), RecurrenceType.MONTHLY, Set.of(5)));
    }

    public void save(final FixedBill fixedBill) {
        System.out.println("Saving fixed bill..." + fixedBill.toString());
        fixedBillList.add(fixedBill);
    }

    public List<FixedBill> findAllFixedBills() {
        return fixedBillList;
    }
}
