import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './mapeo-registro.reducer';

export const MapeoRegistro = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const mapeoRegistroList = useAppSelector(state => state.mapeoRegistro.entities);
  const loading = useAppSelector(state => state.mapeoRegistro.loading);
  const totalItems = useAppSelector(state => state.mapeoRegistro.totalItems);

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
      <h2 id="mapeo-registro-heading" data-cy="MapeoRegistroHeading">
        <Translate contentKey="inversorMonitorApp.mapeoRegistro.home.title">Mapeo Registros</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="inversorMonitorApp.mapeoRegistro.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/mapeo-registro/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="inversorMonitorApp.mapeoRegistro.home.createLabel">Create new Mapeo Registro</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {mapeoRegistroList && mapeoRegistroList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="inversorMonitorApp.mapeoRegistro.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('direccionRegistro')}>
                  <Translate contentKey="inversorMonitorApp.mapeoRegistro.direccionRegistro">Direccion Registro</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('direccionRegistro')} />
                </th>
                <th className="hand" onClick={sort('nombreRegistro')}>
                  <Translate contentKey="inversorMonitorApp.mapeoRegistro.nombreRegistro">Nombre Registro</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nombreRegistro')} />
                </th>
                <th className="hand" onClick={sort('campoSql')}>
                  <Translate contentKey="inversorMonitorApp.mapeoRegistro.campoSql">Campo Sql</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('campoSql')} />
                </th>
                <th className="hand" onClick={sort('tipoDato')}>
                  <Translate contentKey="inversorMonitorApp.mapeoRegistro.tipoDato">Tipo Dato</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tipoDato')} />
                </th>
                <th className="hand" onClick={sort('escala')}>
                  <Translate contentKey="inversorMonitorApp.mapeoRegistro.escala">Escala</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('escala')} />
                </th>
                <th className="hand" onClick={sort('unidad')}>
                  <Translate contentKey="inversorMonitorApp.mapeoRegistro.unidad">Unidad</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('unidad')} />
                </th>
                <th className="hand" onClick={sort('activo')}>
                  <Translate contentKey="inversorMonitorApp.mapeoRegistro.activo">Activo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('activo')} />
                </th>
                <th className="hand" onClick={sort('descripcion')}>
                  <Translate contentKey="inversorMonitorApp.mapeoRegistro.descripcion">Descripcion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('descripcion')} />
                </th>
                <th>
                  <Translate contentKey="inversorMonitorApp.mapeoRegistro.marca">Marca</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {mapeoRegistroList.map((mapeoRegistro, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/mapeo-registro/${mapeoRegistro.id}`} color="link" size="sm">
                      {mapeoRegistro.id}
                    </Button>
                  </td>
                  <td>{mapeoRegistro.direccionRegistro}</td>
                  <td>{mapeoRegistro.nombreRegistro}</td>
                  <td>{mapeoRegistro.campoSql}</td>
                  <td>
                    <Translate contentKey={`inversorMonitorApp.TipoDato.${mapeoRegistro.tipoDato}`} />
                  </td>
                  <td>{mapeoRegistro.escala}</td>
                  <td>{mapeoRegistro.unidad}</td>
                  <td>{mapeoRegistro.activo ? 'true' : 'false'}</td>
                  <td>{mapeoRegistro.descripcion}</td>
                  <td>{mapeoRegistro.marca ? <Link to={`/marca/${mapeoRegistro.marca.id}`}>{mapeoRegistro.marca.nombre}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/mapeo-registro/${mapeoRegistro.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/mapeo-registro/${mapeoRegistro.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/mapeo-registro/${mapeoRegistro.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="inversorMonitorApp.mapeoRegistro.home.notFound">No Mapeo Registros found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={mapeoRegistroList && mapeoRegistroList.length > 0 ? '' : 'd-none'}>
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

export default MapeoRegistro;
