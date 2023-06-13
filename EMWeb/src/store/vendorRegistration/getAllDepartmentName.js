import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config';
import axios from 'axios';

const initialCountryState = { deptName: null, isLoading: false, isError: '' }

const getAllDeptSlice = createSlice({
    name: 'country',
    initialState: initialCountryState,
    reducers: {
        getAllDeptNameRequested(state, action) {
            state.isLoading = true;
        },
        getAllDeptNameSuccess(state, action) {
            state.isLoading = false;
            state.deptName = action.payload;
        },
        getAllDeptNameFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        }
    }
});

export const getAllVerifiedDepartmentName = () => {
    return (dispatch) => {
        dispatch(getAllDeptActions.getAllDeptNameRequested());

        axios.get(`${apiUrls.baseUrl}/departmentName/get`, {
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(getAllDeptActions.getAllDeptNameSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(getAllDeptActions.getAllDeptNameFailure(errMsg))
            }
        }).catch((error) => {
            dispatch(getAllDeptActions.getAllDeptNameFailure(error.message))
        });
    };
};

export const getAllDeptActions = getAllDeptSlice.actions;

export default getAllDeptSlice.reducer;