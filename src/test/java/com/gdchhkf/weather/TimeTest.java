package com.gdchhkf.weather;

import org.junit.Test;

import java.time.LocalDateTime;

/**
 * @ClassName TimeTest
 * @Description TODO
 * @Author gdchhkf@163.com
 * @Date 2019/3/1 10:28
 * @Version 1.0
 **/
public class TimeTest {

    @Test
    public void timeTest(){
        LocalDateTime today = LocalDateTime.now();
        int number = today.getDayOfWeek().getValue();
        System.out.println(today.plusDays(7 + 1 - number));
    }
}
