import React from 'react';
import { Route } from 'react-router';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import Sitio from './sitio';

const SitioRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Sitio />} />
  </ErrorBoundaryRoutes>
);

export default SitioRoutes;
