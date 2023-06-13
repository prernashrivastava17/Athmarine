import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import Flatpickr from "react-flatpickr";
import moment from "moment";
import "flatpickr/dist/themes/material_blue.css";

import ConfirmationModal from "./../../Modals/ConfirmationModal/ConfirmationModal";
import Select from "react-select";
import "./BusinessInformation.scss";
import companyLogoPlaceholder from "./../../../../../assets/images/upload-picture.png";
import calendarIcon from "./../../../../../assets/images/calendar-icon.png";
import Loading from "../../../../../components/Loading/Loading";
import Tooltip from "./../../../../../components/Tooltip/Tooltip";
import { ToastError } from "./../../../../../components/Tostify";
import cancelIcon from "./../../../../../assets/images/cancel_other_icon.png";
import {
  getAuthData,
  getAuthToken,
  getCompanyId,
  getReferralCode,
  getToken,
  getUserId,
} from "../../../../../config";
import country, { getCountries } from "./../../../../../store/common/country";
import {
  getAllCityByCountryId,
  cityActions,
} from "./../../../../../store/common/city";
import {
  saveBusinessInformation,
  saveBusinessInformationActions,
} from "../../../../../store/vendorRegistration/businessInformation";
import {
  commonGetAPiCall,
  commonGetApiActions,
} from "../../../../../store/common/commonGetApi";
import {
  fileUpload,
  uploadActions,
} from "../../../../../store/common/fileUpload";
import {
  documentDownloadApi,
  documentRequestorActions,
} from "../../../../../store/common/documentRequest";
import {
  addressGetAPiCall,
  addressGetApiActions,
} from "../../../../../store/vendorRegistration/addressGetApi";

