package com.gdchhkf.weather.hadoop.mapper;

import com.gdchhkf.weather.web.domain.Weather;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;


public class WeatherWeekMapper extends Mapper<Object, Text, Text, FloatWritable> {

    public void map(Object key, Text text, Context context) throws IOException, InterruptedException {
        StringTokenizer tokenizer = new StringTokenizer(text.toString());

        while(tokenizer.hasMoreTokens()) {
            String tempKey = tokenizer.nextToken();

            if(tempKey.equals(Weather.TEM_MAX) ||
                tempKey.equals(Weather.TEM_MIN) ||
                tempKey.equals(Weather.PRE)){

                float tempValue = Float.parseFloat(tokenizer.nextToken());
                context.write(new Text(tempKey), new FloatWritable(tempValue));
            }else {
                tokenizer.nextToken();
            }
        }
    }
}
