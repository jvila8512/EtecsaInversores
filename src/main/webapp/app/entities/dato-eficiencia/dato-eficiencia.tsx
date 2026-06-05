import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './dato-eficiencia.reducer';

export const DatoEficiencia = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const datoEficienciaList = useAppSelector(state => state.datoEficiencia.entities);
  const loading = useAppSelector(state => state.datoEficiencia.loading);
  const totalItems = useAppSelector(state => state.datoEficiencia.totalItems);

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
      <h2 id="dato-eficiencia-heading" data-cy="DatoEficienciaHeading">
        <Translate contentKey="inversorMonitorApp.datoEficiencia.home.title">Dato Eficiencias</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="inversorMonitorApp.datoEficiencia.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/dato-eficiencia/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="inversorMonitorApp.datoEficiencia.home.createLabel">Create new Dato Eficiencia</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {datoEficienciaList && datoEficienciaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="inversorMonitorApp.datoEficiencia.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('fecha')}>
                  <Translate contentKey="inversorMonitorApp.datoEficiencia.fecha">Fecha</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fecha')} />
                </th>
                <th className="hand" onClick={sort('energiaPvWh')}>
                  <Translate contentKey="inversorMonitorApp.datoEficiencia.energiaPvWh">Energia Pv Wh</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('energiaPvWh')} />
                </th>
                <th className="hand" onClick={sort('energiaCargaWh')}>
                  <Translate contentKey="inversorMonitorApp.datoEficiencia.energiaCargaWh">Energia Carga Wh</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('energiaCargaWh')} />
                </th>
                <th className="hand" onClick={sort('energiaRedWh')}>
                  <Translate contentKey="inversorMonitorApp.datoEficiencia.energiaRedWh">Energia Red Wh</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('energiaRedWh')} />
                </th>
                <th className="hand" onClick={sort('eficienciaPromPct')}>
                  <Translate contentKey="inversorMonitorApp.datoEficiencia.eficienciaPromPct">Eficiencia Prom Pct</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('eficienciaPromPct')} />
                </th>
                <th className="hand" onClick={sort('horasSol')}>
                  <Translate contentKey="inversorMonitorApp.datoEficiencia.horasSol">Horas Sol</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('horasSol')} />
                </th>
                <th className="hand" onClick={sort('picoPvW')}>
                  <Translate contentKey="inversorMonitorApp.datoEficiencia.picoPvW">Pico Pv W</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('picoPvW')} />
                </th>
                <th className="hand" onClick={sort('picoCargaW')}>
                  <Translate contentKey="inversorMonitorApp.datoEficiencia.picoCargaW">Pico Carga W</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('picoCargaW')} />
                </th>
                <th className="hand" onClick={sort('maxBateriaV')}>
                  <Translate contentKey="inversorMonitorApp.datoEficiencia.maxBateriaV">Max Bateria V</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('maxBateriaV')} />
                </th>
                <th className="hand" onClick={sort('minBateriaV')}>
                  <Translate contentKey="inversorMonitorApp.datoEficiencia.minBateriaV">Min Bateria V</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('minBateriaV')} />
                </th>
                <th className="hand" onClick={sort('totalLecturas')}>
                  <Translate contentKey="inversorMonitorApp.datoEficiencia.totalLecturas">Total Lecturas</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalLecturas')} />
                </th>
                <th className="hand" onClick={sort('calculadoEn')}>
                  <Translate contentKey="inversorMonitorApp.datoEficiencia.calculadoEn">Calculado En</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('calculadoEn')} />
                </th>
                <th>
                  <Translate contentKey="inversorMonitorApp.datoEficiencia.inversor">Inversor</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {datoEficienciaList.map((datoEficiencia, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/dato-eficiencia/${datoEficiencia.id}`} color="link" size="sm">
                      {datoEficiencia.id}
                    </Button>
                  </td>
                  <td>
                    {datoEficiencia.fecha ? <TextFormat type="date" value={datoEficiencia.fecha} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{datoEficiencia.energiaPvWh}</td>
                  <td>{datoEficiencia.energiaCargaWh}</td>
                  <td>{datoEficiencia.energiaRedWh}</td>
                  <td>{datoEficiencia.eficienciaPromPct}</td>
                  <td>{datoEficiencia.horasSol}</td>
                  <td>{datoEficiencia.picoPvW}</td>
                  <td>{datoEficiencia.picoCargaW}</td>
                  <td>{datoEficiencia.maxBateriaV}</td>
                  <td>{datoEficiencia.minBateriaV}</td>
                  <td>{datoEficiencia.totalLecturas}</td>
                  <td>
                    {datoEficiencia.calculadoEn ? (
                      <TextFormat type="date" value={datoEficiencia.calculadoEn} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {datoEficiencia.inversor ? (
                      <Link to={`/inversor/${datoEficiencia.inversor.id}`}>{datoEficiencia.inversor.nombre}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/dato-eficiencia/${datoEficiencia.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/dato-eficiencia/${datoEficiencia.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/dato-eficiencia/${datoEficiencia.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="inversorMonitorApp.datoEficiencia.home.notFound">No Dato Eficiencias found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={datoEficienciaList && datoEficienciaList.length > 0 ? '' : 'd-none'}>
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

export default DatoEficiencia;
