import { createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { apiUrls } from "../../config";
import { ToastError } from "../../components/Tostify";

const initialState = { state: null, isLoading: false, isError: "" };

const promotionDetailsPostSlice = createSlice({
  name: "promotionDetailsPost",
  initialState,
  reducers: {
    savePromotionDetailsPostRequested(state, action) {
      state.isLoading = true;
    },
    savePromotionDetailsPostSuccess(state, action) {
      state.isLoading = false;
      state.state = action.payload;
    },
    savePromotionDetailsPostFailure(state, action) {
      state.isLoading = false;
      state.isError = action.payload;
    },
    emptyError(state, action) {
      state.isError = "";
    },
  },
});

export const promotionDetailsPost = (data, endPoint) => {
  return (dispatch) => {
    dispatch(promotionDetailsPostActions.emptyError());
    dispatch(promotionDetailsPostActions.savePromotionDetailsPostRequested());
    axios({
      method: "put",
      url: `${apiUrls.baseUrl}/${endPoint}`,
      data,
    })
      .then((response) => {
        if (response && response.status === 200) {
          dispatch(
            promotionDetailsPostActions.savePromotionDetailsPostSuccess(
              response.data
            )
          );
        } else {
          let errMsg = response.data.error.message;
          dispatch(
            promotionDetailsPostActions.savePromotionDetailsPostFailure(errMsg)
          );
        }
      })
      .catch((error) => {
        ToastError(`${error.response.data.error.message}`);
        dispatch(
          promotionDetailsPostActions.savePromotionDetailsPostFailure(
            error.response.data.error.message
          )
        );
      });
  };
};

export const promotionDetailsPostActions = promotionDetailsPostSlice.actions;
export default promotionDetailsPostSlice.reducer;
