package com.etecsa.monitor.service.mapper;

import com.etecsa.monitor.domain.GatewayTcp;
import com.etecsa.monitor.domain.Sitio;
import com.etecsa.monitor.service.dto.GatewayTcpDTO;
import com.etecsa.monitor.service.dto.SitioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GatewayTcp} and its DTO {@link GatewayTcpDTO}.
 */
@Mapper(componentModel = "spring")
public interface GatewayTcpMapper extends EntityMapper<GatewayTcpDTO, GatewayTcp> {
    @Mapping(target = "sitio", source = "sitio", qualifiedByName = "sitioNombre")
    GatewayTcpDTO toDto(GatewayTcp s);

    @Named("sitioNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    SitioDTO toDtoSitioNombre(Sitio sitio);
}
