import React from 'react';
import { Route } from 'react-router';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import Marca from './marca';

const MarcaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Marca />} />
  </ErrorBoundaryRoutes>
);

export default MarcaRoutes;
