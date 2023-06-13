import React from "react";
import "./PaymentSlip.scss";
import logo from "../../../assets/images/logo.png";
import visa from "./../../../assets/images/visa.svg";
import mastercard from "./../../../assets/images/mastercard.svg";
import amex from "./../../../assets/images/amex.svg";
import jcb from "./../../../assets/images/jcb.svg";
import discover from "./../../../assets/images/discover.svg";
import unionpay from "./../../../assets/images/union-pay.svg";
import diners from "./../../../assets/images/diner.svg";
import cardcvv from "./../../../assets/images/card-cvv.png";

import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getPaymentSlip } from "../../../store/payment/paymentSlip";
import RegistrationSuccessFull from "../Modal/RegistrationSuccessFull";
import { useHistory } from "react-router-dom";
import moment from "moment";

function PaymentSlip() {
  const [isConfirmationModal, setIsConfirmationModal] = useState(false);
  const [cardType, setCardType] = useState(``);

  const dispatch = useDispatch();
  const history = useHistory();
  const paymentData = useSelector(
    (state) => state.paymentSlipReducer.paymentSlip
  );
  const isPaymentDataLoading = useSelector(
    (state) => state.paymentSlipReducer.isLoading
  );
  const isPaymentDataError = useSelector(
    (state) => state.paymentSlipReducer.isError
  );

  useEffect(() => {
    dispatch(getPaymentSlip());
  }, [dispatch]);

  const changeModal = () => {
    setIsConfirmationModal(!isConfirmationModal);
  };
  const redirectHandler = () => {
    window.location.href = "/";
  };

  function detectCardType(number) {
    var re = {
      electron: /^(4026|417500|4405|4508|4844|4913|4917)\d+$/,
      maestro:
        /^(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\d+$/,
      dankort: /^(5019)\d+$/,
      interpayment: /^(636)\d+$/,
      unionpay: /^(62|88)\d+$/,
      visa: /^4[0-9]{12}(?:[0-9]{3})?$/,
      mastercard: /^5[1-5][0-9]{14}$/,
      amex: /^3[47][0-9]{13}$/,
      diners: /^3(?:0[0-5]|[68][0-9])[0-9]{11}$/,
      discover: /^6(?:011|5[0-9]{2})[0-9]{12}$/,
      jcb: /^(?:2131|1800|35\d{3})\d{11}$/,
    };

    for (var key in re) {
      if (re[key].test(number)) {
        return key;
      }
    }
  }
  useEffect(() => {
    setCardType(
      detectCardType(`${paymentData?.atmCardNumber}`.replaceAll(" ", ""))
    );
  });

  const selectCardByCardTypeHandler = () => {
    switch (cardType) {
      case "unionpay":
        return (
          <div className="formFieldInputsIconspay-icon">
            <img src={unionpay} alt="unionpay" />
          </div>
        );

      case "jcb":
        return (
          <div className="formFieldInputsIconspay-icon">
            <img src={jcb} alt="jcb" />
          </div>
        );

      case "visa":
        return (
          <div className="formFieldInputsIconspay-icon">
            <img src={visa} alt="visa" />
          </div>
        );

      case "mastercard":
        return (
          <div className="formFieldInputsIconspay-icon">
            <img src={mastercard} alt="mastercard" />
          </div>
        );

      case "discover":
        return (
          <div className="formFieldInputsIconspay-icon">
            <img src={discover} alt="discover" />
          </div>
        );

      case "amex":
        return (
          <div className="formFieldInputsIconspay-icon">
            <img src={amex} alt="amex" />
          </div>
        );

      case "electron":
        break;

      case "maestro":
        break;

      case "interpayment":
        break;

      default:
        return "";
    }
  };

  return (
    <>
      {isConfirmationModal && (
        <RegistrationSuccessFull redirect={redirectHandler} />
      )}
      <div classname="section_modalPaymentSlip">
        <table
          className="st-Background"
          bgcolor="#f6f9fc"
          border={0}
          cellPadding={0}
          cellSpacing={0}
          width="100%"
          style={{ border: 0, margin: 0, padding: 0 }}
        >
          <tbody>
            <tr>
              <td style={{ border: 0, margin: 0, padding: 0 }}>
                <table
                  className="st-Wrapper"
                  align="center"
                  bgcolor="#ffffff"
                  border={0}
                  cellPadding={0}
                  cellSpacing={0}
                  width={600}
                  style={{
                    borderBottomLeftRadius: "5px",
                    borderBottomRightRadius: "5px",
                    margin: "0 auto",
                    minWidth: "600px",
                  }}
                >
                  <tbody>
                    <tr>
                      <td style={{ border: 0, margin: 0, padding: 0 }}>
                        <table
                          className="st-Preheader st-Width st-Width--mobile"
                          border={0}
                          cellPadding={0}
                          cellSpacing={0}
                          width={600}
                          style={{ minWidth: "600px" }}
                        >
                          <tbody>
                            <tr>
                              <td
                                align="center"
                                height={0}
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  color: "#ffffff",
                                  display: "none !important",
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  maxHeight: 0,
                                  maxWidth: 0,
                                  msoHide: "all !important",
                                  opacity: 0,
                                  overflow: "hidden",
                                  visibility: "hidden",
                                }}
                              >
                                <span
                                  className="st-Delink st-Delink--preheader"
                                  style={{
                                    color: "#ffffff",
                                    textDecoration: "none",
                                  }}
                                >
                                  Receipt from ATH MARINE PTE LTD [#1705-2233]
                                  Amount paid {paymentData?.currencySymbol}
                                  {paymentData?.amount} Date paid
                                  {paymentData?.transactionLocalDate}
                                  {/* Prevents elements showing up in email client preheader text */}
                                  ‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;‌&nbsp;
                                  {/* /Prevents elements showing up in email client preheader text */}
                                </span>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                        <div
                          style={{
                            backgroundColor: "#f6f9fc",
                            paddingTop: "20px",
                          }}
                        >
                          <table
                            dir="ltr"
                            className="Section Header"
                            width="100%"
                            style={{
                              border: 0,
                              borderCollapse: "collapse",
                              margin: 0,
                              padding: 0,
                              backgroundColor: "#ffffff",
                            }}
                          >
                            <tbody>
                              <tr>
                                <td
                                  className="Header-left Target"
                                  style={{
                                    backgroundColor: "#4F2729",
                                    border: 0,
                                    borderCollapse: "collapse",
                                    margin: 0,
                                    padding: 0,
                                    WebkitFontSmoothing: "antialiased",
                                    MozOsxFontSmoothing: "grayscale",
                                    fontSize: 0,
                                    lineHeight: "0px",
                                    msoLineHeightRule: "exactly",
                                    backgroundSize: "100% 100%",
                                    borderTopLeftRadius: "5px",
                                  }}
                                  align="right"
                                  height={156}
                                  valign="bottom"
                                  width={252}
                                >
                                  <a
                                    href="http://www.athmarine.com"
                                    target="_blank"
                                    style={{
                                      WebkitFontSmoothing: "antialiased",
                                      MozOsxFontSmoothing: "grayscale",
                                      outline: 0,
                                      textDecoration: "none",
                                    }}
                                    rel="noreferrer"
                                  >
                                    <img
                                      alt=""
                                      height={156}
                                      width={252}
                                      src="https://stripe-images.s3.amazonaws.com/notifications/hosted/20180110/Header/Left.png"
                                      style={{
                                        display: "block",
                                        border: 0,
                                        lineHeight: "100%",
                                        width: "100%",
                                      }}
                                    />
                                  </a>
                                </td>
                                <td
                                  className="Header-icon Target"
                                  style={{
                                    backgroundColor: "#4F2729",
                                    border: 0,
                                    borderCollapse: "collapse",
                                    margin: 0,
                                    padding: 0,
                                    WebkitFontSmoothing: "antialiased",
                                    MozOsxFontSmoothing: "grayscale",
                                    fontSize: 0,
                                    lineHeight: "0px",
                                    msoLineHeightRule: "exactly",
                                    backgroundSize: "100% 100%",
                                    width: "96px !important",
                                  }}
                                  align="center"
                                  height={156}
                                  valign="bottom"
                                >
                                  <a
                                    href="http://www.athmarine.com"
                                    target="_blank"
                                    style={{
                                      WebkitFontSmoothing: "antialiased",
                                      MozOsxFontSmoothing: "grayscale",
                                      outline: 0,
                                      textDecoration: "none",
                                    }}
                                    rel="noreferrer"
                                  >
                                    <img
                                      alt=""
                                      height={156}
                                      width={96}
                                      src="https://stripe-images.s3.amazonaws.com/notifications/hosted/20180110/Header/Icon--empty.png"
                                      style={{
                                        display: "block",
                                        border: 0,
                                        lineHeight: "100%",
                                      }}
                                    />
                                  </a>
                                </td>
                                <td
                                  className="Header-right Target"
                                  style={{
                                    backgroundColor: "#4F2729",
                                    border: 0,
                                    borderCollapse: "collapse",
                                    margin: 0,
                                    padding: 0,
                                    WebkitFontSmoothing: "antialiased",
                                    MozOsxFontSmoothing: "grayscale",
                                    fontSize: 0,
                                    lineHeight: "0px",
                                    msoLineHeightRule: "exactly",
                                    backgroundSize: "100% 100%",
                                    borderTopRightRadius: "5px",
                                  }}
                                  align="left"
                                  height={156}
                                  valign="bottom"
                                  width={252}
                                >
                                  <a
                                    href="http://www.athmarine.com"
                                    target="_blank"
                                    style={{
                                      WebkitFontSmoothing: "antialiased",
                                      MozOsxFontSmoothing: "grayscale",
                                      outline: 0,
                                      textDecoration: "none",
                                    }}
                                    rel="noreferrer"
                                  >
                                    <img
                                      alt=""
                                      height={156}
                                      width={252}
                                      src="https://stripe-images.s3.amazonaws.com/notifications/hosted/20180110/Header/Right.png"
                                      style={{
                                        display: "block",
                                        border: 0,
                                        lineHeight: "100%",
                                        width: "100%",
                                      }}
                                    />
                                  </a>
                                </td>
                              </tr>
                            </tbody>
                          </table>
                        </div>

                        <table
                          className="st-Copy st-Copy--caption st-Width st-Width--mobile"
                          border={0}
                          cellPadding={0}
                          cellSpacing={0}
                          width={600}
                          style={{ minWidth: "600px" }}
                        >
                          <tbody>
                            <tr>
                              <td
                                align="center"
                                style={{ verticalAlign: "middle" }}
                              >
                                <img
                                  style={{
                                    heiht: "25px",
                                    width: "125px",
                                    verticalAlign: "middle",
                                    marginTop: "-29px",
                                  }}
                                  src={logo}
                                  alt="logo"
                                />
                              </td>
                            </tr>
                            <tr>
                              <td
                                className="Content Title-copy Font Font--title"
                                align="center"
                                style={{
                                  border: 0,
                                  borderCollapse: "collapse",
                                  margin: 0,
                                  padding: 0,
                                  WebkitFontSmoothing: "antialiased",
                                  MozOsxFontSmoothing: "grayscale",
                                  width: "472px",
                                  fontFamily:
                                    '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                  msoLineHeightRule: "exactly",
                                  verticalAlign: "middle",
                                  color: "#4F2729",
                                  fontSize: "24px",
                                  lineHeight: "32px",
                                }}
                              >
                                Receipt from ATH MARINE PTE LTD
                              </td>
                            </tr>
                            <tr>
                              <td
                                className="st-Spacer st-Spacer--stacked"
                                colSpan={3}
                                height={12}
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                        <table
                          className="st-Copy st-Copy--caption st-Width st-Width--mobile"
                          border={0}
                          cellPadding={0}
                          cellSpacing={0}
                          width={600}
                          style={{ minWidth: "600px" }}
                        >
                          <tbody>
                            <tr>
                              <td
                                className="Content Title-copy Font Font--title"
                                align="center"
                                style={{
                                  border: 0,
                                  borderCollapse: "collapse",
                                  margin: 0,
                                  padding: 0,
                                  WebkitFontSmoothing: "antialiased",
                                  MozOsxFontSmoothing: "grayscale",
                                  width: "472px",
                                  fontFamily:
                                    '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                  msoLineHeightRule: "exactly",
                                  verticalAlign: "middle",
                                  color: "#4F2729",
                                  fontSize: "15px",
                                  lineHeight: "18px",
                                  fontWeight: "bold",
                                }}
                              >
                                Receipt #1705-2233
                              </td>
                            </tr>
                            <tr>
                              <td
                                className="st-Spacer st-Spacer--stacked"
                                colSpan={3}
                                height={12}
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                        <table
                          className="st-Spacer st-Spacer--standalone st-Width st-Width--mobile"
                          border={0}
                          cellPadding={0}
                          cellSpacing={0}
                          width="100%"
                        >
                          <tbody>
                            <tr>
                              <td
                                height={20}
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  maxHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                        <table
                          className="st-Copy st-Copy--standalone st-Copy--caption"
                          border={0}
                          cellPadding={0}
                          cellSpacing={0}
                          width="100%"
                        >
                          <tbody>
                            <tr>
                              <td
                                className="st-Font st-Font--caption"
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  color: "#4F2729",
                                  fontFamily:
                                    '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                  fontSize: "12px",
                                  fontWeight: "bold",
                                  lineHeight: "16px",
                                  textTransform: "uppercase",
                                }}
                              ></td>
                              <td
                                width={64}
                                style={{
                                  border: 0,
                                  borderCollapse: "collapse",
                                  margin: 0,
                                  padding: 0,
                                  WebkitFontSmoothing: "antialiased",
                                  MozOsxFontSmoothing: "grayscale",
                                  color: "#ffffff",
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                &nbsp;
                              </td>
                              <td
                                className="DataBlocks-item"
                                valign="top"
                                style={{
                                  border: 0,
                                  borderCollapse: "collapse",
                                  margin: 0,
                                  padding: 0,
                                  WebkitFontSmoothing: "antialiased",
                                  MozOsxFontSmoothing: "grayscale",
                                }}
                              >
                                <table
                                  style={{
                                    border: 0,
                                    borderCollapse: "collapse",
                                    margin: 0,
                                    padding: 0,
                                  }}
                                >
                                  <tbody>
                                    <tr>
                                      <td
                                        className="Font Font--caption Font--uppercase Font--mute Font--noWrap"
                                        style={{
                                          border: 0,
                                          borderCollapse: "collapse",
                                          margin: 0,
                                          padding: 0,
                                          WebkitFontSmoothing: "antialiased",
                                          MozOsxFontSmoothing: "grayscale",
                                          fontFamily:
                                            '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                          msoLineHeightRule: "exactly",
                                          verticalAlign: "middle",
                                          color: "#4F2729",
                                          fontSize: "12px",
                                          lineHeight: "16px",
                                          whiteSpace: "nowrap",
                                          fontWeight: "bold",
                                          textTransform: "uppercase",
                                        }}
                                      >
                                        Amount paid
                                      </td>
                                    </tr>
                                    <tr>
                                      <td
                                        className="Font Font--body Font--noWrap"
                                        style={{
                                          border: 0,
                                          borderCollapse: "collapse",
                                          margin: 0,
                                          padding: 0,
                                          WebkitFontSmoothing: "antialiased",
                                          MozOsxFontSmoothing: "grayscale",
                                          fontFamily:
                                            '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                          msoLineHeightRule: "exactly",
                                          verticalAlign: "middle",
                                          color: "#4F2729",
                                          fontSize: "15px",
                                          lineHeight: "24px",
                                          whiteSpace: "nowrap",
                                        }}
                                      >
                                        {paymentData?.currencySymbol}
                                        {paymentData?.amount}
                                      </td>
                                    </tr>
                                  </tbody>
                                </table>
                              </td>
                              <td
                                width={20}
                                style={{
                                  border: 0,
                                  borderCollapse: "collapse",
                                  margin: 0,
                                  padding: 0,
                                  WebkitFontSmoothing: "antialiased",
                                  MozOsxFontSmoothing: "grayscale",
                                  color: "#ffffff",
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                &nbsp;
                              </td>
                              <td
                                className="DataBlocks-item"
                                valign="top"
                                style={{
                                  border: 0,
                                  borderCollapse: "collapse",
                                  margin: 0,
                                  padding: 0,
                                  WebkitFontSmoothing: "antialiased",
                                  MozOsxFontSmoothing: "grayscale",
                                }}
                              >
                                <table
                                  style={{
                                    border: 0,
                                    borderCollapse: "collapse",
                                    margin: 0,
                                    padding: 0,
                                  }}
                                >
                                  <tbody>
                                    <tr>
                                      <td
                                        className="Font Font--caption Font--uppercase Font--mute Font--noWrap"
                                        style={{
                                          border: 0,
                                          borderCollapse: "collapse",
                                          margin: 0,
                                          padding: 0,
                                          WebkitFontSmoothing: "antialiased",
                                          MozOsxFontSmoothing: "grayscale",
                                          fontFamily:
                                            '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                          msoLineHeightRule: "exactly",
                                          verticalAlign: "middle",
                                          color: "#4F2729",
                                          fontSize: "12px",
                                          lineHeight: "16px",
                                          whiteSpace: "nowrap",
                                          fontWeight: "bold",
                                          textTransform: "uppercase",
                                        }}
                                      >
                                        Date paid
                                      </td>
                                    </tr>
                                    <tr>
                                      <td
                                        className="Font Font--body Font--noWrap"
                                        style={{
                                          border: 0,
                                          borderCollapse: "collapse",
                                          margin: 0,
                                          padding: 0,
                                          WebkitFontSmoothing: "antialiased",
                                          MozOsxFontSmoothing: "grayscale",
                                          fontFamily:
                                            '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                          msoLineHeightRule: "exactly",
                                          verticalAlign: "middle",
                                          color: "#4F2729",
                                          fontSize: "15px",
                                          lineHeight: "24px",
                                          whiteSpace: "nowrap",
                                        }}
                                      >
                                        {moment(
                                          paymentData?.transactionLocalDate
                                        ).format("Do MMMM YYYY")}
                                      </td>
                                    </tr>
                                  </tbody>
                                </table>
                              </td>
                              <td
                                width={20}
                                style={{
                                  border: 0,
                                  borderCollapse: "collapse",
                                  margin: 0,
                                  padding: 0,
                                  WebkitFontSmoothing: "antialiased",
                                  MozOsxFontSmoothing: "grayscale",
                                  color: "#ffffff",
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                &nbsp;
                              </td>
                              <td
                                className="DataBlocks-item"
                                valign="top"
                                style={{
                                  border: 0,
                                  borderCollapse: "collapse",
                                  margin: 0,
                                  padding: 0,
                                  WebkitFontSmoothing: "antialiased",
                                  MozOsxFontSmoothing: "grayscale",
                                }}
                              >
                                <table
                                  style={{
                                    border: 0,
                                    borderCollapse: "collapse",
                                    margin: 0,
                                    padding: 0,
                                  }}
                                >
                                  <tbody>
                                    <tr>
                                      <td
                                        className="Font Font--caption Font--uppercase Font--mute Font--noWrap"
                                        style={{
                                          border: 0,
                                          borderCollapse: "collapse",
                                          margin: 0,
                                          padding: 0,
                                          WebkitFontSmoothing: "antialiased",
                                          MozOsxFontSmoothing: "grayscale",
                                          fontFamily:
                                            '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                          msoLineHeightRule: "exactly",
                                          verticalAlign: "middle",
                                          color: "#4F2729",
                                          fontSize: "12px",
                                          lineHeight: "16px",
                                          whiteSpace: "nowrap",
                                          fontWeight: "bold",
                                          textTransform: "uppercase",
                                        }}
                                      >
                                        Payment method
                                      </td>
                                    </tr>
                                    <tr>
                                      <td
                                        className="Font Font--body Font--noWrap"
                                        style={{
                                          border: 0,
                                          borderCollapse: "collapse",
                                          margin: 0,
                                          padding: 0,
                                          WebkitFontSmoothing: "antialiased",
                                          MozOsxFontSmoothing: "grayscale",
                                          fontFamily:
                                            '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                          msoLineHeightRule: "exactly",
                                          verticalAlign: "middle",
                                          color: "#4F2729",
                                          fontSize: "15px",
                                          lineHeight: "24px",
                                          whiteSpace: "nowrap",
                                          display: "flex",
                                        }}
                                      >
                                        {cardType !== `` ? (
                                          selectCardByCardTypeHandler()
                                        ) : (
                                          <>
                                            <div className="formFieldInputsIconspay-icon">
                                              <img src={visa} alt="visa" />
                                            </div>
                                            <div className="formFieldInputsIconspay-icon">
                                              <img
                                                src={mastercard}
                                                alt="mastercard"
                                              />
                                            </div>
                                            <div className="formFieldInputsIconspay-icon">
                                              <img src={amex} alt="amex" />
                                            </div>
                                            <div className="formFieldInputsIconspay-icon">
                                              <img src={jcb} alt="jcb" />
                                            </div>
                                          </>
                                        )}

                                        <span>
                                          -
                                          {paymentData?.atmCardNumber.substr(
                                            -4
                                          )}
                                        </span>
                                      </td>
                                    </tr>
                                  </tbody>
                                </table>
                              </td>
                              <td
                                width={64}
                                style={{
                                  border: 0,
                                  borderCollapse: "collapse",
                                  margin: 0,
                                  padding: 0,
                                  WebkitFontSmoothing: "antialiased",
                                  MozOsxFontSmoothing: "grayscale",
                                  color: "#ffffff",
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                &nbsp;
                              </td>
                            </tr>
                          </tbody>
                        </table>
                        <table
                          className="st-Spacer st-Spacer--standalone st-Width st-Width--mobile"
                          border={0}
                          cellPadding={0}
                          cellSpacing={0}
                          width="100%"
                        >
                          <tbody>
                            <tr>
                              <td
                                height={32}
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  maxHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                        <table
                          className="st-Copy st-Copy--caption st-Width st-Width--mobile"
                          border={0}
                          cellPadding={0}
                          cellSpacing={0}
                          width={600}
                          style={{ minWidth: "600px" }}
                        >
                          <tbody>
                            <tr>
                              <td
                                className="st-Spacer st-Spacer--gutter"
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                                width={64}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                              <td
                                className="st-Font st-Font--caption"
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  color: "#4F2729",
                                  fontFamily:
                                    '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                  fontSize: "12px",
                                  lineHeight: "16px",
                                  textTransform: "uppercase",
                                }}
                              >
                                <span
                                  className="st-Delink"
                                  style={{
                                    border: 0,
                                    margin: 0,
                                    padding: 0,
                                    fontWeight: "bold",
                                  }}
                                >
                                  Summary
                                </span>
                              </td>
                              <td
                                className="st-Spacer st-Spacer--gutter"
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                                width={64}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                            </tr>
                            <tr>
                              <td
                                className="st-Spacer st-Spacer--stacked"
                                colSpan={3}
                                height={12}
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                        <table
                          className="st-Blocks st-Width st-Width--mobile"
                          border={0}
                          cellPadding={0}
                          cellSpacing={0}
                          width={600}
                          style={{ minWidth: "600px" }}
                        >
                          <tbody>
                            <tr>
                              <td
                                className="st-Spacer st-Spacer--stacked"
                                colSpan={3}
                                height={4}
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                            </tr>
                            <tr>
                              <td
                                className="st-Spacer st-Spacer--kill"
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                                width={64}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                              <td style={{ border: 0, margin: 0, padding: 0 }}>
                                <table
                                  className="st-Blocks-inner"
                                  bgcolor="#f6f9fc"
                                  border={0}
                                  cellPadding={0}
                                  cellSpacing={0}
                                  style={{
                                    borderRadius: "5px",
                                    backgroundColor: "rgb(79, 39, 41, 0.1)",
                                  }}
                                  width="100%"
                                >
                                  <tbody>
                                    <tr>
                                      <td
                                        style={{
                                          border: 0,
                                          margin: 0,
                                          padding: 0,
                                        }}
                                      >
                                        <table
                                          className="st-Blocks-item"
                                          border={0}
                                          cellPadding={0}
                                          cellSpacing={0}
                                          width="100%"
                                        >
                                          <tbody>
                                            <tr>
                                              <td
                                                className="st-Spacer st-Spacer--blocksItemEnds"
                                                colSpan={3}
                                                height={12}
                                                style={{
                                                  border: 0,
                                                  margin: 0,
                                                  padding: 0,
                                                  fontSize: "1px",
                                                  lineHeight: "1px",
                                                  msoLineHeightRule: "exactly",
                                                }}
                                              >
                                                <div className="st-Spacer st-Spacer--filler">
                                                  &nbsp;
                                                </div>
                                              </td>
                                            </tr>
                                            <tr>
                                              <td
                                                className="st-Spacer st-Spacer--gutter"
                                                style={{
                                                  border: 0,
                                                  color: "#4F2729",
                                                  margin: 0,
                                                  padding: 0,
                                                  fontSize: "1px",
                                                  lineHeight: "1px",
                                                  msoLineHeightRule: "exactly",
                                                }}
                                                width={16}
                                              >
                                                <div className="st-Spacer st-Spacer--filler">
                                                  &nbsp;
                                                </div>
                                              </td>
                                              <td
                                                className="st-Blocks-item-cell st-Font st-Font--body"
                                                style={{
                                                  border: 0,
                                                  margin: 0,
                                                  padding: 0,
                                                  color: "#4F2729",
                                                  fontFamily:
                                                    '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                                  fontSize: "16px",
                                                  lineHeight: "24px",
                                                }}
                                              >
                                                <table
                                                  style={{
                                                    paddingLeft: "5px",
                                                    paddingRight: "5px",
                                                  }}
                                                  width="100%"
                                                >
                                                  <tbody>
                                                    <tr>
                                                      <td></td>
                                                    </tr>
                                                    <tr>
                                                      <td
                                                        className="Table-description Font Font--body"
                                                        style={{
                                                          border: 0,
                                                          borderCollapse:
                                                            "collapse",
                                                          margin: 0,
                                                          padding: 0,
                                                          WebkitFontSmoothing:
                                                            "antialiased",
                                                          MozOsxFontSmoothing:
                                                            "grayscale",
                                                          fontFamily:
                                                            '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                                          msoLineHeightRule:
                                                            "exactly",
                                                          verticalAlign:
                                                            "middle",
                                                          color: "#4F2729",
                                                          fontSize: "15px",
                                                          lineHeight: "24px",
                                                          width: "100%",
                                                          // opacity:
                                                          //   "100% !important",
                                                        }}
                                                      >
                                                        Payment to ATH MARINE
                                                        PTE LTD
                                                      </td>
                                                      <td
                                                        className="Spacer Table-gap"
                                                        width={8}
                                                        style={{
                                                          border: 0,
                                                          borderCollapse:
                                                            "collapse",
                                                          margin: 0,
                                                          padding: 0,
                                                          WebkitFontSmoothing:
                                                            "antialiased",
                                                          MozOsxFontSmoothing:
                                                            "grayscale",
                                                          color: "#ffffff",
                                                          fontSize: "1px",
                                                          lineHeight: "1px",
                                                          msoLineHeightRule:
                                                            "exactly",
                                                        }}
                                                      >
                                                        &nbsp;
                                                      </td>
                                                      <td
                                                        className="Table-amount Font Font--body"
                                                        align="right"
                                                        valign="top"
                                                        style={{
                                                          border: 0,
                                                          borderCollapse:
                                                            "collapse",
                                                          margin: 0,
                                                          padding: 0,
                                                          WebkitFontSmoothing:
                                                            "antialiased",
                                                          MozOsxFontSmoothing:
                                                            "grayscale",
                                                          fontFamily:
                                                            '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                                          msoLineHeightRule:
                                                            "exactly",
                                                          verticalAlign:
                                                            "middle",
                                                          color: "#4F2729",
                                                          fontSize: "15px",
                                                          lineHeight: "24px",
                                                        }}
                                                      >
                                                        {
                                                          paymentData?.currencySymbol
                                                        }
                                                        {paymentData?.amount}
                                                      </td>
                                                    </tr>
                                                    <tr>
                                                      <td
                                                        className="Table-divider Spacer"
                                                        colSpan={3}
                                                        height={6}
                                                        style={{
                                                          border: 0,
                                                          borderCollapse:
                                                            "collapse",
                                                          margin: 0,
                                                          padding: 0,
                                                          WebkitFontSmoothing:
                                                            "antialiased",
                                                          MozOsxFontSmoothing:
                                                            "grayscale",
                                                          color: "#ffffff",
                                                          fontSize: "1px",
                                                          lineHeight: "1px",
                                                          msoLineHeightRule:
                                                            "exactly",
                                                        }}
                                                      >
                                                        &nbsp;
                                                      </td>
                                                    </tr>
                                                    <tr>
                                                      <td
                                                        className="Table-divider Spacer"
                                                        colSpan={3}
                                                        height={6}
                                                        style={{
                                                          border: 0,
                                                          borderCollapse:
                                                            "collapse",
                                                          margin: 0,
                                                          padding: 0,
                                                          WebkitFontSmoothing:
                                                            "antialiased",
                                                          MozOsxFontSmoothing:
                                                            "grayscale",
                                                          color: "#ffffff",
                                                          fontSize: "1px",
                                                          lineHeight: "1px",
                                                          msoLineHeightRule:
                                                            "exactly",
                                                        }}
                                                      >
                                                        &nbsp;
                                                      </td>
                                                    </tr>
                                                    <tr>
                                                      <td
                                                        className="Spacer"
                                                        bgcolor="#909090"
                                                        colSpan={3}
                                                        height={1}
                                                        style={{
                                                          border: 0,
                                                          borderCollapse:
                                                            "collapse",
                                                          margin: 0,
                                                          padding: 0,
                                                          WebkitFontSmoothing:
                                                            "antialiased",
                                                          MozOsxFontSmoothing:
                                                            "grayscale",
                                                          color: "#ffffff",
                                                          fontSize: "1px",
                                                          lineHeight: "1px",
                                                          msoLineHeightRule:
                                                            "exactly",
                                                        }}
                                                      >
                                                        &nbsp;
                                                      </td>
                                                    </tr>
                                                    <tr>
                                                      <td
                                                        className="Table-divider Spacer"
                                                        colSpan={3}
                                                        height={8}
                                                        style={{
                                                          border: 0,
                                                          borderCollapse:
                                                            "collapse",
                                                          margin: 0,
                                                          padding: 0,
                                                          WebkitFontSmoothing:
                                                            "antialiased",
                                                          MozOsxFontSmoothing:
                                                            "grayscale",
                                                          color: "#ffffff",
                                                          fontSize: "1px",
                                                          lineHeight: "1px",
                                                          msoLineHeightRule:
                                                            "exactly",
                                                        }}
                                                      >
                                                        &nbsp;
                                                      </td>
                                                    </tr>
                                                    <tr>
                                                      <td
                                                        className="Table-description Font Font--body"
                                                        style={{
                                                          border: 0,
                                                          borderCollapse:
                                                            "collapse",
                                                          margin: 0,
                                                          padding: 0,
                                                          WebkitFontSmoothing:
                                                            "antialiased",
                                                          MozOsxFontSmoothing:
                                                            "grayscale",
                                                          fontFamily:
                                                            '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                                          msoLineHeightRule:
                                                            "exactly",
                                                          verticalAlign:
                                                            "middle",
                                                          color: "#4F2729",
                                                          fontSize: "15px",
                                                          lineHeight: "24px",
                                                          width: "100%",
                                                        }}
                                                      >
                                                        <strong>
                                                          Amount charged
                                                        </strong>
                                                      </td>
                                                      <td
                                                        className="Spacer Table-gap"
                                                        width={8}
                                                        style={{
                                                          border: 0,
                                                          borderCollapse:
                                                            "collapse",
                                                          margin: 0,
                                                          padding: 0,
                                                          WebkitFontSmoothing:
                                                            "antialiased",
                                                          MozOsxFontSmoothing:
                                                            "grayscale",
                                                          color: "#ffffff",
                                                          fontSize: "1px",
                                                          lineHeight: "1px",
                                                          msoLineHeightRule:
                                                            "exactly",
                                                        }}
                                                      >
                                                        &nbsp;
                                                      </td>
                                                      <td
                                                        className="Table-amount Font Font--body"
                                                        align="right"
                                                        valign="top"
                                                        style={{
                                                          border: 0,
                                                          borderCollapse:
                                                            "collapse",
                                                          margin: 0,
                                                          padding: 0,
                                                          WebkitFontSmoothing:
                                                            "antialiased",
                                                          MozOsxFontSmoothing:
                                                            "grayscale",
                                                          fontFamily:
                                                            '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                                          msoLineHeightRule:
                                                            "exactly",
                                                          verticalAlign:
                                                            "middle",
                                                          color: "#4F2729",
                                                          fontSize: "15px",
                                                          lineHeight: "24px",
                                                        }}
                                                      >
                                                        <strong>
                                                          {
                                                            paymentData?.currencySymbol
                                                          }
                                                          {paymentData?.amount}
                                                        </strong>
                                                      </td>
                                                    </tr>
                                                    <tr>
                                                      <td
                                                        className="Table-divider Spacer"
                                                        colSpan={3}
                                                        height={6}
                                                        style={{
                                                          border: 0,
                                                          borderCollapse:
                                                            "collapse",
                                                          margin: 0,
                                                          padding: 0,
                                                          WebkitFontSmoothing:
                                                            "antialiased",
                                                          MozOsxFontSmoothing:
                                                            "grayscale",
                                                          color: "#ffffff",
                                                          fontSize: "1px",
                                                          lineHeight: "1px",
                                                          msoLineHeightRule:
                                                            "exactly",
                                                        }}
                                                      >
                                                        &nbsp;
                                                      </td>
                                                    </tr>
                                                  </tbody>
                                                </table>
                                              </td>
                                              <td
                                                className="st-Spacer st-Spacer--gutter"
                                                style={{
                                                  border: 0,
                                                  margin: 0,
                                                  padding: 0,
                                                  fontSize: "1px",
                                                  lineHeight: "1px",
                                                  msoLineHeightRule: "exactly",
                                                }}
                                                width={16}
                                              >
                                                <div className="st-Spacer st-Spacer--filler">
                                                  &nbsp;
                                                </div>
                                              </td>
                                            </tr>
                                            <tr>
                                              <td
                                                className="st-Spacer st-Spacer--blocksItemEnds"
                                                colSpan={3}
                                                height={12}
                                                style={{
                                                  border: 0,
                                                  margin: 0,
                                                  padding: 0,
                                                  fontSize: "1px",
                                                  lineHeight: "1px",
                                                  msoLineHeightRule: "exactly",
                                                }}
                                              >
                                                <div className="st-Spacer st-Spacer--filler">
                                                  &nbsp;
                                                </div>
                                              </td>
                                            </tr>
                                          </tbody>
                                        </table>
                                      </td>
                                    </tr>
                                  </tbody>
                                </table>
                              </td>
                              <td
                                className="st-Spacer st-Spacer--kill"
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                                width={64}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                            </tr>
                            <tr>
                              <td
                                className="st-Spacer st-Spacer--stacked"
                                colSpan={3}
                                height={16}
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                        <div className="button_containerPaymentSlip">
                          <button className="button" onClick={changeModal}>
                            Proceed
                          </button>
                        </div>
                        <table
                          className="st-Divider st-Width st-Width--mobile"
                          border={0}
                          cellPadding={0}
                          cellSpacing={0}
                          width={600}
                          style={{ minWidth: "600px" }}
                        >
                          <tbody>
                            <tr>
                              <td
                                className="st-Spacer st-Spacer--divider"
                                colSpan={3}
                                height={20}
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  maxHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                            </tr>
                            <tr>
                              <td
                                className="st-Spacer st-Spacer--gutter"
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  maxHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                                width={64}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                              <td
                                bgcolor="#4F2729"
                                height={1}
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  maxHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                              <td
                                className="st-Spacer st-Spacer--gutter"
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  maxHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                                width={64}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                            </tr>
                            <tr>
                              <td
                                className="st-Spacer st-Spacer--divider"
                                colSpan={3}
                                height={31}
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  maxHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                        <table
                          className="st-Copy st-Width st-Width--mobile"
                          border={0}
                          cellPadding={0}
                          cellSpacing={0}
                          width={600}
                          style={{ minWidth: "600px" }}
                        >
                          <tbody>
                            <tr>
                              <td
                                className="st-Spacer st-Spacer--gutter"
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                  color: "#4F2729 !important",
                                }}
                                width={64}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                              <td
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  color: "#4F2729",
                                  fontFamily:
                                    '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                  fontSize: "16px",
                                  lineHeight: "24px",
                                }}
                              >
                                If you have any questions, contact us at
                                <a
                                  style={{
                                    border: 0,
                                    margin: 0,
                                    padding: 0,
                                    color: "#4F2729 !important",
                                    textDecoration: "none",
                                  }}
                                  href="mailto:contact@athmarine.com"
                                >
                                  contact@athmarine.com
                                </a>
                                or call at
                                <a
                                  style={{
                                    border: 0,
                                    margin: 0,
                                    padding: 0,
                                    color: "#4F2729 !important",
                                    textDecoration: "none",
                                  }}
                                  href="tel:+6582284069"
                                >
                                  +65 8228 4069
                                </a>
                                .
                              </td>
                              <td
                                className="st-Spacer st-Spacer--gutter"
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                                width={64}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                            </tr>
                            <tr>
                              <td
                                className="st-Spacer st-Spacer--stacked"
                                colSpan={3}
                                height={12}
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                        {/* <table
                          className="st-Divider st-Width st-Width--mobile"
                          border={0}
                          cellPadding={0}
                          cellSpacing={0}
                          width={600}
                          style={{ minWidth: "600px" }}
                        >
                          <tbody>
                            <tr>
                              <td
                                className="st-Spacer st-Spacer--divider"
                                colSpan={3}
                                height={20}
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  maxHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                            </tr>
                            <tr>
                              <td
                                className="st-Spacer st-Spacer--gutter"
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  maxHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                                width={64}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                              <td
                                bgcolor="#e6ebf1"
                                height={1}
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  maxHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                              <td
                                className="st-Spacer st-Spacer--gutter"
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  maxHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                                width={64}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                            </tr>
                            <tr>
                              <td
                                className="st-Spacer st-Spacer--divider"
                                colSpan={3}
                                height={31}
                                style={{
                                  border: 0,
                                  margin: 0,
                                  padding: 0,
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  maxHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                <div className="st-Spacer st-Spacer--filler">
                                  &nbsp;
                                </div>
                              </td>
                            </tr>
                          </tbody>
                        </table> */}
                        {/* <table
                          className="Section Copy"
                          style={{
                            border: 0,
                            borderCollapse: "collapse",
                            margin: 0,
                            padding: 0,
                            backgroundColor: "#ffffff",
                          }}
                        >
                          <tbody>
                            <tr>
                              <td
                                className="Spacer Spacer--gutter"
                                width={64}
                                style={{
                                  border: 0,
                                  borderCollapse: "collapse",
                                  margin: 0,
                                  padding: 0,
                                  WebkitFontSmoothing: "antialiased",
                                  MozOsxFontSmoothing: "grayscale",
                                  color: "#ffffff",
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                &nbsp;
                              </td>
                              <td
                                className="Content Footer-legal Font Font--caption Font--mute"
                                style={{
                                  border: 0,
                                  borderCollapse: "collapse",
                                  margin: 0,
                                  padding: 0,
                                  WebkitFontSmoothing: "antialiased",
                                  MozOsxFontSmoothing: "grayscale",
                                  width: "472px",
                                  fontFamily:
                                    '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                  msoLineHeightRule: "exactly",
                                  verticalAlign: "middle",
                                  color: "#4F2729",
                                  fontSize: "12px",
                                  lineHeight: "16px",
                                }}
                              >
                                Something wrong with the email?
                                <a
                                  className="browser-link"
                                  href="https://dashboard.stripe.com/emails/receipts/pmtrc_1KtBb3HbXZa50bvbzx5eK5oY"
                                  style={{
                                    border: 0,
                                    margin: 0,
                                    padding: 0,
                                    color: "#4F2729",
                                    fontFamily:
                                      '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                    textDecoration: "none",
                                  }}
                                  target="_blank"
                                  rel="noreferrer"
                                >
                                  <span
                                    style={{
                                      border: 0,
                                      margin: 0,
                                      padding: 0,
                                      color: "#4F2729",
                                      textDecoration: "none",
                                    }}
                                  >
                                    View it in your browser.
                                  </span>
                                </a>
                              </td>
                              <td
                                className="Spacer Spacer--gutter"
                                width={64}
                                style={{
                                  border: 0,
                                  borderCollapse: "collapse",
                                  margin: 0,
                                  padding: 0,
                                  WebkitFontSmoothing: "antialiased",
                                  MozOsxFontSmoothing: "grayscale",
                                  color: "#ffffff",
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                &nbsp;
                              </td>
                            </tr>
                          </tbody>
                        </table> */}
                        {/* <table
                          className="Section Divider Divider--small"
                          width="100%"
                          style={{
                            border: 0,
                            borderCollapse: "collapse",
                            margin: 0,
                            padding: 0,
                            backgroundColor: "#ffffff",
                          }}
                        >
                          <tbody>
                            <tr>
                              <td
                                className="Spacer Spacer--divider"
                                height={20}
                                style={{
                                  border: 0,
                                  borderCollapse: "collapse",
                                  margin: 0,
                                  padding: 0,
                                  WebkitFontSmoothing: "antialiased",
                                  MozOsxFontSmoothing: "grayscale",
                                  color: "#ffffff",
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                &nbsp;
                              </td>
                            </tr>
                          </tbody>
                        </table> */}
                        {/* <table
                          className="Section Copy"
                          style={{
                            border: 0,
                            borderCollapse: "collapse",
                            margin: 0,
                            padding: 0,
                            backgroundColor: "#ffffff",
                          }}
                        >
                          <tbody>
                            <tr>
                              <td
                                className="Spacer Spacer--gutter"
                                width={64}
                                style={{
                                  border: 0,
                                  borderCollapse: "collapse",
                                  margin: 0,
                                  padding: 0,
                                  WebkitFontSmoothing: "antialiased",
                                  MozOsxFontSmoothing: "grayscale",
                                  color: "#ffffff",
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                &nbsp;
                              </td>
                              <td
                                className="Content Footer-legal Font Font--caption Font--mute"
                                style={{
                                  border: 0,
                                  borderCollapse: "collapse",
                                  margin: 0,
                                  padding: 0,
                                  WebkitFontSmoothing: "antialiased",
                                  MozOsxFontSmoothing: "grayscale",
                                  width: "472px",
                                  fontFamily:
                                    '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                  msoLineHeightRule: "exactly",
                                  verticalAlign: "middle",
                                  color: "#4F2729",
                                  fontSize: "12px",
                                  lineHeight: "16px",
                                }}
                              >
                                You're receiving this email because you made a
                                purchase at ATH MARINE PTE LTD, which partners
                                with
                                <a
                                  style={{
                                    border: 0,
                                    margin: 0,
                                    padding: 0,
                                    color: "#4F2729",
                                    fontFamily:
                                      '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Ubuntu, sans-serif',
                                    textDecoration: "none",
                                  }}
                                  target="_blank"
                                  rel="noreferrer"
                                  href="https://stripe.com/"
                                >
                                  Stripe
                                </a>
                                to provide invoicing and payment processing.
                              </td>
                            </tr>
                          </tbody>
                        </table> */}
                        {/* <table
                          className="Section Divider Divider--small"
                          width="100%"
                          style={{
                            border: 0,
                            borderCollapse: "collapse",
                            margin: 0,
                            padding: 0,
                            backgroundColor: "#ffffff",
                          }}
                        >
                          <tbody>
                            <tr>
                              <td
                                className="Spacer Spacer--divider"
                                height={20}
                                style={{
                                  border: 0,
                                  borderCollapse: "collapse",
                                  margin: 0,
                                  padding: 0,
                                  WebkitFontSmoothing: "antialiased",
                                  MozOsxFontSmoothing: "grayscale",
                                  color: "#ffffff",
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                &nbsp;
                              </td>
                            </tr>
                          </tbody>
                        </table>
                        <table
                          className="Section Section--last Divider Divider--large"
                          width="100%"
                          style={{
                            border: 0,
                            borderCollapse: "collapse",
                            margin: 0,
                            padding: 0,
                            backgroundColor: "#ffffff",
                            borderBottomLeftRadius: "5px",
                            borderBottomRightRadius: "5px",
                          }}
                        >
                          <tbody>
                            <tr>
                              <td
                                className="Spacer Spacer--divider"
                                height={64}
                                style={{
                                  border: 0,
                                  borderCollapse: "collapse",
                                  margin: 0,
                                  padding: 0,
                                  WebkitFontSmoothing: "antialiased",
                                  MozOsxFontSmoothing: "grayscale",
                                  color: "#ffffff",
                                  fontSize: "1px",
                                  lineHeight: "1px",
                                  msoLineHeightRule: "exactly",
                                }}
                              >
                                &nbsp;
                              </td>
                            </tr>
                          </tbody>
                        </table> */}
                      </td>
                    </tr>
                  </tbody>
                </table>
                {/* /Wrapper */}
              </td>
            </tr>
            <tr>
              <td
                className="st-Spacer st-Spacer--emailEnd"
                height={64}
                style={{
                  border: 0,
                  margin: 0,
                  padding: 0,
                  fontSize: "1px",
                  lineHeight: "1px",
                  msoLineHeightRule: "exactly",
                }}
              >
                <div className="st-Spacer st-Spacer--filler">&nbsp;</div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </>
  );
}

export default PaymentSlip;
