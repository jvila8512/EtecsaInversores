import React from 'react';
import { Route } from 'react-router';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import Inversor from './inversor';

const InversorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Inversor />} />
  </ErrorBoundaryRoutes>
);

export default InversorRoutes;
