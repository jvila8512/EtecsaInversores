import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getInversors } from 'app/entities/inversor/inversor.reducer';
import { createEntity, getEntity, updateEntity } from './dato-inversor.reducer';

export const DatoInversorUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const inversors = useAppSelector(state => state.inversor.entities);
  const datoInversorEntity = useAppSelector(state => state.datoInversor.entity);
  const loading = useAppSelector(state => state.datoInversor.loading);
  const updating = useAppSelector(state => state.datoInversor.updating);
  const updateSuccess = useAppSelector(state => state.datoInversor.updateSuccess);

  const handleClose = () => {
    navigate('/dato-inversor');
  };

  useEffect(() => {
    if (!isNew) {
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
    values.tiempo = convertDateTimeToServer(values.tiempo);
    if (values.bateriaVoltajeV !== undefined && typeof values.bateriaVoltajeV !== 'number') {
      values.bateriaVoltajeV = Number(values.bateriaVoltajeV);
    }
    if (values.bateriaCorrienteA !== undefined && typeof values.bateriaCorrienteA !== 'number') {
      values.bateriaCorrienteA = Number(values.bateriaCorrienteA);
    }
    if (values.bateriaPotenciaW !== undefined && typeof values.bateriaPotenciaW !== 'number') {
      values.bateriaPotenciaW = Number(values.bateriaPotenciaW);
    }
    if (values.pvVoltajeV !== undefined && typeof values.pvVoltajeV !== 'number') {
      values.pvVoltajeV = Number(values.pvVoltajeV);
    }
    if (values.pvPotenciaW !== undefined && typeof values.pvPotenciaW !== 'number') {
      values.pvPotenciaW = Number(values.pvPotenciaW);
    }
    if (values.salidaVoltajeV !== undefined && typeof values.salidaVoltajeV !== 'number') {
      values.salidaVoltajeV = Number(values.salidaVoltajeV);
    }
    if (values.salidaPotenciaW !== undefined && typeof values.salidaPotenciaW !== 'number') {
      values.salidaPotenciaW = Number(values.salidaPotenciaW);
    }
    if (values.salidaAparenteVa !== undefined && typeof values.salidaAparenteVa !== 'number') {
      values.salidaAparenteVa = Number(values.salidaAparenteVa);
    }
    if (values.cargaPorcentaje !== undefined && typeof values.cargaPorcentaje !== 'number') {
      values.cargaPorcentaje = Number(values.cargaPorcentaje);
    }
    if (values.redVoltajeV !== undefined && typeof values.redVoltajeV !== 'number') {
      values.redVoltajeV = Number(values.redVoltajeV);
    }
    if (values.redFrecuenciaHz !== undefined && typeof values.redFrecuenciaHz !== 'number') {
      values.redFrecuenciaHz = Number(values.redFrecuenciaHz);
    }
    if (values.codigoFallo !== undefined && typeof values.codigoFallo !== 'number') {
      values.codigoFallo = Number(values.codigoFallo);
    }
    if (values.eficienciaPct !== undefined && typeof values.eficienciaPct !== 'number') {
      values.eficienciaPct = Number(values.eficienciaPct);
    }
    if (values.consumoRedW !== undefined && typeof values.consumoRedW !== 'number') {
      values.consumoRedW = Number(values.consumoRedW);
    }
    if (values.latenciaMs !== undefined && typeof values.latenciaMs !== 'number') {
      values.latenciaMs = Number(values.latenciaMs);
    }

    const entity = {
      ...datoInversorEntity,
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
          tiempo: displayDefaultDateTime(),
        }
      : {
          ...datoInversorEntity,
          tiempo: convertDateTimeFromServer(datoInversorEntity.tiempo),
          inversor: datoInversorEntity?.inversor?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="inversorMonitorApp.datoInversor.home.createOrEditLabel" data-cy="DatoInversorCreateUpdateHeading">
            <Translate contentKey="inversorMonitorApp.datoInversor.home.createOrEditLabel">Create or edit a DatoInversor</Translate>
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
                  id="dato-inversor-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.tiempo')}
                id="dato-inversor-tiempo"
                name="tiempo"
                data-cy="tiempo"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.bateriaVoltajeV')}
                id="dato-inversor-bateriaVoltajeV"
                name="bateriaVoltajeV"
                data-cy="bateriaVoltajeV"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.bateriaCorrienteA')}
                id="dato-inversor-bateriaCorrienteA"
                name="bateriaCorrienteA"
                data-cy="bateriaCorrienteA"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.bateriaPotenciaW')}
                id="dato-inversor-bateriaPotenciaW"
                name="bateriaPotenciaW"
                data-cy="bateriaPotenciaW"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.pvVoltajeV')}
                id="dato-inversor-pvVoltajeV"
                name="pvVoltajeV"
                data-cy="pvVoltajeV"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.pvPotenciaW')}
                id="dato-inversor-pvPotenciaW"
                name="pvPotenciaW"
                data-cy="pvPotenciaW"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.salidaVoltajeV')}
                id="dato-inversor-salidaVoltajeV"
                name="salidaVoltajeV"
                data-cy="salidaVoltajeV"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.salidaPotenciaW')}
                id="dato-inversor-salidaPotenciaW"
                name="salidaPotenciaW"
                data-cy="salidaPotenciaW"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.salidaAparenteVa')}
                id="dato-inversor-salidaAparenteVa"
                name="salidaAparenteVa"
                data-cy="salidaAparenteVa"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.cargaPorcentaje')}
                id="dato-inversor-cargaPorcentaje"
                name="cargaPorcentaje"
                data-cy="cargaPorcentaje"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.redVoltajeV')}
                id="dato-inversor-redVoltajeV"
                name="redVoltajeV"
                data-cy="redVoltajeV"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.redFrecuenciaHz')}
                id="dato-inversor-redFrecuenciaHz"
                name="redFrecuenciaHz"
                data-cy="redFrecuenciaHz"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.modoOperacion')}
                id="dato-inversor-modoOperacion"
                name="modoOperacion"
                data-cy="modoOperacion"
                type="text"
                validate={{
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                }}
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.codigoFallo')}
                id="dato-inversor-codigoFallo"
                name="codigoFallo"
                data-cy="codigoFallo"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.eficienciaPct')}
                id="dato-inversor-eficienciaPct"
                name="eficienciaPct"
                data-cy="eficienciaPct"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.consumoRedW')}
                id="dato-inversor-consumoRedW"
                name="consumoRedW"
                data-cy="consumoRedW"
                type="text"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.bateriaCargando')}
                id="dato-inversor-bateriaCargando"
                name="bateriaCargando"
                data-cy="bateriaCargando"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.pvActivo')}
                id="dato-inversor-pvActivo"
                name="pvActivo"
                data-cy="pvActivo"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.redActiva')}
                id="dato-inversor-redActiva"
                name="redActiva"
                data-cy="redActiva"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.crcValido')}
                id="dato-inversor-crcValido"
                name="crcValido"
                data-cy="crcValido"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('inversorMonitorApp.datoInversor.latenciaMs')}
                id="dato-inversor-latenciaMs"
                name="latenciaMs"
                data-cy="latenciaMs"
                type="text"
              />
              <ValidatedField
                id="dato-inversor-inversor"
                name="inversor"
                data-cy="inversor"
                label={translate('inversorMonitorApp.datoInversor.inversor')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/dato-inversor" replace color="info">
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

export default DatoInversorUpdate;
