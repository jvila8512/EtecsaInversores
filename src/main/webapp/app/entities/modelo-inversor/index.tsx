import React from 'react';
import { Route } from 'react-router';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import ModeloInversor from './modelo-inversor';

const ModeloInversorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ModeloInversor />} />
  </ErrorBoundaryRoutes>
);

export default ModeloInversorRoutes;
