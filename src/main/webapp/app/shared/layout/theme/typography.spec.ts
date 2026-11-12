// eslint-disable-next-line spaced-comment
/// <reference types="node" />
import fs from 'fs';
import path from 'path';

import { MONITOR_THEME_CSS } from '../../../modules/monitor/theme';

const WEBAPP_ROOT = path.join(__dirname, '../../../..');
const FONTS_CSS_PATH = path.join(WEBAPP_ROOT, 'content', 'css', 'fonts.css');
const FONTS_DIR = path.join(WEBAPP_ROOT, 'content', 'fonts');
const INDEX_HTML_PATH = path.join(WEBAPP_ROOT, 'index.html');
const APP_SCSS_PATH = path.join(WEBAPP_ROOT, 'app', 'app.scss');

const readIfExists = (filePath: string): string => (fs.existsSync(filePath) ? fs.readFileSync(filePath, 'utf8') : '');

const FONTS_CSS = readIfExists(FONTS_CSS_PATH);
const INDEX_HTML = readIfExists(INDEX_HTML_PATH);
const APP_SCSS = readIfExists(APP_SCSS_PATH);

interface FontFace {
  family: string;
  weight: string;
  url: string;
}

/** Parses every `@font-face` block into a flat, comparable shape. */
const parseFontFaces = (css: string): FontFace[] => {
  const faces: FontFace[] = [];
  const rulePattern = /@font-face\s*\{([^}]*)\}/g;
  let match = rulePattern.exec(css);

  while (match !== null) {
    const body = match[1];
    const family = /font-family:\s*['"]?([^;'"\n]+)/.exec(body)?.[1]?.trim();
    const weight = /font-weight:\s*(\d+)/.exec(body)?.[1];
    const url = /src:\s*url\(([^)]+)\)/.exec(body)?.[1]?.replace(/['"]/g, '').trim();

    if (family && weight && url) {
      faces.push({ family, weight, url });
    }

    match = rulePattern.exec(css);
  }

  return faces;
};

const FONT_FACES = parseFontFaces(FONTS_CSS);

const EXPECTED_FAMILIES: { family: string; weights: string[] }[] = [
  { family: 'Inter', weights: ['400', '500', '600', '700'] },
  { family: 'IBM Plex Sans', weights: ['400', '500', '600', '700'] },
  { family: 'IBM Plex Mono', weights: ['400', '500', '600', '700'] },
];

describe('self hosted typography', () => {
  it('ships a font face for every family and weight used by the application', () => {
    EXPECTED_FAMILIES.forEach(({ family, weights }) => {
      weights.forEach(weight => {
        const face = FONT_FACES.find(candidate => candidate.family === family && candidate.weight === weight);

        expect(face).toBeDefined();
      });
    });
  });

  it('points every font face to the self hosted woff2 asset', () => {
    FONT_FACES.forEach(face => {
      expect(face.url).toMatch(/^\/content\/fonts\/[a-z0-9-]+\.woff2$/);
    });
  });

  it('references only font files that actually exist', () => {
    FONT_FACES.forEach(face => {
      const fileName = path.basename(face.url);

      expect(fs.existsSync(path.join(FONTS_DIR, fileName))).toBe(true);
    });
  });

  it('renders the fonts without blocking the first paint', () => {
    const swapCount = (FONTS_CSS.match(/font-display:\s*swap/g) ?? []).length;

    expect(FONT_FACES).toHaveLength(12);
    expect(swapCount).toBe(FONT_FACES.length);
  });

  it('loads the font stylesheet in parallel with the application bundle', () => {
    expect(INDEX_HTML).toContain('content/css/fonts.css');
  });

  it('uses the self hosted families on the application body and the monitor', () => {
    expect(APP_SCSS).toMatch(/font-family:[^;]*'Inter'/);
    expect(MONITOR_THEME_CSS).toMatch(/font-family:[^;]*'IBM Plex Sans'/);
    expect(MONITOR_THEME_CSS).toMatch(/font-family:[^;]*'IBM Plex Mono'/);
  });
});
