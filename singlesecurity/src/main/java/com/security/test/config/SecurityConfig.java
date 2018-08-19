package com.security.test.config;


import com.security.test.authenticationprovider.CustomProvider;
import com.security.test.userdetailservice.CustomUserDetialService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();//禁止csrf
        http.formLogin();//使用表单登录
        http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/loginpage"));
        http.addFilter(customAuthenticationFilter());
        http.authorizeRequests().antMatchers("/home").permitAll();
        http.authorizeRequests().antMatchers("/**").authenticated();

    }

    public CustomUserDetialService customUserDetialService(){
        return new CustomUserDetialService();
    }

    private UsernamePasswordAuthenticationFilter customAuthenticationFilter()
    {
        UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter=new UsernamePasswordAuthenticationFilter();
        usernamePasswordAuthenticationFilter.setUsernameParameter("user");//设置校验前端用户名参数命名
        usernamePasswordAuthenticationFilter.setPasswordParameter("pwd");//设置校验前端密码参数命名
        usernamePasswordAuthenticationFilter.setAuthenticationManager(customAuthenticationManager());
        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/helloworld"));//成功Handler在后面分布式中还会继续分析
        usernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/fail"));
        usernamePasswordAuthenticationFilter.setFilterProcessesUrl("/loginTest");//校验
        return usernamePasswordAuthenticationFilter;
    }

    private AuthenticationProvider customProvider(){
        CustomProvider customProvider = new CustomProvider();
        customProvider.setUserDetailsService(customUserDetialService());
        return customProvider;
    }


    private AuthenticationManager customAuthenticationManager(){
        List<AuthenticationProvider> authenticationProviders=new ArrayList<AuthenticationProvider>();
        authenticationProviders.add(customProvider());
        return new ProviderManager(authenticationProviders);
    }
}
