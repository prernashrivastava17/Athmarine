import { createSlice } from '@reduxjs/toolkit';
import { apiUrls, getCompanyId } from '../../config/index';
import axios from 'axios';

const initialState = { managingHeadRes: null, approverList: null, isLoading: false, isError: '', navigationCheck: false };

const managingHeadSlice = createSlice({
    name: 'managingHead',
    initialState,
    reducers: {
        saveManagingHeadRequested(state, action) {
            state.isLoading = true;
        },
        saveManagingHeadSuccess(state, action) {
            state.isLoading = false;
            state.managingHeadRes = action.payload;
            state.navigationCheck = true;
        },
        saveManagingHeadFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
            state.navigationCheck = false;
        },

        getApproverRequested(state, action) {
            state.isLoading = true;
        },
        getApproverSuccess(state, action) {
            state.isLoading = false;
            state.approverList = action.payload;
            // state.navigationCheck = true;
        },
        getApproverFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
            state.navigationCheck = false;
        },
        emptyError(state, action) {
            state.isError = ""
        },
        managingHeadNavigation(state, action) {
            state.navigationCheck = action.payload;
        },
    }
});

export const saveManagingHead = (data, endPoint) => {
    return (dispatch) => {
        dispatch(saveManagingHeadSlice.emptyError());
        dispatch(saveManagingHeadSlice.saveManagingHeadRequested());

        axios({
            method: 'post',
            url: `${apiUrls.baseUrl}/${endPoint}`,
            data
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(saveManagingHeadSlice.saveManagingHeadSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(saveManagingHeadSlice.saveManagingHeadFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(saveManagingHeadSlice.saveManagingHeadFailure(error.message));
        });
    };
};

export const getApproverList = () => {
    return (dispatch) => {
        dispatch(saveManagingHeadSlice.getApproverRequested());
        axios.get(`${apiUrls.baseUrl}/customer/getAllApprover/${getCompanyId()}`, {})
            .then((response) => {
                if (response && response?.status === 200 && response.data.message === "Success") {
                    dispatch(saveManagingHeadSlice.getApproverSuccess(response.data.responseData));
                } else {
                    let errMsg = response.data.responseData.message;
                    dispatch(saveManagingHeadSlice.getApproverFailure(errMsg));
                }
            }).catch((error) => {
                dispatch(saveManagingHeadSlice.getApproverFailure(error.message));
            });
    };
};

export const saveManagingHeadSlice = managingHeadSlice.actions;

export default managingHeadSlice.reducer;