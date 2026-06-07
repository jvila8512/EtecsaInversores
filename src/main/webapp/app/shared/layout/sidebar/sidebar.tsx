import './sidebar.scss';

import React, { useState } from 'react';
import { useMediaQuery, useTheme } from '@mui/material';
import Drawer from '@mui/material/Drawer';
import Box from '@mui/material/Box';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import IconButton from '@mui/material/IconButton';
import Divider from '@mui/material/Divider';
import Tooltip from '@mui/material/Tooltip';
import Collapse from '@mui/material/Collapse';
import ExpandLessIcon from '@mui/icons-material/ExpandLess';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { NavLink } from 'react-router-dom';

import HomeIcon from '@mui/icons-material/Home';
import MenuIcon from '@mui/icons-material/Menu';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import PersonIcon from '@mui/icons-material/Person';
import AdminPanelSettingsIcon from '@mui/icons-material/AdminPanelSettings';
import BadgeIcon from '@mui/icons-material/Badge';
import BoltIcon from '@mui/icons-material/Bolt';
import DashboardIcon from '@mui/icons-material/Dashboard';
import CategoryIcon from '@mui/icons-material/Category';
import SettingsIcon from '@mui/icons-material/Settings';
import MemoryIcon from '@mui/icons-material/Memory';
import HubIcon from '@mui/icons-material/Hub';
import SolarPowerIcon from '@mui/icons-material/SolarPower';
import BarChartIcon from '@mui/icons-material/BarChart';
import WarningAmberIcon from '@mui/icons-material/WarningAmber';
import NotificationsActiveIcon from '@mui/icons-material/NotificationsActive';
import LockIcon from '@mui/icons-material/Lock';
import LogoutIcon from '@mui/icons-material/Logout';

import { Translate } from 'react-jhipster';
import { useAppDispatch } from 'app/config/store';
import { setLocale } from 'app/shared/reducers/locale';
import { logout } from 'app/shared/reducers/authentication';

import { UserProfile, ProfileInfo } from './user-profile';

const DRAWER_WIDTH = 260;
const DRAWER_WIDTH_COLLAPSED = 72;

export interface ISidebarProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
  currentLocale: string;
  onCollapse?: (collapsed: boolean) => void;
}

interface NavItemProps {
  to: string;
  icon: React.ReactNode;
  label: string;
  collapsed: boolean;
  isMobile: boolean;
  inset?: boolean;
}

const NavItem: React.FC<NavItemProps> = ({ to, icon, label, collapsed, isMobile, inset }) => {
  const content = (
    <ListItem disablePadding>
      <ListItemButton
        component={NavLink}
        to={to}
        sx={{
          minHeight: inset ? 36 : 40,
          px: inset ? 3 : 2,
          justifyContent: collapsed && !isMobile ? 'center' : 'initial',
          color: 'text.primary',
          borderRadius: 1,
          mx: 1,
          '&.active': {
            bgcolor: '#eff6ff',
            color: '#2563eb',
            '& .MuiListItemIcon-root': { color: '#2563eb' },
          },
        }}
      >
        <ListItemIcon sx={{ minWidth: 0, mr: collapsed && !isMobile ? 0 : 2, color: 'inherit' }}>{icon}</ListItemIcon>
        {!collapsed && <ListItemText primary={label} primaryTypographyProps={{ fontSize: inset ? '0.8rem' : '0.85rem' }} />}
      </ListItemButton>
    </ListItem>
  );

  if (collapsed && !isMobile) {
    return (
      <Tooltip title={label} placement="right">
        {content}
      </Tooltip>
    );
  }
  return content;
};

interface ExpandableSectionProps {
  icon: React.ReactNode;
  label: string;
  collapsed: boolean;
  isMobile: boolean;
  open: boolean;
  onClick: () => void;
  children: React.ReactNode;
}

