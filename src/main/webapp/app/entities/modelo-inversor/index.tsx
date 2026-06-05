import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ModeloInversor from './modelo-inversor';
import ModeloInversorDetail from './modelo-inversor-detail';
import ModeloInversorUpdate from './modelo-inversor-update';
import ModeloInversorDeleteDialog from './modelo-inversor-delete-dialog';

const ModeloInversorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ModeloInversor />} />
    <Route path="new" element={<ModeloInversorUpdate />} />
    <Route path=":id">
      <Route index element={<ModeloInversorDetail />} />
      <Route path="edit" element={<ModeloInversorUpdate />} />
      <Route path="delete" element={<ModeloInversorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ModeloInversorRoutes;
