package com.gdchhkf.weather.controller;

import com.gdchhkf.weather.domain.Weather;
import com.gdchhkf.weather.domain.vo.WeatherMonth;
import com.gdchhkf.weather.domain.vo.WeatherWeek;
import com.gdchhkf.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取气象数据的Controller
 * @author gdchhkf@163.com
 * @version 1.0
 **/
@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    /**
     * 获取过去一周的气象数据
     * @author gdchhkf@163.com
     * @see WeatherWeek
     **/
    @GetMapping("/week")
    public WeatherWeek weatherWeek() {
        return weatherService.getWeatherWeek();
    }

    /**
     * 获取过去一月的气象数据
     * @author gdchhkf@163.com
     * @see WeatherMonth
     **/
    @GetMapping("/month")
    public WeatherMonth weatherMonth() {
        return weatherService.getWeatherMonth();
    }

    /**
     * 获取现在的气象数据
     * @author gdchhkf@163.com
     * @see Weather
     **/
    @GetMapping("/now")
    public Weather weatherNow() {
        return weatherService.getWeatherNow();
    }
}
