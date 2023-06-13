import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { apiUrls, getToken } from '../../../config';
import { ToastError } from '../../../components/Tostify';

const initialState = { state: null, isLoading: false, isError: '' };

const vesselSlice = createSlice({
    name: "getVessel",
    initialState,
    reducers: {
        getVesselRequested(state, action) {
            state.isLoading = true;
            state.state = null;
        },
        getVesselSuccess(state, action) {
            state.isLoading = false;
            state.state = action.payload
        },
        getVesselFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        }
    }
});


export const getVessel = () => {
    return (dispatch) => {
        dispatch(getVesselSlice.getVesselRequested());

        axios({
            method: 'get',
            url: `${apiUrls.baseUrl}/vessel/getVesselList`,
            headers: {
                'Authorization': `Bearer ${getToken()}`,
            },
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(getVesselSlice.getVesselSuccess(response.data.responseData));
            } else {
                ToastError(response.data.responseData.message);
            }
        }).catch((error) => {
            ToastError(error.message);
        });
    };
};

export const getVesselSlice = vesselSlice.actions;

export default vesselSlice.reducer;