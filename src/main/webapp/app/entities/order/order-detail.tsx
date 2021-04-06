import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './order.reducer';
import { IOrder } from 'app/shared/model/order.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrderDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OrderDetail = (props: IOrderDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { orderEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Order [<b>{orderEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{orderEntity.name}</dd>
          <dt>
            <span id="uuid">Uuid</span>
          </dt>
          <dd>{orderEntity.uuid}</dd>
          <dt>
            <span id="dateCreated">Date Created</span>
          </dt>
          <dd>{orderEntity.dateCreated ? <TextFormat value={orderEntity.dateCreated} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{orderEntity.createdBy}</dd>
          <dt>
            <span id="dateUpdated">Date Updated</span>
          </dt>
          <dd>{orderEntity.dateUpdated ? <TextFormat value={orderEntity.dateUpdated} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedBy">Updated By</span>
          </dt>
          <dd>{orderEntity.updatedBy}</dd>
          <dt>
            <span id="buyerUserId">Buyer User Id</span>
          </dt>
          <dd>{orderEntity.buyerUserId}</dd>
          <dt>
            <span id="buyerOrganizationId">Buyer Organization Id</span>
          </dt>
          <dd>{orderEntity.buyerOrganizationId}</dd>
          <dt>
            <span id="supplierOrganizationId">Supplier Organization Id</span>
          </dt>
          <dd>{orderEntity.supplierOrganizationId}</dd>
          <dt>
            <span id="primarySupplierUserId">Primary Supplier User Id</span>
          </dt>
          <dd>{orderEntity.primarySupplierUserId}</dd>
        </dl>
        <Button tag={Link} to="/order" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order/${orderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ order }: IRootState) => ({
  orderEntity: order.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OrderDetail);
