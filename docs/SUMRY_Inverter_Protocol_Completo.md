# Protocolo de Comunicación Serial — Inversores SUMRY

## Guía Completa por Familia de Modelos

> **⚠️ ADVERTENCIA CRÍTICA:** Sumry NO utiliza un único protocolo.  
> Dependiendo de la familia del modelo, el inversor puede usar uno de **tres protocolos completamente distintos**.  
> Identificar correctamente el modelo antes de intentar comunicarse es **obligatorio**.

---

## 1. Las Tres Familias de Protocolo SUMRY

| Familia de Protocolo           | Modelos SUMRY                                | Protocolo                            | Interfaz        |
| ------------------------------ | -------------------------------------------- | ------------------------------------ | --------------- |
| **Familia A — Voltronic/PI30** | MPS-VIII ECO, RS Series, HMS, HGS, SP Series | PI30 (ASCII con CRC16)               | RS232 o USB-HID |
| **Familia B — 8851 (PowMr)**   | HGX 4KW, HGX 6KW                             | Protocolo binario propietario "8851" | RS232           |
| **Familia C — Axpert/PI18**    | MPS-V PLUS, EP Series                        | PI18/PI41 (ASCII con CRC16)          | RS232 o USB-HID |

> **Cómo identificar el protocolo de tu unidad:**
> Conectar vía RS232 (9600 bps, 8N1) y enviar: `QPI\xBE\xAC\r`
>
> - Si responde `(PI30\r` → **Familia A**
> - Si responde `(PI18\r` → **Familia C**
> - Si **no responde nada** → **Familia B** (protocolo binario 8851)

---

## 2. Modelos SUMRY y su Protocolo Correspondiente

### Familia A — Protocolo PI30 (Voltronic/WatchPower)

| Modelo             | Potencia     | Tensión Batería | Confirmado PI30         |
| ------------------ | ------------ | --------------- | ----------------------- |
| MPS-VIII ECO 3.6KW | 3.6 kVA      | 24V             | ✅ Confirmado comunidad |
| MPS-VIII ECO 4.2KW | 4.2 kVA      | 24V             | ✅ Confirmado comunidad |
| MPS-VIII ECO 6.2KW | 6.2 kVA      | 48V             | ✅ Confirmado comunidad |
| RS-3K              | 3 kW         | 24/48V          | ✅ Confirmado           |
| RS-5K              | 5 kW         | 48V             | ✅ Confirmado           |
| HMS 1.5K           | 1.5 kW       | 12/24V          | ✅                      |
| HMS 3K             | 3 kW         | 24/48V          | ✅                      |
| HMS 5K             | 5 kW         | 48V             | ✅                      |
| HGS-5500           | 5.5 kW       | 48V             | ✅                      |
| SP 4.2KVA          | 4.2 kVA      | 24V             | ✅                      |
| SP 7.0KVA          | 7.0 kVA      | 48V             | ✅                      |
| ECO LV 3.6KW       | 3.6 kW       | 24V             | ✅                      |
| 2.2KVA Hybrid      | 2.2 kVA      | 12/24V          | ✅                      |
| 3.2KVA Hybrid      | 3.2 kVA      | 24V             | ✅                      |
| 3.6KW 6.2KW Hybrid | 3.6 / 6.2 kW | 24/48V          | ✅                      |

### Familia B — Protocolo 8851 (Binario propietario)

| Modelo         | Potencia | Tensión Batería | Notas                             |
| -------------- | -------- | --------------- | --------------------------------- |
| HGX 4KW (120V) | 4 kW     | 24V             | ⚠️ NO usa PI30 ni Modbus estándar |
| HGX 6KW (230V) | 6 kW     | 48V             | ⚠️ NO usa PI30 ni Modbus estándar |

### Familia C — Protocolo PI18/PI41

| Modelo            | Potencia | Notas                           |
| ----------------- | -------- | ------------------------------- |
| MPS-V PLUS Series | 3–10 kW  | PI18, responde diferente a PI30 |
| EP-4000L          | 4 kW     |                                 |
| EP4200-H          | 4.2 kW   |                                 |

---

## 3. FAMILIA A — Protocolo PI30 (Modelos MPS-VIII, RS, HMS, HGS, SP)

### 3.1 Parámetros de Conexión Serial

