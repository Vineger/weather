package com.gdchhkf.weather.service;

import java.util.List;

public interface MapReduceService {
    boolean createWeatherDayJob(String srcFile);
    boolean createWeatherWeekJob(List<String> files);
    boolean createWeatherMonthJob(List<String> files);
}
