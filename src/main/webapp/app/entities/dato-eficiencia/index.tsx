import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DatoEficiencia from './dato-eficiencia';
import DatoEficienciaDetail from './dato-eficiencia-detail';
import DatoEficienciaUpdate from './dato-eficiencia-update';
import DatoEficienciaDeleteDialog from './dato-eficiencia-delete-dialog';

const DatoEficienciaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DatoEficiencia />} />
    <Route path="new" element={<DatoEficienciaUpdate />} />
    <Route path=":id">
      <Route index element={<DatoEficienciaDetail />} />
      <Route path="edit" element={<DatoEficienciaUpdate />} />
      <Route path="delete" element={<DatoEficienciaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DatoEficienciaRoutes;
