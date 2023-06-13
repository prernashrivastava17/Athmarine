import React, { useEffect, useState } from "react";
import moment from "moment";
import "./paymentDetailsByInvoice.scss";
import Select from "react-select";
import Backdrop from "./../../../../../components/Backdrop/Backdrop";
import logo from "../../../../../assets/images/logo.png";
import websiteIcon from "../../../../../assets/images/Group 2100.svg";
import phoneIcon from "../../../../../assets/images/Group 2092.svg";
import emailIcon from "../../../../../assets/images/Group 2093.svg";

function PaymentDetailsByInoice(props) {
  return (
    <Backdrop>
      <div className="InvoiceContainer">
        <h1 className="upperHeading">Invoice Preview</h1>
        <div className="Invoice__header_container">
          <div className="Invoice__header">
            <div className="InvoiceHeader__left">
              <img src={logo} alt="logo" />
            </div>
            <div className="InvoiceHeader__right">
              <p
                style={{
                  fontSize: "11px",
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
              <h2 className="Invoice__text__invoice">INVOICE</h2>
              <p>Invoice Number: {props.invoiceNumber}</p>
              <p>Date: {moment().format("DD MMMM YYYY")}</p>
            </div>
          </div>
          <div className="Invoice__details">
            <div className="Invoice__body__left">
              <h4 className="Invoice__text__customer">Customer Details</h4>
              <p>Name: {props.name}</p>
              <p>Address: {props.address}</p>
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
              <p className="Invoice__desc__right">{props.totalEngineer}</p>
            </div>
            <div className="Invoice__total">
              <p className="Invoice__total__left">Total Amount[USD]</p>
              <p className="Invoice__total__right">{props.totalPayment}</p>
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
              <p className="Invoice__total__left">9 Crist Road, NYC Cater</p>
              <p className="Invoice__total__left">Acc No: 321-23421-213</p>
              <p className="Invoice__total__left">SWIFT Code: HASD2SD</p>
            </div>
          </div>
          <div className="Invoice__desc__body">
            <p className="Invoice__desc__left">
              "All bank charges to be borne by Payer"
            </p>
          </div>
        </div>

        <div className="footer">
          <div className="first_point">E. & O. E.</div>
          <div className="second_point">
            ("This is a computer generated invoice, no signature required.")
          </div>
          <div className="horizontal_line">Ath Marine</div>
        </div>
        <div className="Invoice__send_btn">
          <button className="btn_submit_form" onClick={props.ok}>
            Proceed to pay
          </button>
        </div>
      </div>
      <div className="close_container_charge" onClick={props.cancel}>
        <span className="close_icon">&times;</span>
      </div>
    </Backdrop>
  );
}

export default PaymentDetailsByInoice;
