package com.gdchhkf.weather.configuration;

import com.gdchhkf.weather.domain.Weather;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CacheConfiguration
 * @Description TODO
 * @Author gdchhkf@163.com
 * @Date 2019/3/3 21:41
 * @Version 1.0
 **/
@Configuration
public class CacheConfiguration {

    @Bean(name = "cache")
    public Map<String, Weather> cache(){
        return new HashMap<>();
    }
}
