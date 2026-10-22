# Análisis General — Inversor Monitor (ETECSA Solar)

> **Stack**: JHipster 8.11.0 | Spring Boot 3.4.5 | React + MUI v5 | PostgreSQL 17 | Modbus TCP
> **Branch**: `javier` — 15 commits desde init, ~5 semanas de desarrollo

---

## 1. Arquitectura General

```
┌─────────────────────────────────────────────────────┐
│                   FRONTEND (React)                   │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌────────┐ │
│  │Dashboard │ │Inversor  │ │ History  │ │Settings│ │
│  │Screen    │ │Detail    │ │Screen    │ │Screen  │ │
│  └────┬─────┘ └────┬─────┘ └────┬─────┘ └───┬────┘ │
│       │            │            │            │      │
│  ┌────┴────────────┴────────────┴────────────┴───┐  │
│  │           MonitorProvider (Context)            │  │
│  │  ┌────────────┐  ┌──────────┐                  │  │
│  │  │useInversores│  │useLiveData│                 │  │
│  │  │(REST fetch) │  │(WebSocket)│                 │  │
│  │  └────────────┘  └──────────┘                  │  │
│  └────────────────────────────────────────────────┘  │
│                          │ REST + WS                  │
└──────────────────────────┼──────────────────────────┘
                           │
┌──────────────────────────┼──────────────────────────┐
│               BACKEND (Spring Boot)                  │
│  ┌──────────────────────────────────────────────┐   │
│  │          REST Controllers (DatoInversor, etc) │   │
│  ├──────────────────────────────────────────────┤   │
│  │            Services + QueryServices            │   │
│  ├──────────────────────────────────────────────┤   │
│  │          ┌────────────┐ ┌──────────┐          │   │
│  │          │PollingSvc  │ │AlertEngine│          │   │
│  │          │(Modbus TCP)│ │(rules)   │          │   │
│  │          └────────────┘ └──────────┘          │   │
│  │          ┌──────────────────────────┐          │   │
│  │          │ LiveDataBroadcaster (WS)  │          │   │
│  │          └──────────────────────────┘          │   │
│  ├──────────────────────────────────────────────┤   │
│  │     Spring Data JPA Repositories + Liquibase   │   │
│  └──────────────────────────────────────────────┘   │
│                          │                           │
└──────────────────────────┼──────────────────────────┘
                           │
                    ┌──────┴──────┐
                    │  PostgreSQL  │
                    │  (17, range  │
                    │ partitioned) │
                    └─────────────┘
```

### Entidades principales

| Entidad            | Propósito                              | Filas estimadas |
| ------------------ | -------------------------------------- | --------------- |
| **Inversor**       | Catálogo de inversores (master/slave)  | ~300            |
| **GatewayTcp**     | Gateways Modbus TCP (IP:puerto)        | ~10-20          |
| **DatoInversor**   | Lecturas de telemetría (time-series)   | ~5.2M/día       |
| **DatoEficiencia** | Resúmenes diarios de eficiencia        | ~300/día        |
| **Marca**          | Marcas de inversor (Felicity, MUST)    | 2               |
| **ModeloInversor** | Modelos por marca                      | ~10             |
| **MapeoRegistro**  | Mapeo de registros Modbus → campos SQL | ~60+            |
| **EventoAlerta**   | Alertas disparadas por el motor        | variable        |
| **AlertaConfig**   | Configuración de reglas de alerta      | variable        |
| **Sitio**          | Ubicaciones físicas                    | variable        |

---

## 2. Sistema de Polling (Modbus TCP)

### Arquitectura Multi-Master

```
GatewayTcp (IP:502)
  └── Master #1 (slaveId=1) ← MapeoRegistros de la MARCA
  │     └── Slave #1 (slaveId=2, masterRef=#1)
  │     └── Slave #2 (slaveId=3, masterRef=#1)
  └── Master #2 (slaveId=4) ← MapeoRegistros de la MARCA
        └── Slave #3 (slaveId=5, masterRef=#2)
```

### PollingService — Mecanismo

```
ApplicationReadyEvent
    └── discoverAndScheduleGateways()
          └── por cada Gateway activo →
                scheduler.scheduleAtFixedRate(
                    pollGateway(gw), 0, interval, SECONDS
                )
```

