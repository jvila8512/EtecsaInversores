# PRD Técnico v3.0 — Solar Inverter Monitor

## Diseño UI/UX Completo — Multi-Master · Dark/Light · Tiempo Real

---

## 1. Identidad Visual — Industrial Precision

### 1.1 Filosofía de diseño

Sistema de monitoreo crítico con estética industrial. Legibilidad de datos numéricos como prioridad absoluta. Sin decoración superflua. Cada píxel comunica un dato.

### 1.2 Tema dual — Dark (default) / Light

El toggle ☀/🌙 en la topbar conmuta entre ambos modos sin recargar.

```css
/* ─── DARK MODE (default) ─── */
:root {
  --bg: #0a0f0d; /* fondo principal */
  --bg2: #111814; /* topbar, bottomnav, modales */
  --bg3: #1a2119; /* botones ghost, chips, inputs */
  --card: #141c17; /* tarjetas, paneles */
  --bdr: #1f2d24; /* bordes principales */
  --bdr2: #2a3d30; /* bordes secundarios, hover */
  --tx: #e8f0ea; /* texto principal */
  --tx2: #7a9882; /* texto secundario, labels */
  --tx3: #4a6454; /* texto terciario, section labels */
}

/* ─── LIGHT MODE ─── */
[data-light] {
  --bg: #f0f4f1;
  --bg2: #e8ece9;
  --bg3: #dce4de;
  --card: #ffffff;
  --bdr: #ccd8d0;
  --bdr2: #b3c4bc;
  --tx: #0d1a11;
  --tx2: #3d6b4f;
  --tx3: #6b9278;
}

/* ─── SEMÁNTICOS (iguales en ambos modos) ─── */
--bl: #2563eb; /* azul industrial — acciones, master, datos PV */
--bl2: #1d4ed8; /* azul hover */
--gr: #22c55e; /* verde — normal, cargando, SOC alto */
--am: #f59e0b; /* ámbar — consumo, advertencia, SOC medio */
--rd: #ef4444; /* rojo — alarma crítica, temp alta, SOC bajo */
```

### 1.3 Tipografía

| Familia       | Uso                                                       | Pesos              |
| ------------- | --------------------------------------------------------- | ------------------ |
| IBM Plex Sans | Interfaz general, labels, botones                         | 400, 500, 600, 700 |
| IBM Plex Mono | Valores numéricos, IPs, timestamps, IDs, registros Modbus | 400, 500           |

Escala de tamaños:

- **21px** — KPI principal (Generación, Consumo, Potencia)
- **18px** — Métricas de detalle (batería, PV, AC)
- **15px** — Nombre del inversor en detalle
- **13px** — Título de sección en topbar
- **12px** — Nombre de inversor en tarjeta lista
- **11px** — Mensaje de alerta
- **10px** — Botones, tabs, labels de config, toggle labels
- **9px** — Section labels uppercase, badges, live indicator
- **8px** — Sub-labels de KPI, columnas de tabla, leyenda
- **7px** — Celdas BMS, tooltips

### 1.4 Formas y espaciado

- **Border radius:** 4px en todos los componentes
- **Borde tarjeta:** `1px solid var(--bdr)`
- **Borde tarjeta en alarma:** `1px solid var(--rd)` + `box-shadow: 0 0 0 1px #ef444420`
- **Padding tarjeta:** 11px
- **Gap entre tarjetas:** 8px
- **Padding lateral del scroll principal:** 10px
- **Letter-spacing labels:** 0.9px uppercase
- **Transiciones:** 150ms para hover, 180ms para toggles

---

## 2. Estructura de Navegación

### 2.1 Top App Bar

```
┌─────────────────────────────────────────────────────────┐
│  DASHBOARD            [●LIVE] [☀] [🔔●] [JL]           │
└─────────────────────────────────────────────────────────┘
```

**Componentes:**

| Elemento       | Descripción                                                                                 |
| -------------- | ------------------------------------------------------------------------------------------- |
| Título         | Nombre de la pantalla activa en mayúsculas. Fuente 13px, weight 600                         |
| Indicador LIVE | Punto verde animado (`pulse`) + texto "LIVE" en Mono. Rojo cuando WebSocket pierde conexión |
| Toggle tema    | Botón 28×28px, border-radius 4px. Ícono ☀ en dark, 🌙 en light                             |
| Notificaciones | Badge rojo cuando hay alertas sin resolver                                                  |
| Avatar         | Círculo azul 27px con iniciales. Abre perfil                                                |

