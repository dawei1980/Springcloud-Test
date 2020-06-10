package com.springcloud.gateway.repository;

import com.springcloud.gateway.entity.GatewayRouteDto;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface GatewayRouteDtoRepository extends JpaRepository<GatewayRouteDto, Long> {

}
