import { Moment } from 'moment';
import { IOrder } from 'app/shared/model/order.model';

export interface IOrderUser {
  id?: number;
  userId?: number;
  dateCreated?: string;
  order?: IOrder;
}

export const defaultValue: Readonly<IOrderUser> = {};
