import 'react-toastify/dist/ReactToastify.css';
import './app.scss';
import 'app/config/dayjs';

import React, { useEffect, useState, createContext, useContext, useCallback, useMemo } from 'react';
import { BrowserRouter } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import { ThemeProvider } from '@mui/material/styles';
import type { PaletteMode } from '@mui/material';
import CssBaseline from '@mui/material/CssBaseline';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import { getProfile } from 'app/shared/reducers/application-profile';
import { getTheme } from 'app/shared/layout/theme/mui-theme';
import { MONITOR_THEME_CSS } from 'app/modules/monitor/theme';
import Sidebar from 'app/shared/layout/sidebar/sidebar';
import TopBar from 'app/shared/layout/topbar/topbar';
import Footer from 'app/shared/layout/footer/footer';
import ErrorBoundary from 'app/shared/error/error-boundary';
import { AUTHORITIES } from 'app/config/constants';
import AppRoutes from 'app/routes';

const baseHref = document.querySelector('base').getAttribute('href').replace(/\/$/, '');

/* --- Global Theme Context --- */
interface ThemeCtxValue {
  mode: PaletteMode;
  toggleMode: () => void;
}

const readThemeMode = (): PaletteMode => {
  return (localStorage.getItem('app-theme-mode') as PaletteMode) || 'dark';
};

const ThemeCtx = createContext<ThemeCtxValue>({ mode: 'dark' as PaletteMode, toggleMode() {} });

export const useThemeMode = () => useContext(ThemeCtx);

export const App = () => {
  const dispatch = useAppDispatch();
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  const [mode, setMode] = useState<PaletteMode>(readThemeMode);

  useEffect(() => {
    dispatch(getSession());
    dispatch(getProfile());
  }, []);

  // Inject Solar Monitor CSS variables once at app level
  useEffect(() => {
    const existing = document.getElementById('solar-monitor-theme');
    if (!existing) {
      const style = document.createElement('style');
      style.id = 'solar-monitor-theme';
      style.textContent = MONITOR_THEME_CSS;
      document.head.appendChild(style);
    }
  }, []);

  const toggleMode = useCallback(() => {
    setMode(prev => {
      const next: PaletteMode = prev === 'dark' ? 'light' : 'dark';
      localStorage.setItem('app-theme-mode', next);
      return next;
    });
  }, []);

  const theme = useMemo(() => getTheme(mode), [mode]);

  useEffect(() => {
    // Sync CSS variable for Solar Monitor (which reads #app[data-light])
    const appEl = document.getElementById('app');
    if (appEl) {
      if (mode === 'light') {
        appEl.setAttribute('data-light', '');
      } else {
        appEl.removeAttribute('data-light');
      }
    }
  }, [mode]);

  const currentLocale = useAppSelector(state => state.locale.currentLocale);
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const ribbonEnv = useAppSelector(state => state.applicationProfile.ribbonEnv);
  const isInProduction = useAppSelector(state => state.applicationProfile.inProduction);
  const isOpenAPIEnabled = useAppSelector(state => state.applicationProfile.isOpenAPIEnabled);

  const handleSidebarCollapse = (collapsed: boolean) => {
    setSidebarCollapsed(collapsed);
  };

  return (
    <ThemeCtx.Provider value={{ mode, toggleMode }}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <BrowserRouter basename={baseHref}>
          <div className={`app-container${sidebarCollapsed ? ' sidebar-collapsed' : ''}`}>
            <ToastContainer position="top-left" className="toastify-container" toastClassName="toastify-toast" />
            <ErrorBoundary>
              <Sidebar
                isAuthenticated={isAuthenticated}
                isAdmin={isAdmin}
                currentLocale={currentLocale}
                ribbonEnv={ribbonEnv}
                isInProduction={isInProduction}
                isOpenAPIEnabled={isOpenAPIEnabled}
                onCollapse={handleSidebarCollapse}
              />
            </ErrorBoundary>
            <div className="main-content">
              <TopBar />
              <div className="view-container" id="app-view-container">
                <ErrorBoundary>
                  <AppRoutes />
                </ErrorBoundary>
              </div>
              <Footer />
            </div>
          </div>
        </BrowserRouter>
      </ThemeProvider>
    </ThemeCtx.Provider>
  );
};

const hasAnyAuthority = (authorities: string[], hasAny: string[]): boolean => {
  if (authorities && authorities.length !== 0) {
    if (hasAny && hasAny.length !== 0) {
      return authorities.some((authority: string) => hasAny.includes(authority));
    }
  }
  return false;
};

export default App;
