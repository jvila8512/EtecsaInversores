# Optimizaciones para Escalabilidad — 300 Inversores

> Documentación técnica de las optimizaciones implementadas para soportar
> 300+ inversores con generación de ~5.2M lecturas/día.

---

## 1. Endpoints Optimizados

### 1.1 `GET /api/dato-inversors/latest-readings`

**Problema que resuelve:**
El endpoint estándar `GET /api/dato-inversors` devuelve filas históricas paginadas.
Con 300 inversores, el dashboard necesita **la última lectura de cada inversor**, no
una página arbitraria de datos viejos. Usar `?size=200` ni siquiera alcanza para
cubrir los 300 inversores, y devuelve filas desordenadas.

**Solución:**
Nuevo endpoint dedicado que usa un `INNER JOIN` sobre `MAX(tiempo)` por `inversor_id`:

```sql
SELECT di.* FROM dato_inversor di
INNER JOIN (
  SELECT inversor_id, MAX(tiempo) AS max_tiempo
  FROM dato_inversor
  GROUP BY inversor_id
) latest ON di.inversor_id = latest.inversor_id AND di.tiempo = latest.max_tiempo
```

**Resultado:** Una sola query devuelve exactamente 300 filas (1 por inversor),
siempre la más reciente. Sin paginación, sin datos de más.

**Código afectado:**

- `DatoInversorRepository.java` → `findLatestReadings()` (native query)
- `DatoInversorService.java` → `findLatestReadings()` (delegador)
- `DatoInversorResource.java` → `GET /api/dato-inversors/latest-readings`

---

### 1.2 `GET /api/inversors/all`

**Problema que resuelve:**
El Monitor Solar necesita listar todos los inversores para dropdowns y tarjetas.
`GET /api/inversors?size=200` solo devuelve 200 de 300 inversores. Si mañana
hay 500, vuelve a fallar.

**Solución:**
Endpoint sin paginación que devuelve todos los inversores con relaciones eagerly
cargadas (marca, modelo, gateway, sitio, master).

```java
@GetMapping("/all")
public ResponseEntity<List<InversorDTO>> getAllInversorsWithoutPagination() {
  List<InversorDTO> inversors = inversorService.findAllWithEagerRelationships();
  return ResponseEntity.ok().body(inversors);
}

```

**Código afectado:**

- `InversorService.java` → `findAllWithEagerRelationships()` (sin Pageable)
- `InversorResource.java` → `GET /api/inversors/all`

---

### 1.3 Frontend — Hook `useInversores.ts`

**Cambio:**
| Antes | Después |
|-------|---------|
| `GET /api/inversors?size=200` | `GET /api/inversors/all` |
| `GET /api/dato-inversors?size=200` | `GET /api/dato-inversors/latest-readings` |

Se agregó `latestReadings: IDatoInversor[]` al return del hook y al `MonitorContext`
para que todas las pantallas del monitor puedan acceder a las últimas lecturas
sin re-fetch.

**Código afectado:**

- `useInversores.ts` → nuevos endpoints, nuevo campo `latestReadings`
- `SolarMonitorApp.tsx` → `MonitorContextValue` incluye `latestReadings`

---

## 2. Auto-Purge Programado (`@Scheduled`)

### 2.1 El Problema

| Inversores | Filas/día | 30 días | 90 días | 1 año |
| ---------- | --------- | ------- | ------- | ----- |
| **80**     | 1.38M     | 41M     | 124M    | 504M  |
| **300**    | 5.2M      | 156M    | 468M    | 1.9B  |

Sin purge, la tabla `dato_inversor` crece indefinidamente:

- El disco se llena → PostgreSQL muere
- Los índices B-tree se fragmentan → queries lentísimas
- Los `VACUUM` automáticos tardan cada vez más
- La app se vuelve inusable

### 2.2 La Solución

Un `@Scheduled` que corre **todos los días a las 3:00 AM** y ejecuta:

```sql
DELETE FROM dato_inversor WHERE tiempo < :cutoff
```

donde `cutoff = Instant.now().minus(dataRetentionDays, ChronoUnit.DAYS)`.

### 2.3 Configuración

La retención es **configurable por profile**:

| Profile | `app.data-retention-days` | Justificación                                            |
| ------- | ------------------------- | -------------------------------------------------------- |
| `dev`   | **90**                    | Desarrollo, menos inversores, más historia para debug    |
| `prod`  | **30**                    | Producción con 300 inversores, balance disco vs historia |

Para cambiar el valor, agregar en `application-prod.yml`:

```yaml
app:
  data-retention-days: 45 # ej: 45 días si hay 200 inversores
```

### 2.4 Código

