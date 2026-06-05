# Especificación Unificada de Arquitectura y UI: Solar Monitor Multi-Master

Este documento consolida y unifica la **arquitectura técnica del backend (Spring Boot / Modbus TCP/IP)** y las **especificaciones de diseño de la interfaz de usuario (UI/UX - Dark Mode)** para el sistema de monitoreo en tiempo real de inversores solares. Su objetivo es servir de guía definitiva de desarrollo para asegurar la total coherencia entre la base de datos, el motor de comunicaciones en paralelo y la experiencia visual del usuario.

---

## 1. Arquitectura del Sistema e Integración de Protocolos

El sistema está diseñado bajo un paradigma **Multi-Master adaptable**, capaz de soportar cualquier marca de inversor (Felicity, Solis, etc.) y múltiples protocolos o variantes de conexión, centralizando la lógica física en gateways de red.

### 1.1. Modelo Jerárquico de Conexiones

La topología de red se estructura de forma que el Polling Service maneje múltiples hilos de ejecución en paralelo, agrupados por su hardware de comunicación compartido.

- **Sitio (Planta Solar):** Agrupación lógica de la infraestructura física.
- **GatewayTcp (Unidad de Red):** Representa el bridge físico (IP:Puerto). Cada Gateway inicializa **un único driver TCP/IP** en memoria para evitar colisiones de sockets y entrelazado de tramas.
- **Inversor Master:** El inversor principal conectado directamente al Gateway. Mapea la relación directa de red.
- **Inversor Esclavo:** Inversores conectados en cascada RS-485 detrás del inversor Master. Comparten el mismo Gateway de forma transparente, diferenciándose únicamente por su `slave_id`.

```
[ PollingService ]
       │
       ├──► Thread Gateway 1 (192.168.1.10:502) ──► Driver TCP Único
       │         │
       │         ├──► Inversor Master (Slave ID: 1) -> [Felicity]
       │         ├──► Inversor Esclavo (Slave ID: 2) -> [Felicity]
       │         └──► Inversor Esclavo (Slave ID: 3) -> [Felicity]
       │
       └──► Thread Gateway 2 (192.168.1.20:502) ──► Driver TCP Único
                 │
                 └──► Inversor Master (Slave ID: 1) -> [Solis / Otra Marca]
```

### 1.2. Reglas del Modelo de Datos (Restricciones Críticas)

Para asegurar que el sistema soporte cualquier marca y protocolo sin romper la consistencia, el esquema físico (JDL / Liquibase) implementa las siguientes reglas de negocio:

1.  **Restricción de Unicidad Local:** El identificador Modbus (`slave_id`) **no es único globalmente**. Dos inversores diferentes pueden tener `slave_id = 1` siempre y cuando pertenezcan a Gateways de red distintos.
    - _Constraint en BD:_ `UNIQUE(gateway_id, slave_id)`
2.  **Herencia de Configuración:** Los inversores esclavos heredan dinámicamente las propiedades de conectividad de su Master de referencia (`Sitio`, `GatewayTcp`, `Marca`, `Periodo de Polling`). Al registrar un esclavo, solo se requiere su `nombre` y su `slave_id`.

---

## 2. Guía de Identidad Visual (Industrial Precision)

El diseño de la interfaz responde a un entorno crítico e industrial, donde la legibilidad de la telemetría numérica y la velocidad de respuesta ante anomalías son las prioridades absolutas.

### 2.1. Paleta de Colores y Superficies

- **Modo de Visualización:** Oscuro Obligatorio (Dark Mode).
- **Fondo de la Aplicación:** `#0e1511` (Negro verdoso profundo de alta densidad).
- **Superficies de Componentes (Tarjetas/Paneles):** `#161f1a` con bordes sutiles en `#223028`.
- **Color Primario (Acciones/Destacados):** Azul Industrial (`#2563eb`).
- **Semántica de Estados:**
  - _Normal / Seguro:_ Verde Vibrante (`#10b981`)
  - _Alerta Crítica:_ Rojo Brillante (`#ef4444`) (ej. `E05 High Temp`).
  - _Advertencia:_ Ámbar (`#f59e0b`).
- **Tipografía:** `IBM Plex Sans` o `JetBrains Mono` para datos tabulares y lecturas analógicas, garantizando que los números no sufran desalineación por ancho de fuente.
- **Geometría:** Radio de borde estricto de 4px (`ROUND_FOUR`) para todos los elementos contenedores y botones.

---

## 3. Mapeo Completo: Vistas de la App y Flujo de Datos del Backend

A continuación se detalla cómo interactúa cada pantalla definida en el diseño del Frontend con los servicios, entidades y mapas de registros del Backend en Spring Boot.

### 3.1. Vista 1: Dashboard Global (Resumen Ejecutivo)

- **Objetivo de UI:** Mostrar la salud del ecosistema completo de la planta de forma inmediata.
- **Elementos Visuales:**
  - Kpis numéricos grandes en Azul Industrial con la Potencia Total Actual (Suma de `potencia_activa` de todos los inversores en estado activo).
  - Métrica de Eficiencia General de la Planta mediante Gráficos de Donas Reactivos.
  - Mosaico de Estado de Dispositivos: Tarjetas compactas agrupadas por **Gateway/Grupo**. Cada tarjeta representa un inversor (`InversorDTO`).
