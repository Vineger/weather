package com.gdchhkf.weather.service.timeTask;

import com.gdchhkf.weather.domain.vo.WeatherWeek;
import com.gdchhkf.weather.service.HDFSService;
import com.gdchhkf.weather.service.MapReduceService;
import com.gdchhkf.weather.service.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @ClassName WeatherWeektask
 * @Description 每周执行一次, 分析过去7天气象数据
 * @Author gdchhkf@163.com
 * @Date 2019/3/3 14:36
 * @Version 1.0
 **/
@Component
public class WeatherWeekTask {

    @Autowired
    private TimeUtils utils;
    @Autowired
    private HDFSService hdfsService;
    @Autowired
    private MapReduceService mapReduceService;
    @Autowired
    private Map cache;

    /**
     * @Author gdchhkf@163.com
     * @Description 每周执行一次, 分析统计出周数据
     *
     * @Date 10:13 2019/3/1
     * @Param [event]
     * @return void
     **/
    @Scheduled(cron = "0 0 10 ? * MON")
    private void weatherWeekListener(){
        List<String> files = utils.getExistsPastWeekFile();
        mapReduceService.createWeatherWeekJob(files);
        WeatherWeek weatherWeek = new WeatherWeek();
        weatherWeek.setWeatherMap(hdfsService.readLastWeekFiles());
        hdfsService.setLastWeekResult(weatherWeek);
        cache.put("week", weatherWeek);
    }
}
