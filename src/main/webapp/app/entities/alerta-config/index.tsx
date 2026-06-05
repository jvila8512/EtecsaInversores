import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AlertaConfig from './alerta-config';
import AlertaConfigDetail from './alerta-config-detail';
import AlertaConfigUpdate from './alerta-config-update';
import AlertaConfigDeleteDialog from './alerta-config-delete-dialog';

const AlertaConfigRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AlertaConfig />} />
    <Route path="new" element={<AlertaConfigUpdate />} />
    <Route path=":id">
      <Route index element={<AlertaConfigDetail />} />
      <Route path="edit" element={<AlertaConfigUpdate />} />
      <Route path="delete" element={<AlertaConfigDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AlertaConfigRoutes;
