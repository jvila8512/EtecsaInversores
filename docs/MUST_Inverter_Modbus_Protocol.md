# Protocolo de Comunicación Serial — Inversores MUST Solar
## Modbus RTU sobre RS485 — Mapa Completo de Registros

> **Fuentes:** Protocolo oficial PH/PV RS485 Modbus RTU v1.4.3/v1.4.15 (Scribd),
> integración Home Assistant `ha-must-inverter` (mukaschultze), configuración
> ESPHome `esphome-must-inverter` (vladyspavlov), comunidad DIY Solar Power Forum.

---

## 1. Parámetros de Conexión Serial

| Parámetro        | Valor Series PH/PV/EP | Valor Series PV18 / PH18 |
|------------------|-----------------------|--------------------------|
| Interfaz física  | RS485 (half-duplex)   | RS485 (half-duplex)      |
| Protocolo        | Modbus RTU            | Modbus RTU               |
| Baud rate        | **9600 bps**          | **19200 bps**            |
| Bits de datos    | 8                     | 8                        |
| Paridad          | Ninguna               | Ninguna                  |
| Bits de parada   | 1                     | 1                        |
| Slave ID         | **0x04** (dec 4)      | **0x04** (dec 4)         |
| Function codes   | 03 (Read), 06 (Write Single), 10 (Write Multiple) | Iguales |

> ⚠️ **Importante para Java:** Los modelos **PH18 / PV18 Pro y superiores** usan 19200 bps.
> Los modelos más antiguos (PH1800, PV1800, EP1800) usan 9600 bps.
> Confirmar con el manual del modelo específico.

---

## 2. Modelos MUST y Protocolo Aplicable

### 2.1 Familia Principal (protocolo PH/PV v1.4.x — mismo mapa de registros)

| Familia       | Modelos Principales                                                              | Tipo               | Potencia       |
|---------------|----------------------------------------------------------------------------------|--------------------|----------------|
| **PH1800**    | PH1800, PH1800 Plus, PH1800 Pro, PH1800 ECO, PH1800 LV, PH1800 PLUS 4K/5K      | Híbrido On/Off-Grid| 1.5–10 kW      |
| **PV1800**    | PV1800, PV1800 VPM, PV1800 VHM, PV1800 PRO, PV1800 PRO II, PV1800 PREM, PV1800 VPM II | Off-Grid       | 1–10 kW        |
| **EP1800**    | EP1800, EP18-5048                                                                | Off-Grid           | 3–5 kW         |
| **PV3500**    | PV3500, PV3500 MPK                                                               | Off-Grid           | 1–12 kW        |
| **EP3500**    | EP3500                                                                           | Off-Grid           | 3–12 kW        |
| **PV18**      | PV18-1012, PV18-2024, PV18-3024, PV18-5048 VHM/VPM                             | Off-Grid           | 1–5 kW         |
| **PH18**      | PH18-3024, PH18-5048 PRO, PH18-5448 PRO                                         | Híbrido            | 3–5.5 kW       |
| **PC1800**    | PC1800, PC1800-3024, PC1800-5048                                                 | Off-Grid paralelo  | 3–5 kW         |
| **PV3000**    | PV3000 PK                                                                        | Off-Grid           | 3–12 kW        |
| **PH5000-T**  | PH5000-T Series                                                                  | Trifásico          | 5–10 kW        |
| **PH30**      | PH30 Series                                                                      | Almacenamiento     | 3–10 kW        |
| **EP33**      | EP33-1024TLV                                                                     | Off-Grid           | 1–3 kW         |
| **HBP1800**   | HBP1800                                                                          | Híbrido + BMS      | 3–5 kW         |
| **PV1100 PLUS**| PV1100 PLUS 1200VA / 1800VA / 2400VA                                            | Off-Grid pequeño   | 1–2.4 kW       |

> **Modelos con protocolo Modbus confirmado por la comunidad:**
> PH1800, PV1800 (PV18-3024), PH18-5448 PRO, EP1800, PV3500, EP3500, PC1800.
> Los modelos **PH1800 ECO** con WiFi integrado pueden usar protocolo diferente.

---

## 3. Estructura de Bloques de Registros

Los registros están organizados en 4 bloques funcionales (Holding Registers, Function Code 03):

| Bloque | Rango de Direcciones | Descripción                         |
|--------|----------------------|-------------------------------------|
| A      | 10001 – 10xxx        | Control/Configuración del Cargador  |
| B      | 15201 – 15xxx        | Telemetría en tiempo real del **Cargador PV** |
| C      | 20001 – 20xxx        | Control/Configuración del **Inversor** |
| D      | 25201 – 25xxx        | Telemetría en tiempo real del **Inversor** |

