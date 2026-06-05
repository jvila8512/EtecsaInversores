# IMPLEMENTACIÓN COMPLETA — MULTI-MASTER

## SQL real + PollingService + Liquibase + React

---

## PARTE 1: DATOS REALES EN BD

### Escenario: 10 inversores Felicity, 2 grupos

```sql
-- ════════════════════════════════════════════════════════════════
-- 1. SITIO
-- ════════════════════════════════════════════════════════════════
INSERT INTO sitio (nombre, ubicacion, activo, fecha_instalacion)
VALUES ('Planta Solar Norte', 'Calle 42, Havana', true, '2025-01-15');
-- id = 1

-- ════════════════════════════════════════════════════════════════
-- 2. MARCA
-- ════════════════════════════════════════════════════════════════
INSERT INTO marca (nombre, fabricante, registro_inicio, registro_fin,
                  registro_info_inicio, registro_info_fin,
                  baud_rate_default, activa)
VALUES ('Felicity', 'Felicity Solar', 4353, 4394, 8192, 8207, 2400, true);
-- id = 1
-- 4353 = 0x1101, 4394 = 0x112A, 8192 = 0x2000, 8207 = 0x200F

-- ════════════════════════════════════════════════════════════════
-- 3. DOS GATEWAYS — uno por grupo
-- ════════════════════════════════════════════════════════════════
INSERT INTO gateway_tcp (nombre, descripcion, ubicacion,
                         direccion_ip, puerto, activo, sitio_id)
VALUES
  ('GW-Techo-Norte', 'Grupo A — techo norte, 5 inversores',
   'Techo norte, sala 1', '192.168.1.10', 502, true, 1),

  ('GW-Techo-Sur',   'Grupo B — techo sur, 5 inversores',
   'Techo sur, sala 2',   '192.168.1.20', 502, true, 1);
-- ids = 1, 2

-- ════════════════════════════════════════════════════════════════
-- 4. INVERSORES — GRUPO A (gateway_id=1, IP 192.168.1.10)
-- ════════════════════════════════════════════════════════════════

-- Master A
INSERT INTO inversor (nombre, es_master, slave_id, tipo_conexion,
                     gateway_id, master_referencia_id,
                     marca_id, sitio_id, activo,
                     periodo_muestreo_seg, fecha_registro)
VALUES ('Felicity-A1', true, 1, 'TcpModbus',
        1, null, 1, 1, true, 5, NOW());
-- id = 1

-- Esclavos A (slave_id 2,3,4,5 — todos apuntan a master_referencia=1)
INSERT INTO inversor (nombre, es_master, slave_id, tipo_conexion,
                     gateway_id, master_referencia_id,
                     marca_id, sitio_id, activo,
                     periodo_muestreo_seg, fecha_registro)
VALUES
  ('Felicity-A2', false, 2, 'TcpModbus', 1, 1, 1, 1, true, 5, NOW()),
  ('Felicity-A3', false, 3, 'TcpModbus', 1, 1, 1, 1, true, 5, NOW()),
  ('Felicity-A4', false, 4, 'TcpModbus', 1, 1, 1, 1, true, 5, NOW()),
  ('Felicity-A5', false, 5, 'TcpModbus', 1, 1, 1, 1, true, 5, NOW());
-- ids = 2,3,4,5

-- ════════════════════════════════════════════════════════════════
-- 5. INVERSORES — GRUPO B (gateway_id=2, IP 192.168.1.20)
-- ════════════════════════════════════════════════════════════════

-- Master B  ← slave_id=1 también, pero en gateway diferente → OK
INSERT INTO inversor (nombre, es_master, slave_id, tipo_conexion,
                     gateway_id, master_referencia_id,
                     marca_id, sitio_id, activo,
                     periodo_muestreo_seg, fecha_registro)
VALUES ('Felicity-B1', true, 1, 'TcpModbus',
        2, null, 1, 1, true, 5, NOW());
-- id = 6

-- Esclavos B
INSERT INTO inversor (nombre, es_master, slave_id, tipo_conexion,
                     gateway_id, master_referencia_id,
                     marca_id, sitio_id, activo,
                     periodo_muestreo_seg, fecha_registro)
VALUES
  ('Felicity-B2', false, 2, 'TcpModbus', 2, 6, 1, 1, true, 5, NOW()),
  ('Felicity-B3', false, 3, 'TcpModbus', 2, 6, 1, 1, true, 5, NOW()),
  ('Felicity-B4', false, 4, 'TcpModbus', 2, 6, 1, 1, true, 5, NOW()),
  ('Felicity-B5', false, 5, 'TcpModbus', 2, 6, 1, 1, true, 5, NOW());
-- ids = 7,8,9,10

-- ════════════════════════════════════════════════════════════════
-- VERIFICACIÓN — Vista de toda la instalación
-- ════════════════════════════════════════════════════════════════
SELECT
  i.nombre                      AS inversor,
  CASE WHEN i.es_master THEN '★ Master' ELSE '  Esclavo' END AS rol,
  i.slave_id,
  g.direccion_ip || ':' || g.puerto  AS gateway,
  m.nombre                      AS master_ref
FROM inversor i
JOIN gateway_tcp g       ON i.gateway_id = g.id
LEFT JOIN inversor m     ON i.master_referencia_id = m.id
ORDER BY g.id, i.slave_id;

-- inversor     | rol       | slave_id | gateway          | master_ref
-- Felicity-A1  | ★ Master  |    1     | 192.168.1.10:502 | null
-- Felicity-A2  |   Esclavo |    2     | 192.168.1.10:502 | Felicity-A1
-- Felicity-A3  |   Esclavo |    3     | 192.168.1.10:502 | Felicity-A1
-- Felicity-A4  |   Esclavo |    4     | 192.168.1.10:502 | Felicity-A1
-- Felicity-A5  |   Esclavo |    5     | 192.168.1.10:502 | Felicity-A1
-- Felicity-B1  | ★ Master  |    1     | 192.168.1.20:502 | null
-- Felicity-B2  |   Esclavo |    2     | 192.168.1.20:502 | Felicity-B1
-- Felicity-B3  |   Esclavo |    3     | 192.168.1.20:502 | Felicity-B1
-- Felicity-B4  |   Esclavo |    4     | 192.168.1.20:502 | Felicity-B1
-- Felicity-B5  |   Esclavo |    5     | 192.168.1.20:502 | Felicity-B1
```

