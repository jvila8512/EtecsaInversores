package com.etecsa.monitor.repository;

import com.etecsa.monitor.domain.DatoEficiencia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DatoEficiencia entity.
 */
@Repository
public interface DatoEficienciaRepository extends JpaRepository<DatoEficiencia, Long>, JpaSpecificationExecutor<DatoEficiencia> {
    default Optional<DatoEficiencia> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DatoEficiencia> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DatoEficiencia> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select datoEficiencia from DatoEficiencia datoEficiencia left join fetch datoEficiencia.inversor",
        countQuery = "select count(datoEficiencia) from DatoEficiencia datoEficiencia"
    )
    Page<DatoEficiencia> findAllWithToOneRelationships(Pageable pageable);

    @Query("select datoEficiencia from DatoEficiencia datoEficiencia left join fetch datoEficiencia.inversor")
    List<DatoEficiencia> findAllWithToOneRelationships();

    @Query("select datoEficiencia from DatoEficiencia datoEficiencia left join fetch datoEficiencia.inversor where datoEficiencia.id =:id")
    Optional<DatoEficiencia> findOneWithToOneRelationships(@Param("id") Long id);
}
