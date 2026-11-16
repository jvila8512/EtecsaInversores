# Plan de integración: inversor Felicity IVGM-8KLP1G1 al mapa Modbus

> Análisis realizado sobre `docs/IVGM8k/` (IVGM8K_CONEXION_MODBUS.md, ivgm8k_regs.json con 322 registros,
> ivgm8k_table.md) contrastado contra el modelo de datos real de la app y la base de datos viva.

## 1. Qué es

- **Modelo**: Felicity IVGM-8KLP1G1 (8 kVA, 48 V, híbrido, **sin fase C, sin PV3/PV4, sin batería 2**).
- **Bus**: RS485 en cadena (daisy chain) con adaptador TCP→RTU. Modbus RTU, holding registers, FC 0x03, 8N1.
- **Baudrate: 9600 bps** (la marca Felicity Lumin usa 2400 → no sirve el mismo gateway).
- **3 inversores en paralelo**, cada uno es un servidor Modbus independiente con su **slave_id propio** (1, 2, 3).
  El master NO agrega datos de los esclavos.
- Sin fase C: solo se leen los registros de fase A.

## 2. Hallazgos que definen el approach

### 2.1 La app DIVIDE por `escala` ⇒ las potencias en kW exigen escala fraccionaria

`ModbusTcpClient` línea 172: `finalValue = (escala != null && escala != 0.0) ? rawValue / escala : rawValue`.
La escala es un `Double` y el formulario la parsea con `parseFloat` (acepta decimales).

El IVGM8k reporta potencia de telemetría (dir < 0x2000) en **kW con 0.01 kW/cuenta**:
`kW = raw/100` → para guardar en W: `W = raw*10` → **escala = 0.1** (dividir por 0.1 multiplica por 10).
Intuitivamente se leería "escala 100", pero con esta fórmula 0.1 es lo correcto. Es contraintuitivo: documentarlo.

### 2.2 El bloque 0x1100 del IVGM DIVERGE del Lumin ⇒ necesita MARCA propia

Comparación 0x1100-0x1118 (ambas marcas comparten direcciones pero NO significado):

| Dir    | Felicity Lumin (marca 1) | Felicity IVGM (nueva)            |
| ------ | ------------------------ | -------------------------------- |
| 0x1101 | Working Mode             | State1                           |
| 0x1102 | Battery Charge Stage     | State2                           |
| 0x1103 | Fault Code               | State3                           |
| 0x1104 | Power Flow Bits          | State4                           |
| 0x1108 | Battery Voltage          | Time: Year-Month                 |
| 0x1118 | PV1 Voltage              | PV1 Voltage (única coincidencia) |

→ Crear una **marca nueva** (mismo fabricante, layout/protocolo distinto). `mapeo_registro` está indexado por
`marca_id`, así que la app lo soporta sin tocar código. El doc lo exige: _"una dirección IVGM debe venir del
documento IVGM, nunca prestada del T-REX por parecerse"_.

### 2.3 El baudrate NO lo aplica la app

`marca.baudRateDefault` existe en el dominio y en el DTO pero **ningún cliente Modbus lo lee**: el campo es
documentación. El 9600 hay que configurarlo en el **gateway TCP→RS485**. Ojo: los gateways actuales
(192.168.1.10 / .20) están configurados para 2400 (Lumin) → o se reconfiguran o hace falta un gateway nuevo.

### 2.4 El JSON de docs está incompleto y con mojibake

- Faltan los 4 registros de identidad/paralelo que sí están validados en campo:
  **8512 (0x2140) Parallel Count = 3, 8513 (0x2141) rol = 1, 8514 (0x2142) Modbus ID = 1, 8515 (0x2143) Rated Power = 8000 W**.
- El JSON trae mojibake en las unidades: `┬░C` (debe ser °C) y `m╬®` (debe ser mΩ).
- El equipo expone **988 registros**, el JSON modela 322. El CSV `registros_192-168-1-10_20261111_102040.csv`
  tiene el volcado real completo.
- `0xFFFE` (65534) = "no configurado / no disponible" (todo el bloque 0x230C-0x2347) → tratar como N/A.
- Corrientes y temperaturas: tratar como **int16 con signo** (`raw > 32767 ? raw - 65536 : raw`).

## 3. Traducción a las columnas que la app ya lee

Escalas confirmadas con lecturas reales de campo (§5 del doc).

