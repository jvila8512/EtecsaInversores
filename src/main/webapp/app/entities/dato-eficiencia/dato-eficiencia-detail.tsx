import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dato-eficiencia.reducer';

export const DatoEficienciaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const datoEficienciaEntity = useAppSelector(state => state.datoEficiencia.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="datoEficienciaDetailsHeading">
          <Translate contentKey="inversorMonitorApp.datoEficiencia.detail.title">DatoEficiencia</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{datoEficienciaEntity.id}</dd>
          <dt>
            <span id="fecha">
              <Translate contentKey="inversorMonitorApp.datoEficiencia.fecha">Fecha</Translate>
            </span>
          </dt>
          <dd>
            {datoEficienciaEntity.fecha ? (
              <TextFormat value={datoEficienciaEntity.fecha} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="energiaPvWh">
              <Translate contentKey="inversorMonitorApp.datoEficiencia.energiaPvWh">Energia Pv Wh</Translate>
            </span>
          </dt>
          <dd>{datoEficienciaEntity.energiaPvWh}</dd>
          <dt>
            <span id="energiaCargaWh">
              <Translate contentKey="inversorMonitorApp.datoEficiencia.energiaCargaWh">Energia Carga Wh</Translate>
            </span>
          </dt>
          <dd>{datoEficienciaEntity.energiaCargaWh}</dd>
          <dt>
            <span id="energiaRedWh">
              <Translate contentKey="inversorMonitorApp.datoEficiencia.energiaRedWh">Energia Red Wh</Translate>
            </span>
          </dt>
          <dd>{datoEficienciaEntity.energiaRedWh}</dd>
          <dt>
            <span id="eficienciaPromPct">
              <Translate contentKey="inversorMonitorApp.datoEficiencia.eficienciaPromPct">Eficiencia Prom Pct</Translate>
            </span>
          </dt>
          <dd>{datoEficienciaEntity.eficienciaPromPct}</dd>
          <dt>
            <span id="horasSol">
              <Translate contentKey="inversorMonitorApp.datoEficiencia.horasSol">Horas Sol</Translate>
            </span>
          </dt>
          <dd>{datoEficienciaEntity.horasSol}</dd>
          <dt>
            <span id="picoPvW">
              <Translate contentKey="inversorMonitorApp.datoEficiencia.picoPvW">Pico Pv W</Translate>
            </span>
          </dt>
          <dd>{datoEficienciaEntity.picoPvW}</dd>
          <dt>
            <span id="picoCargaW">
              <Translate contentKey="inversorMonitorApp.datoEficiencia.picoCargaW">Pico Carga W</Translate>
            </span>
          </dt>
          <dd>{datoEficienciaEntity.picoCargaW}</dd>
          <dt>
            <span id="maxBateriaV">
              <Translate contentKey="inversorMonitorApp.datoEficiencia.maxBateriaV">Max Bateria V</Translate>
            </span>
          </dt>
          <dd>{datoEficienciaEntity.maxBateriaV}</dd>
          <dt>
            <span id="minBateriaV">
              <Translate contentKey="inversorMonitorApp.datoEficiencia.minBateriaV">Min Bateria V</Translate>
            </span>
          </dt>
          <dd>{datoEficienciaEntity.minBateriaV}</dd>
          <dt>
            <span id="totalLecturas">
              <Translate contentKey="inversorMonitorApp.datoEficiencia.totalLecturas">Total Lecturas</Translate>
            </span>
          </dt>
          <dd>{datoEficienciaEntity.totalLecturas}</dd>
          <dt>
            <span id="calculadoEn">
              <Translate contentKey="inversorMonitorApp.datoEficiencia.calculadoEn">Calculado En</Translate>
            </span>
          </dt>
          <dd>
            {datoEficienciaEntity.calculadoEn ? (
              <TextFormat value={datoEficienciaEntity.calculadoEn} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="inversorMonitorApp.datoEficiencia.inversor">Inversor</Translate>
          </dt>
          <dd>{datoEficienciaEntity.inversor ? datoEficienciaEntity.inversor.nombre : ''}</dd>
        </dl>
        <Button tag={Link} to="/dato-eficiencia" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dato-eficiencia/${datoEficienciaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DatoEficienciaDetail;