---

## 4. Bloque A — Registros de Control del Cargador (10001–10xxx)

> Holding Registers — Lectura (FC03) y Escritura (FC06 / FC10)

| Dirección | Tipo | Nombre del Registro              | Factor | Unidad | Rango / Notas                         |
|-----------|------|----------------------------------|--------|--------|---------------------------------------|
| 10001     | RW   | Tipo de cargador                 | —      | —      | Identificación modelo cargador        |
| 10002     | RW   | Corriente máxima de carga        | 1      | A      | Depende del modelo                    |
| 10003     | RW   | Voltage absorción (Bulk)         | 0.1    | V      | Ej: 288 = 28.8V (24V system)          |
| 10004     | RW   | Voltage flotación (Float)        | 0.1    | V      |                                       |
| 10005     | RW   | Voltage corte batería baja       | 0.1    | V      |                                       |
| 10006     | RW   | Calibración voltage batería      | 0.1    | V      | ⚠️ En algunos firmwares NO escribible  |
| 10007     | RW   | Calibración corriente cargador   | 0.1    | A      | ⚠️ En algunos firmwares NO escribible  |
| 10008     | RW   | Calibración corriente PV         | 0.1    | A      | ⚠️ En algunos firmwares NO escribible  |
| 10101     | RW   | Dirección Modbus del dispositivo | 1      | —      | 1–247                                 |

---

## 5. Bloque B — Telemetría del Cargador PV (15201–15xxx)

> Holding Registers — **Solo lectura** (FC03)

| Dirección | Tipo | Nombre del Registro               | Factor | Unidad | Descripción                              |
|-----------|------|-----------------------------------|--------|--------|------------------------------------------|
| 15201     | RO   | PV Charger Work State             | —      | enum   | Ver tabla de estados abajo               |
| 15202     | RO   | PV Charger MPPT State             | —      | enum   | Estado del algoritmo MPPT                |
| 15203     | RO   | PV Charger Charging State         | —      | enum   | Estado de la carga de batería            |
| 15204     | RO   | PV Charger Fault Code             | —      | bits   | Código de falla (ver bitfield)           |
| 15205     | RO   | **PV Voltage** (Tensión panel)    | 0.1    | V      | Ej: 3560 = 356.0V                        |
| 15206     | RO   | **Battery Voltage** (Cargador)    | 0.1    | V      | Ej: 245 = 24.5V                          |
| 15207     | RO   | **PV Charging Current**           | 0.1    | A      |                                          |
| 15208     | RO   | **PV Charging Power**             | 1      | W      |                                          |
| 15209     | RO   | PV Radiator Temperature           | 1      | °C     | Temperatura del disipador PV             |
| 15210     | RO   | External Temperature              | 1      | °C     | Temperatura ambiente (si aplica)         |
| 15211     | RO   | Battery Temperature               | 1      | °C     | (si tiene sensor)                        |
| 15212     | RO   | PV Relay State                    | —      | bits   | Estado del relé del cargador PV          |
| 15213     | RO   | Error Message                     | —      | bits   | Bitfield de errores del cargador         |
| 15217     | RO   | Accumulated Charger Power MWh     | 1      | MWh    | Parte entera en MWh (combinar con 15218) |
| 15218     | RO   | Accumulated Charger Power kWh     | 0.1    | kWh    | Parte decimal en kWh                     |
| 15219     | RO   | Accumulated Charger Day           | 1      | Wh     | Energía cargada hoy                      |
| 15220     | RO   | Accumulated Charger Hour          | 1      | Wh     |                                          |
| 15221     | RO   | Accumulated Charger Minute        | 1      | Wh     |                                          |

### Tabla: PV Charger Work State (reg. 15201)

| Valor | Estado              |
|-------|---------------------|
| 0     | Initialization Mode |
| 1     | Self-test Mode      |
| 2     | Work Mode           |
| 3     | Stop Mode           |

---

## 6. Bloque C — Registros de Control del Inversor (20001–20xxx)

> Holding Registers — Lectura y Escritura (FC03 / FC06 / FC10)

### 6.1 Información del Dispositivo (Solo Lectura)

