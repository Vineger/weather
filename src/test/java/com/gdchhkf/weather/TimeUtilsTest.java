package com.gdchhkf.weather;

import com.gdchhkf.weather.utils.TimeUtils;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName TimeUtilsTest
 * @Description TODO
 * @Author gdchhkf@163.com
 * @Date 2019/3/1 10:28
 * @Version 1.0
 **/
public class TimeUtilsTest {

    @Test
    public void getLastMonthTest(){
        List<String> dates = TimeUtils.getLastMonth();
        System.out.println(dates);
    }
}
