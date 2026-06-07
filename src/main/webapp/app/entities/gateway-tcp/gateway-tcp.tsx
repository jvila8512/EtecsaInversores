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
import { getEntities, createEntity, updateEntity, deleteEntity } from './gateway-tcp.reducer';
import { getEntities as getSitios } from 'app/entities/sitio/sitio.reducer';
import { IGatewayTcp } from 'app/shared/model/gateway-tcp.model';
import { EstadoDispositivo } from 'app/shared/model/enumerations/estado-dispositivo.model';

const ALL_COLUMNS = [
  { key: 'id', label: 'Id', default: true },
  { key: 'nombre', label: 'Nombre', default: true },
  { key: 'descripcion', label: 'Descripción', default: false },
  { key: 'ubicacion', label: 'Ubicación', default: true },
  { key: 'direccionIp', label: 'Dirección IP', default: true },
  { key: 'puerto', label: 'Puerto', default: true },
  { key: 'activo', label: 'Activo', default: true },
  { key: 'estadoConexion', label: 'Estado', default: true },
  { key: 'ultimaLectura', label: 'Última Lectura', default: false },
  { key: 'ultimoError', label: 'Último Error', default: false },
  { key: 'tiempoEsperaMs', label: 'Timeout (ms)', default: false },
  { key: 'reintentos', label: 'Reintentos', default: false },
  { key: 'sitio', label: 'Sitio', default: true },
];

const emptyForm = {
  nombre: '',
  descripcion: '',
  ubicacion: '',
  direccionIp: '',
  puerto: '',
  activo: false,
  estadoConexion: '',
  ultimaLectura: '',
  ultimoError: '',
  tiempoEsperaMs: '',
  reintentos: '',
  sitioId: '',
};

const estadoColors: Record<string, 'success' | 'error' | 'default' | 'warning'> = {
  Conectado: 'success',
  Error: 'error',
  Desconectado: 'default',
  Inicializando: 'warning',
};

