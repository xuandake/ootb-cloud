package com.ootb.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class OotbGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(OotbGatewayApplication.class, args);
    }
}