# Guia de Administracion — Inversor Monitor ETECSA

## Tabla de Contenidos

1. [Optimizaciones de Escala (300+ inversores)](#1-optimizaciones-de-escala)
2. [Endpoints REST](#2-endpoints-rest)
3. [Tareas Programadas (@Scheduled)](#3-tareas-programadas)
4. [Configuracion de Retencion de Datos](#4-configuracion-de-retencion)
5. [Partitioning PostgreSQL](#5-partitioning-postgresql)
6. [Agregar una Nueva Marca de Inversor](#6-agregar-una-nueva-marca)
7. [Configurar la Marca MUST](#7-configurar-la-marca-must)
8. [Monitoreo y Troubleshooting](#8-monitoreo-y-troubleshooting)

---

## 1. Optimizaciones de Escala

### Problema

Con 300 inversores generando lecturas cada 5 segundos, la tabla `dato_inversor` crece a **~5.2 millones de filas por dia**. Sin optimizaciones:

| Escala         | Filas/dia | Filas/mes | Problema                        |
| -------------- | --------- | --------- | ------------------------------- |
| 80 inversores  | 1.38M     | 41M       | Manejable, pero sin purge crece |
| 300 inversores | 5.2M      | 156M      | Query lento, disco lleno        |
| 500 inversores | 8.6M      | 259M      | Inusable sin optimizar          |

### Optimizaciones Implementadas

| #   | Optimizacion                   | Beneficio                                                     |
| --- | ------------------------------ | ------------------------------------------------------------- |
| 1   | **Endpoint `latest-readings`** | 1 sola query = 300 filas (1 por inversor) vs paginar millones |
| 2   | **Endpoint `/inversors/all`**  | Lista completa sin paginar, para dropdowns y dashboard        |
| 3   | **Auto-purge @Scheduled**      | Borra datos viejos automaticamente, tabla controlada          |
| 4   | **PostgreSQL partitioning**    | Purge instantaneo (DROP PARTITION), queries mas rapidas       |

---

## 2. Endpoints REST

### Endpoints Estandar (JHipster)

Todas las entidades exponen los endpoints CRUD estandar:

```
GET    /api/{entity}s           — Lista paginada (?page=0&size=20&sort=campo,asc)
POST   /api/{entity}s           — Crear
PUT    /api/{entity}s/{id}      — Actualizar
PATCH  /api/{entity}s/{id}      — Actualizacion parcial
DELETE /api/{entity}s/{id}      — Eliminar
GET    /api/{entity}s/{id}      — Obtener por ID
GET    /api/{entity}s/count     — Contar
```

### Endpoints Optimizados (nuevos)

#### `GET /api/dato-inversors/latest-readings`

Devuelve la **ultima lectura de cada inversor** — una sola fila por inversor.

**Query interna:**

```sql
SELECT di.* FROM dato_inversor di
INNER JOIN (
  SELECT inversor_id, MAX(tiempo) AS max_tiempo
  FROM dato_inversor
  GROUP BY inversor_id
) latest ON di.inversor_id = latest.inversor_id
  AND di.tiempo = latest.max_tiempo
```

**Por que no usar el endpoint paginado estandar?**

El endpoint `GET /api/dato-inversors?size=200` devuelve 200 filas cualesquiera de millones. No garantiza que sean las mas recientes ni que haya una por inversor. `latest-readings` devuelve exactamente lo que el dashboard necesita.

**Uso:**

```bash
curl -H "Authorization: Bearer <token>" \
  http://localhost:8081/api/dato-inversors/latest-readings
```

**Respuesta:** Array de `DatoInversorDTO`, uno por inversor activo.

---

#### `GET /api/inversors/all`

Devuelve **todos los inversores** sin paginar, con sus relaciones cargadas (marca, modelo, gateway, sitio, master).

**Por que?** Con 300 inversores, `?size=200` no alcanza. Este endpoint devuelve la lista completa en una sola request. Los inversores son registros de configuracion (no datos de sensores), asi que 300 filas son ligeras.

**Uso:**

```bash
curl -H "Authorization: Bearer <token>" \
  http://localhost:8081/api/inversors/all
```

**Respuesta:** Array completo de `InversorDTO`.

---

#### `POST /api/inversors/{masterId}/esclavos`

Registra un inversor esclavo bajo un master. El esclavo HEREDA del master: marca, modelo, gateway, sitio, tipoConexion, periodoMuestreoSeg.

**Uso:**

```bash
curl -X POST -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"ESCLAVO-01","slaveId":2,"numeroSerie":"SN-ESCL-001"}' \
  http://localhost:8081/api/inversors/1/esclavos
```

---

## 3. Tareas Programadas (@Scheduled)

### Auto-Purge de `dato_inversor`

| Propiedad  | Valor                                              |
| ---------- | -------------------------------------------------- |
| **Clase**  | `DatoInversorService`                              |
| **Metodo** | `purgeOldReadings()`                               |
| **Cron**   | `0 0 3 * * ?` (todos los dias a las 3:00 AM)       |
| **Config** | `app.data-retention-days` (default: 30)            |
| **Accion** | `DELETE FROM dato_inversor WHERE tiempo < :cutoff` |

**Que hace:** Calcula `cutoff = ahora - N dias` y borra todas las lecturas anteriores. Luego loguea cuantas filas borro.

**Log de ejemplo:**

```
INFO  c.e.m.s.DatoInversorService - Auto-purge: deleting DatoInversor readings older than 2026-05-10T03:00:00Z (30 days retention)
INFO  c.e.m.s.DatoInversorService - Auto-purge complete: 5234567 rows deleted
```

### Rediscovery de Gateways

| Propiedad      | Valor                                                               |
| -------------- | ------------------------------------------------------------------- |
| **Clase**      | `PollingService`                                                    |
| **Metodo**     | `discoverAndScheduleGateways()`                                     |
| **Frecuencia** | Cada 60 segundos (`fixedRate = 60000`)                              |
| **Accion**     | Verifica gateways nuevos/inactivos y lanza/detiene hilos de polling |

### Limpieza de Tokens Expirados (JHipster standard)

| Propiedad | Valor                                       |
| --------- | ------------------------------------------- |
| **Clase** | `UserService`                               |
| **Cron**  | `0 0 1 * * ?` (todos los dias a la 1:00 AM) |

---

## 4. Configuracion de Retencion

La propiedad `app.data-retention-days` controla cuantos dias de datos historicos se conservan en `dato_inversor` antes del purge automatico.

| Profile  | Valor default | Archivo                |
| -------- | ------------- | ---------------------- |
| **dev**  | 90 dias       | `application-dev.yml`  |
| **prod** | 30 dias       | `application-prod.yml` |

**Calcular el valor optimo:**

| Inversores | Filas/dia | 30 dias | 90 dias | 180 dias |
| ---------- | --------- | ------- | ------- | -------- |
| 80         | 1.38M     | 41M     | 124M    | 248M     |
| 150        | 2.59M     | 78M     | 233M    | 466M     |
| 300        | 5.18M     | 156M    | 468M    | 936M     |
| 500        | 8.64M     | 259M    | 778M    | 1.5B     |

**Recomendaciones:**

- **80 inversores** → 90 dias (dev y prod)
- **150 inversores** → 60 dias
- **300 inversores** → 30 dias
- **500+ inversores** → 15-20 dias

**Para cambiar el valor en produccion (sin recompilar):**

```yaml
# application-prod.yml
app:
  data-retention-days: 60
```

O via variable de entorno:

```bash
export APP_DATA_RETENTION_DAYS=60
```

---

## 5. Partitioning PostgreSQL

### Como funciona

La tabla `dato_inversor` esta particionada por **mes** usando `PARTITION BY RANGE (tiempo)`:

```
dato_inversor (tabla padre, virtual)
  ├── dato_inversor_2026_06  (Jun 2026)
  ├── dato_inversor_2026_07  (Jul 2026)
  ├── dato_inversor_2026_08  (Ago 2026)
  ├── dato_inversor_2026_09  (Sep 2026)
  ├── dato_inversor_2026_10  (Oct 2026)
  ├── dato_inversor_2026_11  (Nov 2026)
  ├── dato_inversor_2026_12  (Dic 2026)
  └── dato_inversor_default  (fuera de rango)
```

### Beneficios

| Operacion           | Sin partitioning                      | Con partitioning                                     |
| ------------------- | ------------------------------------- | ---------------------------------------------------- |
| **Purge mensual**   | `DELETE` de 5M filas (lento, bloquea) | `DROP TABLE dato_inversor_2026_05` (instantaneo)     |
| **Query por fecha** | Scan full table                       | Partition pruning: solo escanea la particion del mes |
| **VACUUM**          | Sobre tabla gigante                   | Sobre particiones chicas                             |
| **Indice**          | Un B-tree enorme (no cabe en RAM)     | Un B-tree por particion (cabe en RAM)                |

### Mantenimiento de particiones

**Crear particiones para un nuevo mes (hacer antes de que empiece):**

```sql
-- Ejemplo: crear particion para Enero 2027
CREATE TABLE dato_inversor_2027_01 PARTITION OF dato_inversor
  FOR VALUES FROM ('2027-01-01') TO ('2027-02-01');
```

**Eliminar particion vieja (purge instantaneo):**

```sql
-- Eliminar todos los datos de Mayo 2026
DROP TABLE dato_inversor_2026_05;
```

> **Nota:** El `@Scheduled` actual usa `DELETE` que funciona con partitioning (PostgreSQL routing automatico). En una iteracion futura se puede cambiar a `DROP PARTITION` para purge instantaneo.

**Ver particiones existentes:**

```sql
SELECT inhrelid::regclass AS partition
FROM pg_inherits
WHERE inhparent = 'dato_inversor'::regclass
ORDER BY partition;
```

**Ver tamano de cada particion:**

```sql
SELECT relname AS partition,
       pg_size_pretty(pg_total_relation_size(oid)) AS size
FROM pg_class
WHERE relname LIKE 'dato_inversor_2026%'
ORDER BY relname;
```

---

## 6. Agregar una Nueva Marca de Inversor

El sistema soporta **cualquier marca** de inversor sin cambiar codigo Java. Solo necesita datos en la base (Marca + Modelo + MapeoRegistro).

### Paso a Paso

#### Paso 1: Crear la Marca

Ir a **Entidades > Marcas** y crear:

| Campo                | Ejemplo (MUST)    | Descripcion                        |
| -------------------- | ----------------- | ---------------------------------- |
| nombre               | MUST              | Nombre de la marca                 |
| fabricante           | MUST PV Power Co. | Fabricante                         |
| registro_inicio      | 0                 | Registro Modbus inicial de lectura |
| registro_fin         | 49                | Registro Modbus final de lectura   |
| registro_info_inicio | 100               | Registro inicio de configuracion   |
| registro_info_fin    | 149               | Registro fin de configuracion      |
| baud_rate_default    | 9600              | Velocidad RS485                    |
| activa_marca_default | true              | Marca activa por defecto           |

#### Paso 2: Crear el Modelo

Ir a **Entidades > Modelos Inversor** y crear:

| Campo               | Ejemplo            | Descripcion                 |
| ------------------- | ------------------ | --------------------------- |
| nombre              | MUST PH1800        | Nombre del modelo           |
| capacidad_va        | 1800               | Capacidad en VA             |
| voltaje_sistema_nom | 24                 | Voltaje nominal del sistema |
| marca               | MUST (seleccionar) | Marca creada en Paso 1      |

#### Paso 3: Crear los Mapeos de Registro

Ir a **Entidades > Mapeo Registros** y crear **uno por cada campo** que se quiera leer del inversor. Cada mapeo define:

| Campo              | Descripcion                       | Ejemplo MUST          |
| ------------------ | --------------------------------- | --------------------- |
| direccion_registro | Direccion Modbus (decimal)        | 0                     |
| nombre_registro    | Nombre legible                    | Battery Voltage       |
| campo_sql          | Columna en `dato_inversor`        | bateria_voltaje_v     |
| tipo_dato          | Tipo de dato Modbus               | UnsignedInt16         |
| escala             | Factor de escala (1 = sin escala) | 100 (dividir por 100) |
| unidad             | Unidad de medida                  | V                     |
| activo             | Si se lee en el polling           | true                  |
| marca              | Marca a la que pertenece          | MUST                  |

**Tipos de dato disponibles:**

| Tipo          | Descripcion               | Rango                  |
| ------------- | ------------------------- | ---------------------- |
| UnsignedInt16 | Entero sin signo 16 bits  | 0–65535                |
| SignedInt16   | Entero con signo 16 bits  | -32768–32767           |
| UnsignedInt32 | Entero sin signo 32 bits  | 0–4294967295           |
| SignedInt32   | Entero con signo 32 bits  | -2147483648–2147483647 |
| FloatIEEE754  | Flotante 32 bits IEEE 754 | -3.4E38–3.4E38         |
| AsciiText     | Texto ASCII               | —                      |

**Campos SQL disponibles en `dato_inversor`:**

| campo_sql           | Descripcion                 | Tipo Java  |
| ------------------- | --------------------------- | ---------- |
| bateria_voltaje_v   | Voltaje de bateria          | Double     |
| bateria_corriente_a | Corriente de bateria        | Double     |
| bateria_potencia_w  | Potencia de bateria         | Double     |
| pv_voltaje_v        | Voltaje del panel solar     | Double     |
| pv_potencia_w       | Potencia del panel solar    | Double     |
| salida_voltaje_v    | Voltaje de salida AC        | Double     |
| salida_potencia_w   | Potencia de salida AC       | Double     |
| salida_aparente_va  | Potencia aparente de salida | Double     |
| carga_porcentaje    | Porcentaje de carga         | Integer    |
| red_voltaje_v       | Voltaje de red AC           | Double     |
| red_frecuencia_hz   | Frecuencia de red AC        | Double     |
| modo_operacion      | Modo de operacion           | String(20) |
| codigo_fallo        | Codigo de fallo             | Integer    |
| eficiencia_pct      | Eficiencia porcentual       | Double     |
| consumo_red_w       | Consumo de red              | Double     |
| bateria_cargando    | Bateria cargando (flag)     | Boolean    |
| pv_activo           | Panel solar activo (flag)   | Boolean    |
| red_activa          | Red activa (flag)           | Boolean    |

#### Paso 4: Asignar Marca al Inversor

Ir a **Entidades > Inversores** y asignar:

- **Marca** → MUST
- **Modelo** → MUST PH1800
- **Gateway** → El gateway TCP correspondiente

A partir de ese momento, el PollingService leera los registros Modbus definidos en los mapeos y los almacenara en `dato_inversor`.

---

## 7. Configurar la Marca MUST

### Informacion Necesaria

Para configurar la marca **MUST** (o cualquier otra), necesitamos:

1. **Mapa de registros Modbus** — Que direccion de registro contiene cada dato y con que tipo/escala. Esto sale del manual del inversor (usualmente la seccion "Modbus Communication Protocol").

2. **Rango de registros** — Desde/hasta que registro leer para obtener datos operativos.

3. **Rango de configuracion** — Registros de configuracion (opcionales, para la pantalla Settings).

4. **Baud rate** — Velocidad de comunicacion RS485 (tipico: 9600 o 2400).

### Que necesito de vos

Para configurar MUST necesito que me proporciones:

| Dato                                | Donde encontrarlo                            | Ejemplo                           |
| ----------------------------------- | -------------------------------------------- | --------------------------------- |
| **Manual Modbus** del inversor MUST | PDF del fabricante o sitio web               | "MUST PV1800 Modbus Protocol.pdf" |
| **Modelo exacto**                   | Placa del inversor                           | PH1800, PV1800, EP1800, etc.      |
| **Rango de registros de datos**     | Seccion "Read Holding Registers" del manual  | 0x0000–0x0031                     |
| **Rango de registros de config**    | Seccion "Write Holding Registers" del manual | 0x0064–0x0095                     |
| **Baud rate**                       | Configuracion DIP del gateway                | 9600                              |
| **Tipo de conexion**                | RS485 o TCP                                  | SerialRs485 o TcpModbus           |

### Formato de la informacion

Idealmente, una tabla como esta (extraida del manual):

| Direccion (hex/dec) | Nombre          | Tipo | Escala | Unidad | Campo SQL           |
| ------------------- | --------------- | ---- | ------ | ------ | ------------------- |
| 0x0001 / 1          | Battery Voltage | U16  | 100    | V      | bateria_voltaje_v   |
| 0x0002 / 2          | Battery Current | S16  | 1      | A      | bateria_corriente_a |
| ...                 | ...             | ...  | ...    | ...    | ...                 |

Con esa tabla, yo genero:

1. Los registros de `MapeoRegistro` en la base (via seed data o CRUD)
2. La marca MUST con sus rangos
3. Los modelos MUST disponibles
4. Todo funciona automaticamente con el PollingService existente

**NO hace que cambiar codigo Java.** El sistema esta disenado para ser **100% data-driven** — la marca Felicity funciona solo con datos en la base, y MUST va a funcionar igual.

### Si no tenes el manual

Si no tenes el manual Modbus, podemos:

1. Buscarlo online (muchos fabricantes lo publican)
2. Hacer un scan de registros con una herramienta Modbus (qModMaster, modpoll)
3. Empezar con mapeos basicos y agregar mas a medida que se descubren

---

## 8. Monitoreo y Troubleshooting

### Verificar que el polling funciona

```bash
# Ver estado de gateways
curl -H "Authorization: Bearer <token>" \
  http://localhost:8081/api/gateway-tcps

# Ver ultimas lecturas
curl -H "Authorization: Bearer <token>" \
  http://localhost:8081/api/dato-inversors/latest-readings
```

### Verificar que el purge funciona

Buscar en los logs:

```
grep "Auto-purge" logs/inversor-monitor.log
```

### Verificar particiones

```sql
-- Conectarse a PostgreSQL
psql -U inversorMonitor -d inversorMonitor

-- Listar particiones
SELECT inhrelid::regclass FROM pg_inherits
WHERE inhparent = 'dato_inversor'::regclass;

-- Ver tamano
SELECT relname, pg_size_pretty(pg_total_relation_size(oid))
FROM pg_class WHERE relname LIKE 'dato_inversor_2026%';
```

### Problemas comunes

| Problema                  | Causa                               | Solucion                                               |
| ------------------------- | ----------------------------------- | ------------------------------------------------------ |
| No hay datos en dashboard | Polling no arranca                  | Verificar que el gateway este activo y con IP correcta |
| Datos viejos en dashboard | Frontend usa endpoint paginado      | Verificar que use `/latest-readings`                   |
| Tabla muy grande          | Purge no corre o retencion muy alta | Verificar `app.data-retention-days` y logs de purge    |
| Error de conexion Modbus  | Gateway inalcanzable                | Verificar IP, puerto, y que el gateway este encendido  |
| Inversor sin datos        | Sin marca o sin mapeos              | Asignar marca y crear MapeoRegistros para esa marca    |
| Particion faltante        | Mes nuevo sin particion creada      | Crear particion con `CREATE TABLE ... PARTITION OF`    |

---

## Resumen de Archivos Modificados

| Archivo                                      | Cambio                                                                            |
| -------------------------------------------- | --------------------------------------------------------------------------------- |
| `DatoInversorService.java`                   | +`findLatestReadings()`, +`deleteOlderThan()`, +`purgeOldReadings()` (@Scheduled) |
| `DatoInversorResource.java`                  | +`GET /api/dato-inversors/latest-readings`                                        |
| `DatoInversorRepository.java`                | +`findLatestReadings()` (native query), +`deleteOlderThan()` (native DELETE)      |
| `InversorService.java`                       | +`findAllWithEagerRelationships()` (sin paginar)                                  |
| `InversorResource.java`                      | +`GET /api/inversors/all`                                                         |
| `useInversores.ts`                           | Usa `/inversors/all` y `/dato-inversors/latest-readings`                          |
| `SolarMonitorApp.tsx`                        | +`latestReadings` en MonitorContext                                               |
| `application-dev.yml`                        | +`app.data-retention-days: 90`                                                    |
| `application-prod.yml`                       | +`app.data-retention-days: 30`                                                    |
| `sidebar.tsx`                                | +link a Tracker en Administracion                                                 |
| `20260609000001_partition_dato_inversor.xml` | Liquibase: partitioning mensual de dato_inversor                                  |
| `master.xml`                                 | Include del changeset de partitioning                                             |