export const GatewayTcp = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const gatewayTcpList = useAppSelector(state => state.gatewayTcp.entities);
  const loading = useAppSelector(state => state.gatewayTcp.loading);
  const updating = useAppSelector(state => state.gatewayTcp.updating);
  const updateSuccess = useAppSelector(state => state.gatewayTcp.updateSuccess);
  const totalItems = useAppSelector(state => state.gatewayTcp.totalItems);
  const sitioList = useAppSelector(state => state.sitio.entities);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [selectedEntity, setSelectedEntity] = useState<IGatewayTcp | null>(null);
  const [isOpen, setIsOpen] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [globalFilter, setGlobalFilter] = useState('');
  const [columnMenuAnchor, setColumnMenuAnchor] = useState<null | HTMLElement>(null);
  const [visibleColumns, setVisibleColumns] = useState<string[]>(() => {
    const saved = localStorage.getItem('gateway-tcp-columns');
    if (saved) return JSON.parse(saved);
    return ALL_COLUMNS.filter(c => c.default).map(c => c.key);
  });
  const [formKey, setFormKey] = useState(0);
  const [formValues, setFormValues] = useState(emptyForm);

  const saveColumns = (cols: string[]) => {
    setVisibleColumns(cols);
    localStorage.setItem('gateway-tcp-columns', JSON.stringify(cols));
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
  }, []);

  const sort = (field: string) =>
    setPaginationState({ ...paginationState, order: paginationState.order === ASC ? DESC : ASC, sort: field });
  const handlePagination = (currentPage: number) => setPaginationState({ ...paginationState, activePage: currentPage });
  const handleSyncList = () => sortEntities();

  const filteredList = gatewayTcpList?.filter(
    e =>
      !globalFilter ||
      e.nombre?.toLowerCase().includes(globalFilter.toLowerCase()) ||
      e.direccionIp?.toLowerCase().includes(globalFilter.toLowerCase()) ||
      e.ubicacion?.toLowerCase().includes(globalFilter.toLowerCase()),
  );

  const openNew = () => {
    setSelectedEntity(null);
    setFormValues(emptyForm);
    setFormKey(prev => prev + 1);
    setIsOpen(true);
  };
  const openEdit = (row: IGatewayTcp) => {
    setSelectedEntity(row);
    setFormValues({
      nombre: row.nombre || '',
      descripcion: row.descripcion || '',
      ubicacion: row.ubicacion || '',
      direccionIp: row.direccionIp || '',
      puerto: row.puerto?.toString() || '',
      activo: row.activo ?? false,
      estadoConexion: row.estadoConexion || '',
      ultimaLectura: '',
      ultimoError: row.ultimoError || '',
      tiempoEsperaMs: row.tiempoEsperaMs?.toString() || '',
      reintentos: row.reintentos?.toString() || '',
      sitioId: row.sitio?.id?.toString() || '',
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
      descripcion: formValues.descripcion || null,
      ubicacion: formValues.ubicacion || null,
      direccionIp: formValues.direccionIp,
      puerto: formValues.puerto ? parseInt(formValues.puerto, 10) : undefined,
      activo: formValues.activo,
      estadoConexion: formValues.estadoConexion || null,
      ultimoError: formValues.ultimoError || null,
      tiempoEsperaMs: formValues.tiempoEsperaMs ? parseInt(formValues.tiempoEsperaMs, 10) : null,
      reintentos: formValues.reintentos ? parseInt(formValues.reintentos, 10) : null,
      sitio: formValues.sitioId ? { id: parseInt(formValues.sitioId, 10) } : null,
    };
    if (selectedEntity?.id) entity.id = selectedEntity.id;
    dispatch(selectedEntity?.id ? updateEntity(entity) : createEntity(entity));
  };
  const openDelete = (row: IGatewayTcp) => {
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
          Gateways TCP
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
            Nuevo Gateway
          </Button>
        </Box>
      </Toolbar>

      {loading && !gatewayTcpList?.length && (
        <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
          <CircularProgress size={40} sx={{ color: '#2563eb' }} />
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
        <Table sx={{ minWidth: 900 }}>
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
              {showColumn('descripcion') && <TableCell sx={{ fontWeight: 600 }}>Descripción</TableCell>}
              {showColumn('ubicacion') && <TableCell sx={{ fontWeight: 600 }}>Ubicación</TableCell>}
              {showColumn('direccionIp') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('direccionIp')}>
                  Dirección IP
                </TableCell>
              )}
              {showColumn('puerto') && <TableCell sx={{ fontWeight: 600 }}>Puerto</TableCell>}
              {showColumn('activo') && <TableCell sx={{ fontWeight: 600 }}>Activo</TableCell>}
              {showColumn('estadoConexion') && <TableCell sx={{ fontWeight: 600 }}>Estado</TableCell>}
              {showColumn('ultimaLectura') && <TableCell sx={{ fontWeight: 600 }}>Última Lectura</TableCell>}
              {showColumn('ultimoError') && <TableCell sx={{ fontWeight: 600 }}>Último Error</TableCell>}
              {showColumn('tiempoEsperaMs') && <TableCell sx={{ fontWeight: 600 }}>Timeout (ms)</TableCell>}
              {showColumn('reintentos') && <TableCell sx={{ fontWeight: 600 }}>Reintentos</TableCell>}
              {showColumn('sitio') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('sitio.nombre')}>
                  Sitio
                </TableCell>
              )}
              <TableCell sx={{ fontWeight: 600 }}>Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredList?.map((row: IGatewayTcp) => (
              <TableRow key={row.id} hover>
                {showColumn('id') && <TableCell>{row.id}</TableCell>}
                {showColumn('nombre') && <TableCell>{row.nombre}</TableCell>}
                {showColumn('descripcion') && (
                  <TableCell sx={{ maxWidth: 200, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                    {row.descripcion}
                  </TableCell>
                )}
                {showColumn('ubicacion') && <TableCell>{row.ubicacion}</TableCell>}
                {showColumn('direccionIp') && <TableCell>{row.direccionIp}</TableCell>}
                {showColumn('puerto') && <TableCell>{row.puerto}</TableCell>}
                {showColumn('activo') && <TableCell>{row.activo ? 'Sí' : 'No'}</TableCell>}
                {showColumn('estadoConexion') && (
                  <TableCell>
                    <Chip label={row.estadoConexion} color={estadoColors[row.estadoConexion || ''] || 'default'} size="small" />
                  </TableCell>
                )}
                {showColumn('ultimaLectura') && <TableCell>{row.ultimaLectura?.format('DD/MM/YYYY HH:mm')}</TableCell>}
                {showColumn('ultimoError') && (
                  <TableCell sx={{ maxWidth: 200, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                    {row.ultimoError}
                  </TableCell>
                )}
                {showColumn('tiempoEsperaMs') && <TableCell>{row.tiempoEsperaMs}</TableCell>}
                {showColumn('reintentos') && <TableCell>{row.reintentos}</TableCell>}
                {showColumn('sitio') && <TableCell>{row.sitio?.nombre}</TableCell>}
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

      {totalItems && gatewayTcpList && gatewayTcpList.length > 0 ? (
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
        <DialogTitle>{selectedEntity ? 'Editar Gateway TCP' : 'Nuevo Gateway TCP'}</DialogTitle>
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
                  id="gateway-nombre"
                  name="nombre"
                  value={formValues.nombre}
                  onChange={handleInputChange}
                  fullWidth
                  required
                  error={!formValues.nombre}
                  helperText={!formValues.nombre ? 'Requerido' : ''}
                />
                <TextField
                  label="Descripción"
                  id="gateway-descripcion"
                  name="descripcion"
                  value={formValues.descripcion}
                  onChange={handleInputChange}
                  fullWidth
                  multiline
                  rows={2}
                />
                <TextField
                  label="Ubicación"
                  id="gateway-ubicacion"
                  name="ubicacion"
                  value={formValues.ubicacion}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Dirección IP"
                  id="gateway-direccionIp"
                  name="direccionIp"
                  value={formValues.direccionIp}
                  onChange={handleInputChange}
                  fullWidth
                  required
                  error={!formValues.direccionIp}
                  helperText={!formValues.direccionIp ? 'Requerido' : ''}
                />
                <TextField
                  label="Puerto"
                  id="gateway-puerto"
                  name="puerto"
                  type="number"
                  value={formValues.puerto}
                  onChange={handleInputChange}
                  fullWidth
                  required
                  error={!formValues.puerto}
                  helperText={!formValues.puerto ? 'Requerido' : ''}
                />
                <FormControlLabel
                  control={<Checkbox name="activo" checked={formValues.activo} onChange={handleInputChange} />}
                  label="Activo"
                />
                <FormControl fullWidth>
                  <InputLabel id="gateway-estado-label">Estado Conexión</InputLabel>
                  <Select
                    labelId="gateway-estado-label"
                    id="gateway-estadoConexion"
                    name="estadoConexion"
                    value={formValues.estadoConexion}
                    onChange={e => setFormValues(prev => ({ ...prev, estadoConexion: e.target.value }))}
                    label="Estado Conexión"
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
                  label="Último Error"
                  id="gateway-ultimoError"
                  name="ultimoError"
                  value={formValues.ultimoError}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Timeout (ms)"
                  id="gateway-tiempoEsperaMs"
                  name="tiempoEsperaMs"
                  type="number"
                  value={formValues.tiempoEsperaMs}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Reintentos"
                  id="gateway-reintentos"
                  name="reintentos"
                  type="number"
                  value={formValues.reintentos}
                  onChange={handleInputChange}
                  fullWidth
                />
                <FormControl fullWidth>
                  <InputLabel id="gateway-sitio-label">Sitio</InputLabel>
                  <Select
                    labelId="gateway-sitio-label"
                    id="gateway-sitioId"
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
                <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 1, mt: 2 }}>
                  <Button onClick={closeDialog}>Cancelar</Button>
                  <Button
                    variant="contained"
                    onClick={save}
                    disabled={updating || !formValues.nombre || !formValues.direccionIp || !formValues.puerto}
                  >
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
                ¿Seguro que quiere eliminar el Gateway: <strong>{selectedEntity.nombre}</strong>?
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

export default GatewayTcp;
