package com.gdchhkf.weather.web.service;

import java.io.OutputStream;

public interface ExcelService {
    void getWeatherWeekExcel(OutputStream outputStream, ExcelType type);
    void getCustomWeatherExcel(OutputStream outputStream, String start, String end);
}
