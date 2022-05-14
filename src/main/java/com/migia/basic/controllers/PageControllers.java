package com.migia.basic.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageControllers {

    @GetMapping("/home")
    public ModelAndView home(){
        var model = new ModelAndView();
        model.setViewName("home");
        model.addObject("name", "Stephen");

        return model;
    }
}

