package com.gdchhkf.weather.hadoop.mapper;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;


/**
 * @ClassName WeatherDayMapper
 * @Description TODO
 * @Author gdchhkf@163.com
 * @Date 2019/3/1 10:57
 * @Version 1.0
 **/
public class WeatherDayMapper extends Mapper<Object, Text, Text, FloatWritable> {
    private Text word = new Text();
    private FloatWritable number = new FloatWritable();

    public void map(Object key, Text value, Context content) throws IOException, InterruptedException{
        StringTokenizer itr = new StringTokenizer(value.toString(), "=\n,");
        while(itr.hasMoreTokens()){
            word.set(itr.nextToken());
            number.set(Float.parseFloat(itr.nextToken()));
            content.write(word, number);
        }
    }
}
