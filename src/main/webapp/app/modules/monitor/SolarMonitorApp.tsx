import React, { useState, useEffect, useMemo, useCallback } from 'react';
import { MONITOR_THEME_CSS, applyTheme, readTheme } from './theme';
import { useLiveData } from './hooks/useLiveData';
import { useInversores } from './hooks/useInversores';
import DashboardScreen from './screens/DashboardScreen';
import InversorDetailScreen from './screens/InversorDetailScreen';
import HistoryScreen from './screens/HistoryScreen';
import AlertsScreen from './screens/AlertsScreen';
import SettingsScreen from './screens/SettingsScreen';
import type { ScreenName, ThemeMode, WsStatus } from './types';
import type { IInversor } from 'app/shared/model/inversor.model';
import type { IEventoAlerta } from 'app/shared/model/evento-alerta.model';

/* ─── SVG icons for bottom nav ─── */
const GridIcon = () => (
  <svg viewBox="0 0 20 20" fill="currentColor">
    <rect x="1" y="1" width="8" height="8" rx="1" />
    <rect x="11" y="1" width="8" height="8" rx="1" />
    <rect x="1" y="11" width="8" height="8" rx="1" />
    <rect x="11" y="11" width="8" height="8" rx="1" />
  </svg>
);

const DocIcon = () => (
  <svg viewBox="0 0 20 20" fill="currentColor">
    <rect x="3" y="1" width="14" height="18" rx="2" fill="none" stroke="currentColor" strokeWidth="1.5" />
    <line x1="6" y1="6" x2="14" y2="6" stroke="currentColor" strokeWidth="1.2" />
    <line x1="6" y1="9" x2="14" y2="9" stroke="currentColor" strokeWidth="1.2" />
    <line x1="6" y1="12" x2="10" y2="12" stroke="currentColor" strokeWidth="1.2" />
  </svg>
);

const ActivityIcon = () => (
  <svg viewBox="0 0 20 20" fill="currentColor">
    <polyline points="1,10 5,10 7,4 10,16 13,10 15,10 19,10" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinejoin="round" />
  </svg>
);

const BellIcon = () => (
  <svg viewBox="0 0 20 20" fill="currentColor">
    <path d="M10 2a5 5 0 0 0-5 5v3l-2 2v1h14v-1l-2-2V7a5 5 0 0 0-5-5zm0 16a2 2 0 0 1-2-2h4a2 2 0 0 1-2 2z" />
  </svg>
);

const SettingsIcon = () => (
  <svg viewBox="0 0 20 20" fill="currentColor">
    <rect x="1" y="1" width="7" height="7" rx="1" fill="none" stroke="currentColor" strokeWidth="1.5" />
    <rect x="12" y="1" width="7" height="7" rx="1" fill="none" stroke="currentColor" strokeWidth="1.5" />
    <rect x="1" y="12" width="7" height="7" rx="1" fill="none" stroke="currentColor" strokeWidth="1.5" />
    <circle cx="15.5" cy="15.5" r="3" fill="none" stroke="currentColor" strokeWidth="1.5" />
  </svg>
);

const SCREEN_TITLES: Record<ScreenName, string> = {
  dashboard: 'DASHBOARD',
  'inverter-detail': 'INVERTER DETAIL',
  history: 'HISTORY',
  alerts: 'ALERTS',
  settings: 'SETTINGS',
};

const NAV_ITEMS: { key: ScreenName; label: string; icon: React.FC }[] = [
  { key: 'dashboard', label: 'DASH', icon: GridIcon },
  { key: 'inverter-detail', label: 'INVERTERS', icon: DocIcon },
  { key: 'history', label: 'HISTORY', icon: ActivityIcon },
  { key: 'alerts', label: 'ALERTS', icon: BellIcon },
  { key: 'settings', label: 'SETTINGS', icon: SettingsIcon },
];

