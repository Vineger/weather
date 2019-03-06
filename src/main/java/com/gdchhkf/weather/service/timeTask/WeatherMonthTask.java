package com.gdchhkf.weather.service.timeTask;

import com.gdchhkf.weather.service.MapReduceService;
import com.gdchhkf.weather.service.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName WeatherMonthTask
 * @Description 每月执行一次, 分析过去一个月的气象数据
 * @Author gdchhkf@163.com
 * @Date 2019/3/3 14:39
 * @Version 1.0
 **/
@Component
public class WeatherMonthTask {

    @Autowired
    private MapReduceService mapReduceService;
    @Autowired
    private TimeUtils utils;

    @Scheduled(cron = "0 0 10 1 1/1 ?")
    public void weatherMonthListener() {
        List<String> files = utils.getExistsPastMonthFile();
        mapReduceService.createWeatherMonthJob(files);
    }

}
