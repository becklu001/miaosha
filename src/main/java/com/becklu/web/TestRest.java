package com.becklu.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRest {

    @RequestMapping("/testRest")
    public String getRest(){
        return "test rest!!!";
    }
}
