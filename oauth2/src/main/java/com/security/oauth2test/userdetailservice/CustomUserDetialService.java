package com.security.oauth2test.userdetailservice;

import com.security.oauth2test.userdetail.CustomUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetialService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> userRole=new ArrayList();
        if("stephanie".equals(username))
        {
            CustomUser customUser=new CustomUser("stephanie","12345678");
            userRole.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            customUser.setAuthorities(userRole);
            return customUser;
        }
        else
        {
            CustomUser customUser=new CustomUser("normaluser","12345678");
            userRole.add(new SimpleGrantedAuthority("ROLE_USER"));
            customUser.setAuthorities(userRole);
            return customUser;
        }
    }
}