**Animación LIVE:**

```css
@keyframes pulse {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.25;
  }
}
/* duración: 1.4s infinite */
```

### 2.2 Bottom Navigation Bar

Altura 62px, fija en la parte inferior.

| #   | Ícono SVG     | Label     | Pantalla              |
| --- | ------------- | --------- | --------------------- |
| 1   | Grid 2×2      | DASH      | Dashboard global      |
| 2   | Documento     | INVERTERS | Detalle de inversor   |
| 3   | ECG/actividad | HISTORY   | Análisis histórico    |
| 4   | Campana       | ALERTS    | Eventos críticos      |
| 5   | Monitor       | SETTINGS  | Dispositivos y config |

**Estados:**

- Inactivo: color `var(--tx3)`, sin sombra
- Activo: color `var(--bl)`, punto 3px debajo del ícono, `filter: drop-shadow(0 0 5px #2563eb88)`
- Badge rojo (ALERTS): cuando hay alertas críticas activas

---

## 3. Pantallas — Especificación Completa

---

### 3.1 Dashboard Global

#### KPIs (grid 3 columnas)

```
┌─────────────────┬─────────────────┬─────────────────┐
│  24.7           │  18.3           │  38.4           │
│  kWh            │  kWh            │  kW             │
│  GENERACIÓN     │  CONSUMO        │  POTENCIA       │
│  [azul]         │  [ámbar]        │  [verde]        │
└─────────────────┴─────────────────┴─────────────────┘
```

- **Generación** (azul `--bl`): Energía PV generada hoy en kWh — de `DatoEficiencia.energiaPvWh`
- **Consumo** (ámbar `--am`): Energía entregada a carga en kWh — de `DatoEficiencia.energiaCargaWh`
- **Potencia** (verde `--gr`): Suma de `pvPotenciaW` de todos los inversores activos — actualización en tiempo real vía WebSocket cada 2s

#### Gráfica Aggregate Trend 24h

Barras verticales flex. Altura del área: 68px.

- Barras en horario nocturno (0:00–5:00, 20:00–23:00): `var(--bg3)` — sin producción
- Barras en horario solar: degradado azul `var(--bl)`
- **Barra con alarma activa**: rojo `var(--rd)` — marca la hora del evento
- Tooltip al hover: `HH:00 → X.X kW`
- Eje X: `00:00 · 06:00 · 12:00 · 18:00 · 23:59`

#### Lista de inversores agrupados por gateway

Los inversores se organizan en grupos definidos por su `gateway_tcp`. Nunca en lista plana.

**Cabecera de grupo:**

```
● GRUPO A · GW-01 Techo Norte   192.168.1.10:502   [2 alarmas]
```

- Punto verde (estado del gateway)
- Nombre del grupo + nombre del gateway
- IP:Puerto en Mono azul
- Badge rojo con contador si hay alarmas en el grupo

**Tarjeta de inversor:**

```
┌────────────────────────────────────────────────────────┐
│ [SOC Ring]  INV-A3 · SLAVE 3                  2.90 kW  │
│             Felicity-A3                       Power Out │
│             [⚠ ALARM] [71%]                   67°C     │
│                                               Temp      │
└────────────────────────────────────────────────────────┘
```

**SOC Ring (38×38px):**

- Círculo SVG con trazo proporcional al porcentaje
- Verde > 60%, Ámbar 30–60%, Rojo < 30% o en alarma
- Valor SOC centrado en Mono 9px

**Eficiencia junto al badge:**

- Verde si eficiencia > 80%
- Ámbar si 70–80%
- Rojo si < 70%

**Métricas derechas:**

- Potencia en kW (rojo si en alarma)
- Temperatura °C (rojo si > 60°C)

**Borde de tarjeta:**

- Normal: `1px solid var(--bdr)`
- En alarma: `1px solid var(--rd)` + glow rojo sutil

**Pie de grupo — totalizador:**

