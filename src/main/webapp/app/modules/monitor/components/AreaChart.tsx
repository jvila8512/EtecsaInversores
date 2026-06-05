import React from 'react';
import type { AnomalyPoint } from '../types';

interface AreaChartProps {
  data1: { x: number; y: number }[];
  data2?: { x: number; y: number }[];
  color1?: string;
  color2?: string;
  height?: number;
  anomaly?: AnomalyPoint;
  legendLine1?: string;
  legendLine2?: string;
}

const AreaChart: React.FC<AreaChartProps> = ({
  data1,
  data2,
  color1 = '#2563eb',
  color2 = '#22c55e',
  height = 62,
  anomaly,
  legendLine1 = 'Línea 1',
  legendLine2 = 'Línea 2',
}) => {
  const width = 320;
  const padX = 4;
  const padY = 4;
  const chartW = width - padX * 2;
  const chartH = height - padY * 2;

  const allY = [...data1.map(d => d.y), ...(data2?.map(d => d.y) || [])];
  const minY = Math.min(...allY, 0);
  const maxY = Math.max(...allY, 1);
  const rangeY = maxY - minY || 1;

  const scaleX = (i: number) => padX + (i / Math.max(data1.length - 1, 1)) * chartW;
  const scaleY = (v: number) => padY + chartH - ((v - minY) / rangeY) * chartH;

  const line1Path = data1.map((d, i) => `${i === 0 ? 'M' : 'L'}${scaleX(i).toFixed(1)},${scaleY(d.y).toFixed(1)}`).join(' ');

  const area1Path =
    line1Path + ` L${scaleX(data1.length - 1).toFixed(1)},${scaleY(minY).toFixed(1)} L${scaleX(0).toFixed(1)},${scaleY(minY).toFixed(1)} Z`;

  let line2Path = '';
  let area2Path = '';
  if (data2 && data2.length > 0) {
    line2Path = data2.map((d, i) => `${i === 0 ? 'M' : 'L'}${scaleX(i).toFixed(1)},${scaleY(d.y).toFixed(1)}`).join(' ');
    area2Path =
      line2Path +
      ` L${scaleX(data2.length - 1).toFixed(1)},${scaleY(minY).toFixed(1)} L${scaleX(0).toFixed(1)},${scaleY(minY).toFixed(1)} Z`;
  }

  return (
    <div>
      <svg width="100%" viewBox={`0 0 ${width} ${height}`} preserveAspectRatio="none" style={{ display: 'block' }}>
        <defs>
          <linearGradient id="grad1" x1="0" y1="0" x2="0" y2="1">
            <stop offset="0%" stopColor={color1} stopOpacity="0.3" />
            <stop offset="100%" stopColor={color1} stopOpacity="0.02" />
          </linearGradient>
          <linearGradient id="grad2" x1="0" y1="0" x2="0" y2="1">
            <stop offset="0%" stopColor={color2} stopOpacity="0.3" />
            <stop offset="100%" stopColor={color2} stopOpacity="0.02" />
          </linearGradient>
        </defs>

        {/* Area fills */}
        <path d={area1Path} fill="url(#grad1)" />
        {area2Path && <path d={area2Path} fill="url(#grad2)" />}

        {/* Line 1 — solid */}
        <path d={line1Path} fill="none" stroke={color1} strokeWidth="1.8" />

        {/* Line 2 — dashed */}
        {line2Path && <path d={line2Path} fill="none" stroke={color2} strokeWidth="1.5" strokeDasharray="3 2" />}

        {/* Anomaly marker */}
        {anomaly && data1[anomaly.index] && (
          <g>
            <circle cx={scaleX(anomaly.index)} cy={scaleY(data1[anomaly.index].y)} r="4.5" fill="var(--rd)" />
            <circle
              cx={scaleX(anomaly.index)}
              cy={scaleY(data1[anomaly.index].y)}
              r="9"
              fill="none"
              stroke="var(--rd)"
              strokeWidth="1"
              opacity="0.4"
            />
            <text
              x={scaleX(anomaly.index)}
              y={scaleY(data1[anomaly.index].y) - 12}
              textAnchor="middle"
              fontFamily="'IBM Plex Mono', monospace"
              fontSize="7"
              fill="var(--rd)"
            >
              {anomaly.label}
            </text>
          </g>
        )}
      </svg>

      {/* Legend */}
      <div className="monitor-legend">
        <div className="legend-item">
          <div className="legend-swatch" style={{ background: color1 }} />
          <span>{legendLine1}</span>
        </div>
        {data2 && data2.length > 0 && (
          <div className="legend-item">
            <div className="legend-swatch" style={{ background: color2, opacity: 0.6 }} />
            <span>{legendLine2}</span>
          </div>
        )}
        {anomaly && (
          <div className="legend-item">
            <div className="legend-swatch" style={{ background: 'var(--rd)', borderRadius: '50%' }} />
            <span>Anomalía</span>
          </div>
        )}
      </div>
    </div>
  );
};

export default AreaChart;
