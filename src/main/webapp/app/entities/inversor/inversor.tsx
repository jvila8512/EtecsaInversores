import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { JhiPagination, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  Box,
  Button,
  CircularProgress,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  IconButton,
  Menu,
  MenuItem,
  ListItemIcon,
  ListItemText,
  Checkbox,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
  Toolbar,
  Typography,
  FormControlLabel,
  FormControl,
  InputLabel,
  Select,
  Chip,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import RefreshIcon from '@mui/icons-material/Refresh';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import SearchIcon from '@mui/icons-material/Search';
import ViewColumnIcon from '@mui/icons-material/ViewColumn';
import WarningAmberIcon from '@mui/icons-material/WarningAmber';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities, createEntity, updateEntity, deleteEntity } from './inversor.reducer';
import { getEntities as getSitios } from 'app/entities/sitio/sitio.reducer';
import { getEntities as getMarcas } from 'app/entities/marca/marca.reducer';
import { getEntities as getModelos } from 'app/entities/modelo-inversor/modelo-inversor.reducer';
import { getEntities as getGateways } from 'app/entities/gateway-tcp/gateway-tcp.reducer';
import { IInversor } from 'app/shared/model/inversor.model';
import { TipoConexion } from 'app/shared/model/enumerations/tipo-conexion.model';
import { EstadoDispositivo } from 'app/shared/model/enumerations/estado-dispositivo.model';

const ALL_COLUMNS = [
  { key: 'id', label: 'Id', default: true },
  { key: 'nombre', label: 'Nombre', default: true },
  { key: 'numeroSerie', label: 'N° Serie', default: true },
  { key: 'esMaster', label: 'Master', default: true },
  { key: 'tipoConexion', label: 'Tipo Conexión', default: true },
  { key: 'slaveId', label: 'Slave ID', default: false },
  { key: 'activo', label: 'Activo', default: true },
  { key: 'estadoDispositivo', label: 'Estado', default: true },
  { key: 'periodoMuestreoSeg', label: 'Muestreo (s)', default: false },
  { key: 'fechaRegistro', label: 'Fecha Registro', default: false },
  { key: 'ultimaConexion', label: 'Última Conexión', default: false },
  { key: 'sitio', label: 'Sitio', default: true },
  { key: 'marca', label: 'Marca', default: true },
  { key: 'modelo', label: 'Modelo', default: true },
  { key: 'gateway', label: 'Gateway', default: false },
];

const emptyForm = {
  nombre: '',
  numeroSerie: '',
  esMaster: false,
  tipoConexion: '',
  puertoSerial: '',
  slaveId: '',
  activo: false,
  estadoDispositivo: '',
  periodoMuestreoSeg: '',
  fechaRegistro: '',
  sitioId: '',
  marcaId: '',
  modeloId: '',
  gatewayId: '',
  masterReferenciaId: '',
};

const estadoColors: Record<string, 'success' | 'error' | 'default' | 'warning'> = {
  Conectado: 'success',
  Error: 'error',
  Desconectado: 'default',
  Inicializando: 'warning',
};

