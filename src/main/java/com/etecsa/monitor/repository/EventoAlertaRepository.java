package com.etecsa.monitor.repository;

import com.etecsa.monitor.domain.EventoAlerta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventoAlerta entity.
 */
@Repository
public interface EventoAlertaRepository extends JpaRepository<EventoAlerta, Long>, JpaSpecificationExecutor<EventoAlerta> {
    default Optional<EventoAlerta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<EventoAlerta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<EventoAlerta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select eventoAlerta from EventoAlerta eventoAlerta left join fetch eventoAlerta.inversor left join fetch eventoAlerta.alertaConfig",
        countQuery = "select count(eventoAlerta) from EventoAlerta eventoAlerta"
    )
    Page<EventoAlerta> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select eventoAlerta from EventoAlerta eventoAlerta left join fetch eventoAlerta.inversor left join fetch eventoAlerta.alertaConfig"
    )
    List<EventoAlerta> findAllWithToOneRelationships();

    @Query(
        "select eventoAlerta from EventoAlerta eventoAlerta left join fetch eventoAlerta.inversor left join fetch eventoAlerta.alertaConfig where eventoAlerta.id =:id"
    )
    Optional<EventoAlerta> findOneWithToOneRelationships(@Param("id") Long id);
}
