import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config';
import axios from 'axios';

const initialState = { state: null, isLoading: false, isError: '' };

const addNewRequestSlice = createSlice({
    name: 'addNewRequest',
    initialState,
    reducers: {
        saveAddNewRequestRequested(state, action) {
            state.isLoading = true;
        },
        saveAddNewRequestSuccess(state, action) {
            state.isLoading = false;
            state.bidder = action.payload;
            state.navigationCheck = true;
        },
        saveAddNewRequestFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
            state.navigationCheck = false;
        }
    }
});

export const addNewRequest = (data, endPoint) => {
    return (dispatch) => {
        dispatch(newRequestSlice.saveAddNewRequestRequested());

        axios({
            method: 'post',
            url: `${apiUrls.baseUrl}/${endPoint}`,
            data
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(newRequestSlice.saveAddNewRequestSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(newRequestSlice.saveBidderFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(newRequestSlice.saveBidderFailure(error.message));
        });
    };
};

export const newRequestSlice = addNewRequestSlice.actions;

export default addNewRequestSlice.reducer;