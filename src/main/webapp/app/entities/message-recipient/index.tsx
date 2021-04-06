import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MessageRecipient from './message-recipient';
import MessageRecipientDetail from './message-recipient-detail';
import MessageRecipientUpdate from './message-recipient-update';
import MessageRecipientDeleteDialog from './message-recipient-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MessageRecipientDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MessageRecipientUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MessageRecipientUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MessageRecipientDetail} />
      <ErrorBoundaryRoute path={match.url} component={MessageRecipient} />
    </Switch>
  </>
);

export default Routes;
