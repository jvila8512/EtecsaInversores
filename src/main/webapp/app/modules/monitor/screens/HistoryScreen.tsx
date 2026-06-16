import React, { useState, useEffect, useMemo, useCallback } from 'react';
import axios from 'axios';
import AreaChart from '../components/AreaChart';
import dayjs from 'dayjs';
import type { IInversor } from 'app/shared/model/inversor.model';
import type { IGatewayTcp } from 'app/shared/model/gateway-tcp.model';
import type { IDatoInversor } from 'app/shared/model/dato-inversor.model';

interface HistoryScreenProps {
  inversores: IInversor[];
  gateways: IGatewayTcp[];
}

const API_URL = 'api';

const HistoryScreen: React.FC<HistoryScreenProps> = ({ inversores, gateways }) => {
  const [selectedGrupo, setSelectedGrupo] = useState<number | null>(null);
  const [selectedVariables, setSelectedVariables] = useState<string[]>(['voltaje', 'potencia']);
  const [fechaDesde, setFechaDesde] = useState(dayjs().subtract(7, 'day').format('YYYY-MM-DD'));
  const [fechaHasta, setFechaHasta] = useState(dayjs().format('YYYY-MM-DD'));
  const [logData, setLogData] = useState<IDatoInversor[]>([]);
  const [loading, setLoading] = useState(false);

  const variableOptions = ['Voltaje', 'Potencia', 'Temp', 'SOC', 'Frecuencia'];

  const toggleVariable = (v: string) => {
    setSelectedVariables(prev => (prev.includes(v.toLowerCase()) ? prev.filter(x => x !== v.toLowerCase()) : [...prev, v.toLowerCase()]));
  };

  // Determine which inversor IDs to filter
  const filteredInversorIds = useMemo(() => {
    if (selectedGrupo === null) return null; // all
    const gw = gateways.find(g => g.id === selectedGrupo);
    if (!gw) return null;
    return inversores
      .filter(i => i.gateway?.id === gw.id && i.activo)
      .map(i => i.id)
      .filter((id): id is number => id !== undefined);
  }, [selectedGrupo, gateways, inversores]);

  // Fetch data from API
  const fetchHistory = useCallback(async () => {
    setLoading(true);
    try {
      const params: Record<string, string> = {
        sort: 'tiempo,desc',
        size: '500',
        'tiempo.greaterThanOrEqual': `${fechaDesde}T00:00:00Z`,
        'tiempo.lessThan': `${fechaHasta}T23:59:59Z`,
      };
      // If a specific gateway group is selected, add inversorId.in filter
      if (filteredInversorIds && filteredInversorIds.length > 0) {
        params['inversorId.in'] = filteredInversorIds.join(',');
      }
      const res = await axios.get<IDatoInversor[]>(`${API_URL}/dato-inversors`, { params });
      setLogData(res.data || []);
    } catch (e) {
      console.error('Error fetching history:', e);
      setLogData([]);
    } finally {
      setLoading(false);
    }
  }, [fechaDesde, fechaHasta, filteredInversorIds]);

  // Fetch on mount and when filters change
  useEffect(() => {
    fetchHistory();
  }, [fetchHistory]);

  // Chart data from real readings
  const chartPts = useMemo(() => {
    if (logData.length === 0) return [];
    // Aggregate by hour: group readings by hour and average values
    const hourly: Record<number, { pvSum: number; socSum: number; count: number }> = {};
    logData.forEach(d => {
      if (!d.tiempo) return;
      const h = dayjs(d.tiempo).hour();
      if (!hourly[h]) hourly[h] = { pvSum: 0, socSum: 0, count: 0 };
      hourly[h].pvSum += d.pvPotenciaW || 0;
      hourly[h].socSum += d.cargaPorcentaje || 0;
      hourly[h].count += 1;
    });
    return Array.from({ length: 24 }, (_, i) => {
      const h = hourly[i];
      return {
        x: i,
        y1: h ? h.pvSum / (h.count * 1000) : 0, // avg kW
        y2: h ? h.socSum / h.count : 0, // avg SOC %
      };
    });
  }, [logData]);

  const formatTime = (d: IDatoInversor) => {
    if (!d.tiempo) return '—';
    return dayjs(d.tiempo).format('DD/MM HH:mm:ss');
  };

  const handleExportCsv = () => {
    const params = new URLSearchParams();
    if (fechaDesde) params.set('from', fechaDesde);
    if (fechaHasta) params.set('to', fechaHasta);
    if (filteredInversorIds && filteredInversorIds.length === 1) {
      params.set('inversorId', String(filteredInversorIds[0]));
    }
    window.open(`${API_URL}/dato-inversors/export-csv?${params.toString()}`, '_blank');
  };

  return (
    <div className="monitor-scroll">
      {/* Group filter */}
      <div style={{ display: 'flex', flexWrap: 'wrap', padding: '6px 0' }}>
        <button className={`monitor-chip ${selectedGrupo === null ? 'active' : ''}`} onClick={() => setSelectedGrupo(null)}>
          Todos
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
            {v}
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
        color1="var(--bl)"
        color2="var(--gr)"
        legendLine1="PV kW"
        legendLine2="SOC %"
      />

      {/* Log table */}
      <div className="monitor-section-label">Log de Datos ({logData.length} registros)</div>
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
          {loading ? (
            <tr>
              <td colSpan={6} style={{ textAlign: 'center', color: 'var(--tx3)', padding: 16 }}>
                Cargando...
              </td>
            </tr>
          ) : logData.length === 0 ? (
            <tr>
              <td colSpan={6} style={{ textAlign: 'center', color: 'var(--tx3)', padding: 16 }}>
                Sin datos en el rango seleccionado
              </td>
            </tr>
          ) : (
            logData.map((d, i) => {
              const batV = d.bateriaVoltajeV;
              const temp = d.temperaturaC || d.bateriaVoltajeV; // use temp field if available
              const soc = d.cargaPorcentaje;
              return (
                <tr key={`${d.id}-${i}`}>
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
      <button className="monitor-export-btn" onClick={handleExportCsv}>
        ⬇ EXPORT CSV
      </button>
    </div>
  );
};

export default HistoryScreen;
