import { createSlice } from "@reduxjs/toolkit";
import { apiUrls } from "../../config";
import axios from "axios";

const initialState = {
  createServiceRes: null,
  isLoading: false,
  isError: "",
  navigationCheck: false,
};

const createServiceSlice = createSlice({
  name: "createService",
  initialState,
  reducers: {
    savecreateServiceRequested(state, action) {
      state.isLoading = true;
    },
    savecreateServiceSuccess(state, action) {
      state.isLoading = false;
      state.createServiceRes = action.payload;
      state.navigationCheck = true;
    },
    savecreateServiceFailure(state, action) {
      state.isLoading = false;
      state.isError = action.payload;
      state.navigationCheck = false;
    },
    emptyError(state, action) {
      state.isError = "";
    },
    serviceNavigation(state, action) {
      state.navigationCheck = action.payload;
    },
  },
});

export const createServiceFunc = (data, endPoint) => {
  return (dispatch) => {
    dispatch(createServiceActions.savecreateServiceRequested());
    dispatch(createServiceActions.emptyError());

    axios({
      method: "POST",
      url: `${apiUrls.baseUrl}/service/createService`,
      data,
    })
      .then((response) => {
        if (
          response &&
          response?.status === 200 &&
          response.data.message === "Success"
        ) {
          dispatch(
            createServiceActions.savecreateServiceSuccess(
              response.data.responseData
            )
          );
        } else {
          let errMsg = response.data.responseData.message;
          dispatch(createServiceActions.savecreateServiceFailure(errMsg));
        }
      })
      .catch((error) => {
        dispatch(createServiceActions.savecreateServiceFailure(error.message));
      });
  };
};

export const createServiceActions = createServiceSlice.actions;

export default createServiceSlice.reducer;