```
GRUPO A TOTAL                              19.90 kW
```

Suma de potencia de los inversores del grupo en tiempo real.

---

### 3.2 Detalle de Inversor

Se activa tocando cualquier tarjeta del dashboard.

#### Header de estado

```
┌─────────────────────────────────────────────────────────┐
│ [● PULSE] E05 HIGH TEMP — ALARM          ← solo si alarm│
│                                                         │
│ Felicity-A3                                             │
│ SLAVE_ID: 03 · 192.168.1.10:502                         │
│ GRUPO A · ★ MASTER · Felicity 5048 · 5kVA/48V          │
│                                                         │
│ [EDITAR]  [SILENCIAR ← solo alarm]  [← VOLVER]         │
└─────────────────────────────────────────────────────────┘
```

- Fondo alarma: `background: #ef444406`
- Borde alarma: `1px solid var(--rd)`
- Subtítulo incluye: slave_id (zero-padded), IP:puerto, grupo, rol (★ MASTER / esclavo), marca, modelo y capacidad
- Botón SILENCIAR solo visible cuando `alarm = true`

#### Battery Storage

```
VOLTAJE        CORRIENTE              SOC
48.3           +12.4                  78
V              A  (cargando)          %
```

- Corriente positiva = cargando → texto verde
- Corriente negativa = descargando → texto blanco
- Voltaje en blanco (rojo si < umbral configurado)

#### PV Array Input

```
POTENCIA       VOLTAJE        CORRIENTE
3840           365            10.5
W              V              A
```

- Potencia en azul `--bl`

#### AC Load Output

```
POTENCIA       VOLTAJE        FRECUENCIA
2.9            220            50.0
kW             V              Hz
```

#### Pestañas de análisis gráfico

```
[ Energy ]  [ Voltages ]  [ Temperature ]
```

Cada tab muestra un gráfico de área SVG doble con leyenda dinámica.

| Tab         | Línea 1                       | Línea 2                         | Leyenda                              |
| ----------- | ----------------------------- | ------------------------------- | ------------------------------------ |
| Energy      | Azul sólida: Generación PV    | Verde discontinua: Consumo AC   | Generación PV · Consumo AC           |
| Voltages    | Ámbar sólida: Voltaje batería | Azul discontinua: Voltaje AC    | Voltaje Batería (V) · Voltaje AC (V) |
| Temperature | Rojo sólida: Temp MOSFET      | Ámbar discontinua: Temp interna | Temp MOSFET (°C) · Temp Interna (°C) |

- Línea discontinua: `stroke-dasharray: 3 2`
- Gradiente de área: 30% opacidad arriba → 2% abajo
- **Punto de anomalía** (si temp > 65°C o pico bajo): círculo rojo 4.5px + anillo 9px semi-transparente + etiqueta "anomalía" en Mono rojo
- Eje X: 5 puntos horarios
- Altura del área: 62px

#### Quick Config — Registros Modbus 0x211F–0x2159

Sliders para configuración del hardware del inversor (zona de escritura Modbus):

| Slider              | Registro | Rango   | Default |
| ------------------- | -------- | ------- | ------- |
| Voltaje corte bajo  | 0x211F   | 40–48V  | 42V     |
| Voltaje absorción   | 0x2122   | 54–58V  | 57V     |
| Voltaje flotación   | 0x2123   | 52–56V  | 54V     |
| Corriente máx carga | 0x212E   | 10–100A | 60A     |

Estilo slider:

```css
input[type='range'] {
  height: 3px;
  accent-color: var(--bl);
}
/* thumb: 12px circular azul, box-shadow: 0 0 0 2px #2563eb30 */
```

Toggles después de separador:

| Toggle            | Registro | Default |
| ----------------- | -------- | ------- |
| Prioridad PV      | 0x212A   | ON      |
| Carga inteligente | —        | ON      |
| Modo ECO          | —        | OFF     |
| Batería LiFePO4   | 0x212D   | ON      |

#### BMS Summary

**Métricas superiores (2 filas de 3 columnas):**

Fila 1:

- TEMP MOSFET: rojo si > 65°C, ámbar si > 50°C, blanco si normal
- DELTA CELDAS: ámbar si > 30mV, blanco si normal
- CICLOS USO: blanco

