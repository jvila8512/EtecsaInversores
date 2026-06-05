import dayjs from 'dayjs';
import { IInversor } from 'app/shared/model/inversor.model';

export interface IDatoEficiencia {
  id?: number;
  fecha?: dayjs.Dayjs;
  energiaPvWh?: number | null;
  energiaCargaWh?: number | null;
  energiaRedWh?: number | null;
  eficienciaPromPct?: number | null;
  horasSol?: number | null;
  picoPvW?: number | null;
  picoCargaW?: number | null;
  maxBateriaV?: number | null;
  minBateriaV?: number | null;
  totalLecturas?: number | null;
  calculadoEn?: dayjs.Dayjs | null;
  inversor?: IInversor | null;
}

export const defaultValue: Readonly<IDatoEficiencia> = {};
