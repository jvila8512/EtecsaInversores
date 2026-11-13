/* ─── Shared design scale ───
 * Single source of truth for the geometry shared by the MUI components, the
 * Bootstrap components and the plain CSS of the application. Keeping one scale
 * is what makes a reactstrap button look like a MUI button.
 */

export const RADIUS = {
  sm: 4,
  md: 8,
  lg: 12,
} as const;

export const TRANSITION = {
  fast: '150ms',
  normal: '200ms',
} as const;

/**
 * Publishes the scale as CSS custom properties.
 * The `--radius-*` properties drive the plain CSS, while the `--bs-*` ones
 * bind the Bootstrap radius scale to the shared one.
 */
export const toScaleCssVariables = (): Record<string, string> => ({
  '--radius-sm': `${RADIUS.sm}px`,
  '--radius-md': `${RADIUS.md}px`,
  '--radius-lg': `${RADIUS.lg}px`,
  '--bs-border-radius': `${RADIUS.md}px`,
  '--bs-border-radius-sm': `${RADIUS.sm}px`,
  '--bs-border-radius-lg': `${RADIUS.lg}px`,
});

export default RADIUS;
