import { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import type { IInversor } from 'app/shared/model/inversor.model';
import type { IGatewayTcp } from 'app/shared/model/gateway-tcp.model';
import type { IDatoEficiencia } from 'app/shared/model/dato-eficiencia.model';
import type { IEventoAlerta } from 'app/shared/model/evento-alerta.model';
import type { InversorGroup, DashboardKPIs } from '../types';

const API_URL = 'api';

interface UseInversoresResult {
  inversores: IInversor[];
  gateways: IGatewayTcp[];
  groups: InversorGroup[];
  kpis: DashboardKPIs;
  alertas: IEventoAlerta[];
  alertCount: number;
  loading: boolean;
  refresh: () => void;
}

export function useInversores(): UseInversoresResult {
  const [inversores, setInversores] = useState<IInversor[]>([]);
  const [gateways, setGateways] = useState<IGatewayTcp[]>([]);
  const [eficiencias, setEficiencias] = useState<IDatoEficiencia[]>([]);
  const [alertas, setAlertas] = useState<IEventoAlerta[]>([]);
  const [loading, setLoading] = useState(true);

  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      const [invRes, gwRes, efRes, alertRes] = await Promise.all([
        axios.get<IInversor[]>(`${API_URL}/inversors?size=200`),
        axios.get<IGatewayTcp[]>(`${API_URL}/gateway-tcps?size=50`),
        axios.get<IDatoEficiencia[]>(`${API_URL}/dato-eficiencias?size=200`),
        axios.get<IEventoAlerta[]>(`${API_URL}/evento-alertas?sort=timestamp,desc&size=50`),
      ]);
      setInversores(invRes.data || []);
      setGateways((gwRes.data || []).filter(g => g.activo));
      setEficiencias(efRes.data || []);
      setAlertas(alertRes.data || []);
    } catch (e) {
      console.error('Error fetching monitor data:', e);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  // Build groups
  const groups: InversorGroup[] = gateways.map(gw => {
    const gwInversores = inversores.filter(i => i.gateway?.id === gw.id && i.activo);
    const master = gwInversores.find(i => i.esMaster);
    const esclavos = gwInversores.filter(i => !i.esMaster);
    const alertCount = alertas.filter(a => a.inversor?.gateway?.id === gw.id && !a.resuelto).length;
    return {
      gateway: gw,
      master,
      esclavos,
      totalPotenciaW: 0, // will be updated by live data
      alertCount,
    };
  });

  // Also add independent inversores (no gateway)
  const independientes = inversores.filter(i => i.activo && !i.gateway);

  if (independientes.length > 0) {
    groups.push({
      gateway: { id: 0, nombre: 'Independientes', activo: true } as IGatewayTcp,
      master: undefined,
      esclavos: independientes,
      totalPotenciaW: 0,
      alertCount: 0,
    });
  }

  // KPIs
  const today = new Date();
  const todayStr = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;
  const todayEficiencias = eficiencias.filter(e => e.fecha && (e.fecha as any).startsWith?.(todayStr));

  const kpis: DashboardKPIs = {
    generacionKwh: todayEficiencias.reduce((sum, e) => sum + (e.energiaPvWh || 0), 0) / 1000,
    consumoKwh: todayEficiencias.reduce((sum, e) => sum + (e.energiaCargaWh || 0), 0) / 1000,
    potenciaKw: 0, // will be calculated from live data
  };

  const unresolvedAlertas = alertas.filter(a => !a.resuelto);
  const alertCount = unresolvedAlertas.length;

  return {
    inversores,
    gateways,
    groups,
    kpis,
    alertas: unresolvedAlertas,
    alertCount,
    loading,
    refresh: fetchData,
  };
}
