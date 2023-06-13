import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { apiUrls, getAuthToken, getToken } from '../../config';

const initialState = { getApiRes: null, isLoading: false, isError: '', registrationStatusKey: null };

const commonGetSlice = createSlice({
    name: "CommonGetApi",
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

export const commonGetAPiCall = (endPoint) => {
    return (dispatch) => {
        dispatch(commonGetApiActions.dispatchGetRequest());
        dispatch(commonGetApiActions.emptyError());
        axios({
            method: 'get',
            url: `${apiUrls.baseUrl}${endPoint}`,
            headers: {
                'Authorization': `Bearer ${getTokenFunc()} `,
            },
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(commonGetApiActions.dispatchGetSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(commonGetApiActions.dispatchGetFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(commonGetApiActions.dispatchGetFailure(error.message));
        });
    };
};

export const commonGetApiActions = commonGetSlice.actions;
export default commonGetSlice.reducer;