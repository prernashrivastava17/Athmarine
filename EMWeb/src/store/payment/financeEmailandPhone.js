import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { apiUrls, getCompanyId, getToken } from '../../config';

const initialState = { financeEmailDetails: null, isLoading: false, isError: '' };

const financeEmailDetailsSlice = createSlice({
    name: "financeEmailDetails",
    initialState,
    reducers: {
        dispatchFinanceEmailDetailsRequest(state, action) {
            state.isLoading = true;
        },
        dispatchFinanceEmailDetailsSuccess(state, action) {
            state.isLoading = false;
            state.financeEmailDetails = action.payload;
        },
        dispatchFinanceEmailDetailsFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = ""
        }
    }
});

export const getFinanceEmailDetails = (data) => {
    return (dispatch) => {
        dispatch(getFinanceEmailDetailsSlice.dispatchFinanceEmailDetailsRequest());
        dispatch(getFinanceEmailDetailsSlice.emptyError());

        axios({
            method: 'get',
            url: `${apiUrls.baseUrl}/vendor/getFinaceEmailPhone/${getCompanyId()}`,
            headers: {
                Authorization: `Bearer ${getToken()}`,
            },
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(getFinanceEmailDetailsSlice.dispatchFinanceEmailDetailsSuccess(response.data.responseData));

            } else {
                let errMsg = response.data.responseData.message;
                dispatch(getFinanceEmailDetailsSlice.dispatchFinanceEmailDetailsFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(getFinanceEmailDetailsSlice.dispatchFinanceEmailDetailsFailure(error.message));
        });
    };
};

export const getFinanceEmailDetailsSlice = financeEmailDetailsSlice.actions;
export default financeEmailDetailsSlice.reducer;