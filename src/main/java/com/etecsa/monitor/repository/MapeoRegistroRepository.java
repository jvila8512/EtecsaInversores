package com.etecsa.monitor.repository;

import com.etecsa.monitor.domain.MapeoRegistro;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MapeoRegistro entity.
 */
@Repository
public interface MapeoRegistroRepository extends JpaRepository<MapeoRegistro, Long>, JpaSpecificationExecutor<MapeoRegistro> {
    default Optional<MapeoRegistro> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MapeoRegistro> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MapeoRegistro> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select mapeoRegistro from MapeoRegistro mapeoRegistro left join fetch mapeoRegistro.marca",
        countQuery = "select count(mapeoRegistro) from MapeoRegistro mapeoRegistro"
    )
    Page<MapeoRegistro> findAllWithToOneRelationships(Pageable pageable);

    @Query("select mapeoRegistro from MapeoRegistro mapeoRegistro left join fetch mapeoRegistro.marca")
    List<MapeoRegistro> findAllWithToOneRelationships();

    @Query("select mapeoRegistro from MapeoRegistro mapeoRegistro left join fetch mapeoRegistro.marca where mapeoRegistro.id =:id")
    Optional<MapeoRegistro> findOneWithToOneRelationships(@Param("id") Long id);

    List<MapeoRegistro> findByMarcaAndActivoTrue(com.etecsa.monitor.domain.Marca marca);
}
