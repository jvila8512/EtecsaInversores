import React, { createContext, useContext, useState, useCallback, useEffect, useMemo } from 'react';
import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';

import { getTheme } from './mui-theme';
import { TOKENS, toCssVariables } from './tokens';
import type { ThemeMode } from './tokens';

interface ThemeCtxValue {
  mode: ThemeMode;
  toggleMode: () => void;
}

const readThemeMode = (): ThemeMode => {
  return (localStorage.getItem('app-theme-mode') as ThemeMode) || 'dark';
};

/**
 * Applies the active mode to the document root:
 * - `data-theme` drives the mode specific CSS rules.
 * - `data-bs-theme` switches the Bootstrap 5.3 palette.
 * - The custom properties are set inline so they always win over any stylesheet rule.
 */
export const applyThemeTokens = (mode: ThemeMode): void => {
  const root = document.documentElement;

  root.setAttribute('data-theme', mode);
  root.setAttribute('data-bs-theme', mode);

  Object.entries(toCssVariables(TOKENS[mode])).forEach(([variable, value]) => {
    root.style.setProperty(variable, value);
  });
};

const ThemeCtx = createContext<ThemeCtxValue>({ mode: 'dark', toggleMode() {} });

export const useThemeMode = () => useContext(ThemeCtx);

export const AppThemeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [mode, setMode] = useState<ThemeMode>(readThemeMode);

  const toggleMode = useCallback(() => {
    setMode(prev => {
      const next: ThemeMode = prev === 'dark' ? 'light' : 'dark';
      localStorage.setItem('app-theme-mode', next);
      return next;
    });
  }, []);

  const theme = useMemo(() => getTheme(mode), [mode]);

  // Publish the canonical tokens on the document root
  useEffect(() => {
    applyThemeTokens(mode);
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
