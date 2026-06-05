package com.etecsa.monitor.repository;

import com.etecsa.monitor.domain.Sitio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Sitio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SitioRepository extends JpaRepository<Sitio, Long> {}
