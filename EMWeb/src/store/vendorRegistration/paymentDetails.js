import { createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { apiUrls, getAuthData, getCompanyId, getToken } from "../../config";

const initialState = { paymentDetails: null, isLoading: false, isError: "" };

const paymentDetailsSlice = createSlice({
  name: "paymentDetails",
  initialState,
  reducers: {
    dispatchPaymentDetailsRequest(state, action) {
      state.isLoading = true;
    },
    dispatchPaymentDetailsSuccess(state, action) {
      state.isLoading = false;
      state.paymentDetails = action.payload;
    },
    dispatchPaymentDetailsFailure(state, action) {
      state.isLoading = false;
      state.isError = action.payload;
    },
    emptyError(state, action) {
      state.isError = "";
    },
  },
});

export const getPaymentDetails = (data) => {
  return (dispatch) => {
    dispatch(getPaymentDetailsSlice.dispatchPaymentDetailsRequest());
    dispatch(getPaymentDetailsSlice.emptyError());

    axios({
      method: "get",
      url: `${apiUrls.baseUrl}/payment/totalAmount/${getCompanyId()}`,
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
            getPaymentDetailsSlice.dispatchPaymentDetailsSuccess(
              response.data.responseData
            )
          );
        } else {
          let errMsg = response.data.responseData.message;
          dispatch(
            getPaymentDetailsSlice.dispatchPaymentDetailsFailure(errMsg)
          );
        }
      })
      .catch((error) => {
        dispatch(
          getPaymentDetailsSlice.dispatchPaymentDetailsFailure(error.message)
        );
      });
  };
};

export const getPaymentDetailsSlice = paymentDetailsSlice.actions;
export default paymentDetailsSlice.reducer;
