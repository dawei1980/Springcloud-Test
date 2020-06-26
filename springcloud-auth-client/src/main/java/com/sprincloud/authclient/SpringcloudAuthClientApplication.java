package com.sprincloud.authclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableEurekaClient
@EnableResourceServer
@EnableDiscoveryClient
@SpringBootApplication
public class SpringcloudAuthClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudAuthClientApplication.class, args);
    }

}
