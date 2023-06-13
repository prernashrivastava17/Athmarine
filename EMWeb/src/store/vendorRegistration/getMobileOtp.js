import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config/index';
import axios from 'axios';

const initialState = { getMobileOTPRes: null, isLoading: false, isError: '' }

const vendorMobileOtpSlice = createSlice({
    name: "getMobileOtp",
    initialState,
    reducers: {
        getMobileOtpRequested(state, action) {
            state.isLoading = true;
            state.getMobileOTPRes = null;
        },
        getMobileOtpSuccess(state, action) {
            state.isLoading = false;
            state.getMobileOTPRes = action.payload
        },
        getMobileOtpFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        }
    }
});


export const getMobileOTPFunc = (data) => {
    return (dispatch) => {
        dispatch(getMobileOtpSlice.getMobileOtpRequested());

        axios({
            method: 'get',
            url: `${apiUrls.baseUrl}/OTP/getSmsOTP/${data.mobile}`,
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(getMobileOtpSlice.getMobileOtpSuccess(response));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(getMobileOtpSlice.getMobileOtpFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(getMobileOtpSlice.getMobileOtpFailure(error.message));
        });
    };
};

export const getMobileOtpSlice = vendorMobileOtpSlice.actions;

export default vendorMobileOtpSlice.reducer;