export const Inversor = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const inversorList = useAppSelector(state => state.inversor.entities);
  const loading = useAppSelector(state => state.inversor.loading);
  const updating = useAppSelector(state => state.inversor.updating);
  const updateSuccess = useAppSelector(state => state.inversor.updateSuccess);
  const totalItems = useAppSelector(state => state.inversor.totalItems);
  const sitioList = useAppSelector(state => state.sitio.entities);
  const marcaList = useAppSelector(state => state.marca.entities);
  const modeloInversorList = useAppSelector(state => state.modeloInversor.entities);
  const gatewayTcpList = useAppSelector(state => state.gatewayTcp.entities);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [selectedEntity, setSelectedEntity] = useState<IInversor | null>(null);
  const [isOpen, setIsOpen] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [globalFilter, setGlobalFilter] = useState('');
  const [columnMenuAnchor, setColumnMenuAnchor] = useState<null | HTMLElement>(null);
  const [visibleColumns, setVisibleColumns] = useState<string[]>(() => {
    const saved = localStorage.getItem('inversor-columns');
    if (saved) return JSON.parse(saved);
    return ALL_COLUMNS.filter(c => c.default).map(c => c.key);
  });
  const [formKey, setFormKey] = useState(0);
  const [formValues, setFormValues] = useState(emptyForm);

  const saveColumns = (cols: string[]) => {
    setVisibleColumns(cols);
    localStorage.setItem('inversor-columns', JSON.stringify(cols));
  };
  const toggleColumn = (key: string) => {
    if (visibleColumns.includes(key)) {
      if (visibleColumns.length > 1) saveColumns(visibleColumns.filter(c => c !== key));
    } else {
      saveColumns([...visibleColumns, key]);
    }
  };
  const showColumn = (key: string) => visibleColumns.includes(key);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };
  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) navigate(`${pageLocation.pathname}${endURL}`);
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);
  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({ ...paginationState, activePage: +page, sort: sortSplit[0], order: sortSplit[1] });
    }
  }, [pageLocation.search]);
  useEffect(() => {
    if (updateSuccess) {
      setIsOpen(false);
      setSelectedEntity(null);
      setFormValues(emptyForm);
    }
  }, [updateSuccess]);
  useEffect(() => {
    dispatch(getSitios({ page: 0, size: 1000, sort: 'id,asc' }));
    dispatch(getMarcas({ page: 0, size: 1000, sort: 'id,asc' }));
    dispatch(getModelos({ page: 0, size: 1000, sort: 'id,asc' }));
    dispatch(getGateways({ page: 0, size: 1000, sort: 'id,asc' }));
  }, []);

  const sort = (field: string) =>
    setPaginationState({ ...paginationState, order: paginationState.order === ASC ? DESC : ASC, sort: field });
  const handlePagination = (currentPage: number) => setPaginationState({ ...paginationState, activePage: currentPage });
  const handleSyncList = () => sortEntities();

  const filteredList = inversorList?.filter(
    e =>
      !globalFilter ||
      e.nombre?.toLowerCase().includes(globalFilter.toLowerCase()) ||
      e.numeroSerie?.toLowerCase().includes(globalFilter.toLowerCase()) ||
      e.sitio?.nombre?.toLowerCase().includes(globalFilter.toLowerCase()),
  );

  const openNew = () => {
    setSelectedEntity(null);
    setFormValues(emptyForm);
    setFormKey(prev => prev + 1);
    setIsOpen(true);
  };
  const openEdit = (row: IInversor) => {
    setSelectedEntity(row);
    setFormValues({
      nombre: row.nombre || '',
      numeroSerie: row.numeroSerie || '',
      esMaster: row.esMaster ?? false,
      tipoConexion: row.tipoConexion || '',
      puertoSerial: row.puertoSerial || '',
      slaveId: row.slaveId?.toString() || '',
      activo: row.activo ?? false,
      estadoDispositivo: row.estadoDispositivo || '',
      periodoMuestreoSeg: row.periodoMuestreoSeg?.toString() || '',
      fechaRegistro: row.fechaRegistro ? row.fechaRegistro.format('YYYY-MM-DD') : '',
      sitioId: row.sitio?.id?.toString() || '',
      marcaId: row.marca?.id?.toString() || '',
      modeloId: row.modelo?.id?.toString() || '',
      gatewayId: row.gateway?.id?.toString() || '',
      masterReferenciaId: row.masterReferencia?.id?.toString() || '',
    });
    setFormKey(prev => prev + 1);
    setIsOpen(true);
  };
  const closeDialog = () => {
    setIsOpen(false);
    setSelectedEntity(null);
    setFormValues(emptyForm);
  };
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setFormValues(prev => ({ ...prev, [name]: type === 'checkbox' ? checked : value }));
  };
  const save = () => {
    const entity: any = {
      nombre: formValues.nombre,
      numeroSerie: formValues.numeroSerie || null,
      esMaster: formValues.esMaster,
      tipoConexion: formValues.tipoConexion || null,
      puertoSerial: formValues.puertoSerial || null,
      slaveId: formValues.slaveId ? parseInt(formValues.slaveId, 10) : undefined,
      activo: formValues.activo,
      estadoDispositivo: formValues.estadoDispositivo || null,
      periodoMuestreoSeg: formValues.periodoMuestreoSeg ? parseInt(formValues.periodoMuestreoSeg, 10) : null,
      fechaRegistro: formValues.fechaRegistro || null,
      sitio: formValues.sitioId ? { id: parseInt(formValues.sitioId, 10) } : null,
      marca: formValues.marcaId ? { id: parseInt(formValues.marcaId, 10) } : null,
      modelo: formValues.modeloId ? { id: parseInt(formValues.modeloId, 10) } : null,
      gateway: formValues.gatewayId ? { id: parseInt(formValues.gatewayId, 10) } : null,
      masterReferencia: formValues.masterReferenciaId ? { id: parseInt(formValues.masterReferenciaId, 10) } : null,
    };
    if (selectedEntity?.id) entity.id = selectedEntity.id;
    dispatch(selectedEntity?.id ? updateEntity(entity) : createEntity(entity));
  };
  const openDelete = (row: IInversor) => {
    setSelectedEntity(row);
    setIsDeleting(true);
  };
  const confirmDelete = () => {
    if (selectedEntity?.id) dispatch(deleteEntity(selectedEntity.id));
    setIsDeleting(false);
    setSelectedEntity(null);
  };
  const closeDelete = () => {
    setIsDeleting(false);
    setSelectedEntity(null);
  };

  return (
    <Paper sx={{ p: 3 }}>
      <Toolbar sx={{ justifyContent: 'space-between', mb: 2, px: 0 }}>
        <Typography variant="h5" sx={{ fontWeight: 700 }}>
          Inversores
        </Typography>
        <Box sx={{ display: 'flex', gap: 1 }}>
          <Button variant="outlined" startIcon={<ViewColumnIcon />} onClick={e => setColumnMenuAnchor(e.currentTarget)}>
            Columnas
          </Button>
          <Menu anchorEl={columnMenuAnchor} open={Boolean(columnMenuAnchor)} onClose={() => setColumnMenuAnchor(null)}>
            {ALL_COLUMNS.map(col => (
              <MenuItem key={col.key} onClick={() => toggleColumn(col.key)}>
                <ListItemIcon>
                  <Checkbox checked={visibleColumns.includes(col.key)} size="small" />
                </ListItemIcon>
                <ListItemText>{col.label}</ListItemText>
              </MenuItem>
            ))}
          </Menu>
          <Button variant="outlined" startIcon={<RefreshIcon />} onClick={handleSyncList} disabled={loading}>
            Actualizar
          </Button>
          <Button variant="contained" startIcon={<AddIcon />} onClick={openNew}>
            Nuevo Inversor
          </Button>
        </Box>
      </Toolbar>

      {loading && !inversorList?.length && (
        <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
          <CircularProgress size={40} sx={{ color: 'primary.main' }} />
        </Box>
      )}

      <TextField
        size="small"
        placeholder="Buscar..."
        value={globalFilter}
        onChange={e => setGlobalFilter(e.target.value)}
        InputProps={{ startAdornment: <SearchIcon sx={{ color: 'text.secondary', mr: 1 }} /> }}
        sx={{ mb: 2, width: 300 }}
      />

      <TableContainer sx={{ overflowX: 'auto' }}>
        <Table sx={{ minWidth: 1100 }}>
          <TableHead>
            <TableRow sx={{ bgcolor: '#f8fafc' }}>
              {showColumn('id') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('id')}>
                  Id
                </TableCell>
              )}
              {showColumn('nombre') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('nombre')}>
                  Nombre
                </TableCell>
              )}
              {showColumn('numeroSerie') && <TableCell sx={{ fontWeight: 600 }}>N° Serie</TableCell>}
              {showColumn('esMaster') && <TableCell sx={{ fontWeight: 600 }}>Master</TableCell>}
              {showColumn('tipoConexion') && <TableCell sx={{ fontWeight: 600 }}>Tipo Conexión</TableCell>}
              {showColumn('slaveId') && <TableCell sx={{ fontWeight: 600 }}>Slave ID</TableCell>}
              {showColumn('activo') && <TableCell sx={{ fontWeight: 600 }}>Activo</TableCell>}
              {showColumn('estadoDispositivo') && <TableCell sx={{ fontWeight: 600 }}>Estado</TableCell>}
              {showColumn('periodoMuestreoSeg') && <TableCell sx={{ fontWeight: 600 }}>Muestreo (s)</TableCell>}
              {showColumn('fechaRegistro') && <TableCell sx={{ fontWeight: 600 }}>Fecha Registro</TableCell>}
              {showColumn('ultimaConexion') && <TableCell sx={{ fontWeight: 600 }}>Última Conexión</TableCell>}
              {showColumn('sitio') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('sitio.nombre')}>
                  Sitio
                </TableCell>
              )}
              {showColumn('marca') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('marca.nombre')}>
                  Marca
                </TableCell>
              )}
              {showColumn('modelo') && <TableCell sx={{ fontWeight: 600 }}>Modelo</TableCell>}
              {showColumn('gateway') && <TableCell sx={{ fontWeight: 600 }}>Gateway</TableCell>}
              <TableCell sx={{ fontWeight: 600 }}>Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredList?.map((row: IInversor) => (
              <TableRow key={row.id} hover>
                {showColumn('id') && <TableCell>{row.id}</TableCell>}
                {showColumn('nombre') && <TableCell>{row.nombre}</TableCell>}
                {showColumn('numeroSerie') && <TableCell>{row.numeroSerie}</TableCell>}
                {showColumn('esMaster') && <TableCell>{row.esMaster ? 'Sí' : 'No'}</TableCell>}
                {showColumn('tipoConexion') && <TableCell>{row.tipoConexion}</TableCell>}
                {showColumn('slaveId') && <TableCell>{row.slaveId}</TableCell>}
                {showColumn('activo') && <TableCell>{row.activo ? 'Sí' : 'No'}</TableCell>}
                {showColumn('estadoDispositivo') && (
                  <TableCell>
                    <Chip label={row.estadoDispositivo} color={estadoColors[row.estadoDispositivo || ''] || 'default'} size="small" />
                  </TableCell>
                )}
                {showColumn('periodoMuestreoSeg') && <TableCell>{row.periodoMuestreoSeg}</TableCell>}
                {showColumn('fechaRegistro') && <TableCell>{row.fechaRegistro?.format('DD/MM/YYYY')}</TableCell>}
                {showColumn('ultimaConexion') && <TableCell>{row.ultimaConexion?.format('DD/MM/YYYY HH:mm')}</TableCell>}
                {showColumn('sitio') && <TableCell>{row.sitio?.nombre}</TableCell>}
                {showColumn('marca') && <TableCell>{row.marca?.nombre}</TableCell>}
                {showColumn('modelo') && <TableCell>{row.modelo?.nombre}</TableCell>}
                {showColumn('gateway') && <TableCell>{row.gateway?.nombre}</TableCell>}
                <TableCell>
                  <Box sx={{ display: 'flex', gap: 0.5 }}>
                    <IconButton size="small" color="warning" onClick={() => openEdit(row)}>
                      <EditIcon fontSize="small" />
                    </IconButton>
                    <IconButton size="small" color="error" onClick={() => openDelete(row)}>
                      <DeleteIcon fontSize="small" />
                    </IconButton>
                  </Box>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {totalItems && inversorList && inversorList.length > 0 ? (
        <Box sx={{ mt: 2, display: 'flex', justifyContent: 'center' }}>
          <JhiPagination
            activePage={paginationState.activePage}
            onSelect={handlePagination}
            maxButtons={5}
            itemsPerPage={paginationState.itemsPerPage}
            totalItems={totalItems}
          />
        </Box>
      ) : null}

      <Dialog open={isOpen} onClose={closeDialog} maxWidth="sm" fullWidth>
        <DialogTitle>{selectedEntity ? 'Editar Inversor' : 'Nuevo Inversor'}</DialogTitle>
        <DialogContent>
          {loading ? (
            <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
              <CircularProgress />
            </Box>
          ) : (
            <form key={formKey}>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 1 }}>
                <TextField
                  label="Nombre"
                  id="inversor-nombre"
                  name="nombre"
                  value={formValues.nombre}
                  onChange={handleInputChange}
                  fullWidth
                  required
                  error={!formValues.nombre}
                  helperText={!formValues.nombre ? 'Requerido' : ''}
                />
                <TextField
                  label="Número de Serie"
                  id="inversor-numeroSerie"
                  name="numeroSerie"
                  value={formValues.numeroSerie}
                  onChange={handleInputChange}
                  fullWidth
                />
                <FormControlLabel
                  control={<Checkbox name="esMaster" checked={formValues.esMaster} onChange={handleInputChange} />}
                  label="Es Master"
                />
                <FormControl fullWidth>
                  <InputLabel id="inversor-tipoConexion-label">Tipo Conexión</InputLabel>
                  <Select
                    labelId="inversor-tipoConexion-label"
                    id="inversor-tipoConexion"
                    name="tipoConexion"
                    value={formValues.tipoConexion}
                    onChange={e => setFormValues(prev => ({ ...prev, tipoConexion: e.target.value }))}
                    label="Tipo Conexión"
                  >
                    <MenuItem value="">— Seleccionar —</MenuItem>
                    {Object.keys(TipoConexion).map(k => (
                      <MenuItem key={k} value={k}>
                        {TipoConexion[k as keyof typeof TipoConexion]}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
                <TextField
                  label="Puerto Serial"
                  id="inversor-puertoSerial"
                  name="puertoSerial"
                  value={formValues.puertoSerial}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Slave ID"
                  id="inversor-slaveId"
                  name="slaveId"
                  type="number"
                  value={formValues.slaveId}
                  onChange={handleInputChange}
                  fullWidth
                  required
                  error={!formValues.slaveId}
                  helperText={!formValues.slaveId ? 'Requerido' : ''}
                />
                <FormControlLabel
                  control={<Checkbox name="activo" checked={formValues.activo} onChange={handleInputChange} />}
                  label="Activo"
                />
                <FormControl fullWidth>
                  <InputLabel id="inversor-estado-label">Estado Dispositivo</InputLabel>
                  <Select
                    labelId="inversor-estado-label"
                    id="inversor-estadoDispositivo"
                    name="estadoDispositivo"
                    value={formValues.estadoDispositivo}
                    onChange={e => setFormValues(prev => ({ ...prev, estadoDispositivo: e.target.value }))}
                    label="Estado Dispositivo"
                  >
                    <MenuItem value="">— Seleccionar —</MenuItem>
                    {Object.keys(EstadoDispositivo).map(k => (
                      <MenuItem key={k} value={k}>
                        {EstadoDispositivo[k as keyof typeof EstadoDispositivo]}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
                <TextField
                  label="Periodo Muestreo (seg)"
                  id="inversor-periodoMuestreoSeg"
                  name="periodoMuestreoSeg"
                  type="number"
                  value={formValues.periodoMuestreoSeg}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Fecha Registro"
                  id="inversor-fechaRegistro"
                  name="fechaRegistro"
                  type="date"
                  value={formValues.fechaRegistro}
                  onChange={handleInputChange}
                  fullWidth
                  InputLabelProps={{ shrink: true }}
                />
                <FormControl fullWidth>
                  <InputLabel id="inversor-sitio-label">Sitio</InputLabel>
                  <Select
                    labelId="inversor-sitio-label"
                    id="inversor-sitioId"
                    name="sitioId"
                    value={formValues.sitioId}
                    onChange={e => setFormValues(prev => ({ ...prev, sitioId: e.target.value }))}
                    label="Sitio"
                  >
                    <MenuItem value="">— Ninguno —</MenuItem>
                    {sitioList?.map((s: any) => (
                      <MenuItem key={s.id} value={s.id}>
                        {s.nombre}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
                <FormControl fullWidth>
                  <InputLabel id="inversor-marca-label">Marca</InputLabel>
                  <Select
                    labelId="inversor-marca-label"
                    id="inversor-marcaId"
                    name="marcaId"
                    value={formValues.marcaId}
                    onChange={e => setFormValues(prev => ({ ...prev, marcaId: e.target.value }))}
                    label="Marca"
                  >
                    <MenuItem value="">— Ninguna —</MenuItem>
                    {marcaList?.map((m: any) => (
                      <MenuItem key={m.id} value={m.id}>
                        {m.nombre}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
                <FormControl fullWidth>
                  <InputLabel id="inversor-modelo-label">Modelo</InputLabel>
                  <Select
                    labelId="inversor-modelo-label"
                    id="inversor-modeloId"
                    name="modeloId"
                    value={formValues.modeloId}
                    onChange={e => setFormValues(prev => ({ ...prev, modeloId: e.target.value }))}
                    label="Modelo"
                  >
                    <MenuItem value="">— Ninguno —</MenuItem>
                    {modeloInversorList?.map((m: any) => (
                      <MenuItem key={m.id} value={m.id}>
                        {m.nombre}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
                <FormControl fullWidth>
                  <InputLabel id="inversor-gateway-label">Gateway</InputLabel>
                  <Select
                    labelId="inversor-gateway-label"
                    id="inversor-gatewayId"
                    name="gatewayId"
                    value={formValues.gatewayId}
                    onChange={e => setFormValues(prev => ({ ...prev, gatewayId: e.target.value }))}
                    label="Gateway"
                  >
                    <MenuItem value="">— Ninguno —</MenuItem>
                    {gatewayTcpList?.map((g: any) => (
                      <MenuItem key={g.id} value={g.id}>
                        {g.nombre}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
                <FormControl fullWidth>
                  <InputLabel id="inversor-master-label">Master Referencia</InputLabel>
                  <Select
                    labelId="inversor-master-label"
                    id="inversor-masterReferenciaId"
                    name="masterReferenciaId"
                    value={formValues.masterReferenciaId}
                    onChange={e => setFormValues(prev => ({ ...prev, masterReferenciaId: e.target.value }))}
                    label="Master Referencia"
                  >
                    <MenuItem value="">— Ninguno —</MenuItem>
                    {inversorList
                      ?.filter((i: any) => i.esMaster)
                      .map((i: any) => (
                        <MenuItem key={i.id} value={i.id}>
                          {i.nombre}
                        </MenuItem>
                      ))}
                  </Select>
                </FormControl>
                <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 1, mt: 2 }}>
                  <Button onClick={closeDialog}>Cancelar</Button>
                  <Button variant="contained" onClick={save} disabled={updating || !formValues.nombre || !formValues.slaveId}>
                    <FontAwesomeIcon icon="save" />
                    &nbsp;Guardar
                  </Button>
                </Box>
              </Box>
            </form>
          )}
        </DialogContent>
      </Dialog>

      <Dialog open={isDeleting} onClose={closeDelete} maxWidth="xs" fullWidth>
        <DialogTitle>Confirmar</DialogTitle>
        <DialogContent>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, py: 1 }}>
            <WarningAmberIcon sx={{ fontSize: 40, color: '#f59e0b' }} />
            {selectedEntity && (
              <Typography>
                ¿Seguro que quiere eliminar el Inversor: <strong>{selectedEntity.nombre}</strong>?
              </Typography>
            )}
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeDelete}>No</Button>
          <Button onClick={confirmDelete} color="error" variant="contained">
            Sí
          </Button>
        </DialogActions>
      </Dialog>
    </Paper>
  );
};

export default Inversor;