---

## PARTE 2: LIQUIBASE — CONSTRAINT ÚNICO POR GATEWAY

JHipster genera las tablas pero NO sabe que `(gateway_id, slave_id)` debe ser único.
Esto se agrega manualmente en un changeset Liquibase.

```xml
<!-- src/main/resources/config/liquibase/changelog/20260530_constraints_inversor.xml -->

<?xml version="1.0" encoding="utf-8"?>
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
                   http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-latest.xsd">

    <!-- Garantiza que no haya dos inversores con mismo slave_id en el mismo gateway -->
    <changeSet id="20260530-01" author="tu-nombre">
        <addUniqueConstraint
            tableName="inversor"
            columnNames="gateway_id, slave_id"
            constraintName="uq_inversor_gateway_slave_id"/>
        <rollback>
            <dropUniqueConstraint
                tableName="inversor"
                constraintName="uq_inversor_gateway_slave_id"/>
        </rollback>
    </changeSet>

    <!-- Índice para búsquedas por gateway -->
    <changeSet id="20260530-02" author="tu-nombre">
        <createIndex tableName="inversor"
                     indexName="idx_inversor_gateway_id">
            <column name="gateway_id"/>
        </createIndex>
    </changeSet>

    <!-- Índice para búsquedas por master_referencia (obtener esclavos) -->
    <changeSet id="20260530-03" author="tu-nombre">
        <createIndex tableName="inversor"
                     indexName="idx_inversor_master_ref">
            <column name="master_referencia_id"/>
        </createIndex>
    </changeSet>

    <!-- Índice en dato_inversor para series temporales -->
    <changeSet id="20260530-04" author="tu-nombre">
        <createIndex tableName="dato_inversor"
                     indexName="idx_dato_inversor_tiempo">
            <column name="inversor_id"/>
            <column name="tiempo" descending="true"/>
        </createIndex>
    </changeSet>

</databaseChangeLog>
```

