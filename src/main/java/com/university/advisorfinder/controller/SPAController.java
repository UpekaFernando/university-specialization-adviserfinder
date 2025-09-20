package com.university.advisorfinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SPAController {
    
    // This handles React Router paths - forwards them to index.html
    @RequestMapping(value = {
        "/register",
        "/browse-advisors",
        "/advisor-profile/**",
        "/student-dashboard",
        "/lecturer-dashboard",
        "/about",
        "/contact"
    })
    public String spa() {
        return "forward:/index.html";
    }
}
