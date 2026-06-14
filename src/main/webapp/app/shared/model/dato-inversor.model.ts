import dayjs from 'dayjs';
import { IInversor } from 'app/shared/model/inversor.model';

export interface IDatoInversor {
  id?: number;
  tiempo?: dayjs.Dayjs;
  bateriaVoltajeV?: number | null;
  bateriaCorrienteA?: number | null;
  bateriaPotenciaW?: number | null;
  pvVoltajeV?: number | null;
  pvPotenciaW?: number | null;
  salidaVoltajeV?: number | null;
  salidaPotenciaW?: number | null;
  salidaAparenteVa?: number | null;
  cargaPorcentaje?: number | null;
  redVoltajeV?: number | null;
  redFrecuenciaHz?: number | null;
  modoOperacion?: string | null;
  codigoFallo?: number | null;
  eficienciaPct?: number | null;
  consumoRedW?: number | null;
  bateriaCargando?: boolean | null;
  pvActivo?: boolean | null;
  redActiva?: boolean | null;
  crcValido?: boolean | null;
  latenciaMs?: number | null;
  pvCorrienteA?: number | null;
  salidaCorrienteA?: number | null;
  salidaFrecuenciaHz?: number | null;
  temperaturaC?: number | null;
  busVoltajeV?: number | null;
  socPct?: number | null;
  inversor?: IInversor | null;
}

export const defaultValue: Readonly<IDatoInversor> = {
  bateriaCargando: false,
  pvActivo: false,
  redActiva: false,
  crcValido: false,
};
