import React, { useState, useMemo } from 'react';
import MetricSection from '../components/MetricSection';
import AreaChart from '../components/AreaChart';
import StatusBadge from '../components/StatusBadge';
import CellGrid from '../components/CellGrid';
import type { IInversor } from 'app/shared/model/inversor.model';
import type { IDatoInversor } from 'app/shared/model/dato-inversor.model';
import type { IEventoAlerta } from 'app/shared/model/evento-alerta.model';

interface InversorDetailScreenProps {
  inversor: IInversor;
  dato: IDatoInversor | null;
  alertas: IEventoAlerta[];
  onBack: () => void;
}

type TabKey = 'energy' | 'voltages' | 'temperature';

const InversorDetailScreen: React.FC<InversorDetailScreenProps> = ({ inversor, dato, alertas, onBack }) => {
  const [activeTab, setActiveTab] = useState<TabKey>('energy');

  const isAlarm = inversor.estadoDispositivo === 'Error';
  const invAlertas = alertas.filter(a => a.inversor?.id === inversor.id && !a.resuelto);

  // Quick config state
  const [configSliders, setConfigSliders] = useState([
    { label: 'Voltaje corte bajo', register: '0x211F', min: 40, max: 48, defaultValue: 42, unit: 'V', value: 42 },
    { label: 'Voltaje absorción', register: '0x2122', min: 54, max: 58, defaultValue: 57, unit: 'V', value: 57 },
    { label: 'Voltaje flotación', register: '0x2123', min: 52, max: 56, defaultValue: 54, unit: 'V', value: 54 },
    { label: 'Corriente máx carga', register: '0x212E', min: 10, max: 100, defaultValue: 60, unit: 'A', value: 60 },
  ]);

  const [configToggles, setConfigToggles] = useState([
    { label: 'Prioridad PV', register: '0x212A', defaultValue: true, value: true },
    { label: 'Carga inteligente', register: undefined, defaultValue: true, value: true },
    { label: 'Modo ECO', register: undefined, defaultValue: false, value: false },
    { label: 'Batería LiFePO4', register: '0x212D', defaultValue: true, value: true },
  ]);

  // Fake chart data for demo (8 data points)
  const chartData = useMemo(() => {
    const pts = 8;
    const now = Date.now();
    return Array.from({ length: pts }, (_, i) => ({
      x: i,
      y1: Math.random() * 3000 + 1000,
      y2: Math.random() * 2000 + 500,
    }));
  }, []);

  const batteryItems = [
    {
      label: 'VOLTAJE',
      value: dato?.bateriaVoltajeV?.toFixed(1) || '—',
      unit: 'V',
      color: dato?.bateriaVoltajeV != null && dato.bateriaVoltajeV < 47.5 ? 'var(--rd)' : 'var(--tx)',
    },
    {
      label: 'CORRIENTE',
      value: dato?.bateriaCorrienteA?.toFixed(1) || '—',
      unit: 'A',
      color: dato?.bateriaCorrienteA != null ? (dato.bateriaCorrienteA >= 0 ? 'var(--gr)' : 'var(--tx)') : 'var(--tx)',
    },
    {
      label: 'SOC',
      value: dato?.cargaPorcentaje?.toFixed(0) || '—',
      unit: dato?.bateriaCargando ? '(cargando)' : '%',
      color:
        dato?.cargaPorcentaje != null
          ? dato.cargaPorcentaje > 60
            ? 'var(--gr)'
            : dato.cargaPorcentaje > 30
              ? 'var(--am)'
              : 'var(--rd)'
          : 'var(--tx)',
    },
  ];

  const pvItems = [
    { label: 'POTENCIA', value: dato?.pvPotenciaW?.toFixed(0) || '—', unit: 'W', color: 'var(--bl)' },
    { label: 'VOLTAJE', value: dato?.pvVoltajeV?.toFixed(1) || '—', unit: 'V', color: 'var(--tx)' },
    { label: 'CORRIENTE', value: ((dato?.pvPotenciaW || 0) / (dato?.pvVoltajeV || 1)).toFixed(1), unit: 'A', color: 'var(--tx)' },
  ];

  const acItems = [
    { label: 'POTENCIA', value: ((dato?.salidaPotenciaW || 0) / 1000).toFixed(1), unit: 'kW', color: 'var(--tx)' },
    { label: 'VOLTAJE', value: dato?.salidaVoltajeV?.toFixed(0) || '—', unit: 'V', color: 'var(--tx)' },
    { label: 'FRECUENCIA', value: dato?.redFrecuenciaHz?.toFixed(1) || '—', unit: 'Hz', color: 'var(--tx)' },
  ];

  const getTabData = () => {
    switch (activeTab) {
      case 'energy':
        return {
          data1: chartData.map(d => ({ x: d.x, y: d.y1 })),
          data2: chartData.map(d => ({ x: d.x, y: d.y2 })),
          color1: '#2563eb',
          color2: '#22c55e',
          legend1: 'Generación PV',
          legend2: 'Consumo AC',
        };
      case 'voltages':
        return {
          data1: chartData.map(d => ({ x: d.x, y: 40 + Math.random() * 15 })),
          data2: chartData.map(d => ({ x: d.x, y: 210 + Math.random() * 20 })),
          color1: '#f59e0b',
          color2: '#2563eb',
          legend1: 'Voltaje Batería (V)',
          legend2: 'Voltaje AC (V)',
        };
      case 'temperature':
        return {
          data1: chartData.map(d => ({ x: d.x, y: 30 + Math.random() * 40 })),
          data2: chartData.map(d => ({ x: d.x, y: 25 + Math.random() * 20 })),
          color1: '#ef4444',
          color2: '#f59e0b',
          legend1: 'Temp MOSFET (°C)',
          legend2: 'Temp Interna (°C)',
        };
      default:
        return {
          data1: chartData.map(d => ({ x: d.x, y: d.y1 })),
          data2: chartData.map(d => ({ x: d.x, y: d.y2 })),
          color1: '#2563eb',
          color2: '#22c55e',
          legend1: 'Generación PV',
          legend2: 'Consumo AC',
        };
    }
  };

  const tabData = getTabData();

  // BMS fake data (16 cells)
  const bmsVoltages = Array.from({ length: 16 }, () => 3280 + Math.floor(Math.random() * 60));
  const bmsMetrics1 = [
    { label: 'TEMP MOSFET', value: '41', unit: '°C', color: 'var(--tx)' },
    { label: 'DELTA CELDAS', value: '12', unit: 'mV', color: 'var(--tx)' },
    { label: 'CICLOS USO', value: '345', unit: '', color: 'var(--tx)' },
  ];
  const bmsMetrics2 = [
    { label: 'PACK VOLTAJE', value: '52.8', unit: 'V', color: 'var(--tx)' },
    { label: 'SOC BMS', value: '78', unit: '%', color: 'var(--gr)' },
    { label: 'CHARGE MOS', value: 'ON', unit: '', color: 'var(--gr)' },
  ];

  return (
    <div className="monitor-scroll">
      {/* Header */}
      <div className={`monitor-detail-header ${isAlarm ? 'alarm' : ''}`}>
        {isAlarm && (
          <div className="alarm-tag">
            <span>●</span>
            {invAlertas[0]?.nombreAlerta || 'ALARM'}
          </div>
        )}
        <div className="inv-title">{inversor.nombre}</div>
        <div className="inv-subtitle">
          SLAVE_ID: {String(inversor.slaveId || 1).padStart(2, '0')} · {inversor.gateway?.direccionIp}:{inversor.gateway?.puerto}
          <br />
          {inversor.sitio?.nombre || 'Sin sitio'} · {inversor.esMaster ? '★ MASTER' : 'esclavo'} · {inversor.marca?.nombre}{' '}
          {inversor.modelo?.nombre} · {inversor.modelo?.capacidadVa ? `${inversor.modelo.capacidadVa / 1000}kVA` : ''}/
          {inversor.modelo?.voltajeSistemaNom || ''}V
        </div>
        <div className="inv-actions">
          <button className="monitor-btn" onClick={onBack}>
            ← VOLVER
          </button>
          <button className="monitor-btn">EDITAR</button>
          {isAlarm && <button className="monitor-btn danger">SILENCIAR</button>}
        </div>
      </div>

      {/* Battery */}
      <MetricSection title="Battery Storage" items={batteryItems} />

      {/* PV Array */}
      <MetricSection title="PV Array Input" items={pvItems} />

      {/* AC Load */}
      <MetricSection title="AC Load Output" items={acItems} />

      {/* Tabs */}
      <div className="monitor-tabs">
        {(['energy', 'voltages', 'temperature'] as TabKey[]).map(tab => (
          <button key={tab} className={`monitor-tab ${activeTab === tab ? 'active' : ''}`} onClick={() => setActiveTab(tab)}>
            {tab.charAt(0).toUpperCase() + tab.slice(1)}
          </button>
        ))}
      </div>

      <AreaChart
        data1={tabData.data1}
        data2={tabData.data2}
        color1={tabData.color1}
        color2={tabData.color2}
        legendLine1={tabData.legend1}
        legendLine2={tabData.legend2}
      />

      {/* Quick Config */}
      <div className="monitor-section-label">Quick Config — Registros Modbus 0x211F–0x2159</div>
      <div className="monitor-metric-section">
        {configSliders.map((slider, idx) => (
          <div key={idx} className="monitor-slider-row">
            <label>{slider.label}</label>
            <input
              type="range"
              min={slider.min}
              max={slider.max}
              value={slider.value}
              onChange={e => {
                const newVal = Number(e.target.value);
                setConfigSliders(prev => prev.map((s, i) => (i === idx ? { ...s, value: newVal } : s)));
              }}
            />
            <span className="slider-value">
              {slider.value} {slider.unit}
            </span>
          </div>
        ))}

        <div style={{ borderTop: '1px solid var(--bdr)', margin: '8px 0' }} />

        {configToggles.map((toggle, idx) => (
          <div key={idx} className="monitor-toggle-row">
            <label>{toggle.label}</label>
            <div
              className={`monitor-toggle-switch ${toggle.value ? 'on' : ''}`}
              onClick={() => setConfigToggles(prev => prev.map((t, i) => (i === idx ? { ...t, value: !t.value } : t)))}
            >
              <div className="thumb" />
            </div>
          </div>
        ))}
      </div>

      {/* BMS Summary */}
      <div className="monitor-section-label">BMS Summary</div>
      <MetricSection title="BMS Metrics" items={[...bmsMetrics1, ...bmsMetrics2]} />
      <CellGrid voltagesMv={bmsVoltages} />
    </div>
  );
};

export default InversorDetailScreen;
