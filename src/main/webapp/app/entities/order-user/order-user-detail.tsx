import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './order-user.reducer';
import { IOrderUser } from 'app/shared/model/order-user.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrderUserDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OrderUserDetail = (props: IOrderUserDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { orderUserEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          OrderUser [<b>{orderUserEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="userId">User Id</span>
          </dt>
          <dd>{orderUserEntity.userId}</dd>
          <dt>
            <span id="dateCreated">Date Created</span>
          </dt>
          <dd>
            {orderUserEntity.dateCreated ? <TextFormat value={orderUserEntity.dateCreated} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>Order</dt>
          <dd>{orderUserEntity.order ? orderUserEntity.order.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order-user" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-user/${orderUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ orderUser }: IRootState) => ({
  orderUserEntity: orderUser.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OrderUserDetail);
