import { ILookupValue } from 'app/shared/model/lookup-value.model';

export interface ILookup {
  id?: number;
  name?: string;
  lookupValues?: ILookupValue[];
}

export const defaultValue: Readonly<ILookup> = {};
