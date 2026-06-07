import React from 'react';
import { Route } from 'react-router';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import DatoInversor from './dato-inversor';

const DatoInversorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DatoInversor />} />
  </ErrorBoundaryRoutes>
);

export default DatoInversorRoutes;
