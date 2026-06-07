import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import {
  Box,
  IconButton,
  Badge,
  Avatar,
  Menu,
  MenuItem,
  Typography,
  Divider,
  Button,
  InputBase,
  ListItemIcon,
  ListItemText,
} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import NotificationsIcon from '@mui/icons-material/Notifications';
import PersonIcon from '@mui/icons-material/Person';
import SettingsIcon from '@mui/icons-material/Settings';
import LogoutIcon from '@mui/icons-material/Logout';
import WarningIcon from '@mui/icons-material/Warning';
import InfoIcon from '@mui/icons-material/Info';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import { useAppSelector } from 'app/config/store';

const mockNotifications = [{ id: 1, title: 'Sistema', message: 'Nueva actualización disponible', type: 'info' }];

const TopBar: React.FC = () => {
  const account = useAppSelector(state => state.authentication.account);
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const [accountMenuAnchor, setAccountMenuAnchor] = useState<null | HTMLElement>(null);
  const [notificationsAnchor, setNotificationsAnchor] = useState<null | HTMLElement>(null);

  const getUserInitials = () => {
    if (!account) return '?';
    const first = account.firstName?.[0] || '';
    const last = account.lastName?.[0] || '';
    return (first + last).toUpperCase() || account.login?.[0].toUpperCase() || '?';
  };

  const getUserDisplayName = () => {
    if (!account) return 'Usuario';
    return `${account.firstName || ''} ${account.lastName || ''}`.trim() || account.login || 'Usuario';
  };

  const getNotificationIcon = (type: string) => {
    switch (type) {
      case 'success':
        return <CheckCircleIcon fontSize="small" sx={{ color: '#22c55e' }} />;
      case 'warning':
        return <WarningIcon fontSize="small" sx={{ color: '#f59e0b' }} />;
      default:
        return <InfoIcon fontSize="small" sx={{ color: '#3b82f6' }} />;
    }
  };

  return (
    <Box
      component="header"
      sx={{
        height: 64,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        px: 3,
        backgroundColor: '#ffffff',
        borderBottom: '1px solid #e2e8f0',
        gap: 2,
      }}
    >
      <Box sx={{ display: 'flex', alignItems: 'center', flex: 1, maxWidth: 400 }}>
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            backgroundColor: '#f8fafc',
            border: '1px solid #e2e8f0',
            borderRadius: 2,
            px: 2,
            py: 0.5,
            flex: 1,
            '&:focus-within': {
              borderColor: '#2563eb',
              backgroundColor: '#ffffff',
            },
          }}
        >
          <SearchIcon sx={{ color: '#64748b', mr: 1 }} />
          <InputBase
            placeholder="Buscar inversores, alertas..."
            sx={{
              flex: 1,
              fontSize: '0.9rem',
              '& input::placeholder': {
                color: '#94a3b8',
                opacity: 1,
              },
            }}
          />
        </Box>
      </Box>

      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
        {isAuthenticated && (
          <>
            <Box>
              <IconButton
                onClick={e => setNotificationsAnchor(e.currentTarget)}
                sx={{
                  color: '#64748b',
                  transition: 'all 0.2s ease',
                  '&:hover': {
                    color: '#1e293b',
                    backgroundColor: 'rgba(30, 41, 59, 0.1)',
                  },
                }}
              >
                <Badge
                  badgeContent={mockNotifications.length}
                  sx={{
                    '& .MuiBadge-badge': {
                      backgroundColor: '#2563eb',
                      color: '#fff',
                      fontWeight: 600,
                    },
                  }}
                >
                  <NotificationsIcon />
                </Badge>
              </IconButton>
            </Box>

            <Menu
              anchorEl={notificationsAnchor}
              open={Boolean(notificationsAnchor)}
              onClose={() => setNotificationsAnchor(null)}
              PaperProps={{
                elevation: 4,
                sx: { mt: 1.5, width: 360, borderRadius: 2, overflow: 'hidden' },
              }}
              transformOrigin={{ horizontal: 'right', vertical: 'top' }}
              anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
            >
              <Box sx={{ px: 2.5, py: 2, borderBottom: '1px solid #e2e8f0', display: 'flex', alignItems: 'center', gap: 1 }}>
                <NotificationsIcon sx={{ fontSize: 20, color: '#2563eb' }} />
                <Typography variant="subtitle1" fontWeight={600}>
                  Notificaciones
                </Typography>
              </Box>
              <Box sx={{ maxHeight: 320, overflowY: 'auto' }}>
                {mockNotifications.map((notification, index) => (
                  <MenuItem
                    key={notification.id}
                    onClick={() => setNotificationsAnchor(null)}
                    sx={{
                      py: 2,
                      px: 2.5,
                      borderBottom: index < mockNotifications.length - 1 ? '1px solid #e2e8f0' : 'none',
                    }}
                  >
                    <ListItemIcon sx={{ minWidth: 36 }}>{getNotificationIcon(notification.type)}</ListItemIcon>
                    <ListItemText
                      primary={notification.title}
                      secondary={notification.message}
                      primaryTypographyProps={{ variant: 'body2', fontWeight: 500 }}
                      secondaryTypographyProps={{ variant: 'caption', sx: { color: 'text.secondary' } }}
                    />
                  </MenuItem>
                ))}
              </Box>
            </Menu>
          </>
        )}

        {isAuthenticated ? (
          <>
            <IconButton
              onClick={e => setAccountMenuAnchor(e.currentTarget)}
              size="small"
              sx={{
                ml: 1,
                transition: 'all 0.2s ease',
                '&:hover': { transform: 'scale(1.05)' },
              }}
            >
              <Avatar
                sx={{
                  width: 38,
                  height: 38,
                  background: 'linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%)',
                  fontSize: '0.9rem',
                  fontWeight: 600,
                  boxShadow: '0 2px 8px rgba(37, 99, 235, 0.3)',
                }}
              >
                {getUserInitials()}
              </Avatar>
            </IconButton>

            <Menu
              anchorEl={accountMenuAnchor}
              open={Boolean(accountMenuAnchor)}
              onClose={() => setAccountMenuAnchor(null)}
              PaperProps={{ elevation: 4, sx: { mt: 1.5, minWidth: 220, borderRadius: 2 } }}
              transformOrigin={{ horizontal: 'right', vertical: 'top' }}
              anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
            >
              <Box sx={{ px: 2.5, py: 2 }}>
                <Typography variant="subtitle2" fontWeight={600}>
                  {getUserDisplayName()}
                </Typography>
                <Typography variant="caption" sx={{ color: 'text.secondary' }}>
                  {account?.email}
                </Typography>
              </Box>
              <Divider />
              <MenuItem component={Link} to="/account/settings" onClick={() => setAccountMenuAnchor(null)}>
                <PersonIcon fontSize="small" sx={{ mr: 1.5, color: '#64748b' }} />
                Ajustes de cuenta
              </MenuItem>
              <MenuItem component={Link} to="/account/password" onClick={() => setAccountMenuAnchor(null)}>
                <SettingsIcon fontSize="small" sx={{ mr: 1.5, color: '#64748b' }} />
                Cambiar contraseña
              </MenuItem>
              <Divider />
              <MenuItem component={Link} to="/logout" onClick={() => setAccountMenuAnchor(null)} sx={{ color: 'error.main' }}>
                <LogoutIcon fontSize="small" sx={{ mr: 1.5 }} />
                Cerrar sesión
              </MenuItem>
            </Menu>
          </>
        ) : (
          <Button
            variant="outlined"
            startIcon={<PersonIcon />}
            component={Link}
            to="/login"
            sx={{
              borderColor: '#e2e8f0',
              color: '#1e293b',
              fontWeight: 500,
              px: 2,
              '&:hover': {
                borderColor: '#2563eb',
                backgroundColor: '#eff6ff',
                color: '#2563eb',
              },
            }}
          >
            Iniciar Sesión
          </Button>
        )}
      </Box>
    </Box>
  );
};

export default TopBar;
