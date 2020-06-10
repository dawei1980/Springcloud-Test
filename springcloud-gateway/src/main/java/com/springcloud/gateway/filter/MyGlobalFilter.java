package com.springcloud.gateway.filter;

import io.netty.buffer.ByteBufAllocator;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @title: MyGlobalFilter
 * @projectName:
 * @description: TODO
 * @author: Tzh
 * @date: 2019/11/25  19:45
 */
@Component
public class MyGlobalFilter implements GlobalFilter , Ordered {
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        System.out.println("-----------------全局过滤器MyGlobalFilter---------------------\n");
//        String token = exchange.getRequest().getQueryParams().getFirst("token");
//        if (StringUtils.isBlank(token)) {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); //401 Unauthorized
//            return exchange.getResponse().setComplete();
//        }
//        return chain.filter(exchange);
//    }
//    //设置过滤器的优先级，值越小优先级越高
//    @Override
//    public int getOrder() {
//        return 0;
//    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String method = serverHttpRequest.getMethodValue();
        if ("POST".equals(method)) {
            //从请求里获取Post请求体
            String bodyStr = resolveBodyFromRequest(serverHttpRequest);
            //TODO 得到Post请求的请求参数后，做你想做的事

            //下面的将请求体再次封装写回到request里，传到下一级，否则，由于请求体已被消费，后续的服务将取不到值
            URI uri = serverHttpRequest.getURI();
            ServerHttpRequest request = serverHttpRequest.mutate().uri(uri).build();
            DataBuffer bodyDataBuffer = stringBuffer(bodyStr);
            Flux<DataBuffer> bodyFlux = Flux.just(bodyDataBuffer);

            request = new ServerHttpRequestDecorator(request) {
                @Override
                public Flux<DataBuffer> getBody() {
                    return bodyFlux;
                }
            };
            //封装request，传给下一级
            return chain.filter(exchange.mutate().request(request).build());
        } else if ("GET".equals(method)) {
//            Map requestQueryParams = serverHttpRequest.getQueryParams();

            /**Get请求需要带token参数*/
            System.out.println("-----------------全局过滤器MyGlobalFilter---------------------\n");
            String token = exchange.getRequest().getQueryParams().getFirst("token");
            if (StringUtils.isBlank(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        }
        return chain.filter(exchange);
    }

    /**
     * 从Flux<DataBuffer>中获取字符串的方法
     * @return 请求体
     */
    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        //获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();

        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        //获取request body
        return bodyRef.get();
    }

    private DataBuffer stringBuffer(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

