import { createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { ToastError } from "../../components/Tostify";
import { apiUrls, getToken } from "../../config";

const initialState = { currencyCountryStatus: null, isLoading: false, isError: "" };

const currencyCountrySlice = createSlice({
  name: "currencyCountryStatus",
  initialState,
  reducers: {
    dispatchCurrencyCountryStatusRequest(state, action) {
      state.isLoading = true;
      state.currencyCountryStatus = null;
    },
    dispatchCurrencyCountryStatusSuccess(state, action) {
      state.isLoading = false;
      state.currencyCountryStatus = action.payload;
      state.isError = "";
    },
    dispatchCurrencyCountryStatusFailure(state, action) {
      state.isLoading = false;
      state.isError = action.payload;
      state.currencyCountryStatus = null;
    },
    emptyError(state, action) {
      state.isError = "";
    },
  },
});

export const getCurrencyCountryStatus = () => {
  return (dispatch) => {
    dispatch(currencyCountryStatusSlice.emptyError());
    dispatch(currencyCountryStatusSlice.dispatchCurrencyCountryStatusRequest());
    axios({
      method: "get",
      url: `${apiUrls.baseUrl}/master/getCountryCurrencyList`
    //   headers: {
    //     Authorization: `Bearer ${getToken()}`,
    //   },
    })
      .then((response) => {
        if (
          response &&
          response?.status === 200 &&
          response.data.message === "Success"
        ) {
          dispatch(
            currencyCountryStatusSlice.dispatchCurrencyCountryStatusSuccess(
              response.data.responseData
            )
          );
        } else {
          let errMsg = response.data.responseData.message;
          ToastError(errMsg);
          dispatch(currencyCountryStatusSlice.dispatchCurrencyCountryStatusFailure(errMsg));
        }
      })
      .catch((error) => {
        dispatch(
          currencyCountryStatusSlice.dispatchCurrencyCountryStatusFailure(error.message)
        );
      });
  };
};

export const currencyCountryStatusSlice = currencyCountrySlice.actions;
export default currencyCountrySlice.reducer;
