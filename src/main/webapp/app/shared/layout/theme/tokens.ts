/* ─── Canonical design tokens ───
 * Single source of truth for every color used by the application.
 * The MUI palette and the CSS custom properties are both derived from here.
 */

export type ThemeMode = 'dark' | 'light';

export interface ThemeTokens {
  bg: string;
  bg2: string;
  bg3: string;
  surface: string;
  border: string;
  borderStrong: string;
  text: string;
  textMuted: string;
  textDisabled: string;
  primary: string;
  primaryDark: string;
  success: string;
  warning: string;
  error: string;
  info: string;
}

export const TOKENS: Record<ThemeMode, ThemeTokens> = {
  dark: {
    bg: '#0f1412',
    bg2: '#141b18',
    bg3: '#1a211e',
    surface: '#1a211e',
    border: '#2a342e',
    borderStrong: '#354033',
    text: '#e8edeb',
    textMuted: '#c5d1cb',
    textDisabled: '#9aafa5',
    primary: '#3b82f6',
    primaryDark: '#2563eb',
    success: '#22c55e',
    warning: '#f59e0b',
    error: '#ef4444',
    info: '#3b82f6',
  },
  light: {
    bg: '#f0f3f2',
    bg2: '#e8ece9',
    bg3: '#dce4de',
    surface: '#ffffff',
    border: '#d0d8d4',
    borderStrong: '#b3c4bc',
    text: '#0f1a15',
    textMuted: '#3a4d45',
    textDisabled: '#6a7f76',
    primary: '#2563eb',
    primaryDark: '#1d4ed8',
    success: '#22c55e',
    warning: '#f59e0b',
    error: '#ef4444',
    info: '#2563eb',
  },
};

/* ─── Token → CSS custom property mapping ─── */
const CSS_VARIABLES: Record<keyof ThemeTokens, string[]> = {
  bg: ['--bg'],
  bg2: ['--bg2'],
  bg3: ['--bg3'],
  surface: ['--card'],
  border: ['--bdr'],
  borderStrong: ['--bdr2'],
  text: ['--tx'],
  textMuted: ['--tx2'],
  textDisabled: ['--tx3'],
  primary: ['--bl', '--md-primary'],
  primaryDark: ['--bl2'],
  success: ['--gr', '--md-tertiary'],
  warning: ['--am', '--md-secondary'],
  error: ['--rd'],
  info: ['--md-info'],
};

/* ─── Bootstrap bridge ───
 * Bootstrap 5.3 reads its palette from the `--bs-*` custom properties and
 * switches to the dark palette through the `data-bs-theme` attribute. Binding
 * them to the canonical tokens keeps every Bootstrap component (tables,
 * dropdowns, form controls, modals) in sync with the application theme.
 */
const BOOTSTRAP_VARIABLES: Record<keyof ThemeTokens, string[]> = {
  bg: ['--bs-body-bg'],
  text: ['--bs-body-color', '--bs-emphasis-color'],
  textMuted: ['--bs-secondary-color'],
  border: ['--bs-border-color'],
  bg2: ['--bs-secondary-bg'],
  bg3: ['--bs-tertiary-bg'],
};

/** Converts a hex color into the `r, g, b` triplet Bootstrap expects. */
const hexToRgbTriplet = (hex: string): string => {
  const value = hex.replace('#', '');
  const parse = (chunk: string): number => parseInt(chunk, 16);

  return [parse(value.slice(0, 2)), parse(value.slice(2, 4)), parse(value.slice(4, 6))].join(', ');
};

export function toCssVariables(tokens: ThemeTokens): Record<string, string> {
  const variables: Record<string, string> = {};

  (Object.keys(CSS_VARIABLES) as (keyof ThemeTokens)[]).forEach(token => {
    CSS_VARIABLES[token].forEach(variable => {
      variables[variable] = tokens[token];
    });
  });

  (Object.keys(BOOTSTRAP_VARIABLES) as (keyof ThemeTokens)[]).forEach(token => {
    BOOTSTRAP_VARIABLES[token].forEach(variable => {
      variables[variable] = tokens[token];
    });
  });

  // Bootstrap builds its alpha variants from the rgb triplets of the core colors
  variables['--bs-body-bg-rgb'] = hexToRgbTriplet(tokens.bg);
  variables['--bs-body-color-rgb'] = hexToRgbTriplet(tokens.text);
  variables['--bs-border-color-rgb'] = hexToRgbTriplet(tokens.border);

  return variables;
}

export default TOKENS;
