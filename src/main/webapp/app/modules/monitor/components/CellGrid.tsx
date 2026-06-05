import React from 'react';

interface CellGridProps {
  voltagesMv: number[]; // 16 cell voltages in millivolts
}

const CellGrid: React.FC<CellGridProps> = ({ voltagesMv }) => {
  const avg = voltagesMv.length > 0 ? voltagesMv.reduce((s, v) => s + v, 0) / voltagesMv.length : 0;

  const getCellStatus = (voltageMv: number): 'ok' | 'warn' | 'crit' => {
    const delta = Math.abs(voltageMv - avg);
    if (delta > 30) return 'crit';
    if (delta > 15) return 'warn';
    return 'ok';
  };

  return (
    <div>
      <div className="bms-cell-grid">
        {voltagesMv.map((v, i) => {
          const status = getCellStatus(v);
          return (
            <div key={i} className={`bms-cell ${status}`} title={`C${i + 1}: ${(v / 1000).toFixed(3)}V`}>
              {(v / 1000).toFixed(2)}
            </div>
          );
        })}
      </div>
      <div className="monitor-legend" style={{ marginTop: 4 }}>
        <div className="legend-item">
          <div className="legend-swatch" style={{ background: '#22c55e28' }} />
          <span>Normal (&lt;15mV)</span>
        </div>
        <div className="legend-item">
          <div className="legend-swatch" style={{ background: '#f59e0b28' }} />
          <span>Advertencia (15-30mV)</span>
        </div>
        <div className="legend-item">
          <div className="legend-swatch" style={{ background: '#ef444440' }} />
          <span>Crítico (&gt;30mV)</span>
        </div>
      </div>
    </div>
  );
};

export default CellGrid;
