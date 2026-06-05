package com.etecsa.monitor.service.mapper;

import com.etecsa.monitor.domain.GatewayTcp;
import com.etecsa.monitor.domain.Inversor;
import com.etecsa.monitor.domain.Marca;
import com.etecsa.monitor.domain.ModeloInversor;
import com.etecsa.monitor.domain.Sitio;
import com.etecsa.monitor.service.dto.GatewayTcpDTO;
import com.etecsa.monitor.service.dto.InversorDTO;
import com.etecsa.monitor.service.dto.MarcaDTO;
import com.etecsa.monitor.service.dto.ModeloInversorDTO;
import com.etecsa.monitor.service.dto.SitioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inversor} and its DTO {@link InversorDTO}.
 */
@Mapper(componentModel = "spring")
public interface InversorMapper extends EntityMapper<InversorDTO, Inversor> {
    @Mapping(target = "sitio", source = "sitio", qualifiedByName = "sitioNombre")
    @Mapping(target = "marca", source = "marca", qualifiedByName = "marcaNombre")
    @Mapping(target = "modelo", source = "modelo", qualifiedByName = "modeloInversorNombre")
    @Mapping(target = "gateway", source = "gateway", qualifiedByName = "gatewayTcpNombre")
    @Mapping(target = "masterReferencia", source = "masterReferencia", qualifiedByName = "inversorNombre")
    InversorDTO toDto(Inversor s);

    @Named("sitioNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    SitioDTO toDtoSitioNombre(Sitio sitio);

    @Named("marcaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    MarcaDTO toDtoMarcaNombre(Marca marca);

    @Named("modeloInversorNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ModeloInversorDTO toDtoModeloInversorNombre(ModeloInversor modeloInversor);

    @Named("gatewayTcpNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    GatewayTcpDTO toDtoGatewayTcpNombre(GatewayTcp gatewayTcp);

    @Named("inversorNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    InversorDTO toDtoInversorNombre(Inversor inversor);
}
