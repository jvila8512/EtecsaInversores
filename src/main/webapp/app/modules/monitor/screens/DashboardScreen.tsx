import React, { useMemo } from 'react';
import SocRing from '../components/SocRing';
import TrendBars from '../components/TrendBars';
import StatusBadge from '../components/StatusBadge';
import type { InversorGroup, DashboardKPIs, LiveDataMap } from '../types';
import type { IInversor } from 'app/shared/model/inversor.model';
import type { IDatoInversor } from 'app/shared/model/dato-inversor.model';

interface DashboardScreenProps {
  groups: InversorGroup[];
  kpis: DashboardKPIs;
  liveData: LiveDataMap;
  onInversorTap: (inversor: IInversor) => void;
  trendValues: number[];
  trendAlarmIndex?: number;
  onToggleTheme: () => void;
}

const DashboardScreen: React.FC<DashboardScreenProps> = ({
  groups,
  kpis,
  liveData,
  onInversorTap,
  trendValues,
  trendAlarmIndex,
  onToggleTheme,
}) => {
  const totalPotenciaKw = useMemo(() => {
    return Object.values(liveData).reduce((sum: number, d: IDatoInversor) => sum + (d.pvPotenciaW || 0), 0) / 1000;
  }, [liveData]);

  return (
    <div className="monitor-scroll">
      <div className="monitor-topbar">
        <div className="monitor-topbar-title">Monitor Solar</div>
        <div className="monitor-topbar-actions">
          <span className="monitor-topbar-live">
            <span className="dot" /> Vivo
          </span>
          <button className="monitor-theme-toggle" aria-label="Cambiar tema" onClick={onToggleTheme}>
            <span className="mono">&#9882;</span>
          </button>
        </div>
      </div>

      <div className="monitor-kpi-grid">
        <div className="monitor-kpi-card">
          <div className="monitor-kpi-value" style={{ color: 'var(--md-primary)' }}>
            {kpis.generacionKwh.toFixed(1)}
          </div>
          <div className="monitor-kpi-unit">kWh</div>
          <div className="monitor-kpi-label">Generacion</div>
        </div>
        <div className="monitor-kpi-card">
          <div className="monitor-kpi-value" style={{ color: 'var(--md-secondary)' }}>
            {kpis.consumoKwh.toFixed(1)}
          </div>
          <div className="monitor-kpi-unit">kWh</div>
          <div className="monitor-kpi-label">Consumo</div>
        </div>
        <div className="monitor-kpi-card">
          <div className="monitor-kpi-value" style={{ color: 'var(--md-tertiary)' }}>
            {(totalPotenciaKw || kpis.potenciaKw).toFixed(1)}
          </div>
          <div className="monitor-kpi-unit">kW</div>
          <div className="monitor-kpi-label">Potencia</div>
        </div>
      </div>

      <div className="monitor-trend-card">
        <div className="monitor-section-label">Tendencia 24h</div>
        <div style={{ position: 'relative', marginBottom: 20 }}>
          <TrendBars values={trendValues} alarmIndex={trendAlarmIndex} />
        </div>
      </div>

      {groups.map(group => {
        const allGroupInversores = [group.master, ...group.esclavos].filter((i): i is IInversor => i !== undefined);
        const groupTotalW = allGroupInversores.reduce(
          (sum, inv) => sum + (inv.id && liveData[inv.id]?.pvPotenciaW ? liveData[inv.id].pvPotenciaW : 0),
          0,
        );

        return (
          <div key={group.gateway.id}>
            <div className="monitor-group-header">
              <span
                className="gw-status"
                style={{ background: group.gateway.estadoConexion === 'Conectado' ? 'var(--gr)' : 'var(--rd)' }}
              />
              {group.gateway.nombre}
              <span className="gw-ip">
                {group.gateway.direccionIp}:{group.gateway.puerto}
              </span>
              {group.alertCount > 0 && <span className="alarm-badge">{group.alertCount} alarmas</span>}
            </div>

            {allGroupInversores.map(inv => {
              const dato = inv.id ? liveData[inv.id] : undefined;
              const isAlarm = inv.estadoDispositivo === 'Error';
              const soc = dato?.cargaPorcentaje || 0;
              const power = dato?.pvPotenciaW || 0;
              const eff = dato?.eficienciaPct;
              const codigoFallo = dato?.codigoFallo;

              return (
                <div key={inv.id} className={`monitor-inversor-card ${isAlarm ? 'alarm' : ''}`} onClick={() => onInversorTap(inv)}>
                  <SocRing soc={soc} alarm={isAlarm} />
                  <div className="inv-info">
                    <div className="inv-name">
                      {inv.nombre}
                      {inv.esMaster ? ' MASTER' : ''}
                      {!inv.esMaster && inv.slaveId ? ` SLAVE ${inv.slaveId}` : ''}
                    </div>
                    <div className="inv-model">
                      {inv.marca?.nombre} {inv.modelo?.nombre}
                      {codigoFallo && codigoFallo > 0 && (
                        <span style={{ color: 'var(--rd)', marginLeft: 6 }}>E{String(codigoFallo).padStart(2, '0')}</span>
                      )}
                    </div>
                    <div style={{ display: 'flex', gap: 4, marginTop: 2 }}>
                      {isAlarm ? <StatusBadge variant="alarm" /> : <StatusBadge variant="normal" />}
                      {eff != null && (
                        <span
                          style={{
                            fontSize: 9,
                            color: eff > 80 ? 'var(--gr)' : eff > 70 ? 'var(--am)' : 'var(--rd)',
                          }}
                        >
                          {eff}%
                        </span>
                      )}
                    </div>
                  </div>
                  <div className="inv-metrics">
                    <div className="inv-power" style={{ color: isAlarm ? 'var(--rd)' : 'var(--tx)' }}>
                      {(power / 1000).toFixed(2)} kW
                    </div>
                  </div>
                </div>
              );
            })}

            <div className="monitor-group-total">
              <span>{group.gateway.nombre} TOTAL</span>
              <span className="total-value">{(groupTotalW / 1000).toFixed(2)} kW</span>
            </div>
          </div>
        );
      })}

      <button className="monitor-fab" aria-label="Agregar inversor">
        +
      </button>
    </div>
  );
};

export default DashboardScreen;
