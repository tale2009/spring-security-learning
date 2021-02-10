package cloudtest.controller;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ResourceController {
    @RequestMapping(value ="/userinfo", method = RequestMethod.GET)
    public Principal user(Principal principal) {
        return principal;
    }
}
