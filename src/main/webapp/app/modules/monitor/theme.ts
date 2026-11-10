/* ─── Solar Monitor layout styles — colors come from the canonical theme tokens ─── */
export const MONITOR_THEME_CSS = `
/* ─── Animations ─── */
@keyframes monitor-pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.25; }
}

@keyframes monitor-pulse-fast {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.25; }
}

@keyframes monitor-pulse-alarm {
  0%, 100% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.08); opacity: 0.7; }
}

/* ─── Base container ─── */
.solar-monitor-root {
  background: var(--bg);
  color: var(--tx);
  font-family: 'IBM Plex Sans', -apple-system, BlinkMacSystemFont, sans-serif;
  min-height: calc(100vh - 64px);
  position: relative;
  overflow-x: hidden;
}

.solar-monitor-root * {
  box-sizing: border-box;
}

.solar-monitor-root .mono {
  font-family: 'IBM Plex Mono', 'SF Mono', 'Cascadia Code', monospace;
}

/* ─── Scroll container ─── */
.monitor-scroll {
  padding: 0 10px;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}

/* ─── Top App Bar ─── */
.monitor-topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 44px;
  padding: 0 10px;
  background: var(--bg2);
  border-bottom: 1px solid var(--bdr);
  position: sticky;
  top: 0;
  z-index: 100;
}

.monitor-topbar-title {
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 0.9px;
  text-transform: uppercase;
  color: var(--tx);
}

.monitor-topbar-live {
  display: flex;
  align-items: center;
  gap: 4px;
  font-family: 'IBM Plex Mono', monospace;
  font-size: 9px;
  font-weight: 500;
  color: var(--gr);
  margin-left: 8px;
}

.monitor-topbar-live .dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--gr);
  animation: monitor-pulse 1.4s infinite;
}

.monitor-topbar-live.reconnecting .dot {
  background: var(--am);
  animation: monitor-pulse-fast 0.6s infinite;
}

.monitor-topbar-live.disconnected .dot {
  background: var(--rd);
  animation: none;
}

.monitor-topbar-live.disconnected {
  color: var(--rd);
}

.monitor-topbar-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.monitor-theme-toggle {
  width: 28px;
  height: 28px;
  border-radius: 4px;
  border: 1px solid var(--bdr);
  background: var(--bg3);
  color: var(--tx);
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: border-color 150ms, background 150ms;
}

.monitor-theme-toggle:hover {
  border-color: var(--bdr2);
  background: var(--bdr2);
}

.monitor-bell-badge {
  position: relative;
  width: 28px;
  height: 28px;
  border-radius: 4px;
  border: 1px solid var(--bdr);
  background: var(--bg3);
  color: var(--tx2);
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.monitor-bell-badge .badge-count {
  position: absolute;
  top: -4px;
  right: -4px;
  min-width: 14px;
  height: 14px;
  border-radius: 7px;
  background: var(--rd);
  color: #fff;
  font-size: 8px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 3px;
}

.monitor-avatar {
  width: 27px;
  height: 27px;
  border-radius: 50%;
  background: var(--bl);
  color: #fff;
  font-size: 10px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

/* ─── Bottom Navigation ─── */
.monitor-bottomnav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 62px;
  background: var(--bg2);
  border-top: 1px solid var(--bdr);
  display: flex;
  align-items: center;
  justify-content: space-around;
  z-index: 100;
}

.monitor-nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  cursor: pointer;
  color: var(--tx3);
  font-size: 9px;
  font-weight: 500;
  letter-spacing: 0.5px;
  text-transform: uppercase;
  padding: 4px 8px;
  border: none;
  background: none;
  transition: color 150ms;
  position: relative;
}

.monitor-nav-item.active {
  color: var(--bl);
}

.monitor-nav-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  width: 6px;
  height: 3px;
  border-radius: 1.5px;
  background: var(--bl);
  filter: drop-shadow(0 0 5px #2563eb88);
}

.monitor-nav-item svg {
  width: 20px;
  height: 20px;
  fill: currentColor;
}

.monitor-nav-item .nav-badge {
  position: absolute;
  top: 0;
  right: 2px;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--rd);
}

/* ─── KPI Cards ─── */
.monitor-kpi-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
  padding: 8px 0;
}

.monitor-kpi-card {
  background: var(--card);
  border: 1px solid var(--bdr);
  border-radius: 4px;
  padding: 11px;
  text-align: center;
}

.monitor-kpi-value {
  font-family: 'IBM Plex Mono', monospace;
  font-size: 21px;
  font-weight: 500;
  line-height: 1.1;
}

.monitor-kpi-unit {
  font-family: 'IBM Plex Mono', monospace;
  font-size: 10px;
  color: var(--tx2);
  margin-top: 2px;
}

.monitor-kpi-label {
  font-size: 9px;
  font-weight: 600;
  letter-spacing: 0.9px;
  text-transform: uppercase;
  margin-top: 4px;
}

/* ─── Trend card ─── */
.monitor-trend-card {
  background: var(--card);
  border: 1px solid var(--bdr);
  border-radius: 4px;
  padding: 11px;
  margin-bottom: 8px;
}

.monitor-trend-card .monitor-section-label {
  padding: 0 0 6px;
}

/* ─── Group Header ─── */
.monitor-group-header {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 0 4px;
  font-size: 10px;
  font-weight: 600;
  letter-spacing: 0.5px;
  text-transform: uppercase;
  color: var(--tx2);
}

.monitor-group-header .gw-status {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--gr);
}

.monitor-group-header .gw-ip {
  font-family: 'IBM Plex Mono', monospace;
  font-size: 9px;
  color: var(--bl);
}

.monitor-group-header .alarm-badge {
  margin-left: auto;
  background: var(--rd);
  color: #fff;
  font-size: 8px;
  font-weight: 700;
  padding: 1px 5px;
  border-radius: 3px;
}

/* ─── Inversor Card ─── */
.monitor-inversor-card {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 11px;
  background: var(--card);
  border: 1px solid var(--bdr);
  border-radius: 4px;
  margin-bottom: 6px;
  cursor: pointer;
  transition: border-color 150ms, background 150ms;
}

.monitor-inversor-card:hover {
  border-color: var(--bdr2);
  background: var(--bg3);
}

.monitor-inversor-card.alarm {
  border-color: var(--rd);
  box-shadow: 0 0 0 1px #ef444420;
}

.monitor-inversor-card .inv-info {
  flex: 1;
  min-width: 0;
}

.monitor-inversor-card .inv-name {
  font-size: 12px;
  font-weight: 600;
  color: var(--tx);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.monitor-inversor-card .inv-model {
  font-size: 9px;
  color: var(--tx2);
  margin-top: 1px;
}

.monitor-inversor-card .inv-metrics {
  text-align: right;
}

.monitor-inversor-card .inv-power {
  font-family: 'IBM Plex Mono', monospace;
  font-size: 12px;
  font-weight: 500;
  color: var(--tx);
}

.monitor-inversor-card .inv-temp {
  font-family: 'IBM Plex Mono', monospace;
  font-size: 9px;
  color: var(--tx2);
  margin-top: 1px;
}

/* ─── Group totalizer ─── */
.monitor-group-total {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
  padding: 4px 0 10px;
  font-size: 9px;
  font-weight: 600;
  letter-spacing: 0.5px;
  text-transform: uppercase;
  color: var(--tx2);
  border-bottom: 1px solid var(--bdr);
  margin-bottom: 6px;
}

.monitor-group-total .total-value {
  font-family: 'IBM Plex Mono', monospace;
  color: var(--tx);
}

/* ─── Detail Header ─── */
.monitor-detail-header {
  padding: 11px;
  border-radius: 4px;
  border: 1px solid var(--bdr);
  background: var(--card);
  margin-bottom: 8px;
}

.monitor-detail-header.alarm {
  border-color: var(--rd);
  background: #ef444406;
}

.monitor-detail-header .alarm-tag {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 10px;
  font-weight: 600;
  color: var(--rd);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  animation: monitor-pulse-alarm 1.0s infinite;
}

.monitor-detail-header .inv-title {
  font-size: 15px;
  font-weight: 600;
  margin: 6px 0 4px;
}

.monitor-detail-header .inv-subtitle {
  font-family: 'IBM Plex Mono', monospace;
  font-size: 9px;
  color: var(--tx2);
  line-height: 1.6;
}

.monitor-detail-header .inv-actions {
  display: flex;
  gap: 6px;
  margin-top: 8px;
}

/* ─── Buttons ─── */
.monitor-btn {
  border: 1px solid var(--bdr);
  border-radius: 4px;
  background: var(--bg3);
  color: var(--tx);
  font-size: 10px;
  font-weight: 600;
  letter-spacing: 0.5px;
  text-transform: uppercase;
  padding: 6px 12px;
  cursor: pointer;
  transition: background 150ms, border-color 150ms;
}

.monitor-btn:hover {
  border-color: var(--bdr2);
  background: var(--bdr2);
}

.monitor-btn.primary {
  background: var(--bl);
  border-color: var(--bl);
  color: #fff;
}

.monitor-btn.primary:hover {
  background: var(--bl2);
  border-color: var(--bl2);
}

.monitor-btn.danger {
  border-color: var(--rd);
  color: var(--rd);
}

/* ─── Metric Section ─── */
.monitor-metric-section {
  padding: 11px;
  background: var(--card);
  border: 1px solid var(--bdr);
  border-radius: 4px;
  margin-bottom: 8px;
}

.monitor-metric-section .section-title {
  font-size: 9px;
  font-weight: 600;
  letter-spacing: 0.9px;
  text-transform: uppercase;
  color: var(--tx3);
  padding-bottom: 6px;
  margin-bottom: 8px;
  border-bottom: 1px solid var(--bdr);
}

.monitor-metric-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 6px;
}

.monitor-metric-item {
  text-align: center;
}

.monitor-metric-item .metric-value {
  font-family: 'IBM Plex Mono', monospace;
  font-size: 18px;
  font-weight: 500;
  line-height: 1.2;
}

.monitor-metric-item .metric-unit {
  font-family: 'IBM Plex Mono', monospace;
  font-size: 9px;
  color: var(--tx2);
}

.monitor-metric-item .metric-label {
  font-size: 8px;
  font-weight: 600;
  letter-spacing: 0.9px;
  text-transform: uppercase;
  color: var(--tx3);
  margin-top: 2px;
}

/* ─── Tab bar ─── */
.monitor-tabs {
  display: flex;
  gap: 0;
  border-bottom: 1px solid var(--bdr);
  margin-bottom: 8px;
}

.monitor-tab {
  flex: 1;
  text-align: center;
  padding: 8px;
  font-size: 10px;
  font-weight: 600;
  letter-spacing: 0.5px;
  text-transform: uppercase;
  color: var(--tx3);
  cursor: pointer;
  border: none;
  border-bottom: 2px solid transparent;
  background: none;
  transition: color 150ms, border-color 150ms;
}

.monitor-tab.active {
  color: var(--bl);
  border-bottom-color: var(--bl);
}

/* ─── Chips (filters) ─── */
.monitor-chip {
  display: inline-flex;
  align-items: center;
  padding: 4px 10px;
  border: 1px solid var(--bdr);
  border-radius: 4px;
  background: var(--bg3);
  color: var(--tx2);
  font-size: 10px;
  font-weight: 500;
  cursor: pointer;
  transition: border-color 150ms, color 150ms, background 150ms;
  margin: 0 4px 4px 0;
}

.monitor-chip.active {
  border-color: var(--bl);
  color: var(--bl);
  background: #2563eb15;
}

/* ─── Slider ─── */
.monitor-slider-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 6px 0;
}

.monitor-slider-row label {
  font-size: 10px;
  color: var(--tx2);
  flex: 1;
}

.monitor-slider-row .slider-value {
  font-family: 'IBM Plex Mono', monospace;
  font-size: 10px;
  color: var(--tx);
  min-width: 45px;
  text-align: right;
}

.monitor-slider-row input[type='range'] {
  flex: 2;
  height: 3px;
  accent-color: var(--bl);
  margin: 0 8px;
}

/* ─── Toggle switch ─── */
.monitor-toggle-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 6px 0;
}

.monitor-toggle-row label {
  font-size: 10px;
  color: var(--tx2);
}

.monitor-toggle-switch {
  position: relative;
  width: 36px;
  height: 20px;
  background: var(--bg3);
  border: 1px solid var(--bdr);
  border-radius: 10px;
  cursor: pointer;
  transition: background 180ms, border-color 180ms;
}

.monitor-toggle-switch.on {
  background: var(--bl);
  border-color: var(--bl);
}

.monitor-toggle-switch .thumb {
  position: absolute;
  top: 2px;
  left: 2px;
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background: #fff;
  transition: transform 180ms;
}

.monitor-toggle-switch.on .thumb {
  transform: translateX(16px);
}

/* ─── Table (log, bus) ─── */
.monitor-table {
  width: 100%;
  border-collapse: collapse;
  font-family: 'IBM Plex Mono', monospace;
  font-size: 10px;
}

.monitor-table th {
  font-size: 8px;
  font-weight: 600;
  letter-spacing: 0.9px;
  text-transform: uppercase;
  color: var(--tx3);
  text-align: left;
  padding: 4px 6px;
  border-bottom: 1px solid var(--bdr);
}

.monitor-table td {
  padding: 4px 6px;
  color: var(--tx);
  border-bottom: 1px solid var(--bdr);
}

.monitor-table td.alarm-value {
  color: var(--rd);
  font-weight: 700;
}

/* ─── Alert item ─── */
.monitor-alert-item {
  padding: 10px 11px;
  border: 1px solid var(--bdr);
  border-radius: 4px;
  margin-bottom: 6px;
  background: var(--card);
}

.monitor-alert-item.critica {
  border-color: var(--rd);
}

.monitor-alert-item.advertencia {
  border-color: var(--am);
}

.monitor-alert-item .alert-severity-dot {
  display: inline-block;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  margin-right: 6px;
}

.monitor-alert-item .alert-main {
  font-size: 11px;
  font-weight: 500;
  color: var(--tx);
  display: inline;
}

.monitor-alert-item .alert-badge {
  display: inline;
  font-size: 8px;
  font-weight: 700;
  padding: 1px 5px;
  border-radius: 3px;
  margin-left: 6px;
}

.monitor-alert-item .alert-detail {
  font-size: 9px;
  color: var(--tx2);
  margin-top: 3px;
}

.monitor-alert-item .alert-meta {
  font-family: 'IBM Plex Mono', monospace;
  font-size: 9px;
  color: var(--tx3);
  margin-top: 2px;
}

/* ─── FAB ─── */
.monitor-fab {
  position: fixed;
  bottom: 74px;
  right: 14px;
  width: 46px;
  height: 46px;
  border-radius: 50%;
  background: var(--bl);
  box-shadow: 0 4px 16px #2563eb60;
  font-size: 24px;
  color: #fff;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 150ms;
  z-index: 90;
}

.monitor-fab:hover {
  transform: scale(1.08);
}

/* ─── Modal ─── */
.monitor-modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.6);
  z-index: 200;
  display: flex;
  align-items: center;
  justify-content: center;
}

.monitor-modal {
  background: var(--bg2);
  border: 1px solid var(--bdr);
  border-radius: 4px;
  width: 90%;
  max-width: 420px;
  padding: 16px;
}

.monitor-modal h3 {
  font-size: 13px;
  font-weight: 600;
  margin-bottom: 4px;
}

.monitor-modal p {
  font-size: 10px;
  color: var(--tx2);
  margin-bottom: 12px;
}

.monitor-modal label {
  display: block;
  font-size: 9px;
  font-weight: 600;
  letter-spacing: 0.9px;
  text-transform: uppercase;
  color: var(--tx3);
  margin-bottom: 4px;
  margin-top: 10px;
}

.monitor-modal input,
.monitor-modal select {
  width: 100%;
  padding: 7px 10px;
  border: 1px solid var(--bdr);
  border-radius: 4px;
  background: var(--bg3);
  color: var(--tx);
  font-size: 12px;
  font-family: inherit;
}

.monitor-modal input:focus,
.monitor-modal select:focus {
  outline: none;
  border-color: var(--bl);
}

.monitor-modal .modal-actions {
  display: flex;
  gap: 8px;
  margin-top: 14px;
}

/* ─── BMS cell grid ─── */
.bms-cell-grid {
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  gap: 3px;
}

.bms-cell {
  height: 24px;
  border-radius: 2px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'IBM Plex Mono', monospace;
  font-size: 7px;
  font-weight: 500;
  cursor: default;
  transition: transform 100ms;
}

.bms-cell:hover {
  transform: scale(1.05);
}

.bms-cell.ok {
  background: #22c55e12;
  color: var(--gr);
  border: 1px solid #22c55e28;
}

.bms-cell.warn {
  background: #f59e0b12;
  color: var(--am);
  border: 1px solid #f59e0b28;
}

.bms-cell.crit {
  background: #ef444412;
  color: var(--rd);
  border: 1px solid #ef444440;
}

/* ─── Legend ─── */
.monitor-legend {
  display: flex;
  gap: 10px;
  padding: 6px 0;
  font-size: 8px;
  color: var(--tx2);
}

.monitor-legend .legend-item {
  display: flex;
  align-items: center;
  gap: 3px;
}

.monitor-legend .legend-swatch {
  width: 8px;
  height: 8px;
  border-radius: 2px;
}

/* ─── Export button ─── */
.monitor-export-btn {
  width: 100%;
  padding: 8px;
  border: 1px solid var(--bl);
  border-radius: 4px;
  background: var(--bl);
  color: #fff;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.9px;
  text-transform: uppercase;
  cursor: pointer;
  margin-top: 8px;
  transition: background 150ms;
}

.monitor-export-btn:hover {
  background: var(--bl2);
}

/* ─── Gateway card ─── */
.monitor-gw-card {
  padding: 11px;
  background: var(--card);
  border: 1px solid var(--bdr);
  border-radius: 4px;
  margin-bottom: 6px;
}

.monitor-gw-card .gw-name {
  font-size: 12px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 6px;
}

.monitor-gw-card .gw-ip {
  font-family: 'IBM Plex Mono', monospace;
  font-size: 9px;
  color: var(--bl);
  margin-top: 2px;
}

.monitor-gw-card .gw-metrics {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 6px;
  margin-top: 8px;
}

.monitor-gw-card .gw-metric-value {
  font-family: 'IBM Plex Mono', monospace;
  font-size: 14px;
  font-weight: 500;
}

.monitor-gw-card .gw-metric-label {
  font-size: 8px;
  font-weight: 600;
  letter-spacing: 0.9px;
  text-transform: uppercase;
  color: var(--tx3);
  margin-top: 1px;
}

/* ─── Section separator ─── */
.monitor-section-label {
  font-size: 9px;
  font-weight: 600;
  letter-spacing: 0.9px;
  text-transform: uppercase;
  color: var(--tx3);
  padding: 10px 0 4px;
}

/* ─── Date input ─── */
.monitor-date-input {
  padding: 6px 10px;
  border: 1px solid var(--bdr);
  border-radius: 4px;
  background: var(--bg3);
  color: var(--tx);
  font-family: 'IBM Plex Mono', monospace;
  font-size: 11px;
  width: 100%;
}

.monitor-date-input:focus {
  outline: none;
  border-color: var(--bl);
}
`;
