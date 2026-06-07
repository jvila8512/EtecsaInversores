import React from 'react';
import { Link } from 'react-router-dom';
import { Menu, MenuItem, ListItemIcon, ListItemText, Divider } from '@mui/material';
import CategoryIcon from '@mui/icons-material/Category';
import BoltIcon from '@mui/icons-material/Bolt';
import SettingsIcon from '@mui/icons-material/Settings';
import MemoryIcon from '@mui/icons-material/Memory';
import HubIcon from '@mui/icons-material/Hub';
import SolarPowerIcon from '@mui/icons-material/SolarPower';
import BarChartIcon from '@mui/icons-material/BarChart';
import WarningAmberIcon from '@mui/icons-material/WarningAmber';
import NotificationsActiveIcon from '@mui/icons-material/NotificationsActive';

interface EntitiesMenuSectionProps {
  anchorEl: HTMLElement | null;
  onClose: () => void;
}

export const EntitiesMenuSection: React.FC<EntitiesMenuSectionProps> = ({ anchorEl, onClose }) => {
  const handleClick = () => {
    onClose();
  };

  return (
    <Menu
      anchorEl={anchorEl}
      open={Boolean(anchorEl)}
      onClose={onClose}
      PaperProps={{
        sx: { minWidth: 240 },
      }}
    >
      <MenuItem component={Link} to="/marca" onClick={handleClick}>
        <ListItemIcon>
          <CategoryIcon fontSize="small" />
        </ListItemIcon>
        <ListItemText primary="Marcas" />
      </MenuItem>

      <MenuItem component={Link} to="/modelo-inversor" onClick={handleClick}>
        <ListItemIcon>
          <SettingsIcon fontSize="small" />
        </ListItemIcon>
        <ListItemText primary="Modelos Inversor" />
      </MenuItem>

      <MenuItem component={Link} to="/mapeo-registro" onClick={handleClick}>
        <ListItemIcon>
          <BoltIcon fontSize="small" />
        </ListItemIcon>
        <ListItemText primary="Mapeo Registros" />
      </MenuItem>

      <Divider />

      <MenuItem component={Link} to="/sitio" onClick={handleClick}>
        <ListItemIcon>
          <HubIcon fontSize="small" />
        </ListItemIcon>
        <ListItemText primary="Sitios" />
      </MenuItem>

      <MenuItem component={Link} to="/gateway-tcp" onClick={handleClick}>
        <ListItemIcon>
          <SettingsIcon fontSize="small" />
        </ListItemIcon>
        <ListItemText primary="Gateways TCP" />
      </MenuItem>

      <MenuItem component={Link} to="/inversor" onClick={handleClick}>
        <ListItemIcon>
          <SolarPowerIcon fontSize="small" />
        </ListItemIcon>
        <ListItemText primary="Inversores" />
      </MenuItem>

      <Divider />

      <MenuItem component={Link} to="/dato-inversor" onClick={handleClick}>
        <ListItemIcon>
          <BarChartIcon fontSize="small" />
        </ListItemIcon>
        <ListItemText primary="Datos Inversor" />
      </MenuItem>

      <MenuItem component={Link} to="/dato-eficiencia" onClick={handleClick}>
        <ListItemIcon>
          <BarChartIcon fontSize="small" />
        </ListItemIcon>
        <ListItemText primary="Datos Eficiencia" />
      </MenuItem>

      <Divider />

      <MenuItem component={Link} to="/alerta-config" onClick={handleClick}>
        <ListItemIcon>
          <WarningAmberIcon fontSize="small" />
        </ListItemIcon>
        <ListItemText primary="Config. Alertas" />
      </MenuItem>

      <MenuItem component={Link} to="/evento-alerta" onClick={handleClick}>
        <ListItemIcon>
          <NotificationsActiveIcon fontSize="small" />
        </ListItemIcon>
        <ListItemText primary="Eventos Alerta" />
      </MenuItem>

      <Divider />

      <MenuItem component={Link} to="/monitor" onClick={handleClick}>
        <ListItemIcon>
          <MemoryIcon fontSize="small" color="primary" />
        </ListItemIcon>
        <ListItemText primary="Monitor Solar (Live)" primaryTypographyProps={{ fontWeight: 600, color: '#2563eb' }} />
      </MenuItem>
    </Menu>
  );
};
