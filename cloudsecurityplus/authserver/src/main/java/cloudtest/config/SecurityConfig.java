package cloudtest.config;

import cloudtest.authenticationprovider.CustomProvider;
import cloudtest.userdetailservice.CustomUserDetialService;
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
import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.SessionRepositoryFilter;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();//禁止csrf
        http.formLogin();//使用表单登录
        http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("http://localcloudoauth:8080/oauthserver/loginpage"));
        http.addFilter(customAuthenticationFilter());
        http.authorizeRequests().antMatchers("/home","/oauth/**","/health","/loginpage","/userinfo").permitAll();
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
        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("http://localcloudoauth:8080/api/sso"));//成功Handler在后面分布式中还会继续分析
        usernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/authserver/fail"));
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

    /**
     * 鉴权服务的cookice不会影响其他服务
     * @return
     */
    @Bean
    public CookieSerializer httpSessionIdResolver() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setCookiePath("/oauthserver");
        cookieSerializer.setCookieName("oauthsession");
        return cookieSerializer;
    }

    @Bean
    public <S extends ExpiringSession> SessionRepositoryFilter<? extends ExpiringSession> springSessionRepositoryFilter(
            SessionRepository<S> sessionRepository, ServletContext servletContext) {
        SessionRepositoryFilter<S> sessionRepositoryFilter = new SessionRepositoryFilter<S>(sessionRepository);
        sessionRepositoryFilter.setServletContext(servletContext);
        CookieHttpSessionStrategy httpSessionStrategy = new CookieHttpSessionStrategy();
        httpSessionStrategy.setCookieSerializer(httpSessionIdResolver());
        sessionRepositoryFilter.setHttpSessionStrategy(httpSessionStrategy);
        return sessionRepositoryFilter;
    }


}

