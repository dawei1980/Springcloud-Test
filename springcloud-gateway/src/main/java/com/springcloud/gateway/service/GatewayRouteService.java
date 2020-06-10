package com.springcloud.gateway.service;

import java.util.Date;

import com.springcloud.gateway.entity.GatewayRoute;
import com.springcloud.gateway.entity.GatewayRouteDto;
import com.springcloud.gateway.repository.GatewayRouteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GatewayRouteService {

    @Autowired
    private GatewayRouteRepository gatewayRouteRepository;

    public Object add(GatewayRouteDto gatewayRouteDto) {
        GatewayRoute gatewayRoute = new GatewayRoute();
        BeanUtils.copyProperties(gatewayRouteDto, gatewayRoute);
        gatewayRoute.setCreateDate(new Date());
        gatewayRoute.setCreatorId("");
        return gatewayRouteRepository.save(gatewayRoute);
    }

    public Object update(GatewayRouteDto gatewayRouteDto) {
        GatewayRoute gatewayRoute = new GatewayRoute();
        BeanUtils.copyProperties(gatewayRouteDto, gatewayRoute);
        gatewayRoute.setUpdateDate(new Date());
        gatewayRoute.setCreateDate(new Date());
        gatewayRoute.setUpdateId("");
        return gatewayRouteRepository.save(gatewayRoute);
    }

    public Integer delete(String id) {
        return gatewayRouteRepository.deleteGatewayRoute(Long.parseLong(id));
    }

}
