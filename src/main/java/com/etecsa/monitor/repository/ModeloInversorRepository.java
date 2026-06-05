package com.etecsa.monitor.repository;

import com.etecsa.monitor.domain.ModeloInversor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ModeloInversor entity.
 */
@Repository
public interface ModeloInversorRepository extends JpaRepository<ModeloInversor, Long> {
    default Optional<ModeloInversor> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ModeloInversor> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ModeloInversor> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select modeloInversor from ModeloInversor modeloInversor left join fetch modeloInversor.marca",
        countQuery = "select count(modeloInversor) from ModeloInversor modeloInversor"
    )
    Page<ModeloInversor> findAllWithToOneRelationships(Pageable pageable);

    @Query("select modeloInversor from ModeloInversor modeloInversor left join fetch modeloInversor.marca")
    List<ModeloInversor> findAllWithToOneRelationships();

    @Query("select modeloInversor from ModeloInversor modeloInversor left join fetch modeloInversor.marca where modeloInversor.id =:id")
    Optional<ModeloInversor> findOneWithToOneRelationships(@Param("id") Long id);
}
