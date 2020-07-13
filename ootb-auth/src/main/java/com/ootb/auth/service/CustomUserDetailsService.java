package com.ootb.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @ClassName CustomUserDetailsService
 * @Description 继承 UserDetailsService 自定义法方法
 * @Author xdk
 * @Date 20-07-13 16:06
 * @Version 1.0
 **/

public interface CustomUserDetailsService extends UserDetailsService {
    //UserDetails loadUserByUserName(String userName, String userType) throws UsernameNotFoundException;

    UserDetails loadUserByUsername(String name, String userType) throws UsernameNotFoundException;;
}