```java
// DatoInversorService.java
@Value("${app.data-retention-days:30}")
private int dataRetentionDays;

@Scheduled(cron = "0 0 3 * * ?")
public void purgeOldReadings() {
  Instant cutoff = Instant.now().minus(dataRetentionDays, ChronoUnit.DAYS);
  LOG.info("Auto-purge: deleting readings older than {} ({} days retention)", cutoff, dataRetentionDays);
  int deleted = deleteOlderThan(cutoff);
  LOG.info("Auto-purge complete: {} rows deleted", deleted);
}

```

**Log de ejemplo:**

```
2026-06-10 03:00:00 INFO  Auto-purge: deleting readings older than 2026-05-11T03:00:00Z (30 days retention)
2026-06-10 03:01:23 INFO  Auto-purge complete: 5200000 rows deleted
```

---

## 3. PostgreSQL Partitioning (RANGE por `tiempo`)

### 3.1 El Problema

Un `DELETE` masivo sobre una tabla monolítica es lento y bloqueante:

- PostgreSQL escanea el índice → marca filas como "dead tuples"
- El `VACUUM` posterior tiene que limpiar millones de tuplas muertas
- Con 156M filas/mes, borrar 5M filas puede tardar **minutos**
- Mientras tanto, los inserts se encolan

### 3.2 La Solución: Particionamiento Mensual

La tabla `dato_inversor` se convierte en una tabla particionada por RANGE
sobre la columna `tiempo`, con una partición por mes:

```
dato_inversor (tabla padre, PARTITION BY RANGE)
  ├── dato_inversor_2026_06  (2026-06-01 → 2026-07-01)
  ├── dato_inversor_2026_07  (2026-07-01 → 2026-08-01)
  ├── ...
  └── dato_inversor_default  (cualquier fecha fuera de rango)
```

### 3.3 Beneficios Concretos

| Operación             | Sin Partitioning                  | Con Partitioning                           |
| --------------------- | --------------------------------- | ------------------------------------------ |
| Purge mensual         | `DELETE 5M filas` (~3 min)        | `DROP PARTITION` (~5 ms)                   |
| Query latest-readings | Escanea tabla completa            | Partition pruning → solo mes actual        |
| Índice B-tree         | 1 índice gigante (no cabe en RAM) | 1 índice chico por partición (cabe en RAM) |
| VACUUM                | Sobre toda la tabla               | Sobre 1 partición chica                    |

### 3.4 Detalles Técnicos

- **Primary Key**: `(id, tiempo)` — PostgreSQL requiere que la partition key
  sea parte de la PK en tablas particionadas. JPA/Hibernate mapea solo `id`
  como `@Id`, pero funciona porque `ddl-auto: none` y Liquibase controla el schema.
- **Índices**: `idx_dato_inversor_inversor_id` e `idx_dato_inversor_tiempo`
  se crean sobre la tabla padre y se propagan automáticamente a todas las particiones.
- **Default partition**: Captura cualquier insert con fecha fuera de los rangos
  definidos, evitando errores.
- **Particiones futuras**: Se pueden agregar con:
  ```sql
  CREATE TABLE dato_inversor_2027_01 PARTITION OF dato_inversor
    FOR VALUES FROM ('2027-01-01') TO ('2027-02-01');
  ```

### 3.5 Liquibase Changeset

Archivo: `20260609000001_partition_dato_inversor.xml`

Pasos:

1. `ALTER TABLE dato_inversor RENAME TO dato_inversor_legacy` — preserva datos
2. `CREATE TABLE dato_inversor (...) PARTITION BY RANGE (tiempo)` — nueva tabla
3. `CREATE TABLE dato_inversor_2026_06 PARTITION OF ...` — particiones Jun-Dic 2026
4. `CREATE INDEX ...` — índices sobre tabla padre (se propagan)
5. `INSERT INTO dato_inversor SELECT * FROM dato_inversor_legacy` — migración
6. `ALTER TABLE dato_inversor ADD CONSTRAINT fk_...` — re-agregar FK
7. `DROP TABLE dato_inversor_legacy` — limpiar

---

## 4. Resumen de Archivos Modificados

