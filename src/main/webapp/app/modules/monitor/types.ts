import dayjs from 'dayjs';
import { IInversor } from 'app/shared/model/inversor.model';
import { IGatewayTcp } from 'app/shared/model/gateway-tcp.model';
import { IDatoInversor } from 'app/shared/model/dato-inversor.model';
import { IDatoEficiencia } from 'app/shared/model/dato-eficiencia.model';
import { IEventoAlerta } from 'app/shared/model/evento-alerta.model';

/* ─── Theme ─── */
export type ThemeMode = 'dark' | 'light';

/* ─── Navigation ─── */
export type ScreenName = 'dashboard' | 'inverter-detail' | 'history' | 'alerts' | 'settings';

/* ─── WebSocket connection state ─── */
export type WsStatus = 'connected' | 'reconnecting' | 'disconnected';

/* ─── Inversor group (by gateway) ─── */
export interface InversorGroup {
  gateway: IGatewayTcp;
  master: IInversor | undefined;
  esclavos: IInversor[];
  totalPotenciaW: number;
  alertCount: number;
}

/* ─── Live data map: inversorId -> latest DatoInversor ─── */
export type LiveDataMap = Record<number, IDatoInversor>;

/* ─── Alert event with inversor info for display ─── */
export interface AlertDisplay extends IEventoAlerta {
  inversorNombre?: string;
}

/* ─── KPI values for dashboard ─── */
export interface DashboardKPIs {
  generacionKwh: number;
  consumoKwh: number;
  potenciaKw: number;
}

/* ─── Trend bar data point ─── */
export interface TrendHour {
  hour: number;
  value: number;
}

/* ─── Chart data point ─── */
export interface ChartPoint {
  time: dayjs.Dayjs;
  value1: number;
  value2?: number;
}

/* ─── Anomaly marker for charts ─── */
export interface AnomalyPoint {
  index: number;
  label: string;
}

/* ─── BMS cell data ─── */
export interface BmsCell {
  index: number;
  voltageMv: number;
  status: 'ok' | 'warn' | 'crit';
}

/* ─── Quick config slider definition ─── */
export interface QuickConfigSlider {
  label: string;
  register: string;
  min: number;
  max: number;
  defaultValue: number;
  unit: string;
  value: number;
}

/* ─── Quick config toggle definition ─── */
export interface QuickConfigToggle {
  label: string;
  register?: string;
  defaultValue: boolean;
  value: boolean;
}

/* ─── Register auto-discovery request ─── */
export interface RegisterAutoRequest {
  nombre: string;
  tipoConexion: string;
  puerto: string;
  slaveId: number;
  masterReferenciaId?: number | null;
}

/* ─── History filter state ─── */
export interface HistoryFilters {
  grupoId: number | null;
  variables: string[];
  fechaDesde: dayjs.Dayjs;
  fechaHasta: dayjs.Dayjs;
}

/* ─── Alert filter state ─── */
export interface AlertFilters {
  soloActivas: boolean;
}