| campo_sql            | dir  | hex    | registro IVGM         | escala  | unidad | validado en campo     |
| -------------------- | ---- | ------ | --------------------- | ------- | ------ | --------------------- |
| modo_operacion       | 4352 | 0x1100 | work_mode_status      | 1       | -      | -                     |
| codigo_fallo         | 4362 | 0x110A | fault_codes           | 1       | -      | -                     |
| red_activa           | 4496 | 0x1190 | power_flow_msg        | 1       | -      | -                     |
| pv_voltaje_v         | 4376 | 0x1118 | pv1_voltage           | 10      | V      | raw 3102 → 310.2 V    |
| pv_corriente_a       | 4380 | 0x111C | pv1_current           | 10      | A      | raw 108 → 10.8 A      |
| pv_potencia_w        | 4392 | 0x1128 | pv1_power             | **0.1** | kW→W   | raw 3368 → 33680 W    |
| bateria_voltaje_v    | 4396 | 0x112C | bat1_voltage          | 10      | V      | raw 539 → 53.9 V      |
| bateria_corriente_a  | 4400 | 0x1130 | bat1_current          | 10      | A      | raw 3262 → 32.62 A    |
| bateria_potencia_w   | 4401 | 0x1131 | bat1_power            | **0.1** | kW→W   | raw 3262 → 32620 W    |
| soc_pct              | 4408 | 0x1138 | bat1_soc              | 10      | %      | raw 460 → 46.0 %      |
| carga_porcentaje     | 4479 | 0x117F | load_a_persent        | 1       | %      | -                     |
| salida_voltaje_v     | 4435 | 0x1153 | inverter_a_voltage    | 10      | V      | -                     |
| salida_corriente_a   | 4447 | 0x115F | inverter_a_current    | 10      | A      | -                     |
| salida_potencia_w    | 4470 | 0x1176 | inverter_aphase_power | **0.1** | kW→W   | -                     |
| salida_frecuencia_hz | 4462 | 0x116E | inverter_frequency    | 100     | Hz     | -                     |
| red_voltaje_v        | 4432 | 0x1150 | grid_a_voltage        | 10      | V      | -                     |
| red_frecuencia_hz    | 4461 | 0x116D | grid_frequency        | 100     | Hz     | -                     |
| consumo_red_w        | 4467 | 0x1173 | grid_a_phase_power    | **0.1** | kW→W   | -                     |
| temperatura_c        | 4421 | 0x1145 | inverter_temperature  | 10      | °C     | -                     |
| ac_output_freq       | 4461 | 0x116D | grid_frequency        | 100     | Hz     | (duplicado, opcional) |

### Columnas SIN equivalente en el IVGM8k

| campo_sql          | por qué                                                                |
| ------------------ | ---------------------------------------------------------------------- |
| salida_aparente_va | el IVGM8k no expone potencia aparente                                  |
| eficiencia_pct     | no expone eficiencia                                                   |
| bus_voltaje_v      | no expone tensión de bus                                               |
| pv_activo          | no hay registro de estado de PV (solo corriente)                       |
| bateria_cargando   | el equivalente es state2/state3, semántica a confirmar con la pantalla |

→ Esas quedan **sin mapear** para esta marca. Consecuencia: el `dato_inversor` de un IVGM8k tendrá esas
columnas en NULL. Hay que decidir si el dashboard las tolera (hoy la app ya usa `soc_pct` y `carga_porcentaje`
como campos distintos, pero el anillo SOC del dashboard se alimenta de `cargaPorcentaje` — que en Lumin es
LOAD% y en MUST es System Load% → **el anillo muestra carga, no SOC**: semántica preexistente a revisar).

## 4. Topología: ya está implementada

- `POST /api/inversors/{masterId}/esclavos` crea el esclavo heredando marca, modelo, gateway, sitio,
  tipoConexion y periodoMuestreoSeg. Solo pide nombre + slaveId.
- El polling consulta cada esclavo cambiando el `unitId` Modbus.
- `TipoConexion` ya incluye `SerialRs485` y `TcpModbus`.
- `Inversor.esMaster` + `slaveId` + `masterReferencia` modelan la cadena paralela.

## 5. Plan de ejecución

1. **Liquibase nuevo** `20260615000001_seed_felicity_ivgm.xml`:
   - INSERT marca (nombre `Felicity IVGM`, fabricante `Felicity Solar`, 4352-8525, baud 9600)
   - INSERT modelo_inversor (`IVGM-8KLP1G1`, capacidad_va 8000, voltaje 48)
   - INSERT ~20 `mapeo_registro` con la tabla de arriba (id nuevo, marca_id nueva, `ON CONFLICT (id) DO UPDATE`
     sobre la fila completa)
   - Sin escrituras a registros de configuración (el doc lo marca como alto riesgo).
2. **Test de las escalas** con los valores reales de campo (539 → 53.9, 460 → 46.0, 3262 → 32620 W, 3102 → 310.2).
3. **Alta en la app**: master IVGM8K-1 (slave 1) + 2 esclavos (slave 2 y 3) vía `POST /api/inversors/{id}/esclavos`.
4. **Gateway**: configurar 9600 en el TCP→RS485, o crear un gateway nuevo.
5. **Verificación**: polling real contra 192.168.1.10:502 y contraste de los valores con la pantalla del inversor.

## 6. Riesgos

- **El gateway actual está en 2400**: sin reconfigurarlo, la lectura falla (y hay que verificar que el cambio
  no rompa los Lumin que comparten gateway).
- **El bus RS485 no admite polling paralelo**: secuencial, 9600 baud es lento.
- **Semántica de state1/state2/state3 sin confirmar en campo** (el doc pide validar contra la pantalla).
- **El ID Modbus del slave 2 estaba en 1 de fábrica** (hallazgo del doc): hay que cambiar 0x2142 físicamente.
- La app no implementa el valor centinela 0xFFFE → llegaría como 65534 en lugar de N/A.
