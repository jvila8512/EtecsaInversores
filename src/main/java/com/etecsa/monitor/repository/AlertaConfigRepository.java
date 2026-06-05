package com.etecsa.monitor.repository;

import com.etecsa.monitor.domain.AlertaConfig;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlertaConfig entity.
 */
@Repository
public interface AlertaConfigRepository extends JpaRepository<AlertaConfig, Long> {
    default Optional<AlertaConfig> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AlertaConfig> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AlertaConfig> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select alertaConfig from AlertaConfig alertaConfig left join fetch alertaConfig.inversor",
        countQuery = "select count(alertaConfig) from AlertaConfig alertaConfig"
    )
    Page<AlertaConfig> findAllWithToOneRelationships(Pageable pageable);

    @Query("select alertaConfig from AlertaConfig alertaConfig left join fetch alertaConfig.inversor")
    List<AlertaConfig> findAllWithToOneRelationships();

    @Query("select alertaConfig from AlertaConfig alertaConfig left join fetch alertaConfig.inversor where alertaConfig.id =:id")
    Optional<AlertaConfig> findOneWithToOneRelationships(@Param("id") Long id);

    List<AlertaConfig> findByInversorAndActivaTrue(com.etecsa.monitor.domain.Inversor inversor);
}
