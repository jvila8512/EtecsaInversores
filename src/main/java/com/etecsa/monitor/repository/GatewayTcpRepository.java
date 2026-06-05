package com.etecsa.monitor.repository;

import com.etecsa.monitor.domain.GatewayTcp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GatewayTcp entity.
 */
@Repository
public interface GatewayTcpRepository extends JpaRepository<GatewayTcp, Long>, JpaSpecificationExecutor<GatewayTcp> {
    default Optional<GatewayTcp> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<GatewayTcp> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<GatewayTcp> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select gatewayTcp from GatewayTcp gatewayTcp left join fetch gatewayTcp.sitio",
        countQuery = "select count(gatewayTcp) from GatewayTcp gatewayTcp"
    )
    Page<GatewayTcp> findAllWithToOneRelationships(Pageable pageable);

    @Query("select gatewayTcp from GatewayTcp gatewayTcp left join fetch gatewayTcp.sitio")
    List<GatewayTcp> findAllWithToOneRelationships();

    @Query("select gatewayTcp from GatewayTcp gatewayTcp left join fetch gatewayTcp.sitio where gatewayTcp.id =:id")
    Optional<GatewayTcp> findOneWithToOneRelationships(@Param("id") Long id);

    List<GatewayTcp> findByActivoTrue();
}
