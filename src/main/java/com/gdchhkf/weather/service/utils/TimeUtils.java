package com.gdchhkf.weather.service.utils;

import com.gdchhkf.weather.service.HDFSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName TimeUtils
 * @Description LocalDateTime 等的工具类
 * @Author gdchhkf@163.com
 * @Date 2019/3/3 14:25
 * @Version 1.0
 **/
@Component
public class TimeUtils {

    @Autowired
    private HDFSService hdfsService;

    /**
     * @Author gdchhkf@163.com
     * @Description 获取上个星期一
     * @Date 12:25 2019/3/4
     * @Param []
     * @return java.time.LocalDateTime
     **/
    public LocalDateTime getLastMonday() {
        LocalDateTime today = LocalDateTime.now();
        return today.minusDays(today.getDayOfWeek().getValue() - 1).minusDays(7);
    }

    /**
     * @Author gdchhkf@163.com
     * @Description
     *
     *
     * @Date 14:20 2019/3/5
     * @Param [dateTime]
     * @return Date
     **/
    public Date parseLocalDateTimeToDate(LocalDateTime dateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = dateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * @Author gdchhkf@163.com
     * @Description LocalDateTime to Date
     * @Date 23:28 2019/3/1
     * @Param
     * @return
     **/
    public LocalDateTime parseTimeStampToLocalDateTime(long timeStamp){
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = Instant.ofEpochMilli(timeStamp);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, zoneId);
        return dateTime;
    }

    /**
     * @Author gdchhkf@163.com
     * @Description
     * @Date 21:29 2019/3/3
     * @Param []
     * @return java.util.List<java.lang.String>
     **/
    public List<String> getExistsPastMonthFile() {
        List<String> files = new ArrayList<>();
        LocalDateTime dateOfMonth = LocalDateTime.now().minusMonths(1);

        do {
            String fileName = dateOfMonth.format(DateTimeFormatter.ofPattern("uuuu_mm"));
            String file = "/weather/day/" + fileName;
            if(hdfsService.exists(file)) {
                files.add(file);
            }
            dateOfMonth = dateOfMonth.plusDays(1);
        } while (dateOfMonth.getDayOfMonth() != 1);
        return files;
    }

    /**
     * @Author gdchhkf@163.com
     * @Description
     *   获取过去一周7天中确实存在的文件列表
     *   输入文件可能不存在的问题, 可能不是最优解
     * @Date 15:53 2019/3/3
     * @Param []
     * @return java.util.List<java.lang.String>
     **/
    public List<String> getExistsPastWeekFile() {
        List<String> files = new ArrayList<>();
        LocalDateTime lastMonday = getLastMonday();

        //循环判断文件是否存在
        for (int i = 0; i <= 6; i++){
            LocalDateTime temp = lastMonday.plusDays(i);
            String fileName = temp.format(DateTimeFormatter.ofPattern("uuuu_MM_dd"));
            String file = "/weather/day/" + fileName;
            //利用FileSystem的API判断文件是否存在
            if(hdfsService.exists(file)) {
                files.add(file);
            }
        }
        return files;
    }
}
