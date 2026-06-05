package com.etecsa.monitor.service.mapper;

import com.etecsa.monitor.domain.DatoInversor;
import com.etecsa.monitor.domain.Inversor;
import com.etecsa.monitor.service.dto.DatoInversorDTO;
import com.etecsa.monitor.service.dto.InversorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DatoInversor} and its DTO {@link DatoInversorDTO}.
 */
@Mapper(componentModel = "spring")
public interface DatoInversorMapper extends EntityMapper<DatoInversorDTO, DatoInversor> {
    @Mapping(target = "inversor", source = "inversor", qualifiedByName = "inversorNombre")
    DatoInversorDTO toDto(DatoInversor s);

    @Named("inversorNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    InversorDTO toDtoInversorNombre(Inversor inversor);
}
