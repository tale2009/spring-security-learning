package com.security.cloudtest.config;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;


@Configuration
@EnableOAuth2Sso
public class ZuulConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    ResourceServerProperties resourceServerProperties;//资源服务器配置

    @Value("${security.oauth2.client.clientId}")
    String clientId;

    @Autowired
    private OAuth2RestTemplate oauth2RestTemplate;

    @Bean
    @LoadBalanced
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
        return new OAuth2RestTemplate(resource, context);
    }

    /**
     * 定制我们自己的oauth2客户端拦截器器
     * 我们只对/api/**的范文进行鉴权
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.addFilterAfter(createoauth2ClientAuthenticationProcessingFilter(),
                AbstractPreAuthenticatedProcessingFilter.class);
        http.authorizeRequests().antMatchers("/api/**").
                access("isAuthenticated()");
    }

    /**
     * 避免个性化配置初始BEAN循环
     * @return
     */
    @Bean
    OAuth2ClientAuthenticationProcessingFilter createoauth2ClientAuthenticationProcessingFilter() {
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(
                "/api/sso");
        UserInfoTokenServices userInfoTokenServices = new UserInfoTokenServices(resourceServerProperties.getUserInfoUri(),
                clientId);
        userInfoTokenServices.setRestTemplate(oauth2RestTemplate);
        filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("http://localcloudoauth:8080/oauthserver/helloworld"));
        filter.setTokenServices(userInfoTokenServices);
        filter.setRestTemplate(oauth2RestTemplate);
        return filter;
    }
}