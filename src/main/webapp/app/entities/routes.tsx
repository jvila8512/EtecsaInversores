import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Marca from './marca';
import MapeoRegistro from './mapeo-registro';
import ModeloInversor from './modelo-inversor';
import Sitio from './sitio';
import GatewayTcp from './gateway-tcp';
import Inversor from './inversor';
import DatoInversor from './dato-inversor';
import DatoEficiencia from './dato-eficiencia';
import AlertaConfig from './alerta-config';
import EventoAlerta from './evento-alerta';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="marca/*" element={<Marca />} />
        <Route path="mapeo-registro/*" element={<MapeoRegistro />} />
        <Route path="modelo-inversor/*" element={<ModeloInversor />} />
        <Route path="sitio/*" element={<Sitio />} />
        <Route path="gateway-tcp/*" element={<GatewayTcp />} />
        <Route path="inversor/*" element={<Inversor />} />
        <Route path="dato-inversor/*" element={<DatoInversor />} />
        <Route path="dato-eficiencia/*" element={<DatoEficiencia />} />
        <Route path="alerta-config/*" element={<AlertaConfig />} />
        <Route path="evento-alerta/*" element={<EventoAlerta />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
