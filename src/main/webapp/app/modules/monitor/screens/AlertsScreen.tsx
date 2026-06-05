import React, { useState } from 'react';
import type { IEventoAlerta } from 'app/shared/model/evento-alerta.model';
import type { AlertFilters } from '../types';

interface AlertsScreenProps {
  alertas: IEventoAlerta[];
}

const FELICITY_CODES: Record<number, string> = {
  1: 'Over temperature',
  2: 'Over voltage battery',
  3: 'Under voltage battery',
  4: 'Short circuit',
  5: 'High temperature MOSFET',
  6: 'Fan failure',
  0: 'Sin fallo',
};

const AlertsScreen: React.FC<AlertsScreenProps> = ({ alertas }) => {
  const [filters, setFilters] = useState<AlertFilters>({ soloActivas: true });

  const filtered = alertas.filter(a => (filters.soloActivas ? !a.resuelto : true));

  const getSeverity = (a: IEventoAlerta): 'critica' | 'advertencia' | 'ok' => {
    const nivel = a.nivelSeveridad?.toLowerCase() || '';
    if (nivel.includes('crit') || nivel.includes('alta')) return 'critica';
    if (nivel.includes('adv') || nivel.includes('med') || nivel.includes('warn')) return 'advertencia';
    return 'ok';
  };

  const dotColor = (severity: string) => {
    switch (severity) {
      case 'critica':
        return 'var(--rd)';
      case 'advertencia':
        return 'var(--am)';
      default:
        return 'var(--gr)';
    }
  };

  const badgeStyle = (severity: string): React.CSSProperties => {
    switch (severity) {
      case 'critica':
        return { background: '#ef444420', color: 'var(--rd)' };
      case 'advertencia':
        return { background: '#f59e0b20', color: 'var(--am)' };
      default:
        return { background: '#22c55e20', color: 'var(--gr)' };
    }
  };

  return (
    <div className="monitor-scroll">
      {/* Filter bar */}
      <div style={{ display: 'flex', alignItems: 'center', gap: 8, padding: '8px 0' }}>
        <span style={{ fontSize: 10, fontWeight: 600, color: 'var(--tx2)', textTransform: 'uppercase', letterSpacing: '0.5px' }}>
          Eventos Críticos
        </span>
        <button className={`monitor-chip ${filters.soloActivas ? 'active' : ''}`} onClick={() => setFilters({ soloActivas: true })}>
          Activas ✓
        </button>
        <button className={`monitor-chip ${!filters.soloActivas ? 'active' : ''}`} onClick={() => setFilters({ soloActivas: false })}>
          Resueltas
        </button>
      </div>

      {/* Alert items */}
      {filtered.length === 0 ? (
        <div style={{ textAlign: 'center', color: 'var(--tx3)', padding: 32, fontSize: 12 }}>
          No hay alertas {filters.soloActivas ? 'activas' : 'resueltas'}
        </div>
      ) : (
        filtered.map(alerta => {
          const severity = getSeverity(alerta);
          return (
            <div key={alerta.id} className={`monitor-alert-item ${severity}`}>
              <span className="alert-severity-dot" style={{ background: dotColor(severity) }} />
              <span className="alert-main">
                {alerta.nombreAlerta} — {alerta.inversor?.nombre || 'Inversor'}
              </span>
              <span className="alert-badge" style={badgeStyle(severity)}>
                {severity.toUpperCase()}
              </span>
              <div className="alert-detail">{alerta.mensaje}</div>
              <div className="alert-meta">
                {alerta.timestamp
                  ? (typeof alerta.timestamp === 'string' || alerta.timestamp instanceof Date
                      ? new Date(alerta.timestamp)
                      : (alerta.timestamp as any)?.toDate?.() || alerta.timestamp
                    ).toLocaleTimeString()
                  : '—'}{' '}
                · {alerta.inversor?.nombre || '—'}
              </div>
            </div>
          );
        })
      )}
    </div>
  );
};

export default AlertsScreen;
