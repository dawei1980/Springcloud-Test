package com.springcloud.gateway.filter;

import io.netty.buffer.ByteBufAllocator;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
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
        ServerRequest serverRequest = new DefaultServerRequest(exchange);
        // mediaType
        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
        String method = exchange.getRequest().getMethodValue();
        // read & modify body
        if ("POST".equals(method)) {

            Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(body -> {
                if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
                    // origin body map
                    //这是你post请求获取到的参数
                    Map<String, Object> bodyMap = decodeBody(body);
                    //这里做你的业务操作
                    //重新放回
                    return Mono.just(encodeBody(bodyMap));
                }
                return Mono.empty();
            });

            BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
            HttpHeaders headers = new HttpHeaders();
            headers.putAll(exchange.getRequest().getHeaders());

            // the new content type will be computed by bodyInserter
            // and then set in the request decorator
            headers.remove(HttpHeaders.CONTENT_LENGTH);

            CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
            return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
                ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {

                    public HttpHeaders getHeaders() {
                        long contentLength = headers.getContentLength();
                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.putAll(super.getHeaders());
                        if (contentLength > 0) {
                            httpHeaders.setContentLength(contentLength);
                        } else {
                            httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                        }
                        return httpHeaders;
                    }

                    public Flux<DataBuffer> getBody() {
                        return outputMessage.getBody();
                    }
                };
                return chain.filter(exchange.mutate().request(decorator).build());
            }));
        }else if ("GET".equals(method)) {
//            Map requestQueryParams = exchange.getRequest().getQueryParams();
//            //TODO 得到Get请求的请求参数后，做你想做的事
//
//            System.out.println("-----------------全局过滤器MyGlobalFilter---------------------\n");
//            String token = exchange.getRequest().getQueryParams().getFirst("token");
//            if (StringUtils.isBlank(token)) {
//                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                return exchange.getResponse().setComplete();
//            }

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

