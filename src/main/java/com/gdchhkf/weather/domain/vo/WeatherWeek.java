package com.gdchhkf.weather.domain.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 传回前端的数据容器
 * @author gdchhkf@163.com
 * @version 1.0
 **/
@Data
public class WeatherWeek {
    private List<Map> weatherMap = new ArrayList<>();
    private float temMax;
    private float temMin;
    //降水量
    private float pre;
}
