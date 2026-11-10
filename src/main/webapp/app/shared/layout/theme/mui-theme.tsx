import { createTheme } from '@mui/material/styles';
import type { PaletteMode } from '@mui/material';

import { TOKENS } from './tokens';
import type { ThemeMode, ThemeTokens } from './tokens';

/* ─── Palette derived from the canonical tokens ─── */
const buildPalette = (mode: ThemeMode, tokens: ThemeTokens) => ({
  mode: mode as PaletteMode,
  primary: { main: tokens.primary, dark: tokens.primaryDark, contrastText: '#ffffff' },
  secondary: { main: tokens.success },
  success: { main: tokens.success },
  warning: { main: tokens.warning },
  error: { main: tokens.error },
  info: { main: tokens.info },
  background: {
    default: tokens.bg,
    paper: tokens.surface,
  },
  text: {
    primary: tokens.text,
    secondary: tokens.textMuted,
    disabled: tokens.textDisabled,
  },
  divider: tokens.border,
});

export const getTheme = (mode: ThemeMode) => {
  const tokens = TOKENS[mode];

  return createTheme({
    palette: buildPalette(mode, tokens),
    typography: {
      fontFamily: '"Inter", "Roboto", "Helvetica", "Arial", sans-serif',
      h6: { fontWeight: 600 },
      subtitle1: { fontWeight: 500 },
      body2: { fontSize: '0.85rem' },
      caption: { fontSize: '0.75rem' },
    },
    shape: {
      borderRadius: 8,
    },
    components: {
      MuiCssBaseline: {
        styleOverrides: {
          '*': { '&:focus': { outline: 'none !important' } },
          body: {
            '&::-webkit-scrollbar': { width: '8px' },
            '&::-webkit-scrollbar-track': {
              background: tokens.bg2,
            },
            '&::-webkit-scrollbar-thumb': {
              background: tokens.border,
              borderRadius: '4px',
            },
          },
        },
      },
      MuiButton: {
        styleOverrides: {
          root: {
            textTransform: 'none',
            fontWeight: 500,
            borderRadius: 8,
          },
        },
      },
      MuiCard: {
        styleOverrides: {
          root: {
            boxShadow:
              mode === 'dark'
                ? '0 1px 3px 0 rgba(0,0,0,0.4), 0 1px 2px -1px rgba(0,0,0,0.3)'
                : '0 1px 3px 0 rgba(0,0,0,0.1), 0 1px 2px -1px rgba(0,0,0,0.1)',
            borderRadius: 12,
            border: mode === 'dark' ? `1px solid ${tokens.border}` : 'none',
          },
        },
      },
      MuiPaper: {
        styleOverrides: {
          root: {
            borderRadius: 12,
          },
        },
      },
      MuiTableHead: {
        styleOverrides: {
          root: {
            '& .MuiTableCell-head': {
              fontWeight: 600,
              fontSize: '0.75rem',
              textTransform: 'uppercase',
              letterSpacing: '0.05em',
              backgroundColor: tokens.bg2,
            },
          },
        },
      },
      MuiTableCell: {
        styleOverrides: {
          root: {
            borderBottomColor: tokens.border,
          },
        },
      },
      MuiDialog: {
        styleOverrides: {
          paper: {
            boxShadow: mode === 'dark' ? '0 8px 32px rgba(0,0,0,0.6)' : '0 8px 32px rgba(0,0,0,0.12)',
          },
        },
      },
      MuiDrawer: {
        styleOverrides: {
          paper: {
            borderRight: `1px solid ${tokens.border}`,
          },
        },
      },
      MuiDivider: {
        styleOverrides: {
          root: {
            borderColor: tokens.border,
          },
        },
      },
      MuiTextField: {
        styleOverrides: {
          root: {
            '& .MuiOutlinedInput-root': {
              '& fieldset': {
                borderColor: tokens.border,
              },
              '&:hover fieldset': {
                borderColor: tokens.primary,
              },
            },
          },
        },
      },
      MuiChip: {
        styleOverrides: {
          root: {
            fontWeight: 500,
          },
        },
      },
      MuiTooltip: {
        styleOverrides: {
          tooltip: {
            fontSize: '0.75rem',
          },
        },
      },
    },
  });
};

export default getTheme;
