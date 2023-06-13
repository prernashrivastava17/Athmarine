import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { apiUrls, getCompanyId } from '../../config';

const initialState = { engineerCharges: null, isLoading: false, isError: '' };

const engineerChargesSlice = createSlice({
    name: "engineerCharges",
    initialState,
    reducers: {
        dispatchEngineerChargesRequest(state, action) {
            state.isLoading = true;
        },
        dispatchEngineerChargesSuccess(state, action) {
            state.isLoading = false;
            state.engineerCharges = action.payload;
        },
        dispatchEngineerChargesFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        }
    }
});


export const getEngineerCharges = (data) => {
    return (dispatch) => {
        dispatch(getEngineerChargesSlice.dispatchEngineerChargesRequest());

        axios({
            method: 'get',
            url: `${apiUrls.baseUrl}/chages/getEngineerCharges`
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(getEngineerChargesSlice.dispatchEngineerChargesSuccess(response.data.responseData));
            } else if (response && response?.status === 500) {
                dispatch(getEngineerChargesSlice.dispatchEngineerFailure(response.data.error));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(getEngineerChargesSlice.dispatchEngineerChargesFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(getEngineerChargesSlice.dispatchEngineerChargesFailure(error));
        });
    };
};


export const getEngineerChargesSlice = engineerChargesSlice.actions;
export default engineerChargesSlice.reducer;