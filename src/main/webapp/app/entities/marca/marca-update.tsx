import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './marca.reducer';

export const MarcaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const marcaEntity = useAppSelector(state => state.marca.entity);
  const loading = useAppSelector(state => state.marca.loading);
  const updating = useAppSelector(state => state.marca.updating);
  const updateSuccess = useAppSelector(state => state.marca.updateSuccess);

  const handleClose = () => {
    navigate(`/marca${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.registroInicio !== undefined && typeof values.registroInicio !== 'number') {
      values.registroInicio = Number(values.registroInicio);
    }
    if (values.registroFin !== undefined && typeof values.registroFin !== 'number') {
      values.registroFin = Number(values.registroFin);
    }
    if (values.registroInfoInicio !== undefined && typeof values.registroInfoInicio !== 'number') {
      values.registroInfoInicio = Number(values.registroInfoInicio);
    }
    if (values.registroInfoFin !== undefined && typeof values.registroInfoFin !== 'number') {
      values.registroInfoFin = Number(values.registroInfoFin);
    }
    if (values.baudRateDefault !== undefined && typeof values.baudRateDefault !== 'number') {
      values.baudRateDefault = Number(values.baudRateDefault);
    }

    const entity = {
      ...marcaEntity,
      ...values,
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
          ...marcaEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="inversorMonitorApp.marca.home.createOrEditLabel" data-cy="MarcaCreateUpdateHeading">
            <Translate contentKey="inversorMonitorApp.marca.home.createOrEditLabel">Create or edit a Marca</Translate>
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
                  id="marca-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('inversorMonitorApp.marca.nombre')}
                id="marca-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.marca.fabricante')}
                id="marca-fabricante"
                name="fabricante"
                data-cy="fabricante"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.marca.registroInicio')}
                id="marca-registroInicio"
                name="registroInicio"
                data-cy="registroInicio"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.marca.registroFin')}
                id="marca-registroFin"
                name="registroFin"
                data-cy="registroFin"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.marca.registroInfoInicio')}
                id="marca-registroInfoInicio"
                name="registroInfoInicio"
                data-cy="registroInfoInicio"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.marca.registroInfoFin')}
                id="marca-registroInfoFin"
                name="registroInfoFin"
                data-cy="registroInfoFin"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.marca.baudRateDefault')}
                id="marca-baudRateDefault"
                name="baudRateDefault"
                data-cy="baudRateDefault"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.marca.descripcion')}
                id="marca-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.marca.activaMarcaDefault')}
                id="marca-activaMarcaDefault"
                name="activaMarcaDefault"
                data-cy="activaMarcaDefault"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/marca" replace color="info">
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

export default MarcaUpdate;
