/* eslint-disable no-unused-expressions */
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import Select from "react-select";
import "./Details.scss";
import ConfirmationModal from "./../../Modals/ConfirmationModal/ConfirmationModal";
import Loading from "../../../../../components/Loading/Loading";
import Tooltip from "./../../../../../components/Tooltip/Tooltip";
import { ToastError } from "./../../../../../components/Tostify";
import {
  validateEmailPhone,
  validateEmailPhoneActions,
} from "../../../../../store/vendorRegistration/validateEmailPhone";
import {
  emailValidation,
  passwordValidation,
  numberValidation,
} from "../../../../../utils/helpers";
import {
  getAuthData,
  getCompanyId,
  getUserId,
} from "./../../../../../config/index";
import eyeIconOn from "./../../../../../assets/images/Icon-ionic-md-eye.png";
import eyeIconOff from "./../../../../../assets/images/Icon-md-eye-off.png";
import { useHistory } from "react-router-dom";
import {
  saveDetails,
  saveDetailActions,
} from "./../../../../../store/vendorRegistration/details";
import { saveDetailsForm } from "../../../../../store/vendorRegistration/saveDetailsForm";
import { getAvailableOn } from "./../../../../../store/common/availableOn";
import {
  commonGetAPiCall,
  commonGetApiActions,
} from "../../../../../store/common/commonGetApi";

import TermsAndConditions from "../../../../Login/components/Modals/TermsAndConditions/TermsAndConditions";
import {
  addressGetAPiCall,
  addressGetApiActions,
} from "../../../../../store/vendorRegistration/addressGetApi";

