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

    @RequestMapping(value="/users",method = RequestMethod.GET)
    public String users(){
        return "users/index";
    }

    @RequestMapping(value="/teams",method = RequestMethod.GET)
    public String teams(){
        return "teams/index";
    }

    @RequestMapping(value="/projects",method = RequestMethod.GET)
    public String projects(){
        return "projects/index";
    }

    @RequestMapping(value="/pipelines",method = RequestMethod.GET)
    public String pipelines(){
        return "pipelines/index";
    }

    @RequestMapping(value="/engines",method = RequestMethod.GET)
    public String engines(){
        return "engines/index";
    }

    @RequestMapping(value="/notifications",method = RequestMethod.GET)
    public String notifications(){
        return "notifications/index";
    }

    @RequestMapping(value="/settings",method = RequestMethod.GET)
    public String settings(){
        return "settings";
    }
}