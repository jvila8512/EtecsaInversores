import React from 'react';

interface TrendBarsProps {
  values: number[]; // 24 hourly values
  alarmIndex?: number;
}

const TrendBars: React.FC<TrendBarsProps> = ({ values, alarmIndex }) => {
  const maxVal = Math.max(...values.filter(v => v > 0), 1);

  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'flex-end', height: 68, gap: 1, padding: '4px 0' }}>
        {values.map((val, i) => {
          const isNight = i < 6 || i >= 20;
          const isAlarm = i === alarmIndex;
          const height = val > 0 ? Math.max(2, (val / maxVal) * 60) : 2;
          const bg = isAlarm ? 'var(--rd)' : isNight ? 'var(--bg3)' : 'var(--bl)';

          return (
            <div
              key={i}
              title={`${String(i).padStart(2, '0')}:00 → ${(val / 1000).toFixed(1)} kW`}
              style={{
                flex: 1,
                height,
                background: bg,
                borderRadius: '1px 1px 0 0',
                minWidth: 2,
                transition: 'height 300ms',
              }}
            />
          );
        })}
      </div>
      {/* X-axis labels — own row so they never sit on top of the bars */}
      <div
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          fontSize: 10,
          fontFamily: "'IBM Plex Sans', sans-serif",
          fontVariantNumeric: 'tabular-nums',
          color: 'var(--tx3)',
          marginTop: 6,
        }}
      >
        <span>00:00</span>
        <span>06:00</span>
        <span>12:00</span>
        <span>18:00</span>
        <span>23:59</span>
      </div>
    </div>
  );
};

export default TrendBars;