| Parámetro              | Valor                                       |
| ---------------------- | ------------------------------------------- |
| Interfaz               | RS232 (conector RJ45) o USB-HID             |
| Baud rate              | **2400 bps** (RS232 estándar PI30)          |
| Bits de datos          | 8                                           |
| Paridad                | Ninguna                                     |
| Bits de parada         | 1                                           |
| Formato                | `2400, 8N1`                                 |
| Terminador de trama    | `\r` (0x0D)                                 |
| CRC                    | CRC16 propio (no Modbus) — 2 bytes al final |
| Respuesta comienza con | `(` (0x28)                                  |

> ⚠️ El protocolo PI30 usa **RS232** con conector RJ45 en los inversores SUMRY.
> El pinout del RJ45 es el mismo que EASUN Power; si hay problemas, intercambiar pines 1,2 con pines 5,6.

### 3.2 Pinout RJ45 — Puerto de Comunicación (RS232)

| Pin RJ45 | Señal | Descripción          |
| -------- | ----- | -------------------- |
| 1        | TX    | Transmisión          |
| 2        | RX    | Recepción            |
| 5        | GND   | Tierra               |
| 6        | GND   | Tierra (alternativo) |

> Si no hay comunicación: intercambiar pines 1↔5 y 2↔6.

### 3.3 Estructura de Trama PI30

**Petición (Master → Inversor):**

```
[COMANDO][CRC_HIGH][CRC_LOW][0x0D]
```

**Respuesta (Inversor → Master):**

```
[(][DATOS SEPARADOS POR ESPACIO][CRC_HIGH][CRC_LOW][0x0D]
```

**Cálculo del CRC PI30:**
El CRC es un CRC16 propio (diferente al CRC16 de Modbus). Basado en el polinomio 0x1021.

```java
// CRC16 para protocolo PI30
static int[] CRC_TABLE = new int[256]; // precomputar con polinomio 0x1021
// Calcular el CRC de los bytes del comando (sin el 0x0D final)
// Luego añadir los 2 bytes del CRC y el 0x0D

```

Ejemplo completo para el comando QPIGS:

```
Bytes a enviar: 51 50 49 47 53 B7 A9 0D
ASCII: Q  P  I  G  S [CRC1][CRC2] \r
```

---

### 3.4 Comandos de Consulta (Read) — Protocolo PI30

#### QPI — Identificación del Protocolo

```
Comando: QPI\xBE\xAC\r
Respuesta: (PI30\x9A\x0B\r
```

#### QPIGS — Parámetros de Estado General ⭐ (El más importante)

```
Comando: QPIGS\xB7\xA9\r
```

**Respuesta (25 campos separados por espacio):**

```
(238.6 50.0 238.6 50.0 0500 0450 012 435 54.10 005 100 0032 00.0 000.0 00.00 00000 00010000 00 00 00000 100\r
```

**Mapa de campos QPIGS:**

| Campo | Posición                     | Unidad | Factor | Descripción                 |
| ----- | ---------------------------- | ------ | ------ | --------------------------- |
| 1     | Grid Voltage                 | V      | 0.1    | Tensión de entrada de red   |
| 2     | Grid Frequency               | Hz     | 0.1    | Frecuencia de red           |
| 3     | AC Out Voltage               | V      | 0.1    | Tensión de salida AC        |
| 4     | AC Out Frequency             | Hz     | 0.1    | Frecuencia de salida        |
| 5     | AC Out Apparent Power        | VA     | 1      | Potencia aparente de salida |
| 6     | AC Out Active Power          | W      | 1      | Potencia activa de salida   |
| 7     | Output Load Percent          | %      | 1      | % de carga del inversor     |
| 8     | Bus Voltage                  | V      | 1      | Tensión bus DC interno      |
| 9     | **Battery Voltage**          | V      | 0.01   | Tensión de batería          |
| 10    | **Battery Charging Current** | A      | 1      | Corriente de carga batería  |
| 11    | **Battery Capacity** (SOC)   | %      | 1      | Estado de carga batería     |
| 12    | Inverter Heat Sink Temp      | °C     | 1      | Temperatura disipador       |
| 13    | **PV Input Current**         | A      | 0.1    | Corriente panel solar       |
| 14    | **PV Input Voltage**         | V      | 0.1    | Tensión panel solar         |
| 15    | Battery Voltage (SCC)        | V      | 0.01   | Tensión bat. desde SCC      |
| 16    | Battery Discharge Current    | A      | 1      | Corriente de descarga       |
| 17    | Device Status Bits           | —      | bits   | Ver tabla de bits abajo     |
| 18    | Battery Voltage Offset       | 10mV   | —      | Para activar ventilador     |
| 19    | EEPROM Version               | —      | —      | Versión EEPROM              |
| 20    | **PV Charging Power**        | W      | 1      | Potencia de carga PV        |
| 21    | Device Status2 Bits          | —      | bits   | Ver tabla de bits abajo     |

