import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './evento-alerta.reducer';

export const EventoAlertaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const eventoAlertaEntity = useAppSelector(state => state.eventoAlerta.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="eventoAlertaDetailsHeading">
          <Translate contentKey="inversorMonitorApp.eventoAlerta.detail.title">EventoAlerta</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{eventoAlertaEntity.id}</dd>
          <dt>
            <span id="timestamp">
              <Translate contentKey="inversorMonitorApp.eventoAlerta.timestamp">Timestamp</Translate>
            </span>
          </dt>
          <dd>
            {eventoAlertaEntity.timestamp ? <TextFormat value={eventoAlertaEntity.timestamp} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="nombreAlerta">
              <Translate contentKey="inversorMonitorApp.eventoAlerta.nombreAlerta">Nombre Alerta</Translate>
            </span>
          </dt>
          <dd>{eventoAlertaEntity.nombreAlerta}</dd>
          <dt>
            <span id="mensaje">
              <Translate contentKey="inversorMonitorApp.eventoAlerta.mensaje">Mensaje</Translate>
            </span>
          </dt>
          <dd>{eventoAlertaEntity.mensaje}</dd>
          <dt>
            <span id="campoMonitorizado">
              <Translate contentKey="inversorMonitorApp.eventoAlerta.campoMonitorizado">Campo Monitorizado</Translate>
            </span>
          </dt>
          <dd>{eventoAlertaEntity.campoMonitorizado}</dd>
          <dt>
            <span id="valorDetectado">
              <Translate contentKey="inversorMonitorApp.eventoAlerta.valorDetectado">Valor Detectado</Translate>
            </span>
          </dt>
          <dd>{eventoAlertaEntity.valorDetectado}</dd>
          <dt>
            <span id="umbral">
              <Translate contentKey="inversorMonitorApp.eventoAlerta.umbral">Umbral</Translate>
            </span>
          </dt>
          <dd>{eventoAlertaEntity.umbral}</dd>
          <dt>
            <span id="operador">
              <Translate contentKey="inversorMonitorApp.eventoAlerta.operador">Operador</Translate>
            </span>
          </dt>
          <dd>{eventoAlertaEntity.operador}</dd>
          <dt>
            <span id="nivelSeveridad">
              <Translate contentKey="inversorMonitorApp.eventoAlerta.nivelSeveridad">Nivel Severidad</Translate>
            </span>
          </dt>
          <dd>{eventoAlertaEntity.nivelSeveridad}</dd>
          <dt>
            <span id="notificacionEnviada">
              <Translate contentKey="inversorMonitorApp.eventoAlerta.notificacionEnviada">Notificacion Enviada</Translate>
            </span>
          </dt>
          <dd>{eventoAlertaEntity.notificacionEnviada ? 'true' : 'false'}</dd>
          <dt>
            <span id="canalUsado">
              <Translate contentKey="inversorMonitorApp.eventoAlerta.canalUsado">Canal Usado</Translate>
            </span>
          </dt>
          <dd>{eventoAlertaEntity.canalUsado}</dd>
          <dt>
            <span id="resuelto">
              <Translate contentKey="inversorMonitorApp.eventoAlerta.resuelto">Resuelto</Translate>
            </span>
          </dt>
          <dd>{eventoAlertaEntity.resuelto ? 'true' : 'false'}</dd>
          <dt>
            <span id="fechaResolucion">
              <Translate contentKey="inversorMonitorApp.eventoAlerta.fechaResolucion">Fecha Resolucion</Translate>
            </span>
          </dt>
          <dd>
            {eventoAlertaEntity.fechaResolucion ? (
              <TextFormat value={eventoAlertaEntity.fechaResolucion} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="notas">
              <Translate contentKey="inversorMonitorApp.eventoAlerta.notas">Notas</Translate>
            </span>
          </dt>
          <dd>{eventoAlertaEntity.notas}</dd>
          <dt>
            <Translate contentKey="inversorMonitorApp.eventoAlerta.inversor">Inversor</Translate>
          </dt>
          <dd>{eventoAlertaEntity.inversor ? eventoAlertaEntity.inversor.nombre : ''}</dd>
          <dt>
            <Translate contentKey="inversorMonitorApp.eventoAlerta.alertaConfig">Alerta Config</Translate>
          </dt>
          <dd>{eventoAlertaEntity.alertaConfig ? eventoAlertaEntity.alertaConfig.nombre : ''}</dd>
        </dl>
        <Button tag={Link} to="/evento-alerta" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/evento-alerta/${eventoAlertaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EventoAlertaDetail;
