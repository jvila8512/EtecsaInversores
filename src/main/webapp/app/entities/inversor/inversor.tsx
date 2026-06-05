import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './inversor.reducer';

export const Inversor = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const inversorList = useAppSelector(state => state.inversor.entities);
  const loading = useAppSelector(state => state.inversor.loading);
  const totalItems = useAppSelector(state => state.inversor.totalItems);

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
      <h2 id="inversor-heading" data-cy="InversorHeading">
        <Translate contentKey="inversorMonitorApp.inversor.home.title">Inversors</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="inversorMonitorApp.inversor.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/inversor/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="inversorMonitorApp.inversor.home.createLabel">Create new Inversor</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {inversorList && inversorList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="inversorMonitorApp.inversor.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('nombre')}>
                  <Translate contentKey="inversorMonitorApp.inversor.nombre">Nombre</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nombre')} />
                </th>
                <th className="hand" onClick={sort('numeroSerie')}>
                  <Translate contentKey="inversorMonitorApp.inversor.numeroSerie">Numero Serie</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('numeroSerie')} />
                </th>
                <th className="hand" onClick={sort('esMaster')}>
                  <Translate contentKey="inversorMonitorApp.inversor.esMaster">Es Master</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('esMaster')} />
                </th>
                <th className="hand" onClick={sort('tipoConexion')}>
                  <Translate contentKey="inversorMonitorApp.inversor.tipoConexion">Tipo Conexion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tipoConexion')} />
                </th>
                <th className="hand" onClick={sort('puertoSerial')}>
                  <Translate contentKey="inversorMonitorApp.inversor.puertoSerial">Puerto Serial</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('puertoSerial')} />
                </th>
                <th className="hand" onClick={sort('slaveId')}>
                  <Translate contentKey="inversorMonitorApp.inversor.slaveId">Slave Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('slaveId')} />
                </th>
                <th className="hand" onClick={sort('activo')}>
                  <Translate contentKey="inversorMonitorApp.inversor.activo">Activo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('activo')} />
                </th>
                <th className="hand" onClick={sort('estadoDispositivo')}>
                  <Translate contentKey="inversorMonitorApp.inversor.estadoDispositivo">Estado Dispositivo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('estadoDispositivo')} />
                </th>
                <th className="hand" onClick={sort('ultimaLectura')}>
                  <Translate contentKey="inversorMonitorApp.inversor.ultimaLectura">Ultima Lectura</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ultimaLectura')} />
                </th>
                <th className="hand" onClick={sort('ultimoError')}>
                  <Translate contentKey="inversorMonitorApp.inversor.ultimoError">Ultimo Error</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ultimoError')} />
                </th>
                <th className="hand" onClick={sort('periodoMuestreoSeg')}>
                  <Translate contentKey="inversorMonitorApp.inversor.periodoMuestreoSeg">Periodo Muestreo Seg</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('periodoMuestreoSeg')} />
                </th>
                <th className="hand" onClick={sort('fechaRegistro')}>
                  <Translate contentKey="inversorMonitorApp.inversor.fechaRegistro">Fecha Registro</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fechaRegistro')} />
                </th>
                <th className="hand" onClick={sort('ultimaConexion')}>
                  <Translate contentKey="inversorMonitorApp.inversor.ultimaConexion">Ultima Conexion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ultimaConexion')} />
                </th>
                <th>
                  <Translate contentKey="inversorMonitorApp.inversor.sitio">Sitio</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="inversorMonitorApp.inversor.marca">Marca</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="inversorMonitorApp.inversor.modelo">Modelo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="inversorMonitorApp.inversor.gateway">Gateway</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="inversorMonitorApp.inversor.masterReferencia">Master Referencia</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {inversorList.map((inversor, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/inversor/${inversor.id}`} color="link" size="sm">
                      {inversor.id}
                    </Button>
                  </td>
                  <td>{inversor.nombre}</td>
                  <td>{inversor.numeroSerie}</td>
                  <td>{inversor.esMaster ? 'true' : 'false'}</td>
                  <td>
                    <Translate contentKey={`inversorMonitorApp.TipoConexion.${inversor.tipoConexion}`} />
                  </td>
                  <td>{inversor.puertoSerial}</td>
                  <td>{inversor.slaveId}</td>
                  <td>{inversor.activo ? 'true' : 'false'}</td>
                  <td>
                    <Translate contentKey={`inversorMonitorApp.EstadoDispositivo.${inversor.estadoDispositivo}`} />
                  </td>
                  <td>
                    {inversor.ultimaLectura ? <TextFormat type="date" value={inversor.ultimaLectura} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{inversor.ultimoError}</td>
                  <td>{inversor.periodoMuestreoSeg}</td>
                  <td>
                    {inversor.fechaRegistro ? <TextFormat type="date" value={inversor.fechaRegistro} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {inversor.ultimaConexion ? <TextFormat type="date" value={inversor.ultimaConexion} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{inversor.sitio ? <Link to={`/sitio/${inversor.sitio.id}`}>{inversor.sitio.nombre}</Link> : ''}</td>
                  <td>{inversor.marca ? <Link to={`/marca/${inversor.marca.id}`}>{inversor.marca.nombre}</Link> : ''}</td>
                  <td>{inversor.modelo ? <Link to={`/modelo-inversor/${inversor.modelo.id}`}>{inversor.modelo.nombre}</Link> : ''}</td>
                  <td>{inversor.gateway ? <Link to={`/gateway-tcp/${inversor.gateway.id}`}>{inversor.gateway.nombre}</Link> : ''}</td>
                  <td>
                    {inversor.masterReferencia ? (
                      <Link to={`/inversor/${inversor.masterReferencia.id}`}>{inversor.masterReferencia.nombre}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/inversor/${inversor.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/inversor/${inversor.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/inversor/${inversor.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="inversorMonitorApp.inversor.home.notFound">No Inversors found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={inversorList && inversorList.length > 0 ? '' : 'd-none'}>
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

export default Inversor;
