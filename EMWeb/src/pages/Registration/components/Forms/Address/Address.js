/* eslint-disable no-unused-expressions */
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import Select from "react-select";
import "./Address.scss";
import { saveAddressActions } from "./../../../../../store/vendorRegistration/address";
import ConfirmationModal from "./../../Modals/ConfirmationModal/ConfirmationModal";
import IndividualType from "../../Modals/IndividualType.js/individualType";
import Loading from "./../../../../../components/Loading/Loading";
import Tooltip from "./../../../../../components/Tooltip/Tooltip";
import { ToastError } from "./../../../../../components/Tostify";
import {
  validateEmailPhone,
  validateEmailPhoneActions,
} from "../../../../../store/vendorRegistration/validateEmailPhone";
import { getAuthData, getAuthToken, getCompanyId } from "../../../../../config";
import {
  emailValidation,
  numberValidation,
} from "./../../../../../utils/helpers";
import cancelIcon from "./../../../../../assets/images/cancel_other_icon.png";
import country, { getCountries } from "./../../../../../store/common/country";
import { getStates } from "./../../../../../store/common/state";
import {
  getAllCityByCountryId,
  getAllCityByStateId,
} from "./../../../../../store/common/city";
import { saveAddress } from "./../../../../../store/vendorRegistration/address";
import {
  commonGetAPiCall,
  commonGetApiActions,
} from "../../../../../store/common/commonGetApi";
import { saveReferralCode } from "../../../../../store/vendorRegistration/address/referralCode";
import { getAvailableOn } from "./../../../../../store/common/availableOn";
import eyeIconOn from "./../../../../../assets/images/Icon-ionic-md-eye.png";
import eyeIconOff from "./../../../../../assets/images/Icon-md-eye-off.png";
import { passwordValidation } from "../../../../../utils/helpers";
import TermsAndConditions from "../../../../Login/components/Modals/TermsAndConditions/TermsAndConditions";

