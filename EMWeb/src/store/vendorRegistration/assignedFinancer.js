import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { apiUrls } from '../../config';

const initialState = { financerRes: null, isLoading: false, isError: '' , navigationCheck: false};

const financerSlice = createSlice({
    name: "financerSlice", 
    initialState,
    reducers: {
        dispatchFinancerRequest(state, action) {
            state.isLoading = true;
        },
        dispatchFinancerSuccess(state, action) {
            state.isLoading = false;
            state.financerRes = action.payload;
            state.navigationCheck=true;
        },
        dispatchFinancerFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
            state.navigationCheck=false;
        },
        emptyError(state, action) {
            state.isError = ""
        },
        financeNavigation(state,action) {
            state.navigationCheck=action.payload;
        },
    }
});

export const saveFinancer = (data, endPont) => {
    return (dispatch) => {
        dispatch(saveFinancerSlice.dispatchFinancerRequest());
        dispatch(saveFinancerSlice.emptyError());

        axios({
            method: 'post',
            url: `${apiUrls.baseUrl}/${endPont}`,
            data
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(saveFinancerSlice.dispatchFinancerSuccess(response.data.responseData));

            } else {
                let errMsg = response.data.responseData.message;
                dispatch(saveFinancerSlice.dispatchFinancerFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(saveFinancerSlice.dispatchFinancerFailure(error.message));
        });
    };
};

export const saveFinancerSlice = financerSlice.actions;
export default financerSlice.reducer