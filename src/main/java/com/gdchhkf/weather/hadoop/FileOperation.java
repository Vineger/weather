package com.gdchhkf.weather.hadoop;

import com.gdchhkf.weather.web.domain.Weather;
import com.gdchhkf.weather.hadoop.configuration.HadoopConfiguration;
import com.gdchhkf.weather.utils.TimeUtils;
import com.gdchhkf.weather.web.domain.vo.WeatherWeek;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class FileOperation {

    public static final String DAY = "/weather/day/";
    public static final String DAT_TMP = "/weather/day/tmp/";
    public static final String WEEK = "/weather/week/";
    public static final String WEEK_TMP = "/weather/week/tmp/";
    public static final String MONTH = "/weather/month/";
    public static final String MONTH_TMP = "/weather/month/tmp/";
    public static final String HOUR = "/weather/hour/";

    public List<String> getExistsFile(FileType type) {
        List<String> files = new ArrayList<>();
        List<String> dates = null;

        if(type == FileType.WEEK) {
            dates = TimeUtils.getLastWeek();
        } else if (type == FileType.MONTH) {
            dates = TimeUtils.getLastMonth();
        }

        for (String fileName : dates) {
            String file = DAY + fileName;
            if(exists(file)) {
                files.add(file);
            }
        }

        return files;
    }

    public void setLastWeekResult (WeatherWeek week) {
        //获得MapReduce 统计出来的数据
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeUtils.DAY);
        String fileName = TimeUtils.getLastMonday().format(formatter);
        String file = WEEK + fileName;
        String temp = readFile(file);
        
        StringTokenizer tokenizer = new StringTokenizer(temp);
        while (tokenizer.hasMoreTokens()) {
            String key = tokenizer.nextToken();
            String value = tokenizer.nextToken();

            // 通过判断键的类型, 设置WeatherWeek的属性
            switch (key) {
                case Weather.TEM_MAX:
                    week.setTemMax(Float.parseFloat(value));
                    break;
                case Weather.TEM_MIN:
                    week.setTemMin(Float.parseFloat(value));
                    break;
                case Weather.PRE:
                    week.setPre(Float.parseFloat(value));
                    break;
            }
        }
    }
    

    public List<Map<String, String>> readFiles(FileType type) {
        List<String> files = getExistsFile(type);
        List<Map<String, String>> weatherList = new ArrayList<>();

        for (String file : files) {
            Map<String, String> weather = new HashMap<>();
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


    private boolean exists(String file) {
        boolean result = false;

        try (FileSystem fileSystem = HadoopConfiguration.getFileSystem())
        {
            result = fileSystem.exists(new Path(file));
        } catch(IOException e){
            e.printStackTrace();
        }

        return result;
    }

    private String readFile(String fileName){
        String result = "";
        try (FileSystem fileSystem = HadoopConfiguration.getFileSystem()){
            FSDataInputStream input = fileSystem.open(new Path(fileName));
            result = IOUtils.toString(input, "UTF-8");
        } catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }

    public void appendFile(String filePath, String content) {
        Path path = new Path(filePath);
        createFile(path);

        try (
                FileSystem fileSystem = HadoopConfiguration.getFileSystem();
                FSDataOutputStream outputStream = fileSystem.append(path);
                PrintWriter writer = new PrintWriter(outputStream)
        ){
            writer.append(content);
            writer.flush();
            outputStream.hflush();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void renameFile(String srcDir, String desDir, String newFileName){
        try (FileSystem fileSystem = HadoopConfiguration.getFileSystem()){
            fileSystem.rename(
                    new Path(srcDir + "part-r-00000"),
                    new Path(desDir + newFileName));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void deleteDir(String dir) {
        try (FileSystem fileSystem = HadoopConfiguration.getFileSystem()){
            fileSystem.delete(new Path(dir), true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void createFile(Path path){
        try (FileSystem fileSystem = HadoopConfiguration.getFileSystem()) {
            if(!fileSystem.exists(path)){
                fileSystem.createNewFile(path);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
