import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MapeoRegistro from './mapeo-registro';
import MapeoRegistroDetail from './mapeo-registro-detail';
import MapeoRegistroUpdate from './mapeo-registro-update';
import MapeoRegistroDeleteDialog from './mapeo-registro-delete-dialog';

const MapeoRegistroRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MapeoRegistro />} />
    <Route path="new" element={<MapeoRegistroUpdate />} />
    <Route path=":id">
      <Route index element={<MapeoRegistroDetail />} />
      <Route path="edit" element={<MapeoRegistroUpdate />} />
      <Route path="delete" element={<MapeoRegistroDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MapeoRegistroRoutes;
