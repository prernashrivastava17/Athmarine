import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config/index.js';
import axios from 'axios';

const initialCountryState = { states: null, isLoading: false, isError: '' }

const stateSlice = createSlice({
    name: "state",
    initialState: initialCountryState,
    reducers: {
        getStateRequested(state, action) {
            state.isLoading = true;
        },
        getStateSuccess(state, action) {
            state.isLoading = false;
            state.states = action.payload;
        },
        getStateFailure(state, action) {
            state.isLoading = false;
            state.state = action.payload;
        },
        emptyError(state, action) {
            state.isError = '';
        }
    }
});


export const getStates = (id) => {
    return (dispatch) => {
        dispatch(stateActions.emptyError());
        dispatch(stateActions.getStateRequested());

        axios.get(`${apiUrls.baseUrl}/master/getStateListId/${id}`, {
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(stateActions.getStateSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(stateActions.getStateFailure(errMsg))
            }
            dispatch(stateActions.getStateSuccess(response.data.responseData));

        }).catch((error) => {
            dispatch(stateActions.getStateFailure(error.message))
        });
    };
};

export const stateActions = stateSlice.actions;

export default stateSlice.reducer;