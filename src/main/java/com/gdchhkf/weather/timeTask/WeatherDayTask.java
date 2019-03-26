package com.gdchhkf.weather.timeTask;

import com.gdchhkf.weather.hadoop.MapReduceOperation;
import com.gdchhkf.weather.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class WeatherDayTask {

    @Autowired
    private MapReduceOperation operation;

    @Scheduled(cron = "0 0 10 1/1 * ?")
    private void weatherDayListener(){
        operation.createWeatherDayJob(getWeatherHourFileName());
    }


    private String getWeatherHourFileName () {
        LocalDateTime today = LocalDateTime.now();
        LocalDate yesterday = today.minusDays(1).toLocalDate();
        String fileName = yesterday.format(DateTimeFormatter.ofPattern(TimeUtils.DAY));
        return fileName;
    }
}
