import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './modelo-inversor.reducer';

export const ModeloInversorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const modeloInversorEntity = useAppSelector(state => state.modeloInversor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="modeloInversorDetailsHeading">
          <Translate contentKey="inversorMonitorApp.modeloInversor.detail.title">ModeloInversor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{modeloInversorEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="inversorMonitorApp.modeloInversor.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{modeloInversorEntity.nombre}</dd>
          <dt>
            <span id="capacidadVa">
              <Translate contentKey="inversorMonitorApp.modeloInversor.capacidadVa">Capacidad Va</Translate>
            </span>
          </dt>
          <dd>{modeloInversorEntity.capacidadVa}</dd>
          <dt>
            <span id="voltajeSistemaNom">
              <Translate contentKey="inversorMonitorApp.modeloInversor.voltajeSistemaNom">Voltaje Sistema Nom</Translate>
            </span>
          </dt>
          <dd>{modeloInversorEntity.voltajeSistemaNom}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="inversorMonitorApp.modeloInversor.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{modeloInversorEntity.descripcion}</dd>
          <dt>
            <Translate contentKey="inversorMonitorApp.modeloInversor.marca">Marca</Translate>
          </dt>
          <dd>{modeloInversorEntity.marca ? modeloInversorEntity.marca.nombre : ''}</dd>
        </dl>
        <Button tag={Link} to="/modelo-inversor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/modelo-inversor/${modeloInversorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ModeloInversorDetail;
