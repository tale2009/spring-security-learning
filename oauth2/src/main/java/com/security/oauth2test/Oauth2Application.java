package com.security.oauth2test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * Created by Stephanie on 2018/8/17.
 */
@SpringBootApplication
public class Oauth2Application {
    public static void main(String args[])
    {
        SpringApplication.run(Oauth2Application.class,args);
    }


    @Configuration
    @EnableResourceServer
    public static class ResourceServer extends ResourceServerConfigurerAdapter {

        //设置token取的相关信息的url
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/user").authorizeRequests().anyRequest().authenticated();
            http.antMatcher("/oauthapi/**").authorizeRequests().anyRequest().authenticated();
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId("oauth2-resource");
        }

    }

}
