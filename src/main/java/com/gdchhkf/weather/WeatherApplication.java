package com.gdchhkf.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WeatherApplication {

    public static void main(String[] args) {
        System.setProperty("HADOOP_USER_NAME", "root");
        SpringApplication.run(WeatherApplication.class, args);
    }

}
