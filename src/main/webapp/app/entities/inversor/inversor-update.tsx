import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getSitios } from 'app/entities/sitio/sitio.reducer';
import { getEntities as getMarcas } from 'app/entities/marca/marca.reducer';
import { getEntities as getModeloInversors } from 'app/entities/modelo-inversor/modelo-inversor.reducer';
import { getEntities as getGatewayTcps } from 'app/entities/gateway-tcp/gateway-tcp.reducer';
import { getEntities as getInversors } from 'app/entities/inversor/inversor.reducer';
import { TipoConexion } from 'app/shared/model/enumerations/tipo-conexion.model';
import { EstadoDispositivo } from 'app/shared/model/enumerations/estado-dispositivo.model';
import { createEntity, getEntity, reset, updateEntity } from './inversor.reducer';

export const InversorUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const sitios = useAppSelector(state => state.sitio.entities);
  const marcas = useAppSelector(state => state.marca.entities);
  const modeloInversors = useAppSelector(state => state.modeloInversor.entities);
  const gatewayTcps = useAppSelector(state => state.gatewayTcp.entities);
  const inversors = useAppSelector(state => state.inversor.entities);
  const inversorEntity = useAppSelector(state => state.inversor.entity);
  const loading = useAppSelector(state => state.inversor.loading);
  const updating = useAppSelector(state => state.inversor.updating);
  const updateSuccess = useAppSelector(state => state.inversor.updateSuccess);
  const tipoConexionValues = Object.keys(TipoConexion);
  const estadoDispositivoValues = Object.keys(EstadoDispositivo);

  const handleClose = () => {
    navigate(`/inversor${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSitios({}));
    dispatch(getMarcas({}));
    dispatch(getModeloInversors({}));
    dispatch(getGatewayTcps({}));
    dispatch(getInversors({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.slaveId !== undefined && typeof values.slaveId !== 'number') {
      values.slaveId = Number(values.slaveId);
    }
    values.ultimaLectura = convertDateTimeToServer(values.ultimaLectura);
    if (values.periodoMuestreoSeg !== undefined && typeof values.periodoMuestreoSeg !== 'number') {
      values.periodoMuestreoSeg = Number(values.periodoMuestreoSeg);
    }
    values.fechaRegistro = convertDateTimeToServer(values.fechaRegistro);
    values.ultimaConexion = convertDateTimeToServer(values.ultimaConexion);

    const entity = {
      ...inversorEntity,
      ...values,
      sitio: sitios.find(it => it.id.toString() === values.sitio?.toString()),
      marca: marcas.find(it => it.id.toString() === values.marca?.toString()),
      modelo: modeloInversors.find(it => it.id.toString() === values.modelo?.toString()),
      gateway: gatewayTcps.find(it => it.id.toString() === values.gateway?.toString()),
      masterReferencia: inversors.find(it => it.id.toString() === values.masterReferencia?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          ultimaLectura: displayDefaultDateTime(),
          fechaRegistro: displayDefaultDateTime(),
          ultimaConexion: displayDefaultDateTime(),
        }
      : {
          tipoConexion: 'SerialRs485',
          estadoDispositivo: 'Conectado',
          ...inversorEntity,
          ultimaLectura: convertDateTimeFromServer(inversorEntity.ultimaLectura),
          fechaRegistro: convertDateTimeFromServer(inversorEntity.fechaRegistro),
          ultimaConexion: convertDateTimeFromServer(inversorEntity.ultimaConexion),
          sitio: inversorEntity?.sitio?.id,
          marca: inversorEntity?.marca?.id,
          modelo: inversorEntity?.modelo?.id,
          gateway: inversorEntity?.gateway?.id,
          masterReferencia: inversorEntity?.masterReferencia?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="inversorMonitorApp.inversor.home.createOrEditLabel" data-cy="InversorCreateUpdateHeading">
            <Translate contentKey="inversorMonitorApp.inversor.home.createOrEditLabel">Create or edit a Inversor</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="inversor-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('inversorMonitorApp.inversor.nombre')}
                id="inversor-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.inversor.numeroSerie')}
                id="inversor-numeroSerie"
                name="numeroSerie"
                data-cy="numeroSerie"
                type="text"
                validate={{
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.inversor.esMaster')}
                id="inversor-esMaster"
                name="esMaster"
                data-cy="esMaster"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.inversor.tipoConexion')}
                id="inversor-tipoConexion"
                name="tipoConexion"
                data-cy="tipoConexion"
                type="select"
              >
                {tipoConexionValues.map(tipoConexion => (
                  <option value={tipoConexion} key={tipoConexion}>
                    {translate(`inversorMonitorApp.TipoConexion.${tipoConexion}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('inversorMonitorApp.inversor.puertoSerial')}
                id="inversor-puertoSerial"
                name="puertoSerial"
                data-cy="puertoSerial"
                type="text"
                validate={{
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.inversor.slaveId')}
                id="inversor-slaveId"
                name="slaveId"
                data-cy="slaveId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.inversor.activo')}
                id="inversor-activo"
                name="activo"
                data-cy="activo"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.inversor.estadoDispositivo')}
                id="inversor-estadoDispositivo"
                name="estadoDispositivo"
                data-cy="estadoDispositivo"
                type="select"
              >
                {estadoDispositivoValues.map(estadoDispositivo => (
                  <option value={estadoDispositivo} key={estadoDispositivo}>
                    {translate(`inversorMonitorApp.EstadoDispositivo.${estadoDispositivo}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('inversorMonitorApp.inversor.ultimaLectura')}
                id="inversor-ultimaLectura"
                name="ultimaLectura"
                data-cy="ultimaLectura"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.inversor.ultimoError')}
                id="inversor-ultimoError"
                name="ultimoError"
                data-cy="ultimoError"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.inversor.periodoMuestreoSeg')}
                id="inversor-periodoMuestreoSeg"
                name="periodoMuestreoSeg"
                data-cy="periodoMuestreoSeg"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.inversor.fechaRegistro')}
                id="inversor-fechaRegistro"
                name="fechaRegistro"
                data-cy="fechaRegistro"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.inversor.ultimaConexion')}
                id="inversor-ultimaConexion"
                name="ultimaConexion"
                data-cy="ultimaConexion"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="inversor-sitio"
                name="sitio"
                data-cy="sitio"
                label={translate('inversorMonitorApp.inversor.sitio')}
                type="select"
              >
                <option value="" key="0" />
                {sitios
                  ? sitios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="inversor-marca"
                name="marca"
                data-cy="marca"
                label={translate('inversorMonitorApp.inversor.marca')}
                type="select"
              >
                <option value="" key="0" />
                {marcas
                  ? marcas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="inversor-modelo"
                name="modelo"
                data-cy="modelo"
                label={translate('inversorMonitorApp.inversor.modelo')}
                type="select"
              >
                <option value="" key="0" />
                {modeloInversors
                  ? modeloInversors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="inversor-gateway"
                name="gateway"
                data-cy="gateway"
                label={translate('inversorMonitorApp.inversor.gateway')}
                type="select"
              >
                <option value="" key="0" />
                {gatewayTcps
                  ? gatewayTcps.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="inversor-masterReferencia"
                name="masterReferencia"
                data-cy="masterReferencia"
                label={translate('inversorMonitorApp.inversor.masterReferencia')}
                type="select"
              >
                <option value="" key="0" />
                {inversors
                  ? inversors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/inversor" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default InversorUpdate;