**Tabla Device Status Bits (campo 17) — 8 bits:**

| Bit | Nombre                    | Descripción                  |
| --- | ------------------------- | ---------------------------- |
| 7   | Is SBU Priority Version   | 1 = prioridad SBU            |
| 6   | Is Configuration Changed  | 1 = configuración modificada |
| 5   | Is SCC Firmware Updated   | 1 = firmware SCC actualizado |
| 4   | Is Load On                | 1 = carga activa             |
| 3   | Is Battery Voltage Steady | 1 = voltaje batería estable  |
| 2   | Is Charging On            | 1 = cargando                 |
| 1   | Is SCC Charging On        | 1 = SCC está cargando        |
| 0   | Is AC Charging On         | 1 = cargando desde AC        |

---

#### QPIRI — Configuración Actual del Inversor

```
Comando: QPIRI\xF8\x54\r
Respuesta (17 campos):
(230.0 13.0 230.0 50.0 13.0 3000 3000 24.0 23.0 21.0 28.2 27.0 0 10 060 0 0 3 9 01\r
```

**Mapa de campos QPIRI:**

| Campo | Descripción                      | Unidad                                            |
| ----- | -------------------------------- | ------------------------------------------------- |
| 1     | Grid Rating Voltage              | V                                                 |
| 2     | Grid Rating Current              | A                                                 |
| 3     | AC Output Rating Voltage         | V                                                 |
| 4     | AC Output Rating Frequency       | Hz                                                |
| 5     | AC Output Rating Current         | A                                                 |
| 6     | AC Output Rating Apparent Power  | VA                                                |
| 7     | AC Output Rating Active Power    | W                                                 |
| 8     | **Battery Rating Voltage**       | V                                                 |
| 9     | **Battery Re-charge Voltage**    | V                                                 |
| 10    | **Battery Under Voltage**        | V                                                 |
| 11    | **Battery Bulk Charge Voltage**  | V                                                 |
| 12    | **Battery Float Charge Voltage** | V                                                 |
| 13    | Battery Type                     | 0=AGM, 1=Flooded, 2=User, 3=Li                    |
| 14    | Max AC Charging Current          | A                                                 |
| 15    | Max Charging Current             | A                                                 |
| 16    | Input Voltage Range              | 0=Appliance, 1=UPS                                |
| 17    | Output Source Priority           | 0=Utility, 1=Solar, 2=SBU                         |
| 18    | Charger Source Priority          | 0=Utility, 1=Solar, 2=Solar+Utility, 3=Solar only |
| 19    | Max Parallel Units               | —                                                 |
| 20    | Machine Type                     | 00=GridTie, 01=Off Grid                           |

---

#### QMOD — Modo de Operación

```
Comando: QMOD\xC1\r
Respuesta: (B\r  o  (L\r  o  (S\r  o  (F\r  o  (P\r  o  (D\r
```

| Respuesta | Modo              |
| --------- | ----------------- |
| P         | Power On mode     |
| S         | Standby mode      |
| L         | Line (Grid) mode  |
| B         | Battery mode      |
| F         | Fault mode        |
| H         | Power Saving mode |
| D         | Shutdown mode     |

---

#### QPIWS — Estado de Alarmas y Warnings

```
Comando: QPIWS\xB4\xDA\r
Respuesta: (00000000000000000000000000000000\r  (32 bits)
```

**Mapa de bits de advertencia (de izquierda a derecha, posición 1 a 32):**

