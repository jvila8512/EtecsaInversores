import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './mapeo-registro.reducer';

export const MapeoRegistroDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const mapeoRegistroEntity = useAppSelector(state => state.mapeoRegistro.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="mapeoRegistroDetailsHeading">
          <Translate contentKey="inversorMonitorApp.mapeoRegistro.detail.title">MapeoRegistro</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{mapeoRegistroEntity.id}</dd>
          <dt>
            <span id="direccionRegistro">
              <Translate contentKey="inversorMonitorApp.mapeoRegistro.direccionRegistro">Direccion Registro</Translate>
            </span>
          </dt>
          <dd>{mapeoRegistroEntity.direccionRegistro}</dd>
          <dt>
            <span id="nombreRegistro">
              <Translate contentKey="inversorMonitorApp.mapeoRegistro.nombreRegistro">Nombre Registro</Translate>
            </span>
          </dt>
          <dd>{mapeoRegistroEntity.nombreRegistro}</dd>
          <dt>
            <span id="campoSql">
              <Translate contentKey="inversorMonitorApp.mapeoRegistro.campoSql">Campo Sql</Translate>
            </span>
          </dt>
          <dd>{mapeoRegistroEntity.campoSql}</dd>
          <dt>
            <span id="tipoDato">
              <Translate contentKey="inversorMonitorApp.mapeoRegistro.tipoDato">Tipo Dato</Translate>
            </span>
          </dt>
          <dd>{mapeoRegistroEntity.tipoDato}</dd>
          <dt>
            <span id="escala">
              <Translate contentKey="inversorMonitorApp.mapeoRegistro.escala">Escala</Translate>
            </span>
          </dt>
          <dd>{mapeoRegistroEntity.escala}</dd>
          <dt>
            <span id="unidad">
              <Translate contentKey="inversorMonitorApp.mapeoRegistro.unidad">Unidad</Translate>
            </span>
          </dt>
          <dd>{mapeoRegistroEntity.unidad}</dd>
          <dt>
            <span id="activo">
              <Translate contentKey="inversorMonitorApp.mapeoRegistro.activo">Activo</Translate>
            </span>
          </dt>
          <dd>{mapeoRegistroEntity.activo ? 'true' : 'false'}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="inversorMonitorApp.mapeoRegistro.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{mapeoRegistroEntity.descripcion}</dd>
          <dt>
            <Translate contentKey="inversorMonitorApp.mapeoRegistro.marca">Marca</Translate>
          </dt>
          <dd>{mapeoRegistroEntity.marca ? mapeoRegistroEntity.marca.nombre : ''}</dd>
        </dl>
        <Button tag={Link} to="/mapeo-registro" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/mapeo-registro/${mapeoRegistroEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MapeoRegistroDetail;
