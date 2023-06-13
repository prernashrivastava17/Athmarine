/* eslint-disable react/jsx-no-undef */
/* eslint-disable no-unused-expressions */
/* eslint-disable no-lone-blocks */
import React, { useState, useEffect, useReducer } from "react";
import { useDispatch, useSelector } from "react-redux";
import AwesomeSlider from "react-awesome-slider";
import Select from "react-select";
import CreatableSelect from "react-select/creatable";
import dropdownIcon from "./../../../../../assets/images/drop-icon.svg";
import "./Services.scss";
import Loading from "../../../../../components/Loading/Loading";
import { ToastError } from "../../../../../components/Tostify";
import ConfirmationModal from "../../Modals/ConfirmationModal/ConfirmationModal";
import addRowIcon from "./../../../../../assets/images/addition-icon.png";
import removeRowIcon from "./../../../../../assets/images/remove-icon.png";
import ConfirmationAddNewData from "../../Modals/confirmationAddNewData/confirmationAddNewData";
import cancel from "./../../../../../assets/images/cancel.png";
import cancelForOther from "./../../../../../assets/images/cancel_other_icon.png";

import {
  getAuthData,
  getCompanyId,
  getReferralCode,
} from "../../../../../config";
import { getCountries } from "../../../../../store/common/country";
import { getStates } from "../../../../../store/common/state";
import { getPortsByStateId } from "../../../../../store/common/getPortByStateId";
import { getPortsByCountryId } from "../../../../../store/common/getPortByCountry";
import {
  createServiceFunc,
  createServiceActions,
} from "../../../../../store/vendorRegistration/createService";
import {
  commonGetAPiCall,
  commonGetApiActions,
} from "../../../../../store/common/commonGetApi";

import { getAllEquipmentCategory } from "../../../../../store/vendorRegistration/getAllEquipmentCategory";
import { getEquipmentByCategory } from "../../../../../store/vendorRegistration/equipmentByCategory";
import { getAllEquipmentManufacturer } from "../../../../../store/vendorRegistration/getAllEquipmentManufacturer";

