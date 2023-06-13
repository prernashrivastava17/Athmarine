import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config';
import axios from 'axios';

const initialAvailableOn = { availableOnRes: null, isLoading: false, isError: '' }

const availableOnSlice = createSlice({
    name: "availableOn",
    initialState: initialAvailableOn,
    reducers: {
        getAvailableOnRequested(state, action) {
            state.isLoading = true;
        },
        getAvailableOnSuccess(state, action) {
            state.isLoading = false;
            state.availableOnRes = action.payload;
        },
        getAvailableOnFailure(state, action) {
            state.isLoading = false;
            state.error = action.payload;
        },
        emptyError(state, action) {
            state.isError = '';
        }
    }
});

export const getAvailableOn = () => {
    return (dispatch) => {
        dispatch(availableOnActions.emptyError())
        dispatch(availableOnActions.getAvailableOnRequested())
        axios.get(`${apiUrls.baseUrl}/availableOn/get`, {
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(availableOnActions.getAvailableOnSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(availableOnActions.getAvailableOnFailure(errMsg))
            }
        }).catch((error) => {
            dispatch(availableOnActions.getAvailableOnFailure(error.message))
        });
    };
};

export const availableOnActions = availableOnSlice.actions;

export default availableOnSlice.reducer;