import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EventoAlerta from './evento-alerta';
import EventoAlertaDetail from './evento-alerta-detail';
import EventoAlertaUpdate from './evento-alerta-update';
import EventoAlertaDeleteDialog from './evento-alerta-delete-dialog';

const EventoAlertaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EventoAlerta />} />
    <Route path="new" element={<EventoAlertaUpdate />} />
    <Route path=":id">
      <Route index element={<EventoAlertaDetail />} />
      <Route path="edit" element={<EventoAlertaUpdate />} />
      <Route path="delete" element={<EventoAlertaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EventoAlertaRoutes;
