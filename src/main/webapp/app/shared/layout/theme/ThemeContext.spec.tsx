import React from 'react';
import { fireEvent, render, screen } from '@testing-library/react';

import { AppThemeProvider, useThemeMode } from './ThemeContext';
import { TOKENS } from './tokens';

const ToggleThemeButton = () => {
  const { toggleMode } = useThemeMode();

  return (
    <button type="button" onClick={toggleMode}>
      toggle theme
    </button>
  );
};

const renderProvider = () =>
  render(
    <AppThemeProvider>
      <ToggleThemeButton />
    </AppThemeProvider>,
  );

describe('AppThemeProvider runtime token injection', () => {
  beforeEach(() => {
    localStorage.clear();
    document.documentElement.removeAttribute('data-theme');
    document.documentElement.removeAttribute('style');
  });

  it('marks the document root as dark and injects the dark custom properties by default', () => {
    renderProvider();

    expect(document.documentElement.getAttribute('data-theme')).toBe('dark');
    expect(document.documentElement.style.getPropertyValue('--bg')).toBe(TOKENS.dark.bg);
    expect(document.documentElement.style.getPropertyValue('--tx')).toBe(TOKENS.dark.text);
    expect(document.documentElement.style.getPropertyValue('--card')).toBe(TOKENS.dark.surface);
    expect(document.documentElement.style.getPropertyValue('--bdr')).toBe(TOKENS.dark.border);
  });

  it('restores the stored light mode and injects the light custom properties', () => {
    localStorage.setItem('app-theme-mode', 'light');

    renderProvider();

    expect(document.documentElement.getAttribute('data-theme')).toBe('light');
    expect(document.documentElement.style.getPropertyValue('--bg')).toBe(TOKENS.light.bg);
    expect(document.documentElement.style.getPropertyValue('--tx')).toBe(TOKENS.light.text);
    expect(document.documentElement.style.getPropertyValue('--card')).toBe(TOKENS.light.surface);
    expect(document.documentElement.style.getPropertyValue('--bdr')).toBe(TOKENS.light.border);
  });

  it('updates the root attribute and the custom properties when the mode is toggled', () => {
    renderProvider();

    fireEvent.click(screen.getByRole('button', { name: 'toggle theme' }));

    expect(document.documentElement.getAttribute('data-theme')).toBe('light');
    expect(document.documentElement.style.getPropertyValue('--bg')).toBe(TOKENS.light.bg);
    expect(document.documentElement.style.getPropertyValue('--tx')).toBe(TOKENS.light.text);

    fireEvent.click(screen.getByRole('button', { name: 'toggle theme' }));

    expect(document.documentElement.getAttribute('data-theme')).toBe('dark');
    expect(document.documentElement.style.getPropertyValue('--bg')).toBe(TOKENS.dark.bg);
    expect(document.documentElement.style.getPropertyValue('--tx')).toBe(TOKENS.dark.text);
  });

  it('switches the bootstrap palette together with the application theme', () => {
    renderProvider();

    expect(document.documentElement.getAttribute('data-bs-theme')).toBe('dark');
    expect(document.documentElement.style.getPropertyValue('--bs-body-bg')).toBe(TOKENS.dark.bg);
    expect(document.documentElement.style.getPropertyValue('--bs-body-color')).toBe(TOKENS.dark.text);
    expect(document.documentElement.style.getPropertyValue('--bs-border-color')).toBe(TOKENS.dark.border);

    fireEvent.click(screen.getByRole('button', { name: 'toggle theme' }));

    expect(document.documentElement.getAttribute('data-bs-theme')).toBe('light');
    expect(document.documentElement.style.getPropertyValue('--bs-body-bg')).toBe(TOKENS.light.bg);
    expect(document.documentElement.style.getPropertyValue('--bs-body-color')).toBe(TOKENS.light.text);
  });

  it('publishes the shared design scale on the document root', () => {
    renderProvider();

    expect(document.documentElement.style.getPropertyValue('--radius-sm')).toBe('4px');
    expect(document.documentElement.style.getPropertyValue('--radius-md')).toBe('8px');
    expect(document.documentElement.style.getPropertyValue('--radius-lg')).toBe('12px');
    expect(document.documentElement.style.getPropertyValue('--bs-border-radius')).toBe('8px');
  });

  it('never uses the legacy data-light attribute', () => {
    const probe = document.createElement('div');
    probe.setAttribute('data-light', '');
    document.body.appendChild(probe);
    expect(document.querySelectorAll('[data-light]')).toHaveLength(1);
    document.body.removeChild(probe);

    const appElement = document.createElement('div');
    appElement.id = 'app';
    document.body.appendChild(appElement);

    renderProvider();

    expect(document.documentElement.hasAttribute('data-light')).toBe(false);
    expect(appElement.hasAttribute('data-light')).toBe(false);
    expect(document.querySelectorAll('[data-light]')).toHaveLength(0);

    document.body.removeChild(appElement);
  });

  it('injects every custom property of the active mode', () => {
    renderProvider();

    const expected = [
      '--bg',
      '--bg2',
      '--bg3',
      '--card',
      '--bdr',
      '--bdr2',
      '--tx',
      '--tx2',
      '--tx3',
      '--bl',
      '--bl2',
      '--gr',
      '--am',
      '--rd',
      '--md-primary',
      '--md-secondary',
      '--md-tertiary',
      '--md-info',
    ];

    const injected = expected.filter(name => document.documentElement.style.getPropertyValue(name) !== '');

    expect(injected).toEqual(expected);
  });
});
