import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import moment from "moment";
import "./invoice.scss";
import logo from "../../../../../assets/images/logo.png";
import emailIcon from "../../../../../assets/images/icon-email.png";
import phoneIcon from "../../../../../assets/images/icon-phone.png";
import websiteIcon from "../../../../../assets/images/Group 2100.svg";
import locationIcon from "../../../../../assets/images/icon-location.png";
import downloadIcon from "../../../../../assets/images/downloadicon.svg";
import Loading from "../../../../../components/Loading/Loading";

import { getPaymentDetails } from "./../../../../../store/vendorRegistration/paymentDetails";
import RegistrationSuccessByInvoice from "../../../../Payment/Modal/RegistrationSuccessByInvoice";
import { getInvoiceDownload } from "../../../../../store/payment/invoiceDownload";

function Invoice(props) {
  const dispatch = useDispatch();

  const [showModal, setShowModal] = useState(false);
  const [showInvoice, setShowInvoice] = useState(true);

  const paymentDetails = useSelector(
    (state) => state.paymentDetailsReducer.paymentDetails
  );
  const isLoading_paymentDetails = useSelector(
    (state) => state.paymentDetailsReducer.isLoading
  );
  const isPaymentDetailsError = useSelector(
    (state) => state.paymentDetailsReducer.isError
  );

  const invoicePdfDownload = useSelector(
    (state) => state.invoiceDownloadReducer.invoiceDownload
  );

  useEffect(() => {
    dispatch(getPaymentDetails());
  }, [dispatch]);

  const handleModal = () => {
    setShowModal(!showModal);
  };
  const onConfirm = () => {
    setShowModal(!showModal);
  };

  const InvoicePdfDownload = () => {
    dispatch(getInvoiceDownload());
  };
  let isFormTypePartner = false,
    isFormTypePartnersIndividual = false,
    isFormTypeCustomer = false;
  switch (props.selectedUser) {
    case "partner":
      isFormTypePartner = true;
      break;
    case "partners-individual":
      isFormTypePartnersIndividual = true;
      break;
    case "customer":
      isFormTypeCustomer = true;
      break;
    default:
      return null;
  }
  return (
    <>
      <div className="invoice_container">
        {isLoading_paymentDetails && <Loading text="Loading..." />}
        {showModal && <RegistrationSuccessByInvoice onConfirm={onConfirm} />}
        <div className="invoice_fetaure">
          {isFormTypePartnersIndividual ? (
            <div className="invoice_details">
              Invoice will be sent to your email ID
            </div>
          ) : (
            <div className="invoice_details">
              Invoice will be sent to selected team members
            </div>
          )}

          <div style={{ height: "47px" }}>
            <button className="invoice_download" onClick={InvoicePdfDownload}>
              Invoice
              <img
                src={downloadIcon}
                alt="logo"
                style={{ width: "20px", marginLeft: "8px" }}
              />
            </button>
          </div>
        </div>

        {showInvoice ? (
          <div className="container">
            <div className="Invoice">
              <div className="Invoice__header_container">
                <div className="Invoice__header">
                  <div className="InvoiceHeader__left">
                    <img src={logo} alt="logo" />
                  </div>
                  <div className="InvoiceHeader__right">
                    <p
                      style={{
                        fontSize: "10px",
                        paddingBottom: "2px",
                        fontFamily: "poppins-regular",
                      }}
                    >
                      Contact
                    </p>
                    <div className="Invoice__info__email">
                      <img src={emailIcon} alt="email" className="icon" />
                      <p>finance@athmarine.com</p>
                    </div>
                    <div className="Invoice__info__phone">
                      <img src={phoneIcon} alt="phone" className="icon" />
                      <p>1-585-928-7030</p>
                    </div>
                    <div className="Invoice__info__phone">
                      <img src={websiteIcon} alt="phone" className="icon" />
                      <p>www.athmarine.com</p>
                    </div>
                  </div>
                </div>
                <div className="horizontal_line"></div>
              </div>
              <div className="map_background">
                <div className="Invoice__body">
                  <div className="Invoice__body__left"></div>
                  <div className="Invoice__body__right">
                    <h2 className="Invoice__text__invoice">INOVICE</h2>
                    <p>Invoice Number: {paymentDetails?.invoiceNumber}</p>
                    <p>Date: {moment().format("DD MMMM YYYY")}</p>
                  </div>
                </div>
                <div className="Invoice__details">
                  <div className="Invoice__body__left">
                    <h4 className="Invoice__text__customer">
                      Customer Details
                    </h4>
                    <p>Name: {paymentDetails?.name}</p>
                    <p>Address: {paymentDetails?.address}</p>
                  </div>
                  <div className="Invoice__body__right"></div>
                </div>
                <div className="Invoice__desc">
                  <h4 className="Invoice__text__description">Description</h4>
                  <div className="Invoice__desc__body">
                    <p className="Invoice__desc__left">Registration Charges</p>
                  </div>
                  <div className="Invoice__desc__body">
                    <p className="Invoice__desc__left">Number of engineers</p>
                    <p className="Invoice__desc__right">
                      {paymentDetails?.totalEngineer}
                    </p>
                  </div>
                  <div className="Invoice__total">
                    <p className="Invoice__total__left">Total Amount[USD]</p>
                    <p className="Invoice__total__right">
                      {paymentDetails?.totalAmount}
                    </p>
                  </div>
                </div>
                <div className="Invoice__desc">
                  <h4 className="Invoice__text__description">
                    Payment in favour of Ath Marine Pte. Ltd.
                  </h4>
                  <div className="Invoice__desc__body bankDetails">
                    <p className="Invoice__desc__left">Remit to:</p>
                    <p className="Invoice__desc__left">
                      United Overseas Bank Limited (UOB)
                    </p>
                    <p className="Invoice__total__left">
                      9 Crist Road, NYC Cater
                    </p>
                    <p className="Invoice__total__left">
                      Acc No: 321-23421-213
                    </p>
                    <p className="Invoice__total__left">SWIFT Code: HASD2SD</p>
                  </div>
                </div>
                <div className="Invoice__desc__body">
                  <p
                    className="Invoice__desc__left"
                    style={{ fontSize: "11px" }}
                  >
                    "All bank charges to be borne by Payer"
                  </p>
                </div>
              </div>

              <div className="footer">
                <div className="first_point">E. & O. E.</div>
                <div className="second_point">
                  ("This is a computer generated invoice, no signature
                  required.")
                </div>
                <div className="horizontal_line">Ath Marine</div>
              </div>
            </div>
          </div>
        ) : (
          <div className="container">
            <div className="Invoice_1">
              <div className="Invoice__header_container_1">
                <div className="InvoiceHeader__left">
                  <img src={logo} alt="logo" />
                </div>
                <div className="horizontal_line"></div>
              </div>
              <div className="map_background_1"></div>
              <div className="footer_1">
                <div className="horizontal_line">Ath Marine</div>
              </div>
            </div>
          </div>
        )}
        {/* <div className="Invoice__dot">
          <input
            type="radio"
            name="first"
            value="1"
            defaultChecked={true}
            onChange={() => setShowInvoice(true)}
          />
          <input
            type="radio"
            name="first"
            value="2"
            onChange={() => setShowInvoice(false)}
          />
        </div> */}

        <div className="Invoice__send">
          <button className="btn_submit_form" onClick={handleModal}>
            Proceed
          </button>
        </div>
      </div>
    </>
  );
}

export default Invoice;
