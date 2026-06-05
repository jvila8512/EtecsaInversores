import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Inversor from './inversor';
import InversorDetail from './inversor-detail';
import InversorUpdate from './inversor-update';
import InversorDeleteDialog from './inversor-delete-dialog';

const InversorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Inversor />} />
    <Route path="new" element={<InversorUpdate />} />
    <Route path=":id">
      <Route index element={<InversorDetail />} />
      <Route path="edit" element={<InversorUpdate />} />
      <Route path="delete" element={<InversorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InversorRoutes;
