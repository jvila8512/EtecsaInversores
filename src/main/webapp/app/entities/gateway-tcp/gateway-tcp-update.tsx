import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getSitios } from 'app/entities/sitio/sitio.reducer';
import { EstadoDispositivo } from 'app/shared/model/enumerations/estado-dispositivo.model';
import { createEntity, getEntity, reset, updateEntity } from './gateway-tcp.reducer';

export const GatewayTcpUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const sitios = useAppSelector(state => state.sitio.entities);
  const gatewayTcpEntity = useAppSelector(state => state.gatewayTcp.entity);
  const loading = useAppSelector(state => state.gatewayTcp.loading);
  const updating = useAppSelector(state => state.gatewayTcp.updating);
  const updateSuccess = useAppSelector(state => state.gatewayTcp.updateSuccess);
  const estadoDispositivoValues = Object.keys(EstadoDispositivo);

  const handleClose = () => {
    navigate(`/gateway-tcp${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSitios({}));
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
    if (values.puerto !== undefined && typeof values.puerto !== 'number') {
      values.puerto = Number(values.puerto);
    }
    values.ultimaLectura = convertDateTimeToServer(values.ultimaLectura);
    if (values.tiempoEsperaMs !== undefined && typeof values.tiempoEsperaMs !== 'number') {
      values.tiempoEsperaMs = Number(values.tiempoEsperaMs);
    }
    if (values.reintentos !== undefined && typeof values.reintentos !== 'number') {
      values.reintentos = Number(values.reintentos);
    }

    const entity = {
      ...gatewayTcpEntity,
      ...values,
      sitio: sitios.find(it => it.id.toString() === values.sitio?.toString()),
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
        }
      : {
          estadoConexion: 'Conectado',
          ...gatewayTcpEntity,
          ultimaLectura: convertDateTimeFromServer(gatewayTcpEntity.ultimaLectura),
          sitio: gatewayTcpEntity?.sitio?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="inversorMonitorApp.gatewayTcp.home.createOrEditLabel" data-cy="GatewayTcpCreateUpdateHeading">
            <Translate contentKey="inversorMonitorApp.gatewayTcp.home.createOrEditLabel">Create or edit a GatewayTcp</Translate>
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
                  id="gateway-tcp-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('inversorMonitorApp.gatewayTcp.nombre')}
                id="gateway-tcp-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.gatewayTcp.descripcion')}
                id="gateway-tcp-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.gatewayTcp.ubicacion')}
                id="gateway-tcp-ubicacion"
                name="ubicacion"
                data-cy="ubicacion"
                type="text"
                validate={{
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.gatewayTcp.direccionIp')}
                id="gateway-tcp-direccionIp"
                name="direccionIp"
                data-cy="direccionIp"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 45, message: translate('entity.validation.maxlength', { max: 45 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.gatewayTcp.puerto')}
                id="gateway-tcp-puerto"
                name="puerto"
                data-cy="puerto"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.gatewayTcp.activo')}
                id="gateway-tcp-activo"
                name="activo"
                data-cy="activo"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.gatewayTcp.estadoConexion')}
                id="gateway-tcp-estadoConexion"
                name="estadoConexion"
                data-cy="estadoConexion"
                type="select"
              >
                {estadoDispositivoValues.map(estadoDispositivo => (
                  <option value={estadoDispositivo} key={estadoDispositivo}>
                    {translate(`inversorMonitorApp.EstadoDispositivo.${estadoDispositivo}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('inversorMonitorApp.gatewayTcp.ultimaLectura')}
                id="gateway-tcp-ultimaLectura"
                name="ultimaLectura"
                data-cy="ultimaLectura"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.gatewayTcp.ultimoError')}
                id="gateway-tcp-ultimoError"
                name="ultimoError"
                data-cy="ultimoError"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.gatewayTcp.tiempoEsperaMs')}
                id="gateway-tcp-tiempoEsperaMs"
                name="tiempoEsperaMs"
                data-cy="tiempoEsperaMs"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.gatewayTcp.reintentos')}
                id="gateway-tcp-reintentos"
                name="reintentos"
                data-cy="reintentos"
                type="text"
              />
              <ValidatedField
                id="gateway-tcp-sitio"
                name="sitio"
                data-cy="sitio"
                label={translate('inversorMonitorApp.gatewayTcp.sitio')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/gateway-tcp" replace color="info">
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

export default GatewayTcpUpdate;
