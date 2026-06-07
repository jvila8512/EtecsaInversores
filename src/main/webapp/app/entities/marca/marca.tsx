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
import { getEntities, createEntity, updateEntity, deleteEntity } from './marca.reducer';
import { IMarca } from 'app/shared/model/marca.model';

const ALL_COLUMNS = [
  { key: 'id', label: 'Id', default: true },
  { key: 'nombre', label: 'Nombre', default: true },
  { key: 'fabricante', label: 'Fabricante', default: true },
  { key: 'registroInicio', label: 'Reg. Inicio', default: true },
  { key: 'registroFin', label: 'Reg. Fin', default: true },
  { key: 'registroInfoInicio', label: 'Reg. Info Inicio', default: false },
  { key: 'registroInfoFin', label: 'Reg. Info Fin', default: false },
  { key: 'baudRateDefault', label: 'Baud Rate', default: false },
  { key: 'descripcion', label: 'Descripción', default: false },
  { key: 'activaMarcaDefault', label: 'Activa Default', default: true },
];

const emptyForm = {
  nombre: '',
  fabricante: '',
  registroInicio: '',
  registroFin: '',
  registroInfoInicio: '',
  registroInfoFin: '',
  baudRateDefault: '',
  descripcion: '',
  activaMarcaDefault: false,
};

