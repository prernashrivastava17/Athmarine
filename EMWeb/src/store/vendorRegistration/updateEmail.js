import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config';
import axios from 'axios';
import { ToastError } from '../../components/Tostify';

const initialState = { updatedEmailRes: null, isLoading: false, isError: '' }

const updateUserEmailSlice = createSlice({
    name: "updateUserEmail",
    initialState,
    reducers: {
        updateUserEmailRequested(state, action) {
            state.isLoading = true;
            state.updatedEmailRes = null;
        },
        updateUserEmailSuccess(state, action) {
            state.isLoading = false;
            state.updatedEmailRes = action.payload
        },
        updateUserEmailFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = '';
            state.updatedEmailRes = null;
        },
    }
});

export const updateUserEmail_getOTPFunc = (data) => {
    return (dispatch) => {
        dispatch(updateUserEmailActions.updateUserEmailRequested());
        dispatch(updateUserEmailActions.emptyError());

        axios({
            method: 'PUT',
            url: `${apiUrls.baseUrl}/user/updateEmail`,
            data,
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(updateUserEmailActions.updateUserEmailSuccess(response.data.message));
            } else {
                let errMsg = response.data.responseData.message;
                ToastError(errMsg);
                dispatch(updateUserEmailActions.updateUserEmailFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(updateUserEmailActions.updateUserEmailFailure(error.message));
        });
    };
};

export const updateUserEmailActions = updateUserEmailSlice.actions;

export default updateUserEmailSlice.reducer;