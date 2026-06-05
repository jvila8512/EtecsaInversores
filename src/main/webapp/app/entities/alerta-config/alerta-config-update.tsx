import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getInversors } from 'app/entities/inversor/inversor.reducer';
import { createEntity, getEntity, reset, updateEntity } from './alerta-config.reducer';

export const AlertaConfigUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const inversors = useAppSelector(state => state.inversor.entities);
  const alertaConfigEntity = useAppSelector(state => state.alertaConfig.entity);
  const loading = useAppSelector(state => state.alertaConfig.loading);
  const updating = useAppSelector(state => state.alertaConfig.updating);
  const updateSuccess = useAppSelector(state => state.alertaConfig.updateSuccess);

  const handleClose = () => {
    navigate(`/alerta-config${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    if (values.umbral !== undefined && typeof values.umbral !== 'number') {
      values.umbral = Number(values.umbral);
    }
    if (values.cooldownMinutos !== undefined && typeof values.cooldownMinutos !== 'number') {
      values.cooldownMinutos = Number(values.cooldownMinutos);
    }

    const entity = {
      ...alertaConfigEntity,
      ...values,
      inversor: inversors.find(it => it.id.toString() === values.inversor?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...alertaConfigEntity,
          inversor: alertaConfigEntity?.inversor?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="inversorMonitorApp.alertaConfig.home.createOrEditLabel" data-cy="AlertaConfigCreateUpdateHeading">
            <Translate contentKey="inversorMonitorApp.alertaConfig.home.createOrEditLabel">Create or edit a AlertaConfig</Translate>
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
                  id="alerta-config-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('inversorMonitorApp.alertaConfig.nombre')}
                id="alerta-config-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.alertaConfig.descripcion')}
                id="alerta-config-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.alertaConfig.campoMonitorizado')}
                id="alerta-config-campoMonitorizado"
                name="campoMonitorizado"
                data-cy="campoMonitorizado"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 60, message: translate('entity.validation.maxlength', { max: 60 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.alertaConfig.operador')}
                id="alerta-config-operador"
                name="operador"
                data-cy="operador"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 5, message: translate('entity.validation.maxlength', { max: 5 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.alertaConfig.umbral')}
                id="alerta-config-umbral"
                name="umbral"
                data-cy="umbral"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.alertaConfig.activa')}
                id="alerta-config-activa"
                name="activa"
                data-cy="activa"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.alertaConfig.nivelSeveridad')}
                id="alerta-config-nivelSeveridad"
                name="nivelSeveridad"
                data-cy="nivelSeveridad"
                type="text"
                validate={{
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.alertaConfig.canalNotificacion')}
                id="alerta-config-canalNotificacion"
                name="canalNotificacion"
                data-cy="canalNotificacion"
                type="text"
                validate={{
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.alertaConfig.cooldownMinutos')}
                id="alerta-config-cooldownMinutos"
                name="cooldownMinutos"
                data-cy="cooldownMinutos"
                type="text"
              />
              <ValidatedField
                id="alerta-config-inversor"
                name="inversor"
                data-cy="inversor"
                label={translate('inversorMonitorApp.alertaConfig.inversor')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/alerta-config" replace color="info">
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

export default AlertaConfigUpdate;
