import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './gateway-tcp.reducer';

export const GatewayTcpDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const gatewayTcpEntity = useAppSelector(state => state.gatewayTcp.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gatewayTcpDetailsHeading">
          <Translate contentKey="inversorMonitorApp.gatewayTcp.detail.title">GatewayTcp</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{gatewayTcpEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="inversorMonitorApp.gatewayTcp.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{gatewayTcpEntity.nombre}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="inversorMonitorApp.gatewayTcp.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{gatewayTcpEntity.descripcion}</dd>
          <dt>
            <span id="ubicacion">
              <Translate contentKey="inversorMonitorApp.gatewayTcp.ubicacion">Ubicacion</Translate>
            </span>
          </dt>
          <dd>{gatewayTcpEntity.ubicacion}</dd>
          <dt>
            <span id="direccionIp">
              <Translate contentKey="inversorMonitorApp.gatewayTcp.direccionIp">Direccion Ip</Translate>
            </span>
          </dt>
          <dd>{gatewayTcpEntity.direccionIp}</dd>
          <dt>
            <span id="puerto">
              <Translate contentKey="inversorMonitorApp.gatewayTcp.puerto">Puerto</Translate>
            </span>
          </dt>
          <dd>{gatewayTcpEntity.puerto}</dd>
          <dt>
            <span id="activo">
              <Translate contentKey="inversorMonitorApp.gatewayTcp.activo">Activo</Translate>
            </span>
          </dt>
          <dd>{gatewayTcpEntity.activo ? 'true' : 'false'}</dd>
          <dt>
            <span id="estadoConexion">
              <Translate contentKey="inversorMonitorApp.gatewayTcp.estadoConexion">Estado Conexion</Translate>
            </span>
          </dt>
          <dd>{gatewayTcpEntity.estadoConexion}</dd>
          <dt>
            <span id="ultimaLectura">
              <Translate contentKey="inversorMonitorApp.gatewayTcp.ultimaLectura">Ultima Lectura</Translate>
            </span>
          </dt>
          <dd>
            {gatewayTcpEntity.ultimaLectura ? (
              <TextFormat value={gatewayTcpEntity.ultimaLectura} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="ultimoError">
              <Translate contentKey="inversorMonitorApp.gatewayTcp.ultimoError">Ultimo Error</Translate>
            </span>
          </dt>
          <dd>{gatewayTcpEntity.ultimoError}</dd>
          <dt>
            <span id="tiempoEsperaMs">
              <Translate contentKey="inversorMonitorApp.gatewayTcp.tiempoEsperaMs">Tiempo Espera Ms</Translate>
            </span>
          </dt>
          <dd>{gatewayTcpEntity.tiempoEsperaMs}</dd>
          <dt>
            <span id="reintentos">
              <Translate contentKey="inversorMonitorApp.gatewayTcp.reintentos">Reintentos</Translate>
            </span>
          </dt>
          <dd>{gatewayTcpEntity.reintentos}</dd>
          <dt>
            <Translate contentKey="inversorMonitorApp.gatewayTcp.sitio">Sitio</Translate>
          </dt>
          <dd>{gatewayTcpEntity.sitio ? gatewayTcpEntity.sitio.nombre : ''}</dd>
        </dl>
        <Button tag={Link} to="/gateway-tcp" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/gateway-tcp/${gatewayTcpEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GatewayTcpDetail;
