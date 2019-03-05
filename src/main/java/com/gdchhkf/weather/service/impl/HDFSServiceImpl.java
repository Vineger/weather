package com.gdchhkf.weather.service.impl;

import com.gdchhkf.weather.domain.Weather;
import com.gdchhkf.weather.domain.vo.WeatherWeek;
import com.gdchhkf.weather.service.HDFSService;
import com.gdchhkf.weather.service.utils.TimeUtils;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @ClassName HDFSServiceImpl
 * @Description HDFS操作集合
 * @Author gdchhkf@163.com
 * @Date 2019/2/28 15:05
 * @Version 1.0
 **/
@Service
public class HDFSServiceImpl implements HDFSService {

    @Autowired
    private FileSystem fileSystem;
    @Autowired
    private TimeUtils utils;
    
    /**
     * @Author gdchhkf@163.com
     * @Description
     * @Date 13:09 2019/3/4
     * @Param [week]
     * @return void
     **/
    public void setLastWeekResult (WeatherWeek week) {
        //获得MapReduce 统计出来的数据
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu_mm_dd");
        String fileName = utils.getLastMonday().format(formatter);
        String file = "/weather/week/" + fileName;
        String temp = readFile(file);
        
        StringTokenizer tokenizer = new StringTokenizer(temp);
        while (tokenizer.hasMoreTokens()) {
            String key = tokenizer.nextToken();
            String value = tokenizer.nextToken();
            
            // 通过判断键的类型, 设置WeatherWeek的属性
            if(key.equals(Weather.TEM_MAX)) {
                week.setTemMax(Float.parseFloat(value));
            } else if (key.equals(Weather.TEM_MIN)) {
                week.setTemMin(Float.parseFloat(value));
            } else if (key.equals(Weather.PRE)) {
                week.setPre(Float.parseFloat(value));
            }
        }
    }
    
    /**
     * @Author gdchhkf@163.com
     * @Description 
     * @Date 12:35 2019/3/4
     * @Param []
     * @return java.util.Map
     **/
    public Map readLastWeekFiles () {
        List<String> files = utils.getExistsPastWeekFile();
        Map weatherMap = new HashMap();

        for (String file : files) {
            String temp = readFile(file);
            StringTokenizer tokenizer = new StringTokenizer(temp);
            while (tokenizer.hasMoreTokens()) {
                String key = tokenizer.nextToken();
                String value = tokenizer.nextToken();
                weatherMap.put(key, value);
            }
        }
        return weatherMap;
    }

    /**
     * @Author gdchhkf@163.com
     * @Description
     * @Date 12:35 2019/3/4
     * @Param [file]
     * @return boolean
     **/
    public boolean exists(String file) {
        boolean result = false;

        try {
            result = fileSystem.exists(new Path(file));
        } catch(IOException e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @Author gdchhkf@163.com
     * @Description
     * @Date 15:21 2019/3/3
     * @Param [fileName]
     * @return java.lang.String
     **/
    public String readFile(String fileName){
        String result = "";
        try {
            FSDataInputStream input = fileSystem.open(new Path(fileName));
            result = IOUtils.toString(input, "UTF-8");
        } catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }


    /**
     * @Author gdchhkf@163.com
     * @Description
     *      往HDFS中写文件的辅助方法. 内容写入为追加的形式.
     *      文件不存在则创建文件.
     * @Date 16:18 2019/2/28
     * @Param [filePath, content]
     * @return boolean
     **/
    public boolean appendFile(String filePath, String content) {
        Path path = new Path(filePath);
        FSDataOutputStream outputStream = null;
        PrintWriter writer = null;

        try{
            if(!fileSystem.exists(path)){
                fileSystem.createNewFile(path);
            }

            outputStream =  fileSystem.append(path);
            writer = new PrintWriter(outputStream);
            writer.append(content);
            writer.flush();
            outputStream.hflush();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            closePrintWriter(writer);
            closeFSDataOutPutStream(outputStream);
        }


        return false;
    }


    /**
     * @Author gdchhkf@163.com
     * @Description 移动暂存的气象数据文件到其他位置, 并修改其名字
     * @Date 15:53 2019/3/2
     * @Param
     * @return
     **/
    public boolean renameFile(String srcDir, String desDir, String newFileName){
        boolean result = false;
        try {
            result = fileSystem.rename(
                    new Path(srcDir + "part-r-00000"),
                    new Path(desDir + newFileName));
        } catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @Author gdchhkf@163.com
     * @Description 递归删除HDFS的目录及其文件
     * @Date 14:19 2019/3/3
     * @Param [dir]
     * @return boolean
     **/
    public boolean deleteDir(String dir) {
        boolean result = false;
        try {
            result = fileSystem.delete(new Path(dir), true);
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @Author gdchhkf@163.com
     * @Description 关闭FSDataOutputStream的辅助方法
     * @Date 16:10 2019/2/28
     * @Param [outputStream]
     * @return void
     **/
    private void closeFSDataOutPutStream(FSDataOutputStream outputStream){
        if(outputStream != null){
            try{
                outputStream.close();
            }catch (IOException e){

            }
        }
    }

    /**
     * @Author gdchhkf@163.com
     * @Description 关闭PrintWriter的辅助方法
     * @Date 16:17 2019/2/28
     * @Param [writer]
     * @return void
     **/
    private void closePrintWriter(PrintWriter writer){
        if(writer != null){
            writer.close();
        }
    }
}
