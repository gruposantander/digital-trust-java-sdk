package com.santander.digital.verifiedid.util;

import com.santander.digital.verifiedid.exceptions.DigitalIdConfigurationException;

import java.util.Calendar;
import java.util.Date;

public class CommonUtils {
    public static String isoDate (final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final Integer year = calendar.get(Calendar.YEAR);
        final Integer month = calendar.get(Calendar.MONTH) + 1;
        final Integer day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null)
            throw new DigitalIdConfigurationException(message);
        return obj;
    }
}
