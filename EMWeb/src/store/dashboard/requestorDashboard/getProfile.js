import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { apiUrls, getToken } from '../../../config';
import { ToastError } from '../../../components/Tostify';

const initialState = { state: null, isLoading: false, isError: '' };

const profileSlice = createSlice({
    name: "getVessel",
    initialState,
    reducers: {
        getProfileRequested(state, action) {
            state.isLoading = true;
            state.state = null;
        },
        getProfileSuccess(state, action) {
            state.isLoading = false;
            state.state = action.payload
        },
        getProfileFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        }
    }
});


export const getProfile = (data, endPoint) => {
    return (dispatch) => {
        dispatch(getProfileSlice.getProfileRequested());

        axios({
            method: 'get',
            url: `${apiUrls.baseUrl}/${endPoint}`,
            headers: {
                'Authorization': `Bearer ${getToken()}`,
            },
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(getProfileSlice.getProfileSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(getProfileSlice.getProfileFailure(errMsg));
                ToastError(errMsg);
            }
        }).catch((error) => {
            ToastError(error.message);
        });
    };
};

export const getProfileSlice = profileSlice.actions;

export default profileSlice.reducer;