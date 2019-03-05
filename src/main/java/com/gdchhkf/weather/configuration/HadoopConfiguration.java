package com.gdchhkf.weather.configuration;

import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @ClassName HadoopConfiguration
 * @Description TODO
 * @Author gdchhkf@163.com
 * @Date 2019/3/2 11:21
 * @Version 1.0
 **/
@Configuration
public class HadoopConfiguration {

    @Value("${hdfs.url}")
    private String defaultFS;

    /**
     * @Author gdchhkf@163.com
     * @Description 配置Configuration
     * @Date 14:16 2019/3/3
     * @Param []
     * @return org.apache.hadoop.conf.Configuration
     **/
    @Bean("configuration")
    public org.apache.hadoop.conf.Configuration getConfiguration(){
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set("fs.defaultFS", defaultFS);
        conf.setBoolean("dfs.support.append", true);
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        conf.setBoolean("dfs.client.block.write.replace-data node-on-failure.enable", true);
        return conf;
    }

    /**
     * @Author gdchhkf@163.com
     * @Description 配置FileSystem
     * @Date 14:15 2019/3/3
     * @Param []
     * @return org.apache.hadoop.fs.FileSystem
     **/
    @Bean("fileSystem")
    public FileSystem getFileSystem() {
        org.apache.hadoop.conf.Configuration conf = getConfiguration();
        FileSystem fileSystem = null;
        try{
            fileSystem = FileSystem.get(conf);
        }catch (IOException e){
            e.printStackTrace();
        }
        return fileSystem;
    }
}
