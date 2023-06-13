import React, { useState, useEffect, useRef } from "react";
import { useSelector, useDispatch } from "react-redux";
import Select from "react-select";
import { Multiselect } from "multiselect-react-dropdown";
import dropdownIcon from "./../../../../../assets/images/drop-icon.svg";
import cancelIcon from "./../../../../../assets/images/cancel_other_icon.png";
import cancel from "./../../../../../assets/images/cancel.png";
import {
  validateEmailPhone,
  validateEmailPhoneActions,
} from "../../../../../store/vendorRegistration/validateEmailPhone";
import "./Approver.scss";
import ConfirmationModal from "./../../Modals/ConfirmationModal/ConfirmationModal";
import ConfirmationAddNewData from "../../Modals/confirmationAddNewData/confirmationAddNewData";
import iIcon from "./../../../../../assets/images/i-icon.png";
import iRedIcon from "./../../../../../assets/images/i-red-icon.png";
import Loading from "./../../../../../components/Loading/Loading";
import Tooltip from "./../../../../../components/Tooltip/Tooltip";
import { ToastError } from "./../../../../../components/Tostify";
import {
  emailValidation,
  numberValidation,
} from "../../../../../utils/helpers";

import { getAvailableOn } from "./../../../../../store/common/availableOn";
import approver, {
  getBiddersList,
  getRequesterList,
  saveApprovers,
  saveApproverSlice,
} from "./../../../../../store/vendorRegistration/approver";
import {
  getAuthData,
  getCompanyId,
  getReferralCode,
} from "../../../../../config";
import {
  commonGetAPiCall,
  commonGetApiActions,
} from "../../../../../store/common/commonGetApi";
import { getAllUserDetails } from "../../../../../store/common/allUserDetails";
import { getCurrencyStatus } from "../../../../../store/vendorRegistration/currency";

