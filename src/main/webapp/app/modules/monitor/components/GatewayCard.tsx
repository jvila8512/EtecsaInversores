import React from 'react';
import type { IGatewayTcp } from 'app/shared/model/gateway-tcp.model';

interface GatewayCardProps {
  gateway: IGatewayTcp;
  numInversores?: number;
  latenciaMs?: number;
  loadPct?: number;
}

const GatewayCard: React.FC<GatewayCardProps> = ({ gateway, numInversores = 0, latenciaMs, loadPct }) => {
  const connected = gateway.estadoConexion === 'Conectado';
  const latenciaColor = latenciaMs != null && latenciaMs > 30 ? 'var(--am)' : 'var(--tx)';
  const cargaColor = loadPct != null ? (loadPct > 95 ? 'var(--rd)' : loadPct > 80 ? 'var(--am)' : 'var(--gr)') : 'var(--tx)';

  return (
    <div className="monitor-gw-card">
      <div className="gw-name">
        <span
          style={{
            width: 6,
            height: 6,
            borderRadius: '50%',
            background: connected ? 'var(--gr)' : 'var(--rd)',
            display: 'inline-block',
          }}
        />
        {gateway.nombre}
      </div>
      <div className="gw-ip">
        {gateway.direccionIp}:{gateway.puerto}
      </div>
      <div className="gw-metrics">
        <div>
          <div className="gw-metric-value" style={{ color: 'var(--bl)' }}>
            {numInversores}
          </div>
          <div className="gw-metric-label">Inversores</div>
        </div>
        <div>
          <div className="gw-metric-value" style={{ color: latenciaColor }}>
            {latenciaMs != null ? `${latenciaMs}ms` : '—'}
          </div>
          <div className="gw-metric-label">Latencia</div>
        </div>
        <div>
          <div className="gw-metric-value" style={{ color: cargaColor }}>
            {loadPct != null ? `${loadPct}%` : '—'}
          </div>
          <div className="gw-metric-label">Carga</div>
        </div>
      </div>
    </div>
  );
};

export default GatewayCard;
