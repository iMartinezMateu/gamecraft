package com.gamecraft.gamecraftui.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {

    @RequestMapping(value="/",method = RequestMethod.GET)
    public String home(){
        return "login";
    }

    @RequestMapping(value="/dashboard",method = RequestMethod.GET)
    public String dashboard(){
        return "dashboard";
    }

}