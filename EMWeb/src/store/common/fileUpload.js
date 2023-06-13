import { createSlice } from '@reduxjs/toolkit';
import { apiUrls, getAuthToken, getToken } from '../../config';
import axios from 'axios';

const initialFileUploadState = { upload: null, isLoading: false, isError: '', index: null };

const uploadSlice = createSlice({
    name: 'upload',
    initialState: initialFileUploadState,
    reducers: {
        getUploadyRequested(state, action) {
            state.isLoading = true;
        },
        getUploadSuccess(state, action) {
            state.isLoading = false;
            state.upload = action.payload;
        },
        getUploadFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = '';
        },
        emptyState(state, action) {
            state.upload = null;
        },
        setCurrentIndex(state, action) {
            state.index = action.payload;
        }
    }
});

const getTokenFunc = () => {
    if (getAuthToken()) {
        return getAuthToken();
    }
    return getToken();
}

export const fileUpload = (data, endPoint, index = null) => {
    return (dispatch) => {
        dispatch(uploadActions.emptyError());
        dispatch(uploadActions.setCurrentIndex(index));
        dispatch(uploadActions.getUploadyRequested());

        axios({
            method: 'post',
            url: `${apiUrls.baseUrl}/${endPoint}`,
            data,
            headers: {
                'Authorization': `Bearer ${getTokenFunc()}`,
            },
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(uploadActions.getUploadSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(uploadActions.getUploadFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(uploadActions.getUploadFailure(error.message));
        });
    };
};

export const uploadActions = uploadSlice.actions;

export default uploadSlice.reducer;