| Dirección | Tipo | Nombre del Registro         | Factor | Unidad | Notas                        |
|-----------|------|-----------------------------|--------|--------|------------------------------|
| 20001     | RO   | Machine Type                | —      | —      | Tipo de máquina              |
| 20002     | RO   | Serial Number High          | —      | —      |                              |
| 20003     | RW   | Serial Number Low           | —      | —      |                              |
| 20004     | RO   | Hardware Version            | —      | —      |                              |
| 20005     | RO   | Software Version            | —      | —      |                              |
| 20006     | RO   | Rated Power High            | 1      | VA     |                              |
| 20007     | RO   | Rated Power Low             | 1      | VA     |                              |

### 6.2 Parámetros de Configuración (Lectura/Escritura)

| Dirección | Tipo | Nombre del Registro                   | Factor | Unidad | Rango                          |
|-----------|------|---------------------------------------|--------|--------|--------------------------------|
| 20101     | RW   | Modbus Address                        | 1      | —      | 1–247                          |
| 20102     | RW   | Output Voltage Setting                | 0.1    | V      | Ej: 2200 = 220.0V              |
| 20103     | RW   | Output Frequency Setting              | 0.1    | Hz     | 500 = 50.0Hz / 600 = 60.0Hz   |
| 20104     | RW   | Output Source Priority                | —      | enum   | 0=SBU, 1=SUB, 2=UTI           |
| 20105     | RW   | Charger Source Priority               | —      | enum   | 0=PV+Utility, 1=PV, 2=PV+Bat |
| 20106     | RW   | Max Charging Current                  | 1      | A      |                                |
| 20107     | RW   | Battery Type                          | —      | enum   | 0=AGM, 1=Flooded, 2=User, 3=Li|
| 20108     | RW   | Battery Low Voltage Alarm             | 0.1    | V      | ⚠️ Algunos FW bloquean escritura|
| 20109     | RW   | Battery Low Voltage Shutdown          | 0.1    | V      |                                |
| 20110     | RW   | Battery Bulk Charging Voltage         | 0.1    | V      | Ej: 288 = 28.8V                |
| 20111     | RW   | Battery Float Charging Voltage        | 0.1    | V      |                                |
| 20112     | RW   | Battery Over-charge Voltage           | 0.1    | V      |                                |
| 20113     | RW   | Max AC Charging Current               | 1      | A      |                                |
| 20114     | RW   | AC Input Voltage Range                | —      | enum   | 0=Appliance, 1=UPS            |
| 20115     | RW   | Output Mode                           | —      | enum   | 0=Single, 1=Parallel Master, 2=Parallel Slave |
| 20116     | RW   | Battery Equalization Voltage          | 0.1    | V      |                                |
| 20117     | RW   | Battery Equalization Time             | 1      | min    |                                |
| 20118     | RW   | Battery Equalization Interval         | 1      | day    |                                |
| 20119     | RW   | Battery Equalization Timeout          | 1      | min    |                                |
| 20120     | RW   | Battery Equalization Activated        | —      | bool   | 0=Off, 1=On                   |
| 20121     | RW   | Battery Over-charge Return Voltage    | 0.1    | V      |                                |
| 20122     | RW   | Battery Discharge Max Current         | 1      | A      |                                |
| 20123     | RW   | AC Charging Boost Current             | 1      | A      |                                |
| 20124     | RW   | PV Start Voltage                      | 0.1    | V      |                                |
| 20125     | RW   | Grid-tie Build Voltage                | 0.1    | V      |                                |
| 20126     | RW   | Max Utility Charging Current          | 1      | A      |                                |
| 20127     | RW   | Split-phase Output Voltage Setting    | 0.1    | V      | (solo modelos split-phase)    |
| 20128     | RW   | Backlight Time                        | 1      | min    |                                |
| 20129     | RW   | Battery Temperature Compensation      | 0.01   | mV/°C  |                                |
| 20130     | RW   | System Setting Bits                   | —      | bits   | Ver tabla bit-flags abajo      |
| 20131     | RW   | Charger Start Time (Hour)             | 1      | h      | 0–23                           |
| 20132     | RW   | Charger Start Time (Minutes)          | 1      | min    | 0–59                           |
| 20133     | RW   | Charger End Time (Hour)               | 1      | h      |                                |
| 20134     | RW   | Charger End Time (Minutes)            | 1      | min    |                                |
| 20135     | RW   | Charger End Time Hour (alt)           | 1      | h      |                                |
| 20136     | RW   | Charger End Time Minutes (alt)        | 1      | min    |                                |
| 20137     | RW   | Discharger Start Time (Hour)          | 1      | h      |                                |
| 20138     | RW   | Discharger Start Time (Minutes)       | 1      | min    |                                |
| 20139     | RW   | Discharger End Time (Hour)            | 1      | h      |                                |
| 20140     | RW   | Discharger End Time (Minutes)         | 1      | min    |                                |
| 20141     | RW   | Battery Discharge Stop Voltage        | 0.1    | V      |                                |
| 20142     | RW   | Battery Equalization Standby          | —      | bool   |                                |
| 20143     | RW   | Discharge Enable (Time-based)         | —      | bool   |                                |
| 20144     | RW   | Charge Enable (Time-based)            | —      | bool   |                                |
| 20145     | RW   | Battery Discharge SOC Low             | 1      | %      | Para LiFePO4                  |
| 20146     | RW   | Battery Charge SOC Stop               | 1      | %      |                                |
| 20191     | RW   | Output Q Mode                         | —      | enum   |                                |
| 20192     | RW   | Active Power Setting                  | 1      | W      |                                |
| 20193     | RW   | Reactive Power Setting                | 1      | VAR    |                                |
| 20201     | RW   | Year (RTC)                            | 1      | —      | Reloj interno                  |
| 20202     | RW   | Month (RTC)                           | 1      | —      |                                |
| 20203     | RW   | Day (RTC)                             | 1      | —      |                                |
| 20204     | RW   | (reservado)                           | —      | —      |                                |
| 20205     | RW   | Hour (RTC)                            | 1      | —      |                                |
| 20206     | RW   | Minutes (RTC)                         | 1      | —      |                                |
| 20207     | RW   | Seconds (RTC)                         | 1      | —      |                                |
| 20209     | RW   | Force Charger Enable                  | —      | bool   | 0=Off, 1=On (forzar carga)    |
| 20210     | RW   | Force Discharger Enable               | —      | bool   | 0=Off, 1=On (forzar descarga) |

