package com.springcloud.gateway.controller;

import com.springcloud.gateway.service.GatewayDynamicRouteService;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@RestController
@RequestMapping("/gateway")
public class GatewayDynamicRouteController {

    @Resource
    private GatewayDynamicRouteService gatewayDynamicRouteService;

    @PostMapping("/add")
    public String create(@RequestBody RouteDefinition entity) {
        int result = gatewayDynamicRouteService.add(entity);
        return String.valueOf(result);
    }

    @PostMapping("/update")
    public String update(@RequestBody RouteDefinition entity) {
        int result = gatewayDynamicRouteService.update(entity);
        return String.valueOf(result);
    }

    @PostMapping("/delete")
    public Mono<ResponseEntity<Object>> delete(@RequestParam(value = "gateway_id") String id) {
        return gatewayDynamicRouteService.delete(id);
    }

}
