import dayjs from 'dayjs';
import { ISitio } from 'app/shared/model/sitio.model';
import { EstadoDispositivo } from 'app/shared/model/enumerations/estado-dispositivo.model';

export interface IGatewayTcp {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
  ubicacion?: string | null;
  direccionIp?: string;
  puerto?: number;
  activo?: boolean;
  estadoConexion?: keyof typeof EstadoDispositivo | null;
  ultimaLectura?: dayjs.Dayjs | null;
  ultimoError?: string | null;
  tiempoEsperaMs?: number | null;
  reintentos?: number | null;
  sitio?: ISitio | null;
}

export const defaultValue: Readonly<IGatewayTcp> = {
  activo: false,
};
