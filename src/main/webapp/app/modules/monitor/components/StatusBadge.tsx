import React from 'react';

type BadgeVariant = 'master' | 'normal' | 'alarm' | 'offline';

interface StatusBadgeProps {
  variant: BadgeVariant;
}

const BADGE_STYLES: Record<BadgeVariant, React.CSSProperties> = {
  master: { background: '#2563eb18', color: 'var(--bl)' },
  normal: { background: '#22c55e18', color: 'var(--gr)' },
  alarm: { background: '#ef444418', color: 'var(--rd)' },
  offline: { background: 'var(--bg3)', color: 'var(--tx3)' },
};

const BADGE_LABELS: Record<BadgeVariant, string> = {
  master: '★ MASTER',
  normal: '● NORMAL',
  alarm: '⚠ ALARM',
  offline: '◌ OFFLINE',
};

const StatusBadge: React.FC<StatusBadgeProps> = ({ variant }) => (
  <span
    style={{
      ...BADGE_STYLES[variant],
      fontSize: 9,
      fontWeight: 600,
      letterSpacing: '0.5px',
      padding: '2px 6px',
      borderRadius: 3,
      display: 'inline-flex',
      alignItems: 'center',
      gap: 3,
    }}
  >
    {BADGE_LABELS[variant]}
  </span>
);

export default StatusBadge;
