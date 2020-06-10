package com.springcloud.gateway.controller;

import com.springcloud.gateway.entity.GatewayRoute;
import com.springcloud.gateway.entity.GatewayRouteDto;
import com.springcloud.gateway.handler.GatewayServiceHandler;
import com.springcloud.gateway.service.GatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.BeanUtils;

@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private GatewayServiceHandler gatewayServiceHandler;

    @Autowired
    private GatewayRouteService gatewayRouteService;

    /**
     * 刷新路由配置
     *
     * @param gwdefinition
     * @return
     */
    @GetMapping("/refresh")
    public String refresh() throws Exception {
        return this.gatewayServiceHandler.loadRouteConfig();
    }

    /**
     * 增加路由记录
     *
     * @param gwdefinition
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestBody GatewayRouteDto gatewayRouteDto) throws Exception {
        gatewayRouteService.add(gatewayRouteDto);
        GatewayRoute gatewayRoute=new GatewayRoute();
        BeanUtils.copyProperties(gatewayRouteDto, gatewayRoute);
        gatewayServiceHandler.saveRoute(gatewayRoute);
        return "success";
    }

    @PostMapping("/update")
    public String update(@RequestBody GatewayRouteDto gatewayRouteDto) throws Exception {
        gatewayRouteService.update(gatewayRouteDto);
        GatewayRoute gatewayRoute=new GatewayRoute();
        BeanUtils.copyProperties(gatewayRouteDto, gatewayRoute);
        gatewayServiceHandler.update(gatewayRoute);
        return "success";
    }

    @GetMapping("/delete")
    public String delete(@PathVariable String id) throws Exception {
        gatewayRouteService.delete(id);
        gatewayServiceHandler.deleteRoute(id);
        return "success";
    }

}
