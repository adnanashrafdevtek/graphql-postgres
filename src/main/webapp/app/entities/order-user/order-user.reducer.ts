import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOrderUser, defaultValue } from 'app/shared/model/order-user.model';

export const ACTION_TYPES = {
  FETCH_ORDERUSER_LIST: 'orderUser/FETCH_ORDERUSER_LIST',
  FETCH_ORDERUSER: 'orderUser/FETCH_ORDERUSER',
  CREATE_ORDERUSER: 'orderUser/CREATE_ORDERUSER',
  UPDATE_ORDERUSER: 'orderUser/UPDATE_ORDERUSER',
  DELETE_ORDERUSER: 'orderUser/DELETE_ORDERUSER',
  RESET: 'orderUser/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOrderUser>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type OrderUserState = Readonly<typeof initialState>;

// Reducer

export default (state: OrderUserState = initialState, action): OrderUserState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ORDERUSER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ORDERUSER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ORDERUSER):
    case REQUEST(ACTION_TYPES.UPDATE_ORDERUSER):
    case REQUEST(ACTION_TYPES.DELETE_ORDERUSER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_ORDERUSER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ORDERUSER):
    case FAILURE(ACTION_TYPES.CREATE_ORDERUSER):
    case FAILURE(ACTION_TYPES.UPDATE_ORDERUSER):
    case FAILURE(ACTION_TYPES.DELETE_ORDERUSER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORDERUSER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORDERUSER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ORDERUSER):
    case SUCCESS(ACTION_TYPES.UPDATE_ORDERUSER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ORDERUSER):
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

const apiUrl = 'api/order-users';

// Actions

export const getEntities: ICrudGetAllAction<IOrderUser> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ORDERUSER_LIST,
    payload: axios.get<IOrderUser>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IOrderUser> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ORDERUSER,
    payload: axios.get<IOrderUser>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IOrderUser> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ORDERUSER,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOrderUser> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ORDERUSER,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOrderUser> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ORDERUSER,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
