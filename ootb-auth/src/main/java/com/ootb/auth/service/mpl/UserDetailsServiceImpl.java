package com.ootb.auth.service.mpl;

import com.ootb.auth.service.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @ClassName UserDetailsServiceImpl
 * @Description TODO
 * @Author xdk
 * @Date 20-07-13 16:11
 * @Version 1.0
 **/

public class UserDetailsServiceImpl implements CustomUserDetailsService {

    @Override
    public UserDetails loadUserByUserName(String userName, String userType) throws UsernameNotFoundException {
        //1、通过类型获取用户
        //2、获取用户可访问权限信息
        //3、构造UserDetails信息并返回
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}
