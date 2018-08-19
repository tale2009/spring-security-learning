package com.security.test.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Stephanie on 2018/8/17.
 */
@RestController
public class SecurityController {
    @RequestMapping("/home")
    public String home(){
        return "home";
    }

    @RequestMapping("/loginpage")
    public String loginpage(){
        return "loginpage";
    }

    @RequestMapping("/helloworld")
    public String helloworld(){
        return "helloworld";
    }

    @RequestMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String user(){
        return "user";
    }

    @RequestMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin(){
        return "admin";
    }

    @RequestMapping("/fail")
    public String fail(){
        return "fail";
    }

    @RequestMapping("/authenticattest")
    public String authenticattest(){
        return "authenticattest";
    }


}
