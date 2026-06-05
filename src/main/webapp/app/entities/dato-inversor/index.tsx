import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DatoInversor from './dato-inversor';
import DatoInversorDetail from './dato-inversor-detail';
import DatoInversorUpdate from './dato-inversor-update';
import DatoInversorDeleteDialog from './dato-inversor-delete-dialog';

const DatoInversorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DatoInversor />} />
    <Route path="new" element={<DatoInversorUpdate />} />
    <Route path=":id">
      <Route index element={<DatoInversorDetail />} />
      <Route path="edit" element={<DatoInversorUpdate />} />
      <Route path="delete" element={<DatoInversorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DatoInversorRoutes;
