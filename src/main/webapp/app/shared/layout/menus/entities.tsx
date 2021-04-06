import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <MenuItem icon="asterisk" to="/order">
      Order
    </MenuItem>
    <MenuItem icon="asterisk" to="/order-item">
      Order Item
    </MenuItem>
    <MenuItem icon="asterisk" to="/message">
      Message
    </MenuItem>
    <MenuItem icon="asterisk" to="/message-recipient">
      Message Recipient
    </MenuItem>
    <MenuItem icon="asterisk" to="/order-user">
      Order User
    </MenuItem>
    <MenuItem icon="asterisk" to="/lookup">
      Lookup
    </MenuItem>
    <MenuItem icon="asterisk" to="/lookup-value">
      Lookup Value
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
