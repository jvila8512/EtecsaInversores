// eslint-disable-next-line spaced-comment
/// <reference types="node" />
import fs from 'fs';
import path from 'path';

import { MONITOR_THEME_CSS } from '../../../modules/monitor/theme';

const readComponent = (relativePath: string): string => fs.readFileSync(path.join(__dirname, relativePath), 'utf8');

const TREND_BARS = readComponent('../../../modules/monitor/components/TrendBars.tsx');
const SOC_RING = readComponent('../../../modules/monitor/components/SocRing.tsx');
const AREA_CHART = readComponent('../../../modules/monitor/components/AreaChart.tsx');

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

const MONO_DECLARATION = "font-family: 'IBM Plex Mono'";

const fontSizeOf = (selector: string): number => {
  const match = /font-size:\s*(\d+(?:\.\d+)?)px/.exec(blockOf(MONITOR_THEME_CSS, selector));
  if (!match) {
    throw new Error(`font-size not found: ${selector}`);
  }
  return Number(match[1]);
};

const fontWeightOf = (selector: string): number => {
  const match = /font-weight:\s*(\d+)/.exec(blockOf(MONITOR_THEME_CSS, selector));
  if (!match) {
    throw new Error(`font-weight not found: ${selector}`);
  }
  return Number(match[1]);
};

const TECHNICAL_SELECTORS = [
  '.solar-monitor-root .mono {',
  '.monitor-group-header .gw-ip {',
  '.monitor-gw-card .gw-ip {',
  '.monitor-date-input {',
];

const NUMERIC_SELECTORS = [
  '.monitor-kpi-value {',
  '.monitor-inversor-card .inv-power {',
  '.monitor-inversor-card .inv-temp {',
  '.monitor-group-total .total-value {',
  '.monitor-metric-item .metric-value {',
  '.monitor-slider-row .slider-value {',
  '.monitor-table {',
  '.bms-cell {',
  '.monitor-gw-card .gw-metric-value {',
];

const PROSE_SELECTORS = [
  '.monitor-detail-header .inv-subtitle {',
  '.monitor-alert-item .alert-meta {',
  '.monitor-kpi-unit {',
  '.monitor-metric-item .metric-unit {',
  '.monitor-topbar-live {',
];

describe('solar monitor typography', () => {
  it('renders the monitor with a humanist sans as the base family', () => {
    expect(blockOf(MONITOR_THEME_CSS, '.solar-monitor-root {')).toContain("font-family: 'IBM Plex Sans'");
  });

  it('uses no monospace family anywhere in the monitor stylesheet', () => {
    expect(MONITOR_THEME_CSS).not.toContain('IBM Plex Mono');
  });

  it('renders every numeric readout with tabular figures', () => {
    NUMERIC_SELECTORS.forEach(selector => {
      const block = blockOf(MONITOR_THEME_CSS, selector);

      expect(block).toContain('font-variant-numeric: tabular-nums');
      expect(block).not.toContain(MONO_DECLARATION);
    });
  });

  it('never uses the monospace family for prose', () => {
    PROSE_SELECTORS.forEach(selector => {
      expect(blockOf(MONITOR_THEME_CSS, selector)).not.toContain(MONO_DECLARATION);
    });
  });
});

describe('solar monitor visual hierarchy', () => {
  it('renders the hero kpi large enough to lead the dashboard', () => {
    expect(fontSizeOf('.monitor-kpi-value {')).toBeGreaterThanOrEqual(36);
  });

  it('keeps the kpi hierarchy descending from the value to the label', () => {
    const value = fontSizeOf('.monitor-kpi-value {');
    const unit = fontSizeOf('.monitor-kpi-unit {');
    const label = fontSizeOf('.monitor-kpi-label {');

    expect(value).toBeGreaterThan(unit);
    expect(unit).toBeGreaterThan(label);
  });

  it('gives the hero kpi a strong weight', () => {
    expect(fontWeightOf('.monitor-kpi-value {')).toBeGreaterThanOrEqual(600);
  });

  it('never wraps the hero value', () => {
    expect(blockOf(MONITOR_THEME_CSS, '.monitor-kpi-value {')).toContain('white-space: nowrap');
  });

  it('scales the metric readouts of the detail screen with the hero', () => {
    expect(fontSizeOf('.monitor-metric-item .metric-value {')).toBeGreaterThanOrEqual(20);
  });
});

describe('monitor chart typography', () => {
  it('never renders the charts with the monospace family', () => {
    [TREND_BARS, SOC_RING, AREA_CHART].forEach(source => {
      expect(source).not.toContain('IBM Plex Mono');
    });
  });

  it('gives the trend axis its own row so the labels never sit on the bars', () => {
    expect(TREND_BARS).not.toContain("position: 'absolute'");
  });

  it('renders the trend axis labels large enough to be read', () => {
    expect(TREND_BARS).toMatch(/fontSize:\s*1[0-9]/);
  });

  it('renders the soc ring value large enough to be read', () => {
    expect(SOC_RING).toMatch(/fontSize="1[0-9]"/);
  });
});
