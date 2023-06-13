import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { ToastError } from '../../components/Tostify';

const initialValidateCardState = { state: null, isLoading: false, isError: '' };

const validateCardSlice = createSlice({
    name: 'validateCard',
    initialState: initialValidateCardState,
    reducers: {
        saveValidateCardRequested(state, action) {
            state.isLoading = true;
        },
        saveValidateCardSuccess(state, action) {
            state.isLoading = false;
            state.state = action.payload;
        },
        saveValidateCardFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = '';
        }
    }
});

export const validateCardForPayment = (data, endPoint) => {
    return (dispatch) => {
        dispatch(validateCardActions.emptyError());
        dispatch(validateCardActions.saveValidateCardRequested());

        axios({
            method: 'POST',
            url: `${endPoint}`,
            data,
            headers: {
                Accept: 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded',
                Authorization: `Bearer pk_test_51JWmGRHbXZa50bvbC68XKlwHpzresALIy8NT9XwVzktVmi0SliS4olhkzH0jphmtbzGOhD4xp2q6jcVaHNHZirrb00ZD6tiiGo`
            },
        }).then((response) => {
            if (response && response?.status === 200) {
                dispatch(validateCardActions.saveValidateCardSuccess(response.data));
            } else {
                let errMsg = response.data.error.message;
                dispatch(validateCardActions.saveValidateCardFailure(errMsg));
            }
        }).catch((error) => {
            ToastError(`${error.response.data.error.message}`);
            dispatch(validateCardActions.saveValidateCardFailure(error.response.data.error.message));
        });
    };
};

export const validateCardActions = validateCardSlice.actions;

export default validateCardSlice.reducer;