| Posición | Descripción               |
| -------- | ------------------------- |
| 1        | Reserved                  |
| 2        | Inverter Fault            |
| 3        | Bus Over                  |
| 4        | Bus Under                 |
| 5        | Bus Soft Fail             |
| 6        | Line Fail                 |
| 7        | OPV Short                 |
| 8        | Inverter Voltage Too Low  |
| 9        | Inverter Voltage Too High |
| 10       | Over Temperature          |
| 11       | Fan Locked                |
| 12       | Battery Voltage High      |
| 13       | **Battery Low Alarm**     |
| 14       | Reserved                  |
| 15       | Battery Under Shutdown    |
| 16       | Reserved                  |
| 17       | Overload                  |
| 18       | EEPROM Fault              |
| 19       | Inverter Over Current     |
| 20       | Inverter Soft Fail        |
| 21       | Self Test Fail            |
| 22       | OP DC Voltage Over        |
| 23       | Bat Open                  |
| 24       | Current Sensor Fail       |
| 25       | Battery Short             |
| 26       | Power Limit               |
| 27       | PV Voltage High           |
| 28       | MPPT Overload Fault       |
| 29       | MPPT Overload Warning     |
| 30       | Battery Too Low to Charge |
| 31       | Reserved                  |
| 32       | Reserved                  |

---

#### QDI — Valores por Defecto

```
Comando: QDI\x71\x1B\r
Respuesta: (230.0 50.0 0030 21.0 27.0 28.2 23.0 50 0 0 2 0 0 0 0 0 1 1 0 0 1 0 54.0 0 1\r
```

---

#### QFLAG — Estado de Flags del Sistema

```
Comando: QFLAG\x98\x74\r
Respuesta ejemplo: (EXYZabcjkuvxyz\r
```

| Flag | Enable (letra mayúscula) | Disable (letra minúscula) | Descripción                      |
| ---- | ------------------------ | ------------------------- | -------------------------------- |
| a/A  | a                        | A                         | Buzzer                           |
| b/B  | b                        | B                         | Overload Bypass                  |
| j/J  | j                        | J                         | Power Saving                     |
| k/K  | k                        | K                         | LCD Auto Reset                   |
| u/U  | u                        | U                         | Overload Restart                 |
| v/V  | v                        | V                         | Over Temperature Restart         |
| x/X  | x                        | X                         | Backlight                        |
| y/Y  | y                        | Y                         | Alarm Primary Source Interrupted |
| z/Z  | z                        | Z                         | Fault Code Record                |

---

#### QVFW — Versión de Firmware

```
Comando: QVFW\xC1\xF9\r
Respuesta: (VERFW:00052.30\r
```

#### QID — Número de Serie

```
Comando: QID\xD6\xEA\r
Respuesta: (92932004101659\r
```

#### QMCHGCR — Opciones de Corriente Máxima de Carga

```
Comando: QMCHGCR\xD5\xC0\r
Respuesta: (010 020 030 040 050 060 070 080 090 100 110 120\r
```

#### QMUCHGCR — Opciones de Corriente Máxima de Carga desde AC

```
Comando: QMUCHGCR\xC1\r
Respuesta: (002 010 020 030 040 050 060\r
```

---

### 3.5 Comandos de Configuración (Write) — Protocolo PI30

> Todos los comandos de escritura responden `(ACK\r` si exitoso o `(NAK\r` si falla.

| Comando     | Descripción                                       | Ejemplo             |
| ----------- | ------------------------------------------------- | ------------------- |
| `POP00`     | Set Output Source Priority: Utility               | `POP00\r`           |
| `POP01`     | Set Output Source Priority: Solar                 | `POP01\r`           |
| `POP02`     | Set Output Source Priority: SBU                   | `POP02\r`           |
| `PCP00`     | Set Charger Priority: Utility First               | `PCP00\r`           |
| `PCP01`     | Set Charger Priority: Solar First                 | `PCP01\r`           |
| `PCP02`     | Set Charger Priority: Solar and Utility           | `PCP02\r`           |
| `PCP03`     | Set Charger Priority: Solar Only                  | `PCP03\r`           |
| `MCHGC0nnn` | Set Max Charging Current (nnn = amperios)         | `MCHGC0060\r` = 60A |
| `MUCHGC0nn` | Set Max AC Charging Current                       | `MUCHGC020\r` = 20A |
| `PBFT nn.n` | Set Battery Float Voltage                         | `PBFT27.0\r`        |
| `PCVV nn.n` | Set Battery Bulk/CV Voltage                       | `PCVV28.2\r`        |
| `PBDV nn.n` | Set Battery Re-discharge Voltage                  | `PBDV24.0\r`        |
| `PBCV nn.n` | Set Battery Re-charge Voltage                     | `PBCV23.0\r`        |
| `PSDV nn.n` | Set Battery Cut-off (Shutdown) Voltage            | `PSDV21.0\r`        |
| `PBT n`     | Set Battery Type (0=AGM, 1=Flooded, 2=User, 3=Li) | `PBT0\r`            |
| `PF`        | Reset to Factory Defaults                         | `PF\r`              |
| `PEPD a b`  | Enable/Disable features (ver QFLAG)               | `PEPD a x\r`        |
| `F nn`      | Set Output Frequency (50 o 60)                    | `F50\r`             |
| `PGR00`     | Set Grid Range: Appliance (wide, 90-280V)         | `PGR00\r`           |
| `PGR01`     | Set Grid Range: UPS (narrow, 170-265V)            | `PGR01\r`           |

