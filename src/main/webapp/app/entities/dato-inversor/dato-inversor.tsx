import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './dato-inversor.reducer';

export const DatoInversor = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const datoInversorList = useAppSelector(state => state.datoInversor.entities);
  const loading = useAppSelector(state => state.datoInversor.loading);
  const links = useAppSelector(state => state.datoInversor.links);
  const updateSuccess = useAppSelector(state => state.datoInversor.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="dato-inversor-heading" data-cy="DatoInversorHeading">
        <Translate contentKey="inversorMonitorApp.datoInversor.home.title">Dato Inversors</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="inversorMonitorApp.datoInversor.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/dato-inversor/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="inversorMonitorApp.datoInversor.home.createLabel">Create new Dato Inversor</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={datoInversorList ? datoInversorList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {datoInversorList && datoInversorList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.id">ID</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('tiempo')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.tiempo">Tiempo</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('tiempo')} />
                  </th>
                  <th className="hand" onClick={sort('bateriaVoltajeV')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.bateriaVoltajeV">Bateria Voltaje V</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('bateriaVoltajeV')} />
                  </th>
                  <th className="hand" onClick={sort('bateriaCorrienteA')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.bateriaCorrienteA">Bateria Corriente A</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('bateriaCorrienteA')} />
                  </th>
                  <th className="hand" onClick={sort('bateriaPotenciaW')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.bateriaPotenciaW">Bateria Potencia W</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('bateriaPotenciaW')} />
                  </th>
                  <th className="hand" onClick={sort('pvVoltajeV')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.pvVoltajeV">Pv Voltaje V</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('pvVoltajeV')} />
                  </th>
                  <th className="hand" onClick={sort('pvPotenciaW')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.pvPotenciaW">Pv Potencia W</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('pvPotenciaW')} />
                  </th>
                  <th className="hand" onClick={sort('salidaVoltajeV')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.salidaVoltajeV">Salida Voltaje V</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('salidaVoltajeV')} />
                  </th>
                  <th className="hand" onClick={sort('salidaPotenciaW')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.salidaPotenciaW">Salida Potencia W</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('salidaPotenciaW')} />
                  </th>
                  <th className="hand" onClick={sort('salidaAparenteVa')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.salidaAparenteVa">Salida Aparente Va</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('salidaAparenteVa')} />
                  </th>
                  <th className="hand" onClick={sort('cargaPorcentaje')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.cargaPorcentaje">Carga Porcentaje</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('cargaPorcentaje')} />
                  </th>
                  <th className="hand" onClick={sort('redVoltajeV')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.redVoltajeV">Red Voltaje V</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('redVoltajeV')} />
                  </th>
                  <th className="hand" onClick={sort('redFrecuenciaHz')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.redFrecuenciaHz">Red Frecuencia Hz</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('redFrecuenciaHz')} />
                  </th>
                  <th className="hand" onClick={sort('modoOperacion')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.modoOperacion">Modo Operacion</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('modoOperacion')} />
                  </th>
                  <th className="hand" onClick={sort('codigoFallo')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.codigoFallo">Codigo Fallo</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('codigoFallo')} />
                  </th>
                  <th className="hand" onClick={sort('eficienciaPct')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.eficienciaPct">Eficiencia Pct</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('eficienciaPct')} />
                  </th>
                  <th className="hand" onClick={sort('consumoRedW')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.consumoRedW">Consumo Red W</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('consumoRedW')} />
                  </th>
                  <th className="hand" onClick={sort('bateriaCargando')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.bateriaCargando">Bateria Cargando</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('bateriaCargando')} />
                  </th>
                  <th className="hand" onClick={sort('pvActivo')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.pvActivo">Pv Activo</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('pvActivo')} />
                  </th>
                  <th className="hand" onClick={sort('redActiva')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.redActiva">Red Activa</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('redActiva')} />
                  </th>
                  <th className="hand" onClick={sort('crcValido')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.crcValido">Crc Valido</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('crcValido')} />
                  </th>
                  <th className="hand" onClick={sort('latenciaMs')}>
                    <Translate contentKey="inversorMonitorApp.datoInversor.latenciaMs">Latencia Ms</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('latenciaMs')} />
                  </th>
                  <th>
                    <Translate contentKey="inversorMonitorApp.datoInversor.inversor">Inversor</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {datoInversorList.map((datoInversor, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/dato-inversor/${datoInversor.id}`} color="link" size="sm">
                        {datoInversor.id}
                      </Button>
                    </td>
                    <td>{datoInversor.tiempo ? <TextFormat type="date" value={datoInversor.tiempo} format={APP_DATE_FORMAT} /> : null}</td>
                    <td>{datoInversor.bateriaVoltajeV}</td>
                    <td>{datoInversor.bateriaCorrienteA}</td>
                    <td>{datoInversor.bateriaPotenciaW}</td>
                    <td>{datoInversor.pvVoltajeV}</td>
                    <td>{datoInversor.pvPotenciaW}</td>
                    <td>{datoInversor.salidaVoltajeV}</td>
                    <td>{datoInversor.salidaPotenciaW}</td>
                    <td>{datoInversor.salidaAparenteVa}</td>
                    <td>{datoInversor.cargaPorcentaje}</td>
                    <td>{datoInversor.redVoltajeV}</td>
                    <td>{datoInversor.redFrecuenciaHz}</td>
                    <td>{datoInversor.modoOperacion}</td>
                    <td>{datoInversor.codigoFallo}</td>
                    <td>{datoInversor.eficienciaPct}</td>
                    <td>{datoInversor.consumoRedW}</td>
                    <td>{datoInversor.bateriaCargando ? 'true' : 'false'}</td>
                    <td>{datoInversor.pvActivo ? 'true' : 'false'}</td>
                    <td>{datoInversor.redActiva ? 'true' : 'false'}</td>
                    <td>{datoInversor.crcValido ? 'true' : 'false'}</td>
                    <td>{datoInversor.latenciaMs}</td>
                    <td>
                      {datoInversor.inversor ? (
                        <Link to={`/inversor/${datoInversor.inversor.id}`}>{datoInversor.inversor.nombre}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/dato-inversor/${datoInversor.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`/dato-inversor/${datoInversor.id}/edit`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/dato-inversor/${datoInversor.id}/delete`)}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="inversorMonitorApp.datoInversor.home.notFound">No Dato Inversors found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default DatoInversor;
