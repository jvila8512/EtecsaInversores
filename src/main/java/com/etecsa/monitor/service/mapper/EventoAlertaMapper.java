package com.etecsa.monitor.service.mapper;

import com.etecsa.monitor.domain.AlertaConfig;
import com.etecsa.monitor.domain.EventoAlerta;
import com.etecsa.monitor.domain.Inversor;
import com.etecsa.monitor.service.dto.AlertaConfigDTO;
import com.etecsa.monitor.service.dto.EventoAlertaDTO;
import com.etecsa.monitor.service.dto.InversorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventoAlerta} and its DTO {@link EventoAlertaDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventoAlertaMapper extends EntityMapper<EventoAlertaDTO, EventoAlerta> {
    @Mapping(target = "inversor", source = "inversor", qualifiedByName = "inversorNombre")
    @Mapping(target = "alertaConfig", source = "alertaConfig", qualifiedByName = "alertaConfigNombre")
    EventoAlertaDTO toDto(EventoAlerta s);

    @Named("inversorNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    InversorDTO toDtoInversorNombre(Inversor inversor);

    @Named("alertaConfigNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    AlertaConfigDTO toDtoAlertaConfigNombre(AlertaConfig alertaConfig);
}
