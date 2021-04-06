import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OrderUser from './order-user';
import OrderUserDetail from './order-user-detail';
import OrderUserUpdate from './order-user-update';
import OrderUserDeleteDialog from './order-user-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={OrderUserDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OrderUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OrderUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OrderUserDetail} />
      <ErrorBoundaryRoute path={match.url} component={OrderUser} />
    </Switch>
  </>
);

export default Routes;
