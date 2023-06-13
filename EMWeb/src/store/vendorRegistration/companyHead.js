import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config';
import axios from 'axios';

const initialState = { companyHeadObject: null, isLoading: false, isError: '' ,navigationCheck:false};

const companyHeadSlice = createSlice({
    name: 'companyHead',
    initialState,
    reducers: {
        dispatchCompanyHeadCall(state, action) {
            state.isLoading = true;
        },
        successCompanyHeadCall(state, action) {
            state.isLoading = false;
            state.companyHeadObject = action.payload;
            state.navigationCheck=true;
        },
        failureCompanyHeadCall(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
            state.navigationCheck=false;
        },
        emptyError(state, action) {
            state.isError = '';
        } ,
        companyHeadNavigationPass(state,action){
            state.navigationCheck=action.payload;
        }
    }
});

export const saveCompanyHead = (data) => {
    return (dispatch) => {
        dispatch(saveCompanyHeadSlice.emptyError());
        dispatch(saveCompanyHeadSlice.dispatchCompanyHeadCall());

        axios({
            method: 'post',
            url: `${apiUrls.baseUrl}/vendor/createVendorCompanyHead`,
            data,
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(saveCompanyHeadSlice.successCompanyHeadCall(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(saveCompanyHeadSlice.failureCompanyHeadCall(errMsg));
            }
        }).catch((error) => {
            dispatch(saveCompanyHeadSlice.failureCompanyHeadCall(error.message));
        });
    };
};

export const saveCompanyHeadSlice = companyHeadSlice.actions;

export default companyHeadSlice.reducer;