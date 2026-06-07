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
import { getEntities, createEntity, updateEntity, deleteEntity } from './sitio.reducer';
import { ISitio } from 'app/shared/model/sitio.model';

const ALL_COLUMNS = [
  { key: 'id', label: 'Id', default: true },
  { key: 'nombre', label: 'Nombre', default: true },
  { key: 'descripcion', label: 'Descripción', default: false },
  { key: 'ubicacion', label: 'Ubicación', default: true },
  { key: 'contacto', label: 'Contacto', default: true },
  { key: 'telefono', label: 'Teléfono', default: true },
  { key: 'activo', label: 'Activo', default: true },
  { key: 'fechaInstalacion', label: 'Fecha Instalación', default: false },
];

const emptyForm = { nombre: '', descripcion: '', ubicacion: '', contacto: '', telefono: '', activo: false, fechaInstalacion: '' };

export const Sitio = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const sitioList = useAppSelector(state => state.sitio.entities);
  const loading = useAppSelector(state => state.sitio.loading);
  const updating = useAppSelector(state => state.sitio.updating);
  const updateSuccess = useAppSelector(state => state.sitio.updateSuccess);
  const totalItems = useAppSelector(state => state.sitio.totalItems);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [selectedEntity, setSelectedEntity] = useState<ISitio | null>(null);
  const [isOpen, setIsOpen] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [globalFilter, setGlobalFilter] = useState('');
  const [columnMenuAnchor, setColumnMenuAnchor] = useState<null | HTMLElement>(null);
  const [visibleColumns, setVisibleColumns] = useState<string[]>(() => {
    const saved = localStorage.getItem('sitio-columns');
    if (saved) return JSON.parse(saved);
    return ALL_COLUMNS.filter(c => c.default).map(c => c.key);
  });
  const [formKey, setFormKey] = useState(0);
  const [formValues, setFormValues] = useState(emptyForm);

  const saveColumns = (cols: string[]) => {
    setVisibleColumns(cols);
    localStorage.setItem('sitio-columns', JSON.stringify(cols));
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

  const sort = (field: string) =>
    setPaginationState({ ...paginationState, order: paginationState.order === ASC ? DESC : ASC, sort: field });
  const handlePagination = (currentPage: number) => setPaginationState({ ...paginationState, activePage: currentPage });
  const handleSyncList = () => sortEntities();

  const filteredList = sitioList?.filter(
    e =>
      !globalFilter ||
      e.nombre?.toLowerCase().includes(globalFilter.toLowerCase()) ||
      e.ubicacion?.toLowerCase().includes(globalFilter.toLowerCase()) ||
      e.contacto?.toLowerCase().includes(globalFilter.toLowerCase()),
  );

  const openNew = () => {
    setSelectedEntity(null);
    setFormValues(emptyForm);
    setFormKey(prev => prev + 1);
    setIsOpen(true);
  };
  const openEdit = (row: ISitio) => {
    setSelectedEntity(row);
    setFormValues({
      nombre: row.nombre || '',
      descripcion: row.descripcion || '',
      ubicacion: row.ubicacion || '',
      contacto: row.contacto || '',
      telefono: row.telefono || '',
      activo: row.activo ?? false,
      fechaInstalacion: row.fechaInstalacion ? row.fechaInstalacion.format('YYYY-MM-DD') : '',
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
      contacto: formValues.contacto || null,
      telefono: formValues.telefono || null,
      activo: formValues.activo,
      fechaInstalacion: formValues.fechaInstalacion || null,
    };
    if (selectedEntity?.id) entity.id = selectedEntity.id;
    dispatch(selectedEntity?.id ? updateEntity(entity) : createEntity(entity));
  };
  const openDelete = (row: ISitio) => {
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
          Sitios
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
            Nuevo Sitio
          </Button>
        </Box>
      </Toolbar>

      {loading && !sitioList?.length && (
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
              {showColumn('descripcion') && <TableCell sx={{ fontWeight: 600 }}>Descripción</TableCell>}
              {showColumn('ubicacion') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('ubicacion')}>
                  Ubicación
                </TableCell>
              )}
              {showColumn('contacto') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('contacto')}>
                  Contacto
                </TableCell>
              )}
              {showColumn('telefono') && <TableCell sx={{ fontWeight: 600 }}>Teléfono</TableCell>}
              {showColumn('activo') && (
                <TableCell sx={{ fontWeight: 600, cursor: 'pointer' }} onClick={() => sort('activo')}>
                  Activo
                </TableCell>
              )}
              {showColumn('fechaInstalacion') && <TableCell sx={{ fontWeight: 600 }}>Fecha Instalación</TableCell>}
              <TableCell sx={{ fontWeight: 600 }}>Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredList?.map((row: ISitio) => (
              <TableRow key={row.id} hover>
                {showColumn('id') && <TableCell>{row.id}</TableCell>}
                {showColumn('nombre') && <TableCell>{row.nombre}</TableCell>}
                {showColumn('descripcion') && (
                  <TableCell sx={{ maxWidth: 200, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                    {row.descripcion}
                  </TableCell>
                )}
                {showColumn('ubicacion') && <TableCell>{row.ubicacion}</TableCell>}
                {showColumn('contacto') && <TableCell>{row.contacto}</TableCell>}
                {showColumn('telefono') && <TableCell>{row.telefono}</TableCell>}
                {showColumn('activo') && <TableCell>{row.activo ? 'Sí' : 'No'}</TableCell>}
                {showColumn('fechaInstalacion') && <TableCell>{row.fechaInstalacion?.format('DD/MM/YYYY')}</TableCell>}
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

      {totalItems && sitioList && sitioList.length > 0 ? (
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
        <DialogTitle>{selectedEntity ? 'Editar Sitio' : 'Nuevo Sitio'}</DialogTitle>
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
                  id="sitio-nombre"
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
                  id="sitio-descripcion"
                  name="descripcion"
                  value={formValues.descripcion}
                  onChange={handleInputChange}
                  fullWidth
                  multiline
                  rows={2}
                />
                <TextField
                  label="Ubicación"
                  id="sitio-ubicacion"
                  name="ubicacion"
                  value={formValues.ubicacion}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Contacto"
                  id="sitio-contacto"
                  name="contacto"
                  value={formValues.contacto}
                  onChange={handleInputChange}
                  fullWidth
                />
                <TextField
                  label="Teléfono"
                  id="sitio-telefono"
                  name="telefono"
                  value={formValues.telefono}
                  onChange={handleInputChange}
                  fullWidth
                />
                <FormControlLabel
                  control={<Checkbox name="activo" checked={formValues.activo} onChange={handleInputChange} />}
                  label="Activo"
                />
                <TextField
                  label="Fecha Instalación"
                  id="sitio-fechaInstalacion"
                  name="fechaInstalacion"
                  type="date"
                  value={formValues.fechaInstalacion}
                  onChange={handleInputChange}
                  fullWidth
                  InputLabelProps={{ shrink: true }}
                />
                <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 1, mt: 2 }}>
                  <Button onClick={closeDialog}>Cancelar</Button>
                  <Button variant="contained" onClick={save} disabled={updating || !formValues.nombre}>
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
                ¿Seguro que quiere eliminar el Sitio: <strong>{selectedEntity.nombre}</strong>?
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

export default Sitio;
