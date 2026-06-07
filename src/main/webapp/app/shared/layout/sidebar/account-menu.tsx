import React from 'react';
import { Link } from 'react-router-dom';
import { Menu, MenuItem, ListItemIcon } from '@mui/material';
import SettingsIcon from '@mui/icons-material/Settings';
import LockIcon from '@mui/icons-material/Lock';
import LogoutIcon from '@mui/icons-material/Logout';
import LockOpenIcon from '@mui/icons-material/LockOpen';
import VpnKeyIcon from '@mui/icons-material/VpnKey';
import { Translate } from 'react-jhipster';

interface AccountMenuSectionProps {
  anchorEl: HTMLElement | null;
  onClose: () => void;
  isAuthenticated: boolean;
}

export const AccountMenuSection: React.FC<AccountMenuSectionProps> = ({ anchorEl, onClose, isAuthenticated }) => (
  <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={onClose}>
    {isAuthenticated ? (
      <>
        <MenuItem component={Link} to="/account/settings" onClick={onClose}>
          <ListItemIcon>
            <SettingsIcon fontSize="small" />
          </ListItemIcon>
          <Translate contentKey="global.menu.account.settings">Settings</Translate>
        </MenuItem>
        <MenuItem component={Link} to="/account/password" onClick={onClose}>
          <ListItemIcon>
            <LockIcon fontSize="small" />
          </ListItemIcon>
          <Translate contentKey="global.menu.account.password">Password</Translate>
        </MenuItem>
        <MenuItem component={Link} to="/logout" onClick={onClose}>
          <ListItemIcon>
            <LogoutIcon fontSize="small" />
          </ListItemIcon>
          <Translate contentKey="global.menu.account.logout">Sign out</Translate>
        </MenuItem>
      </>
    ) : (
      <>
        <MenuItem component={Link} to="/login" onClick={onClose}>
          <ListItemIcon>
            <LockOpenIcon fontSize="small" />
          </ListItemIcon>
          <Translate contentKey="global.menu.account.login">Sign in</Translate>
        </MenuItem>
        <MenuItem component={Link} to="/account/register" onClick={onClose}>
          <ListItemIcon>
            <VpnKeyIcon fontSize="small" />
          </ListItemIcon>
          <Translate contentKey="global.menu.account.register">Register</Translate>
        </MenuItem>
      </>
    )}
  </Menu>
);
