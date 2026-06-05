import marca from 'app/entities/marca/marca.reducer';
import mapeoRegistro from 'app/entities/mapeo-registro/mapeo-registro.reducer';
import modeloInversor from 'app/entities/modelo-inversor/modelo-inversor.reducer';
import sitio from 'app/entities/sitio/sitio.reducer';
import gatewayTcp from 'app/entities/gateway-tcp/gateway-tcp.reducer';
import inversor from 'app/entities/inversor/inversor.reducer';
import datoInversor from 'app/entities/dato-inversor/dato-inversor.reducer';
import datoEficiencia from 'app/entities/dato-eficiencia/dato-eficiencia.reducer';
import alertaConfig from 'app/entities/alerta-config/alerta-config.reducer';
import eventoAlerta from 'app/entities/evento-alerta/evento-alerta.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  marca,
  mapeoRegistro,
  modeloInversor,
  sitio,
  gatewayTcp,
  inversor,
  datoInversor,
  datoEficiencia,
  alertaConfig,
  eventoAlerta,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
