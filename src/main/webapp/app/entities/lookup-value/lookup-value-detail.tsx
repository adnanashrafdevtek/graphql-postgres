import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './lookup-value.reducer';
import { ILookupValue } from 'app/shared/model/lookup-value.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILookupValueDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const LookupValueDetail = (props: ILookupValueDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { lookupValueEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          LookupValue [<b>{lookupValueEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="value">Value</span>
          </dt>
          <dd>{lookupValueEntity.value}</dd>
          <dt>Lookup</dt>
          <dd>{lookupValueEntity.lookup ? lookupValueEntity.lookup.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/lookup-value" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/lookup-value/${lookupValueEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ lookupValue }: IRootState) => ({
  lookupValueEntity: lookupValue.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(LookupValueDetail);
