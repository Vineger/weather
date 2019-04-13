package com.gdchhkf.weather.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TimeUtils {

    public static final String DAY = "uuuu_MM_dd";
    public static final String MONTH = "uuuu_MM";
    public static final String HOUR = "uuuuMMddHHmmss";

    public static LocalDateTime getLastMonday() {
        LocalDateTime today = LocalDateTime.now();
        return today.minusDays(today.getDayOfWeek().getValue() - 1).minusDays(7);
    }

    public static LocalDateTime getLastMonthFisrtDay() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime firstDay = today.minusMonths(1).withDayOfMonth(1);
        return firstDay;
    }

    /**
     * 返回上个星期, 格式为@see{TimeUtils.DAY}的日期字符串
     * @author gdchhkf@163.com
     * @return String[]
     */
    public static List<String> getLastWeek() {
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDateTime temp = getLastMonday().plusDays(i);
            String date = temp.format(DateTimeFormatter.ofPattern(TimeUtils.DAY));
            dates.add(date);
        }
        return dates;
    }

    /**
     * 返回上个月, 格式为@see{TimeUtils.DAY}的日期字符串
     * @author gdchhkf@163.com
     * @return String[]
     */
    public static List<String> getLastMonth() {
        List<String> dates = new ArrayList<>();
        LocalDateTime dateOfMonth = LocalDateTime.now().withDayOfMonth(1).minusMonths(1);

        do {
            String data = dateOfMonth.format(DateTimeFormatter.ofPattern(TimeUtils.DAY));
            dates.add(data);
            dateOfMonth = dateOfMonth.plusDays(1);
        } while (dateOfMonth.getDayOfMonth() != 1);

        return dates;
    }

    public static LocalDate parseToLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DAY);
        return LocalDate.parse(date, formatter);
    }

    public static LocalDateTime parseTimeStampToLocalDateTime(long timeStamp){
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = Instant.ofEpochMilli(timeStamp);
        return LocalDateTime.ofInstant(instant, zoneId);
    }


}
