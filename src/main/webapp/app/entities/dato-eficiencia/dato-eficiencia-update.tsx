import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getInversors } from 'app/entities/inversor/inversor.reducer';
import { createEntity, getEntity, reset, updateEntity } from './dato-eficiencia.reducer';

export const DatoEficienciaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const inversors = useAppSelector(state => state.inversor.entities);
  const datoEficienciaEntity = useAppSelector(state => state.datoEficiencia.entity);
  const loading = useAppSelector(state => state.datoEficiencia.loading);
  const updating = useAppSelector(state => state.datoEficiencia.updating);
  const updateSuccess = useAppSelector(state => state.datoEficiencia.updateSuccess);

  const handleClose = () => {
    navigate(`/dato-eficiencia${location.search}`);
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
    if (values.energiaPvWh !== undefined && typeof values.energiaPvWh !== 'number') {
      values.energiaPvWh = Number(values.energiaPvWh);
    }
    if (values.energiaCargaWh !== undefined && typeof values.energiaCargaWh !== 'number') {
      values.energiaCargaWh = Number(values.energiaCargaWh);
    }
    if (values.energiaRedWh !== undefined && typeof values.energiaRedWh !== 'number') {
      values.energiaRedWh = Number(values.energiaRedWh);
    }
    if (values.eficienciaPromPct !== undefined && typeof values.eficienciaPromPct !== 'number') {
      values.eficienciaPromPct = Number(values.eficienciaPromPct);
    }
    if (values.horasSol !== undefined && typeof values.horasSol !== 'number') {
      values.horasSol = Number(values.horasSol);
    }
    if (values.picoPvW !== undefined && typeof values.picoPvW !== 'number') {
      values.picoPvW = Number(values.picoPvW);
    }
    if (values.picoCargaW !== undefined && typeof values.picoCargaW !== 'number') {
      values.picoCargaW = Number(values.picoCargaW);
    }
    if (values.maxBateriaV !== undefined && typeof values.maxBateriaV !== 'number') {
      values.maxBateriaV = Number(values.maxBateriaV);
    }
    if (values.minBateriaV !== undefined && typeof values.minBateriaV !== 'number') {
      values.minBateriaV = Number(values.minBateriaV);
    }
    if (values.totalLecturas !== undefined && typeof values.totalLecturas !== 'number') {
      values.totalLecturas = Number(values.totalLecturas);
    }
    values.calculadoEn = convertDateTimeToServer(values.calculadoEn);

    const entity = {
      ...datoEficienciaEntity,
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
      ? {
          calculadoEn: displayDefaultDateTime(),
        }
      : {
          ...datoEficienciaEntity,
          calculadoEn: convertDateTimeFromServer(datoEficienciaEntity.calculadoEn),
          inversor: datoEficienciaEntity?.inversor?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="inversorMonitorApp.datoEficiencia.home.createOrEditLabel" data-cy="DatoEficienciaCreateUpdateHeading">
            <Translate contentKey="inversorMonitorApp.datoEficiencia.home.createOrEditLabel">Create or edit a DatoEficiencia</Translate>
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
                  id="dato-eficiencia-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('inversorMonitorApp.datoEficiencia.fecha')}
                id="dato-eficiencia-fecha"
                name="fecha"
                data-cy="fecha"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoEficiencia.energiaPvWh')}
                id="dato-eficiencia-energiaPvWh"
                name="energiaPvWh"
                data-cy="energiaPvWh"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoEficiencia.energiaCargaWh')}
                id="dato-eficiencia-energiaCargaWh"
                name="energiaCargaWh"
                data-cy="energiaCargaWh"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoEficiencia.energiaRedWh')}
                id="dato-eficiencia-energiaRedWh"
                name="energiaRedWh"
                data-cy="energiaRedWh"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoEficiencia.eficienciaPromPct')}
                id="dato-eficiencia-eficienciaPromPct"
                name="eficienciaPromPct"
                data-cy="eficienciaPromPct"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoEficiencia.horasSol')}
                id="dato-eficiencia-horasSol"
                name="horasSol"
                data-cy="horasSol"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoEficiencia.picoPvW')}
                id="dato-eficiencia-picoPvW"
                name="picoPvW"
                data-cy="picoPvW"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoEficiencia.picoCargaW')}
                id="dato-eficiencia-picoCargaW"
                name="picoCargaW"
                data-cy="picoCargaW"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoEficiencia.maxBateriaV')}
                id="dato-eficiencia-maxBateriaV"
                name="maxBateriaV"
                data-cy="maxBateriaV"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoEficiencia.minBateriaV')}
                id="dato-eficiencia-minBateriaV"
                name="minBateriaV"
                data-cy="minBateriaV"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoEficiencia.totalLecturas')}
                id="dato-eficiencia-totalLecturas"
                name="totalLecturas"
                data-cy="totalLecturas"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoEficiencia.calculadoEn')}
                id="dato-eficiencia-calculadoEn"
                name="calculadoEn"
                data-cy="calculadoEn"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="dato-eficiencia-inversor"
                name="inversor"
                data-cy="inversor"
                label={translate('inversorMonitorApp.datoEficiencia.inversor')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/dato-eficiencia" replace color="info">
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

export default DatoEficienciaUpdate;
