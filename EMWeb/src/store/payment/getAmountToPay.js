import { createSlice } from '@reduxjs/toolkit';
import { apiUrls, getToken } from '../../config/index';
import axios from 'axios';
import { ToastError } from '../../components/Tostify';

const initialState = { state: null, isLoading: false, isError: '' }

const amountToPaySlice = createSlice({
    name: "getAmountToPay",
    initialState,
    reducers: {
        getAmountToPayRequested(state, action) {
            state.isLoading = true;
            state.state = null;
        },
        getAmountToPaySuccess(state, action) {
            state.isLoading = false;
            state.state = action.payload
        },
        getAmountToPayFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        }
    }
});


export const getAmountToPayFunc = (data, endPoint) => {
    return (dispatch) => {
        dispatch(getAmountToPaySlice.getAmountToPayRequested());

        axios({
            method: 'get',
            url: `${apiUrls.baseUrl}${endPoint}`,
            headers: {
                'Authorization': `Bearer ${getToken()}`,
            },
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(getAmountToPaySlice.getAmountToPaySuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                ToastError(errMsg);
                // dispatch(getAmountToPaySlice.getAmountToPayFailure(errMsg));
            }
        }).catch((error) => {
            ToastError(error.message);
            // dispatch(getAmountToPaySlice.getAmountToPayFailure(error.message));
        });
    };
};

export const getAmountToPaySlice = amountToPaySlice.actions;

export default amountToPaySlice.reducer;