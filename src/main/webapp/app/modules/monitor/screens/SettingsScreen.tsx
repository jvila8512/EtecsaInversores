import React, { useState } from 'react';
import axios from 'axios';
import GatewayCard from '../components/GatewayCard';
import type { IInversor } from 'app/shared/model/inversor.model';
import type { IGatewayTcp } from 'app/shared/model/gateway-tcp.model';
import type { LiveDataMap } from '../types';

interface SettingsScreenProps {
  inversores: IInversor[];
  gateways: IGatewayTcp[];
  liveData: LiveDataMap;
  onRefresh: () => void;
}

const SettingsScreen: React.FC<SettingsScreenProps> = ({ inversores, gateways, liveData, onRefresh }) => {
  const [showModal, setShowModal] = useState(false);
  const [modalForm, setModalForm] = useState({
    nombre: '',
    tipoConexion: 'TcpModbus',
    puerto: '192.168.1.10:502',
    slaveId: 1,
    masterReferenciaId: null as number | null,
  });
  const [registering, setRegistering] = useState(false);

  // Infrastructure KPIs
  const totalUnits = inversores.filter(i => i.activo).length;
  const avgLatency =
    Object.values(liveData).length > 0
      ? Object.values(liveData).reduce((sum, d) => sum + (d.latenciaMs || 0), 0) / Object.values(liveData).length
      : 0;
  const systemLoad = totalUnits > 0 ? Math.min(100, (totalUnits / 20) * 100) : 0;
  const loadColor = systemLoad > 95 ? 'var(--rd)' : systemLoad > 80 ? 'var(--am)' : 'var(--gr)';
  const latencyColor = avgLatency > 30 ? 'var(--am)' : avgLatency > 0 ? 'var(--gr)' : 'var(--tx)';

  const handleRegister = async () => {
    setRegistering(true);
    try {
      const [ip, port] = modalForm.puerto.split(':');
      await axios.post(`/api/inversors/${modalForm.masterReferenciaId}/esclavos`, {
        nombre: modalForm.nombre,
        slaveId: modalForm.slaveId,
      });
      setShowModal(false);
      onRefresh();
    } catch (e) {
      console.error('Error registering inverter:', e);
    } finally {
      setRegistering(false);
    }
  };

  return (
    <div className="monitor-scroll">
      {/* Infrastructure KPIs */}
      <div className="monitor-kpi-grid">
        <div className="monitor-kpi-card">
          <div className="monitor-kpi-value" style={{ color: 'var(--bl)' }}>
            {totalUnits}
          </div>
          <div className="monitor-kpi-unit">inversores</div>
          <div className="monitor-kpi-label" style={{ color: 'var(--bl)' }}>
            TOTAL UNITS
          </div>
        </div>
        <div className="monitor-kpi-card">
          <div className="monitor-kpi-value" style={{ color: loadColor }}>
            {systemLoad.toFixed(0)}%
          </div>
          <div className="monitor-kpi-unit">capacidad</div>
          <div className="monitor-kpi-label" style={{ color: loadColor }}>
            SYSTEM LOAD
          </div>
        </div>
        <div className="monitor-kpi-card">
          <div className="monitor-kpi-value" style={{ color: latencyColor }}>
            {avgLatency.toFixed(0)}ms
          </div>
          <div className="monitor-kpi-unit">promedio</div>
          <div className="monitor-kpi-label" style={{ color: latencyColor }}>
            GW LATENCY
          </div>
        </div>
      </div>

      {/* Gateways */}
      <div className="monitor-section-label">Gateways TCP/IP activos</div>
      {gateways.map(gw => {
        const gwInversores = inversores.filter(i => i.gateway?.id === gw.id);
        const gwLatency =
          Object.values(liveData)
            .filter(d => d.inversor?.gateway?.id === gw.id)
            .reduce((sum, d, _, arr) => sum + (d.latenciaMs || 0), 0) / (gwInversores.length || 1);

        return (
          <GatewayCard
            key={gw.id}
            gateway={gw}
            numInversores={gwInversores.length}
            latenciaMs={gwLatency || undefined}
            loadPct={gwInversores.length > 0 ? Math.min(100, (gwInversores.length / 10) * 100) : undefined}
          />
        );
      })}

      {/* Device Bus Table */}
      <div className="monitor-section-label">Bus de Dispositivos</div>
      <table className="monitor-table">
        <thead>
          <tr>
            <th>NOMBRE</th>
            <th>ROL</th>
            <th>TIPO</th>
            <th>IP / SID</th>
          </tr>
        </thead>
        <tbody>
          {inversores
            .filter(i => i.activo)
            .map(inv => (
              <tr key={inv.id}>
                <td>
                  {inv.nombre} {inv.esMaster ? '★' : ''}
                </td>
                <td>
                  {inv.esMaster ? (
                    <span style={{ background: '#2563eb18', color: 'var(--bl)', fontSize: 8, padding: '1px 5px', borderRadius: 3 }}>
                      Master
                    </span>
                  ) : (
                    <span style={{ color: 'var(--tx2)', fontSize: 9 }}>esclavo</span>
                  )}
                </td>
                <td style={{ fontSize: 9 }}>{inv.tipoConexion === 'TcpModbus' ? 'TCP/IP' : inv.tipoConexion}</td>
                <td>
                  <div style={{ fontSize: 9 }}>{inv.gateway?.direccionIp}</div>
                  <div style={{ fontSize: 9, color: 'var(--tx3)' }}>SID:{inv.slaveId || 1}</div>
                </td>
              </tr>
            ))}
        </tbody>
      </table>

      {/* FAB */}
      <button className="monitor-fab" onClick={() => setShowModal(true)}>
        +
      </button>

      {/* Modal */}
      {showModal && (
        <div className="monitor-modal-overlay" onClick={() => setShowModal(false)}>
          <div className="monitor-modal" onClick={e => e.stopPropagation()}>
            <h3>Registrar Nuevo Inversor</h3>
            <p>El sistema detecta marca, modelo y serie automáticamente</p>

            <label>Nombre</label>
            <input
              value={modalForm.nombre}
              onChange={e => setModalForm(prev => ({ ...prev, nombre: e.target.value }))}
              placeholder="Felicity-A6"
            />

            <label>Tipo de conexión</label>
            <select value={modalForm.tipoConexion} onChange={e => setModalForm(prev => ({ ...prev, tipoConexion: e.target.value }))}>
              <option value="TcpModbus">TCP/IP Modbus</option>
              <option value="SerialRs485">Serial RS485</option>
            </select>

            <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: 8 }}>
              <div>
                <label>IP / Puerto</label>
                <input
                  value={modalForm.puerto}
                  onChange={e => setModalForm(prev => ({ ...prev, puerto: e.target.value }))}
                  placeholder="192.168.1.10:502"
                />
              </div>
              <div>
                <label>Slave ID</label>
                <input
                  type="number"
                  min={1}
                  max={247}
                  value={modalForm.slaveId}
                  onChange={e => setModalForm(prev => ({ ...prev, slaveId: Number(e.target.value) }))}
                />
              </div>
            </div>

            <label>Master de referencia (opcional)</label>
            <select
              value={modalForm.masterReferenciaId || ''}
              onChange={e => setModalForm(prev => ({ ...prev, masterReferenciaId: e.target.value ? Number(e.target.value) : null }))}
            >
              <option value="">— Inversor independiente —</option>
              {inversores
                .filter(i => i.esMaster && i.activo)
                .map(i => (
                  <option key={i.id} value={i.id}>
                    {i.nombre}
                  </option>
                ))}
            </select>

            <div className="modal-actions">
              <button className="monitor-btn primary" onClick={handleRegister} disabled={registering}>
                {registering ? 'Registrando...' : '🔍 DESCUBRIR Y REGISTRAR'}
              </button>
              <button className="monitor-btn" onClick={() => setShowModal(false)}>
                CANCELAR
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default SettingsScreen;