### 6.3 Tabla: System Setting Bits (reg. 20130)

| Bit | Nombre                    | Descripción                               |
|-----|---------------------------|-------------------------------------------|
| 0   | OverLoadRestartForbid      | Prohibir reinicio tras sobrecarga         |
| 1   | OverTempRestartForbid      | Prohibir reinicio tras sobretemperatura   |
| 2   | OverLoadBypassForbid       | Prohibir bypass en sobrecarga             |
| 3   | AutoTurnPageFlagForbid     | Deshabilitar cambio automático de pantalla|
| 4   | GridBuzzEnable             | Activar zumbador de red (solo PV1800)     |
| 5   | BuzzForbide                | Deshabilitar zumbador (solo PV1800)       |
| 6   | LcdLightEnable             | Habilitar luz de fondo LCD                |
| 7   | RecordFaultForbid          | Deshabilitar grabación de fallos          |
| 8–15| Reservado                 | —                                         |

---

## 7. Bloque D — Telemetría del Inversor en Tiempo Real (25201–25xxx)

> Holding Registers — **Solo lectura** (FC03)

| Dirección | Tipo | Nombre del Registro                   | Factor | Unidad | Descripción                              |
|-----------|------|---------------------------------------|--------|--------|------------------------------------------|
| 25201     | RO   | **Inverter Work State**               | —      | enum   | Ver tabla de estados abajo               |
| 25202     | RO   | Inverter Fault Code (High)            | —      | bits   |                                          |
| 25203     | RO   | Inverter Fault Code (Low)             | —      | bits   |                                          |
| 25204     | RO   | Inverter Warning Code                 | —      | bits   |                                          |
| 25205     | RO   | **Battery Voltage**                   | 0.1    | V      | Ej: 245 = 24.5V                          |
| 25206     | RO   | **Inverter Output Voltage**           | 0.1    | V      | Tensión de salida (AC out)               |
| 25207     | RO   | **Grid Voltage** (Tensión Red AC)     | 0.1    | V      | Tensión de entrada de red                |
| 25208     | RO   | **BUS Voltage**                       | 0.1    | V      | Tensión bus interno DC                   |
| 25209     | RO   | **Control Current** (Battery)         | 0.1    | A      | Corriente batería (carga/descarga)       |
| 25210     | RO   | **Inverter Output Current**           | 0.1    | A      | Corriente de salida del inversor         |
| 25211     | RO   | **Grid Current**                      | 0.1    | A      | Corriente desde/hacia la red             |
| 25212     | RO   | **Load Current**                      | 0.1    | A      | Corriente consumida por la carga         |
| 25213     | RO   | **Inverter Power**                    | 1      | W      | Potencia de salida del inversor          |
| 25214     | RO   | **Grid Power**                        | 1      | W      | ⚠️ Signed (S_WORD) — negativo = exportar |
| 25215     | RO   | **Load Power**                        | 1      | W      | Potencia consumida por carga             |
| 25216     | RO   | **System Load Percentage**            | 1      | %      | % de carga del sistema                  |
| 25217     | RO   | Inverter Efficiency                   | 0.1    | %      |                                          |
| 25218     | RO   | Output Frequency                      | 0.01   | Hz     | Ej: 5000 = 50.00Hz                       |
| 25219     | RO   | Battery Capacity (SOC)                | 1      | %      | Estado de carga batería                  |
| 25220     | RO   | Battery Current (AC Charger)          | 0.1    | A      | Corriente de carga desde AC              |
| 25221     | RO   | Battery Power                         | 1      | W      | Potencia batería (+ cargando, – descarg.)|
| 25222     | RO   | Grid Frequency                        | 0.01   | Hz     |                                          |
| 25223     | RO   | Grid Active Power                     | 1      | W      |                                          |
| 25224     | RO   | Grid Reactive Power                   | 1      | VAR    |                                          |
| 25225     | RO   | Grid Apparent Power                   | 1      | VA     |                                          |
| 25226     | RO   | Inverter Active Power                 | 1      | W      |                                          |
| 25227     | RO   | Inverter Reactive Power               | 1      | VAR    |                                          |
| 25228     | RO   | Inverter Apparent Power               | 1      | VA     |                                          |
| 25229     | RO   | Load Active Power                     | 1      | W      |                                          |
| 25230     | RO   | Load Reactive Power                   | 1      | VAR    |                                          |
| 25231     | RO   | Load Apparent Power                   | 1      | VA     |                                          |
| 25232     | RO   | Inverter DC Voltage                   | 0.1    | V      |                                          |
| 25233     | RO   | **AC Radiator Temperature**           | 1      | °C     | Temperatura disipador AC                 |
| 25234     | RO   | **Transformer Temperature**           | 1      | °C     |                                          |
| 25235     | RO   | **DC Radiator Temperature**           | 1      | °C     | Temperatura disipador DC                 |
| 25236     | RO   | External Temperature                  | 1      | °C     |                                          |
| 25237     | RO   | **Inverter Relay State**              | —      | bits   | Estado de relés internos                 |
| 25238     | RO   | **Grid Relay State**                  | —      | bits   |                                          |
| 25239     | RO   | **Load Relay State**                  | —      | bits   |                                          |
| 25240     | RO   | **N-Line Relay State**                | —      | bits   |                                          |
| 25241     | RO   | **DC Relay State**                    | —      | bits   |                                          |
| 25242     | RO   | **Earth Relay State**                 | —      | bits   |                                          |
| 25243     | RO   | Current Battery Charge/Discharge Power| 1     | W      | Positivo = carga, negativo = descarga    |
| 25247     | RO   | Accumulated Discharge Power (MWh)     | 1      | MWh    | Combinar con 25248                       |
| 25248     | RO   | Accumulated Discharge Power (kWh)     | 0.1    | kWh    |                                          |
| 25249     | RO   | Accumulated Buy Power (MWh)           | 1      | MWh    | Energía comprada de la red               |
| 25250     | RO   | Accumulated Buy Power (kWh)           | 0.1    | kWh    |                                          |
| 25251     | RO   | Accumulated Sell Power (MWh)          | 1      | MWh    | Energía vendida a la red                 |
| 25252     | RO   | Accumulated Sell Power (kWh)          | 0.1    | kWh    |                                          |
| 25253     | RO   | Accumulated Load Power (MWh)          | 1      | MWh    |                                          |
| 25254     | RO   | Accumulated Load Power (kWh)          | 0.1    | kWh    |                                          |
| 25257     | RO   | Accumulated PV Power (MWh)            | 1      | MWh    |                                          |
| 25258     | RO   | Accumulated PV Power (kWh)            | 0.1    | kWh    |                                          |

