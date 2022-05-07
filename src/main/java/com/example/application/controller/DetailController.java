package com.example.application.controller;

import com.example.application.service.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailController {

    private final DetailService detailService;

    @Autowired
    public DetailController(DetailService detailService) {
        this.detailService = detailService;
    }

    @GetMapping("/user/detail/{page}")
    public String getAllDetails(Model model, @PathVariable int page){
        model.addAttribute("details", detailService.getAllDetails(page));

        model.addAttribute("page", page);
        model.addAttribute("maxPages", detailService.getMaxPages());

        return "details/index";
    }
}
