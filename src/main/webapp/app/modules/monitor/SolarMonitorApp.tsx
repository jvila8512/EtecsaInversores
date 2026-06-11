import React, { useState, useEffect, useMemo, useCallback, createContext, useContext } from 'react';
import { Routes, Route, useNavigate, useParams, useLocation } from 'react-router-dom';
import { MONITOR_THEME_CSS, applyTheme, readTheme } from './theme';
import { useLiveData } from './hooks/useLiveData';
import { useInversores } from './hooks/useInversores';
import DashboardScreen from './screens/DashboardScreen';
import InversorDetailScreen from './screens/InversorDetailScreen';
import HistoryScreen from './screens/HistoryScreen';
import AlertsScreen from './screens/AlertsScreen';
import SettingsScreen from './screens/SettingsScreen';
import type { ThemeMode, WsStatus, LiveDataMap, InversorGroup, DashboardKPIs } from './types';
import type { IInversor } from 'app/shared/model/inversor.model';
import type { IDatoInversor } from 'app/shared/model/dato-inversor.model';
import type { IGatewayTcp } from 'app/shared/model/gateway-tcp.model';
import type { IEventoAlerta } from 'app/shared/model/evento-alerta.model';

interface MonitorContextValue {
  inversores: IInversor[];
  latestReadings: IDatoInversor[];
  gateways: IGatewayTcp[];
  groups: InversorGroup[];
  kpis: DashboardKPIs;
  alertas: IEventoAlerta[];
  alertCount: number;
  loading: boolean;
  refresh: () => void;
  liveData: LiveDataMap;
  wsStatus: WsStatus;
  theme: ThemeMode;
  toggleTheme: () => void;
}

const MonitorContext = createContext<MonitorContextValue | null>(null);

export const useMonitorContext = () => {
  const ctx = useContext(MonitorContext);
  if (!ctx) throw new Error('useMonitorContext must be used inside MonitorProvider');
  return ctx;
};

const MonitorProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [theme, setTheme] = useState<ThemeMode>(readTheme);
  const [liveAlertas, setLiveAlertas] = useState<IEventoAlerta[]>([]);

  const { inversores, latestReadings, gateways, groups, kpis, alertas, alertCount, loading, refresh } = useInversores();

  const inversorIds = useMemo(
    () =>
      inversores
        .filter(i => i.activo)
        .map(i => i.id)
        .filter((id): id is number => id !== undefined),
    [inversores],
  );

  const handleNewAlert = useCallback((alerta: IEventoAlerta) => {
    setLiveAlertas(prev => [alerta, ...prev.slice(0, 49)]);
  }, []);

  const { liveData, wsStatus } = useLiveData({ inversorIds, onAlert: handleNewAlert });

  useEffect(() => {
    applyTheme(theme);
  }, [theme]);

  useEffect(() => {
    const existingStyle = document.getElementById('solar-monitor-theme');
    if (!existingStyle) {
      const style = document.createElement('style');
      style.id = 'solar-monitor-theme';
      style.textContent = MONITOR_THEME_CSS;
      document.head.appendChild(style);
    }
  }, []);

  const allAlertas = useMemo(() => {
    const existingIds = new Set(alertas.map(a => a.id));
    const liveOnly = liveAlertas.filter(a => !existingIds.has(a.id));
    return [...liveOnly, ...alertas];
  }, [alertas, liveAlertas]);

  const toggleTheme = useCallback(() => {
    setTheme(prev => (prev === 'dark' ? 'light' : 'dark'));
  }, []);

  const value = useMemo(
    () => ({
      inversores,
      latestReadings,
      gateways,
      groups,
      kpis,
      alertas: allAlertas,
      alertCount,
      loading,
      refresh,
      liveData,
      wsStatus,
      theme,
      toggleTheme,
    }),
    [inversores, latestReadings, gateways, groups, kpis, allAlertas, alertCount, loading, refresh, liveData, wsStatus, theme, toggleTheme],
  );

  return <MonitorContext.Provider value={value}>{children}</MonitorContext.Provider>;
};

const DashboardRoute = () => {
  const { groups, kpis, liveData, loading, inversores } = useMonitorContext();
  const navigate = useNavigate();
  const trendValues = useMemo(() => Array.from({ length: 24 }, (_, i) => (i >= 6 && i < 20 ? Math.random() * 3000 + 500 : 0)), []);

  const handleInversorTap = (inv: IInversor) => {
    navigate(`/monitor/inversor/${inv.id}`);
  };

  if (loading && inversores.length === 0) {
    return <div style={{ textAlign: 'center', padding: 40, color: 'var(--tx3)', fontSize: 12 }}>Cargando datos del monitor...</div>;
  }

  return <DashboardScreen groups={groups} kpis={kpis} liveData={liveData} onInversorTap={handleInversorTap} trendValues={trendValues} />;
};

const InversorDetailRoute = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { inversores, liveData, alertas, loading } = useMonitorContext();

  const inversor = useMemo(() => inversores.find(i => String(i.id) === id) || null, [inversores, id]);
  const dato = inversor?.id ? liveData[inversor.id] || null : null;

  const handleBack = () => {
    navigate('/monitor');
  };

  if (loading) {
    return <div style={{ textAlign: 'center', padding: 40, color: 'var(--tx3)', fontSize: 12 }}>Cargando...</div>;
  }

  if (!inversor) {
    return (
      <div style={{ textAlign: 'center', padding: 40, color: 'var(--tx3)', fontSize: 12 }}>
        Inversor no encontrado
        <br />
        <button className="monitor-btn" onClick={handleBack} style={{ marginTop: 12 }}>
          Volver
        </button>
      </div>
    );
  }

  return <InversorDetailScreen inversor={inversor} dato={dato} alertas={alertas} onBack={handleBack} />;
};

const HistoryRoute = () => {
  const { inversores, gateways } = useMonitorContext();
  return <HistoryScreen inversores={inversores} gateways={gateways} />;
};

const AlertsRoute = () => {
  const { alertas } = useMonitorContext();
  return <AlertsScreen alertas={alertas} />;
};

const SettingsRoute = () => {
  const { inversores, gateways, liveData, refresh } = useMonitorContext();
  return <SettingsScreen inversores={inversores} gateways={gateways} liveData={liveData} onRefresh={refresh} />;
};

const SolarMonitorApp: React.FC = () => {
  const location = useLocation();

  return (
    <MonitorProvider>
      <div id="app">
        <div className="solar-monitor-root">
          <Routes>
            <Route index element={<DashboardRoute />} />
            <Route path="inversor/:id" element={<InversorDetailRoute />} />
            <Route path="history" element={<HistoryRoute />} />
            <Route path="alerts" element={<AlertsRoute />} />
            <Route path="settings" element={<SettingsRoute />} />
          </Routes>
        </div>
      </div>
    </MonitorProvider>
  );
};

export default SolarMonitorApp;
