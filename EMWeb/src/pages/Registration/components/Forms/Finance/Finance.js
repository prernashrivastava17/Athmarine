import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import AwesomeSlider from "react-awesome-slider";
import "react-awesome-slider/dist/styles.css";
import { useHistory } from "react-router-dom";
import Select from "react-select";
import "./Finance.scss";
import moment from "moment";
import cancelIcon from "./../../../../../assets/images/cancel_other_icon.png";
import cancel from "./../../../../../assets/images/cancel.png";
import ConfirmationModal from "./../../Modals/ConfirmationModal/ConfirmationModal";
import EngineerPayment from "./../../Modals/paymentModal/engineerPayment";
import ConfirmationAddNewFinancer from "./../../Modals/confirmationAddNewFinancer/confirmationAddNewFinancer";
import Loading from "./../../../../../components/Loading/Loading";
import Tooltip from "./../../../../../components/Tooltip/Tooltip";
import {
  validateEmailPhone,
  validateEmailPhoneActions,
} from "../../../../../store/vendorRegistration/validateEmailPhone";
import PaymentRedirect from "../../Modals/paymentRedirectModal/PaymentRedirect";
import {
  saveFinancer,
  saveFinancerSlice,
} from "../../../../../store/vendorRegistration/assignedFinancer";
import { getCountries } from "./../../../../../store/common/country";
import { getAvailableOn } from "./../../../../../store/common/availableOn";
import { getCurrencyCountryStatus } from "../../../../../store/vendorRegistration/allCurrency";
import {
  getAuthData,
  getAuthToken,
  getCompanyId,
  getReferralCode,
  getToken,
  getUserId,
} from "../../../../../config";
import {
  emailValidation,
  numberValidation,
} from "./../../../../../utils/helpers";
import {
  commonGetAPiCall,
  commonGetApiActions,
} from "../../../../../store/common/commonGetApi";

import { ToastError } from "./../../../../../components/Tostify";
import {
  fileUpload,
  uploadActions,
} from "../../../../../store/common/fileUpload";

import { getPaymentDetails } from "./../../../../../store/vendorRegistration/paymentDetails";
import { getAllUserDetails } from "../../../../../store/common/allUserDetails";
import TransactionType from "./../../Modals/TransactionType/transactionType";
import InvoiceModal from "../../Modals/Invoice/InvoiceModal";
import {
  addressGetAPiCall,
  addressGetApiActions,
} from "../../../../../store/vendorRegistration/addressGetApi";
import PaymentDetailsByInoice from "../../Modals/paymentDetailsByInvoice/paymentDetailsByInvoice";
import { getCurrencyStatus } from "../../../../../store/vendorRegistration/currency";
import { getPromotionDetails } from "./../../../../../store/common/promotionsDetails";
import RegistrationSuccessFull from "../../../../Payment/Modal/RegistrationSuccessFull";
import { paymentRequest } from "../../../../../store/payment/paymentRequest";