const Approver = (props) => {
  let multiselectRef = React.createRef();
  const dispatch = useDispatch();

  let isFormTypePartner = props.selectedUser === "partner" ? true : false;
  let isFormTypeCustomer = props.selectedUser === "customer" ? true : false;

  const countries = useSelector((state) => state.countries.countries);
  const isCountryError = useSelector((state) => state.countries.isError);
  const bidderList = useSelector((state) => state.vendorApprover.biddersList);
  const isErrorBidderList = useSelector(
    (state) => state.vendorApprover.isError
  );
  const requesterList = useSelector(
    (state) => state.vendorApprover.requesterList
  );
  const isApproverLoading = useSelector(
    (state) => state.vendorApprover.isLoading
  );
  const isError = useSelector((state) => state.vendorApprover.isError);
  const approverResponse = useSelector(
    (state) => state.vendorApprover.approverRes
  );
  const navigationCheck = useSelector(
    (state) => state.vendorApprover.navigationCheck
  );
  const availableOnRes = useSelector(
    (state) => state.availableOnReducer.availableOnRes
  );
  const isErrorAvailableOnRes = useSelector(
    (state) => state.availableOnReducer.isError
  );
  const isAvailableOnLoading = useSelector(
    (state) => state.availableOnReducer.isLoading
  );
  const navigationResponse = useSelector(
    (state) => state.commonGetApiReducer.getApiRes
  );

  const authResponse = useSelector((state) => state.auth.authRes);
  const registrationStatusKey = useSelector(
    (state) => state.commonGetApiReducer.registrationStatusKey
  );

  const currencyCountry = useSelector(
    (state) => state.currencyCountryReducer.currencyCountryStatus
  );

  const currencyData = useSelector(
    (state) => state.currencyStatusReducer.currencyStatus
  );

  const allUserDetails = useSelector(
    (state) => state.allUserDetailsReducer.allUserDetails
  );

  // Bidder dropdown options
  const [bidderClone, setBidderClone] = useState([]);
  const [requesterClone, setRequesterClone] = useState([]);
  const [allUsersData, setAllUsersData] = useState([]);

  // Vendor initial Values
  const partnerInitialValues = {
    bidderApproverModel: {
      isApprovedStatus: "PENDING",
    },
    userModel: {
      companyId: Number(getCompanyId()),
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
          id: 4,
        },
      ],
    },
    bidderModel: [],
  };

  // Customer initial values
  const customerInitialValues = {
    customerApproverModel: {
      isApprovedStatus: "PENDING",
      status: 0,
    },
    approver: {
      companyId: Number(getCompanyId()),
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
          id: 4,
        },
      ],
      biddingLimit: "",
      approverId: null,
    },
    requesterIds: [],
  };

  const initialValues =
    isFormTypePartner === true ? partnerInitialValues : customerInitialValues;

  const [formValues, setFormValues] = useState(initialValues);
  const [approverList, setApproverList] = useState([]);
  const [approverListCount, setApproverListCount] = useState([1]);
  const [activeApprover, setActiveApprover] = useState(1);
  const [isSaveBtnActive, setIsSaveBtnActive] = useState(false);
  const [isAddApproverandSaveBtnActive, setIsAddApproverandSaveBtnActive] =
    useState(true);
  const [lastApproverBtnStatus, setLastApproverBtnStatus] = useState(false);
  const [lastApproverFilledData, setLastApproverFilledData] =
    useState(initialValues);
  const [errors, setErrors] = useState(false);
  const [isConfirmationModal, setIsConfirmationModal] = useState(false);
  const [isConfirmationAddNewData, setIsConfirmationAddNewData] =
    useState(false);
  const [hide, setHide] = useState();
  const [sameAsAdmin, setSameAsAdmin] = useState([""]);
  const [dataResponse, setDataResponse] = useState(false);
  const [isUserClickedRemoved, setIsUserClickedRemoved] = useState(false);

  useEffect(() => {
    setBidderClone(bidderList);
  }, [bidderList]);

  useEffect(() => {
    if (
      navigationResponse !== null &&
      isFormTypePartner &&
      Array.isArray(navigationResponse)
    ) {
      setApproverList(navigationResponse);
      setFormValues(navigationResponse?.[navigationResponse.length - 1]);
      let bidderCount = navigationResponse?.map((item, index) => index + 1);
      setApproverListCount(bidderCount);
      setActiveApprover(navigationResponse.length);
    } else if (
      navigationResponse !== null &&
      isFormTypeCustomer &&
      Array.isArray(navigationResponse)
    ) {
      // do here
      setApproverList(navigationResponse);
      setFormValues(navigationResponse?.[navigationResponse.length - 1]);
      let bidderCount = navigationResponse?.map((item, index) => index + 1);
      setApproverListCount(bidderCount);
      setActiveApprover(navigationResponse.length);
    }
  }, [navigationResponse, isFormTypePartner, isFormTypeCustomer]);

  useEffect(() => {
    setRequesterClone(requesterList);
  }, [requesterList]);

  function onSelectApproverBidder(selectedOptions) {
    if (selectedOptions.length < formValues.bidderModel.length) {
      let remainingBidders = formValues.bidderModel.filter(function (objFromA) {
        return !selectedOptions.find(function (objFromB) {
          return objFromA.id === objFromB.id;
        });
      });

      let finalSection = [];
      for (let k = 0; k < approverList.length; k++) {
        finalSection.push(...approverList[k]["bidderModel"]);
      }

      let a1 = bidderList;
      let a2 = [
        ...finalSection,
        ...formValues.bidderModel,
        ...lastApproverFilledData.bidderModel,
      ];

      const uniqueArray = [
        ...new Set(a2.map((item) => JSON.stringify(item))),
      ].map((string) => JSON.parse(string));

      let a3 = uniqueArray;
      let flag = false;
      let remainingBiddersdata = [];
      for (let i = 0; i < a1.length; i++) {
        for (let j = 0; j < a3.length; j++) {
          if (a1[i]["id"] === a3[j]["id"]) {
            flag = true;
          }
        }
        if (flag === false) {
          remainingBiddersdata.push(a1[i]);
        }
        flag = false;
      }

      setBidderClone(remainingBiddersdata);
      const currentSelectedBidder = selectedOptions.map((bidder) => ({
        id: bidder.id,
        name: bidder.name,
      }));
      setFormValues({
        ...formValues,
        userModel: {
          ...formValues.userModel,
        },
        bidderModel: currentSelectedBidder,
      });
    } else {
      let remainingBidders = bidderClone.filter(function (objFromA) {
        return !selectedOptions.find(function (objFromB) {
          return objFromA.id === objFromB.id;
        });
      });
      setBidderClone(remainingBidders);
      const currentSelectedBidder = selectedOptions.map((bidder) => ({
        id: bidder.id,
        name: bidder.name,
      }));
      setFormValues({
        ...formValues,
        userModel: {
          ...formValues.userModel,
        },
        bidderModel: currentSelectedBidder,
      });
    }
  }

  const onSelectBidder = (selectedList, selectedItem) => {
    setFormValues({
      ...formValues,
      userModel: {
        ...formValues.userModel,
      },
      bidderModel: selectedList,
    });

    let finalSection = [];
    for (let k = 0; k < approverList.length; k++) {
      finalSection.push(...approverList[k]["bidderModel"]);
    }

    let a1 = bidderList;
    let a2 = [...finalSection];

    const uniqueArray = [
      ...new Set(a2.map((item) => JSON.stringify(item))),
    ].map((string) => JSON.parse(string));

    let a3 = uniqueArray;
    let flag = false;
    let remainingBiddersdata = [];
    for (let i = 0; i < a1.length; i++) {
      for (let j = 0; j < a3.length; j++) {
        if (a1[i]["id"] === a3[j]["id"]) {
          flag = true;
        }
      }
      if (flag === false) {
        remainingBiddersdata.push(a1[i]);
      }
      flag = false;
    }

    setBidderClone(remainingBiddersdata);
  };
  const onBidderRemove = (selectedList, selectedItem) => {
    setFormValues({
      ...formValues,
      userModel: {
        ...formValues.userModel,
      },
      bidderModel: selectedList,
    });
  };

  // requestor select function
  function onSelectApproverRequestor(selectedOptions) {
    if (selectedOptions.length < formValues.requesterIds.length) {
      let remainingBidders = formValues.requesterIds.filter(function (
        objFromA
      ) {
        return !selectedOptions.find(function (objFromB) {
          return objFromA.id === objFromB.id;
        });
      });
      setRequesterClone(remainingBidders);
      const currentSelectedBidder = selectedOptions.map((bidder) => ({
        id: bidder.id,
        name: bidder.name,
      }));
      setFormValues({
        ...formValues,
        userModel: {
          ...formValues.userModel,
        },
        requesterIds: currentSelectedBidder,
      });
    } else {
      let remainingBidders = requesterClone.filter(function (objFromA) {
        return !selectedOptions.find(function (objFromB) {
          return objFromA.id === objFromB.id;
        });
      });
      setRequesterClone(remainingBidders);
      const currentSelectedBidder = selectedOptions.map((bidder) => ({
        id: bidder.id,
        name: bidder.name,
      }));
      setFormValues({
        ...formValues,
        approver: {
          ...formValues.approver,
        },
        requesterIds: currentSelectedBidder,
      });
    }
  }

  // Requester dropdown options

  useEffect(() => {
    dispatch(getAvailableOn());
    dispatch(getAllUserDetails());
    dispatch(getCurrencyStatus());
  }, [dispatch]);

  useEffect(() => {
    setSameAsAdmin(allUserDetails);
  }, [allUserDetails]);

  useEffect(() => {
    if (isFormTypePartner) {
      dispatch(getBiddersList());
    } else if (isFormTypeCustomer) {
      dispatch(getRequesterList());
    }
  }, [dispatch, isFormTypePartner, isFormTypeCustomer]);

  useEffect(() => {
    if (navigationCheck) {
      if (approverResponse !== null) {
        if (isFormTypePartner) {
          props.setFormLabel("engineer", 2);
        } else if (isFormTypeCustomer) {
          props.setFormLabel("managingHead", 2);
        }
      }
    }
  }, [
    approverResponse,
    isFormTypePartner,
    isFormTypeCustomer,
    props,
    navigationCheck,
  ]);

  const getCompanyIdCheck = () => {
    if (getAuthData()?.companyId) {
      return getAuthData()?.companyId;
    }
    return getCompanyId();
  };

  useEffect(() => {
    if (registrationStatusKey - 5 > 0 && isFormTypePartner) {
      dispatch(
        commonGetAPiCall(`/vendor/getVendorApprover/${getCompanyIdCheck()}`)
      );
    } else if (registrationStatusKey - 5 > 0 && isFormTypeCustomer) {
      dispatch(
        commonGetAPiCall(
          `/customer/approver/getByCompanyId/${getCompanyIdCheck()}`
        )
      );
    }
  }, [registrationStatusKey, isFormTypePartner, isFormTypeCustomer]);

  useEffect(() => {
    if (!navigationCheck && approverResponse !== null && isFormTypePartner) {
      dispatch(commonGetAPiCall(`/vendor/getVendorApprover/${getCompanyId()}`));
    } else if (
      !navigationCheck &&
      approverResponse !== null &&
      isFormTypeCustomer
    ) {
      dispatch(
        commonGetAPiCall(`/customer/approver/getByCompanyId/${getCompanyId()}`)
      );
    }
  }, [
    dispatch,
    navigationCheck,
    approverResponse,
    isFormTypePartner,
    isFormTypeCustomer,
  ]);

  const handleFormSubmission = (event) => {
    event.preventDefault();

    if (validateForm()) {
      setIsConfirmationModal(!isConfirmationModal);
    }
  };

  const checkUniqueApprover = () => {
    let returnedEmailData = approverList.filter(
      (el) => el.userModel?.email === formValues.email
    );
    let returnedPhoneData = approverList.filter(
      (el) => el.userModel?.primaryPhone === formValues.primaryPhone
    );

    let emailFlag = false,
      phoneFlag = false;
    for (let i = 0; i < approverList.length; i++) {
      if (i === activeApprover - 1) {
      } else {
        if (
          approverList[i]?.userModel?.email === formValues?.userModel?.email &&
          approverList[i]?.userModel?.primaryPhone ===
            formValues?.userModel?.primaryPhone
        ) {
          emailFlag = true;
          phoneFlag = true;
          // } else if (
          //   approverList[i].approver.email === formValues.approver.email &&
          //   approverList[i].approver.primaryPhone ===
          //     formValues.approver.primaryPhone
          // ) {
          emailFlag = true;
          phoneFlag = true;
        } else if (
          approverList[i]?.userModel?.email === formValues?.userModel?.email
        ) {
          emailFlag = true;
        } else if (
          approverList[i]?.userModel?.primaryPhone ===
          formValues?.userModel?.primaryPhone
        ) {
          phoneFlag = true;
        }
      }
    }

    if (emailFlag && phoneFlag) {
      ToastError(`Duplicate Approver`);
      // ToastError(`Approver email'id & Mobile no. already entered!`);
      return false;
    } else if (emailFlag) {
      ToastError(`Duplicate Approver`);
      // ToastError(`Approver email'id already entered!`);
      return false;
    } else if (phoneFlag) {
      ToastError(`Duplicate Approver`);
      // ToastError(`Approver Mobile no. already entered!`);
      return false;
    } else {
      return true;
    }
  };

  const validateForm = () => {
    if (isFormTypePartner) {
      if (
        formValues.userModel.name.trim() &&
        formValues.userModel.email.trim() &&
        formValues.userModel.primaryPhone.length > 5 &&
        formValues.userModel.primaryPhone.length < 14 &&
        formValues.userModel.phoneCode &&
        ((formValues.userModel.onWhatsapp === false &&
          formValues.userModel.availableOn !== "") ||
        (formValues.userModel.onWhatsapp === true &&
          formValues.userModel.availableOn === "")
          ? true
          : false) &&
        emailValidation(formValues.userModel.email.trim())
      ) {
        setErrors(false);
        return true;
      } else {
        setErrors(true);
        return false;
      }
    } else if (isFormTypeCustomer) {
      if (
        formValues?.approver?.name.trim() &&
        formValues?.approver?.email.trim() &&
        formValues?.approver?.primaryPhone.trim() &&
        formValues?.approver?.primaryPhone.length > 5 &&
        formValues?.approver?.primaryPhone.length < 14 &&
        formValues?.approver?.phoneCode &&
        ((formValues.approver.onWhatsapp === false &&
          formValues.approver.availableOn !== "") ||
        (formValues.approver.onWhatsapp === true &&
          formValues.approver.availableOn === "")
          ? true
          : false) &&
        emailValidation(formValues.approver.email.trim())
      ) {
        setErrors(false);
        return true;
      } else {
        setErrors(true);
        return false;
      }
    }
  };
  const handlePartnerInputChangePhone = (e) => {
    setFormValues({
      ...formValues,
      userModel: { ...formValues.userModel, phoneCode: e.value },
    });
  };
  const handlePartnerInputChangeAvailable = (e) => {
    setFormValues({
      ...formValues,
      userModel: { ...formValues.userModel, availableOn: e.value },
    });
  };

  const handlePartnerInputChange = (e) => {
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
          userModel: { ...formValues.userModel, email: value.trim() },
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

      case "onWhatsapp":
        setFormValues({
          ...formValues,
          userModel: {
            ...formValues.userModel,
            onWhatsapp: value.trim() === "No" ? false : true,
            availableOn: "",
          },
        });
        break;

      case "availableOn":
        setFormValues({
          ...formValues,
          userModel: { ...formValues.userModel, availableOn: value.trim() },
        });
        break;

      case "phoneCode":
        setFormValues({
          ...formValues,
          userModel: { ...formValues.userModel, phoneCode: value.trim() },
        });
        break;

      case "sameAsBidder":
        setErrors(false);
        setFormValues({
          ...formValues,
          bidderModel: [],
          userModel: {
            ...formValues.userModel,
            availableOn: "",
            designation: "",
            email: "",
            name: "",
            onWhatsapp: false,
            phoneCode: "",
            primaryPhone: "",
            roleModel: { id: value.trim() === "yes" ? 7 : 4 },
          },
        });
        break;

      default:
      // do nothing
    }
  };

  const handleCustomerInputChangePhone = (e) => {
    setFormValues({
      ...formValues,
      approver: { ...formValues.approver, phoneCode: e.value },
    });
  };
  const handleCustomerInputChangeAvailable = (e) => {
    setFormValues({
      ...formValues,
      approver: { ...formValues.approver, availableOn: e.value },
    });
  };

  const handleCustomerInputChange = (e) => {
    const { name, value } = e.target;

    switch (name) {
      case "name":
        if (!Number(e.nativeEvent.data))
          setFormValues({
            ...formValues,
            approver: { ...formValues?.approver, name: value },
          });
        break;

      case "designation":
        if (!Number(e.nativeEvent.data))
          setFormValues({
            ...formValues,
            approver: { ...formValues?.approver, designation: value },
          });
        break;

      case "email":
        setFormValues({
          ...formValues,
          approver: { ...formValues.approver, email: value.trim() },
        });
        break;

      case "primaryPhone":
        setFormValues({
          ...formValues,
          approver: { ...formValues.approver, primaryPhone: value.trim() },
        });
        break;

      case "phoneCode":
        setFormValues({
          ...formValues,
          approver: { ...formValues.approver, phoneCode: value.trim() },
        });
        break;

      case "onWhatsapp":
        setFormValues({
          ...formValues,
          approver: {
            ...formValues.approver,
            onWhatsapp: value.trim() == "No" ? false : true,
          },
        });
        break;

      case "availableOn":
        setFormValues({
          ...formValues,
          approver: { ...formValues.approver, availableOn: value.trim() },
        });
        break;

      case "biddingLimit":
        setFormValues({
          ...formValues,
          approver: {
            ...formValues.approver,
            biddingLimit: (
              Number(value.replace(/\D/g, "")) || ""
            ).toLocaleString(),
          },
        });
        break;

      default:
      // do nothing
    }
  };

  const handleInputChangeCurrency = (e) => {
    setRemoveState(true);
    setFormValues({
      ...formValues,
      approver: { ...formValues.approver, currency: e.value },
    });
  };

  const removeSelectState = () => {
    setRemoveState(false);

    setFormValues({
      ...formValues,
      approver: { ...formValues.approver, currency: "" },
    });
  };

  useEffect(() => {
    if (isCountryError !== "") {
      ToastError(isCountryError);
    } else if (isErrorBidderList !== "") {
      ToastError(isErrorBidderList);
    } else if (isErrorAvailableOnRes !== "") {
      ToastError(isErrorAvailableOnRes);
    }
  }, [isErrorAvailableOnRes, isErrorBidderList, isCountryError]);

  const saveData = (flag) => {
    if (flag === "already-exists") {
      let approvers = approverList.slice();
      approvers[activeApprover - 1] = formValues;
      setApproverList(approvers);
    } else {
      setApproverList([...approverList, formValues]);
    }
  };

  const addApprover = () => {
    if (!validateForm() && isFormTypePartner) return false;
    if (!validateForm() && isFormTypeCustomer) return false;

    if (isFormTypePartner) {
      if (bidderList?.length === 0) {
        if (!checkUniqueApprover()) {
          return false;
        }
      }
    }

    setLastApproverBtnStatus(false);
    if (activeApprover === 1) {
      // saveData();
      // setApproverListCount([
      //   ...approverListCount,
      //   Number(approverListCount.length) + 1,
      // ]);
      // setActiveApprover(activeApprover + 1);
      // setIsSaveBtnActive(true);
      // setIsAddApproverandSaveBtnActive(false);
      // setFormValues(initialValues);
      setIsConfirmationAddNewData(!isConfirmationAddNewData);
    } else {
      // setApproverListCount([
      //   ...approverListCount,
      //   Number(approverListCount.length) + 1,
      // ]);
      // setActiveApprover(activeApprover + 1);
      // setIsSaveBtnActive(true);
      // setIsAddApproverandSaveBtnActive(false);
      // setFormValues(initialValues);
      setIsConfirmationAddNewData(!isConfirmationAddNewData);
    }
  };
  const onCancelModal = () => {
    setIsConfirmationAddNewData(!isConfirmationAddNewData);
  };
  const onConfirm = () => {
    setIsConfirmationAddNewData(!isConfirmationAddNewData);
    setRemoveState(false);
    if (activeApprover === 1) {
      saveData();
      setApproverListCount([
        ...approverListCount,
        Number(approverListCount?.length) + 1,
      ]);
      setAllUsersData([...allUsersData, formValues?.userModel?.primaryPhone]);
      setActiveApprover(activeApprover + 1);
      setIsSaveBtnActive(true);
      setIsAddApproverandSaveBtnActive(false);
      setFormValues(initialValues);
      // removeOtherOption();

      isFormTypePartner
        ? setFormValues({
            ...formValues,
            userModel: {
              companyId: Number(getCompanyId()),
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
                  id: 4,
                },
              ],
            },
            bidderModel: [],
          })
        : setFormValues(customerInitialValues);
    } else {
      setApproverListCount([
        ...approverListCount,
        Number(approverListCount.length) + 1,
      ]);
      setAllUsersData([...allUsersData, formValues?.userModel?.primaryPhone]);
      setActiveApprover(activeApprover + 1);
      setIsSaveBtnActive(true);
      setIsAddApproverandSaveBtnActive(false);
      setFormValues(initialValues);
      // removeOtherOption();

      isFormTypePartner
        ? setFormValues({
            ...formValues,
            userModel: {
              companyId: Number(getCompanyId()),
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
                  id: 4,
                },
              ],
            },
            bidderModel: [],
          })
        : setFormValues(customerInitialValues);
    }
  };

  const approverSaveHandler = () => {
    if (isFormTypePartner && !validateForm()) return false;
    if (isFormTypeCustomer && !validateForm()) return false;

    if (!checkUniqueApprover()) {
      return false;
    }
    setToggleOptions(false);

    if (activeApprover < approverListCount.length) {
      saveData("already-exists");
      if (lastApproverBtnStatus === true) {
        setFormValues({ ...approverList[approverListCount.length - 1] });
        setActiveApprover(approverListCount.length);
        setIsSaveBtnActive(false);
        setIsAddApproverandSaveBtnActive(true);
      } else {
        setActiveApprover(approverListCount.length);
        setIsSaveBtnActive(true);
        setIsAddApproverandSaveBtnActive(false);
        setFormValues(lastApproverFilledData);
      }
    } else if (activeApprover === approverListCount.length) {
      if (isUserClickedRemoved) {
        setIsUserClickedRemoved(false);
      } else {
        saveData();
      }
      setIsSaveBtnActive(false);
      setIsAddApproverandSaveBtnActive(true);
      if (activeApprover === 1) {
        setLastApproverBtnStatus(false);
      } else {
        setLastApproverBtnStatus(true);
      }
    }
  };

  const makeApproverActive = (activeApproverSerial) => {
    setToggleOptions(false);

    if (activeApprover === approverListCount.length) {
      if (navigationResponse != null) {
        setLastApproverFilledData(approverList[approverList.length - 1]);
      } else {
        setLastApproverFilledData(formValues);
      }
      // setLastApproverFilledData(formValues);
    }
    setActiveApprover((prevState) => {
      innerApproverActive(activeApproverSerial);
      return activeApproverSerial;
    });
  };

  const innerApproverActive = (approverSerial) => {
    if (approverSerial === approverListCount.length) {
      setFormValues(lastApproverFilledData);
      if (lastApproverBtnStatus === true) {
        setIsSaveBtnActive(false);
        setIsAddApproverandSaveBtnActive(true);
      } else {
        setIsSaveBtnActive(true);
        setIsAddApproverandSaveBtnActive(false);
      }
    } else {
      setFormValues({ ...approverList[approverSerial - 1] });
      setIsSaveBtnActive(true);
      setIsAddApproverandSaveBtnActive(false);
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
    if (approverList.length === 0 && isFormTypePartner && validateForm()) {
      let approverClone = JSON.parse(JSON.stringify(formValues));
      const bidderIds = approverClone.bidderModel.map((bidder) => {
        return { id: bidder.id };
      });
      const approverList = {
        bidderApproverModel: approverClone.bidderApproverModel,
        userModel: approverClone.userModel,
        bidderModel: bidderIds,
      };
      dispatch(saveApprovers([approverList], "vendor/createVendorApprover"));
      return false;
    } else if (
      approverList.length === 0 &&
      isFormTypeCustomer &&
      validateForm()
    ) {
      let approverClone = JSON.parse(JSON.stringify(formValues));

      if (approverClone["approver"]["onWhatsapp"] === true) {
        approverClone["approver"]["availableOn"] = "whatsapp";
      }
      const requesterIds = approverClone.requesterIds.map((requester) => {
        return { id: requester.id };
      });
      const approverList = {
        customerApproverModel: approverClone.customerApproverModel,
        approver: approverClone.approver,
        requesterIds: requesterIds,
      };
      dispatch(saveApprovers([approverList], "customer/approver/add"));
      return false;
    }

    if (isFormTypePartner && validateForm()) {
      const transformedLastApproverObject = [...approverList];
      transformedLastApproverObject[approverList.length - 1] = formValues;
      setApproverList(transformedLastApproverObject);

      let approverClone = JSON.parse(
        JSON.stringify(transformedLastApproverObject)
      );
      const transformedApproverList = approverClone.map((approver) => {
        approver.bidderModel = approver.bidderModel.map((bidder) => {
          return { id: bidder.id };
        });
        return approver;
      });
      dispatch(
        saveApprovers(transformedApproverList, "vendor/createVendorApprover")
      );
      return false;
    } else if (isFormTypeCustomer && validateForm()) {
      const transformedLastApproverObject = [...approverList];
      transformedLastApproverObject[approverList.length - 1] = formValues;
      setApproverList(transformedLastApproverObject);

      let approverClone = JSON.parse(JSON.stringify(approverList));

      const transformedApproverList = approverClone.map((approver) => {
        if (approver["approver"]["onWhatsapp"] === true) {
          approver["approver"]["availableOn"] = "whatsapp";
        }
        approver.requesterIds = approver.requesterIds.map((requester) => {
          return { id: requester.id };
        });
        return approver;
      });
      dispatch(saveApprovers(transformedApproverList, "customer/approver/add"));
      return false;
    }
  };

  const nextNavigationHandler = () => {
    dispatch(saveApproverSlice.approverNavigation(true));
    dispatch(commonGetApiActions.dispatchGetRequest());
    props.navigationHandler({
      currentForm: "engineer",
      goForward: true,
    });
  };

  const nextNavigationHandlerCustomer = () => {
    dispatch(saveApproverSlice.approverNavigation(true));
    dispatch(commonGetApiActions.dispatchGetRequest());
    props.navigationHandler({
      currentForm: "managingHead",
      goForward: true,
    });
  };

  const [optionData, setOptionData] = useState(bidderList);
  useEffect(() => {
    setOptionData(bidderList);
  }, [bidderList]);

  // --------------------------------------------------------------------
  const [unAllocatedData, setUnAllocatedData] = useState(null);
  const [removeState, setRemoveState] = useState(false);

  useEffect(() => {
    setUnAllocatedData(bidderList);
  }, [bidderList]);

  const addBidderToTheList = (e) => {
    const id = Number(e.target.getAttribute("data-id"));
    const name = e.target.getAttribute("data-name");
    const email = e.target.getAttribute("data-email");
    const newBidderToAddIntoList = { id, name, email };

    if (activeApprover - 1 === 0 && approverList.length === 0) {
      setFormValues({
        ...formValues,
        userModel: {
          ...formValues.userModel,
        },
        bidderModel: [...formValues.bidderModel, newBidderToAddIntoList],
      });

      const myUnallocatedData = [...unAllocatedData];
      let removeFromUnallocatedData = myUnallocatedData.filter(
        (bidder) => bidder.id !== newBidderToAddIntoList.id
      );
      if (
        JSON.stringify(removeFromUnallocatedData) ===
        JSON.stringify([newBidderToAddIntoList])
      ) {
        removeFromUnallocatedData = [];
      }
      setUnAllocatedData(removeFromUnallocatedData);
    } else if (activeApprover - 1 === approverList.length) {
      setFormValues({
        ...formValues,
        userModel: {
          ...formValues.userModel,
        },
        bidderModel: [...formValues.bidderModel, newBidderToAddIntoList],
      });

      const myUnallocatedData = [...unAllocatedData];
      let removeFromUnallocatedData = myUnallocatedData.filter(
        (bidder) => bidder.id !== newBidderToAddIntoList.id
      );
      if (
        JSON.stringify(removeFromUnallocatedData) ===
        JSON.stringify([newBidderToAddIntoList])
      ) {
        removeFromUnallocatedData = [];
      }
      setUnAllocatedData(removeFromUnallocatedData);
    } else if (activeApprover - 1 < approverList.length) {
      setFormValues({
        ...formValues,
        userModel: {
          ...formValues.userModel,
        },
        bidderModel: [...formValues.bidderModel, newBidderToAddIntoList],
      });

      const myUnallocatedData = [...unAllocatedData];
      let removeFromUnallocatedData = myUnallocatedData.filter(
        (bidder) => bidder.id !== newBidderToAddIntoList.id
      );
      if (
        JSON.stringify(removeFromUnallocatedData) ===
        JSON.stringify([newBidderToAddIntoList])
      ) {
        removeFromUnallocatedData = [];
      }
      setUnAllocatedData(removeFromUnallocatedData);

      const cloneApproverList = [...approverList];
      cloneApproverList[activeApprover - 1] = {
        ...formValues,
        userModel: {
          ...formValues.userModel,
        },
        bidderModel: [...formValues.bidderModel, newBidderToAddIntoList],
      };
      setApproverList(cloneApproverList);
    }

    // setFormValues({
    //   ...formValues,
    //   userModel: {
    //     ...formValues.userModel,
    //   },
    //   bidderModel: [...formValues.bidderModel, newBidderToAddIntoList],
    // });
    // const myUnallocatedData = unAllocatedData;
    // let removeFromUnallocatedData = myUnallocatedData.filter(bidder => bidder.id !== Number(newBidderToAddIntoList.id));
    // if (JSON.stringify(removeFromUnallocatedData) === JSON.stringify([newBidderToAddIntoList])) {
    //   removeFromUnallocatedData = [];
    // }
    // setUnAllocatedData(removeFromUnallocatedData);
  };

  const removeBidderFromSelection = (e) => {
    const id = Number(e.target.getAttribute("data-id"));
    const name = e.target.getAttribute("data-name");
    const email = e.target.getAttribute("data-email");
    const newBidderToRemoveFromList = { id, name, email };

    if (activeApprover - 1 === 0 && approverList.length === 0) {
      //  Add to the Unallocated data
      setUnAllocatedData([...unAllocatedData, newBidderToRemoveFromList]);

      // Remove from allocated data
      let transformedBidderModel = [...formValues.bidderModel];
      const tranformedBidder = transformedBidderModel.filter(
        (bidder) => bidder.id !== newBidderToRemoveFromList.id
      );
      setFormValues({
        ...formValues,
        userModel: {
          ...formValues.userModel,
        },
        bidderModel: tranformedBidder,
      });
    } else if (activeApprover - 1 === approverList.length) {
      //  Add to the Unallocated data
      setUnAllocatedData([...unAllocatedData, newBidderToRemoveFromList]);

      // Remove from allocated data
      let transformedBidderModel = [...formValues.bidderModel];
      const tranformedBidder = transformedBidderModel.filter(
        (bidder) => bidder.id !== newBidderToRemoveFromList.id
      );
      setFormValues({
        ...formValues,
        userModel: {
          ...formValues.userModel,
        },
        bidderModel: tranformedBidder,
      });
    } else if (activeApprover - 1 < approverList.length) {
      //  Add to the Unallocated data
      setUnAllocatedData([...unAllocatedData, newBidderToRemoveFromList]);

      // Remove from allocated data
      let transformedBidderModel = [...formValues.bidderModel];
      const tranformedBidder = transformedBidderModel.filter(
        (bidder) => bidder.id !== newBidderToRemoveFromList.id
      );
      setFormValues({
        ...formValues,
        userModel: {
          ...formValues.userModel,
        },
        bidderModel: tranformedBidder,
      });

      const cloneApproverList = [...approverList];
      cloneApproverList[activeApprover - 1] = {
        ...formValues,
        userModel: {
          ...formValues.userModel,
        },
        bidderModel: tranformedBidder,
      };
      setApproverList(cloneApproverList);
    }

    //  //  Add to the Unallocated data
    //  setUnAllocatedData([...unAllocatedData, newBidderToRemoveFromList]);

    //  // Remove from allocated data
    //  let transformedBidderModel = formValues.bidderModel;
    //  const tranformedBidder = transformedBidderModel.filter(bidder => bidder.id !== newBidderToRemoveFromList.id);
    //  setFormValues({
    //    ...formValues,
    //    userModel: {
    //      ...formValues.userModel,
    //    },
    //    bidderModel: tranformedBidder,
    //  });
  };

  // --------------------------------------------------------------------

  const [toggleOptions, setToggleOptions] = useState(false);
  const showCustomSelectionBox = () => {
    setToggleOptions(true);
  };

  const hideCustomSelectionBox = () => {
    setToggleOptions(false);
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

  const currencyCountryOption = currencyData?.map((availableAt) => ({
    value: availableAt.currency,
    label: availableAt.currency,
  }));

  const prevData = sameAsAdmin?.map((index) => ({
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
      <span style={{ fontFamily: "Poppins-Regular" }}>{index.name}</span>,
      <span style={{ fontFamily: "Poppins-Regular", marginLeft: "12px" }}>
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
        sameASUser: "Yes",
        designation: val[6],
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

  const handleSelectChangeCustomer = (e) => {
    const val = e.value;
    setFormValues({
      ...formValues,
      approver: {
        ...formValues.approver,
        name: val[0],
        email: val[1],
        phoneCode: val[2],
        primaryPhone: val[3],
        onWhatsapp: val[4],
        availableOn: val[5],
        sameASUser: "Yes",
        designation: val[6],
      },
    });
    setDataResponse(true);
  };

  const removeOtherOptionCustomer = () => {
    setFormValues({
      ...formValues,
      approver: {
        ...formValues.approver,
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

  const removeBidder = () => {
    setActiveApprover(activeApprover - 1);
    const trasformedApproverCount = [...approverListCount];
    trasformedApproverCount.splice(trasformedApproverCount.length - 1, 1);
    setApproverListCount(trasformedApproverCount);
    setFormValues(approverList[approverList.length - 1]);

    const transformedApproverList = [...approverList];
    transformedApproverList.pop();
    setApproverList(transformedApproverList);
    setIsUserClickedRemoved(true);
  };

  return (
    <div className="form_container_area">
      <div className="approver_form positioning_container">
        {(isApproverLoading || isAvailableOnLoading) && (
          <Loading text="Loading..." />
        )}
        {isConfirmationModal && (
          <ConfirmationModal cancel={onCancelHandler} ok={onOkHandler} />
        )}
        {isConfirmationAddNewData && (
          <ConfirmationAddNewData
            cancel={onCancelModal}
            ok={onConfirm}
            name={"Approver"}
          />
        )}

        {/* ---------------- Partner form ---------------- */}
        {isFormTypePartner && (
          <>
            <div className="nav_container">
              <div className="nav_items">
                <div
                  className="nav_item active"
                  onClick={() =>
                    props.navigationHandler({ currentForm: "bidder" })
                  }
                >
                  Bidder
                </div>
                <div className="horizotal_row_container">
                  <span className="horizontal_row active">&nbsp;</span>
                </div>
                <div className="nav_item active">
                  Approver
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
                    props.navigationHandler({ currentForm: "engineer" })
                  }
                >
                  Engineer
                </div>
              </div>
            </div>

            <div className="company_id_details">
              <p className="company_id">Company id: {getReferralCode()}</p>
            </div>

            <div className="add_bidder_container">
              {approverListCount?.map((el, index) => {
                if (activeApprover === index + 1) {
                  return (
                    <div className="bidder active" key={index}>
                      <span className="bidder_text"> A{index + 1} </span>
                      {navigationResponse === null &&
                        activeApprover === approverListCount.length &&
                        activeApprover !== 1 &&
                        !isAddApproverandSaveBtnActive && (
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
                      onClick={() => makeApproverActive(index + 1)}
                    >
                      <span className="bidder_text"> A{index + 1} </span>
                    </div>
                  );
                }
              })}
            </div>

            <form onSubmit={handleFormSubmission}>
              <div className="form_container">
                <div className="form_control_spread">
                  <label className="form_label">Same as</label>
                  {navigationResponse !== null ? (
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
                        disabled={navigationResponse != null ? true : false}
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
                    Name <span className="label_mandatory">*</span>
                    <Tooltip text="Approvers are final authority to authorize the bid." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    name="name"
                    value={formValues?.userModel?.name}
                    onChange={handlePartnerInputChange}
                    disabled={
                      navigationResponse != null ||
                      formValues?.userModel?.sameASUser === "Yes"
                        ? true
                        : false
                    }
                  />
                  {errors && formValues.userModel.name.trim() === "" && (
                    <span className="error">Enter Full Name </span>
                  )}
                </div>

                <div className="form_control_area address_margin">
                  <label className="form_label"> Designation </label>
                  <input
                    type="text"
                    className="form_control"
                    name="designation"
                    value={formValues?.userModel?.designation}
                    onChange={handlePartnerInputChange}
                    disabled={
                      navigationResponse != null ||
                      formValues?.userModel?.sameASUser === "Yes"
                        ? true
                        : false
                    }
                  />
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Email <span className="label_mandatory">*</span>
                    <Tooltip text="Official email (not personal email). This email will be used for all official communication." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    name="email"
                    value={formValues?.userModel?.email}
                    onChange={(e) => {
                      handlePartnerInputChange(e);
                      validateEmail(e);
                    }}
                    disabled={
                      navigationResponse != null ||
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
                    Mobile No. <span className="label_mandatory">*</span>
                    <Tooltip text="Official mobile number (not personal mobile number). This mobile number will be used for all official communication." />
                  </label>
                  <div className="input_container">
                    {navigationResponse != null ||
                    formValues?.userModel?.sameASUser === "Yes" ? (
                      <select
                        className="select_input_disabled"
                        disabled={
                          navigationResponse != null ||
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
                          onChange={handlePartnerInputChangePhone}
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
                        handlePartnerInputChange(e);
                        validatePhone(e);
                      }}
                      disabled={
                        navigationResponse != null ||
                        formValues?.userModel?.sameASUser === "Yes"
                          ? true
                          : false
                      }
                    />
                  </div>
                  {errors &&
                    (!numberValidation(
                      formValues?.userModel?.primaryPhone.trim()
                    ) ||
                      formValues?.userModel?.primaryPhone.trim() === "" ||
                      formValues?.userModel?.phoneCode === "") && (
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
                        onChange={handlePartnerInputChange}
                        disabled={
                          navigationResponse != null ||
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
                        onChange={handlePartnerInputChange}
                        disabled={
                          navigationResponse != null ||
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
                  <span
                    value={
                      formValues?.userModel?.onWhatsapp === true
                        ? ""
                        : formValues?.userModel?.availableOn
                    }
                    disabled={
                      navigationResponse != null ||
                      formValues?.userModel?.sameASUser === "Yes" ||
                      formValues?.onWhatsapp === true
                        ? true
                        : formValues?.userModel?.onWhatsapp === true
                        ? true
                        : false
                    }
                  >
                    {navigationResponse != null ||
                    formValues?.userModel?.sameASUser === "Yes" ? (
                      <option className="form_control_disable" disabled>
                        {formValues?.userModel?.availableOn}
                      </option>
                    ) : formValues?.userModel?.onWhatsapp === true ? (
                      <input className="form_control_disable" disabled></input>
                    ) : (
                      <Select
                        options={socialMedia}
                        className="form_control_enable"
                        autocomplete="nope"
                        onChange={handlePartnerInputChangeAvailable}
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

                <div className="form_control_area fontClassSelect">
                  <label className="form_label">
                    Bidder
                    <Tooltip text="Bidder is mainly responsible for Quoting and assigning  the engineers." />
                  </label>

                  <div
                    className={`custom_bidder_dropdown ${
                      navigationResponse !== null
                        ? `disable_bidder_dropdown`
                        : ""
                    }`}
                    onMouseLeave={hideCustomSelectionBox}
                  >
                    <div
                      className={
                        navigationResponse != null
                          ? "select_box"
                          : "select_box set_background"
                      }
                      onClick={showCustomSelectionBox}
                    >
                      {formValues?.bidderModel?.map((bidder) => (
                        <div className="selected_bidder">
                          <span className="bidder_name">{bidder.name}</span>
                          <span
                            className="bidder_cancel"
                            data-id={bidder.id}
                            data-name={bidder.name}
                            data-email={bidder.email}
                            onClick={(e) => removeBidderFromSelection(e)}
                          >
                            x
                          </span>
                        </div>
                      ))}
                    </div>
                    <div
                      className={
                        navigationResponse != null
                          ? "dropdown_icon_container"
                          : "dropdown_icon_container set_background"
                      }
                      onClick={showCustomSelectionBox}
                    >
                      <img
                        src={dropdownIcon}
                        alt="dropdown-icon"
                        className="dropdown_icon"
                      />
                    </div>
                    <div>&nbsp;</div>
                    {toggleOptions && (
                      <div className="options_container">
                        {unAllocatedData?.map((bidder) => (
                          <span
                            className="option"
                            onClick={(e) => addBidderToTheList(e)}
                            key={bidder.id}
                            data-id={bidder.id}
                            data-name={bidder.name}
                            data-email={bidder.email}
                          >
                            {bidder.name}
                          </span>
                        ))}
                        {unAllocatedData.length === 0 && (
                          <span style={{ marginLeft: "10px" }}>
                            No Data Available
                          </span>
                        )}
                      </div>
                    )}
                  </div>
                </div>

                <div className="form_submit_container">
                  {(approverResponse && navigationCheck === false) ||
                  (registrationStatusKey - 5 > 0 &&
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
                          onClick={approverSaveHandler}
                        />
                      )}
                      {isAddApproverandSaveBtnActive && (
                        <>
                          <input
                            type="button"
                            value="Add Approver"
                            className="btn btn_add_bidder"
                            onClick={() => addApprover()}
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
            {/* <button
              onClick={() =>
                console.log({ formValues, approverList, approverListCount })
              }
            >
              Get State
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
                <div className="nav_item active">
                  Approver
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
              {approverListCount?.map((el, index) => {
                if (activeApprover === index + 1) {
                  return (
                    <div className="bidder active" key={index}>
                      <span className="bidder_text"> A{index + 1} </span>
                      {navigationResponse === null &&
                        activeApprover === approverListCount.length &&
                        activeApprover !== 1 &&
                        !isAddApproverandSaveBtnActive && (
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
                      onClick={() => makeApproverActive(index + 1)}
                    >
                      <span className="bidder_text"> A{index + 1} </span>
                    </div>
                  );
                }
              })}
            </div>

            <form onSubmit={handleFormSubmission}>
              <div className="form_container">
                <div className="form_control_spread">
                  <label className="form_label">Same as</label>
                  {navigationResponse !== null ? (
                    <input
                      type="text"
                      className="form_control"
                      value={
                        formValues?.approver?.sameASUser === "Yes"
                          ? formValues?.approver?.name
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
                        onChange={handleSelectChangeCustomer}
                        options={adminsData}
                        classNamePrefix="select"
                        value={
                          formValues?.approver?.sameASUser === "Yes"
                            ? {
                                value: formValues?.approver?.name,
                                label: formValues?.approver?.name,
                              }
                            : ""
                        }
                        placeholder=""
                        disabled={navigationResponse != null ? true : false}
                      />
                      {dataResponse && (
                        <img
                          src={cancelIcon}
                          alt="cancel icon"
                          className="remove_data"
                          onClick={removeOtherOptionCustomer}
                        />
                      )}
                    </>
                  )}
                </div>
                <div className="form_control_area">
                  <label className="form_label">
                    Name <span className="label_mandatory">*</span>
                    <Tooltip text="Approvers are final authority to authorize the bid." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    name="name"
                    value={formValues?.approver?.name.trim()}
                    onChange={handleCustomerInputChange}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors && formValues?.approver?.name.trim() === "" && (
                    <span className="error">Enter Full Name </span>
                  )}
                </div>

                <div className="form_control_area address_margin">
                  <label className="form_label"> Designation </label>
                  <input
                    type="text"
                    className="form_control"
                    name="designation"
                    value={formValues?.approver?.designation}
                    onChange={handleCustomerInputChange}
                    disabled={navigationResponse != null ? true : false}
                  />
                </div>

                <div className="form_control_area">
                  <label className="form_label">
                    Email <span className="label_mandatory">*</span>
                    <Tooltip text="Official email (not personal email). This email will be used for all official communication." />
                  </label>
                  <input
                    type="text"
                    className="form_control"
                    name="email"
                    value={formValues?.approver?.email.trim()}
                    onChange={(e) => {
                      handleCustomerInputChange(e);
                      validateEmail(e);
                    }}
                    disabled={navigationResponse != null ? true : false}
                  />
                  {errors &&
                    formValues.approver.email.trim() === "" &&
                    !emailValidation(formValues.approver.email.trim()) && (
                      <span className="error">Company Email ID</span>
                    )}
                  {errors &&
                    formValues.approver.email.trim() !== "" &&
                    !emailValidation(formValues.approver.email.trim()) && (
                      <span className="error">
                        Invalid Email ID, please check and re-enter
                      </span>
                    )}
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
                        <option>{formValues?.approver?.phoneCode}</option>
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
                          {formValues?.approver?.phoneCode}
                        </div>
                        <Select
                          onChange={handleCustomerInputChangePhone}
                          onInputChange={setHide}
                          disabled={navigationResponse != null ? true : false}
                          placeholder="Search code"
                          className="select_input"
                          options={options}
                          styles={customStyles}
                          value={
                            formValues?.approver?.phoneCode.length > 0
                              ? {
                                  label: formValues?.approver?.phoneCode,
                                  value: formValues?.approver?.phoneCode,
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
                      value={formValues?.approver?.primaryPhone}
                      onChange={(e) => {
                        handleCustomerInputChange(e);
                        validatePhone(e);
                      }}
                      disabled={navigationResponse != null ? true : false}
                    />
                  </div>
                  {errors &&
                    (!numberValidation(
                      formValues?.approver?.primaryPhone.trim()
                    ) ||
                      formValues?.approver?.primaryPhone.trim() === "" ||
                      formValues?.approver?.phoneCode === "") && (
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
                        checked={formValues?.approver?.onWhatsapp === true}
                        onChange={handleCustomerInputChange}
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
                        checked={formValues?.approver?.onWhatsapp === false}
                        onChange={handleCustomerInputChange}
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
                      formValues?.approver?.onWhatsapp === true
                        ? ""
                        : formValues?.approver?.availableOn
                    }
                    disabled={
                      navigationResponse != null ||
                      formValues?.onWhatsapp === true
                        ? true
                        : formValues?.approver?.onWhatsapp === true
                        ? true
                        : false
                    }
                  >
                    {navigationResponse != null ? (
                      <option className="form_control_disable" disabled>
                        {formValues?.approver?.availableOn}
                      </option>
                    ) : formValues?.approver?.onWhatsapp === true ? (
                      <input className="form_control_disable" disabled></input>
                    ) : (
                      <Select
                        options={socialMedia}
                        autocomplete="nope"
                        className="form_control_enable"
                        onChange={handleCustomerInputChangeAvailable}
                        value={{
                          value: formValues?.approver?.availableOn,
                          label: formValues?.approver?.availableOn,
                        }}
                      ></Select>
                    )}
                  </span>
                  {errors &&
                    formValues.approver.onWhatsapp === false &&
                    formValues.approver.availableOn === "" && (
                      <span className="error">
                        Please select at least one option.
                      </span>
                    )}
                </div>

                <div className="form_control_area fontClassSelect">
                  <label className="form_label">Requester</label>

                  <Select
                    isMulti
                    className="requster_select"
                    getOptionLabel={(option) => option.name}
                    getOptionValue={(option) => option.id}
                    isSearchable={true}
                    value={formValues?.requesterIds}
                    onChange={onSelectApproverRequestor}
                    options={requesterClone}
                    isDisabled={navigationResponse != null ? true : false}
                  />
                </div>

                <div className="form_control_spread">
                  <label className="form_label">
                    Bidding Limit
                    <span className="info_icon">
                      <Tooltip text="Bidder is mainly responsible for Quoting and assigning  the engineers." />
                    </span>
                  </label>
                  <div className="select_container">
                    {navigationResponse != null ? (
                      <select className="forDisableSelect" disabled>
                        {<option>{formValues?.approver?.currency}</option>}
                      </select>
                    ) : (
                      <>
                        <Select
                          className="select_control_enable amount_version"
                          onChange={handleInputChangeCurrency}
                          classNamePrefix="select-currency"
                          options={currencyCountryOption}
                          value={{
                            value: formValues?.approver?.currency,
                            label: formValues?.approver?.currency,
                          }}
                        />

                        {formValues?.approver?.currency && (
                          <img
                            src={cancelIcon}
                            alt="cancel icon"
                            className="remove_data_state"
                            onClick={removeSelectState}
                          />
                        )}
                      </>
                    )}

                    {/* <input
                      type="number"
                      className="form_control"
                      name="biddingLimit"
                      style={{ width: "55%", marginLeft: "15px" }}
                      autoComplete="off"
                      value={formValues?.approver?.biddingLimit}
                      onChange={handleCustomerInputChange}
                      disabled={navigationResponse != null ? true : false}
                    /> */}
                    {/* c */}
                    {navigationResponse != null ? (
                      <input
                        type="text"
                        className="form_control_disablebiddinglimit"
                        name="biddingLimit"
                        style={{ width: "60%", marginLeft: "15px" }}
                        value={formValues?.approver?.biddingLimit}
                        disabled={navigationResponse != null ? true : false}
                      />
                    ) : (
                      <input
                        type="text"
                        className="form_control_biddinglimit"
                        name="biddingLimit"
                        style={{ width: "60%", marginLeft: "15px" }}
                        value={formValues?.approver?.biddingLimit}
                        onChange={(e) => {
                          if (e.target.value.length <= 15)
                            handleCustomerInputChange(e);
                        }}
                        disabled={navigationResponse != null ? true : false}
                      />
                    )}
                  </div>
                  {errors &&
                    ((formValues?.approver?.biddingLimit === "" &&
                      formValues?.approver?.currency !== "") ||
                      (formValues?.approver?.biddingLimit !== "" &&
                        formValues?.approver?.currency === "")) && (
                      <span className="error">
                        Please select both the fields.
                      </span>
                    )}
                </div>

                <div className="form_submit_container">
                  {(approverResponse && navigationCheck === false) ||
                  (registrationStatusKey - 5 > 0 &&
                    navigationCheck === false &&
                    navigationResponse) ? (
                    <input
                      type="button"
                      value=" Next"
                      className="btn btn_submit_form"
                      onClick={nextNavigationHandlerCustomer}
                    />
                  ) : (
                    <>
                      {isSaveBtnActive && (
                        <input
                          type="button"
                          value="Save"
                          className="btn btn_submit_form"
                          onClick={approverSaveHandler}
                        />
                      )}
                      {isAddApproverandSaveBtnActive && (
                        <>
                          <input
                            type="button"
                            value="Add Approver"
                            className="btn btn_add_bidder"
                            onClick={() => addApprover()}
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

export default Approver;
