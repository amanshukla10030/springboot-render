package com.happyghumakkads.controller;

import com.happyghumakkads.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private PackageService packageService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("featuredPackages", packageService.getActivePackages());
        return "index";
    }
}
