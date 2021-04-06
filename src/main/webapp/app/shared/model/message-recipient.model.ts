import { Moment } from 'moment';
import { IMessage } from 'app/shared/model/message.model';

export interface IMessageRecipient {
  id?: number;
  userId?: number;
  read?: boolean;
  dateMessageRead?: string;
  message?: IMessage;
}

export const defaultValue: Readonly<IMessageRecipient> = {
  read: false,
};
