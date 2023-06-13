import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { ToastError, ToastSuccess } from "../../components/Tostify";
import { apiUrls, getToken } from '../../config';

const initialState = { validateEmailPhone: null, isLoading: false, isError: '', navigationCheck: false }

const validateEmailPhoneSlice = createSlice({
    name: "validateEmailPhone",
    initialState,
    reducers: {
        validateEmailPhoneRequested(state, action) {
            state.isLoading = true;
            state.details=null;
        },
        validateEmailPhoneSuccess(state, action) {
            state.isLoading = false;
            state.details = action.payload;
            state.navigationCheck = true;
        },
        validateEmailPhoneFailure(state, action) {
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

export const validateEmailPhone = (data, endPoint) => {
    return (dispatch) => {
        dispatch(validateEmailPhoneActions.emptyError());
        dispatch(validateEmailPhoneActions.validateEmailPhoneRequested());

        axios({
            method: 'POST',
            url: `${apiUrls.baseUrl}/${endPoint}`,
            data,
            headers: {
                Authorization: `Bearer ${getToken()}`,
            },
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(validateEmailPhoneActions.validateEmailPhoneSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                ToastError(errMsg);
                dispatch(validateEmailPhoneActions.validateEmailPhoneFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(validateEmailPhoneActions.validateEmailPhoneFailure(error.message));
        });
    };
};

export const validateEmailPhoneActions = validateEmailPhoneSlice.actions;

export default validateEmailPhoneSlice.reducer;