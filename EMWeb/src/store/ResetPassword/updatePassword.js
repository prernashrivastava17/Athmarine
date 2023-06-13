import { createSlice } from '@reduxjs/toolkit';
import { apiUrls, getToken } from '../../config/index.js';
import axios from 'axios';

const initialState = { updatePasswordRes: null, isLoading: false, isError: '' }

const updatePasswordSlice = createSlice({
    name: "updatePassword",
    initialState,
    reducers: {
        updatePasswordRequested(state, action) {
            state.isLoading = true;
        },
        updatePasswordSuccess(state, action) {
            state.isLoading = false;
            state.updatePasswordRes = action.payload;
        },
        updatePasswordFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = '';
            state.updatePasswordRes =null
        },
        

    }
});

export const updatePasswordFunc = (data) => {
    return (dispatch) => {
        dispatch(updatePasswordActions.emptyError());
        dispatch(updatePasswordActions.updatePasswordRequested());

        axios({
            method: 'post',
            url: `${apiUrls.baseUrl}/updatePassword`,
            data,
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(updatePasswordActions.updatePasswordSuccess(response.data.message));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(updatePasswordActions.updatePasswordFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(updatePasswordActions.updatePasswordFailure(error.message));
        });
    };
};

export const updatePasswordActions = updatePasswordSlice.actions;

export default updatePasswordSlice.reducer;