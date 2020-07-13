package com.ootb.auth.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @ClassName CustomUserDetailsByNameServiceWrapper
 * @Description TODO
 * @Author xdk
 * @Date 20-07-13 17:52
 * @Version 1.0
 **/

public class CustomUserDetailsByNameServiceWrapper <T extends Authentication> implements AuthenticationUserDetailsService<T>, InitializingBean {
    private  CustomUserDetailsService userDetailsService = null;

    public CustomUserDetailsByNameServiceWrapper() {
    }

    public CustomUserDetailsByNameServiceWrapper(CustomUserDetailsService userDetailsService) {
        Assert.notNull(userDetailsService, "userDetailsService cannot be null.");
        this.userDetailsService = userDetailsService;
    }

    public void afterPropertiesSet() {
        Assert.notNull(this.userDetailsService, "UserDetailsService must be set");
    }

    public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
        AbstractAuthenticationToken principal = (AbstractAuthenticationToken) authentication.getPrincipal();
        Map<String, String> map = (Map<String , String>)principal.getDetails();
        String userType = map.get("userType");
        return this.userDetailsService.loadUserByUsername(authentication.getName(), userType);
    }

    public void setUserDetailsService(CustomUserDetailsService aUserDetailsService) {
        this.userDetailsService = aUserDetailsService;
    }
}
