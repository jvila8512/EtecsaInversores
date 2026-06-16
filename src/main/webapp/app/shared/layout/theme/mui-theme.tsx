import { createTheme } from '@mui/material/styles';
import type { PaletteMode } from '@mui/material';

/* ─── Industrial Solar Palette ─── */
const palette = {
  dark: {
    mode: 'dark' as PaletteMode,
    primary: { main: '#3b82f6', light: '#60a5fa', dark: '#2563eb', contrastText: '#fff' },
    secondary: { main: '#10b981', light: '#34d399', dark: '#059669' },
    success: { main: '#22c55e' },
    warning: { main: '#f59e0b' },
    error: { main: '#ef4444' },
    info: { main: '#3b82f6' },
    background: {
      default: '#0f1412',
      paper: '#1a211e',
    },
    text: {
      primary: '#e8edeb',
      secondary: '#889a91',
      disabled: '#4a5c54',
    },
    divider: '#2a342e',
  },
  light: {
    mode: 'light' as PaletteMode,
    primary: { main: '#2563eb', light: '#3b82f6', dark: '#1d4ed8', contrastText: '#fff' },
    secondary: { main: '#059669', light: '#10b981', dark: '#047857' },
    success: { main: '#22c55e' },
    warning: { main: '#f59e0b' },
    error: { main: '#ef4444' },
    info: { main: '#3b82f6' },
    background: {
      default: '#f0f3f2',
      paper: '#ffffff',
    },
    text: {
      primary: '#0f1a15',
      secondary: '#5b6b63',
      disabled: '#a3b1ab',
    },
    divider: '#d0d8d4',
  },
};

export const getTheme = (mode: PaletteMode) =>
  createTheme({
    palette: palette[mode],
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
              background: mode === 'dark' ? '#1a211e' : '#e8ece9',
            },
            '&::-webkit-scrollbar-thumb': {
              background: mode === 'dark' ? '#2a342e' : '#c4ccc8',
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
            border: mode === 'dark' ? '1px solid #2a342e' : 'none',
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
              backgroundColor: mode === 'dark' ? '#141b18' : '#f8faf9',
            },
          },
        },
      },
      MuiTableCell: {
        styleOverrides: {
          root: {
            borderBottomColor: mode === 'dark' ? '#2a342e' : '#d0d8d4',
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
            borderRight: mode === 'dark' ? '1px solid #2a342e' : '1px solid #d0d8d4',
          },
        },
      },
      MuiDivider: {
        styleOverrides: {
          root: {
            borderColor: mode === 'dark' ? '#2a342e' : '#d0d8d4',
          },
        },
      },
      MuiTextField: {
        styleOverrides: {
          root: {
            '& .MuiOutlinedInput-root': {
              '& fieldset': {
                borderColor: mode === 'dark' ? '#2a342e' : '#d0d8d4',
              },
              '&:hover fieldset': {
                borderColor: mode === 'dark' ? '#3b82f6' : '#2563eb',
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

export default getTheme;
