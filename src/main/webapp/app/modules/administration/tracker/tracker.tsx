import React from 'react';
import { Box, Chip, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Toolbar, Typography } from '@mui/material';
import FiberManualRecordIcon from '@mui/icons-material/FiberManualRecord';
import PersonIcon from '@mui/icons-material/Person';
import LanguageIcon from '@mui/icons-material/Language';
import WebIcon from '@mui/icons-material/Web';
import ScheduleIcon from '@mui/icons-material/Schedule';
import { useAppSelector } from 'app/config/store';

const PAGE_STATUS_MAP: Record<
  string,
  { label: string; color: 'success' | 'warning' | 'error' | 'default' | 'info' | 'primary' | 'secondary' }
> = {
  login: { label: 'Login', color: 'info' },
  logout: { label: 'Logout', color: 'warning' },
  'navigate/': { label: 'Home', color: 'success' },
};

const getPageChipColor = (
  page: string,
): { label: string; color: 'success' | 'warning' | 'error' | 'default' | 'info' | 'primary' | 'secondary' } => {
  if (PAGE_STATUS_MAP[page]) return PAGE_STATUS_MAP[page];
  if (page.startsWith('navigate/')) return { label: page.replace('navigate/', ''), color: 'primary' };
  return { label: page, color: 'default' };
};

export const TrackerPage = () => {
  const activities = useAppSelector(state => state.administration.tracker.activities);

  return (
    <Paper sx={{ p: 3 }}>
      <Toolbar sx={{ justifyContent: 'space-between', mb: 2, px: 0 }}>
        <Typography variant="h5" sx={{ fontWeight: 700 }}>
          Actividad en Tiempo Real
        </Typography>
        <Chip
          icon={
            <FiberManualRecordIcon
              sx={{
                fontSize: 12,
                animation: 'pulse 2s infinite',
                '@keyframes pulse': { '0%': { opacity: 1 }, '50%': { opacity: 0.4 }, '100%': { opacity: 1 } },
              }}
            />
          }
          label="Live"
          color="success"
          variant="outlined"
          size="small"
        />
      </Toolbar>

      {activities.length === 0 ? (
        <Box sx={{ textAlign: 'center', py: 6 }}>
          <FiberManualRecordIcon sx={{ fontSize: 48, color: 'text.disabled', mb: 1 }} />
          <Typography variant="body1" color="text.secondary">
            No hay actividad registrada. Las sesiones de usuario aparecerán aquí en tiempo real.
          </Typography>
        </Box>
      ) : (
        <TableContainer sx={{ overflowX: 'auto' }}>
          <Table>
            <TableHead>
              <TableRow sx={{ bgcolor: '#f8fafc' }}>
                <TableCell sx={{ fontWeight: 600 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                    <PersonIcon fontSize="small" sx={{ color: 'text.secondary' }} />
                    Usuario
                  </Box>
                </TableCell>
                <TableCell sx={{ fontWeight: 600 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                    <LanguageIcon fontSize="small" sx={{ color: 'text.secondary' }} />
                    Dirección IP
                  </Box>
                </TableCell>
                <TableCell sx={{ fontWeight: 600 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                    <WebIcon fontSize="small" sx={{ color: 'text.secondary' }} />
                    Página Actual
                  </Box>
                </TableCell>
                <TableCell sx={{ fontWeight: 600 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                    <ScheduleIcon fontSize="small" sx={{ color: 'text.secondary' }} />
                    Hora
                  </Box>
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {activities.map((activity, i) => {
                const pageInfo = getPageChipColor(activity.page);
                return (
                  <TableRow key={`tracker-row-${i}`} hover>
                    <TableCell>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <PersonIcon fontSize="small" sx={{ color: 'primary.main' }} />
                        <Typography variant="body2" sx={{ fontWeight: 500 }}>
                          {activity.userLogin}
                        </Typography>
                      </Box>
                    </TableCell>
                    <TableCell>
                      <Typography variant="body2" sx={{ fontFamily: 'monospace', fontSize: '0.85rem' }}>
                        {activity.ipAddress}
                      </Typography>
                    </TableCell>
                    <TableCell>
                      <Chip label={pageInfo.label} color={pageInfo.color} size="small" variant="outlined" />
                    </TableCell>
                    <TableCell>
                      <Typography variant="body2" color="text.secondary">
                        {activity.time}
                      </Typography>
                    </TableCell>
                  </TableRow>
                );
              })}
            </TableBody>
          </Table>
        </TableContainer>
      )}
    </Paper>
  );
};

export default TrackerPage;
