import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config';
import axios from 'axios';

const initialCountryState = { CountryPorts: null, isLoading: false, isError: '' }

const countryPortSlice = createSlice({
    name: 'countryPorts',
    initialState: initialCountryState,
    reducers: {
        getCountryPortRequested(state, action) {
            state.isLoading = true;
        },
        getCountryPortSuccess(state, action) {
            state.isLoading = false;
            state.CountryPorts = action.payload;
        },
        getCountryPortFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = '';
        }
    }
});

export const getPortsByCountryId = (id) => {
    return (dispatch) => {
        dispatch(countryPortActions.emptyError());
        dispatch(countryPortActions.getCountryPortRequested());

        axios.get(`${apiUrls.baseUrl}/master/getPortsListCountryId/${id}`, {
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(countryPortActions.getCountryPortSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(countryPortActions.getCountryPortFailure(errMsg))
            }
        }).catch((error) => {
            dispatch(countryPortActions.getCountryPortFailure(error.message))
        });
    };
};

export const countryPortActions = countryPortSlice.actions;

export default countryPortSlice.reducer;