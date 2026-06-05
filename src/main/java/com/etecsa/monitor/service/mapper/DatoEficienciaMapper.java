package com.etecsa.monitor.service.mapper;

import com.etecsa.monitor.domain.DatoEficiencia;
import com.etecsa.monitor.domain.Inversor;
import com.etecsa.monitor.service.dto.DatoEficienciaDTO;
import com.etecsa.monitor.service.dto.InversorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DatoEficiencia} and its DTO {@link DatoEficienciaDTO}.
 */
@Mapper(componentModel = "spring")
public interface DatoEficienciaMapper extends EntityMapper<DatoEficienciaDTO, DatoEficiencia> {
    @Mapping(target = "inversor", source = "inversor", qualifiedByName = "inversorNombre")
    DatoEficienciaDTO toDto(DatoEficiencia s);

    @Named("inversorNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    InversorDTO toDtoInversorNombre(Inversor inversor);
}
