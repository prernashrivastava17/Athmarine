import { createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { apiUrls, getToken, getCompanyId } from "../../config";

const initialState = { invoiceDownload: null, isLoading: false, isError: "" };

const invoiceDownloadSlice = createSlice({
  name: "invoiceDownload",
  initialState,
  reducers: {
    dispatchInvoiceDownloadRequest(state, action) {
      state.isLoading = true;
    },
    dispatchInvoiceDownloadSuccess(state, action) {
      state.isLoading = false;
      state.invoiceDownload = action.payload;
    },
    dispatchInvoiceDownloadFailure(state, action) {
      state.isLoading = false;
      state.isError = action.payload;
    },
    emptyError(state, action) {
      state.isError = "";
    },
  },
});

export const getInvoiceDownload = (data) => {
  return (dispatch) => {
    dispatch(getInvoiceDownloadSlice.dispatchInvoiceDownloadRequest());
    dispatch(getInvoiceDownloadSlice.emptyError());

    fetch(
      `${apiUrls.baseUrl}/payment/downloadInvoiceByCompanyID/${getCompanyId()}`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${getToken()}`,
        },
      }
    )
      .then(function (res) {
        return res.arrayBuffer();
      })
      .then(function (resp) {
        const file = new Blob([resp], { type: "application/pdf" });
        const fileURL = URL.createObjectURL(file);
        // const link = document.createElement("a");
        // link.href = fileURL;
        // link.download = `invoice.pdf`;
        // link.click();
        window.open(fileURL, '_blank');
      });

    // axios({
    //   method: "get",
    //   url: `${
    //     apiUrls.baseUrl
    //   }/payment/downloadInvoiceByCompanyID/${getCompanyId()}`,
    //   headers: {
    //     Authorization: `Bearer ${getToken()}`,
    //   },
    // })
    //   .then((response) => {
    //     if (response && response?.status === 200) {
    //       let blob = new Blob([response.data], {
    //         type: "application/pdf",
    //       });
    //       const link = document.createElement("a");
    //       link.href = window.URL.createObjectURL(blob);
    //       link.download = "invoice.pdf";
    //       link.click();

    //       dispatch(
    //         getInvoiceDownloadSlice.dispatchInvoiceDownloadSuccess(
    //           response.data
    //         )
    //       );
    //     } else {
    //       let errMsg = response.data.responseData.message;
    //       dispatch(
    //         getInvoiceDownloadSlice.dispatchInvoiceDownloadFailure(errMsg)
    //       );
    //     }
    //   })
    //   .catch((error) => {
    //     dispatch(
    //       getInvoiceDownloadSlice.dispatchInvoiceDownloadFailure(error.message)
    //     );
    //   });
  };
};

export const getInvoiceDownloadSlice = invoiceDownloadSlice.actions;
export default invoiceDownloadSlice.reducer;
