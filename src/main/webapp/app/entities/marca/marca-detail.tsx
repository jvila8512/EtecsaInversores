import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './marca.reducer';

export const MarcaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const marcaEntity = useAppSelector(state => state.marca.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="marcaDetailsHeading">
          <Translate contentKey="inversorMonitorApp.marca.detail.title">Marca</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{marcaEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="inversorMonitorApp.marca.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{marcaEntity.nombre}</dd>
          <dt>
            <span id="fabricante">
              <Translate contentKey="inversorMonitorApp.marca.fabricante">Fabricante</Translate>
            </span>
          </dt>
          <dd>{marcaEntity.fabricante}</dd>
          <dt>
            <span id="registroInicio">
              <Translate contentKey="inversorMonitorApp.marca.registroInicio">Registro Inicio</Translate>
            </span>
          </dt>
          <dd>{marcaEntity.registroInicio}</dd>
          <dt>
            <span id="registroFin">
              <Translate contentKey="inversorMonitorApp.marca.registroFin">Registro Fin</Translate>
            </span>
          </dt>
          <dd>{marcaEntity.registroFin}</dd>
          <dt>
            <span id="registroInfoInicio">
              <Translate contentKey="inversorMonitorApp.marca.registroInfoInicio">Registro Info Inicio</Translate>
            </span>
          </dt>
          <dd>{marcaEntity.registroInfoInicio}</dd>
          <dt>
            <span id="registroInfoFin">
              <Translate contentKey="inversorMonitorApp.marca.registroInfoFin">Registro Info Fin</Translate>
            </span>
          </dt>
          <dd>{marcaEntity.registroInfoFin}</dd>
          <dt>
            <span id="baudRateDefault">
              <Translate contentKey="inversorMonitorApp.marca.baudRateDefault">Baud Rate Default</Translate>
            </span>
          </dt>
          <dd>{marcaEntity.baudRateDefault}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="inversorMonitorApp.marca.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{marcaEntity.descripcion}</dd>
          <dt>
            <span id="activaMarcaDefault">
              <Translate contentKey="inversorMonitorApp.marca.activaMarcaDefault">Activa Marca Default</Translate>
            </span>
          </dt>
          <dd>{marcaEntity.activaMarcaDefault ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/marca" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/marca/${marcaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MarcaDetail;
