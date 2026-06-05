import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getMarcas } from 'app/entities/marca/marca.reducer';
import { TipoDato } from 'app/shared/model/enumerations/tipo-dato.model';
import { createEntity, getEntity, reset, updateEntity } from './mapeo-registro.reducer';

export const MapeoRegistroUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const marcas = useAppSelector(state => state.marca.entities);
  const mapeoRegistroEntity = useAppSelector(state => state.mapeoRegistro.entity);
  const loading = useAppSelector(state => state.mapeoRegistro.loading);
  const updating = useAppSelector(state => state.mapeoRegistro.updating);
  const updateSuccess = useAppSelector(state => state.mapeoRegistro.updateSuccess);
  const tipoDatoValues = Object.keys(TipoDato);

  const handleClose = () => {
    navigate(`/mapeo-registro${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMarcas({}));
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
    if (values.direccionRegistro !== undefined && typeof values.direccionRegistro !== 'number') {
      values.direccionRegistro = Number(values.direccionRegistro);
    }
    if (values.escala !== undefined && typeof values.escala !== 'number') {
      values.escala = Number(values.escala);
    }

    const entity = {
      ...mapeoRegistroEntity,
      ...values,
      marca: marcas.find(it => it.id.toString() === values.marca?.toString()),
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
          tipoDato: 'UnsignedInt16',
          ...mapeoRegistroEntity,
          marca: mapeoRegistroEntity?.marca?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="inversorMonitorApp.mapeoRegistro.home.createOrEditLabel" data-cy="MapeoRegistroCreateUpdateHeading">
            <Translate contentKey="inversorMonitorApp.mapeoRegistro.home.createOrEditLabel">Create or edit a MapeoRegistro</Translate>
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
                  id="mapeo-registro-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('inversorMonitorApp.mapeoRegistro.direccionRegistro')}
                id="mapeo-registro-direccionRegistro"
                name="direccionRegistro"
                data-cy="direccionRegistro"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.mapeoRegistro.nombreRegistro')}
                id="mapeo-registro-nombreRegistro"
                name="nombreRegistro"
                data-cy="nombreRegistro"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.mapeoRegistro.campoSql')}
                id="mapeo-registro-campoSql"
                name="campoSql"
                data-cy="campoSql"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 60, message: translate('entity.validation.maxlength', { max: 60 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.mapeoRegistro.tipoDato')}
                id="mapeo-registro-tipoDato"
                name="tipoDato"
                data-cy="tipoDato"
                type="select"
              >
                {tipoDatoValues.map(tipoDato => (
                  <option value={tipoDato} key={tipoDato}>
                    {translate(`inversorMonitorApp.TipoDato.${tipoDato}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('inversorMonitorApp.mapeoRegistro.escala')}
                id="mapeo-registro-escala"
                name="escala"
                data-cy="escala"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.mapeoRegistro.unidad')}
                id="mapeo-registro-unidad"
                name="unidad"
                data-cy="unidad"
                type="text"
                validate={{
                  maxLength: { value: 10, message: translate('entity.validation.maxlength', { max: 10 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.mapeoRegistro.activo')}
                id="mapeo-registro-activo"
                name="activo"
                data-cy="activo"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.mapeoRegistro.descripcion')}
                id="mapeo-registro-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                id="mapeo-registro-marca"
                name="marca"
                data-cy="marca"
                label={translate('inversorMonitorApp.mapeoRegistro.marca')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/mapeo-registro" replace color="info">
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

export default MapeoRegistroUpdate;
