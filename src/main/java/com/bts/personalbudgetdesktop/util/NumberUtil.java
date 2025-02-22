package com.bts.personalbudgetdesktop.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import static com.bts.personalbudgetdesktop.util.StringUtils.IN_LOCALE;

public class NumberUtil {

    public static String format(final BigDecimal number) {
        final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(IN_LOCALE);
        return numberFormat.format(number);
    }
}
