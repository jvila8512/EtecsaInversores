import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dato-inversor.reducer';

export const DatoInversorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const datoInversorEntity = useAppSelector(state => state.datoInversor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="datoInversorDetailsHeading">
          <Translate contentKey="inversorMonitorApp.datoInversor.detail.title">DatoInversor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.id}</dd>
          <dt>
            <span id="tiempo">
              <Translate contentKey="inversorMonitorApp.datoInversor.tiempo">Tiempo</Translate>
            </span>
          </dt>
          <dd>
            {datoInversorEntity.tiempo ? <TextFormat value={datoInversorEntity.tiempo} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="bateriaVoltajeV">
              <Translate contentKey="inversorMonitorApp.datoInversor.bateriaVoltajeV">Bateria Voltaje V</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.bateriaVoltajeV}</dd>
          <dt>
            <span id="bateriaCorrienteA">
              <Translate contentKey="inversorMonitorApp.datoInversor.bateriaCorrienteA">Bateria Corriente A</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.bateriaCorrienteA}</dd>
          <dt>
            <span id="bateriaPotenciaW">
              <Translate contentKey="inversorMonitorApp.datoInversor.bateriaPotenciaW">Bateria Potencia W</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.bateriaPotenciaW}</dd>
          <dt>
            <span id="pvVoltajeV">
              <Translate contentKey="inversorMonitorApp.datoInversor.pvVoltajeV">Pv Voltaje V</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.pvVoltajeV}</dd>
          <dt>
            <span id="pvPotenciaW">
              <Translate contentKey="inversorMonitorApp.datoInversor.pvPotenciaW">Pv Potencia W</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.pvPotenciaW}</dd>
          <dt>
            <span id="salidaVoltajeV">
              <Translate contentKey="inversorMonitorApp.datoInversor.salidaVoltajeV">Salida Voltaje V</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.salidaVoltajeV}</dd>
          <dt>
            <span id="salidaPotenciaW">
              <Translate contentKey="inversorMonitorApp.datoInversor.salidaPotenciaW">Salida Potencia W</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.salidaPotenciaW}</dd>
          <dt>
            <span id="salidaAparenteVa">
              <Translate contentKey="inversorMonitorApp.datoInversor.salidaAparenteVa">Salida Aparente Va</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.salidaAparenteVa}</dd>
          <dt>
            <span id="cargaPorcentaje">
              <Translate contentKey="inversorMonitorApp.datoInversor.cargaPorcentaje">Carga Porcentaje</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.cargaPorcentaje}</dd>
          <dt>
            <span id="redVoltajeV">
              <Translate contentKey="inversorMonitorApp.datoInversor.redVoltajeV">Red Voltaje V</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.redVoltajeV}</dd>
          <dt>
            <span id="redFrecuenciaHz">
              <Translate contentKey="inversorMonitorApp.datoInversor.redFrecuenciaHz">Red Frecuencia Hz</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.redFrecuenciaHz}</dd>
          <dt>
            <span id="modoOperacion">
              <Translate contentKey="inversorMonitorApp.datoInversor.modoOperacion">Modo Operacion</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.modoOperacion}</dd>
          <dt>
            <span id="codigoFallo">
              <Translate contentKey="inversorMonitorApp.datoInversor.codigoFallo">Codigo Fallo</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.codigoFallo}</dd>
          <dt>
            <span id="eficienciaPct">
              <Translate contentKey="inversorMonitorApp.datoInversor.eficienciaPct">Eficiencia Pct</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.eficienciaPct}</dd>
          <dt>
            <span id="consumoRedW">
              <Translate contentKey="inversorMonitorApp.datoInversor.consumoRedW">Consumo Red W</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.consumoRedW}</dd>
          <dt>
            <span id="bateriaCargando">
              <Translate contentKey="inversorMonitorApp.datoInversor.bateriaCargando">Bateria Cargando</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.bateriaCargando ? 'true' : 'false'}</dd>
          <dt>
            <span id="pvActivo">
              <Translate contentKey="inversorMonitorApp.datoInversor.pvActivo">Pv Activo</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.pvActivo ? 'true' : 'false'}</dd>
          <dt>
            <span id="redActiva">
              <Translate contentKey="inversorMonitorApp.datoInversor.redActiva">Red Activa</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.redActiva ? 'true' : 'false'}</dd>
          <dt>
            <span id="crcValido">
              <Translate contentKey="inversorMonitorApp.datoInversor.crcValido">Crc Valido</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.crcValido ? 'true' : 'false'}</dd>
          <dt>
            <span id="latenciaMs">
              <Translate contentKey="inversorMonitorApp.datoInversor.latenciaMs">Latencia Ms</Translate>
            </span>
          </dt>
          <dd>{datoInversorEntity.latenciaMs}</dd>
          <dt>
            <Translate contentKey="inversorMonitorApp.datoInversor.inversor">Inversor</Translate>
          </dt>
          <dd>{datoInversorEntity.inversor ? datoInversorEntity.inversor.nombre : ''}</dd>
        </dl>
        <Button tag={Link} to="/dato-inversor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dato-inversor/${datoInversorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DatoInversorDetail;
