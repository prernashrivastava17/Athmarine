import React, { useState, useEffect, useReducer } from "react";
import { useDispatch, useSelector } from "react-redux";
import AwesomeSlider from "react-awesome-slider";
import "react-awesome-slider/dist/styles.css";
import Select from "react-select";
import "./Engineer.scss";
import { ToastError } from "./../../../../../components/Tostify";
import ConfirmationModal from "./../../Modals/ConfirmationModal/ConfirmationModal";
import AdditionalCharges from "./../../Modals/AdditionalCharges/AdditionalCharges";
import warningIcon from "./../../../../../assets/images/warning-red-icon.png";
import Loading from "./../../../../../components/Loading/Loading";
import Tooltip from "./../../../../../components/Tooltip/Tooltip";
import ConfirmationAddNewData from "../../Modals/confirmationAddNewData/confirmationAddNewData";
import {
  validateEmailPhone,
  validateEmailPhoneActions,
} from "../../../../../store/vendorRegistration/validateEmailPhone";
import {
  emailValidation,
  numberValidation,
} from "../../../../../utils/helpers";
import { fileUpload } from "../../../../../store/common/fileUpload";
import { uploadActions } from "../../../../../store/common/fileUpload";
import additionIcon from "./../../../../../assets/images/addition-icon.png";
import removeIcon from "./../../../../../assets/images/remove-icon.png";
import cancelIcon from "./../../../../../assets/images/cancel_other_icon.png";

import { getCategories } from "../../../../../store/vendorRegistration/category";
import { getEquipmentByCategory } from "../../../../../store/vendorRegistration/equipmentByCategory";
import {
  saveEngineer,
  saveEngineerSlice,
} from "../../../../../store/vendorRegistration/vendorEngineer";
import { getEngineerStatus } from "../../../../../store/vendorRegistration/engineerStatus";
import { getAvailableOn } from "./../../../../../store/common/availableOn";
import {
  getAuthData,
  getAuthToken,
  getCompanyId,
  getReferralCode,
  getToken,
} from "../../../../../config";
import { getEngineerMake } from "./../../../../../store/vendorRegistration/engineerMake";
import { getEngineerCharges } from "./../../../../../store/vendorRegistration/engineerCharges";
import {
  commonGetAPiCall,
  commonGetApiActions,
} from "../../../../../store/common/commonGetApi";
import { getDynamicPriceForEngineers } from "../../../../../store/vendorRegistration/getDynamicEngineersPrice";
import { getAllUserDetails } from "../../../../../store/common/allUserDetails";
import {
  addressGetAPiCall,
  addressGetApiActions,
} from "../../../../../store/vendorRegistration/addressGetApi";
import cancel from "./../../../../../assets/images/cancel.png";
import { toast } from "react-toastify";
import CertificateModal from "../../Modals/CertificateModal.js/CertificateModal";