**Características clave:**

| Aspecto                | Detalle                                                               |
| ---------------------- | --------------------------------------------------------------------- |
| Thread pool            | `Executors.newScheduledThreadPool(4)`                                 |
| Scheduling dinámico    | Cada gateway tiene su propio `ScheduledFuture` en `ConcurrentHashMap` |
| Intervalo configurable | Por gateway: `tiempo_espera_ms` en la entidad GatewayTcp              |
| Intervalo mínimo       | 5 segundos (hardcoded)                                                |
| Rediscovery            | Cada 60 segundos vía `@Scheduled(fixedRate = 60000)`                  |
| Shutdown graceful      | `@PreDestroy` cancela todos los futures                               |

### Ciclo de polling por gateway

```
pollGateway(gateway)
  ├── inversorRepository.findByGatewayAndActivoTrue(gateway)
  ├── Separa masters de slaves
  ├── Para cada MASTER:
  │     ├── pollInversor(host, port, master)
  │     │     ├── mapeoRepository.findByMarcaAndActivoTrue(marca)
  │     │     ├── modbusClient.readRegistersWithLatency(host, port, slaveId, mapeos)
  │     │     ├── buildDatoInversor(inversor, raw, latencyMs) → mapeo campo por campo
  │     │     ├── datoInversorRepository.save(dato)
  │     │     ├── updateInversorSuccess(inversor, latencyMs)
  │     │     ├── broadcaster.broadcastDatoInversor(inversorId, dato)
  │     │     └── alertEngine.evaluate(inversor, dato)
  │     └── Para cada SLAVE del master:
  │           └── pollInversor(host, port, slave) ← misma conexión, distinto slaveId
  └── Actualiza estado del gateway (Conectado/Desconectado)
```

### ModbusTcpClient — Driver ligero

- **Sin dependencias externas**: implementa Modbus TCP con sockets Java puros
- **Function Code**: 0x03 (Read Holding Registers)
- **Timeout conexión**: 5 segundos
- **Timeout lectura**: 3 segundos
- **Agrupación de registros**: `groupIntoBlocks()` → minimiza pedidos agrupando registros contiguos en un solo request FC03 (máx 100 registros por pedido)
- **Byte Order**: Big-Endian (Modbus estándar)
- **Tipos de dato soportados**: UnsignedInt16, SignedInt16, UnsignedInt32, SignedInt32, FloatIEEE754, AsciiText
- **Métrica de latencia**: `readRegistersWithLatency()` mide tiempo total del ciclo

---

## 3. WebSocket — Tiempo Real

### Backend (LiveDataBroadcaster)

```
STOMP sobre SockJS en /websocket/tracker (config JHipster)
  ├── /topic/inversor/{id}/live    → DatoInversorDTO por inversor
  ├── /topic/inversor/{id}/alerta  → EventoAlertaDTO por inversor
  └── /topic/alertas               → EventoAlertaDTO global
```

### Frontend (useLiveData hook)

```
SockJS → STOMP → suscripción a /topic/inversor/{id}/live
  ├── Se suscribe a TODOS los inversores activos
  ├── Cada mensaje → actualiza LiveDataMap (Record<id, DatoInversor>)
  ├── Alerta nueva → se agrega al array de alertas en vivo
  ├── Auto-reconnect cada 5 segundos
  └── Re-suscribe cuando cambian los inversorIds
```

### Flujo de datos en tiempo real

```
[Modbus] → PollingService → DatoInversor (DB)
                              ↓
                        LiveDataBroadcaster
                              ↓ (STOMP)
                        [Frontend] → setLiveData(prev => ({...prev, [id]: dato}))
                              ↓
                        DashboardScreen re-renders con:
                          - LiveDataMap actualizada
                          - KPIs recalculados
                          - Grupos actualizados
```

---

## 4. Optimizaciones de Base de Datos

### 4.1 Particionamiento por Rango (PostgreSQL)

**Archivo**: `20260609000001_partition_dato_inversor.xml`

