import { createSlice } from '@reduxjs/toolkit';
import { apiUrls, getToken } from '../../config';
import axios from 'axios';
import { ToastError } from '../../components/Tostify';

const initialPaymentRequestState = { state: null, isLoading: false, isError: '' };

const paymentRequestSlice = createSlice({
    name: 'validateCard',
    initialState: initialPaymentRequestState,
    reducers: {
        savePaymentRequested(state, action) {
            state.isLoading = true;
        },
        savePaymentSuccess(state, action) {
            state.isLoading = false;
            state.state = action.payload;
        },
        savePaymentFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = '';
        }
    }
});

export const paymentRequest = (data, endPoint) => {
    return (dispatch) => {
        dispatch(paymentRequestActions.emptyError());
        dispatch(paymentRequestActions.savePaymentRequested());

        axios({
            method: 'POST',
            url: `${apiUrls.baseUrl}/${endPoint}`,
            data,
            headers: {
                'Authorization': `Bearer ${getToken()}`,
            },
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(paymentRequestActions.savePaymentSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                ToastError(errMsg);
                // dispatch(paymentRequestActions.savePaymentFailure(errMsg));
            }
        }).catch((error) => {
            ToastError(error.message);
            // dispatch(paymentRequestActions.savePaymentFailure(error.message));
        });
    };
};

export const paymentRequestActions = paymentRequestSlice.actions;

export default paymentRequestSlice.reducer;