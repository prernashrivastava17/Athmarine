import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { apiUrls, getCompanyId } from '../../config';

const initialState = { engineer: null, approverList: null, isLoading: false, isError: '', navigationCheck: false };

const engineerSlice = createSlice({
    name: "engineer",
    initialState,
    reducers: {
        dispatchEngineerRequest(state, action) {
            state.isLoading = true;
        },
        dispatchEngineerSuccess(state, action) {
            state.isLoading = false;
            state.engineer = action.payload;
            state.navigationCheck = true;
        },
        dispatchEngineerFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
            state.navigationCheck = false;
        },

        dispatchApproverRequest(state, action) {
            state.isLoading = true;
        },
        successApproverListCall(state, action) {
            state.isLoading = false;
            state.approverList = action.payload;
        },
        failureApproverListCall(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = ""
        },
        engineerNavigation(state, action) {
            state.navigationCheck = action.payload;
        },
    }
});


export const saveEngineer = (data) => {
    return (dispatch) => {
        dispatch(saveEngineerSlice.emptyError());
        dispatch(saveEngineerSlice.dispatchEngineerRequest());

        axios({
            method: 'post',
            url: `${apiUrls.baseUrl}/equipments/createEngineer`,
            data
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(saveEngineerSlice.dispatchEngineerSuccess(response.data.responseData));
            } else if (response && response?.status === 500) {
                dispatch(saveEngineerSlice.dispatchEngineerFailure(response.data.error));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(saveEngineerSlice.dispatchEngineerFailure(errMsg));
            }
            // dispatch(saveEngineerSlice.dispatchEngineerSuccess(response.data.responseData));
        }).catch((error) => {
            dispatch(saveEngineerSlice.dispatchEngineerFailure(error));
        });
    };
};

export const getApproverList = () => {
    return (dispatch) => {
        dispatch(saveEngineerSlice.emptyError());
        dispatch(saveEngineerSlice.dispatchApproverRequest());
        axios.get(`${apiUrls.baseUrl}/customer/approver/getAll/${getCompanyId()}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(saveEngineerSlice.successApproverListCall(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(saveEngineerSlice.failureApproverListCall(errMsg));
            }
        }).catch((error) => {
            dispatch(saveEngineerSlice.failureApproverListCall(error.message));
        });
    };
};

export const saveEngineerSlice = engineerSlice.actions;
export default engineerSlice.reducer;