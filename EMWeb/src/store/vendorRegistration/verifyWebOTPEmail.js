import { createSlice } from "@reduxjs/toolkit";
import { apiUrls } from "../../config";
import axios from "axios";
import { ToastError } from "../../components/Tostify";

const initialState = { verifyEmailRes: null, isLoading: false, isError: "" };

const webVerifyEmailSlice = createSlice({
  name: "verifyEmail",
  initialState,
  reducers: {
    emailVerifyRequested(state, action) {
      state.isLoading = true;
      state.verifyEmailRes = null;
    },
    emailVerifySuccess(state, action) {
      state.isLoading = false;
      state.verifyEmailRes = action.payload;
    },
    emailVerifyFailure(state, action) {
      state.isLoading = false;
      state.isError = action.payload;
    },
    emptyError(state, action) {
      state.isError = "";
      state.verifyEmailRes = null;
    },
  },
});

export const verifyWebEmailOTP = (data, endPoint) => {
  return (dispatch) => {
    dispatch(saveWebVerifyEmail.emailVerifyRequested());
    dispatch(saveWebVerifyEmail.emptyError());

    axios({
      method: "post",
      url: `${apiUrls.baseUrl}/${endPoint}`,
      data,
    })
      .then((response) => {
        if (
          response &&
          response?.status === 200 &&
          response.data.message === "Success"
        ) {
          dispatch(
            saveWebVerifyEmail.emailVerifySuccess(response.data.responseData)
          );
        } else {
          let errMsg = response.data.responseData.message;
          dispatch(saveWebVerifyEmail.emailVerifyFailure(errMsg));
        }
      })
      .catch((error) => {
        dispatch(saveWebVerifyEmail.emailVerifyFailure(error.message));
      });
  };
};

export const saveWebVerifyEmail = webVerifyEmailSlice.actions;

export default webVerifyEmailSlice.reducer;
