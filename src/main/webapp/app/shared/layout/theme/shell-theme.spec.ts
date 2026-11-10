// eslint-disable-next-line spaced-comment
/// <reference types="node" />
import fs from 'fs';
import path from 'path';

import { MONITOR_THEME_CSS } from '../../../modules/monitor/theme';

const read = (relativePath: string): string => fs.readFileSync(path.join(__dirname, relativePath), 'utf8');

const APP_SCSS = read('../../../app.scss');
const FOOTER_SCSS = read('../footer/footer.scss');
const SIDEBAR_SCSS = read('../sidebar/sidebar.scss');

/** Extracts the body of the first rule matching `selector`, honouring nested braces. */
const blockOf = (source: string, selector: string): string => {
  const start = source.indexOf(selector);
  if (start === -1) {
    throw new Error(`selector not found: ${selector}`);
  }

  const open = source.indexOf('{', start);
  let depth = 0;

  for (let index = open; index < source.length; index++) {
    if (source[index] === '{') {
      depth++;
    } else if (source[index] === '}') {
      depth--;
      if (depth === 0) {
        return source.slice(open + 1, index);
      }
    }
  }

  throw new Error(`unbalanced block for selector: ${selector}`);
};

describe('theme aware application shell', () => {
  it('paints the body with the theme background and text tokens', () => {
    const body = blockOf(APP_SCSS, 'body {');

    expect(body).toContain('background: var(--bg)');
    expect(body).toContain('color: var(--tx)');
    expect(body).not.toContain('#f8fafc');
  });

  it('paints the app container with the theme background token', () => {
    const appContainer = blockOf(APP_SCSS, '.app-container {');

    expect(appContainer).toContain('background-color: var(--bg)');
    expect(appContainer).not.toContain('#f8fafc');
  });

  it('paints the view cards with the card and border tokens', () => {
    const viewContainer = blockOf(APP_SCSS, '.view-container {');
    const card = blockOf(viewContainer, '.card {');

    expect(card).toContain('background: var(--card)');
    expect(card).toContain('border: 1px solid var(--bdr)');
    expect(card).toMatch(/box-shadow:/);
  });

  it('uses a stronger card shadow in dark mode', () => {
    expect(APP_SCSS).toContain(":root[data-theme='dark'] .view-container .card");
    expect(APP_SCSS).toMatch(/box-shadow:[^;]*0\.4[^;]*;/);
  });

  it('paints the readonly blocks with the background 3 token', () => {
    const readonly = blockOf(APP_SCSS, '.readonly {');

    expect(readonly).toContain('background-color: var(--bg3)');
    expect(readonly).not.toContain('#eee');
  });

  it('drives the theme specific rules from the document root attribute', () => {
    expect(APP_SCSS).toContain(":root[data-theme='light']");
    expect(APP_SCSS).toContain(":root[data-theme='dark']");
    expect(APP_SCSS).not.toContain('[data-light]');
  });

  it('keeps the modal close button readable in dark mode', () => {
    const darkRoot = blockOf(APP_SCSS, ":root[data-theme='dark'] {");
    const lightRoot = blockOf(APP_SCSS, ":root[data-theme='light'] {");

    expect(darkRoot).toContain('--close-filter: invert(1)');
    expect(lightRoot).toContain('--close-filter: none');
    // the filter must be carried by a variable: an id scoped `.btn-close` rule would
    // otherwise win over the attribute selector and leave the icon invisible
    expect(APP_SCSS).not.toContain('var(--close-filter, none)');
  });

  it('resolves the modal and form controls against the theme variables without white fallbacks', () => {
    expect(APP_SCSS).not.toMatch(/var\(--(bg|bg2|bg3|tx|tx2|tx3|bdr|bl|bl2|gr|am|rd)[a-z0-9-]*\s*,/);
  });

  it('paints the footer with the background 2 and border tokens', () => {
    expect(FOOTER_SCSS).toContain('background-color: var(--bg2)');
    expect(FOOTER_SCSS).toContain('border-top: 1px solid var(--bdr)');
    expect(FOOTER_SCSS).not.toContain('#f8fafc');
    expect(FOOTER_SCSS).not.toContain('#e2e8f0');
  });

  it('paints the sidebar navigation with the theme tokens', () => {
    const sidebarNav = blockOf(SIDEBAR_SCSS, '.sidebar-nav {');
    const menuPaper = blockOf(sidebarNav, '.MuiMenu-paper {');

    expect(sidebarNav).toContain('background-color: var(--bg3)');
    expect(sidebarNav).toContain('color: var(--bl)');
    expect(sidebarNav).not.toContain('#eff6ff');
    expect(sidebarNav).not.toContain('#dbeafe');
    expect(sidebarNav).not.toContain('#bfdbfe');

    expect(menuPaper).toContain('background-color: var(--card) !important');
    expect(menuPaper).toContain('color: var(--tx)');
    expect(SIDEBAR_SCSS).not.toContain('#fff !important');
  });
});

describe('solar monitor theme stylesheet', () => {
  it('still exports the monitor stylesheet contract', () => {
    expect(MONITOR_THEME_CSS).toContain('.solar-monitor-root {');
    expect(MONITOR_THEME_CSS).toContain('.monitor-topbar {');
    expect(MONITOR_THEME_CSS).toContain('.monitor-trend-card {');
    expect(MONITOR_THEME_CSS).toContain('@keyframes monitor-pulse');
  });

  it('no longer defines the color variables', () => {
    expect(MONITOR_THEME_CSS).not.toMatch(/^\s*--[a-z0-9-]+\s*:/m);
    expect(MONITOR_THEME_CSS).not.toContain('#app {');
    expect(MONITOR_THEME_CSS).not.toContain('[data-light]');
  });

  it('keeps the layout rules consuming the theme variables', () => {
    expect(blockOf(MONITOR_THEME_CSS, '.solar-monitor-root {')).toContain('background: var(--bg)');
    expect(blockOf(MONITOR_THEME_CSS, '.monitor-topbar {')).toContain('background: var(--bg2)');
    expect(blockOf(MONITOR_THEME_CSS, '.monitor-trend-card {')).toContain('background: var(--card)');
    expect(blockOf(MONITOR_THEME_CSS, '.monitor-kpi-card {')).toContain('border: 1px solid var(--bdr)');
  });
});
