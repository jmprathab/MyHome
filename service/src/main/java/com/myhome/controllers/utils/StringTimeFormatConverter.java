package com.myhome.controllers.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public abstract class StringTimeFormatConverter {
    private static final String DATE_TIME_PATTERN ="dd-MM-yyyy[['T']HH[:mm][:ss]]";

    public static TemporalAccessor getTemporalAccessor(String text) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        return formatter.parseBest(text, LocalDateTime::from, LocalDate::from);
    }

    public static LocalDateTime getLocalDateTime(TemporalAccessor temporalAccessor) {
        LocalDateTime dateTime;
        if ((temporalAccessor instanceof LocalDateTime)) {
            dateTime = (LocalDateTime) temporalAccessor;
        }else{
            dateTime = ((LocalDate) temporalAccessor).atStartOfDay();
        }
        return dateTime;
    }

    public static  LocalDateTime stringToLocalDateTime(String time){
        return getLocalDateTime(getTemporalAccessor(time));
    }

}
