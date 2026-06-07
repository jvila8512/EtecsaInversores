import React from 'react';
import { Route } from 'react-router';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import EventoAlerta from './evento-alerta';

const EventoAlertaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EventoAlerta />} />
  </ErrorBoundaryRoutes>
);

export default EventoAlertaRoutes;