const Finance = (props) => {
  const dispatch = useDispatch();
  const history = useHistory();

  let isFormTypePartner = props.selectedUser === "partner" ? true : false;
  let isFormTypeCustomer = props.selectedUser === "customer" ? true : false;
  let isFormTypePartnersIndividual =
    props.selectedUser === "partners-individual" ? true : false;

  const isLoading = useSelector((state) => state.financerReducer.isLoading);
  const financerRes = useSelector((state) => state.financerReducer.financerRes);
  const navigationCheck = useSelector(
    (state) => state.financerReducer.navigationCheck
  );
  const isErrorfinancerRes = useSelector(
    (state) => state.financerReducer.isError
  );
  const countries = useSelector((state) => state.countries.countries);
  const isLoading_countries = useSelector((state) => state.countries.isLoading);
  const isCountryError = useSelector((state) => state.countries.isError);
  const availableOnRes = useSelector(
    (state) => state.availableOnReducer.availableOnRes
  );
  const isErroravailableOnRes = useSelector(
    (state) => state.availableOnReducer.isError
  );
  const isAvailableOnLoading = useSelector(
    (state) => state.availableOnReducer.isLoading
  );
  const navigationResponse = useSelector(
    (state) => state.commonGetApiReducer.getApiRes
  );
  const fileUploadRes = useSelector((state) => state.fileUploadReducer.upload);
  const fileUploadResIsLoading = useSelector(
    (state) => state.fileUploadReducer.isLoading
  );
  const fileUploadIndex = useSelector((state) => state.fileUploadReducer.index);
  const registrationStatusKey = useSelector(
    (state) => state.commonGetApiReducer.registrationStatusKey
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

  const currencyCountry = useSelector(
    (state) => state.currencyCountryReducer.currencyCountryStatus
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

  const allUserDetails = useSelector(
    (state) => state.allUserDetailsReducer.allUserDetails
  );

  const addressData = useSelector(
    (state) => state.addressGetApiReducer.getApiRes
  );

  const currencyData = useSelector(
    (state) => state.currencyStatusReducer.currencyStatus
  );

  const promotionDetailsRes = useSelector(
    (state) => state.getPromotionDetailsReducer.promotionDetailsRes
  );

  const initialValues = {
    financeModel: {
      currency: "",
      registrationstatus: "",
    },
    userModel: {
      companyId: getCompanyId(),
      designation: "",
      email: "",
      name: "",
      onWhatsapp: false,
      availableOn: "",
      primaryPhone: "",
      phoneCode: "",
      sameASUser: "No",
      roleModel: [
        {
          id: 6, // finance 6 role id
        },
      ],
    },
  };

  useEffect(() => {
    dispatch(getCountries());
    dispatch(getAvailableOn());
    dispatch(getAllUserDetails());
  }, [dispatch]);

  useEffect(() => {
    setSameAsAdmin(allUserDetails);
  }, [allUserDetails]);

  useEffect(() => {
    if (navigationResponse === null) {
      dispatch(uploadActions.emptyState());
    }
  }, [dispatch]);

  const [years, setYears] = useState([]);
  const [years_1, setYears_1] = useState([]);
  const [commonYear, setCommonYear] = useState([]);
  const [currencyValue_1, setCurrencyValue_1] = useState("NA");
  const [currencyValue_2, setCurrencyValue_2] = useState("NA");
  const [hide, setHide] = useState();
  const [allUsersData, setAllUsersData] = useState([]);
  const [formValues, setFormValues] = useState(initialValues);
  const [financerList, setFinancerList] = useState([]);
  const [financerListcount, setFinancerListCount] = useState([1]);
  const [activeEngineer, setActiveEngineer] = useState(1);
  const [errors, setErrors] = useState(false);

  const [isSaveBtnActive, setIsSaveBtnActive] = useState(false);
  const [isAddBidderandSaveBtnActive, setisAddBidderandSaveBtnActive] =
    useState(true);
  const [lastBidderBtnStatus, setlastBidderBtnStatus] = useState(false);
  const [lastBidderFilledData, setlastBidderFilledData] =
    useState(initialValues);
  const [isConfirmationModal, setIsConfirmationModal] = useState(false);
  const [isConfirmationAddNewFinancer, setIsConfirmationAddNewFinancer] =
    useState(false);
  const [sameAsAdmin, setSameAsAdmin] = useState([""]);
  const [dataResponse, setDataResponse] = useState(false);
  const [showTransactionType, setShowTransactionType] = useState(false);
  const [showPaymentRedirect, setShowPaymentRedirect] = useState(false);
  const [showInvoiceModal, setShowInvoiceModal] = useState(false);
  const [isUserClickedRemoved, setIsUserClickedRemoved] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);
  const [isNavigationData, setIsNavigationData] = useState(null);
  const [isShowPaymentRedirect, setIsShowPaymentRedirect] = useState(false);
  const [masterPromotionStragyId, setMasterPromotionStragyId] = useState("");
  const [isRegistrationSuccessfull, setIsRegistrationSuccessfull] =
    useState(false);
  const [onConfirmation, setOnConfirmation] = useState(false);

  const [turnoverModels, setTurnoverModels] = useState({
    docUpload: "",
    docname: "N/A",
    turnover: "",
    year: "NA",
    currency: "",
  });
  const [turnooverModels_1, setTurnooverModels_1] = useState({
    docUpload: "",
    docname: "N/A",
    turnover: "",
    year: "NA",
    currency: "",
  });

  useEffect(() => {
    if (navigationCheck) {
      if (financerRes !== null && financerRes.length !== 0) {
        if (masterPromotionStragyId === 2) {
          setShowTransactionType(!showTransactionType);
          // setIsShowPaymentRedirect(!isShowPaymentRedirect);
          // setTimeout(handlePaymentFlow, 2000);
        } else if (masterPromotionStragyId === 3) {
          setShowTransactionType(!showTransactionType);
          // setIsShowPaymentRedirect(!isShowPaymentRedirect);
          // setTimeout(handlePaymentFlow, 2000);
        }
      }
    }
  }, [financerRes]);

  useEffect(() => {
    if (financerRes !== null && paymentRequestRes !== null) {
      setIsRegistrationSuccessfull(!isRegistrationSuccessfull);
    }
  }, [financerRes, paymentRequestRes]);

  useEffect(() => {
    dispatch(getPromotionDetails());
  }, []);

  // const handlePaymentFlow = () => {
  //   setShowTransactionType(!showTransactionType);
  //   setIsShowPaymentRedirect(false);
  // };

  const paymentViaOnline = () => {
    if (
      (financerRes !== null || navigationResponse !== null) &&
      paymentRequestRes === null
    ) {
      history.push(`/payment`);
    } else {
      props.navigationHandler({
        currentForm: "payment",
        goForward: true,
      });
    }
  };

  const paymentViaOffline = () => {
    if (
      (financerRes !== null || navigationResponse !== null) &&
      paymentRequestRes === null
    ) {
      props.navigationHandler({
        currentForm: "invoice",
        goForward: true,
      });
    }
  };

  useEffect(() => {
    if (fileUploadIndex !== null) {
      if (fileUploadIndex === 0) {
        const transformedTurnOverModel = { ...turnoverModels };
        transformedTurnOverModel["docUpload"] = fileUploadRes?.fileKey;
        setTurnoverModels(transformedTurnOverModel);
      } else if (fileUploadIndex === 1) {
        const transformedTurnOverModel = { ...turnooverModels_1 };
        transformedTurnOverModel["docUpload"] = fileUploadRes?.fileKey;
        setTurnooverModels_1(transformedTurnOverModel);
      }
    }
  }, [fileUploadRes, fileUploadIndex]);
  useEffect(() => {
    dispatch(getCountries());
    dispatch(getAvailableOn());
    dispatch(getCurrencyCountryStatus());
    dispatch(getCurrencyStatus());
  }, [dispatch]);

  useEffect(() => {
    dispatch(getPaymentDetails());
  }, []);

  useEffect(() => {
    if (isCountryError !== "") {
      ToastError(isCountryError);
    } else if (isErroravailableOnRes !== "") {
      ToastError(isErroravailableOnRes);
    } else if (isErrorfinancerRes !== "") {
      ToastError(isErrorfinancerRes);
    }
  }, [isCountryError, isErroravailableOnRes, isErrorfinancerRes]);

  const getCompanyIdCheck = () => {
    if (getAuthData()?.companyId) {
      return getAuthData()?.companyId;
    }
    return getCompanyId();
  };

  useEffect(() => {
    if (registrationStatusKey - 8 > 0 && isFormTypePartner) {
      dispatch(commonGetAPiCall(`/finance/get/${getCompanyIdCheck()}`));
    } else if (registrationStatusKey - 8 > 0 && isFormTypePartnersIndividual) {
      dispatch(commonGetAPiCall(`/finance/get/${getCompanyIdCheck()}`));
    }
  }, [
    dispatch,
    registrationStatusKey,
    isFormTypePartner,
    isFormTypeCustomer,
    isFormTypePartnersIndividual,
  ]);

  useEffect(() => {
    if (!navigationCheck && financerRes !== null && isFormTypePartner) {
      dispatch(commonGetAPiCall(`/finance/get/${getCompanyId()}`));
    } else if (!navigationCheck && financerRes !== null && isFormTypeCustomer) {
      dispatch(commonGetAPiCall(`/customer/getByCompanyId/${getCompanyId()}`));
    } else if (
      !navigationCheck &&
      financerRes !== null &&
      isFormTypePartnersIndividual
    ) {
      dispatch(commonGetAPiCall(`/finance/get/${getCompanyId()}`));
    }
  }, [dispatch, navigationCheck, financerRes]);

  useEffect(() => {
    if (navigationResponse?.[0]?.["equipmentCategory"] === undefined) {
      if (navigationResponse !== null && Array.isArray(navigationResponse)) {
        setIsDisabled(true);
        setIsNavigationData(navigationResponse);
        setFinancerList(navigationResponse);
        setFormValues(navigationResponse[navigationResponse.length - 1]);
        let bidderCount = navigationResponse.map((item, index) => index + 1);
        setFinancerListCount(bidderCount);
        setActiveEngineer(navigationResponse.length);

        if (
          navigationResponse[0] &&
          navigationResponse[0]?.turnoverModels?.length > 0 &&
          navigationResponse[0]?.turnoverModels[0]
        ) {
          setTurnoverModels(navigationResponse[0]?.turnoverModels[0]);
          setTurnooverModels_1(navigationResponse[0]?.turnoverModels[1]);
        }
      }
    } else {
      setIsDisabled(false);
      setIsNavigationData(null);
    }
  }, [navigationResponse]);

  // const handleChangeTurnover = (e) => {
  //   const { name, value } = e.target;
  //   setTurnoverModels((prevState) => ({
  //     ...prevState,
  //     [name]: value,
  //   }));
  // };

  // const handleChangeTurnover_1 = (e) => {
  //   const { name, value } = e.target;
  //   setTurnooverModels_1((prevState) => ({
  //     ...prevState,
  //     [name]: value,
  //   }));
  // };

  const handleChangeTurnover = (e) => {
    const { name, value } = e.target;
    setTurnoverModels((prevState) => ({
      ...prevState,
      [name]: (Number(value.replace(/\D/g, "")) || "").toLocaleString(),
    }));
  };

  const handleChangeTurnover_1 = (e) => {
    const { name, value } = e.target;
    setTurnooverModels_1((prevState) => ({
      ...prevState,
      [name]: (Number(value.replace(/\D/g, "")) || "").toLocaleString(),
    }));
  };

  const handleChangeTurnoverSelect = (e) => {
    const { name, value } = e.target;

    if (value === "NA") {
      setTurnoverModels({
        docUpload: "",
        docname: "",
        turnover: "",
        year: "NA",
        currency: "",
      });
      setCurrencyValue_1("NA");
    } else {
      setTurnoverModels((prevState) => ({
        ...prevState,
        [name]: value,
      }));
    }
  };

  const handleChangeTurnoverSelect_1 = (e) => {
    const { name, value } = e.target;
    if (value === "NA") {
      setTurnoverModels({
        docUpload: "",
        docname: "",
        turnover: "",
        year: "NA",
        currency: "",
      });
      setCurrencyValue_1("NA");
    } else {
      setTurnooverModels_1((prevState) => ({
        ...prevState,
        [name]: value,
      }));
    }
  };

  const clearUploadHandler = (e) => {
    setTurnoverModels((prevState) => ({
      ...prevState,
      docUpload: "",
      docname: "",
    }));
  };

  const clearUploadHandler1 = (e) => {
    setTurnooverModels_1((prevState) => ({
      ...prevState,
      docUpload: "",
      docname: "",
    }));
  };

  useEffect(() => {
    const currentYear = new Date().getFullYear();
    const yearsArray = [];
    for (let i = currentYear; i > currentYear - 3; i--) {
      yearsArray.push(i.toString());
      yearsArray.sort();
    }
    setYears(yearsArray);
    setYears_1(yearsArray);
    setCommonYear(yearsArray);
  }, []);

  const handleChangeTurnoverYear = (e) => {
    const { name, value } = e.target;
    if (value === "NA") {
      setTurnoverModels({
        docUpload: "",
        docname: "",
        turnover: "",
        year: "NA",
        currency: "",
      });
      setCurrencyValue_1("NA");
    } else {
      setTurnoverModels((prevState) => ({
        ...prevState,
        [name]: value,
      }));
    }
  };
  const handleChangeTurnoverYear_1 = (e) => {
    const { name, value } = e.target;
    if (value === "NA") {
      setTurnooverModels_1({
        docUpload: "",
        docname: "",
        turnover: "",
        year: "NA",
        currency: "",
      });
      setCurrencyValue_2("NA");
    } else {
      setTurnooverModels_1((prevState) => ({
        ...prevState,
        [name]: value,
      }));
    }
  };

  const handleChangeToTurnover = (e) => {
    setTurnoverModels((prevState) => ({
      ...prevState,
      currency: e.value,
    }));
    setCurrencyValue_1(e.value);
  };
  const handleChangeToTurnover_1 = (e) => {
    setTurnooverModels_1((prevState) => ({
      ...prevState,
      currency: e.value,
    }));
    setCurrencyValue_2(e.value);
  };

  const getTokenFunc = () => {
    if (getAuthToken()) {
      return getAuthToken();
    }
    return getToken();
  };

  const handleInputChangePhone = (e) => {
    setFormValues({
      ...formValues,
      userModel: { ...formValues.userModel, phoneCode: e.value },
    });
  };
  const handleInputChangeAvailable = (e) => {
    setFormValues({
      ...formValues,
      userModel: { ...formValues.userModel, availableOn: e.value },
    });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    switch (name) {
      case "name":
        if (!Number(e.nativeEvent.data))
          setFormValues({
            ...formValues,
            userModel: { ...formValues.userModel, name: value },
          });
        break;

      case "designation":
        if (!Number(e.nativeEvent.data))
          setFormValues({
            ...formValues,
            userModel: { ...formValues.userModel, designation: value },
          });
        break;

      case "email":
        setFormValues({
          ...formValues,
          userModel: { ...formValues.userModel, email: value },
        });
        break;

      case "primaryPhone":
        if (value.length < 14)
          setFormValues({
            ...formValues,
            userModel: { ...formValues.userModel, primaryPhone: value },
          });
        break;

      case "onWhatsapp":
        setFormValues({
          ...formValues,
          userModel: {
            ...formValues.userModel,
            onWhatsapp: value === "No" ? false : true,
            availableOn: "",
          },
        });
        break;

      case "phoneCode":
        setFormValues({
          ...formValues,
          userModel: { ...formValues.userModel, phoneCode: value },
        });
        break;

      case "availableOn":
        setFormValues({
          ...formValues,
          userModel: { ...formValues.userModel, availableOn: value },
        });
        break;

      default:
      // do nothing
    }
  };

  //changed
  const saveData = (flag) => {
    if (flag === "already-exists") {
      let approvers = financerList.slice();
      approvers[activeEngineer - 1] = formValues;
      setFinancerList(approvers);
    } else {
      setFinancerList([
        ...financerList,
        {
          financeModel: {
            ...formValues.financeModel,
          },
          userModel: {
            ...formValues.userModel,
            primaryPhone: formValues.userModel.primaryPhone,
          },
          turnoverModels: [turnoverModels, turnooverModels_1],
        },
      ]);
    }
  };

  // changed
  const addFinancer = () => {
    if (!validateForm()) return false;

    if (financerList.length === 0) {
      if (!checkUniqueFinancer()) {
        return false;
      }
    }
    setlastBidderBtnStatus(false);
    if (activeEngineer === 1) {
      // saveData();
      // setFinancerListCount([
      //   ...financerListcount,
      //   Number(financerListcount.length) + 1,
      // ]);
      // setActiveEngineer(activeEngineer + 1);
      // setIsSaveBtnActive(true);
      // setisAddBidderandSaveBtnActive(false);
      // setFormValues(initialValues);
      setIsConfirmationAddNewFinancer(!isConfirmationAddNewFinancer);
    } else {
      // setFinancerListCount([
      //   ...financerListcount,
      //   Number(financerListcount.length) + 1,
      // ]);
      // setActiveEngineer(activeEngineer + 1);
      // setIsSaveBtnActive(true);
      // setisAddBidderandSaveBtnActive(false);
      // setFormValues(initialValues);
      setIsConfirmationAddNewFinancer(!isConfirmationAddNewFinancer);
    }
  };

  const approverSaveHandler = () => {
    if (!validateForm()) return false;

    if (!checkUniqueFinancer()) {
      return false;
    }

    if (isUserClickedRemoved) {
      if (financerList.length > 0) {
        setFinancerList([...financerList, formValues]);
        setIsUserClickedRemoved(false);
      }
    }

    if (activeEngineer < financerListcount.length) {
      saveData("already-exists");
      if (lastBidderBtnStatus === true) {
        setFormValues({ ...financerList[financerListcount.length - 1] });
        setActiveEngineer(financerListcount.length);
        setIsSaveBtnActive(false);
        setisAddBidderandSaveBtnActive(true);
      } else {
        setActiveEngineer(financerListcount.length);
        setIsSaveBtnActive(true);
        setisAddBidderandSaveBtnActive(false);
        setFormValues(lastBidderFilledData);
      }
    } else if (activeEngineer === financerListcount.length) {
      // saveData();
      if (isUserClickedRemoved) {
        setIsUserClickedRemoved(false);
      } else {
        saveData();
      }
      setIsSaveBtnActive(false);
      setisAddBidderandSaveBtnActive(true);
      if (activeEngineer === 1) {
        setlastBidderBtnStatus(false);
      } else {
        setlastBidderBtnStatus(true);
      }
    }
  };

  const makeApproverActive = (activeBidderSerial) => {
    if (activeEngineer === financerListcount.length) {
      if (isNavigationData) {
        setlastBidderFilledData(financerList[financerList.length - 1]);
      } else {
        setlastBidderFilledData(formValues);
      }
    }
    setActiveEngineer((prevState) => {
      innerApproverActive(activeBidderSerial);
      return activeBidderSerial;
    });
  };

  const innerApproverActive = (approverSerial) => {
    if (approverSerial === financerListcount.length) {
      setFormValues(lastBidderFilledData);
      if (lastBidderBtnStatus === true) {
        setIsSaveBtnActive(false);
        setisAddBidderandSaveBtnActive(true);
      } else {
        setIsSaveBtnActive(true);
        setisAddBidderandSaveBtnActive(false);
      }
    } else {
      setFormValues({ ...financerList[approverSerial - 1] });
      setIsSaveBtnActive(true);
      setisAddBidderandSaveBtnActive(false);
    }
  };

  const makeEngineerActive = (activeBidderSerial) => {
    if (activeEngineer === financerListcount.length) {
      if (isNavigationData) {
        setlastBidderFilledData(financerList[financerList.length - 1]);
      } else {
        setlastBidderFilledData(formValues);
      }
    }
    setActiveEngineer((prevState) => {
      innerApproverActive(activeBidderSerial);
      return activeBidderSerial;
    });
  };

  const onCancelModal = () => {
    setIsConfirmationAddNewFinancer(!isConfirmationAddNewFinancer);
  };

  const onConfirm = () => {
    setIsConfirmationAddNewFinancer(!isConfirmationAddNewFinancer);
    if (activeEngineer === 1) {
      saveData();
      setFinancerListCount([
        ...financerListcount,
        Number(financerListcount.length) + 1,
      ]);
      setAllUsersData([...allUsersData, formValues.userModel.primaryPhone]);
      setActiveEngineer(activeEngineer + 1);
      setIsSaveBtnActive(true);
      setisAddBidderandSaveBtnActive(false);
      setFormValues(initialValues);
      // removeOtherOption();
    } else {
      setFinancerListCount([
        ...financerListcount,
        Number(financerListcount.length) + 1,
      ]);
      setAllUsersData([...allUsersData, formValues.userModel.primaryPhone]);
      setActiveEngineer(activeEngineer + 1);
      setIsSaveBtnActive(true);
      setisAddBidderandSaveBtnActive(false);
      setFormValues(initialValues);
      // removeOtherOption();
    }
  };

  const onCancelHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
  };

  const onOkHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
    apiCall();
  };

  const onConfirmInovice = () => {
    setShowTransactionType(!showTransactionType);
    setShowInvoiceModal(!showInvoiceModal);
  };

  const cancelInvoice = () => {
    setShowInvoiceModal(!showInvoiceModal);
  };
  const confirmInvoice = () => {
    setShowInvoiceModal(!showInvoiceModal);
    offlinePayment();
    paymentViaOffline();
  };

  const onConfirmOnlinePayment = () => {
    setShowTransactionType(!showTransactionType);
    onlinePayment();
    paymentViaOnline();
  };

  const cancelTransactionType = () => {
    setShowTransactionType(!showTransactionType);
  };

  const validateForm = () => {
    if (
      formValues.userModel.name.trim() &&
      formValues.userModel.email.trim() &&
      formValues.userModel.primaryPhone.trim() &&
      numberValidation(formValues.userModel.primaryPhone) &&
      formValues.userModel.phoneCode &&
      emailValidation(formValues.userModel.email.trim()) &&
      ((formValues.userModel.onWhatsapp === false &&
        formValues.userModel.availableOn !== "") ||
      (formValues.userModel.onWhatsapp === true &&
        formValues.userModel.availableOn === "")
        ? true
        : false) &&
      (turnoverModels.year !== "NA"
        ? turnoverModels.currency !== "" &&
          // turnoverModels.docname !== "" &&
          turnoverModels.turnover !== ""
        : true) &&
      (turnooverModels_1.year !== "NA"
        ? turnooverModels_1.currency !== "" &&
          //  turnooverModels_1.docname !== "" &&
          turnooverModels_1.turnover !== ""
        : true)
    ) {
      setErrors(false);
      return true;
    } else {
      setErrors(true);
      return false;
    }
  };

  const validateFormIndividual = () => {
    if (
      (turnoverModels.year !== "NA"
        ? turnoverModels.currency !== "" &&
          // turnoverModels.docname !== "" &&
          turnoverModels.turnover !== ""
        : true) &&
      (turnooverModels_1.year !== "NA"
        ? turnooverModels_1.currency !== "" &&
          // turnooverModels_1.docname !== "" &&
          turnooverModels_1.turnover !== ""
        : true)
    ) {
      setErrors(false);
      return true;
    } else {
      setErrors(true);
      return false;
    }
  };

  const handleFormSubmission = (event) => {
    event.preventDefault();

    let flag = null;
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
        console.log("testing1");
        setMasterPromotionStragyId(1);
        setFormValues({
          ...formValues,
          financeModel: { ...formValues.financeModel, registrationstatus: 1 },
        });
        flag = 1;
      } else if (
        new Date(`${promotionDetailsRes[1]?.startedDate}`).valueOf() <
          new Date(
            `${moment(new Date()).format("YYYY-MM-DD").toString()}`
          ).valueOf() &&
        new Date(
          `${moment(new Date()).format("YYYY-MM-DD").toString()}`
        ).valueOf() < new Date(`${promotionDetailsRes[1]?.endDate}`).valueOf()
      ) {
        // setMasterPromotionStragyId(2);
        flag = 2;
      } else if (
        new Date(`${promotionDetailsRes[2]?.startedDate}`).valueOf() <
          new Date(
            `${moment(new Date()).format("YYYY-MM-DD").toString()}`
          ).valueOf() &&
        new Date(
          `${moment(new Date()).format("YYYY-MM-DD").toString()}`
        ).valueOf() < new Date(`${promotionDetailsRes[2]?.endDate}`).valueOf()
      ) {
        flag = 3;
      }
    }

    if (flag === 1) {
      if (isFormTypePartner || isFormTypeCustomer) {
        if (validateForm()) {
          setOnConfirmation(!onConfirmation);
        }
      } else if (isFormTypePartnersIndividual) {
        if (validateFormIndividual()) {
          setOnConfirmation(!onConfirmation);
        }
      }
    }

    if (flag !== 1) {
      if (isFormTypePartner || isFormTypeCustomer) {
        if (validateForm()) {
          setIsConfirmationModal(!isConfirmationModal);
        }
      } else if (isFormTypePartnersIndividual) {
        if (validateFormIndividual()) {
          setIsConfirmationModal(!isConfirmationModal);
        }
      }
    }
  };

  //   if (isFormTypePartner) {
  //     if (validateForm()) {
  //       setIsConfirmationModal(!isConfirmationModal);
  //     }
  //   } else if (isFormTypePartnersIndividual) {
  //     if (validateFormIndividual()) {
  //       setIsConfirmationModal(!isConfirmationModal);
  //     }
  //   }
  // };

  const checkUniqueFinancer = () => {
    let emailFlag = false,
      phoneFlag = false;
    for (let i = 0; i < financerList.length; i++) {
      if (i === activeEngineer - 1) {
      } else {
        if (
          financerList[i].userModel.email === formValues.userModel.email &&
          financerList[i].userModel.primaryPhone ===
            formValues.userModel.primaryPhone
        ) {
          emailFlag = true;
          phoneFlag = true;
        } else if (
          financerList[i].userModel.email === formValues.userModel.email
        ) {
          emailFlag = true;
        } else if (
          financerList[i].userModel.primaryPhone ===
          formValues.userModel.primaryPhone
        ) {
          phoneFlag = true;
        }
      }
    }

    if (emailFlag && phoneFlag) {
      ToastError(`Duplicate Financer`);
      return false;
    } else if (emailFlag) {
      ToastError(`Duplicate Financer`);
      return false;
    } else if (phoneFlag) {
      ToastError(`Duplicate Financer`);
      return false;
    } else {
      return true;
    }
  };

  useEffect(() => {
    if (isFormTypePartnersIndividual) {
      dispatch(addressGetAPiCall(`/vendor/get/${getCompanyIdCheck()}`));
    }
  }, [dispatch, isFormTypePartnersIndividual]);

  useEffect(() => {
    if (isFormTypePartnersIndividual) {
      if (sameAsAdmin?.length === 0) {
        setFormValues({
          ...formValues,
          userModel: {
            ...formValues.userModel,
            name: addressData?.userModel?.name,
            email: addressData?.userModel?.email,
            phoneCode: addressData?.userModel?.phoneCode,
            primaryPhone: addressData?.userModel?.primaryPhone,
            onWhatsapp: addressData?.userModel?.onWhatsapp,
            availableOn: addressData?.userModel?.availableOn,
            sameASUser: "Yes",
          },
        });
      } else {
        setFormValues({
          ...formValues,
          userModel: {
            ...formValues.userModel,
            name: sameAsAdmin?.[0]?.name,
            email: sameAsAdmin?.[0]?.email,
            phoneCode: sameAsAdmin?.[0]?.phoneCode,
            primaryPhone: sameAsAdmin?.[0]?.primaryPhone,
            onWhatsapp: sameAsAdmin?.[0]?.onWhatsapp,
            availableOn: sameAsAdmin?.[0]?.availableOn,
            sameASUser: "Yes",
          },
        });
      }
    }
  }, [isFormTypePartnersIndividual, sameAsAdmin]);

  const apiCall = () => {
    if (isFormTypePartner) {
      if (
        formValues.userModel.name &&
        formValues.userModel.email &&
        emailValidation(formValues.userModel.email) &&
        formValues.userModel.primaryPhone
      ) {
        let customTurnOver = null;
        if (turnoverModels.year === "NA" && turnooverModels_1.year !== "NA") {
          customTurnOver = [
            {
              docUpload: "",
              docname: "",
              turnover: "",
              year: "",
              currency: "",
            },
            turnooverModels_1,
          ];
        } else if (
          turnoverModels.year !== "NA" &&
          turnooverModels_1.year === "NA"
        ) {
          customTurnOver = [
            turnoverModels,
            {
              docUpload: "",
              docname: "",
              turnover: "",
              year: "",
              currency: "",
            },
          ];
        } else {
          customTurnOver = [turnoverModels, turnooverModels_1];
        }

        if (financerList?.length === 0) {
          let vendorArray = [
            {
              financeModel: formValues.financeModel,
              userModel: formValues.userModel,
              turnoverModels: customTurnOver,
            },
          ];

          dispatch(saveFinancer(vendorArray, "finance/add"));
          return;
        } else {
          const financerListClone = JSON.parse(JSON.stringify(financerList));
          const transformedLastFinancerObject = {
            ...formValues,
            turnoverModels: customTurnOver,
          };
          financerListClone[financerListClone.length - 1] =
            transformedLastFinancerObject;

          dispatch(saveFinancer(financerListClone, "finance/add"));
        }
      }
    } else if (isFormTypeCustomer) {
      if (
        formValues.userModel.name &&
        formValues.userModel.email &&
        emailValidation(formValues.userModel.email) &&
        formValues.userModel.primaryPhone
      ) {
        if (financerList?.length === 0) {
          let customerArray = [
            {
              financeModel: formValues.financeModel,
              userModel: formValues.userModel,
              turnoverModels: [turnoverModels, turnooverModels_1],
            },
          ];

          dispatch(saveFinancer(customerArray, "customer/addFinance"));
          return;
        }
        dispatch(saveFinancer(financerList, "customer/addFinance"));
      }
    } else if (isFormTypePartnersIndividual) {
      let individualArray = [
        {
          financeModel: formValues.financeModel,
          userModel: formValues.userModel,
          turnoverModels: [turnoverModels, turnooverModels_1],
        },
      ];

      dispatch(saveFinancer(individualArray, "finance/add"));
    }
  };

  const uploadSelectedFile = (e, index = null) => {
    const allSelectedFiles = e.target.files;
    const numberOfSelectedFiles = e.target.files.length;
    const allowedFileExtensions = e.target
      .getAttribute("data-allowed-file-extension")
      .split(",")
      .map((ext) => ext.trim());

    let isFileForNotAllowed = false;
    let isFileForNotAllowedText = "";
    if (numberOfSelectedFiles > 0 && numberOfSelectedFiles <= 1) {
      for (let i = 0; i < numberOfSelectedFiles; i++) {
        const fileName = allSelectedFiles.item(i).name;
        const fileSize = allSelectedFiles.item(i).size;
        const fileType = allSelectedFiles.item(i).type;

        const selectedFileExtension = fileType.split("/")[1];
        if (!allowedFileExtensions.includes(selectedFileExtension)) {
          isFileForNotAllowed = true;
          isFileForNotAllowedText = `fileName: ${fileName} file is not allowed. Acceptable format png, jpg, jpeg, pdf.\n`;
        } else if (fileSize > 3145728) {
          isFileForNotAllowed = true;
          isFileForNotAllowedText = `fileName: ${fileName}  size should be less than 3 mb.\n`;
        }
      }
    }
    if (isFileForNotAllowed) {
      // error in selected file
      ToastError(isFileForNotAllowedText);
    } else {
      // all right thump's up
      const formData = new FormData();
      formData.append("file", allSelectedFiles[0]);
      dispatch(
        fileUpload(
          formData,
          `file/upload?fileKey=${
            allSelectedFiles.item(0).name
          }&token=${getTokenFunc()}`,
          index
        )
      );
    }
    // Always put it into last of the function, otherwise same file doesn't upload multiple times
    e.target.value = "";
  };

  // const nextNavigationHandler = () => {
  //     dispatch(saveFinancerSlice.serviceNavigation(true));
  //     dispatch(commonGetApiActions.dispatchGetRequest());
  //     props.navigationHandler({
  //         currentForm: "finance",
  //         goForward: true,
  //     })
  // }

  // const nextNavigationHandler = () => {
  //   if (
  //     JSON.parse(localStorage.getItem(`authData`))?.registeredSuccessfully ===
  //     undefined
  //   ) {
  //     if (paymentRequestRes !== null) {
  //       props.navigationHandler({
  //         currentForm: "payment",
  //         goForward: true,
  //       });
  //     }
  //   }
  //   if (JSON.parse(localStorage.getItem(`authData`))?.registeredSuccessfully) {
  //     history.push(`/dashboard`);
  //   } else {
  //     history.push(`/payment`);
  //   }
  // };

  const nextNavigationHandler = () => {
    if (
      JSON.parse(localStorage.getItem(`authData`))?.registeredSuccessfully ===
      undefined
    ) {
      if (paymentRequestRes !== null) {
        props.navigationHandler({
          currentForm: "payment",
          goForward: true,
        });
      }
    }
    if (JSON.parse(localStorage.getItem(`authData`))?.registeredSuccessfully) {
      history.push(`/dashboard`);
    } else {
      setShowTransactionType(!showTransactionType);
      // setIsShowPaymentRedirect(!isShowPaymentRedirect);
      // setTimeout(handlePaymentFlow, 2000);
    }
  };

  // useEffect(() => {
  //   if (navigationCheck) {
  //     if (financerRes !== null) {
  //       props.setFormLabel("Payment", 5);
  //     }
  //   }
  // }, [financerRes, props, navigationCheck]);

  const onlinePayment = () => {
    if (financerRes !== null || navigationResponse !== null) {
      props.setFormLabel("Payment", 5);
    }
  };

  const offlinePayment = () => {
    if (financerRes !== null || navigationResponse !== null) {
      props.setFormLabel("invoice", 5);
    }
  };

  const options = countries?.map((country) => ({
    value: country.phoneCode,
    label: [
      <img
        src={country.flag}
        style={{ width: "20px", marginRight: "15px" }}
        alt="flag"
      />,
      <span style={{ marginRight: "12px", fontFamily: "Poppins-Regular" }}>
        {country.phoneCode}
      </span>,
      <span style={{ fontFamily: "Poppins-Regular" }}>{country.name}</span>,
    ],
  }));

  const optionCurrency = currencyData?.map((country) => ({
    value: country.currency,
    label: country.currency,
  }));

  const socialMedia = availableOnRes?.map((availableAt) => ({
    value: availableAt.app_Name,
    label: availableAt.app_Name,
  }));

  const validateEmail = (e) => {
    const regex =
      /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
    if (e.target.value === "") {
      return false;
    } else if (regex.test(e.target.value) === false) {
      return false;
    }
    const data = {
      email: e.target.value,
      phone: null,
    };
    dispatch(validateEmailPhone(data, `user/validateEmailAndPhone`));
  };

  const validatePhone = (e) => {
    if (e.target.value === "") {
      return false;
    } else if (e.target.value.length < 5 && e.target.value.length < 14) {
      return false;
    }
    const data = {
      phoneNumber: e.target.value,
      email: null,
    };
    dispatch(validateEmailPhone(data, `user/validateEmailAndPhone`));
  };

  const customStyles = {
    container: (base, state) => ({
      ...base,
    }),
    menu: (styles) => ({
      ...styles,
      width: "310px",
    }),
  };

  const handleSelectChange = (e) => {
    const val = e.value;
    setFormValues({
      ...formValues,
      userModel: {
        ...formValues.userModel,
        name: val[0],
        email: val[1],
        phoneCode: val[2],
        primaryPhone: val[3],
        onWhatsapp: val[4],
        availableOn: val[5],
        designation: val[6],
        sameASUser: "Yes",
      },
    });
    setDataResponse(true);
  };

  const removeOtherOption = () => {
    setFormValues({
      ...formValues,
      userModel: {
        ...formValues.userModel,
        name: "",
        email: "",
        phoneCode: "",
        primaryPhone: "",
        onWhatsapp: false,
        availableOn: "",
        sameASUser: "No",
        designation: "",
      },
    });
    setDataResponse(false);
    const id = allUsersData.indexOf(formValues?.userModel?.primaryPhone);
    const dataRemove = allUsersData.splice(id, 1);
    setAllUsersData(allUsersData);
  };

  const adminsData = sameAsAdmin
    ?.filter((item) => !allUsersData?.includes(item.primaryPhone))
    ?.map((index) => ({
      value: [
        index.name,
        index.email,
        index.phoneCode,
        index.primaryPhone,
        index.onWhatsapp,
        index.availableOn,
        index.designation,
      ],
      label: [
        <span
          style={{ fontFamily: "Poppins-Regular", textTransform: "capitalize" }}
        >
          {index.name}
        </span>,
        <br />,
        <span
          style={{ fontFamily: "Poppins-Regular", textTransform: "lowercase" }}
        >
          {index.email}
        </span>,
      ],
    }));

  const removeFinance = () => {
    setActiveEngineer(activeEngineer - 1);
    const trasformedFinanceCount = [...financerListcount];
    trasformedFinanceCount.splice(trasformedFinanceCount.length - 1, 1);
    setFinancerListCount(trasformedFinanceCount);
    setFormValues(financerList[financerList.length - 1]);

    const transformedFinaneList = [...financerList];
    transformedFinaneList.pop();
    setFinancerList(transformedFinaneList);
    setIsUserClickedRemoved(true);
  };

  const redirectHandler = () => {
    window.location.href = "/";
  };

  const onCancelHandlerFinance = () => {
    setOnConfirmation(!onConfirmation);
  };

  const onOkHandlerFinance = () => {
    setOnConfirmation(!onConfirmation);
    apiCall();
    const data = {
      amount: 0,
      emailSendToFinance: null,
      currency: null,
      tokenStripe: null,
      transactionCardDeatils: null,
      user_id: getUserId() === null ? getCompanyId() : getUserId(),
      masterPromotionStragyId: 1,
      userReferalCode: null,
    };

    console.log({ data });
    dispatch(paymentRequest(data, `payment/createPayment`));
  };

  return (
    <div className="form_container_area">
      <div className="finance_form positioning_container">
        {isRegistrationSuccessfull && (
          <RegistrationSuccessFull redirect={redirectHandler} />
        )}

        {(isLoading ||
          isAvailableOnLoading ||
          isLoading_countries ||
          fileUploadResIsLoading) && <Loading text="Loading..." />}

        {onConfirmation && (
          <ConfirmationModal
            cancel={onCancelHandlerFinance}
            ok={onOkHandlerFinance}
          />
        )}
        {/* {isConfirmationModal && (
          <EngineerPayment
            cancel={onCancelHandler}
            ok={onOkHandler}
            totalEngineer={paymentDetails?.totalEngineer}
            totalPayment={paymentDetails?.totalAmount}
          />
        )} */}
        {isConfirmationModal && (
          <PaymentDetailsByInoice
            cancel={onCancelHandler}
            ok={onOkHandler}
            totalEngineer={paymentDetails?.totalEngineer}
            totalPayment={paymentDetails?.totalAmount}
            name={paymentDetails?.name}
            address={paymentDetails?.address}
            invoiceNumber={paymentDetails?.invoiceNumber}
          />
        )}
        {isShowPaymentRedirect && <PaymentRedirect />}
        {isConfirmationAddNewFinancer && (
          <ConfirmationAddNewFinancer cancel={onCancelModal} ok={onConfirm} />
        )}
        {showTransactionType && (
          <TransactionType
            onInvoice={onConfirmInovice}
            onPayment={onConfirmOnlinePayment}
            cancelTransactionType={cancelTransactionType}
          />
        )}
        {showPaymentRedirect && <PaymentRedirect />}
        {showInvoiceModal && (
          <InvoiceModal
            cancelInvoice={cancelInvoice}
            confirmInvoice={confirmInvoice}
            totalEngineer={paymentDetails?.totalEngineer}
            totalPayment={paymentDetails?.totalAmount}
            name={paymentDetails?.name}
            address={paymentDetails?.address}
            invoiceNumber={paymentDetails?.invoiceNumber}
          />
        )}

        <div className="company_id_details">
          <p className="company_id">Company id: {getReferralCode()}</p>
        </div>

        {/* ---------------- Partner form ---------------- */}
        {isFormTypePartner && (
          <form onSubmit={handleFormSubmission}>
            <div className="form_container">
              <div className="form_control_spread">
                <label className="form_label">
                  Upload last 2 years documents indicating Company Turnover
                </label>
                <div
                  className={
                    navigationResponse !== null
                      ? "equipment_container"
                      : "equipment_container set_background"
                  }
                >
                  <div className="equipment_container_heading">
                    <span className="heading a_year">
                      Year
                      {/* <span className="lbl_mandatory">*</span> */}
                    </span>
                    <span className="heading a_turnover">
                      Turnover
                      {/* <span className="lbl_mandatory">*</span> */}
                    </span>
                    <span className="heading a_supported_document">
                      Supported Document
                    </span>
                    <span className="heading a_upload_document">
                      Upload Document
                    </span>
                  </div>

                  <div className="equipment_container_body_row">
                    {/* web view rows */}
                    <div className="no_carousel">
                      <div className="equipment_container_body">
                        <div className="equipment_col a_year">
                          <select
                            className="form_control"
                            name="year"
                            value={turnoverModels?.year}
                            onChange={handleChangeTurnoverYear}
                            disabled={isDisabled}
                          >
                            <option value="NA" selected>
                              NA
                            </option>
                            {commonYear
                              .filter(
                                (item) =>
                                  !item.includes(turnooverModels_1?.year)
                              )
                              .map((item) => (
                                <option key={item} value={item}>
                                  {item}
                                </option>
                              ))}
                          </select>
                          {/* {
                           errors && turnoverModels.year !== "NA" && <span className="error">Year is required</span>
                          } */}
                        </div>
                        {/* web  turnover partner-1  start  */}
                        <div className="equipment_col a_turnover">
                          <div className="input_container">
                            {isNavigationData ||
                            turnoverModels?.year === `NA` ? (
                              <select
                                className="select_input"
                                disabled={
                                  isDisabled || turnoverModels?.year === `NA`
                                }
                                style={{ padding: "0px 0px 0px 12px" }}
                              >
                                <option value="" selected>
                                  NA
                                </option>
                                <option>{turnoverModels?.currency}</option>
                              </select>
                            ) : (
                              <Select
                                name="currency"
                                className="select_input"
                                onChange={handleChangeToTurnover}
                                classNamePrefix="select-currency"
                                disabled={isDisabled}
                                options={optionCurrency}
                                value={{
                                  value: currencyValue_1,
                                  label: currencyValue_1,
                                }}
                              ></Select>
                            )}
                            <input
                              type="text"
                              className="form_control_phone"
                              name="turnover"
                              value={turnoverModels?.turnover}
                              placeholder="NA"
                              onChange={(e) => {
                                if (e.target.value.length <= 15)
                                  handleChangeTurnover(e);
                              }}
                              disabled={
                                isDisabled || turnoverModels?.year === `NA`
                              }
                            />
                          </div>
                          {/* {errors &&
                            turnoverModels.year !== "NA" &&
                            (turnoverModels.turnover === "" ||
                              turnoverModels.currency === "") && (
                              <span className="error">
                                Turnover is required
                              </span>
                            )} */}
                        </div>
                        <div className="equipment_col a_supported_document">
                          <select
                            className="form_control"
                            name="docname"
                            value={turnoverModels?.docname}
                            onChange={handleChangeTurnoverSelect}
                            disabled={
                              isDisabled || turnoverModels?.year === `NA`
                            }
                            defaultValue="N/A"
                          >
                            <option value="N/A" selected>
                              NA
                            </option>
                            <option value="Annual Report">Annual Report</option>
                            <option value="Audited  Financial Statement">
                              Audited Financial Statement
                            </option>
                            <option value="Others">Others</option>
                          </select>
                        </div>
                        {/* doc upload web 1 */}
                        <div className="equipment_col a_upload_document">
                          <label
                            className={
                              isNavigationData
                                ? "a_upload uploadFileName"
                                : "a_upload uploadFileName set_background"
                            }
                            htmlFor="file-upload_0"
                          >
                            {turnoverModels?.docUpload
                              ? turnoverModels?.docUpload?.split("/")[1]
                              : "Upload"}
                          </label>
                          <input
                            id="file-upload_0"
                            type="file"
                            accept=".png, .jpg, .jpeg, .pdf"
                            data-allowed-file-extension="png, jpg, jpeg, pdf"
                            onChange={(e) => uploadSelectedFile(e, 0)}
                            disabled={
                              isDisabled || turnoverModels?.year === `NA`
                            }
                          />
                          {navigationResponse !== null
                            ? ""
                            : turnoverModels?.docUpload && (
                                <div
                                  onClick={(e) => clearUploadHandler(e)}
                                  className="remove_logo_container"
                                >
                                  <img
                                    src={cancelIcon}
                                    alt="cancelIcon"
                                    className="cancel_icon"
                                  />
                                </div>
                              )}
                        </div>
                      </div>
                      <div className="equipment_container_body">
                        <div className="equipment_col a_year">
                          <select
                            className="form_control"
                            name="year"
                            value={turnooverModels_1?.year}
                            onChange={handleChangeTurnoverYear_1}
                            disabled={isDisabled}
                          >
                            <option value="NA" selected>
                              NA
                            </option>
                            {commonYear
                              .filter(
                                (item) => !item.includes(turnoverModels?.year)
                              )
                              .map((item) => (
                                <option key={item} value={item}>
                                  {item}
                                </option>
                              ))}
                          </select>
                          {/* {
                                                        errors && turnooverModels_1.year === "NA" && <span className="error">Year is required</span>
                                                    } */}
                        </div>
                        <div className="equipment_col a_turnover">
                          {/* web  turnover-2 partner start  */}
                          <div className="input_container">
                            {isNavigationData ||
                            turnooverModels_1?.year === `NA` ? (
                              <select
                                className="select_input"
                                style={{ padding: "0px 0px 0px 12px" }}
                                disabled={
                                  isDisabled || turnooverModels_1?.year === `NA`
                                }
                              >
                                <option disabled selected>
                                  NA
                                </option>
                                <option>{turnooverModels_1?.currency}</option>
                              </select>
                            ) : (
                              <Select
                                name="currency"
                                className="select_input"
                                onChange={handleChangeToTurnover_1}
                                disabled={isDisabled}
                                classNamePrefix="select-currency"
                                options={optionCurrency}
                                value={{
                                  value: currencyValue_2,
                                  label: currencyValue_2,
                                }}
                              ></Select>
                            )}

                            <input
                              type="text"
                              className="form_control_phone"
                              name="turnover"
                              value={turnooverModels_1?.turnover}
                              placeholder="NA"
                              onChange={(e) => {
                                if (e.target.value.length <= 15)
                                  handleChangeTurnover_1(e);
                              }}
                              disabled={
                                isDisabled || turnooverModels_1?.year === `NA`
                              }
                            />
                          </div>
                          {/* {errors &&
                            turnooverModels_1.year !== "NA" &&
                            (turnooverModels_1.turnover === "" ||
                              turnooverModels_1.currency === "") && (
                              <span className="error">
                                Turnover is required
                              </span>
                            )} */}
                        </div>
                        <div className="equipment_col a_supported_document">
                          <select
                            className="form_control"
                            name="docname"
                            value={turnooverModels_1?.docname}
                            onChange={handleChangeTurnoverSelect_1}
                            disabled={
                              isDisabled || turnooverModels_1?.year === `NA`
                            }
                          >
                            <option value="" selected>
                              NA
                            </option>
                            <option value="Annual Report">Annual Report</option>
                            <option value="Audited  Financial Statement">
                              Audited Financial Statement
                            </option>
                            <option value="Others">Others</option>
                          </select>
                        </div>
                        <div className="equipment_col a_upload_document">
                          <label
                            className={
                              isNavigationData
                                ? "a_upload uploadFileName"
                                : "a_upload uploadFileName set_background"
                            }
                            htmlFor="file-upload_1"
                          >
                            {turnooverModels_1?.docUpload
                              ? turnooverModels_1?.docUpload?.split("/")[1]
                              : "Upload"}
                          </label>
                          <input
                            id="file-upload_1"
                            type="file"
                            accept=".png, .jpg, .jpeg, .pdf"
                            data-allowed-file-extension="png, jpg, jpeg, pdf"
                            onChange={(e) => uploadSelectedFile(e, 1)}
                            disabled={
                              isDisabled || turnooverModels_1?.year === `NA`
                            }
                          />
                          {navigationResponse !== null
                            ? ""
                            : turnooverModels_1?.docUpload && (
                                <div
                                  onClick={(e) => clearUploadHandler1(e)}
                                  className="remove_logo_container"
                                >
                                  <img
                                    src={cancelIcon}
                                    alt="cancelIcon"
                                    className="cancel_icon"
                                  />
                                </div>
                              )}
                        </div>
                      </div>
                    </div>
                    <div className="display_carousel">
                      {/* mobile view carousel */}
                      <AwesomeSlider className="aws-btn ">
                        <div className="equipment_container_body">
                          {/* carousel patner -1 row */}
                          <div className="equipment_col a_year">
                            <label className="mobile_label">Year</label>
                            <select
                              className="form_control"
                              name="year"
                              value={turnoverModels?.year}
                              onChange={handleChangeTurnoverYear}
                              disabled={isDisabled}
                            >
                              <option value="NA" selected disabled>
                                NA
                              </option>
                              {commonYear
                                .filter(
                                  (item) =>
                                    !item.includes(turnooverModels_1?.year)
                                )
                                .map((item) => (
                                  <option key={item} value={item}>
                                    {item}
                                  </option>
                                ))}
                            </select>
                            {/* {
                                                            errors && turnoverModels.year === "NA" && <span className="error">Year is required</span>
                                                        } */}
                          </div>
                          <div className="equipment_col a_turnover">
                            <label className="mobile_label">Turnover</label>
                            <div className="input_container">
                              {isNavigationData ||
                              turnoverModels?.year === `NA` ? (
                                <select
                                  className="select_input"
                                  disabled={
                                    isDisabled || turnoverModels?.year === `NA`
                                  }
                                >
                                  <option selected disabled>
                                    NA
                                  </option>
                                  <option>{turnoverModels?.currency}</option>
                                </select>
                              ) : (
                                <Select
                                  name="currency"
                                  className="select_input"
                                  onChange={handleChangeToTurnover}
                                  disabled={isDisabled}
                                  classNamePrefix="select-currency"
                                  options={optionCurrency}
                                  value={{
                                    value: currencyValue_1,
                                    label: currencyValue_1,
                                  }}
                                ></Select>
                              )}

                              {/* <select
                                className="select_input"
                                //className="form_control"
                                name="currency"
                                value={turnoverModels?.currency}
                                onChange={handleChangeTurnover}
                                disabled={
                                  isDisabled
                                }
                              >
                                <option value="" disabled selected></option>
                                {countries?.map((country) => (
                                  <option
                                    value={country.currency}
                                    key={country.currency}
                                  >
                                    {country.currency}
                                  </option>
                                ))}
                              </select> */}
                              <input
                                type="text"
                                className="form_control_phone"
                                name="turnover"
                                value={turnoverModels?.turnover}
                                placeholder="NA"
                                onChange={(e) => {
                                  if (e.target.value.length <= 15)
                                    handleChangeTurnover(e);
                                }}
                                disabled={
                                  isDisabled || turnoverModels?.year === `NA`
                                }
                              />
                            </div>
                            {/* {errors &&
                              turnoverModels.year !== "NA" &&
                              (turnoverModels.turnover === "" ||
                                turnoverModels.currency === "") && (
                                <span className="error">
                                  Turnover is required
                                </span>
                              )} */}
                          </div>
                          <div className="equipment_col a_supported_document">
                            <label className="mobile_label">
                              Supported Document
                            </label>
                            <select
                              className="form_control"
                              name="docname"
                              value={turnoverModels?.docname}
                              onChange={handleChangeTurnoverSelect}
                              disabled={
                                isDisabled || turnoverModels?.year === `NA`
                              }
                              defaultValue="N/A"
                            >
                              <option value="N/A" selected>
                                NA
                              </option>

                              <option value="Annual Report">
                                Annual Report
                              </option>
                              <option value="Audited  Financial Statement">
                                Audited Financial Statement
                              </option>
                              <option value="Others">Others</option>
                            </select>
                          </div>
                          <div className="equipment_col a_upload_document">
                            <label className="mobile_label">
                              Supported Document
                            </label>
                            <label
                              className={
                                isNavigationData
                                  ? "a_upload uploadFileName"
                                  : "a_upload uploadFileName set_background"
                              }
                              htmlFor="file-upload_0"
                            >
                              {turnoverModels?.docUpload
                                ? turnoverModels?.docUpload?.split("/")[1]
                                : "Upload"}
                            </label>
                            <input
                              id="file-upload_0"
                              type="file"
                              accept=".png, .jpg, .jpeg, .pdf"
                              data-allowed-file-extension="png, jpg, jpeg, pdf"
                              onChange={(e) => uploadSelectedFile(e, 0)}
                              disabled={
                                isDisabled || turnoverModels?.year === `NA`
                              }
                            />
                            {navigationResponse !== null
                              ? ""
                              : turnoverModels?.docUpload && (
                                  <div
                                    onClick={(e) => clearUploadHandler(e)}
                                    className="remove_logo_container"
                                  >
                                    <img
                                      src={cancelIcon}
                                      alt="cancelIcon"
                                      className="cancel_icon"
                                    />
                                  </div>
                                )}
                          </div>
                        </div>
                        {/* carousel patner -2 row */}
                        <div className="equipment_container_body">
                          <div className="equipment_col a_year">
                            <label className="mobile_label">Year</label>
                            <select
                              className="form_control"
                              name="year"
                              value={turnooverModels_1?.year}
                              onChange={handleChangeTurnoverYear_1}
                              disabled={isDisabled}
                            >
                              <option selected disabled>
                                NA
                              </option>
                              {commonYear
                                .filter(
                                  (item) => !item.includes(turnoverModels?.year)
                                )
                                .map((item) => (
                                  <option key={item} value={item}>
                                    {item}
                                  </option>
                                ))}
                            </select>
                            {/* {
                                                            errors && turnooverModels_1.year === "NA" && <span className="error">Year is required</span>
                                                        } */}
                          </div>
                          <div className="equipment_col a_turnover">
                            <label className="mobile_label">Turnover </label>
                            <div className="input_container">
                              {isNavigationData ||
                              turnooverModels_1?.year === `NA` ? (
                                <select
                                  className="select_input"
                                  disabled={
                                    isDisabled ||
                                    turnooverModels_1?.year === `NA`
                                  }
                                >
                                  <option disabled selected>
                                    NA
                                  </option>
                                  <option>{turnooverModels_1?.currency}</option>
                                </select>
                              ) : (
                                <Select
                                  name="currency"
                                  className="select_input"
                                  onChange={handleChangeToTurnover_1}
                                  disabled={isDisabled}
                                  classNamePrefix="select-currency"
                                  options={optionCurrency}
                                  value={{
                                    value: currencyValue_2,
                                    label: currencyValue_2,
                                  }}
                                ></Select>
                              )}

                              {/* <select
                                className="select_input"
                                name="currency"
                                value={turnoverModels?.currency}
                                onChange={handleChangeTurnover}
                                disabled={
                                  isDisabled
                                }
                              >
                                <option value="" disabled selected></option>
                                {countries?.map((country) => (
                                  <option
                                    value={country.currency}
                                    key={country.currency}
                                  >
                                    {country.currency}
                                  </option>
                                ))}
                              </select> */}
                              <input
                                type="text"
                                className="form_control_phone"
                                placeholder="NA"
                                name="turnover"
                                value={turnooverModels_1?.turnover}
                                onChange={(e) => {
                                  if (e.target.value.length <= 15)
                                    handleChangeTurnover_1(e);
                                }}
                                disabled={
                                  isDisabled || turnooverModels_1?.year === `NA`
                                }
                              />
                            </div>
                            {/* {errors &&
                              turnooverModels_1.year !== "NA" &&
                              (turnooverModels_1.turnover === "" ||
                                turnooverModels_1.currency === "") && (
                                <span className="error">
                                  Turnover is required
                                </span>
                              )} */}
                          </div>
                          <div className="equipment_col a_supported_document">
                            <label className="mobile_label">
                              Supported Document
                            </label>
                            <select
                              className="form_control"
                              name="docname"
                              value={turnooverModels_1?.docname}
                              onChange={handleChangeTurnoverSelect_1}
                              disabled={
                                isDisabled || turnooverModels_1?.year === `NA`
                              }
                              defaultValue="N/A"
                            >
                              <option value="N/A" selected>
                                NA
                              </option>
                              <option value="Annual Report">
                                Annual Report
                              </option>
                              <option value="Audited  Financial Statement">
                                Audited Financial Statement
                              </option>
                              <option value="Others">Others</option>
                            </select>
                          </div>
                          <div className="equipment_col a_upload_document">
                            <label className="mobile_label">
                              Supported Document
                            </label>
                            <label
                              className={
                                isNavigationData
                                  ? "a_upload uploadFileName"
                                  : "a_upload uploadFileName set_background"
                              }
                              htmlFor="file-upload_1"
                            >
                              {turnooverModels_1?.docUpload
                                ? turnooverModels_1?.docUpload?.split("/")[1]
                                : "Upload"}
                            </label>
                            <input
                              id="file-upload_1"
                              type="file"
                              accept=".png, .jpg, .jpeg, .pdf"
                              data-allowed-file-extension="png, jpg, jpeg, pdf"
                              onChange={(e) => uploadSelectedFile(e, 1)}
                              disabled={
                                isDisabled || turnooverModels_1?.year === `NA`
                              }
                            />
                            {navigationResponse !== null
                              ? ""
                              : turnooverModels_1?.docUpload && (
                                  <div
                                    onClick={(e) => clearUploadHandler1(e)}
                                    className="remove_logo_container"
                                  >
                                    <img
                                      src={cancelIcon}
                                      alt="cancelIcon"
                                      className="cancel_icon"
                                    />
                                  </div>
                                )}
                          </div>
                        </div>
                      </AwesomeSlider>
                    </div>
                  </div>
                </div>

                {errors &&
                  ((turnoverModels.year !== "NA" &&
                    (turnoverModels.turnover === "" ||
                      // turnoverModels.docname === "" ||
                      // turnoverModels.docUpload === "" ||
                      turnoverModels.currency === "")) ||
                    (turnooverModels_1.year !== "NA" &&
                      (turnooverModels_1.turnover === "" ||
                        // turnooverModels_1.docname === "" ||
                        // turnooverModels_1.docUpload === "" ||
                        turnooverModels_1.currency === ""))) && (
                    <span className="error">
                      Kindly fill complete turnover details
                    </span>
                  )}
              </div>

              <div className="horizontal_line">&nbsp;</div>

              {/* check get state data */}
              {/* <button
                onClick={() =>
                  console.log({
                    financerList,
                    formValues,
                    financerListcount,
                    activeEngineer,
                    turnoverModels,
                    turnooverModels_1,
                  })
                }
              >
                get state
              </button> */}

              <div className="add_bidder_container">
                {financerListcount?.map((el, index) => {
                  if (activeEngineer === index + 1) {
                    return (
                      <div className="bidder active" key={index}>
                        <span className="bidder_text"> F{index + 1} </span>
                        {activeEngineer === financerListcount.length &&
                          activeEngineer !== 1 &&
                          !isAddBidderandSaveBtnActive && (
                            <span className="remove_bidder">
                              <img
                                src={cancel}
                                alt="cancel"
                                onClick={removeFinance}
                              />
                            </span>
                          )}
                      </div>
                    );
                  } else {
                    return (
                      <div
                        className="bidder completed"
                        key={index}
                        onClick={() => makeApproverActive(index + 1)}
                      >
                        <span className="bidder_text"> F{index + 1} </span>
                      </div>
                    );
                  }
                })}
              </div>

              <div className="section_heading">
                <h3>Assigned Finance team member</h3>
              </div>

              <div className="form_control_spread">
                <label className="form_label">Same as</label>
                {isNavigationData ? (
                  <input
                    type="text"
                    className="form_control"
                    value={
                      formValues?.userModel?.sameASUser === "Yes"
                        ? formValues?.userModel?.name
                        : ""
                    }
                    style={{
                      background: "transparent",
                      textTransform: "uppercase",
                    }}
                    disabled
                  />
                ) : (
                  <>
                    <Select
                      className="form_control_select_same"
                      onChange={handleSelectChange}
                      options={adminsData}
                      classNamePrefix="select"
                      value={
                        formValues?.userModel?.sameASUser === "Yes"
                          ? {
                              value: formValues?.userModel?.name,
                              label: formValues?.userModel?.name,
                            }
                          : ""
                      }
                      placeholder=""
                      disabled={isDisabled}
                    />
                    {formValues?.userModel?.sameASUser === "Yes" && (
                      <img
                        src={cancelIcon}
                        alt="cancel icon"
                        className="remove_data"
                        onClick={removeOtherOption}
                      />
                    )}
                  </>
                )}
              </div>

              <div className="form_control_area">
                <label className="form_label">
                  Name
                  <span className="label_mandatory">*</span>
                  <Tooltip text="Full name as per passport / national ID." />
                </label>
                <input
                  type="text"
                  className="form_control"
                  name="name"
                  value={formValues?.userModel?.name}
                  onChange={handleInputChange}
                  disabled={
                    isNavigationData ||
                    formValues?.userModel?.sameASUser === "Yes"
                      ? true
                      : false
                  }
                />
                {errors && formValues.userModel.name.trim() === "" && (
                  <span className="error">Enter Full Name</span>
                )}
              </div>

              <div className="form_control_area address_margin">
                <label className="form_label"> Designation </label>
                <input
                  type="text"
                  className="form_control"
                  name="designation"
                  value={formValues?.userModel?.designation}
                  onChange={handleInputChange}
                  disabled={
                    isNavigationData ||
                    formValues?.userModel?.sameASUser === "Yes"
                      ? true
                      : false
                  }
                />
              </div>

              <div className="form_control_area">
                <label className="form_label">
                  Email
                  <span className="label_mandatory">*</span>
                  <Tooltip text="Official email (not personal email). This email will be used for all official communication." />
                </label>
                <input
                  type="text"
                  className="form_control"
                  name="email"
                  value={formValues?.userModel?.email}
                  onChange={(e) => {
                    handleInputChange(e);
                    validateEmail(e);
                  }}
                  disabled={
                    isNavigationData ||
                    formValues?.userModel?.sameASUser === "Yes"
                      ? true
                      : false
                  }
                />

                {errors &&
                  formValues.userModel.email.trim() === "" &&
                  !emailValidation(formValues.userModel.email.trim()) && (
                    <span className="error">Company Email ID</span>
                  )}
                {errors &&
                  formValues.userModel.email.trim() !== "" &&
                  !emailValidation(formValues.userModel.email.trim()) && (
                    <span className="error">
                      Invalid Email ID, please check and re-enter
                    </span>
                  )}
              </div>

              <div className="form_control_area">
                <label className="form_label">
                  Mobile No.
                  <span className="label_mandatory">*</span>
                  <Tooltip text="Official mobile number (not personal mobile number). This mobile number will be used for all official communication." />
                </label>
                <div className="input_container_phone">
                  {isNavigationData ||
                  formValues?.userModel?.sameASUser === "Yes" ? (
                    <select
                      className="select_input_disabled"
                      name="phoneCode"
                      value={formValues?.userModel?.phoneCode}
                      onChange={handleInputChange}
                      disabled={
                        isNavigationData ||
                        formValues?.userModel?.sameASUser === "Yes"
                          ? true
                          : false
                      }
                    >
                      <option>{formValues?.userModel?.phoneCode}</option>
                    </select>
                  ) : (
                    <>
                      <div
                        style={{
                          display: hide && "none",
                          fontFamily: "Poppins-Regular",
                          position: "absolute",
                          marginLeft: "10px",
                          marginTop: "8px",
                          zIndex: 10,
                          fontSize: "16px",
                        }}
                      >
                        {formValues?.userModel?.phoneCode}
                      </div>
                      <Select
                        onChange={handleInputChangePhone}
                        onInputChange={setHide}
                        disabled={isDisabled}
                        placeholder="Search code"
                        className="select_input"
                        options={options}
                        styles={customStyles}
                        value={
                          formValues?.userModel?.phoneCode.length > 0
                            ? {
                                label: formValues?.userModel?.phoneCode,
                                value: formValues?.userModel?.phoneCode,
                              }
                            : ""
                        }
                        components={{
                          SingleValue: () => {
                            return null;
                          },
                        }}
                      ></Select>
                    </>
                  )}
                  <input
                    type="number"
                    className=" form_control_phone"
                    name="primaryPhone"
                    value={formValues?.userModel?.primaryPhone}
                    onChange={(e) => {
                      handleInputChange(e);
                      validatePhone(e);
                    }}
                    disabled={
                      isNavigationData ||
                      formValues?.userModel?.sameASUser === "Yes"
                        ? true
                        : false
                    }
                  />
                </div>
                {errors &&
                  (!numberValidation(formValues.userModel.primaryPhone) ||
                    formValues.userModel.primaryPhone == "" ||
                    formValues.userModel.phoneCode === "") && (
                    <span className="error">
                      Select at least one code / Contact number should be
                      between 6 to 13 digit
                    </span>
                  )}
              </div>

              <div className="form_control_spread">
                <label className="form_label">
                  I am available on Whatsapp
                  <Tooltip text="We use Whatsapp for convenience of communication with parties involved." />
                </label>
                <div className="radio_container">
                  <span className="a_control">
                    <input
                      type="radio"
                      name="onWhatsapp"
                      className="form_control_radio"
                      id="a_yes"
                      value="Yes"
                      checked={formValues?.userModel?.onWhatsapp === true}
                      onChange={handleInputChange}
                      disabled={
                        isNavigationData ||
                        formValues?.userModel?.sameASUser === "Yes"
                          ? true
                          : false
                      }
                    />
                    <label className="form_label" htmlFor="a_yes">
                      Yes
                    </label>
                  </span>
                  <span className="a_control">
                    <input
                      type="radio"
                      name="onWhatsapp"
                      className="form_control_radio"
                      id="a_no"
                      value="No"
                      checked={formValues?.userModel?.onWhatsapp === false}
                      onChange={handleInputChange}
                      disabled={
                        isNavigationData ||
                        formValues?.userModel?.sameASUser === "Yes"
                          ? true
                          : false
                      }
                    />
                    <label className="form_label" htmlFor="a_no">
                      No
                    </label>
                  </span>
                </div>
              </div>

              <div className="form_control_spread">
                <label className="form_label">
                  I am available on
                  <Tooltip text="Any alternate communication application other than Whatsapp." />
                </label>

                <span>
                  {isNavigationData ||
                  formValues?.userModel?.sameASUser === "Yes" ? (
                    <select className="form_control_disable" disabled>
                      <option>
                        {formValues?.userModel?.onWhatsapp === true
                          ? " "
                          : formValues?.userModel?.availableOn}
                      </option>
                    </select>
                  ) : formValues?.userModel?.onWhatsapp === true ? (
                    <input className="form_control_disable" disabled></input>
                  ) : (
                    <Select
                      options={socialMedia}
                      className="form_control_enable"
                      onChange={handleInputChangeAvailable}
                      autocomplete="nope"
                      value={{
                        value: formValues?.userModel?.availableOn,
                        label: formValues?.userModel?.availableOn,
                      }}
                    ></Select>
                  )}
                </span>

                {errors &&
                  formValues.userModel.onWhatsapp === false &&
                  formValues.userModel.availableOn === "" && (
                    <span className="error">
                      Please select at least one option.
                    </span>
                  )}
              </div>

              <div className="form_submit_container">
                {(financerRes && navigationCheck === false) ||
                (registrationStatusKey - 8 > 0 &&
                  navigationCheck === false &&
                  navigationResponse) ||
                financerRes ? (
                  <input
                    type="button"
                    value=" Next"
                    className="btn btn_submit_form"
                    onClick={nextNavigationHandler}
                  />
                ) : (
                  <>
                    {isSaveBtnActive && (
                      <input
                        type="button"
                        value="Save"
                        className="btn btn_submit_form"
                        onClick={approverSaveHandler}
                        disabled={isDisabled}
                      />
                    )}
                    {isAddBidderandSaveBtnActive && (
                      <>
                        <input
                          type="button"
                          value="Add Finance Team Member"
                          className="btn btn_add_bidder"
                          onClick={() => addFinancer()}
                          disabled={isDisabled}
                        />
                        <input
                          type="submit"
                          value="Proceed"
                          className="btn btn_submit_form"
                          disabled={isDisabled}
                        />
                      </>
                    )}
                  </>
                )}
              </div>
            </div>
          </form>
        )}

        {/* ---------------- Customer form ---------------- */}
        {isFormTypeCustomer && (
          <form onSubmit={handleFormSubmission}>
            <div className="form_container">
              <div className="form_control_spread">
                <label className="form_label">
                  Upload last 2 years documents indicating Company Turnover
                </label>
                <div
                  className={
                    navigationResponse !== null
                      ? "equipment_container"
                      : "equipment_container set_background"
                  }
                >
                  <div className="equipment_container_heading">
                    <span className="heading a_year">
                      Year
                      {/* <span className="lbl_mandatory">*</span> */}
                    </span>
                    <span className="heading a_turnover">
                      Turnover
                      {/* <span className="lbl_mandatory">*</span> */}
                    </span>
                    <span className="heading a_supported_document">
                      Supported Document
                    </span>
                    <span className="heading a_upload_document">
                      Upload Document
                    </span>
                  </div>

                  <div className="equipment_container_body_row">
                    {/* web view rows */}
                    <div className="no_carousel">
                      <div className="equipment_container_body">
                        <div className="equipment_col a_year">
                          <select
                            className="form_control"
                            name="year"
                            value={turnoverModels?.year}
                            onChange={handleChangeTurnoverYear}
                            disabled={isDisabled}
                          >
                            <option value="NA" selected>
                              NA
                            </option>
                            {commonYear
                              .filter(
                                (item) =>
                                  !item.includes(turnooverModels_1?.year)
                              )
                              .map((item) => (
                                <option key={item} value={item}>
                                  {item}
                                </option>
                              ))}
                          </select>
                          {/* {
                           errors && turnoverModels.year !== "NA" && <span className="error">Year is required</span>
                          } */}
                        </div>
                        {/* web  turnover partner-1  start  */}
                        <div className="equipment_col a_turnover">
                          <div className="input_container">
                            {isNavigationData ||
                            turnoverModels?.year === `NA` ? (
                              <select
                                className="select_input"
                                disabled={
                                  isDisabled || turnoverModels?.year === `NA`
                                }
                                style={{ padding: "0px 0px 0px 12px" }}
                              >
                                <option value="" selected>
                                  NA
                                </option>
                                <option>{turnoverModels?.currency}</option>
                              </select>
                            ) : (
                              <Select
                                name="currency"
                                className="select_input"
                                onChange={handleChangeToTurnover}
                                classNamePrefix="select-currency"
                                disabled={isDisabled}
                                options={optionCurrency}
                                value={{
                                  value: currencyValue_1,
                                  label: currencyValue_1,
                                }}
                              ></Select>
                            )}
                            <input
                              type="text"
                              className="form_control_phone"
                              name="turnover"
                              value={turnoverModels?.turnover}
                              placeholder="NA"
                              onChange={(e) => {
                                if (e.target.value.length <= 15)
                                  handleChangeTurnover(e);
                              }}
                              disabled={
                                isDisabled || turnoverModels?.year === `NA`
                              }
                            />
                          </div>
                          {/* {errors &&
                            turnoverModels.year !== "NA" &&
                            (turnoverModels.turnover === "" ||
                              turnoverModels.currency === "") && (
                              <span className="error">
                                Turnover is required
                              </span>
                            )} */}
                        </div>
                        <div className="equipment_col a_supported_document">
                          <select
                            className="form_control"
                            name="docname"
                            value={turnoverModels?.docname}
                            onChange={handleChangeTurnoverSelect}
                            disabled={
                              isDisabled || turnoverModels?.year === `NA`
                            }
                            defaultValue="N/A"
                          >
                            <option value="N/A" selected>
                              NA
                            </option>
                            <option value="Annual Report">Annual Report</option>
                            <option value="Audited  Financial Statement">
                              Audited Financial Statement
                            </option>
                            <option value="Others">Others</option>
                          </select>
                        </div>
                        {/* doc upload web 1 */}
                        <div className="equipment_col a_upload_document">
                          <label
                            className={
                              isNavigationData
                                ? "a_upload uploadFileName"
                                : "a_upload uploadFileName set_background"
                            }
                            htmlFor="file-upload_0"
                          >
                            {turnoverModels?.docUpload
                              ? turnoverModels?.docUpload?.split("/")[1]
                              : "Upload"}
                          </label>
                          <input
                            id="file-upload_0"
                            type="file"
                            accept=".png, .jpg, .jpeg, .pdf"
                            data-allowed-file-extension="png, jpg, jpeg, pdf"
                            onChange={(e) => uploadSelectedFile(e, 0)}
                            disabled={
                              isDisabled || turnoverModels?.year === `NA`
                            }
                          />

                          {navigationResponse !== null
                            ? ""
                            : turnoverModels?.docUpload && (
                                <div
                                  onClick={(e) => clearUploadHandler(e)}
                                  className="remove_logo_container"
                                >
                                  <img
                                    src={cancelIcon}
                                    alt="cancelIcon"
                                    className="cancel_icon"
                                  />
                                </div>
                              )}
                        </div>
                      </div>
                      <div className="equipment_container_body">
                        <div className="equipment_col a_year">
                          <select
                            className="form_control"
                            name="year"
                            value={turnooverModels_1?.year}
                            onChange={handleChangeTurnoverYear_1}
                            disabled={isDisabled}
                          >
                            <option value="NA" selected>
                              NA
                            </option>
                            {commonYear
                              .filter(
                                (item) => !item.includes(turnoverModels?.year)
                              )
                              .map((item) => (
                                <option key={item} value={item}>
                                  {item}
                                </option>
                              ))}
                          </select>
                          {/* {
                                                        errors && turnooverModels_1.year === "NA" && <span className="error">Year is required</span>
                                                    } */}
                        </div>
                        <div className="equipment_col a_turnover">
                          {/* web  turnover-2 partner start  */}
                          <div className="input_container">
                            {isNavigationData ||
                            turnooverModels_1?.year === `NA` ? (
                              <select
                                className="select_input"
                                style={{ padding: "0px 0px 0px 12px" }}
                                disabled={
                                  isDisabled || turnooverModels_1?.year === `NA`
                                }
                              >
                                <option disabled selected>
                                  NA
                                </option>
                                <option>{turnooverModels_1?.currency}</option>
                              </select>
                            ) : (
                              <Select
                                name="currency"
                                className="select_input"
                                onChange={handleChangeToTurnover_1}
                                disabled={isDisabled}
                                classNamePrefix="select-currency"
                                options={optionCurrency}
                                value={{
                                  value: currencyValue_2,
                                  label: currencyValue_2,
                                }}
                              ></Select>
                            )}

                            <input
                              type="text"
                              className="form_control_phone"
                              name="turnover"
                              value={turnooverModels_1?.turnover}
                              placeholder="NA"
                              onChange={(e) => {
                                if (e.target.value.length <= 15)
                                  handleChangeTurnover_1(e);
                              }}
                              disabled={
                                isDisabled || turnooverModels_1?.year === `NA`
                              }
                            />
                          </div>
                          {/* {errors &&
                            turnooverModels_1.year !== "NA" &&
                            (turnooverModels_1.turnover === "" ||
                              turnooverModels_1.currency === "") && (
                              <span className="error">
                                Turnover is required
                              </span>
                            )} */}
                        </div>
                        <div className="equipment_col a_supported_document">
                          <select
                            className="form_control"
                            name="docname"
                            value={turnooverModels_1?.docname}
                            onChange={handleChangeTurnoverSelect_1}
                            disabled={
                              isDisabled || turnooverModels_1?.year === `NA`
                            }
                            defaultValue="N/A"
                          >
                            <option value="N/A" selected>
                              NA
                            </option>
                            <option value="Annual Report">Annual Report</option>
                            <option value="Audited  Financial Statement">
                              Audited Financial Statement
                            </option>
                            <option value="Others">Others</option>
                          </select>
                        </div>
                        <div className="equipment_col a_upload_document">
                          <label
                            className={
                              isNavigationData
                                ? "a_upload uploadFileName"
                                : "a_upload uploadFileName set_background"
                            }
                            htmlFor="file-upload_1"
                          >
                            {turnooverModels_1?.docUpload
                              ? turnooverModels_1?.docUpload?.split("/")[1]
                              : "Upload"}
                          </label>
                          <input
                            id="file-upload_1"
                            type="file"
                            accept=".png, .jpg, .jpeg, .pdf"
                            data-allowed-file-extension="png, jpg, jpeg, pdf"
                            onChange={(e) => uploadSelectedFile(e, 1)}
                            disabled={
                              isDisabled || turnooverModels_1?.year === `NA`
                            }
                          />
                          {navigationResponse !== null
                            ? ""
                            : turnooverModels_1?.docUpload && (
                                <div
                                  onClick={(e) => clearUploadHandler1(e)}
                                  className="remove_logo_container"
                                >
                                  <img
                                    src={cancelIcon}
                                    alt="cancelIcon"
                                    className="cancel_icon"
                                  />
                                </div>
                              )}
                        </div>
                      </div>
                    </div>
                    <div className="display_carousel">
                      {/* mobile view carousel */}
                      <AwesomeSlider className="aws-btn ">
                        <div className="equipment_container_body">
                          {/* carousel patner -1 row */}
                          <div className="equipment_col a_year">
                            <label className="mobile_label">Year</label>
                            <select
                              className="form_control"
                              name="year"
                              value={turnoverModels?.year}
                              onChange={handleChangeTurnoverYear}
                              disabled={isDisabled}
                            >
                              <option value="NA" selected disabled>
                                NA
                              </option>
                              {commonYear
                                .filter(
                                  (item) =>
                                    !item.includes(turnooverModels_1?.year)
                                )
                                .map((item) => (
                                  <option key={item} value={item}>
                                    {item}
                                  </option>
                                ))}
                            </select>
                            {/* {
                                                            errors && turnoverModels.year === "NA" && <span className="error">Year is required</span>
                                                        } */}
                          </div>
                          <div className="equipment_col a_turnover">
                            <label className="mobile_label">Turnover</label>
                            <div className="input_container">
                              {isNavigationData ||
                              turnoverModels?.year === `NA` ? (
                                <select
                                  className="select_input"
                                  disabled={
                                    isDisabled || turnoverModels?.year === `NA`
                                  }
                                >
                                  <option selected disabled>
                                    NA
                                  </option>
                                  <option>{turnoverModels?.currency}</option>
                                </select>
                              ) : (
                                <Select
                                  name="currency"
                                  className="select_input"
                                  onChange={handleChangeToTurnover}
                                  disabled={isDisabled}
                                  classNamePrefix="select-currency"
                                  options={optionCurrency}
                                  value={{
                                    value: currencyValue_1,
                                    label: currencyValue_1,
                                  }}
                                ></Select>
                              )}

                              {/* <select
                                className="select_input"
                                //className="form_control"
                                name="currency"
                                value={turnoverModels?.currency}
                                onChange={handleChangeTurnover}
                                disabled={
                                  isDisabled
                                }
                              >
                                <option value="" disabled selected></option>
                                {countries?.map((country) => (
                                  <option
                                    value={country.currency}
                                    key={country.currency}
                                  >
                                    {country.currency}
                                  </option>
                                ))}
                              </select> */}
                              <input
                                type="text"
                                className="form_control_phone"
                                name="turnover"
                                value={turnoverModels?.turnover}
                                placeholder="NA"
                                onChange={(e) => {
                                  if (e.target.value.length <= 15)
                                    handleChangeTurnover(e);
                                }}
                                disabled={
                                  isDisabled || turnoverModels?.year === `NA`
                                }
                              />
                            </div>
                            {/* {errors &&
                              turnoverModels.year !== "NA" &&
                              (turnoverModels.turnover === "" ||
                                turnoverModels.currency === "") && (
                                <span className="error">
                                  Turnover is required
                                </span>
                              )} */}
                          </div>
                          <div className="equipment_col a_supported_document">
                            <label className="mobile_label">
                              Supported Document
                            </label>
                            <select
                              className="form_control"
                              name="docname"
                              value={turnoverModels?.docname}
                              onChange={handleChangeTurnoverSelect}
                              disabled={
                                isDisabled || turnoverModels?.year === `NA`
                              }
                              defaultValue="N/A"
                            >
                              <option value="N/A" selected>
                                NA
                              </option>

                              <option value="Annual Report">
                                Annual Report
                              </option>
                              <option value="Audited  Financial Statement">
                                Audited Financial Statement
                              </option>
                              <option value="Others">Others</option>
                            </select>
                          </div>
                          <div className="equipment_col a_upload_document">
                            <label className="mobile_label">
                              Supported Document
                            </label>
                            <label
                              className={
                                isNavigationData
                                  ? "a_upload uploadFileName"
                                  : "a_upload uploadFileName set_background"
                              }
                              htmlFor="file-upload_0"
                            >
                              {turnoverModels?.docUpload
                                ? turnoverModels?.docUpload?.split("/")[1]
                                : "Upload"}
                            </label>
                            <input
                              id="file-upload_0"
                              type="file"
                              accept=".png, .jpg, .jpeg, .pdf"
                              data-allowed-file-extension="png, jpg, jpeg, pdf"
                              onChange={(e) => uploadSelectedFile(e, 0)}
                              disabled={
                                isDisabled || turnoverModels?.year === `NA`
                              }
                            />
                            {navigationResponse !== null
                              ? ""
                              : turnoverModels?.docUpload && (
                                  <div
                                    onClick={(e) => clearUploadHandler(e)}
                                    className="remove_logo_container"
                                  >
                                    <img
                                      src={cancelIcon}
                                      alt="cancelIcon"
                                      className="cancel_icon"
                                    />
                                  </div>
                                )}
                          </div>
                        </div>
                        {/* carousel patner -2 row */}
                        <div className="equipment_container_body">
                          <div className="equipment_col a_year">
                            <label className="mobile_label">Year</label>
                            <select
                              className="form_control"
                              name="year"
                              value={turnooverModels_1?.year}
                              onChange={handleChangeTurnoverYear_1}
                              disabled={isDisabled}
                            >
                              <option selected disabled>
                                NA
                              </option>
                              {commonYear
                                .filter(
                                  (item) => !item.includes(turnoverModels?.year)
                                )
                                .map((item) => (
                                  <option key={item} value={item}>
                                    {item}
                                  </option>
                                ))}
                            </select>
                            {/* {
                                                            errors && turnooverModels_1.year === "NA" && <span className="error">Year is required</span>
                                                        } */}
                          </div>
                          <div className="equipment_col a_turnover">
                            <label className="mobile_label">Turnover </label>
                            <div className="input_container">
                              {isNavigationData ||
                              turnooverModels_1?.year === `NA` ? (
                                <select
                                  className="select_input"
                                  disabled={
                                    isDisabled ||
                                    turnooverModels_1?.year === `NA`
                                  }
                                >
                                  <option disabled selected>
                                    NA
                                  </option>
                                  <option>{turnooverModels_1?.currency}</option>
                                </select>
                              ) : (
                                <Select
                                  name="currency"
                                  className="select_input"
                                  onChange={handleChangeToTurnover_1}
                                  disabled={isDisabled}
                                  classNamePrefix="select-currency"
                                  options={optionCurrency}
                                  value={{
                                    value: currencyValue_2,
                                    label: currencyValue_2,
                                  }}
                                ></Select>
                              )}

                              {/* <select
                                className="select_input"
                                name="currency"
                                value={turnoverModels?.currency}
                                onChange={handleChangeTurnover}
                                disabled={
                                  isDisabled
                                }
                              >
                                <option value="" disabled selected></option>
                                {countries?.map((country) => (
                                  <option
                                    value={country.currency}
                                    key={country.currency}
                                  >
                                    {country.currency}
                                  </option>
                                ))}
                              </select> */}
                              <input
                                type="text"
                                className="form_control_phone"
                                placeholder="NA"
                                name="turnover"
                                value={turnooverModels_1?.turnover}
                                onChange={(e) => {
                                  if (e.target.value.length <= 15)
                                    handleChangeTurnover_1(e);
                                }}
                                disabled={
                                  isDisabled || turnooverModels_1?.year === `NA`
                                }
                              />
                            </div>
                            {/* {errors &&
                              turnooverModels_1.year !== "NA" &&
                              (turnooverModels_1.turnover === "" ||
                                turnooverModels_1.currency === "") && (
                                <span className="error">
                                  Turnover is required
                                </span>
                              )} */}
                          </div>
                          <div className="equipment_col a_supported_document">
                            <label className="mobile_label">
                              Supported Document
                            </label>
                            <select
                              className="form_control"
                              name="docname"
                              value={turnooverModels_1?.docname}
                              onChange={handleChangeTurnoverSelect_1}
                              disabled={
                                isDisabled || turnooverModels_1?.year === `NA`
                              }
                              defaultValue="N/A"
                            >
                              <option value="N/A" selected>
                                NA
                              </option>
                              <option value="Annual Report">
                                Annual Report
                              </option>
                              <option value="Audited  Financial Statement">
                                Audited Financial Statement
                              </option>
                              <option value="Others">Others</option>
                            </select>
                          </div>
                          <div className="equipment_col a_upload_document">
                            <label className="mobile_label">
                              Supported Document
                            </label>
                            <label
                              className={
                                isNavigationData
                                  ? "a_upload uploadFileName"
                                  : "a_upload uploadFileName set_background"
                              }
                              htmlFor="file-upload_1"
                            >
                              {turnooverModels_1?.docUpload
                                ? turnooverModels_1?.docUpload?.split("/")[1]
                                : "Upload"}
                            </label>
                            <input
                              id="file-upload_1"
                              type="file"
                              accept=".png, .jpg, .jpeg, .pdf"
                              data-allowed-file-extension="png, jpg, jpeg, pdf"
                              onChange={(e) => uploadSelectedFile(e, 1)}
                              disabled={
                                isDisabled || turnooverModels_1?.year === `NA`
                              }
                            />
                            {navigationResponse !== null
                              ? ""
                              : turnooverModels_1?.docUpload && (
                                  <div
                                    onClick={(e) => clearUploadHandler1(e)}
                                    className="remove_logo_container"
                                  >
                                    <img
                                      src={cancelIcon}
                                      alt="cancelIcon"
                                      className="cancel_icon"
                                    />
                                  </div>
                                )}
                          </div>
                        </div>
                      </AwesomeSlider>
                    </div>
                  </div>
                </div>

                {errors &&
                  ((turnoverModels.year !== "NA" &&
                    (turnoverModels.turnover === "" ||
                      // turnoverModels.docname === "" ||
                      // turnoverModels.docUpload === "" ||
                      turnoverModels.currency === "")) ||
                    (turnooverModels_1.year !== "NA" &&
                      (turnooverModels_1.turnover === "" ||
                        // turnooverModels_1.docname === "" ||
                        // turnooverModels_1.docUpload === "" ||
                        turnooverModels_1.currency === ""))) && (
                    <span className="error">
                      Kindly fill complete turnover details
                    </span>
                  )}
              </div>

              <div className="horizontal_line">&nbsp;</div>

              {/* check get state data */}
              {/* <button
                onClick={() =>
                  console.log({
                    financerList,
                    formValues,
                    financerListcount,
                    activeEngineer,
                  })
                }
              >
                get state
              </button> */}

              <div className="add_bidder_container">
                {financerListcount?.map((el, index) => {
                  if (activeEngineer === index + 1) {
                    return (
                      <div className="bidder active" key={index}>
                        <span className="bidder_text"> F{index + 1} </span>
                        {activeEngineer === financerListcount.length &&
                          activeEngineer !== 1 &&
                          !isAddBidderandSaveBtnActive && (
                            <span className="remove_bidder">
                              <img
                                src={cancel}
                                alt="cancel"
                                onClick={removeFinance}
                              />
                            </span>
                          )}
                      </div>
                    );
                  } else {
                    return (
                      <div
                        className="bidder completed"
                        key={index}
                        onClick={() => makeApproverActive(index + 1)}
                      >
                        <span className="bidder_text"> F{index + 1} </span>
                      </div>
                    );
                  }
                })}
              </div>

              <div className="section_heading">
                <h3>Assigned Finance team member</h3>
              </div>

              {/* <div className="form_control_spread">
                <label className="form_label">Same as</label>
                {isNavigationData ? (
                  <input
                    type="text"
                    className="form_control"
                    value={
                      formValues?.userModel?.sameASUser === "Yes"
                        ? formValues?.userModel?.name
                        : ""
                    }
                    style={{
                      background: "transparent",
                      textTransform: "uppercase",
                    }}
                    disabled
                  />
                ) : (
                  <>
                    <Select
                      className="form_control_select_same"
                      onChange={handleSelectChange}
                      options={adminsData}
                      value={
                        formValues?.userModel?.sameASUser === "Yes"
                          ? {
                              value: formValues?.userModel?.name,
                              label: formValues?.userModel?.name,
                            }
                          : ""
                      }
                      placeholder=""
                      disabled={isDisabled}
                    />
                    {formValues?.userModel?.sameASUser === "Yes" && (
                      <img
                        src={cancelIcon}
                        alt="cancel icon"
                        className="remove_data"
                        onClick={removeOtherOption}
                      />
                    )}
                  </>
                )}
              </div> */}

              <div className="form_control_area">
                <label className="form_label">
                  Name
                  <span className="label_mandatory">*</span>
                  <Tooltip text="Full name as per passport / national ID." />
                </label>
                <input
                  type="text"
                  className="form_control"
                  name="name"
                  value={formValues?.userModel?.name}
                  onChange={handleInputChange}
                  disabled={
                    isNavigationData ||
                    formValues?.userModel?.sameASUser === "Yes"
                      ? true
                      : false
                  }
                />
                {errors && formValues.userModel.name.trim() === "" && (
                  <span className="error">Enter Full Name</span>
                )}
              </div>

              <div className="form_control_area address_margin">
                <label className="form_label"> Designation </label>
                <input
                  type="text"
                  className="form_control"
                  name="designation"
                  value={formValues?.userModel?.designation}
                  onChange={handleInputChange}
                  disabled={
                    isNavigationData ||
                    formValues?.userModel?.sameASUser === "Yes"
                      ? true
                      : false
                  }
                />
              </div>

              <div className="form_control_area">
                <label className="form_label">
                  Email
                  <span className="label_mandatory">*</span>
                  <Tooltip text="Official email (not personal email). This email will be used for all official communication." />
                </label>
                <input
                  type="text"
                  className="form_control"
                  name="email"
                  value={formValues?.userModel?.email}
                  onChange={(e) => {
                    handleInputChange(e);
                    validateEmail(e);
                  }}
                  disabled={
                    isNavigationData ||
                    formValues?.userModel?.sameASUser === "Yes"
                      ? true
                      : false
                  }
                />

                {errors &&
                  formValues.userModel.email.trim() === "" &&
                  !emailValidation(formValues.userModel.email.trim()) && (
                    <span className="error">Company Email ID</span>
                  )}
                {errors &&
                  formValues.userModel.email.trim() !== "" &&
                  !emailValidation(formValues.userModel.email.trim()) && (
                    <span className="error">
                      Invalid Email ID, please check and re-enter
                    </span>
                  )}
              </div>

              <div className="form_control_area">
                <label className="form_label">
                  Mobile No.
                  <span className="label_mandatory">*</span>
                  <Tooltip text="Official mobile number (not personal mobile number). This mobile number will be used for all official communication." />
                </label>
                <div className="input_container_phone">
                  {isNavigationData ||
                  formValues?.userModel?.sameASUser === "Yes" ? (
                    <select
                      className="select_input_disabled"
                      name="phoneCode"
                      value={formValues?.userModel?.phoneCode}
                      onChange={handleInputChange}
                      disabled={
                        isNavigationData ||
                        formValues?.userModel?.sameASUser === "Yes"
                          ? true
                          : false
                      }
                    >
                      <option>{formValues?.userModel?.phoneCode}</option>
                    </select>
                  ) : (
                    <>
                      <div
                        style={{
                          display: hide && "none",
                          fontFamily: "Poppins-Regular",
                          position: "absolute",
                          marginLeft: "10px",
                          marginTop: "8px",
                          zIndex: 10,
                          fontSize: "16px",
                        }}
                      >
                        {formValues?.userModel?.phoneCode}
                      </div>
                      <Select
                        onChange={handleInputChangePhone}
                        onInputChange={setHide}
                        disabled={isDisabled}
                        placeholder="Search code"
                        className="select_input"
                        options={options}
                        styles={customStyles}
                        value={
                          formValues?.userModel?.phoneCode.length > 0
                            ? {
                                label: formValues?.userModel?.phoneCode,
                                value: formValues?.userModel?.phoneCode,
                              }
                            : ""
                        }
                        components={{
                          SingleValue: () => {
                            return null;
                          },
                        }}
                      ></Select>
                    </>
                  )}
                  <input
                    type="number"
                    className=" form_control_phone"
                    name="primaryPhone"
                    value={formValues?.userModel?.primaryPhone}
                    onChange={(e) => {
                      handleInputChange(e);
                      validatePhone(e);
                    }}
                    disabled={
                      isNavigationData ||
                      formValues?.userModel?.sameASUser === "Yes"
                        ? true
                        : false
                    }
                  />
                </div>
                {errors &&
                  (!numberValidation(formValues.userModel.primaryPhone) ||
                    formValues.userModel.primaryPhone == "" ||
                    formValues.userModel.phoneCode === "") && (
                    <span className="error">
                      Select at least one code / Contact number should be
                      between 6 to 13 digit
                    </span>
                  )}
              </div>

              <div className="form_control_spread">
                <label className="form_label">
                  I am available on Whatsapp
                  <Tooltip text="We use Whatsapp for convenience of communication with parties involved." />
                </label>
                <div className="radio_container">
                  <span className="a_control">
                    <input
                      type="radio"
                      name="onWhatsapp"
                      className="form_control_radio"
                      id="a_yes"
                      value="Yes"
                      checked={formValues?.userModel?.onWhatsapp === true}
                      onChange={handleInputChange}
                      disabled={
                        isNavigationData ||
                        formValues?.userModel?.sameASUser === "Yes"
                          ? true
                          : false
                      }
                    />
                    <label className="form_label" htmlFor="a_yes">
                      Yes
                    </label>
                  </span>
                  <span className="a_control">
                    <input
                      type="radio"
                      name="onWhatsapp"
                      className="form_control_radio"
                      id="a_no"
                      value="No"
                      checked={formValues?.userModel?.onWhatsapp === false}
                      onChange={handleInputChange}
                      disabled={
                        isNavigationData ||
                        formValues?.userModel?.sameASUser === "Yes"
                          ? true
                          : false
                      }
                    />
                    <label className="form_label" htmlFor="a_no">
                      No
                    </label>
                  </span>
                </div>
              </div>

              <div className="form_control_spread">
                <label className="form_label">
                  I am available on
                  <Tooltip text="Any alternate communication application other than Whatsapp." />
                </label>

                <span>
                  {isNavigationData ||
                  formValues?.userModel?.sameASUser === "Yes" ? (
                    <select className="form_control_disable" disabled>
                      <option>
                        {formValues?.userModel?.onWhatsapp === true
                          ? " "
                          : formValues?.userModel?.availableOn}
                      </option>
                    </select>
                  ) : formValues?.userModel?.onWhatsapp === true ? (
                    <input className="form_control_disable" disabled></input>
                  ) : (
                    <Select
                      options={socialMedia}
                      className="form_control_enable"
                      autocomplete="nope"
                      onChange={handleInputChangeAvailable}
                      value={{
                        value: formValues?.userModel?.availableOn,
                        label: formValues?.userModel?.availableOn,
                      }}
                    ></Select>
                  )}
                </span>

                {errors &&
                  formValues.userModel.onWhatsapp === false &&
                  formValues.userModel.availableOn === "" && (
                    <span className="error">
                      Please select at least one option.
                    </span>
                  )}
              </div>

              <div className="form_submit_container">
                {(financerRes && navigationCheck === false) ||
                (registrationStatusKey - 8 > 0 &&
                  navigationCheck === false &&
                  navigationResponse) ||
                financerRes ? (
                  <input
                    type="button"
                    value=" Next"
                    className="btn btn_submit_form"
                    onClick={nextNavigationHandler}
                  />
                ) : (
                  <>
                    {isSaveBtnActive && (
                      <input
                        type="button"
                        value="Save"
                        className="btn btn_submit_form"
                        onClick={approverSaveHandler}
                        disabled={isDisabled}
                      />
                    )}
                    {isAddBidderandSaveBtnActive && (
                      <>
                        <input
                          type="button"
                          value="Add Finance Team Member"
                          className="btn btn_add_bidder"
                          onClick={() => addFinancer()}
                          disabled={isDisabled}
                        />
                        <input
                          type="submit"
                          value="Proceed"
                          className="btn btn_submit_form"
                          disabled={isDisabled}
                        />
                      </>
                    )}
                  </>
                )}
              </div>
            </div>
          </form>
        )}

        {/* ---------------- Partner Individual form ---------------- */}
        {isFormTypePartnersIndividual && (
          <form onSubmit={handleFormSubmission}>
            <div className="form_container forHeight">
              <div className="form_control_spread forBottomMargin">
                <label className="form_label">
                  Upload last 2 years documents indicating Company Turnover
                </label>
                <div
                  className={
                    navigationResponse !== null
                      ? "equipment_container"
                      : "equipment_container set_background"
                  }
                >
                  <div className="equipment_container_heading">
                    <span className="heading a_year">
                      Year
                      {/* <span className="lbl_mandatory">*</span> */}
                    </span>
                    <span className="heading a_turnover">
                      Turnover
                      {/* <span className="lbl_mandatory">*</span> */}
                    </span>
                    <span className="heading a_supported_document">
                      Supported Document
                    </span>
                    <span className="heading a_upload_document">
                      Upload Document
                    </span>
                  </div>

                  <div className="equipment_container_body_row">
                    {/* web view rows */}
                    <div className="no_carousel">
                      <div className="equipment_container_body">
                        <div className="equipment_col a_year">
                          <select
                            className="form_control"
                            name="year"
                            value={turnoverModels?.year}
                            onChange={handleChangeTurnoverYear}
                            disabled={isDisabled}
                          >
                            <option value="NA" selected>
                              NA
                            </option>
                            {commonYear
                              .filter(
                                (item) =>
                                  !item.includes(turnooverModels_1?.year)
                              )
                              .map((item) => (
                                <option key={item} value={item}>
                                  {item}
                                </option>
                              ))}
                          </select>
                          {/* {
                           errors && turnoverModels.year !== "NA" && <span className="error">Year is required</span>
                          } */}
                        </div>
                        {/* web  turnover partner-1  start  */}
                        <div className="equipment_col a_turnover">
                          <div className="input_container">
                            {isNavigationData ||
                            turnoverModels?.year === `NA` ? (
                              <select
                                className="select_input"
                                disabled={
                                  isDisabled || turnoverModels?.year === `NA`
                                }
                                style={{ padding: "0px 0px 0px 12px" }}
                              >
                                <option value="" selected>
                                  NA
                                </option>
                                <option>{turnoverModels?.currency}</option>
                              </select>
                            ) : (
                              <Select
                                name="currency"
                                className="select_input"
                                onChange={handleChangeToTurnover}
                                classNamePrefix="select-currency"
                                disabled={isDisabled}
                                options={optionCurrency}
                                value={{
                                  value: currencyValue_1,
                                  label: currencyValue_1,
                                }}
                              ></Select>
                            )}
                            <input
                              type="text"
                              className="form_control_phone"
                              name="turnover"
                              value={turnoverModels?.turnover}
                              placeholder="NA"
                              onChange={(e) => {
                                if (e.target.value.length <= 15)
                                  handleChangeTurnover(e);
                              }}
                              disabled={
                                isDisabled || turnoverModels?.year === `NA`
                              }
                            />
                          </div>
                          {/* {errors &&
                            turnoverModels.year !== "NA" &&
                            (turnoverModels.turnover === "" ||
                              turnoverModels.currency === "") && (
                              <span className="error">
                                Turnover is required
                              </span>
                            )} */}
                        </div>
                        <div className="equipment_col a_supported_document">
                          <select
                            className="form_control"
                            name="docname"
                            value={turnoverModels?.docname}
                            onChange={handleChangeTurnoverSelect}
                            disabled={
                              isDisabled || turnoverModels?.year === `NA`
                            }
                            defaultValue="N/A"
                          >
                            <option value="N/A" selected>
                              NA
                            </option>
                            <option value="Annual Report">Annual Report</option>
                            <option value="Audited  Financial Statement">
                              Audited Financial Statement
                            </option>
                            <option value="Others">Others</option>
                          </select>
                        </div>
                        {/* doc upload web 1 */}
                        <div className="equipment_col a_upload_document">
                          <label
                            className={
                              isNavigationData
                                ? "a_upload uploadFileName"
                                : "a_upload uploadFileName set_background"
                            }
                            htmlFor="file-upload_0"
                          >
                            {turnoverModels?.docUpload
                              ? turnoverModels?.docUpload?.split("/")[1]
                              : "Upload"}
                          </label>
                          <input
                            id="file-upload_0"
                            type="file"
                            accept=".png, .jpg, .jpeg, .pdf"
                            data-allowed-file-extension="png, jpg, jpeg, pdf"
                            onChange={(e) => uploadSelectedFile(e, 0)}
                            disabled={
                              isDisabled || turnoverModels?.year === `NA`
                            }
                          />

                          {navigationResponse !== null
                            ? ""
                            : turnoverModels?.docUpload && (
                                <div
                                  onClick={(e) => clearUploadHandler(e)}
                                  className="remove_logo_container"
                                >
                                  <img
                                    src={cancelIcon}
                                    alt="cancelIcon"
                                    className="cancel_icon"
                                  />
                                </div>
                              )}
                        </div>
                      </div>
                      <div className="equipment_container_body">
                        <div className="equipment_col a_year">
                          <select
                            className="form_control"
                            name="year"
                            value={turnooverModels_1?.year}
                            onChange={handleChangeTurnoverYear_1}
                            disabled={isDisabled}
                          >
                            <option value="NA" selected>
                              NA
                            </option>
                            {commonYear
                              .filter(
                                (item) => !item.includes(turnoverModels?.year)
                              )
                              .map((item) => (
                                <option key={item} value={item}>
                                  {item}
                                </option>
                              ))}
                          </select>
                          {/* {
                                                        errors && turnooverModels_1.year === "NA" && <span className="error">Year is required</span>
                                                    } */}
                        </div>
                        <div className="equipment_col a_turnover">
                          {/* web  turnover-2 partner start  */}
                          <div className="input_container">
                            {isNavigationData ||
                            turnooverModels_1?.year === `NA` ? (
                              <select
                                className="select_input"
                                style={{ padding: "0px 0px 0px 12px" }}
                                disabled={
                                  isDisabled || turnooverModels_1?.year === `NA`
                                }
                              >
                                <option disabled selected>
                                  NA
                                </option>
                                <option>{turnooverModels_1?.currency}</option>
                              </select>
                            ) : (
                              <Select
                                name="currency"
                                className="select_input"
                                onChange={handleChangeToTurnover_1}
                                disabled={isDisabled}
                                classNamePrefix="select-currency"
                                options={optionCurrency}
                                value={{
                                  value: currencyValue_2,
                                  label: currencyValue_2,
                                }}
                              ></Select>
                            )}

                            <input
                              type="text"
                              className="form_control_phone"
                              name="turnover"
                              value={turnooverModels_1?.turnover}
                              placeholder="NA"
                              onChange={(e) => {
                                if (e.target.value.length <= 15)
                                  handleChangeTurnover_1(e);
                              }}
                              disabled={
                                isDisabled || turnooverModels_1?.year === `NA`
                              }
                            />
                          </div>
                          {/* {errors &&
                            turnooverModels_1.year !== "NA" &&
                            (turnooverModels_1.turnover === "" ||
                              turnooverModels_1.currency === "") && (
                              <span className="error">
                                Turnover is required
                              </span>
                            )} */}
                        </div>
                        <div className="equipment_col a_supported_document">
                          <select
                            className="form_control"
                            name="docname"
                            value={turnooverModels_1?.docname}
                            onChange={handleChangeTurnoverSelect_1}
                            disabled={
                              isDisabled || turnooverModels_1?.year === `NA`
                            }
                            defaultValue="N/A"
                          >
                            <option value="N/A" selected>
                              NA
                            </option>
                            <option value="Annual Report">Annual Report</option>
                            <option value="Audited  Financial Statement">
                              Audited Financial Statement
                            </option>
                            <option value="Others">Others</option>
                          </select>
                        </div>
                        <div className="equipment_col a_upload_document">
                          <label
                            className={
                              isNavigationData
                                ? "a_upload uploadFileName"
                                : "a_upload uploadFileName set_background"
                            }
                            htmlFor="file-upload_1"
                          >
                            {turnooverModels_1?.docUpload
                              ? turnooverModels_1?.docUpload?.split("/")[1]
                              : "Upload"}
                          </label>
                          <input
                            id="file-upload_1"
                            type="file"
                            accept=".png, .jpg, .jpeg, .pdf"
                            data-allowed-file-extension="png, jpg, jpeg, pdf"
                            onChange={(e) => uploadSelectedFile(e, 1)}
                            disabled={
                              isDisabled || turnooverModels_1?.year === `NA`
                            }
                          />
                          {navigationResponse !== null
                            ? ""
                            : turnooverModels_1?.docUpload && (
                                <div
                                  onClick={(e) => clearUploadHandler1(e)}
                                  className="remove_logo_container"
                                >
                                  <img
                                    src={cancelIcon}
                                    alt="cancelIcon"
                                    className="cancel_icon"
                                  />
                                </div>
                              )}
                        </div>
                      </div>
                    </div>
                    <div className="display_carousel">
                      {/* mobile view carousel */}
                      <AwesomeSlider className="aws-btn ">
                        <div className="equipment_container_body">
                          {/* carousel patner -1 row */}
                          <div className="equipment_col a_year">
                            <label className="mobile_label">Year</label>
                            <select
                              className="form_control"
                              name="year"
                              value={turnoverModels?.year}
                              onChange={handleChangeTurnoverYear}
                              disabled={isDisabled}
                            >
                              <option value="NA" selected disabled>
                                NA
                              </option>
                              {commonYear
                                .filter(
                                  (item) =>
                                    !item.includes(turnooverModels_1?.year)
                                )
                                .map((item) => (
                                  <option key={item} value={item}>
                                    {item}
                                  </option>
                                ))}
                            </select>
                            {/* {
                                                            errors && turnoverModels.year === "NA" && <span className="error">Year is required</span>
                                                        } */}
                          </div>
                          <div className="equipment_col a_turnover">
                            <label className="mobile_label">Turnover</label>
                            <div className="input_container">
                              {isNavigationData ||
                              turnoverModels?.year === `NA` ? (
                                <select
                                  className="select_input"
                                  disabled={
                                    isDisabled || turnoverModels?.year === `NA`
                                  }
                                >
                                  <option selected disabled>
                                    NA
                                  </option>
                                  <option>{turnoverModels?.currency}</option>
                                </select>
                              ) : (
                                <Select
                                  name="currency"
                                  className="select_input"
                                  onChange={handleChangeToTurnover}
                                  disabled={isDisabled}
                                  classNamePrefix="select-currency"
                                  options={optionCurrency}
                                  value={{
                                    value: currencyValue_1,
                                    label: currencyValue_1,
                                  }}
                                ></Select>
                              )}

                              {/* <select
                                className="select_input"
                                //className="form_control"
                                name="currency"
                                value={turnoverModels?.currency}
                                onChange={handleChangeTurnover}
                                disabled={
                                  isDisabled
                                }
                              >
                                <option value="" disabled selected></option>
                                {countries?.map((country) => (
                                  <option
                                    value={country.currency}
                                    key={country.currency}
                                  >
                                    {country.currency}
                                  </option>
                                ))}
                              </select> */}
                              <input
                                type="text"
                                className="form_control_phone"
                                name="turnover"
                                value={turnoverModels?.turnover}
                                placeholder="NA"
                                onChange={(e) => {
                                  if (e.target.value.length <= 15)
                                    handleChangeTurnover(e);
                                }}
                                disabled={
                                  isDisabled || turnoverModels?.year === `NA`
                                }
                              />
                            </div>
                            {/* {errors &&
                              turnoverModels.year !== "NA" &&
                              (turnoverModels.turnover === "" ||
                                turnoverModels.currency === "") && (
                                <span className="error">
                                  Turnover is required
                                </span>
                              )} */}
                          </div>
                          <div className="equipment_col a_supported_document">
                            <label className="mobile_label">
                              Supported Document
                            </label>
                            <select
                              className="form_control"
                              name="docname"
                              value={turnoverModels?.docname}
                              onChange={handleChangeTurnoverSelect}
                              disabled={
                                isDisabled || turnoverModels?.year === `NA`
                              }
                              defaultValue="N/A"
                            >
                              <option value="N/A" selected>
                                NA
                              </option>

                              <option value="Annual Report">
                                Annual Report
                              </option>
                              <option value="Audited  Financial Statement">
                                Audited Financial Statement
                              </option>
                              <option value="Others">Others</option>
                            </select>
                          </div>
                          <div className="equipment_col a_upload_document">
                            <label className="mobile_label">
                              Supported Document
                            </label>
                            <label
                              className={
                                isNavigationData
                                  ? "a_upload uploadFileName"
                                  : "a_upload uploadFileName set_background"
                              }
                              htmlFor="file-upload_0"
                            >
                              {turnoverModels?.docUpload
                                ? turnoverModels?.docUpload?.split("/")[1]
                                : "Upload"}
                            </label>
                            <input
                              id="file-upload_0"
                              type="file"
                              accept=".png, .jpg, .jpeg, .pdf"
                              data-allowed-file-extension="png, jpg, jpeg, pdf"
                              onChange={(e) => uploadSelectedFile(e, 0)}
                              disabled={
                                isDisabled || turnoverModels?.year === `NA`
                              }
                            />
                            {navigationResponse !== null
                              ? ""
                              : turnoverModels?.docUpload && (
                                  <div
                                    onClick={(e) => clearUploadHandler(e)}
                                    className="remove_logo_container"
                                  >
                                    <img
                                      src={cancelIcon}
                                      alt="cancelIcon"
                                      className="cancel_icon"
                                    />
                                  </div>
                                )}
                          </div>
                        </div>
                        {/* carousel patner -2 row */}
                        <div className="equipment_container_body">
                          <div className="equipment_col a_year">
                            <label className="mobile_label">Year</label>
                            <select
                              className="form_control"
                              name="year"
                              value={turnooverModels_1?.year}
                              onChange={handleChangeTurnoverYear_1}
                              disabled={isDisabled}
                            >
                              <option selected disabled>
                                NA
                              </option>
                              {commonYear
                                .filter(
                                  (item) => !item.includes(turnoverModels?.year)
                                )
                                .map((item) => (
                                  <option key={item} value={item}>
                                    {item}
                                  </option>
                                ))}
                            </select>
                            {/* {
                                                            errors && turnooverModels_1.year === "NA" && <span className="error">Year is required</span>
                                                        } */}
                          </div>
                          <div className="equipment_col a_turnover">
                            <label className="mobile_label">Turnover </label>
                            <div className="input_container">
                              {isNavigationData ||
                              turnooverModels_1?.year === `NA` ? (
                                <select
                                  className="select_input"
                                  disabled={
                                    isDisabled ||
                                    turnooverModels_1?.year === `NA`
                                  }
                                >
                                  <option disabled selected>
                                    NA
                                  </option>
                                  <option>{turnooverModels_1?.currency}</option>
                                </select>
                              ) : (
                                <Select
                                  name="currency"
                                  className="select_input"
                                  onChange={handleChangeToTurnover_1}
                                  disabled={isDisabled}
                                  classNamePrefix="select-currency"
                                  options={optionCurrency}
                                  value={{
                                    value: currencyValue_2,
                                    label: currencyValue_2,
                                  }}
                                ></Select>
                              )}

                              {/* <select
                                className="select_input"
                                name="currency"
                                value={turnoverModels?.currency}
                                onChange={handleChangeTurnover}
                                disabled={
                                  isDisabled
                                }
                              >
                                <option value="" disabled selected></option>
                                {countries?.map((country) => (
                                  <option
                                    value={country.currency}
                                    key={country.currency}
                                  >
                                    {country.currency}
                                  </option>
                                ))}
                              </select> */}
                              <input
                                type="text"
                                className="form_control_phone"
                                placeholder="NA"
                                name="turnover"
                                value={turnooverModels_1?.turnover}
                                onChange={(e) => {
                                  if (e.target.value.length <= 15)
                                    handleChangeTurnover_1(e);
                                }}
                                disabled={
                                  isDisabled || turnooverModels_1?.year === `NA`
                                }
                              />
                            </div>
                            {/* {errors &&
                              turnooverModels_1.year !== "NA" &&
                              (turnooverModels_1.turnover === "" ||
                                turnooverModels_1.currency === "") && (
                                <span className="error">
                                  Turnover is required
                                </span>
                              )} */}
                          </div>
                          <div className="equipment_col a_supported_document">
                            <label className="mobile_label">
                              Supported Document
                            </label>
                            <select
                              className="form_control"
                              name="docname"
                              value={turnooverModels_1?.docname}
                              onChange={handleChangeTurnoverSelect_1}
                              disabled={
                                isDisabled || turnooverModels_1?.year === `NA`
                              }
                              defaultValue="N/A"
                            >
                              <option value="N/A" selected>
                                NA
                              </option>
                              <option value="Annual Report">
                                Annual Report
                              </option>
                              <option value="Audited  Financial Statement">
                                Audited Financial Statement
                              </option>
                              <option value="Others">Others</option>
                            </select>
                          </div>
                          <div className="equipment_col a_upload_document">
                            <label className="mobile_label">
                              Supported Document
                            </label>
                            <label
                              className={
                                isNavigationData
                                  ? "a_upload uploadFileName"
                                  : "a_upload uploadFileName set_background"
                              }
                              htmlFor="file-upload_1"
                            >
                              {turnooverModels_1?.docUpload
                                ? turnooverModels_1?.docUpload?.split("/")[1]
                                : "Upload"}
                            </label>
                            <input
                              id="file-upload_1"
                              type="file"
                              accept=".png, .jpg, .jpeg, .pdf"
                              data-allowed-file-extension="png, jpg, jpeg, pdf"
                              onChange={(e) => uploadSelectedFile(e, 1)}
                              disabled={
                                isDisabled || turnooverModels_1?.year === `NA`
                              }
                            />
                            {navigationResponse !== null
                              ? ""
                              : turnooverModels_1?.docUpload && (
                                  <div
                                    onClick={(e) => clearUploadHandler1(e)}
                                    className="remove_logo_container"
                                  >
                                    <img
                                      src={cancelIcon}
                                      alt="cancelIcon"
                                      className="cancel_icon"
                                    />
                                  </div>
                                )}
                          </div>
                        </div>
                      </AwesomeSlider>
                    </div>
                  </div>
                </div>

                {errors &&
                  ((turnoverModels.year !== "NA" &&
                    (turnoverModels.turnover === "" ||
                      // turnoverModels.docname === "" ||
                      // turnoverModels.docUpload === "" ||
                      turnoverModels.currency === "")) ||
                    (turnooverModels_1.year !== "NA" &&
                      (turnooverModels_1.turnover === "" ||
                        // turnooverModels_1.docname === "" ||
                        // turnooverModels_1.docUpload === "" ||
                        turnooverModels_1.currency === ""))) && (
                    <span className="error">
                      Kindly fill complete turnover details
                    </span>
                  )}
              </div>
              <div className="form_submit_container">
                {(financerRes && navigationCheck === false) ||
                (registrationStatusKey - 8 > 0 &&
                  navigationCheck === false &&
                  navigationResponse) ||
                financerRes ? (
                  <input
                    type="button"
                    value=" Next"
                    className="btn btn_submit_form"
                    onClick={nextNavigationHandler}
                  />
                ) : (
                  <>
                    {isSaveBtnActive && (
                      <input
                        type="button"
                        value="Save &amp; Next"
                        className="btn btn_submit_form"
                        onClick={approverSaveHandler}
                        disabled={isDisabled}
                      />
                    )}
                    {isAddBidderandSaveBtnActive && (
                      <>
                        {/* <input
                          type="button"
                          value="Add Financer"
                          className="btn btn_add_bidder"
                          onClick={() => addFinancer()}
                          disabled={isDisabled}
                        /> */}
                        <input
                          type="submit"
                          value="Proceed"
                          className="btn btn_submit_form"
                          disabled={isDisabled}
                        />
                      </>
                    )}
                  </>
                )}
              </div>
            </div>
          </form>
        )}
      </div>
    </div>
  );
};

export default Finance;
