import { IInversor } from 'app/shared/model/inversor.model';

export interface IAlertaConfig {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
  campoMonitorizado?: string;
  operador?: string;
  umbral?: number;
  activa?: boolean;
  nivelSeveridad?: string | null;
  canalNotificacion?: string | null;
  cooldownMinutos?: number | null;
  inversor?: IInversor | null;
}

export const defaultValue: Readonly<IAlertaConfig> = {
  activa: false,
};
