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
import { getEntities, createEntity, updateEntity, deleteEntity } from './alerta-config.reducer';
import { getEntities as getInversores } from 'app/entities/inversor/inversor.reducer';
import { IAlertaConfig } from 'app/shared/model/alerta-config.model';

const ALL_COLUMNS = [
  { key: 'id', label: 'Id', default: true },
  { key: 'nombre', label: 'Nombre', default: true },
  { key: 'descripcion', label: 'Descripción', default: false },
  { key: 'campoMonitorizado', label: 'Campo Monitorizado', default: true },
  { key: 'operador', label: 'Operador', default: true },
  { key: 'umbral', label: 'Umbral', default: true },
  { key: 'activa', label: 'Activa', default: true },
  { key: 'nivelSeveridad', label: 'Severidad', default: true },
  { key: 'canalNotificacion', label: 'Canal Notif.', default: false },
  { key: 'cooldownMinutos', label: 'Cooldown (min)', default: false },
  { key: 'inversor', label: 'Inversor', default: true },
];

const emptyForm = {
  nombre: '',
  descripcion: '',
  campoMonitorizado: '',
  operador: '',
  umbral: '',
  activa: false,
  nivelSeveridad: '',
  canalNotificacion: '',
  cooldownMinutos: '',
  inversorId: '',
};

const severidadColors: Record<string, 'error' | 'warning' | 'info' | 'default'> = {
  CRITICO: 'error',
  ALTO: 'warning',
  MEDIO: 'info',
  BAJO: 'default',
};

