package com.gdchhkf.weather.service;

import com.gdchhkf.weather.domain.vo.WeatherWeek;

import java.util.Map;

public interface HDFSService {

    boolean appendFile(String file, String content);
    boolean deleteDir(String dir);
    boolean renameFile(String srcDir, String desDir, String fileName);
    String readFile(String fileName);
    boolean exists(String file);
    Map readLastWeekFiles();
    void setLastWeekResult(WeatherWeek week);
}
