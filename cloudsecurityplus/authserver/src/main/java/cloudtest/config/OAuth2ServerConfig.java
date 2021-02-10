package cloudtest.config;

import cloudtest.authenticationprovider.CustomProvider;
import cloudtest.userdetailservice.CustomUserDetialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAuthorizationServer //提供/oauth/authorize,/oauth/token,/oauth/check_token,/oauth/confirm_access,/oauth/error
public class OAuth2ServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    public CustomUserDetialService customUserDetialService(){
        return new CustomUserDetialService();
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

    @Bean
    public RedisTokenStore redisTokenStore()
    {
        return new RedisTokenStore(redisTemplate.getConnectionFactory());
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(customAuthenticationManager());
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST);
        endpoints.tokenStore(redisTokenStore());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()//数据存在内存中
                .withClient("demoApp")//授权服务器id
                .secret("demoAppSecret")//授权密码
                .authorizedGrantTypes("authorization_code", "password", "refresh_token")//获取模式
                .scopes("all")
                .resourceIds("resourece")//资源服务器id
                .accessTokenValiditySeconds(1200)//token的存在时间
                .autoApprove("all")
                .refreshTokenValiditySeconds(50000);//刷新token的token的存在时间
    }
}

