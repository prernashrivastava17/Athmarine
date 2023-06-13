import { createSlice } from "@reduxjs/toolkit";
import { apiUrls } from "../../config";
import axios from "axios";
import { ToastError, ToastSuccess } from "../../components/Tostify";

const initialState = {
  state: null,
  isLoading: false,
  isError: "",
};

const contactUsSlice = createSlice({
  name: "contactUs",
  initialState: initialState,
  reducers: {
    getContactUsRequested(state, action) {
      state.isLoading = true;
    },
    getContactUsSuccess(state, action) {
      state.isLoading = false;
      state.state = action.payload;
    },
    getContactUsFailure(state, action) {
      state.isLoading = false;
      state.error = action.payload;
    },
    emptyError(state, action) {
      state.isError = "";
    },
  },
});

export const postWebsiteQueries = (endPoint, data) => {
  return (dispatch) => {
    dispatch(contactUsActions.emptyError());
    dispatch(contactUsActions.getContactUsRequested());
    axios({
      method: 'POST',
      url: `${apiUrls.baseUrl}/${endPoint}`,
      data,
  }).then((response) => {
        if (
          response &&
          response?.status === 200 &&
          response.data.message === "Success"
        ) {
          ToastSuccess(response.data.responseData);
            dispatch(
              contactUsActions.getContactUsSuccess(response.data.responseData)
            );
        } else {
          let errMsg = response.data.responseData.message;
          ToastError(errMsg);
            dispatch(contactUsActions.getContactUsFailure(errMsg));
        }
      })
      .catch((error) => {
        dispatch(contactUsActions.getContactUsFailure(error.message));
      });
  };
};

export const contactUsActions = contactUsSlice.actions;

export default contactUsSlice.reducer;
