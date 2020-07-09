package com.ootb.gateway.config;

import com.ootb.gateway.handler.GatewayBlockExceptionHandler;
import com.ootb.gateway.handler.SentinelExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.List;

/**
 * @ClassName GatewayConfiguration
 * @Description 网关配置
 * @Author xdk
 * @Date 20-07-07 18:17
 * @Version 1.0
 **/

@Configuration
@RequiredArgsConstructor
public class GatewayConfiguration {

    private final ServerProperties serverProperties;
    private final ResourceProperties resourceProperties;
    private final ApplicationContext applicationContext;
    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    /**
     * @Author xdk
     * @Description //自定义限流异常配置
     * @Date 14:19 20-07-09
     * @Param []
     * @return com.ootb.gateway.handler.SentinelExceptionHandler
     **/
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelExceptionHandler sentinelExceptionHandler(){
        return new SentinelExceptionHandler();
    }

    /**
     * @Author xdk
     * @Description //配置自定义异常
     * @Date 12:35 20-07-08
     * @Param [errorAttributes]
     * @return org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
     **/
    @Bean
    @ConditionalOnMissingBean(value = ErrorWebExceptionHandler.class, search = SearchStrategy.CURRENT)
    @Order(-1)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes) {
        GatewayBlockExceptionHandler exceptionHandler = new GatewayBlockExceptionHandler(
                errorAttributes,
                resourceProperties,
                this.serverProperties.getError(),
                applicationContext);
        exceptionHandler.setViewResolvers(this.viewResolvers);
        exceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }

}