Fila 2:

- PACK VOLTAJE
- SOC BMS: verde
- CHARGE MOS: verde "ON" / rojo "OFF"

**Grid de 16 celdas (8 col × 2 filas):**

```
[3.32][3.33][3.31][3.34][3.30][3.28][3.35][3.33]
[3.31][3.32][3.29][3.34][3.36][3.30][3.32][3.33]
```

Colores según desbalance `|V_celda - V_promedio|`:

| Condición             | Fondo       | Texto       | Borde       |
| --------------------- | ----------- | ----------- | ----------- |
| < 15mV (normal)       | `#22c55e12` | `var(--gr)` | `#22c55e28` |
| 15–30mV (advertencia) | `#f59e0b12` | `var(--am)` | `#f59e0b28` |
| > 30mV (crítico)      | `#ef444412` | `var(--rd)` | `#ef444440` |

Altura celda: 24px, font-size: 7px Mono  
Tooltip al hover: `C{N}: {V.VVV}V`

Leyenda de colores debajo del grid:

```
■ Normal (<15mV)   ■ Advertencia (15-30mV)   ■ Crítico (>30mV)
```

---

### 3.3 Historial y Análisis

#### Filtros

**Fila 1 — Por grupo:**

```
[Todos ✓]  [Grupo A]  [Grupo B]  [Solo]
```

**Fila 2 — Por variable:**

```
[Voltaje ✓]  [Potencia ✓]  [Temp]  [SOC]  [Frecuencia]
```

**Selectores de fecha (2 columnas):**

```
[📅 2026-05-20]     [📅 2026-05-20]
   Fecha desde           Fecha hasta
```

Chips — estado activo: `border-color: var(--bl)`, `color: var(--bl)`, `background: #2563eb15`

#### Gráfica Análisis Temporal Multiserie

Altura: 78px. Líneas SVG puras.

- **Línea azul sólida** (1.8px): Potencia PV en kW
- **Línea verde discontinua** (1.5px, `dasharray 3 2`): SOC batería en %
- **Punto anomalía**: círculo rojo 4.5px + anillo 9px 40% opacidad + texto "anomalía" en Mono rojo
- Eje X: 5 timestamps del rango seleccionado

Leyenda:

```
─── PV kW    ╌╌╌ SOC %    ● Anomalía
```

#### Log de Datos

Tabla Mono 10px con 6 columnas:

| TIEMPO   | INV | BAT V    | PV W | TEMP   | SOC     |
| -------- | --- | -------- | ---- | ------ | ------- |
| 14:32:11 | A3  | **47.2** | 2890 | **67** | **65%** |
| 14:30:05 | A1  | 48.3     | 3840 | 41     | 82%     |

- Valores fuera de rango: `color: var(--rd)`, `font-weight: 700`
- Timestamp: color `var(--tx3)`

Umbrales de alarma por defecto:

- BAT V < 47.5 → rojo
- TEMP > 60°C → rojo
- SOC < 70% → rojo

#### Exportación

```
[        ⬇ EXPORT CSV        ]
```

Ancho completo, azul, 11px uppercase bold.

---

### 3.4 Alertas

Cabecera con filtros rápidos:

```
EVENTOS CRÍTICOS            [Activas ✓]  [Resueltas]
```

**Ítem de alerta:**

```
[●]  E05 HIGH TEMP — Felicity-A3          [CRÍTICA]
     Temperatura MOSFET: 67°C (umbral: 65°C)
     14:32:11 · INV-A3
```

| Severidad   | Color punto | Borde tarjeta          | Badge           |
| ----------- | ----------- | ---------------------- | --------------- |
| Crítica     | `var(--rd)` | `1px solid var(--rd)`  | Fondo rojo 20%  |
| Advertencia | `var(--am)` | `1px solid var(--am)`  | Fondo ámbar 20% |
| Normal/OK   | `var(--gr)` | `1px solid var(--bdr)` | Fondo verde 20% |

Cada ítem contiene:

1. Punto de color
2. Mensaje principal (11px, weight 500)
3. Detalle técnico (9px, `var(--tx2)`)
4. Timestamp · ID del inversor (9px Mono, `var(--tx3)`)

