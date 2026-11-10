import { getTheme } from './mui-theme';
import { TOKENS } from './tokens';

type StyleOverrides = { styleOverrides?: Record<string, any> };

const componentOverrides = (mode: 'dark' | 'light'): Record<string, StyleOverrides> =>
  getTheme(mode).components as unknown as Record<string, StyleOverrides>;

describe('MUI theme derived from canonical tokens', () => {
  it('derives the dark palette from the dark tokens', () => {
    const tokens = TOKENS.dark;
    const theme = getTheme('dark');

    expect(theme.palette.mode).toBe('dark');
    expect(theme.palette.background.default).toBe(tokens.bg);
    expect(theme.palette.background.paper).toBe(tokens.surface);
    expect(theme.palette.text.primary).toBe(tokens.text);
    expect(theme.palette.text.secondary).toBe(tokens.textMuted);
    expect(theme.palette.divider).toBe(tokens.border);
    expect(theme.palette.primary.main).toBe(tokens.primary);
    expect(theme.palette.success.main).toBe(tokens.success);
    expect(theme.palette.warning.main).toBe(tokens.warning);
    expect(theme.palette.error.main).toBe(tokens.error);
  });

  it('derives the light palette from the light tokens', () => {
    const tokens = TOKENS.light;
    const theme = getTheme('light');

    expect(theme.palette.mode).toBe('light');
    expect(theme.palette.background.default).toBe(tokens.bg);
    expect(theme.palette.background.paper).toBe(tokens.surface);
    expect(theme.palette.text.primary).toBe(tokens.text);
    expect(theme.palette.text.secondary).toBe(tokens.textMuted);
    expect(theme.palette.divider).toBe(tokens.border);
    expect(theme.palette.primary.main).toBe(tokens.primary);
    expect(theme.palette.success.main).toBe(tokens.success);
    expect(theme.palette.warning.main).toBe(tokens.warning);
    expect(theme.palette.error.main).toBe(tokens.error);
  });

  it('keeps the global shape radius', () => {
    expect(getTheme('dark').shape.borderRadius).toBe(8);
    expect(getTheme('light').shape.borderRadius).toBe(8);
  });

  it('sources the divider and table cell borders from the tokens', () => {
    expect(componentOverrides('dark').MuiDivider.styleOverrides?.root).toMatchObject({ borderColor: TOKENS.dark.border });
    expect(componentOverrides('dark').MuiTableCell.styleOverrides?.root).toMatchObject({ borderBottomColor: TOKENS.dark.border });
    expect(componentOverrides('light').MuiDivider.styleOverrides?.root).toMatchObject({ borderColor: TOKENS.light.border });
    expect(componentOverrides('light').MuiTableCell.styleOverrides?.root).toMatchObject({ borderBottomColor: TOKENS.light.border });
  });

  it('sources the drawer and text field borders from the tokens', () => {
    expect(componentOverrides('dark').MuiDrawer.styleOverrides?.paper).toMatchObject({ borderRight: `1px solid ${TOKENS.dark.border}` });
    expect(componentOverrides('light').MuiDrawer.styleOverrides?.paper).toMatchObject({ borderRight: `1px solid ${TOKENS.light.border}` });

    const darkFieldset = componentOverrides('dark').MuiTextField.styleOverrides?.root?.['& .MuiOutlinedInput-root']?.['& fieldset'];
    const lightFieldset = componentOverrides('light').MuiTextField.styleOverrides?.root?.['& .MuiOutlinedInput-root']?.['& fieldset'];

    expect(darkFieldset).toMatchObject({ borderColor: TOKENS.dark.border });
    expect(lightFieldset).toMatchObject({ borderColor: TOKENS.light.border });
  });

  it('keeps every existing component override registered', () => {
    const expected = [
      'MuiCssBaseline',
      'MuiButton',
      'MuiCard',
      'MuiPaper',
      'MuiTableHead',
      'MuiTableCell',
      'MuiDialog',
      'MuiDrawer',
      'MuiDivider',
      'MuiTextField',
      'MuiChip',
      'MuiTooltip',
    ];

    expect(Object.keys(componentOverrides('dark')).sort()).toEqual(expected.sort());
    expect(Object.keys(componentOverrides('light')).sort()).toEqual(expected.sort());
  });
});
