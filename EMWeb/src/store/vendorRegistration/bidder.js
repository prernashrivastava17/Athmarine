import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config';
import axios from 'axios';

const initialState = { bidder: null, isLoading: false, isError: '', navigationCheck: false };

const BidderSlice = createSlice({
    name: 'bidder',
    initialState,
    reducers: {
        saveBidderRequested(state, action) {
            state.isLoading = true;
        },
        saveBidderSuccess(state, action) {
            state.isLoading = false;
            state.bidder = action.payload;
            state.navigationCheck = true;
        },
        saveBidderFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
            state.navigationCheck = false;
        },
        emptyError(state, action) {
            state.isError = "";
        },
        bidderNavigation(state, action) {
            state.navigationCheck = action.payload;
        },
    }
});

export const saveBidders = (data, endPoint) => {
    return (dispatch) => {
        dispatch(saveBidderSlice.saveBidderRequested());
        dispatch(saveBidderSlice.emptyError());

        axios({
            method: 'post',
            url: `${apiUrls.baseUrl}/${endPoint}`,
            data
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(saveBidderSlice.saveBidderSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(saveBidderSlice.saveBidderFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(saveBidderSlice.saveBidderFailure(error.message));
        });
    };
};

export const saveBidderSlice = BidderSlice.actions;

export default BidderSlice.reducer;