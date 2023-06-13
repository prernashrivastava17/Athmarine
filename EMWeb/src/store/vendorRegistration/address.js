import { createSlice } from "@reduxjs/toolkit";
import { apiUrls, getToken } from "../../config/index.js";
import axios from "axios";

const initialState = {
  addressRes: null,
  isLoading: false,
  isError: "",
  navigationCheck: false,
};

const vendorAddressSlice = createSlice({
  name: "addressRegistration",
  initialState,
  reducers: {
    addressRequested(state, action) {
      state.isLoading = true;
    },
    addressSuccess(state, action) {
      state.isLoading = false;
      state.addressRes = action.payload;
      state.navigationCheck = true;
    },
    addressFailure(state, action) {
      state.isLoading = false;
      state.isError = action.payload;
      state.navigationCheck = false;
    },
    emptyError(state, action) {
      state.isError = "";
    },
    addressNavigation(state, action) {
      state.navigationCheck = action.payload;
    },
    changeNavigationCheckToFalse(state, action) {
      state.navigationCheck = false;
    },
  },
});

export const saveAddress = (data, endPoint) => {
  return (dispatch) => {
    dispatch(saveAddressActions.emptyError());
    dispatch(saveAddressActions.addressRequested());

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
          localStorage.setItem(
            "token",
            JSON.stringify(response?.data?.responseData?.token)
          );
          localStorage.setItem(
            "companyId",
            JSON.stringify(response?.data?.responseData?.id)
          );
          localStorage.setItem(
            "addressInfo",
            JSON.stringify(response?.data?.responseData)
          );
          localStorage.setItem(
            "referalCode",
            JSON.stringify(response?.data?.responseData.referralCode)
          );
          dispatch(
            saveAddressActions.addressSuccess(response.data.responseData)
          );
        } else {
          let errMsg = response.data.responseData.message;
          dispatch(saveAddressActions.addressFailure(errMsg));
        }
      })
      .catch((error) => {
        dispatch(saveAddressActions.addressFailure(error.message));
      });
  };
};

export const saveAddressActions = vendorAddressSlice.actions;

export default vendorAddressSlice.reducer;
