import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ILookup } from 'app/shared/model/lookup.model';
import { getEntities as getLookups } from 'app/entities/lookup/lookup.reducer';
import { getEntity, updateEntity, createEntity, reset } from './lookup-value.reducer';
import { ILookupValue } from 'app/shared/model/lookup-value.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ILookupValueUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const LookupValueUpdate = (props: ILookupValueUpdateProps) => {
  const [lookupId, setLookupId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { lookupValueEntity, lookups, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/lookup-value' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getLookups();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...lookupValueEntity,
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
          <h2 id="jhipsterApp.lookupValue.home.createOrEditLabel">Create or edit a LookupValue</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : lookupValueEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="lookup-value-id">ID</Label>
                  <AvInput id="lookup-value-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="valueLabel" for="lookup-value-value">
                  Value
                </Label>
                <AvField
                  id="lookup-value-value"
                  type="text"
                  name="value"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    maxLength: { value: 50, errorMessage: 'This field cannot be longer than 50 characters.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="lookup-value-lookup">Lookup</Label>
                <AvInput id="lookup-value-lookup" type="select" className="form-control" name="lookup.id">
                  <option value="" key="0" />
                  {lookups
                    ? lookups.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/lookup-value" replace color="info">
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
  lookups: storeState.lookup.entities,
  lookupValueEntity: storeState.lookupValue.entity,
  loading: storeState.lookupValue.loading,
  updating: storeState.lookupValue.updating,
  updateSuccess: storeState.lookupValue.updateSuccess,
});

const mapDispatchToProps = {
  getLookups,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(LookupValueUpdate);
