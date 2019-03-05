package com.gdchhkf.weather.domain.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName WeatherWeek
 * @Description TODO
 * @Author gdchhkf@163.com
 * @Date 2019/3/3 22:35
 * @Version 1.0
 **/
@Data
public class WeatherWeek {
    private Map weatherMap = new HashMap();
    private float temMax;
    private float temMin;
    //降水量
    private float pre;
}
