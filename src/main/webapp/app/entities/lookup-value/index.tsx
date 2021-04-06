import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LookupValue from './lookup-value';
import LookupValueDetail from './lookup-value-detail';
import LookupValueUpdate from './lookup-value-update';
import LookupValueDeleteDialog from './lookup-value-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LookupValueDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LookupValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LookupValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LookupValueDetail} />
      <ErrorBoundaryRoute path={match.url} component={LookupValue} />
    </Switch>
  </>
);

export default Routes;
