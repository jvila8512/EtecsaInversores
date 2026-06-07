import React from 'react';
import { Route } from 'react-router';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import DatoEficiencia from './dato-eficiencia';

const DatoEficienciaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DatoEficiencia />} />
  </ErrorBoundaryRoutes>
);

export default DatoEficienciaRoutes;
