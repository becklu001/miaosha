package com.becklu.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestCon {

    @RequestMapping("/testPage")
    public String test(){
        return "templates-page";
    }

}
