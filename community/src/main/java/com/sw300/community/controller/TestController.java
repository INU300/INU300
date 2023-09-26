package com.sw300.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test")/*서버구동 테스트입니다.*/
    public String test(){
        return "test";
    }
}
