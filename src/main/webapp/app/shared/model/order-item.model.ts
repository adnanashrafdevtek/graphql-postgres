import { Moment } from 'moment';
import { IOrder } from 'app/shared/model/order.model';

export interface IOrderItem {
  id?: number;
  name?: string;
  dateCreated?: string;
  dateUpdated?: string;
  catalogItemId?: number;
  order?: IOrder;
}

export const defaultValue: Readonly<IOrderItem> = {};
