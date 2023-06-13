import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { apiUrls, getToken } from '../../../config';
import { ToastError } from '../../../components/Tostify';

const initialState = { state: null, isLoading: false, isError: '' };

const requestorMainSlice = createSlice({
    name: "getRequestorMain",
    initialState,
    reducers: {
        getRequestorMainRequested(state, action) {
            state.isLoading = true;
            state.state = null;
        },
        getRequestorMainSuccess(state, action) {
            state.isLoading = false;
            state.state = action.payload
        },
        getRequestorMainFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        }
    }
});


export const getRequestorMainFunc = (data, endPoint) => {
    return (dispatch) => {
        dispatch(getRequestorMainSlice.getRequestorMainRequested());

        axios({
            method: 'get',
            url: `${apiUrls.baseUrl}${endPoint}`,
            headers: {
                'Authorization': `Bearer ${getToken()}`,
            },
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(getRequestorMainSlice.getRequestorMainSuccess(response.data.responseData));
            } else {
                ToastError(response.data.responseData.message);

            }
        }).catch((error) => {
            ToastError(error.message);
        });
    };
};

export const getRequestorMainSlice = requestorMainSlice.actions;

export default requestorMainSlice.reducer;