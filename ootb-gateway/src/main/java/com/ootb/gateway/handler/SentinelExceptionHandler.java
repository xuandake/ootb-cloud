package com.ootb.gateway.handler;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.common.core.constant.HttpCode;
import com.common.core.result.Result;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * @ClassName GatewaySentinelExceptionHandler
 * @Description 重写限流限流响应，返回JSON格式
 * @Author xdk
 * @Date 20-07-09 11:20
 * @Version 1.0
 **/

public class SentinelExceptionHandler implements WebExceptionHandler {

    private Mono<Void> writeResponese(ServerResponse response, ServerWebExchange exchange){
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        Result result = Result.error(HttpCode.SERVER_SENTINEL, "服务器开启限流，请稍后再试");
        String resultString =  JSON.toJSONString(result);
        DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(resultString.getBytes());
        return serverHttpResponse.writeWith(Mono.just(buffer));
    }

    private Mono<ServerResponse> handleBlockRequest(ServerWebExchange exchange, Throwable throwable){
        return GatewayCallbackManager.getBlockHandler().handleRequest(exchange, throwable);
    }

    @Override
    public Mono<Void> handle (ServerWebExchange exchange, Throwable ex){
        if(exchange.getResponse().isCommitted()){
            return Mono.error(ex);
        }
        if(!BlockException.isBlockException((ex))){
           return Mono.error(ex);
        }
        return  this.handleBlockRequest(exchange, ex).flatMap(response -> writeResponese(response, exchange));
    }
}
