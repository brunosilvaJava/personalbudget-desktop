package com.bts.personalbudgetdesktop.mapper;

import com.bts.personalbudgetdesktop.model.FixedBill;
import com.bts.personalbudgetdesktop.model.FixedBillDTO;
import com.bts.personalbudgetdesktop.model.recurrence.MonthlyRecurrence;
import com.bts.personalbudgetdesktop.model.recurrence.Recurrence;
import com.bts.personalbudgetdesktop.model.recurrence.RecurrenceType;
import com.bts.personalbudgetdesktop.model.recurrence.WeeklyRecurrence;
import com.bts.personalbudgetdesktop.model.recurrence.YearlyRecurrence;
import java.time.DayOfWeek;
import java.time.MonthDay;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FixedBillMapper {

    FixedBillMapper INSTANCE = Mappers.getMapper(FixedBillMapper.class);

    @Mapping(target = "code", source = "code", qualifiedByName = "mapCode")
    @Mapping(target="amount", expression = "java(new java.math.BigDecimal(dto.amount()))")
    @Mapping(target = "recurrence", expression = "java(mapRecurrence(dto.recurrenceType(), dto.days()))")
    FixedBill dtoToModel(FixedBillDTO dto);

    @Mapping(target = "recurrenceType", expression = "java(fixedBill.recurrence().findType())")
    @Mapping(target = "days", expression = "java(mapRecurrenceDays(fixedBill.recurrence()))")
    FixedBillDTO modelToDto(FixedBill fixedBill);

    Set<FixedBillDTO> modelListToDtoList(Set<FixedBill> fixedBillList);

    @Named("mapCode")
    default UUID mapCode(String code) {
        return (code == null || code.isEmpty()) ? UUID.randomUUID() : UUID.fromString(code);
    }

    @Named("mapRecurrenceDays")
    default Set<String> mapRecurrenceDays(final Recurrence<?> recurrence) {
        if (recurrence == null) {
            return null;
        }
        return switch (recurrence.findType()) {
            case WEEKLY -> ((WeeklyRecurrence) recurrence).findRecurrence().stream()
                    .map(DayOfWeek::name)
                    .collect(Collectors.toSet());
            case MONTHLY -> Set.of(String.valueOf(((MonthlyRecurrence) recurrence).findRecurrence()));
            case YEARLY -> {
                final MonthDay monthDay = ((YearlyRecurrence) recurrence).findRecurrence();
                yield Set.of(monthDay.getDayOfMonth() + "/" + monthDay.getMonthValue());
            }
        };
    }

    @Named("mapRecurrence")
    default Recurrence<?> mapRecurrence(final RecurrenceType recurrenceType,
                                        final Set<String> daysStr) {
        if (daysStr == null || daysStr.isEmpty()) {
            return null;
        }
        return switch (recurrenceType) {
            case WEEKLY -> new WeeklyRecurrence(daysStr.stream()
                    .map(String::toUpperCase)
                    .map(DayOfWeek::valueOf)
                    .toList());
            case MONTHLY -> new MonthlyRecurrence(Integer.valueOf(daysStr.stream().findFirst().orElseThrow()));
            case YEARLY -> {
                String dateStr = daysStr.stream().findFirst().orElseThrow();
                String[] parts = dateStr.split("/");
                if (parts.length != 2) {
                    yield null;
                } else {
                    int day = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    MonthDay monthDay = MonthDay.of(month, day);
                    yield new YearlyRecurrence(monthDay);
                }
            }
        };
    }

}