| Archivo                                      | Cambio                                                                            |
| -------------------------------------------- | --------------------------------------------------------------------------------- |
| `DatoInversorRepository.java`                | +`findLatestReadings()`, +`deleteOlderThan()`                                     |
| `DatoInversorService.java`                   | +`findLatestReadings()`, +`deleteOlderThan()`, +`purgeOldReadings()` (@Scheduled) |
| `DatoInversorResource.java`                  | +`GET /api/dato-inversors/latest-readings`                                        |
| `InversorService.java`                       | +`findAllWithEagerRelationships()` (sin paginación)                               |
| `InversorResource.java`                      | +`GET /api/inversors/all`                                                         |
| `useInversores.ts`                           | Usa `/inversors/all` y `/dato-inversors/latest-readings`, +`latestReadings`       |
| `SolarMonitorApp.tsx`                        | `MonitorContextValue` incluye `latestReadings`                                    |
| `application-dev.yml`                        | +`app.data-retention-days: 90`                                                    |
| `application-prod.yml`                       | +`app.data-retention-days: 30`                                                    |
| `20260609000001_partition_dato_inversor.xml` | Nuevo changeset de partitioning                                                   |
| `master.xml`                                 | Include del changeset de partitioning                                             |
| `sidebar.tsx`                                | +link Tracker en Administración                                                   |

---

## 5. Guía Rápida para el Administrador

### Configuración de Retención de Datos

El sistema purga automáticamente los datos de sensores mayores al período
de retención configurado. El purge corre todos los días a las 3:00 AM.

**Ajustar según cantidad de inversores:**

| Inversores | Recomendación | Config en `application-prod.yml` |
| ---------- | ------------- | -------------------------------- |
| 50-100     | 90 días       | `app.data-retention-days: 90`    |
| 100-200    | 45 días       | `app.data-retention-days: 45`    |
| 200-300    | 30 días       | `app.data-retention-days: 30`    |
| 300+       | 15-20 días    | `app.data-retention-days: 20`    |

**Estimación de almacenamiento:**

| Inversores | Filas/día | 30 días (aprox.) | Tamaño en disco (estimado) |
| ---------- | --------- | ---------------- | -------------------------- |
| 80         | 1.38M     | 41M              | ~8 GB                      |
| 150        | 2.6M      | 78M              | ~15 GB                     |
| 300        | 5.2M      | 156M             | ~30 GB                     |

> Cada fila de `dato_inversor` ocupa ~200 bytes. Con particionamiento,
> el VACUUM mantiene el tamaño estable.

### Agregar Nueva Partición Mensual

Las particiones están pre-creadas hasta Diciembre 2026. Para 2027:

```sql
-- Conectarse a PostgreSQL
psql -U inversorMonitor -d inversorMonitor

-- Crear partición de Enero 2027
CREATE TABLE dato_inversor_2027_01 PARTITION OF dato_inversor
  FOR VALUES FROM ('2027-01-01') TO ('2027-02-01');

-- Crear partición de Febrero 2027
CREATE TABLE dato_inversor_2027_02 PARTITION OF dato_inversor
  FOR VALUES FROM ('2027-02-01') TO ('2027-03-01');

-- Y así sucesivamente...
```

> **Recomendación:** Crear las particiones del mes siguiente al menos
> una semana antes del cambio de mes. Se puede automatizar con un
> cron job o agregar un `@Scheduled` en Java.

### Purge Manual (Emergencia)

Si necesitás purgar datos manualmente fuera del horario programado:

```bash
# Conectarse a la BD
psql -U inversorMonitor -d inversorMonitor

# Ver tamaño de la tabla
SELECT pg_size_pretty(pg_total_relation_size('dato_inversor'));

# Borrar datos anteriores a 30 días
DELETE FROM dato_inversor WHERE tiempo < NOW() - INTERVAL '30 days';

# Forzar VACUUM después de un DELETE masivo
VACUUM ANALYZE dato_inversor;
```

**O mejor aún, si usás partitioning — borrar la partición completa:**

```sql
-- Ver qué particiones existen
SELECT inhrelid::regclass FROM pg_inherits WHERE inhparent = 'dato_inversor'::regclass;

-- Borrar la partición de Mayo 2026 (instantáneo, sin VACUUM)
DROP TABLE dato_inversor_2026_05;
```

### Monitoreo

```sql
-- Cantidad de filas por partición (estimación rápida)
SELECT relname, reltuples::bigint
FROM pg_class
WHERE relname LIKE 'dato_inversor_20%'
ORDER BY relname;

-- Última lectura por inversor (lo que usa el dashboard)
SELECT i.nombre, di.tiempo, di.bateria_voltaje_v, di.pv_potencia_w
FROM dato_inversor di
JOIN inversor i ON i.id = di.inversor_id
WHERE di.tiempo = (SELECT MAX(tiempo) FROM dato_inversor WHERE inversor_id = i.id);

-- Espacio en disco por tabla
SELECT relname, pg_size_pretty(pg_total_relation_size(C.oid))
FROM pg_class C
LEFT JOIN pg_namespace N ON N.oid = C.relnamespace
WHERE relname LIKE 'dato_inversor%'
ORDER BY pg_total_relation_size(C.oid) DESC;
```

---

_Documento generado Junio 2026 — Etecsa Inversor Monitor_
