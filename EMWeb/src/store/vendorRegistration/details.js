import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config/index.js';
import axios from 'axios';

const initialState = { details: null, isLoading: false, isError: '', navigationCheck: false }

const vendorDetailsSlice = createSlice({
    name: "details",
    initialState,
    reducers: {
        saveDetailsRequested(state, action) {
            state.isLoading = true;
            state.details=null;
        },
        saveDetailsSuccess(state, action) {
            state.isLoading = false;
            state.details = action.payload;
            state.navigationCheck = true;
        },
        saveDetailsFailure(state, action) {
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

export const saveDetails = (data, endPoint) => {
    return (dispatch) => {
        dispatch(saveDetailActions.emptyError());
        dispatch(saveDetailActions.saveDetailsRequested());

        axios({
            method: 'POST',
            url: `${apiUrls.baseUrl}/${endPoint}`,
            data,
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                localStorage.setItem('userId', JSON.stringify(response.data.responseData.id));
                dispatch(saveDetailActions.saveDetailsSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(saveDetailActions.saveDetailsFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(saveDetailActions.saveDetailsFailure(error.message));
        });
    };
};

export const saveDetailActions = vendorDetailsSlice.actions;

export default vendorDetailsSlice.reducer;