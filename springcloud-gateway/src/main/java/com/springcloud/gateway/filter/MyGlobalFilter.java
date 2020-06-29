package com.springcloud.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @title: MyGlobalFilter
 * @projectName:
 * @description: TODO
 * @author: Jiang dawei
 * @date: 2020/06/11
 */
@Component
public class MyGlobalFilter implements GlobalFilter , Ordered {

    /**Get请求需要带token参数
     * localhost:8084/springcloud-login/login/getAllLogin?token=1
     *
     * Post请求需要传递参数
     * */
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("-----------------全局过滤器MyGlobalFilter---------------------");
        System.out.println("-----------------请求方式---------------------");

        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethodValue();
        String contentType = request.getHeaders().getFirst("Content-Type");

        if ("POST".equals(method) && Objects.requireNonNull(contentType).startsWith("multipart/form-data")){
            return DataBufferUtils.join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        String bodyString = new String(bytes, StandardCharsets.UTF_8);
                        exchange.getAttributes().put("POST_BODY",bodyString);
                        DataBufferUtils.release(dataBuffer);
                        Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                            DataBuffer buffer = exchange.getResponse().bufferFactory()
                                    .wrap(bytes);
                            return Mono.just(buffer);
                        });

                        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(
                                exchange.getRequest()) {
                            @Override
                            public Flux<DataBuffer> getBody() {
                                return cachedFlux;
                            }
                        };
                        return chain.filter(exchange.mutate().request(mutatedRequest)
                                .build());
                    });
        }
        else if ("GET".equals(method)) {
            return chain.filter(exchange);
        }
        return chain.filter(exchange);
    }


    private Map<String, Object> decodeBody(String body) {
        return Arrays.stream(body.split("&")).map(s -> s.split("="))
                .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
    }

    private String encodeBody(Map<String, Object> map) {
        return map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

