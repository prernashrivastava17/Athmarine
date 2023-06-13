import { createSlice } from "@reduxjs/toolkit";
import { apiUrls, getToken } from "../../../config/index.js";
import axios from "axios";

const initialState = {
  referralCodeResponse: null,
  isLoading: false,
  isError: "",
};

const referralCodeSlice = createSlice({
  name: "referralCodeChecking",
  initialState,
  reducers: {
    referralCodeRequested(state, action) {
      state.isLoading = true;
    },
    referralCodeSuccess(state, action) {
      state.isLoading = false;
      state.referralCodeResponse = action.payload;
      state.isError = "";
    },
    referralCodeFailure(state, action) {
      state.isLoading = false;
      state.isError = action.payload;
    },
  },
});

export const saveReferralCode = (data) => {
  return (dispatch) => {
    dispatch(saveReferralCodesActions.referralCodeRequested());

    axios({
      method: "get",
      url: `${apiUrls.baseUrl}/vendor/validateReferralCode/${data}`,
    })
      .then((response) => {
        if (
          response &&
          response?.status === 200 &&
          response.data.message === "Success"
        ) {
          dispatch(
            saveReferralCodesActions.referralCodeSuccess(
              response.data.responseData
            )
          );
        } else {
          let errMsg = response.data.responseData.message;
          dispatch(saveReferralCodesActions.referralCodeFailure(errMsg));
        }
      })
      .catch((error) => {
        dispatch(saveReferralCodesActions.referralCodeFailure(error.message));
      });
  };
};

export const saveReferralCodesActions = referralCodeSlice.actions;

export default referralCodeSlice.reducer;
