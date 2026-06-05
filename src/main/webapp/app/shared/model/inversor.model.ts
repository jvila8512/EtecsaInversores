import dayjs from 'dayjs';
import { ISitio } from 'app/shared/model/sitio.model';
import { IMarca } from 'app/shared/model/marca.model';
import { IModeloInversor } from 'app/shared/model/modelo-inversor.model';
import { IGatewayTcp } from 'app/shared/model/gateway-tcp.model';
import { TipoConexion } from 'app/shared/model/enumerations/tipo-conexion.model';
import { EstadoDispositivo } from 'app/shared/model/enumerations/estado-dispositivo.model';

export interface IInversor {
  id?: number;
  nombre?: string;
  numeroSerie?: string | null;
  esMaster?: boolean;
  tipoConexion?: keyof typeof TipoConexion;
  puertoSerial?: string | null;
  slaveId?: number;
  activo?: boolean;
  estadoDispositivo?: keyof typeof EstadoDispositivo | null;
  ultimaLectura?: dayjs.Dayjs | null;
  ultimoError?: string | null;
  periodoMuestreoSeg?: number | null;
  fechaRegistro?: dayjs.Dayjs;
  ultimaConexion?: dayjs.Dayjs | null;
  sitio?: ISitio | null;
  marca?: IMarca | null;
  modelo?: IModeloInversor | null;
  gateway?: IGatewayTcp | null;
  masterReferencia?: IInversor | null;
}

export const defaultValue: Readonly<IInversor> = {
  esMaster: false,
  activo: false,
};
