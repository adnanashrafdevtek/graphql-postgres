import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Order from './order';
import OrderItem from './order-item';
import Message from './message';
import MessageRecipient from './message-recipient';
import OrderUser from './order-user';
import Lookup from './lookup';
import LookupValue from './lookup-value';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}order`} component={Order} />
      <ErrorBoundaryRoute path={`${match.url}order-item`} component={OrderItem} />
      <ErrorBoundaryRoute path={`${match.url}message`} component={Message} />
      <ErrorBoundaryRoute path={`${match.url}message-recipient`} component={MessageRecipient} />
      <ErrorBoundaryRoute path={`${match.url}order-user`} component={OrderUser} />
      <ErrorBoundaryRoute path={`${match.url}lookup`} component={Lookup} />
      <ErrorBoundaryRoute path={`${match.url}lookup-value`} component={LookupValue} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