---

### 3.6 Implementación Java — Protocolo PI30

```java
// Configuración del puerto serial
SerialPort port = SerialPort.getCommPort("/dev/ttyUSB0");
port.setBaudRate(2400);
port.setNumDataBits(8);
port.setParity(SerialPort.NO_PARITY);
port.setNumStopBits(SerialPort.ONE_STOP_BIT);
port.openPort();

// Enviar comando QPIGS
byte[] cmd = buildPI30Command("QPIGS");
port.writeBytes(cmd, cmd.length);

// Leer respuesta (termina en 0x0D)
// Parsear: quitar el '(' inicial y los últimos 3 bytes (CRC+\r)
// Dividir por espacios
```

**Función para construir un comando PI30:**

```java
byte[] buildPI30Command(String command) {
  byte[] cmdBytes = command.getBytes();
  int crc = calculatePI30CRC(cmdBytes);
  byte[] fullCmd = new byte[cmdBytes.length + 3];
  System.arraycopy(cmdBytes, 0, fullCmd, 0, cmdBytes.length);
  fullCmd[cmdBytes.length] = (byte) ((crc >> 8) & 0xFF);
  fullCmd[cmdBytes.length + 1] = (byte) (crc & 0xFF);
  fullCmd[cmdBytes.length + 2] = 0x0D; // \r
  return fullCmd;
}

```

**CRC pre-calculados para los comandos más usados:**

| Comando | Bytes a enviar (hex completo) |
| ------- | ----------------------------- |
| QPI     | `51 50 49 BE AC 0D`           |
| QPIGS   | `51 50 49 47 53 B7 A9 0D`     |
| QPIRI   | `51 50 49 52 49 F8 54 0D`     |
| QMOD    | `51 4D 4F 44 49 C1 0D`        |
| QPIWS   | `51 50 49 57 53 B4 DA 0D`     |
| QFLAG   | `51 46 4C 41 47 98 74 0D`     |
| QDI     | `51 44 49 71 1B 0D`           |
| QID     | `51 49 44 D6 EA 0D`           |
| QVFW    | `51 56 46 57 C1 F9 0D`        |

---

## 4. FAMILIA B — Protocolo 8851 (Modelos HGX 4KW / HGX 6KW)

> ⚠️ **Este protocolo es completamente diferente.** No usa ASCII. Es un protocolo binario propietario.  
> Confirmado por la comunidad: **NO responde a QPIGS, NO responde a Modbus RTU estándar.**

### 4.1 Parámetros de Conexión Serial

| Parámetro       | Valor                                                |
| --------------- | ---------------------------------------------------- |
| Interfaz        | RS232 (conector RJ45, mismos pines que PI30)         |
| Baud rate       | **9600 bps**                                         |
| Bits de datos   | 8                                                    |
| Paridad         | Ninguna                                              |
| Bits de parada  | 1                                                    |
| Formato         | `9600, 8N1`                                          |
| Header de trama | `88 51`                                              |
| CRC             | CRC16 Modbus (mismo algoritmo, diferente aplicación) |

### 4.2 Trama de Petición de Estado

Para solicitar datos de estado del inversor:

```
Bytes: 88 51 00 03 00 00 00 00 4D 08
```

### 4.3 Trama de Respuesta de Estado

