package com.gdchhkf.weather.service.impl;

import com.gdchhkf.weather.domain.Weather;
import com.gdchhkf.weather.domain.vo.WeatherMonth;
import com.gdchhkf.weather.domain.vo.WeatherWeek;
import com.gdchhkf.weather.service.HDFSService;
import com.gdchhkf.weather.service.WeatherService;
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
    private HDFSService hdfsService;

    /**
     * @Author gdchhkf@163.com
     * @Description
     *   获取最新的天气信息
     *
     * @Date 22:22 2019/3/3
     * @Param []
     * @return com.gdchhkf.weather.domain.Weather
     **/
    public Weather getWeatherNow() {
        return (Weather) cache.get("now");
    }

    /**
     * @Author gdchhkf@163.com
     * @Description
     *
     * @Date 13:01 2019/3/4
     * @Param []
     * @return com.gdchhkf.weather.domain.vo.WeatherWeek
     **/
    public WeatherWeek getWeatherWeek() {
        if (cache.get("week") != null) {
            return (WeatherWeek) cache.get("week");
        }
        WeatherWeek weatherWeek = new WeatherWeek();
        weatherWeek.setWeatherMap(hdfsService.readLastWeekFiles());
        hdfsService.setLastWeekResult(weatherWeek);
        return weatherWeek;
    }

    /**
     * @Author gdchhkf@163.com
     * @Description
     * @Date 13:01 2019/3/4
     * @Param []
     * @return com.gdchhkf.weather.domain.vo.WeatherMonth
     **/
    public WeatherMonth getWeatherMonth () {

        return null;
    }
}
