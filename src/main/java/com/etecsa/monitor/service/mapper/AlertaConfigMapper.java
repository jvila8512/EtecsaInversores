package com.etecsa.monitor.service.mapper;

import com.etecsa.monitor.domain.AlertaConfig;
import com.etecsa.monitor.domain.Inversor;
import com.etecsa.monitor.service.dto.AlertaConfigDTO;
import com.etecsa.monitor.service.dto.InversorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlertaConfig} and its DTO {@link AlertaConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlertaConfigMapper extends EntityMapper<AlertaConfigDTO, AlertaConfig> {
    @Mapping(target = "inversor", source = "inversor", qualifiedByName = "inversorNombre")
    AlertaConfigDTO toDto(AlertaConfig s);

    @Named("inversorNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    InversorDTO toDtoInversorNombre(Inversor inversor);
}