const BusinessInformation = (props) => {
  const dispatch = useDispatch();

  let isFormTypePartner = props.selectedUser === "partner" ? true : false;
  let isFormTypeCustomer = props.selectedUser === "customer" ? true : false;
  let isFormTypePartnersIndividual =
    props.selectedUser === "partners-individual" ? true : false;

  const countries = useSelector((state) => state.countries.countries);
  const isCountryLoading = useSelector((state) => state.countries.isLoading);
  const isCountryError = useSelector((state) => state.countries.isError);
  const cities = useSelector((state) => state.cities.cities);
  const iscityLoading = useSelector((state) => state.cities.isLoading);
  const iscityError = useSelector((state) => state.cities.isError);
  const isBusinessInfoLoading = useSelector(
    (state) => state.businessInformation.isLoading
  );
  const businessInfoResponse = useSelector(
    (state) => state.businessInformation.businessInformationRes
  );
  const navigationCheck = useSelector(
    (state) => state.businessInformation.navigationCheck
  );
  const businessInfoResponseError = useSelector(
    (state) => state.businessInformation.isError
  );
  const navigationResponse = useSelector(
    (state) => state.commonGetApiReducer.getApiRes
  );
  const fileUploadRes = useSelector((state) => state.fileUploadReducer.upload);
  const fileUploadResIsLoading = useSelector(
    (state) => state.fileUploadReducer.isLoading
  );
  const documentRequestRes = useSelector(
    (state) => state.documentRequestReducer.fileRes
  );
  const registrationStatusKey = useSelector(
    (state) => state.commonGetApiReducer.registrationStatusKey
  );
  const addressData = useSelector(
    (state) => state.addressGetApiReducer.getApiRes
  );

  useEffect(() => {
    dispatch(documentRequestorActions.emptyState());
    if (isFormTypePartner || isFormTypePartnersIndividual) {
      dispatch(addressGetAPiCall(`/vendor/get/${getCompanyId()}`));
    } else {
      dispatch(addressGetAPiCall(`/customer/get/${getCompanyId()}`));
    }
  }, []);

  useEffect(() => {
    if (fileUploadRes !== null) {
      dispatch(
        documentDownloadApi(
          `file/download?documentRequest=${encodeURIComponent(
            fileUploadRes?.fileKey
          )}`
        )
      );
    }
  }, [dispatch, fileUploadRes]);

  useEffect(() => {
    if (navigationResponse !== null) {
      setIsCompanyRegistered(
        navigationResponse?.registered === true ? "true" : "false"
      );
      if (navigationResponse?.registered === false) {
        setEstablishedOn(navigationResponse?.yearOfReg);
      }
      setRegisteredOn(navigationResponse?.yearOfReg);
      setRegistrationNo(navigationResponse?.registrationNo);
      setCountryOfRegistration(navigationResponse?.country_of_registration);
      setCityOfRegistration(navigationResponse?.city_of_registration);
      setIsEstablishedInFiveYears(
        navigationResponse?.isEstablishedLastFiveYear === "false"
          ? "false"
          : "true"
      );
      setLogo(navigationResponse?.logo);
      if (navigationResponse?.logo) {
        // document image download api
        dispatch(
          documentDownloadApi(
            `file/download?documentRequest=${encodeURIComponent(
              navigationResponse?.logo
            )}`
          )
        );
      }
    }
  }, [dispatch, navigationResponse]);

  const getCompanyIdCheck = () => {
    if (getAuthData()?.companyId) {
      return getAuthData()?.companyId;
    }
    return getCompanyId();
  };

  // get api call after login
  useEffect(() => {
    if (
      registrationStatusKey - 3 > 0 &&
      (isFormTypePartner || isFormTypePartnersIndividual)
    ) {
      dispatch(
        commonGetAPiCall(`/vendor/getBuisnessInfo/${getCompanyIdCheck()}`)
      );
    } else if (registrationStatusKey - 2 > 0 && isFormTypeCustomer) {
      dispatch(
        commonGetAPiCall(`/customer/getBuisnessInfo/${getCompanyIdCheck()}`)
      );
    }
  }, [
    dispatch,
    registrationStatusKey,
    isFormTypePartner,
    isFormTypeCustomer,
    isFormTypePartnersIndividual,
  ]);

  useEffect(() => {
    if (
      !navigationCheck &&
      businessInfoResponse !== null &&
      (isFormTypePartner || isFormTypePartnersIndividual)
    ) {
      dispatch(uploadActions.emptyState());
      dispatch(commonGetAPiCall(`/vendor/getBuisnessInfo/${getCompanyId()}`));
    } else if (
      !navigationCheck &&
      businessInfoResponse !== null &&
      isFormTypeCustomer
    ) {
      dispatch(uploadActions.emptyState());
      dispatch(commonGetAPiCall(`/customer/getBuisnessInfo/${getCompanyId()}`));
    }
  }, [
    dispatch,
    navigationCheck,
    businessInfoResponse,
    isFormTypePartner,
    isFormTypeCustomer,
    isFormTypePartnersIndividual,
  ]);

  const [registeredOn, setRegisteredOn] = useState("");
  const [establishedOn, setEstablishedOn] = useState("");
  const [isCompanyRegistered, setIsCompanyRegistered] = useState("true");
  const [registrationNo, setRegistrationNo] = useState("");
  const [countryOfRegistration, setCountryOfRegistration] = useState("");
  const [cityOfRegistration, setCityOfRegistration] = useState("");
  const [isEstablishedInFiveYears, setIsEstablishedInFiveYears] =
    useState("false");
  const [isConfirmationModal, setIsConfirmationModal] = useState(false);
  const [logo, setLogo] = useState(null);
  const [isShowCalendar, setIsShowCalendar] = useState(false);
  const [errors, setErrors] = useState(false);
  const [errorsEstablished, setErrorsEstablished] = useState(false);
  const [hide, setHide] = useState();
  const [countryName, setCountryName] = useState();
  const [isCompanyNotRegistered, setIsCompanyNotRegistered] = useState(false);

  const validateForm = () => {
    if (
      registeredOn.length !== 0 &&
      registrationNo.trim().length !== 0 &&
      countryOfRegistration.length !== 0
      // &&
      // cityOfRegistration.length !== 0  && isCompanyRegistered === "true"
    ) {
      setErrors(false);
      return true;
    } else if (
      establishedOn.length !== 0 &&
      isEstablishedInFiveYears === "true"
    ) {
      setErrorsEstablished(false);
      return true;
    } else {
      setErrors(true);
      setErrorsEstablished(true);
      return false;
    }
  };

  useEffect(() => {
    document.querySelector("#my_nav").scrollLeft += 200;
    dispatch(getCountries());
    if (navigationResponse === null) {
      dispatch(cityActions.setEmptyCity());
    }
  }, [dispatch, navigationResponse]);

  useEffect(() => {
    if (isCountryError !== "") {
      ToastError(isCountryError);
    } else if (iscityError !== "") {
      ToastError(iscityError);
    } else if (businessInfoResponseError !== "") {
      ToastError(businessInfoResponseError);
    }
  }, [isCountryError, iscityError, businessInfoResponseError]);

  useEffect(() => {
    if (navigationCheck) {
      if (businessInfoResponse !== null && isFormTypePartner) {
        props.setFormLabel("bidder", 2);
      } else if (businessInfoResponse !== null && isFormTypeCustomer) {
        props.setFormLabel("bidder", 2);
      } else if (
        businessInfoResponse !== null &&
        isFormTypePartnersIndividual
      ) {
        props.setFormLabel("engineer", 2);
      }
    }
  }, [
    businessInfoResponse,
    props,
    isFormTypePartner,
    isFormTypeCustomer,
    navigationCheck,
    isFormTypePartnersIndividual,
  ]);

  const onChangeCountryHandler = (e) => {
    const id = e.value;
    setCityOfRegistration("");
    setCountryName(e.value);
    setCountryOfRegistration(e.value);
    // dispatch(getAllCityByCountryId(id));
  };

  console.log("iuyuiuy", addressData?.vendorCompanysModel?.masterCityModel);

  function getDate() {
    if (registeredOn !== "") {
      return registeredOn && moment(registeredOn[0]).format();
    } else {
      return establishedOn && moment(establishedOn[0]).format();
    }
  }

  const handleFormSubmission = (event) => {
    event.preventDefault();
    //changes to reset state based on iscompanyRegistered state
    if (isCompanyRegistered !== "true" && isEstablishedInFiveYears !== "true") {
      setCountryOfRegistration("");
      setCityOfRegistration("");
      // setIsEstablishedInFiveYears("false");
      setRegisteredOn("");
      setRegistrationNo("");
      setIsConfirmationModal(!isConfirmationModal);
    } else if (isCompanyRegistered === "true") {
      if (validateForm()) {
        setIsConfirmationModal(!isConfirmationModal);
      }
    } else if (isEstablishedInFiveYears === "true") {
      if (validateForm()) {
        setIsConfirmationModal(!isConfirmationModal);
      }
    }
    // else if(validateForm() || isCompanyRegistered === "true")
  };

  const onCancelHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
  };

  const onOkHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
    apiCall();
  };

  const apiCall = () => {
    let companyDetails = JSON.parse(localStorage.getItem("addressInfo"));
    let businessInformationObj = {
      registered: isCompanyRegistered === "true" ? true : false,
      // isEstablishedLastFiveYear:
      //   isEstablishedInFiveYears === "true" ? true : false,
      isEstablishedLastFiveYear: false,
      id: getCompanyId(),
      userId: getUserId(),
      logo: fileUploadRes?.fileKey,
      registrationNo: registrationNo,
      country_of_registration: countryOfRegistration,
      city_of_registration: cityOfRegistration,
      yearOfReg: getDate(),

      masterCountryModel: {
        id: companyDetails?.masterCountryModel.id,
      },
      mstateStateModel:
        companyDetails?.mstateStateModel === undefined
          ? null
          : companyDetails?.mstateStateModel,
      masterCityModel: isFormTypeCustomer
        ? addressData?.customerCompanyModel?.masterCity
        : addressData?.vendorCompanysModel?.masterCityModel,
    };

    if (isFormTypePartner || isFormTypePartnersIndividual) {
      console.log("data", businessInformationObj);
      dispatch(
        saveBusinessInformation(
          businessInformationObj,
          "vendor/updateBuisnessInformation"
        )
      );
    } else if (isFormTypeCustomer) {
      dispatch(
        saveBusinessInformation(businessInformationObj, "customer/update")
      );
    }
  };

  const getTokenFunc = () => {
    if (getAuthToken()) {
      return getAuthToken();
    }
    return getToken();
  };

  const getSelectedLogoInfo = (e) => {
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
          isFileForNotAllowedText = `fileName: ${fileName} file is not allowed. Acceptable format png, jpg, jpeg.\n`;
        } else if (fileSize > 3145728) {
          isFileForNotAllowed = true;
          isFileForNotAllowedText = `fileName: ${fileName} size should be less than 3 mb.\n`;
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
      // added ?. to avoid empty image upload call
      dispatch(
        fileUpload(
          formData,
          `file/upload?fileKey=${
            allSelectedFiles?.item(0).name
          }&token=${getTokenFunc()}`
        )
      );
    }
    // Always put it into last of the function, otherwise same file doesn't upload multiple times
    e.target.value = "";
  };

  const nextNavigationHandler = () => {
    dispatch(
      saveBusinessInformationActions.businessInformationNavigation(true)
    );
    dispatch(commonGetApiActions.dispatchGetRequest());
    props.navigationHandler({
      currentForm: "bidder",
      goForward: true,
    });
    props.setFormLabel("bidder", 2);
  };

  const nextNavigationHandlerforIndividual = () => {
    dispatch(
      saveBusinessInformationActions.businessInformationNavigation(true)
    );
    dispatch(commonGetApiActions.dispatchGetRequest());
    props.navigationHandler({
      currentForm: "engineer",
      goForward: true,
    });
    props.setFormLabel("engineer", 2);
  };

  const blankRegisteredCompanyFieldHandler = (e) => {
    setIsCompanyRegistered(e.target.value);
    setRegistrationNo("");
    setRegisteredOn("");
    setCountryOfRegistration("");
    setCityOfRegistration("");
    setEstablishedOn("");
    setIsEstablishedInFiveYears("true");
    setIsCompanyNotRegistered(!isCompanyNotRegistered);
  };

  const blankEstablisedOnHandler = (e) => {
    setIsEstablishedInFiveYears(e.target.value);
    setEstablishedOn("");
  };

  const handleLettersOnlyInput = (e) => {
    if (Number.isInteger(Number(e.key))) {
      e.preventDefault();
    }
  };

  const countryOptions = countries?.map((country) => ({
    value: country.name,
    label: [
      <img
        src={country.flag}
        style={{ width: "20px", marginRight: "15px" }}
        alt="countryFlag"
      />,
      <span style={{ fontFamily: "Poppins-Regular" }}>{country.name}</span>,
    ],
  }));

  const customStyle = {
    container: (base, state) => ({
      ...base,
    }),
    menu: (styles) => ({
      ...styles,
      width: "100%",
    }),
  };

  return (
    <div className="form_container_area">
      <div className="business_information_form positioning_container">
        {(isBusinessInfoLoading ||
          isCountryLoading ||
          iscityLoading ||
          fileUploadResIsLoading) && <Loading text={"Loading...."} />}
        {isConfirmationModal && (
          <ConfirmationModal cancel={onCancelHandler} ok={onOkHandler} />
        )}

        {
          // <button onClick={() => console.log(fileUploadRes?.fileKey)}>
          //   Get State
          // </button>
        }

        {/* ---------------- Partner form ---------------- */}
        {isFormTypePartner && (
          <>
            <div className="nav_container">
              <div className="nav_items" id="my_nav">
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
                <div
                  className="nav_item active"
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
                  <span className="horizontal_row active">&nbsp;</span>
                </div>
                <div className="nav_item active">Business Information</div>
              </div>
            </div>

            <div className="company_id_details">
              <p className="company_id">Company id: {getReferralCode()} </p>
            </div>

            <form onSubmit={handleFormSubmission} encType="multipart/form-data">
              <div className="form_container">
                <div
                  className={
                    fileUploadRes
                      ? "form_control_area for_remove_img"
                      : "form_control_area"
                  }
                >
                  <label htmlFor="file-upload" style={{ width: "100%" }}>
                    <div
                      className={
                        navigationResponse != null
                          ? "upload_container_disabled"
                          : "upload_container"
                      }
                    >
                      <div className="display_logo_container">
                        <img
                          src={
                            fileUploadRes
                              ? documentRequestRes
                              : companyLogoPlaceholder
                          }
                          alt="company logo"
                        />
                      </div>
                      <div className="upload_logo_container">
                        <label className="upload_text" htmlFor="file-upload">
                          {navigationResponse === null &&
                            (fileUploadRes ? (
                              <div className="uploadFileName">
                                {fileUploadRes?.fileKey?.split("/")[1]}
                              </div>
                            ) : (
                              <>
                                <span> Upload Company Logo</span>
                                <Tooltip text="File should be in PNG, JPEG or JPG format." />
                              </>
                            ))}
                          {navigationResponse !== null &&
                            fileUploadRes == null &&
                            logo !== null && (
                              <div className="uploadFileName">
                                {logo?.split("/")[1]}
                              </div>
                            )}

                          {}
                        </label>
                        <input
                          id="file-upload"
                          type="file"
                          accept=".png, .jpg, .jpeg"
                          data-allowed-file-extension="png, jpg, jpeg"
                          onChange={(e) => getSelectedLogoInfo(e)}
                          disabled={navigationResponse != null ? true : false}
                        />
                      </div>
                    </div>
                  </label>
                  {fileUploadRes && (
                    <div
                      onClick={() => dispatch(uploadActions.emptyState())}
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

                <div className="form_control_spread">
                  <label className="form_label">
                    My company is registered?
                  </label>
                  <div className="radio_container">
                    <span className="a_control">
                      <input
                        type="radio"
                        name="admin-account"
                        className="form_control_radio"
                        id="admin-account-yes"
                        value="true"
                        checked={isCompanyRegistered === "true"}
                        // onChange={(e) => setIsCompanyRegistered(e.target.value)}
                        onChange={blankRegisteredCompanyFieldHandler}
                        disabled={navigationResponse !== null ? true : false}
                      />
                      <label className="form_label" htmlFor="admin-account-yes">
                        Yes
                      </label>
                    </span>
                    <span className="a_control">
                      <input
                        type="radio"
                        name="admin-account"
                        className="form_control_radio"
                        id="admin-account-no"
                        value="false"
                        checked={isCompanyRegistered === "false"}
                        // onChange={(e) => setIsCompanyRegistered(e.target.value)}
                        onChange={blankRegisteredCompanyFieldHandler}
                        disabled={navigationResponse !== null ? true : false}
                      />
                      <label className="form_label" htmlFor="admin-account-no">
                        No
                      </label>
                    </span>
                  </div>
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Registration No.
                    <span
                      className={
                        isCompanyRegistered === "true"
                          ? "label_mandatory"
                          : "fade_label_mandatory"
                      }
                    >
                      *
                    </span>
                    <Tooltip text="Enter company registration number." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={registrationNo}
                    onChange={(e) => setRegistrationNo(e.target.value)}
                    disabled={
                      navigationResponse != null
                        ? true
                        : isCompanyRegistered === "false"
                        ? true
                        : false
                    }
                  />
                  {errors &&
                    registrationNo.trim().length === 0 &&
                    isCompanyRegistered === "true" && (
                      <span className="error">
                        Registration number is required.
                      </span>
                    )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Registered On
                    <span
                      className={
                        isCompanyRegistered === "true"
                          ? "label_mandatory"
                          : "fade_label_mandatory"
                      }
                    >
                      *
                    </span>
                    <Tooltip text="Enter the date when company was registered. " />
                  </label>
                  {isCompanyRegistered === "false" ? (
                    <input
                      type="text"
                      disabled
                      className="my_form_control"
                      placeholder="Months and years"
                    />
                  ) : navigationResponse != null ? (
                    <input
                      type="text"
                      className="form_control"
                      value={registeredOn}
                      disabled={
                        navigationResponse != null
                          ? true
                          : isCompanyRegistered === "false"
                          ? true
                          : false
                      }
                    />
                  ) : (
                    <Flatpickr
                      className={
                        navigationResponse !== null
                          ? "form_control_flatpickr_disabled"
                          : "form_control_flatpickr"
                      }
                      value={registeredOn}
                      onChange={(date) => setRegisteredOn(date)}
                      options={{
                        maxDate: "today",
                        minDate: "1-1-1950",
                        altInput: true,
                        altFormat: "F j, Y",
                        dateFormat: "Y-m-d",
                      }}
                    />
                  )}
                  <span
                    className="icon_container"
                    onClick={() => setIsShowCalendar(!isShowCalendar)}
                  >
                    <img src={calendarIcon} alt="calendar-icon" />
                  </span>
                  {errors &&
                    isCompanyRegistered === "true" &&
                    registeredOn.length === 0 && (
                      <span className="error">
                        Please enter date of company registration.
                      </span>
                    )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Country of Registration
                    <span
                      className={
                        isCompanyRegistered === "true"
                          ? "label_mandatory"
                          : "fade_label_mandatory"
                      }
                    >
                      *
                    </span>
                  </label>
                  {isCompanyRegistered === "false" ? (
                    <input
                      type="text"
                      disabled
                      className="my_form_control"
                      placeholder=" "
                    />
                  ) : navigationResponse != null ? (
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
                        {navigationResponse?.country_of_registration}
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
                          zIndex: 10,
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
                  {errors &&
                    isCompanyRegistered === "true" &&
                    countryOfRegistration.length === 0 && (
                      <span className="error">
                        Please select country of registration.
                      </span>
                    )}
                </div>

                <div className="form_control_area address_margin">
                  <label className="form_label"> City of Registration</label>
                  {isCompanyRegistered === "false" ? (
                    <input
                      type="text"
                      disabled
                      className="my_form_control"
                      placeholder=" "
                    />
                  ) : (
                    <input
                      type="text"
                      className="form_control"
                      value={cityOfRegistration}
                      autocomplete="nope"
                      onChange={(e) => {
                        setCityOfRegistration(e.target.value);
                      }}
                      disabled={navigationResponse != null ? true : false}
                    />
                  )}

                  {/* {
                  errors && isCompanyRegistered === "true" && (cityOfRegistration.length === 0) &&(
                    <span className="error">
                      City is required.
                    </span>
                  )
                } */}
                </div>
                {(isCompanyNotRegistered || establishedOn) && (
                  <>
                    <div className="form_control_spread">
                      {/* <label className="form_label">
                        Is your Company established in last five years?
                      </label> */}

                      {/* <div className="radio_container">
                        <span className="a_control">
                          <input
                            type="radio"
                            name="a_admin-account-five-yes"
                            className="form_control_radio"
                            id="a_admin-account-five-yes"
                            value="true"
                            checked={isEstablishedInFiveYears === "true"}
                            // onChange={(e) =>
                            //   setIsEstablishedInFiveYears(e.target.value)
                            // }
                            onChange={blankEstablisedOnHandler}
                            // disabled={navigationResponse !== null ? true : false}
                            disabled={
                              isCompanyRegistered === "true" ? true : false
                            }
                          />
                          <label
                            className="form_label"
                            htmlFor="a_admin-account-five-yes"
                          >
                            Yes
                          </label>
                        </span>
                        <span className="a_control">
                          <input
                            type="radio"
                            name="a_admin-account-five-yes"
                            className="form_control_radio"
                            id="a_admin-account-five-no"
                            value="false"
                            checked={isEstablishedInFiveYears === "false"}
                            // onChange={(e) =>
                            //   setIsEstablishedInFiveYears(e.target.value)
                            // }
                            onChange={blankEstablisedOnHandler}
                            disabled={
                              navigationResponse !== null ? true : false
                            }
                          />
                          <label
                            className="form_label"
                            htmlFor="a_admin-account-five-no"
                          >
                            No
                          </label>
                        </span>
                      </div> */}
                    </div>

                    <div className="form_control_area">
                      <label className="form_label">
                        Company Established On
                        <span
                          className={
                            isEstablishedInFiveYears === "true"
                              ? "label_mandatory"
                              : "fade_label_mandatory"
                          }
                        >
                          *
                        </span>
                      </label>
                      {isCompanyRegistered === "false" &&
                      isEstablishedInFiveYears === "true" ? (
                        <Flatpickr
                          className="form_control_flatpickr"
                          // placeholder="Months and years"
                          value={establishedOn}
                          onChange={(date) => setEstablishedOn(date)}
                          options={{
                            maxDate: "today",
                            minDate: "1-1-1950",
                            altInput: true,
                            altFormat: "F j, Y",
                            dateFormat: "Y-m-d",
                          }}
                          disabled={navigationResponse !== null ? true : null}
                        />
                      ) : (
                        <input
                          type="text"
                          disabled
                          className="form_control"
                          value={establishedOn}
                          // placeholder="Months and years"
                        />
                      )}
                      <span className="icon_container">
                        <img src={calendarIcon} alt="calendar-icon" />
                      </span>
                      {errorsEstablished &&
                        establishedOn.length === 0 &&
                        isEstablishedInFiveYears === "true" && (
                          <span className="error">
                            Established date is required.
                          </span>
                        )}
                    </div>
                  </>
                )}
                <div className="form_submit_container">
                  {(businessInfoResponse && navigationCheck === false) ||
                  (registrationStatusKey - 3 > 0 &&
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

        {/* ---------------- Partner Individual form ---------------- */}
        {isFormTypePartnersIndividual && (
          <>
            <div className="nav_container">
              <div className="nav_items" id="my_nav">
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
                <div className="nav_item active">Business Information</div>
              </div>
            </div>

            <div className="company_id_details">
              <p className="company_id">Company id: {getReferralCode()} </p>
            </div>

            <form onSubmit={handleFormSubmission} encType="multipart/form-data">
              <div className="form_container">
                <div
                  className={
                    fileUploadRes
                      ? "form_control_area for_remove_img"
                      : "form_control_area"
                  }
                >
                  <label htmlFor="file-upload" style={{ width: "100%" }}>
                    <div
                      className={
                        navigationResponse != null
                          ? "upload_container_disabled"
                          : "upload_container"
                      }
                    >
                      <div className="display_logo_container">
                        <img
                          src={
                            fileUploadRes
                              ? documentRequestRes
                              : companyLogoPlaceholder
                          }
                          alt="company logo"
                        />
                      </div>
                      <div className="upload_logo_container">
                        <label className="upload_text" htmlFor="file-upload">
                          {navigationResponse === null &&
                            (fileUploadRes ? (
                              <div className="uploadFileName">
                                {fileUploadRes?.fileKey?.split("/")[1]}
                              </div>
                            ) : (
                              <>
                                <span> Upload Company Logo</span>
                                <Tooltip text="File should be in PNG, JPEG or JPG format." />
                              </>
                            ))}
                          {navigationResponse !== null &&
                            fileUploadRes == null &&
                            logo !== null && (
                              <div className="uploadFileName">
                                {logo?.split("/")[1]}
                              </div>
                            )}

                          {}
                        </label>
                        <input
                          id="file-upload"
                          type="file"
                          accept=".png"
                          data-allowed-file-extension="png"
                          onChange={(e) => getSelectedLogoInfo(e)}
                          disabled={navigationResponse != null ? true : false}
                        />
                      </div>
                    </div>
                  </label>
                  {fileUploadRes && (
                    <div
                      onClick={() => dispatch(uploadActions.emptyState())}
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

                <div className="form_control_spread">
                  <label className="form_label">
                    My company is registered?
                  </label>
                  <div className="radio_container">
                    <span className="a_control">
                      <input
                        type="radio"
                        name="admin-account"
                        className="form_control_radio"
                        id="admin-account-yes"
                        value="true"
                        checked={isCompanyRegistered === "true"}
                        // onChange={(e) => setIsCompanyRegistered(e.target.value)}
                        onChange={blankRegisteredCompanyFieldHandler}
                        disabled={navigationResponse !== null ? true : false}
                      />
                      <label className="form_label" htmlFor="admin-account-yes">
                        Yes
                      </label>
                    </span>
                    <span className="a_control">
                      <input
                        type="radio"
                        name="admin-account"
                        className="form_control_radio"
                        id="admin-account-no"
                        value="false"
                        checked={isCompanyRegistered === "false"}
                        // onChange={(e) => setIsCompanyRegistered(e.target.value)}
                        onChange={blankRegisteredCompanyFieldHandler}
                        disabled={navigationResponse !== null ? true : false}
                      />
                      <label className="form_label" htmlFor="admin-account-no">
                        No
                      </label>
                    </span>
                  </div>
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Registration No.
                    <span
                      className={
                        isCompanyRegistered === "true"
                          ? "label_mandatory"
                          : "fade_label_mandatory"
                      }
                    >
                      *
                    </span>
                    <Tooltip text="Enter company registration number." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={registrationNo}
                    onChange={(e) => setRegistrationNo(e.target.value)}
                    disabled={
                      navigationResponse != null
                        ? true
                        : isCompanyRegistered === "false"
                        ? true
                        : false
                    }
                  />
                  {errors &&
                    registrationNo.trim().length === 0 &&
                    isCompanyRegistered === "true" && (
                      <span className="error">
                        Registration number is required.
                      </span>
                    )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Registered On
                    <span
                      className={
                        isCompanyRegistered === "true"
                          ? "label_mandatory"
                          : "fade_label_mandatory"
                      }
                    >
                      *
                    </span>
                    <Tooltip text="Enter the date when company was registered. " />
                  </label>
                  {isCompanyRegistered === "false" ? (
                    <input
                      type="text"
                      disabled
                      className="my_form_control"
                      placeholder="Months and years"
                    />
                  ) : navigationResponse != null ? (
                    <input
                      type="text"
                      className="form_control"
                      value={registeredOn}
                      disabled={
                        navigationResponse != null
                          ? true
                          : isCompanyRegistered === "false"
                          ? true
                          : false
                      }
                    />
                  ) : (
                    <Flatpickr
                      className={
                        navigationResponse !== null
                          ? "form_control_flatpickr_disabled"
                          : "form_control_flatpickr"
                      }
                      value={registeredOn}
                      onChange={(date) => setRegisteredOn(date)}
                      options={{
                        maxDate: "today",
                        minDate: "1-1-1950",
                        altInput: true,
                        altFormat: "F j, Y",
                        dateFormat: "Y-m-d",
                      }}
                    />
                  )}
                  <span
                    className="icon_container"
                    onClick={() => setIsShowCalendar(!isShowCalendar)}
                  >
                    <img src={calendarIcon} alt="calendar-icon" />
                  </span>
                  {errors &&
                    isCompanyRegistered === "true" &&
                    registeredOn.length === 0 && (
                      <span className="error">
                        Please enter date of company registration.
                      </span>
                    )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Country of Registration
                    <span
                      className={
                        isCompanyRegistered === "true"
                          ? "label_mandatory"
                          : "fade_label_mandatory"
                      }
                    >
                      *
                    </span>
                  </label>
                  {isCompanyRegistered === "false" ? (
                    <input
                      type="text"
                      disabled
                      className="my_form_control"
                      placeholder=" "
                    />
                  ) : navigationResponse != null ? (
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
                        {navigationResponse?.country_of_registration}
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
                          zIndex: 10,
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
                  {errors &&
                    isCompanyRegistered === "true" &&
                    countryOfRegistration.length === 0 && (
                      <span className="error">
                        Please select country of registration.
                      </span>
                    )}
                </div>

                <div className="form_control_area address_margin">
                  <label className="form_label"> City of Registration</label>
                  {isCompanyRegistered === "false" ? (
                    <input
                      type="text"
                      disabled
                      className="my_form_control"
                      placeholder=" "
                    />
                  ) : (
                    <input
                      type="text"
                      className="form_control"
                      value={cityOfRegistration}
                      autocomplete="nope"
                      onChange={(e) => {
                        setCityOfRegistration(e.target.value);
                      }}
                      disabled={navigationResponse != null ? true : false}
                    />
                  )}

                  {/* {
                  errors && isCompanyRegistered === "true" && (cityOfRegistration.length === 0) &&(
                    <span className="error">
                      City is required.
                    </span>
                  )
                } */}
                </div>
                {(isCompanyNotRegistered || establishedOn) && (
                  <>
                    <div className="form_control_spread">
                      {/* <label className="form_label">
                        Is your Company established in last five years?
                      </label> */}

                      {/* <div className="radio_container">
                        <span className="a_control">
                          <input
                            type="radio"
                            name="a_admin-account-five-yes"
                            className="form_control_radio"
                            id="a_admin-account-five-yes"
                            value="true"
                            checked={isEstablishedInFiveYears === "true"}
                            // onChange={(e) =>
                            //   setIsEstablishedInFiveYears(e.target.value)
                            // }
                            onChange={blankEstablisedOnHandler}
                            // disabled={navigationResponse !== null ? true : false}
                            disabled={
                              isCompanyRegistered === "true" ? true : false
                            }
                          />
                          <label
                            className="form_label"
                            htmlFor="a_admin-account-five-yes"
                          >
                            Yes
                          </label>
                        </span>
                        <span className="a_control">
                          <input
                            type="radio"
                            name="a_admin-account-five-yes"
                            className="form_control_radio"
                            id="a_admin-account-five-no"
                            value="false"
                            checked={isEstablishedInFiveYears === "false"}
                            // onChange={(e) =>
                            //   setIsEstablishedInFiveYears(e.target.value)
                            // }
                            onChange={blankEstablisedOnHandler}
                            disabled={
                              navigationResponse !== null ? true : false
                            }
                          />
                          <label
                            className="form_label"
                            htmlFor="a_admin-account-five-no"
                          >
                            No
                          </label>
                        </span>
                      </div> */}
                    </div>

                    <div className="form_control_area">
                      <label className="form_label">
                        Company Established On
                        <span
                          className={
                            isEstablishedInFiveYears === "true"
                              ? "label_mandatory"
                              : "fade_label_mandatory"
                          }
                        >
                          *
                        </span>
                      </label>
                      {isCompanyRegistered === "false" &&
                      isEstablishedInFiveYears === "true" ? (
                        <Flatpickr
                          className="form_control_flatpickr"
                          // placeholder="Months and years"
                          value={establishedOn}
                          onChange={(date) => setEstablishedOn(date)}
                          options={{
                            maxDate: "today",
                            minDate: "1-1-1950",
                            altInput: true,
                            altFormat: "F j, Y",
                            dateFormat: "Y-m-d",
                          }}
                          disabled={navigationResponse !== null ? true : null}
                        />
                      ) : (
                        <input
                          type="text"
                          disabled
                          className="form_control"
                          value={establishedOn}
                          // placeholder="Months and years"
                        />
                      )}
                      <span className="icon_container">
                        <img src={calendarIcon} alt="calendar-icon" />
                      </span>
                      {errorsEstablished &&
                        establishedOn.length === 0 &&
                        isEstablishedInFiveYears === "true" && (
                          <span className="error">
                            Established date is required.
                          </span>
                        )}
                    </div>
                  </>
                )}
                <div className="form_submit_container">
                  {(businessInfoResponse && navigationCheck === false) ||
                  (registrationStatusKey - 3 > 0 &&
                    navigationCheck === false &&
                    navigationResponse) ? (
                    <input
                      type="button"
                      value=" Next"
                      className="btn_submit_form"
                      onClick={nextNavigationHandlerforIndividual}
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
              <div className="nav_items" id="my_nav">
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
                <div className="nav_item active">Business Information</div>
              </div>
            </div>

            <form onSubmit={handleFormSubmission} encType="multipart/form-data">
              <div className="form_container">
                <div
                  className={
                    fileUploadRes
                      ? "form_control_area for_remove_img"
                      : "form_control_area"
                  }
                >
                  <label htmlFor="file-upload" style={{ width: "100%" }}>
                    <div
                      className={
                        navigationResponse != null
                          ? "upload_container_disabled"
                          : "upload_container"
                      }
                    >
                      <div className="display_logo_container">
                        <img
                          src={
                            fileUploadRes
                              ? documentRequestRes
                              : companyLogoPlaceholder
                          }
                          alt="company logo"
                        />
                      </div>
                      <div className="upload_logo_container">
                        <label className="upload_text" htmlFor="file-upload">
                          {navigationResponse === null &&
                            (fileUploadRes ? (
                              <div className="uploadFileName">
                                {fileUploadRes?.fileKey?.split("/")[1]}
                              </div>
                            ) : (
                              <>
                                <span> Upload Company Logo</span>
                                <Tooltip text="File should be in PNG, JPEG or JPG format." />
                              </>
                            ))}
                          {navigationResponse !== null &&
                            fileUploadRes == null &&
                            logo !== null && (
                              <div className="uploadFileName">
                                {logo?.split("/")[1]}
                              </div>
                            )}

                          {}
                        </label>
                        <input
                          id="file-upload"
                          type="file"
                          accept=".png, .jpg, .jpeg"
                          data-allowed-file-extension="png, jpg, jpeg"
                          onChange={(e) => getSelectedLogoInfo(e)}
                          disabled={navigationResponse != null ? true : false}
                        />
                      </div>
                    </div>
                  </label>
                  {fileUploadRes && (
                    <div
                      onClick={() => dispatch(uploadActions.emptyState())}
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

                <div className="form_control_spread">
                  <label className="form_label">
                    My company is registered?
                  </label>
                  <div className="radio_container">
                    <span className="a_control">
                      <input
                        type="radio"
                        name="admin-account"
                        className="form_control_radio"
                        id="admin-account-yes"
                        value="true"
                        checked={isCompanyRegistered === "true"}
                        // onChange={(e) => setIsCompanyRegistered(e.target.value)}
                        onChange={blankRegisteredCompanyFieldHandler}
                        disabled={navigationResponse !== null ? true : false}
                      />
                      <label className="form_label" htmlFor="admin-account-yes">
                        Yes
                      </label>
                    </span>
                    <span className="a_control">
                      <input
                        type="radio"
                        name="admin-account"
                        className="form_control_radio"
                        id="admin-account-no"
                        value="false"
                        checked={isCompanyRegistered === "false"}
                        // onChange={(e) => setIsCompanyRegistered(e.target.value)}
                        onChange={blankRegisteredCompanyFieldHandler}
                        disabled={navigationResponse !== null ? true : false}
                      />
                      <label className="form_label" htmlFor="admin-account-no">
                        No
                      </label>
                    </span>
                  </div>
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Registration No.
                    <span
                      className={
                        isCompanyRegistered === "true"
                          ? "label_mandatory"
                          : "fade_label_mandatory"
                      }
                    >
                      *
                    </span>
                    <Tooltip text="Enter company registration number." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    value={registrationNo}
                    onChange={(e) => setRegistrationNo(e.target.value)}
                    disabled={
                      navigationResponse != null
                        ? true
                        : isCompanyRegistered === "false"
                        ? true
                        : false
                    }
                  />
                  {errors &&
                    registrationNo.trim().length === 0 &&
                    isCompanyRegistered === "true" && (
                      <span className="error">
                        Registration number is required.
                      </span>
                    )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Registered On
                    <span
                      className={
                        isCompanyRegistered === "true"
                          ? "label_mandatory"
                          : "fade_label_mandatory"
                      }
                    >
                      *
                    </span>
                    <Tooltip text="Enter the date when company was registered." />
                  </label>
                  {isCompanyRegistered === "false" ? (
                    <input
                      type="text"
                      disabled
                      className="my_form_control"
                      placeholder="Months and years"
                    />
                  ) : navigationResponse != null ? (
                    <input
                      type="text"
                      className="form_control"
                      value={registeredOn}
                      disabled={
                        navigationResponse != null
                          ? true
                          : isCompanyRegistered === "false"
                          ? true
                          : false
                      }
                    />
                  ) : (
                    <Flatpickr
                      className={
                        navigationResponse !== null
                          ? "form_control_flatpickr_disabled"
                          : "form_control_flatpickr"
                      }
                      value={registeredOn}
                      onChange={(date) => setRegisteredOn(date)}
                      options={{
                        maxDate: "today",
                        minDate: "1-1-1950",
                        altInput: true,
                        altFormat: "F j, Y",
                        dateFormat: "Y-m-d",
                      }}
                    />
                  )}
                  <span
                    className="icon_container"
                    onClick={() => setIsShowCalendar(!isShowCalendar)}
                  >
                    <img src={calendarIcon} alt="calendar-icon" />
                  </span>
                  {errors &&
                    isCompanyRegistered === "true" &&
                    registeredOn.length === 0 && (
                      <span className="error">
                        Please enter date of company registration.
                      </span>
                    )}
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Country of Registration
                    <span
                      className={
                        isCompanyRegistered === "true"
                          ? "label_mandatory"
                          : "fade_label_mandatory"
                      }
                    >
                      *
                    </span>
                  </label>
                  {isCompanyRegistered === "false" ? (
                    <input
                      type="text"
                      disabled
                      className="my_form_control"
                      placeholder=" "
                    />
                  ) : navigationResponse != null ? (
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
                        {navigationResponse?.country_of_registration}
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
                          zIndex: 10,
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
                  {errors &&
                    isCompanyRegistered === "true" &&
                    countryOfRegistration.length === 0 && (
                      <span className="error">
                        Please select country of registration.
                      </span>
                    )}
                </div>

                <div className="form_control_area address_margin">
                  <label className="form_label"> City of Registration</label>
                  {isCompanyRegistered === "false" ? (
                    <input
                      type="text"
                      disabled
                      className="my_form_control"
                      placeholder=" "
                    />
                  ) : (
                    <input
                      type="text"
                      className="form_control"
                      value={cityOfRegistration}
                      autocomplete="nope"
                      onChange={(e) => {
                        setCityOfRegistration(e.target.value);
                      }}
                      disabled={navigationResponse != null ? true : false}
                    />
                  )}

                  {/* {
                  errors && isCompanyRegistered === "true" && (cityOfRegistration.length === 0) &&(
                    <span className="error">
                      City is required.
                    </span>
                  )
                } */}
                </div>
                {(isCompanyNotRegistered || establishedOn) && (
                  <>
                    <div className="form_control_spread">
                      {/* <label className="form_label">
                        Is your Company established in last five years?
                      </label> */}

                      {/* <div className="radio_container">
                        <span className="a_control">
                          <input
                            type="radio"
                            name="a_admin-account-five-yes"
                            className="form_control_radio"
                            id="a_admin-account-five-yes"
                            value="true"
                            checked={isEstablishedInFiveYears === "true"}
                            // onChange={(e) =>
                            //   setIsEstablishedInFiveYears(e.target.value)
                            // }
                            onChange={blankEstablisedOnHandler}
                            // disabled={navigationResponse !== null ? true : false}
                            disabled={
                              isCompanyRegistered === "true" ? true : false
                            }
                          />
                          <label
                            className="form_label"
                            htmlFor="a_admin-account-five-yes"
                          >
                            Yes
                          </label>
                        </span>
                        <span className="a_control">
                          <input
                            type="radio"
                            name="a_admin-account-five-yes"
                            className="form_control_radio"
                            id="a_admin-account-five-no"
                            value="false"
                            checked={isEstablishedInFiveYears === "false"}
                            // onChange={(e) =>
                            //   setIsEstablishedInFiveYears(e.target.value)
                            // }
                            onChange={blankEstablisedOnHandler}
                            disabled={
                              navigationResponse !== null ? true : false
                            }
                          />
                          <label
                            className="form_label"
                            htmlFor="a_admin-account-five-no"
                          >
                            No
                          </label>
                        </span>
                      </div> */}
                    </div>

                    <div className="form_control_area">
                      <label className="form_label">
                        Company Established On
                        <span
                          className={
                            isEstablishedInFiveYears === "true"
                              ? "label_mandatory"
                              : "fade_label_mandatory"
                          }
                        >
                          *
                        </span>
                      </label>
                      {isCompanyRegistered === "false" &&
                      isEstablishedInFiveYears === "true" ? (
                        <Flatpickr
                          className="form_control_flatpickr"
                          // placeholder="Months and years"
                          value={establishedOn}
                          onChange={(date) => setEstablishedOn(date)}
                          options={{
                            maxDate: "today",
                            minDate: "1-1-1950",
                            altInput: true,
                            altFormat: "F j, Y",
                            dateFormat: "Y-m-d",
                          }}
                          disabled={navigationResponse !== null ? true : null}
                        />
                      ) : (
                        <input
                          type="text"
                          disabled
                          className="form_control"
                          value={establishedOn}
                          // placeholder="Months and years"
                        />
                      )}
                      <span className="icon_container">
                        <img src={calendarIcon} alt="calendar-icon" />
                      </span>
                      {errorsEstablished &&
                        establishedOn.length === 0 &&
                        isEstablishedInFiveYears === "true" && (
                          <span className="error">
                            Established date is required.
                          </span>
                        )}
                    </div>
                  </>
                )}
                <div className="form_submit_container">
                  {(businessInfoResponse && navigationCheck === false) ||
                  (registrationStatusKey - 3 > 0 &&
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
      </div>
    </div>
  );
};

export default BusinessInformation;
