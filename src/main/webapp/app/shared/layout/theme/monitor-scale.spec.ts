// eslint-disable-next-line spaced-comment
/// <reference types="node" />
import { MONITOR_THEME_CSS } from '../../../modules/monitor/theme';

describe('monitor corner radii', () => {
  it('takes every radius from the shared scale or from the circle shape', () => {
    const radii = MONITOR_THEME_CSS.match(/border-radius:\s*[^;]+;/g) ?? [];
    const offenders = radii.filter(radius => !/var\(--radius-|50%/.test(radius));

    expect(offenders).toEqual([]);
  });

  it('uses the shared radius scale for the container surfaces', () => {
    expect(MONITOR_THEME_CSS).toContain('border-radius: var(--radius-sm);');
    expect(MONITOR_THEME_CSS).toContain('border-radius: var(--radius-md);');
  });

  it('expresses the round shapes as circles instead of magic pixels', () => {
    expect(MONITOR_THEME_CSS).toContain('border-radius: 50%;');
  });
});
