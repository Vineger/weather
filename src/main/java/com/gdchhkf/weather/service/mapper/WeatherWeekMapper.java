package com.gdchhkf.weather.service.mapper;

import com.gdchhkf.weather.domain.Weather;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @ClassName WeatherWeekMapper
 * @Description TODO
 * @Author gdchhkf@163.com
 * @Date 2019/3/3 16:06
 * @Version 1.0
 **/
public class WeatherWeekMapper extends Mapper<Object, Text, Text, FloatWritable> {

    public void map(Object key, Text text, Context context) throws IOException, InterruptedException {
        StringTokenizer tokenizer = new StringTokenizer(text.toString());

        while(tokenizer.hasMoreTokens()) {
            String tempKey = tokenizer.nextToken();

            if(tempKey.equals(Weather.TEM_MAX) ||
                tempKey.equals(Weather.TEM_MIN) ||
                tempKey.equals(Weather.PRS)){

                float tempValue = Float.parseFloat(tokenizer.nextToken());
                context.write(new Text(tempKey), new FloatWritable(tempValue));
            }else {
                tokenizer.nextToken();
            }
        }
    }
}
