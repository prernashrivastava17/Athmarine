import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { apiUrls } from '../../config';

const initialState = { DetailsForm: null, isError: '' }

const saveDetailSFormSlice = createSlice({
    name: "saveDetails",
    initialState,
    reducers: {
        saveDetailsForm(state, action) {
            state.DetailsForm = action.payload
        },
        updateEmail(state, action) {
            state.DetailsForm = { ...state.DetailsForm, email: action.payload }
        },
        updateMobile(state, action) {
            state.DetailsForm = {
                ...state.DetailsForm,
                phoneCode: action.payload.phoneCode,
                primaryPhone: action.payload.mobile
            }
        }
    }
});

export const { saveDetailsForm, updateEmail, updateMobile } = saveDetailSFormSlice.actions;

export default saveDetailSFormSlice.reducer;