const SolarMonitorApp: React.FC = () => {
  const [screen, setScreen] = useState<ScreenName>('dashboard');
  const [theme, setTheme] = useState<ThemeMode>(readTheme);
  const [selectedInversor, setSelectedInversor] = useState<IInversor | null>(null);
  const [liveAlertas, setLiveAlertas] = useState<IEventoAlerta[]>([]);

  const { inversores, gateways, groups, kpis, alertas, alertCount, loading, refresh } = useInversores();

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

  // Apply theme on mount and change
  useEffect(() => {
    applyTheme(theme);
  }, [theme]);

  // Inject CSS once
  useEffect(() => {
    const existingStyle = document.getElementById('solar-monitor-theme');
    if (!existingStyle) {
      const style = document.createElement('style');
      style.id = 'solar-monitor-theme';
      style.textContent = MONITOR_THEME_CSS;
      document.head.appendChild(style);
    }
  }, []);

  const handleInversorTap = (inv: IInversor) => {
    setSelectedInversor(inv);
    setScreen('inverter-detail');
  };

  const handleBack = () => {
    setSelectedInversor(null);
    setScreen('dashboard');
  };

  // Combine REST + live alerts
  const allAlertas = useMemo(() => {
    const existingIds = new Set(alertas.map(a => a.id));
    const liveOnly = liveAlertas.filter(a => !existingIds.has(a.id));
    return [...liveOnly, ...alertas];
  }, [alertas, liveAlertas]);

  // Fake 24h trend data (placeholder)
  const trendValues = useMemo(() => Array.from({ length: 24 }, (_, i) => (i >= 6 && i < 20 ? Math.random() * 3000 + 500 : 0)), []);

  const wsClass = wsStatus === 'connected' ? '' : wsStatus === 'reconnecting' ? 'reconnecting' : 'disconnected';
  const wsText = wsStatus === 'connected' ? 'LIVE' : wsStatus === 'reconnecting' ? 'RECON' : 'OFF';

  // Get user initials for avatar
  const userInitials = 'U'; // placeholder — could read from JHipster auth store

  return (
    <div id="app">
      <div className="solar-monitor-root">
        {/* Top App Bar */}
        <div className="monitor-topbar">
          <div style={{ display: 'flex', alignItems: 'center' }}>
            <span className="monitor-topbar-title">{SCREEN_TITLES[screen]}</span>
            <div className={`monitor-topbar-live ${wsClass}`}>
              <span className="dot" />
              {wsText}
            </div>
          </div>
          <div className="monitor-topbar-actions">
            <button className="monitor-theme-toggle" onClick={() => setTheme(prev => (prev === 'dark' ? 'light' : 'dark'))}>
              {theme === 'dark' ? '☀' : '🌙'}
            </button>
            <button className="monitor-bell-badge" onClick={() => setScreen('alerts')}>
              🔔
              {alertCount > 0 && <span className="badge-count">{alertCount > 99 ? '99+' : alertCount}</span>}
            </button>
            <div className="monitor-avatar">{userInitials}</div>
          </div>
        </div>

        {/* Screen content */}
        {loading && inversores.length === 0 ? (
          <div style={{ textAlign: 'center', padding: 40, color: 'var(--tx3)', fontSize: 12 }}>Cargando datos del monitor...</div>
        ) : (
          <>
            {screen === 'dashboard' && (
              <DashboardScreen
                groups={groups}
                kpis={kpis}
                liveData={liveData}
                onInversorTap={handleInversorTap}
                trendValues={trendValues}
              />
            )}
            {screen === 'inverter-detail' && selectedInversor && (
              <InversorDetailScreen
                inversor={selectedInversor}
                dato={selectedInversor.id ? liveData[selectedInversor.id] || null : null}
                alertas={allAlertas}
                onBack={handleBack}
              />
            )}
            {screen === 'history' && <HistoryScreen inversores={inversores} gateways={gateways} />}
            {screen === 'alerts' && <AlertsScreen alertas={allAlertas} />}
            {screen === 'settings' && (
              <SettingsScreen inversores={inversores} gateways={gateways} liveData={liveData} onRefresh={refresh} />
            )}
          </>
        )}

        {/* Bottom Navigation */}
        <div className="monitor-bottomnav">
          {NAV_ITEMS.map(item => (
            <button
              key={item.key}
              className={`monitor-nav-item ${screen === item.key ? 'active' : ''}`}
              onClick={() => {
                if (item.key === 'inverter-detail' && !selectedInversor) {
                  // If no inverter selected yet, go to dashboard
                  setScreen('dashboard');
                } else {
                  setScreen(item.key);
                }
              }}
            >
              <item.icon />
              {item.label}
              {item.key === 'alerts' && alertCount > 0 && <span className="nav-badge" />}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default SolarMonitorApp;