---

### 3.5 Gestión de Dispositivos / Settings

#### Métricas de infraestructura (3 columnas)

```
┌─────────────────┬─────────────────┬─────────────────┐
│  10             │  76%            │  34ms           │
│  inversores     │  capacidad      │  promedio       │
│  TOTAL UNITS    │  SYSTEM LOAD    │  GW LATENCY     │
└─────────────────┴─────────────────┴─────────────────┘
```

- Total Units: azul `--bl`
- System Load: verde si < 80%, ámbar si 80–95%, rojo si > 95%
- GW Latency: verde si ≤ 20ms, ámbar si > 20ms

#### Sección Gateways TCP/IP activos (NUEVO)

Una tarjeta por gateway, con métricas individuales:

```
┌─────────────────────────────────────────────────────┐
│ GW-01 Techo Norte                                ●  │
│ 192.168.1.10:502                                    │
│                                                     │
│    5           28ms          76%                    │
│ inversores    latencia       carga                  │
└─────────────────────────────────────────────────────┘
```

- Punto verde si conectado
- Latencia en ámbar si > 30ms
- Carga en ámbar si > 80%

#### Tabla Bus de Dispositivos

| NOMBRE        | ROL                   | TIPO     | IP / SID             |
| ------------- | --------------------- | -------- | -------------------- |
| Felicity-A1 ★ | `Master` (badge azul) | `TCP/IP` | 192.168.1.10 / SID:1 |
| Felicity-A2   | esclavo               | `TCP/IP` | 192.168.1.10 / SID:2 |
| Felicity-B1 ★ | `Master` (badge azul) | `TCP/IP` | 192.168.1.20 / SID:1 |

- ★ junto al nombre del master
- IP + SID en líneas separadas, Mono 9px

#### FAB — Botón flotante (+)

Solo visible en la pantalla Settings.

```css
.fab {
  position: fixed;
  bottom: 74px;
  right: 14px;
  width: 46px;
  height: 46px;
  border-radius: 50%;
  background: var(--bl);
  box-shadow: 0 4px 16px #2563eb60;
  font-size: 24px;
  color: #fff;
}
```

Al tocarlo: abre el **modal de autodescubrimiento**.

#### Modal — Registrar nuevo inversor

```
╔═══════════════════════════════════════════╗
║  Registrar Nuevo Inversor                 ║
║  El sistema detecta marca, modelo y       ║
║  serie automáticamente                    ║
║                                           ║
║  Nombre                                   ║
║  [________________________]               ║
║                                           ║
║  Tipo de conexión                         ║
║  [TCP/IP Modbus          ▼]               ║
║                                           ║
║  IP / Puerto              Slave ID        ║
║  [192.168.1.10:502]       [1    ]         ║
║                                           ║
║  Master de referencia (opcional)          ║
║  [— Inversor independiente —      ▼]      ║
║                                           ║
║  [🔍 DESCUBRIR Y REGISTRAR]  [CANCELAR]  ║
╚═══════════════════════════════════════════╝
```

El modal llama a `POST /api/inversores/register-auto` con:

```json
{
  "nombre": "Felicity-A6",
  "tipoConexion": "TcpModbus",
  "puerto": "192.168.1.10:502",
  "slaveId": 6,
  "masterReferenciaId": 1
}
```

El backend lee registros 0x2000–0x200F y auto-completa marca, modelo, serie, capacidad y voltaje.

---

## 4. Comportamiento de Alarmas

### 4.1 Propagación visual en cascada

Cuando `estadoDispositivo = Error` en un inversor:

```
1. Tarjeta en dashboard   → border-color: var(--rd) + glow
2. Badge de estado        → cambia de "● NORMAL" a "⚠ ALARM"
3. Eficiencia             → color rojo
4. Temperatura (si > 60)  → color rojo
5. Cabecera de grupo      → badge contador de alarmas
6. Barra hora del evento  → color rojo en trend chart
7. Tab ALERTS             → badge rojo en nav
8. Header de detalle      → banda roja + tag pulsante
9. BMS celdas afectadas   → borde y texto rojo
```

### 4.2 Códigos de error Felicity

