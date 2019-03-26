package com.gdchhkf.weather.utils;

import org.apache.hadoop.io.FloatWritable;


public class FloatWritableUtils {

    public static FloatWritable findMax(Iterable<FloatWritable> values) {
        float max = Float.MIN_VALUE;
        for(FloatWritable val : values){
            max = val.get() > max ? val.get() : max;
        }
        return new FloatWritable(max);
    }

    public static FloatWritable findMin(Iterable<FloatWritable> values) {
        float min = Float.MAX_VALUE;
        for(FloatWritable val : values){
            min = val.get() < min ? val.get() : min;
        }
        return new FloatWritable(min);
    }


    public static FloatWritable sum(Iterable<FloatWritable> values) {

        float sum = 0f;
        for(FloatWritable val : values){
            sum += val.get();
        }

        return new FloatWritable(sum);
    }


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