### Tabla: Inverter Work State (reg. 25201)

| Valor | Estado              |
|-------|---------------------|
| 0     | Power On            |
| 1     | Self-test Mode      |
| 2     | Off Grid Mode       |
| 3     | Grid Tie Mode       |
| 4     | Bypass Mode         |
| 5     | Charging Mode       |
| 6     | Fault Mode          |

---

## 8. Registro Battery SOC — Serie PH18 Pro con CAN (LiFePO4)

> Específico para modelos PH18-5448 PRO con batería Pylontech/CAN BMS.

| Dirección | Tipo | Nombre                 | Factor | Unidad |
|-----------|------|------------------------|--------|--------|
| 44180     | RO   | Battery SOC (CAN BMS)  | 1      | %      |

---

## 9. Cálculo de Energía Acumulada

Los registros de energía acumulada se expresan en dos partes y deben combinarse:

```
Energía total (kWh) = Registro_MWh × 1000 + Registro_kWh
```

Ejemplo para energía cargada por PV:
```java
double pvEnergyKwh = reg15217 * 1000.0 + (reg15218 * 0.1);
```

---

## 10. Notas para Integración en Java (Modbus)

### Librerías recomendadas
- **jamod** — `net.wimpi.modbus` (clásica)
- **j2mod** — fork actualizado de jamod (recomendada)
- **EasyModbus** — sencilla, funciona bien con RTU/TCP

