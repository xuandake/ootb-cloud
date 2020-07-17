package com.ootb.auth.service.mpl;

import com.ootb.auth.service.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @ClassName UserDetailsServiceImpl
 * @Description TODO
 * @Author xdk
 * @Date 20-07-13 16:11
 * @Version 1.0
 **/
@Component
public class UserDetailsServiceImpl implements CustomUserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String name, String userType) throws UsernameNotFoundException {
        return null;
    }
}
