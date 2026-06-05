import React, { useState, useMemo } from 'react';
import AreaChart from '../components/AreaChart';
import dayjs from 'dayjs';
import type { IInversor } from 'app/shared/model/inversor.model';
import type { IGatewayTcp } from 'app/shared/model/gateway-tcp.model';
import type { IDatoInversor } from 'app/shared/model/dato-inversor.model';

interface HistoryScreenProps {
  inversores: IInversor[];
  gateways: IGatewayTcp[];
}

const HistoryScreen: React.FC<HistoryScreenProps> = ({ inversores, gateways }) => {
  const [selectedGrupo, setSelectedGrupo] = useState<number | null>(null);
  const [selectedVariables, setSelectedVariables] = useState<string[]>(['voltaje', 'potencia']);
  const [fechaDesde, setFechaDesde] = useState(dayjs().subtract(7, 'day').format('YYYY-MM-DD'));
  const [fechaHasta, setFechaHasta] = useState(dayjs().format('YYYY-MM-DD'));
  const [logData] = useState<IDatoInversor[]>([]); // placeholder — will fetch from API

  const variableOptions = ['Voltaje', 'Potencia', 'Temp', 'SOC', 'Frecuencia'];

  const toggleVariable = (v: string) => {
    setSelectedVariables(prev => (prev.includes(v.toLowerCase()) ? prev.filter(x => x !== v.toLowerCase()) : [...prev, v.toLowerCase()]));
  };

  // Fake chart data for demo
  const chartPts = useMemo(() => {
    return Array.from({ length: 24 }, (_, i) => ({
      x: i,
      y1: Math.random() * 3000 + 500,
      y2: Math.random() * 40 + 60,
    }));
  }, [selectedGrupo, fechaDesde, fechaHasta]);

  const formatTime = (d: IDatoInversor) => {
    if (!d.tiempo) return '—';
    return dayjs(d.tiempo).format('HH:mm:ss');
  };

  return (
    <div className="monitor-scroll">
      {/* Group filter */}
      <div style={{ display: 'flex', flexWrap: 'wrap', padding: '6px 0' }}>
        <button className={`monitor-chip ${selectedGrupo === null ? 'active' : ''}`} onClick={() => setSelectedGrupo(null)}>
          Todos ✓
        </button>
        {gateways.map(gw => (
          <button
            key={gw.id}
            className={`monitor-chip ${selectedGrupo === gw.id ? 'active' : ''}`}
            onClick={() => gw.id !== undefined && setSelectedGrupo(gw.id)}
          >
            {gw.nombre}
          </button>
        ))}
      </div>

      {/* Variable filter */}
      <div style={{ display: 'flex', flexWrap: 'wrap', padding: '4px 0' }}>
        {variableOptions.map(v => (
          <button
            key={v}
            className={`monitor-chip ${selectedVariables.includes(v.toLowerCase()) ? 'active' : ''}`}
            onClick={() => toggleVariable(v)}
          >
            {v} {selectedVariables.includes(v.toLowerCase()) ? '✓' : ''}
          </button>
        ))}
      </div>

      {/* Date selectors */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 8, padding: '6px 0' }}>
        <div>
          <div style={{ fontSize: 8, color: 'var(--tx3)', letterSpacing: '0.9px', textTransform: 'uppercase', marginBottom: 2 }}>
            Fecha desde
          </div>
          <input type="date" className="monitor-date-input" value={fechaDesde} onChange={e => setFechaDesde(e.target.value)} />
        </div>
        <div>
          <div style={{ fontSize: 8, color: 'var(--tx3)', letterSpacing: '0.9px', textTransform: 'uppercase', marginBottom: 2 }}>
            Fecha hasta
          </div>
          <input type="date" className="monitor-date-input" value={fechaHasta} onChange={e => setFechaHasta(e.target.value)} />
        </div>
      </div>

      {/* Chart */}
      <div className="monitor-section-label">Análisis Temporal</div>
      <AreaChart
        data1={chartPts.map(d => ({ x: d.x, y: d.y1 }))}
        data2={chartPts.map(d => ({ x: d.x, y: d.y2 }))}
        color1="#2563eb"
        color2="#22c55e"
        legendLine1="PV kW"
        legendLine2="SOC %"
      />

      {/* Log table */}
      <div className="monitor-section-label">Log de Datos</div>
      <table className="monitor-table">
        <thead>
          <tr>
            <th>Tiempo</th>
            <th>INV</th>
            <th>BAT V</th>
            <th>PV W</th>
            <th>TEMP</th>
            <th>SOC</th>
          </tr>
        </thead>
        <tbody>
          {logData.length === 0 ? (
            <tr>
              <td colSpan={6} style={{ textAlign: 'center', color: 'var(--tx3)', padding: 16 }}>
                Sin datos en el rango seleccionado
              </td>
            </tr>
          ) : (
            logData.map((d, i) => {
              const batV = d.bateriaVoltajeV;
              const temp = d.bateriaVoltajeV; // placeholder
              const soc = d.cargaPorcentaje;
              return (
                <tr key={i}>
                  <td>{formatTime(d)}</td>
                  <td>{d.inversor?.nombre || '—'}</td>
                  <td className={batV != null && batV < 47.5 ? 'alarm-value' : ''}>{batV?.toFixed(1) || '—'}</td>
                  <td>{d.pvPotenciaW?.toFixed(0) || '—'}</td>
                  <td className={temp != null && temp > 60 ? 'alarm-value' : ''}>{temp?.toFixed(0) || '—'}</td>
                  <td className={soc != null && soc < 70 ? 'alarm-value' : ''}>{soc != null ? `${soc}%` : '—'}</td>
                </tr>
              );
            })
          )}
        </tbody>
      </table>

      {/* Export */}
      <button className="monitor-export-btn">⬇ EXPORT CSV</button>
    </div>
  );
};

export default HistoryScreen;
