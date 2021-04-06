import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Lookup from './lookup';
import LookupDetail from './lookup-detail';
import LookupUpdate from './lookup-update';
import LookupDeleteDialog from './lookup-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LookupDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LookupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LookupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LookupDetail} />
      <ErrorBoundaryRoute path={match.url} component={Lookup} />
    </Switch>
  </>
);

export default Routes;
