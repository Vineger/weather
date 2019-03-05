package com.gdchhkf.weather.service.reducer;

import com.gdchhkf.weather.domain.Weather;
import com.gdchhkf.weather.service.utils.FloatWritableUtils;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @ClassName WeatherDayReducer
 * @Description TODO
 * @Author gdchhkf@163.com
 * @Date 2019/3/1 21:58
 * @Version 1.0
 **/
public class WeatherDayReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {
    private FloatWritable result = new FloatWritable();

    public void reduce(Text key, Iterable<FloatWritable> values, Context context)
            throws IOException, InterruptedException{

        String keyStr = key.toString();

        if (keyStr.equals(Weather.TEM_MAX)){
            //找出最高温度最大的
            FloatWritable max = FloatWritableUtils.findMax(values);
            context.write(key, max);

        } else if(keyStr.equals(Weather.TEM_MIN)){
            //找出最低温度最小的
            FloatWritable min = FloatWritableUtils.findMin(values);
            context.write(key, min);

        } else if (keyStr.equals(Weather.PRE_1H)){
            //算出总降雨量
            FloatWritable sum = FloatWritableUtils.sum(values);
            context.write(new Text(Weather.PRE), sum);

        } else {
            FloatWritable avg = FloatWritableUtils.avg(values);
            context.write(key, avg);
        }
    }
}