| Código | Descripción             |
| ------ | ----------------------- |
| E01    | Over temperature        |
| E02    | Over voltage battery    |
| E03    | Under voltage battery   |
| E04    | Short circuit           |
| E05    | High temperature MOSFET |
| E06    | Fan failure             |
| 0      | Sin fallo               |

### 4.3 Indicador LIVE (WebSocket)

| Estado       | Visual                                    |
| ------------ | ----------------------------------------- |
| Conectado    | `● LIVE` verde, animación pulse 1.4s      |
| Reconectando | `● LIVE` ámbar, animación más rápida 0.6s |
| Desconectado | `● OFF` rojo, sin animación               |

Reconexión automática cada 5 segundos.

---

## 5. Datos en Tiempo Real — Fuentes por pantalla

| Pantalla              | REST (carga inicial)                                    | WebSocket (actualizaciones)                   |
| --------------------- | ------------------------------------------------------- | --------------------------------------------- |
| Dashboard KPIs        | `GET /api/dato-eficiencias?fecha=hoy`                   | `/topic/lectura/inversor/{id}` × N inversores |
| Dashboard trend       | `GET /api/lecturas-inversores?desde=00:00`              | —                                             |
| Dashboard lista       | `GET /api/inversores?activo=true`                       | Mismo que KPIs                                |
| Detalle Battery/PV/AC | Última `LecturaInversor` del inversor                   | `/topic/lectura/inversor/{id}`                |
| Detalle gráficos      | `GET /api/lecturas-inversores?inversorId=X&desde=06:00` | —                                             |
| History chart/tabla   | `GET /api/lecturas-inversores?desde=...&hasta=...`      | —                                             |
| Alertas               | `GET /api/evento-alertas?sort=timestamp,desc&size=50`   | `/topic/alertas`                              |
| Settings gateways     | `GET /api/gateway-tcps?activo=true`                     | —                                             |
| Settings tabla        | `GET /api/inversores?size=200`                          | —                                             |

### Agrupación de inversores para el dashboard

```typescript
const grupos = gateways.map(gw => ({
  gateway:      gw,
  master:       inversores.find(i => i.esMaster && i.gateway.id === gw.id),
  esclavos:     inversores.filter(i => !i.esMaster && i.gateway.id === gw.id),
  totalPotencia: lecturas de ese grupo sumadas en tiempo real
}));

const independientes = inversores.filter(
  i => !i.esMaster && !i.masterReferencia
);
```

---

## 6. Componentes Reutilizables

### SocRing

```
Props: soc (0-100), alarm (bool), size (default 38)
Implementación: SVG con 2 círculos concéntricos
Colores: verde >60%, ámbar 30-60%, rojo <30% o alarm=true
Valor: IBM Plex Mono 9px centrado en el círculo
```

### TrendBars

```
Props: values (number[] × 24), alarmIndex? (number)
Barras flex, altura relativa al máximo
Color: azul en horario solar, bg3 en nocturno, rojo en alarmIndex
```

### AreaChart (SVG doble)

```
Props: data1, data2, color1, color2, height
Dos líneas con gradiente de área
Línea 2 siempre discontinua (stroke-dasharray: 3 2)
Gradiente: 30% arriba → 2% abajo
AnomalyMarker opcional: círculo 4.5px + anillo 9px + label
```

### MetricSection

```
Props: title, items[]{ label, value, unit, color? }
Grid 3 columnas
Título uppercase con línea divisora CSS ::after
Valores IBM Plex Mono 18px
```

### StatusBadge

```
'★ MASTER'  → bg #2563eb18, color var(--bl)
'● NORMAL'  → bg #22c55e18, color var(--gr)
'⚠ ALARM'   → bg #ef444418, color var(--rd)
'◌ OFFLINE' → bg var(--bg3), color var(--tx3)
```

### CellGrid (BMS)

```
Props: voltagesMv (number[] × 16)
Grid 8×2, altura 24px por celda
Colores según desviación del promedio:
  < 15mV → ok (verde)
  15-30mV → warn (ámbar)
  > 30mV → crit (rojo)
```

### GatewayCard

