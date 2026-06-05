package com.etecsa.monitor.repository;

import com.etecsa.monitor.domain.GatewayTcp;
import com.etecsa.monitor.domain.Inversor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Inversor entity.
 */
@Repository
public interface InversorRepository extends JpaRepository<Inversor, Long>, JpaSpecificationExecutor<Inversor> {
    default Optional<Inversor> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Inversor> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Inversor> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select inversor from Inversor inversor left join fetch inversor.sitio left join fetch inversor.marca left join fetch inversor.modelo left join fetch inversor.gateway left join fetch inversor.masterReferencia",
        countQuery = "select count(inversor) from Inversor inversor"
    )
    Page<Inversor> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select inversor from Inversor inversor left join fetch inversor.sitio left join fetch inversor.marca left join fetch inversor.modelo left join fetch inversor.gateway left join fetch inversor.masterReferencia"
    )
    List<Inversor> findAllWithToOneRelationships();

    @Query(
        "select inversor from Inversor inversor left join fetch inversor.sitio left join fetch inversor.marca left join fetch inversor.modelo left join fetch inversor.gateway left join fetch inversor.masterReferencia where inversor.id =:id"
    )
    Optional<Inversor> findOneWithToOneRelationships(@Param("id") Long id);

    List<Inversor> findByGatewayAndActivoTrue(GatewayTcp gateway);

    List<Inversor> findByMasterReferenciaAndActivoTrue(Inversor master);

    List<Inversor> findByEsMasterTrueAndActivoTrue();
}
