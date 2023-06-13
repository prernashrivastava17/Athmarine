import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config';
import axios from 'axios';
import { ToastError } from '../../components/Tostify';

const initialState = { verifyEmail: null, isLoading: false, isError: '' }

const vendorverifyEmailSlice = createSlice({
    name: "verifyEmail",
    initialState,
    reducers: {
        emailVerifyRequested(state, action) {
            state.isLoading = true;
            state.verifyEmail = null;
        },
        emailVerifySuccess(state, action) {
            state.isLoading = false;
            state.verifyEmail = action.payload
        },
        emailVerifyFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = '';
            state.verifyEmail = null;
        }
    }
});

export const verifyEmailFun = (data) => {
    return (dispatch) => {
        dispatch(emailVerifyActions.emailVerifyRequested());
        dispatch(emailVerifyActions.emptyError());

        axios({
            method: 'get',
            url: `${apiUrls.baseUrl}/OTP/validateOTPEmail?OTP=${data.otp}&email=${data.email}`,
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(emailVerifyActions.emailVerifySuccess(response.data.message));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(emailVerifyActions.emailVerifyFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(emailVerifyActions.emailVerifyFailure(error.message));
        });
    };
};

export const emailVerifyActions = vendorverifyEmailSlice.actions;

export default vendorverifyEmailSlice.reducer;