package com.gdchhkf.weather.web.controller;

import com.gdchhkf.weather.utils.TimeUtils;
import com.gdchhkf.weather.web.domain.Weather;
import com.gdchhkf.weather.web.domain.vo.WeatherMonth;
import com.gdchhkf.weather.web.domain.vo.WeatherWeek;
import com.gdchhkf.weather.web.service.ExcelService;
import com.gdchhkf.weather.web.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;

/**
 * 获取气象数据的Controller
 * @author gdchhkf@163.com
 * @version 1.0
 **/
@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;
    @Autowired
    private ExcelService excelService;

    /**
     * 获取过去一周的气象数据
     * @author gdchhkf@163.com
     * @see WeatherWeek
     **/
    @GetMapping("/week")
    public WeatherWeek weatherWeek() {
        return weatherService.getWeatherWeek();
    }

    @GetMapping("/week/excel")
    public void WeatherWeekExcelDownload(HttpServletResponse response) {
        String fileName = TimeUtils.getLastMonday().format(DateTimeFormatter.ofPattern(TimeUtils.DAY));
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName + ".xls");
        response.setContentType("application/x-download");
        try (OutputStream outputStream = response.getOutputStream()) {
            excelService.getWeatherWeekExcel(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
