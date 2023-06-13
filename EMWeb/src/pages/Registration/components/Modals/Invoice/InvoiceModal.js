import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import Select from "react-select";
import moment from "moment";
import { getUserId } from "../../../../../config";
import "./invoiceModal.scss";
import Backdrop from "./../../../../../components/Backdrop/Backdrop";
import logo from "../../../../../assets/images/logo.png";
import emailIcon from "../../../../../assets/images/Group 2093.svg";
import phoneIcon from "../../../../../assets/images/Group 2092.svg";
import websiteIcon from "../../../../../assets/images/Group 2100.svg";
import locationIcon from "../../../../../assets/images/icon-location.png";
import { emailValidation } from "../../../../../utils/helpers";
import arrow from "../../../../../assets/images/dropdown.svg";
import {
  saveInvoice,
  saveInvoiceAction,
} from "../../../../../store/payment/saveInvoice";
import { getFinanceEmailDetails } from "../../../../../store/payment/financeEmailandPhone";

function InvoiceModal(props) {
  const dispatch = useDispatch();

  const financeData = useSelector(
    (state) => state.financeEmailandPhoneReducer.financeEmailDetails
  );

  const [error, setError] = useState(false);
  const [selectedOptions, setSelectedOptions] = useState([]);

  useEffect(() => {
    dispatch(getFinanceEmailDetails());
  }, [dispatch]);

  const validateForm = () => {
    if (selectedOptions.length !== 0) {
      setError(false);
      return true;
    } else {
      setError(true);
      return false;
    }
  };

  const options = financeData?.map((item) => ({
    value: item.email,
    label: item.email,
  }));

  const handleChange = (e) => {
    setSelectedOptions(e);
  };

  const emailId = selectedOptions.map((item) => {
    return item?.value;
  });
  const handleEmailSend = () => {
    if (validateForm()) {
      props.confirmInvoice();
      const data = {
        amount: props.totalPayment,
        currency: "USD",
        currencySymbol: "$",
        emailSendToFinance: emailId,
        user_id: getUserId(),
      };
      dispatch(saveInvoice(data));
    }
  };

  return (
    <Backdrop>
      <div className="InvoiceContainer">
        <h1 className="upperHeading">Select users for sending the invoice:</h1>
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
              <h2 className="Invoice__text__invoice">INOVICE</h2>
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
        <div className="Invoice__send__email">
          <p>Send Inovice To:</p>
          <div className="invoice_email_select">
            <Select
              classNamePrefix="payment_detail"
              placeholder="Please enter the email"
              className="form_control_country"
              isMulti={true}
              options={options}
              closeMenuOnSelect={false}
              onChange={handleChange}
              noOptionsMessage={(e) => (e.inputValue ? "No options" : null)}
            />
            {error && selectedOptions.length === 0 && (
              <span className="error">Please select an email</span>
            )}
          </div>
        </div>
        <div className="Invoice__send_btn">
          <button className="btn_submit_form" onClick={handleEmailSend}>
            Proceed
          </button>
        </div>
      </div>
      <div className="close_container_charges" onClick={props.cancelInvoice}>
        <span className="close_icon">&times;</span>
      </div>
    </Backdrop>
  );
}

export default InvoiceModal;
