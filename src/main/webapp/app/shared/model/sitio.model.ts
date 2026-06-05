import dayjs from 'dayjs';

export interface ISitio {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
  ubicacion?: string | null;
  contacto?: string | null;
  telefono?: string | null;
  activo?: boolean;
  fechaInstalacion?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<ISitio> = {
  activo: false,
};
