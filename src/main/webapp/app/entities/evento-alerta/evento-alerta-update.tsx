import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getInversors } from 'app/entities/inversor/inversor.reducer';
import { getEntities as getAlertaConfigs } from 'app/entities/alerta-config/alerta-config.reducer';
import { createEntity, getEntity, updateEntity } from './evento-alerta.reducer';

export const EventoAlertaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const inversors = useAppSelector(state => state.inversor.entities);
  const alertaConfigs = useAppSelector(state => state.alertaConfig.entities);
  const eventoAlertaEntity = useAppSelector(state => state.eventoAlerta.entity);
  const loading = useAppSelector(state => state.eventoAlerta.loading);
  const updating = useAppSelector(state => state.eventoAlerta.updating);
  const updateSuccess = useAppSelector(state => state.eventoAlerta.updateSuccess);

  const handleClose = () => {
    navigate('/evento-alerta');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getInversors({}));
    dispatch(getAlertaConfigs({}));
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
    values.timestamp = convertDateTimeToServer(values.timestamp);
    if (values.valorDetectado !== undefined && typeof values.valorDetectado !== 'number') {
      values.valorDetectado = Number(values.valorDetectado);
    }
    if (values.umbral !== undefined && typeof values.umbral !== 'number') {
      values.umbral = Number(values.umbral);
    }
    values.fechaResolucion = convertDateTimeToServer(values.fechaResolucion);

    const entity = {
      ...eventoAlertaEntity,
      ...values,
      inversor: inversors.find(it => it.id.toString() === values.inversor?.toString()),
      alertaConfig: alertaConfigs.find(it => it.id.toString() === values.alertaConfig?.toString()),
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
          timestamp: displayDefaultDateTime(),
          fechaResolucion: displayDefaultDateTime(),
        }
      : {
          ...eventoAlertaEntity,
          timestamp: convertDateTimeFromServer(eventoAlertaEntity.timestamp),
          fechaResolucion: convertDateTimeFromServer(eventoAlertaEntity.fechaResolucion),
          inversor: eventoAlertaEntity?.inversor?.id,
          alertaConfig: eventoAlertaEntity?.alertaConfig?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="inversorMonitorApp.eventoAlerta.home.createOrEditLabel" data-cy="EventoAlertaCreateUpdateHeading">
            <Translate contentKey="inversorMonitorApp.eventoAlerta.home.createOrEditLabel">Create or edit a EventoAlerta</Translate>
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
                  id="evento-alerta-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('inversorMonitorApp.eventoAlerta.timestamp')}
                id="evento-alerta-timestamp"
                name="timestamp"
                data-cy="timestamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.eventoAlerta.nombreAlerta')}
                id="evento-alerta-nombreAlerta"
                name="nombreAlerta"
                data-cy="nombreAlerta"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.eventoAlerta.mensaje')}
                id="evento-alerta-mensaje"
                name="mensaje"
                data-cy="mensaje"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.eventoAlerta.campoMonitorizado')}
                id="evento-alerta-campoMonitorizado"
                name="campoMonitorizado"
                data-cy="campoMonitorizado"
                type="text"
                validate={{
                  maxLength: { value: 60, message: translate('entity.validation.maxlength', { max: 60 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.eventoAlerta.valorDetectado')}
                id="evento-alerta-valorDetectado"
                name="valorDetectado"
                data-cy="valorDetectado"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.eventoAlerta.umbral')}
                id="evento-alerta-umbral"
                name="umbral"
                data-cy="umbral"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.eventoAlerta.operador')}
                id="evento-alerta-operador"
                name="operador"
                data-cy="operador"
                type="text"
                validate={{
                  maxLength: { value: 5, message: translate('entity.validation.maxlength', { max: 5 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.eventoAlerta.nivelSeveridad')}
                id="evento-alerta-nivelSeveridad"
                name="nivelSeveridad"
                data-cy="nivelSeveridad"
                type="text"
                validate={{
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.eventoAlerta.notificacionEnviada')}
                id="evento-alerta-notificacionEnviada"
                name="notificacionEnviada"
                data-cy="notificacionEnviada"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.eventoAlerta.canalUsado')}
                id="evento-alerta-canalUsado"
                name="canalUsado"
                data-cy="canalUsado"
                type="text"
                validate={{
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.eventoAlerta.resuelto')}
                id="evento-alerta-resuelto"
                name="resuelto"
                data-cy="resuelto"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.eventoAlerta.fechaResolucion')}
                id="evento-alerta-fechaResolucion"
                name="fechaResolucion"
                data-cy="fechaResolucion"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.eventoAlerta.notas')}
                id="evento-alerta-notas"
                name="notas"
                data-cy="notas"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                id="evento-alerta-inversor"
                name="inversor"
                data-cy="inversor"
                label={translate('inversorMonitorApp.eventoAlerta.inversor')}
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
              <ValidatedField
                id="evento-alerta-alertaConfig"
                name="alertaConfig"
                data-cy="alertaConfig"
                label={translate('inversorMonitorApp.eventoAlerta.alertaConfig')}
                type="select"
              >
                <option value="" key="0" />
                {alertaConfigs
                  ? alertaConfigs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/evento-alerta" replace color="info">
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

export default EventoAlertaUpdate;
