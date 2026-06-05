export interface IMarca {
  id?: number;
  nombre?: string;
  fabricante?: string | null;
  registroInicio?: number;
  registroFin?: number;
  registroInfoInicio?: number | null;
  registroInfoFin?: number | null;
  baudRateDefault?: number | null;
  descripcion?: string | null;
  activaMarcaDefault?: boolean;
}

export const defaultValue: Readonly<IMarca> = {
  activaMarcaDefault: false,
};
