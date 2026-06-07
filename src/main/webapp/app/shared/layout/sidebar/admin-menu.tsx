import React from 'react';
import { Link } from 'react-router-dom';
import { Menu, MenuItem } from '@mui/material';
import { Translate } from 'react-jhipster';

interface AdminMenuSectionProps {
  anchorEl: HTMLElement | null;
  onClose: () => void;
  isOpenAPIEnabled: boolean;
}

export const AdminMenuSection: React.FC<AdminMenuSectionProps> = ({ anchorEl, onClose, isOpenAPIEnabled }) => (
  <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={onClose}>
    <MenuItem component={Link} to="/admin/user-management" onClick={onClose}>
      <Translate contentKey="global.menu.admin.userManagement">User management</Translate>
    </MenuItem>
    <MenuItem component={Link} to="/admin/tracker" onClick={onClose}>
      <Translate contentKey="global.menu.admin.tracker">User tracker</Translate>
    </MenuItem>
    <MenuItem component={Link} to="/admin/metrics" onClick={onClose}>
      <Translate contentKey="global.menu.admin.metrics">Metrics</Translate>
    </MenuItem>
    <MenuItem component={Link} to="/admin/health" onClick={onClose}>
      <Translate contentKey="global.menu.admin.health">Health</Translate>
    </MenuItem>
    <MenuItem component={Link} to="/admin/configuration" onClick={onClose}>
      <Translate contentKey="global.menu.admin.configuration">Configuration</Translate>
    </MenuItem>
    <MenuItem component={Link} to="/admin/logs" onClick={onClose}>
      <Translate contentKey="global.menu.admin.logs">Logs</Translate>
    </MenuItem>
    {isOpenAPIEnabled && (
      <MenuItem component={Link} to="/admin/docs" onClick={onClose}>
        <Translate contentKey="global.menu.admin.apidocs">API</Translate>
      </MenuItem>
    )}
  </Menu>
);
