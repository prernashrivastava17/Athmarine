import { createSlice } from "@reduxjs/toolkit";
import { apiUrls, getToken } from "../../config/index.js";
import axios from "axios";

const initialState = { fileRes: null, isLoading: false, isError: "" };

const documentRequestorSlice = createSlice({
  name: "documentDownload",
  initialState,
  reducers: {
    documentRequested(state, action) {
      state.isLoading = true;
    },
    documentSuccess(state, action) {
      state.isLoading = false;
      state.fileRes = action.payload;
      state.navigationCheck = true;
    },
    documentFailure(state, action) {
      state.isLoading = false;
      state.isError = action.payload;
      state.navigationCheck = false;
    },
    emptyError(state, action) {
      state.isError = "";
    },
    emptyState(state, action) {
      state.fileRes = null;
    },
  },
});

export const documentDownloadApi = (endPoint) => {
  return (dispatch) => {
    dispatch(documentRequestorActions.emptyError());
    dispatch(documentRequestorActions.documentRequested());

    axios({
      method: "POST",
      url: `${apiUrls.baseUrl}/${endPoint}`,
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
            documentRequestorActions.documentSuccess(
              response.data.responseData?.presignedUrlForDownload
            )
          );
        } else {
          let errMsg = response.data.responseData.message;
          dispatch(documentRequestorActions.documentFailure(errMsg));
        }
      })
      .catch((error) => {
        dispatch(documentRequestorActions.documentFailure(error.message));
      });
  };
};

export const documentRequestorActions = documentRequestorSlice.actions;

export default documentRequestorSlice.reducer;
