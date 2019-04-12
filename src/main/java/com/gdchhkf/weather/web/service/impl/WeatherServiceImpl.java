package com.gdchhkf.weather.web.service.impl;

import com.gdchhkf.weather.hadoop.FileType;
import com.gdchhkf.weather.web.domain.Weather;
import com.gdchhkf.weather.hadoop.FileOperation;
import com.gdchhkf.weather.web.domain.vo.WeatherMonth;
import com.gdchhkf.weather.web.domain.vo.WeatherWeek;
import com.gdchhkf.weather.web.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    @Qualifier("cache")
    private Map cache;
    @Autowired
    private FileOperation fileOperation;

    public Weather getWeatherNow() {
        return (Weather) cache.get("now");
    }


    public WeatherWeek getWeatherWeek() {
        if (cache.get("week") != null) {
            return (WeatherWeek) cache.get("week");
        }
        WeatherWeek weatherWeek = new WeatherWeek();
        weatherWeek.setWeatherMap(fileOperation.readFiles(FileType.WEEK));
        fileOperation.setLastWeekResult(weatherWeek);
        cache.put("week", weatherWeek);
        return weatherWeek;
    }


    public WeatherMonth getWeatherMonth () {
        if (cache.get("month") != null) {
            return (WeatherMonth) cache.get("month");
        }
        WeatherMonth weatherMonth = new WeatherMonth();
        weatherMonth.setWeatherDates(fileOperation.readFiles(FileType.MONTH));
        cache.put("month", weatherMonth);
        return weatherMonth;
    }
}
