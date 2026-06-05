import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import GatewayTcp from './gateway-tcp';
import GatewayTcpDetail from './gateway-tcp-detail';
import GatewayTcpUpdate from './gateway-tcp-update';
import GatewayTcpDeleteDialog from './gateway-tcp-delete-dialog';

const GatewayTcpRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<GatewayTcp />} />
    <Route path="new" element={<GatewayTcpUpdate />} />
    <Route path=":id">
      <Route index element={<GatewayTcpDetail />} />
      <Route path="edit" element={<GatewayTcpUpdate />} />
      <Route path="delete" element={<GatewayTcpDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GatewayTcpRoutes;
