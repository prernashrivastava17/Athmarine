import { createSlice } from "@reduxjs/toolkit";
import { apiUrls } from "../../config";
import axios from "axios";
import { ToastError } from "../../components/Tostify";

const initialState = { verifyMobileRes: null, isLoading: false, isError: "" };

const vendorverifyMobileSlice = createSlice({
  name: "verifyMobile",
  initialState,
  reducers: {
    mobileVerifyRequested(state, action) {
      state.isLoading = true;
    },
    mobileVerifySuccess(state, action) {
      state.isLoading = false;
      state.verifyMobileRes = action.payload;
    },
    mobileVerifyFailure(state, action) {
      state.isLoading = false;
      state.isError = action.payload;
    },
    emptyError(state, action) {
      state.isError = "";
    },
  },
});

export const verifyMobileOTPFunc = (data) => {
  let phoneCode = parseInt(data.phoneCode);
  return (dispatch) => {
    dispatch(mobileVerifyActions.mobileVerifyRequested());
    dispatch(mobileVerifyActions.emptyError());

    axios({
      method: "get",
      url: `${apiUrls.baseUrl}/OTP/validateOTPSms?OTP=${
        data.otp
      }&phoneCode=${encodeURIComponent(data.phoneCode)}&phoneNumber=${
        data.mobile
      }`,
    })
      .then((response) => {
        if (
          response &&
          response?.status === 200 &&
          response.data.message === "Success"
        ) {
          dispatch(
            mobileVerifyActions.mobileVerifySuccess(response.data.message)
          );
        } else {
          let errMsg = response.data.responseData.message;
          dispatch(mobileVerifyActions.mobileVerifyFailure(errMsg));
        }
      })
      .catch((error) => {
        dispatch(mobileVerifyActions.mobileVerifyFailure(error.message));
      });
  };
};

export const mobileVerifyActions = vendorverifyMobileSlice.actions;

export default vendorverifyMobileSlice.reducer;
