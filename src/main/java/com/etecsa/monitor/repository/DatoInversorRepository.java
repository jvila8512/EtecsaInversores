package com.etecsa.monitor.repository;

import com.etecsa.monitor.domain.DatoInversor;
import java.time.Instant;
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

    /**
     * Find the latest reading for each inversor using a native query.
     * Uses DISTINCT ON (PostgreSQL-specific) which is the most efficient way
     * to get the most recent row per group.
     *
     * @return list of latest DatoInversor rows, one per inversor
     */
    @Query(
        value = "SELECT di.* FROM dato_inversor di " +
        "INNER JOIN (SELECT inversor_id, MAX(tiempo) AS max_tiempo FROM dato_inversor GROUP BY inversor_id) latest " +
        "ON di.inversor_id = latest.inversor_id AND di.tiempo = latest.max_tiempo",
        nativeQuery = true
    )
    List<DatoInversor> findLatestReadings();

    /**
     * Get hourly aggregate (AVG of pvPotenciaW) for the last 24 hours.
     * Returns one row per hour with hour index (0-23) and average power in watts.
     * Uses PostgreSQL's EXTRACT and date_trunc for efficient hourly grouping.
     *
     * @return list of Object[] where [0]=hour (Integer), [1]=avgPotenciaW (Double)
     */
    @Query(
        value = "SELECT " +
        "  EXTRACT(HOUR FROM tiempo)::int AS hora, " +
        "  AVG(pv_potencia_w) AS avg_potencia " +
        "FROM dato_inversor " +
        "WHERE tiempo >= NOW() - INTERVAL '24 hours' " +
        "  AND pv_potencia_w IS NOT NULL " +
        "  AND pv_potencia_w > 0 " +
        "GROUP BY EXTRACT(HOUR FROM tiempo) " +
        "ORDER BY hora",
        nativeQuery = true
    )
    List<Object[]> findHourlyTrend24h();

    /**
     * Delete all readings older than the specified instant.
     *
     * @param cutoff the timestamp before which data will be deleted
     */
    @Modifying
    @Query(value = "DELETE FROM dato_inversor WHERE tiempo < :cutoff", nativeQuery = true)
    int deleteOlderThan(@Param("cutoff") Instant cutoff);
}
