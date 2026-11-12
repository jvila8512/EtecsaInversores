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
import { getEntities, createEntity, updateEntity, deleteEntity } from './dato-inversor.reducer';
import { getEntities as getInversores } from 'app/entities/inversor/inversor.reducer';
import { IDatoInversor } from 'app/shared/model/dato-inversor.model';

const ALL_COLUMNS = [
  { key: 'id', label: 'Id', default: true },
  { key: 'tiempo', label: 'Tiempo', default: true },
  { key: 'bateriaVoltajeV', label: 'Batería V', default: true },
  { key: 'bateriaCorrienteA', label: 'Batería A', default: false },
  { key: 'bateriaPotenciaW', label: 'Batería W', default: false },
  { key: 'pvVoltajeV', label: 'PV V', default: true },
  { key: 'pvCorrienteA', label: 'PV A', default: false },
  { key: 'pvPotenciaW', label: 'PV W', default: true },
  { key: 'salidaVoltajeV', label: 'Salida V', default: false },
  { key: 'salidaCorrienteA', label: 'Salida A', default: false },
  { key: 'salidaPotenciaW', label: 'Salida W', default: false },
  { key: 'salidaFrecuenciaHz', label: 'Salida Hz', default: false },
  { key: 'cargaPorcentaje', label: 'Carga %', default: true },
  { key: 'socPct', label: 'SOC %', default: true },
  { key: 'redVoltajeV', label: 'Red V', default: false },
  { key: 'redFrecuenciaHz', label: 'Red Hz', default: false },
  { key: 'eficienciaPct', label: 'Eficiencia %', default: true },
  { key: 'temperaturaC', label: 'Temp °C', default: true },
  { key: 'busVoltajeV', label: 'Bus V', default: false },
  { key: 'bateriaCargando', label: 'Cargando', default: true },
  { key: 'pvActivo', label: 'PV Activo', default: false },
  { key: 'crcValido', label: 'CRC OK', default: false },
  { key: 'inversor', label: 'Inversor', default: true },
];

const emptyForm = {
  tiempo: '',
  bateriaVoltajeV: '',
  bateriaCorrienteA: '',
  bateriaPotenciaW: '',
  pvVoltajeV: '',
  pvPotenciaW: '',
  salidaVoltajeV: '',
  salidaPotenciaW: '',
  salidaAparenteVa: '',
  cargaPorcentaje: '',
  redVoltajeV: '',
  redFrecuenciaHz: '',
  modoOperacion: '',
  codigoFallo: '',
  eficienciaPct: '',
  consumoRedW: '',
  bateriaCargando: false,
  pvActivo: false,
  redActiva: false,
  crcValido: false,
  latenciaMs: '',
  pvCorrienteA: '',
  salidaCorrienteA: '',
  salidaFrecuenciaHz: '',
  temperaturaC: '',
  busVoltajeV: '',
  socPct: '',
  inversorId: '',
};

