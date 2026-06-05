package com.etecsa.monitor.service.mapper;

import com.etecsa.monitor.domain.Sitio;
import com.etecsa.monitor.service.dto.SitioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sitio} and its DTO {@link SitioDTO}.
 */
@Mapper(componentModel = "spring")
public interface SitioMapper extends EntityMapper<SitioDTO, Sitio> {}
