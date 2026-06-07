import './footer.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import Typography from '@mui/material/Typography';

const Footer = () => (
  <div className="footer page-content">
    <Typography variant="body2" color="text.secondary" align="center">
      <Translate contentKey="footer">Your footer</Translate>
    </Typography>
  </div>
);

export default Footer;