Agregar en `master.xml`:

```xml
<include file="config/liquibase/changelog/20260530_constraints_inversor.xml"
         relativeToChangelogFile="false"/>
```

---

## PARTE 3: POLLING SERVICE — MULTI-MASTER

```java
// polling/PollingService.java

@Slf4j
@Service
@RequiredArgsConstructor
public class PollingService {

  private final InversorRepository inversorRepo;
  private final GatewayTcpRepository gatewayRepo;
  private final MapeoRegistroRepository mapeoRepo;
  private final DatoInversorRepository datoRepo;
  private final LiveDataBroadcaster broadcaster;
  private final AlertEngine alertEngine;

  /**
   * UN driver TCP por gateway.
   * Con 2 gateways → 2 drivers.
   * Con 10 gateways → 10 drivers.
   * Cada driver es compartido por todos los inversores de ese gateway.
   *
   * Clave: gateway_id → ModbusTcpDriver
   */
  private final Map<Long, ModbusTcpDriver> driversGateway = new ConcurrentHashMap<>();

  /**
   * Driver serial por inversor independiente (sin gateway).
   * Clave: inversor_id → ModbusSerialDriver
   */
  private final Map<Long, ModbusSerialDriver> driversSerial = new ConcurrentHashMap<>();

  // ──────────────────────────────────────────────────────────────
  // INICIALIZACIÓN: crear 1 driver por gateway
  // ──────────────────────────────────────────────────────────────
  @PostConstruct
  public void init() {
    List<GatewayTcp> gateways = gatewayRepo.findByActivoTrue();

    for (GatewayTcp gw : gateways) {
      ModbusTcpDriver driver = new ModbusTcpDriver(
        gw.getDireccionIp(),
        gw.getPuerto(),
        gw.getTiempoEsperaMs() != null ? gw.getTiempoEsperaMs() : 1000
      );
      driver.connect();
      driversGateway.put(gw.getId(), driver);

      log.info("✓ Driver TCP [{}] {}:{} listo", gw.getNombre(), gw.getDireccionIp(), gw.getPuerto());
    }

    log.info("✓ {} gateways inicializados", driversGateway.size());
  }

  // ──────────────────────────────────────────────────────────────
  // CICLO DE POLLING: cada 1 segundo evalúa quién toca leer
  // ──────────────────────────────────────────────────────────────
  private long tick = 0;

  @Scheduled(fixedDelay = 1000)
  public void ejecutarPolling() {
    tick++;

    List<Inversor> todos = inversorRepo.findByActivoTrue();

    for (Inversor inv : todos) {
      int periodo = inv.getPeriodoMuestreoSeg() != null ? inv.getPeriodoMuestreoSeg() : 5;

      if (tick % periodo != 0) continue;

      try {
        ModbusDriver driver = resolverDriver(inv);
        if (driver == null) continue;
        if (!driver.isConnected()) driver.connect();

        leerInversor(inv, driver);
      } catch (Exception e) {
        marcarError(inv, e.getMessage());
        log.error("[{}] Error: {}", inv.getNombre(), e.getMessage());
      }
    }
  }

  // ──────────────────────────────────────────────────────────────
  // RESOLUCIÓN DE DRIVER
  //
  // Si el inversor tiene gateway → usar el driver del gateway
  //   (compartido entre master y todos sus esclavos)
  // Si no tiene gateway → conexión serial individual
  // ──────────────────────────────────────────────────────────────
  private ModbusDriver resolverDriver(Inversor inv) {
    if (inv.getGateway() != null) {
      ModbusTcpDriver driver = driversGateway.get(inv.getGateway().getId());
      if (driver == null) {
        log.warn("[{}] No hay driver para gateway {}", inv.getNombre(), inv.getGateway().getNombre());
      }
      return driver;
    }

    // Sin gateway → serial directo
    if (inv.getPuertoSerial() != null) {
      return driversSerial.computeIfAbsent(inv.getId(), id -> {
        ModbusSerialDriver d = new ModbusSerialDriver(inv.getPuertoSerial(), inv.getMarca().getBaudRateDefault());
        d.connect();
        return d;
      });
    }

    log.warn("[{}] Sin gateway ni puerto serial — ignorando", inv.getNombre());
    return null;
  }

  // ──────────────────────────────────────────────────────────────
  // LECTURA DE UN INVERSOR
  // El slave_id diferencia a cada inversor dentro del mismo gateway
  // ──────────────────────────────────────────────────────────────
  private void leerInversor(Inversor inv, ModbusDriver driver) throws Exception {
    Marca marca = inv.getMarca();
    int inicio = marca.getRegistroInicio();
    int cantidad = marca.getRegistroFin() - inicio + 1;

    // Aquí está la magia: mismo driver TCP, diferente slave_id
    int[] regs = driver.readHoldingRegisters(
      inv.getSlaveId(), // ← 1=MasterA, 2=EsclavoA2, 3=EsclavoA3...
      inicio, // ← 0x1101 para Felicity
      cantidad // ← 42 registros
    );

    if (regs == null) {
      throw new Exception("Sin respuesta (slave_id=" + inv.getSlaveId() + ")");
    }

    // Mapear raw → DatoInversor
    DatoInversor dato = mapearRegistros(inv, marca, regs, inicio);
    calcularDerivados(dato);

    // Persistir, difundir, alertar
    datoRepo.save(dato);
    broadcaster.broadcast(inv.getId(), dato);
    alertEngine.evaluar(inv, dato);

    // Actualizar estado
    inv.setEstadoDispositivo(EstadoDispositivo.CONECTADO);
    inv.setUltimaLectura(Instant.now());
    inversorRepo.save(inv);
  }

  // ──────────────────────────────────────────────────────────────
  // MAPEO DE REGISTROS (genérico por marca)
  // ──────────────────────────────────────────────────────────────
  private DatoInversor mapearRegistros(Inversor inv, Marca marca, int[] regs, int inicio) {
    DatoInversor dato = new DatoInversor();
    dato.setInversor(inv);
    dato.setTiempo(Instant.now());
    dato.setCrcValido(true);

    mapeoRepo
      .findByMarcaIdAndActivoTrue(marca.getId())
      .forEach(m -> {
        int offset = m.getDireccionRegistro() - inicio;
        if (offset >= 0 && offset < regs.length) {
          asignarCampo(dato, m.getCampoSql(), convertir(regs[offset], m));
        }
      });

    return dato;
  }

  private double convertir(int raw, MapeoRegistro m) {
    double v =
      switch (m.getTipoDato()) {
        case SIGNED_INT16 -> (raw & 0x8000) != 0 ? raw - 65536 : raw;
        default -> raw & 0xFFFF;
      };
    return (m.getEscala() != null && m.getEscala() != 0) ? v / m.getEscala() : v;
  }

  private void calcularDerivados(DatoInversor d) {
    if (d.getPvPotenciaW() != null && d.getPvPotenciaW() > 0 && d.getSalidaPotenciaW() != null) {
      d.setEficienciaPct((d.getSalidaPotenciaW() / d.getPvPotenciaW()) * 100);
    }
    d.setBateriaCargando(d.getBateriaCorrienteA() != null && d.getBateriaCorrienteA() > 0);
    d.setPvActivo(d.getPvPotenciaW() != null && d.getPvPotenciaW() > 10);
    d.setRedActiva(d.getRedVoltajeV() != null && d.getRedVoltajeV() > 100);
  }

  private void asignarCampo(DatoInversor dato, String campo, double valor) {
    try {
      String setter =
        "set" + Arrays.stream(campo.split("_")).map(p -> Character.toUpperCase(p.charAt(0)) + p.substring(1)).collect(Collectors.joining());
      dato.getClass().getMethod(setter, Double.class).invoke(dato, valor);
    } catch (NoSuchMethodException ignored) {} catch (Exception e) {
      log.error("Error asignando {}: {}", campo, e.getMessage());
    }
  }

  private void marcarError(Inversor inv, String msg) {
    inv.setEstadoDispositivo(EstadoDispositivo.ERROR);
    inv.setUltimoError(msg);
    inversorRepo.save(inv);
  }

  // Hot-reload: agregar gateway en runtime sin reiniciar
  public void agregarGateway(GatewayTcp gw) {
    ModbusTcpDriver driver = new ModbusTcpDriver(gw.getDireccionIp(), gw.getPuerto(), 1000);
    driver.connect();
    driversGateway.put(gw.getId(), driver);
    log.info("✓ Gateway agregado en runtime: {}", gw.getNombre());
  }

  // Hot-reload: remover gateway cuando se desactiva
  public void removerGateway(Long gatewayId) {
    ModbusTcpDriver driver = driversGateway.remove(gatewayId);
    if (driver != null) driver.close();
  }
}

```

