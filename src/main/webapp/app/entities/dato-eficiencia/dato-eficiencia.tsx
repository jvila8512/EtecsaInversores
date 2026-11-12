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
import { getEntities, createEntity, updateEntity, deleteEntity } from './dato-eficiencia.reducer';
import { getEntities as getInversores } from 'app/entities/inversor/inversor.reducer';
import { IDatoEficiencia } from 'app/shared/model/dato-eficiencia.model';

const ALL_COLUMNS = [
  { key: 'id', label: 'Id', default: true },
  { key: 'fecha', label: 'Fecha', default: true },
  { key: 'energiaPvWh', label: 'Energía PV (Wh)', default: true },
  { key: 'energiaCargaWh', label: 'Energía Carga (Wh)', default: true },
  { key: 'energiaRedWh', label: 'Energía Red (Wh)', default: false },
  { key: 'eficienciaPromPct', label: 'Eficiencia Prom %', default: true },
  { key: 'horasSol', label: 'Horas Sol', default: false },
  { key: 'picoPvW', label: 'Pico PV (W)', default: false },
  { key: 'picoCargaW', label: 'Pico Carga (W)', default: false },
  { key: 'maxBateriaV', label: 'Max Batería V', default: false },
  { key: 'minBateriaV', label: 'Min Batería V', default: false },
  { key: 'totalLecturas', label: 'Total Lecturas', default: false },
  { key: 'inversor', label: 'Inversor', default: true },
];

const emptyForm = {
  fecha: '',
  energiaPvWh: '',
  energiaCargaWh: '',
  energiaRedWh: '',
  eficienciaPromPct: '',
  horasSol: '',
  picoPvW: '',
  picoCargaW: '',
  maxBateriaV: '',
  minBateriaV: '',
  totalLecturas: '',
  inversorId: '',
};

