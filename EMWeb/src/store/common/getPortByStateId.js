import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config';
import axios from 'axios';

const initialCountryState = { StatePorts: null, isLoading: false, isError: '' }

const statePortSlice = createSlice({
    name: 'StatePorts',
    initialState: initialCountryState,
    reducers: {
        getStatePortRequested(state, action) {
            state.isLoading = true;
        },
        getStatePortSuccess(state, action) {
            state.isLoading = false;
            state.StatePorts = action.payload;
        },
        getStatePortFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = '';
        }
    }
});

export const getPortsByStateId = (data) => {
    let params = data.map(item => `stateIdList=${item?.id}`)
    let qparams = params.toString().replace(",", "&")


    return (dispatch) => {
        dispatch(statePortActions.emptyError());
        dispatch(statePortActions.getStatePortRequested());

        axios.get(`${apiUrls.baseUrl}/master/getPortsListByStateId?${qparams}`, {
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(statePortActions.getStatePortSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(statePortActions.getStatePortFailure(errMsg))
            }
        }).catch((error) => {
            dispatch(statePortActions.getStatePortFailure(error.message))
        });
    };
};

export const statePortActions = statePortSlice.actions;

export default statePortSlice.reducer;