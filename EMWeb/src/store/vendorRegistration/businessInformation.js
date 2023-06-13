import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config';
import axios from 'axios';

const initialState = { businessInformationRes: null, isLoading: false, isError: '' , navigationCheck: false }

const businessInformationSlice = createSlice({
    name: "businessInformation",
    initialState,
    reducers: {
        saveBusinessInformationRequested(state, action) {
            state.isLoading = true;
        },
        saveBusinessInformationSuccess(state, action) {
            state.isLoading = false;
            state.businessInformationRes = action.payload;
            state.navigationCheck=true;
        },
        saveBusinessInformationFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
            state.navigationCheck=false;
        },
        emptyError(state, action) {
            state.isError = '';
        },
        businessInformationNavigation(state,action) {
            state.navigationCheck=action.payload;
        },
    }
});


export const saveBusinessInformation = (data, endPoint) => {
    return (dispatch) => {
        dispatch(saveBusinessInformationActions.emptyError());
        dispatch(saveBusinessInformationActions.saveBusinessInformationRequested());
        axios({
            method: 'PUT',
            url: `${apiUrls.baseUrl}/${endPoint}`,
            data
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(saveBusinessInformationActions.saveBusinessInformationSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(saveBusinessInformationActions.saveBusinessInformationFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(saveBusinessInformationActions.saveBusinessInformationFailure(error.message));
        });
    };
};

export const saveBusinessInformationActions = businessInformationSlice.actions;

export default businessInformationSlice.reducer;