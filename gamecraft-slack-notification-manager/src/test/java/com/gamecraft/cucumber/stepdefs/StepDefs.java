package com.gamecraft.cucumber.stepdefs;

import com.gamecraft.GamecraftslacknotificationmanagerApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = GamecraftslacknotificationmanagerApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
