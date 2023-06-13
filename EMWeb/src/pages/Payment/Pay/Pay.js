import React, { useState, useEffect } from "react";
import "./Pay.scss";
import Logo from "./../../../assets/images/logo.png";
import visa from "./../../../assets/images/visa.svg";
import mastercard from "./../../../assets/images/mastercard.svg";
import amex from "./../../../assets/images/amex.svg";
import jcb from "./../../../assets/images/jcb.svg";
import discover from "./../../../assets/images/discover.svg";
import unionpay from "./../../../assets/images/union-pay.svg";
import diners from "./../../../assets/images/diner.svg";
import cardcvv from "./../../../assets/images/card-cvv.png";
import Loading from "../../../components/Loading/Loading";
import { validateCardForPayment } from "../../../store/payment/cardValidation";
import Select from "react-select";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import { paymentRequest } from "../../../store/payment/paymentRequest";
import SuccessPayment from "../Modal/SuccessPayment";
import { getCompanyId, getUserId } from "../../../config";
import { getAmountToPayFunc } from "../../../store/payment/getAmountToPay";
import { getCurrencyStatus } from "../../../store/vendorRegistration/currency";
import { getPaymentDetails } from "../../../store/vendorRegistration/paymentDetails";
import Disclaimer from "../Modal/Disclaimer";
import { getFinanceEmailDetails } from "../../../store/payment/financeEmailandPhone";
import Invoice from "../../Registration/components/Forms/Invoice/invoice";
import moment from "moment";
import logo from "./../../../assets/images/logo.png";
import emailIcon from "./../../../assets/images/Group 2093.svg";
import phoneIcon from "./../../../assets/images/Group 2092.svg";
import websiteIcon from "./../../../assets/images/Group 2100.svg";
import locationIcon from "./../../../assets/images/icon-location.png";
import downloadIcon from "./../../../assets/images/downloadicon.svg";
import TransactionType from "../../Registration/components/Modals/TransactionType/transactionType";
import InvoiceModal from "../../Registration/components/Modals/Invoice/InvoiceModal";
import { getPromotionDetails } from "../../../store/common/promotionsDetails";

