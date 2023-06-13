import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config/index.js';
import axios from 'axios';
import { ToastError, ToastSuccess } from "../../components/Tostify";

const initialState = { phoneSubscription: null, isLoading: false, isError: '', navigationCheck: false }

const phoneSubscriptionSlice = createSlice({
    name: "phoneSubscription",
    initialState,
    reducers: {
        phoneSubscriptionRequested(state, action) {
            state.isLoading = true;
            state.details=null;
        },
        phoneSubscriptionSuccess(state, action) {
            state.isLoading = false;
            state.details = action.payload;
            state.navigationCheck = true;
        },
        phoneSubscriptionFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
            state.navigationCheck = false;
        },
        emptyError(state, action) {
            state.isError = '';
        },
        detailsNavigation(state, action) {
            state.navigationCheck = action.payload;
        },
    }
});

export const phoneSubscription = (data, endPoint) => {
    return (dispatch) => {
        dispatch(phoneSubscriptionActions.emptyError());
        dispatch(phoneSubscriptionActions.phoneSubscriptionRequested());

        axios({
            method: 'POST',
            url: `${apiUrls.baseUrl}/${endPoint}`,
            data,
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                ToastSuccess(``);
                dispatch(phoneSubscriptionActions.phoneSubscriptionSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                ToastError(errMsg);
                dispatch(phoneSubscriptionActions.phoneSubscriptionFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(phoneSubscriptionActions.phoneSubscriptionFailure(error.message));
        });
    };
};

export const phoneSubscriptionActions = phoneSubscriptionSlice.actions;

export default phoneSubscriptionSlice.reducer;