El inversor responde con una trama larga de ~140 bytes:

```
Header: 88 51 00 03 00 00 90 00 ...datos... [CRC_LOW] [CRC_HIGH]
```

### 4.4 Mapa de Offsets — Estado en Tiempo Real

> Los valores se extraen como `uint16_t` (2 bytes, little-endian) desde los offsets del frame completo.

| Offset (bytes) | Variable                     | Factor | Unidad | Descripción                                              |
| -------------- | ---------------------------- | ------ | ------ | -------------------------------------------------------- |
| 100            | Inverter Voltage             | / 10   | V      | Tensión de salida del inversor                           |
| 104            | Inverter Current             | / 100  | A      | Corriente de salida                                      |
| 108            | Inverter Frequency           | / 100  | Hz     | Frecuencia de salida                                     |
| 112            | Inverter VA (Apparent Power) | × 1    | VA     | Potencia aparente                                        |
| 116            | Load VA                      | × 1    | VA     | Potencia aparente de la carga                            |
| 124            | **Load Power (Active)**      | × 1    | W      | Potencia activa de la carga                              |
| 136            | **Load Current**             | / 100  | A      | Corriente de la carga                                    |
| 148            | **Grid Voltage**             | / 10   | V      | Tensión de la red AC                                     |
| 152            | **Grid Current**             | / 100  | A      | Corriente de la red                                      |
| 156            | Grid Frequency               | / 100  | Hz     | Frecuencia de la red                                     |
| 172            | **Battery Voltage**          | / 100  | V      | Tensión de batería                                       |
| 176            | **Battery Charge Current**   | / 10   | A      | Corriente de carga batería (signed, negativo = descarga) |
| 188            | **PV Voltage**               | / 10   | V      | Tensión del panel solar                                  |
| 192            | **PV Current**               | / 100  | A      | Corriente del panel solar                                |
| 196            | **PV Power**                 | × 1    | W      | Potencia del panel solar                                 |
| 200            | **Bus Voltage**              | / 10   | V      | Tensión bus DC interno                                   |

> ⚠️ Battery Charge Current en offset 176 es **signed** (int16_t):  
> valor positivo = cargando, valor negativo = descargando

### 4.5 Trama de Petición de Configuración

Para leer la configuración actual (necesaria antes de escribir):

```
Bytes: 88 51 00 03 02 00 00 00 4C B0
```

### 4.6 Trama de Escritura de Configuración

Se lee la configuración completa, se modifica el byte correspondiente, y se reenvía todo:

```
Header escritura: 88 51 00 10 02 00 5A 00 [90 bytes de config] [CRC_LOW] [CRC_HIGH]
```

**Offsets de configuración conocidos (dentro de la respuesta de config):**

| Offset Config   | Parámetro                         | Valores                                                 |
| --------------- | --------------------------------- | ------------------------------------------------------- |
| 4 (byte)        | Output Energy Priority            | 00=PV+Grid+Batt, 01=PV+Batt+Grid, 04=PV+Grid (sin Batt) |
| 5 (nibble alto) | Charge Energy Source              | 0=PV+Grid, 8=Solo Grid, 9=PV>Grid                       |
| 5 (nibble bajo) | Grid Voltage Range                | 0=90-265V, 3=170-265V                                   |
| 5 (bit 4)       | Grid Enable                       | 0=Desactivado, 1=Activado                               |
| ~offset         | Max Total Charge Current (A×10)   | uint16_t LE                                             |
| ~offset         | Max AC Charge Current (A×10)      | uint16_t LE                                             |
| ~offset         | Battery Bulk Voltage (V×100)      | uint16_t LE                                             |
| ~offset         | Battery Float Voltage (V×100)     | uint16_t LE                                             |
| ~offset         | Battery Re-charge Voltage (V×100) | uint16_t LE                                             |

### 4.7 Verificación del CRC en Protocolo 8851

```java
// El CRC16 usa el algoritmo estándar Modbus CRC16
// Se calcula sobre TODOS los bytes del frame excepto los 2 últimos (CRC)
// Ejemplo de verificación:
short crc = modbusCRC16(buffer, bufferLength - 2);

boolean valid = (buffer[bufferLength - 2] == (crc & 0xFF)) && (buffer[bufferLength - 1] == ((crc >> 8) & 0xFF));

```

