package com.itmayiedu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 跳转页面
 */
@Controller
public class UserController {

    @RequestMapping("/pageIndex")
    public String pageIndex(){
        System.out.println("访问/pageIndex接口");
        return "pageIndex";
    }
}