const Address = (props) => {
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

  const dispatch = useDispatch();
  const countries = useSelector((state) => state.countries.countries);
  const isCountryLoading = useSelector((state) => state.countries.isLoading);
  const isCountryError = useSelector((state) => state.countries.isError);
  const states = useSelector((state) => state.states.states);
  const isStateLoading = useSelector((state) => state.states.isLoading);
  const isStateError = useSelector((state) => state.states.isError);
  const cities = useSelector((state) => state.cities.cities);
  const iscityLoading = useSelector((state) => state.cities.isLoading);
  const addressResponse = useSelector(
    (state) => state.addressReducer.addressRes
  );
  const navigationCheck = useSelector(
    (state) => state.addressReducer.navigationCheck
  );
  const isAddressResponseLoading = useSelector(
    (state) => state.addressReducer.isLoading
  );
  const isAddressResponseError = useSelector(
    (state) => state.addressReducer.isError
  );
  const navigationResponse = useSelector(
    (state) => state.commonGetApiReducer.getApiRes
  );
  const isReferralCodeLoading = useSelector(
    (state) => state.referralCodeSlice.isLoading
  );
  const isReferralCodeError = useSelector(
    (state) => state.referralCodeSlice.isError
  );
  const authResponse = useSelector((state) => state.auth.authRes);
  const registrationStatusKey = useSelector(
    (state) => state.commonGetApiReducer.registrationStatusKey
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

  const [selectedCountry, setSelectedCountry] = useState("");
  const [selectedState, setSelectedState] = useState(null);
  const [selectedCity, setSelectedCity] = useState("");
  const [selectedPhonePrefix, setSelectedPhonePrefix] = useState("");
  const [companyName, setCompanyName] = useState("");
  const [companyEmail, setCompanyEmail] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [addressLine1, setAddressLine1] = useState("");
  const [addressLine2, setAddressLine2] = useState("");
  const [postalCode, setPostalCode] = useState("");
  const [referralCode, setReferralCode] = useState("");
  const [errors, setErrors] = useState(false);
  const [isConfirmationModal, setIsConfirmationModal] = useState(false);
  const [hide, setHide] = useState();
  const [hideContact, setHideContact] = useState();
  const [countryName, setCountryName] = useState();
  const [selectStateByName, setSelectStateByName] = useState();
  const [removeState, setRemoveState] = useState(false);
  const [forIndividual, setForIndividual] = useState(
    isFormTypePartnersIndividual
  );
  const [individualHaveCompany, setIndividualHaveCompany] = useState(
    isFormTypePartnersIndividual
  );
  const [individualHaveNotCompany, setIndividualHaveNotCompany] =
    useState(false);
  const [onWhatsapp, setOnWhatsapp] = useState("No");
  const [availableOn, setAvailableOn] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [termConditionConfirmationModal, setTermConditionConfirmationModal] =
    useState(false);

  useEffect(() => {
    dispatch(getCountries());
  }, [dispatch]);

  useEffect(() => {
    if (isCountryError !== "") {
      ToastError(isCountryError);
    } else if (isStateError !== "") {
      ToastError(isStateError);
    } else if (isAddressResponseError !== "") {
      ToastError(isAddressResponseError);
    }
  }, [isCountryError, isStateError, isAddressResponseError]);

  const getCompanyIdCheck = () => {
    if (getAuthData()?.companyId) {
      return getAuthData()?.companyId;
    }
    return getCompanyId();
  };
  useEffect(() => {
    dispatch(getAvailableOn());
  }, [dispatch]);

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
  const whatsappAndAvailableOnHandler = (e) => {
    setOnWhatsapp(e.target.value);
    setAvailableOn("");
  };

  useEffect(() => {
    if (navigationCheck) {
      if (addressResponse !== null && isFormTypePartner && navigationCheck) {
        props.setFormLabel("details", 1);
      } else if (
        addressResponse !== null &&
        isFormTypeCustomer &&
        navigationCheck
      ) {
        props.setFormLabel("details", 1);
      } else if (
        addressResponse !== null &&
        isFormTypePartnersIndividual &&
        navigationCheck &&
        individualHaveCompany
      ) {
        props.setFormLabel("details", 1);
      } else if (
        addressResponse !== null &&
        isFormTypePartnersIndividual &&
        navigationCheck &&
        individualHaveNotCompany
      ) {
        props.setFormLabel("verifyEmail", 1);
      }
    }
  }, [
    addressResponse,
    navigationCheck,
    props,
    isFormTypePartner,
    isFormTypeCustomer,
    individualHaveCompany,
    individualHaveNotCompany,
    isFormTypePartnersIndividual,
  ]);

  // get api call after login
  useEffect(() => {
    if (
      registrationStatusKey - 0 > 0 &&
      (isFormTypePartner || isFormTypePartnersIndividual)
    ) {
      dispatch(commonGetAPiCall(`/vendor/get/${getCompanyIdCheck()}`));
      setForIndividual(false);
    } else if (registrationStatusKey - 0 > 0 && isFormTypeCustomer) {
      dispatch(commonGetAPiCall(`/customer/get/${getCompanyIdCheck()}`));
    }
  }, [
    registrationStatusKey,
    isFormTypePartner,
    isFormTypeCustomer,
    isFormTypePartnersIndividual,
  ]);

  // for get api calls
  useEffect(() => {
    if (
      !navigationCheck &&
      addressResponse !== null &&
      (isFormTypePartner || isFormTypePartnersIndividual)
    ) {
      dispatch(commonGetAPiCall(`/vendor/get/${getCompanyId()}`));
    } else if (
      !navigationCheck &&
      addressResponse !== null &&
      isFormTypeCustomer
    ) {
      dispatch(commonGetAPiCall(`/customer/get/${getCompanyId()}`));
      setForIndividual(false);
    }
  }, [
    dispatch,
    navigationCheck,
    addressResponse,
    isFormTypePartner,
    isFormTypeCustomer,
    isFormTypePartnersIndividual,
  ]);

  useEffect(() => {
    if (navigationResponse !== null) {
      dispatch(saveAddressActions.changeNavigationCheckToFalse());
    }
  }, [navigationResponse]);

  useEffect(() => {
    if (navigationResponse !== null && isFormTypePartnersIndividual) {
      setForIndividual(false);
      setIndividualHaveCompany(
        navigationResponse?.userModel?.individualNo === null ? true : false
      );
      setIndividualHaveNotCompany(
        navigationResponse?.userModel?.individualNo === "Yes" ? true : false
      );
    }
  }, [navigationResponse, isFormTypePartnersIndividual]);

  // get api response setstate
  useEffect(() => {
    if (
      navigationResponse !== null &&
      (isFormTypePartner || isFormTypePartnersIndividual)
    ) {
      localStorage.setItem(
        "addressInfo",
        JSON.stringify(navigationResponse.vendorCompanysModel)
      );
      setCompanyName(navigationResponse?.userModel?.name);
      setCompanyEmail(navigationResponse?.userModel?.email);
      setPhoneNumber(navigationResponse?.userModel?.primaryPhone);
      setSelectedPhonePrefix(navigationResponse?.userModel?.phoneCode);
      setSelectedCountry(
        navigationResponse?.vendorCompanysModel?.masterCountryModel?.id
      );
      setSelectedState(
        navigationResponse?.vendorCompanysModel?.mstateStateModel
      );
      setSelectedCity(navigationResponse?.vendorCompanysModel?.masterCityModel);
      setAddressLine1(
        navigationResponse?.vendorCompanysModel?.address.split("\n")[0].trim()
      );
      setAddressLine2(
        navigationResponse?.vendorCompanysModel?.address.split("\n")[1]
      );
      setPostalCode(navigationResponse?.vendorCompanysModel?.pincode);
      setReferralCode(
        navigationResponse?.vendorCompanysModel?.referralCodeUsed
      );
      setOnWhatsapp(
        navigationResponse?.userModel?.onWhatsapp === true ? "Yes" : "No"
      );
      setAvailableOn(navigationResponse?.userModel?.availableOn);
      setPassword("**********");
      setConfirmPassword("**********");
    } else if (navigationResponse !== null && isFormTypeCustomer) {
      localStorage.setItem(
        "addressInfo",
        JSON.stringify(navigationResponse.customerCompanyModel)
      );
      setCompanyName(navigationResponse?.userModel?.name);
      setCompanyEmail(navigationResponse?.userModel?.email);
      setPhoneNumber(navigationResponse?.userModel?.primaryPhone);
      setSelectedPhonePrefix(navigationResponse?.userModel?.phoneCode);
      setSelectedCountry(
        navigationResponse?.customerCompanyModel?.masterCountryModel?.id
      );
      setSelectedState(
        navigationResponse?.customerCompanyModel?.mstateStateModel
      );
      setSelectedCity(navigationResponse?.customerCompanyModel?.masterCity);
      setAddressLine1(
        navigationResponse?.customerCompanyModel?.address.split("\n")[0].trim()
      );
      setAddressLine2(
        navigationResponse?.customerCompanyModel?.address.split("\n")[1].trim()
      );
      setPostalCode(navigationResponse?.customerCompanyModel?.pincode);
      setReferralCode(navigationResponse?.vendorCompanyModel?.referralCodeUsed);
    }
  }, [
    navigationResponse,
    isFormTypePartner,
    isFormTypeCustomer,
    isFormTypePartnersIndividual,
  ]);

  const validateForm = () => {
    if (!individualHaveNotCompany) {
      if (
        companyName.trim() &&
        companyEmail &&
        phoneNumber &&
        phoneNumber.length > 5 &&
        phoneNumber.length < 14 &&
        addressLine1.trim() &&
        selectedPhonePrefix &&
        selectedCountry &&
        selectedCity &&
        (navigationResponse == null &&
          isReferralCodeError !== "" &&
          referralCode.length !== 0) === false &&
        emailValidation(companyEmail.trim())
      ) {
        setErrors(false);
        return true;
      } else {
        setErrors(true);
        return false;
      }
    } else {
      if (
        companyName.trim() &&
        companyEmail &&
        phoneNumber &&
        phoneNumber.length > 5 &&
        phoneNumber.length < 14 &&
        addressLine1.trim() &&
        selectedPhonePrefix &&
        selectedCountry &&
        selectedCity &&
        (navigationResponse == null &&
          isReferralCodeError !== "" &&
          referralCode.length !== 0) === false &&
        emailValidation(companyEmail.trim()) &&
        password &&
        confirmPassword &&
        ((onWhatsapp === "No" && availableOn !== "") ||
        (onWhatsapp === "Yes" && availableOn === "")
          ? true
          : false) &&
        emailValidation(companyEmail.trim()) &&
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
    }
  };

  const handleFormSubmission = (event) => {
    event.preventDefault();
    if (validateForm()) {
      setIsConfirmationModal(!isConfirmationModal);
    }
  };

  const handleFormSubmissionIdividual = (event) => {
    event.preventDefault();
    if (validateForm()) {
      setTermConditionConfirmationModal(!termConditionConfirmationModal);
    }
  };

  const termsAndConditionsCancel = () => {
    setTermConditionConfirmationModal(!termConditionConfirmationModal);
  };

  const termsAndConditionsConfirm = () => {
    setTermConditionConfirmationModal(!termConditionConfirmationModal);
    localStorage.clear();
    apiCall();
  };

  const onChangeCountryHandler = (e) => {
    setSelectedState("");
    setSelectedCity("");
    setPostalCode("");
    setSelectStateByName("");
    setRemoveState(false);
    setSelectedCountry(e.value[0]);
    setCountryName(e.value[1]);
    dispatch(getStates(e.value[0]));
    dispatch(getAllCityByCountryId(e.value[0]));
  };

  const onChangeStateHandler = (e) => {
    setSelectedState(e.value[0]);
    setSelectStateByName(e.value[1]);
    setSelectedCity("");
    setRemoveState(true);
  };

  const removeSelectState = () => {
    setRemoveState(false);
    setSelectedState("");
    setSelectedCity("");
    setPostalCode("");
    setSelectStateByName("");
  };

  const haveCompany = () => {
    setIndividualHaveCompany(true);
    setIndividualHaveNotCompany(false);
    setForIndividual(!forIndividual);
  };
  const haveNotCompany = () => {
    setIndividualHaveNotCompany(true);
    setIndividualHaveCompany(false);
    setForIndividual(!forIndividual);
  };

  const onChangeCityHandler = (e) => {
    setSelectedCity(e.target.value.trim());
    setPostalCode("");
  };

  const onCancelHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
  };

  const onOkHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
    localStorage.clear();
    apiCall();
  };

  const apiCall = () => {
    if ((isFormTypePartner || isFormTypePartnersIndividual) && validateForm()) {
      const userModel = {
        name: companyName,
        email: companyEmail,
        phoneCode: selectedPhonePrefix,
        primaryPhone: phoneNumber,
        roleModel: [
          {
            id: 1,
          },
        ],
      };

      if (isFormTypePartnersIndividual) {
        userModel["type"] = "Individual";
      }

      if (isFormTypePartnersIndividual && individualHaveNotCompany) {
        userModel["type"] = "Individual";
        userModel["password"] = password;
        userModel["onWhatsapp"] = onWhatsapp === "Yes" ? true : false;
        userModel["availableOn"] = onWhatsapp === "Yes" ? "" : availableOn;
        userModel["individualNo"] = "Yes";
      }

      const vendorCompanyModel = {
        address: `${addressLine1} \n ${addressLine2}`,
        pincode: postalCode,
        masterCountryModel: {
          id: selectedCountry,
        },
        mstateStateModel: selectedState !== "" ? { id: selectedState } : null,
        // masterCityModel: {
        //   id: selectedCity,
        // },
        masterCity: selectedCity,
        referralCodeUsed: isReferralCodeError !== "" ? "" : referralCode,
      };
      const addressObj = {
        userModel,
        vendorCompanyModel,
      };
      dispatch(saveAddress(addressObj, "vendor/add"));
    } else if (isFormTypeCustomer && validateForm()) {
      const customerOBJ = {
        userModel: {
          name: companyName,
          email: companyEmail,
          primaryPhone: phoneNumber,
          phoneCode: selectedPhonePrefix,
          type: "Customer",
          roleModel: [
            {
              id: 1,
            },
          ],
        },
        customerCompanyModel: {
          address: `${addressLine1} \n ${addressLine2}`,
          pincode: postalCode,
          masterCountryModel: {
            id: selectedCountry,
          },
          mstateStateModel: selectedState !== "" ? { id: selectedState } : null,
          // masterCityModel: {
          //   id: selectedCity,
          // },
          masterCity: selectedCity,
          referralCodeUsed: referralCode,
        },
      };
      dispatch(saveAddress(customerOBJ, "customer/add"));
    }
  };

  const nextNavigationHandler = () => {
    dispatch(saveAddressActions.addressNavigation(true));
    dispatch(commonGetApiActions.dispatchGetRequest());
    props.navigationHandler({
      currentForm: "details",
      goForward: true,
    });
  };
  const nextNavigationHandlerHavenotCompany = () => {
    // dispatch(saveAddressActions.addressNavigation(true));
    // dispatch(commonGetApiActions.dispatchGetRequest());
    // props.navigationHandler({
    //   currentForm: "engineer",
    //   goForward: true,
    // });
    // props.setFormLabel("engineer", 2);

    if (navigationResponse?.userModel?.phoneVerified) {
      dispatch(saveAddressActions.addressNavigation(true));
      dispatch(commonGetApiActions.dispatchGetRequest());
      props.navigationHandler({
        currentForm: "engineer",
        goForward: true,
      });
      props.setFormLabel("engineer", 2);
    } else {
      dispatch(saveAddressActions.addressNavigation(true));
      dispatch(commonGetApiActions.dispatchGetRequest());
      props.navigationHandler({
        currentForm: "details",
        goForward: true,
      });
    }
  };

  const callApiReferralCode = (e) => {
    if (e.target.value.length > 3) {
      dispatch(saveReferralCode(e.target.value));
    }
  };

  // const downloadImage = () => {
  //   var link = document.createElement("a");
  //   link.href = window.URL.createObjectURL(
  //     new Blob([`http://www.africau.edu/images/default/sample.pdf`], { type: "application/octet-stream" })
  //   );
  //   link.download = "name_of_file_with_extension";

  //   document.body.appendChild(link);

  //   link.click();
  //   setTimeout(function () {
  //     window.URL.revokeObjectURL(link);
  //   }, 200);
  // }

  // useEffect(() => {
  //   // const favicon = document.getElementById("favicon");
  //   const favicon = document.querySelector("link");
  //   favicon.href = "https://www.google.com/favicon.ico";
  // }, [])

  const options = countries?.map((country) => ({
    value: country.phoneCode,
    label: [
      <img
        src={country.flag}
        alt="flag"
        style={{ width: "20px", marginRight: "15px", marginTop: "8px" }}
      />,
      <span style={{ marginRight: "12px", fontFamily: "Poppins-Regular" }}>
        {country.phoneCode}
      </span>,
      <span style={{ fontFamily: "Poppins-Regular" }}>{country.name}</span>,
    ],
  }));

  const countryOptions = countries?.map((country) => ({
    value: [country.id, country.name],
    label: [
      <img
        src={country.flag}
        alt="flag"
        style={{ width: "20px", marginRight: "15px" }}
      />,
      <span style={{ fontFamily: "Poppins-Regular" }}>{country.name}</span>,
    ],
  }));

  const stateOptions = states?.map((country) => ({
    value: [country.id, country.name],
    label: country.name,
  }));

  const socialMedia = availableOnRes?.map((availableAt) => ({
    value: availableAt.app_Name,
    label: availableAt.app_Name,
  }));

  const customStyles = {
    container: (base, state) => ({
      ...base,
    }),
    menu: (styles) => ({
      ...styles,
      width: "320px",
      zIndex: "999",
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
  const customStyle = {
    container: (base, state) => ({
      ...base,
    }),
    menu: (styles) => ({
      ...styles,
      width: "100%",
    }),
  };

  const handleLettersOnlyInput = (e) => {
    if (Number.isInteger(Number(e.key))) {
      e.preventDefault();
    }
  };

  // const handleNumberOnlyInput = (e) => {
  //   if (!Number.isInteger(Number(e.key))) {
  //     e.preventDefault();
  //   }
  // };
  return (
    <div className="form_container_area">
      <div className="address_form positioning_container">
        {(isCountryLoading ||
          isStateLoading ||
          iscityLoading ||
          isReferralCodeLoading ||
          isAddressResponseLoading) && <Loading text={"Loading...."} />}
        {isConfirmationModal && (
          <ConfirmationModal cancel={onCancelHandler} ok={onOkHandler} />
        )}
        {forIndividual && (
          <IndividualType
            haveCompany={haveCompany}
            haveNotCompany={haveNotCompany}
          />
        )}
        {termConditionConfirmationModal && (
          <TermsAndConditions
            cancel={termsAndConditionsCancel}
            ok={termsAndConditionsConfirm}
          />
        )}

        {/* <button onClick={() => console.log(selectedState)}>Get State</button> */}

        {/* ---------------- Partner form ---------------- */}
        {isFormTypePartner && (
          <>
            <div className="nav_container">
              <div className="nav_items">
                <div className="nav_item active">Address</div>
                <div className="horizotal_row_container">
                  <span className="horizontal_row">&nbsp;</span>
                </div>
                <div
                  className="nav_item"
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
                  <span className="horizontal_row">&nbsp;</span>
                </div>
                <div
                  className="nav_item"
                  onClick={() =>
                    props.navigationHandler({
                      currentForm: "companyHead",
                      goForward: true,
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

            <form onSubmit={handleFormSubmission}>
              <div className="form_container">
                <div className="form_control_area">
                  <label className="form_label">
                    Company Name <span className="label_mandatory">*</span>
                    <Tooltip text="Full name of the company as registered." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={companyName}
                    autocomplete="nope"
                    onChange={(e) => {
                      if (!Number(e.nativeEvent.data))
                        setCompanyName(e.target.value);
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors && companyName.trim() === "" && (
                    <span className="error">Full name, as registered.</span>
                  )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Company Email <span className="label_mandatory">*</span>
                    <Tooltip text="Please provide company's email id." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={companyEmail}
                    autocomplete="nope"
                    onChange={(e) => setCompanyEmail(e.target.value.trim())}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors &&
                    companyEmail.trim() === "" &&
                    !emailValidation(companyEmail.trim()) && (
                      <span className="error">Company Email ID</span>
                    )}
                  {errors &&
                    companyEmail.trim() !== "" &&
                    !emailValidation(companyEmail.trim()) && (
                      <span className="error">
                        Invalid Email ID, please check and re-enter
                      </span>
                    )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Contact No. <span className="label_mandatory">*</span>
                    <Tooltip text="Please provide company's contact number." />
                  </label>
                  <div className="input_container">
                    {navigationResponse != null ? (
                      <select
                        className="select_input_disabled"
                        //  onChange={(e) => setSelectedPhonePrefix(e.target.value)}
                        disabled={navigationResponse != null ? true : false}
                      >
                        {/* <option value="" selected disabled></option> */}

                        <option
                          selected={
                            country?.phoneCode === selectedPhonePrefix
                              ? true
                              : false
                          }
                          value={country.phoneCode}
                          key={country.id}
                        >
                          {selectedPhonePrefix}
                        </option>
                      </select>
                    ) : (
                      <>
                        <div
                          style={{
                            display: hideContact && "none",
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
                          onInputChange={setHideContact}
                          disabled={navigationResponse != null ? true : false}
                          placeholder="Search code"
                          className="select_input"
                          options={options}
                          // onKeyDown={handleNumberOnlyInput}
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
                      value={phoneNumber}
                      autocomplete="nope"
                      onChange={(e) => {
                        if (e.target.value.length < 14)
                          setPhoneNumber(e.target.value.trim());
                      }}
                      disabled={navigationResponse != null ? true : false}
                    />
                  </div>
                  {errors &&
                    (!numberValidation(phoneNumber) ||
                      phoneNumber === "" ||
                      selectedPhonePrefix === "") && (
                      <span className="error">
                        Select at least one code / Contact number should be
                        between 6 to 13 digit
                      </span>
                    )}
                </div>

                <div className="horizontal_line">&nbsp;</div>

                <div className="form_control_area">
                  <label className="form_label">
                    Address Line 1 <span className="label_mandatory">*</span>
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={addressLine1}
                    autocomplete="nope"
                    onChange={(e) => setAddressLine1(e.target.value)}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors && addressLine1.trim() === "" && (
                    <span className="error">Complete address</span>
                  )}
                </div>

                <div className="form_control_area address_margin">
                  <label className="form_label"> Address Line 2 </label>
                  <input
                    type="text"
                    className="form_control"
                    value={addressLine2}
                    onChange={(e) => setAddressLine2(e.target.value)}
                    disabled={navigationResponse != null ? true : false}
                  />
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Country <span className="label_mandatory">*</span>
                  </label>
                  {navigationResponse != null ? (
                    <select
                      className="form_control"
                      onChange={onChangeCountryHandler}
                      disabled={navigationResponse != null ? true : false}
                    >
                      <option
                        selected
                        value={
                          navigationResponse?.vendorCompanysModel
                            ?.masterCountryModel?.id
                        }
                        key={
                          navigationResponse?.vendorCompanysModel
                            ?.masterCountryModel?.id
                        }
                      >
                        {
                          navigationResponse?.vendorCompanysModel
                            ?.masterCountryModel?.name
                        }
                      </option>
                      )
                    </select>
                  ) : (
                    <>
                      <div
                        style={{
                          display: hide && "none",
                          position: "absolute",
                          marginLeft: "12px",
                          lineHeight: "2.5",
                          zIndex: 1,
                          wordWrap: "normal",
                          fontSize: "16px",
                          fontFamily: "Poppins-Regular",
                        }}
                      >
                        {countryName}
                      </div>
                      <Select
                        onInputChange={setHide}
                        className="form_control_country"
                        onChange={onChangeCountryHandler}
                        disabled={navigationResponse != null ? true : false}
                        options={countryOptions}
                        styles={customStyle}
                        onKeyDown={handleLettersOnlyInput}
                        components={{
                          SingleValue: () => {
                            return null;
                          },
                        }}
                      ></Select>
                    </>
                  )}
                  {errors && selectedCountry === "" && (
                    <span className="error">Select Country</span>
                  )}
                </div>

                <div className="form_control_area address_margin">
                  <label className="form_label"> State </label>
                  {navigationResponse != null ? (
                    <select
                      className="form_control"
                      onChange={onChangeStateHandler}
                      disabled={navigationResponse != null ? true : false}
                    >
                      <option
                        selected
                        value={
                          navigationResponse?.vendorCompanysModel
                            ?.mstateStateModel?.id
                        }
                        key={
                          navigationResponse?.vendorCompanysModel
                            ?.mstateStateModel?.id
                        }
                      >
                        {
                          navigationResponse?.vendorCompanysModel
                            ?.mstateStateModel?.name
                        }
                      </option>
                      )
                    </select>
                  ) : (
                    <>
                      <Select
                        className="form_control_country"
                        onChange={onChangeStateHandler}
                        disabled={navigationResponse != null ? true : false}
                        options={stateOptions}
                        onKeyDown={handleLettersOnlyInput}
                        value={{
                          value: selectStateByName,
                          label: selectStateByName,
                        }}
                        placeholder={""}
                      />
                      {removeState && (
                        <img
                          src={cancelIcon}
                          alt="cancel icon"
                          className="remove_data_state"
                          onClick={removeSelectState}
                        />
                      )}
                    </>
                  )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    City <span className="label_mandatory">*</span>
                    <Tooltip text="Enter your city here." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={selectedCity}
                    autocomplete="nope"
                    onChange={(e) => {
                      onChangeCityHandler(e);
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors && selectedCity === "" && (
                    <span className="error">Enter City</span>
                  )}
                </div>

                <div className="form_control_area address_margin">
                  <label className="form_label">Postal Code</label>
                  <input
                    type="number"
                    className="form_control"
                    value={postalCode}
                    autocomplete="nope"
                    onChange={(e) => {
                      if (e.target.value.length < 17)
                        setPostalCode(e.target.value.trim());
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {/* {errors &&
                    (postalCode === "" ||
                      postalCode.length < 4 ||
                      postalCode.length > 16) && (
                      <span className="error">Enter Postal code</span>
                    )} */}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Referral Code
                    <Tooltip text="If you have been referred by registered partner, mention their company id." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={referralCode}
                    style={{ textTransform: "uppercase" }}
                    autocomplete="nope"
                    // onBlur={callApiReferralCode}
                    onChange={(e) => {
                      setReferralCode(e.target.value.trim());
                      callApiReferralCode(e);
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {navigationResponse == null &&
                    isReferralCodeError !== "" &&
                    referralCode.length !== 0 && (
                      <span className="error">Invalid Referral Code</span>
                    )}
                </div>

                <div className="form_submit_container">
                  {(addressResponse && navigationCheck === false) ||
                  (registrationStatusKey - 0 > 0 &&
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
          </>
        )}

        {/* ---------------- Customer form ---------------- */}
        {isFormTypeCustomer && (
          <>
            <div className="nav_container">
              <div className="nav_items">
                <div className="nav_item active">Address</div>
                <div className="horizotal_row_container">
                  <span className="horizontal_row">&nbsp;</span>
                </div>
                <div
                  className="nav_item"
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
                    Company Name <span className="label_mandatory">*</span>
                    <Tooltip text="Full name of the company as registered." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={companyName}
                    autocomplete="nope"
                    onChange={(e) => {
                      if (!Number(e.nativeEvent.data))
                        setCompanyName(e.target.value);
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors && companyName === "" && (
                    <span className="error">Full name, as registered.</span>
                  )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Company Email <span className="label_mandatory">*</span>
                    <Tooltip text="Please provide company's email id." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={companyEmail}
                    autocomplete="nope"
                    onChange={(e) => setCompanyEmail(e.target.value.trim())}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors &&
                    (companyEmail === "" || !emailValidation(companyEmail)) && (
                      <span className="error">Company Email ID</span>
                    )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Contact No. <span className="label_mandatory">*</span>
                    <Tooltip text="Please provide company's contact number." />
                  </label>
                  <div className="input_container">
                    {navigationResponse != null ? (
                      <select
                        className="select_input_disabled"
                        //  onChange={(e) => setSelectedPhonePrefix(e.target.value)}
                        disabled={navigationResponse != null ? true : false}
                      >
                        {/* <option value="" selected disabled></option> */}

                        <option
                          selected={
                            country?.phoneCode === selectedPhonePrefix
                              ? true
                              : false
                          }
                          value={country.phoneCode}
                          key={country.id}
                        >
                          {navigationResponse?.userModel?.phoneCode}
                        </option>
                      </select>
                    ) : (
                      <>
                        <div
                          style={{
                            display: hideContact && "none",
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
                          onInputChange={setHideContact}
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
                      value={phoneNumber}
                      autocomplete="nope"
                      onChange={(e) => {
                        if (e.target.value.length < 14)
                          setPhoneNumber(e.target.value.trim());
                      }}
                      disabled={navigationResponse != null ? true : false}
                    />
                  </div>
                  {errors &&
                    (!numberValidation(phoneNumber) ||
                      phoneNumber === "" ||
                      selectedPhonePrefix === "") && (
                      <span className="error">
                        Select at least one code / Contact number should be
                        between 6 to 13 digit
                      </span>
                    )}
                </div>

                <div className="horizontal_line">&nbsp;</div>

                <div className="form_control_area">
                  <label className="form_label">
                    Address Line 1 <span className="label_mandatory">*</span>
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={addressLine1}
                    autocomplete="nope"
                    onChange={(e) => setAddressLine1(e.target.value)}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors && addressLine1.trim() === "" && (
                    <span className="error">Complete address</span>
                  )}
                </div>

                <div className="form_control_area address_margin">
                  <label className="form_label"> Address Line 2 </label>
                  <input
                    type="text"
                    className="form_control"
                    value={addressLine2}
                    autocomplete="nope"
                    onChange={(e) => setAddressLine2(e.target.value)}
                    disabled={navigationResponse != null ? true : false}
                  />
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Country <span className="label_mandatory">*</span>
                  </label>
                  {navigationResponse != null ? (
                    <select
                      className="form_control"
                      onChange={onChangeCountryHandler}
                      disabled={navigationResponse != null ? true : false}
                    >
                      <option
                        selected
                        value={
                          navigationResponse?.customerCompanyModel
                            ?.masterCountryModel?.id
                        }
                        key={
                          navigationResponse?.customerCompanyModel
                            ?.masterCountryModel?.id
                        }
                      >
                        {
                          navigationResponse?.customerCompanyModel
                            ?.masterCountryModel?.name
                        }
                      </option>
                      )
                    </select>
                  ) : (
                    <>
                      <div
                        style={{
                          display: hide && "none",
                          fontFamily: "Poppins-Regular",
                          position: "absolute",
                          marginLeft: "12px",
                          lineHeight: "2.5",
                          zIndex: 1,
                          fontSize: "16px",
                          fontFamily: "Poppins-Regular",
                        }}
                      >
                        {countryName}
                      </div>
                      <Select
                        onInputChange={setHide}
                        className="form_control_country"
                        onChange={onChangeCountryHandler}
                        disabled={navigationResponse != null ? true : false}
                        options={countryOptions}
                        onKeyDown={handleLettersOnlyInput}
                        styles={customStyle}
                        components={{
                          SingleValue: () => {
                            return null;
                          },
                        }}
                      ></Select>
                    </>
                  )}
                  {errors && selectedCountry === "" && (
                    <span className="error">Select Country</span>
                  )}
                </div>

                <div className="form_control_area address_margin">
                  <label className="form_label"> State </label>
                  {navigationResponse != null ? (
                    <select
                      className="form_control"
                      onChange={onChangeStateHandler}
                      disabled={navigationResponse != null ? true : false}
                    >
                      <option
                        selected
                        value={
                          navigationResponse?.vendorCompanysModel
                            ?.mstateStateModel?.id
                        }
                        key={
                          navigationResponse?.vendorCompanysModel
                            ?.mstateStateModel?.id
                        }
                      >
                        {
                          navigationResponse?.vendorCompanysModel
                            ?.mstateStateModel?.name
                        }
                      </option>
                      )
                    </select>
                  ) : (
                    <>
                      <Select
                        className="form_control_country"
                        onChange={onChangeStateHandler}
                        onKeyDown={handleLettersOnlyInput}
                        disabled={navigationResponse != null ? true : false}
                        options={stateOptions}
                        value={{
                          value: selectStateByName,
                          label: selectStateByName,
                        }}
                        placeholder={""}
                      />
                      {removeState && (
                        <img
                          src={cancelIcon}
                          alt="cancel icon"
                          className="remove_data_state"
                          onClick={removeSelectState}
                        />
                      )}
                    </>
                  )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    City <span className="label_mandatory">*</span>
                    <Tooltip text="Enter your city here." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={selectedCity}
                    autocomplete="nope"
                    onChange={(e) => {
                      onChangeCityHandler(e);
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors && selectedCity === "" && (
                    <span className="error">Enter City</span>
                  )}
                </div>

                <div className="form_control_area address_margin">
                  <label className="form_label">Postal Code</label>
                  <input
                    type="number"
                    className="form_control"
                    value={postalCode}
                    autocomplete="nope"
                    onChange={(e) => {
                      if (e.target.value.length < 17)
                        setPostalCode(e.target.value.trim());
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {/* {errors &&
                    (postalCode === "" ||
                      postalCode.length < 4 ||
                      postalCode.length > 16) && (
                      <span className="error">Enter Postal code</span>
                    )} */}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Referral Code
                    <Tooltip text="If you have been referred by registered partner, mention their company id." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={referralCode}
                    style={{ textTransform: "uppercase" }}
                    autocomplete="nope"
                    // onBlur={callApiReferralCode}
                    onChange={(e) => {
                      setReferralCode(e.target.value.trim());
                      callApiReferralCode(e);
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {navigationResponse == null &&
                    isReferralCodeError !== "" &&
                    referralCode.length !== 0 && (
                      <span className="error">Invalid Referral Code</span>
                    )}
                </div>

                <div className="form_submit_container">
                  {(addressResponse && navigationCheck === false) ||
                  (registrationStatusKey - 0 > 0 &&
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
          </>
        )}

        {/* ---------------- Partner's Individual have company form ---------------- */}
        {individualHaveCompany && (
          <>
            <div className="nav_container">
              <div className="nav_items">
                <div className="nav_item active">Address</div>
                <div className="horizotal_row_container">
                  <span className="horizontal_row">&nbsp;</span>
                </div>
                <div
                  className="nav_item"
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
                    Company Name <span className="label_mandatory">*</span>
                    <Tooltip text="Full name of the company as registered." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={companyName}
                    autocomplete="nope"
                    onChange={(e) => {
                      if (!Number(e.nativeEvent.data))
                        setCompanyName(e.target.value);
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors && companyName.trim() === "" && (
                    <span className="error">Full name, as registered.</span>
                  )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Company Email <span className="label_mandatory">*</span>
                    <Tooltip text="Please provide company's email id." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={companyEmail}
                    autocomplete="nope"
                    onChange={(e) => setCompanyEmail(e.target.value.trim())}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors &&
                    companyEmail.trim() === "" &&
                    !emailValidation(companyEmail.trim()) && (
                      <span className="error">Company Email ID</span>
                    )}
                  {errors &&
                    companyEmail.trim() !== "" &&
                    !emailValidation(companyEmail.trim()) && (
                      <span className="error">
                        Invalid Email ID, please check and re-enter
                      </span>
                    )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Contact No. <span className="label_mandatory">*</span>
                    <Tooltip text="Please provide company's contact number." />
                  </label>
                  <div className="input_container">
                    {navigationResponse != null ? (
                      <select
                        className="select_input_disabled"
                        //  onChange={(e) => setSelectedPhonePrefix(e.target.value)}
                        disabled={navigationResponse != null ? true : false}
                      >
                        {/* <option value="" selected disabled></option> */}

                        <option
                          selected={
                            country?.phoneCode === selectedPhonePrefix
                              ? true
                              : false
                          }
                          value={country.phoneCode}
                          key={country.id}
                        >
                          {selectedPhonePrefix}
                        </option>
                      </select>
                    ) : (
                      <>
                        <div
                          style={{
                            display: hideContact && "none",
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
                          onInputChange={setHideContact}
                          disabled={navigationResponse != null ? true : false}
                          placeholder="Search code"
                          className="select_input"
                          options={options}
                          // onKeyDown={handleNumberOnlyInput}
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
                      value={phoneNumber}
                      autocomplete="nope"
                      onChange={(e) => {
                        if (e.target.value.length < 14)
                          setPhoneNumber(e.target.value.trim());
                      }}
                      disabled={navigationResponse != null ? true : false}
                    />
                  </div>
                  {errors &&
                    (!numberValidation(phoneNumber) ||
                      phoneNumber === "" ||
                      selectedPhonePrefix === "") && (
                      <span className="error">
                        Select at least one code / Contact number should be
                        between 6 to 13 digit
                      </span>
                    )}
                </div>

                <div className="horizontal_line">&nbsp;</div>

                <div className="form_control_area">
                  <label className="form_label">
                    Address Line 1 <span className="label_mandatory">*</span>
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={addressLine1}
                    autocomplete="nope"
                    onChange={(e) => setAddressLine1(e.target.value)}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors && addressLine1.trim() === "" && (
                    <span className="error">Complete address</span>
                  )}
                </div>

                <div className="form_control_area address_margin">
                  <label className="form_label"> Address Line 2 </label>
                  <input
                    type="text"
                    className="form_control"
                    value={addressLine2}
                    autocomplete="nope"
                    onChange={(e) => setAddressLine2(e.target.value)}
                    disabled={navigationResponse != null ? true : false}
                  />
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Country <span className="label_mandatory">*</span>
                  </label>
                  {navigationResponse != null ? (
                    <select
                      className="form_control"
                      onChange={onChangeCountryHandler}
                      disabled={navigationResponse != null ? true : false}
                    >
                      <option
                        selected
                        value={
                          navigationResponse?.vendorCompanysModel
                            ?.masterCountryModel?.id
                        }
                        key={
                          navigationResponse?.vendorCompanysModel
                            ?.masterCountryModel?.id
                        }
                      >
                        {
                          navigationResponse?.vendorCompanysModel
                            ?.masterCountryModel?.name
                        }
                      </option>
                      )
                    </select>
                  ) : (
                    <>
                      <div
                        style={{
                          display: hide && "none",
                          fontFamily: "Poppins-Regular",
                          position: "absolute",
                          marginLeft: "12px",
                          lineHeight: "2.5",
                          zIndex: 1,
                          fontSize: "16px",
                          fontFamily: "Poppins-Regular",
                        }}
                      >
                        {countryName}
                      </div>
                      <Select
                        onInputChange={setHide}
                        className="form_control_country"
                        onChange={onChangeCountryHandler}
                        onKeyDown={handleLettersOnlyInput}
                        disabled={navigationResponse != null ? true : false}
                        options={countryOptions}
                        styles={customStyle}
                        components={{
                          SingleValue: () => {
                            return null;
                          },
                        }}
                      ></Select>
                    </>
                  )}
                  {errors && selectedCountry === "" && (
                    <span className="error">Select Country</span>
                  )}
                </div>
                <div className="form_control_area address_margin">
                  <label className="form_label"> State </label>
                  {navigationResponse != null ? (
                    <select
                      className="form_control"
                      onChange={onChangeStateHandler}
                      disabled={navigationResponse != null ? true : false}
                    >
                      <option
                        selected
                        value={
                          navigationResponse?.vendorCompanysModel
                            ?.mstateStateModel?.id
                        }
                        key={
                          navigationResponse?.vendorCompanysModel
                            ?.mstateStateModel?.id
                        }
                      >
                        {
                          navigationResponse?.vendorCompanysModel
                            ?.mstateStateModel?.name
                        }
                      </option>
                      )
                    </select>
                  ) : (
                    <>
                      <Select
                        className="form_control_country"
                        onChange={onChangeStateHandler}
                        onKeyDown={handleLettersOnlyInput}
                        disabled={navigationResponse != null ? true : false}
                        options={stateOptions}
                        value={{
                          value: selectStateByName,
                          label: selectStateByName,
                        }}
                        placeholder={""}
                      />
                      {removeState && (
                        <img
                          src={cancelIcon}
                          alt="cancel icon"
                          className="remove_data_state"
                          onClick={removeSelectState}
                        />
                      )}
                    </>
                  )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    City <span className="label_mandatory">*</span>
                    <Tooltip text="Enter your city here." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={selectedCity}
                    autocomplete="nope"
                    onChange={(e) => {
                      onChangeCityHandler(e);
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors && selectedCity === "" && (
                    <span className="error">Enter City</span>
                  )}
                </div>

                <div className="form_control_area address_margin">
                  <label className="form_label">Postal Code</label>
                  <input
                    type="number"
                    className="form_control"
                    value={postalCode}
                    autocomplete="nope"
                    onChange={(e) => {
                      if (e.target.value.length < 17)
                        setPostalCode(e.target.value.trim());
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {/* {errors &&
                    (postalCode === "" ||
                      postalCode.length < 4 ||
                      postalCode.length > 16) && (
                      <span className="error">Enter Postal code</span>
                    )} */}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Referral Code
                    <Tooltip text="If you have been referred by registered partner, mention their company id." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={referralCode}
                    autocomplete="nope"
                    style={{ textTransform: "uppercase" }}
                    // onBlur={callApiReferralCode}
                    onChange={(e) => {
                      setReferralCode(e.target.value.trim());
                      callApiReferralCode(e);
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {navigationResponse == null &&
                    isReferralCodeError !== "" &&
                    referralCode.length !== 0 && (
                      <span className="error">Invalid Referral Code</span>
                    )}
                </div>

                <div className="form_submit_container">
                  {(addressResponse && navigationCheck === false) ||
                  (registrationStatusKey - 0 > 0 &&
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
          </>
        )}
        {/* ---------------- Partner's Individual have not company form ---------------- */}
        {individualHaveNotCompany && (
          <>
            <div className="nav_container">
              <div className="nav_items">
                <div className="nav_item active">Personal Details</div>
              </div>
            </div>

            <form onSubmit={handleFormSubmissionIdividual}>
              <div className="form_container">
                <div className="form_control_area">
                  <label className="form_label">
                    Name <span className="label_mandatory">*</span>
                    <Tooltip text="Enter Full Name." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={companyName}
                    autocomplete="nope"
                    onChange={(e) => {
                      if (!Number(e.nativeEvent.data))
                        setCompanyName(e.target.value);
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors && companyName.trim() === "" && (
                    <span className="error">Full name, as registered.</span>
                  )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Email <span className="label_mandatory">*</span>
                    <Tooltip text="Please provide your email id." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={companyEmail}
                    autocomplete="nope"
                    onChange={(e) => setCompanyEmail(e.target.value.trim())}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors &&
                    companyEmail.trim() === "" &&
                    !emailValidation(companyEmail.trim()) && (
                      <span className="error">Company Email ID</span>
                    )}
                  {errors &&
                    companyEmail.trim() !== "" &&
                    !emailValidation(companyEmail.trim()) && (
                      <span className="error">Invalid email</span>
                    )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Contact No. <span className="label_mandatory">*</span>
                    <Tooltip text="Please provide your contact number." />
                  </label>
                  <div className="input_container">
                    {navigationResponse != null ? (
                      <select
                        className="select_input_disabled"
                        //  onChange={(e) => setSelectedPhonePrefix(e.target.value)}
                        disabled={navigationResponse != null ? true : false}
                      >
                        {/* <option value="" selected disabled></option> */}

                        <option
                          selected={
                            country?.phoneCode === selectedPhonePrefix
                              ? true
                              : false
                          }
                          value={country.phoneCode}
                          key={country.id}
                        >
                          {
                            navigationResponse?.vendorCompanysModel
                              ?.masterCountryModel?.phoneCode
                          }
                        </option>
                      </select>
                    ) : (
                      <>
                        <div
                          style={{
                            display: hideContact && "none",
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
                          onInputChange={setHideContact}
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
                      value={phoneNumber}
                      autocomplete="nope"
                      onChange={(e) => {
                        if (e.target.value.length < 14)
                          setPhoneNumber(e.target.value.trim());
                      }}
                      disabled={navigationResponse != null ? true : false}
                    />
                  </div>
                  {errors &&
                    (!numberValidation(phoneNumber) ||
                      phoneNumber === "" ||
                      selectedPhonePrefix === "") && (
                      <span className="error">
                        Select at least one code / Contact number should be
                        between 6 to 13 digit
                      </span>
                    )}
                </div>

                <div className="horizontal_line">&nbsp;</div>

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
                        {navigationResponse?.userModel?.availableOn}
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
                    <span className="error">Please select available on</span>
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
                        {`Password must contain at least eight characters, at least one number and both lower and uppercase letters and special characters (!"#$%&'()*+,-./:;<=>?@[\]^_{|}~).`}
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
                <div className="horizontal_line">&nbsp;</div>

                <div className="form_control_area">
                  <label className="form_label">
                    Address Line 1 <span className="label_mandatory">*</span>
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={addressLine1}
                    autocomplete="nope"
                    onChange={(e) => setAddressLine1(e.target.value)}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors && addressLine1.trim() === "" && (
                    <span className="error">Complete address</span>
                  )}
                </div>

                <div className="form_control_area address_margin">
                  <label className="form_label"> Address Line 2 </label>
                  <input
                    type="text"
                    className="form_control"
                    value={addressLine2}
                    autocomplete="nope"
                    onChange={(e) => setAddressLine2(e.target.value)}
                    disabled={navigationResponse != null ? true : false}
                  />
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Country <span className="label_mandatory">*</span>
                  </label>
                  {navigationResponse != null ? (
                    <select
                      className="form_control"
                      onChange={onChangeCountryHandler}
                      disabled={navigationResponse != null ? true : false}
                    >
                      <option
                        selected
                        value={
                          navigationResponse?.vendorCompanysModel
                            ?.masterCountryModel?.id
                        }
                        key={
                          navigationResponse?.vendorCompanysModel
                            ?.masterCountryModel?.id
                        }
                      >
                        {
                          navigationResponse?.vendorCompanysModel
                            ?.masterCountryModel?.name
                        }
                      </option>
                      )
                    </select>
                  ) : (
                    <>
                      <div
                        style={{
                          display: hide && "none",
                          fontFamily: "Poppins-Regular",
                          position: "absolute",
                          marginLeft: "12px",
                          lineHeight: "2.5",
                          zIndex: 1,
                          fontSize: "16px",
                          fontFamily: "Poppins-Regular",
                        }}
                      >
                        {countryName}
                      </div>
                      <Select
                        onInputChange={setHide}
                        className="form_control_country"
                        onChange={onChangeCountryHandler}
                        onKeyDown={handleLettersOnlyInput}
                        disabled={navigationResponse != null ? true : false}
                        options={countryOptions}
                        styles={customStyle}
                        components={{
                          SingleValue: () => {
                            return null;
                          },
                        }}
                      ></Select>
                    </>
                  )}
                  {errors && selectedCountry === "" && (
                    <span className="error">Select Country</span>
                  )}
                </div>

                <div className="form_control_area address_margin">
                  <label className="form_label"> State </label>
                  {navigationResponse != null ? (
                    <select
                      className="form_control"
                      onChange={onChangeStateHandler}
                      disabled={navigationResponse != null ? true : false}
                    >
                      <option
                        selected
                        value={
                          navigationResponse?.vendorCompanysModel
                            ?.mstateStateModel?.id
                        }
                        key={
                          navigationResponse?.vendorCompanysModel
                            ?.mstateStateModel?.id
                        }
                      >
                        {
                          navigationResponse?.vendorCompanysModel
                            ?.mstateStateModel?.name
                        }
                      </option>
                      )
                    </select>
                  ) : (
                    <>
                      <Select
                        className="form_control_country"
                        onChange={onChangeStateHandler}
                        onKeyDown={handleLettersOnlyInput}
                        disabled={navigationResponse != null ? true : false}
                        options={stateOptions}
                        value={{
                          value: selectStateByName,
                          label: selectStateByName,
                        }}
                        placeholder={""}
                      />
                      {removeState && (
                        <img
                          src={cancelIcon}
                          alt="cancel icon"
                          className="remove_data_state"
                          onClick={removeSelectState}
                        />
                      )}
                    </>
                  )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    City <span className="label_mandatory">*</span>
                    <Tooltip text="Enter your city here." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={selectedCity}
                    autocomplete="nope"
                    onChange={(e) => {
                      onChangeCityHandler(e);
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors && selectedCity === "" && (
                    <span className="error">Enter City</span>
                  )}
                </div>

                <div className="form_control_area address_margin">
                  <label className="form_label">Postal Code</label>
                  <input
                    type="number"
                    className="form_control"
                    value={postalCode}
                    autocomplete="nope"
                    onChange={(e) => {
                      if (e.target.value.length < 17)
                        setPostalCode(e.target.value.trim());
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {/* {errors &&
                    (postalCode === "" ||
                      postalCode.length < 4 ||
                      postalCode.length > 16) && (
                      <span className="error">Enter Postal code</span>
                    )} */}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Referral Code
                    <Tooltip text="If you have been referred by registered partner, mention their company id." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={referralCode}
                    autocomplete="nope"
                    style={{ textTransform: "uppercase" }}
                    // onBlur={callApiReferralCode}
                    onChange={(e) => {
                      setReferralCode(e.target.value.trim());
                      callApiReferralCode(e);
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {navigationResponse == null &&
                    isReferralCodeError !== "" &&
                    referralCode.length !== 0 && (
                      <span className="error">Invalid Referral Code</span>
                    )}
                </div>

                <div className="form_submit_container">
                  {(addressResponse && navigationCheck === false) ||
                  (registrationStatusKey - 0 > 0 &&
                    navigationCheck === false &&
                    navigationResponse) ? (
                    <input
                      type="button"
                      value=" Next"
                      className="btn_submit_form"
                      onClick={nextNavigationHandlerHavenotCompany}
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
          </>
        )}
      </div>
    </div>
  );
};

export default Address;
