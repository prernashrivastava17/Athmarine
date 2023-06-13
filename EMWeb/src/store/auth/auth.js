import { createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { ToastError } from "../../components/Tostify";
import { apiUrls } from "./../../config/index";

const initialAuthState = { isAuthenticated: false, isLoading: false, isError: "", authRes: null, };

const authSlice = createSlice({
  name: "authentication",
  initialState: initialAuthState,
  reducers: {
    loginAPIRequested(state) {
      state.isLoading = true;
      state.isAuthenticated = false;
    },
    loginAPISuccess(state, action) {
      state.isLoading = false;
      state.isAuthenticated = true;
      state.authRes = action.payload;
    },
    loginAPIFailure(state, action) {
      state.isLoading = false;
      state.isAuthenticated = false;
      state.isError = action.payload;
    },
    logoutAction(state, action) {
      state.isAuthenticated = false;
      state.authRes = null;
      localStorage.clear();
    },
  },
});

export const userAuthentication = (data) => {
  return (dispatch) => {
    dispatch(authActions.loginAPIRequested());
    axios({
      method: "post",
      url: `${apiUrls.baseUrl}/authenticate`,
      data,
    })
      .then((response) => {
        if (response && response?.status === 200 && response.data.message === "Success") {
          if (response.data.responseData.registeredSuccessfully === false) {
            localStorage.setItem(`userId`, JSON.stringify(response.data.responseData.userId));

          } else if (response.data.responseData.name === `Requestor` && response.data.responseData.registeredSuccessfully === true) {
            localStorage.setItem(`requestorId`, JSON.stringify(response.data.responseData.userId));

          }
          localStorage.setItem("authData", JSON.stringify(response.data.responseData));
          dispatch(authActions.loginAPISuccess(response.data.responseData));
        } else {
          let errMsg = response.data.responseData.message;
          ToastError('Invalid credentials');
          dispatch(authActions.loginAPIFailure(errMsg));
        }
      })
      .catch((error) => {
        const errorMsg = error.message;
        ToastError(errorMsg);
        dispatch(authActions.loginAPIFailure());
      });
  };
};

export const authActions = authSlice.actions;

export default authSlice.reducer;