import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './order-item.reducer';
import { IOrderItem } from 'app/shared/model/order-item.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrderItemDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OrderItemDetail = (props: IOrderItemDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { orderItemEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          OrderItem [<b>{orderItemEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{orderItemEntity.name}</dd>
          <dt>
            <span id="dateCreated">Date Created</span>
          </dt>
          <dd>
            {orderItemEntity.dateCreated ? <TextFormat value={orderItemEntity.dateCreated} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="dateUpdated">Date Updated</span>
          </dt>
          <dd>
            {orderItemEntity.dateUpdated ? <TextFormat value={orderItemEntity.dateUpdated} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="catalogItemId">Catalog Item Id</span>
          </dt>
          <dd>{orderItemEntity.catalogItemId}</dd>
          <dt>Order</dt>
          <dd>{orderItemEntity.order ? orderItemEntity.order.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order-item" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-item/${orderItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ orderItem }: IRootState) => ({
  orderItemEntity: orderItem.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OrderItemDetail);
