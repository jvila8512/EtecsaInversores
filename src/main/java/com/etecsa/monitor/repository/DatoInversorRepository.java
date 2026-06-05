package com.etecsa.monitor.repository;

import com.etecsa.monitor.domain.DatoInversor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DatoInversor entity.
 */
@Repository
public interface DatoInversorRepository extends JpaRepository<DatoInversor, Long>, JpaSpecificationExecutor<DatoInversor> {
    default Optional<DatoInversor> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DatoInversor> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DatoInversor> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select datoInversor from DatoInversor datoInversor left join fetch datoInversor.inversor",
        countQuery = "select count(datoInversor) from DatoInversor datoInversor"
    )
    Page<DatoInversor> findAllWithToOneRelationships(Pageable pageable);

    @Query("select datoInversor from DatoInversor datoInversor left join fetch datoInversor.inversor")
    List<DatoInversor> findAllWithToOneRelationships();

    @Query("select datoInversor from DatoInversor datoInversor left join fetch datoInversor.inversor where datoInversor.id =:id")
    Optional<DatoInversor> findOneWithToOneRelationships(@Param("id") Long id);
}
