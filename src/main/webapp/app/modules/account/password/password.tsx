import React, { useEffect, useState } from 'react';
import { Translate, translate } from 'react-jhipster';
import { toast } from 'react-toastify';

import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';
import LockIcon from '@mui/icons-material/Lock';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { reset, savePassword } from './password.reducer';

export const PasswordPage = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.password.successMessage);
  const errorMessage = useAppSelector(state => state.password.errorMessage);

  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [errors, setErrors] = useState<Record<string, string>>({});

  useEffect(() => {
    dispatch(reset());
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  useEffect(() => {
    if (successMessage) {
      toast.success(translate(successMessage));
      setCurrentPassword('');
      setNewPassword('');
      setConfirmPassword('');
    } else if (errorMessage) {
      toast.error(translate(errorMessage));
    }
    dispatch(reset());
  }, [successMessage, errorMessage]);

  const validate = () => {
    const newErrors: Record<string, string> = {};
    if (!currentPassword) newErrors.currentPassword = translate('global.messages.validate.newpassword.required');
    if (!newPassword || newPassword.length < 4) newErrors.newPassword = translate('global.messages.validate.newpassword.minlength');
    if (newPassword.length > 50) newErrors.newPassword = translate('global.messages.validate.newpassword.maxlength');
    if (!confirmPassword) newErrors.confirmPassword = translate('global.messages.validate.confirmpassword.required');
    if (newPassword && confirmPassword && newPassword !== confirmPassword)
      newErrors.confirmPassword = translate('global.messages.error.dontmatch');
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = () => {
    if (!validate()) return;
    dispatch(savePassword({ currentPassword, newPassword }));
  };

  return (
    <Box sx={{ maxWidth: 600, mx: 'auto', p: 3 }}>
      <Typography variant="h5" sx={{ mb: 3, fontWeight: 600 }}>
        <Translate contentKey="password.title" interpolate={{ username: account?.login || '' }}>
          Contraseña
        </Translate>
      </Typography>

      <Card>
        <CardContent>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
            <LockIcon color="action" />
            <Typography variant="subtitle1" sx={{ fontWeight: 600 }}>
              Cambiar contraseña
            </Typography>
          </Box>

          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <TextField
              label={translate('global.form.currentpassword.label')}
              type="password"
              value={currentPassword}
              onChange={e => setCurrentPassword(e.target.value)}
              error={!!errors.currentPassword}
              helperText={errors.currentPassword}
              size="small"
              fullWidth
            />
            <TextField
              label={translate('global.form.newpassword.label')}
              type="password"
              value={newPassword}
              onChange={e => setNewPassword(e.target.value)}
              error={!!errors.newPassword}
              helperText={errors.newPassword}
              size="small"
              fullWidth
            />
            {newPassword && <PasswordStrengthBar password={newPassword} />}
            <TextField
              label={translate('global.form.confirmpassword.label')}
              type="password"
              value={confirmPassword}
              onChange={e => setConfirmPassword(e.target.value)}
              error={!!errors.confirmPassword}
              helperText={errors.confirmPassword}
              size="small"
              fullWidth
            />
            <Divider />
            <Button variant="contained" color="success" onClick={handleSubmit} startIcon={<LockIcon />} sx={{ alignSelf: 'flex-start' }}>
              <Translate contentKey="password.form.button">Guardar</Translate>
            </Button>
          </Box>
        </CardContent>
      </Card>
    </Box>
  );
};

export default PasswordPage;
