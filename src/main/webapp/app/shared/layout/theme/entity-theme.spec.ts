// eslint-disable-next-line spaced-comment
/// <reference types="node" />
import fs from 'fs';
import path from 'path';

const WEBAPP_ROOT = path.join(__dirname, '../../../..');
const ENTITIES_DIR = path.join(WEBAPP_ROOT, 'app', 'entities');

/** Every `.tsx` file of the application, relative to the webapp root. */
const collectTsxFiles = (directory: string): string[] => {
  const collected: string[] = [];

  fs.readdirSync(directory, { withFileTypes: true }).forEach(entry => {
    const fullPath = path.join(directory, entry.name);

    if (entry.isDirectory()) {
      collected.push(...collectTsxFiles(fullPath));
    } else if (entry.name.endsWith('.tsx')) {
      collected.push(fullPath);
    }
  });

  return collected;
};

const ENTITY_FILES = collectTsxFiles(ENTITIES_DIR).map(filePath => ({
  name: path.relative(WEBAPP_ROOT, filePath),
  source: fs.readFileSync(filePath, 'utf8'),
}));

/** Matches a hardcoded color inside a style prop such as `bgcolor` or `color`. */
const HARDCODED_STYLE_COLOR = /(bgcolor|backgroundColor|background|borderColor|color)\s*:\s*['"]#[0-9a-fA-F]{3,8}['"]/g;

describe('entity tables follow the application theme', () => {
  it('finds the entity list modules', () => {
    expect(ENTITY_FILES.length).toBeGreaterThan(0);
  });

  it('never hardcodes a color in a style prop', () => {
    const offenders: string[] = [];

    ENTITY_FILES.forEach(({ name, source }) => {
      const matches = source.match(HARDCODED_STYLE_COLOR);

      if (matches) {
        offenders.push(`${name}: ${[...new Set(matches)].join(', ')}`);
      }
    });

    expect(offenders).toEqual([]);
  });

  it('paints the table header with the theme token instead of a fixed color', () => {
    const withTableHead = ENTITY_FILES.filter(({ source }) => source.includes('<TableHead>'));

    expect(withTableHead.length).toBeGreaterThan(0);

    withTableHead.forEach(({ name, source }) => {
      expect(`${name}: ${source.includes("bgcolor: 'var(--bg2)'")}`).toBe(`${name}: true`);
    });
  });
});
