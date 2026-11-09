import React, { createContext, useContext, useState, useCallback, useEffect, useMemo } from 'react';
import { ThemeProvider } from '@mui/material/styles';
import type { PaletteMode } from '@mui/material';
import CssBaseline from '@mui/material/CssBaseline';
import { getTheme } from './mui-theme';

interface ThemeCtxValue {
  mode: PaletteMode;
  toggleMode: () => void;
}

const readThemeMode = (): PaletteMode => {
  return (localStorage.getItem('app-theme-mode') as PaletteMode) || 'dark';
};

const ThemeCtx = createContext<ThemeCtxValue>({ mode: 'dark', toggleMode() {} });

export const useThemeMode = () => useContext(ThemeCtx);

export const AppThemeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [mode, setMode] = useState<PaletteMode>(readThemeMode);

  const toggleMode = useCallback(() => {
    setMode(prev => {
      const next: PaletteMode = prev === 'dark' ? 'light' : 'dark';
      localStorage.setItem('app-theme-mode', next);
      return next;
    });
  }, []);

  const theme = useMemo(() => getTheme(mode), [mode]);

  // Sync data-light attribute for Solar Monitor CSS variables
  useEffect(() => {
    const appEl = document.getElementById('app');
    if (appEl) {
      if (mode === 'light') {
        appEl.setAttribute('data-light', '');
      } else {
        appEl.removeAttribute('data-light');
      }
    }
  }, [mode]);

  return (
    <ThemeCtx.Provider value={{ mode, toggleMode }}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        {children}
      </ThemeProvider>
    </ThemeCtx.Provider>
  );
};
