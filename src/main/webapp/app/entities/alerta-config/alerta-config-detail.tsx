import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './alerta-config.reducer';

export const AlertaConfigDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const alertaConfigEntity = useAppSelector(state => state.alertaConfig.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="alertaConfigDetailsHeading">
          <Translate contentKey="inversorMonitorApp.alertaConfig.detail.title">AlertaConfig</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{alertaConfigEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="inversorMonitorApp.alertaConfig.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{alertaConfigEntity.nombre}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="inversorMonitorApp.alertaConfig.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{alertaConfigEntity.descripcion}</dd>
          <dt>
            <span id="campoMonitorizado">
              <Translate contentKey="inversorMonitorApp.alertaConfig.campoMonitorizado">Campo Monitorizado</Translate>
            </span>
          </dt>
          <dd>{alertaConfigEntity.campoMonitorizado}</dd>
          <dt>
            <span id="operador">
              <Translate contentKey="inversorMonitorApp.alertaConfig.operador">Operador</Translate>
            </span>
          </dt>
          <dd>{alertaConfigEntity.operador}</dd>
          <dt>
            <span id="umbral">
              <Translate contentKey="inversorMonitorApp.alertaConfig.umbral">Umbral</Translate>
            </span>
          </dt>
          <dd>{alertaConfigEntity.umbral}</dd>
          <dt>
            <span id="activa">
              <Translate contentKey="inversorMonitorApp.alertaConfig.activa">Activa</Translate>
            </span>
          </dt>
          <dd>{alertaConfigEntity.activa ? 'true' : 'false'}</dd>
          <dt>
            <span id="nivelSeveridad">
              <Translate contentKey="inversorMonitorApp.alertaConfig.nivelSeveridad">Nivel Severidad</Translate>
            </span>
          </dt>
          <dd>{alertaConfigEntity.nivelSeveridad}</dd>
          <dt>
            <span id="canalNotificacion">
              <Translate contentKey="inversorMonitorApp.alertaConfig.canalNotificacion">Canal Notificacion</Translate>
            </span>
          </dt>
          <dd>{alertaConfigEntity.canalNotificacion}</dd>
          <dt>
            <span id="cooldownMinutos">
              <Translate contentKey="inversorMonitorApp.alertaConfig.cooldownMinutos">Cooldown Minutos</Translate>
            </span>
          </dt>
          <dd>{alertaConfigEntity.cooldownMinutos}</dd>
          <dt>
            <Translate contentKey="inversorMonitorApp.alertaConfig.inversor">Inversor</Translate>
          </dt>
          <dd>{alertaConfigEntity.inversor ? alertaConfigEntity.inversor.nombre : ''}</dd>
        </dl>
        <Button tag={Link} to="/alerta-config" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/alerta-config/${alertaConfigEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AlertaConfigDetail;
