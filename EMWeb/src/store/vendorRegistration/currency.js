import { createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { ToastError } from "../../components/Tostify";
import { apiUrls, getToken } from "../../config";

const initialState = { currencyStatus: null, isLoading: false, isError: "" };

const currencySlice = createSlice({
  name: "currencyStatus",
  initialState,
  reducers: {
    dispatchCurrencyStatusRequest(state, action) {
      state.isLoading = true;
      state.currencyStatus = null;
    },
    dispatchCurrencyStatusSuccess(state, action) {
      state.isLoading = false;
      state.currencyStatus = action.payload;
      state.isError = "";
    },
    dispatchCurrencyStatusFailure(state, action) {
      state.isLoading = false;
      state.isError = action.payload;
      state.currencyStatus = null;
    },
    emptyError(state, action) {
      state.isError = "";
    },
  },
});

export const getCurrencyStatus = () => {
  return (dispatch) => {
    dispatch(currencyStatusSlice.emptyError());
    dispatch(currencyStatusSlice.dispatchCurrencyStatusRequest());
    axios({
      method: "get",
      url: `${apiUrls.baseUrl}/currency/api/get`,
      headers: {
        Authorization: `Bearer ${getToken()}`,
      },
    })
      .then((response) => {
        if (
          response &&
          response?.status === 200 &&
          response.data.message === "Success"
        ) {
          dispatch(
            currencyStatusSlice.dispatchCurrencyStatusSuccess(
              response.data.responseData
            )
          );
        } else {
          let errMsg = response.data.responseData.message;
          ToastError(errMsg);
          dispatch(currencyStatusSlice.dispatchCurrencyStatusFailure(errMsg));
        }
      })
      .catch((error) => {
        dispatch(
          currencyStatusSlice.dispatchCurrencyStatusFailure(error.message)
        );
      });
  };
};

export const currencyStatusSlice = currencySlice.actions;
export default currencySlice.reducer;
