import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './inversor.reducer';

export const InversorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const inversorEntity = useAppSelector(state => state.inversor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="inversorDetailsHeading">
          <Translate contentKey="inversorMonitorApp.inversor.detail.title">Inversor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{inversorEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="inversorMonitorApp.inversor.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{inversorEntity.nombre}</dd>
          <dt>
            <span id="numeroSerie">
              <Translate contentKey="inversorMonitorApp.inversor.numeroSerie">Numero Serie</Translate>
            </span>
          </dt>
          <dd>{inversorEntity.numeroSerie}</dd>
          <dt>
            <span id="esMaster">
              <Translate contentKey="inversorMonitorApp.inversor.esMaster">Es Master</Translate>
            </span>
          </dt>
          <dd>{inversorEntity.esMaster ? 'true' : 'false'}</dd>
          <dt>
            <span id="tipoConexion">
              <Translate contentKey="inversorMonitorApp.inversor.tipoConexion">Tipo Conexion</Translate>
            </span>
          </dt>
          <dd>{inversorEntity.tipoConexion}</dd>
          <dt>
            <span id="puertoSerial">
              <Translate contentKey="inversorMonitorApp.inversor.puertoSerial">Puerto Serial</Translate>
            </span>
          </dt>
          <dd>{inversorEntity.puertoSerial}</dd>
          <dt>
            <span id="slaveId">
              <Translate contentKey="inversorMonitorApp.inversor.slaveId">Slave Id</Translate>
            </span>
          </dt>
          <dd>{inversorEntity.slaveId}</dd>
          <dt>
            <span id="activo">
              <Translate contentKey="inversorMonitorApp.inversor.activo">Activo</Translate>
            </span>
          </dt>
          <dd>{inversorEntity.activo ? 'true' : 'false'}</dd>
          <dt>
            <span id="estadoDispositivo">
              <Translate contentKey="inversorMonitorApp.inversor.estadoDispositivo">Estado Dispositivo</Translate>
            </span>
          </dt>
          <dd>{inversorEntity.estadoDispositivo}</dd>
          <dt>
            <span id="ultimaLectura">
              <Translate contentKey="inversorMonitorApp.inversor.ultimaLectura">Ultima Lectura</Translate>
            </span>
          </dt>
          <dd>
            {inversorEntity.ultimaLectura ? <TextFormat value={inversorEntity.ultimaLectura} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="ultimoError">
              <Translate contentKey="inversorMonitorApp.inversor.ultimoError">Ultimo Error</Translate>
            </span>
          </dt>
          <dd>{inversorEntity.ultimoError}</dd>
          <dt>
            <span id="periodoMuestreoSeg">
              <Translate contentKey="inversorMonitorApp.inversor.periodoMuestreoSeg">Periodo Muestreo Seg</Translate>
            </span>
          </dt>
          <dd>{inversorEntity.periodoMuestreoSeg}</dd>
          <dt>
            <span id="fechaRegistro">
              <Translate contentKey="inversorMonitorApp.inversor.fechaRegistro">Fecha Registro</Translate>
            </span>
          </dt>
          <dd>
            {inversorEntity.fechaRegistro ? <TextFormat value={inversorEntity.fechaRegistro} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="ultimaConexion">
              <Translate contentKey="inversorMonitorApp.inversor.ultimaConexion">Ultima Conexion</Translate>
            </span>
          </dt>
          <dd>
            {inversorEntity.ultimaConexion ? (
              <TextFormat value={inversorEntity.ultimaConexion} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="inversorMonitorApp.inversor.sitio">Sitio</Translate>
          </dt>
          <dd>{inversorEntity.sitio ? inversorEntity.sitio.nombre : ''}</dd>
          <dt>
            <Translate contentKey="inversorMonitorApp.inversor.marca">Marca</Translate>
          </dt>
          <dd>{inversorEntity.marca ? inversorEntity.marca.nombre : ''}</dd>
          <dt>
            <Translate contentKey="inversorMonitorApp.inversor.modelo">Modelo</Translate>
          </dt>
          <dd>{inversorEntity.modelo ? inversorEntity.modelo.nombre : ''}</dd>
          <dt>
            <Translate contentKey="inversorMonitorApp.inversor.gateway">Gateway</Translate>
          </dt>
          <dd>{inversorEntity.gateway ? inversorEntity.gateway.nombre : ''}</dd>
          <dt>
            <Translate contentKey="inversorMonitorApp.inversor.masterReferencia">Master Referencia</Translate>
          </dt>
          <dd>{inversorEntity.masterReferencia ? inversorEntity.masterReferencia.nombre : ''}</dd>
        </dl>
        <Button tag={Link} to="/inversor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/inversor/${inversorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default InversorDetail;