---

## PARTE 4: REPOSITORIOS CLAVE

```java
// repository/InversorRepository.java

public interface InversorRepository extends JpaRepository<Inversor, Long> {
  // Todos los activos para el polling
  List<Inversor> findByActivoTrue();

  // Todos los masters activos (para inicializar drivers)
  List<Inversor> findByActivoTrueAndEsMasterTrue();

  // Esclavos de un master
  List<Inversor> findByMasterReferencia_IdAndActivoTrue(Long masterId);

  // Todos los inversores de un gateway (master + esclavos)
  List<Inversor> findByGateway_IdAndActivoTrue(Long gatewayId);

  // Grupo completo dado el ID del master
  @Query(
    """
    SELECT i FROM Inversor i
    WHERE i.activo = true
    AND (i.id = :masterId
         OR i.masterReferencia.id = :masterId)
    ORDER BY i.slaveId ASC
    """
  )
  List<Inversor> findGrupoCompleto(@Param("masterId") Long masterId);

  // Verificar si ya existe ese slave_id en el mismo gateway
  @Query(
    """
    SELECT COUNT(i) > 0 FROM Inversor i
    WHERE i.gateway.id = :gatewayId
    AND i.slaveId = :slaveId
    AND i.id <> :inversorId
    """
  )
  boolean slaveIdOcupado(@Param("gatewayId") Long gatewayId, @Param("slaveId") Integer slaveId, @Param("inversorId") Long inversorId);
}

// repository/GatewayTcpRepository.java

public interface GatewayTcpRepository extends JpaRepository<GatewayTcp, Long> {
  List<GatewayTcp> findByActivoTrue();

  // Todos los gateways de un sitio
  List<GatewayTcp> findBySitio_IdAndActivoTrue(Long sitioId);
}

```

