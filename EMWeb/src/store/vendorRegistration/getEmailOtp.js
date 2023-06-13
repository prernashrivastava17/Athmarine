import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config/index';
import axios from 'axios';

const initialState = { verifyEmail: null, isLoading: false, isError: '' }

const vendorEmailOtpSlice = createSlice({
    name: "getEmailOtp",
    initialState,
    reducers: {
        getEmailOtpRequested(state, action) {
            state.isLoading = true;
            state.verifyEmail = null;
        },
        getEmailOtpSuccess(state, action) {
            state.isLoading = false;
            state.verifyEmail = action.payload
        },
        getEmailOtpFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = '';
            state.updatePasswordRes = null;
        },
    }
});


export const getEmailOTPFun = (data) => {
    return (dispatch) => {
        dispatch(getEmailOtpSlice.getEmailOtpRequested());
        dispatch(getEmailOtpSlice.emptyError());

        axios({
            method: 'get',
            url: `${apiUrls.baseUrl}/OTP/getEmailOTP/${data.email}`,
            data,
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(getEmailOtpSlice.getEmailOtpSuccess(response));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(getEmailOtpSlice.getEmailOtpFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(getEmailOtpSlice.getEmailOtpFailure(error.message));
        });
    };
};

export const getEmailOtpSlice = vendorEmailOtpSlice.actions;

export default vendorEmailOtpSlice.reducer;