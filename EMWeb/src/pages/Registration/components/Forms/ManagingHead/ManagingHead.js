import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import Select from "react-select";
import ConfirmationAddNewData from "../../Modals/confirmationAddNewData/confirmationAddNewData";
import "./ManagingHead.scss";
import Loading from "../../../../../components/Loading/Loading";
import ConfirmationModal from "./../../Modals/ConfirmationModal/ConfirmationModal";
import iRedIcon from "./../../../../../assets/images/i-red-icon.png";
import Tooltip from "./../../../../../components/Tooltip/Tooltip";
import cancel from "./../../../../../assets/images/cancel.png";
import {
  emailValidation,
  numberValidation,
} from "../../../../../utils/helpers";
import { ToastError } from "./../../../../../components/Tostify";
import {
  validateEmailPhone,
  validateEmailPhoneActions,
} from "../../../../../store/vendorRegistration/validateEmailPhone";
import {
  getApproverList,
  saveManagingHead,
  saveManagingHeadSlice,
} from "./../../../../../store/vendorRegistration/managingHead";
import { getCompanyId } from "../../../../../config";
import { getAvailableOn } from "./../../../../../store/common/availableOn";
import {
  commonGetAPiCall,
  commonGetApiActions,
} from "../../../../../store/common/commonGetApi";

