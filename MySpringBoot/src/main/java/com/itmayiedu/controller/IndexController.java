package com.itmayiedu.controller;

import com.itmayiedu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //SpringMVC提供的注解
public class IndexController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/index",produces = "text/html;charset=utf-8")
    public String index(){
        return userService.index();
    }
}