export const Marca = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const marcaList = useAppSelector(state => state.marca.entities);
  const loading = useAppSelector(state => state.marca.loading);
  const updating = useAppSelector(state => state.marca.updating);
  const updateSuccess = useAppSelector(state => state.marca.updateSuccess);
  const totalItems = useAppSelector(state => state.marca.totalItems);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const [selectedEntity, setSelectedEntity] = useState<IMarca | null>(null);
  const [isOpen, setIsOpen] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [globalFilter, setGlobalFilter] = useState('');
  const [columnMenuAnchor, setColumnMenuAnchor] = useState<null | HTMLElement>(null);
  const [visibleColumns, setVisibleColumns] = useState<string[]>(() => {
    const saved = localStorage.getItem('marca-columns');
    if (saved) return JSON.parse(saved);
    return ALL_COLUMNS.filter(c => c.default).map(c => c.key);
  });
  const [formKey, setFormKey] = useState(0);
  const [formValues, setFormValues] = useState(emptyForm);

  const saveColumns = (cols: string[]) => {
    setVisibleColumns(cols);
    localStorage.setItem('marca-columns', JSON.stringify(cols));
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
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
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

  const sort = (field: string) => {
    setPaginationState({ ...paginationState, order: paginationState.order === ASC ? DESC : ASC, sort: field });
  };

  const handlePagination = (currentPage: number) => setPaginationState({ ...paginationState, activePage: currentPage });
  const handleSyncList = () => sortEntities();

  const filteredList = marcaList?.filter(
    e =>
      !globalFilter ||
      e.nombre?.toLowerCase().includes(globalFilter.toLowerCase()) ||
      e.fabricante?.toLowerCase().includes(globalFilter.toLowerCase()) ||
      e.descripcion?.toLowerCase().includes(globalFilter.toLowerCase()),
  );

  const openNew = () => {
    setSelectedEntity(null);
    setFormValues(emptyForm);
    setFormKey(prev => prev + 1);
    setIsOpen(true);
  };

  const openEdit = (row: IMarca) => {
    setSelectedEntity(row);
    setFormValues({
      nombre: row.nombre || '',
      fabricante: row.fabricante || '',
      registroInicio: row.registroInicio?.toString() || '',
      registroFin: row.registroFin?.toString() || '',
      registroInfoInicio: row.registroInfoInicio?.toString() || '',
      registroInfoFin: row.registroInfoFin?.toString() || '',
      baudRateDefault: row.baudRateDefault?.toString() || '',
      descripcion: row.descripcion || '',
      activaMarcaDefault: row.activaMarcaDefault ?? false,
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
      fabricante: formValues.fabricante || null,
      registroInicio: formValues.registroInicio ? parseInt(formValues.registroInicio, 10) : undefined,
      registroFin: formValues.registroFin ? parseInt(formValues.registroFin, 10) : undefined,
      registroInfoInicio: formValues.registroInfoInicio ? parseInt(formValues.registroInfoInicio, 10) : null,
      registroInfoFin: formValues.registroInfoFin ? parseInt(formValues.registroInfoFin, 10) : null,
      baudRateDefault: formValues.baudRateDefault ? parseInt(formValues.baudRateDefault, 10) : null,
      descripcion: formValues.descripcion || null,
      activaMarcaDefault: formValues.activaMarcaDefault,
    };
    if (selectedEntity?.id) entity.id = selectedEntity.id;
    dispatch(selectedEntity?.id ? updateEntity(entity) : createEntity(entity));
  };

  const openDelete = (row: IMarca) => {
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
          Marcas
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
            Nueva Marca
          </Button>
        </Box>
      </Toolbar>

      {loading && !marcaList?.length && (
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
        <Table sx={{ minWidth: 700 }}>
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
              {showColumn('fabricante') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('fabricante')}>
                  Fabricante
                </TableCell>
              )}
              {showColumn('registroInicio') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('registroInicio')}>
                  Reg. Inicio
                </TableCell>
              )}
              {showColumn('registroFin') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('registroFin')}>
                  Reg. Fin
                </TableCell>
              )}
              {showColumn('registroInfoInicio') && <TableCell sx={{ fontWeight: 600 }}>Reg. Info Inicio</TableCell>}
              {showColumn('registroInfoFin') && <TableCell sx={{ fontWeight: 600 }}>Reg. Info Fin</TableCell>}
              {showColumn('baudRateDefault') && <TableCell sx={{ fontWeight: 600 }}>Baud Rate</TableCell>}
              {showColumn('descripcion') && <TableCell sx={{ fontWeight: 600 }}>Descripción</TableCell>}
              {showColumn('activaMarcaDefault') && <TableCell sx={{ fontWeight: 600 }}>Activa</TableCell>}
              <TableCell sx={{ fontWeight: 600 }}>Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredList?.map((row: IMarca) => (
              <TableRow key={row.id} hover>
                {showColumn('id') && <TableCell>{row.id}</TableCell>}
                {showColumn('nombre') && <TableCell>{row.nombre}</TableCell>}
                {showColumn('fabricante') && <TableCell>{row.fabricante}</TableCell>}
                {showColumn('registroInicio') && <TableCell>{row.registroInicio}</TableCell>}
                {showColumn('registroFin') && <TableCell>{row.registroFin}</TableCell>}
                {showColumn('registroInfoInicio') && <TableCell>{row.registroInfoInicio}</TableCell>}
                {showColumn('registroInfoFin') && <TableCell>{row.registroInfoFin}</TableCell>}
                {showColumn('baudRateDefault') && <TableCell>{row.baudRateDefault}</TableCell>}
                {showColumn('descripcion') && (
                  <TableCell sx={{ maxWidth: 200, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                    {row.descripcion}
                  </TableCell>
                )}
                {showColumn('activaMarcaDefault') && <TableCell>{row.activaMarcaDefault ? 'Sí' : 'No'}</TableCell>}
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

      {totalItems && marcaList && marcaList.length > 0 ? (
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
        <DialogTitle>{selectedEntity ? 'Editar Marca' : 'Nueva Marca'}</DialogTitle>
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
                  id="marca-nombre"
                  name="nombre"
                  value={formValues.nombre}
                  onChange={handleInputChange}
                  fullWidth
                  required
                  error={!formValues.nombre}
                  helperText={!formValues.nombre ? 'Requerido' : ''}
                />
                <TextField
                  label="Fabricante"
                  id="marca-fabricante"
                  name="fabricante"
                  value={formValues.fabricante}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Registro Inicio"
                  id="marca-registroInicio"
                  name="registroInicio"
                  type="number"
                  value={formValues.registroInicio}
                  onChange={handleInputChange}
                  fullWidth
                  required
                  error={!formValues.registroInicio}
                  helperText={!formValues.registroInicio ? 'Requerido' : ''}
                />
                <TextField
                  label="Registro Fin"
                  id="marca-registroFin"
                  name="registroFin"
                  type="number"
                  value={formValues.registroFin}
                  onChange={handleInputChange}
                  fullWidth
                  required
                  error={!formValues.registroFin}
                  helperText={!formValues.registroFin ? 'Requerido' : ''}
                />
                <TextField
                  label="Registro Info Inicio"
                  id="marca-registroInfoInicio"
                  name="registroInfoInicio"
                  type="number"
                  value={formValues.registroInfoInicio}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Registro Info Fin"
                  id="marca-registroInfoFin"
                  name="registroInfoFin"
                  type="number"
                  value={formValues.registroInfoFin}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Baud Rate Default"
                  id="marca-baudRateDefault"
                  name="baudRateDefault"
                  type="number"
                  value={formValues.baudRateDefault}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Descripción"
                  id="marca-descripcion"
                  name="descripcion"
                  value={formValues.descripcion}
                  onChange={handleInputChange}
                  fullWidth
                  multiline
                  rows={2}
                />
                <FormControlLabel
                  control={<Checkbox name="activaMarcaDefault" checked={formValues.activaMarcaDefault} onChange={handleInputChange} />}
                  label="Activa por defecto"
                />
                <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 1, mt: 2 }}>
                  <Button onClick={closeDialog}>Cancelar</Button>
                  <Button
                    variant="contained"
                    onClick={save}
                    disabled={updating || !formValues.nombre || !formValues.registroInicio || !formValues.registroFin}
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
                ¿Seguro que quiere eliminar la Marca: <strong>{selectedEntity.nombre}</strong>?
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

export default Marca;
