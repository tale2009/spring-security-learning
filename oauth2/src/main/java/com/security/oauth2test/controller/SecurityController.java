package com.security.oauth2test.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Stephanie on 2018/8/17.
 */
@Controller
public class SecurityController {
    @ResponseBody
    @RequestMapping("/home")
    public String home(){
        return "home";
    }

    @ResponseBody
    @RequestMapping("/loginpage")
    public String loginpage(){
        return "loginpage";
    }

    @ResponseBody
    @RequestMapping("/helloworld")
    public String helloworld(){
        return "helloworld";
    }

    @ResponseBody
    @RequestMapping("/user")
    public String user(){
        return "user";
    }

    @ResponseBody
    @RequestMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin(){
        return "admin";
    }

    @RequestMapping("/fail")
    public String fail(){
        return "fail";
    }
    @ResponseBody
    @RequestMapping("/authenticattest")
    public String authenticattest(){
        return "authenticattest";
    }
    @ResponseBody
    @RequestMapping("/oauthapi/oauthtest")
    public String oauthtest(){
        return "oauthtest";
    }


    @GetMapping("/loginpage")
    public String html() {
        return "/login";
    }
}
