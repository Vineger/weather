package com.gdchhkf.weather.hadoop;

import com.gdchhkf.weather.hadoop.configuration.HadoopConfiguration;
import com.gdchhkf.weather.hadoop.mapper.WeatherDayMapper;
import com.gdchhkf.weather.hadoop.mapper.WeatherWeekMapper;
import com.gdchhkf.weather.hadoop.reducer.WeatherDayReducer;
import com.gdchhkf.weather.hadoop.reducer.WeatherWeekReducer;
import com.gdchhkf.weather.utils.TimeUtils;
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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class MapReduceOperation {

    @Autowired
    private FileOperation fileOperation;

    private Configuration conf = HadoopConfiguration.getConfiguration();

    /**
     * 创建 DayJob 分析过去24小时的气象数据
     * 具体的Mapper逻辑 {@link WeatherDayMapper}
     * 具体的Reducer逻辑 {@link WeatherWeekReducer}
     * @author gdchhkf@163.com
     * @param  srcFileName 要处理的hour文件的文件名
     * @return boolean 任务创建并提交是否成功
     **/
    public boolean createWeatherDayJob(String srcFileName) {

        Job job = getInstance("analysis weather day");
        setJob(job, WeatherDayMapper.class, WeatherDayReducer.class);
        setSingleInput(job, FileOperation.HOUR + srcFileName);
        setOutputPath(job, FileOperation.DAT_TMP);
        return submitJob(job, FileOperation.DAY, FileOperation.DAT_TMP, srcFileName);

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
        String newFileName = today.format(DateTimeFormatter.ofPattern(TimeUtils.DAY));

        Job job = getInstance("analysis weather week");
        setJob(job, WeatherWeekMapper.class, WeatherWeekReducer.class);
        setMultipleInputs(job, files);
        setOutputPath(job, FileOperation.WEEK_TMP);
        return submitJob(job, FileOperation.WEEK, FileOperation.WEEK_TMP, newFileName);
    }


    /**
     *
     * @author gdchhkf@163.com
     * @param files
     * @return boolean
     **/
    public boolean createWeatherMonthJob(List<String> files) {
        LocalDate today = LocalDate.now();
        String newFileName = today.format(DateTimeFormatter.ofPattern(TimeUtils.MONTH));

        Job job = getInstance("analysis weather month");
        boolean result;
        // 由于 Month 和 Day 的处理逻辑相同
        // 所以使用相同的 Mapper 和 Reducer
        setJob(job, WeatherWeekMapper.class, WeatherWeekReducer.class);
        setMultipleInputs(job, files);
        setOutputPath(job, FileOperation.MONTH_TMP);
        result = submitJob(job, FileOperation.MONTH, FileOperation.MONTH_TMP, newFileName);
        return result;
    }


    private Job getInstance(String jobName) {
        Job job = null;
        try {
            job = Job.getInstance(conf, jobName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return job;
    }

    private void setJob(Job job, Class<? extends Mapper> mapper, Class<? extends Reducer> reducer) {

        job.setJarByClass(this.getClass());
        job.setMapperClass(mapper);
        job.setCombinerClass(reducer);
        job.setReducerClass(reducer);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);
    }

    private void setMultipleInputs(Job job, List<String> files) {
        for(String file : files){
            MultipleInputs.addInputPath(job, new Path(file), FileInputFormat.class);
        }
    }

    private void setSingleInput(Job job, String inputFile) {
        try {
            FileInputFormat.addInputPath(job, new Path(inputFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setOutputPath(Job job, String outputTmp) {
        FileOutputFormat.setOutputPath(job, new Path(outputTmp));
    }

    /**
     *
     * @param job MapReduce中每一个提交的任务所对应的实例
     * @param outputDir 处理后的数据结果真正存放的位置
     * @param outputTmp 由于MapReduce的特性，处理后的数据暂存的位置
     * @param newFileName 处理后的数据存放在 outputDir 中真正的文件名
     * @return
     *   true 代表任务提交成功
     *   false 代表任务提交失败
     */
    private boolean submitJob(Job job, String outputDir, String outputTmp, String newFileName) {
        boolean result = false;
        try {
            //判断HDFS中输出目录是否存在, 存在则将其删除
            fileOperation.deleteDir(outputTmp);
            result = job.waitForCompletion(true);
            //迁移处理出来的结果文件
            fileOperation.renameFile(outputTmp, outputDir, newFileName);
        }catch (InterruptedException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
