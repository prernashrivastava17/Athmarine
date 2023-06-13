import { createSlice } from '@reduxjs/toolkit';
import { apiUrls, getAuthData, getCompanyId } from '../../config';
import axios from 'axios';

const initialState = { approverRes: null, biddersList: null, requesterList: null, isLoading: false, isError: '' , navigationCheck: false};

const ApproverSlice = createSlice({
    name: 'bidder',
    initialState,
    reducers: {
        saveApproverRequested(state, action) {
            state.isLoading = true;
        },
        saveApproverSuccess(state, action) {
            state.isLoading = false;
            state.approverRes = action.payload;
            state.navigationCheck=true;
        },
        saveApproverFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
            state.navigationCheck=false;
        },


        getBidderListRequested(state, action) {
            state.isLoading = true;
        },
        getBidderListSuccess(state, action) {
            state.isLoading = false;
            state.biddersList = action.payload;
        },
        getBidderListFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },


        getRequesterListRequested(state, action) {
            state.isLoading = true;
        },
        getRequesterListSuccess(state, action) {
            state.isLoading = false;
            state.requesterList = action.payload;
        },
        getRequesterListFailure(state, action) {
            state.isLoading = false;
            state.isError = action.payload;
        },
        emptyError(state, action) {
            state.isError = ""
        },
        approverNavigation(state,action) {
            state.navigationCheck=action.payload;
        },
    }
});

const getCompanyIdCheck = () => {
    if (getAuthData()?.companyId) {
      return getAuthData()?.companyId;
    }
    return getCompanyId();
  };

export const saveApprovers = (data, endPoint) => {
    return (dispatch) => {
        dispatch(saveApproverSlice.saveApproverRequested());
        dispatch(saveApproverSlice.emptyError());

        axios({
            method: 'post',
            url: `${apiUrls.baseUrl}/${endPoint}`,
            data,
        }).then((response) => {
            if (response && response?.status === 200 && response.data.message === "Success") {
                dispatch(saveApproverSlice.saveApproverSuccess(response.data.responseData));
            } else {
                let errMsg = response.data.responseData.message;
                dispatch(saveApproverSlice.saveApproverFailure(errMsg));
            }
        }).catch((error) => {
            dispatch(saveApproverSlice.saveApproverFailure(error.message));
        });
    };
};

export const getBiddersList = (data) => {
    return (dispatch) => {
        dispatch(saveApproverSlice.getBidderListRequested());

        axios.get(`${apiUrls.baseUrl}/vendor/getAllVendorBidder/${getCompanyIdCheck()}`, {})
            .then((response) => {
                if (response && response?.status === 200 && response.data.message === "Success") {
                    dispatch(saveApproverSlice.getBidderListSuccess(response.data.responseData));
                } else {
                    let errMsg = response.data.responseData.message;
                    dispatch(saveApproverSlice.getBidderListFailure(errMsg));
                }
            }).catch((error) => {
                dispatch(saveApproverSlice.getBidderListFailure(error.message));
            });
    };
};

export const getRequesterList = (data) => {
    return (dispatch) => {
        dispatch(saveApproverSlice.getRequesterListRequested());

        axios.get(`${apiUrls.baseUrl}/customer/getAllRequester/${getCompanyId()}`, {})
            .then((response) => {
                if (response && response?.status === 200 && response.data.message === "Success") {
                    dispatch(saveApproverSlice.getRequesterListSuccess(response.data.responseData));
                } else {
                    let errMsg = response.data.responseData.message;
                    dispatch(saveApproverSlice.getRequesterListFailure(errMsg));
                }
            }).catch((error) => {
                dispatch(saveApproverSlice.getRequesterListFailure(error.message));
            });
    };
};

export const saveApproverSlice = ApproverSlice.actions;

export default ApproverSlice.reducer;