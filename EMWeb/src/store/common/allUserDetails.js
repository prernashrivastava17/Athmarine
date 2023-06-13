import { createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { apiUrls, getCompanyId, getToken } from "../../config";

const initialState = { allUserDetails: null, isLoading: false, isError: "" };

const allUserDetailsSlice = createSlice({
  name: "allUserDetails",
  initialState,
  reducers: {
    dispatchAllUserDetailsRequest(state, action) {
      state.isLoading = true;
    },
    dispatchAllUserDetailsSuccess(state, action) {
      state.isLoading = false;
      state.allUserDetails = action.payload;
    },
    dispatchAllUserDetailsFailure(state, action) {
      state.isLoading = false;
      state.isError = action.payload;
    },
    emptyError(state, action) {
      state.isError = "";
    },
  },
});

export const getAllUserDetails = (data) => {
  return (dispatch) => {
    dispatch(getAllUserDetailsActions.dispatchAllUserDetailsRequest());
    dispatch(getAllUserDetailsActions.emptyError());

    axios({
      method: "get",
      url: `${apiUrls.baseUrl}/user/getUserCompany/{id}?id=${getCompanyId()}`,
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
            getAllUserDetailsActions.dispatchAllUserDetailsSuccess(
              response.data.responseData
            )
          );
        } else {
          let errMsg = response.data.responseData.message;
          dispatch(
            getAllUserDetailsActions.dispatchAllUserDetailsFailure(errMsg)
          );
        }
      })
      .catch((error) => {
        dispatch(
          getAllUserDetailsActions.dispatchAllUserDetailsFailure(error.message)
        );
      });
  };
};

export const getAllUserDetailsActions = allUserDetailsSlice.actions;
export default allUserDetailsSlice.reducer;
