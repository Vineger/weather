package com.gdchhkf.weather.service.utils;

import org.apache.hadoop.io.FloatWritable;

/**
 * @ClassName FloatWritableUtils
 * @Description TODO
 * @Author gdchhkf@163.com
 * @Date 2019/3/3 17:08
 * @Version 1.0
 **/
public class FloatWritableUtils {

    /**
     * @Author gdchhkf@163.com
     * @Description
     * @Date 17:09 2019/3/3
     * @Param [values]
     * @return org.apache.hadoop.io.FloatWritable
     **/
    public static FloatWritable findMax(Iterable<FloatWritable> values) {
        float max = Float.MIN_VALUE;
        for(FloatWritable val : values){
            max = val.get() > max ? val.get() : max;
        }
        return new FloatWritable(max);
    }

    /**
     * @Author gdchhkf@163.com
     * @Description
     * @Date 17:11 2019/3/3
     * @Param [values]
     * @return org.apache.hadoop.io.FloatWritable
     **/
    public static FloatWritable findMin(Iterable<FloatWritable> values) {
        float min = Float.MAX_VALUE;
        for(FloatWritable val : values){
            min = val.get() < min ? val.get() : min;
        }
        return new FloatWritable(min);
    }

    /**
     * @Author gdchhkf@163.com
     * @Description
     * @Date 17:13 2019/3/3
     * @Param [values]
     * @return org.apache.hadoop.io.FloatWritable
     **/
    public static FloatWritable sum(Iterable<FloatWritable> values) {

        float sum = 0f;
        for(FloatWritable val : values){
            sum += val.get();
        }

        return new FloatWritable(sum);
    }

    /**
     * @Author gdchhkf@163.com
     * @Description
     * @Date 17:15 2019/3/3
     * @Param [values]
     * @return org.apache.hadoop.io.FloatWritable
     **/
    public static FloatWritable avg(Iterable<FloatWritable> values) {
        float sum = 0f;
        int time = 0;
        for(FloatWritable val : values){
            time++;
            sum += val.get();
        }

        return new FloatWritable(sum / time);
    }
}
