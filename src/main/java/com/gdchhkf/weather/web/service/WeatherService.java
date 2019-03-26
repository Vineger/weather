package com.gdchhkf.weather.web.service;

import com.gdchhkf.weather.web.domain.Weather;
import com.gdchhkf.weather.web.domain.vo.WeatherMonth;
import com.gdchhkf.weather.web.domain.vo.WeatherWeek;

public interface WeatherService {

    Weather getWeatherNow();
    WeatherWeek getWeatherWeek();
    WeatherMonth getWeatherMonth();
}
