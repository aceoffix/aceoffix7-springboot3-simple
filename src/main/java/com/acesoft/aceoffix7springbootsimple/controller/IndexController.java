package com.acesoft.aceoffix7springbootsimple.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class IndexController {
    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        return mv;
    }
}
