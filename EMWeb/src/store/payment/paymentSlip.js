import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { apiUrls, getUserId, getToken, getCompanyId } from '../../config';

const initialState = { paymentSlip: null, isLoading: false, isError: '' };

const paymentSlipSlice = createSlice({
    name: "paymentSlip",
    initialState,
    reducers: {
        dispatchPaymentSlipRequest(state, action) {
            state.isLoading = true;
        },
        dispatchPaymentSlipSuccess(state, action) {
            state.isLoading = false;
            state.paymentSlip = action.payload;
        },
        dispatchPaymentSlipFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = ""
        }
    }
});



export const getPaymentSlip = (data) => {
    return (dispatch) => {
        dispatch(getPaymentSlipSlice.dispatchPaymentSlipRequest());
        dispatch(getPaymentSlipSlice.emptyError());

        axios({
            method: 'get',
            url: `${apiUrls.baseUrl}/payment/getPaymentDetail/${ getUserId() === null ? getCompanyId() : getUserId()}`,
            headers: {
                Authorization: `Bearer ${getToken()}`,
            },
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(getPaymentSlipSlice.dispatchPaymentSlipSuccess(response.data.responseData));

            } else {
                let errMsg = response.data.responseData.message;
                dispatch(getPaymentSlipSlice.dispatchPaymentSlipFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(getPaymentSlipSlice.dispatchPaymentSlipFailure(error.message));
        });
    };
};

export const getPaymentSlipSlice = paymentSlipSlice.actions;
export default paymentSlipSlice.reducer;