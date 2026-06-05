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

import { getEntities, reset } from './evento-alerta.reducer';

export const EventoAlerta = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const eventoAlertaList = useAppSelector(state => state.eventoAlerta.entities);
  const loading = useAppSelector(state => state.eventoAlerta.loading);
  const links = useAppSelector(state => state.eventoAlerta.links);
  const updateSuccess = useAppSelector(state => state.eventoAlerta.updateSuccess);

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
      <h2 id="evento-alerta-heading" data-cy="EventoAlertaHeading">
        <Translate contentKey="inversorMonitorApp.eventoAlerta.home.title">Evento Alertas</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="inversorMonitorApp.eventoAlerta.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/evento-alerta/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="inversorMonitorApp.eventoAlerta.home.createLabel">Create new Evento Alerta</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={eventoAlertaList ? eventoAlertaList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {eventoAlertaList && eventoAlertaList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="inversorMonitorApp.eventoAlerta.id">ID</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('timestamp')}>
                    <Translate contentKey="inversorMonitorApp.eventoAlerta.timestamp">Timestamp</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('timestamp')} />
                  </th>
                  <th className="hand" onClick={sort('nombreAlerta')}>
                    <Translate contentKey="inversorMonitorApp.eventoAlerta.nombreAlerta">Nombre Alerta</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('nombreAlerta')} />
                  </th>
                  <th className="hand" onClick={sort('mensaje')}>
                    <Translate contentKey="inversorMonitorApp.eventoAlerta.mensaje">Mensaje</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('mensaje')} />
                  </th>
                  <th className="hand" onClick={sort('campoMonitorizado')}>
                    <Translate contentKey="inversorMonitorApp.eventoAlerta.campoMonitorizado">Campo Monitorizado</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('campoMonitorizado')} />
                  </th>
                  <th className="hand" onClick={sort('valorDetectado')}>
                    <Translate contentKey="inversorMonitorApp.eventoAlerta.valorDetectado">Valor Detectado</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('valorDetectado')} />
                  </th>
                  <th className="hand" onClick={sort('umbral')}>
                    <Translate contentKey="inversorMonitorApp.eventoAlerta.umbral">Umbral</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('umbral')} />
                  </th>
                  <th className="hand" onClick={sort('operador')}>
                    <Translate contentKey="inversorMonitorApp.eventoAlerta.operador">Operador</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('operador')} />
                  </th>
                  <th className="hand" onClick={sort('nivelSeveridad')}>
                    <Translate contentKey="inversorMonitorApp.eventoAlerta.nivelSeveridad">Nivel Severidad</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('nivelSeveridad')} />
                  </th>
                  <th className="hand" onClick={sort('notificacionEnviada')}>
                    <Translate contentKey="inversorMonitorApp.eventoAlerta.notificacionEnviada">Notificacion Enviada</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('notificacionEnviada')} />
                  </th>
                  <th className="hand" onClick={sort('canalUsado')}>
                    <Translate contentKey="inversorMonitorApp.eventoAlerta.canalUsado">Canal Usado</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('canalUsado')} />
                  </th>
                  <th className="hand" onClick={sort('resuelto')}>
                    <Translate contentKey="inversorMonitorApp.eventoAlerta.resuelto">Resuelto</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('resuelto')} />
                  </th>
                  <th className="hand" onClick={sort('fechaResolucion')}>
                    <Translate contentKey="inversorMonitorApp.eventoAlerta.fechaResolucion">Fecha Resolucion</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('fechaResolucion')} />
                  </th>
                  <th className="hand" onClick={sort('notas')}>
                    <Translate contentKey="inversorMonitorApp.eventoAlerta.notas">Notas</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('notas')} />
                  </th>
                  <th>
                    <Translate contentKey="inversorMonitorApp.eventoAlerta.inversor">Inversor</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="inversorMonitorApp.eventoAlerta.alertaConfig">Alerta Config</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {eventoAlertaList.map((eventoAlerta, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/evento-alerta/${eventoAlerta.id}`} color="link" size="sm">
                        {eventoAlerta.id}
                      </Button>
                    </td>
                    <td>
                      {eventoAlerta.timestamp ? <TextFormat type="date" value={eventoAlerta.timestamp} format={APP_DATE_FORMAT} /> : null}
                    </td>
                    <td>{eventoAlerta.nombreAlerta}</td>
                    <td>{eventoAlerta.mensaje}</td>
                    <td>{eventoAlerta.campoMonitorizado}</td>
                    <td>{eventoAlerta.valorDetectado}</td>
                    <td>{eventoAlerta.umbral}</td>
                    <td>{eventoAlerta.operador}</td>
                    <td>{eventoAlerta.nivelSeveridad}</td>
                    <td>{eventoAlerta.notificacionEnviada ? 'true' : 'false'}</td>
                    <td>{eventoAlerta.canalUsado}</td>
                    <td>{eventoAlerta.resuelto ? 'true' : 'false'}</td>
                    <td>
                      {eventoAlerta.fechaResolucion ? (
                        <TextFormat type="date" value={eventoAlerta.fechaResolucion} format={APP_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>{eventoAlerta.notas}</td>
                    <td>
                      {eventoAlerta.inversor ? (
                        <Link to={`/inversor/${eventoAlerta.inversor.id}`}>{eventoAlerta.inversor.nombre}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {eventoAlerta.alertaConfig ? (
                        <Link to={`/alerta-config/${eventoAlerta.alertaConfig.id}`}>{eventoAlerta.alertaConfig.nombre}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/evento-alerta/${eventoAlerta.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`/evento-alerta/${eventoAlerta.id}/edit`}
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
                          onClick={() => (window.location.href = `/evento-alerta/${eventoAlerta.id}/delete`)}
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
                <Translate contentKey="inversorMonitorApp.eventoAlerta.home.notFound">No Evento Alertas found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default EventoAlerta;
