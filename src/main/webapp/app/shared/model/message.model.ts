import { Moment } from 'moment';
import { IMessageRecipient } from 'app/shared/model/message-recipient.model';
import { IOrder } from 'app/shared/model/order.model';

export interface IMessage {
  id?: number;
  message?: string;
  lkpMessageTypeId?: number;
  senderUserId?: number;
  senderAlias?: string;
  createdBy?: string;
  dateCreated?: string;
  messageRecipients?: IMessageRecipient[];
  order?: IOrder;
}

export const defaultValue: Readonly<IMessage> = {};
