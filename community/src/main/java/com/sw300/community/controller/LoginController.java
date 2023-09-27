package com.sw300.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {



    @GetMapping("/")
    public String mainLogin(){
        return "mainLogin";
    }

    @GetMapping("/join")
    public String join(){
        return "joinPage";
    }

    @GetMapping("/home")
    public String home(){
        return "home";
    }


}