### Parámetros de conexión típicos (j2mod)

```java
SerialParameters params = new SerialParameters();
params.setPortName("COM3");          // o /dev/ttyUSB0 en Linux
params.setBaudRate(9600);            // PH1800/PV1800 clásico
// params.setBaudRate(19200);        // PV18 Pro / PH18
params.setDatabits(8);
params.setParity(SerialPort.PARITY_NONE);
params.setStopbits(1);
params.setEncoding("rtu");

ModbusSerialTransaction tx = new ModbusSerialTransaction(conn);
ReadMultipleRegistersRequest req = new ReadMultipleRegistersRequest(
    25205 - 1,  // dirección base 0 (j2mod descuenta 1)
    10          // leer 10 registros de corrido
);
req.setUnitID(0x04);  // Slave ID del inversor
```

> ⚠️ Algunos frameworks Modbus usan direcciones base-0 (la dirección real − 1).
> Verificar con la librería elegida.

### Lectura de voltaje de batería (reg. 25205)

```java
int rawValue = registers[0].getValue();   // registro 25205
double batteryVoltage = rawValue * 0.1;   // factor × 0.1 → V
```

### Lectura de Grid Power con signo (reg. 25214)

```java
// Grid Power es SIGNED (S_WORD): positivo = importando, negativo = exportando
short rawSigned = (short) registers[9].getValue();
int gridPowerW = rawSigned;               // ya tiene signo correcto
```

---

## 11. Resumen de Lecturas de Telemetría Recomendadas (Polling)

Leer en un solo bloque desde 25205 hasta 25216 (12 registros):

| Offset | Dirección | Valor                      |
|--------|-----------|----------------------------|
| 0      | 25205     | Battery Voltage × 0.1 V    |
| 1      | 25206     | Output Voltage × 0.1 V     |
| 2      | 25207     | Grid Voltage × 0.1 V       |
| 3      | 25208     | BUS Voltage × 0.1 V        |
| 4      | 25209     | Battery Current × 0.1 A    |
| 5      | 25210     | Output Current × 0.1 A     |
| 6      | 25211     | Grid Current × 0.1 A       |
| 7      | 25212     | Load Current × 0.1 A       |
| 8      | 25213     | Inverter Power W            |
| 9      | 25214     | Grid Power W (signed)       |
| 10     | 25215     | Load Power W                |
| 11     | 25216     | System Load %               |

Leer bloque del cargador PV desde 15205 hasta 15209 (5 registros):

| Offset | Dirección | Valor                      |
|--------|-----------|----------------------------|
| 0      | 15205     | PV Voltage × 0.1 V         |
| 1      | 15206     | Battery Voltage × 0.1 V    |
| 2      | 15207     | PV Current × 0.1 A         |
| 3      | 15208     | PV Power W                 |
| 4      | 15209     | PV Radiator Temp °C         |

---

## 12. Links y Referencias

| Recurso | URL |
|---------|-----|
| Protocolo oficial v1.4.3 (Scribd) | https://www.scribd.com/document/604607537/ |
| Integración Home Assistant (register map) | https://github.com/mukaschultze/ha-must-inverter |
| Configuración ESPHome completa | https://github.com/vladyspavlov/esphome-must-inverter |
| Sitio oficial MUST Power | https://www.mustpower.com |
| Foro comunidad (experiencias reales) | https://diysolarforum.com (buscar "MUST PV1800") |

---

*Documento generado con información de protocolo oficial v1.4.x y datos confirmados por la comunidad. Algunos registros pueden variar según el firmware instalado. Verificar siempre con el manual del modelo específico.*
