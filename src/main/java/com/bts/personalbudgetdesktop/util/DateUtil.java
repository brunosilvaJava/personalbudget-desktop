package com.bts.personalbudgetdesktop.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Optional;
import static com.bts.personalbudgetdesktop.util.StringUtils.IN_LOCALE;

public class DateUtil {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final String SHORT_DATE_FORMATTER = "dd/MM";

    public static Optional<String> format(final LocalDate date) {
        if (date == null) {
            return Optional.empty();
        }
        return Optional.of(DATE_FORMATTER.format(date));
    }

    public static String buildWeeklyDayName(final Integer day) {
        return DayOfWeek.of(day).minus(1).getDisplayName(TextStyle.SHORT_STANDALONE, IN_LOCALE);
    }

    public static String buildDateFormat(final LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(SHORT_DATE_FORMATTER));
    }

}
