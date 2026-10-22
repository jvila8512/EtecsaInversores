import 'react-toastify/dist/ReactToastify.css';
import './app.scss';
import 'app/config/dayjs';

import React, { useEffect, useState } from 'react';
import { BrowserRouter } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import { getProfile } from 'app/shared/reducers/application-profile';
import { AppThemeProvider } from 'app/shared/layout/theme/ThemeContext';
import { MONITOR_THEME_CSS } from 'app/modules/monitor/theme';
import Sidebar from 'app/shared/layout/sidebar/sidebar';
import TopBar from 'app/shared/layout/topbar/topbar';
import Footer from 'app/shared/layout/footer/footer';
import ErrorBoundary from 'app/shared/error/error-boundary';
import { AUTHORITIES } from 'app/config/constants';
import AppRoutes from 'app/routes';

const baseHref = document.querySelector('base').getAttribute('href').replace(/\/$/, '');

export const App = () => {
  const dispatch = useAppDispatch();
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);

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
    <AppThemeProvider>
      <BrowserRouter basename={baseHref}>
        <div id="app" className={`app-container${sidebarCollapsed ? ' sidebar-collapsed' : ''}`}>
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
    </AppThemeProvider>
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
