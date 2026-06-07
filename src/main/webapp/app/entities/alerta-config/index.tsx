import React from 'react';
import { Route } from 'react-router';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import AlertaConfig from './alerta-config';

const AlertaConfigRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AlertaConfig />} />
  </ErrorBoundaryRoutes>
);

export default AlertaConfigRoutes;
