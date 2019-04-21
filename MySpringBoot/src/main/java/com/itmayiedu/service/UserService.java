package com.itmayiedu.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    public String index(){
        System.out.println("调用UserService的index方法");
        return "我是SpringBoot2.0  正在加载UserService...";
    }
}
