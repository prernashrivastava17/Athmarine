import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { apiUrls, getAuthToken, getToken } from '../../config';

const initialState = { getApiRes: null, isLoading: false, isError: '', registrationStatusKey: null };

const addressGetSlice = createSlice({
    name: "AddressGetApi",
    initialState,
    reducers: {
        dispatchGetRequest(state, action) {
            state.isLoading = true;
            state.getApiRes = null;
            state.isLoading = true;
        },
        dispatchGetSuccess(state, action) {
            state.isLoading = false;
            state.getApiRes = action.payload;
            state.isLoading = false;
        },
        dispatchGetFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
            state.isLoading = false;
        },
        emptyError(state, action) {
            state.isError = '';
            state.isLoading = false;
            state.getApiRes = null;
        },
        updateRegistrationKey(state, action) {
            state.registrationStatusKey = action.payload + 1;
        },
        setAPIResponseToBlank(state, action) {
            state.getApiRes = null;
        }
    }
});

const getTokenFunc = () => {
    if (getAuthToken()) {
        return getAuthToken();
    }
    return getToken()
}

export const addressGetAPiCall = (endPoint) => {
    return (dispatch) => {
        dispatch(addressGetApiActions.dispatchGetRequest());
        dispatch(addressGetApiActions.emptyError());
        axios({
            method: 'get',
            url: `${apiUrls.baseUrl}${endPoint}`,
            headers: {
                'Authorization': `Bearer ${getTokenFunc()} `,
            },
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(addressGetApiActions.dispatchGetSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(addressGetApiActions.dispatchGetFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(addressGetApiActions.dispatchGetFailure(error.message));
        });
    };
};

export const addressGetApiActions = addressGetSlice.actions;
export default addressGetSlice.reducer;