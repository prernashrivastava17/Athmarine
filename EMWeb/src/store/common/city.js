import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config';
import axios from 'axios';

const initialcities = { cities: null, isLoading: false, isError: '' }

const citySlice = createSlice({
    name: "city",
    initialState: initialcities,
    reducers: {
        getCityRequested(state, action) {
            state.isLoading = true;
        },
        getCitySuccess(state, action) {
            state.isLoading = false;
            state.cities = action.payload;
        },
        getCityFailure(state, action) {
            state.isLoading = false;
            state.error = action.payload;
        },
        setEmptyCity(state, action) {
            state.cities = null;
        },
        emptyError(state, action) {
            state.isError = '';
        }
    }
});

export const getAllCityByStateId = (id) => {
    return (dispatch) => {
        dispatch(cityActions.emptyError());
        dispatch(cityActions.getCityRequested());
        axios.get(`${apiUrls.baseUrl}/master/getCityListId/${id}`, {
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(cityActions.getCitySuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(cityActions.getCityFailure(errMsg))
            }
        }).catch((error) => {
            dispatch(cityActions.getCityFailure(error.message))
        });
    };
};

export const getAllCityByCountryId = (id) => {
    return (dispatch) => {
        dispatch(cityActions.getCityRequested());
        axios.get(`${apiUrls.baseUrl}/master/getCityListByCountryId/${id}`, {
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(cityActions.getCitySuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(cityActions.getCityFailure(errMsg))
            }
        }).catch((error) => {
            dispatch(cityActions.getCityFailure(error.message))
        });
    };
};

export const cityActions = citySlice.actions;

export default citySlice.reducer;