package com.gdchhkf.weather.service.timeTask;

import com.gdchhkf.weather.service.MapReduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName WeatherDayTask
 * @Description 每天执行一次分析过去24小时采集到的气象数据
 * @Author gdchhkf@163.com
 * @Date 2019/3/3 14:31
 * @Version 1.0
 **/
@Component
public class WeatherDayTask {

    @Autowired
    private MapReduceService mapReduceService;

    /**
     * @Author gdchhkf@163.com
     * @Description 每天分析一次气象数据
     * @Date 23:24 2019/3/1
     * @Param [event]
     * @return void
     **/
    @Scheduled(cron = "0 0 10 1/1 * ?")
    private void weatherDayListener(){
        mapReduceService.createWeatherDayJob(getWeatherHourFileName());
    }


    /**
     * @Author gdchhkf@163.com
     * @Description
     * @Date 18:45 2019/3/3
     * @Param []
     * @return java.lang.String
     **/
    private String getWeatherHourFileName () {
        LocalDateTime today = LocalDateTime.now();
        LocalDate yesterday = today.minusDays(1).toLocalDate();
        String fileName = yesterday.format(DateTimeFormatter.ofPattern("uuuu_MM_d"));
        return fileName;
    }
}
