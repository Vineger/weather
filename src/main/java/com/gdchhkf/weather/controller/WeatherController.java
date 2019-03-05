package com.gdchhkf.weather.controller;

import com.gdchhkf.weather.domain.Weather;
import com.gdchhkf.weather.domain.vo.WeatherMonth;
import com.gdchhkf.weather.domain.vo.WeatherWeek;
import com.gdchhkf.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName WeatherController
 * @Description TODO
 * @Author gdchhkf@163.com
 * @Date 2019/3/3 21:29
 * @Version 1.0
 **/
@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/week")
    public WeatherWeek weatherWeek() {
        return weatherService.getWeatherWeek();
    }

    @GetMapping("/month")
    public WeatherMonth weatherMonth() {
        return weatherService.getWeatherMonth();
    }

    @GetMapping("/now")
    public Weather weatherNow() {
        return weatherService.getWeatherNow();
    }
}
