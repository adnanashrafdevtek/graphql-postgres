import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './message-recipient.reducer';
import { IMessageRecipient } from 'app/shared/model/message-recipient.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMessageRecipientDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MessageRecipientDetail = (props: IMessageRecipientDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { messageRecipientEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          MessageRecipient [<b>{messageRecipientEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="userId">User Id</span>
          </dt>
          <dd>{messageRecipientEntity.userId}</dd>
          <dt>
            <span id="read">Read</span>
          </dt>
          <dd>{messageRecipientEntity.read ? 'true' : 'false'}</dd>
          <dt>
            <span id="dateMessageRead">Date Message Read</span>
          </dt>
          <dd>
            {messageRecipientEntity.dateMessageRead ? (
              <TextFormat value={messageRecipientEntity.dateMessageRead} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Message</dt>
          <dd>{messageRecipientEntity.message ? messageRecipientEntity.message.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/message-recipient" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/message-recipient/${messageRecipientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ messageRecipient }: IRootState) => ({
  messageRecipientEntity: messageRecipient.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MessageRecipientDetail);