const ExpandableSection: React.FC<ExpandableSectionProps> = ({ icon, label, collapsed, isMobile, open, onClick, children }) => {
  const content = (
    <>
      <ListItem disablePadding>
        <ListItemButton
          onClick={onClick}
          sx={{
            minHeight: 40,
            px: 2,
            justifyContent: collapsed && !isMobile ? 'center' : 'initial',
            color: 'text.primary',
            borderRadius: 1,
            mx: 1,
          }}
        >
          <ListItemIcon sx={{ minWidth: 0, mr: collapsed && !isMobile ? 0 : 2, color: 'inherit' }}>{icon}</ListItemIcon>
          {!collapsed && (
            <>
              <ListItemText primary={label} primaryTypographyProps={{ fontSize: '0.85rem', fontWeight: 500 }} />
              {open ? (
                <ExpandLessIcon fontSize="small" sx={{ color: 'text.secondary' }} />
              ) : (
                <ExpandMoreIcon fontSize="small" sx={{ color: 'text.secondary' }} />
              )}
            </>
          )}
        </ListItemButton>
      </ListItem>
      {!collapsed && (
        <Collapse in={open} timeout="auto">
          {children}
        </Collapse>
      )}
    </>
  );

  if (collapsed && !isMobile) {
    return (
      <Tooltip title={label} placement="right">
        {content}
      </Tooltip>
    );
  }
  return <>{content}</>;
};

