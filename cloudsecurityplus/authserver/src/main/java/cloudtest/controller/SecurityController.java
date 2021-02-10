package cloudtest.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class SecurityController {
    @ResponseBody
    @RequestMapping("/home")
    public String home(){
        return "home";
    }


    @ResponseBody
    @RequestMapping("/helloworld")
    public String helloworld(){
        return "helloworld";
    }

    @ResponseBody
    @RequestMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String user(){
        return "user";
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


    @RequestMapping("/loginpage")
    public String html() {
        return "/login";
    }
}
