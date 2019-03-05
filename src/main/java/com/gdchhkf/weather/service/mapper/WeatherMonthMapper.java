package com.gdchhkf.weather.service.mapper;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * @ClassName WeatherMonthMapper
 * @Description TODO
 * @Author gdchhkf@163.com
 * @Date 2019/3/3 16:06
 * @Version 1.0
 **/
public class WeatherMonthMapper extends Mapper<Object, Text, Text, FloatWritable> {

    public void map(Object key, Text text, Context context) {

    }
}
