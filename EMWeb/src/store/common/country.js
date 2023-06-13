import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config';
import axios from 'axios';

const initialCountryState = { countries: null, isLoading: false, isError: '' }

const countrySlice = createSlice({
    name: 'country',
    initialState: initialCountryState,
    reducers: {
        getCountryRequested(state, action) {
            state.isLoading = true;
        },
        getCountrySuccess(state, action) {
            state.isLoading = false;
            state.countries = action.payload;
        },
        getCountryFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = '';
        }
    }
});

export const getCountries = () => {
    return (dispatch) => {
        dispatch(countryActions.emptyError());
        dispatch(countryActions.getCountryRequested());
        axios.get(`${apiUrls.baseUrl}/${apiUrls.path.getCountries}`, {
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(countryActions.getCountrySuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(countryActions.getCountryFailure(errMsg))
            }
        }).catch((error) => {
            dispatch(countryActions.getCountryFailure(error.message))
        });
    };
};

export const countryActions = countrySlice.actions;

export default countrySlice.reducer;