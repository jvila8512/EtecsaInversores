package com.etecsa.monitor.service.mapper;

import com.etecsa.monitor.domain.MapeoRegistro;
import com.etecsa.monitor.domain.Marca;
import com.etecsa.monitor.service.dto.MapeoRegistroDTO;
import com.etecsa.monitor.service.dto.MarcaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MapeoRegistro} and its DTO {@link MapeoRegistroDTO}.
 */
@Mapper(componentModel = "spring")
public interface MapeoRegistroMapper extends EntityMapper<MapeoRegistroDTO, MapeoRegistro> {
    @Mapping(target = "marca", source = "marca", qualifiedByName = "marcaNombre")
    MapeoRegistroDTO toDto(MapeoRegistro s);

    @Named("marcaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    MarcaDTO toDtoMarcaNombre(Marca marca);
}
