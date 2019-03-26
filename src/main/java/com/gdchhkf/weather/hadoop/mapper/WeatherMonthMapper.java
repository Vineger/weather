package com.gdchhkf.weather.hadoop.mapper;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class WeatherMonthMapper extends Mapper<Object, Text, Text, FloatWritable> {

    public void map(Object key, Text text, Context context) {

    }
}
