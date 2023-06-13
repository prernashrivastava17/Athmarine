import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config';
import axios from 'axios';

const initialDynamicPriceState = { price: null, isLoading: false, isError: '' };

const dynamicPriceSlice = createSlice({
    name: 'getDynamicPrice',
    initialState: initialDynamicPriceState,
    reducers: {
        getDynamicPriceRequested(state, action) {
            state.isLoading = true;
        },
        getDynamicPriceSuccess(state, action) {
            state.isLoading = false;
            state.price = action.payload;
        },
        getDynamicPriceFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = '';
        }
    }
});

export const getDynamicPriceForEngineers = (data, endPoint) => {
    return (dispatch) => {
        dispatch(dynamicPriceActions.emptyError());
        dispatch(dynamicPriceActions.getDynamicPriceRequested());

        axios({
            method: 'get',
            url: `${apiUrls.baseUrl}/${endPoint}`,
            data,
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(dynamicPriceActions.getDynamicPriceSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(dynamicPriceActions.getDynamicPriceFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(dynamicPriceActions.getDynamicPriceFailure(error.message));
        });
    };
};

export const dynamicPriceActions = dynamicPriceSlice.actions;

export default dynamicPriceSlice.reducer;