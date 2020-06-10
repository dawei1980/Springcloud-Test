package com.springcloud.gateway.repository;

import com.springcloud.gateway.entity.GatewayRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
public interface GatewayRouteRepository extends JpaRepository<GatewayRoute,Long> {
    List<GatewayRoute> findAll();

    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM gateway_route WHERE id = ?1",nativeQuery = true)
    int deleteGatewayRoute(Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE gateway_route SET service_id=?2,uri=?3,predicates=?4,filters=?5,order=?6,creator_id=?7,create_date=?8,update_id=?9,update_date=?10,remarks=?11,del_flag=?12,gorder=?13 WHERE id = ?1",nativeQuery = true)
    int updateGatewayRoute(Long id, String serviceId, String uri, String predicates, String filters, String order, String creatorId, Date createDate, String updateId, Date updateDate, String remarks, String delFlag, String gorder);
}
