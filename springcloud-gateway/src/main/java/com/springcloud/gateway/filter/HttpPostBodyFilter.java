package com.springcloud.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: MyGlobalFilter
 * @projectName:
 * @description: TODO
 * @author: Jiang dawei
 * @date: 2020/06/11
 */
@Component
public class HttpPostBodyFilter implements GlobalFilter , Ordered {

    /**Get请求需要带token参数
     * localhost:8084/springcloud-login/login/getAllLogin?token=1
     *
     * Post请求需要传递参数
     * */
    private static final String REQUESTID = "traceId";

    private static final String CONTENT_TYPE_JSON = "application/json";

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("-----------------请求方式---------------------");

        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethodValue();
        String contentType = request.getHeaders().getFirst("Content-Type");

//        String bodyStr = "";

        //判断是否为POST请求
        if (null != contentType && HttpMethod.POST.name().equalsIgnoreCase(method) && contentType.contains(CONTENT_TYPE_JSON)) {
            ServerRequest serverRequest = new DefaultServerRequest(exchange);
            List<String> list = new ArrayList<>();
            // 读取请求体
            Mono<String> modifiedBody = serverRequest.bodyToMono(String.class)
                    .flatMap(body -> {
                        //记录请求体日志
//                        final String nId = saveRequestOperLog(exchange, body);
//                        //记录日志id
//                        list.add(nId);
                        return Mono.just(body);
                    });

            BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
            HttpHeaders headers = new HttpHeaders();
            headers.putAll(exchange.getRequest().getHeaders());
            headers.remove(HttpHeaders.CONTENT_LENGTH);

            CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
            return bodyInserter.insert(outputMessage, new BodyInserterContext())
                    .then(Mono.defer(() -> {
                        ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(
                                exchange.getRequest()) {
                            @Override
                            public HttpHeaders getHeaders() {
                                long contentLength = headers.getContentLength();
                                HttpHeaders httpHeaders = new HttpHeaders();
                                httpHeaders.putAll(super.getHeaders());
                                httpHeaders.put(REQUESTID,list);
                                if (contentLength > 0) {
                                    httpHeaders.setContentLength(contentLength);
                                } else {
                                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                                }
                                return httpHeaders;
                            }

                            @Override
                            public Flux<DataBuffer> getBody() {
                                return outputMessage.getBody();
                            }
                        };

                        return chain.filter(exchange.mutate().request(decorator).build());
                    }));
        }else if (HttpMethod.GET.name().equalsIgnoreCase(method)) {
//            bodyStr = request.getQueryParams().toString();
//            String nId = saveRequestOperLog(exchange, bodyStr);
//            ServerHttpRequest userInfo = exchange.getRequest().mutate().header(REQUESTID, nId).build();
//            return chain.filter(exchange.mutate().request(userInfo).build());
            return chain.filter(exchange);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

