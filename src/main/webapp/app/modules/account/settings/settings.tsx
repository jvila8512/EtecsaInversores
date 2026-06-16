import React, { useEffect, useRef, useState } from 'react';
import { Translate, translate } from 'react-jhipster';
import { toast } from 'react-toastify';
import axios from 'axios';

import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Avatar from '@mui/material/Avatar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import MenuItem from '@mui/material/MenuItem';
import Select from '@mui/material/Select';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import Divider from '@mui/material/Divider';
import PhotoCameraIcon from '@mui/icons-material/PhotoCamera';
import DeleteIcon from '@mui/icons-material/Delete';
import SaveIcon from '@mui/icons-material/Save';
import Badge from '@mui/material/Badge';

import { languages, locales } from 'app/config/translation';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import { reset, saveAccountSettings } from './settings.reducer';

export const SettingsPage = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.settings.successMessage);
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [uploading, setUploading] = useState(false);
  const [imageUrl, setImageUrl] = useState<string | null>(null);

  const [formValues, setFormValues] = useState({
    firstName: '',
    lastName: '',
    email: '',
    langKey: 'es',
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  useEffect(() => {
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  useEffect(() => {
    if (account) {
      setFormValues({
        firstName: account.firstName || '',
        lastName: account.lastName || '',
        email: account.email || '',
        langKey: account.langKey || 'es',
      });
      setImageUrl(account.imageUrl || null);
    }
  }, [account]);

  useEffect(() => {
    if (successMessage) {
      toast.success(translate(successMessage));
    }
  }, [successMessage]);

  const validate = () => {
    const newErrors: Record<string, string> = {};
    if (!formValues.firstName || formValues.firstName.trim().length < 1) {
      newErrors.firstName = translate('settings.messages.validate.firstname.required');
    }
    if (!formValues.lastName || formValues.lastName.trim().length < 1) {
      newErrors.lastName = translate('settings.messages.validate.lastname.required');
    }
    if (!formValues.email || formValues.email.trim().length < 5) {
      newErrors.email = translate('global.messages.validate.email.required');
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleValidSubmit = () => {
    if (!validate()) return;
    dispatch(
      saveAccountSettings({
        ...account,
        ...formValues,
      }),
    );
  };

  const handleImageUpload = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    if (file.size > 5 * 1024 * 1024) {
      toast.error('La imagen no puede superar 5MB');
      return;
    }

    if (!['image/jpeg', 'image/png', 'image/gif', 'image/webp'].includes(file.type)) {
      toast.error('Formato no válido. Use JPEG, PNG, GIF o WebP');
      return;
    }

    setUploading(true);
    try {
      const formData = new FormData();
      formData.append('file', file);
      const response = await axios.post<string>('api/account/image', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      setImageUrl(response.data);
      dispatch(getSession());
      toast.success('Imagen actualizada');
    } catch (e) {
      toast.error('Error al subir la imagen');
    } finally {
      setUploading(false);
      if (fileInputRef.current) fileInputRef.current.value = '';
    }
  };

  const handleImageDelete = async () => {
    try {
      await axios.delete('api/account/image');
      setImageUrl(null);
      dispatch(getSession());
      toast.success('Imagen eliminada');
    } catch (e) {
      toast.error('Error al eliminar la imagen');
    }
  };

  const userInitials = `${formValues.firstName?.[0] || ''}${formValues.lastName?.[0] || ''}`.toUpperCase() || 'U';

  return (
    <Box sx={{ maxWidth: 600, mx: 'auto', p: 3 }}>
      <Typography variant="h5" sx={{ mb: 3, fontWeight: 600 }}>
        <Translate contentKey="settings.title" interpolate={{ username: account?.login || '' }}>
          Configuración de usuario
        </Translate>
      </Typography>

      {/* Avatar Section */}
      <Card sx={{ mb: 3 }}>
        <CardContent sx={{ display: 'flex', alignItems: 'center', gap: 3 }}>
          <Badge
            overlap="circular"
            anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
            badgeContent={
              <IconButton
                size="small"
                onClick={() => fileInputRef.current?.click()}
                disabled={uploading}
                sx={{
                  bgcolor: 'primary.main',
                  color: '#fff',
                  '&:hover': { bgcolor: 'primary.dark' },
                  width: 28,
                  height: 28,
                }}
              >
                <PhotoCameraIcon sx={{ fontSize: 16 }} />
              </IconButton>
            }
          >
            <Avatar src={imageUrl || undefined} sx={{ width: 80, height: 80, fontSize: 28, bgcolor: 'primary.main' }}>
              {userInitials}
            </Avatar>
          </Badge>
          <Box sx={{ flex: 1 }}>
            <Typography variant="subtitle1" sx={{ fontWeight: 600 }}>
              {formValues.firstName} {formValues.lastName}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              {account?.login}
            </Typography>
            <Box sx={{ mt: 1, display: 'flex', gap: 1 }}>
              <Button
                variant="outlined"
                size="small"
                onClick={() => fileInputRef.current?.click()}
                disabled={uploading}
                startIcon={<PhotoCameraIcon />}
              >
                {uploading ? 'Subiendo...' : 'Cambiar'}
              </Button>
              {imageUrl && (
                <Button variant="outlined" size="small" color="error" onClick={handleImageDelete} startIcon={<DeleteIcon />}>
                  Eliminar
                </Button>
              )}
            </Box>
          </Box>
          <input
            ref={fileInputRef}
            type="file"
            accept="image/jpeg,image/png,image/gif,image/webp"
            onChange={handleImageUpload}
            style={{ display: 'none' }}
          />
        </CardContent>
      </Card>

      {/* Profile Form */}
      <Card>
        <CardContent>
          <Typography variant="subtitle1" sx={{ fontWeight: 600, mb: 2 }}>
            Datos personales
          </Typography>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <TextField
              label={translate('settings.form.firstname')}
              value={formValues.firstName}
              onChange={e => setFormValues(prev => ({ ...prev, firstName: e.target.value }))}
              error={!!errors.firstName}
              helperText={errors.firstName}
              size="small"
              fullWidth
            />
            <TextField
              label={translate('settings.form.lastname')}
              value={formValues.lastName}
              onChange={e => setFormValues(prev => ({ ...prev, lastName: e.target.value }))}
              error={!!errors.lastName}
              helperText={errors.lastName}
              size="small"
              fullWidth
            />
            <TextField
              label={translate('global.form.email.label')}
              type="email"
              value={formValues.email}
              onChange={e => setFormValues(prev => ({ ...prev, email: e.target.value }))}
              error={!!errors.email}
              helperText={errors.email}
              size="small"
              fullWidth
            />
            <FormControl size="small" fullWidth>
              <InputLabel>{translate('settings.form.language')}</InputLabel>
              <Select
                value={formValues.langKey}
                label={translate('settings.form.language')}
                onChange={e => setFormValues(prev => ({ ...prev, langKey: e.target.value }))}
              >
                {locales.map(locale => (
                  <MenuItem value={locale} key={locale}>
                    {languages[locale].name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <Divider />
            <Button variant="contained" onClick={handleValidSubmit} startIcon={<SaveIcon />} sx={{ alignSelf: 'flex-start' }}>
              <Translate contentKey="settings.form.button">Guardar</Translate>
            </Button>
          </Box>
        </CardContent>
      </Card>
    </Box>
  );
};

export default SettingsPage;
