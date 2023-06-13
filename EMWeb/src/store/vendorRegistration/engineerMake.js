import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { apiUrls, getCompanyId } from '../../config';

const initialState = { engineerMake: null, isLoading: false, isError: '' };

const engineerMakeSlice = createSlice({
    name: "engineerMake",
    initialState,
    reducers: {
        dispatchEngineerMakeRequest(state, action) {
            state.isLoading = true;
        },
        dispatchEngineerMakeSuccess(state, action) {
            state.isLoading = false;
            state.engineerMake = action.payload;
        },
        dispatchEngineerMakeFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        }
    }
});


export const getEngineerMake = (data) => {
    return (dispatch) => {
        dispatch(getEngineerMakeSlice.dispatchEngineerMakeRequest());

        axios({
            method: 'get',
            url: `${apiUrls.baseUrl}/make/getAllMake`
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(getEngineerMakeSlice.dispatchEngineerMakeSuccess(response.data.responseData));
            } else if (response && response?.status === 500) {
                dispatch(getEngineerMakeSlice.dispatchEngineerFailure(response.data.error));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(getEngineerMakeSlice.dispatchEngineerMakeFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(getEngineerMakeSlice.dispatchEngineerMakeFailure(error));
        });
    };
};


export const getEngineerMakeSlice = engineerMakeSlice.actions;
export default engineerMakeSlice.reducer;