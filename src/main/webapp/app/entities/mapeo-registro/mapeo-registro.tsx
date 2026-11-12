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
import { getEntities, createEntity, updateEntity, deleteEntity } from './mapeo-registro.reducer';
import { getEntities as getMarcas } from 'app/entities/marca/marca.reducer';
import { IMapeoRegistro } from 'app/shared/model/mapeo-registro.model';
import { TipoDato } from 'app/shared/model/enumerations/tipo-dato.model';

const ALL_COLUMNS = [
  { key: 'id', label: 'Id', default: true },
  { key: 'direccionRegistro', label: 'Dir. Registro', default: true },
  { key: 'nombreRegistro', label: 'Nombre Registro', default: true },
  { key: 'campoSql', label: 'Campo SQL', default: true },
  { key: 'tipoDato', label: 'Tipo Dato', default: true },
  { key: 'escala', label: 'Escala', default: false },
  { key: 'unidad', label: 'Unidad', default: true },
  { key: 'activo', label: 'Activo', default: true },
  { key: 'descripcion', label: 'Descripción', default: false },
  { key: 'marca', label: 'Marca', default: true },
];

const emptyForm = {
  direccionRegistro: '',
  nombreRegistro: '',
  campoSql: '',
  tipoDato: '',
  escala: '',
  unidad: '',
  activo: false,
  descripcion: '',
  marcaId: '',
};

