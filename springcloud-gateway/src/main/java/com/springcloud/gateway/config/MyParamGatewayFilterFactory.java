package com.springcloud.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @title: MyParamGatewayFilterFactory
 * @projectName: spring_cloud
 * @description: TODO
 * @author: Tzh
 * @date: 2019/11/25  19:06
 */
@Component
public class MyParamGatewayFilterFactory extends AbstractGatewayFilterFactory<MyParamGatewayFilterFactory.MyParam> {
    public static final String PARAM_NAME = "param";
    public MyParamGatewayFilterFactory() {
        super(MyParam.class);
    }
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(PARAM_NAME);
    }
    @Override
    public GatewayFilter apply(MyParam config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (request.getQueryParams().containsKey(config.param)) {
                request.getQueryParams().get(config.param).
                        forEach(value -> System.out.printf("---MyParamGatewayFilterFactory--- %s = %s ---\n",config.param,value));
            }
            return chain.filter(exchange);
        };
    }
    public static class MyParam{
        public String param;
        public String getParam() {
            return param;
        }
        public void setParam(String param) {
            this.param = param;
        }
    }
}

