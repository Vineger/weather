package com.gdchhkf.weather.hadoop.configuration;

import lombok.extern.java.Log;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;


import java.io.IOException;

@Log
public class HadoopConfiguration {

    private static FileSystem fileSystem;

    public static Configuration getConfiguration(){
        Configuration conf = new org.apache.hadoop.conf.Configuration();
        // 虚拟机地址：hdfs://192.168.254.100:9000
        // 云服务地址：hdfs://172.17.21.51:9000
        conf.set("fs.defaultFS", "hdfs://172.17.21.51:9000");
        conf.setBoolean("dfs.support.append", true);
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        conf.setBoolean("dfs.client.block.write.replace-data node-on-failure.enable", true);
        return conf;
    }

    public static FileSystem getFileSystem(){
        Configuration conf = getConfiguration();
        if(fileSystem == null) {
            try {
                fileSystem = FileSystem.get(conf);
            } catch (IOException e) {
                log.warning("与Hadoop连接发送错误");
                e.printStackTrace();
            }
        } else {
            return fileSystem;
        }
        return fileSystem;
    }
}
