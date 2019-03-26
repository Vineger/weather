package com.gdchhkf.weather.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class TimeUtils {

    public static final String DAY = "uuuu_MM_dd";
    public static final String MONTH = "uuuu_MM";
    public static final String HOUR = "uuuuMMddHHmmss";

    public static LocalDateTime getLastMonday() {
        LocalDateTime today = LocalDateTime.now();
        return today.minusDays(today.getDayOfWeek().getValue() - 1).minusDays(7);
    }

    public static Date parseLocalDateTimeToDate(LocalDateTime dateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = dateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    public static LocalDateTime parseTimeStampToLocalDateTime(long timeStamp){
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = Instant.ofEpochMilli(timeStamp);
        return LocalDateTime.ofInstant(instant, zoneId);
    }


}
