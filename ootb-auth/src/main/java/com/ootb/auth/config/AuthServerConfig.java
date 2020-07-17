package com.ootb.auth.config;

import com.ootb.auth.service.CustomTokenService;
import com.ootb.auth.service.CustomUserDetailsByNameServiceWrapper;
import com.ootb.auth.service.mpl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import com.common.core.util.RSAUtil;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * OAuth2 认证服务配置
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    //认证管理器
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    private UserDetailsService userDetailsService(){
        return userDetailsService;
    }


    @Bean
    protected JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(RSAUtil.getKeyPair());
        return converter;
    }

    @Bean
   public TokenStore jwtTokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    private CustomTokenService customTokenService(AuthorizationServerEndpointsConfigurer endpoints){
        CustomTokenService tokenService = new CustomTokenService();
        tokenService.setTokenStore(endpoints.getTokenStore());
        tokenService.setSupportRefreshToken(true);
        tokenService.setReuseRefreshToken(true);
        tokenService.setClientDetailsService(endpoints.getClientDetailsService());
        tokenService.setTokenEnhancer(endpoints.getTokenEnhancer());
        //设置自定义的CustomUserDetailsByNameService
        if(this.userDetailsService != null){
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new CustomUserDetailsByNameServiceWrapper(this.userDetailsService));
            tokenService.setAuthenticationManager(new ProviderManager(Arrays.<AuthenticationProvider>asList(provider)));
        }
        return tokenService;
    }

    /**
     *配置授权（authorization）以及令牌（token）
     * 的访问端点和令牌服务(token services)
     * */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception{
        endpoints.tokenStore(jwtTokenStore())
                .accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(this.authenticationManager)
                .reuseRefreshTokens(true)
                .userDetailsService(this.userDetailsService)
                .tokenServices(customTokenService(endpoints));
    }

    /**
     * @Description //配置令牌端点(Token Endpoint)的安全约束
     **/
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception{
        security
                //开启/oauth/token_key验证端口无权访问
                .tokenKeyAccess("permitAll")
                //开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("isAuthenticated")
                //主要是让/oauth/token支持client_id以及client_secret作登录认证
                .allowFormAuthenticationForClients();
    }
    /**
     * @Description 配置客户端详情信息 (这里是从数据库取)
     **/
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception{
        clients
                .jdbc(dataSource)
                .passwordEncoder(passwordEncoder);
    }

}
