import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config';
import axios from 'axios';
import { ToastError } from '../../components/Tostify';

const initialState = { updateMobileRes: null, isLoading: false, isError: '' }

const updateUserMobileSlice = createSlice({
    name: "updateUserMobile",
    initialState,
    reducers: {
        updateUserMobileRequested(state, action) {
            state.isLoading = true;
            state.updateMobileRes = null;
        },
        updateUserMobileSuccess(state, action) {
            state.isLoading = false;
            state.updateMobileRes = action.payload
        },
        updateUserMobileFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = '';
            state.updateMobileRes = null;
        }
    }
});

export const updateUserMobile_getOTPFunc = (data) => {
    return (dispatch) => {
        dispatch(updateUserMobileActions.updateUserMobileRequested());
        dispatch(updateUserMobileActions.emptyError());

        axios({
            method: 'PUT',
            url: `${apiUrls.baseUrl}/user/updatePhoneNumber`,
            data,
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(updateUserMobileActions.updateUserMobileSuccess(response.data.message));
            } else {
                let errMsg = response.data.responseData.message;
                ToastError(errMsg);
                dispatch(updateUserMobileActions.updateUserMobileFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(updateUserMobileActions.updateUserMobileFailure(error.message));
        });
    };
};

export const updateUserMobileActions = updateUserMobileSlice.actions;

export default updateUserMobileSlice.reducer;