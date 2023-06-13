import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config/index.js';
import axios from 'axios';
import { ToastError, ToastSuccess } from "../../components/Tostify";

const initialState = { emailSubscription: null, isLoading: false, isError: '', navigationCheck: false }

const emailSubscriptionSlice = createSlice({
    name: "emailSubscription",
    initialState,
    reducers: {
        emailSubscriptionRequested(state, action) {
            state.isLoading = true;
            state.details=null;
        },
        emailSubscriptionSuccess(state, action) {
            state.isLoading = false;
            state.details = action.payload;
            state.navigationCheck = true;
        },
        emailSubscriptionFailure(state, action) {
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

export const emailSubscription = (data, endPoint) => {
    return (dispatch) => {
        dispatch(emailSubscriptionActions.emptyError());
        dispatch(emailSubscriptionActions.emailSubscriptionRequested());

        axios({
            method: 'POST',
            url: `${apiUrls.baseUrl}/${endPoint}`,
            data,
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                ToastSuccess(``);
                dispatch(emailSubscriptionActions.emailSubscriptionSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                ToastError(errMsg);
                dispatch(emailSubscriptionActions.emailSubscriptionFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(emailSubscriptionActions.emailSubscriptionFailure(error.message));
        });
    };
};

export const emailSubscriptionActions = emailSubscriptionSlice.actions;

export default emailSubscriptionSlice.reducer;