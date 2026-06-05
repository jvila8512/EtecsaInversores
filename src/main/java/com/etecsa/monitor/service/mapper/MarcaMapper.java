package com.etecsa.monitor.service.mapper;

import com.etecsa.monitor.domain.Marca;
import com.etecsa.monitor.service.dto.MarcaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Marca} and its DTO {@link MarcaDTO}.
 */
@Mapper(componentModel = "spring")
public interface MarcaMapper extends EntityMapper<MarcaDTO, Marca> {}
