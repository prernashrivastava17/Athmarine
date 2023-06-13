import { createSlice } from '@reduxjs/toolkit';
import { apiUrls } from '../../config';
import axios from 'axios';

const initialCountryState = { getAllEquipmentNameRes: null, isLoading: false, isError: '' }

const getAllEquipmentSlice = createSlice({
    name: 'country',
    initialState: initialCountryState,
    reducers: {
        getAllEquipmenteNameRequested(state, action) {
            state.isLoading = true;
        },
        getAllEquipmenteNameSuccess(state, action) {
            state.isLoading = false;
            state.getAllEquipmentNameRes = action.payload;
        },
        getAllEquipmenteNameFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        }
    }
});

export const getAllEquipmentNameFunc = () => {
    return (dispatch) => {
        dispatch(getAllEquipNameActions.getAllEquipmenteNameRequested())
        axios.get(`${apiUrls.baseUrl}/equipment/getAllEquipmentName`, {})
            .then((response) => {
                if (response && response?.status === 200 && response.data.message === "Success") {
                    dispatch(getAllEquipNameActions.getAllEquipmenteNameSuccess(response.data.responseData));
                } else {
                    let errMsg = response.data.responseData.message;
                    dispatch(getAllEquipNameActions.getAllEquipmenteNameFailure(errMsg))
                }
            }).catch((error) => {
                dispatch(getAllEquipNameActions.getAllEquipmenteNameFailure(error.message))
            });
    };
};

export const getAllEquipNameActions = getAllEquipmentSlice.actions;

export default getAllEquipmentSlice.reducer;