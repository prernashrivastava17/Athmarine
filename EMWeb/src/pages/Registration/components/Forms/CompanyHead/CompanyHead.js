/* eslint-disable no-unused-expressions */
/* eslint-disable no-undef */
import React, { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import Select from "react-select";
import "./CompanyHead.scss";
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
  saveCompanyHead,
  saveCompanyHeadSlice,
} from "./../../../../../store/vendorRegistration/companyHead";
import { getAvailableOn } from "./../../../../../store/common/availableOn";
import {
  getAuthData,
  getCompanyId,
  getReferralCode,
  getUserId,
} from "../../../../../config/index";
import {
  commonGetAPiCall,
  commonGetApiActions,
} from "../../../../../store/common/commonGetApi";
import TermsAndConditions from "../../../../Login/components/Modals/TermsAndConditions/TermsAndConditions";
import eyeIconOn from "./../../../../../assets/images/Icon-ionic-md-eye.png";
import eyeIconOff from "./../../../../../assets/images/Icon-md-eye-off.png";
import { getAllUserDetails } from "../../../../../store/common/allUserDetails";

const CompanyHead = (props) => {
  const dispatch = useDispatch();

  let isFormTypePartner = props.selectedUser === "partner" ? true : false;
  let isFormTypeCustomer = props.selectedUser === "customer" ? true : false;

  const countries = useSelector((state) => state.countries.countries);
  const isCountryError = useSelector((state) => state.countries.isError);
  const companyHeadStatus = useSelector(
    (state) => state.vendorCompanyHead.companyHeadObject
  );
  const navigationCheck = useSelector(
    (state) => state.vendorCompanyHead.navigationCheck
  );
  const isCompanyHeadLoading = useSelector(
    (state) => state.vendorCompanyHead.isLoading
  );
  const isCompanyHeadError = useSelector(
    (state) => state.vendorCompanyHead.isError
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
  const navigationResponseLoading = useSelector(
    (state) => state.commonGetApiReducer.isLoading
  );

  const registrationStatusKey = useSelector(
    (state) => state.commonGetApiReducer.registrationStatusKey
  );

  const allUserDetails = useSelector(
    (state) => state.allUserDetailsReducer.allUserDetails
  );

  const [companyHeadRoleID, setCompanyHeadRoleID] = useState(5);
  const [hide, setHide] = useState();
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [mobileNumber, setMobileNumber] = useState("");
  const [selectedPhonePrefix, setSelectedPhonePrefix] = useState("");
  const [onWhatsapp, setOnWhatsapp] = useState("No");
  const [availableOn, setAvailableOn] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [termAndConditions, setTermsAndConditions] = useState(false);
  const [errors, setErrors] = useState(false);
  const [isConfirmationModal, setIsConfirmationModal] = useState(false);
  const [showTermsAndConditions, setShowTermsAndConditions] = useState(false);
  const [sameAsAdmin, setSameAsAdmin] = useState([""]);
  const [adminResponse, setAdminResponse] = useState(false);

  const getCompanyIdCheck = () => {
    if (getAuthData()?.userId) {
      return getAuthData()?.userId;
    }
    return getUserId();
  };

  // get api call after login
  useEffect(() => {
    if (registrationStatusKey - 2 > 0 && isFormTypePartner) {
      dispatch(
        commonGetAPiCall(
          `/vendor/getVendorCompanyHead/${
            JSON.parse(localStorage.getItem("authData"))?.companyId
          }`
        )
      );
    }
  }, [dispatch, registrationStatusKey, isFormTypePartner, isFormTypeCustomer]);

  useEffect(() => {
    if (!navigationCheck && companyHeadStatus !== null && isFormTypePartner) {
      dispatch(
        commonGetAPiCall(`/vendor/getVendorCompanyHead/${getCompanyId()}`)
      );
    }
  }, [dispatch, navigationCheck, companyHeadStatus, isFormTypePartner]);

  useEffect(() => {
    dispatch(getAvailableOn());
    dispatch(getAllUserDetails());
  }, [dispatch]);

  useEffect(() => {
    setSameAsAdmin(allUserDetails);
  }, [allUserDetails]);

  useEffect(() => {
    if (isCountryError !== "") {
      ToastError(isCountryError);
    } else if (isCompanyHeadError !== "") {
      ToastError(isCompanyHeadError);
    } else if (isAvailableOnError !== "") {
      ToastError(isAvailableOnError);
    }
  }, [isCountryError, isCompanyHeadError, isAvailableOnError]);

  useEffect(() => {
    if (navigationCheck) {
      if (companyHeadStatus !== null) {
        props.setFormLabel("businessInformation", 1);
      }
    }
  }, [companyHeadStatus, props, navigationCheck]);

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
      setName(name);
      setEmail(email);
      setMobileNumber(primaryPhone);
      setSelectedPhonePrefix(phoneCode);
      setOnWhatsapp(onWhatsapp === true ? "Yes" : "No");
      setAvailableOn(availableOn);
      setPassword("**********");
      setConfirmPassword("**********");
      setAdminResponse(sameASUser === "Yes" ? true : false);
      setTermsAndConditions(true);
    }
  }, [navigationResponse]);

  const handlePartnerInputChange = () => {
    setName(sameAsAdmin[0].name);
    setEmail(sameAsAdmin[0].email);
    setMobileNumber(sameAsAdmin[0].primaryPhone);
    setSelectedPhonePrefix(sameAsAdmin[0].phoneCode);
    setOnWhatsapp(sameAsAdmin[0].onWhatsapp === true ? "Yes" : "No");
    setAvailableOn(sameAsAdmin[0].availableOn);
    setPassword(sameAsAdmin[0].password);
    setConfirmPassword(sameAsAdmin[0].password);
    setTermsAndConditions(true);
    setAdminResponse(true);
  };

  const handlePartnerInputEmpty = () => {
    setName("");
    setEmail("");
    setMobileNumber("");
    setSelectedPhonePrefix("");
    setOnWhatsapp("No");
    setAvailableOn("");
    setPassword("");
    setConfirmPassword("");
    setTermsAndConditions(true);
    setAdminResponse(false);
  };

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

  const validateForm = () => {
    if (
      name.trim() &&
      email.trim() &&
      mobileNumber.trim() &&
      mobileNumber.length > 5 &&
      mobileNumber.length < 14 &&
      selectedPhonePrefix &&
      password &&
      confirmPassword &&
      // termAndConditions &&
      ((onWhatsapp === "No" && availableOn !== "") ||
      (onWhatsapp === "Yes" && availableOn === "")
        ? true
        : false) &&
      emailValidation(email) &&
      passwordValidation(password)
    ) {
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

  const apiCall = () => {
    if (validateForm()) {
      const companyHeadObj = {
        name: name,
        email: email,
        password: password,
        primaryPhone: mobileNumber,
        phoneCode: selectedPhonePrefix,
        onWhatsapp: onWhatsapp === "Yes" ? true : false,
        availableOn: onWhatsapp === "Yes" ? "" : availableOn,
        companyId: getCompanyId(),
        sameASUser: adminResponse === true ? "Yes" : "No",
        roleModel: [
          {
            id: 5,
          },
        ],
      };
      dispatch(saveCompanyHead(companyHeadObj));
    }
  };

  const onCancelHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
  };

  const onOkHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
    apiCall();
  };

  const nextNavigationHandler = () => {
    dispatch(saveCompanyHeadSlice.companyHeadNavigationPass(true));
    dispatch(commonGetApiActions.dispatchGetRequest());
    props.navigationHandler({
      currentForm: "businessInformation",
      goForward: true,
    });
  };

  const closeEmailModal = () => {
    setShowTermsAndConditions(!showTermsAndConditions);
  };

  const agreeTermAndConditions = () => {
    setTermsAndConditions(!termAndConditions);
    setShowTermsAndConditions(!showTermsAndConditions);
  };

  const whatsappAndAvailableOnHandler = (e) => {
    setOnWhatsapp(e.target.value);
    setAvailableOn("");
  };

  const options = countries?.map((country) => ({
    value: country.phoneCode,
    label: [
      <img src={country.flag} style={{ width: "20px", marginRight: "15px" }} />,
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
    <>
      <div className="form_container_area">
        <div className="company_head_form positioning_container">
          {(isCompanyHeadLoading || isAvailableOnLoading) && (
            <Loading text={"Loading...."} />
          )}
          {isConfirmationModal && (
            <TermsAndConditions cancel={onCancelHandler} ok={onOkHandler} />
          )}

          {/* {showTermsAndConditions && (
            <TermsAndConditions onClose={closeEmailModal} onConfirm={agreeTermAndConditions} />
          )} */}
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
              <div
                className="nav_item active"
                onClick={() =>
                  props.navigationHandler({
                    currentForm: "details",
                    goForward: true,
                  })
                }
              >
                Details
              </div>
              <div className="horizotal_row_container">
                <span className="horizontal_row active">&nbsp;</span>
              </div>
              <div className="nav_item active">Company Head</div>
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
          {/* <button onClick={() => console.log(availableOn, onWhatsapp)}>Get State</button> */}

          <div className="company_id_details">
            <p className="company_id">Company id: {getReferralCode()}</p>
          </div>

          <form onSubmit={handleFormSubmission}>
            <div className="form_container">
              <div className="form_control_spread">
                <label className="form_label">
                  Is the Company Head same as admin ?
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
                  Name <span className="label_mandatory">*</span>
                  <Tooltip text="Name of company head / final authority." />
                </label>
                <input
                  type="text"
                  className="form_control"
                  value={name}
                  onChange={(e) => {
                    if (!Number(e.nativeEvent.data)) setName(e.target.value);
                  }}
                  disabled={
                    navigationResponse != null || adminResponse ? true : false
                  }
                />
                {errors && name.trim() === "" && (
                  <span className="error"> Enter Full Name </span>
                )}
              </div>

              <div className="form_control_area">
                <label className="form_label">
                  Email <span className="label_mandatory">*</span>
                  <Tooltip text="email id of company head." />
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
                        navigationResponse || adminResponse != null
                          ? true
                          : false
                      }
                    >
                      <option>
                        {adminResponse
                          ? selectedPhonePrefix
                          : navigationResponse?.phoneCode}
                      </option>
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
                    disabled={
                      navigationResponse != null || adminResponse ? true : false
                    }
                  />
                </div>
                {errors &&
                  (!numberValidation(mobileNumber) ||
                    mobileNumber === "" ||
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
                      disabled={
                        navigationResponse != null || adminResponse
                          ? true
                          : false
                      }
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
                      disabled={
                        navigationResponse != null || adminResponse
                          ? true
                          : false
                      }
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
                    navigationResponse != null || adminResponse
                      ? true
                      : onWhatsapp === "Yes"
                      ? true
                      : false
                  }
                >
                  {navigationResponse != null || adminResponse ? (
                    <option className="form_control_disable">
                      {adminResponse
                        ? availableOn
                        : navigationResponse?.availableOn}
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
                  Password <span className="label_mandatory">*</span>
                  <Tooltip text="Password must contain at least eight characters, at least one number and both lower and uppercase letters and special characters (!#$%&'()*+,-./:;<=>?@[]^_{|}~)." />
                </label>
                <input
                  type={inputType}
                  className="form_control"
                  value={password}
                  onChange={(e) => setPassword(e.target.value.trim())}
                  disabled={
                    navigationResponse != null || adminResponse ? true : false
                  }
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
                  disabled={
                    navigationResponse != null || adminResponse ? true : false
                  }
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

              {/* <div className="form_control_spread a_checkbox">
                <input
                  type="checkbox"
                  className="vh"
                  id="terms-condition"
                  onClick={() => setShowTermsAndConditions(true)}
                  // onChange={(e) => setTermsAndConditions(e.target.checked)}
                  disabled={navigationResponse != null ? true : false}
                  checked={navigationResponse != null ? "checked" : null}
                />
                &nbsp;
                <label className="terms_label" htmlFor="terms-condition">
                  I agree with the
                </label>
                <span
                  className="terms_highlight"
                
                >
                  Terms &amp; Conditions.*
                </span>
                {
                  errors && termAndConditions===false && (
                    <span className="error">
                      Plzz confirm Terms &amp; Conditions
                    </span>
                  )
                }
              </div> */}

              <div className="form_submit_container">
                {(companyHeadStatus && navigationCheck === false) ||
                (registrationStatusKey - 2 > 0 &&
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
      </div>
    </>
  );
};

export default CompanyHead;