export const DatoInversor = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const datoInversorList = useAppSelector(state => state.datoInversor.entities);
  const loading = useAppSelector(state => state.datoInversor.loading);
  const updating = useAppSelector(state => state.datoInversor.updating);
  const updateSuccess = useAppSelector(state => state.datoInversor.updateSuccess);
  const totalItems = useAppSelector(state => state.datoInversor.totalItems);
  const inversorList = useAppSelector(state => state.inversor.entities);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [selectedEntity, setSelectedEntity] = useState<IDatoInversor | null>(null);
  const [isOpen, setIsOpen] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [globalFilter, setGlobalFilter] = useState('');
  const [columnMenuAnchor, setColumnMenuAnchor] = useState<null | HTMLElement>(null);
  const [visibleColumns, setVisibleColumns] = useState<string[]>(() => {
    const saved = localStorage.getItem('dato-inversor-columns');
    if (saved) return JSON.parse(saved);
    return ALL_COLUMNS.filter(c => c.default).map(c => c.key);
  });
  const [formKey, setFormKey] = useState(0);
  const [formValues, setFormValues] = useState(emptyForm);

  const saveColumns = (cols: string[]) => {
    setVisibleColumns(cols);
    localStorage.setItem('dato-inversor-columns', JSON.stringify(cols));
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

  const filteredList = datoInversorList?.filter(
    e =>
      !globalFilter ||
      e.inversor?.nombre?.toLowerCase().includes(globalFilter.toLowerCase()) ||
      e.modoOperacion?.toLowerCase().includes(globalFilter.toLowerCase()),
  );

  const openNew = () => {
    setSelectedEntity(null);
    setFormValues(emptyForm);
    setFormKey(prev => prev + 1);
    setIsOpen(true);
  };
  const openEdit = (row: IDatoInversor) => {
    setSelectedEntity(row);
    setFormValues({
      tiempo: row.tiempo ? row.tiempo.format('YYYY-MM-DDTHH:mm') : '',
      bateriaVoltajeV: row.bateriaVoltajeV?.toString() || '',
      bateriaCorrienteA: row.bateriaCorrienteA?.toString() || '',
      bateriaPotenciaW: row.bateriaPotenciaW?.toString() || '',
      pvVoltajeV: row.pvVoltajeV?.toString() || '',
      pvPotenciaW: row.pvPotenciaW?.toString() || '',
      salidaVoltajeV: row.salidaVoltajeV?.toString() || '',
      salidaPotenciaW: row.salidaPotenciaW?.toString() || '',
      salidaAparenteVa: row.salidaAparenteVa?.toString() || '',
      cargaPorcentaje: row.cargaPorcentaje?.toString() || '',
      redVoltajeV: row.redVoltajeV?.toString() || '',
      redFrecuenciaHz: row.redFrecuenciaHz?.toString() || '',
      modoOperacion: row.modoOperacion || '',
      codigoFallo: row.codigoFallo?.toString() || '',
      eficienciaPct: row.eficienciaPct?.toString() || '',
      consumoRedW: row.consumoRedW?.toString() || '',
      bateriaCargando: row.bateriaCargando ?? false,
      pvActivo: row.pvActivo ?? false,
      redActiva: row.redActiva ?? false,
      crcValido: row.crcValido ?? false,
      latenciaMs: row.latenciaMs?.toString() || '',
      pvCorrienteA: row.pvCorrienteA?.toString() || '',
      salidaCorrienteA: row.salidaCorrienteA?.toString() || '',
      salidaFrecuenciaHz: row.salidaFrecuenciaHz?.toString() || '',
      temperaturaC: row.temperaturaC?.toString() || '',
      busVoltajeV: row.busVoltajeV?.toString() || '',
      socPct: row.socPct?.toString() || '',
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
      tiempo: formValues.tiempo || null,
      bateriaVoltajeV: formValues.bateriaVoltajeV ? parseFloat(formValues.bateriaVoltajeV) : null,
      bateriaCorrienteA: formValues.bateriaCorrienteA ? parseFloat(formValues.bateriaCorrienteA) : null,
      bateriaPotenciaW: formValues.bateriaPotenciaW ? parseFloat(formValues.bateriaPotenciaW) : null,
      pvVoltajeV: formValues.pvVoltajeV ? parseFloat(formValues.pvVoltajeV) : null,
      pvPotenciaW: formValues.pvPotenciaW ? parseFloat(formValues.pvPotenciaW) : null,
      salidaVoltajeV: formValues.salidaVoltajeV ? parseFloat(formValues.salidaVoltajeV) : null,
      salidaPotenciaW: formValues.salidaPotenciaW ? parseFloat(formValues.salidaPotenciaW) : null,
      salidaAparenteVa: formValues.salidaAparenteVa ? parseFloat(formValues.salidaAparenteVa) : null,
      cargaPorcentaje: formValues.cargaPorcentaje ? parseFloat(formValues.cargaPorcentaje) : null,
      redVoltajeV: formValues.redVoltajeV ? parseFloat(formValues.redVoltajeV) : null,
      redFrecuenciaHz: formValues.redFrecuenciaHz ? parseFloat(formValues.redFrecuenciaHz) : null,
      modoOperacion: formValues.modoOperacion || null,
      codigoFallo: formValues.codigoFallo ? parseInt(formValues.codigoFallo, 10) : null,
      eficienciaPct: formValues.eficienciaPct ? parseFloat(formValues.eficienciaPct) : null,
      consumoRedW: formValues.consumoRedW ? parseFloat(formValues.consumoRedW) : null,
      bateriaCargando: formValues.bateriaCargando,
      pvActivo: formValues.pvActivo,
      redActiva: formValues.redActiva,
      crcValido: formValues.crcValido,
      latenciaMs: formValues.latenciaMs ? parseInt(formValues.latenciaMs, 10) : null,
      pvCorrienteA: formValues.pvCorrienteA ? parseFloat(formValues.pvCorrienteA) : null,
      salidaCorrienteA: formValues.salidaCorrienteA ? parseFloat(formValues.salidaCorrienteA) : null,
      salidaFrecuenciaHz: formValues.salidaFrecuenciaHz ? parseFloat(formValues.salidaFrecuenciaHz) : null,
      temperaturaC: formValues.temperaturaC ? parseFloat(formValues.temperaturaC) : null,
      busVoltajeV: formValues.busVoltajeV ? parseFloat(formValues.busVoltajeV) : null,
      socPct: formValues.socPct ? parseInt(formValues.socPct, 10) : null,
      inversor: formValues.inversorId ? { id: parseInt(formValues.inversorId, 10) } : null,
    };
    if (selectedEntity?.id) entity.id = selectedEntity.id;
    dispatch(selectedEntity?.id ? updateEntity(entity) : createEntity(entity));
  };
  const openDelete = (row: IDatoInversor) => {
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
          Datos de Inversor
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

      {loading && !datoInversorList?.length && (
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
              {showColumn('tiempo') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('tiempo')}>
                  Tiempo
                </TableCell>
              )}
              {showColumn('bateriaVoltajeV') && <TableCell sx={{ fontWeight: 600 }}>Batería V</TableCell>}
              {showColumn('bateriaCorrienteA') && <TableCell sx={{ fontWeight: 600 }}>Batería A</TableCell>}
              {showColumn('bateriaPotenciaW') && <TableCell sx={{ fontWeight: 600 }}>Batería W</TableCell>}
              {showColumn('pvVoltajeV') && <TableCell sx={{ fontWeight: 600 }}>PV V</TableCell>}
              {showColumn('pvCorrienteA') && <TableCell sx={{ fontWeight: 600 }}>PV A</TableCell>}
              {showColumn('pvPotenciaW') && <TableCell sx={{ fontWeight: 600 }}>PV W</TableCell>}
              {showColumn('salidaVoltajeV') && <TableCell sx={{ fontWeight: 600 }}>Salida V</TableCell>}
              {showColumn('salidaCorrienteA') && <TableCell sx={{ fontWeight: 600 }}>Salida A</TableCell>}
              {showColumn('salidaPotenciaW') && <TableCell sx={{ fontWeight: 600 }}>Salida W</TableCell>}
              {showColumn('salidaFrecuenciaHz') && <TableCell sx={{ fontWeight: 600 }}>Salida Hz</TableCell>}
              {showColumn('cargaPorcentaje') && <TableCell sx={{ fontWeight: 600 }}>Carga %</TableCell>}
              {showColumn('socPct') && <TableCell sx={{ fontWeight: 600 }}>SOC %</TableCell>}
              {showColumn('redVoltajeV') && <TableCell sx={{ fontWeight: 600 }}>Red V</TableCell>}
              {showColumn('redFrecuenciaHz') && <TableCell sx={{ fontWeight: 600 }}>Red Hz</TableCell>}
              {showColumn('eficienciaPct') && <TableCell sx={{ fontWeight: 600 }}>Eficiencia %</TableCell>}
              {showColumn('temperaturaC') && <TableCell sx={{ fontWeight: 600 }}>Temp °C</TableCell>}
              {showColumn('busVoltajeV') && <TableCell sx={{ fontWeight: 600 }}>Bus V</TableCell>}
              {showColumn('bateriaCargando') && <TableCell sx={{ fontWeight: 600 }}>Cargando</TableCell>}
              {showColumn('pvActivo') && <TableCell sx={{ fontWeight: 600 }}>PV Activo</TableCell>}
              {showColumn('crcValido') && <TableCell sx={{ fontWeight: 600 }}>CRC OK</TableCell>}
              {showColumn('inversor') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('inversor.nombre')}>
                  Inversor
                </TableCell>
              )}
              <TableCell sx={{ fontWeight: 600 }}>Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredList?.map((row: IDatoInversor) => (
              <TableRow key={row.id} hover>
                {showColumn('id') && <TableCell>{row.id}</TableCell>}
                {showColumn('tiempo') && <TableCell>{row.tiempo?.format('DD/MM/YYYY HH:mm:ss')}</TableCell>}
                {showColumn('bateriaVoltajeV') && <TableCell>{row.bateriaVoltajeV}</TableCell>}
                {showColumn('bateriaCorrienteA') && <TableCell>{row.bateriaCorrienteA}</TableCell>}
                {showColumn('bateriaPotenciaW') && <TableCell>{row.bateriaPotenciaW}</TableCell>}
                {showColumn('pvVoltajeV') && <TableCell>{row.pvVoltajeV}</TableCell>}
                {showColumn('pvCorrienteA') && <TableCell>{row.pvCorrienteA}</TableCell>}
                {showColumn('pvPotenciaW') && <TableCell>{row.pvPotenciaW}</TableCell>}
                {showColumn('salidaVoltajeV') && <TableCell>{row.salidaVoltajeV}</TableCell>}
                {showColumn('salidaCorrienteA') && <TableCell>{row.salidaCorrienteA}</TableCell>}
                {showColumn('salidaPotenciaW') && <TableCell>{row.salidaPotenciaW}</TableCell>}
                {showColumn('salidaFrecuenciaHz') && <TableCell>{row.salidaFrecuenciaHz}</TableCell>}
                {showColumn('cargaPorcentaje') && <TableCell>{row.cargaPorcentaje}</TableCell>}
                {showColumn('socPct') && <TableCell>{row.socPct}</TableCell>}
                {showColumn('redVoltajeV') && <TableCell>{row.redVoltajeV}</TableCell>}
                {showColumn('redFrecuenciaHz') && <TableCell>{row.redFrecuenciaHz}</TableCell>}
                {showColumn('eficienciaPct') && <TableCell>{row.eficienciaPct}</TableCell>}
                {showColumn('temperaturaC') && <TableCell>{row.temperaturaC}</TableCell>}
                {showColumn('busVoltajeV') && <TableCell>{row.busVoltajeV}</TableCell>}
                {showColumn('bateriaCargando') && <TableCell>{row.bateriaCargando ? 'Sí' : 'No'}</TableCell>}
                {showColumn('pvActivo') && <TableCell>{row.pvActivo ? 'Sí' : 'No'}</TableCell>}
                {showColumn('crcValido') && <TableCell>{row.crcValido ? 'Sí' : 'No'}</TableCell>}
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

      {totalItems && datoInversorList && datoInversorList.length > 0 ? (
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
        <DialogTitle>{selectedEntity ? 'Editar Dato de Inversor' : 'Nuevo Dato de Inversor'}</DialogTitle>
        <DialogContent>
          {loading ? (
            <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
              <CircularProgress />
            </Box>
          ) : (
            <form key={formKey}>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 1 }}>
                <TextField
                  label="Tiempo"
                  id="dato-tiempo"
                  name="tiempo"
                  type="datetime-local"
                  value={formValues.tiempo}
                  onChange={handleInputChange}
                  fullWidth
                  InputLabelProps={{ shrink: true }}
                />
                <TextField
                  label="Batería Voltaje (V)"
                  id="dato-bateriaVoltajeV"
                  name="bateriaVoltajeV"
                  type="number"
                  value={formValues.bateriaVoltajeV}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Batería Corriente (A)"
                  id="dato-bateriaCorrienteA"
                  name="bateriaCorrienteA"
                  type="number"
                  value={formValues.bateriaCorrienteA}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Batería Potencia (W)"
                  id="dato-bateriaPotenciaW"
                  name="bateriaPotenciaW"
                  type="number"
                  value={formValues.bateriaPotenciaW}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="PV Voltaje (V)"
                  id="dato-pvVoltajeV"
                  name="pvVoltajeV"
                  type="number"
                  value={formValues.pvVoltajeV}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="PV Potencia (W)"
                  id="dato-pvPotenciaW"
                  name="pvPotenciaW"
                  type="number"
                  value={formValues.pvPotenciaW}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Salida Voltaje (V)"
                  id="dato-salidaVoltajeV"
                  name="salidaVoltajeV"
                  type="number"
                  value={formValues.salidaVoltajeV}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Salida Potencia (W)"
                  id="dato-salidaPotenciaW"
                  name="salidaPotenciaW"
                  type="number"
                  value={formValues.salidaPotenciaW}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Carga (%)"
                  id="dato-cargaPorcentaje"
                  name="cargaPorcentaje"
                  type="number"
                  value={formValues.cargaPorcentaje}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Eficiencia (%)"
                  id="dato-eficienciaPct"
                  name="eficienciaPct"
                  type="number"
                  value={formValues.eficienciaPct}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="PV Corriente (A)"
                  id="dato-pvCorrienteA"
                  name="pvCorrienteA"
                  type="number"
                  value={formValues.pvCorrienteA}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Salida Corriente (A)"
                  id="dato-salidaCorrienteA"
                  name="salidaCorrienteA"
                  type="number"
                  value={formValues.salidaCorrienteA}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Salida Frecuencia (Hz)"
                  id="dato-salidaFrecuenciaHz"
                  name="salidaFrecuenciaHz"
                  type="number"
                  value={formValues.salidaFrecuenciaHz}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Temperatura (°C)"
                  id="dato-temperaturaC"
                  name="temperaturaC"
                  type="number"
                  value={formValues.temperaturaC}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Bus Voltaje (V)"
                  id="dato-busVoltajeV"
                  name="busVoltajeV"
                  type="number"
                  value={formValues.busVoltajeV}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="SOC (%)"
                  id="dato-socPct"
                  name="socPct"
                  type="number"
                  value={formValues.socPct}
                  onChange={handleInputChange}
                  fullWidth
                />
                <FormControl fullWidth>
                  <InputLabel id="dato-inversor-label">Inversor</InputLabel>
                  <Select
                    labelId="dato-inversor-label"
                    id="dato-inversorId"
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
                ¿Seguro que quiere eliminar el Dato ID: <strong>{selectedEntity.id}</strong>?
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

export default DatoInversor;
