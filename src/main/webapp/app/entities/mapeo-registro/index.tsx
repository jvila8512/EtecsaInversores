import React from 'react';
import { Route } from 'react-router';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import MapeoRegistro from './mapeo-registro';

const MapeoRegistroRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MapeoRegistro />} />
  </ErrorBoundaryRoutes>
);

export default MapeoRegistroRoutes;
