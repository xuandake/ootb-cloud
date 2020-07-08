package com.ootb.gateway.handler;

import com.common.core.constant.HttpCode;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName GatewayConfiguration
 * @Description 重写限流响应， 改造成JSON格式的响应数据
 * @Author xdk
 * @Date 20-07-07 18:17
 * @Version 1.0
 **/

public class GatewayBlockExceptionHandler extends DefaultErrorWebExceptionHandler {

    public GatewayBlockExceptionHandler(ErrorAttributes errorAttributes,
                                        ResourceProperties resourceProperties,
                                        ErrorProperties errorProperties,
                                        ApplicationContext applicationContext){
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    /**
     * @Author xdk
     * @Description 异常类型定制化处理
     * @Date 11:00 20-07-08
     * @Param [request, includeStackTrace]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @Override
    protected  Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace){
        Throwable tableRow = super.getError(request);
        Map<String, Object> errorAttributes = new HashMap<String, Object>(8);
        String msg = tableRow.getMessage();
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

        switch (statusCode){
            case HttpCode.BAD_REQUEST:
                msg = "请求参数有误";
                break;
            case HttpCode.UNAUTHORIZED:
                msg = "当前请求用户未被授权";
                break;
            case HttpCode.FORBIDDEN:
                msg = "当前请求被拒绝";
                break;
            case HttpCode.NOT_FOUND:
                msg = "无法找到请求资源";
                break;
            case HttpCode.METHOD_NOT_ALLOWED:
                msg = "使用无效的HTTP请求类型对请求的URL进行请求";
                break;
            case HttpCode.NOT_ACCEPTABLE:
                msg = "服务器不支持的content type";
                break;
            case HttpCode.REQUEST_TIMEOUT:
                msg = "请求超时";
                break;
            case HttpCode.REQUEST_ENTITY_TOO_LARGE:
                msg = "请求体太大";
                break;
            case HttpCode.REQUEST_URL_TOO_LONG:
                msg = "请求url太长";
                break;
            case HttpCode.INTERNAL_SERVER_ERROR:
                msg = "服务器内部错误";
                break;
            case HttpCode.NOT_IMPLEMENTED:
                msg = "服务器不支持所需功能";
                break;
            case HttpCode.BAD_GATEWAY:
                msg = "服务器网关错误";
                break;
            case HttpCode.SERVICE_UNAVAILABLE:
                msg = "服务器超载或死机";
                break;
            case HttpCode.GATEWAY_TIMEOUT:
                msg = "服务器网关超时";
                break;
            case HttpCode.HTTP_VERSION_NOT_SUPPORTED:
                msg = "服务器不支持服务“http协议”版本";
                break;
            default:
                break;
        }
        errorAttributes.put("message", msg);
        errorAttributes.put("code", statusCode);
        errorAttributes.put("timestamp", System.currentTimeMillis());
        return errorAttributes;
    }

    @Override
    @SuppressWarnings("all")
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        // 这里其实可以根据errorAttributes里面的属性定制HTTP响应码
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}