---

## PARTE 5: REACT — DASHBOARD MULTI-MASTER

```tsx
// app/modules/dashboard/dashboard-completo.tsx

interface GatewayConGrupo {
  gateway: GatewayDTO;
  master: InversorDTO;
  esclavos: InversorDTO[];
  lecturas: Record<number, DatoInversorDTO>;
}

const DashboardCompleto = () => {
  const [grupos, setGrupos] = useState<GatewayConGrupo[]>([]);

  // Cargar todos los gateways y sus grupos
  useEffect(() => {
    const cargar = async () => {
      const gws = await axios.get('/api/gateway-tcps?activo=true');
      const gruposData: GatewayConGrupo[] = [];

      for (const gw of gws.data) {
        // Todos los inversores de este gateway
        const inversores = await axios.get(`/api/inversores?gatewayId.equals=${gw.id}&activo.equals=true`);
        const master = inversores.data.find((i: any) => i.esMaster);
        const esclavos = inversores.data.filter((i: any) => !i.esMaster);

        if (master) {
          gruposData.push({ gateway: gw, master, esclavos, lecturas: {} });
        }
      }

      setGrupos(gruposData);
    };
    cargar();
  }, []);

  // WebSocket: suscribir a todos los inversores de todos los grupos
  useEffect(() => {
    if (grupos.length === 0) return;

    const token = localStorage.getItem('jhi-authenticationToken');
    const client = new Client({
      webSocketFactory: () => new SockJS('/ws/monitor'),
      connectHeaders: { Authorization: `Bearer ${token}` },
      onConnect: () => {
        grupos.forEach(g => {
          [g.master, ...g.esclavos].forEach(inv => {
            client.subscribe(`/topic/lectura/inversor/${inv.id}`, msg => {
              const lectura = JSON.parse(msg.body);
              setGrupos(prev =>
                prev.map(gr => (gr.gateway.id === g.gateway.id ? { ...gr, lecturas: { ...gr.lecturas, [inv.id]: lectura } } : gr)),
              );
            });
          });
        });
      },
      reconnectDelay: 4000,
    });
    client.activate();
    return () => client.deactivate();
  }, [grupos.length]);

  return (
    <div className="container-fluid py-3">
      <h3>Dashboard — {grupos.length} grupos activos</h3>

      <div className="row g-3">
        {grupos.map(g => (
          <div key={g.gateway.id} className="col-12 col-xl-6">
            <GrupoPanel grupo={g} />
          </div>
        ))}
      </div>
    </div>
  );
};

// ── Panel de UN grupo (1 master + N esclavos) ─────────────────────

const GrupoPanel = ({ grupo }: { grupo: GatewayConGrupo }) => {
  const todos = [grupo.master, ...grupo.esclavos];
  const lecturas = grupo.lecturas;

  // Totales del grupo
  const pvTotal = todos.reduce((s, i) => s + (lecturas[i.id]?.pvPotenciaW ?? 0), 0);
  const cargaTotal = todos.reduce((s, i) => s + (lecturas[i.id]?.salidaPotenciaW ?? 0), 0);
  const batPromedio = todos.length > 0 ? todos.reduce((s, i) => s + (lecturas[i.id]?.bateriaVoltajeV ?? 0), 0) / todos.length : 0;

  return (
    <div className="card shadow-sm h-100">
      {/* Cabecera del grupo */}
      <div className="card-header d-flex justify-content-between align-items-center">
        <div>
          <h5 className="mb-0">{grupo.gateway.nombre}</h5>
          <small className="text-muted">
            {grupo.gateway.direccionIp}:{grupo.gateway.puerto}
            {' · '}
            {todos.length} inversores
          </small>
        </div>
        <span className={`badge bg-${todos.every(i => lecturas[i.id]) ? 'success' : 'warning'}`}>
          {todos.filter(i => lecturas[i.id]).length}/{todos.length} leyendo
        </span>
      </div>

      {/* Totales del grupo */}
      <div className="card-body pb-2">
        <div className="row text-center mb-3">
          <div className="col-4">
            <div className="text-muted small">PV Total</div>
            <div className="fw-bold text-warning fs-5">{(pvTotal / 1000).toFixed(1)} kW</div>
          </div>
          <div className="col-4">
            <div className="text-muted small">Carga Total</div>
            <div className="fw-bold text-info fs-5">{(cargaTotal / 1000).toFixed(1)} kW</div>
          </div>
          <div className="col-4">
            <div className="text-muted small">Bat. Prom.</div>
            <div className="fw-bold text-success fs-5">{batPromedio.toFixed(1)} V</div>
          </div>
        </div>

        {/* Tabla de inversores individuales */}
        <table className="table table-sm table-hover mb-0">
          <thead className="table-light">
            <tr>
              <th>Inversor</th>
              <th>Bat (V)</th>
              <th>PV (W)</th>
              <th>Carga (W)</th>
              <th>Efic (%)</th>
              <th>Estado</th>
            </tr>
          </thead>
          <tbody>
            {todos.map(inv => {
              const l = lecturas[inv.id];
              return (
                <tr key={inv.id} className={inv.esMaster ? 'table-warning' : ''}>
                  <td>
                    {inv.esMaster && (
                      <span className="badge bg-warning text-dark me-1" title="Master">
                        ★
                      </span>
                    )}
                    <span className="fw-medium">{inv.nombre}</span>
                    <span className="text-muted ms-1" style={{ fontSize: 10 }}>
                      [{inv.slaveId}]
                    </span>
                  </td>
                  <td>{l?.bateriaVoltajeV?.toFixed(1) ?? '—'}</td>
                  <td>{l?.pvPotenciaW?.toFixed(0) ?? '—'}</td>
                  <td>{l?.salidaPotenciaW?.toFixed(0) ?? '—'}</td>
                  <td>{l?.eficienciaPct?.toFixed(1) ?? '—'}</td>
                  <td>
                    <span
                      className={`badge bg-${
                        !l
                          ? 'secondary'
                          : inv.estadoDispositivo === 'Conectado'
                            ? 'success'
                            : inv.estadoDispositivo === 'Error'
                              ? 'danger'
                              : 'warning'
                      }`}
                      style={{ fontSize: 10 }}
                    >
                      {!l ? 'Sin datos' : inv.estadoDispositivo}
                    </span>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default DashboardCompleto;
```

