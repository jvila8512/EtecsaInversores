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
import { getEntities, createEntity, updateEntity, deleteEntity } from './evento-alerta.reducer';
import { getEntities as getInversores } from 'app/entities/inversor/inversor.reducer';
import { getEntities as getAlertaConfigs } from 'app/entities/alerta-config/alerta-config.reducer';
import { IEventoAlerta } from 'app/shared/model/evento-alerta.model';

const ALL_COLUMNS = [
  { key: 'id', label: 'Id', default: true },
  { key: 'timestamp', label: 'Timestamp', default: true },
  { key: 'nombreAlerta', label: 'Nombre Alerta', default: true },
  { key: 'mensaje', label: 'Mensaje', default: true },
  { key: 'campoMonitorizado', label: 'Campo', default: false },
  { key: 'valorDetectado', label: 'Valor Detectado', default: true },
  { key: 'umbral', label: 'Umbral', default: true },
  { key: 'operador', label: 'Operador', default: false },
  { key: 'nivelSeveridad', label: 'Severidad', default: true },
  { key: 'notificacionEnviada', label: 'Notif. Enviada', default: false },
  { key: 'canalUsado', label: 'Canal', default: false },
  { key: 'resuelto', label: 'Resuelto', default: true },
  { key: 'fechaResolucion', label: 'Fecha Resolución', default: false },
  { key: 'inversor', label: 'Inversor', default: true },
  { key: 'alertaConfig', label: 'Alerta Config', default: false },
];

const emptyForm = {
  timestamp: '',
  nombreAlerta: '',
  mensaje: '',
  campoMonitorizado: '',
  valorDetectado: '',
  umbral: '',
  operador: '',
  nivelSeveridad: '',
  notificacionEnviada: false,
  canalUsado: '',
  resuelto: false,
  notas: '',
  inversorId: '',
  alertaConfigId: '',
};

const severidadColors: Record<string, 'error' | 'warning' | 'info' | 'default'> = {
  CRITICO: 'error',
  ALTO: 'warning',
  MEDIO: 'info',
  BAJO: 'default',
};

