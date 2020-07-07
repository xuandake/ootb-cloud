package com.ootb.gateway.handler;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;

/**
 * @ClassName GatewayConfiguration
 * @Description 重写限流响应， 改造成JSON格式的响应数据
 * @Author xdk
 * @Date 20-07-07 18:17
 * @Version 1.0
 **/

public class GatewayBlockExceptionHandler extends DefaultErrorWebExceptionHandler {

    public GatewayBlockExceptionHandler(ErrorAttributes attributes, ResourceProperties properties,
                                        ErrorProperties errorProperties, ApplicationContext applicationContext){
        super(attributes, properties, errorProperties, applicationContext);
    }


}
