package com.gdchhkf.weather.service.impl;

import com.gdchhkf.weather.service.HDFSService;
import com.gdchhkf.weather.service.MapReduceService;
import com.gdchhkf.weather.service.mapper.WeatherDayMapper;
import com.gdchhkf.weather.service.mapper.WeatherWeekMapper;
import com.gdchhkf.weather.service.reducer.WeatherDayReducer;
import com.gdchhkf.weather.service.reducer.WeatherWeekReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * MapReduce 服务
 * 提供创建DayTask, WeekTask, MonthTask
 *
 * @author gdchhkf@163.com
 * @version 1.0
 **/
@Service
public class MapReduceServiceImpl implements MapReduceService {

    @Autowired
    private HDFSService hdfsService;
    @Autowired
    private Configuration conf;

    /**
     * 创建 DayJob 分析过去24小时的气象数据
     * 具体的Mapper逻辑 {@link WeatherDayMapper}
     * 具体的Reducer逻辑 {@link WeatherWeekReducer}
     * @author gdchhkf@163.com
     * @param  srcFileName 要处理的hour文件的文件名
     * @return boolean 任务创建并提交是否成功
     **/
    public boolean createWeatherDayJob(String srcFileName) {
        String inputFile = "/weather/hour/" + srcFileName;
        String outputTmp = "/weather/day/tmp/";
        String outputDir = "/weather/day/";

        Job job = getInstance("analysis weather day");
        setJob(job, WeatherDayMapper.class, WeatherDayReducer.class);
        setSingleInput(job, inputFile);
        setOutputPath(job, outputTmp);
        return submitJob(job, outputDir, outputTmp, srcFileName);

    }

    /**
     * 创建WeekTask, 分析过去一周的气象数据
     * Mapper的具体逻辑 {@link WeatherWeekMapper}
     * Reducer的具体逻辑 {@link WeatherWeekReducer}
     * @author gdchhkf@163.com
     * @param files 过去一周中确实存在的文件
     * @return boolean 任务创建并提交是否成功
     **/
    public boolean createWeatherWeekJob(List<String> files) {
        LocalDate today = LocalDate.now();
        String newFileName = today.format(DateTimeFormatter.ofPattern("uuuu_MM_dd"));
        String outputTmp = "/weather/week/tmp/";
        String outputDir = "/weather/week/";

        Job job = getInstance("analysis weather week");
        setJob(job, WeatherWeekMapper.class, WeatherWeekReducer.class);
        setMultipleInputs(job, files);
        setOutputPath(job, outputTmp);
        return submitJob(job, outputDir, outputTmp, newFileName);
    }


    /**
     *
     * @author gdchhkf@163.com
     * @param files
     * @return boolean
     **/
    public boolean createWeatherMonthJob(List<String> files) {
        LocalDate today = LocalDate.now();
        String newFileName = today.format(DateTimeFormatter.ofPattern("uuuu_mm"));
        String outputTmp = "/weather/month/tmp/";
        String outputDir = "/weather/month/";


        Job job = getInstance("analysis weather month");
        boolean result;
        // 由于 Month 和 Day 的处理逻辑相同
        // 所有使用相同的 Mapper 和 Reducer
        setJob(job, WeatherWeekMapper.class, WeatherWeekReducer.class);
        setMultipleInputs(job, files);
        setOutputPath(job, outputTmp);
        result = submitJob(job, outputDir, outputTmp, newFileName);
        return result;
    }



    /**
     * @Author gdchhkf@163.com
     * @Description 获取Job实例并命名
     * @Date 18:03 2019/3/3
     * @Param [jobName]
     * @return org.apache.hadoop.mapreduce.Job
     **/
    private Job getInstance(String jobName) {
        Job job = null;
        try {
            job = Job.getInstance(conf, jobName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return job;
    }

    /**
     * @Author gdchhkf@163.com
     * @Description
     *   设置Mapper, Reducer, Combiner的class
     *   设置输出的K, V类型
     * @Date 18:03 2019/3/3
     * @Param [job, mapper, reducer]
     * @return void
     **/
    private void setJob(Job job, Class<? extends Mapper> mapper, Class<? extends Reducer> reducer) {

        job.setJarByClass(this.getClass());
        job.setMapperClass(mapper);
        job.setCombinerClass(reducer);
        job.setReducerClass(reducer);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);
    }

    /**
     * @Author gdchhkf@163.com
     * @Description 设置多文件输入源
     * @Date 18:03 2019/3/3
     * @Param [job, files]
     * @return void
     **/
    private void setMultipleInputs(Job job, List<String> files) {
        for(String file : files){
            MultipleInputs.addInputPath(job, new Path(file), FileInputFormat.class);
        }
    }

    /**
     * @Author gdchhkf@163.com
     * @Description 设置单文件输入源
     * @Date 18:10 2019/3/3
     * @Param [job]
     * @return void
     **/
    private void setSingleInput(Job job, String inputFile) {
        try {
            FileInputFormat.addInputPath(job, new Path(inputFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author gdchhkf@163.com
     * @Description 设置数据处理结果文件的输出路径
     * @Date 18:03 2019/3/3
     * @Param [job, outputTmp]
     * @return void
     **/
    private void setOutputPath(Job job, String outputTmp) {
        FileOutputFormat.setOutputPath(job, new Path(outputTmp));
    }

    /**
     * @Author gdchhkf@163.com
     * @Description 提交job
     * @Date 18:03 2019/3/3
     * @Param [job, outputDir, outputTmp, newFileName]
     * @return boolean
     **/
    private boolean submitJob(Job job, String outputDir, String outputTmp, String newFileName) {
        boolean result = false;
        try {
            //判断HDFS中输出目录是否存在, 存在则将其删除
            hdfsService.deleteDir(outputTmp);
            result = job.waitForCompletion(true);
            //迁移处理出来的结果文件
            hdfsService.renameFile(outputTmp, outputDir, newFileName);
        }catch (InterruptedException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
