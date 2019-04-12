package com.gdchhkf.weather.hadoop.configuration;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;


import java.io.IOException;


public class HadoopConfiguration {

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

    public static FileSystem getFileSystem() throws IOException{
        Configuration conf = getConfiguration();
        return FileSystem.get(conf);
    }
}
