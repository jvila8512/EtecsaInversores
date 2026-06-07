import React from 'react';
import { Route } from 'react-router';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import GatewayTcp from './gateway-tcp';

const GatewayTcpRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<GatewayTcp />} />
  </ErrorBoundaryRoutes>
);

export default GatewayTcpRoutes;
