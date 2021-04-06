import { ILookup } from 'app/shared/model/lookup.model';

export interface ILookupValue {
  id?: number;
  value?: string;
  lookup?: ILookup;
}

export const defaultValue: Readonly<ILookupValue> = {};