export const MapeoRegistro = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const mapeoRegistroList = useAppSelector(state => state.mapeoRegistro.entities);
  const loading = useAppSelector(state => state.mapeoRegistro.loading);
  const updating = useAppSelector(state => state.mapeoRegistro.updating);
  const updateSuccess = useAppSelector(state => state.mapeoRegistro.updateSuccess);
  const totalItems = useAppSelector(state => state.mapeoRegistro.totalItems);
  const marcaList = useAppSelector(state => state.marca.entities);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [selectedEntity, setSelectedEntity] = useState<IMapeoRegistro | null>(null);
  const [isOpen, setIsOpen] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [globalFilter, setGlobalFilter] = useState('');
  const [columnMenuAnchor, setColumnMenuAnchor] = useState<null | HTMLElement>(null);
  const [visibleColumns, setVisibleColumns] = useState<string[]>(() => {
    const saved = localStorage.getItem('mapeo-registro-columns');
    if (saved) return JSON.parse(saved);
    return ALL_COLUMNS.filter(c => c.default).map(c => c.key);
  });
  const [formKey, setFormKey] = useState(0);
  const [formValues, setFormValues] = useState(emptyForm);

  const saveColumns = (cols: string[]) => {
    setVisibleColumns(cols);
    localStorage.setItem('mapeo-registro-columns', JSON.stringify(cols));
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
    dispatch(getMarcas({ page: 0, size: 1000, sort: 'id,asc' }));
  }, []);

  const sort = (field: string) =>
    setPaginationState({ ...paginationState, order: paginationState.order === ASC ? DESC : ASC, sort: field });
  const handlePagination = (currentPage: number) => setPaginationState({ ...paginationState, activePage: currentPage });
  const handleSyncList = () => sortEntities();

  const filteredList = mapeoRegistroList?.filter(
    e =>
      !globalFilter ||
      e.nombreRegistro?.toLowerCase().includes(globalFilter.toLowerCase()) ||
      e.campoSql?.toLowerCase().includes(globalFilter.toLowerCase()) ||
      e.descripcion?.toLowerCase().includes(globalFilter.toLowerCase()),
  );

  const openNew = () => {
    setSelectedEntity(null);
    setFormValues(emptyForm);
    setFormKey(prev => prev + 1);
    setIsOpen(true);
  };
  const openEdit = (row: IMapeoRegistro) => {
    setSelectedEntity(row);
    setFormValues({
      direccionRegistro: row.direccionRegistro?.toString() || '',
      nombreRegistro: row.nombreRegistro || '',
      campoSql: row.campoSql || '',
      tipoDato: row.tipoDato || '',
      escala: row.escala?.toString() || '',
      unidad: row.unidad || '',
      activo: row.activo ?? false,
      descripcion: row.descripcion || '',
      marcaId: row.marca?.id?.toString() || '',
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
      direccionRegistro: formValues.direccionRegistro ? parseInt(formValues.direccionRegistro, 10) : undefined,
      nombreRegistro: formValues.nombreRegistro,
      campoSql: formValues.campoSql,
      tipoDato: formValues.tipoDato || null,
      escala: formValues.escala ? parseFloat(formValues.escala) : null,
      unidad: formValues.unidad || null,
      activo: formValues.activo,
      descripcion: formValues.descripcion || null,
      marca: formValues.marcaId ? { id: parseInt(formValues.marcaId, 10) } : null,
    };
    if (selectedEntity?.id) entity.id = selectedEntity.id;
    dispatch(selectedEntity?.id ? updateEntity(entity) : createEntity(entity));
  };
  const openDelete = (row: IMapeoRegistro) => {
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
          Mapeo de Registros
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
            Nuevo Mapeo
          </Button>
        </Box>
      </Toolbar>

      {loading && !mapeoRegistroList?.length && (
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
              {showColumn('direccionRegistro') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('direccionRegistro')}>
                  Dir. Registro
                </TableCell>
              )}
              {showColumn('nombreRegistro') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('nombreRegistro')}>
                  Nombre Registro
                </TableCell>
              )}
              {showColumn('campoSql') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('campoSql')}>
                  Campo SQL
                </TableCell>
              )}
              {showColumn('tipoDato') && <TableCell sx={{ fontWeight: 600 }}>Tipo Dato</TableCell>}
              {showColumn('escala') && <TableCell sx={{ fontWeight: 600 }}>Escala</TableCell>}
              {showColumn('unidad') && <TableCell sx={{ fontWeight: 600 }}>Unidad</TableCell>}
              {showColumn('activo') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('activo')}>
                  Activo
                </TableCell>
              )}
              {showColumn('descripcion') && <TableCell sx={{ fontWeight: 600 }}>Descripción</TableCell>}
              {showColumn('marca') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('marca.nombre')}>
                  Marca
                </TableCell>
              )}
              <TableCell sx={{ fontWeight: 600 }}>Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredList?.map((row: IMapeoRegistro) => (
              <TableRow key={row.id} hover>
                {showColumn('id') && <TableCell>{row.id}</TableCell>}
                {showColumn('direccionRegistro') && <TableCell>{row.direccionRegistro}</TableCell>}
                {showColumn('nombreRegistro') && <TableCell>{row.nombreRegistro}</TableCell>}
                {showColumn('campoSql') && <TableCell>{row.campoSql}</TableCell>}
                {showColumn('tipoDato') && <TableCell>{row.tipoDato}</TableCell>}
                {showColumn('escala') && <TableCell>{row.escala}</TableCell>}
                {showColumn('unidad') && <TableCell>{row.unidad}</TableCell>}
                {showColumn('activo') && <TableCell>{row.activo ? 'Sí' : 'No'}</TableCell>}
                {showColumn('descripcion') && (
                  <TableCell sx={{ maxWidth: 200, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                    {row.descripcion}
                  </TableCell>
                )}
                {showColumn('marca') && <TableCell>{row.marca?.nombre}</TableCell>}
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

      {totalItems && mapeoRegistroList && mapeoRegistroList.length > 0 ? (
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
        <DialogTitle>{selectedEntity ? 'Editar Mapeo de Registro' : 'Nuevo Mapeo de Registro'}</DialogTitle>
        <DialogContent>
          {loading ? (
            <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
              <CircularProgress />
            </Box>
          ) : (
            <form key={formKey}>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 1 }}>
                <TextField
                  label="Dirección Registro"
                  id="mapeo-direccionRegistro"
                  name="direccionRegistro"
                  type="number"
                  value={formValues.direccionRegistro}
                  onChange={handleInputChange}
                  fullWidth
                  required
                  error={!formValues.direccionRegistro}
                  helperText={!formValues.direccionRegistro ? 'Requerido' : ''}
                />
                <TextField
                  label="Nombre Registro"
                  id="mapeo-nombreRegistro"
                  name="nombreRegistro"
                  value={formValues.nombreRegistro}
                  onChange={handleInputChange}
                  fullWidth
                  required
                  error={!formValues.nombreRegistro}
                  helperText={!formValues.nombreRegistro ? 'Requerido' : ''}
                />
                <TextField
                  label="Campo SQL"
                  id="mapeo-campoSql"
                  name="campoSql"
                  value={formValues.campoSql}
                  onChange={handleInputChange}
                  fullWidth
                  required
                  error={!formValues.campoSql}
                  helperText={!formValues.campoSql ? 'Requerido' : ''}
                />
                <FormControl fullWidth>
                  <InputLabel id="mapeo-tipoDato-label">Tipo Dato</InputLabel>
                  <Select
                    labelId="mapeo-tipoDato-label"
                    id="mapeo-tipoDato"
                    name="tipoDato"
                    value={formValues.tipoDato}
                    onChange={e => setFormValues(prev => ({ ...prev, tipoDato: e.target.value }))}
                    label="Tipo Dato"
                  >
                    <MenuItem value="">— Seleccionar —</MenuItem>
                    {Object.keys(TipoDato).map(k => (
                      <MenuItem key={k} value={k}>
                        {TipoDato[k as keyof typeof TipoDato]}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
                <TextField
                  label="Escala"
                  id="mapeo-escala"
                  name="escala"
                  type="number"
                  value={formValues.escala}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Unidad"
                  id="mapeo-unidad"
                  name="unidad"
                  value={formValues.unidad}
                  onChange={handleInputChange}
                  fullWidth
                />
                <FormControlLabel
                  control={<Checkbox name="activo" checked={formValues.activo} onChange={handleInputChange} />}
                  label="Activo"
                />
                <TextField
                  label="Descripción"
                  id="mapeo-descripcion"
                  name="descripcion"
                  value={formValues.descripcion}
                  onChange={handleInputChange}
                  fullWidth
                  multiline
                  rows={2}
                />
                <FormControl fullWidth>
                  <InputLabel id="mapeo-marca-label">Marca</InputLabel>
                  <Select
                    labelId="mapeo-marca-label"
                    id="mapeo-marcaId"
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
                <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 1, mt: 2 }}>
                  <Button onClick={closeDialog}>Cancelar</Button>
                  <Button
                    variant="contained"
                    onClick={save}
                    disabled={updating || !formValues.direccionRegistro || !formValues.nombreRegistro || !formValues.campoSql}
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
                ¿Seguro que quiere eliminar el Mapeo: <strong>{selectedEntity.nombreRegistro}</strong>?
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

export default MapeoRegistro;
