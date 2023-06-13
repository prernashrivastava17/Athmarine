import { createSlice } from '@reduxjs/toolkit';
import { apiUrls, getToken } from '../../config';
import axios from 'axios';

const encodedUserInformation = { userInformationRes: null, isLoading: false, isError: '' }

const encodeUserSlice = createSlice({
    name: 'encodedUser',
    initialState: encodedUserInformation,
    reducers: {
        getUserRequested(state, action) {
            state.isLoading = true;
        },
        getUserSuccess(state, action) {
            state.isLoading = false;
            state.userInformationRes = action.payload;
        },
        getUserFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = '';
        }
    }
});

export const getEncodedUserInformation = (params) => {
    return (dispatch) => {
        dispatch(encodedUserActions.emptyError());
        dispatch(encodedUserActions.getUserRequested());
        axios.get(`${apiUrls.baseUrl}/user/getUserByEncodedUrl/id?id=${params.userId}`, {
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(encodedUserActions.getUserSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(encodedUserActions.getUserFailure(errMsg))
            }
        }).catch((error) => {
            dispatch(encodedUserActions.getUserFailure(error.message))
        });
    };
};

export const encodedUserActions = encodeUserSlice.actions;

export default encodeUserSlice.reducer;