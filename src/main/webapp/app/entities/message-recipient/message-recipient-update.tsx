import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IMessage } from 'app/shared/model/message.model';
import { getEntities as getMessages } from 'app/entities/message/message.reducer';
import { getEntity, updateEntity, createEntity, reset } from './message-recipient.reducer';
import { IMessageRecipient } from 'app/shared/model/message-recipient.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMessageRecipientUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MessageRecipientUpdate = (props: IMessageRecipientUpdateProps) => {
  const [messageId, setMessageId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { messageRecipientEntity, messages, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/message-recipient' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getMessages();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.dateMessageRead = convertDateTimeToServer(values.dateMessageRead);

    if (errors.length === 0) {
      const entity = {
        ...messageRecipientEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterApp.messageRecipient.home.createOrEditLabel">Create or edit a MessageRecipient</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : messageRecipientEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="message-recipient-id">ID</Label>
                  <AvInput id="message-recipient-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="userIdLabel" for="message-recipient-userId">
                  User Id
                </Label>
                <AvField
                  id="message-recipient-userId"
                  type="string"
                  className="form-control"
                  name="userId"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="readLabel">
                  <AvInput id="message-recipient-read" type="checkbox" className="form-check-input" name="read" />
                  Read
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="dateMessageReadLabel" for="message-recipient-dateMessageRead">
                  Date Message Read
                </Label>
                <AvInput
                  id="message-recipient-dateMessageRead"
                  type="datetime-local"
                  className="form-control"
                  name="dateMessageRead"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.messageRecipientEntity.dateMessageRead)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="message-recipient-message">Message</Label>
                <AvInput id="message-recipient-message" type="select" className="form-control" name="message.id">
                  <option value="" key="0" />
                  {messages
                    ? messages.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/message-recipient" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  messages: storeState.message.entities,
  messageRecipientEntity: storeState.messageRecipient.entity,
  loading: storeState.messageRecipient.loading,
  updating: storeState.messageRecipient.updating,
  updateSuccess: storeState.messageRecipient.updateSuccess,
});

const mapDispatchToProps = {
  getMessages,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MessageRecipientUpdate);
