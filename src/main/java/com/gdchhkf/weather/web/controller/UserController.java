package com.gdchhkf.weather.web.controller;

import com.gdchhkf.weather.web.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author gdchhkf@163.com
 * @version 1.0
 **/
@RestController
public class UserController {

    @Resource(name = "cache")
    private Map cache;

    @PostMapping("/signin")
    public User signIn(@RequestBody User vo) {
        Map<String,User> userMap = (Map<String, User>) cache.get("userMap");
        User user = userMap.get(vo.getUsername());
        if(user.getPassword().equals(vo.getPassword())) {
            return user;
        } else {
            return new User();
        }
    }

    @PostMapping("/signup")
    public User signUp(@RequestBody User vo) {
        Map<String,User> userMap = (Map<String, User>) cache.get("userMap");
        if(userMap.get(vo.getUsername()) == null) {
            User user = new User(vo.getUsername(), vo.getPassword(), true);
            userMap.put(vo.getUsername(), user);
            return user;
        } else {
            return new User();
        }
    }
}
