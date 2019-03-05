package com.gdchhkf.weather.service;

import com.gdchhkf.weather.domain.Weather;
import com.gdchhkf.weather.domain.vo.WeatherMonth;
import com.gdchhkf.weather.domain.vo.WeatherWeek;

public interface WeatherService {

    Weather getWeatherNow();
    WeatherWeek getWeatherWeek();
    WeatherMonth getWeatherMonth();
}
