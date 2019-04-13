package com.gdchhkf.weather.web.configuration;

import com.gdchhkf.weather.web.domain.User;
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
    public Map cache(){
        Map<String, User> userMap = new HashMap<>();
        Map cache = new HashMap();
        User user = new User("a123456", "123456", true);
        userMap.put(user.getUsername(), user);
        cache.put("userMap", userMap);
        return cache;
    }
}
