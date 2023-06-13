import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config';
import axios from 'axios';

const initialCountryState = { getAllEquipmentRes: null, isLoading: false, isError: '' }

const getAllEquipmentSlice = createSlice({
    name: 'country',
    initialState: initialCountryState,
    reducers: {
        getAllEquipmenteRequested(state, action) {
            state.isLoading = true;
        },
        getAllEquipmenteSuccess(state, action) {
            state.isLoading = false;
            state.getAllEquipmentRes = action.payload;
        },
        getAllEquipmenteFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        }
    }
});

export const getAllEquipmentCategory = () => {
    return (dispatch) => {
        dispatch(getAllEquipmentActions.getAllEquipmenteRequested());

        axios.get(`${apiUrls.baseUrl}/equipment/category/getAllEquipmentCategory`, {
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(getAllEquipmentActions.getAllEquipmenteSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(getAllEquipmentActions.getAllEquipmenteFailure(errMsg))
            }
        }).catch((error) => {
            dispatch(getAllEquipmentActions.getAllEquipmenteFailure(error.message))
        });
    };
};

export const getAllEquipmentActions = getAllEquipmentSlice.actions;

export default getAllEquipmentSlice.reducer;