```
dato_inversor (tabla particionada por RANGE en tiempo)
  ├── dato_inversor_2026_06 (junio)
  ├── dato_inversor_2026_07 (julio)
  ├── dato_inversor_2026_08 (agosto)
  ├── dato_inversor_2026_09 (septiembre)
  ├── dato_inversor_2026_10 (octubre)
  ├── dato_inversor_2026_11 (noviembre)
  ├── dato_inversor_2026_12 (diciembre)
  └── dato_inversor_default (para datos fuera de rango)
```

**Por qué**: 300 inversores × 1 lectura/5s = **5.2M filas/día** = **156M filas/mes**

**Beneficios**:

- **Partition pruning**: queries de últimas 24h solo escanean la partición actual
- **Purge instantáneo**: DROP PARTITION en vez de DELETE millones de filas
- **Índices pequeños**: mejor cache hit ratio
- **VACUUM rápido**: opera por partición individual

### 4.2 Índices Estratégicos

**Archivo**: `20260604000001_added_constraints_and_indexes.xml`

| Índice                                 | Tabla           | Columnas                  | Propósito                                    |
| -------------------------------------- | --------------- | ------------------------- | -------------------------------------------- |
| `uk_inversor_gateway_slave`            | inversor        | `gateway_id + slave_id`   | UNIQUE: no duplicar slaveId en mismo gateway |
| `idx_inversor_gateway`                 | inversor        | `gateway_id`              | Find by gateway para polling                 |
| `idx_inversor_sitio`                   | inversor        | `sitio_id`                | Filtro por ubicación                         |
| `idx_inversor_master_ref`              | inversor        | `master_referencia_id`    | Find slaves of master                        |
| `idx_inversor_es_master`               | inversor        | `es_master`               | Separar masters/slaves rápido                |
| `idx_dato_inversor_inversor_tiempo`    | dato_inversor   | `inversor_id + tiempo`    | Time-series queries (CRÍTICO)                |
| `idx_dato_eficiencia_inversor_fecha`   | dato_eficiencia | `inversor_id + fecha`     | KPIs diarios                                 |
| `idx_evento_alerta_inversor_timestamp` | evento_alerta   | `inversor_id + timestamp` | Historial de alertas                         |
| `idx_gateway_tcp_sitio`                | gateway_tcp     | `sitio_id`                | Gateways por ubicación                       |
| `idx_alerta_config_inversor`           | alerta_config   | `inversor_id`             | Alertas por inversor                         |

### 4.3 Query de Latest Readings (Optimizada)

```sql
SELECT di.* FROM dato_inversor di
INNER JOIN (
    SELECT inversor_id, MAX(tiempo) AS max_tiempo
    FROM dato_inversor
    GROUP BY inversor_id
) latest ON di.inversor_id = latest.inversor_id AND di.tiempo = latest.max_tiempo
```

- Usa INNER JOIN con subquery agregada en vez de DISTINCT ON
- Con el índice `idx_dato_inversor_inversor_tiempo`, el GROUP BY es un Index-Only Scan
- Devuelve exactamente 1 fila por inversor (300 filas en vez de millones)

### 4.4 Auto-Purge Schedule

```java
@Scheduled(cron = "0 0 3 * * ?") // 3 AM todos los días
public void purgeOldReadings() {
  // Elimina datos anteriores a app.data-retention-days (default: 30)
  // DELETE nativo → rápido para bulk
  // PostgreSQL auto-VACUUM después
}

```

### 4.5 Trend-24h Query

```sql
SELECT EXTRACT(HOUR FROM tiempo)::int AS hora, AVG(pv_potenciaw) AS avg_potencia
FROM dato_inversor
WHERE tiempo >= NOW() - INTERVAL '24 hours'
  AND pv_potenciaw IS NOT NULL AND pv_potenciaw > 0
GROUP BY EXTRACT(HOUR FROM tiempo)
ORDER BY hora
```

- Agregación por hora: 24 filas de resultado sin importar cuántos datos haya
- Filtra valores nulos y cero (noche sin sol)
- Particion pruning → solo escanea la partición actual

---

## 5. Soporte para Múltiples Marcas

### Felicity Solar (id=1)

