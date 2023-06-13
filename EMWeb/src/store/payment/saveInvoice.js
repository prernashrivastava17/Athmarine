import { createSlice } from "@reduxjs/toolkit";
import { apiUrls, getToken } from "../../config";
import axios from "axios";

const initialState = { saveInvoice: null, isLoading: false, isError: null };

const saveInvoiceSlice = createSlice({
  name: "saveInvoice",
  initialState,
  reducers: {
    saveInvoiceStart: (state) => {
      state.isLoading = true;
      state.isError = null;
    },
    saveInvoiceSuccess: (state, action) => {
      state.isLoading = false;
      state.state = action.payload;
    },
    saveInvoiceFailure: (state, action) => {
      state.isLoading = false;
      state.isError = action.payload;
    },
    emptyerror(state, action) {
      state.isError = "";
    },
  },
});

export const saveInvoice = (data) => {
  return (dispatch) => {
    dispatch(saveInvoiceAction.emptyerror());
    dispatch(saveInvoiceAction.saveInvoiceStart());

    axios({
      method: "post",
      url: `${apiUrls.baseUrl}/invoice/saveInvoice`,
      data,
      headers: {
        Authorization: `Bearer ${getToken()}`,
      },
    })
      .then((response) => {
        if (response.data.status === 200) {
          dispatch(saveInvoiceAction.saveInvoiceSuccess(response.data.data));
        } else {
          dispatch(saveInvoiceAction.saveInvoiceFailure(response.data.message));
        }
      })
      .catch((error) => {
        dispatch(saveInvoiceAction.saveInvoiceFailure(error.message));
      });
  };
};

export const saveInvoiceAction = saveInvoiceSlice.actions;
export default saveInvoiceSlice.reducer;
