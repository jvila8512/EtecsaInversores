// eslint-disable-next-line spaced-comment
/// <reference types="node" />
import { MONITOR_THEME_CSS } from '../../../modules/monitor/theme';

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

  it('keeps the monospace family for the technical fields only', () => {
    TECHNICAL_SELECTORS.forEach(selector => {
      expect(blockOf(MONITOR_THEME_CSS, selector)).toContain(MONO_DECLARATION);
    });
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