### 4.8 Recursos para Protocolo 8851

- Ingeniería inversa completa: https://github.com/leodesigner/powmr4500_comm
- Header con todos los registros: `include/inv8851.h` (en el repo anterior)
- ESPHome compatible: https://github.com/lufton/esphome-inv-8851

---

## 5. FAMILIA C — Protocolo PI18/PI41 (MPS-V PLUS, EP Series)

### 5.1 Parámetros de Conexión

| Parámetro | Valor                        |
| --------- | ---------------------------- |
| Interfaz  | RS232 o USB-HID              |
| Baud rate | **2400 bps**                 |
| Formato   | `2400, 8N1`                  |
| CRC       | CRC16 PI30 (mismo algoritmo) |

### 5.2 Diferencias con PI30

El protocolo PI18/PI41 responde igual que PI30 en estructura, pero los campos de `QPIGS` tienen **posiciones y factores distintos** para algunos valores:

| Campo             | PI30              | PI41 (diferencia)            |
| ----------------- | ----------------- | ---------------------------- |
| PV Input Current  | campo 13, × 0.1 A | campo 13, × 1 W (en algunos) |
| Battery Voltage   | × 0.01            | puede ser × 0.1 en PI41      |
| PV Charging Power | campo 20          | campo diferente              |

> Recomendación: Usar la librería `mpp-solar` de Python o el proyecto `Solar2MQTT`  
> y especificar `-P PI41` para estos modelos. Solar Assistant soporta SUMRY  
> solo via RS232 (no RS485) para los modelos con PI30.

---

## 6. Resumen de Identificación Rápida

```
1. Conectar RS232 (9600 bps 8N1 primero, si no funciona probar 2400 bps)
2. Enviar bytes: 51 50 49 BE AC 0D   (= "QPI" + CRC + \r)
3. Resultado:
   - Responde "(PI30..." → Familia A, cambiar a 2400 bps, usar comandos PI30
   - Responde "(PI18..." → Familia C, usar comandos PI18/PI41 a 2400 bps
   - No responde nada → Familia B (HGX), usar protocolo 8851 a 9600 bps
                         Enviar: 88 51 00 03 00 00 00 00 4D 08
```

---

## 7. Comparación de Protocolos SUMRY vs Otros Fabricantes

| Característica    | SUMRY Familia A  | SUMRY Familia B        | MUST Solar         |
| ----------------- | ---------------- | ---------------------- | ------------------ |
| Protocolo         | PI30 (ASCII)     | 8851 (Binario)         | Modbus RTU         |
| Baud rate         | 2400             | 9600                   | 9600–19200         |
| Interfaz          | RS232/USB        | RS232                  | RS485              |
| Slave ID          | N/A              | N/A                    | 0x04               |
| Lectura de estado | Comando QPIGS    | Frame binario          | Reg. 25205–25216   |
| Escritura config  | Comandos P\_\_\_ | Frame binario completo | FC06 en reg. 20xxx |
| Integración Java  | SerialPort ASCII | SerialPort binario     | j2mod / jamod      |

---

## 8. Recursos y Links

| Recurso                                | URL                                                                   |
| -------------------------------------- | --------------------------------------------------------------------- |
| Protocolo 8851 completo (HGX)          | https://github.com/leodesigner/powmr4500_comm                         |
| ESPHome inv-8851                       | https://github.com/lufton/esphome-inv-8851                            |
| Solar2MQTT (PI30)                      | https://github.com/softwarecrash/Solar2MQTT                           |
| Librería mpp-solar (PI30/PI41)         | https://github.com/jblance/mpp-solar                                  |
| Foro comunidad Sumry HGX               | https://diysolarforum.com/threads/sumry-hgx-4kw.81132/                |
| Soporte Solar Assistant para Sumry     | https://solar-assistant.io/help/easun/configuration                   |
| Protocolo Voltronic/Axpert (base PI30) | https://github.com/ardupic/voltronic-inverter-communication-protocols |

---

_Documento generado con datos de protocolo oficial Voltronic PI30, ingeniería inversa del protocolo 8851 (leodesigner/powmr4500_comm), y experiencias de la comunidad DIY Solar Power Forum. Los modelos HGX usan el mismo hardware y protocolo que PowMr 4500/6500._
