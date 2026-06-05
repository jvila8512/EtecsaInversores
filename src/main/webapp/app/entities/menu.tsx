import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/marca">
        <Translate contentKey="global.menu.entities.marca" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/mapeo-registro">
        <Translate contentKey="global.menu.entities.mapeoRegistro" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/modelo-inversor">
        <Translate contentKey="global.menu.entities.modeloInversor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sitio">
        <Translate contentKey="global.menu.entities.sitio" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/gateway-tcp">
        <Translate contentKey="global.menu.entities.gatewayTcp" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/inversor">
        <Translate contentKey="global.menu.entities.inversor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/dato-inversor">
        <Translate contentKey="global.menu.entities.datoInversor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/dato-eficiencia">
        <Translate contentKey="global.menu.entities.datoEficiencia" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/alerta-config">
        <Translate contentKey="global.menu.entities.alertaConfig" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/evento-alerta">
        <Translate contentKey="global.menu.entities.eventoAlerta" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
