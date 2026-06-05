package com.etecsa.monitor.service.mapper;

import com.etecsa.monitor.domain.Marca;
import com.etecsa.monitor.domain.ModeloInversor;
import com.etecsa.monitor.service.dto.MarcaDTO;
import com.etecsa.monitor.service.dto.ModeloInversorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ModeloInversor} and its DTO {@link ModeloInversorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModeloInversorMapper extends EntityMapper<ModeloInversorDTO, ModeloInversor> {
    @Mapping(target = "marca", source = "marca", qualifiedByName = "marcaNombre")
    ModeloInversorDTO toDto(ModeloInversor s);

    @Named("marcaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    MarcaDTO toDtoMarcaNombre(Marca marca);
}
