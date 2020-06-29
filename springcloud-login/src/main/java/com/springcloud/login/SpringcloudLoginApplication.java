package com.springcloud.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;

@Import(value = MultipartAutoConfiguration.class)
@EnableEurekaClient
@EnableDiscoveryClient
@SpringBootApplication
public class SpringcloudLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudLoginApplication.class, args);
    }

}