const ManagingHead = (props) => {
  const dispatch = useDispatch();

  let isFormTypePartner = props.selectedUser === "partner" ? true : false;
  let isFormTypeCustomer = props.selectedUser === "customer" ? true : false;

  const countries = useSelector((state) => state.countries.countries);
  const isCountryError = useSelector((state) => state.countries.isError);
  const approverList = useSelector(
    (state) => state.managingHeadReducer.approverList
  );
  const isErrorApproverList = useSelector(
    (state) => state.managingHeadReducer.isError
  );
  const managingHeadRes = useSelector(
    (state) => state.managingHeadReducer.managingHeadRes
  );
  const navigationCheck = useSelector(
    (state) => state.managingHeadReducer.navigationCheck
  );
  const isErrormanagingHeadRes = useSelector(
    (state) => state.managingHeadReducer.isError
  );
  const isLoading = useSelector((state) => state.managingHeadReducer.isLoading);
  const availableOnRes = useSelector(
    (state) => state.availableOnReducer.availableOnRes
  );
  const IsErroravailableOnRes = useSelector(
    (state) => state.availableOnReducer.isError
  );
  const isAvailableOnLoading = useSelector(
    (state) => state.availableOnReducer.isLoading
  );
  const navigationResponse = useSelector(
    (state) => state.commonGetApiReducer.getApiRes
  );

  const initialValues = {
    approverModel: [
      // { id: 170 },
      // { id: 170 }
    ],
    customerApproverModel: {
      isApprovedStatus: "PENDING",
    },
    userModel: {
      name: "",
      email: "",
      primaryPhone: "",
      phoneCode: "",
      onWhatsapp: false,
      availableOn: "",
      designation: "",
      roleModel: [
        {
          id: 5,
        },
      ],
      companyId: Number(getCompanyId()),
    },
  };

  const [formValues, setFormValues] = useState(initialValues);
  const [managingHead, setManagingHead] = useState([]);
  const [managingHeadCount, setManagingHeadCount] = useState([1]);
  const [activeManagingHead, setactiveManagingHead] = useState(1);
  const [isSaveBtnActive, setIsSaveBtnActive] = useState(false);
  const [managingHeadList, setManagingHeadList] = useState([]);
  const [isUserClickedRemoved, setIsUserClickedRemoved] = useState(false);
  const [
    isAddManagingHeadandSaveBtnActive,
    setisAddManagingHeadandSaveBtnActive,
  ] = useState(true);
  const [lastManagingHeadBtnStatus, setlastManagingHeadBtnStatus] =
    useState(false);
  const [lastManagingHeadFilledData, setlastManagingHeadFilledData] =
    useState(initialValues);
  const [errors, setErrors] = useState(false);
  const [isConfirmationModal, setIsConfirmationModal] = useState(false);
  const [hide, setHide] = useState();
  const [isConfirmationAddNewData, setIsConfirmationAddNewData] =
    useState(false);

  useEffect(() => {
    dispatch(getAvailableOn());
    dispatch(getApproverList());
  }, [dispatch]);

  useEffect(() => {
    if (navigationCheck) {
      if (managingHeadRes !== null) {
        props.setFormLabel("finance", 3);
      }
    }
  }, [managingHeadRes, props, navigationCheck]);

  useEffect(() => {
    if (navigationResponse !== null && Array.isArray(navigationResponse)) {
      setManagingHead(navigationResponse);
      setFormValues(navigationResponse[navigationResponse.length - 1]);
      let bidderCount = navigationResponse.map((item, index) => index + 1);
      setManagingHeadCount(bidderCount);
      setactiveManagingHead(navigationResponse.length);
    }
  }, [navigationResponse]);

  useEffect(() => {
    if (!navigationCheck && managingHeadRes !== null && isFormTypeCustomer) {
      dispatch(commonGetAPiCall(`/customer/getAllHead/${getCompanyId()}`));
    }
  }, [dispatch, navigationCheck, managingHeadRes, isFormTypeCustomer]);

  useEffect(() => {
    if (isCountryError !== "") {
      ToastError(isCountryError);
    } else if (isErrorApproverList !== "") {
      ToastError(isErrorApproverList);
    } else if (isErrormanagingHeadRes !== "") {
      ToastError(isErrormanagingHeadRes);
    } else if (IsErroravailableOnRes !== "") {
      ToastError(IsErroravailableOnRes);
    }
  }, [
    isCountryError,
    isErrorApproverList,
    isErrormanagingHeadRes,
    IsErroravailableOnRes,
  ]);

  const [approverClone, setApproverClone] = useState([]);
  useEffect(() => {
    setApproverClone(approverList);
  }, [approverList]);

  function onSelectmanagingApprover(selectedOptions) {
    if (selectedOptions.length < formValues.approverModel.length) {
      let remainingApprover = formValues.approverModel.filter(function (
        objFromA
      ) {
        return !selectedOptions.find(function (objFromB) {
          return objFromA.id === objFromB.id;
        });
      });
      setApproverClone(remainingApprover);
      const currentSelectedApprover = selectedOptions.map((approver) => ({
        id: approver.id,
        name: approver.name,
      }));
      setFormValues({
        ...formValues,
        userModel: {
          ...formValues.userModel,
        },
        approverModel: currentSelectedApprover,
      });
    } else {
      let remainingApprover = approverClone.filter(function (objFromA) {
        return !selectedOptions.find(function (objFromB) {
          return objFromA.id === objFromB.id;
        });
      });
      setApproverClone(remainingApprover);
      const currentSelectedApprover = selectedOptions.map((approver) => ({
        id: approver.id,
        name: approver.name,
      }));
      setFormValues({
        ...formValues,
        userModel: {
          ...formValues.userModel,
        },
        approverModel: currentSelectedApprover,
      });
    }
  }

  const validateForm = () => {
    if (
      formValues.userModel.name.trim() &&
      formValues.userModel.email.trim() &&
      formValues.userModel.primaryPhone.trim() &&
      formValues.userModel.primaryPhone.trim().length > 5 &&
      formValues.userModel.primaryPhone.trim().length < 14 &&
      formValues.userModel.phoneCode &&
      emailValidation(formValues.userModel.email.trim())
    ) {
      setErrors(false);
      return true;
    } else {
      setErrors(true);
      return false;
    }
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

  const handleInputChange = (e, index) => {
    const { name, value } = e.target;
    switch (name) {
      case "name":
        if (!Number(e.nativeEvent.data))
          setFormValues({
            ...formValues,
            userModel: { ...formValues.userModel, name: value },
          });
        break;

      case "email":
        setFormValues({
          ...formValues,
          userModel: { ...formValues.userModel, email: value.trim() },
        });
        break;

      case "designation":
        if (!Number(e.nativeEvent.data))
          setFormValues({
            ...formValues,
            userModel: { ...formValues.userModel, designation: value },
          });
        break;

      case "primaryPhone":
        if (value.length < 14) {
          setFormValues({
            ...formValues,
            userModel: { ...formValues.userModel, primaryPhone: value.trim() },
          });
        }
        break;

      case "phoneCode":
        setFormValues({
          ...formValues,
          userModel: { ...formValues.userModel, phoneCode: value.trim() },
        });
        break;

      case "onWhatsapp":
        setFormValues({
          ...formValues,
          userModel: {
            ...formValues.userModel,
            onWhatsapp: value.trim() == "No" ? false : true,
          },
        });
        break;

      case "availableOn":
        setFormValues({
          ...formValues,
          userModel: { ...formValues.userModel, availableOn: value.trim() },
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

  const handleFormSubmission = (event) => {
    event.preventDefault();
    if (validateForm()) {
      setIsConfirmationModal(!isConfirmationModal);
    }
  };

  const apiCall = () => {
    if (managingHead.length === 0 && isFormTypeCustomer && validateForm()) {
      let managingHeadClone = { ...formValues };

      if (managingHeadClone["userModel"]["onWhatsapp"] === true) {
        managingHeadClone["userModel"]["availableOn"] = "whatsapp";
      }
      const approverModel = managingHeadClone.approverModel.map((approver) => {
        return { id: approver.id };
      });

      const managingHeadList = {
        approverModel: approverModel,
        customerApproverModel: managingHeadClone.customerApproverModel,
        userModel: managingHeadClone.userModel,
      };
      if (isFormTypeCustomer) {
        managingHeadList["type"] = "Customer";
      }
      dispatch(saveManagingHead([managingHeadList], "customer/addHead"));
    } else if (isFormTypeCustomer && validateForm()) {
      let managingHeadClone = [...managingHead];

      const transformedManagingHeadList = managingHeadClone.map((approver) => {
        if (approver["userModel"]["onWhatsapp"] === true) {
          approver["userModel"]["availableOn"] = "whatsapp";
        }
        approver.approverModel = approver.approverModel.map((approver) => {
          return { id: approver.id };
        });
        return approver;
      });
      if (isFormTypeCustomer) {
        transformedManagingHeadList["type"] = "Customer";
      }
      dispatch(
        saveManagingHead(transformedManagingHeadList, "customer/addHead")
      );
      return false;
    }
  };
  const saveData = (flag) => {
    if (flag === "already-exists") {
      let approvers = managingHead.slice();
      approvers[activeManagingHead - 1] = formValues;
      setManagingHead(approvers);
    } else {
      setManagingHead([...managingHead, formValues]);
    }
  };

  const onCancelModal = () => {
    setIsConfirmationAddNewData(!isConfirmationAddNewData);
  };

  const addManagingHead = () => {
    if (!validateForm()) return false;
    setIsConfirmationAddNewData(!isConfirmationAddNewData);
  };

  const onConfirm = () => {
    setIsConfirmationAddNewData(!isConfirmationAddNewData);
    setlastManagingHeadBtnStatus(false);
    if (activeManagingHead === 1) {
      saveData();
      setManagingHeadCount([
        ...managingHeadCount,
        Number(managingHeadCount.length) + 1,
      ]);
      setactiveManagingHead(activeManagingHead + 1);
      setIsSaveBtnActive(true);
      setisAddManagingHeadandSaveBtnActive(false);
      setFormValues(initialValues);
    } else {
      setManagingHeadCount([
        ...managingHeadCount,
        Number(managingHeadCount.length) + 1,
      ]);
      setactiveManagingHead(activeManagingHead + 1);
      setIsSaveBtnActive(true);
      setisAddManagingHeadandSaveBtnActive(false);
      setFormValues(initialValues);
    }
  };

  const managingHeadSaveHandler = () => {
    if (!validateForm()) return false;

    if (activeManagingHead < managingHeadCount.length) {
      saveData("already-exists");
      if (lastManagingHeadBtnStatus === true) {
        setFormValues({ ...managingHead[managingHeadCount.length - 1] });
        setactiveManagingHead(managingHeadCount.length);
        setIsSaveBtnActive(false);
        setisAddManagingHeadandSaveBtnActive(true);
      } else {
        setactiveManagingHead(managingHeadCount.length);
        setIsSaveBtnActive(true);
        setisAddManagingHeadandSaveBtnActive(false);
        setFormValues(lastManagingHeadFilledData);
      }
    } else if (activeManagingHead === managingHeadCount.length) {
      saveData();
      setIsSaveBtnActive(false);
      setisAddManagingHeadandSaveBtnActive(true);
      if (activeManagingHead === 1) {
        setlastManagingHeadBtnStatus(false);
      } else {
        setlastManagingHeadBtnStatus(true);
      }
    }
  };

  const makeManagingHeadActive = (activeManagingHeadSerial) => {
    if (activeManagingHead === managingHeadCount.length) {
      if (navigationResponse != null) {
        setlastManagingHeadFilledData(managingHead[managingHead.length - 1]);
      } else {
        setlastManagingHeadFilledData(formValues);
      }
    }
    setactiveManagingHead((prevState) => {
      innerApproverActive(activeManagingHeadSerial);
      return activeManagingHeadSerial;
    });
  };

  const innerApproverActive = (approverSerial) => {
    if (approverSerial === managingHeadCount.length) {
      setFormValues(lastManagingHeadFilledData);
      if (lastManagingHeadBtnStatus === true) {
        setIsSaveBtnActive(false);
        setisAddManagingHeadandSaveBtnActive(true);
      } else {
        setIsSaveBtnActive(true);
        setisAddManagingHeadandSaveBtnActive(false);
      }
    } else {
      setFormValues({ ...managingHead[approverSerial - 1] });
      setIsSaveBtnActive(true);
      setisAddManagingHeadandSaveBtnActive(false);
    }
  };

  const nextNavigationHandler = () => {
    dispatch(saveManagingHeadSlice.managingHeadNavigation(true));
    dispatch(commonGetApiActions.dispatchGetRequest());
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
  };

  const removeHead = () => {
    setactiveManagingHead(activeManagingHead - 1);
    const trasformedHeadCount = [...managingHeadCount];
    trasformedHeadCount.splice(managingHeadCount.length - 1, 1);
    setManagingHeadCount(trasformedHeadCount);
    setFormValues(managingHead[managingHead.length - 1]);
    const transformedBidderList = [...managingHead];
    transformedBidderList.pop();
    setManagingHead(transformedBidderList);
    setIsUserClickedRemoved(true);
  };

  return (
    <>
      <div className="form_container_area">
        <div className="managing_head_form positioning_container">
          {(isLoading || isAvailableOnLoading) && <Loading text="Loading..." />}
          {isConfirmationModal && (
            <ConfirmationModal cancel={onCancelHandler} ok={onOkHandler} />
          )}
          {isConfirmationAddNewData && (
            <ConfirmationAddNewData
              cancel={onCancelModal}
              ok={onConfirm}
              name={"Managing Head"}
            />
          )}

          {isFormTypeCustomer && (
            <>
              {/* <button
                onClick={() =>
                  console.log({
                    managingHead,
                    formValues,
                    managingHeadCount,
                    activeManagingHead,
                  })
                }
              >
                get state
              </button> */}
              <div className="nav_container">
                <div className="nav_items">
                  <div
                    className="nav_item active"
                    onClick={() =>
                      props.navigationHandler({ currentForm: "bidder" })
                    }
                  >
                    Requester
                  </div>
                  <div className="horizotal_row_container">
                    <span className="horizontal_row active">&nbsp;</span>
                  </div>
                  <div
                    className="nav_item active"
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
                    <span className="horizontal_row active">&nbsp;</span>
                  </div>
                  <div className="nav_item active">Managing Head</div>
                </div>
              </div>

              <div className="add_bidder_container">
                {managingHeadCount?.map((el, index) => {
                  if (activeManagingHead === index + 1) {
                    return (
                      <div className="bidder active" key={index}>
                        <span className="bidder_text"> M{index + 1} </span>
                        {navigationResponse === null &&
                          activeManagingHead === managingHeadCount.length &&
                          activeManagingHead !== 1 &&
                          !isAddManagingHeadandSaveBtnActive && (
                            <span className="remove_head">
                              <img
                                src={cancel}
                                alt="cancel"
                                onClick={removeHead}
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
                        onClick={() => makeManagingHeadActive(index + 1)}
                      >
                        <span className="bidder_text"> M{index + 1} </span>
                      </div>
                    );
                  }
                })}
              </div>

              <form onSubmit={handleFormSubmission}>
                <div className="form_container">
                  <div className="form_control_area">
                    <label className="form_label">
                      Name <span className="label_mandatory">*</span>
                      <Tooltip text="Name." />
                    </label>
                    <input
                      type="text"
                      className="form_control"
                      name="name"
                      value={formValues?.userModel?.name}
                      onChange={handleInputChange}
                      disabled={navigationResponse != null ? true : false}
                    />
                    {errors && formValues?.userModel?.name.trim() === "" && (
                      <span className="error">Enter name</span>
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
                      disabled={navigationResponse != null ? true : false}
                    />
                  </div>

                  <div className="form_control_area">
                    <label className="form_label">
                      Email <span className="label_mandatory">*</span>
                      <Tooltip text="Email." />
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
                      disabled={navigationResponse != null ? true : false}
                    />
                    {errors &&
                      (formValues?.userModel?.email.trim() === "" ||
                        !emailValidation(
                          formValues?.userModel?.email.trim()
                        )) && <span className="error">Enter Email</span>}
                  </div>

                  <div className="form_control_area">
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
                            disabled={navigationResponse != null ? true : false}
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
                        className="form_control_phone"
                        name="primaryPhone"
                        value={formValues?.userModel?.primaryPhone}
                        onChange={(e) => {
                          handleInputChange(e);
                          validatePhone(e);
                        }}
                        disabled={navigationResponse != null ? true : false}
                      />
                    </div>
                    {errors &&
                      (!numberValidation(formValues.userModel.primaryPhone) ||
                        formValues?.userModel?.phoneCode === "" ||
                        formValues?.userModel?.primaryPhone === "") && (
                        <span className="error">
                          Phone code can't be empty or phone number should be
                          between 6 to 13 digit
                        </span>
                      )}
                  </div>

                  <div className="form_control_spread">
                    <label className="form_label">
                      I am available on Whatsapp
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
                          disabled={navigationResponse != null ? true : false}
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
                          disabled={navigationResponse != null ? true : false}
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

                    <span
                      value={
                        formValues?.userModel?.onWhatsapp === "Yes"
                          ? ""
                          : formValues?.userModel?.availableOn
                      }
                      // onChange={(e) => setAvailableOn(e.value)}
                      disabled={
                        navigationResponse != null ||
                        formValues?.userModel?.onWhatsapp === true
                          ? true
                          : formValues?.userModel?.onWhatsapp === true
                          ? true
                          : false
                      }
                    >
                      {navigationResponse != null ? (
                        <select className="form_control_disable" disabled>
                          <option>{formValues?.userModel?.availableOn}</option>
                        </select>
                      ) : formValues?.userModel?.onWhatsapp === true ? (
                        <input
                          className="form_control_disable"
                          disabled
                        ></input>
                      ) : (
                        <Select
                          options={socialMedia}
                          className="form_control_enable"
                          autocomplete="nope"
                          value={{
                            value: formValues?.userModel?.availableOn,
                            label: formValues?.userModel?.availableOn,
                          }}
                          onChange={handleInputChangeAvailable}
                        ></Select>
                      )}
                    </span>
                    {errors &&
                      formValues?.userModel?.onWhatsapp === false &&
                      formValues?.userModel?.availableOn === "" && (
                        <span className="error">
                          Please select available on
                        </span>
                      )}
                  </div>

                  <div className="form_control_area">
                    <label className="form_label">
                      Approver
                      <Tooltip text="Bidder is mainly responsible for Quoting and assigning  the engineers." />
                    </label>

                    <Select
                      isMulti
                      className="requster_select"
                      getOptionLabel={(option) => option.name}
                      getOptionValue={(option) => option.id}
                      isSearchable={true}
                      value={formValues?.approverModel}
                      onChange={onSelectmanagingApprover}
                      options={approverClone}
                      isDisabled={navigationResponse != null ? true : false}
                    />
                  </div>

                  <div className="form_submit_container m-top">
                    {managingHeadRes && navigationCheck === false ? (
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
                            onClick={managingHeadSaveHandler}
                          />
                        )}
                        {isAddManagingHeadandSaveBtnActive && (
                          <>
                            <input
                              type="button"
                              value="Add Managing Head"
                              className="btn btn_add_bidder"
                              onClick={() => addManagingHead()}
                            />
                            <input
                              type="submit"
                              value="Save &amp; Next"
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
    </>
  );
};

export default ManagingHead;
