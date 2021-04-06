import { Moment } from 'moment';
import { IOrderUser } from 'app/shared/model/order-user.model';
import { IMessage } from 'app/shared/model/message.model';
import { IOrderItem } from 'app/shared/model/order-item.model';

export interface IOrder {
  id?: number;
  name?: string;
  uuid?: string;
  dateCreated?: string;
  createdBy?: string;
  dateUpdated?: string;
  updatedBy?: string;
  buyerUserId?: number;
  buyerOrganizationId?: number;
  supplierOrganizationId?: number;
  primarySupplierUserId?: number;
  orderUsers?: IOrderUser[];
  messages?: IMessage[];
  orderItems?: IOrderItem[];
}

export const defaultValue: Readonly<IOrder> = {};
