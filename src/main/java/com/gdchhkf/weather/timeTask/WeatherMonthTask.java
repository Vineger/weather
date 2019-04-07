package com.gdchhkf.weather.timeTask;

import com.gdchhkf.weather.hadoop.FileOperation;
import com.gdchhkf.weather.hadoop.FileType;
import com.gdchhkf.weather.hadoop.MapReduceOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class WeatherMonthTask {

    @Autowired
    private MapReduceOperation mapReduceOperation;
    @Autowired
    private FileOperation fileOperation;

    @Scheduled(cron = "0 0 11 1 1/1 ?")
    public void weatherMonthListener() {
        List<String> files = fileOperation.getExistsFile(FileType.MONTH);
        mapReduceOperation.createWeatherMonthJob(files);
    }

}