const Services = (props) => {
  const dispatch = useDispatch();
  const portListRef = React.createRef();

  let isFormTypePartner = props.selectedUser === "partner" ? true : false;
  let isFormTypeCustomer = props.selectedUser === "customer" ? true : false;
  let isFormTypePartnersIndividual =
    props.selectedUser === "partners-individual" ? true : false;

  const isCreateServiceLoading = useSelector(
    (state) => state.createServiceReducer.isLoading
  );
  const navigationResponse = useSelector(
    (state) => state.commonGetApiReducer.getApiRes
  );
  const isNavigationResponseResponse = useSelector(
    (state) => state.commonGetApiReducer.isError
  );
  const registrationStatusKey = useSelector(
    (state) => state.commonGetApiReducer.registrationStatusKey
  );
  const createServiceRes = useSelector(
    (state) => state.createServiceReducer.createServiceRes
  );
  const isCreateServiceResError = useSelector(
    (state) => state.createServiceReducer.isError
  );
  const navigationCheck = useSelector(
    (state) => state.createServiceReducer.navigationCheck
  );

  useEffect(() => {
    if (navigationResponse !== null) {
      setHaveSavePortData(true);
      const servicePortList = navigationResponse.map((service, index) => {
        return {
          country: service?.country?.id,
          port: service?.country?.portId.map((port) => {
            return {
              label: port.name,
              value: port.id,
            };
          }),
        };
      });

      const tempServicePortList = [...servicePortList];
      tempServicePortList.pop();
      setPortList(tempServicePortList);
      setServiceList(navigationResponse);
      setPortFormValues(servicePortList[servicePortList?.length - 1]);
      let portCount = servicePortList?.map((item, index) => index + 1);
      setPortListCount(portCount);
      setactivePort(servicePortList?.length);

      let equipmentData = navigationResponse.map((service, index) => {
        const serviceEquipment = service?.equipmentCategory?.[0];

        if (navigationResponse.length - 1 === index) {
          return {
            equipmentCategoryId: {
              id: serviceEquipment?.["equipmentCategoryId"]?.["id"],
            },
            serviceDetailsModels: [
              ...serviceEquipment?.["equipmentCategoryId"]?.[
                "serviceDetailsModels"
              ],
            ],
          };
        }
      });

      let equipmentDataSecond = navigationResponse.map((service, index) => {
        const serviceEquipmentSecond = service?.equipmentCategory;

        if (navigationResponse.length - 1 === index) {
          return serviceEquipmentSecond.map((equipmentCategory) => {
            return {
              equipmentCategoryId: {
                id: equipmentCategory?.["equipmentCategoryId"]?.["id"],
              },
              serviceDetailsModels: [
                ...equipmentCategory?.["equipmentCategoryId"]?.[
                  "serviceDetailsModels"
                ],
              ],
            };
          });
        }
      });

      equipmentData = equipmentData.filter((el) => el !== undefined);
      equipmentDataSecond = equipmentDataSecond.filter(
        (el) => el !== undefined
      );

      const trasformedEquipmentList = [...equipmentDataSecond].flat(1);
      setEquipmentList(trasformedEquipmentList);
      setEquipmentFormValues(equipmentData?.[equipmentData.length - 1]);
      let equippmentCount = trasformedEquipmentList?.map(
        (item, index) => index + 1
      );
      setEquipmentListCount(equippmentCount);
      setActiveEquipment(trasformedEquipmentList.length);

      let dummyEquipmentData = navigationResponse.map((service, index) => {
        const serviceEquipment = service?.equipmentCategory?.[0];

        const serviceEquipmentSecond = service?.equipmentCategory;

        return serviceEquipmentSecond.map((service) => {
          return {
            equipmentCategoryId: {
              id: service?.["equipmentCategoryId"]?.["id"],
            },
            serviceDetailsModels: [
              ...service?.["equipmentCategoryId"]?.["serviceDetailsModels"],
            ],
          };
        });
      });

      setDummyEquipmentList(dummyEquipmentData);
      setForNavigation(
        navigationResponse[navigationResponse.length - 1]?.vendorServiceKey
      );
      setIsShowEquipmentRemoveIcon(servicePortList.map((el) => false));
    }
  }, [navigationResponse]);

  useEffect(() => {
    if (navigationCheck) {
      if (
        createServiceRes !== null &&
        createServiceRes.vendorServiceKey === true
      ) {
        props.setFormLabel("finance", 4);
      }
    }
  }, [createServiceRes, props, navigationCheck]);

  useEffect(() => {
    if (registrationStatusKey - 7 > 0 && isFormTypePartner) {
      dispatch(
        commonGetAPiCall(`/service/getAllServiceData/${getCompanyIdCheck()}`)
      );
    } else if (registrationStatusKey - 7 > 0 && isFormTypePartnersIndividual) {
      dispatch(
        commonGetAPiCall(`/service/getAllServiceData/${getCompanyIdCheck()}`)
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
    if (!navigationCheck && createServiceRes !== null && isFormTypePartner) {
      dispatch(
        commonGetAPiCall(`/service/getAllServiceData/${getCompanyId()}`)
      );
    } else if (
      !navigationCheck &&
      createServiceRes !== null &&
      isFormTypePartnersIndividual
    ) {
      dispatch(
        commonGetAPiCall(`/service/getAllServiceData/${getCompanyId()}`)
      );
    }
  }, [
    dispatch,
    isFormTypePartner,
    createServiceRes,
    isFormTypePartnersIndividual,
    navigationCheck,
  ]);

  // *************************************** Port ***************************************

  useEffect(() => {
    dispatch(getCountries());
    dispatch(getAllEquipmentManufacturer(1));
  }, [dispatch]);

  const countries = useSelector((state) => state.countries.countries);
  const isCountryLoading = useSelector((state) => state.countries.isLoading);
  const isCountryError = useSelector((state) => state.countries.isError);
  const portByCountryId = useSelector(
    (state) => state.CountryPortsReducer.CountryPorts
  );

  const [allCountries, setAllCountries] = useState(null);
  const isPortByCountryIdLoading = useSelector(
    (state) => state.CountryPortsReducer.isLoading
  );
  const isPortByCountryIdError = useSelector(
    (state) => state.CountryPortsReducer.isError
  );

  const portInitialState = {
    country: "",
    // state: [],
    port: [],
  };

  const handleLettersOnlyInput = (e) => {
    if (Number.isInteger(Number(e.key))) {
      e.preventDefault();
    }
  };

  useEffect(() => {
    if (navigationResponse !== null && countries !== null) {
      setServiceCountryValues([]);
      const alreadySelectedCountiesTemp = navigationResponse.map((res) => {
        return res?.country?.name;
      });
      setServiceCountryValues([...alreadySelectedCountiesTemp]);
    }
  }, [navigationResponse, countries]);

  // *************************************** Service ***************************************
  const [serviceCountryValues, setServiceCountryValues] = useState([]);
  const [serviceList, setServiceList] = useState([]);

  const [dummyEquipmentFormValues, setDummyEquipmentFormValues] = useState({
    equipmentCategoryId: { id: "" },
    serviceDetailsModels: [
      {
        equipmentExperience: "",
        equipmentModel: { id: "", name: "" },
        manufacturerModule: { id: "", name: "" },
      },
    ],
  });

  const equipmentInitialValues = {
    equipmentCategoryId: { id: "" },
    serviceDetailsModels: [
      {
        equipmentExperience: "",
        equipmentModel: { id: "", name: "" },
        manufacturerModule: { id: "", name: "" },
      },
    ],
  };

  const [equipmentFormValues, setEquipmentFormValues] = useState(
    equipmentInitialValues
  );
  const [equipmentList, setEquipmentList] = useState([]);
  const [equipmentListCount, setEquipmentListCount] = useState([1]);
  const [activeEquipment, setActiveEquipment] = useState(1);
  const [isSaveBtnActiveEquipment, setIsSaveBtnActiveEquipment] =
    useState(false);
  const [isAddEquipmentandSaveBtnActive, setisAddEquipmentandSaveBtnActive] =
    useState(true);
  const [lastEquipmentBtnStatus, setlastEquipmentBtnStatus] = useState(false);
  const [lastEquipmentFilledData, setlastEquipmentFilledData] = useState(
    equipmentInitialValues
  );
  const [isWorkingExperienceError, setIsWorkingExperienceError] =
    useState(false);
  const [equipmentDropdownData, setEquipmentDropdownData] = useState([]);
  const [categoryData, setCategoryData] = useState();
  const [dummyEquipmentList, setDummyEquipmentList] = useState([]);
  const [isUserClickedEquipmentRemoved, setIsUserClickedEquipmentRemoved] =
    useState(false);
  const [isUserClickedPortRemoved, setIsUserClickedPortRemoved] =
    useState(false);
  const [isShowPortRemoveIcon, setIsShowPortRemoveIcon] = useState(false);
  const [isShowEquipmentRemoveIcon, setIsShowEquipmentRemoveIcon] = useState([
    true,
  ]);
  const [isDuplicacyError, setIsDuplicacyError] = useState(false);

  // *************************************** Service ***************************************

  const [portFormValues, setPortFormValues] = useState(portInitialState);
  const [portList, setPortList] = useState([]);
  const [portListCount, setPortListCount] = useState([1]);
  const [activePort, setactivePort] = useState(1);
  const [isSaveBtnActivePort, setIsSaveBtnActivePort] = useState(false);
  const [isAddPortandSaveBtnActive, setisAddPortandSaveBtnActive] =
    useState(true);
  const [lastPortBtnStatus, setlastPortBtnStatus] = useState(false);
  const [lastPortFilledData, setlastPortFilledData] =
    useState(portInitialState);
  const [errors, setErrors] = useState(false);
  const [errorsEquipment, setErrorsEquipment] = useState(false);
  const [isConfirmationModal, setIsConfirmationModal] = useState(false);
  const [toggleOptions, setToggleOptions] = useState(false);
  const [unAllocatedData, setUnAllocatedData] = useState([]);
  const [retainedPortData, setRetainedPortData] = useState([]);
  const [isConfirmationAddNewData, setIsConfirmationAddNewData] =
    useState(false);
  const [isConfirmationAddNewEquipment, setIsConfirmationAddNewEquipment] =
    useState(false);
  const [hide, setHide] = useState();
  const [countryName, setCountryName] = useState();
  const [equipmentCategoryData, setEquipmentCategoryData] = useState([]);
  const [forNavigation, setForNavigation] = useState(false);
  const [runLimit, setRunLimit] = useState(0);
  const [portData, setPortData] = useState([]);
  const [portListDropdown, setPortListDropdown] = useState(false);
  const [haveSavePortData, setHaveSavePortData] = useState(false);

  const validatePortForm = () => {
    if (portFormValues.country === "") {
      setErrors(true);
      return false;
    } else {
      setErrors(false);
      return true;
    }
  };

  const handlePortChange = (e, selectedList) => {
    setPortFormValues({ country: e.value[0], port: [] });
    setCountryName(e.value[1]);
    dispatch(getPortsByCountryId(e.value[0]));
    setPortData([]);
  };

  useEffect(() => {
    if (navigationResponse !== null && countries !== null) {
      setServiceCountryValues([]);
      const alreadySelectedCountiesTemp = navigationResponse.map((res) => {
        return res?.country?.name;
      });
      setServiceCountryValues([...alreadySelectedCountiesTemp]);
    }
  }, [navigationResponse, countries]);

  useEffect(() => {
    setUnAllocatedData(portByCountryId);
    if (portByCountryId !== null) {
      const transformedRetainedPortData = JSON.parse(
        JSON.stringify(retainedPortData)
      );
      transformedRetainedPortData[activePort - 1] = portByCountryId;
      setRetainedPortData(transformedRetainedPortData);
    }
  }, [portByCountryId]);

  const addPortToTheList = (e) => {
    const id = Number(e.target.getAttribute("data-id"));
    const name = e.target.getAttribute("data-name");
    const newPortToAddIntoList = { id, name };

    // Add into selection
    setPortFormValues({
      ...portFormValues,
      port: [...portFormValues.port, newPortToAddIntoList],
    });

    // Remove from the list
    let transformedRetainedPortData = JSON.parse(
      JSON.stringify(retainedPortData)
    );

    transformedRetainedPortData[activePort - 1] = transformedRetainedPortData[
      activePort - 1
    ].filter((port) => {
      return port.id !== newPortToAddIntoList.id;
    });
    setRetainedPortData(transformedRetainedPortData);
  };

  const removePortFromSelection = (e) => {
    const id = Number(e.target.getAttribute("data-id"));
    const name = e.target.getAttribute("data-name");
    const oldPortToRemoveIntoList = { id, name };

    // Remove from selection
    const currentFormPortClone = [...portFormValues.port];
    const transformedPort = currentFormPortClone.filter(
      (port) => port.id !== oldPortToRemoveIntoList.id
    );
    setPortFormValues({ ...portFormValues, port: [...transformedPort] });

    // Back to add into list
    let transformedRetainedPortData = JSON.parse(
      JSON.stringify(retainedPortData)
    );

    transformedRetainedPortData[activePort - 1].push(oldPortToRemoveIntoList);
    transformedRetainedPortData[activePort - 1].sort((a, b) =>
      a.name.localeCompare(b.name)
    );
    setRetainedPortData(transformedRetainedPortData);
  };

  const addPortToListByCreateableSelect = (e) => {
    setPortData(e);

    const newPortToAddToList = e.map((port, index) => {
      if (port.__isNew__ === true) {
        return {
          portId: port.value,
          portValue: port.label,
          type: "new",
        };
      } else {
        return {
          portId: port.value,
          portValue: port.label,
          type: "old",
        };
      }
    });

    setPortFormValues({
      ...portFormValues,
      port: [...newPortToAddToList],
    });
  };

  const showCustomSelectionBox = () => {
    setErrors(false);
    setToggleOptions(true);
  };

  const hideCustomSelectionBox = () => {
    setToggleOptions(false);
  };

  const savePortData = (flag) => {
    if (flag === "already-exists") {
      let ports = portList.slice();
      ports[activePort - 1] = portFormValues;
      setPortList(ports);
    } else {
      setPortList([...portList, portFormValues]);
    }
  };

  const addPort = () => {
    if (!validatePortForm()) return false;
    if (activePort === 1) {
      savePortData();
      setPortListCount([...portListCount, Number(portListCount.length) + 1]);
      setactivePort(activePort + 1);
      setIsSaveBtnActivePort(true);
      setisAddPortandSaveBtnActive(false);
      setPortFormValues(portInitialState);
    } else {
      setPortListCount([...portListCount, Number(portListCount.length) + 1]);
      setactivePort(activePort + 1);
      setIsSaveBtnActivePort(true);
      setisAddPortandSaveBtnActive(false);
      setPortFormValues(portInitialState);
    }
  };

  const portSaveHandler = () => {
    if (!validatePortForm()) return false;

    if (isUserClickedPortRemoved) {
      if (portList.length > 0) {
        setPortList([...portList, portFormValues]);
        setIsUserClickedPortRemoved(false);
      }
    }

    if (activePort < portListCount.length) {
      savePortData("already-exists");
      if (lastPortBtnStatus === true) {
        setPortFormValues({ ...portList[portListCount.length - 1] });
        setactivePort(portListCount.length);
        setIsSaveBtnActivePort(false);
        setisAddPortandSaveBtnActive(true);
      } else {
        setactivePort(portListCount.length);
        setIsSaveBtnActivePort(true);
        setisAddPortandSaveBtnActive(false);
        setPortFormValues(lastPortFilledData);
      }
    } else if (activePort === portListCount.length) {
      if (isUserClickedPortRemoved) {
        setIsUserClickedPortRemoved(false);
      } else {
        savePortData();
      }
      setIsSaveBtnActivePort(false);
      setisAddPortandSaveBtnActive(true);
      if (activePort === 1) {
        setlastPortBtnStatus(false);
      } else {
        setlastPortBtnStatus(true);
      }
    }
  };

  const makePortActive = (activeBidderSerial) => {
    setIsWorkingExperienceError(false);
    setIsDuplicacyError(false);
    if (
      activeBidderSerial === serviceList.length &&
      navigationResponse !== null &&
      navigationResponse.length === serviceList.length
    ) {
      const servicePortList = navigationResponse.map((service, index) => {
        return {
          country: service?.country?.id,
          port: service?.country?.portId.map((port) => {
            return {
              label: port.name,
              value: port.id,
            };
          }),
        };
      });

      setPortFormValues(servicePortList[servicePortList?.length - 1]);

      setactivePort(servicePortList?.length);

      let equipmentData = navigationResponse.map((service, index) => {
        const serviceEquipment = service?.equipmentCategory?.[0];

        if (navigationResponse.length - 1 === index) {
          return {
            equipmentCategoryId: {
              id: serviceEquipment?.["equipmentCategoryId"]?.["id"],
            },
            serviceDetailsModels: [
              ...serviceEquipment?.["equipmentCategoryId"]?.[
                "serviceDetailsModels"
              ],
            ],
          };
        }
      });

      let equipmentDataSecond = navigationResponse.map((service, index) => {
        const serviceEquipmentSecond = service?.equipmentCategory;

        if (navigationResponse.length - 1 === index) {
          return serviceEquipmentSecond.map((equipmentCategory) => {
            return {
              equipmentCategoryId: {
                id: equipmentCategory?.["equipmentCategoryId"]?.["id"],
              },
              serviceDetailsModels: [
                ...equipmentCategory?.["equipmentCategoryId"]?.[
                  "serviceDetailsModels"
                ],
              ],
            };
          });
        }
      });

      equipmentData = equipmentData.filter((el) => el !== undefined);
      equipmentDataSecond = equipmentDataSecond.filter(
        (el) => el !== undefined
      );

      const trasformedEquipmentList = [...equipmentDataSecond].flat(1);
      setEquipmentList(trasformedEquipmentList);
      setEquipmentFormValues(equipmentData?.[equipmentData.length - 1]);
      let equippmentCount = trasformedEquipmentList?.map(
        (item, index) => index + 1
      );
      setEquipmentListCount(equippmentCount);
      setActiveEquipment(trasformedEquipmentList.length);

      let dummyEquipmentData = navigationResponse.map((service, index) => {
        const serviceEquipment = service?.equipmentCategory?.[0];

        const serviceEquipmentSecond = service?.equipmentCategory;

        return serviceEquipmentSecond.map((service) => {
          return {
            equipmentCategoryId: {
              id: service?.["equipmentCategoryId"]?.["id"],
            },
            serviceDetailsModels: [
              ...service?.["equipmentCategoryId"]?.["serviceDetailsModels"],
            ],
          };
        });
      });

      setDummyEquipmentList(dummyEquipmentData);
    } else {
      setErrors(false);
      setUnAllocatedData([]);
      setEquipmentList(dummyEquipmentList);
      setActiveEquipment(1);
      setEquipmentListCount(() => {
        return dummyEquipmentList.map((el, index) => index);
      });

      if (activePort === portListCount.length) {
        if (navigationResponse != null) {
          setlastPortFilledData(portList[portList.length - 1]);
        } else {
          setlastPortFilledData(portFormValues);
        }
      }
      setactivePort((prevState) => {
        innerPortActive(activeBidderSerial);
        return activeBidderSerial;
      });

      // *******************************************************
      if (activePort === serviceList.length - 1) {
        // setPortFormValues({
        //   country: serviceList?.[activePort]?.["country"]?.["id"],
        //   port: serviceList?.[activePort]?.["country"]?.["portId"],
        // });
      }
    }
  };

  const innerPortActive = (portSerial) => {
    setEquipmentList(dummyEquipmentList[portSerial - 1]);
    setActiveEquipment(1);

    if (portSerial === portListCount.length) {
      setEquipmentListCount([1]);
      if (!navigationResponse) {
        setEquipmentFormValues(equipmentInitialValues);
        setEquipmentList([]);
      } else {
        if (portSerial === portListCount.length) {
          if (portList.length === portSerial) {
            // update formvalues with the port list array
            setPortFormValues(portList[portSerial.length - 1]);
            setEquipmentFormValues(
              dummyEquipmentList[portSerial - 1][
                dummyEquipmentList[portSerial - 1].length - 1
              ]
            );
            setEquipmentList(dummyEquipmentList[portSerial - 1]);
            setEquipmentListCount(
              dummyEquipmentList[portSerial - 1].map((_, index) => index)
            );
            setActiveEquipment(dummyEquipmentList[portSerial - 1].length);
          } else {
            if (portSerial - 1 === serviceList.length - 1) {
              setPortFormValues({
                country: serviceList?.[portSerial - 1]?.["country"]?.["id"],
                port: serviceList?.[portSerial - 1]?.["country"]?.["portId"],
              });

              const currentEquipmentData = {
                ...serviceList[portSerial - 1],
              };
              const transformedcurrentEquipmentData =
                currentEquipmentData?.equipmentCategory.map((equipment) => {
                  return {
                    equipmentCategoryId: {
                      id: equipment?.equipmentCategoryId?.id,
                    },
                    serviceDetailsModels:
                      equipment?.equipmentCategoryId?.serviceDetailsModels,
                  };
                });

              setEquipmentFormValues(
                transformedcurrentEquipmentData[
                  transformedcurrentEquipmentData.length - 1
                ]
              );
              setEquipmentList(transformedcurrentEquipmentData);
              setActiveEquipment(transformedcurrentEquipmentData.length);
              setEquipmentListCount(
                transformedcurrentEquipmentData.map(
                  (equipment, index) => index + 1
                )
              );
            } else {
              setPortFormValues(portInitialState);
              setEquipmentFormValues(equipmentInitialValues);
              setEquipmentList([]);
            }
          }
        }
      }
      // setEquipmentList([]);
      setIsSaveBtnActiveEquipment(false);
      setlastEquipmentBtnStatus(false);
      setisAddEquipmentandSaveBtnActive(true);
    } else {
      if (dummyEquipmentList[portSerial - 1] == 0) {
        setEquipmentListCount([1]);
        let currentServiceListFormValues = serviceList[portSerial - 1];
        currentServiceListFormValues = {
          equipmentCategoryId: {
            id: currentServiceListFormValues?.["equipmentCategory"][0]?.[
              "equipmentCategoryId"
            ]?.["id"],
          },
          serviceDetailsModels:
            currentServiceListFormValues?.["equipmentCategory"][0]?.[
              "equipmentCategoryId"
            ]?.["serviceDetailsModels"],
        };
        setEquipmentFormValues(currentServiceListFormValues);
      } else {
        setEquipmentFormValues(dummyEquipmentList[portSerial - 1][0]);
        setEquipmentListCount(() => {
          return dummyEquipmentList[portSerial - 1]?.map((el, index) => index);
        });
      }
    }

    if (portSerial === portListCount.length) {
      if (!navigationResponse) {
        setPortFormValues(lastPortFilledData);

        if (serviceList.length === portSerial) {
          const currentEquipmentData = {
            ...serviceList[portSerial - 1],
          };
          const transformedcurrentEquipmentData =
            currentEquipmentData?.equipmentCategory.map((equipment) => {
              return {
                equipmentCategoryId: { id: equipment?.equipmentCategoryId?.id },
                serviceDetailsModels:
                  equipment?.equipmentCategoryId?.serviceDetailsModels,
              };
            });

          setEquipmentFormValues(
            transformedcurrentEquipmentData[
              transformedcurrentEquipmentData.length - 1
            ]
          );
          setEquipmentList(transformedcurrentEquipmentData);
          setActiveEquipment(transformedcurrentEquipmentData.length);
          setEquipmentListCount(
            transformedcurrentEquipmentData.map((equipment, index) => index + 1)
          );
        }
      }

      if (lastPortBtnStatus === true) {
        setIsSaveBtnActivePort(false);
        setisAddPortandSaveBtnActive(true);
      } else {
        setIsSaveBtnActivePort(true);
        setisAddPortandSaveBtnActive(false);
      }
    } else {
      setPortFormValues({ ...portList[portSerial - 1] });
      setIsSaveBtnActivePort(true);
      setisAddPortandSaveBtnActive(false);
    }
  };

  // *************************************** Equipments ***************************************

  const equipmentCategoryRes = useSelector(
    (state) => state.getAllEquipmentReducer.getAllEquipmentRes
  );
  const equipmentCategoryResisLoading = useSelector(
    (state) => state.getAllEquipmentReducer.isLoading
  );

  const equipmentNameRes = useSelector(
    (state) => state.engineerEquipments.equipments
  );
  const equipmentNameResisLoading = useSelector(
    (state) => state.engineerEquipments.isLoading
  );

  const equipmentManufacturerRes = useSelector(
    (state) =>
      state.getAllEquipmentManufacturerReducer.getAllEquipmentManufacturerRes
  );
  const equipmentManufacturerResisLoading = useSelector(
    (state) => state.getAllEquipmentManufacturerReducer.isLoading
  );

  useEffect(() => {
    dispatch(getAllEquipmentCategory());
  }, [dispatch]);

  useEffect(() => {
    if (equipmentNameRes !== null) {
      const transformedEquipmentDropdownData = JSON.parse(
        JSON.stringify(equipmentDropdownData)
      );
      transformedEquipmentDropdownData[activeEquipment - 1] = equipmentNameRes;
      setEquipmentDropdownData(transformedEquipmentDropdownData);
    }
  }, [equipmentNameRes]);

  const addMoreWorkingExperience = (index) => {
    setEquipmentFormValues({
      ...equipmentFormValues,
      serviceDetailsModels: [
        ...equipmentFormValues.serviceDetailsModels,
        {
          equipmentExperience: "",
          equipmentModel: { id: "", name: "" },
          manufacturerModule: { id: "", name: "" },
        },
      ],
    });
  };

  const ValidateFormEquipment = () => {
    const lastEquipment =
      equipmentFormValues.serviceDetailsModels[
        equipmentFormValues.serviceDetailsModels.length - 1
      ];

    if (equipmentFormValues?.equipmentCategoryId?.id === "") {
      setErrorsEquipment(true);
      return false;
    }

    if (
      equipmentFormValues?.equipmentCategoryId?.id !== "" &&
      lastEquipment?.equipmentModel?.id !== 0 &&
      lastEquipment?.manufacturerModule?.id &&
      lastEquipment?.equipmentExperience
    ) {
      setErrorsEquipment(false);
      return true;
    } else {
      if (
        lastEquipment?.equipmentModel?.id === 0 &&
        lastEquipment?.equipmentModel?.name === ""
      ) {
        setErrorsEquipment(true);
        return false;
      } else {
        setErrorsEquipment(false);
        return true;
      }
    }
  };

  const handleEquipmentChangeEquipment = (e) => {
    setCategoryData(e.value);
    setEquipmentFormValues({
      ...equipmentFormValues,
      equipmentCategoryId: {
        id: e.value,
      },
      serviceDetailsModels: equipmentInitialValues.serviceDetailsModels,
    });
    dispatch(getEquipmentByCategory(e.value));
  };

  const handleEquipmentChange = (e, index) => {
    let { name, value } = e.target;

    switch (name) {
      case "equipment-category":
        setEquipmentFormValues({
          ...equipmentFormValues,
          equipmentCategoryId: {
            id: value,
          },
          serviceDetailsModels: equipmentInitialValues.serviceDetailsModels,
        });
        dispatch(getEquipmentByCategory(value));
        break;

      case "equipment-name":
        const equipmentId = value.split("@@")[0].trim();
        const equipmentName = value.split("@@")[1].trim();
        let tranformedServiceDetailsModel = [
          ...equipmentFormValues.serviceDetailsModels,
        ];
        tranformedServiceDetailsModel[index]["equipmentModel"]["id"] =
          equipmentId;
        tranformedServiceDetailsModel[index]["equipmentModel"]["name"] =
          equipmentName;
        tranformedServiceDetailsModel[index]["manufacturerModule"]["id"] = "";
        tranformedServiceDetailsModel[index]["manufacturerModule"]["name"] = "";
        tranformedServiceDetailsModel[index]["equipmentExperience"] = "";
        setEquipmentFormValues({
          ...equipmentFormValues,
          serviceDetailsModels: tranformedServiceDetailsModel,
        });

        // if (checkEmptyEquipmentRow().isEmpty) {
        //   setIsWorkingExperienceError(true);
        //   setIsDuplicacyError(false);
        // } else {
        //   setIsWorkingExperienceError(false);
        //   if (checkDuplicacyInEquipmentRow().status) {
        //     setIsDuplicacyError(true);
        //   } else {
        //     setIsDuplicacyError(false);
        //   }
        // }
        break;

      case "equipment_other_text":
        let tranformedServiceDetailsModelForOthers = [
          ...equipmentFormValues.serviceDetailsModels,
        ];
        tranformedServiceDetailsModelForOthers[index]["equipmentModel"][
          "id"
        ] = 0;
        tranformedServiceDetailsModelForOthers[index]["equipmentModel"][
          "name"
        ] = value;
        setEquipmentFormValues({
          ...equipmentFormValues,
          serviceDetailsModels: tranformedServiceDetailsModelForOthers,
        });
        break;

      case "equipment-manufacturer":
        const manufacturerId = value.split("@@")[0].trim();
        const manufacturerName = value.split("@@")[1].trim();
        const tranformedServiceDetailsModel1 = [
          ...equipmentFormValues.serviceDetailsModels,
        ];
        tranformedServiceDetailsModel1[index]["manufacturerModule"]["id"] =
          manufacturerId;
        tranformedServiceDetailsModel1[index]["manufacturerModule"]["name"] =
          manufacturerName;
        tranformedServiceDetailsModel1[index]["equipmentExperience"] = "";
        setEquipmentFormValues({
          ...equipmentFormValues,
          serviceDetailsModels: tranformedServiceDetailsModel1,
        });
        // if (checkEmptyEquipmentRow().isEmpty) {
        //   setIsWorkingExperienceError(true);
        //   setIsDuplicacyError(false);
        // } else {
        //   setIsWorkingExperienceError(false);
        //   if (checkDuplicacyInEquipmentRow().status) {
        //     setIsDuplicacyError(true);
        //   } else {
        //     setIsDuplicacyError(false);
        //   }
        // }
        break;

      case "manufacturer_other_text":
        let tranformedServiceManufacturerOthers = [
          ...equipmentFormValues.serviceDetailsModels,
        ];
        tranformedServiceManufacturerOthers[index]["manufacturerModule"][
          "id"
        ] = 0;
        tranformedServiceManufacturerOthers[index]["manufacturerModule"][
          "name"
        ] = value;
        setEquipmentFormValues({
          ...equipmentFormValues,
          serviceDetailsModels: tranformedServiceManufacturerOthers,
        });
        break;

      case "equipment-experience":
        const tranformedServiceDetailsModel2 = [
          ...equipmentFormValues.serviceDetailsModels,
        ];
        tranformedServiceDetailsModel2[index]["equipmentExperience"] = value;
        setEquipmentFormValues({
          ...equipmentFormValues,
          serviceDetailsModels: tranformedServiceDetailsModel2,
        });
        if (checkEmptyEquipmentRow().isEmpty) {
          setIsWorkingExperienceError(true);
          setIsDuplicacyError(false);
        } else {
          setIsWorkingExperienceError(false);
        }
        break;

      default:
      // do nothing
    }
  };

  const saveEquipmentData = (flag) => {
    if (flag === "already-exists") {
      let equipments = equipmentList.slice();
      equipments[activeEquipment - 1] = equipmentFormValues;
      setEquipmentList(equipments);
    } else {
      setEquipmentList([...equipmentList, equipmentFormValues]);
    }
  };

  const addEquipment = () => {
    // if (!ValidateFormEquipment()) return false;
    console.log("test90")
    setIsDuplicacyError(false);
    if (checkEmptyEquipmentRow().isEmpty) {
      setIsWorkingExperienceError(true);
      return;
    } else {
      setIsWorkingExperienceError(false);
      if (checkDuplicacyInEquipmentRow().status) {
        setIsDuplicacyError(true);
        return;
      } else {
        setIsDuplicacyError(false);
      }
    }
    if (!ValidateFormEquipment()) return false;
    setEquipmentCategoryData([...equipmentCategoryData, categoryData]);
    setlastEquipmentBtnStatus(false);
    if (activeEquipment === 1) {
      saveEquipmentData();
      setEquipmentListCount([
        ...equipmentListCount,
        Number(equipmentListCount.length) + 1,
      ]);
      setActiveEquipment(activeEquipment + 1);
      setIsSaveBtnActiveEquipment(true);
      setisAddEquipmentandSaveBtnActive(false);
      setEquipmentFormValues(equipmentInitialValues);
    } else {
      setEquipmentListCount([
        ...equipmentListCount,
        Number(equipmentListCount.length) + 1,
      ]);
      setActiveEquipment(activeEquipment + 1);
      setIsSaveBtnActiveEquipment(true);
      setisAddEquipmentandSaveBtnActive(false);
      setEquipmentFormValues(equipmentInitialValues);
    }
  };

  const equipmentSaveHandler = () => {
    if (!ValidateFormEquipment()) return false;

    if (isUserClickedEquipmentRemoved) {
      if (equipmentList.length > 0) {
        setEquipmentList([...equipmentList, equipmentFormValues]);
        setIsUserClickedEquipmentRemoved(false);
      }
    }

    if (activeEquipment < equipmentListCount.length) {
      saveEquipmentData("already-exists");
      if (lastEquipmentBtnStatus === true) {
        setEquipmentFormValues({
          ...equipmentList[equipmentListCount.length - 1],
        });
        setActiveEquipment(equipmentListCount.length);
        setIsSaveBtnActiveEquipment(false);
        setisAddEquipmentandSaveBtnActive(true);
      } else {
        setActiveEquipment(equipmentListCount.length);
        setIsSaveBtnActiveEquipment(true);
        setisAddEquipmentandSaveBtnActive(false);
        setEquipmentFormValues(lastEquipmentFilledData);
      }
    } else if (activeEquipment === equipmentListCount.length) {
      if (isUserClickedEquipmentRemoved) {
        setIsUserClickedEquipmentRemoved(false);
      } else {
        saveEquipmentData();
      }
      setIsSaveBtnActiveEquipment(false);
      setisAddEquipmentandSaveBtnActive(true);
      if (activeEquipment === 1) {
        setlastEquipmentBtnStatus(false);
      } else {
        setlastEquipmentBtnStatus(true);
      }
    }
  };

  const makeEquipmentActive = (activeBidderSerial) => {
    // setIsWorkingExperienceError(false);
    if (activeEquipment === equipmentListCount.length) {
      if (navigationResponse != null) {
        setlastEquipmentFilledData(equipmentList[equipmentList.length - 1]);
      } else {
        setlastEquipmentFilledData(equipmentFormValues);
      }
    }

    setActiveEquipment((prevState) => {
      innerEquipmentActive(activeBidderSerial);
      return activeBidderSerial;
    });
  };

  const innerEquipmentActive = (equipmentSerial) => {
    if (equipmentSerial === equipmentListCount.length) {
      {
        portListCount.length === activePort
          ? setEquipmentFormValues(lastEquipmentFilledData)
          : setEquipmentFormValues(equipmentList[equipmentList?.length - 1]);
      } //for last equipment field

      if (lastEquipmentBtnStatus === true) {
        setIsSaveBtnActiveEquipment(false);
        setisAddEquipmentandSaveBtnActive(true);
      } else {
        if (
          [false, true].includes(
            serviceList[activePort - 1]?.["vendorServiceKey"]
          )
        ) {
        } else {
          setIsSaveBtnActiveEquipment(true);
          setisAddEquipmentandSaveBtnActive(false);
        }
      }
    } else {
      setEquipmentFormValues({ ...equipmentList[equipmentSerial - 1] });
      if (
        [false, true].includes(
          serviceList[activePort - 1]?.["vendorServiceKey"]
        )
      ) {
      } else {
        setIsSaveBtnActiveEquipment(true);
        setisAddEquipmentandSaveBtnActive(false);
      }
    }
  };

  const checkDuplicacyInEquipmentRow = () => {
    const allEquipmentRows = [...equipmentFormValues["serviceDetailsModels"]];

    let duplicateIndex = "-1";
    for (let i = 0; i < allEquipmentRows.length - 1; i++) {
      for (let j = i + 1; j < allEquipmentRows.length; j++) {
        if (
          allEquipmentRows[i]["equipmentModel"]["id"] ===
            allEquipmentRows[j]["equipmentModel"]["id"] &&
          allEquipmentRows[i]["manufacturerModule"]["id"] ===
            allEquipmentRows[j]["manufacturerModule"]["id"]
        ) {
          duplicateIndex = j;
        }
      }
    }

    if (duplicateIndex !== "-1") {
      setIsWorkingExperienceError(false);
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

  const checkEmptyEquipmentRow = () => {
    setIsWorkingExperienceError(false);
    let isEmptyRow = false;
    equipmentFormValues["serviceDetailsModels"].forEach((row) => {
      if (
        row?.equipmentExperience === "" ||
        row?.equipmentModel?.name === "" ||
        row?.equipmentModel?.id === "null" ||
        row?.manufacturerModule?.name === "" ||
        row?.manufacturerModule?.id === "null"
      ) {
        isEmptyRow = true;
      }
    });

    return {
      isEmpty: isEmptyRow,
    };
  };

  const addmoreEquipmentRow = (index) => {
    if (checkEmptyEquipmentRow().isEmpty) {
      setIsWorkingExperienceError(true);
    } else {
      setIsWorkingExperienceError(false);

      if (checkDuplicacyInEquipmentRow().status) {
        setIsDuplicacyError(true);
      } else {
        setIsDuplicacyError(false);
        let flagForAddWorkingExperience = false;

        equipmentFormValues.serviceDetailsModels.forEach((item) => {
          if (
            item.equipmentExperience === "" ||
            item.equipmentModel.id === "" ||
            item.equipmentModel.name === "" ||
            item.manufacturerModule.id === "" ||
            item.manufacturerModule.name === ""
          ) {
            flagForAddWorkingExperience = true;
          }
        });

        if (flagForAddWorkingExperience === true) {
          setIsWorkingExperienceError(true);
        } else {
          addMoreWorkingExperience(index);
        }
      }
    }
  };

  const removePort = () => {
    setIsShowPortRemoveIcon(false);

    const transformedEquipmentRemoveIconArray = [...isShowEquipmentRemoveIcon];
    transformedEquipmentRemoveIconArray.pop();
    setIsShowEquipmentRemoveIcon(transformedEquipmentRemoveIconArray);

    setactivePort(activePort - 1);
    const trasformedPortCount = [...portListCount];
    trasformedPortCount.splice(trasformedPortCount.length - 1, 1);
    setPortListCount(trasformedPortCount);
    setPortFormValues(portList[portList.length - 1]);

    const transformedPortList = [...portList];
    transformedPortList.pop();
    setPortList(transformedPortList);
    setIsUserClickedPortRemoved(true);

    // ----------------------- Equipment change with the change of port -----------------------
    const currentEquipmentData = {
      ...serviceList[activePort - 2],
    };
    const transformedcurrentEquipmentData =
      currentEquipmentData?.equipmentCategory.map((equipment) => {
        return {
          equipmentCategoryId: { id: equipment?.equipmentCategoryId?.id },
          serviceDetailsModels:
            equipment?.equipmentCategoryId?.serviceDetailsModels,
        };
      });
    setEquipmentList(transformedcurrentEquipmentData);
    setActiveEquipment(transformedcurrentEquipmentData?.length);
    setEquipmentListCount(
      transformedcurrentEquipmentData?.map((equipment, index) => index + 1)
    );
    setEquipmentFormValues(
      transformedcurrentEquipmentData[
        transformedcurrentEquipmentData?.length - 1
      ]
    );
  };

  const removeEquipment = () => {
    setActiveEquipment(activeEquipment - 1);
    const trasformedEquipmentCount = [...equipmentListCount];
    trasformedEquipmentCount.splice(trasformedEquipmentCount.length - 1, 1);
    setEquipmentListCount(trasformedEquipmentCount);
    setEquipmentFormValues(equipmentList[equipmentList.length - 1]);

    const transformedEquipmentList = [...equipmentList];
    transformedEquipmentList.pop();
    setEquipmentList(transformedEquipmentList);
    setIsUserClickedEquipmentRemoved(true);
  };

  const [ignored, forceUpdate] = useReducer((x) => x + 1, 0);
  const removeOneEquipmentRow = (index) => {
    setIsWorkingExperienceError(false);
    setIsDuplicacyError(false);

    if (equipmentFormValues.serviceDetailsModels.length > 1) {
      equipmentFormValues.serviceDetailsModels.splice(index, 1);
      forceUpdate();
    }
  };

  const handleFormSubmission = (e) => {
    e.preventDefault();
    setIsDuplicacyError(false);
    if (checkEmptyEquipmentRow().isEmpty) {
      setIsWorkingExperienceError(true);
      return;
    } else {
      setIsWorkingExperienceError(false);
      if (checkDuplicacyInEquipmentRow().status) {
        setIsDuplicacyError(true);
        return;
      } else {
        setIsDuplicacyError(false);
      }
    }

    validatePortForm();
    ValidateFormEquipment();
    if (validatePortForm() && ValidateFormEquipment()) {
      setIsConfirmationModal(!isConfirmationModal);
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
    if (serviceList.length === portListCount.length) {
      const serviceObject = {
        id: serviceList[serviceList.length - 1]["id"],
        status: 0,
        companyId: getCompanyIdCheck(),
        vendorServiceKey: true,
        country: null,
        equipmentCategory: null,
      };
      dispatch(createServiceFunc(serviceObject));
    } else {
      if (serviceList.length === activePort) {
        setPortFormValues(portInitialState);

        setPortList([...portList, portFormValues]);
        setactivePort(activePort + 1);
        setPortListCount([...portListCount, portListCount.length + 1]);

        setEquipmentFormValues(equipmentInitialValues);
        setEquipmentList([]);
        setActiveEquipment(1);
        setEquipmentListCount([1]);

        let transformedRemoveIconAtEquipment = [...isShowEquipmentRemoveIcon];
        transformedRemoveIconAtEquipment.push(true);
        setIsShowEquipmentRemoveIcon(transformedRemoveIconAtEquipment);
        setIsShowPortRemoveIcon(true);
      } else {
        let equipmentDetails = null;
        if (equipmentList.length > 0) {
          equipmentDetails = equipmentList.map((equipment) => {
            return {
              equipmentCategoryId: {
                id: equipment?.equipmentCategoryId?.id,
                status: 0,
                serviceDetailsModels: [...equipment?.serviceDetailsModels],
              },
            };
          });
        } else {
          equipmentDetails = [
            {
              equipmentCategoryId: {
                id: equipmentFormValues?.["equipmentCategoryId"]["id"],
                status: 0,
                serviceDetailsModels: [
                  ...equipmentFormValues?.serviceDetailsModels,
                ],
              },
            },
          ];
        }

        const portDetails = portFormValues.port.map((item, index) => {
          if (item.type === `old`) {
            return {
              value: item.portId,
              label: item.portValue,
            };
          } else {
            return {
              value: 0,
              label: item?.portValue,

              // id: 0,
              // name: item?.portValue,
              // status: 0,
              // verified: false,
              // countryId: portFormValues?.country,
            };
          }
        });

        const portObject = {
          id: portFormValues.country,
          name: countryName,
          portId: portDetails,
        };

        const serviceObject = {
          companyId: getCompanyIdCheck(),
          vendorServiceKey: true,
          country: portObject,
          equipmentCategory: [...equipmentDetails],
        };

        let transformedRemoveIconAtEquipment = [...isShowEquipmentRemoveIcon];
        transformedRemoveIconAtEquipment.splice(activePort - 1, 1, false);
        transformedRemoveIconAtEquipment.push(true);
        setIsShowEquipmentRemoveIcon(transformedRemoveIconAtEquipment);
        setIsShowPortRemoveIcon(true);

        const trasformedPort = serviceObject.country.portId.map((port) => {
          if (typeof port.value === "string" || port.value == 0) {
            if (port.value === 0) {
              return {
                id: port.value,
                name: port.label,
                status: 0,
                verified: false,
                countryId: portFormValues?.country,
              };
            } else if (typeof port.value === "string") {
              return {
                id: 0,
                name: port.label,
                status: 0,
                verified: false,
                countryId: portFormValues?.country,
              };
            }
          } else {
            return {
              id: port.value,
            };
          }
        });

        const newServiceForAPICall = {
          ...serviceObject,
          country: {
            ...serviceObject.country,
            portId: trasformedPort,
          },
        };
        dispatch(createServiceFunc(newServiceForAPICall));
      }
    }
    // {
    //   let equipmentDetails = null;
    //   if (equipmentList.length > 0) {
    //     equipmentDetails = equipmentList.map((equipment) => {
    //       return {
    //         equipmentCategoryId: {
    //           id: equipment?.equipmentCategoryId?.id,
    //           status: 0,
    //           serviceDetailsModels: [...equipment?.serviceDetailsModels],
    //         },
    //       };
    //     });
    //   } else {
    //     equipmentDetails = [
    //       {
    //         equipmentCategoryId: {
    //           id: equipmentFormValues?.["equipmentCategoryId"]["id"],
    //           status: 0,
    //           serviceDetailsModels: [
    //             ...equipmentFormValues?.serviceDetailsModels,
    //           ],
    //         },
    //       },
    //     ];
    //   }

    //   const portDetails = portFormValues.port.map((item, index) => {
    //     if (item.type === `old`) {
    //       return {
    //         id: item.portId,
    //       };
    //     } else {
    //       return {
    //         id: 0,
    //         name: item?.portValue,
    //         status: 0,
    //         verified: false,
    //         countryId: portFormValues?.country,
    //       };
    //     }
    //   });

    //   const portObject = {
    //     id: portFormValues.country,
    //     name: countryName,
    //     portId: portDetails,
    //   };

    //   const serviceObject = {
    //     companyId: getCompanyIdCheck(),
    //     vendorServiceKey: true,
    //     country: portObject,
    //     equipmentCategory: [...equipmentDetails],
    //   };

    //   dispatch(createServiceFunc(serviceObject));
    // }
  };

  // const apiCall = () => {
  //   let portsArray = portList?.map((item) => item?.port);
  //   let portId = portsArray?.flat(1);

  //   if (portList.length === 0 && equipmentList.length === 0) {
  //     let data = {
  //       companyId: getCompanyIdCheck(),
  //       portId: portFormValues.port,
  //       serviceDetails: [equipmentFormValues],
  //     };
  //     dispatch(createServiceFunc(data));
  //   } else if (portList.length === 0 && equipmentList.length > 0) {
  //     let data = {
  //       companyId: getCompanyIdCheck(),
  //       portId: portFormValues.port,
  //       serviceDetails: equipmentList,
  //     };
  //     dispatch(createServiceFunc(data));
  //   } else if (portList.length > 0 && equipmentList.length === 0) {
  //     let data = {
  //       companyId: getCompanyId(),
  //       portId: portId,
  //       serviceDetails: [equipmentFormValues],
  //     };
  //     dispatch(createServiceFunc(data));
  //   } else if (portList.length > 0 && equipmentList.length > 0) {
  //     let data = {
  //       companyId: getCompanyId(),
  //       portId: portId,
  //       serviceDetails: equipmentList,
  //     };
  //     dispatch(createServiceFunc(data));
  //   }
  // };

  const getCompanyIdCheck = () => {
    if (getAuthData()?.companyId) {
      return getAuthData()?.companyId;
    }
    return getCompanyId();
  };

  useEffect(() => {
    // let port = navigationResponse?.portId;
    // if (navigationResponse !== null) {
    //   setEquipmentList(navigationResponse?.serviceDetails);
    //   setEquipmentFormValues(
    //     navigationResponse?.serviceDetails?.[
    //       navigationResponse?.serviceDetails.length - 1
    //     ]
    //   );
    //   let equippmentCount = navigationResponse?.serviceDetails?.map(
    //     (item, index) => index + 1
    //   );
    //   setEquipmentListCount(equippmentCount);
    //   setActiveEquipment(navigationResponse?.serviceDetails.length);
    //   let uniqueCountryList = [
    //     ...new Set(port?.map((country) => country?.countryModel?.name)),
    //   ];
    //   let myFilteredPort = uniqueCountryList.map((country, index) => {
    //     return port.filter((myCountry) => {
    //       if (country === myCountry.countryModel.name) {
    //         return { country, myCountry: myCountry.countryModel.name };
    //       }
    //     });
    //   });
    //   let ports = myFilteredPort.map((myPort) =>
    //     myPort.map((innerPort) => innerPort)
    //   );
    //   uniqueCountryList = [
    //     ...new Set(port?.map((country) => country?.countryModel?.id)),
    //   ];
    //   let countryData = uniqueCountryList.map((finalData, index) => {
    //     return {
    //       country: finalData,
    //       port: [...ports[index]],
    //     };
    //   });
    // setPortList(countryData);
    // setPortFormValues(countryData[countryData?.length - 1]);
    // let portCount = countryData?.map((item, index) => index + 1);
    // setPortListCount(portCount);
    // setactivePort(countryData?.length);
    // }
  }, [navigationResponse]);

  useEffect(() => {
    if (isCountryError !== "") {
      ToastError(isCountryError);
    } else if (isPortByCountryIdError !== "") {
      ToastError(isPortByCountryIdError);
    } else if (isNavigationResponseResponse) {
      ToastError(isNavigationResponseResponse);
    } else if (isCreateServiceResError) {
      //ToastError(isCreateServiceResError);
    }
  }, [
    isCountryError,
    isPortByCountryIdError,
    isNavigationResponseResponse,
    isCreateServiceResError,
  ]);

  useEffect(() => {
    if (
      createServiceRes !== null &&
      createServiceRes.vendorServiceKey === false
    ) {
      if (serviceList.length !== 0) {
        const trasformedServiceList = [...serviceList];
        trasformedServiceList[trasformedServiceList.length - 1]["id"] =
          createServiceRes?.id;
        setServiceList(trasformedServiceList);
      }
    }
  }, [createServiceRes]);

  // if (navigationResponse !== null) {
  //   setRunLimit(runLimit + 1);
  //   const servicePortList = navigationResponse.map((service, index) => {
  //     return {
  //       country: service?.country?.id,
  //       port: [...service?.country?.portId],
  //     };
  //   });

  //   setPortList(servicePortList);
  //   setServiceList(navigationResponse);
  //   setPortFormValues(servicePortList[servicePortList?.length - 1]);
  //   let portCount = servicePortList?.map((item, index) => index + 1);
  //   setPortListCount(portCount);
  //   setactivePort(servicePortList?.length);

  //   let equipmentData = navigationResponse.map((service, index) => {
  //     const serviceEquipment = service?.equipmentCategory?.[0];

  //     if (navigationResponse.length - 1 === index) {
  //       return {
  //         equipmentCategoryId: {
  //           id: serviceEquipment?.["equipmentCategoryId"]?.["id"],
  //         },
  //         serviceDetailsModels: [
  //           ...serviceEquipment?.["equipmentCategoryId"]?.[
  //             "serviceDetailsModels"
  //           ],
  //         ],
  //       };
  //     }
  //   });
  //   equipmentData = equipmentData.filter((el) => el !== undefined);

  //   setEquipmentList(equipmentData);
  //   setEquipmentFormValues(equipmentData?.[equipmentData.length - 1]);
  //   let equippmentCount = equipmentData?.map((item, index) => index + 1);
  //   setEquipmentListCount(equippmentCount);
  //   setActiveEquipment(equipmentData.length);

  //   let dummyEquipmentData = navigationResponse.map((service, index) => {
  //     const serviceEquipment = service?.equipmentCategory?.[0];

  //     return [
  //       {
  //         equipmentCategoryId: {
  //           id: serviceEquipment?.["equipmentCategoryId"]?.["id"],
  //         },
  //         serviceDetailsModels: [
  //           ...serviceEquipment?.["equipmentCategoryId"]?.[
  //             "serviceDetailsModels"
  //           ],
  //         ],
  //       },
  //     ];
  //   });
  //   setDummyEquipmentList(dummyEquipmentData);
  // }

  const removeOtherOptionUIHandler = (index) => {
    const transformedEquipmentName = { ...equipmentFormValues };
    transformedEquipmentName["serviceDetailsModels"][index]["equipmentModel"] =
      { id: "", name: "" };
    setEquipmentFormValues(transformedEquipmentName);
  };

  const removeOtherOptionManuUIHandler = (index) => {
    const transformedEquipmentName = { ...equipmentFormValues };
    transformedEquipmentName["serviceDetailsModels"][index][
      "manufacturerModule"
    ] = { id: "", name: "" };
    setEquipmentFormValues(transformedEquipmentName);
  };

  const equipmentModelFunc = (index) => {
    return (
      <div className="equipment_container_body_row">
        <div className="equipment_container_body">
          <div className="equipment_input_container">
            <label className="mobile_label">Equipment Name</label>

            <select
              className="form_control option_font"
              name="equipment-name"
              value={
                equipmentFormValues.serviceDetailsModels[index][
                  "equipmentModel"
                ]["id"]
              }
              onChange={(event) => handleEquipmentChange(event, index)}
              disabled={
                [false, true].includes(
                  serviceList[activePort - 1]?.["vendorServiceKey"]
                )
                  ? true
                  : false
              }
            >
              <option value="" disabled selected>
                --Select--
              </option>

              {equipmentFormValues.serviceDetailsModels[index][
                "equipmentModel"
              ]["id"] !== "" ? (
                <>
                  <option
                    value={`${equipmentFormValues.serviceDetailsModels[index]["equipmentModel"]["id"]} @@ ${equipmentFormValues.serviceDetailsModels[index]["equipmentModel"]["value"]}`}
               
                 >
                    {
                      equipmentFormValues.serviceDetailsModels[index][
                        "equipmentModel"
                      ]["name"]
                    }
                  </option>
                  {equipmentDropdownData[activeEquipment - 1]?.map((item) => (
                    <option value={`${item.id} @@ ${item.name}`} key={item.id}>
                      {item.name}
                    </option>
                  ))}
                </>
              ) : (
                equipmentDropdownData[activeEquipment - 1]
                  ?.filter((port) => port)
                  ?.map((item) => (
                    <option value={`${item.id} @@ ${item.name}`} key={item.id}>
                      {item.name}
                    </option>
                  ))
              )}
            </select>

            {(equipmentFormValues.serviceDetailsModels[index]["equipmentModel"][
              "id"
            ] === 0 ||
              equipmentFormValues.serviceDetailsModels[index]["equipmentModel"][
                "id"
              ] === "null") && (
              <div className="equipment-others">
                <input
                  type="text"
                  className="other_input"
                  name="equipment_other_text"
                  value={
                    equipmentFormValues.serviceDetailsModels[index][
                      "equipmentModel"
                    ]["name"] === "Others"
                      ? ""
                      : equipmentFormValues.serviceDetailsModels[index][
                          "equipmentModel"
                        ]["name"]
                  }
                  onChange={(event) => handleEquipmentChange(event, index)}
                />
                <div
                  className="equi-others-img"
                  onClick={() => removeOtherOptionUIHandler(index)}
                >
                  <img src={cancelForOther} alt="cancel" />
                </div>
              </div>
            )}
          </div>
          <div className="equipment_input_container">
            <label className="mobile_label">Manufacturer</label>
            <select
              className="form_control option_font"
              name="equipment-manufacturer"
              value={`${equipmentFormValues.serviceDetailsModels[index]["manufacturerModule"]["id"]} @@ ${equipmentFormValues.serviceDetailsModels[index]["manufacturerModule"]["value"]}`}
              onChange={(event) => handleEquipmentChange(event, index)}
              disabled={
                [false, true].includes(
                  serviceList[activePort - 1]?.["vendorServiceKey"]
                )
                  ? true
                  : false
              }
            >
              <option value="" selected>
                --Select--
              </option>
              {equipmentFormValues.serviceDetailsModels[index][
                "manufacturerModule"
              ]["id"] !== "" ? (
                <>
                  <option
                    value={`${equipmentFormValues.serviceDetailsModels[index]["manufacturerModule"]["id"]} @@ ${equipmentFormValues.serviceDetailsModels[index]["manufacturerModule"]["value"]}`}
                  >
                    {
                      equipmentFormValues.serviceDetailsModels[index][
                        "manufacturerModule"
                      ]["name"]
                    }
                  </option>
                  {equipmentManufacturerRes?.map((item) => (
                    <option value={`${item.id} @@ ${item.name}`} key={item.id}>
                      {item.name}
                    </option>
                  ))}
                </>
              ) : (
                equipmentManufacturerRes?.map((item) => (
                  <option value={`${item.id} @@ ${item.name}`} key={item.id}>
                    {item.name}
                  </option>
                ))
              )}
            </select>

            {(equipmentFormValues.serviceDetailsModels[index][
              "manufacturerModule"
            ]["id"] === 0 ||
              equipmentFormValues.serviceDetailsModels[index][
                "manufacturerModule"
              ]["id"] === "null") && (
              <div className="equipment-others">
                <input
                  type="text"
                  className="other_input"
                  name="manufacturer_other_text"
                  value={
                    equipmentFormValues.serviceDetailsModels[index][
                      "manufacturerModule"
                    ]["name"] === "Others"
                      ? ""
                      : equipmentFormValues.serviceDetailsModels[index][
                          "manufacturerModule"
                        ]["name"]
                  }
                  onChange={(event) => handleEquipmentChange(event, index)}
                />
                <div
                  className="equi-others-img"
                  onClick={() => removeOtherOptionManuUIHandler(index)}
                >
                  <img src={cancelForOther} alt="cancel" />
                </div>
              </div>
            )}
          </div>
          <div className="equipment_input_container">
            <label className="mobile_label">Experience in Serving</label>
            <select
              className="form_control option_font"
              name="equipment-experience"
              // style={{ fontFamily: "Poppins-Regular" }}
              value={
                equipmentFormValues.serviceDetailsModels[index][
                  "equipmentExperience"
                ]
              }
              onChange={(event) => handleEquipmentChange(event, index)}
              disabled={
                [false, true].includes(
                  serviceList[activePort - 1]?.["vendorServiceKey"]
                )
                  ? true
                  : false
              }
            >
              <option value="" disabled selected>
                --Select--
              </option>
              <option value="<2 Years"> &lt; 2 Years</option>
              <option value="2 to 5 years"> 2 to 5 years</option>
              <option value="6 to 10 years"> 6 to 10 years</option>
              <option value=">10 Years"> &gt; 10 year</option>
            </select>

            {/* <input
              type="number"
              className="form_control"
              name="equipment-experience"
              value={
                equipmentFormValues.serviceDetailsModels[index][
                  "equipmentExperience"
                ]
              }
              onChange={(event) => handleEquipmentChange(event, index)}
              disabled={
                forNavigation === true && navigationResponse != null
                  ? true
                  : false
              }
            /> */}
          </div>

          <div
            className={`equipment_input_container_small ${
              navigationResponse != null &&
              forNavigation === true &&
              "disable-anchor"
            }`}
          >
            <img
              src={addRowIcon}
              alt="add row"
              className={`add_more ${
                [false, true].includes(
                  serviceList[activePort - 1]?.["vendorServiceKey"]
                ) && "disabled"
              }`}
              disabled={
                [false, true].includes(
                  serviceList[activePort - 1]?.["vendorServiceKey"]
                )
                  ? true
                  : false
              }
              onClick={() => addmoreEquipmentRow(index)}
            />
          </div>
          <div
            className={`equipment_input_container_small ${
              navigationResponse != null &&
              forNavigation === true &&
              "disable-anchor"
            }`}
          >
            <img
              src={removeRowIcon}
              alt="remove row"
              className={`add_more ${
                [false, true].includes(
                  serviceList[activePort - 1]?.["vendorServiceKey"]
                ) && "disabled"
              }`}
              disabled={
                [false, true].includes(
                  serviceList[activePort - 1]?.["vendorServiceKey"]
                )
                  ? true
                  : false
              }
              onClick={() => removeOneEquipmentRow(index)}
            />
          </div>
        </div>
      </div>
    );
  };

  const nextNavigationHandler = () => {
    dispatch(createServiceActions.serviceNavigation(true));
    dispatch(commonGetApiActions.dispatchGetRequest());
    props.navigationHandler({
      currentForm: "finance",
      goForward: true,
    });
    props.setFormLabel("finance", 4);
  };

  const countryOptions = countries?.map((country) => ({
    value: [country.id, country.name],
    label: [
      <img
        src={country.flag}
        style={{ width: "20px", marginRight: "15px" }}
        alt="flag"
      />,
      <span style={{ fontFamily: "Poppins-Regular" }}>{country.name}</span>,
    ],
  }));

  const equipmentFilterCategory = equipmentCategoryRes
    ?.filter((u) => !equipmentCategoryData?.includes(u.id))
    ?.map((index) => ({
      value: index.id,
      label: index.name,
    }));

  const equipmentCategory = equipmentCategoryRes?.map((index) => ({
    value: index.id,
    label: index.name,
  }));

  const portListData = retainedPortData[activePort - 1]?.map((index) => ({
    value: index.id,
    label: index.name,
  }));

  const customStyle = {
    container: (base, state) => ({
      ...base,
    }),
    menu: (styles) => ({
      ...styles,
      width: "100%",

      zIndex: "999",
    }),
  };

  const addNewService = () => {
    setIsDuplicacyError(false);
    if (checkEmptyEquipmentRow().isEmpty) {
      setIsWorkingExperienceError(true);
      return;
    } else {
      setIsWorkingExperienceError(false);
      if (checkDuplicacyInEquipmentRow().status) {
        setIsDuplicacyError(true);
        return;
      } else {
        setIsDuplicacyError(false);
      }
    }

    if (!validatePortForm()) return false;

    if (!ValidateFormEquipment()) return false;

    if (serviceList.length === activePort) {
      setPortFormValues(portInitialState);

      setPortList([...portList, portFormValues]);
      setactivePort(activePort + 1);
      setPortListCount([...portListCount, portListCount.length + 1]);

      setEquipmentFormValues(equipmentInitialValues);
      setEquipmentList([]);
      setActiveEquipment(1);
      setEquipmentListCount([1]);

      let transformedRemoveIconAtEquipment = [...isShowEquipmentRemoveIcon];
      transformedRemoveIconAtEquipment.push(true);
      setIsShowEquipmentRemoveIcon(transformedRemoveIconAtEquipment);
      setIsShowPortRemoveIcon(true);
    } else {
      if (portList.length > 0) {
        portSaveHandler();
        addPort();
      } else {
        addPort();
      }
      if (equipmentList.length > 0) {
        equipmentSaveHandler();
        addEquipment();
      } else {
        addEquipment();
      }
      let equipmentDetails = null;
      if (equipmentList.length > 0) {
        equipmentDetails = equipmentList.map((equipment) => {
          return {
            equipmentCategoryId: {
              id: equipment?.equipmentCategoryId?.id,
              status: 0,
              serviceDetailsModels: [...equipment?.serviceDetailsModels],
            },
          };
        });
      } else {
        equipmentDetails = [
          {
            equipmentCategoryId: {
              id: equipmentFormValues?.["equipmentCategoryId"]["id"],
              status: 0,
              serviceDetailsModels: [
                ...equipmentFormValues?.serviceDetailsModels,
              ],
            },
          },
        ];
      }

      const portDetails = portFormValues.port.map((item, index) => {
        if (item.type === `old`) {
          return {
            value: item.portId,
            label: item.portValue,
          };
        } else {
          return {
            value: 0,
            label: item?.portValue,

            // id: 0,
            // name: item?.portValue,
            // status: 0,
            // verified: false,
            // countryId: portFormValues?.country,
          };
        }
      });

      const portObject = {
        id: portFormValues.country,
        name: countryName,
        portId: portDetails,
      };

      const serviceObject = {
        companyId: getCompanyIdCheck(),
        vendorServiceKey: false,
        country: portObject,
        equipmentCategory: [...equipmentDetails],
      };

      let transformedRemoveIconAtEquipment = [...isShowEquipmentRemoveIcon];
      transformedRemoveIconAtEquipment.splice(activePort - 1, 1, false);
      transformedRemoveIconAtEquipment.push(true);
      setIsShowEquipmentRemoveIcon(transformedRemoveIconAtEquipment);
      setIsShowPortRemoveIcon(true);

      const trasformedPort = serviceObject.country.portId.map((port) => {
        if (typeof port.value === "string" || port.value == 0) {
          if (port.value === 0) {
            return {
              id: port.value,
              name: port.label,
              status: 0,
              verified: false,
              countryId: portFormValues?.country,
            };
          } else if (typeof port.value === "string") {
            return {
              id: 0,
              name: port.label,
              status: 0,
              verified: false,
              countryId: portFormValues?.country,
            };
          }
        } else {
          return {
            id: port.value,
          };
        }
      });

      const newServiceForAPICall = {
        ...serviceObject,
        country: {
          ...serviceObject.country,
          portId: trasformedPort,
        },
      };
      dispatch(createServiceFunc(newServiceForAPICall));

      setDummyEquipmentList([...dummyEquipmentList, equipmentList]);

      setServiceList([...serviceList, serviceObject]);

      setServiceCountryValues([...serviceCountryValues, countryName]);

      setEquipmentFormValues(equipmentInitialValues);

      setEquipmentDropdownData([]);

      setEquipmentList([]);
      setEquipmentListCount([1]);
      setActiveEquipment(1);
      setIsSaveBtnActiveEquipment(false);
      setlastEquipmentBtnStatus(false);
      setisAddEquipmentandSaveBtnActive(true);
      setlastEquipmentFilledData(equipmentInitialValues);
      setEquipmentCategoryData([]);
      setPortData([]);
      setRetainedPortData([]);
    }
  };

  const formatCreate = (inputValue) => {
    return (
      <p>
        <strong>Add Port</strong> "{inputValue}"
      </p>
    );
  };

  return (
    <div className="form_container_area">
      {(isCountryLoading ||
        isPortByCountryIdLoading ||
        isCreateServiceLoading ||
        equipmentCategoryResisLoading ||
        equipmentNameResisLoading ||
        equipmentManufacturerResisLoading) && <Loading text="Loading..." />}
      {isConfirmationModal && (
        <ConfirmationModal cancel={onCancelHandler} ok={onOkHandler} />
      )}
      {/* {isConfirmationAddNewData && (
        <ConfirmationAddNewData
          cancel={onCancelModal}
          ok={onConfirm}
          name={"Country"}
        />
      )} */}
      {/* {isConfirmationAddNewEquipment && (
        <ConfirmationAddNewData
          cancel={onCancelEquipmentModal}
          ok={onConfirmEquipment}
          name={"Equipment"}
        />
      )} */}
      <div className="nav_container">
        <div className="nav_items">
          <div className="nav_item active">Country wise services</div>
        </div>
      </div>

      <div className="company_id_details">
        <p className="company_id">Company id: {getReferralCode()}</p>
      </div>
      {/* <button
        onClick={() =>
          console.log(
            { portList, portFormValues, portListCount, activePort },
            {
              equipmentList,
              equipmentFormValues,
              equipmentListCount,
              activeEquipment,
            },
            {
              serviceList,
            },
            {
              isShowEquipmentRemoveIcon,
              isShowPortRemoveIcon,
              serviceCountryValues,
            }
          )
        }
      >
        get state
      </button> */}

      <div className="service_form positioning_container">
        <form onSubmit={handleFormSubmission}>
          <div className="form_container">
            <div className="add_bidder_container">
              {portListCount?.map((el, index) => {
                if (activePort === index + 1) {
                  return (
                    <div className="bidder active" key={index}>
                      <span className="bidder_text"> P{index + 1} </span>

                      {portList.length != 0 &&
                      [false, true].includes(
                        serviceList[activePort - 1]?.["vendorServiceKey"]
                      )
                        ? true
                        : index !== 0 && (
                            <span className="remove_bidder">
                              <img
                                src={cancel}
                                alt="cancel"
                                onClick={removePort}
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
                      onClick={() => makePortActive(index + 1)}
                    >
                      <span className="bidder_text"> P{index + 1} </span>
                    </div>
                  );
                }
              })}
            </div>

            <div className="form_control_area">
              <label className="form_label">
                Country <span className="label_mandatory">*</span>
              </label>

              {navigationResponse !== null && forNavigation === true ? (
                <select
                  className="form_control"
                  name="country"
                  value={portFormValues.country}
                  onChange={handlePortChange}
                  disabled={
                    [false, true].includes(
                      serviceList[activePort - 1]?.["vendorServiceKey"]
                    )
                      ? true
                      : false
                  }
                >
                  {countries?.map((country) => {
                    return <option value={country.id}>{country.name}</option>;
                  })}
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
                      fontSize: "16px",
                      fontFamily: "Poppins-Regular",
                      marginRight: "20px",
                      width: "auto",
                      maxWidth: "83%",
                      height: "29px",
                      overflow: "hidden",
                      whiteSpace: "nowrap",
                      textOverflow: "ellipsis",
                    }}
                  >
                    {countries
                      ?.filter((index) => index.id === portFormValues.country)
                      ?.map((country) => country.name)}
                  </div>
                  <Select
                    name="country"
                    onInputChange={setHide}
                    className="form_control_country"
                    onChange={handlePortChange}
                    onKeyDown={handleLettersOnlyInput}
                    placeholder=""
                    isDisabled={
                      [false, true].includes(
                        serviceList[activePort - 1]?.["vendorServiceKey"]
                      )
                        ? true
                        : false
                    }
                    options={countryOptions?.filter(
                      (country) =>
                        !serviceCountryValues?.includes(country.value[1])
                    )}
                    styles={customStyle}
                    components={{
                      SingleValue: () => {
                        return null;
                      },
                    }}
                  ></Select>
                </>
              )}
              {errors && portFormValues?.country === "" && (
                <span className="error">Select Country</span>
              )}
            </div>
            <div className="form_control_area fontClassSelect">
              <label className="form_label">
                Port <span className="label_mandatory"></span>
              </label>

              <CreatableSelect
                isMulti
                isDisabled={
                  [false, true].includes(
                    serviceList[activePort - 1]?.["vendorServiceKey"]
                  )
                    ? true
                    : false
                }
                placeholder="Select or Type"
                options={portListData?.filter(
                  (port) => port?.label !== `Others`
                )}
                className="form_control_port"
                onChange={addPortToListByCreateableSelect}
                value={
                  [false, true].includes(
                    serviceList[activePort - 1]?.["vendorServiceKey"]
                  )
                    ? portFormValues?.["port"]?.[0]?.["portId"] !== undefined
                      ? portFormValues?.port?.map((port) => {
                          return {
                            value: port?.portId,
                            label: port?.portValue,
                          };
                        })
                      : portFormValues?.port
                    : portFormValues?.port?.map((port) => {
                        return {
                          value: port?.portId,
                          label: port?.portValue,
                        };
                      })
                }
                formatCreateLabel={formatCreate}
                openMenuOnFocus={portListDropdown}
                ref={portListRef}
                classNamePrefix="react-select"
              />
              <img
                src={dropdownIcon}
                // className="from_control_country_dropdown"
                className={`from_control_country_dropdown ${
                  [false, true].includes(
                    serviceList[activePort - 1]?.["vendorServiceKey"]
                  )
                    ? "colorGrey"
                    : "colorWhite"
                }`}
                alt="dropdown"
                onClick={() => {
                  portListRef.current && portListRef.current.focus(),
                    setPortListDropdown(!portListDropdown);
                }}
              />

              {/* <div
                className={`custom_ports_dropdown ${navigationResponse !== null && forNavigation === true
                    ? `disable_ports_dropdown`
                    : ""
                  }`}
                onMouseLeave={hideCustomSelectionBox}
              >
                <div
                  className={
                    navigationResponse != null && forNavigation === true
                      ? "select_box"
                      : "select_box set_background "
                  }
                  onClick={showCustomSelectionBox}
                >
                  {portFormValues?.port?.map((port) => (
                    <div className="selected_bidder">
                      <span className="bidder_name">{port.name}</span>
                      <span
                        className="port_cancel"
                        data-id={port.id}
                        data-name={port.name}
                        onClick={(e) => removePortFromSelection(e)}
                      >
                        x
                      </span>
                    </div>
                  ))}
                  <div
                    className={
                      navigationResponse != null && forNavigation === true
                        ? "dropdown_icon_container"
                        : "dropdown_icon_container set_background"
                    }
                  >
                    <img
                      src={dropdownIcon}
                      alt="dropdown-icon"
                      className="dropdown_icon"
                    />
                  </div>
                </div>
                <div>&nbsp;</div>
                {toggleOptions && (
                  <div className="options_container">
                    {retainedPortData[activePort - 1]?.map((port) => (
                      <span
                        className="option"
                        onClick={(e) => addPortToTheList(e)}
                        key={port.id}
                        data-id={port.id}
                        data-name={port.name}
                      >
                        {port.name}
                      </span>
                    ))}
                    {(retainedPortData[activePort - 1] === undefined ||
                      retainedPortData[activePort - 1]?.length === 0) && (
                        <span className="option_blank">No Data Available</span>
                      )}
                  </div>
                )}
              </div> */}
            </div>
            {false && (
              <div className="form_submit_container">
                {isSaveBtnActivePort && (
                  <input
                    type="button"
                    value="Save &amp; Next"
                    className="btn btn_submit_form btn_port"
                    onClick={portSaveHandler}
                  />
                )}
                {isAddPortandSaveBtnActive && (
                  <input
                    type="button"
                    value="Add Country"
                    className="btn btn_add_bidder"
                    onClick={() => addPort()}
                    disabled={
                      [false, true].includes(
                        serviceList[activePort - 1]?.["vendorServiceKey"]
                      )
                        ? true
                        : false
                    }
                  />
                )}
              </div>
            )}
            {/* *************************************** Equipments *************************************** */}
            {/* <div className="horizontal_line">&nbsp;</div> */}
            <div className="add_bidder_container">
              {equipmentListCount?.map((el, index) => {
                if (activeEquipment === index + 1) {
                  return (
                    <div className="bidder active" key={index}>
                      <span className="bidder_text"> C{index + 1} </span>

                      {navigationResponse === null &&
                        activeEquipment === equipmentListCount.length &&
                        activeEquipment !== 1 &&
                        isShowEquipmentRemoveIcon[activePort - 1] && (
                          // &&
                          // !isAddEquipmentandSaveBtnActive

                          <span className="remove_bidder">
                            <img
                              src={cancel}
                              alt="cancel"
                              onClick={removeEquipment}
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
                      onClick={() => makeEquipmentActive(index + 1)}
                    >
                      <span className="bidder_text"> C{index + 1} </span>
                    </div>
                  );
                }
              })}
            </div>

            <div className="form_control_area">
              <label className="form_label">
                Equipment Category <span className="label_mandatory">*</span>
              </label>

              {navigationResponse !== null && forNavigation === true ? (
                <select
                  className="form_control"
                  name="country"
                  value={equipmentFormValues["equipmentCategoryId"]["id"]}
                  onChange={handleEquipmentChangeEquipment}
                  disabled={
                    [false, true].includes(
                      serviceList[activePort - 1]?.["vendorServiceKey"]
                    )
                      ? true
                      : false
                  }
                >
                  {equipmentCategory.map((equipment) => {
                    return (
                      <option value={equipment.value}>{equipment.label}</option>
                    );
                  })}
                  )
                </select>
              ) : (
                <Select
                  name="equipment-category"
                  className="form_control_country"
                  onChange={handleEquipmentChangeEquipment}
                  onKeyDown={handleLettersOnlyInput}
                  isDisabled={
                    [false, true].includes(
                      serviceList[activePort - 1]?.["vendorServiceKey"]
                    )
                      ? true
                      : false
                  }
                  styles={customStyle}
                  options={equipmentFilterCategory}
                  value={equipmentCategory?.filter(
                    (item) =>
                      item.value ===
                      equipmentFormValues?.["equipmentCategoryId"]?.["id"]
                  )}
                ></Select>
              )}

              {errorsEquipment &&
                equipmentFormValues?.equipmentCategoryId?.id === "" && (
                  <span className="error">Select Equipment Category </span>
                )}
            </div>

            <div className="form_control_spread">
              <div
                className={
                  navigationResponse !== null && forNavigation === true
                    ? "equipment_container"
                    : "equipment_container set_background"
                }
              >
                <div className="equipment_container_heading">
                  <span className="heading_container_1">Equipment Name</span>
                  <span className="heading_container_2">Manufacturer</span>
                  <span className="heading_container_3">
                    Experience in Serving (in years)
                  </span>
                  {/* <span className="heading_small">&nbsp;</span> */}
                </div>
                <div className="display_carousel">
                  <AwesomeSlider className="aws-btn">
                    {equipmentFormValues?.serviceDetailsModels?.map(
                      (_, index) => equipmentModelFunc(index)
                    )}
                  </AwesomeSlider>
                </div>
                <div className="no_carousel">
                  {equipmentFormValues?.serviceDetailsModels?.map((_, index) =>
                    equipmentModelFunc(index)
                  )}
                </div>
              </div>
              {/* {isCreateServiceResError && (
                <span className="error">
                  Duplicate Equipment Data. Please fill different data
                </span>
              )} */}
              {isWorkingExperienceError && (
                <span className="error">
                  One or more fields are empty. Please Check
                </span>
              )}

              {isDuplicacyError && (
                <span className="error">Duplicate entry not allowed.</span>
              )}
            </div>

            {/* <div className="form_submit_container">
              {false ? (
                activePort === portList.length && (
                  <>
                    <input
                      type="button"
                      value="Add Country"
                      className="btn btn_add_bidder_service"
                      onClick={() => addNewService()}
                    />

                    <input
                      type="submit"
                      value="Proceed"
                      className="btn btn_submit_form"
                    />
                  </>
                )
              ) : (
                <div className="form_submit_container">
                  {isAddEquipmentandSaveBtnActive &&
                    (isShowEquipmentRemoveIcon.includes(true)
                      ? activePort === isShowEquipmentRemoveIcon.length
                      : activePort === isShowEquipmentRemoveIcon.length) && (
                      <div>
                        <input
                          type="button"
                          value="Add Country"
                          className="btn btn_add_bidder_service"
                          onClick={() => addNewService()}
                        />
                      </div>
                    )}
                  <div className="btn_service">
                    {(createServiceRes &&
                      navigationCheck === false &&
                      createServiceRes.vendorServiceKey === true) ||
                    (registrationStatusKey - 7 > 0 &&
                      navigationCheck === false &&
                      forNavigation === true &&
                      navigationResponse) ? (
                      <input
                        type="button"
                        value=" Next"
                        className="btn btn_submit_form_next"
                        onClick={nextNavigationHandler}
                      />
                    ) : (
                      <>
                        {[false, true].includes(
                          serviceList[activePort - 1]?.["vendorServiceKey"]
                        )
                          ? null
                          : false &&
                            isSaveBtnActiveEquipment && (
                              <input
                                type="button"
                                value="Save12"
                                className="btn btn_submit_form"
                                onClick={equipmentSaveHandler}
                              />
                            )}

                        {isAddEquipmentandSaveBtnActive &&
                          isShowEquipmentRemoveIcon[activePort - 1] && (
                            <>
                              <input
                                type="button"
                                value="Add Category"
                                className="btn btn_add_bidder_service"
                                onClick={() => addEquipment()}
                              />
                            </>
                          )}

                        {isAddEquipmentandSaveBtnActive &&
                          (isShowEquipmentRemoveIcon.includes(true)
                            ? activePort === isShowEquipmentRemoveIcon.length
                            : activePort ===
                              isShowEquipmentRemoveIcon.length) && (
                            <>
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
              )}
            </div> */}

            <div className="form_submit_container">
              {!navigationResponse?.[navigationResponse.length - 1][
                "vendorServiceKey"
              ] &&
                isAddEquipmentandSaveBtnActive &&
                (isShowEquipmentRemoveIcon.includes(true)
                  ? activePort === isShowEquipmentRemoveIcon.length
                  : activePort === isShowEquipmentRemoveIcon.length) && (
                  <div>
                    <input
                      type="button"
                      value="Add Country"
                      className="btn btn_add_bidder_service"
                      onClick={() => addNewService()}
                    />
                  </div>
                )}
              <div className="btn_service">
                {(createServiceRes &&
                  navigationCheck === false &&
                  createServiceRes.vendorServiceKey === true) ||
                (registrationStatusKey - 7 > 0 &&
                  navigationCheck === false &&
                  forNavigation === true &&
                  navigationResponse) ? (
                  <input
                    type="button"
                    value=" Next"
                    className="btn btn_submit_form_next"
                    onClick={nextNavigationHandler}
                  />
                ) : (
                  <>
                    {isSaveBtnActiveEquipment && (
                      <input
                        type="button"
                        value="Save"
                        className="btn btn_submit_form"
                        onClick={equipmentSaveHandler}
                      />
                    )}

                    {isAddEquipmentandSaveBtnActive &&
                      isShowEquipmentRemoveIcon[activePort - 1] && (
                        <>
                          <input
                            type="button"
                            value="Add Category"
                            className="btn btn_add_bidder_service"
                            onClick={() => addEquipment()}
                          />
                        </>
                      )}

                    {isAddEquipmentandSaveBtnActive &&
                      (isShowEquipmentRemoveIcon.includes(true)
                        ? activePort === isShowEquipmentRemoveIcon.length
                        : activePort === isShowEquipmentRemoveIcon.length) && (
                        <>
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
          </div>
        </form>
      </div>
    </div>
  );
};

export default Services;
