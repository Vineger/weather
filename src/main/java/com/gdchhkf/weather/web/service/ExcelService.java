package com.gdchhkf.weather.web.service;

import java.io.OutputStream;

public interface ExcelService {
    void getWeatherWeekExcel(OutputStream outputStream);
}
