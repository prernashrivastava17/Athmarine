import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config';
import axios from 'axios';

const initialPromotionDetails = { promotionDetailsRes: null, isLoading: false, isError: '' }

const promotionDetailsSlice = createSlice({
    name: "promotionDetails",
    initialState: initialPromotionDetails,
    reducers: {
        getPromotionDetailsRequested(state, action) {
            state.isLoading = true;
        },
        getPromotionDetailsSuccess(state, action) {
            state.isLoading = false;
            state.promotionDetailsRes = action.payload;
        },
        getPromotionDetailsFailure(state, action) {
            state.isLoading = false;
            state.error = action.payload;
        },
        emptyError(state, action) {
            state.isError = '';
        }
    }
});

export const getPromotionDetails = () => {
    return (dispatch) => {
        dispatch(promotionDetailsActions.emptyError())
        dispatch(promotionDetailsActions.getPromotionDetailsRequested())
        axios.get(`${apiUrls.baseUrl}/masterPromotion/getAllMasterPromotion`, {
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(promotionDetailsActions.getPromotionDetailsSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(promotionDetailsActions.getPromotionDetailsFailure(errMsg))
            }
        }).catch((error) => {
            dispatch(promotionDetailsActions.getPromotionDetailsFailure(error.message))
        });
    };
};

export const promotionDetailsActions = promotionDetailsSlice.actions;

export default promotionDetailsSlice.reducer;