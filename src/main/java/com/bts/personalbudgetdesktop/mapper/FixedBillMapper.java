package com.bts.personalbudgetdesktop.mapper;

import com.bts.personalbudgetdesktop.client.personalbudgetapi.FixedBillRequest;
import com.bts.personalbudgetdesktop.client.personalbudgetapi.FixedBillResponse;
import com.bts.personalbudgetdesktop.model.FixedBill;
import com.bts.personalbudgetdesktop.model.FixedBillDTO;
import com.bts.personalbudgetdesktop.model.recurrence.MonthlyRecurrence;
import com.bts.personalbudgetdesktop.model.recurrence.Recurrence;
import com.bts.personalbudgetdesktop.model.recurrence.RecurrenceType;
import com.bts.personalbudgetdesktop.model.recurrence.WeeklyRecurrence;
import com.bts.personalbudgetdesktop.model.recurrence.YearlyRecurrence;
import java.time.DayOfWeek;
import java.time.MonthDay;
import java.time.Year;
import java.util.List;
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
    String ACTIVE_STATUS = "ACTIVE";

    @Mapping(target = "code", source = "code", qualifiedByName = "mapCode")
    @Mapping(target = "amount", expression = "java(new java.math.BigDecimal(dto.amount()))")
    @Mapping(target = "recurrence", expression = "java(mapRecurrence(dto.recurrenceType(), dto.days()))")
    FixedBill dtoToModel(FixedBillDTO dto);

    @Mapping(target = "recurrenceType", expression = "java(fixedBill.recurrence().findType())")
    @Mapping(target = "days", expression = "java(mapRecurrenceDays(fixedBill.recurrence()))")
    FixedBillDTO modelToDto(FixedBill fixedBill);

    List<FixedBillDTO> modelListToDtoList(List<FixedBill> fixedBillList);

    @Mapping(target = "recurrence", expression = "java(mapRecurrenceByInt(fixedBillResponse.recurrenceType(), fixedBillResponse.days()))")
    @Mapping(target = "active", expression = "java(mapStatus(fixedBillResponse.status()))")
    FixedBill toModel(FixedBillResponse fixedBillResponse);

    List<FixedBill> responseToModelList(List<FixedBillResponse> fixedBillResponseList);

    @Mapping(target = "flgLeapYear", expression = "java(true)") // TODO: implement logic
    @Mapping(target = "recurrenceType", expression = "java(fixedBill.recurrence().findType())")
    @Mapping(target = "days", expression = "java(mapRecurrenceIntDays(fixedBill.recurrence()))")
    FixedBillRequest dtoToRequest(FixedBill fixedBill);

    @Named("mapCode")
    default UUID mapCode(String code) {
        return (code == null || code.isEmpty()) ? UUID.randomUUID() : UUID.fromString(code);
    }

    @Named("mapStatus")
    default boolean mapStatus(String status) {
        return ACTIVE_STATUS.equals(status);
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

    @Named("mapRecurrenceDays")
    default Set<Integer> mapRecurrenceIntDays(final Recurrence<?> recurrence) {
        if (recurrence == null) {
            return null;
        }
        return switch (recurrence.findType()) {
            case WEEKLY -> ((WeeklyRecurrence) recurrence).findRecurrence().stream()
                    .map(DayOfWeek::getValue)
                    .collect(Collectors.toSet());
            case MONTHLY -> Set.of(((MonthlyRecurrence) recurrence).findRecurrence());
            case YEARLY -> {
                final MonthDay monthDay = ((YearlyRecurrence) recurrence).findRecurrence();
                yield Set.of(monthDay.atYear(Year.now().getValue()).getDayOfYear());
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

    @Named("mapRecurrenceByInt")
    default Recurrence<?> mapRecurrenceByInt(final RecurrenceType recurrenceType,
                                             final Set<Integer> days) {
        if (days == null || days.isEmpty()) {
            return null;
        }
        return switch (recurrenceType) {
            case WEEKLY -> new WeeklyRecurrence(days.stream()
                    .map(DayOfWeek::of)
                    .toList());
            case MONTHLY -> new MonthlyRecurrence(days.stream().findFirst().orElseThrow());
            case YEARLY -> {
                int dayOfYear = days.stream().findFirst().orElseThrow();
                Year year = Year.of(dayOfYear);
                yield new YearlyRecurrence(MonthDay.from(year.atDay(dayOfYear)));
            }
        };
    }

}