```
Props: gateway{ nombre, ip, puerto, latenciaMs, loadPct, numInversores }
Tarjeta con 3 métricas: inversores, latencia, carga
Punto verde/rojo según estado de conexión
```

---

## 7. Tokens de Animación

| Elemento                | Keyframe                 | Duración      | Trigger         |
| ----------------------- | ------------------------ | ------------- | --------------- |
| Punto LIVE activo       | `pulse` opacity 1→0.25→1 | 1.4s infinite | Siempre         |
| Punto LIVE reconectando | `pulse`                  | 0.6s infinite | WS reconectando |
| Pulse alarma en header  | `pulse` scale+opacity    | 1.0s infinite | alarm=true      |
| Toggle ON/OFF           | `translateX(14px)`       | 180ms         | Click           |
| Hover tarjeta inversor  | border-color, background | 150ms         | :hover          |
| Hover celda BMS         | `scale(1.05)`            | 100ms         | :hover          |
| FAB hover               | `scale(1.08)`            | 150ms         | :hover          |

---

## 8. Guía de Implementación React/JHipster

### Estructura de archivos

```
src/main/webapp/app/modules/monitor/
├── SolarMonitorApp.tsx        ← componente raíz, tema, navegación
├── types.ts                   ← interfaces TypeScript
├── theme.ts                   ← applyTheme(), variables CSS
│
├── hooks/
│   ├── useLiveData.ts         ← WebSocket STOMP, lecturas en tiempo real
│   └── useInversores.ts       ← fetch inversores, gateways, alertas
│
├── components/
│   ├── SocRing.tsx
│   ├── TrendBars.tsx
│   ├── AreaChart.tsx
│   ├── MetricSection.tsx
│   ├── StatusBadge.tsx
│   ├── CellGrid.tsx
│   └── GatewayCard.tsx
│
└── screens/
    ├── DashboardScreen.tsx
    ├── InversorDetailScreen.tsx
    ├── HistoryScreen.tsx
    ├── AlertsScreen.tsx
    └── SettingsScreen.tsx
```

### Integración en app.tsx

```tsx
import SolarMonitorApp from './modules/monitor/SolarMonitorApp';

// En el router de JHipster:
<Route path="/monitor" component={SolarMonitorApp} />;
```

### WebSocket STOMP

```typescript
const client = new Client({
  webSocketFactory: () => new SockJS('/ws/monitor'),
  connectHeaders: { Authorization: `Bearer ${token}` },
  onConnect: () => {
    // Suscribir a cada inversor activo
    inversores.forEach(inv => {
      client.subscribe(`/topic/lectura/inversor/${inv.id}`, msg => updateLectura(JSON.parse(msg.body)));
    });
    // Suscribir a alertas globales
    client.subscribe('/topic/alertas', msg => addAlerta(JSON.parse(msg.body)));
  },
  reconnectDelay: 5000,
});
```

### Aplicación del tema dual

```typescript
export function applyTheme(mode: 'dark' | 'light') {
  const app = document.getElementById('app');
  mode === 'light' ? app.setAttribute('data-light', '') : app.removeAttribute('data-light');
}
```

---

## 9. Flujo de Navegación Completo

```
                    ┌─── DASHBOARD ───┐
                    │                 │
        ┌───────────┤  KPIs en vivo   ├───────────┐
        │           │  Trend 24h      │           │
        │           │  Grupos A, B    │           │
        │           └────────┬────────┘           │
        │                    │ tap tarjeta         │
        ▼                    ▼                     ▼
   HISTORY              DETALLE INV           ALERTS
   Filtros               Battery              Lista eventos
   Chart multi           PV / AC              Filtros
   Log tabla             Tabs chart           Resolución
   Export CSV            Quick Config
                         BMS cells
                              │
                              │ ← VOLVER
                              ▼
                         DASHBOARD

                         SETTINGS
                         Infraestructura
                         Gateways
                         Tabla bus
                         FAB (+) → Modal autodescubrimiento
                                    └→ POST /api/inversores/register-auto
                                       └→ Dashboard con nuevo inversor
```

---

_PRD v3.0 — Sistema Solar Monitor · Arquitectura Multi-Master Modbus TCP/IP_
_Paleta: Industrial Precision · Tipografía: IBM Plex · Tema: Dark/Light_
