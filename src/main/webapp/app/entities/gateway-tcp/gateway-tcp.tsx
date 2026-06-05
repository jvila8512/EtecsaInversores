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

import { getEntities } from './gateway-tcp.reducer';

export const GatewayTcp = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const gatewayTcpList = useAppSelector(state => state.gatewayTcp.entities);
  const loading = useAppSelector(state => state.gatewayTcp.loading);
  const totalItems = useAppSelector(state => state.gatewayTcp.totalItems);

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
      <h2 id="gateway-tcp-heading" data-cy="GatewayTcpHeading">
        <Translate contentKey="inversorMonitorApp.gatewayTcp.home.title">Gateway Tcps</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="inversorMonitorApp.gatewayTcp.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/gateway-tcp/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="inversorMonitorApp.gatewayTcp.home.createLabel">Create new Gateway Tcp</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {gatewayTcpList && gatewayTcpList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="inversorMonitorApp.gatewayTcp.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('nombre')}>
                  <Translate contentKey="inversorMonitorApp.gatewayTcp.nombre">Nombre</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nombre')} />
                </th>
                <th className="hand" onClick={sort('descripcion')}>
                  <Translate contentKey="inversorMonitorApp.gatewayTcp.descripcion">Descripcion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('descripcion')} />
                </th>
                <th className="hand" onClick={sort('ubicacion')}>
                  <Translate contentKey="inversorMonitorApp.gatewayTcp.ubicacion">Ubicacion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ubicacion')} />
                </th>
                <th className="hand" onClick={sort('direccionIp')}>
                  <Translate contentKey="inversorMonitorApp.gatewayTcp.direccionIp">Direccion Ip</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('direccionIp')} />
                </th>
                <th className="hand" onClick={sort('puerto')}>
                  <Translate contentKey="inversorMonitorApp.gatewayTcp.puerto">Puerto</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('puerto')} />
                </th>
                <th className="hand" onClick={sort('activo')}>
                  <Translate contentKey="inversorMonitorApp.gatewayTcp.activo">Activo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('activo')} />
                </th>
                <th className="hand" onClick={sort('estadoConexion')}>
                  <Translate contentKey="inversorMonitorApp.gatewayTcp.estadoConexion">Estado Conexion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('estadoConexion')} />
                </th>
                <th className="hand" onClick={sort('ultimaLectura')}>
                  <Translate contentKey="inversorMonitorApp.gatewayTcp.ultimaLectura">Ultima Lectura</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ultimaLectura')} />
                </th>
                <th className="hand" onClick={sort('ultimoError')}>
                  <Translate contentKey="inversorMonitorApp.gatewayTcp.ultimoError">Ultimo Error</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ultimoError')} />
                </th>
                <th className="hand" onClick={sort('tiempoEsperaMs')}>
                  <Translate contentKey="inversorMonitorApp.gatewayTcp.tiempoEsperaMs">Tiempo Espera Ms</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tiempoEsperaMs')} />
                </th>
                <th className="hand" onClick={sort('reintentos')}>
                  <Translate contentKey="inversorMonitorApp.gatewayTcp.reintentos">Reintentos</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reintentos')} />
                </th>
                <th>
                  <Translate contentKey="inversorMonitorApp.gatewayTcp.sitio">Sitio</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {gatewayTcpList.map((gatewayTcp, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/gateway-tcp/${gatewayTcp.id}`} color="link" size="sm">
                      {gatewayTcp.id}
                    </Button>
                  </td>
                  <td>{gatewayTcp.nombre}</td>
                  <td>{gatewayTcp.descripcion}</td>
                  <td>{gatewayTcp.ubicacion}</td>
                  <td>{gatewayTcp.direccionIp}</td>
                  <td>{gatewayTcp.puerto}</td>
                  <td>{gatewayTcp.activo ? 'true' : 'false'}</td>
                  <td>
                    <Translate contentKey={`inversorMonitorApp.EstadoDispositivo.${gatewayTcp.estadoConexion}`} />
                  </td>
                  <td>
                    {gatewayTcp.ultimaLectura ? <TextFormat type="date" value={gatewayTcp.ultimaLectura} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{gatewayTcp.ultimoError}</td>
                  <td>{gatewayTcp.tiempoEsperaMs}</td>
                  <td>{gatewayTcp.reintentos}</td>
                  <td>{gatewayTcp.sitio ? <Link to={`/sitio/${gatewayTcp.sitio.id}`}>{gatewayTcp.sitio.nombre}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/gateway-tcp/${gatewayTcp.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/gateway-tcp/${gatewayTcp.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/gateway-tcp/${gatewayTcp.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="inversorMonitorApp.gatewayTcp.home.notFound">No Gateway Tcps found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={gatewayTcpList && gatewayTcpList.length > 0 ? '' : 'd-none'}>
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

export default GatewayTcp;
