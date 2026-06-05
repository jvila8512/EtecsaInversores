import dayjs from 'dayjs';
import { IInversor } from 'app/shared/model/inversor.model';
import { IAlertaConfig } from 'app/shared/model/alerta-config.model';

export interface IEventoAlerta {
  id?: number;
  timestamp?: dayjs.Dayjs;
  nombreAlerta?: string;
  mensaje?: string;
  campoMonitorizado?: string | null;
  valorDetectado?: number | null;
  umbral?: number | null;
  operador?: string | null;
  nivelSeveridad?: string | null;
  notificacionEnviada?: boolean | null;
  canalUsado?: string | null;
  resuelto?: boolean | null;
  fechaResolucion?: dayjs.Dayjs | null;
  notas?: string | null;
  inversor?: IInversor | null;
  alertaConfig?: IAlertaConfig | null;
}

export const defaultValue: Readonly<IEventoAlerta> = {
  notificacionEnviada: false,
  resuelto: false,
};
