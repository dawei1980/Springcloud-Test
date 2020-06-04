package com.springcloud.springcloudmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SpringcloudManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudManagementApplication.class, args);
    }

}
