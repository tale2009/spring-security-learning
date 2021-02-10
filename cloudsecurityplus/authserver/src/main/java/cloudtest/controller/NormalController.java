package cloudtest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class NormalController {

    @RequestMapping("/nornmalapi")
    public String nornmalApi()
    {
        return "this is normal Api";
    }
}
