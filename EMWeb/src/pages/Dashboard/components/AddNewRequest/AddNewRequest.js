import { useEffect, useState } from "react";
import Sidebar from "./../Sidebar/Sidebar";
import "./AddNewRequest.scss";
import { useDispatch, useSelector } from "react-redux";
import { getCountries } from "../../../../store/common/country";
import { getPortsByCountryId } from "../../../../store/common/getPortByCountry";
import { getCategories } from "../../../../store/vendorRegistration/category";
import { ToastError } from "../../../../components/Tostify";
import Loading from "../../../../components/Loading/Loading";
import { getVessel } from "../../../../store/dashboard/requestorDashboard/vessel";
import ConfirmationRequestModal from "../../../Registration/components/Modals/ConfirmationRequestModal/ConfirmationRequestModal";

const AddNewRequest = () => {
  const dispatch = useDispatch();
  const vessels = useSelector((state) => state.getVesselReducer.state);
  const isVesselLoading = useSelector(
    (state) => state.getVesselReducer.isLoading
  );

  const countries = useSelector((state) => state.countries.countries);
  const isCountriesLoading = useSelector((state) => state.countries.isLoading);
  const isCountriesError = useSelector((state) => state.countries.isError);

  const ports = useSelector((state) => state.CountryPortsReducer.CountryPorts);
  const isPortsLoading = useSelector(
    (state) => state.CountryPortsReducer.isLoading
  );
  const isPortsError = useSelector(
    (state) => state.CountryPortsReducer.isError
  );

  const categories = useSelector(
    (state) => state.engineerCategories.categories
  );
  const isCategoriesLoading = useSelector(
    (state) => state.engineerCategories.isLoading
  );
  const isCategoriesError = useSelector(
    (state) => state.engineerCategories.isError
  );

  const [vessel, setVessel] = useState(null);
  const [country, setCountry] = useState(null);
  const [port, setPort] = useState(null);
  const [email, setEmail] = useState(null);
  const [eta, setETA] = useState(null);
  const [etd, setETD] = useState(null);
  const [serviceRequiredTiming, setServiceRequiredTiming] = useState(null);
  const [category, setCategory] = useState(null);
  const [currencyAcceptable, setCurrencyAcceptable] = useState(null);
  const [equipmentName, setEquipmentName] = useState(null);
  const [issueType, setIssueType] = useState(null);
  const [manufacture, setManufacture] = useState(null);
  const [model, setModel] = useState(null);
  const [imoNumber, setImoNumber] = useState(null);
  const [time, setTime] = useState(null);
  const [isConfirmationModal, setIsConfirmationModal] = useState(false);

  useEffect(() => {
    if (vessels === null) {
      dispatch(getVessel());
    }
    if (countries === null) {
      dispatch(getCountries());
    }
    if (categories === null) {
      dispatch(getCategories());
    }
  }, [dispatch, vessels, countries, categories]);

  useEffect(() => {
    if (country !== null) {
      dispatch(getPortsByCountryId(country));
    }
  }, [dispatch, country]);

  useEffect(() => {
    if (isCountriesError !== ``) {
      ToastError(isCountriesError);
    }
    if (isPortsError !== ``) {
      ToastError(isPortsError);
    }
    if (isCategoriesError !== ``) {
      ToastError(isCategoriesError);
    }
  }, [isCountriesError, isPortsError, isCategoriesError]);

  const handleFormSubmission = (e) => {
    e.preventDefault();
  };
  const onCancelHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
  };

  const onOkHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
  };

  const requestHandle = () => {
    setIsConfirmationModal(!isConfirmationModal);
  };

  return (
    <div className="add_new_request_container">
      {/* {(isVesselLoading || isCountriesLoading || isPortsLoading || isCategoriesLoading) && <Loading text="Loading..." />} */}

      <div className="bredcrum">
        Dashboard <span className="active_page"> Add New Request</span>
      </div>

      {isConfirmationModal && (
        <ConfirmationRequestModal cancel={onCancelHandler} ok={onOkHandler} />
      )}

      <div className="main_heading">Add New Request</div>
      <form onSubmit={handleFormSubmission}>
        <div className="request_form">
          <div className="request_details_1">
            <div className="form_input_container">
              <label htmlFor="">Vessel</label>
              <select
                className="select_input_control"
                value={vessel}
                onChange={(e) => setVessel(e.target.value)}
              >
                <option value=""></option>
                {vessels?.map((vessel) => (
                  <option value="vessle 1">vessle 1</option>
                ))}
              </select>
            </div>

            <div className="form_input_container">
              <label htmlFor="">IMO Number</label>
              <select
                className="select_input_control"
                value={imoNumber}
                onChange={(e) => setImoNumber(e.target.value)}
              >
                <option value=""></option>
                {imoNumber?.map((imoNumber) => (
                  <option value="imoNumber 1">imoNumber 1</option>
                ))}
              </select>
            </div>

            <div className="form_input_container">
              <label htmlFor="">Country</label>
              <select
                className="select_input_control"
                value={country}
                onChange={(e) => setCountry(e.target.value)}
              >
                <option value=""></option>
                {countries?.map((country) => (
                  <option value={country?.id}> {country?.name} </option>
                ))}
              </select>
            </div>

            <div className="form_input_container">
              <label htmlFor="">Port</label>
              <select
                className="select_input_control"
                value={port}
                onChange={(e) => setPort(e.target.value)}
              >
                <option value=""></option>
                {ports?.map((port) => (
                  <option value={port?.id}> {port?.name} </option>
                ))}
              </select>
            </div>
          </div>

          <hr className="horizontal_line" />

          <div className="request_details_2">
            <div className="form_input_container">
              <label htmlFor="">E-mail ID</label>
              <input
                type="text"
                className="input_control"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>

            <div className="form_input_container">
              <label htmlFor="">E.T.A</label>
              <input
                type="text"
                className="input_control"
                value={eta}
                onChange={(e) => setETA(e.target.value)}
              />
            </div>

            <div className="form_input_container">
              <label htmlFor="">E.T.D</label>
              <input
                type="text"
                className="input_control"
                value={etd}
                onChange={(e) => setETD(e.target.value)}
              />
            </div>

            <div className="form_input_container">
              <label htmlFor="">Service Required Timing</label>
              <select className="select_input_control">
                <option value=""></option>
                <option value="vessle 1">vessle 1</option>
                <option value="vessle 2">vessle 2</option>
                <option value="vessle 3">vessle 3</option>
              </select>
            </div>

            <div className="form_input_container">
              <label htmlFor="">Category</label>
              <select
                className="select_input_control"
                value={category}
                onChange={(e) => setCategory(e.target.value)}
              >
                <option value=""></option>
                {categories?.map((category) => (
                  <option value={category?.id}> {category?.name} </option>
                ))}
                <option value="vessle 1">vessle 1</option>
                <option value="vessle 2">vessle 2</option>
                <option value="vessle 3">vessle 3</option>
              </select>
            </div>

            <div className="form_input_container">
              <label htmlFor="">Currency Acceptable</label>
              <select
                className="select_input_control"
                value={currencyAcceptable}
                onChange={(e) => setCurrencyAcceptable(e.target.value)}
              >
                <option value=""></option>
                {countries?.map((currency) => (
                  <option value={currency?.currency}>
                    {currency?.currency}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <hr className="horizontal_line" />

          <div className="request_details_3">
            <div className="form_input_container">
              <label htmlFor="">Equipment Name</label>
              <input
                type="text"
                className="input_control"
                value={equipmentName}
                onChange={(e) => setEquipmentName(e.target.value)}
              />
            </div>

            <div className="form_input_container">
              <label htmlFor="">Issue Type</label>
              <input
                type="text"
                className="input_control"
                value={issueType}
                onChange={(e) => setIssueType(e.target.value)}
              />
            </div>

            <div className="form_input_container">
              <label htmlFor="">Manufacturer/OEM</label>
              <input
                type="text"
                className="input_control"
                value={manufacture}
                onChange={(e) => setManufacture(e.target.value)}
              />
            </div>

            <div className="form_input_container">
              <label htmlFor="">Model</label>
              <input
                type="text"
                className="input_control"
                value={model}
                onChange={(e) => setModel(e.target.value)}
              />
            </div>
          </div>

          <hr className="horizontal_line" />

          <div>
            <p className="addNewRequest_heading">Quotation needed in:</p>
            <div className="timeManagement_class">
              <div className="form_input_container_time">
                <div className="first_time_container">
                  <select
                    className="select_input_time"
                    value={time}
                    onChange={(e) => setTime(e.target.value)}
                  >
                    <option value="" disabled selected hidden>
                      72 hours
                    </option>
                    <option value="time 1">71-60 Hrs</option>
                    <option value="time 2">59-30 Hrs</option>
                    <option value="time 3">29-01 Hrs</option>
                  </select>
                </div>

                <div className="second_time_container">
                  <select
                    className="select_input_perticularTime"
                    value={time}
                    onChange={(e) => setTime(e.target.value)}
                  >
                    <option value="" disabled selected hidden>
                      72 hours
                    </option>
                    <option value="time 1">71-60 Hrs</option>
                    <option value="time 2">59-30 Hrs</option>
                    <option value="time 3">29-01 Hrs</option>
                  </select>
                </div>
              </div>

              <div className="color_container_box">
                <div className="indicate_box">
                  <div className="general_block"></div>
                  <div>General</div>
                </div>
                <div className="indicate_box">
                  <div className="urgent_block"></div>
                  <div>Urgent</div>
                </div>
                <div className="indicate_box">
                  <div className="emergency_block"></div>
                  <div>Emergency</div>
                </div>
              </div>
            </div>
          </div>

          <hr className="horizontal_line" />

          <div>
            <p className="addNewRequest_heading">
              Description of problem / service required:
            </p>
            <div className="msg_box">
              Lorem Ipsum is simply dummy text of the printing and typesetting
              industry. Lorem Ipsum has been the industry’s standard dummy text
              ever since the 1500s, when an unknown printer took a galley of
              type and scrambledLorem Ipsum is simply dummy text of the printing
              and typesetting industry. Lorem Ipsum has been the industry’s
              standard dummy text ever since the 1500s, when an unknown printer
              took a galley of type and scrambled Lorem Ipsum is simply dummy
              text of the printing and typesetting industry. Lorem Ipsum has
              been the industry’s standard dummy text ever since the 1500s, when
              an unknown printer took a galley of type and scrambledLorem Ipsum
              is simply dummy text of the printing and typesetting industry.
              Lorem Ipsum has been the industry’s standard dummy text ever since
              the 1500s, when an unknown printer took a galley of type and
              scrambled Lorem Ipsum is simply dummy text of the printing and
              typesetting industry. Lorem Ipsum has been the industry’s standard
              dummy text ever since the 1500s, when an unknown printer took a
              galley of type and scrambledLorem Ipsum is simply dummy text of
              the printing and typesetting industry. Lorem Ipsum has been the
              industry’s standard dummy text ever since the 1500s, when an
              unknown printer took a galley of type and scrambled Lorem Ipsum is
              simply dummy text of the printing and typesetting industry. Lorem
              Ipsum has been the industry’s standard dummy text ever since the
              1500s, when an unknown printer took a galley of type and
              scrambledLorem Ipsum is simply dummy text of the printing and
              typesetting industry. Lorem Ipsum has been the industry’s standard
              dummy text ever since the 1500s, when an unknown printer took a
              galley of type and scrambled Lorem Ipsum is simply dummy Lorem
              Ipsum is simply dummy text of the printing and typesetting
              industry. Lorem Ipsum has been the industry’s standard dummy text
              ever since the 1500s, when an unknown printer took a galley of
              type and scrambledLorem Ipsum is simply dummy text of the printing
              and typesetting industry. Lorem Ipsum has been the industry’s
              standard dummy text ever since the 1500s, when an unknown printer
              took a galley of type and scrambled Lorem Ipsum is simply dummy
              text of the printing and typesetting industry. Lorem Ipsum has
              been the industry’s standard dummy text ever since the 1500s, when
              an unknown printer took a galley of type and scrambledLorem Ipsum
            </div>
          </div>
          <div className="form_submit_container">
            <input
              type="submit"
              value="Upload Document"
              className="btn btn_update"
            />
          </div>
        </div>

        <div className="form_submit_container">
          <input
            type="submit"
            value="Submit"
            className="btn btn_update"
            onClick={requestHandle}
          />
          <input type="button" value="Cancel" className="btn btn_cancel" />
        </div>
      </form>
    </div>
  );
};

export default AddNewRequest;