| Propiedad       | Valor                            |
| --------------- | -------------------------------- |
| Rango registros | 0x1101-0x112A (lectura)          |
| Rango info      | 0x211F-0x2159 (configuración)    |
| Baud rate       | 2400                             |
| Modelos         | Lumin Plus 5.5K, Lumin Plus 3.5K |

### MUST Power (id=2)

| Propiedad       | Valor                                                      |
| --------------- | ---------------------------------------------------------- |
| Rango registros | Bloque B 15201-15221 (PV), Bloque D 25201-25243 (inversor) |
| Rango info      | 20101-20146                                                |
| Baud rate       | 9600 (PH1800/PV1800), 19200 (PH18/PV18 Pro)                |
| Slave ID        | 0x04                                                       |
| Modelos         | PH1800 5K, PV1800 5K, PH18-5448 PRO                        |

### Sistema de Mapeo por Marca

```
Inversor → Marca → MapeoRegistro[]
  ├── direccion_registro: offset en el holding register
  ├── tipo_dato: UnsignedInt16, SignedInt32, FloatIEEE754, etc.
  ├── escala: divisor (ej: 10 para voltaje, 100 para temperatura)
  ├── campo_sql: columna destino en DatoInversor
  └── activo: si el mapeo está habilitado
```

**Campos de telemetría mapeados** (30+ columnas):

```
bateria_voltaje_v     → Batería
bateria_corriente_a   → Batería
bateria_potencia_w    → Batería
pv_voltaje_v          → Panel solar
pv_potencia_w         → Panel solar
pv_corriente_a        → Panel solar ← Fase 3
salida_voltaje_v      → Salida AC
salida_potencia_w     → Salida AC
salida_corriente_a    → Salida AC ← Fase 3
salida_frecuencia_hz  → Salida AC ← Fase 3
carga_porcentaje      → Batería (SoC aproximado)
soc_pct               → Batería (SoC preciso) ← Fase 3
red_voltaje_v         → Red eléctrica
red_frecuencia_hz     → Red eléctrica
eficiencia_pct        → Eficiencia ← Fase 3
consumo_red_w         → Consumo de red ← Fase 3
temperatura_c         → Temperatura interna ← Fase 3
bus_voltaje_v         → Bus DC ← Fase 3
modo_operacion        → Modo de operación (string)
codigo_fallo          → Código de error
bateria_cargando      → Flag booleano
pv_activo             → Flag booleano
red_activa            → Flag booleano
crc_valido            → Integridad
latencia_ms           → Latencia de comunicación
```

**Decodificación de Modo de Operación** (por marca):

| Código | Felicity | MUST      |
| ------ | -------- | --------- |
| 0      | SIN_RED  | POWER_ON  |
| 1      | RED      | SELF_TEST |
| 2      | SOLAR    | OFF_GRID  |
| 3      | HIBRIDO  | GRID_TIE  |
| 4      | BATERIA  | BYPASS    |
| 5      | —        | CHARGING  |
| 6      | —        | FAULT     |

---

## 6. Motor de Alertas

**AlertEngine** — evaluado sincrónicamente en cada ciclo de polling:

```java
evaluate(inversor, dato)
  └── alertaConfigRepository.findByInversorAndActivaTrue(inversor)
        └── Por cada AlertaConfig:
              ├── Lee valor del campo (getFieldValue por nombre SQL)
              ├── Evalúa condición: valor operador umbral (>, <, >=, <=, ==, !=)
              ├── Verifica cooldown (evita spam)
              ├── Genera EventoAlerta → DB
              ├── Actualiza cooldown en ConcurrentHashMap
              └── broadcaster.broadcastEventoAlerta(inversorId, evento)
```

---

## 7. Endpoints REST (dashboard)

| Endpoint                                  | Propósito                       | Optimización             |
| ----------------------------------------- | ------------------------------- | ------------------------ |
| `GET /api/dato-inversors/latest-readings` | Última lectura de cada inversor | INNER JOIN + MAX(tiempo) |
| `GET /api/dato-inversors/trend-24h`       | Trend horario de PV power       | EXTRACT(HOUR) + AVG      |
| `GET /api/dato-inversors/export-csv`      | Exportación CSV con filtros     | Paginado 10k filas       |
| `GET /api/inversors/all`                  | Todos los inversores            | —                        |
| `GET /api/gateway-tcps`                   | Gateways activos                | —                        |
| `GET /api/dato-eficiencias`               | Eficiencias diarias             | —                        |
| `GET /api/evento-alertas`                 | Alertas (sort by timestamp)     | —                        |