- **Vinculación con Backend:**
  - Consume un Endpoint agregado (`/api/dashboard/summary`).
  - Determina el color de la tarjeta según el atributo `Working Mode` (Registro `0x1101` para Felicity). Si el modo es `4 (Fault)`, la UI activa dinámicamente un borde rojo brillante (`#ef4444`) con parpadeo suave y el texto de error devuelto por el backend.

### 3.2. Vista 2: Detalle Técnico del Inversor (Detalle de Unidad)

- **Objetivo de UI:** Diagnóstico profundo de una máquina individual. Organizado en pestañas técnicas para evitar la saturación de información.
- **Pestañas de UI e Integración de Registros:**
  - **Tab 1: Energy (Potencia y Red):**
    - Muestra curvas de generación mediante gráficos de área.
    - _Registros Relacionados (Felicity):_ `0x110b` (Total PV Power), `0x1116` (Grid Power).
  - **Tab 2: Voltages (Tensiones y Corrientes):**
    - Tabla comparativa de strings de paneles y fases de CA.
    - _Registros Relacionados (Felicity):_ `0x1107` (PV1 Voltage), `0x1109` (PV2 Voltage), `0x1112` (Grid Voltage).
  - **Tab 3: Temperature (Termodinámica):**
    - Monitoreo de temperatura interna del inversor para prevención de fallas.
    - _Registros Relacionados (Felicity):_ `0x1106` (Inverter Temperature). Requiere conversión mediante la función `toSigned16()` del backend para registrar valores bajo cero de forma exacta.
  - **Tab 4: BMS Summary (Control de Baterías):**
    - Monitoreo exclusivo de acumuladores de litio o JK-BMS integrados. Muestra voltajes de celdas y diferenciales.
    - _Registros Relacionados (Felicity):_ `0x110d` (Battery Voltage - Escala `1/100`), `0x110e` (Battery Current - Signado, positivo para carga, negativo para descarga).

### 3.3. Vista 3: Gestión de Dispositivos e Infraestructura

- **Objetivo de UI:** Configuración, alta y baja de elementos de red de forma estructurada.
- **Elementos Visuales:**
  - **Tabla de Buses de Comunicación:** Columnas claras indicando Nombre del Gateway, Tipo de Conexión (TCP/IP, Serial), Dirección de red (IP/Puerto o Puerto COM).
  - **Contadores de Carga de Sistema:** Métricas de rendimiento del Polling Engine (Latencia media de Gateway en ms y porcentaje de hilos ocupados).
  - **Acciones:** Botón flotante de acción (FAB) en color Azul Industrial (`#2563eb`) con ícono `+` que despliega el formulario inteligente de registro.
- **Vinculación con Backend (Formulario Automatizado):**
  - Al seleccionar que el nuevo dispositivo es un **Esclavo**, la UI oculta dinámicamente los campos de configuración de red (IP, Puerto) y despliega un selector (`Dropdown`) con los Inversores Master existentes.
  - El backend procesa la petición a través de `RegistrarEsclavoRequest`, vinculando el ID del Master seleccionado, clonando la configuración de red internamente en base de datos y validando que el `slave_id` introducido no colisione dentro de ese Gateway específico.

### 3.4. Vista 4: Historial, Análisis y Auditoría

- **Objetivo de UI:** Extracción y análisis de series temporales de datos históricos para auditorías energéticas.
- **Elementos Visuales:**
  - **Filtros Avanzados:** Selectores de fecha nativos, dropdown multiselect de inversores y checkboxes de variables físicas (`Tensión`, `Potencia`, `Temperatura`).
  - **Gráficos Multiserie de Líneas (Recharts):** Representación visual de las variables a lo largo del tiempo. Las anomalías o valores fuera de rango calculados por el sistema se renderizan automáticamente con nodos circulares de color rojo.
  - **Tabla de Logs Detallada:** Visualización pura de datos tabulares con scroll infinito.
- **Vinculación con Backend:**
  - La tabla se alimenta de la entidad `DatoInversor` utilizando paginación dinámica del lado del servidor (`infinite-scroll` / `pagination` especificado en el JDL) para no saturar la memoria del Frontend con millones de registros históricos.
  - **Exportación:** Botón prominente "EXPORT CSV" centrado que invoca directamente al servicio de descarga stream del backend.

---

## 4. Flujo de Control ante Alertas Críticas (Simulación de Fallo)

El siguiente flujo describe el comportamiento en cascada del sistema (desde el hardware hasta la UI) cuando ocurre un evento crítico, por ejemplo, una alerta por alta temperatura (`E05 High Temp`).

```
[ Inversor Físico ] ──► Registra Temperatura > Umbral Máximo
       │
       ▼
[ PollingService ] ──► Lee Registro 0x1106 mediante ModbusTcpDriver
       │
       ▼
[ AlertaConfig / EventoAlerta ] ──► Genera registro en BD con Estado CRÍTICO
       │
       ▼
[ WebSocket / Server-Sent Events ] ──► Notifica al Frontend en tiempo real
       │
       ▼
[ Interfaz React (UI) ]
       ├──► Aplica Borde Rojo (#ef4444) con animación en la tarjeta del Dashboard.
       ├──► Cambia el texto del estado de Working Mode a "E05 High Temp".
       └──► Inserta el evento al tope de la vista de Notificaciones / Registro de Alertas.
```

Este mapa de diseño unificado garantiza que cualquier cambio estructural en el mapa de registros Modbus o en las restricciones de la base de datos tenga un reflejo directo, coherente y estilizado en la interfaz de usuario, manteniendo la robustez del sistema de monitoreo solar industrial.
