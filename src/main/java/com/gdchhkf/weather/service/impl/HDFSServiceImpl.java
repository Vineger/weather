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
import java.util.*;

/**
 * HDFS常用的操作集合
 * @author gdchhkf@163.com
 * @version 1.0
 **/
@Service
public class HDFSServiceImpl implements HDFSService {

    @Autowired
    private FileSystem fileSystem;
    @Autowired
    private TimeUtils utils;
    
    /**
     * 获取分析过去一周气象数据文件的结果
     * @author gdchhkf@163.com
     * @param week
     * @return void
     **/
    public void setLastWeekResult (WeatherWeek week) {
        //组装结果文件路径
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu_mm_dd");
        String fileName = utils.getLastMonday().format(formatter);
        String file = "/weather/week/" + fileName;
        //调用 readFile() 获取文件中的信息
        String temp = readFile(file);

        //对文件信息进行处理, 封装
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
     *
     * @author gdchhkf@163.com
     * @return void
     **/
    public List<Map> readLastWeekFiles () {
        List<String> files = utils.getExistsPastWeekFile();
        List<Map> weatherList = new ArrayList<>();
        Map weather = new HashMap();

        for (String file : files) {
            String temp = readFile(file);
            StringTokenizer tokenizer = new StringTokenizer(temp);
            while (tokenizer.hasMoreTokens()) {
                String key = tokenizer.nextToken();
                String value = tokenizer.nextToken();
                weather.put(key, value);
            }
            weatherList.add(weather);
        }
        return weatherList;
    }

    /**
     * @author gdchhkf@163.com
     * @param week
     * @return void
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
     * @author gdchhkf@163.com
     * @param week
     * @return void
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
     * @author gdchhkf@163.com
     * @param week
     * @return void
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
     * @author gdchhkf@163.com
     * @param week
     * @return void
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
     * @author gdchhkf@163.com
     * @param week
     * @return void
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
     * @author gdchhkf@163.com
     * @param week
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
     * @author gdchhkf@163.com
     * @param week
     * @return void
     **/
    private void closePrintWriter(PrintWriter writer){
        if(writer != null){
            writer.close();
        }
    }
}