const Sidebar = (props: ISidebarProps) => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));

  const [collapsed, setCollapsed] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);
  const [entitiesOpen, setEntitiesOpen] = useState(true);
  const [monitorOpen, setMonitorOpen] = useState(true);
  const [adminOpen, setAdminOpen] = useState(false);
  const [accountOpen, setAccountOpen] = useState(false);

  const dispatch = useAppDispatch();

  const handleLocaleChange = () => {
    const current = sessionStorage.getItem('locale') || 'en';
    const next = current === 'en' ? 'es' : 'en';
    sessionStorage.setItem('locale', next);
    dispatch(setLocale(next));
  };

  const toggleCollapsed = () => {
    const newCollapsed = !collapsed;
    setCollapsed(newCollapsed);
    props.onCollapse?.(newCollapsed);
  };

  const toggleMobile = () => setMobileOpen(!mobileOpen);

  const drawerWidth = collapsed && !isMobile ? DRAWER_WIDTH_COLLAPSED : DRAWER_WIDTH;

  const renderDevRibbon = () =>
    !props.isInProduction ? (
      <Box className="sidebar-ribbon">
        <Translate contentKey={`global.ribbon.${props.ribbonEnv}`} />
      </Box>
    ) : null;

  const renderDrawerContent = () => (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100%', overflow: 'hidden' }}>
      {renderDevRibbon()}

      <Box sx={{ display: 'flex', alignItems: 'center', px: 2, minHeight: 64, borderBottom: '1px solid rgba(0,0,0,0.08)' }}>
        <IconButton
          onClick={toggleCollapsed}
          sx={{ color: 'text.secondary', '&:hover': { color: 'text.primary', bgcolor: 'rgba(0,0,0,0.06)' } }}
        >
          {collapsed && !isMobile ? <MenuIcon /> : <ChevronLeftIcon />}
        </IconButton>
        {!collapsed && !isMobile && (
          <Box sx={{ ml: 1, flex: 1 }}>
            <Box sx={{ fontSize: '1.1rem', fontWeight: 600, color: 'text.primary' }}>ETECSA</Box>
            <Box sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>Solar Monitoring</Box>
          </Box>
        )}
      </Box>

      <Box className="sidebar-profile" sx={{ display: collapsed || !props.isAuthenticated ? 'none' : 'flex' }}>
        <UserProfile collapsed={collapsed} isMobile={isMobile} />
        <ProfileInfo collapsed={collapsed} isMobile={isMobile} />
      </Box>

      <Divider sx={{ borderColor: 'rgba(0,0,0,0.08)' }} />

      <Box className="sidebar-nav" sx={{ flex: 1, overflowY: 'auto', overflowX: 'hidden', py: 1 }}>
        <List>
          <NavItem to="/" icon={<HomeIcon />} label="Inicio" collapsed={collapsed} isMobile={isMobile} />
        </List>

        {props.isAuthenticated && (
          <>
            <List component="nav">
              <ExpandableSection
                icon={<BoltIcon />}
                label="Monitor Solar"
                collapsed={collapsed}
                isMobile={isMobile}
                open={monitorOpen}
                onClick={() => setMonitorOpen(!monitorOpen)}
              >
                <List disablePadding>
                  <NavItem
                    to="/monitor"
                    icon={<DashboardIcon fontSize="small" />}
                    label="Dashboard"
                    collapsed={false}
                    isMobile={isMobile}
                    inset
                  />
                  <NavItem
                    to="/monitor/history"
                    icon={<BarChartIcon fontSize="small" />}
                    label="Historial"
                    collapsed={false}
                    isMobile={isMobile}
                    inset
                  />
                  <NavItem
                    to="/monitor/alerts"
                    icon={<NotificationsActiveIcon fontSize="small" />}
                    label="Alertas"
                    collapsed={false}
                    isMobile={isMobile}
                    inset
                  />
                  <NavItem
                    to="/monitor/settings"
                    icon={<SettingsIcon fontSize="small" />}
                    label="Configuración"
                    collapsed={false}
                    isMobile={isMobile}
                    inset
                  />
                </List>
              </ExpandableSection>
            </List>

            <List component="nav">
              <ExpandableSection
                icon={<CategoryIcon />}
                label="Entidades"
                collapsed={collapsed}
                isMobile={isMobile}
                open={entitiesOpen}
                onClick={() => setEntitiesOpen(!entitiesOpen)}
              >
                <List disablePadding>
                  <NavItem
                    to="/marca"
                    icon={<CategoryIcon fontSize="small" />}
                    label="Marcas"
                    collapsed={false}
                    isMobile={isMobile}
                    inset
                  />
                  <NavItem
                    to="/modelo-inversor"
                    icon={<SettingsIcon fontSize="small" />}
                    label="Modelos Inversor"
                    collapsed={false}
                    isMobile={isMobile}
                    inset
                  />
                  <NavItem
                    to="/mapeo-registro"
                    icon={<BoltIcon fontSize="small" />}
                    label="Mapeo Registros"
                    collapsed={false}
                    isMobile={isMobile}
                    inset
                  />
                  <NavItem to="/sitio" icon={<HubIcon fontSize="small" />} label="Sitios" collapsed={false} isMobile={isMobile} inset />
                  <NavItem
                    to="/gateway-tcp"
                    icon={<MemoryIcon fontSize="small" />}
                    label="Gateways TCP"
                    collapsed={false}
                    isMobile={isMobile}
                    inset
                  />
                  <NavItem
                    to="/inversor"
                    icon={<SolarPowerIcon fontSize="small" />}
                    label="Inversores"
                    collapsed={false}
                    isMobile={isMobile}
                    inset
                  />
                  <NavItem
                    to="/dato-inversor"
                    icon={<BarChartIcon fontSize="small" />}
                    label="Datos Inversor"
                    collapsed={false}
                    isMobile={isMobile}
                    inset
                  />
                  <NavItem
                    to="/dato-eficiencia"
                    icon={<BarChartIcon fontSize="small" />}
                    label="Datos Eficiencia"
                    collapsed={false}
                    isMobile={isMobile}
                    inset
                  />
                  <NavItem
                    to="/alerta-config"
                    icon={<WarningAmberIcon fontSize="small" />}
                    label="Config. Alertas"
                    collapsed={false}
                    isMobile={isMobile}
                    inset
                  />
                  <NavItem
                    to="/evento-alerta"
                    icon={<NotificationsActiveIcon fontSize="small" />}
                    label="Eventos Alerta"
                    collapsed={false}
                    isMobile={isMobile}
                    inset
                  />
                </List>
              </ExpandableSection>
            </List>
          </>
        )}

        {props.isAuthenticated && props.isAdmin && (
          <List component="nav">
            <ExpandableSection
              icon={<AdminPanelSettingsIcon />}
              label="Administración"
              collapsed={collapsed}
              isMobile={isMobile}
              open={adminOpen}
              onClick={() => setAdminOpen(!adminOpen)}
            >
              <List disablePadding>
                <NavItem
                  to="/admin/user-management"
                  icon={<PersonIcon fontSize="small" />}
                  label="Usuarios"
                  collapsed={false}
                  isMobile={isMobile}
                  inset
                />
                <NavItem
                  to="/admin/metrics"
                  icon={<BarChartIcon fontSize="small" />}
                  label="Métricas"
                  collapsed={false}
                  isMobile={isMobile}
                  inset
                />
                <NavItem
                  to="/admin/health"
                  icon={<MemoryIcon fontSize="small" />}
                  label="Salud"
                  collapsed={false}
                  isMobile={isMobile}
                  inset
                />
                <NavItem
                  to="/admin/configuration"
                  icon={<SettingsIcon fontSize="small" />}
                  label="Configuración"
                  collapsed={false}
                  isMobile={isMobile}
                  inset
                />
                <NavItem
                  to="/admin/logs"
                  icon={<CategoryIcon fontSize="small" />}
                  label="Logs"
                  collapsed={false}
                  isMobile={isMobile}
                  inset
                />
                {props.isOpenAPIEnabled && (
                  <NavItem
                    to="/admin/docs"
                    icon={<MemoryIcon fontSize="small" />}
                    label="API"
                    collapsed={false}
                    isMobile={isMobile}
                    inset
                  />
                )}
              </List>
            </ExpandableSection>
          </List>
        )}

        {props.isAuthenticated && (
          <List component="nav">
            <ExpandableSection
              icon={<PersonIcon />}
              label="Cuenta"
              collapsed={collapsed}
              isMobile={isMobile}
              open={accountOpen}
              onClick={() => setAccountOpen(!accountOpen)}
            >
              <List disablePadding>
                <NavItem
                  to="/account/settings"
                  icon={<SettingsIcon fontSize="small" />}
                  label="Ajustes"
                  collapsed={false}
                  isMobile={isMobile}
                  inset
                />
                <NavItem
                  to="/account/password"
                  icon={<LockIcon fontSize="small" />}
                  label="Contraseña"
                  collapsed={false}
                  isMobile={isMobile}
                  inset
                />
                <ListItem disablePadding>
                  <ListItemButton
                    onClick={() => dispatch(logout())}
                    sx={{ minHeight: 36, px: 3, borderRadius: 1, mx: 1, color: 'error.main' }}
                  >
                    <ListItemIcon sx={{ minWidth: 0, mr: 2, color: 'error.main' }}>
                      <LogoutIcon fontSize="small" />
                    </ListItemIcon>
                    <ListItemText primary="Cerrar sesión" primaryTypographyProps={{ fontSize: '0.8rem' }} />
                  </ListItemButton>
                </ListItem>
              </List>
            </ExpandableSection>
          </List>
        )}

        <Box sx={{ mt: 'auto' }}>
          <List>
            <ListItem disablePadding>
              <ListItemButton
                onClick={handleLocaleChange}
                sx={{
                  minHeight: 40,
                  px: 2,
                  justifyContent: collapsed && !isMobile ? 'center' : 'initial',
                  color: 'text.secondary',
                  borderRadius: 1,
                  mx: 1,
                }}
              >
                <ListItemIcon sx={{ minWidth: 0, mr: collapsed && !isMobile ? 0 : 2, color: 'text.secondary' }}>
                  <BadgeIcon />
                </ListItemIcon>
                {!collapsed && <ListItemText primary={props.currentLocale.toUpperCase()} />}
              </ListItemButton>
            </ListItem>
          </List>
        </Box>
      </Box>
    </Box>
  );

  const renderMobileMenuButton = () =>
    isMobile && (
      <IconButton
        color="inherit"
        aria-label="open drawer"
        edge="start"
        onClick={toggleMobile}
        sx={{
          position: 'fixed',
          top: 8,
          left: 8,
          zIndex: theme.zIndex.drawer + 1,
          bgcolor: '#2563eb',
          color: '#fff',
          '&:hover': { bgcolor: '#1d4ed8' },
        }}
      >
        <MenuIcon />
      </IconButton>
    );

  return (
    <>
      {renderMobileMenuButton()}
      <Drawer
        variant="temporary"
        open={mobileOpen}
        onClose={toggleMobile}
        ModalProps={{ keepMounted: true }}
        sx={{
          display: { xs: 'block', md: 'none' },
          '& .MuiDrawer-paper': {
            boxSizing: 'border-box',
            width: DRAWER_WIDTH,
            bgcolor: '#ffffff',
            color: 'text.primary',
          },
        }}
      >
        {renderDrawerContent()}
      </Drawer>
      <Drawer
        variant="permanent"
        sx={{
          display: { xs: 'none', md: 'block' },
          '& .MuiDrawer-paper': {
            boxSizing: 'border-box',
            width: drawerWidth,
            bgcolor: '#ffffff',
            color: 'text.primary',
            borderRight: '1px solid #e2e8f0',
            transition: theme.transitions.create('width', {
              easing: theme.transitions.easing.sharp,
              duration: theme.transitions.duration.leavingScreen,
            }),
            overflowX: 'hidden',
          },
        }}
        open
      >
        {renderDrawerContent()}
      </Drawer>
    </>
  );
};

export default Sidebar;