const Engineer = (props) => {
  const dispatch = useDispatch();

  let isFormTypePartner = props.selectedUser === "partner" ? true : false;
  let isFormTypeCustomer = props.selectedUser === "customer" ? true : false;
  let isFormTypePartnersIndividual =
    props.selectedUser === "partners-individual" ? true : false;

  const countries = useSelector((state) => state.countries.countries);
  const isCountryError = useSelector((state) => state.countries.isError);
  const categories = useSelector(
    (state) => state.engineerCategories.categories
  );
  const isErrorCategories = useSelector(
    (state) => state.engineerCategories.isError
  );
  const equipments = useSelector(
    (state) => state.engineerEquipments.equipments
  );
  const isErrorEquipments = useSelector(
    (state) => state.engineerEquipments.isError
  );
  const engineerStatus = useSelector(
    (state) => state.engineerStatus.engineerStatus
  );
  const is_Loading_engineerStatus = useSelector(
    (state) => state.engineerStatus.isLoading
  );
  const isErrorEengineerStatus = useSelector(
    (state) => state.engineerStatus.isError
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
  const engineerCharges = useSelector(
    (state) => state.engineerChargesReducer.engineerCharges
  );
  const isErrorEengineerCharges = useSelector(
    (state) => state.engineerChargesReducer.isError
  );
  const isLoadingEngineerCharges = useSelector(
    (state) => state.engineerChargesReducer.isLoading
  );
  const makeResponse = useSelector(
    (state) => state.engineerMakeReducer.engineerMake
  );
  const isErrorMake = useSelector((state) => state.engineerMakeReducer.isError);
  const isMakeLoading = useSelector(
    (state) => state.engineerMakeReducer.isLoading
  );
  const isError_ResponseErr = useSelector(
    (state) => state.vendorEngineerRes.isError
  );
  const vendorEngineer_Response = useSelector(
    (state) => state.vendorEngineerRes.engineer
  );
  const isLoading_vendorEngineer = useSelector(
    (state) => state.vendorEngineerRes.isLoading
  );
  const navigationCheck = useSelector(
    (state) => state.vendorEngineerRes.navigationCheck
  );
  const navigationResponse = useSelector(
    (state) => state.commonGetApiReducer.getApiRes
  );
  const fileUploadRes = useSelector((state) => state.fileUploadReducer.upload);
  const fileUploadResIsLoading = useSelector(
    (state) => state.fileUploadReducer.isLoading
  );
  const dynamicEngineerPriceRes = useSelector(
    (state) => state.dynamicEngineerPriceReducer.price
  );
  const dynamicEngineerPriceLoading = useSelector(
    (state) => state.dynamicEngineerPriceReducer.isLoading
  );
  const registrationStatusKey = useSelector(
    (state) => state.commonGetApiReducer.registrationStatusKey
  );
  const fileUploadIndex = useSelector((state) => state.fileUploadReducer.index);
  const allUserDetails = useSelector(
    (state) => state.allUserDetailsReducer.allUserDetails
  );
  const addressData = useSelector(
    (state) => state.addressGetApiReducer.getApiRes
  );

  const [errors, setErrors] = useState(false);
  const [hide, setHide] = useState();
  const [allUsersData, setAllUsersData] = useState([]);
  const [isUserClickedRemoved, setIsUserClickedRemoved] = useState(false);

  useEffect(() => {
    if (vendorEngineer_Response == null) {
      if (fileUploadIndex !== null) {
        let currentEngineerModelList4 = formValues.engineerModelList;

        if (
          formValues.engineerModelList[fileUploadIndex]["certificate"]
            .length === 1 &&
          ["", undefined].includes(
            formValues.engineerModelList[fileUploadIndex]["certificate"][0][
              "certificates"
            ]
          )
        ) {
          console.log("iffffffffffff");
          currentEngineerModelList4[fileUploadIndex]["certificate"][0][
            "certificates"
          ] = fileUploadRes?.fileKey;
        } else {
          console.log("elseeeeeeeeee");
          currentEngineerModelList4[fileUploadIndex]["certificate"][
            currentEngineerModelList4[fileUploadIndex]["certificate"].length
          ] = { certificates: fileUploadRes?.fileKey, id: "" };
        }

        setFormValues({
          ...formValues,
          engineerModelList: currentEngineerModelList4,
        });
      }
    }
  }, [fileUploadRes]);

  useEffect(() => {
    if (fileUploadRes !== null) {
      dispatch(uploadActions.emptyState());
    }
  }, [dispatch]);

  useEffect(() => {
    dispatch(getCategories());
    dispatch(getAvailableOn());
    dispatch(getAllUserDetails());
    dispatch(getEngineerMake());
    dispatch(getEngineerCharges());
    dispatch(getDynamicPriceForEngineers(``, `chages/getEngineerCharges`));
  }, [dispatch]);

  useEffect(() => {
    setSameAsAdmin(allUserDetails);
  }, [allUserDetails]);

  useEffect(() => {
    if (navigationCheck) {
      if (vendorEngineer_Response !== null) {
        props.setFormLabel("services", 3);
      }
    }
  }, [vendorEngineer_Response, props, navigationCheck]);

  const getCompanyIdCheck = () => {
    if (getAuthData()?.companyId) {
      return getAuthData()?.companyId;
    }
    return getCompanyId();
  };

  useEffect(() => {
    if (registrationStatusKey - 6 > 0 && isFormTypePartner) {
      dispatch(
        commonGetAPiCall(`/equipments/getAllEngineer/${getCompanyIdCheck()}`)
      );
    } else if (registrationStatusKey - 6 > 0 && isFormTypePartnersIndividual) {
      dispatch(
        commonGetAPiCall(`/equipments/getAllEngineer/${getCompanyIdCheck()}`)
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
      vendorEngineer_Response !== null &&
      isFormTypePartner
    ) {
      dispatch(
        commonGetAPiCall(`/equipments/getAllEngineer/${getCompanyId()}`)
      );
    } else if (
      !navigationCheck &&
      vendorEngineer_Response !== null &&
      isFormTypePartnersIndividual
    ) {
      dispatch(
        commonGetAPiCall(`/equipments/getAllEngineer/${getCompanyId()}`)
      );
    }
  }, [
    dispatch,
    navigationCheck,
    vendorEngineer_Response,
    isFormTypePartner,
    isFormTypePartnersIndividual,
  ]);

  useEffect(() => {
    if (navigationResponse !== null && Array.isArray(navigationResponse)) {
      setengineerList(navigationResponse);
      setFormValues(navigationResponse[navigationResponse.length - 1]);
      let bidderCount = navigationResponse?.map((item, index) => index + 1);
      setengineerListCount(bidderCount);
      setactiveEngineer(navigationResponse.length);
    }
  }, [navigationResponse]);

  useEffect(() => {
    if (isFormTypePartnersIndividual) {
      dispatch(addressGetAPiCall(`/vendor/get/${getCompanyIdCheck()}`));
    }
  }, [dispatch, isFormTypePartnersIndividual]);

  const options = countries?.map((country) => ({
    value: country.phoneCode,
    label: [
      <img
        src={country.flag}
        style={{ width: "20px", marginRight: "15px" }}
        alt="country flag"
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

  useEffect(() => {
    if (isCountryError !== "") {
      ToastError(isCountryError);
    } else if (isErrorCategories !== "") {
      ToastError(isErrorCategories);
    } else if (isErrorAvailableOnRes !== "") {
      ToastError(isErrorAvailableOnRes);
    } else if (isError_ResponseErr !== "") {
      ToastError(isError_ResponseErr);
    }
  }, [
    isErrorAvailableOnRes,
    isErrorEengineerStatus,
    isErrorCategories,
    isCountryError,
    isError_ResponseErr,
  ]);

  useEffect(() => {
    if (isErrorEquipments !== "") {
      ToastError(isErrorEquipments);
    }
  }, [isErrorEquipments]);

  const validateForm = () => {
    if (
      formValues.userModel.name.trim() &&
      formValues.userModel.email.trim() &&
      emailValidation(formValues.userModel.email.trim()) &&
      formValues.userModel.primaryPhone.trim() &&
      formValues.userModel.primaryPhone.length > 5 &&
      formValues.userModel.primaryPhone.length < 14 &&
      formValues.userModel.phoneCode &&
      ((formValues.userModel.onWhatsapp === false &&
        formValues.userModel.availableOn !== "") ||
      (formValues.userModel.onWhatsapp === true &&
        formValues.userModel.availableOn === "")
        ? true
        : false)
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
    let { name, value } = e.target;

    switch (name) {
      case "engineerId":
        setFormValues({ ...formValues, engineerId: value });
        break;

      case "registeredEngineer":
        setErrors(false);
        setDataResponse(false);
        setFormValues({ ...initialValues, yesOrNo: value });
        break;

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

      case "sameAsBidder":
        setFormValues({
          ...formValues,
          userModel: {
            ...formValues.userModel,
            roleModel: [{ id: value.trim() === "yes" ? 7 : 4 }],
          },
        });
        break;

      case "categoryModel":
        setEquipmentFlag(true);
        setEquipmentChangedIndex(index + 1);

        let currentEngineerModelList = formValues.engineerModelList;
        currentEngineerModelList[index]["categoryModel"]["id"] = value;
        currentEngineerModelList[index]["equipmentModel"] = {
          id: "",
          name: "",
        };

        currentEngineerModelList[index]["make"] = { id: "" };
        currentEngineerModelList[index]["experience"] = "";
        setFormValues({
          ...formValues,
          engineerModelList: currentEngineerModelList,
        });
        dispatch(getEquipmentByCategory(value));

        break;

      case "equipmentModel":
        if (JSON.parse(value).name !== "Others") {
          let currentEngineerModelList1 = formValues.engineerModelList;
          currentEngineerModelList1[index]["equipmentModel"] =
            JSON.parse(value);
          currentEngineerModelList1[index]["make"] = { id: "" };
          currentEngineerModelList1[index]["experience"] = "";
          setFormValues({
            ...formValues,
            engineerModelList: currentEngineerModelList1,
          });
        } else {
          let currentEngineerModelList1 = formValues.engineerModelList;
          currentEngineerModelList1[index]["equipmentModel"] = {
            id: null,
            name: "",
          };
          currentEngineerModelList1[index]["make"] = { id: "" };
          currentEngineerModelList1[index]["experience"] = "";
          setFormValues({
            ...formValues,
            engineerModelList: currentEngineerModelList1,
          });
        }
        break;

      case "equipmentModel-otherName":
        let cloneEngineerModelList = formValues.engineerModelList;
        cloneEngineerModelList[index]["equipmentModel"] = {
          id: null,
          name: value,
          manufacturerModule: [{ id: 1 }],
          masterEquipmentCategoryModel: {
            id: Number(
              formValues.engineerModelList[index]["categoryModel"]["id"]
            ),
          },
          isVerified: `0`,
        };
        setFormValues({
          ...formValues,
          engineerModelList: cloneEngineerModelList,
        });
        break;

      case "make":
        if (value !== "0") {
          let currentEngineerModelList2 = formValues.engineerModelList;
          currentEngineerModelList2[index]["make"]["id"] = value;
          currentEngineerModelList2[index]["experience"] = "";
          setFormValues({
            ...formValues,
            engineerModelList: currentEngineerModelList2,
          });
        } else {
          let currentEngineerModelList2 = formValues.engineerModelList;
          currentEngineerModelList2[index]["make"]["id"] = value;
          currentEngineerModelList2[index]["experience"] = "";
          setFormValues({
            ...formValues,
            engineerModelList: currentEngineerModelList2,
          });
        }
        break;

      case "make-otherName":
        let currentEngineerModelList2 = formValues.engineerModelList;

        currentEngineerModelList2[index]["make"] = {
          id: 0,
          name: value,
          status: 0,
          verified: false,
        };
        setFormValues({
          ...formValues,
          engineerModelList: currentEngineerModelList2,
        });
        break;

      case "experienceYear":
        let currentEngineerModelList3 = formValues.engineerModelList;
        currentEngineerModelList3[index]["experience"] = value;
        setFormValues({
          ...formValues,
          engineerModelList: currentEngineerModelList3,
        });
        break;

      default:
      // do nothing
    }
  };

  const engineerCostModalHandler = () => {
    setEngineerModal(!engineerModal);
  };

  const initialValues = {
    engineerId: "",
    yesOrNo: "No",
    userModel: {
      name: "",
      email: "",
      primaryPhone: "",
      phoneCode: "",
      onWhatsapp: false,
      availableOn: "",
      sameASUser: "No",
      roleModel: [
        {
          id: 3,
        },
      ],
      companyId: getCompanyId(),
    },
    engineerModelList: [
      {
        categoryModel: {
          id: "",
        },
        certificate: [
          {
            certificates: "",
            id: "",
          },
        ],
        certified: "",
        equipmentModel: { id: "", name: "" },
        experience: "",
        experienceYear: "",
        experienceMonth: "",
        make: {
          id: "",
        },
      },
    ],
  };

  const [engineerModal, setEngineerModal] = useState(false);
  const [category, setCategory] = useState("");
  const [engineerId, setEngineerId] = useState("");
  const [formValues, setFormValues] = useState(initialValues);
  const [engineerList, setengineerList] = useState([]);
  const [engineerListCount, setengineerListCount] = useState([1]);
  const [activeEngineer, setactiveEngineer] = useState(1);
  const [isSaveBtnActive, setIsSaveBtnActive] = useState(false);
  const [isAddEngineerandSaveBtnActive, setIsAddEngineerandSaveBtnActive] =
    useState(true);
  const [lastApproverBtnStatus, setLastEngineerBtnStatus] = useState(false);
  const [lastEngineerFilledData, setlastEngineerFilledData] =
    useState(initialValues);
  const [isConfirmationModal, setIsConfirmationModal] = useState(false);
  const [isWorkingExperienceError, setIsWorkingExperienceError] =
    useState(false);
  const [otherState, setOtherState] = useState([[false]]);
  const [equipmentFlag, setEquipmentFlag] = useState(false);
  const [equipmentChangedIndex, setEquipmentChangedIndex] = useState(0);
  const [equipmentDropdownValues, setEquipmentDropdownValues] = useState([[]]);
  const [isConfirmationAddNewData, setIsConfirmationAddNewData] =
    useState(false);
  const [sameAsAdmin, setSameAsAdmin] = useState([""]);
  const [dataResponse, setDataResponse] = useState(false);
  const [duplicateWorkingExpIndex, setDuplicateWorkingExpIndex] = useState(-1);
  const [isShowDocumentModal, setIsShowDocumentModal] = useState(false);
  const [isDataShhowInDocumentModal, setDataShowInDocumentModal] =
    useState(null);
  const [currentActiveRow, setCurrentActiveRow] = useState(-1);

  useEffect(() => {
    if (equipments !== null) {
      let equipmentDropdownValuesClone = JSON.parse(
        JSON.stringify(equipmentDropdownValues)
      );
      equipmentDropdownValuesClone[activeEngineer - 1][
        equipmentChangedIndex - 1
      ] = equipments;
      setEquipmentDropdownValues(equipmentDropdownValuesClone);
    }
  }, [equipments]);

  const onCancelHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
  };

  const onOkHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
    apiCall();
  };

  const saveData = (flag) => {
    if (flag === "already-exists") {
      let approvers = engineerList.slice();
      approvers[activeEngineer - 1] = formValues;
      setengineerList(approvers);
    } else {
      setengineerList([
        ...engineerList,
        {
          userModel: formValues.userModel,
          engineerModelList: formValues.engineerModelList,
          yesOrNo: formValues?.yesOrNo,
          engineerId: formValues.engineerId,
        },
      ]);
    }
  };

  const [checkEngineerCount, setCheckEngineerCount] = useState(0);
  const addEngineer = () => {
    if (!validateForm()) return false;

    if (activeEngineer === 1) {
      if (!checkUniqueEngineer("checkValidity")) {
        return;
      }
    }

    if (checkDuplicacyWorkingExperience()?.status === true) {
      return;
    } else {
      setDuplicateWorkingExpIndex("-1");
    }

    // if (activeEngineer === 1) {
    //   if (checkDuplicacyWorkingExperience()?.status === true) {
    //     return;
    //   } else {
    //     setDuplicateWorkingExpIndex("-1");
    //   }
    // }

    let equipmentDropdownValuesClone = JSON.parse(
      JSON.stringify(equipmentDropdownValues)
    );
    equipmentDropdownValuesClone.push([]);
    setEquipmentDropdownValues(equipmentDropdownValuesClone);

    const otherStateClone = JSON.parse(JSON.stringify(otherState));
    otherStateClone.push([false]);
    setOtherState(otherStateClone);

    setIsWorkingExperienceError(false);

    // setRegisteredEngineer("No");
    // setEngineerId("");

    if (engineerList.length === 10) {
      if (checkEngineerCount === 0) {
        setEngineerModal(true);
        setCheckEngineerCount(1);
        return;
      }
      if (checkEngineerCount === 1) {
        setCheckEngineerCount(0);
      }
    }
    if (engineerList.length === 15) {
      if (checkEngineerCount === 0) {
        setEngineerModal(true);
        setCheckEngineerCount(1);
        return;
      }
      if (checkEngineerCount === 1) {
        setCheckEngineerCount(0);
      }
    }

    setLastEngineerBtnStatus(false);
    if (activeEngineer === 1) {
      // saveData();
      // setengineerListCount([
      //   ...engineerListCount,
      //   Number(engineerListCount.length) + 1,
      // ]);
      // setactiveEngineer(activeEngineer + 1);
      // setIsSaveBtnActive(true);
      // setIsAddEngineerandSaveBtnActive(false);
      // setFormValues(initialValues);
      setIsConfirmationAddNewData(!isConfirmationAddNewData);
    } else {
      // setengineerListCount([
      //   ...engineerListCount,
      //   Number(engineerListCount.length) + 1,
      // ]);
      // setactiveEngineer(activeEngineer + 1);
      // setIsSaveBtnActive(true);
      // setIsAddEngineerandSaveBtnActive(false);
      // setFormValues(initialValues);
      setIsConfirmationAddNewData(!isConfirmationAddNewData);
    }
  };

  const onCancelModal = () => {
    setIsConfirmationAddNewData(!isConfirmationAddNewData);
  };

  const onConfirm = () => {
    removeSelectOption();
    setIsConfirmationAddNewData(!isConfirmationAddNewData);
    if (activeEngineer === 1) {
      saveData();
      setengineerListCount([
        ...engineerListCount,
        Number(engineerListCount.length) + 1,
      ]);
      setAllUsersData([...allUsersData, formValues.userModel.primaryPhone]);
      setactiveEngineer(activeEngineer + 1);
      setIsSaveBtnActive(true);
      setIsAddEngineerandSaveBtnActive(false);
      setFormValues(initialValues);
      // document.getElementById("userType").value = "Select";
      setDataResponse(false);
    } else {
      setengineerListCount([
        ...engineerListCount,
        Number(engineerListCount.length) + 1,
      ]);
      setAllUsersData([...allUsersData, formValues.userModel.primaryPhone]);
      setactiveEngineer(activeEngineer + 1);
      setIsSaveBtnActive(true);
      setIsAddEngineerandSaveBtnActive(false);
      setFormValues(initialValues);
      setDataResponse(false);
      // document.getElementById("userType").value = "Select";
    }
  };

  const engineerSaveHandler = () => {
    if (!validateForm()) return false;
    if (!checkUniqueEngineer("checkValidity")) return false;

    if (checkDuplicacyWorkingExperience()?.status === true) {
      return;
    } else {
      setDuplicateWorkingExpIndex("-1");
    }

    if (isUserClickedRemoved) {
      if (engineerList.length > 0)
        setengineerList([...engineerList, formValues]);
      setIsUserClickedRemoved(false);
    }

    if (activeEngineer < engineerListCount.length) {
      saveData("already-exists");
      if (lastApproverBtnStatus === true) {
        setFormValues({ ...engineerList[engineerListCount.length - 1] });
        setactiveEngineer(engineerListCount.length);
        setIsSaveBtnActive(false);
        setIsAddEngineerandSaveBtnActive(true);
      } else {
        setactiveEngineer(engineerListCount.length);
        setIsSaveBtnActive(true);
        setIsAddEngineerandSaveBtnActive(false);
        setFormValues(lastEngineerFilledData);
      }
    } else if (activeEngineer === engineerListCount.length) {
      if (isUserClickedRemoved) {
        setIsUserClickedRemoved(false);
      } else {
        saveData();
      }
      setIsSaveBtnActive(false);
      setIsAddEngineerandSaveBtnActive(true);
      if (activeEngineer === 1) {
        setLastEngineerBtnStatus(false);
      } else {
        setLastEngineerBtnStatus(true);
      }
    }
  };

  const makeEngineerActive = (activeEngineerSerial) => {
    setIsWorkingExperienceError(false);

    if (activeEngineer === engineerListCount.length) {
      if (navigationResponse != null) {
        setlastEngineerFilledData(engineerList[engineerList.length - 1]);
      } else {
        setlastEngineerFilledData(formValues);
      }
      // setlastEngineerFilledData(formValues);
    }
    setactiveEngineer((prevState) => {
      innerEngineerActive(activeEngineerSerial);
      return activeEngineerSerial;
    });
  };

  const innerEngineerActive = (engineerSerial) => {
    if (engineerSerial === engineerListCount.length) {
      setFormValues(lastEngineerFilledData);
      if (lastApproverBtnStatus === true) {
        setIsSaveBtnActive(false);
        setIsAddEngineerandSaveBtnActive(true);
      } else {
        setIsSaveBtnActive(true);
        setIsAddEngineerandSaveBtnActive(false);
      }
    } else {
      setFormValues({ ...engineerList[engineerSerial - 1] });
      setIsSaveBtnActive(true);
      setIsAddEngineerandSaveBtnActive(false);
    }
  };

  const callApiEngineerStatus = () => {
    if (formValues.engineerId.length < 5) {
      const getYesOrNo = formValues?.yesOrNo;
      const getEngineerId = formValues.engineerId;
      setFormValues({
        ...initialValues,
        userModel: { ...initialValues.userModel },
        yesOrNo: getYesOrNo,
        engineerId: getEngineerId,
      });
    }

    let isEngineerIdExist = false;
    for (let j = 0; j < engineerList.length; j++) {
      if (j === activeEngineer - 1) {
      } else if (
        formValues.yesOrNo === "Yes" &&
        formValues.engineerId !== "" &&
        formValues.engineerId.length > 5
      ) {
        if (engineerList[j]?.engineerId === formValues.engineerId) {
          isEngineerIdExist = true;
        }
      }
    }

    if (isEngineerIdExist) {
      ToastError(`Duplicate engineer Id`);
    } else {
      formValues.engineerId.length > 0 &&
        dispatch(getEngineerStatus(formValues.engineerId));
    }
  };

  useEffect(() => {
    //  setFormValues({userModel:engineerStatus?.userModel,engineerModelList:engineerStatus?.engineerModelList})
    if (engineerStatus !== null) {
      if (
        engineerStatus?.userModel?.name !== null &&
        engineerStatus?.userModel?.name !== undefined &&
        navigationResponse === null &&
        engineerStatus.userModel?.status === 1
      ) {
        let transformedFormValues = { ...formValues };
        if (transformedFormValues.userModel.onWhatsapp) {
          transformedFormValues.userModel = {
            ...engineerStatus?.userModel,
            companyId: getCompanyId(),
            availableOn: "",
          };
          transformedFormValues.engineerModelList = [
            ...engineerStatus?.engineerModelList,
          ];
        } else {
          transformedFormValues.userModel = {
            ...engineerStatus?.userModel,
            companyId: getCompanyId(),
          };
          transformedFormValues.engineerModelList = [
            ...engineerStatus?.engineerModelList,
          ];
        }
        setFormValues(transformedFormValues);
      } else if (
        engineerStatus?.userModel?.name !== null &&
        engineerStatus?.userModel?.name !== undefined &&
        navigationResponse === null &&
        engineerStatus?.userModel?.status === 0
      ) {
        const previousEngineerId = formValues.engineerId;
        const previousYesOrNo = formValues?.yesOrNo;
        setErrors(false);
        setFormValues({
          ...initialValues,
          engineerId: previousEngineerId,
          yesOrNo: previousYesOrNo,
        });
      } else if (engineerStatus === null && isErrorEengineerStatus) {
        setFormValues(initialValues);
      } else {
        ToastError("Invalid engineer Id");
      }
    }
  }, [engineerStatus, isErrorEengineerStatus]);

  const checkUniqueEngineer = (flag) => {
    if (flag === "callApi") {
      callApiEngineerStatus();
      return;
    } else if (flag === "checkValidity") {
      if (formValues.yesOrNo === "Yes" && formValues.engineerId === "") {
        ToastError("Please fill engineer ID");
        return false;
      }

      const workingExperienceCount = formValues.engineerModelList.length - 1;
      if (
        formValues.engineerModelList[workingExperienceCount].categoryModel
          .id === "" ||
        formValues.engineerModelList[workingExperienceCount].equipmentModel
          .id === undefined ||
        formValues.engineerModelList[workingExperienceCount].experience ===
          "" ||
        formValues.engineerModelList[workingExperienceCount].make.id === "" ||
        formValues.engineerModelList[workingExperienceCount].equipmentModel
          .name === ""
      ) {
        ToastError(`Work experience can't be blank.`);
        return false;
      }

      if (
        Number(activeEngineer.length) <= Number(engineerListCount.length - 1)
      ) {
        if (engineerList[activeEngineer]["yesOrNo"] !== formValues.yesOrNo) {
          if (
            engineerList[activeEngineer]["yesOrNo"] === "false" &&
            formValues.yesOrNo === "true"
          ) {
            setFormValues(initialValues);
          } else if (
            engineerList[activeEngineer]["yesOrNo"] === "true" &&
            formValues.yesOrNo === "false"
          ) {
            setFormValues({
              ...formValues,
              engineerId: "",
            });
          }
        }
      }

      let emailFlag = false,
        phoneFlag = false,
        engineerId = false;
      for (let j = 0; j < engineerList.length; j++) {
        if (j === activeEngineer - 1) {
          // Do nothing
        } else if (
          formValues.yesOrNo === "true" &&
          formValues.engineerId !== ""
        ) {
          if (engineerList[j]?.engineerId === formValues.engineerId) {
            engineerId = true;
          }
        } else if (
          formValues.userModel.email !== "" &&
          formValues.userModel.primaryPhone !== ""
        ) {
          if (
            engineerList[j]?.userModel?.email === formValues.userModel.email
          ) {
            emailFlag = true;
          }
          if (
            engineerList[j]?.userModel?.primaryPhone ===
            formValues.userModel.primaryPhone
          ) {
            phoneFlag = true;
          }
        } else if (
          formValues.userModel.email !== "" &&
          engineerList[j]?.userModel?.email === formValues.userModel.email
        ) {
          emailFlag = true;
        } else if (
          formValues.userModel.primaryPhone !== "" &&
          engineerList[j]?.userModel?.primaryPhone ===
            formValues.userModel.primaryPhone
        ) {
          phoneFlag = true;
        }
      }

      if (engineerId) {
        setFormValues(initialValues);
        ToastError(`Duplicate Engineer`);
        return false;
      }
      if (emailFlag && phoneFlag) {
        ToastError(`Duplicate Engineer`);
        return false;
      } else if (emailFlag) {
        ToastError(`Duplicate Engineer`);
        return false;
      } else if (phoneFlag) {
        ToastError(`Duplicate Engineer`);
        return false;
      } else {
        return true;
      }
    }
  };

  const handleFormSubmission = (event) => {
    event.preventDefault();
    if (isFormTypePartner) {
      //duplicate equipment details
      if (checkDuplicacyWorkingExperience()?.status === true) {
        return;
      } else {
        setDuplicateWorkingExpIndex("-1");
      }

      if (validateForm() && checkUniqueEngineer("checkValidity")) {
        setIsConfirmationModal(!isConfirmationModal);
      }
    } else if (isFormTypePartnersIndividual) {
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
            roleModel: [{ id: 2 }, { id: 3 }, { id: 4 }, { id: 5 }],
          },
        });
      } else {
        setFormValues({
          ...formValues,
          userModel: {
            ...formValues.userModel,
            name: sameAsAdmin[0]?.name,
            email: sameAsAdmin[0]?.email,
            phoneCode: sameAsAdmin[0]?.phoneCode,
            primaryPhone: sameAsAdmin[0]?.primaryPhone,
            onWhatsapp: sameAsAdmin[0]?.onWhatsapp,
            availableOn: sameAsAdmin[0]?.availableOn,
            sameASUser: "Yes",
            roleModel: [{ id: 2 }, { id: 3 }, { id: 4 }, { id: 5 }],
          },
        });
      }

      //duplicate equipment details
      if (checkDuplicacyWorkingExperience()?.status === true) {
        return;
      } else {
        setDuplicateWorkingExpIndex("-1");
      }

      if (checkUniqueEngineer("checkValidity")) {
        setIsConfirmationModal(!isConfirmationModal);
      }
    }
  };

  const getTokenFunc = () => {
    if (getAuthToken()) {
      return getAuthToken();
    }
    return getToken();
  };

  const apiCall = () => {
    if (isFormTypePartner) {
      if (validateForm()) {
        if (engineerList.length === 0) {
          let formValuesClone = JSON.parse(JSON.stringify(formValues));
          if (formValuesClone["userModel"]["onWhatsapp"] === true) {
            formValuesClone.userModel.availableOn = "";
          }
          dispatch(saveEngineer([formValuesClone]));
        } else {
          const transformedLastEngineerObject = [...engineerList];
          transformedLastEngineerObject[engineerList.length - 1] = formValues;
          setengineerList(transformedLastEngineerObject);

          let engineerListClone = JSON.parse(
            JSON.stringify(transformedLastEngineerObject)
          );
          const transformedEngineerList = engineerListClone.map((engineer) => {
            if (engineer["userModel"]["onWhatsapp"] === true) {
              engineer.userModel.availableOn = "";
            }
            return engineer;
          });
          dispatch(saveEngineer(transformedEngineerList));
        }
      }
    } else if (isFormTypePartnersIndividual) {
      if (engineerList.length === 0) {
        let formValuesClone = JSON.parse(JSON.stringify(formValues));
        if (formValuesClone["userModel"]["onWhatsapp"] === true) {
          formValuesClone.userModel.availableOn = "";
        }
        dispatch(saveEngineer([formValuesClone]));
      } else {
        const transformedLastEngineerObject = [...engineerList];
        transformedLastEngineerObject[engineerList.length - 1] = formValues;
        setengineerList(transformedLastEngineerObject);

        let engineerListClone = JSON.parse(
          JSON.stringify(transformedLastEngineerObject)
        );
        const transformedEngineerList = engineerListClone.map((engineer) => {
          if (engineer["userModel"]["onWhatsapp"] === true) {
            engineer.userModel.availableOn = "";
          }
          return engineer;
        });
        dispatch(saveEngineer(transformedEngineerList));
      }
    }
  };

  const addMoreWorkingExperience = (index) => {
    setFormValues({
      ...formValues,
      engineerModelList: [
        ...formValues.engineerModelList,
        {
          categoryModel: {
            id: "",
          },
          certificate: [
            {
              certificates: "",
              id: "",
            },
          ],
          certified: "",
          equipmentModel: { id: "", name: "" },
          experience: "",
          experienceYear: "",
          experienceMonth: "",
          make: {
            id: "",
          },
        },
      ],
    });
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

  const getSelectedCertificate = (e, index) => {
    console.log({ getSelectedCertificate: index });
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
      dispatch(
        fileUpload(
          formData,
          `file/upload?fileKey=${
            allSelectedFiles.item(0)?.name
          }&token=${getTokenFunc()}`,
          index
        )
      );
    }
    // Always put it into last of the function, otherwise same file doesn't upload multiple times
    e.target.value = "";
  };

  const nextNavigationHandler = () => {
    dispatch(saveEngineerSlice.engineerNavigation(true));
    dispatch(commonGetApiActions.dispatchGetRequest());
    props.navigationHandler({
      currentForm: "services",
      goForward: true,
    });
    props.setFormLabel("services", 3);
  };

  const checkDuplicacyWorkingExperience = () => {
    const transformedWorkingExpData = formValues?.["engineerModelList"].map(
      (exp) => {
        return {
          categoryModel: exp?.categoryModel?.id,
          equipmentModel: exp?.equipmentModel.id?.toString(),
          make: exp?.make?.id,
        };
      }
    );

    let duplicateIndex = "-1";
    for (let i = 0; i < transformedWorkingExpData.length - 1; i++) {
      for (let j = i + 1; j < transformedWorkingExpData.length; j++) {
        if (
          transformedWorkingExpData[i]["categoryModel"] ===
            transformedWorkingExpData[j]["categoryModel"] &&
          transformedWorkingExpData[i]["equipmentModel"] ===
            transformedWorkingExpData[j]["equipmentModel"] &&
          transformedWorkingExpData[i]["make"] ===
            transformedWorkingExpData[j]["make"]
        ) {
          duplicateIndex = j;
        }
      }
    }

    if (duplicateIndex !== "-1") {
      return {
        duplicateIndex,
        status: true,
      };
    } else {
      return {
        duplicateIndex,
        status: false,
      };
    }
  };

  const workingExperienceHandler = (index) => {
    const duplicateStatus = checkDuplicacyWorkingExperience();

    if (duplicateStatus.status == true) {
      setDuplicateWorkingExpIndex(parseInt(duplicateStatus?.duplicateIndex));
      return false;
    } else {
      setDuplicateWorkingExpIndex(parseInt(duplicateStatus?.duplicateIndex));
      setIsWorkingExperienceError(false);
      let flagForAddWorkingExperience = false;
      formValues.engineerModelList.forEach((data) => {
        if (
          data.categoryModel.id === "" ||
          data.equipmentModel.id === undefined ||
          data.experience === "" ||
          data.make.id === "" ||
          data.equipmentModel.name === ""
        ) {
          flagForAddWorkingExperience = true;
        }
      });

      if (flagForAddWorkingExperience === true) {
        setIsWorkingExperienceError(true);
      } else {
        addMoreWorkingExperience(index);
      }

      const otherStateClone = JSON.parse(JSON.stringify(otherState));
      otherStateClone[activeEngineer - 1].push(false);
      setOtherState(otherStateClone);
    }
  };

  // this need to be bottom of the page otherwise not working
  useEffect(() => {
    setFormValues(initialValues);
  }, []);

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

  const [ignored, forceUpdate] = useReducer((x) => x + 1, 0);
  const removeWorkingExperienceHandler = (index) => {
    if (formValues.engineerModelList.length > 1) {
      formValues.engineerModelList.splice(index, 1);
      const otherStateClone = JSON.parse(JSON.stringify(otherState));
      otherStateClone[activeEngineer - 1].splice(index, 1);
      setOtherState(otherStateClone);
      forceUpdate();
    }
  };

  const removeOtherOption = (index) => {
    const trasformedFormValues = [...formValues.engineerModelList];
    trasformedFormValues[index]["equipmentModel"] = { id: "", name: "" };
    setFormValues({
      ...formValues,
      engineerModelList: [...trasformedFormValues],
    });
  };

  const removeOtherOptionAtMake = (index) => {
    const updateEngineerModelList = [...formValues.engineerModelList];
    updateEngineerModelList[index]["make"] = { id: "" };
    const formValueClone = {
      ...formValues,
      engineerModelList: [...updateEngineerModelList],
    };
    setFormValues(formValueClone);
  };

  const showUploadFileHanlder = (index, value) => {
    console.log("showUploadFileHanlder", { index, value });
    setDataShowInDocumentModal(null);
    console.log({ index, value });

    setCurrentActiveRow(index);
    setIsShowDocumentModal(true);
    setDataShowInDocumentModal(value);
  };

  const okHandler = () => {
    setIsShowDocumentModal(!isShowDocumentModal);
  };

  const onRemoveHandler = (index, activeRow) => {
    console.log({ index, activeRow });

    const trasformedFormValues = { ...formValues };

    if (
      trasformedFormValues["engineerModelList"][activeRow]["certificate"]
        .length > 1
    ) {
      trasformedFormValues["engineerModelList"][activeRow][
        "certificate"
      ].splice(index, 1);
    } else {
      trasformedFormValues["engineerModelList"][activeRow]["certificate"][
        index
      ] = {
        certificates: "",
        id: "",
      };
    }

    console.log({ trasformedFormValues });

    setFormValues(trasformedFormValues);

    // setIsShowDocumentModal(false);
  };

  const equipmentHandler = (index) => {
    if (formValues.engineerModelList[index]["equipmentModel"]["id"] != null) {
      return (
        <>
          <option value="" selected>
            {formValues.engineerModelList[index]["equipmentModel"]["id"] == ""
              ? "Select"
              : formValues.engineerModelList[index]["equipmentModel"]["name"]}
          </option>

          {/* <option
            id={formValues.engineerModelList[index]["equipmentModel"]["id"]}
          >
            {formValues.engineerModelList[index]["equipmentModel"]["name"]}
          </option> */}

          {equipmentDropdownValues?.[activeEngineer - 1]?.[index]?.map(
            (equipment) => {
              return (
                <option key={equipment.id} value={JSON.stringify(equipment)}>
                  {equipment.name}
                </option>
              );
            }
          )}
        </>
      );
    } else {
      if (equipmentFlag) {
        return (
          <>
            <option value="" disabled selected>
              Select
            </option>
            {equipmentDropdownValues[activeEngineer - 1][index]?.map(
              (equipment) => (
                <option key={equipment.id} value={JSON.stringify(equipment)}>
                  {equipment.name}
                </option>
              )
            )}
          </>
        );
      }
    }
  };

  const addWorkingExperience = (index) => {
    return (
      <div className="equipment_container_body_row">
        <div className="equipment_container_body">
          <div className="equipment_col a_category">
            <label className="mobile_label">Category</label>
            <select
              className="a_form_control"
              name="categoryModel"
              value={formValues.engineerModelList[index].categoryModel.id}
              onChange={(event) => handleInputChange(event, index)}
              disabled={
                navigationResponse !== null ||
                (formValues.yesOrNo === "Yes" && formValues.engineerId)
                  ? true
                  : false
              }
            >
              <option value="" disabled selected>
                Select
              </option>
              {categories?.map((category) => (
                <option
                  key={category.id}
                  value={category.id}
                  className="option_font"
                  // style={{ fontFamily: "Poppins-Regular" }}
                >
                  {category.name}
                </option>
              ))}
            </select>
          </div>

          <div className="equipment_col a_equipment">
            <label className="mobile_label">Equipment</label>

            {
              <select
                className="a_form_control option_font"
                name="equipmentModel"
                // style={{ fontFamily: "Poppins-Regular" }}
                value={formValues.engineerModelList[index].equipmentModel.name}
                onChange={(event) => handleInputChange(event, index)}
                disabled={
                  navigationResponse !== null ||
                  (formValues.yesOrNo === "Yes" && formValues.engineerId)
                    ? true
                    : false
                }
              >
                {equipmentHandler(index)}
              </select>
            }

            {formValues.engineerModelList[index]["equipmentModel"]["id"] ==
              null && (
              <div className={`other_container other_${index}`}>
                <input
                  type="text"
                  name="equipmentModel-otherName"
                  className="other_text_btn"
                  autoFocus={true}
                  onChange={(event) => handleInputChange(event, index)}
                  value={
                    formValues.engineerModelList[index]["equipmentModel"][
                      "name"
                    ]
                  }
                />
                <img
                  src={cancelIcon}
                  alt="cancel icon"
                  className="other_cancel_btn"
                  onClick={(e) => removeOtherOption(index)}
                />
              </div>
            )}
          </div>

          <div className="equipment_col a_make">
            <label className="mobile_label">Make</label>
            {navigationResponse !== null && (
              <select className="a_form_control " name="make" disabled>
                <option value="" disabled selected>
                  {formValues.engineerModelList[index]["make"]["name"]}
                </option>
              </select>
            )}

            {(formValues.engineerModelList[index]["make"]["status"] ===
              undefined ||
              formValues.engineerModelList[index]["make"]["status"] === "" ||
              formValues.engineerModelList[index]["make"]["status"] ===
                "-1") && (
              <select
                className="a_form_control"
                name="make"
                value={formValues.engineerModelList[index]["make"]["id"]}
                onChange={(event) => handleInputChange(event, index)}
                disabled={
                  navigationResponse !== null ||
                  (formValues.yesOrNo === "Yes" && formValues.engineerId)
                    ? true
                    : false
                }
              >
                <option value="" disabled selected>
                  Select
                </option>
                {makeResponse?.map((make) => (
                  <option
                    value={make.id}
                    key={make.id}
                    className="option_font"
                    // style={{ fontFamily: "Poppins-Regular"}}
                  >
                    {make.name}
                  </option>
                ))}
              </select>
            )}

            {formValues.engineerModelList[index]["make"]["id"].toString() ===
              "0" && (
              <div className={`other_container_make other_${index}`}>
                <input
                  type="text"
                  name="make-otherName"
                  className="other_text_btn"
                  autoFocus={true}
                  onChange={(event) => handleInputChange(event, index)}
                  value={formValues.engineerModelList[index]["make"]["name"]}
                />
                <img
                  src={cancelIcon}
                  alt="cancel icon"
                  className="other_cancel_btn"
                  onClick={(e) => removeOtherOptionAtMake(index)}
                />
              </div>
            )}
          </div>
          <div className="equipment_col a_experience">
            <label className="mobile_label">Experience</label>
            <select
              className="a_form_control option_font"
              id={`experience_${index}`}
              name="experienceYear"
             
              // style={{ fontFamily: "Poppins-Regular" }}
              value={formValues.engineerModelList[index].experience}
              onChange={(event) => handleInputChange(event, index)}
              disabled={
                navigationResponse !== null ||
                (formValues.yesOrNo === "Yes" && formValues.engineerId)
                  ? true
                  : false
              }
            >
              <option value="" disabled selected>
                Select
              </option>
              <option value="<2 Years"> &lt; 2 Years</option>
              <option value="2 to 5 years"> 2 to 5 years</option>
              <option value="6 to 10 years"> 6 to 10 years</option>
              <option value="> Years"> &gt; 10 year</option>
            </select>
          </div>

          <div className="equipment_col a_upload_certificate">
            <label className="mobile_label">Upload Certificate</label>

            <label
              className={
                navigationResponse !== null
                  ? "a_upload"
                  : "a_upload set_background"
              }
              htmlFor={
                formValues?.engineerModelList[index]?.certificate[0][
                  "certificates"
                ] === "" && `certificate${index}`
              }
            >
              {formValues?.engineerModelList[index]?.certificate[0][
                "certificates"
              ] === "" && <p> Upload </p>}

              {formValues?.engineerModelList[index]?.certificate[0][
                "certificates"
              ] !== "" && (
                <div className="uploadFileName">
                  {
                    <div
                      onClick={() =>
                        showUploadFileHanlder(
                          index,
                          formValues?.engineerModelList[index]["certificate"]
                        )
                      }
                    >
                      View / Add
                    </div>
                  }
                </div>
              )}
            </label>

            <input
              type="file"
              key={index}
              id={`certificate${index}`}
              accept=".png, .jpg, .jpeg, .pdf"
              data-allowed-file-extension="png, jpg, jpeg, pdf"
              onChange={(e) => getSelectedCertificate(e, index)}
              disabled={navigationResponse != null ? true : false}
            />

            {/* {navigationResponse !== null
              ? ""
              : formValues?.engineerModelList[index]?.certificate
                  ?.certificates !== "" &&
                formValues?.engineerModelList[index]?.certificate
                  ?.certificates !== undefined && (
                  <div
                    onClick={(e) => clearUploadHandler(e, index)}
                    className="remove_logo_container"
                  >
                    <img
                      src={cancelIcon}
                      alt="cancelIcon"
                      className="cancel_icon"
                    />
                  </div>
                )} */}
          </div>

          <div
            className={`equipment_col a_add ${
              navigationResponse != null && "disable-anchor"
            }`}
          >
            <img
              src={additionIcon}
              alt="add row"
              className="add_more"
              onClick={(e) => workingExperienceHandler(index)}
              disabled={navigationResponse != null ? true : false}
            />
          </div>

          <div
            className={`equipment_col a_remove ${
              navigationResponse != null && "disable-anchor"
            }`}
          >
            <img
              src={removeIcon}
              alt="remove row"
              className="add_more"
              onClick={(e) => removeWorkingExperienceHandler(index)}
              disabled={navigationResponse != null ? true : false}
            />
          </div>
        </div>
      </div>
    );
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

  const clearUploadHandler = (e, index) => {
    setFormValues({
      ...formValues,
      engineerModelList: [
        ...formValues.engineerModelList.slice(0, index),
        {
          ...formValues.engineerModelList[index],
          certificate: {
            ...formValues.engineerModelList[index].certificate,
            certificates: "",
          },
        },
        ...formValues.engineerModelList.slice(index + 1),
      ],
    });
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
      },
    });
    setDataResponse(true);
  };

  const removeSelectOption = () => {
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
      },
    });
    setDataResponse(false);
    // document.getElementById("userType").value = "Select";
  };

  const removeEngineer = () => {
    setDuplicateWorkingExpIndex("-1");
    setactiveEngineer(activeEngineer - 1);
    const trasformedBidderCount = [...engineerListCount];
    trasformedBidderCount.splice(trasformedBidderCount.length - 1, 1);
    setengineerListCount(trasformedBidderCount);
    setFormValues(engineerList[engineerList.length - 1]);

    const transformedBidderList = [...engineerList];
    transformedBidderList.pop();
    setengineerList(transformedBidderList);
    setIsUserClickedRemoved(true);
  };

  return (
    <>
      {engineerModal && (
        <AdditionalCharges
          closeModal={engineerCostModalHandler}
          additionPriceForMoreThanTen={engineerCharges?.basePrice}
          additionPriceForMoreThanFifteen={engineerCharges?.extraAbove}
          additionPriceForMoreThanTwenty={engineerCharges?.extraPrice}
          additionPriceForMoreThanTwentyFive={engineerCharges?.maxCharge}
        />
      )}
      {isConfirmationAddNewData && (
        <ConfirmationAddNewData
          cancel={onCancelModal}
          ok={onConfirm}
          name={"Engineer"}
        />
      )}

      <div className="form_container_area">
        <div className="engineer_form positioning_container">
          {(isAvailableOnLoading ||
            isLoading_vendorEngineer ||
            is_Loading_engineerStatus ||
            dynamicEngineerPriceLoading ||
            fileUploadResIsLoading) && <Loading text="Loading..." />}
          {isConfirmationModal && (
            <ConfirmationModal cancel={onCancelHandler} ok={onOkHandler} />
          )}

          {isShowDocumentModal && (
            <CertificateModal
              clickOk={okHandler}
              data={isDataShhowInDocumentModal}
              onRemove={onRemoveHandler}
              activeRow={currentActiveRow}
              fileUpload={getSelectedCertificate}
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
                  <div
                    className="nav_item active"
                    onClick={() =>
                      props.navigationHandler({ currentForm: "approver" })
                    }
                  >
                    Approver
                  </div>
                  <div className="horizotal_row_container">
                    <span className="horizontal_row active">&nbsp;</span>
                  </div>
                  <div className="nav_item active">Engineer</div>
                </div>
              </div>
              {
                // <button
                //   onClick={() =>
                //     console.log({
                //       formValues,
                //       equipmentDropdownValues,
                //       activeEngineer,
                //     })
                //   }
                // >
                //   Get State
                // </button>
              }

              <div className="company_id_details">
                <p className="company_id">Company id: {getReferralCode()}</p>
              </div>

              <div className="add_bidder_container">
                {engineerListCount?.map((el, index) => {
                  if (activeEngineer === index + 1) {
                    return (
                      <div className="bidder active" key={index}>
                        <span className="bidder_text"> E{index + 1} </span>
                        {activeEngineer === engineerListCount.length &&
                          activeEngineer !== 1 &&
                          !isAddEngineerandSaveBtnActive && (
                            <span className="remove_bidder">
                              <img
                                src={cancel}
                                alt="cancel"
                                onClick={removeEngineer}
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
                        onClick={() => makeEngineerActive(index + 1)}
                      >
                        <span className="bidder_text"> E{index + 1} </span>
                      </div>
                    );
                  }
                })}
              </div>

              <form onSubmit={handleFormSubmission}>
                <div className="form_container">
                  <div className="form_control_spread">
                    <label className="form_label">
                      Do you want to add already registered Engineer?
                    </label>
                    <div className="radio_container">
                      <span className="a_control">
                        <input
                          type="radio"
                          name="registeredEngineer"
                          className="form_control_radio"
                          id="registeredEngineer-yes"
                          value="Yes"
                          checked={formValues?.yesOrNo === "Yes"}
                          onChange={handleInputChange}
                          disabled={navigationResponse != null ? true : false}
                        />
                        <label
                          className="form_label"
                          htmlFor="registeredEngineer-yes"
                        >
                          Yes
                        </label>
                      </span>
                      <span className="a_control">
                        <input
                          type="radio"
                          name="registeredEngineer"
                          className="form_control_radio"
                          id="registeredEngineer-no"
                          value="No"
                          checked={formValues?.yesOrNo === "No"}
                          onChange={handleInputChange}
                          disabled={navigationResponse != null ? true : false}
                        />
                        <label
                          className="form_label"
                          htmlFor="registeredEngineer-no"
                        >
                          No
                        </label>
                      </span>
                    </div>
                  </div>
                  {formValues?.yesOrNo === "Yes" && (
                    <div className="with_form_control_area">
                      <label className="form_label">
                        Engineer ID <span className="label_mandatory">*</span>
                        <Tooltip text="This the ID generated by AthMarine Platform for engineer when he was first registered." />
                      </label>
                      <input
                        type="text"
                        className="form_control"
                        value={formValues.engineerId}
                        name="engineerId"
                        onChange={handleInputChange}
                        onBlur={(e) => checkUniqueEngineer("callApi")}
                        disabled={navigationResponse != null ? true : false}
                      />
                      {engineerStatus?.userModel?.status === 0 &&
                      formValues.engineerId.length > 5 &&
                      activeEngineer > engineerListCount.length - 1 ? (
                        <>
                          <img src={warningIcon} alt="warning-icon" />
                          <span className="sub_label">
                            The entered Engineer ID is already active in another
                            company.
                          </span>
                        </>
                      ) : null}
                    </div>
                  )}
                  {formValues?.yesOrNo === "No" && (
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
                          {dataResponse && (
                            <img
                              src={cancelIcon}
                              alt="cancel icon"
                              className="remove_data"
                              onClick={removeSelectOption}
                            />
                          )}
                        </>
                      )}
                    </div>
                  )}

                  <div className="form_control_area">
                    <label className="form_label">
                      Name <span className="label_mandatory">*</span>
                      <Tooltip text="Full name as per passport / national ID." />
                    </label>
                    <input
                      type="text"
                      className="form_control"
                      name="name"
                      value={formValues?.userModel?.name}
                      onChange={handleInputChange}
                      disabled={
                        navigationResponse !== null ||
                        dataResponse ||
                        (formValues?.yesOrNo === "Yes" && formValues.engineerId)
                          ? true
                          : false
                      }
                    />
                    {errors && formValues.userModel.name.trim() === "" && (
                      <span className="error">Enter Full Name </span>
                    )}
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
                        handleInputChange(e);
                        validateEmail(e);
                      }}
                      disabled={
                        navigationResponse !== null ||
                        dataResponse ||
                        (formValues?.yesOrNo === "Yes" && formValues.engineerId)
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
                      {navigationResponse !== null ||
                      dataResponse ||
                      (formValues?.yesOrNo === "Yes" &&
                        formValues.engineerId) ? (
                        <select
                          className="select_input_disabled"
                          disabled={
                            navigationResponse !== null ||
                            dataResponse ||
                            (formValues?.yesOrNo === "Yes" &&
                              formValues.engineerId)
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
                            // onChange={(e) => {
                            //     setSelectedPhonePrefix(e.value )
                            // }}
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
                        disabled={
                          navigationResponse !== null ||
                          dataResponse ||
                          (formValues?.yesOrNo === "Yes" &&
                            formValues.engineerId)
                            ? true
                            : false
                        }
                      />
                    </div>
                    {errors &&
                      (!numberValidation(formValues.userModel.primaryPhone) ||
                        formValues.userModel.primaryPhone === "" ||
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
                      <Tooltip text="We use Whatsapp for convenience of communication with parties involved" />
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
                            navigationResponse !== null ||
                            dataResponse ||
                            (formValues?.yesOrNo === "Yes" &&
                              formValues.engineerId)
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
                            navigationResponse !== null ||
                            dataResponse ||
                            (formValues?.yesOrNo === "Yes" &&
                              formValues.engineerId)
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
                      name="availableOn"
                      value={
                        formValues?.userModel?.onWhatsapp === true
                          ? ""
                          : formValues?.userModel?.availableOn
                      }
                      onChange={handleInputChange}
                      disabled={
                        navigationResponse !== null ||
                        dataResponse ||
                        formValues?.userModel?.onWhatsapp ||
                        (formValues?.yesOrNo === "Yes" && formValues.engineerId)
                          ? true
                          : false
                      }
                    >
                      {navigationResponse != null || dataResponse ? (
                        <option className="form_control_disable" disabled>
                          {formValues?.userModel?.availableOn}
                        </option>
                      ) : formValues?.userModel?.onWhatsapp === true ? (
                        <input
                          className="form_control_disable"
                          disabled
                        ></input>
                      ) : (
                        <Select
                          options={socialMedia}
                          className="form_control_enable"
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

                  <div className="form_control_spread">
                    <label className="form_label">
                      Working Experience &amp; Certification
                      <span className="label_mandatory">*</span>
                      <Tooltip text="Elaborate as much as possible - this will be one of the criteria used by customer to assign jobs." />
                    </label>
                    <div
                      className={
                        navigationResponse !== null
                          ? "equipment_container"
                          : "equipment_container set_background"
                      }
                    >
                      <div className="nav_container_engineer">
                        <div className="nav_items_engineer">
                          <div className="nav_item_engineer">
                            Equipment Details
                          </div>
                        </div>
                      </div>
                      <div className="equipment_container_heading">
                        <span className="heading a_category">Category</span>
                        <span className="heading a_equipment">Name</span>
                        <span className="heading a_make">Make</span>
                        <span className="heading a_experience">Experience</span>
                        <span className="heading a_upload_certificate">
                          Certificate
                        </span>
                        <span className="heading a_add">&nbsp;</span>
                        <span className="heading a_add">&nbsp;</span>
                      </div>
                      <div className="display_carousel">
                        <AwesomeSlider className="aws-btn ">
                          {formValues?.engineerModelList?.map((_, index) =>
                            addWorkingExperience(index)
                          )}
                        </AwesomeSlider>
                      </div>

                      <div className="no_carousel">
                        {formValues?.engineerModelList?.map((_, index) =>
                          addWorkingExperience(index)
                        )}
                      </div>
                      {checkDuplicacyWorkingExperience()?.status && (
                        <div className="working_exp_error">
                          Duplicate entry not allowed
                        </div>
                      )}
                    </div>
                    {isWorkingExperienceError && (
                      <span className="error">
                        One or more fields are empty. Please Check
                      </span>
                    )}
                    <div className="instruction">
                      <span className="note_inst">NOTE:&nbsp;</span>
                      <span className="main_inst">
                        Up to 10 engineers can be added free of cost in the
                        basic registration fee – additional engineers can be
                        registered at a nominal fee.&nbsp;
                      </span>
                      <span
                        className="click_inst"
                        onClick={() => setEngineerModal(true)}
                      >
                        Check Here
                      </span>
                    </div>
                  </div>

                  <div className="form_submit_container">
                    {(vendorEngineer_Response && navigationCheck === false) ||
                    (registrationStatusKey - 6 > 0 &&
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
                            onClick={engineerSaveHandler}
                          />
                        )}
                        {isAddEngineerandSaveBtnActive && (
                          <>
                            <input
                              type="button"
                              value="Add Engineer"
                              className="btn btn_add_bidder"
                              onClick={() => addEngineer()}
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

          {/* ---------------- Partner individual form ---------------- */}
          {isFormTypePartnersIndividual && (
            <>
              <div className="nav_container">
                <div className="nav_items">
                  <div className="nav_item active">Professional Details</div>
                </div>
              </div>
              {
                // <button
                //   onClick={() => console.log({ formValues }, { engineerList })}
                // >
                //   Get State
                // </button>
              }

              <form onSubmit={handleFormSubmission}>
                <div className="form_container">
                  <div className="form_control_spread forBottomMargin">
                    <label className="form_label">
                      Working Experience &amp; Certification
                      <span className="label_mandatory">*</span>
                      <Tooltip text="Elaborate as much as possible - this will be one of the criteria used by customer to assign jobs." />
                    </label>
                    <div
                      className={
                        navigationResponse !== null
                          ? "equipment_container"
                          : "equipment_container set_background"
                      }
                    >
                      <div className="nav_container_engineer">
                        <div className="nav_items_engineer">
                          <div className="nav_item_engineer">
                            Equipment Details
                          </div>
                        </div>
                      </div>
                      <div className="equipment_container_heading">
                        <span className="heading a_category">Category</span>
                        <span className="heading a_equipment">Name</span>
                        <span className="heading a_make">Make</span>
                        <span className="heading a_experience">Experience</span>
                        <span className="heading a_upload_certificate">
                          Certificate
                        </span>
                        <span className="heading a_add">&nbsp;</span>
                        <span className="heading a_add">&nbsp;</span>
                      </div>
                      <div className="display_carousel">
                        <AwesomeSlider className="aws-btn ">
                          {formValues?.engineerModelList?.map((_, index) =>
                            addWorkingExperience(index)
                          )}
                        </AwesomeSlider>
                      </div>

                      <div className="no_carousel">
                        {formValues?.engineerModelList?.map((_, index) =>
                          addWorkingExperience(index)
                        )}
                      </div>
                      {checkDuplicacyWorkingExperience()?.status && (
                        <div className="working_exp_error">
                          Duplicate entry not allowed
                        </div>
                      )}
                    </div>
                    {isWorkingExperienceError && (
                      <span className="error">
                        One or more fields are empty. Please Check
                      </span>
                    )}
                  </div>

                  <div className="form_submit_container engineer_btn">
                    {/* {console.log({
                      vendorEngineer_Response,
                      navigationCheck,
                      registrationStatusKey,
                      navigationCheck,
                      navigationResponse,
                    })} */}
                    {navigationResponse !== null ? (
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
                            onClick={engineerSaveHandler}
                          />
                        )}
                        {isAddEngineerandSaveBtnActive && (
                          <>
                            {/* <input
                              type="button"
                              value="Add Engineer"
                              className="btn btn_add_bidder"
                              onClick={() => addEngineer()}
                            /> */}
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
    </>
  );
};

export default Engineer;
