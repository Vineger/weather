package com.gdchhkf.weather.domain.vo;

import com.gdchhkf.weather.domain.Weather;
import lombok.Data;

import java.util.List;

/**
 * @ClassName WeatherMonth
 * @Description TODO
 * @Author gdchhkf@163.com
 * @Date 2019/3/3 22:35
 * @Version 1.0
 **/
@Data
public class WeatherMonth {
    private List<Weather> list;
    private float temMax;
    private float temMin;
    //降水量
    private float pre;
}
