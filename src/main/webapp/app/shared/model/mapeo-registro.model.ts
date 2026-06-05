import { IMarca } from 'app/shared/model/marca.model';
import { TipoDato } from 'app/shared/model/enumerations/tipo-dato.model';

export interface IMapeoRegistro {
  id?: number;
  direccionRegistro?: number;
  nombreRegistro?: string;
  campoSql?: string;
  tipoDato?: keyof typeof TipoDato;
  escala?: number | null;
  unidad?: string | null;
  activo?: boolean;
  descripcion?: string | null;
  marca?: IMarca | null;
}

export const defaultValue: Readonly<IMapeoRegistro> = {
  activo: false,
};