---

## PARTE 6: ENDPOINT — REGISTRAR ESCLAVO NUEVO

```java
// web/rest/InversorResource.java  (métodos adicionales)

/**
 * POST /api/inversores/registrar-esclavo
 * Registra un esclavo nuevo bajo un master existente.
 * Hereda: gateway, tipo conexión, marca, sitio, periodo.
 * Solo necesitas: nombre + slaveId + masterId.
 */
@PostMapping("/registrar-esclavo")
public ResponseEntity<InversorDTO> registrarEsclavo(@RequestBody RegistrarEsclavoRequest req) {
  Inversor master = inversorRepo
    .findById(req.masterId())
    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Master no encontrado"));

  if (!master.getEsMaster()) {
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El inversor indicado no es master");
  }

  // Validar que slave_id no esté ocupado en ese gateway
  if (master.getGateway() != null && inversorRepo.slaveIdOcupado(master.getGateway().getId(), req.slaveId(), -1L)) {
    throw new ResponseStatusException(
      HttpStatus.CONFLICT,
      "El slave_id " + req.slaveId() + " ya está en uso en el gateway " + master.getGateway().getNombre()
    );
  }

  Inversor esclavo = new Inversor();
  esclavo.setNombre(req.nombre());
  esclavo.setEsMaster(false);
  esclavo.setMasterReferencia(master);
  esclavo.setGateway(master.getGateway()); // hereda
  esclavo.setTipoConexion(master.getTipoConexion()); // hereda
  esclavo.setMarca(master.getMarca()); // hereda
  esclavo.setSitio(master.getSitio()); // hereda
  esclavo.setPeriodoMuestreoSeg(master.getPeriodoMuestreoSeg()); // hereda
  esclavo.setSlaveId(req.slaveId());
  esclavo.setActivo(true);
  esclavo.setEstadoDispositivo(EstadoDispositivo.INICIALIZANDO);
  esclavo.setFechaRegistro(Instant.now());

  return ResponseEntity.ok(inversorMapper.toDto(inversorRepo.save(esclavo)));
}

record RegistrarEsclavoRequest(Long masterId, String nombre, Integer slaveId) {}

```

---

## RESUMEN FINAL — REGLAS DEL MODELO

```
┌─────────────────────────────────────────────────────────────────┐
│  REGLA 1: slave_id único POR gateway, no globalmente           │
│  → UNIQUE(gateway_id, slave_id) en Liquibase                   │
│  → Master-A y Master-B pueden ser slave_id=1 (gateways ≠)     │
├─────────────────────────────────────────────────────────────────┤
│  REGLA 2: 1 driver TCP por gateway                              │
│  → driversGateway: Map<gateway_id, ModbusTcpDriver>            │
│  → Todos los inversores del gateway comparten ese driver        │
├─────────────────────────────────────────────────────────────────┤
│  REGLA 3: esclavo hereda todo del master                        │
│  → gateway, tipoConexion, marca, sitio, periodo                │
│  → Solo necesitas: nombre + slaveId al registrar               │
├─────────────────────────────────────────────────────────────────┤
│  REGLA 4: N masters = N grupos independientes                   │
│  → Cada master tiene su propio GatewayTcp                      │
│  → El PollingService los maneja todos en paralelo               │
└─────────────────────────────────────────────────────────────────┘
```
