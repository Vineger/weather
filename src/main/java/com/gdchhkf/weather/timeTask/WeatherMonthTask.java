package com.gdchhkf.weather.timeTask;

import com.gdchhkf.weather.hadoop.FileOperation;
import com.gdchhkf.weather.hadoop.FileType;
import com.gdchhkf.weather.hadoop.MapReduceOperation;
import com.gdchhkf.weather.web.domain.vo.WeatherMonth;
import com.gdchhkf.weather.web.domain.vo.WeatherWeek;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Component
public class WeatherMonthTask {

    @Autowired
    private MapReduceOperation mapReduceOperation;
    @Autowired
    private FileOperation fileOperation;
    @Resource(name = "cache")
    private Map cache;

    @Scheduled(cron = "0 0 11 1 1/1 ?")
    public void weatherMonthListener() {
        List<String> files = fileOperation.getExistsFile(FileType.MONTH);
        mapReduceOperation.createWeatherMonthJob(files);

        WeatherMonth weatherMonth = new WeatherMonth();
        weatherMonth.setWeatherDates(fileOperation.readFiles(FileType.MONTH));
        cache.put("week", weatherMonth);
    }

}
