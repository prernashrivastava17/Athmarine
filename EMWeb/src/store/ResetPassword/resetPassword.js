import { createSlice } from '@reduxjs/toolkit';
import { apiUrls, getToken } from '../../config/index.js';
import axios from 'axios';

const initialState = { resetPassword: null, isLoading: false, isError: '', resetEmail:"" }

const resetPasswordSlice = createSlice({
    name: "resetPassword",
    initialState,
    reducers: {
        resetPasswordRequested(state, action) {
            state.isLoading = true;
        },
        saveResetEmail(state,action){
           state.resetEmail=action.payload.email;
        },
        resetPasswordSuccess(state, action) {
            state.isLoading = false;
            state.resetPassword = action.payload;
        },
        resetPasswordFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = '';
            state.resetPassword= null;
        },
        

    }
});

export const resetPassword = (endPoint) => {
    return (dispatch) => {
        dispatch(resetPasswordActions.emptyError());
        dispatch(resetPasswordActions.resetPasswordRequested());

        axios({
            method: 'post',
            url: `${apiUrls.baseUrl}/${endPoint}`,
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(resetPasswordActions.resetPasswordSuccess(response.data.message));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(resetPasswordActions.resetPasswordFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(resetPasswordActions.resetPasswordFailure(error.message));
        });
    };
};

export const resetPasswordActions = resetPasswordSlice.actions;

export default resetPasswordSlice.reducer;