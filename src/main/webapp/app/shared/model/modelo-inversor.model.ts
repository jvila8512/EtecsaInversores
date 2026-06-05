import { IMarca } from 'app/shared/model/marca.model';

export interface IModeloInversor {
  id?: number;
  nombre?: string;
  capacidadVa?: number;
  voltajeSistemaNom?: number;
  descripcion?: string | null;
  marca?: IMarca | null;
}

export const defaultValue: Readonly<IModeloInversor> = {};
