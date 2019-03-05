package com.gdchhkf.weather.service.reducer;

import com.gdchhkf.weather.domain.Weather;
import com.gdchhkf.weather.service.utils.FloatWritableUtils;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @ClassName WeatherWeekReducer
 * @Description TODO
 * @Author gdchhkf@163.com
 * @Date 2019/3/3 16:07
 * @Version 1.0
 **/
public class WeatherWeekReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {

    public void reduce(Text key, Iterable<FloatWritable> values, Context context) throws IOException, InterruptedException {

        if (key.toString().equals(Weather.TEM_MAX)) {
            FloatWritable max = FloatWritableUtils.findMax(values);
            context.write(key, max);

        } else if (key.toString().equals(Weather.TEM_MIN)) {
            FloatWritable min = FloatWritableUtils.findMin(values);
            context.write(key, min);

        } else if (key.toString().equals(Weather.PRE)) {
            FloatWritable sum = FloatWritableUtils.sum(values);
            context.write(key, sum);
        }
    }
}
