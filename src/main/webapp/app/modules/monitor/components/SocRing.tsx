import React from 'react';

interface SocRingProps {
  soc: number; // 0-100
  alarm?: boolean;
  size?: number;
}

const SocRing: React.FC<SocRingProps> = ({ soc, alarm = false, size = 38 }) => {
  const r = (size - 6) / 2;
  const circumference = 2 * Math.PI * r;
  const pct = Math.max(0, Math.min(100, soc));
  const strokeDashoffset = circumference * (1 - pct / 100);

  const color = alarm ? 'var(--rd)' : pct > 60 ? 'var(--gr)' : pct > 30 ? 'var(--am)' : 'var(--rd)';

  return (
    <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
      {/* background track */}
      <circle cx={size / 2} cy={size / 2} r={r} fill="none" stroke="var(--bdr)" strokeWidth="2.5" />
      {/* progress arc */}
      <circle
        cx={size / 2}
        cy={size / 2}
        r={r}
        fill="none"
        stroke={color}
        strokeWidth="2.5"
        strokeLinecap="round"
        strokeDasharray={circumference}
        strokeDashoffset={strokeDashoffset}
        transform={`rotate(-90 ${size / 2} ${size / 2})`}
      />
      {/* SOC text */}
      <text
        x={size / 2}
        y={size / 2}
        textAnchor="middle"
        dominantBaseline="central"
        fontFamily="'IBM Plex Mono', monospace"
        fontSize="9"
        fontWeight="500"
        fill={color}
      >
        {Math.round(pct)}%
      </text>
    </svg>
  );
};

export default SocRing;
