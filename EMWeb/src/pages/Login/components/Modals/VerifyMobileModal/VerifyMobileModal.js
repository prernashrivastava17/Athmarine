/* eslint-disable jsx-a11y/alt-text */
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import Select from "react-select";
import "./VerifyMobileModal.scss";
import Backdrop from "../../../../../components/Backdrop/Backdrop";
import lockIcon from "./../../../../../assets/images/lock-ico-black.png";
import { updateUserMobile_getOTPFunc } from "../../../../../store/vendorRegistration/updateUserMobile";
import { updateMobile } from "../../../../../store/vendorRegistration/saveDetailsForm";
import { numberValidation } from "../../../../../utils/helpers";
import { getCompanyId, getUserId } from "../../../../../config";

const VerifyMobileModal = (props) => {
  const dispatch = useDispatch();
  const isLoading_getMobileOTP = useSelector(
    (state) => state.mobileOTP.isLoading
  );
  const isLoading_UpdateUserMobile_GetOTP = useSelector(
    (state) => state.updateUserMobileOTPReducer.isLoading
  );
  const DetailsFormMobile = useSelector(
    (state) => state.saveDetailsForm.DetailsForm
  );
  const updateMobileRes = useSelector(
    (state) => state.updateUserMobileOTPReducer.updateMobileRes
  );
  const countries = useSelector((state) => state.countries.countries);

  const [mobile, setMobile] = useState("");
  const [error, setError] = useState(false);
  const [phoneCode, setPhoneCode] = useState("");
  const [hide, setHide] = useState();

  useEffect(() => {
    if (mobile !== "") {
      if (updateMobileRes === "Success") {
        dispatch(
          updateMobile({
            phoneCode: phoneCode,
            mobile: mobile,
          })
        );
      }
    }
  }, [updateMobileRes]);

  const handleFormSubmission = (e) => {
    e.preventDefault();
    if (mobile !== "" && numberValidation(mobile)) {
      let data = {
        newPhoneCode: phoneCode,
        newPrimaryPhone: mobile,
        userId: getUserId() === null ? getCompanyId() : getUserId(),
      };
      dispatch(updateUserMobile_getOTPFunc(data));
    } else {
      setError(true);
    }
  };

  const closePopupHandler = () => {
    props.onClose();
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

  const customStyles = {
    container: (base, state) => ({
      ...base,
    }),
    menu: (styles) => ({
      ...styles,
      width: "320px",
    }),
  };

  return (
    <div className="verify_mobile_backdrop">
      <div className="modal_reset_password">
        <div className="lock_container">
          <img src={lockIcon} alt="lock-icon" className="lock_icon" />
          <p className="modal_heading"> Change Phone </p>
        </div>

        <div className="text_info">
          <p className="text">
            Please enter new mobile number. You will receive an OTP on your new
            mobile number for verification.
          </p>
        </div>

        <div className="form_container">
          <form onSubmit={handleFormSubmission}>
            <div className="form_control_area" style={{ textAlign: "left" }}>
              <label className="form_label">
                Phone Number
                <span className="label_mandatory">*</span>
              </label>
              <div className="input_container">
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
                  {phoneCode}
                </div>
                <Select
                  onChange={(e) => {
                    setPhoneCode(e.value);
                  }}
                  onInputChange={setHide}
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

                <input
                  type="text"
                  className="form_control"
                  name="mobile"
                  value={mobile}
                  onChange={(e) => setMobile(e.target.value)}
                />
                <div style={{ display: "flex" }}>
                  {error && (mobile === "" || !numberValidation(mobile)) && (
                    <span className="error">
                      Phone number should be between 6 to 13 digit
                    </span>
                  )}
                </div>
              </div>
            </div>

            <div className="submit_container">
              <button type="submit"> Update </button>
            </div>
          </form>
        </div>

        <div className="close_container_mobile" onClick={closePopupHandler}>
          <span className="close_icon">&times;</span>
        </div>
      </div>
    </div>
  );
};

export default VerifyMobileModal;
