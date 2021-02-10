package com.security.cloudtest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApioneController {

    @RequestMapping("/api/one/test")
    public String test()
    {
        return "test";
    }


}