export const EventoAlerta = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const eventoAlertaList = useAppSelector(state => state.eventoAlerta.entities);
  const loading = useAppSelector(state => state.eventoAlerta.loading);
  const updating = useAppSelector(state => state.eventoAlerta.updating);
  const updateSuccess = useAppSelector(state => state.eventoAlerta.updateSuccess);
  const totalItems = useAppSelector(state => state.eventoAlerta.totalItems);
  const inversorList = useAppSelector(state => state.inversor.entities);
  const alertaConfigList = useAppSelector(state => state.alertaConfig.entities);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [selectedEntity, setSelectedEntity] = useState<IEventoAlerta | null>(null);
  const [isOpen, setIsOpen] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [globalFilter, setGlobalFilter] = useState('');
  const [columnMenuAnchor, setColumnMenuAnchor] = useState<null | HTMLElement>(null);
  const [visibleColumns, setVisibleColumns] = useState<string[]>(() => {
    const saved = localStorage.getItem('evento-alerta-columns');
    if (saved) return JSON.parse(saved);
    return ALL_COLUMNS.filter(c => c.default).map(c => c.key);
  });
  const [formKey, setFormKey] = useState(0);
  const [formValues, setFormValues] = useState(emptyForm);

  const saveColumns = (cols: string[]) => {
    setVisibleColumns(cols);
    localStorage.setItem('evento-alerta-columns', JSON.stringify(cols));
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
    dispatch(getInversores({ page: 0, size: 1000, sort: 'id,asc' }));
    dispatch(getAlertaConfigs({ page: 0, size: 1000, sort: 'id,asc' }));
  }, []);

  const sort = (field: string) =>
    setPaginationState({ ...paginationState, order: paginationState.order === ASC ? DESC : ASC, sort: field });
  const handlePagination = (currentPage: number) => setPaginationState({ ...paginationState, activePage: currentPage });
  const handleSyncList = () => sortEntities();

  const filteredList = eventoAlertaList?.filter(
    e =>
      !globalFilter ||
      e.nombreAlerta?.toLowerCase().includes(globalFilter.toLowerCase()) ||
      e.mensaje?.toLowerCase().includes(globalFilter.toLowerCase()) ||
      e.inversor?.nombre?.toLowerCase().includes(globalFilter.toLowerCase()),
  );

  const openNew = () => {
    setSelectedEntity(null);
    setFormValues(emptyForm);
    setFormKey(prev => prev + 1);
    setIsOpen(true);
  };
  const openEdit = (row: IEventoAlerta) => {
    setSelectedEntity(row);
    setFormValues({
      timestamp: row.timestamp ? row.timestamp.format('YYYY-MM-DDTHH:mm') : '',
      nombreAlerta: row.nombreAlerta || '',
      mensaje: row.mensaje || '',
      campoMonitorizado: row.campoMonitorizado || '',
      valorDetectado: row.valorDetectado?.toString() || '',
      umbral: row.umbral?.toString() || '',
      operador: row.operador || '',
      nivelSeveridad: row.nivelSeveridad || '',
      notificacionEnviada: row.notificacionEnviada ?? false,
      canalUsado: row.canalUsado || '',
      resuelto: row.resuelto ?? false,
      notas: '',
      inversorId: row.inversor?.id?.toString() || '',
      alertaConfigId: row.alertaConfig?.id?.toString() || '',
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
      timestamp: formValues.timestamp || null,
      nombreAlerta: formValues.nombreAlerta,
      mensaje: formValues.mensaje,
      campoMonitorizado: formValues.campoMonitorizado || null,
      valorDetectado: formValues.valorDetectado ? parseFloat(formValues.valorDetectado) : null,
      umbral: formValues.umbral ? parseFloat(formValues.umbral) : null,
      operador: formValues.operador || null,
      nivelSeveridad: formValues.nivelSeveridad || null,
      notificacionEnviada: formValues.notificacionEnviada,
      canalUsado: formValues.canalUsado || null,
      resuelto: formValues.resuelto,
      notas: formValues.notas || null,
      inversor: formValues.inversorId ? { id: parseInt(formValues.inversorId, 10) } : null,
      alertaConfig: formValues.alertaConfigId ? { id: parseInt(formValues.alertaConfigId, 10) } : null,
    };
    if (selectedEntity?.id) entity.id = selectedEntity.id;
    dispatch(selectedEntity?.id ? updateEntity(entity) : createEntity(entity));
  };
  const openDelete = (row: IEventoAlerta) => {
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
          Eventos de Alerta
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
            Nuevo Evento
          </Button>
        </Box>
      </Toolbar>

      {loading && !eventoAlertaList?.length && (
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
            <TableRow sx={{ bgcolor: 'var(--bg2)' }}>
              {showColumn('id') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('id')}>
                  Id
                </TableCell>
              )}
              {showColumn('timestamp') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('timestamp')}>
                  Timestamp
                </TableCell>
              )}
              {showColumn('nombreAlerta') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('nombreAlerta')}>
                  Nombre Alerta
                </TableCell>
              )}
              {showColumn('mensaje') && <TableCell sx={{ fontWeight: 600 }}>Mensaje</TableCell>}
              {showColumn('campoMonitorizado') && <TableCell sx={{ fontWeight: 600 }}>Campo</TableCell>}
              {showColumn('valorDetectado') && <TableCell sx={{ fontWeight: 600 }}>Valor</TableCell>}
              {showColumn('umbral') && <TableCell sx={{ fontWeight: 600 }}>Umbral</TableCell>}
              {showColumn('operador') && <TableCell sx={{ fontWeight: 600 }}>Operador</TableCell>}
              {showColumn('nivelSeveridad') && <TableCell sx={{ fontWeight: 600 }}>Severidad</TableCell>}
              {showColumn('notificacionEnviada') && <TableCell sx={{ fontWeight: 600 }}>Notif.</TableCell>}
              {showColumn('canalUsado') && <TableCell sx={{ fontWeight: 600 }}>Canal</TableCell>}
              {showColumn('resuelto') && <TableCell sx={{ fontWeight: 600 }}>Resuelto</TableCell>}
              {showColumn('fechaResolucion') && <TableCell sx={{ fontWeight: 600 }}>Fecha Resolución</TableCell>}
              {showColumn('inversor') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('inversor.nombre')}>
                  Inversor
                </TableCell>
              )}
              {showColumn('alertaConfig') && <TableCell sx={{ fontWeight: 600 }}>Alerta Config</TableCell>}
              <TableCell sx={{ fontWeight: 600 }}>Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredList?.map((row: IEventoAlerta) => (
              <TableRow key={row.id} hover>
                {showColumn('id') && <TableCell>{row.id}</TableCell>}
                {showColumn('timestamp') && <TableCell>{row.timestamp?.format('DD/MM/YYYY HH:mm')}</TableCell>}
                {showColumn('nombreAlerta') && <TableCell>{row.nombreAlerta}</TableCell>}
                {showColumn('mensaje') && (
                  <TableCell sx={{ maxWidth: 200, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                    {row.mensaje}
                  </TableCell>
                )}
                {showColumn('campoMonitorizado') && <TableCell>{row.campoMonitorizado}</TableCell>}
                {showColumn('valorDetectado') && <TableCell>{row.valorDetectado}</TableCell>}
                {showColumn('umbral') && <TableCell>{row.umbral}</TableCell>}
                {showColumn('operador') && <TableCell>{row.operador}</TableCell>}
                {showColumn('nivelSeveridad') && (
                  <TableCell>
                    <Chip label={row.nivelSeveridad} color={severidadColors[row.nivelSeveridad || ''] || 'default'} size="small" />
                  </TableCell>
                )}
                {showColumn('notificacionEnviada') && <TableCell>{row.notificacionEnviada ? 'Sí' : 'No'}</TableCell>}
                {showColumn('canalUsado') && <TableCell>{row.canalUsado}</TableCell>}
                {showColumn('resuelto') && (
                  <TableCell>
                    <Chip
                      label={row.resuelto ? 'Resuelto' : 'Pendiente'}
                      color={row.resuelto ? 'success' : 'error'}
                      size="small"
                      variant="outlined"
                    />
                  </TableCell>
                )}
                {showColumn('fechaResolucion') && <TableCell>{row.fechaResolucion?.format('DD/MM/YYYY HH:mm')}</TableCell>}
                {showColumn('inversor') && <TableCell>{row.inversor?.nombre}</TableCell>}
                {showColumn('alertaConfig') && <TableCell>{row.alertaConfig?.nombre}</TableCell>}
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

      {totalItems && eventoAlertaList && eventoAlertaList.length > 0 ? (
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
        <DialogTitle>{selectedEntity ? 'Editar Evento de Alerta' : 'Nuevo Evento de Alerta'}</DialogTitle>
        <DialogContent>
          {loading ? (
            <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
              <CircularProgress />
            </Box>
          ) : (
            <form key={formKey}>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 1 }}>
                <TextField
                  label="Timestamp"
                  id="evento-timestamp"
                  name="timestamp"
                  type="datetime-local"
                  value={formValues.timestamp}
                  onChange={handleInputChange}
                  fullWidth
                  InputLabelProps={{ shrink: true }}
                />
                <TextField
                  label="Nombre Alerta"
                  id="evento-nombreAlerta"
                  name="nombreAlerta"
                  value={formValues.nombreAlerta}
                  onChange={handleInputChange}
                  fullWidth
                  required
                  error={!formValues.nombreAlerta}
                  helperText={!formValues.nombreAlerta ? 'Requerido' : ''}
                />
                <TextField
                  label="Mensaje"
                  id="evento-mensaje"
                  name="mensaje"
                  value={formValues.mensaje}
                  onChange={handleInputChange}
                  fullWidth
                  required
                  error={!formValues.mensaje}
                  helperText={!formValues.mensaje ? 'Requerido' : ''}
                  multiline
                  rows={2}
                />
                <TextField
                  label="Campo Monitorizado"
                  id="evento-campoMonitorizado"
                  name="campoMonitorizado"
                  value={formValues.campoMonitorizado}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Valor Detectado"
                  id="evento-valorDetectado"
                  name="valorDetectado"
                  type="number"
                  value={formValues.valorDetectado}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Umbral"
                  id="evento-umbral"
                  name="umbral"
                  type="number"
                  value={formValues.umbral}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Nivel Severidad"
                  id="evento-nivelSeveridad"
                  name="nivelSeveridad"
                  value={formValues.nivelSeveridad}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Canal Usado"
                  id="evento-canalUsado"
                  name="canalUsado"
                  value={formValues.canalUsado}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Notas"
                  id="evento-notas"
                  name="notas"
                  value={formValues.notas}
                  onChange={handleInputChange}
                  fullWidth
                  multiline
                  rows={2}
                />
                <FormControl fullWidth>
                  <InputLabel id="evento-inversor-label">Inversor</InputLabel>
                  <Select
                    labelId="evento-inversor-label"
                    id="evento-inversorId"
                    name="inversorId"
                    value={formValues.inversorId}
                    onChange={e => setFormValues(prev => ({ ...prev, inversorId: e.target.value }))}
                    label="Inversor"
                  >
                    <MenuItem value="">— Ninguno —</MenuItem>
                    {inversorList?.map((i: any) => (
                      <MenuItem key={i.id} value={i.id}>
                        {i.nombre}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
                <FormControl fullWidth>
                  <InputLabel id="evento-alertaConfig-label">Alerta Config</InputLabel>
                  <Select
                    labelId="evento-alertaConfig-label"
                    id="evento-alertaConfigId"
                    name="alertaConfigId"
                    value={formValues.alertaConfigId}
                    onChange={e => setFormValues(prev => ({ ...prev, alertaConfigId: e.target.value }))}
                    label="Alerta Config"
                  >
                    <MenuItem value="">— Ninguna —</MenuItem>
                    {alertaConfigList?.map((a: any) => (
                      <MenuItem key={a.id} value={a.id}>
                        {a.nombre}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
                <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 1, mt: 2 }}>
                  <Button onClick={closeDialog}>Cancelar</Button>
                  <Button variant="contained" onClick={save} disabled={updating || !formValues.nombreAlerta || !formValues.mensaje}>
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
            <WarningAmberIcon sx={{ fontSize: 40, color: 'var(--am)' }} />
            {selectedEntity && (
              <Typography>
                ¿Seguro que quiere eliminar el Evento: <strong>{selectedEntity.nombreAlerta}</strong>?
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

export default EventoAlerta;