const Details = (props) => {
  const dispatch = useDispatch();
  const history = useHistory();

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

  const countries = useSelector((state) => state.countries.countries);
  const isCountryError = useSelector((state) => state.countries.isError);
  const detailsResponse = useSelector((state) => state.detailsReducer.details);
  const navigationCheck = useSelector(
    (state) => state.detailsReducer.navigationCheck
  );
  const isDetailsLoading = useSelector(
    (state) => state.detailsReducer.isLoading
  );
  const isDetailsResponseError = useSelector(
    (state) => state.detailsReducer.isError
  );
  const availableOnRes = useSelector(
    (state) => state.availableOnReducer.availableOnRes
  );
  const isAvailableOnLoading = useSelector(
    (state) => state.availableOnReducer.isLoading
  );
  const isAvailableOnError = useSelector(
    (state) => state.availableOnReducer.isError
  );
  const navigationResponse = useSelector(
    (state) => state.commonGetApiReducer.getApiRes
  );
  const registrationStatusKey = useSelector(
    (state) => state.commonGetApiReducer.registrationStatusKey
  );
  const addressData = useSelector(
    (state) => state.addressGetApiReducer.getApiRes
  );

  const [hide, setHide] = useState();
  const [adminName, setAdminName] = useState("");
  const [email, setEmail] = useState("");
  const [mobileNumber, setMobileNumber] = useState("");
  const [selectedPhonePrefix, setSelectedPhonePrefix] = useState("");
  const [onWhatsapp, setOnWhatsapp] = useState("No");
  const [availableOn, setAvailableOn] = useState("");
  const [termAndConditions, setTermsAndConditions] = useState(true);
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errors, setErrors] = useState(false);
  const [isConfirmationModal, setIsConfirmationModal] = useState(false);
  const [termConditionConfirmationModal, setTermConditionConfirmationModal] =
    useState(false);
  const [showTermsAndConditions, setShowTermsAndConditions] = useState(false);
  const [adminResponse, setAdminResponse] = useState(false);

  useEffect(() => {
    dispatch(getAvailableOn());
  }, [dispatch]);

  useEffect(() => {
    if (isCountryError !== "") {
      ToastError(isCountryError);
    } else if (isDetailsResponseError !== "") {
      ToastError(isDetailsResponseError);
    } else if (isAvailableOnError !== "") {
      ToastError(isAvailableOnError);
    }
  }, [isCountryError, isDetailsResponseError, isAvailableOnError]);

  useEffect(() => {
    if (navigationCheck) {
      if (detailsResponse !== null && isFormTypePartner) {
        props.setFormLabel("verifyEmail", 1);
      } else if (detailsResponse !== null && isFormTypeCustomer) {
        // props.setFormLabel("verifyEmail", 1);
      } else if (detailsResponse !== null && isFormTypePartnersIndividual) {
        props.setFormLabel("verifyEmail", 1);
      }
    }
  }, [
    detailsResponse,
    navigationCheck,
    props,
    isFormTypePartner,
    isFormTypeCustomer,
    isFormTypePartnersIndividual,
  ]);

  useEffect(() => {
    if (navigationResponse !== null) {
      const {
        name,
        email,
        primaryPhone,
        phoneCode,
        onWhatsapp,
        availableOn,
        sameASUser,
      } = navigationResponse;
      setAdminName(name);
      setEmail(email);
      setMobileNumber(primaryPhone);
      setSelectedPhonePrefix(phoneCode);
      setOnWhatsapp(onWhatsapp === true ? "Yes" : "No");
      setAvailableOn(availableOn);
      setPassword("**********");
      setConfirmPassword("**********");
      setTermsAndConditions(true);
      setAdminResponse(sameASUser === "Yes" ? true : false);
    }
  }, [navigationResponse]);

  const [passwordView, setPasswordView] = useState(eyeIconOff);
  const [showPassword, setShowPassword] = useState(false);
  const [inputType, setInputType] = useState("password");
  const changePasswordView = () => {
    if (showPassword === false) {
      setPasswordView(eyeIconOn);
      setShowPassword(true);
      setInputType("text");
    } else {
      setPasswordView(eyeIconOff);
      setShowPassword(false);
      setInputType("password");
    }
  };

  const [reEnterPasswordView, setReEnterPasswordView] = useState(eyeIconOff);
  const [showReEnterPassword, setShowReEnterPassword] = useState(false);
  const [reEnterInputType, setReEnterInputType] = useState("password");
  const changeReEnterPasswordView = () => {
    if (showReEnterPassword === false) {
      setReEnterPasswordView(eyeIconOn);
      setShowReEnterPassword(true);
      setReEnterInputType("text");
    } else {
      setReEnterPasswordView(eyeIconOff);
      setShowReEnterPassword(false);
      setReEnterInputType("password");
    }
  };

  const getCompanyIdCheck = () => {
    if (getAuthData()?.userId) {
      return getAuthData()?.userId;
    }
    return getUserId();
  };

  // get api call after login
  useEffect(() => {
    if (
      registrationStatusKey - 1 >= 0 &&
      (isFormTypePartner || isFormTypePartnersIndividual)
    ) {
      dispatch(
        commonGetAPiCall(
          `/vendor/getVendorAdmin/${
            JSON.parse(localStorage.getItem("authData"))?.userId
          }`
        )
      );
    } else if (registrationStatusKey - 1 >= 0 && isFormTypeCustomer) {
      dispatch(
        commonGetAPiCall(
          `/customer/getCustomerAdmin/${
            JSON.parse(localStorage.getItem("authData"))?.userId
          }`
        )
      );
    }
  }, [
    registrationStatusKey,
    isFormTypePartner,
    isFormTypeCustomer,
    isFormTypePartnersIndividual,
  ]);

  useEffect(() => {
    if (
      !navigationCheck &&
      detailsResponse !== null &&
      (isFormTypePartner || isFormTypePartnersIndividual)
    ) {
      dispatch(
        commonGetAPiCall(`/vendor/getVendorAdmin/${getCompanyIdCheck()}`)
      );
    } else if (
      !navigationCheck &&
      detailsResponse !== null &&
      isFormTypeCustomer
    ) {
      dispatch(
        commonGetAPiCall(`/customer/getCustomerAdmin/${getCompanyIdCheck()}`)
      );
    }
  }, [
    dispatch,
    navigationCheck,
    detailsResponse,
    isFormTypePartner,
    isFormTypeCustomer,
    isFormTypePartnersIndividual,
  ]);

  const validateForm = () => {
    if (
      adminName.trim() &&
      email.trim() &&
      mobileNumber.trim() &&
      mobileNumber.length > 5 &&
      mobileNumber.length < 14 &&
      selectedPhonePrefix &&
      password &&
      confirmPassword &&
      ((onWhatsapp === "No" && availableOn !== "") ||
      (onWhatsapp === "Yes" && availableOn === "")
        ? true
        : false) &&
      emailValidation(email.trim()) &&
      passwordValidation(password)
    ) {
      if (isFormTypePartnersIndividual && !termAndConditions) {
        setErrors(true);
        return false;
      }
      if (password === confirmPassword) {
        setErrors(false);
        return true;
      } else {
        setErrors(true);
        return false;
      }
    } else {
      setErrors(true);
      return false;
    }
  };

  const handleFormSubmission = (event) => {
    event.preventDefault();
    if (validateForm()) {
      setIsConfirmationModal(!isConfirmationModal);
    }
  };

  const handleFormSubmissionIndividual = (event) => {
    event.preventDefault();
    if (validateForm()) {
      setTermConditionConfirmationModal(!termConditionConfirmationModal);
    }
  };

  useEffect(() => {
    if (isFormTypePartnersIndividual) {
      dispatch(addressGetAPiCall(`/vendor/get/${getCompanyId()}`));
    }
  }, [dispatch, isFormTypePartnersIndividual]);

  const apiCall = () => {
    if (validateForm()) {
      const detailsObj = {
        name: adminName,
        email: email,
        password: password,
        primaryPhone: mobileNumber,
        phoneCode: selectedPhonePrefix,
        onWhatsapp: onWhatsapp === "Yes" ? true : false,
        availableOn: onWhatsapp === "Yes" ? "" : availableOn,
        companyId: getCompanyId(),
        roleModel: [
          {
            id: 1,
          },
        ],
      };
      if (isFormTypePartner) {
        detailsObj["sameASUser"] = "Yes";
      }
      if (isFormTypePartnersIndividual) {
        detailsObj["type"] = "Individual";
        if (adminResponse) {
          detailsObj["sameASUser"] = "Yes";
        } else {
          detailsObj["sameASUser"] = "No";
        }
      }
      if (isFormTypeCustomer) {
        detailsObj["type"] = "Customer";
        detailsObj["sameASUser"] = "No";
      }

      if (isFormTypePartner || isFormTypePartnersIndividual) {
        dispatch(saveDetailsForm(detailsObj));
        dispatch(saveDetails(detailsObj, "vendor/createVendorAdmin"));
      } else if (isFormTypeCustomer) {
        dispatch(saveDetailsForm(detailsObj));
        dispatch(saveDetails(detailsObj, "customer/createCustomerAdmin"));
      }
    }
  };

  const onCancelHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
  };
  const onOkHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
    apiCall();
  };
  const termsAndConditionsCancel = () => {
    setTermConditionConfirmationModal(!termConditionConfirmationModal);
  };

  const termsAndConditionsConfirm = () => {
    setTermConditionConfirmationModal(!termConditionConfirmationModal);
    apiCall();
  };

  const nextNavigationHandler = () => {
    dispatch(saveDetailActions.detailsNavigation(true));
    dispatch(commonGetApiActions.dispatchGetRequest());
    props.navigationHandler({
      currentForm: "companyHead",
      goForward: true,
    });
  };

  const nextNavigationHandlerIndividual = () => {
    dispatch(saveDetailActions.detailsNavigation(true));
    dispatch(commonGetApiActions.dispatchGetRequest());
    props.navigationHandler({
      currentForm: "businessInformation",
      goForward: true,
    });
  };

  const nextNavigationHandlerCustomer = () => {
    dispatch(saveDetailActions.detailsNavigation(true));
    dispatch(commonGetApiActions.dispatchGetRequest());
    props.navigationHandler({
      currentForm: "businessInformation",
      goForward: true,
    });
  };

  const whatsappAndAvailableOnHandler = (e) => {
    setOnWhatsapp(e.target.value);
    setAvailableOn("");
  };

  const closeEmailModal = () => {
    setShowTermsAndConditions(!showTermsAndConditions);
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

  const socialMedia = availableOnRes?.map((availableAt) => ({
    value: availableAt.app_Name,
    label: availableAt.app_Name,
  }));

  const handlePartnerInputChange = () => {
    setAdminName(addressData?.userModel?.name);
    setEmail(addressData?.userModel?.email);
    setMobileNumber(addressData?.userModel?.primaryPhone);
    setSelectedPhonePrefix(addressData?.userModel?.phoneCode);
    setAdminResponse(true);
  };

  const handlePartnerInputEmpty = () => {
    setAdminName("");
    setEmail("");
    setMobileNumber("");
    setSelectedPhonePrefix("");
    setAdminResponse(false);
  };

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
      width: "322px",
    }),
    placeholder: (defaultStyles) => {
      return {
        ...defaultStyles,
        fontSize: "12px",
        fontFamily: "Poppins-Regular",
        "@media only screen and (min-width: 950px) and (max-width: 1280px)": {
          ...defaultStyles[
            "@media only screen and (min-width: 950px) and (max-width: 1280px)"
          ],
          fontSize: "10px",
          fontFamily: "Poppins-Regular",
        },
      };
    },
  };
  return (
    <div className="form_container_area">
      <div className="details_form positioning_container">
        {(isDetailsLoading || isAvailableOnLoading) && (
          <Loading text="Loading..." />
        )}
        {isConfirmationModal && (
          <ConfirmationModal cancel={onCancelHandler} ok={onOkHandler} />
        )}
        {showTermsAndConditions && (
          <TermsAndConditions onClose={closeEmailModal} />
        )}
        {termConditionConfirmationModal && (
          <TermsAndConditions
            cancel={termsAndConditionsCancel}
            ok={termsAndConditionsConfirm}
          />
        )}

        {/* ---------------- Partner form ---------------- */}
        {isFormTypePartner && (
          <div>
            <div className="nav_container">
              <div className="nav_items">
                <div
                  className="nav_item active"
                  onClick={() =>
                    props.navigationHandler({
                      currentForm: "address",
                      goForward: false,
                    })
                  }
                >
                  Address
                </div>
                <div className="horizotal_row_container">
                  <span className="horizontal_row active">&nbsp;</span>
                </div>
                <div className="nav_item active">Details</div>
                <div className="horizotal_row_container">
                  <span className="horizontal_row">&nbsp;</span>
                </div>
                <div
                  className="nav_item"
                  onClick={() =>
                    props.navigationHandler({
                      currentForm: "companyHead",
                      goForward: false,
                    })
                  }
                >
                  Company Head
                </div>
                <div className="horizotal_row_container">
                  <span className="horizontal_row">&nbsp;</span>
                </div>
                <div
                  className="nav_item"
                  onClick={() =>
                    props.navigationHandler({
                      currentForm: "businessInformation",
                      goForward: false,
                    })
                  }
                >
                  Business Information
                </div>
              </div>
            </div>

            {/* <button onClick={() => console.log(isConfirmationModal)}>Get State</button> */}

            <form onSubmit={handleFormSubmission}>
              <div className="form_container">
                <div className="form_control_area">
                  <label className="form_label">
                    Admin Name <span className="label_mandatory">*</span>
                    <Tooltip text="Name of person who would be primarily managing this account." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={adminName}
                    onChange={(e) => {
                      if (!Number(e.nativeEvent.data))
                        setAdminName(e.target.value);
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors && adminName.trim() === "" && (
                    <span className="error">Enter Full Name </span>
                  )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Email <span className="label_mandatory">*</span>
                    <Tooltip text="Official email (not personal email). This email will be used for all administrative communication with us." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={email}
                    onChange={(e) =>
                      setEmail(e.target.value.trim())
                    }
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors &&
                    email.trim() === "" &&
                    !emailValidation(email.trim()) && (
                      <span className="error">Company Email ID</span>
                    )}
                  {errors &&
                    email.trim() !== "" &&
                    !emailValidation(email.trim()) && (
                      <span className="error">
                        Invalid Email ID, please check and re-enter
                      </span>
                    )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Mobile No. <span className="label_mandatory">*</span>
                    <Tooltip text="Mobile number will be used for communication and verification." />
                  </label>
                  <div className="input_container">
                    {navigationResponse != null ? (
                      <select
                        className="select_input_disabled"
                        disabled={navigationResponse != null ? true : false}
                      >
                        <option>{navigationResponse?.phoneCode}</option>
                        {selectedPhonePrefix}
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
                          {selectedPhonePrefix}
                        </div>
                        <Select
                          onChange={(e) => {
                            setSelectedPhonePrefix(e.value);
                          }}
                          onInputChange={setHide}
                          disabled={navigationResponse != null ? true : false}
                          placeholder="Search code"
                          className="select_input"
                          options={options}
                          styles={customStyles}
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
                      className="form_control_phone"
                      value={mobileNumber}
                      onChange={(e) => {
                        if (e.target.value.length < 14)
                          setMobileNumber(e.target.value.trim());
                      }}
                      disabled={navigationResponse != null ? true : false}
                    />
                  </div>
                  {errors &&
                    (!numberValidation(mobileNumber) ||
                      mobileNumber.trim() === "" ||
                      selectedPhonePrefix === "") && (
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
                        name="admin-account"
                        className="form_control_radio"
                        id="yes"
                        value="Yes"
                        onChange={whatsappAndAvailableOnHandler}
                        checked={onWhatsapp === "Yes"}
                        disabled={navigationResponse != null ? true : false}
                      />
                      <label className="form_label" htmlFor="yes">
                        Yes
                      </label>
                    </span>
                    <span className="a_control">
                      <input
                        type="radio"
                        name="admin-account"
                        className="form_control_radio"
                        id="no"
                        value="No"
                        onChange={whatsappAndAvailableOnHandler}
                        checked={onWhatsapp === "No"}
                        disabled={navigationResponse != null ? true : false}
                      />
                      <label className="form_label" htmlFor="no">
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
                  <span
                    value={onWhatsapp === "Yes" ? "" : availableOn}
                    // onChange={(e) => setAvailableOn(e.value)}
                    disabled={
                      navigationResponse != null
                        ? true
                        : onWhatsapp === "Yes"
                        ? true
                        : false
                    }
                  >
                    {navigationResponse != null ? (
                      <option disabled className="form_control_disable">
                        {navigationResponse?.availableOn}
                      </option>
                    ) : onWhatsapp === "Yes" ? (
                      <input className="form_control_disable" disabled></input>
                    ) : (
                      <Select
                        options={socialMedia}
                        className="form_control"
                        autocomplete="nope"
                        onChange={(e) => setAvailableOn(e.value)}
                      ></Select>
                    )}
                  </span>
                  {errors && onWhatsapp === "No" && availableOn === "" && (
                    <span className="error">
                      Please select at least one option.
                    </span>
                  )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Create a Password <span className="label_mandatory">*</span>
                    <Tooltip text="Password must contain at least eight characters, at least one number and both lower and uppercase letters and special characters (!#$%&'()*+,-./:;<=>?@[]^_{|}~)." />
                  </label>
                  <input
                    type={inputType}
                    className="form_control"
                    value={password}
                    onChange={(e) => setPassword(e.target.value.trim())}
                    disabled={navigationResponse != null ? true : false}
                  />
                  <div className="eye_icon">
                    <img
                      src={passwordView}
                      onClick={changePasswordView}
                      alt="eye-icon"
                    />
                  </div>
                  {errors &&
                    (password === "" || !passwordValidation(password)) && (
                      <span className="error">
                        {`The length of Password must be at least eight characters. It should include at least one number, at least one lowercase letter, at least one uppercase letter and at least one special character. (!"#$%&'()*+,-./:;<=>?@[]^_{|}~).`}
                      </span>
                    )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Re-enter Password <span className="label_mandatory">*</span>
                  </label>
                  <input
                    type={reEnterInputType}
                    className="form_control"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value.trim())}
                    disabled={navigationResponse != null ? true : false}
                  />
                  <div className="eye_icon">
                    <img
                      src={reEnterPasswordView}
                      onClick={changeReEnterPasswordView}
                      alt="eye-icon"
                    />
                  </div>
                  {errors &&
                    passwordValidation(password) &&
                    (confirmPassword === "" ||
                      !passwordValidation(confirmPassword) ||
                      confirmPassword !== password) && (
                      <span className="error">
                        Re-enter password should be same as create password
                      </span>
                    )}
                </div>

                <div className="form_submit_container">
                  {(detailsResponse && navigationCheck === false) ||
                  (registrationStatusKey - 1 >= 0 &&
                    navigationCheck === false &&
                    navigationResponse) ? (
                    <input
                      type="button"
                      value=" Next"
                      className="btn_submit_form"
                      onClick={nextNavigationHandler}
                    />
                  ) : (
                    <input
                      type="submit"
                      value="Proceed"
                      className="btn_submit_form"
                    />
                  )}
                </div>
              </div>
            </form>
          </div>
        )}

        {/* ---------------- Customer form ---------------- */}
        {isFormTypeCustomer && (
          <div>
            <div className="nav_container">
              <div className="nav_items">
                <div
                  className="nav_item active"
                  onClick={() =>
                    props.navigationHandler({
                      currentForm: "address",
                      goForward: false,
                    })
                  }
                >
                  Address
                </div>
                <div className="horizotal_row_container">
                  <span className="horizontal_row active">&nbsp;</span>
                </div>
                <div className="nav_item active">Details</div>
                <div className="horizotal_row_container">
                  <span className="horizontal_row">&nbsp;</span>
                </div>
                <div
                  className="nav_item"
                  onClick={() =>
                    props.navigationHandler({
                      currentForm: "businessInformation",
                      goForward: false,
                    })
                  }
                >
                  Business Information
                </div>
              </div>
            </div>

            <form onSubmit={handleFormSubmission}>
              <div className="form_container">
                <div className="form_control_area">
                  <label className="form_label">
                    Admin Name <span className="label_mandatory">*</span>
                    <Tooltip text="Name of person who would be primarily managing this account." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={adminName}
                    onChange={(e) => {
                      if (!Number(e.nativeEvent.data))
                        setAdminName(e.target.value);
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors && adminName.trim() === "" && (
                    <span className="error">Enter Full Name </span>
                  )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Email <span className="label_mandatory">*</span>
                    <Tooltip text="Official email (not personal email). This email will be used for all administrative communication with us." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={email}
                    onChange={(e) =>
                      setEmail(e.target.value.trim()) + validateEmail(e)
                    }
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors &&
                    email.trim() === "" &&
                    !emailValidation(email.trim()) && (
                      <span className="error">Company Email ID</span>
                    )}
                  {errors &&
                    email.trim() !== "" &&
                    !emailValidation(email.trim()) && (
                      <span className="error">
                        Invalid Email ID, please check and re-enter
                      </span>
                    )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Mobile No. <span className="label_mandatory">*</span>
                    <Tooltip text="Mobile number will be used for communication and verification." />
                  </label>
                  <div className="input_container">
                    {navigationResponse != null ? (
                      <select
                        className="select_input_disabled"
                        disabled={navigationResponse != null ? true : false}
                      >
                        <option>{navigationResponse?.phoneCode}</option>
                        {selectedPhonePrefix}
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
                          {selectedPhonePrefix}
                        </div>
                        <Select
                          onChange={(e) => {
                            setSelectedPhonePrefix(e.value);
                          }}
                          onInputChange={setHide}
                          disabled={navigationResponse != null ? true : false}
                          placeholder="Search code"
                          className="select_input"
                          options={options}
                          styles={customStyles}
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
                      className="form_control_phone"
                      value={mobileNumber}
                      onChange={(e) => {
                        if (e.target.value.length < 14)
                          setMobileNumber(e.target.value.trim());
                        +validatePhone(e);
                      }}
                      disabled={navigationResponse != null ? true : false}
                    />
                  </div>
                  {errors &&
                    (!numberValidation(mobileNumber) ||
                      mobileNumber.trim() === "" ||
                      selectedPhonePrefix === "") && (
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
                        name="admin-account"
                        className="form_control_radio"
                        id="yes"
                        value="Yes"
                        onChange={(e) => setOnWhatsapp(e.target.value)}
                        checked={onWhatsapp === "Yes"}
                        disabled={navigationResponse != null ? true : false}
                      />
                      <label className="form_label" htmlFor="yes">
                        Yes
                      </label>
                    </span>
                    <span className="a_control">
                      <input
                        type="radio"
                        name="admin-account"
                        className="form_control_radio"
                        id="no"
                        value="No"
                        onChange={whatsappAndAvailableOnHandler}
                        checked={onWhatsapp === "No"}
                        disabled={navigationResponse != null ? true : false}
                      />
                      <label className="form_label" htmlFor="no">
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
                  <span
                    value={onWhatsapp === "Yes" ? "" : availableOn}
                    // onChange={(e) => setAvailableOn(e.value)}
                    disabled={
                      navigationResponse != null
                        ? true
                        : onWhatsapp === "Yes"
                        ? true
                        : false
                    }
                  >
                    {navigationResponse != null ? (
                      <option disabled className="form_control_disable">
                        {navigationResponse?.availableOn}
                      </option>
                    ) : onWhatsapp === "Yes" ? (
                      <input className="form_control_disable" disabled></input>
                    ) : (
                      <Select
                        options={socialMedia}
                        className="form_control"
                        autocomplete="nope"
                        onChange={(e) => setAvailableOn(e.value)}
                      ></Select>
                    )}
                  </span>
                  {errors && onWhatsapp === "No" && availableOn === "" && (
                    <span className="error">
                      Please select at least one option.
                    </span>
                  )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Create a Password <span className="label_mandatory">*</span>
                    <Tooltip text="Password must contain at least eight characters, at least one number and both lower and uppercase letters and special characters (!#$%&'()*+,-./:;<=>?@[]^_{|}~)." />
                  </label>
                  <input
                    type={inputType}
                    className="form_control"
                    value={password}
                    onChange={(e) => setPassword(e.target.value.trim())}
                    disabled={navigationResponse != null ? true : false}
                  />
                  <div className="eye_icon">
                    <img
                      src={passwordView}
                      onClick={changePasswordView}
                      alt="eye-icon"
                    />
                  </div>
                  {errors &&
                    (password === "" || !passwordValidation(password)) && (
                      <span className="error">
                        {`The length of Password must be at least eight characters. It should include at least one number, at least one lowercase letter, at least one uppercase letter and at least one special character. (!"#$%&'()*+,-./:;<=>?@[]^_{|}~).`}
                      </span>
                    )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Re-enter Password <span className="label_mandatory">*</span>
                  </label>
                  <input
                    type={reEnterInputType}
                    className="form_control"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value.trim())}
                    disabled={navigationResponse != null ? true : false}
                  />
                  <div className="eye_icon">
                    <img
                      src={reEnterPasswordView}
                      onClick={changeReEnterPasswordView}
                      alt="eye-icon"
                    />
                  </div>
                  {errors &&
                    passwordValidation(password) &&
                    (confirmPassword === "" ||
                      !passwordValidation(confirmPassword) ||
                      confirmPassword !== password) && (
                      <span className="error">Password does not match.</span>
                    )}
                </div>

                <div className="form_submit_container">
                  {(detailsResponse && navigationCheck === false) ||
                  (registrationStatusKey - 1 >= 0 &&
                    navigationCheck === false &&
                    navigationResponse) ? (
                    <input
                      type="button"
                      value=" Next"
                      className="btn_submit_form"
                      onClick={nextNavigationHandlerCustomer}
                    />
                  ) : (
                    <input
                      type="submit"
                      value="Proceed"
                      className="btn_submit_form"
                    />
                  )}
                </div>
              </div>
            </form>
          </div>
        )}

        {/* ---------------- Partner's Individual form ---------------- */}
        {isFormTypePartnersIndividual && (
          <div>
            <div className="nav_container">
              <div className="nav_items">
                <div
                  className="nav_item active"
                  onClick={() =>
                    props.navigationHandler({
                      currentForm: "address",
                      goForward: false,
                    })
                  }
                >
                  Address
                </div>
                <div className="horizotal_row_container">
                  <span className="horizontal_row active">&nbsp;</span>
                </div>
                <div className="nav_item active">Details</div>
                <div className="horizotal_row_container">
                  <span className="horizontal_row">&nbsp;</span>
                </div>
                <div
                  className="nav_item"
                  onClick={() =>
                    props.navigationHandler({
                      currentForm: "businessInformation",
                      goForward: false,
                    })
                  }
                >
                  Business Information
                </div>
              </div>
            </div>
            {/* <button onClick={() => console.log(isConfirmationModal)}>Get State</button> */}

            <form onSubmit={handleFormSubmissionIndividual}>
              <div className="form_container">
                <div className="form_control_spread">
                  <label className="form_label">
                    Are your details same as company details?
                  </label>
                  <div className="radio_container">
                    <span className="a_control">
                      <input
                        type="radio"
                        name="sameAsAdmin"
                        className="form_control_radio"
                        id="a_yes"
                        value="yes"
                        checked={adminResponse === true}
                        onChange={handlePartnerInputChange}
                        disabled={navigationResponse != null ? true : false}
                      />
                      <label className="form_label" htmlFor="a_yes">
                        Yes
                      </label>
                    </span>
                    <span className="a_control">
                      <input
                        type="radio"
                        name="sameAsAdmin"
                        className="form_control_radio"
                        id="a_no"
                        value="no"
                        checked={adminResponse === false}
                        onChange={handlePartnerInputEmpty}
                        disabled={navigationResponse != null ? true : false}
                      />
                      <label className="form_label" htmlFor="a_no">
                        No
                      </label>
                    </span>
                  </div>
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Admin Name <span className="label_mandatory">*</span>
                    <Tooltip text="Name of person who would be primarily managing this account." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={adminName}
                    onChange={(e) => {
                      if (!Number(e.nativeEvent.data))
                        setAdminName(e.target.value);
                    }}
                    disabled={
                      navigationResponse != null || adminResponse ? true : false
                    }
                  />
                  {errors && adminName.trim() === "" && (
                    <span className="error">Enter Full Name </span>
                  )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Email <span className="label_mandatory">*</span>
                    <Tooltip text="Official email (not personal email). This email will be used for all administrative communication with us." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={email}
                    onChange={(e) =>
                      setEmail(e.target.value.trim()) + validateEmail(e)
                    }
                    disabled={
                      navigationResponse != null || adminResponse ? true : false
                    }
                  />
                  {errors &&
                    email.trim() === "" &&
                    !emailValidation(email.trim()) && (
                      <span className="error">Company Email ID</span>
                    )}
                  {errors &&
                    email.trim() !== "" &&
                    !emailValidation(email.trim()) && (
                      <span className="error">
                        Invalid Email ID, please check and re-enter
                      </span>
                    )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Mobile No. <span className="label_mandatory">*</span>
                    <Tooltip text="Mobile number will be used for communication and verification." />
                  </label>
                  <div className="input_container">
                    {navigationResponse != null || adminResponse ? (
                      <select
                        className="select_input_disabled"
                        disabled={
                          navigationResponse != null || adminResponse
                            ? true
                            : false
                        }
                      >
                        <option>{selectedPhonePrefix}</option>
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
                          {selectedPhonePrefix}
                        </div>
                        <Select
                          onChange={(e) => {
                            setSelectedPhonePrefix(e.value);
                          }}
                          onInputChange={setHide}
                          disabled={
                            navigationResponse != null || adminResponse
                              ? true
                              : false
                          }
                          placeholder="Search code"
                          className="select_input"
                          options={options}
                          styles={customStyles}
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
                      className="form_control_phone"
                      value={mobileNumber}
                      onChange={(e) => {
                        if (e.target.value.length < 14)
                          setMobileNumber(e.target.value.trim());
                        +validatePhone(e);
                      }}
                      disabled={
                        navigationResponse != null || adminResponse
                          ? true
                          : false
                      }
                    />
                  </div>
                  {errors &&
                    (!numberValidation(mobileNumber) ||
                      mobileNumber.trim() === "" ||
                      selectedPhonePrefix === "") && (
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
                        name="admin-account"
                        className="form_control_radio"
                        id="yes"
                        value="Yes"
                        onChange={whatsappAndAvailableOnHandler}
                        checked={onWhatsapp === "Yes"}
                        disabled={navigationResponse != null ? true : false}
                      />
                      <label className="form_label" htmlFor="yes">
                        Yes
                      </label>
                    </span>
                    <span className="a_control">
                      <input
                        type="radio"
                        name="admin-account"
                        className="form_control_radio"
                        id="no"
                        value="No"
                        onChange={whatsappAndAvailableOnHandler}
                        checked={onWhatsapp === "No"}
                        disabled={navigationResponse != null ? true : false}
                      />
                      <label className="form_label" htmlFor="no">
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
                  <span
                    value={onWhatsapp === "Yes" ? "" : availableOn}
                    // onChange={(e) => setAvailableOn(e.value)}
                    disabled={
                      navigationResponse != null
                        ? true
                        : onWhatsapp === "Yes"
                        ? true
                        : false
                    }
                  >
                    {navigationResponse != null ? (
                      <option disabled className="form_control_disable">
                        {navigationResponse?.availableOn}
                      </option>
                    ) : onWhatsapp === "Yes" ? (
                      <input className="form_control_disable" disabled></input>
                    ) : (
                      <Select
                        options={socialMedia}
                        autocomplete="nope"
                        className="form_control"
                        onChange={(e) => setAvailableOn(e.value)}
                      ></Select>
                    )}
                  </span>
                  {errors && onWhatsapp === "No" && availableOn === "" && (
                    <span className="error">
                      Please select at least one option.
                    </span>
                  )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Create a Password <span className="label_mandatory">*</span>
                    <Tooltip text="Password must contain at least eight characters, at least one number and both lower and uppercase letters and special characters (!#$%&'()*+,-./:;<=>?@[]^_{|}~)." />
                  </label>
                  <input
                    type={inputType}
                    className="form_control"
                    value={password}
                    onChange={(e) => setPassword(e.target.value.trim())}
                    disabled={navigationResponse != null ? true : false}
                  />
                  <div className="eye_icon">
                    <img
                      src={passwordView}
                      onClick={changePasswordView}
                      alt="eye-icon"
                    />
                  </div>
                  {errors &&
                    (password === "" || !passwordValidation(password)) && (
                      <span className="error">
                        {`The length of Password must be at least eight characters. It should include at least one number, at least one lowercase letter, at least one uppercase letter and at least one special character. (!"#$%&'()*+,-./:;<=>?@[]^_{|}~).`}
                      </span>
                    )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Re-enter Password <span className="label_mandatory">*</span>
                  </label>
                  <input
                    type={reEnterInputType}
                    className="form_control"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value.trim())}
                    disabled={navigationResponse != null ? true : false}
                  />
                  <div className="eye_icon">
                    <img
                      src={reEnterPasswordView}
                      onClick={changeReEnterPasswordView}
                      alt="eye-icon"
                    />
                  </div>
                  {errors &&
                    passwordValidation(password) &&
                    (confirmPassword === "" ||
                      !passwordValidation(confirmPassword) ||
                      confirmPassword !== password) && (
                      <span className="error">
                        Re-enter password should be same as create password
                      </span>
                    )}
                </div>

                <div className="form_submit_container">
                  {(detailsResponse && navigationCheck === false) ||
                  (registrationStatusKey - 1 >= 0 &&
                    navigationCheck === false &&
                    navigationResponse) ? (
                    <input
                      type="button"
                      value=" Next"
                      className="btn_submit_form"
                      onClick={nextNavigationHandlerIndividual}
                    />
                  ) : (
                    <input
                      type="submit"
                      value="Proceed"
                      className="btn_submit_form"
                    />
                  )}
                </div>
              </div>
            </form>
          </div>
        )}
      </div>
    </div>
  );
};

export default Details;