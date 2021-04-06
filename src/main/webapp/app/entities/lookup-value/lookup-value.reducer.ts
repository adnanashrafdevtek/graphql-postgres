import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ILookupValue, defaultValue } from 'app/shared/model/lookup-value.model';

export const ACTION_TYPES = {
  FETCH_LOOKUPVALUE_LIST: 'lookupValue/FETCH_LOOKUPVALUE_LIST',
  FETCH_LOOKUPVALUE: 'lookupValue/FETCH_LOOKUPVALUE',
  CREATE_LOOKUPVALUE: 'lookupValue/CREATE_LOOKUPVALUE',
  UPDATE_LOOKUPVALUE: 'lookupValue/UPDATE_LOOKUPVALUE',
  DELETE_LOOKUPVALUE: 'lookupValue/DELETE_LOOKUPVALUE',
  RESET: 'lookupValue/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILookupValue>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type LookupValueState = Readonly<typeof initialState>;

// Reducer

export default (state: LookupValueState = initialState, action): LookupValueState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_LOOKUPVALUE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LOOKUPVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_LOOKUPVALUE):
    case REQUEST(ACTION_TYPES.UPDATE_LOOKUPVALUE):
    case REQUEST(ACTION_TYPES.DELETE_LOOKUPVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_LOOKUPVALUE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LOOKUPVALUE):
    case FAILURE(ACTION_TYPES.CREATE_LOOKUPVALUE):
    case FAILURE(ACTION_TYPES.UPDATE_LOOKUPVALUE):
    case FAILURE(ACTION_TYPES.DELETE_LOOKUPVALUE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_LOOKUPVALUE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_LOOKUPVALUE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_LOOKUPVALUE):
    case SUCCESS(ACTION_TYPES.UPDATE_LOOKUPVALUE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_LOOKUPVALUE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/lookup-values';

// Actions

export const getEntities: ICrudGetAllAction<ILookupValue> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_LOOKUPVALUE_LIST,
    payload: axios.get<ILookupValue>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ILookupValue> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LOOKUPVALUE,
    payload: axios.get<ILookupValue>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ILookupValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LOOKUPVALUE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ILookupValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LOOKUPVALUE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILookupValue> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LOOKUPVALUE,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
