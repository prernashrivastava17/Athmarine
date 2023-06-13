import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { apiUrls } from '../../config';

const initialState = { equipments: null, isLoading: false, isError: '' };

const equipmentSlice = createSlice({
    name: "EquipmentByCategory",
    initialState,
    reducers: {
        dispatchEquipmentRequest(state, action) {
            state.isLoading = true;
        },
        dispatchEquipmentSuccess(state, action) {
            state.isLoading = false;
            state.equipments = action.payload;
        },
        dispatchEquipmentFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = ""
        }
    }
});

export const getEquipmentByCategory = (id) => {
    return (dispatch) => {
        dispatch(getEquipmentSlice.dispatchEquipmentRequest());
        dispatch(getEquipmentSlice.emptyError());

        axios({
            method: 'get',
            url: `${apiUrls.baseUrl}/equipment/getAllEquipmentByCategory/${id}`
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(getEquipmentSlice.dispatchEquipmentSuccess(response.data.responseData));

            } else {
                let errMsg = response.data.responseData.message;
                dispatch(getEquipmentSlice.dispatchEquipmentFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(getEquipmentSlice.dispatchEquipmentFailure(error.message));
        });
    };
};

export const getEquipmentSlice = equipmentSlice.actions;
export default equipmentSlice.reducer;