### Frontend: carga inicial (Promise.all)

```typescript
const [invRes, readingsRes, gwRes, efRes, alertRes, trendRes] = await Promise.all([
  axios.get(`/api/inversors/all`),
  axios.get(`/api/dato-inversors/latest-readings`), // ~300 filas
  axios.get(`/api/gateway-tcps?size=50`), // ~20 filas
  axios.get(`/api/dato-eficiencias?size=200`), // ~200 filas
  axios.get(`/api/evento-alertas?sort=timestamp,desc&size=50`), // ~50 filas
  axios.get(`/api/dato-inversors/trend-24h`), // 24 filas
]);
```

6 endpoints paralelos → carga inicial en ~1-2 segundos incluso con 300 inversores.

---

## 8. Frontend — Estructura

### Screens

| Pantalla                 | Ruta                    | Descripción                             |
| ------------------------ | ----------------------- | --------------------------------------- |
| **DashboardScreen**      | `/monitor`              | KPIs + TrendBars + grupos de inversores |
| **InversorDetailScreen** | `/monitor/inversor/:id` | Detalle individual                      |
| **HistoryScreen**        | `/monitor/history`      | Histórico con filtros                   |
| **AlertsScreen**         | `/monitor/alerts`       | Panel de alertas                        |
| **SettingsScreen**       | `/monitor/settings`     | Configuración rápida                    |

### Componentes compartidos

| Componente        | Propósito                                              |
| ----------------- | ------------------------------------------------------ |
| **TrendBars**     | Barras de trend 24h (0-23hs, colores día/noche/alarma) |
| **SocRing**       | Anillo de State of Charge                              |
| **StatusBadge**   | Indicador de estado (normal/alarma)                    |
| **CellGrid**      | Grid de celdas BMS                                     |
| **MetricSection** | Sección de métricas con valores                        |
| **GatewayCard**   | Card de gateway con estado                             |
| **AreaChart**     | Gráfico de área para time-series                       |

### Tema

- MUI v5 con paleta industrial (oscuro/claro)
- Tema oscuro por defecto (almacenado en localStorage `app-theme-mode`)
- CSS variables para colores: `--tx`, `--bg`, `--bl`, `--gr`, `--am`, `--rd`
- Soporte de tema separado en `ThemeContext.tsx`

---

## 9. Conclusiones y Recomendaciones

### Puntos fuertes

- ✅ **Arquitectura sólida**: Separación clara de responsabilidades, polling desacoplado por gateway
- ✅ **Escalable**: Particionamiento PostgreSQL para 5M+ filas/día
- ✅ **Tiempo real**: WebSocket STOMP con auto-reconnect y live data
- ✅ **Multi-marca**: Sistema de mapeo configurable por marca (Felicity + MUST)
- ✅ **Optimizaciones**: Índices compuestos, queries agregadas, trends agrupados
- ✅ **Auto-mantenimiento**: Purge schedule nocturno, rediscovery periódico
- ✅ **Monitoreable**: Métricas de latencia, estados de conexión, logs estructurados

### A mejorar / próximos pasos

- ⏳ **Auto-creación de particiones**: Las particiones mensuales deberían crearse automáticamente (pg_partman o scheduler)
- ⏳ **DROP PARTITION en purge**: En vez de DELETE, usar DROP PARTITION para meses expirados (mucho más rápido)
- ⏳ **Caché de segundo nivel**: Para Marca, MapeoRegistro y datos de catálogo que cambian poco
- ⏳ **Rate limiting** en endpoints REST exportables (CSV)
- ⏳ **Métricas Prometheus** para monitoreo del propio sistema (tasas de polling, latencias, errores)
- ⏳ **Alertas avanzadas**: Media móvil, detección de anomalías, comparación entre inversores del mismo grupo
