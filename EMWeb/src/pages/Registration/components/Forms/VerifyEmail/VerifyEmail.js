import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import OtpInput from "react-otp-input";

import "./VerifyEmail.scss";
import { ToastError } from "./../../../../../components/Tostify";
import CountDownTimer from "./../../../../../components/CountdownTimer/CountdownTimer";
import lockIcon from "./../../../../../assets/images/lock-icon.png";
import blackLockIcon from "./../../../../../assets/images/lock-ico-black.png";
import resendIcon from "./../../../../../assets/images/refresh-icon.png";
import changeIcon from "./../../../../../assets/images/change-icon.png";
import Loading from "../../../../../components/Loading/Loading";
import { getCompanyId } from "../../../../../config";
import VerifyEmailModal from "../../../../../pages/Login/components/Modals/VerifyEmailModal/VerifyEmailModal";

import { verifyEmailFun } from "../../../../../store/vendorRegistration/verifyEmail";
import { getEmailOTPFun } from "../../../../../store/vendorRegistration/getEmailOtp";

import {
  addressGetAPiCall,
  addressGetApiActions,
} from "../../../../../store/vendorRegistration/addressGetApi";

const VerifyEmail = (props) => {
  const dispatch = useDispatch();
  const hoursMinSecs = { hours: 0, minutes: 1, seconds: 0 };

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
  }

  const emailOTPLoading = useSelector((state) => state.emailOTP.isLoading);
  const emailOTPError = useSelector((state) => state.emailOTP.isError);
  const getEmailOTPRes = useSelector((state) => state.emailOTP.verifyEmail);
  const updateEmailLoading = useSelector(
    (state) => state.updateEmailAndgetOTPReducer.isLoading
  );
  const detailsResponse = useSelector((state) => state.detailsReducer.details);
  const verifyEmail = useSelector((state) => state.verifyEmailOTP.verifyEmail);
  const verifyEmailLoading = useSelector(
    (state) => state.verifyEmailOTP.isLoading
  );
  const verifyEmailError = useSelector((state) => state.verifyEmailOTP.isError);
  const updatedEmailRes = useSelector(
    (state) => state.updateEmailAndgetOTPReducer.updatedEmailRes
  );
  const updatedEmailError = useSelector(
    (state) => state.updateEmailAndgetOTPReducer.isError
  );
  const DetailsFormEmail = useSelector(
    (state) => state.saveDetailsForm.DetailsForm
  );

  const addressData = useSelector(
    (state) => state.addressGetApiReducer.getApiRes
  );

  const [otp, setOtp] = useState("");
  const [changeEmail, setChangeEmail] = useState(false);
  const [error, setError] = useState(false);
  const [timerOut, setTimerOut] = useState(false);
  const [timeState, setTimeState] = useState(hoursMinSecs);

  useEffect(() => {
    if (verifyEmail !== null) {
      if (isFormTypePartner) {
        props.setFormLabel("verifyMobile", 1);
      } else if (isFormTypeCustomer) {
        props.setFormLabel("verifyMobile", 1);
      } else if (isFormTypePartnersIndividual) {
        props.setFormLabel("verifyMobile", 1);
      }
    }
  }, [verifyEmail]);

  useEffect(() => {
    if (emailOTPError !== "") {
      ToastError(emailOTPError);
    } else if (verifyEmailError !== "") {
      ToastError(verifyEmailError);
    }
  }, [emailOTPError, verifyEmailError]);

  const toggleFunc = () => {
    setChangeEmail(!changeEmail);
    setOtp("");
  };

  useEffect(() => {
    if (isFormTypePartnersIndividual) {
      dispatch(addressGetAPiCall(`/vendor/get/${getCompanyId()}`));
    }
  }, [dispatch, isFormTypePartnersIndividual]);

  useEffect(() => {
    if (updatedEmailRes) {
      setChangeEmail(false);
    }
  }, [updatedEmailRes]);

  const resendEmailFunc = () => {
    setError(false);
    dispatch(
      getEmailOTPFun({
        email:
          detailsResponse === null
            ? addressData?.userModel?.email
            : DetailsFormEmail?.email,
      })
    );
    setOtp("");
    setTimerOut(false);
  };

  const handleFormSubmission = (event) => {
    event.preventDefault();

    if (otp.length !== 6) {
      ToastError("Please enter proper OTP.");
      return false;
    } else {
      let data = {
        otp,
        email:
          detailsResponse === null
            ? addressData?.userModel?.email
            : DetailsFormEmail?.email,
      };
      dispatch(verifyEmailFun(data));
    }
  };

  const onTimerExpire = () => {
    setTimerOut(true);
  };

  return (
    <div className="form_container_area">
      <div className="verify_email_form positioning_container">
        {(emailOTPLoading || verifyEmailLoading || updateEmailLoading) && (
          <Loading text={"Loading ...."} />
        )}

        {changeEmail && <VerifyEmailModal onClose={toggleFunc} />}

        {isFormTypePartner && (
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
                    goForward: false,
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
        )}

        {isFormTypeCustomer && (
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
        )}

        {isFormTypePartnersIndividual && detailsResponse !== null && (
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
        )}

        <form onSubmit={handleFormSubmission}>
          <div className="verify_container">
            <h4 className="heading">Verify Email</h4>
            <div className="display_lock_icon">
              <img
                src={lockIcon}
                className="lock-icon theme-lock"
                alt="lock-icon"
              />
              <img
                src={blackLockIcon}
                className="lock-icon black-lock"
                alt="lock-icon"
              />
            </div>
            <div className="subheading_wrapper">
              <p className="subheading">An OTP has been sent to</p>
              <p className="user_email">
                {detailsResponse === null
                  ? addressData?.userModel?.email
                  : DetailsFormEmail?.email}
                <span className="change_option" onClick={toggleFunc}>
                  Change
                  <img
                    src={changeIcon}
                    alt="change-icon"
                    className="change_icon"
                  />
                </span>
              </p>
            </div>
            <div className="otp_container">
              <div className="otp_main_container">
                <span className="text">Enter OTP</span>
                <span className="timing">
                  {(detailsResponse || addressData) !== null &&
                    getEmailOTPRes == null &&
                    updatedEmailRes == null && (
                      <CountDownTimer onTimerExpire={onTimerExpire} />
                    )}
                  {(detailsResponse || addressData) !== null &&
                    getEmailOTPRes !== null &&
                    updatedEmailRes === null && (
                      <CountDownTimer onTimerExpire={onTimerExpire} />
                    )}
                  {(detailsResponse || addressData) !== null &&
                    updatedEmailRes !== null &&
                    getEmailOTPRes === null && (
                      <CountDownTimer onTimerExpire={onTimerExpire} />
                    )}
                  {(detailsResponse || addressData) !== null &&
                    getEmailOTPRes !== null &&
                    updatedEmailRes !== null && (
                      <CountDownTimer onTimerExpire={onTimerExpire} />
                    )}
                </span>
              </div>
              <div className="enter_otp_container">
                <OtpInput
                  className="otpInput"
                  isInputNum={true}
                  value={otp}
                  onChange={(otp) => setOtp(otp)}
                  numInputs={6}
                  inputStyle={{ width: "45px" }}
                  // isDisabled={timerOut}
                  // separator={<span>&nbsp;&nbsp;</span>}
                />
              </div>
              {error && otp === "" && (
                <span className="error">Required fields</span>
              )}
              <div className="did_not_receive_otp_container">
                <p className="text">Didn’t receive the OTP?</p>
                {timerOut ? (
                  <a className="resend_btn " onClick={resendEmailFunc}>
                    Resend
                    <img
                      src={resendIcon}
                      alt="resend-icon"
                      className="resend_icon"
                    />
                  </a>
                ) : (
                  <a
                    className="resend_btn disable-anchor"
                    onClick={resendEmailFunc}
                  >
                    Resend &nbsp;
                    <img
                      src={resendIcon}
                      alt="resend-icon"
                      className="resend_icon"
                    />
                  </a>
                )}
              </div>
            </div>
          </div>
          <div className="submit_container">
            <input type="submit" value="Next" className="a_submit" />
          </div>
        </form>
      </div>
    </div>
  );
};

export default VerifyEmail;