export const AlertaConfig = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const alertaConfigList = useAppSelector(state => state.alertaConfig.entities);
  const loading = useAppSelector(state => state.alertaConfig.loading);
  const updating = useAppSelector(state => state.alertaConfig.updating);
  const updateSuccess = useAppSelector(state => state.alertaConfig.updateSuccess);
  const totalItems = useAppSelector(state => state.alertaConfig.totalItems);
  const inversorList = useAppSelector(state => state.inversor.entities);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [selectedEntity, setSelectedEntity] = useState<IAlertaConfig | null>(null);
  const [isOpen, setIsOpen] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [globalFilter, setGlobalFilter] = useState('');
  const [columnMenuAnchor, setColumnMenuAnchor] = useState<null | HTMLElement>(null);
  const [visibleColumns, setVisibleColumns] = useState<string[]>(() => {
    const saved = localStorage.getItem('alerta-config-columns');
    if (saved) return JSON.parse(saved);
    return ALL_COLUMNS.filter(c => c.default).map(c => c.key);
  });
  const [formKey, setFormKey] = useState(0);
  const [formValues, setFormValues] = useState(emptyForm);

  const saveColumns = (cols: string[]) => {
    setVisibleColumns(cols);
    localStorage.setItem('alerta-config-columns', JSON.stringify(cols));
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
  }, []);

  const sort = (field: string) =>
    setPaginationState({ ...paginationState, order: paginationState.order === ASC ? DESC : ASC, sort: field });
  const handlePagination = (currentPage: number) => setPaginationState({ ...paginationState, activePage: currentPage });
  const handleSyncList = () => sortEntities();

  const filteredList = alertaConfigList?.filter(
    e =>
      !globalFilter ||
      e.nombre?.toLowerCase().includes(globalFilter.toLowerCase()) ||
      e.campoMonitorizado?.toLowerCase().includes(globalFilter.toLowerCase()) ||
      e.inversor?.nombre?.toLowerCase().includes(globalFilter.toLowerCase()),
  );

  const openNew = () => {
    setSelectedEntity(null);
    setFormValues(emptyForm);
    setFormKey(prev => prev + 1);
    setIsOpen(true);
  };
  const openEdit = (row: IAlertaConfig) => {
    setSelectedEntity(row);
    setFormValues({
      nombre: row.nombre || '',
      descripcion: row.descripcion || '',
      campoMonitorizado: row.campoMonitorizado || '',
      operador: row.operador || '',
      umbral: row.umbral?.toString() || '',
      activa: row.activa ?? false,
      nivelSeveridad: row.nivelSeveridad || '',
      canalNotificacion: row.canalNotificacion || '',
      cooldownMinutos: row.cooldownMinutos?.toString() || '',
      inversorId: row.inversor?.id?.toString() || '',
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
      campoMonitorizado: formValues.campoMonitorizado,
      operador: formValues.operador,
      umbral: formValues.umbral ? parseFloat(formValues.umbral) : undefined,
      activa: formValues.activa,
      nivelSeveridad: formValues.nivelSeveridad || null,
      canalNotificacion: formValues.canalNotificacion || null,
      cooldownMinutos: formValues.cooldownMinutos ? parseInt(formValues.cooldownMinutos, 10) : null,
      inversor: formValues.inversorId ? { id: parseInt(formValues.inversorId, 10) } : null,
    };
    if (selectedEntity?.id) entity.id = selectedEntity.id;
    dispatch(selectedEntity?.id ? updateEntity(entity) : createEntity(entity));
  };
  const openDelete = (row: IAlertaConfig) => {
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
          Configuración de Alertas
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
            Nueva Alerta Config
          </Button>
        </Box>
      </Toolbar>

      {loading && !alertaConfigList?.length && (
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
        <Table sx={{ minWidth: 900 }}>
          <TableHead>
            <TableRow sx={{ bgcolor: 'var(--bg2)' }}>
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
              {showColumn('campoMonitorizado') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('campoMonitorizado')}>
                  Campo
                </TableCell>
              )}
              {showColumn('operador') && <TableCell sx={{ fontWeight: 600 }}>Operador</TableCell>}
              {showColumn('umbral') && <TableCell sx={{ fontWeight: 600 }}>Umbral</TableCell>}
              {showColumn('activa') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('activa')}>
                  Activa
                </TableCell>
              )}
              {showColumn('nivelSeveridad') && <TableCell sx={{ fontWeight: 600 }}>Severidad</TableCell>}
              {showColumn('canalNotificacion') && <TableCell sx={{ fontWeight: 600 }}>Canal</TableCell>}
              {showColumn('cooldownMinutos') && <TableCell sx={{ fontWeight: 600 }}>Cooldown</TableCell>}
              {showColumn('inversor') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('inversor.nombre')}>
                  Inversor
                </TableCell>
              )}
              <TableCell sx={{ fontWeight: 600 }}>Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredList?.map((row: IAlertaConfig) => (
              <TableRow key={row.id} hover>
                {showColumn('id') && <TableCell>{row.id}</TableCell>}
                {showColumn('nombre') && <TableCell>{row.nombre}</TableCell>}
                {showColumn('descripcion') && (
                  <TableCell sx={{ maxWidth: 200, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                    {row.descripcion}
                  </TableCell>
                )}
                {showColumn('campoMonitorizado') && <TableCell>{row.campoMonitorizado}</TableCell>}
                {showColumn('operador') && <TableCell>{row.operador}</TableCell>}
                {showColumn('umbral') && <TableCell>{row.umbral}</TableCell>}
                {showColumn('activa') && <TableCell>{row.activa ? 'Sí' : 'No'}</TableCell>}
                {showColumn('nivelSeveridad') && (
                  <TableCell>
                    <Chip label={row.nivelSeveridad} color={severidadColors[row.nivelSeveridad || ''] || 'default'} size="small" />
                  </TableCell>
                )}
                {showColumn('canalNotificacion') && <TableCell>{row.canalNotificacion}</TableCell>}
                {showColumn('cooldownMinutos') && <TableCell>{row.cooldownMinutos}</TableCell>}
                {showColumn('inversor') && <TableCell>{row.inversor?.nombre}</TableCell>}
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

      {totalItems && alertaConfigList && alertaConfigList.length > 0 ? (
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
        <DialogTitle>{selectedEntity ? 'Editar Configuración de Alerta' : 'Nueva Configuración de Alerta'}</DialogTitle>
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
                  id="alerta-nombre"
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
                  id="alerta-descripcion"
                  name="descripcion"
                  value={formValues.descripcion}
                  onChange={handleInputChange}
                  fullWidth
                  multiline
                  rows={2}
                />
                <TextField
                  label="Campo Monitorizado"
                  id="alerta-campoMonitorizado"
                  name="campoMonitorizado"
                  value={formValues.campoMonitorizado}
                  onChange={handleInputChange}
                  fullWidth
                  required
                  error={!formValues.campoMonitorizado}
                  helperText={!formValues.campoMonitorizado ? 'Requerido' : ''}
                />
                <FormControl fullWidth>
                  <InputLabel id="alerta-operador-label">Operador</InputLabel>
                  <Select
                    labelId="alerta-operador-label"
                    id="alerta-operador"
                    name="operador"
                    value={formValues.operador}
                    onChange={e => setFormValues(prev => ({ ...prev, operador: e.target.value }))}
                    label="Operador"
                  >
                    <MenuItem value="">— Seleccionar —</MenuItem>
                    <MenuItem value=">">{'>'} (Mayor que)</MenuItem>
                    <MenuItem value="<">{'<'} (Menor que)</MenuItem>
                    <MenuItem value=">=">{'≥'} (Mayor o igual)</MenuItem>
                    <MenuItem value="<=">{'≤'} (Menor o igual)</MenuItem>
                    <MenuItem value="==">{'='} (Igual)</MenuItem>
                  </Select>
                </FormControl>
                <TextField
                  label="Umbral"
                  id="alerta-umbral"
                  name="umbral"
                  type="number"
                  value={formValues.umbral}
                  onChange={handleInputChange}
                  fullWidth
                  required
                  error={!formValues.umbral}
                  helperText={!formValues.umbral ? 'Requerido' : ''}
                />
                <FormControlLabel
                  control={<Checkbox name="activa" checked={formValues.activa} onChange={handleInputChange} />}
                  label="Activa"
                />
                <TextField
                  label="Nivel Severidad"
                  id="alerta-nivelSeveridad"
                  name="nivelSeveridad"
                  value={formValues.nivelSeveridad}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Canal Notificación"
                  id="alerta-canalNotificacion"
                  name="canalNotificacion"
                  value={formValues.canalNotificacion}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Cooldown (minutos)"
                  id="alerta-cooldownMinutos"
                  name="cooldownMinutos"
                  type="number"
                  value={formValues.cooldownMinutos}
                  onChange={handleInputChange}
                  fullWidth
                />
                <FormControl fullWidth>
                  <InputLabel id="alerta-inversor-label">Inversor</InputLabel>
                  <Select
                    labelId="alerta-inversor-label"
                    id="alerta-inversorId"
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
                <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 1, mt: 2 }}>
                  <Button onClick={closeDialog}>Cancelar</Button>
                  <Button
                    variant="contained"
                    onClick={save}
                    disabled={updating || !formValues.nombre || !formValues.campoMonitorizado || !formValues.umbral}
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
            <WarningAmberIcon sx={{ fontSize: 40, color: 'var(--am)' }} />
            {selectedEntity && (
              <Typography>
                ¿Seguro que quiere eliminar la Alerta: <strong>{selectedEntity.nombre}</strong>?
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

export default AlertaConfig;
