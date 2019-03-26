package com.gdchhkf.weather.timeTask;

import com.gdchhkf.weather.hadoop.FileOperation;
import com.gdchhkf.weather.hadoop.MapReduceOperation;
import com.gdchhkf.weather.web.domain.vo.WeatherWeek;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class WeatherWeekTask {

    @Autowired
    private FileOperation fileOperation;
    @Autowired
    private MapReduceOperation mapReduceOperation;
    @Autowired
    private Map cache;

    @Scheduled(cron = "0 0 10 ? * MON")
    private void weatherWeekListener(){
        List<String> files = fileOperation.getExistsPastWeekFile();
        mapReduceOperation.createWeatherWeekJob(files);

        WeatherWeek weatherWeek = new WeatherWeek();
        weatherWeek.setWeatherMap(fileOperation.readLastWeekFiles());
        fileOperation.setLastWeekResult(weatherWeek);
        cache.put("week", weatherWeek);
    }
}
