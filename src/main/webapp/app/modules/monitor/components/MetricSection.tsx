import React from 'react';

interface MetricItem {
  label: string;
  value: string | number;
  unit: string;
  color?: string;
}

interface MetricSectionProps {
  title: string;
  items: MetricItem[];
}

const MetricSection: React.FC<MetricSectionProps> = ({ title, items }) => (
  <div className="monitor-metric-section">
    <div className="section-title">{title}</div>
    <div className="monitor-metric-grid">
      {items.map((item, idx) => (
        <div key={idx} className="monitor-metric-item">
          <div className="metric-value" style={{ color: item.color || 'var(--tx)' }}>
            {item.value}
          </div>
          <div className="metric-unit">{item.unit}</div>
          <div className="metric-label">{item.label}</div>
        </div>
      ))}
    </div>
  </div>
);

export default MetricSection;
