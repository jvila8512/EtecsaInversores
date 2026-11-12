import { TOKENS, toCssVariables } from './tokens';
import type { ThemeMode } from './tokens';

const HEX_COLOR = /^#(?:[0-9a-fA-F]{3}|[0-9a-fA-F]{6})$/;

const MODES: ThemeMode[] = ['dark', 'light'];

describe('canonical theme tokens', () => {
  it('exposes exactly the dark and the light mode', () => {
    expect(Object.keys(TOKENS).sort()).toEqual(['dark', 'light']);
  });

  it('uses the same token key set for dark and light', () => {
    expect(Object.keys(TOKENS.dark).sort()).toEqual(Object.keys(TOKENS.light).sort());
  });

  it('recognizes valid hex colors and rejects invalid ones', () => {
    expect(HEX_COLOR.test('#0f1412')).toBe(true);
    expect(HEX_COLOR.test('#fff')).toBe(true);
    expect(HEX_COLOR.test('0f1412')).toBe(false);
    expect(HEX_COLOR.test('#12345')).toBe(false);
    expect(HEX_COLOR.test('rgb(15, 20, 18)')).toBe(false);
  });

  it('uses a valid hex color for every token of every mode', () => {
    const invalid: string[] = [];

    MODES.forEach(mode => {
      Object.entries(TOKENS[mode]).forEach(([key, value]) => {
        if (!HEX_COLOR.test(value)) {
          invalid.push(`${mode}.${key}=${value}`);
        }
      });
    });

    expect(invalid).toEqual([]);
  });

  it('exposes the canonical dark palette', () => {
    expect(TOKENS.dark).toEqual({
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
    });
  });

  it('exposes the canonical light palette', () => {
    expect(TOKENS.light).toEqual({
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
    });
  });

  it('maps the dark tokens to the expected CSS custom properties', () => {
    expect(toCssVariables(TOKENS.dark)).toEqual({
      '--bg': '#0f1412',
      '--bg2': '#141b18',
      '--bg3': '#1a211e',
      '--card': '#1a211e',
      '--bdr': '#2a342e',
      '--bdr2': '#354033',
      '--tx': '#e8edeb',
      '--tx2': '#c5d1cb',
      '--tx3': '#9aafa5',
      '--bl': '#3b82f6',
      '--md-primary': '#3b82f6',
      '--bl2': '#2563eb',
      '--gr': '#22c55e',
      '--md-tertiary': '#22c55e',
      '--am': '#f59e0b',
      '--md-secondary': '#f59e0b',
      '--rd': '#ef4444',
      '--md-info': '#3b82f6',
      '--bs-body-bg': '#0f1412',
      '--bs-body-color': '#e8edeb',
      '--bs-border-color': '#2a342e',
      '--bs-secondary-bg': '#141b18',
      '--bs-tertiary-bg': '#1a211e',
      '--bs-emphasis-color': '#e8edeb',
      '--bs-secondary-color': '#c5d1cb',
      '--bs-body-bg-rgb': '15, 20, 18',
      '--bs-body-color-rgb': '232, 237, 235',
      '--bs-border-color-rgb': '42, 52, 46',
    });
  });

  it('maps the light tokens to the expected CSS custom properties', () => {
    expect(toCssVariables(TOKENS.light)).toEqual({
      '--bg': '#f0f3f2',
      '--bg2': '#e8ece9',
      '--bg3': '#dce4de',
      '--card': '#ffffff',
      '--bdr': '#d0d8d4',
      '--bdr2': '#b3c4bc',
      '--tx': '#0f1a15',
      '--tx2': '#3a4d45',
      '--tx3': '#6a7f76',
      '--bl': '#2563eb',
      '--md-primary': '#2563eb',
      '--bl2': '#1d4ed8',
      '--gr': '#22c55e',
      '--md-tertiary': '#22c55e',
      '--am': '#f59e0b',
      '--md-secondary': '#f59e0b',
      '--rd': '#ef4444',
      '--md-info': '#2563eb',
      '--bs-body-bg': '#f0f3f2',
      '--bs-body-color': '#0f1a15',
      '--bs-border-color': '#d0d8d4',
      '--bs-secondary-bg': '#e8ece9',
      '--bs-tertiary-bg': '#dce4de',
      '--bs-emphasis-color': '#0f1a15',
      '--bs-secondary-color': '#3a4d45',
      '--bs-body-bg-rgb': '240, 243, 242',
      '--bs-body-color-rgb': '15, 26, 21',
      '--bs-border-color-rgb': '208, 216, 212',
    });
  });

  it('exposes the same custom properties for every mode', () => {
    expect(Object.keys(toCssVariables(TOKENS.light)).sort()).toEqual(Object.keys(toCssVariables(TOKENS.dark)).sort());
  });
});
