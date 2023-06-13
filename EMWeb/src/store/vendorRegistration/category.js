import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { apiUrls } from '../../config';

const initialState = { categories: null, isLoading: false, isError: '' };

const categorySlice = createSlice({
    name: "category",
    initialState,
    reducers: {
        dispatchCategoryRequest(state, action) {
            state.isLoading = true;
        },
        dispatchCategorySuccess(state, action) {
            state.isLoading = false;
            state.categories = action.payload;
        },
        dispatchCategoryFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        }
    }
});

export const getCategories = () => {
    return (dispatch) => {
        dispatch(getCategorySlice.dispatchCategoryRequest());

        axios({
            method: 'get',
            url: `${apiUrls.baseUrl}/equipment/category/getAllEquipmentCategory`,
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(getCategorySlice.dispatchCategorySuccess(response.data.responseData));

            } else {
                let errMsg = response.data.responseData.message;
                dispatch(getCategorySlice.dispatchCategoryFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(getCategorySlice.dispatchCategoryFailure(error.message));
        });
    };
};

export const getCategorySlice = categorySlice.actions;
export default categorySlice.reducer;