export const DatoEficiencia = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const datoEficienciaList = useAppSelector(state => state.datoEficiencia.entities);
  const loading = useAppSelector(state => state.datoEficiencia.loading);
  const updating = useAppSelector(state => state.datoEficiencia.updating);
  const updateSuccess = useAppSelector(state => state.datoEficiencia.updateSuccess);
  const totalItems = useAppSelector(state => state.datoEficiencia.totalItems);
  const inversorList = useAppSelector(state => state.inversor.entities);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [selectedEntity, setSelectedEntity] = useState<IDatoEficiencia | null>(null);
  const [isOpen, setIsOpen] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [globalFilter, setGlobalFilter] = useState('');
  const [columnMenuAnchor, setColumnMenuAnchor] = useState<null | HTMLElement>(null);
  const [visibleColumns, setVisibleColumns] = useState<string[]>(() => {
    const saved = localStorage.getItem('dato-eficiencia-columns');
    if (saved) return JSON.parse(saved);
    return ALL_COLUMNS.filter(c => c.default).map(c => c.key);
  });
  const [formKey, setFormKey] = useState(0);
  const [formValues, setFormValues] = useState(emptyForm);

  const saveColumns = (cols: string[]) => {
    setVisibleColumns(cols);
    localStorage.setItem('dato-eficiencia-columns', JSON.stringify(cols));
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

  const filteredList = datoEficienciaList?.filter(
    e => !globalFilter || e.inversor?.nombre?.toLowerCase().includes(globalFilter.toLowerCase()),
  );

  const openNew = () => {
    setSelectedEntity(null);
    setFormValues(emptyForm);
    setFormKey(prev => prev + 1);
    setIsOpen(true);
  };
  const openEdit = (row: IDatoEficiencia) => {
    setSelectedEntity(row);
    setFormValues({
      fecha: row.fecha ? row.fecha.format('YYYY-MM-DD') : '',
      energiaPvWh: row.energiaPvWh?.toString() || '',
      energiaCargaWh: row.energiaCargaWh?.toString() || '',
      energiaRedWh: row.energiaRedWh?.toString() || '',
      eficienciaPromPct: row.eficienciaPromPct?.toString() || '',
      horasSol: row.horasSol?.toString() || '',
      picoPvW: row.picoPvW?.toString() || '',
      picoCargaW: row.picoCargaW?.toString() || '',
      maxBateriaV: row.maxBateriaV?.toString() || '',
      minBateriaV: row.minBateriaV?.toString() || '',
      totalLecturas: row.totalLecturas?.toString() || '',
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
      fecha: formValues.fecha || null,
      energiaPvWh: formValues.energiaPvWh ? parseFloat(formValues.energiaPvWh) : null,
      energiaCargaWh: formValues.energiaCargaWh ? parseFloat(formValues.energiaCargaWh) : null,
      energiaRedWh: formValues.energiaRedWh ? parseFloat(formValues.energiaRedWh) : null,
      eficienciaPromPct: formValues.eficienciaPromPct ? parseFloat(formValues.eficienciaPromPct) : null,
      horasSol: formValues.horasSol ? parseFloat(formValues.horasSol) : null,
      picoPvW: formValues.picoPvW ? parseFloat(formValues.picoPvW) : null,
      picoCargaW: formValues.picoCargaW ? parseFloat(formValues.picoCargaW) : null,
      maxBateriaV: formValues.maxBateriaV ? parseFloat(formValues.maxBateriaV) : null,
      minBateriaV: formValues.minBateriaV ? parseFloat(formValues.minBateriaV) : null,
      totalLecturas: formValues.totalLecturas ? parseInt(formValues.totalLecturas, 10) : null,
      inversor: formValues.inversorId ? { id: parseInt(formValues.inversorId, 10) } : null,
    };
    if (selectedEntity?.id) entity.id = selectedEntity.id;
    dispatch(selectedEntity?.id ? updateEntity(entity) : createEntity(entity));
  };
  const openDelete = (row: IDatoEficiencia) => {
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
          Datos de Eficiencia
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
            Nuevo Dato
          </Button>
        </Box>
      </Toolbar>

      {loading && !datoEficienciaList?.length && (
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
              {showColumn('fecha') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('fecha')}>
                  Fecha
                </TableCell>
              )}
              {showColumn('energiaPvWh') && <TableCell sx={{ fontWeight: 600 }}>Energía PV (Wh)</TableCell>}
              {showColumn('energiaCargaWh') && <TableCell sx={{ fontWeight: 600 }}>Energía Carga (Wh)</TableCell>}
              {showColumn('energiaRedWh') && <TableCell sx={{ fontWeight: 600 }}>Energía Red (Wh)</TableCell>}
              {showColumn('eficienciaPromPct') && <TableCell sx={{ fontWeight: 600 }}>Eficiencia Prom %</TableCell>}
              {showColumn('horasSol') && <TableCell sx={{ fontWeight: 600 }}>Horas Sol</TableCell>}
              {showColumn('picoPvW') && <TableCell sx={{ fontWeight: 600 }}>Pico PV (W)</TableCell>}
              {showColumn('picoCargaW') && <TableCell sx={{ fontWeight: 600 }}>Pico Carga (W)</TableCell>}
              {showColumn('maxBateriaV') && <TableCell sx={{ fontWeight: 600 }}>Max Batería V</TableCell>}
              {showColumn('minBateriaV') && <TableCell sx={{ fontWeight: 600 }}>Min Batería V</TableCell>}
              {showColumn('totalLecturas') && <TableCell sx={{ fontWeight: 600 }}>Total Lecturas</TableCell>}
              {showColumn('inversor') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('inversor.nombre')}>
                  Inversor
                </TableCell>
              )}
              <TableCell sx={{ fontWeight: 600 }}>Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredList?.map((row: IDatoEficiencia) => (
              <TableRow key={row.id} hover>
                {showColumn('id') && <TableCell>{row.id}</TableCell>}
                {showColumn('fecha') && <TableCell>{row.fecha?.format('DD/MM/YYYY')}</TableCell>}
                {showColumn('energiaPvWh') && <TableCell>{row.energiaPvWh}</TableCell>}
                {showColumn('energiaCargaWh') && <TableCell>{row.energiaCargaWh}</TableCell>}
                {showColumn('energiaRedWh') && <TableCell>{row.energiaRedWh}</TableCell>}
                {showColumn('eficienciaPromPct') && <TableCell>{row.eficienciaPromPct}</TableCell>}
                {showColumn('horasSol') && <TableCell>{row.horasSol}</TableCell>}
                {showColumn('picoPvW') && <TableCell>{row.picoPvW}</TableCell>}
                {showColumn('picoCargaW') && <TableCell>{row.picoCargaW}</TableCell>}
                {showColumn('maxBateriaV') && <TableCell>{row.maxBateriaV}</TableCell>}
                {showColumn('minBateriaV') && <TableCell>{row.minBateriaV}</TableCell>}
                {showColumn('totalLecturas') && <TableCell>{row.totalLecturas}</TableCell>}
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

      {totalItems && datoEficienciaList && datoEficienciaList.length > 0 ? (
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
        <DialogTitle>{selectedEntity ? 'Editar Dato de Eficiencia' : 'Nuevo Dato de Eficiencia'}</DialogTitle>
        <DialogContent>
          {loading ? (
            <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
              <CircularProgress />
            </Box>
          ) : (
            <form key={formKey}>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 1 }}>
                <TextField
                  label="Fecha"
                  id="eficiencia-fecha"
                  name="fecha"
                  type="date"
                  value={formValues.fecha}
                  onChange={handleInputChange}
                  fullWidth
                  InputLabelProps={{ shrink: true }}
                />
                <TextField
                  label="Energía PV (Wh)"
                  id="eficiencia-energiaPvWh"
                  name="energiaPvWh"
                  type="number"
                  value={formValues.energiaPvWh}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Energía Carga (Wh)"
                  id="eficiencia-energiaCargaWh"
                  name="energiaCargaWh"
                  type="number"
                  value={formValues.energiaCargaWh}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Energía Red (Wh)"
                  id="eficiencia-energiaRedWh"
                  name="energiaRedWh"
                  type="number"
                  value={formValues.energiaRedWh}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Eficiencia Prom (%)"
                  id="eficiencia-eficienciaPromPct"
                  name="eficienciaPromPct"
                  type="number"
                  value={formValues.eficienciaPromPct}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Horas Sol"
                  id="eficiencia-horasSol"
                  name="horasSol"
                  type="number"
                  value={formValues.horasSol}
                  onChange={handleInputChange}
                  fullWidth
                />
                <FormControl fullWidth>
                  <InputLabel id="eficiencia-inversor-label">Inversor</InputLabel>
                  <Select
                    labelId="eficiencia-inversor-label"
                    id="eficiencia-inversorId"
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
                  <Button variant="contained" onClick={save} disabled={updating}>
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
                ¿Seguro que quiere eliminar el Dato de Eficiencia ID: <strong>{selectedEntity.id}</strong>?
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

export default DatoEficiencia;