const Pay = (props) => {
  const history = useHistory();
  const dispatch = useDispatch();

  const [cardNumber, setCardNumber] = useState(``);
  const [cardHolderName, setCardHolderName] = useState(``);
  const [error, setError] = useState(false);
  const [expiryDate, setExpiryDate] = useState(``);
  const [cvv, setCVV] = useState(``);
  const [currencyDetails, setCurrencyDetails] = useState([]);
  const [isCardNumberError, setIsCardNumberError] = useState(false);
  const [isCurrencyError, setIsCurrencyError] = useState(false);
  const [isCardHolderNameError, setIsCardHolderNameError] = useState(false);
  const [isExpiryDateError, setIsExpiryDateError] = useState(false);
  const [isCVVError, setIsCVVError] = useState(false);
  const [isError, setIsError] = useState(false);
  const [isBackPress, setIsBackPress] = useState(false);
  const [cardType, setCardType] = useState(``);
  const [isSuccessPopupShow, setIsSuccessPopupShow] = useState(false);
  const [disclaimerPopupShow, setDisclaimerPopupShow] = useState(true);
  const [rate, setRate] = useState(1);
  const [currencyDataValue, setCurrencyDataValue] = useState("USD");
  const [currencySymbol, setCurrencySymbol] = useState(`$`);
  const [selectedOptions, setSelectedOptions] = useState([]);
  const [transactionType, setTransactionType] = useState(false);
  const [showInvoiceModal, setShowInvoiceModal] = useState(false);
  const [masterPromotionStragyId, setMasterPromotionStragyId] = useState(``);

  const getAmountRes = useSelector(
    (state) => state.getAmountToPayReducer.state
  );
  const getAmountLoading = useSelector(
    (state) => state.getAmountToPayReducer.isLoading
  );
  const financeData = useSelector(
    (state) => state.financeEmailandPhoneReducer.financeEmailDetails
  );
  const cardValidationRes = useSelector(
    (state) => state.cardValidationReducer.state
  );
  const cardValidationError = useSelector(
    (state) => state.cardValidationReducer.isError
  );
  const cardValidationLoading = useSelector(
    (state) => state.cardValidationReducer.isLoading
  );
  const paymentRequestRes = useSelector(
    (state) => state.paymentRequestReducer.state
  );
  const paymentRequestError = useSelector(
    (state) => state.paymentRequestReducer.isError
  );
  const paymentRequestLoading = useSelector(
    (state) => state.paymentRequestReducer.isLoading
  );

  const currencyData = useSelector(
    (state) => state.currencyStatusReducer.currencyStatus
  );
  const currencyDataError = useSelector(
    (state) => state.currencyStatusReducer.isError
  );
  const currencyDataLoading = useSelector(
    (state) => state.currencyStatusReducer.isLoading
  );
  const paymentDetails = useSelector(
    (state) => state.paymentDetailsReducer.paymentDetails
  );

  const isLoading_paymentDetails = useSelector(
    (state) => state.paymentDetailsReducer.isLoading
  );
  const isPaymentDetailsError = useSelector(
    (state) => state.paymentDetailsReducer.isError
  );

  const promotionDetailsRes = useSelector(
    (state) => state.getPromotionDetailsReducer.promotionDetailsRes
  );

  useEffect(() => {
    if (paymentRequestRes !== null) {
      setIsSuccessPopupShow(true);
    }
  }, [paymentRequestRes]);

  useEffect(() => {
    dispatch(getFinanceEmailDetails());
    dispatch(getPromotionDetails());
  }, [dispatch]);

  useEffect(() => {
    if (cardValidationRes !== null) {
      const data = {
        transactionCardDeatils: `${cardValidationRes.card.id}`,
        tokenStripe: `tok_visa_triggerNextRequirements`,
        amount: totalPay,
        currency: currencyDataValue,
        currencySymbol: currencySymbol,
        emailSendToFinance: emailId,
        atmCardNumber: cardNumber,
        user_id: getUserId() === null ? getCompanyId() : getUserId(),
        masterPromotionStragyId: masterPromotionStragyId,
      };
      dispatch(paymentRequest(data, `payment/createPayment`));
    }
  }, [dispatch, cardValidationRes]);

  useEffect(() => {
    dispatch(getCurrencyStatus());
    dispatch(getPaymentDetails());
  }, [dispatch]);

  // useEffect(() => {
  //   if (currencyData) {
  //     setSelectedDefaultOptions(currencyData[137]);
  //   }
  // }, [currencyData, selectedDefaultOptions]);

  const amountPayment = paymentDetails?.totalAmount * rate;
  const totalPay = amountPayment.toFixed(2);

  useEffect(() => {
    if (isError) checkValidation(`all`);
  }, [isError, cardNumber, cardHolderName, expiryDate, cvv, rate]);

  const cardNumberValidation = (card) => {
    card = card.replace(/\D/g, "");
    const match = card.match(/^(\d{0,4})(\d{0,4})(\d{0,4})(\d{0,4})$/);
    if (match) {
      card = `${match[1]}${match[2] ? " " : ""}${match[2]}${
        match[3] ? " " : ""
      }${match[3]}${match[4] ? " " : ""}${match[4]}`;
    }
    return card;
  };

  const expiryDateValidate = (value) => {
    var expiryDate = "";

    for (var i = 0; i < value.length; i++) {
      if (i === 1 && value.length === 2 && isBackPress === false) {
        expiryDate = expiryDate + value[i] + "/";
      } else {
        setIsBackPress(false);
        expiryDate = expiryDate + value[i];
      }
    }
    return expiryDate;
  };

  const handleLettersOnlyInput = (e) => {
    if (Number.isInteger(Number(e.key))) {
      e.preventDefault();
    }
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

  const validateForm = () => {
    if (selectedOptions.length !== 0) {
      setError(false);
      return true;
    } else {
      setError(true);
      return false;
    }
  };

  const handleAllData = (e) => {
    setRate(e.value[2]);
    setCurrencySymbol(e.value[1]);
    setCurrencyDataValue(e.value[3]);
  };

  const handleInputChange = (event) => {
    let { name, value } = event.target;

    const val = value.split("*");
    switch (name) {
      case "rate":
        setRate(val[1]);
        setCurrencySymbol(val[0]);
        break;

      case "cardHolderName":
        setCardHolderName(value.toUpperCase());
        break;

      case "cardNumber":
        if (value.length <= 19) {
          setCardNumber(cardNumberValidation(value));
        }
        if (value.length === 19) {
          setCardType(detectCardType(`${value}`.replaceAll(" ", "")));
        } else if (value.length < 19) {
          setCardType(detectCardType(`${value}`.replaceAll(" ", "")));
        }
        break;

      case "expiryDate":
        if (value.length < 6) {
          setExpiryDate(value);
          setExpiryDate(expiryDateValidate(value));
        }
        break;

      case "cvv":
        if (value.length <= 3) {
          setCVV(value);
        }
        break;

      default:
      // do nothing
    }
  };

  const optionCurrency = currencyData?.map((index) => ({
    value: [index.id, index.currencySymbol, index.rate, index.currency],
    label: (
      <span style={{ fontFamily: "Poppins-Regular" }}>{index.currency}</span>
    ),
  }));

  const checkValidation = (flag) => {
    setIsError(true);
    if (flag === `all`) {
      if (rate.length === 0) {
        setIsCardNumberError(false);
        setIsCurrencyError(true);
        setIsCardHolderNameError(false);
        setIsExpiryDateError(false);
        setIsCVVError(false);
        return false;
      } else {
        setIsCurrencyError(false);
      }
    }
    if (flag === `all`) {
      if (cardHolderName.trim().length === 0) {
        setIsCardNumberError(false);
        setIsCardHolderNameError(true);
        setIsExpiryDateError(false);
        setIsCVVError(false);
        return false;
      } else {
        setIsCardHolderNameError(false);
      }
    }
    if (flag === `all`) {
      if (cardNumber.trim().length < 19) {
        setIsCardNumberError(true);
        setIsCardHolderNameError(false);
        setIsExpiryDateError(false);
        setIsCVVError(false);
        return false;
      } else {
        setIsCardNumberError(false);
      }
    }
    if (flag === `all`) {
      if (expiryDate.trim().length < 5) {
        setIsCardNumberError(false);
        setIsCardHolderNameError(false);
        setIsExpiryDateError(true);
        setIsCVVError(false);
        return false;
      } else {
        setIsExpiryDateError(false);
      }
    }
    if (flag === `all`) {
      if (cvv.trim().length < 3) {
        setIsCardNumberError(false);
        setIsCardHolderNameError(false);
        setIsExpiryDateError(false);
        setIsCVVError(true);
        return false;
      } else {
        setIsCVVError(false);
      }
    }
    return true;
  };

  const paymentHandler = (e) => {
    e.preventDefault();
    if (!checkValidation("all")) return false;
    if (validateForm()) {
      dispatch(
        validateCardForPayment(
          `card[number]=${cardNumber}&card[exp_month]=${
            expiryDate.split("/")[0]
          }&card[exp_year]=${expiryDate.split("/")[1]}&card[cvc]=${cvv}`,
          "https://api.stripe.com/v1/tokens"
        )
      );
    }
  };

  const selectCardByCardTypeHandler = () => {
    switch (cardType) {
      case "unionpay":
        return (
          <div className="formFieldInputsIcons-icon">
            <img src={unionpay} alt="unionpay" />
          </div>
        );

      case "jcb":
        return (
          <div className="formFieldInputsIcons-icon">
            <img src={jcb} alt="jcb" />
          </div>
        );

      case "visa":
        return (
          <div className="formFieldInputsIcons-icon">
            <img src={visa} alt="visa" />
          </div>
        );

      case "mastercard":
        return (
          <div className="formFieldInputsIcons-icon">
            <img src={mastercard} alt="mastercard" />
          </div>
        );

      case "discover":
        return (
          <div className="formFieldInputsIcons-icon">
            <img src={discover} alt="discover" />
          </div>
        );

      case "amex":
        return (
          <div className="formFieldInputsIcons-icon">
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

  const proceedToRedirectHandler = () => {
    history.push("/paymentSlip");
  };

  const onCancelHandler = () => {
    setDisclaimerPopupShow(!disclaimerPopupShow);
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

  const customStyles = {
    container: (base, state) => ({
      ...base,
    }),
    menu: (styles) => ({
      ...styles,
      width: "100%",
    }),
  };

  const offlinePayment = () => {
    setTransactionType(!transactionType);
    setShowInvoiceModal(!showInvoiceModal);
  };
  const onlinePayment = () => {
    setTransactionType(!transactionType);
  };
  const cancelTransactionType = () => {
    setTransactionType(!transactionType);
  };

  const cancelInvoice = () => {
    setShowInvoiceModal(!showInvoiceModal);
  };
  const confirmInvoice = () => {
    setShowInvoiceModal(!showInvoiceModal);
    offlineViaPayment();
    paymentViaOffline();
  };

  const offlineViaPayment = () => {
    props.setFormLabel("invoice", 5);
  };

  const paymentViaOffline = () => {
    if (paymentRequestRes === null) {
      props.navigationHandler({
        currentForm: "invoice",
        goForward: true,
      });
    }
  };

  useEffect(() => {
    if (promotionDetailsRes !== null) {
      if (
        new Date(`${promotionDetailsRes[0]?.startedDate}`).valueOf() <
          new Date(
            `${moment(new Date()).format("YYYY-MM-DD").toString()}`
          ).valueOf() &&
        new Date(
          `${moment(new Date()).format("YYYY-MM-DD").toString()}`
        ).valueOf() < new Date(`${promotionDetailsRes[0]?.endDate}`).valueOf()
      ) {
        setMasterPromotionStragyId(1);
      } else if (
        new Date(`${promotionDetailsRes[1]?.startedDate}`).valueOf() <
          new Date(
            `${moment(new Date()).format("YYYY-MM-DD").toString()}`
          ).valueOf() &&
        new Date(
          `${moment(new Date()).format("YYYY-MM-DD").toString()}`
        ).valueOf() < new Date(`${promotionDetailsRes[1]?.endDate}`).valueOf()
      ) {
        setMasterPromotionStragyId(2);
      } else if (
        new Date(`${promotionDetailsRes[2]?.startedDate}`).valueOf() <
          new Date(
            `${moment(new Date()).format("YYYY-MM-DD").toString()}`
          ).valueOf() &&
        new Date(
          `${moment(new Date()).format("YYYY-MM-DD").toString()}`
        ).valueOf() < new Date(`${promotionDetailsRes[2]?.endDate}`).valueOf()
      ) {
        setMasterPromotionStragyId(3);
      } else {
        setMasterPromotionStragyId(0);
      }
    }
  }, [promotionDetailsRes]);

  return (
    <>
      {(cardValidationLoading ||
        paymentRequestLoading ||
        getAmountLoading ||
        isLoading_paymentDetails) && <Loading text={"Loading..."} />}
      {isSuccessPopupShow && (
        <SuccessPayment redirect={proceedToRedirectHandler} />
      )}
      {disclaimerPopupShow && <Disclaimer redirect={onCancelHandler} />}

      {transactionType && (
        <TransactionType
          onPayment={onlinePayment}
          onInvoice={offlinePayment}
          cancelTransactionType={cancelTransactionType}
        />
      )}

      {showInvoiceModal && (
        <InvoiceModal
          cancelInvoice={cancelInvoice}
          confirmInvoice={confirmInvoice}
          totalEngineer={paymentDetails?.totalEngineer}
          totalPayment={paymentDetails?.totalAmount}
          name={paymentDetails?.name}
          address={paymentDetails?.address}
          invoiceNo={paymentDetails?.invoiceNumber}
        />
      )}

      <div className="payment_container">
        <div className="pay_form positioning_container">
          <div className="left_section">
            <div className="InvoiceContainerPay">
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
                  <p className="Invoice__desc__left">
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
          {/* ------------------------------- */}
          <div className="right_section">
            <label className="global_error_label">
              {cardValidationError !== "" ? (
                `-- ${cardValidationError}`
              ) : (
                <p>&nbsp;</p>
              )}
            </label>
            <form onSubmit={paymentHandler}>
              <div className="formfieldgroup">
                <div className="formfieldgroup_label">CURRENCY</div>
                <div className="checkoutInputContainer">
                  {/* <select
                      className={`checkoutSelect ${isCurrencyError && `error`}`}
                      name="rate"
                      onChange={(e) => handleInputChange(e)}
                    >
                       <option value="" disabled selected></option>
                        {currencyData && currencyData.length > 0
                          ? currencyData.map((item, index) => (
                            <option value={`${item.currencySymbol}*${item.rate}`} key={item.id}>
                              {item.currency}
                            </option>
                          ))
                          : null}
                      </select> */}
                  <Select
                    className={`checkoutSelect ${isCurrencyError && `error`}`}
                    options={optionCurrency}
                    onChange={(e) => handleAllData(e)}
                    onKeyDown={handleLettersOnlyInput}
                    classNamePrefix="select-currency"
                    defaultValue={{
                      label: (
                        <span style={{ fontFamily: "Poppins-Regular" }}>
                          USD
                        </span>
                      ),
                      value: [71, "$", 1, "USD"],
                    }}
                  />
                </div>
                <label className="error_label">
                  {isCurrencyError ? `Currency is required.` : <p>&nbsp;</p>}
                </label>
              </div>

              <div className="formfieldgroup">
                <div className="formfieldgroup_label">CARD HOLDER</div>
                <div className="checkoutInputContainer">
                  <input
                    type="text"
                    className={`checkoutInput ${
                      isCardHolderNameError && `error`
                    }`}
                    autocomplete="off"
                    name="cardHolderName"
                    placeholder="ENTER CARD HOLDER NAME"
                    onChange={(e) => {
                      if (!Number(e.nativeEvent.data)) handleInputChange(e);
                    }}
                    value={cardHolderName}
                  />
                </div>
                <label className="error_label">
                  {isCardHolderNameError ? (
                    `Card Holder name is required.`
                  ) : (
                    <p>&nbsp;</p>
                  )}
                </label>
              </div>
              <div className="formfieldgroup">
                <div className="formfieldgroup_label">CARD NUMBER</div>
                <div className="checkoutInputContainer">
                  <input
                    type="text"
                    className={`checkoutInput ${isCardNumberError && `error`}`}
                    autocomplete="off"
                    name="cardNumber"
                    placeholder="1234 1234 1234 1234"
                    onChange={(e) => handleInputChange(e)}
                    value={cardNumber}
                  />
                </div>
                <div className="formFieldInputIcons">
                  {cardType !== `` ? (
                    selectCardByCardTypeHandler()
                  ) : (
                    <>
                      <div className="formFieldInputsIcons-icon">
                        <img src={visa} alt="visa" />
                      </div>
                      <div className="formFieldInputsIcons-icon">
                        <img src={mastercard} alt="mastercard" />
                      </div>
                      <div className="formFieldInputsIcons-icon">
                        <img src={amex} alt="amex" />
                      </div>
                      <div className="formFieldInputsIcons-icon">
                        <img src={jcb} alt="jcb" />
                      </div>
                    </>
                  )}
                  {cardType === undefined && (
                    <>
                      <div className="formFieldInputsIcons-icon">
                        <img src={visa} alt="visa" />
                      </div>
                      <div className="formFieldInputsIcons-icon">
                        <img src={mastercard} alt="mastercard" />
                      </div>
                      <div className="formFieldInputsIcons-icon">
                        <img src={amex} alt="amex" />
                      </div>
                      <div className="formFieldInputsIcons-icon">
                        <img src={jcb} alt="jcb" />
                      </div>
                    </>
                  )}
                </div>
                <label className="error_label">
                  {isCardNumberError ? (
                    `Your card number is invalid.`
                  ) : (
                    <p>&nbsp;</p>
                  )}
                </label>
              </div>

              <div className="single_container">
                <div className="formfieldgroup expiration_date">
                  <div className="formfieldgroup_label">EXPIRATION DATE</div>
                  <div className="checkoutInputContainer">
                    <input
                      type="text"
                      className={`checkoutInput ${
                        isExpiryDateError && `error`
                      }`}
                      autocomplete="off"
                      name="expiryDate"
                      placeholder="MM/YY"
                      onChange={(e) => handleInputChange(e)}
                      value={expiryDate}
                    />
                  </div>
                  <label className="error_label">
                    {isExpiryDateError ? (
                      `Expiration date is required.`
                    ) : (
                      <p>&nbsp;</p>
                    )}
                  </label>
                </div>
                <div className="formfieldgroup cvv">
                  <div className="formfieldgroup_label">CVV</div>
                  <div className="checkoutInputContainer">
                    <input
                      type="number"
                      className={`checkoutInput ${isCVVError && `error`}`}
                      autocomplete="off"
                      name="cvv"
                      placeholder="CVV"
                      onChange={(e) => handleInputChange(e)}
                      value={cvv}
                    />
                  </div>
                  <label className="error_label">
                    {isCVVError ? `CVV is required.` : <p>&nbsp;</p>}
                  </label>
                </div>
              </div>
              {/* <div className='disclaimer'>Disclaimer: We will not save your card information.</div> */}
              {/* ---------------------------------------------------------------------- */}
              <div className="checkout_button_container">
                <Select
                  className="form_control_country_pay"
                  isMulti={true}
                  options={options}
                  closeMenuOnSelect={false}
                  onChange={handleChange}
                  styles={customStyles}
                  placeholder="Please select an email"
                  noOptionsMessage={(e) => (e.inputValue ? "No options" : null)}
                />
                {error && selectedOptions.length === 0 && (
                  <span className="error_pay">Please select an email</span>
                )}
              </div>

              {/* ------------------------------------------------------------------ */}
              <div className="formfieldgroup">
                <button type="submit" className="btn-pay">
                  Pay ({currencySymbol}) {totalPay}
                </button>
                {/* <button type="submit" className="btn-pay">Pay {(getAmountRes !== null) && getAmountRes}</button> */}
              </div>

              <div className="disclaimer">
                Disclaimer: This is an indicative amount. Actual amount may
                slightly vary based on the FX exchange rates used by your bank.
              </div>
            </form>
          </div>
        </div>
      </div>
    </>
  );
};

export default Pay;
