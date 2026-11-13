import { RADIUS, TRANSITION, toScaleCssVariables } from './scale';

describe('shared design scale', () => {
  it('defines the radius scale used by every design system', () => {
    expect(RADIUS).toEqual({ sm: 4, md: 8, lg: 12 });
  });

  it('defines the transition durations', () => {
    expect(TRANSITION).toEqual({ fast: '150ms', normal: '200ms' });
  });

  it('exposes the radius as css custom properties', () => {
    const variables = toScaleCssVariables();

    expect(variables['--radius-sm']).toBe('4px');
    expect(variables['--radius-md']).toBe('8px');
    expect(variables['--radius-lg']).toBe('12px');
  });

  it('binds the bootstrap radius scale to the shared one', () => {
    const variables = toScaleCssVariables();

    expect(variables['--bs-border-radius']).toBe('8px');
    expect(variables['--bs-border-radius-sm']).toBe('4px');
    expect(variables['--bs-border-radius-lg']).toBe('12px');
  });
});
