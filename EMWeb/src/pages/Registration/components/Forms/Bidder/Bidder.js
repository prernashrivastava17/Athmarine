import React, { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import Select from "react-select";
import "./Bidder.scss";
import ConfirmationModal from "./../../Modals/ConfirmationModal/ConfirmationModal";
import ConfirmationAddNewData from "../../Modals/confirmationAddNewData/confirmationAddNewData";
import iRedIcon from "./../../../../../assets/images/i-red-icon.png";
import Loading from "../../../../../components/Loading/Loading";
import Tooltip from "./../../../../../components/Tooltip/Tooltip";
import { getCurrencyCountryStatus } from "../../../../../store/vendorRegistration/allCurrency";
import cancelIcon from "./../../../../../assets/images/cancel_other_icon.png";
import cancel from "./../../../../../assets/images/cancel.png";
import {
  validateEmailPhone,
  validateEmailPhoneActions,
} from "../../../../../store/vendorRegistration/validateEmailPhone";
import {
  emailValidation,
  numberValidation,
} from "../../../../../utils/helpers";
import { ToastError } from "./../../../../../components/Tostify";
import {
  getAuthData,
  getCompanyId,
  getReferralCode,
} from "../../../../../config/index";

import {
  saveBidders,
  saveBidderSlice,
} from "./../../../../../store/vendorRegistration/bidder";
import { getAvailableOn } from "./../../../../../store/common/availableOn";
import {
  commonGetAPiCall,
  commonGetApiActions,
} from "../../../../../store/common/commonGetApi";
import { getAllUserDetails } from "../../../../../store/common/allUserDetails";
import { getCurrencyStatus } from "../../../../../store/vendorRegistration/currency";

const Bidder = (props) => {
  const dispatch = useDispatch();

  let isFormTypePartner = props.selectedUser === "partner" ? true : false;
  let isFormTypeCustomer = props.selectedUser === "customer" ? true : false;

  const currencyCountry = useSelector(
    (state) => state.currencyCountryReducer.currencyCountryStatus
  );

  const countries = useSelector((state) => state.countries.countries);
  const isCountryError = useSelector((state) => state.countries.isError);
  const biddersResponseObject = useSelector(
    (state) => state.vendorBidder.bidder
  );
  const navigationCheck = useSelector(
    (state) => state.vendorBidder.navigationCheck
  );
  const isErrorInbiddersResponseObject = useSelector(
    (state) => state.vendorBidder.isError
  );
  const isBidderLoading = useSelector((state) => state.vendorBidder.isLoading);
  const isError = useSelector((state) => state.vendorBidder.isError);
  const availableOnRes = useSelector(
    (state) => state.availableOnReducer.availableOnRes
  );
  const isErrorInavailableOnRes = useSelector(
    (state) => state.availableOnReducer.isError
  );
  const isAvailableOnLoading = useSelector(
    (state) => state.availableOnReducer.isLoading
  );
  const navigationResponse = useSelector(
    (state) => state.commonGetApiReducer.getApiRes
  );
  const registrationStatusKey = useSelector(
    (state) => state.commonGetApiReducer.registrationStatusKey
  );

  const allUserDetails = useSelector(
    (state) => state.allUserDetailsReducer.allUserDetails
  );

  const currencyData = useSelector(
    (state) => state.currencyStatusReducer.currencyStatus
  );

  const initialValues = {
    name: "",
    email: "",
    primaryPhone: "",
    phoneCode: "",
    designation: "",
    onWhatsapp: false,
    availableOn: "",
    sameASUser: "No",
    companyId: Number(getCompanyId()),
    roleModel: [
      {
        id: isFormTypePartner ? 2 : 8, // vendor: 2, customer: 8           // managing head 5 id
      },
    ],
    currency: "",
    biddingLimit: "",
  };
  const [hide, setHide] = useState();
  const [formValues, setFormValues] = useState(initialValues);
  const [bidderList, setbidderList] = useState([]);
  const [bidderListCount, setbidderListCount] = useState([1]);
  const [activeBidder, setactiveBidder] = useState(1);
  const [isSaveBtnActive, setIsSaveBtnActive] = useState(false);
  const [isAddBidderandSaveBtnActive, setisAddBidderandSaveBtnActive] =
    useState(true);
  const [lastBidderBtnStatus, setlastBidderBtnStatus] = useState(false);
  const [lastBidderFilledData, setlastBidderFilledData] =
    useState(initialValues);
  const [errors, setErrors] = useState(false);
  const [isConfirmationModal, setIsConfirmationModal] = useState(false);
  const [isConfirmationAddNewData, setIsConfirmationAddNewData] =
    useState(false);
  const [sameAsAdmin, setSameAsAdmin] = useState([""]);
  const [dataResponse, setDataResponse] = useState(false);
  const [allUsersData, setAllUsersData] = useState([]);
  const [isUserClickedRemoved, setIsUserClickedRemoved] = useState(false);
  const [removeState, setRemoveState] = useState(false);

  useEffect(() => {
    if (navigationResponse !== null && Array.isArray(navigationResponse)) {
      setbidderList(navigationResponse);
      setFormValues(navigationResponse?.[navigationResponse.length - 1]);
      let bidderCount = navigationResponse?.map((item, index) => index + 1);
      setbidderListCount(bidderCount);
      setactiveBidder(navigationResponse.length);
    }
  }, [navigationResponse]);

  const getCompanyIdCheck = () => {
    if (getAuthData()?.companyId) {
      return getAuthData()?.companyId;
    }
    return getCompanyId();
  };

  useEffect(() => {
    if (registrationStatusKey - 4 > 0 && isFormTypePartner) {
      dispatch(
        commonGetAPiCall(
          `/vendor/getAllVendorComapnyBidder/${getCompanyIdCheck()}`
        )
      );
    } else if (registrationStatusKey - 4 > 0 && isFormTypeCustomer) {
      dispatch(
        commonGetAPiCall(
          `/customer/getAllRequestersByCompanyId/${getCompanyIdCheck()}`
        )
      );
    }
  }, [registrationStatusKey, isFormTypePartner, isFormTypeCustomer]);

  useEffect(() => {
    if (
      !navigationCheck &&
      biddersResponseObject !== null &&
      isFormTypePartner
    ) {
      dispatch(
        commonGetAPiCall(`/vendor/getAllVendorComapnyBidder/${getCompanyId()}`)
      );
    } else if (
      !navigationCheck &&
      biddersResponseObject !== null &&
      isFormTypeCustomer
    ) {
      dispatch(
        commonGetAPiCall(
          `/customer/getAllRequestersByCompanyId/${getCompanyId()}`
        )
      );
    }
  }, [
    dispatch,
    navigationCheck,
    biddersResponseObject,
    isFormTypePartner,
    isFormTypeCustomer,
  ]);

  useEffect(() => {
    dispatch(getAvailableOn());
    dispatch(getAllUserDetails());
    dispatch(getCurrencyStatus());
  }, [dispatch]);

  useEffect(() => {
    setSameAsAdmin(allUserDetails);
  }, [allUserDetails]);

  useEffect(() => {
    if (isCountryError !== "") {
      ToastError(isCountryError);
    } else if (isErrorInbiddersResponseObject !== "") {
      ToastError(isErrorInbiddersResponseObject);
    } else if (isErrorInavailableOnRes !== "") {
      ToastError(isErrorInavailableOnRes);
    }
  }, [isCountryError, isErrorInbiddersResponseObject, isErrorInavailableOnRes]);

  useEffect(() => {
    if (navigationCheck) {
      if (biddersResponseObject !== null) {
        if (isFormTypePartner) {
          props.setFormLabel("approver", 2);
        } else if (isFormTypeCustomer) {
          props.setFormLabel("approver", 2);
        }
      }
    }
  }, [
    biddersResponseObject,
    props,
    isFormTypePartner,
    isFormTypeCustomer,
    navigationCheck,
  ]);

  const checkUniqueBidder = () => {
    let emailFlag = false,
      phoneFlag = false;
    for (let i = 0; i < bidderList.length; i++) {
      if (i === activeBidder - 1) {
      } else {
        if (
          bidderList[i].email === formValues.email &&
          bidderList[i].primaryPhone === formValues.primaryPhone
        ) {
          emailFlag = true;
          phoneFlag = true;
        } else if (bidderList[i].email === formValues.email) {
          emailFlag = true;
        } else if (bidderList[i].primaryPhone === formValues.primaryPhone) {
          phoneFlag = true;
        }
      }
    }

    if (emailFlag && phoneFlag) {
      ToastError(`Duplicate Bidder`);
      return false;
    } else if (emailFlag) {
      ToastError(`Duplicate Bidder`);
      return false;
    } else if (phoneFlag) {
      ToastError(`Duplicate Bidder`);
      return false;
    } else {
      return true;
    }
  };

  const validateForm = () => {
    if (
      formValues.name.trim() &&
      formValues.email.trim() &&
      formValues.primaryPhone.trim() &&
      formValues.primaryPhone.length > 5 &&
      formValues.primaryPhone.length < 14 &&
      formValues.phoneCode &&
      ((formValues.onWhatsapp === false && formValues.availableOn !== "") ||
      (formValues.onWhatsapp === true && formValues.availableOn === "")
        ? true
        : false) &&
      emailValidation(formValues.email.trim()) &&
      ((formValues.biddingLimit !== "" && formValues.currency !== "") ||
        (formValues.biddingLimit === "" && formValues.currency === ""))
    ) {
      setErrors(false);
      return true;
    } else {
      setErrors(true);
      return false;
    }
  };

  const handleInputChangePhone = (e) => {
    setFormValues({ ...formValues, phoneCode: e.value });
  };
  const handleInputChangeAvailable = (e) => {
    setFormValues({ ...formValues, availableOn: e.value });
  };

  const handleInputChangeCurrency = (e) => {
    setRemoveState(true);

    setFormValues({ ...formValues, currency: e.value });
  };

  const removeSelectState = () => {
    setRemoveState(false);
    setFormValues({ ...formValues, currency: "" });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;

    switch (name) {
      case "name":
        if (!Number(e.nativeEvent.data))
          setFormValues({ ...formValues, name: value });
        break;

      case "email":
        setFormValues({ ...formValues, email: value.trim() });
        break;

      case "primaryPhone":
        if (value.length < 14)
          setFormValues({ ...formValues, primaryPhone: `${value.trim()}` });
        break;

      case "phoneCode":
        setFormValues({ ...formValues, phoneCode: value.trim() });
        break;

      case "onWhatsapp":
        setFormValues({
          ...formValues,
          onWhatsapp: value.trim() === "No" ? false : true,
          availableOn: "",
        });
        break;

      case "availableOn":
        setFormValues({ ...formValues, availableOn: value.trim() });
        break;

      case "designation":
        if (!Number(e.nativeEvent.data))
          setFormValues({ ...formValues, designation: value });
        break;

      case "currency":
        setFormValues({ ...formValues, currency: value.trim() });
        break;

      case "biddingLimit":
        setFormValues({
          ...formValues,
          biddingLimit: (
            Number(value.replace(/\D/g, "")) || ""
          ).toLocaleString(),
        });
        break;

      default:
      // do nothing
    }
  };

  const onCancelHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
  };

  const onOkHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
    apiCall();
  };

  const apiCall = () => {
    if (isFormTypePartner && bidderList.length === 0 && validateForm()) {
      const transformedBidderObject = JSON.parse(JSON.stringify(formValues));
      dispatch(
        saveBidders([transformedBidderObject], "vendor/createVendorBidder")
      );
      return false;
    } else if (
      isFormTypeCustomer &&
      bidderList.length === 0 &&
      validateForm()
    ) {
      const transformedBidderObject = JSON.parse(JSON.stringify(formValues));
      if (isFormTypeCustomer) {
        transformedBidderObject["type"] = "Customer";
      }
      dispatch(saveBidders([transformedBidderObject], "customer/addRequester"));
      return false;
    } else {
      const transformedLastBidderObject = [...bidderList];
      transformedLastBidderObject[bidderList.length - 1] = formValues;
      setbidderList(transformedLastBidderObject);

      if (isFormTypeCustomer) {
        transformedLastBidderObject["type"] = "Customer";
      }

      if (isFormTypePartner && validateForm()) {
        dispatch(
          saveBidders(transformedLastBidderObject, "vendor/createVendorBidder")
        );
      } else if (isFormTypeCustomer && validateForm()) {
        dispatch(
          saveBidders(transformedLastBidderObject, "customer/addRequester")
        );
      }
    }
  };

  const handleFormSubmission = (event) => {
    event.preventDefault();
    if (validateForm()) {
      setIsConfirmationModal(!isConfirmationModal);
    }
  };

  const saveData = (flag) => {
    if (flag === "already-exists") {
      let approvers = bidderList.slice();
      approvers[activeBidder - 1] = formValues;
      setbidderList(approvers);
    } else {
      setbidderList([...bidderList, formValues]);
    }
  };

  const addBidder = () => {
    if (!validateForm()) return false;

    if (bidderList.length === 0) {
      if (!checkUniqueBidder()) {
        return false;
      }
    }

    setlastBidderBtnStatus(false);
    if (activeBidder === 1) {
      // saveData();
      // setbidderListCount([
      //   ...bidderListCount,
      //   Number(bidderListCount.length) + 1,
      // ]);
      // setactiveBidder(activeBidder + 1);
      // setIsSaveBtnActive(true);
      // setisAddBidderandSaveBtnActive(false);
      // setFormValues(initialValues);
      setIsConfirmationAddNewData(!isConfirmationAddNewData);
    } else {
      // setbidderListCount([
      //   ...bidderListCount,
      //   Number(bidderListCount.length) + 1,
      // ]);
      // setactiveBidder(activeBidder + 1);
      // setIsSaveBtnActive(true);
      // setisAddBidderandSaveBtnActive(false);
      // setFormValues(initialValues);
      setIsConfirmationAddNewData(!isConfirmationAddNewData);
    }
  };

  const bidderSaveHandler = () => {
    if (!validateForm()) return false;

    if (!checkUniqueBidder()) {
      return false;
    }

    if (isUserClickedRemoved) {
      if (bidderList.length > 0) {
        setbidderList([...bidderList, formValues]);
        setIsUserClickedRemoved(false);
      }
    }

    if (activeBidder < bidderListCount.length) {
      saveData("already-exists");
      if (lastBidderBtnStatus === true) {
        setFormValues({ ...bidderList[bidderListCount.length - 1] });
        setactiveBidder(bidderListCount.length);
        setIsSaveBtnActive(false);
        setisAddBidderandSaveBtnActive(true);
      } else {
        setactiveBidder(bidderListCount.length);
        setIsSaveBtnActive(true);
        setisAddBidderandSaveBtnActive(false);
        setFormValues(lastBidderFilledData);
      }
    } else if (activeBidder === bidderListCount.length) {
      if (isUserClickedRemoved) {
        setIsUserClickedRemoved(false);
      } else {
        saveData();
      }
      setIsSaveBtnActive(false);
      setisAddBidderandSaveBtnActive(true);
      if (activeBidder === 1) {
        setlastBidderBtnStatus(false);
      } else {
        setlastBidderBtnStatus(true);
      }
    }
  };

  const makeBidderActive = (activeBidderSerial) => {
    if (activeBidder === bidderListCount.length) {
      if (navigationResponse != null) {
        setlastBidderFilledData(bidderList[bidderList.length - 1]);
      } else {
        setlastBidderFilledData(formValues);
      }
    }
    setactiveBidder((prevState) => {
      innerBidderActive(activeBidderSerial);
      return activeBidderSerial;
    });
  };

  const innerBidderActive = (bidderSerial) => {
    if (bidderSerial === bidderListCount.length) {
      setFormValues(lastBidderFilledData);
      if (lastBidderBtnStatus === true) {
        setIsSaveBtnActive(false);
        setisAddBidderandSaveBtnActive(true);
      } else {
        setIsSaveBtnActive(true);
        setisAddBidderandSaveBtnActive(false);
      }
    } else {
      setFormValues({ ...bidderList[bidderSerial - 1] });
      setIsSaveBtnActive(true);
      setisAddBidderandSaveBtnActive(false);
    }
  };

  const nextNavigationHandler = () => {
    dispatch(saveBidderSlice.bidderNavigation(true));
    dispatch(commonGetApiActions.dispatchGetRequest());
    props.navigationHandler({
      currentForm: "approver",
      goForward: true,
    });
  };

  useEffect(() => {
    dispatch(getCurrencyCountryStatus());
  }, [dispatch]);

  const onCancelModal = () => {
    setIsConfirmationAddNewData(!isConfirmationAddNewData);
  };

  const onConfirm = () => {
    setIsConfirmationAddNewData(!isConfirmationAddNewData);
    setRemoveState(false);
    removeOtherOption();
    if (activeBidder === 1) {
      saveData();
      setbidderListCount([
        ...bidderListCount,
        Number(bidderListCount.length) + 1,
      ]);
      setAllUsersData([...allUsersData, formValues.primaryPhone]);
      setactiveBidder(activeBidder + 1);
      setIsSaveBtnActive(true);
      setisAddBidderandSaveBtnActive(false);
      setFormValues(initialValues);
    } else {
      setbidderListCount([
        ...bidderListCount,
        Number(bidderListCount.length) + 1,
      ]);
      setAllUsersData([...allUsersData, formValues.primaryPhone]);
      setactiveBidder(activeBidder + 1);
      setIsSaveBtnActive(true);
      setisAddBidderandSaveBtnActive(false);
      setFormValues(initialValues);
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

  const currencyCountryOption = currencyData?.map((availableAt) => ({
    value: availableAt.currency,
    label: availableAt.currency,
  }));

  const socialMedia = availableOnRes?.map((availableAt) => ({
    value: availableAt.app_Name,
    label: availableAt.app_Name,
  }));

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
      width: "320px",
    }),
  };

  const handleSelectChange = (e) => {
    setFormValues({
      ...formValues,
      name: e.value[0],
      email: e.value[1],
      phoneCode: e.value[2],
      primaryPhone: e.value[3],
      onWhatsapp: e.value[4],
      availableOn: e.value[5],
      sameASUser: "Yes",
    });
    setDataResponse(true);
  };

  const removeOtherOption = () => {
    setFormValues({
      ...formValues,
      name: "",
      email: "",
      phoneCode: "",
      primaryPhone: "",
      onWhatsapp: false,
      availableOn: "",
      sameASUser: "No",
      designation: "",
      biddingLimit: "",
      currency: "",
    });
    setDataResponse(false);
    const id = allUsersData.indexOf(formValues?.primaryPhone); 
    const dataRemove = allUsersData.splice(id,  1);
    setAllUsersData(allUsersData);
  };

  const removeBidder = () => {
    setactiveBidder(activeBidder - 1);
    const trasformedBidderCount = [...bidderListCount];
    trasformedBidderCount.splice(trasformedBidderCount.length - 1, 1);
    setbidderListCount(trasformedBidderCount);
    setFormValues(bidderList[bidderList.length - 1]);

    const transformedBidderList = [...bidderList];
    transformedBidderList.pop();
    setbidderList(transformedBidderList);
    setIsUserClickedRemoved(true);
  };

  return (
    <div className="form_container_area">
      <div className="bidder_form positioning_container">
        {(isBidderLoading || isAvailableOnLoading) && (
          <Loading text={"Loading ...."} />
        )}
        {isConfirmationModal && (
          <ConfirmationModal cancel={onCancelHandler} ok={onOkHandler} />
        )}
        {isConfirmationAddNewData && (
          <ConfirmationAddNewData
            cancel={onCancelModal}
            ok={onConfirm}
            name={"Bidder"}
          />
        )}

        {/* ---------------- Partner form ---------------- */}
        {isFormTypePartner && (
          <>
            <div className="nav_container">
              <div className="nav_items">
                <div className="nav_item active">
                  Bidder
                  {/* <span className="info_red_icon">
                    <img src={iRedIcon} alt="i-red-icon" />
                  </span> */}
                </div>
                <div className="horizotal_row_container">
                  <span className="horizontal_row">&nbsp;</span>
                </div>
                <div
                  className="nav_item"
                  onClick={() =>
                    props.navigationHandler({
                      currentForm: "approver",
                      goForward: true,
                    })
                  }
                >
                  Approver
                </div>
                <div className="horizotal_row_container">
                  <span className="horizontal_row">&nbsp;</span>
                </div>
                <div
                  className="nav_item"
                  onClick={() =>
                    props.navigationHandler({ currentForm: "engineer" })
                  }
                >
                  Engineer
                </div>
              </div>
            </div>

            {/* <button
              onClick={() =>
                console.log({
                  bidderList,
                  formValues,
                  bidderListCount,
                  activeBidder,
                })
              }
            >
              get state
            </button> */}

            <div className="company_id_details">
              <p className="company_id">Company id: {getReferralCode()}</p>
            </div>

            <div className="add_bidder_container">
              {bidderListCount?.map((el, index) => {
                if (activeBidder === index + 1) {
                  return (
                    <div className="bidder active" key={index}>
                      <span className="bidder_text"> B{index + 1} </span>
                      {navigationResponse === null &&
                        activeBidder === bidderListCount.length &&
                        activeBidder !== 1 &&
                        !isAddBidderandSaveBtnActive && (
                          <span className="remove_bidder">
                            <img
                              src={cancel}
                              alt="cancel"
                              onClick={removeBidder}
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
                      onClick={() => makeBidderActive(index + 1)}
                    >
                      <span className="bidder_text"> B{index + 1} </span>
                    </div>
                  );
                }
              })}
            </div>

            {/* <button
              onClick={() =>
                console.log(formValues.availableOn, formValues.onWhatsapp)
              }
            >
              Get State
            </button> */}
            <form onSubmit={handleFormSubmission}>
              <div className="form_container">
                <div className="form_control1_spread">
                  <label className="form_label">Same as </label>
                  {navigationResponse !== null ? (
                    <input
                      type="text"
                      className="form_control1"
                      value={
                        formValues?.sameASUser === "Yes" ? formValues?.name : ""
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
                          formValues?.sameASUser === "Yes"
                            ? {
                                value: formValues?.name,
                                label: formValues?.name,
                              }
                            : ""
                        }
                        placeholder=""
                        disabled={navigationResponse !== null ? true : false}
                      />
                      { formValues?.sameASUser === "Yes" && (
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

                <div className="form_control1_area">
                  <label className="form_label">
                    Name <span className="label_mandatory">*</span>
                    <Tooltip text="Bidder is mainly responsible for Quoting and assigning  the engineers." />
                  </label>
                  <input
                    type="text"
                    className="form_control1"
                    name="name"
                    value={formValues?.name}
                    onChange={handleInputChange}
                    disabled={
                      navigationResponse != null || formValues?.sameASUser === "Yes"  ? true : false
                    }
                  />
                  {errors && formValues.name.trim() === "" && (
                    <span className="error">Enter Full Name </span>
                  )}
                </div>

                <div className="form_control1_area address_margin">
                  <label className="form_label"> Designation </label>
                  <input
                    type="text"
                    className="form_control1"
                    name="designation"
                    value={formValues?.designation}
                    onChange={handleInputChange}
                    disabled={navigationResponse != null ? true : false}
                  />
                </div>

                <div className="form_control1_area">
                  <label className="form_label">
                    Email <span className="label_mandatory">*</span>
                    <Tooltip text="Official email (not personal email). This email will be used for all official communication." />
                  </label>
                  <input
                    type="text"
                    className="form_control1"
                    name="email"
                    value={formValues?.email}
                    onChange={(e) => {
                      handleInputChange(e);
                      validateEmail(e);
                    }}
                    disabled={
                      navigationResponse != null || formValues?.sameASUser === "Yes"  ? true : false
                    }
                  />
                  {errors &&
                    formValues.email.trim() === "" &&
                    !emailValidation(formValues.email.trim()) && (
                      <span className="error">Company Email ID</span>
                    )}
                  {errors &&
                    formValues.email.trim() !== "" &&
                    !emailValidation(formValues.email.trim()) && (
                      <span className="error">
                        Invalid Email ID, please check and re-enter
                      </span>
                    )}
                </div>

                <div className="form_control1_area">
                  <label className="form_label">
                    Mobile No. <span className="label_mandatory">*</span>
                    <Tooltip text="Official mobile number (not personal mobile number). This mobile number will be used for all official communication." />
                  </label>
                  <div className="input_container ">
                    {navigationResponse != null || formValues?.sameASUser === "Yes"  ? (
                      <select
                        className="select_input"
                        style={{ padding: "0px 16px" }}
                        disabled={
                          navigationResponse != null || formValues?.sameASUser === "Yes" 
                            ? true
                            : false
                        }
                      >
                        <option>{formValues?.phoneCode}</option>
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
                          {formValues?.phoneCode}
                        </div>

                        <Select
                          onChange={handleInputChangePhone}
                          onInputChange={setHide}
                          disabled={navigationResponse != null ? true : false}
                          placeholder="Search code"
                          className="select_input"
                          options={options}
                          styles={customStyles}
                          value={
                            formValues?.phoneCode.length > 0
                              ? {
                                  value: formValues?.phoneCode,
                                  label: formValues?.phoneCode,
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
                      className="form_control1_phone"
                      name="primaryPhone"
                      value={formValues?.primaryPhone}
                      onChange={(e) => {
                        handleInputChange(e);
                        validatePhone(e);
                      }}
                      disabled={
                        navigationResponse != null || formValues?.sameASUser === "Yes" 
                          ? true
                          : false
                      }
                    />
                  </div>
                  {errors &&
                    (!numberValidation(formValues.primaryPhone) ||
                      formValues?.phoneCode === "" ||
                      formValues.primaryPhone === "") && (
                      <span className="error">
                        Select at least one code / Contact number should be
                        between 6 to 13 digit
                      </span>
                    )}
                </div>

                <div className="form_control1_spread">
                  <label className="form_label">
                    I am available on Whatsapp
                    <Tooltip text="We use Whatsapp for convenience of communication with parties involved." />
                  </label>
                  <div className="radio_container">
                    <span className="a_control">
                      <input
                        type="radio"
                        name="onWhatsapp"
                        className="form_control1_radio"
                        id="a_yes"
                        value="Yes"
                        checked={formValues?.onWhatsapp === true}
                        // onChange={whatsappAndAvailableOnHandler}
                        onChange={handleInputChange}
                        disabled={
                          navigationResponse != null || formValues?.sameASUser === "Yes" 
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
                        className="form_control1_radio"
                        id="a_no"
                        value="No"
                        checked={formValues?.onWhatsapp === false}
                        onChange={handleInputChange}
                        disabled={
                          navigationResponse != null || formValues?.sameASUser === "Yes" 
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
                <div className="form_control1_spread">
                  <label className="form_label">
                    I am available on
                    <Tooltip text="Any alternate communication application other than Whatsapp." />
                  </label>

                  <span>
                    {navigationResponse != null || formValues?.sameASUser === "Yes"  ? (
                      <select className="form_control1_disable" disabled>
                        <option>
                          {formValues?.onWhatsapp === true
                            ? " "
                            : formValues?.availableOn}
                        </option>
                      </select>
                    ) : formValues?.onWhatsapp === true ? (
                      <input className="form_control1_disable" disabled></input>
                    ) : (
                      <Select
                        options={socialMedia}
                        className="form_control1_enable"
                        autocomplete="nope"
                        onChange={handleInputChangeAvailable}
                        value={{
                          value: formValues?.availableOn,
                          label: formValues?.availableOn,
                        }}
                      ></Select>
                    )}
                  </span>
                  {errors &&
                    formValues?.onWhatsapp === false &&
                    formValues.availableOn === "" && (
                      <span className="error">
                        Please select at least one option.
                      </span>
                    )}
                </div>
                <div className="form_control1_spread">
                  <label className="form_label">
                    Bidding Limit
                    <Tooltip text="Bidding Limit for bidder to quote above which bid requires a Approval from an Approver." />
                  </label>
                  <div
                    className="select_container"
                    style={{ position: "relative" }}
                  >
                    {navigationResponse != null ? (
                      <select
                        className="select_control_disable amount_version"
                        disabled
                      >
                        {<option>{formValues?.currency}</option>}
                      </select>
                    ) : (
                      <>
                        <Select
                          className="select_control_enable amount_version"
                          onChange={handleInputChangeCurrency}
                          options={currencyCountryOption}
                            classNamePrefix="select-currency"
                          value={{
                            value: formValues?.currency,
                            label: formValues?.currency,
                          }}
                        />
                        {formValues?.currency && (
                          <img
                            src={cancelIcon}
                            alt="cancel icon"
                            className="remove_data_state"
                            onClick={removeSelectState}
                          />
                        )}
                      </>
                    )}

                    {/* <select
                      className="select_control amount"
                      name="biddingLimit"
                      value={formValues?.biddingLimit}
                      onChange={handleInputChange}
                      disabled={navigationResponse != null ? true : false}
                    >
                    sdsd
                      <option disabled="disabled"></option>
                      <option value="1000">Above 1,000</option>
                      <option value="2000">Above 2,000</option>
                    </select> */}

                    {navigationResponse != null ? (
                      <input
                        type="text"
                        className="form_control1"
                        name="biddingLimit"
                        style={{ width: "60%", marginLeft: "15px" }}
                        value={formValues?.biddingLimit}
                        disabled={navigationResponse != null ? true : false}
                      />
                    ) : (
                      <input
                        type="text"
                        className="form_control1"
                        name="biddingLimit"
                        style={{ width: "60%", marginLeft: "15px" }}
                        value={formValues?.biddingLimit}
                        onChange={(e) => {
                          if (e.target.value.length <= 15) handleInputChange(e);
                        }}
                        disabled={navigationResponse != null ? true : false}
                      />
                    )}
                  </div>
                  {errors &&
                    ((formValues.biddingLimit === "" &&
                      formValues.currency !== "") ||
                      (formValues.biddingLimit !== "" &&
                        formValues.currency === "")) && (
                      <span className="error">
                        Please select both the fields.
                      </span>
                    )}
                </div>

                <div className="form_submit_container">
                  {(biddersResponseObject && navigationCheck === false) ||
                  (registrationStatusKey - 4 > 0 &&
                    navigationCheck === false &&
                    navigationResponse) ? (
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
                          onClick={bidderSaveHandler}
                        />
                      )}
                      {isAddBidderandSaveBtnActive && (
                        <>
                          <input
                            type="button"
                            value="Add Bidder"
                            className="btn btn_add_bidder"
                            onClick={() => addBidder()}
                          />
                          <input
                            type="submit"
                            value="Proceed"
                            className="btn btn_submit_form"
                          />
                        </>
                      )}
                    </>
                  )}
                </div>
              </div>
            </form>
          </>
        )}

        {/* ---------------- Customer form ---------------- */}

        {isFormTypeCustomer && (
          <>
            {/* {
              <button
                onClick={() =>
                  console.log({
                    bidderList,
                    formValues,
                    bidderListCount,
                    activeBidder,
                  })
                }
              >
                get state
              </button>
            } */}
            <div className="nav_container">
              <div className="nav_items">
                <div className="nav_item active">
                  Requester
                  {/* <span className="info_red_icon">
                    <img src={iRedIcon} alt="i-red-icon" />
                  </span> */}
                </div>
                <div className="horizotal_row_container">
                  <span className="horizontal_row">&nbsp;</span>
                </div>
                <div
                  className="nav_item"
                  onClick={() =>
                    props.navigationHandler({
                      currentForm: "approver",
                      goForward: true,
                    })
                  }
                >
                  Approver
                </div>
                <div className="horizotal_row_container">
                  <span className="horizontal_row">&nbsp;</span>
                </div>
                <div
                  className="nav_item"
                  onClick={() =>
                    props.navigationHandler({
                      currentForm: "managingHead",
                      goForward: true,
                    })
                  }
                >
                  Managing Head
                </div>
              </div>
            </div>

            <div className="add_bidder_container">
              {bidderListCount?.map((el, index) => {
                if (activeBidder === index + 1) {
                  return (
                    <div className="bidder active" key={index}>
                      <span className="bidder_text"> R{index + 1} </span>
                      {navigationResponse === null &&
                        activeBidder === bidderListCount.length &&
                        activeBidder !== 1 &&
                        !isAddBidderandSaveBtnActive && (
                          <span className="remove_bidder">
                            <img
                              src={cancel}
                              alt="cancel"
                              onClick={removeBidder}
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
                      onClick={() => makeBidderActive(index + 1)}
                    >
                      <span className="bidder_text"> R{index + 1} </span>
                    </div>
                  );
                }
              })}
            </div>

            <form onSubmit={handleFormSubmission}>
              <div className="form_container">
                <div className="form_control1_spread">
                  <label className="form_label">Same as </label>
                  {navigationResponse !== null ? (
                    <input
                      type="text"
                      className="form_control1"
                      value={
                        formValues?.sameASUser === "Yes" ? formValues?.name : ""
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
                          formValues?.sameASUser === "Yes"
                            ? {
                                value: formValues?.name,
                                label: formValues?.name,
                              }
                            : ""
                        }
                        placeholder=""
                        disabled={navigationResponse !== null ? true : false}
                      />
                      {dataResponse && (
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
                <div className="form_control1_area">
                  <label className="form_label">
                    Name <span className="label_mandatory">*</span>
                    <Tooltip text="Bidder is mainly responsible for Quoting and assigning  the engineers." />
                  </label>
                  <input
                    type="text"
                    className="form_control1"
                    name="name"
                    value={formValues?.name}
                    onChange={handleInputChange}
                    disabled={
                      navigationResponse != null || dataResponse ? true : false
                    }
                    // disabled={navigationResponse != null ? true : false}
                  />
                  {errors && formValues.name.trim() === "" && (
                    <span className="error">Enter Full Name</span>
                  )}
                </div>

                <div className="form_control1_area address_margin">
                  <label className="form_label"> Designation </label>
                  <input
                    type="text"
                    className="form_control1"
                    name="designation"
                    value={formValues?.designation}
                    onChange={handleInputChange}
                    disabled={navigationResponse != null ? true : false}
                  />
                </div>

                <div className="form_control1_area">
                  <label className="form_label">
                    Email <span className="label_mandatory">*</span>
                    <Tooltip text="Official email (not personal email). This email will be used for all official communication." />
                  </label>
                  <input
                    type="text"
                    className="form_control1"
                    name="email"
                    value={formValues?.email}
                    onChange={(e) => {
                      handleInputChange(e);
                      validateEmail(e);
                    }}
                    disabled={
                      navigationResponse != null || dataResponse ? true : false
                    }
                    //disabled={navigationResponse != null ? true : false}
                  />
                  {errors &&
                    formValues.email.trim() === "" &&
                    !emailValidation(formValues.email.trim()) && (
                      <span className="error">Company Email ID</span>
                    )}
                  {errors &&
                    formValues.email.trim() !== "" &&
                    !emailValidation(formValues.email.trim()) && (
                      <span className="error">
                        Invalid Email ID, please check and re-enter
                      </span>
                    )}
                </div>

                <div className="form_control1_area ">
                  <label className="form_label">
                    Mobile No. <span className="label_mandatory">*</span>
                    <Tooltip text="Official mobile number (not personal mobile number). This mobile number will be used for all official communication." />
                  </label>
                  <div className="input_container">
                    {navigationResponse != null ? (
                      <select
                        className="select_input_disabled"
                        disabled={navigationResponse != null ? true : false}
                      >
                        <option>{formValues?.phoneCode}</option>
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
                          {formValues?.phoneCode}
                        </div>
                        <Select
                          onChange={handleInputChangePhone}
                          onInputChange={setHide}
                          disabled={navigationResponse != null ? true : false}
                          placeholder="Search code"
                          className="select_input"
                          options={options}
                          styles={customStyles}
                          value={
                            formValues?.phoneCode.length > 0
                              ? {
                                  label: formValues?.phoneCode,
                                  value: formValues?.phoneCode,
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
                      className="form_control1_phone"
                      name="primaryPhone"
                      value={formValues?.primaryPhone}
                      onChange={(e) => {
                        handleInputChange(e);
                        validatePhone(e);
                      }}
                      disabled={
                        navigationResponse != null || dataResponse
                          ? true
                          : false
                      }
                      //disabled={navigationResponse != null ? true : false}
                    />
                  </div>
                  {errors &&
                    (!numberValidation(formValues.primaryPhone) ||
                      formValues?.phoneCode === "" ||
                      formValues.primaryPhone === "") && (
                      <span className="error">
                        Select at least one code / Contact number should be
                        between 6 to 13 digit
                      </span>
                    )}
                </div>

                <div className="form_control1_spread">
                  <label className="form_label">
                    I am available on Whatsapp
                    <Tooltip text="We use Whatsapp for convenience of communication with parties involved." />
                  </label>
                  <div className="radio_container">
                    <span className="a_control">
                      <input
                        type="radio"
                        name="onWhatsapp"
                        className="form_control1_radio"
                        id="a_yes"
                        value="Yes"
                        checked={formValues?.onWhatsapp === true}
                        onChange={handleInputChange}
                        disabled={
                          navigationResponse != null || dataResponse
                            ? true
                            : false
                        }
                        // disabled={navigationResponse != null ? true : false}
                      />
                      <label className="form_label" htmlFor="a_yes">
                        Yes
                      </label>
                    </span>
                    <span className="a_control">
                      <input
                        type="radio"
                        name="onWhatsapp"
                        className="form_control1_radio"
                        id="a_no"
                        value="No"
                        checked={formValues?.onWhatsapp === false}
                        onChange={handleInputChange}
                        disabled={navigationResponse != null ? true : false}
                      />
                      <label className="form_label" htmlFor="a_no">
                        No
                      </label>
                    </span>
                  </div>
                </div>

                <div className="form_control1_spread">
                  <label className="form_label">
                    I am available on
                    <Tooltip text="Any alternate communication application other than Whatsapp." />
                  </label>
                  <span>
                    {navigationResponse != null || dataResponse ? (
                      <select className="form_control1_disable" disabled>
                        <option>
                          {formValues?.onWhatsapp === true
                            ? " "
                            : formValues?.availableOn}
                        </option>
                      </select>
                    ) : formValues?.onWhatsapp === true ? (
                      <input className="form_control1_disable" disabled></input>
                    ) : (
                      <Select
                        options={socialMedia}
                        className="form_control1_enable"
                        autocomplete="nope"
                        onChange={handleInputChangeAvailable}
                        value={{
                          value: formValues?.availableOn,
                          label: formValues?.availableOn,
                        }}
                      ></Select>
                    )}
                  </span>
                  {/* <span
                    value={
                      formValues?.onWhatsapp === "Yes"
                        ? ""
                        : formValues?.availableOn
                    }
                    // onChange={(e) => setAvailableOn(e.value)}
                    disabled={
                      navigationResponse != null ||
                      formValues?.onWhatsapp === true
                        ? true
                        : formValues?.onWhatsapp === true
                        ? true
                        : false
                    }
                  >
                    {navigationResponse != null ? (
                      <select className="form_control1_disable" disabled>
                        <option>{formValues?.availableOn}</option>
                      </select>
                    ) : formValues?.onWhatsapp === true ? (
                      <input className="form_control1_disable" disabled></input>
                    ) : (
                      <Select
                        options={socialMedia}
                        className="form_control1_enable"
                        onChange={handleInputChangeAvailable}
                        value={{
                          value: formValues?.availableOn,
                          label: formValues?.availableOn,
                        }}
                      ></Select>
                    )}
                  </span> */}
                  {errors &&
                    formValues.onWhatsapp === false &&
                    formValues.availableOn === "" && (
                      <span className="error">
                        Please select at least one option.
                      </span>
                    )}
                </div>

                <div className="form_control1_spread">
                  <label className="form_label">
                    Bidding Limit
                    <Tooltip text="Bidding Limit for bidder to quote above which bid requires a Approval from an Approver." />
                  </label>
                  <div
                    className="select_container"
                    style={{ position: "relative" }}
                  >
                    {navigationResponse != null ? (
                      <select
                        className="select_control_disable amount_version"
                        disabled
                      >
                        {<option>{formValues?.currency}</option>}
                      </select>
                    ) : (
                      <>
                        <Select
                          className="select_control_enable amount_version"
                          onChange={handleInputChangeCurrency}
                          options={currencyCountryOption}
                          classNamePrefix="select-currency"
                          value={{
                            value: formValues?.currency,
                            label: formValues?.currency,
                          }}
                        />
                        {formValues?.currency && (
                          <img
                            src={cancelIcon}
                            alt="cancel icon"
                            className="remove_data_state"
                            onClick={removeSelectState}
                          />
                        )}
                      </>
                    )}

                    {/* <select
                      className="select_control amount"
                      name="biddingLimit"
                      value={formValues?.biddingLimit}
                      onChange={handleInputChange}
                      disabled={navigationResponse != null ? true : false}
                    >
                    sdsd
                      <option disabled="disabled"></option>
                      <option value="1000">Above 1,000</option>
                      <option value="2000">Above 2,000</option>
                    </select> */}

                    {navigationResponse != null ? (
                      <input
                        type="text"
                        className="form_control1"
                        name="biddingLimit"
                        style={{ width: "60%", marginLeft: "15px" }}
                        value={formValues?.biddingLimit}
                        disabled={navigationResponse != null ? true : false}
                      />
                    ) : (
                      <input
                        type="text"
                        className="form_control1"
                        name="biddingLimit"
                        style={{ width: "60%", marginLeft: "15px" }}
                        value={formValues?.biddingLimit}
                        onChange={(e) => {
                          if (e.target.value.length <= 15) handleInputChange(e);
                        }}
                        disabled={navigationResponse != null ? true : false}
                      />
                    )}
                  </div>
                  {errors &&
                    ((formValues.biddingLimit === "" &&
                      formValues.currency !== "") ||
                      (formValues.biddingLimit !== "" &&
                        formValues.currency === "")) && (
                      <span className="error">
                        Please select both the fields.
                      </span>
                    )}
                </div>

                <div className="form_submit_container">
                  {(biddersResponseObject && navigationCheck === false) ||
                  (registrationStatusKey - 4 > 0 &&
                    navigationCheck === false &&
                    navigationResponse) ? (
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
                          onClick={bidderSaveHandler}
                        />
                      )}
                      {isAddBidderandSaveBtnActive && (
                        <>
                          <input
                            type="button"
                            value="Add Requester"
                            className="btn btn_add_bidder"
                            onClick={() => addBidder()}
                          />
                          <input
                            type="submit"
                            value="Proceed"
                            className="btn btn_submit_form"
                          />
                        </>
                      )}
                    </>
                  )}
                </div>
              </div>
            </form>
          </>
        )}
      </div>
    </div>
  );
};

export default Bidder;
