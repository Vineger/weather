package com.gdchhkf.weather.timeTask;

import com.gdchhkf.weather.hadoop.FileOperation;
import com.gdchhkf.weather.hadoop.MapReduceOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class WeatherWeekTask {

    @Autowired
    private FileOperation fileOperation;
    @Autowired
    private MapReduceOperation mapReduceOperation;

    @Scheduled(cron = "0 0 10 ? * MON")
    private void weatherWeekListener(){
        List<String> files = fileOperation.getExistsPastWeekFile();
        mapReduceOperation.createWeatherWeekJob(files);
    }
}
