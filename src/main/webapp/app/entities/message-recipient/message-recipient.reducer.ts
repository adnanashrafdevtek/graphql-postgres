import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMessageRecipient, defaultValue } from 'app/shared/model/message-recipient.model';

export const ACTION_TYPES = {
  FETCH_MESSAGERECIPIENT_LIST: 'messageRecipient/FETCH_MESSAGERECIPIENT_LIST',
  FETCH_MESSAGERECIPIENT: 'messageRecipient/FETCH_MESSAGERECIPIENT',
  CREATE_MESSAGERECIPIENT: 'messageRecipient/CREATE_MESSAGERECIPIENT',
  UPDATE_MESSAGERECIPIENT: 'messageRecipient/UPDATE_MESSAGERECIPIENT',
  DELETE_MESSAGERECIPIENT: 'messageRecipient/DELETE_MESSAGERECIPIENT',
  RESET: 'messageRecipient/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMessageRecipient>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type MessageRecipientState = Readonly<typeof initialState>;

// Reducer

export default (state: MessageRecipientState = initialState, action): MessageRecipientState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MESSAGERECIPIENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MESSAGERECIPIENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_MESSAGERECIPIENT):
    case REQUEST(ACTION_TYPES.UPDATE_MESSAGERECIPIENT):
    case REQUEST(ACTION_TYPES.DELETE_MESSAGERECIPIENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_MESSAGERECIPIENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MESSAGERECIPIENT):
    case FAILURE(ACTION_TYPES.CREATE_MESSAGERECIPIENT):
    case FAILURE(ACTION_TYPES.UPDATE_MESSAGERECIPIENT):
    case FAILURE(ACTION_TYPES.DELETE_MESSAGERECIPIENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_MESSAGERECIPIENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_MESSAGERECIPIENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_MESSAGERECIPIENT):
    case SUCCESS(ACTION_TYPES.UPDATE_MESSAGERECIPIENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_MESSAGERECIPIENT):
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

const apiUrl = 'api/message-recipients';

// Actions

export const getEntities: ICrudGetAllAction<IMessageRecipient> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_MESSAGERECIPIENT_LIST,
    payload: axios.get<IMessageRecipient>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IMessageRecipient> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MESSAGERECIPIENT,
    payload: axios.get<IMessageRecipient>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IMessageRecipient> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MESSAGERECIPIENT,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMessageRecipient> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MESSAGERECIPIENT,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMessageRecipient> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MESSAGERECIPIENT,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
