import { createSlice } from '@reduxjs/toolkit';
import { apiUrls, getAuthToken, getToken } from '../../config';
import axios from 'axios';

const initialCountryState = { getAllEquipmentManufacturerRes: null, isLoading: false, isError: '' }

const getAllEquipManufacturerSlice = createSlice({
    name: 'country',
    initialState: initialCountryState,
    reducers: {
        getAllEquipmenteManufacturerRequested(state, action) {
            state.isLoading = true;
        },
        getAllEquipmenteManufacturerSuccess(state, action) {
            state.isLoading = false;
            state.getAllEquipmentManufacturerRes = action.payload;
        },
        getAllEquipmenteManufacturerFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        }
    }
});

const getTokenFunc = () =>{
    if(getAuthToken()){
        return getAuthToken();
    }
    return getToken();
}


export const getAllEquipmentManufacturer = (id) => {
    return (dispatch) => {
        dispatch(getAllEquipManufacturerActions.getAllEquipmenteManufacturerRequested());
        axios({
            method: 'get',
            url: `${apiUrls.baseUrl}/manufacturer/getEquipmentManufacturerByEquipmentId/${id}`,
            headers: {
                'Authorization': `Bearer ${getTokenFunc()}`,
            },
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(getAllEquipManufacturerActions.getAllEquipmenteManufacturerSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(getAllEquipManufacturerActions.getAllEquipmenteManufacturerFailure(errMsg))
            }
        }).catch((error) => {
            dispatch(getAllEquipManufacturerActions.getAllEquipmenteManufacturerFailure(error.message))
        });
    };
};

export const getAllEquipManufacturerActions = getAllEquipManufacturerSlice.actions;

export default getAllEquipManufacturerSlice.reducer;