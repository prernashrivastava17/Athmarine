import { createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { ToastError } from "../../components/Tostify";
import { apiUrls, getToken } from "../../config";

const initialState = { engineerStatus: null, isLoading: false, isError: "" };

const engineerSlice = createSlice({
  name: "engineerStatus",
  initialState,
  reducers: {
    dispatchEngineerStatusRequest(state, action) {
      state.isLoading = true;
      state.engineerStatus = null;
    },
    dispatchEngineerStatusSuccess(state, action) {
      state.isLoading = false;
      state.engineerStatus = action.payload;
      state.isError = "";
    },
    dispatchEngineerStatusFailure(state, action) {
      state.isLoading = false;
      state.isError = action.payload;
      state.engineerStatus = null;
    },
    emptyError(state, action) {
      state.isError = "";
    },
  },
});

export const getEngineerStatus = (id) => {
  return (dispatch) => {
    dispatch(engineerStatusSlice.emptyError());
    dispatch(engineerStatusSlice.dispatchEngineerStatusRequest());
    axios({
      method: "get",
      url: `${apiUrls.baseUrl}/equipments/getEngineerByUid/${id}`,
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
            engineerStatusSlice.dispatchEngineerStatusSuccess(
              response.data.responseData
            )
          );
        } else {
          let errMsg = response.data.responseData.message;
          ToastError(errMsg);
          dispatch(engineerStatusSlice.dispatchEngineerStatusFailure(errMsg));
        }
      })
      .catch((error) => {
        dispatch(
          engineerStatusSlice.dispatchEngineerStatusFailure(error.message)
        );
      });
  };
};

export const engineerStatusSlice = engineerSlice.actions;
export default engineerSlice.reducer;
