package com.etecsa.monitor.service;

import com.etecsa.monitor.domain.DatoInversor;
import com.etecsa.monitor.repository.DatoInversorRepository;
import com.etecsa.monitor.service.dto.DatoInversorDTO;
import com.etecsa.monitor.service.mapper.DatoInversorMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.etecsa.monitor.domain.DatoInversor}.
 */
@Service
@Transactional
public class DatoInversorService {

    private static final Logger LOG = LoggerFactory.getLogger(DatoInversorService.class);

    private final DatoInversorRepository datoInversorRepository;

    private final DatoInversorMapper datoInversorMapper;

    /** Retention period in days. Override in application.yml with app.data-retention-days */
    @Value("${app.data-retention-days:30}")
    private int dataRetentionDays;

    public DatoInversorService(DatoInversorRepository datoInversorRepository, DatoInversorMapper datoInversorMapper) {
        this.datoInversorRepository = datoInversorRepository;
        this.datoInversorMapper = datoInversorMapper;
    }

    /**
     * Save a datoInversor.
     *
     * @param datoInversorDTO the entity to save.
     * @return the persisted entity.
     */
    public DatoInversorDTO save(DatoInversorDTO datoInversorDTO) {
        LOG.debug("Request to save DatoInversor : {}", datoInversorDTO);
        DatoInversor datoInversor = datoInversorMapper.toEntity(datoInversorDTO);
        datoInversor = datoInversorRepository.save(datoInversor);
        return datoInversorMapper.toDto(datoInversor);
    }

    /**
     * Update a datoInversor.
     *
     * @param datoInversorDTO the entity to save.
     * @return the persisted entity.
     */
    public DatoInversorDTO update(DatoInversorDTO datoInversorDTO) {
        LOG.debug("Request to update DatoInversor : {}", datoInversorDTO);
        DatoInversor datoInversor = datoInversorMapper.toEntity(datoInversorDTO);
        datoInversor = datoInversorRepository.save(datoInversor);
        return datoInversorMapper.toDto(datoInversor);
    }

    /**
     * Partially update a datoInversor.
     *
     * @param datoInversorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DatoInversorDTO> partialUpdate(DatoInversorDTO datoInversorDTO) {
        LOG.debug("Request to partially update DatoInversor : {}", datoInversorDTO);

        return datoInversorRepository
            .findById(datoInversorDTO.getId())
            .map(existingDatoInversor -> {
                datoInversorMapper.partialUpdate(existingDatoInversor, datoInversorDTO);

                return existingDatoInversor;
            })
            .map(datoInversorRepository::save)
            .map(datoInversorMapper::toDto);
    }

    /**
     * Get all the datoInversors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DatoInversorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return datoInversorRepository.findAllWithEagerRelationships(pageable).map(datoInversorMapper::toDto);
    }

    /**
     * Get one datoInversor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DatoInversorDTO> findOne(Long id) {
        LOG.debug("Request to get DatoInversor : {}", id);
        return datoInversorRepository.findOneWithEagerRelationships(id).map(datoInversorMapper::toDto);
    }

    /**
     * Delete the datoInversor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DatoInversor : {}", id);
        datoInversorRepository.deleteById(id);
    }

    /**
     * Get the latest reading for each inversor.
     * Uses an efficient INNER JOIN on MAX(tiempo) per inversor_id,
     * returning exactly one row per inversor — the most recent one.
     *
     * With 300 inversores this returns 300 rows instead of
     * having to paginate through millions of historical rows.
     *
     * @return list of DTOs with the latest reading per inversor.
     */
    @Transactional(readOnly = true)
    public List<DatoInversorDTO> findLatestReadings() {
        LOG.debug("Request to get latest readings for all inversores");
        List<DatoInversor> readings = datoInversorRepository.findLatestReadings();
        return readings.stream().map(datoInversorMapper::toDto).toList();
    }

    /**
     * Get hourly aggregated trend data for the last 24 hours.
     * Returns 0-24 data points with hour index and average PV power.
     *
     * If there are hours with no data, they will be missing from the result
     * (the frontend should fill gaps with zero).
     *
     * @return list of Object[] where [0]=hour (Integer), [1]=avgPotenciaW (Double)
     */
    @Transactional(readOnly = true)
    public List<Object[]> findHourlyTrend24h() {
        LOG.debug("Request to get hourly trend for last 24h");
        return datoInversorRepository.findHourlyTrend24h();
    }

    /**
     * Delete all readings older than the specified cutoff.
     * Used by the auto-purge scheduled task to prevent
     * unbounded table growth (5.2M rows/day with 300 inversores).
     *
     * @param cutoff timestamp before which data will be deleted
     * @return number of rows deleted
     */
    public int deleteOlderThan(Instant cutoff) {
        LOG.debug("Request to delete DatoInversor readings older than : {}", cutoff);
        return datoInversorRepository.deleteOlderThan(cutoff);
    }

    /**
     * Auto-purge scheduled task: runs every day at 3:00 AM.
     *
     * WHY: With 300 inversores generating readings every 5 seconds,
     * dato_inversor grows ~5.2M rows/day. Without purge the table
     * reaches 156M rows/month, degrading query performance and
     * consuming disk space. This task keeps the table at ~30 days
     * of data (configurable via app.data-retention-days).
     *
     * The 3 AM schedule avoids peak hours. The deleteOlderThan()
     * method uses a native DELETE which is fast for bulk removal.
     * PostgreSQL will auto-VACUUM the dead tuples afterwards.
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void purgeOldReadings() {
        Instant cutoff = Instant.now().minus(dataRetentionDays, ChronoUnit.DAYS);
        LOG.info("Auto-purge: deleting DatoInversor readings older than {} ({} days retention)", cutoff, dataRetentionDays);
        int deleted = deleteOlderThan(cutoff);
        LOG.info("Auto-purge complete: {} rows deleted", deleted);
    }
}
