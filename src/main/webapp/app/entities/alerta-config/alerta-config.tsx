import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './alerta-config.reducer';

export const AlertaConfig = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const alertaConfigList = useAppSelector(state => state.alertaConfig.entities);
  const loading = useAppSelector(state => state.alertaConfig.loading);
  const totalItems = useAppSelector(state => state.alertaConfig.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
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
      <h2 id="alerta-config-heading" data-cy="AlertaConfigHeading">
        <Translate contentKey="inversorMonitorApp.alertaConfig.home.title">Alerta Configs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="inversorMonitorApp.alertaConfig.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/alerta-config/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="inversorMonitorApp.alertaConfig.home.createLabel">Create new Alerta Config</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {alertaConfigList && alertaConfigList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="inversorMonitorApp.alertaConfig.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('nombre')}>
                  <Translate contentKey="inversorMonitorApp.alertaConfig.nombre">Nombre</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nombre')} />
                </th>
                <th className="hand" onClick={sort('descripcion')}>
                  <Translate contentKey="inversorMonitorApp.alertaConfig.descripcion">Descripcion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('descripcion')} />
                </th>
                <th className="hand" onClick={sort('campoMonitorizado')}>
                  <Translate contentKey="inversorMonitorApp.alertaConfig.campoMonitorizado">Campo Monitorizado</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('campoMonitorizado')} />
                </th>
                <th className="hand" onClick={sort('operador')}>
                  <Translate contentKey="inversorMonitorApp.alertaConfig.operador">Operador</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('operador')} />
                </th>
                <th className="hand" onClick={sort('umbral')}>
                  <Translate contentKey="inversorMonitorApp.alertaConfig.umbral">Umbral</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('umbral')} />
                </th>
                <th className="hand" onClick={sort('activa')}>
                  <Translate contentKey="inversorMonitorApp.alertaConfig.activa">Activa</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('activa')} />
                </th>
                <th className="hand" onClick={sort('nivelSeveridad')}>
                  <Translate contentKey="inversorMonitorApp.alertaConfig.nivelSeveridad">Nivel Severidad</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nivelSeveridad')} />
                </th>
                <th className="hand" onClick={sort('canalNotificacion')}>
                  <Translate contentKey="inversorMonitorApp.alertaConfig.canalNotificacion">Canal Notificacion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('canalNotificacion')} />
                </th>
                <th className="hand" onClick={sort('cooldownMinutos')}>
                  <Translate contentKey="inversorMonitorApp.alertaConfig.cooldownMinutos">Cooldown Minutos</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cooldownMinutos')} />
                </th>
                <th>
                  <Translate contentKey="inversorMonitorApp.alertaConfig.inversor">Inversor</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {alertaConfigList.map((alertaConfig, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/alerta-config/${alertaConfig.id}`} color="link" size="sm">
                      {alertaConfig.id}
                    </Button>
                  </td>
                  <td>{alertaConfig.nombre}</td>
                  <td>{alertaConfig.descripcion}</td>
                  <td>{alertaConfig.campoMonitorizado}</td>
                  <td>{alertaConfig.operador}</td>
                  <td>{alertaConfig.umbral}</td>
                  <td>{alertaConfig.activa ? 'true' : 'false'}</td>
                  <td>{alertaConfig.nivelSeveridad}</td>
                  <td>{alertaConfig.canalNotificacion}</td>
                  <td>{alertaConfig.cooldownMinutos}</td>
                  <td>
                    {alertaConfig.inversor ? <Link to={`/inversor/${alertaConfig.inversor.id}`}>{alertaConfig.inversor.nombre}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/alerta-config/${alertaConfig.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/alerta-config/${alertaConfig.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        onClick={() =>
                          (window.location.href = `/alerta-config/${alertaConfig.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
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
              <Translate contentKey="inversorMonitorApp.alertaConfig.home.notFound">No Alerta Configs found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={alertaConfigList && alertaConfigList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default AlertaConfig;
