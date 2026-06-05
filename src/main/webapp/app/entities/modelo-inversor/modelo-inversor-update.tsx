import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getMarcas } from 'app/entities/marca/marca.reducer';
import { createEntity, getEntity, reset, updateEntity } from './modelo-inversor.reducer';

export const ModeloInversorUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const marcas = useAppSelector(state => state.marca.entities);
  const modeloInversorEntity = useAppSelector(state => state.modeloInversor.entity);
  const loading = useAppSelector(state => state.modeloInversor.loading);
  const updating = useAppSelector(state => state.modeloInversor.updating);
  const updateSuccess = useAppSelector(state => state.modeloInversor.updateSuccess);

  const handleClose = () => {
    navigate(`/modelo-inversor${location.search}`);
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
    if (values.capacidadVa !== undefined && typeof values.capacidadVa !== 'number') {
      values.capacidadVa = Number(values.capacidadVa);
    }
    if (values.voltajeSistemaNom !== undefined && typeof values.voltajeSistemaNom !== 'number') {
      values.voltajeSistemaNom = Number(values.voltajeSistemaNom);
    }

    const entity = {
      ...modeloInversorEntity,
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
          ...modeloInversorEntity,
          marca: modeloInversorEntity?.marca?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="inversorMonitorApp.modeloInversor.home.createOrEditLabel" data-cy="ModeloInversorCreateUpdateHeading">
            <Translate contentKey="inversorMonitorApp.modeloInversor.home.createOrEditLabel">Create or edit a ModeloInversor</Translate>
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
                  id="modelo-inversor-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('inversorMonitorApp.modeloInversor.nombre')}
                id="modelo-inversor-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.modeloInversor.capacidadVa')}
                id="modelo-inversor-capacidadVa"
                name="capacidadVa"
                data-cy="capacidadVa"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.modeloInversor.voltajeSistemaNom')}
                id="modelo-inversor-voltajeSistemaNom"
                name="voltajeSistemaNom"
                data-cy="voltajeSistemaNom"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.modeloInversor.descripcion')}
                id="modelo-inversor-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                id="modelo-inversor-marca"
                name="marca"
                data-cy="marca"
                label={translate('inversorMonitorApp.modeloInversor.marca')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/modelo-inversor" replace color="info">
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

export default ModeloInversorUpdate;
