package com.gdchhkf.weather.web.service.impl;

import com.gdchhkf.weather.utils.TimeUtils;
import com.gdchhkf.weather.web.domain.Weather;
import com.gdchhkf.weather.web.domain.vo.WeatherWeek;
import com.gdchhkf.weather.web.service.ExcelService;
import com.gdchhkf.weather.web.service.ExcelType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author gdchhkf@163.com
 * @version 1.0
 **/
@Service
public class ExcelServiceImpl implements ExcelService {

    @Resource(name = "cache")
    private Map cache;

    public void getWeatherWeekExcel(OutputStream outputStream) {
        WeatherWeek weatherWeek = (WeatherWeek) cache.get("week");
        List<Map<String, String>> list = weatherWeek.getWeatherMap();
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("new sheet");
        Row head = sheet.createRow(0);
        setHead(head);
        setDate(sheet, ExcelType.WEEK);
        setData(sheet, list);
        try {
            wb.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setData(Sheet sheet, List<Map<String, String>> list) {
        int index = 1;
        for (Map<String, String> map : list) {
            sheet.getRow(index).createCell(1).setCellValue(Double.parseDouble(map.get(Weather.TEM)));
            sheet.getRow(index).createCell(2).setCellValue(Double.parseDouble(map.get(Weather.TEM_MAX)));
            sheet.getRow(index).createCell(3).setCellValue(Double.parseDouble(map.get(Weather.TEM_MIN)));
            sheet.getRow(index).createCell(4).setCellValue(Double.parseDouble(map.get(Weather.TIGAN)));
            sheet.getRow(index).createCell(5).setCellValue(Double.parseDouble(map.get(Weather.PRS)));
            sheet.getRow(index).createCell(6).setCellValue(Double.parseDouble(map.get(Weather.PRS_SEA)));
            sheet.getRow(index).createCell(7).setCellValue(Double.parseDouble(map.get(Weather.VAP)));
            sheet.getRow(index).createCell(8).setCellValue(Double.parseDouble(map.get(Weather.RHU)));
            sheet.getRow(index).createCell(9).setCellValue(Double.parseDouble(map.get(Weather.PRE)));
            index++;
        }
    }

    private void setHead(Row head) {
        head.createCell(0);
        head.createCell(1).setCellValue("温度");
        head.createCell(2).setCellValue("最高温度");
        head.createCell(3).setCellValue("最低温度");
        head.createCell(4).setCellValue("体感温度");
        head.createCell(5).setCellValue("气压");
        head.createCell(6).setCellValue("海平面气压");
        head.createCell(7).setCellValue("水汽压");
        head.createCell(8).setCellValue("相对湿度");
        head.createCell(9).setCellValue("降水量");
    }

    private void setDate(Sheet sheet, ExcelType type) {
        if (type == ExcelType.WEEK) {
            List<String> dates = TimeUtils.getLastWeek();
            for (int i = 0; i < dates.size(); i++) {
                sheet.createRow(i + 1).createCell(0).setCellValue(dates.get(i));
            }
        } else if (type == ExcelType.MONTH) {
            List<String> dates = TimeUtils.getLastMonth();
            for (int i = 0; i < dates.size(); i++) {
                sheet.createRow(i + 1).createCell(0).setCellValue(dates.get(i));
            }
        }
    }
}
