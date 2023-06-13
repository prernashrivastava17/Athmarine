import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import OtpInput from "react-otp-input";
import { useHistory } from "react-router-dom";
import { ToastError } from "./../../../../../components/Tostify";
import CountDownTimer from "./../../../../../components/CountdownTimer/CountdownTimer";
import "./VerifyMobile.scss";
import lockIcon from "./../../../../../assets/images/lock-icon.png";
import blackLockIcon from "./../../../../../assets/images/lock-ico-black.png";
import resendIcon from "./../../../../../assets/images/refresh-icon.png";
import changeIcon from "./../../../../../assets/images/change-icon.png";
import Loading from "../../../../../components/Loading/Loading";
import VerifyMobileModal from "../../../../../pages/Login/components/Modals/VerifyMobileModal/VerifyMobileModal";
import IndividualPayment from "../../Modals/IndividualPayment/IndividualPayment";
import { getMobileOTPFunc } from "../../../../../store/vendorRegistration/getMobileOtp";
import { verifyMobileOTPFunc } from "../../../../../store/vendorRegistration/verifyMobileOtp";
import { getCompanyId } from "../../../../../config";
import {
  addressGetAPiCall,
  addressGetApiActions,
} from "../../../../../store/vendorRegistration/addressGetApi";

const VerifyMobile = (props) => {
  const dispatch = useDispatch();
  const history = useHistory();
  let isFormTypePartner = props.selectedUser === "partner" ? true : false;
  let isFormTypeCustomer = props.selectedUser === "customer" ? true : false;
  let isFormTypePartnersIndividual =
    props.selectedUser === "partners-individual" ? true : false;

  const isLoading_getMobileOTP = useSelector(
    (state) => state.mobileOTP.isLoading
  );
  const mobileOTPError = useSelector((state) => state.mobileOTP.isError);
  const getMobileOTPRes_success = useSelector(
    (state) => state.mobileOTP.getMobileOTPRes
  );
  const detailsResponse_success = useSelector(
    (state) => state.detailsReducer.details
  );

  const isLoading_verifyMobileOTP = useSelector(
    (state) => state.verifyMobileOTP.isLoading
  );
  const DetailsFormMobile = useSelector(
    (state) => state.saveDetailsForm.DetailsForm
  );

  const verifyMobile = useSelector(
    (state) => state.verifyMobileOTP.verifyMobileRes
  );
  const verifyMobileError = useSelector(
    (state) => state.verifyMobileOTP.isError
  );

  const isError_updateUserPhoneNumber = useSelector(
    (state) => state.updateUserMobileOTPReducer.isError
  );
  const isLoading_updateUserPhoneNumber = useSelector(
    (state) => state.updateUserMobileOTPReducer.isLoading
  );
  const updateMobileRes = useSelector(
    (state) => state.updateUserMobileOTPReducer.updateMobileRes
  );

  const addressData = useSelector(
    (state) => state.addressGetApiReducer.getApiRes
  );

  const [otp, setOtp] = useState("");
  const [mobile, setMobile] = useState("");
  const [toggleMobile, setToggleMobile] = useState(false);
  const [timerOut, setTimerOut] = useState(false);
  const [error, setError] = useState(false);
  const [isConfirmationModal, setIsConfirmationModal] = useState(false);

  useEffect(() => {
    if (updateMobileRes) {
      setToggleMobile(false);
    }
  }, [updateMobileRes]);

  useEffect(() => {
    if (isFormTypePartnersIndividual) {
      dispatch(addressGetAPiCall(`/vendor/get/${getCompanyId()}`));
    }
  }, [dispatch, isFormTypePartnersIndividual]);

  useEffect(() => {
    if (verifyMobile !== null) {
      if (isFormTypePartner) {
        props.setFormLabel("companyHead", 1);
      } else if (isFormTypeCustomer) {
        props.setFormLabel("businessInformation", 1);
      } else if (
        isFormTypePartnersIndividual &&
        detailsResponse_success !== null
      ) {
        props.setFormLabel("businessInformation", 1);
      } else if (
        isFormTypePartnersIndividual &&
        detailsResponse_success === null
      ) {
        props.setFormLabel("engineer", 2);
      }
    }
  }, [verifyMobile]);

  useEffect(() => {
    if (mobileOTPError !== "") {
      ToastError(mobileOTPError);
    } else if (verifyMobileError !== "") {
      ToastError(verifyMobileError);
    }
  }, [mobileOTPError, verifyMobileError]);

  const handleFormSubmission = (event) => {
    event.preventDefault();

    if (otp.length !== 6) {
      ToastError("Please enter proper OTP.");
      return false;
    } else {
      let data = {
        phoneCode:
          detailsResponse_success === null
            ? addressData?.userModel?.phoneCode
            : DetailsFormMobile?.phoneCode,
        otp,
        mobile:
          detailsResponse_success === null
            ? addressData?.userModel?.primaryPhone
            : DetailsFormMobile?.primaryPhone,
      };
      dispatch(verifyMobileOTPFunc(data));
    }
  };

  const handleBlurFunc = () => {
    setToggleMobile(false);
    dispatch(
      getMobileOTPFunc({
        mobile:
          detailsResponse_success === null
            ? addressData?.userModel?.primaryPhone
            : DetailsFormMobile?.primaryPhone,
      })
    );
  };

  const toggleFunc = () => {
    setToggleMobile(!toggleMobile);
    setOtp("");
  };

  const resendMobileOTPFunc = () => {
    setError(false);
    dispatch(
      getMobileOTPFunc({
        mobile: `${
          detailsResponse_success === null
            ? addressData?.userModel?.phoneCode
            : DetailsFormMobile?.phoneCode
        }${
          detailsResponse_success === null
            ? addressData?.userModel?.primaryPhone
            : DetailsFormMobile?.primaryPhone
        }`,
      })
    );
    setOtp("");
    setTimerOut(false);
  };

  const onTimerExpire = () => {
    setTimerOut(true);
  };

  const onCancelHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
  };

  const onOkHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
    history.push(`/payment`);
  };

  return (
    <div className="form_container_area">
      <div className="verify_email_form positioning_container">
        {(isLoading_getMobileOTP ||
          isLoading_verifyMobileOTP ||
          isLoading_updateUserPhoneNumber) && <Loading text={"Loading ...."} />}

        {toggleMobile && <VerifyMobileModal onClose={toggleFunc} />}

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

        {isFormTypePartnersIndividual && detailsResponse_success !== null && (
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
          {isConfirmationModal && (
            <IndividualPayment cancel={onCancelHandler} ok={onOkHandler} />
          )}

          <div className="verify_container">
            <h4 className="heading"> Verify Mobile </h4>
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
              {toggleMobile ? (
                ""
              ) : (
                <p className="user_email">
                  your Mobile Number
                  {detailsResponse_success === null
                    ? addressData?.userModel?.phoneCode
                    : DetailsFormMobile?.phoneCode}
                  {detailsResponse_success === null
                    ? addressData?.userModel?.primaryPhone
                    : DetailsFormMobile?.primaryPhone}
                  <span className="change_option" onClick={toggleFunc}>
                    Change
                    <img
                      src={changeIcon}
                      alt="change-icon"
                      className="change_icon"
                    />
                  </span>
                </p>
              )}
            </div>
            <div className="otp_container">
              <div className="otp_main_container">
                <span className="text">Enter OTP</span>
                <span className="timing">
                  {/* {
                                        (getMobileOTPRes_success) &&
                                        <CountDownTimer onTimerExpire={onTimerExpire} />
                                    }
                                    {
                                        (!getMobileOTPRes_success  && detailsResponse_success) &&
                                        <CountDownTimer onTimerExpire={onTimerExpire} />
                                    } */}

                  {(detailsResponse_success || addressData) !== null &&
                    getMobileOTPRes_success == null &&
                    updateMobileRes == null && (
                      <CountDownTimer onTimerExpire={onTimerExpire} />
                    )}
                  {(detailsResponse_success || addressData) !== null &&
                    getMobileOTPRes_success !== null &&
                    updateMobileRes === null && (
                      <CountDownTimer onTimerExpire={onTimerExpire} />
                    )}
                  {(detailsResponse_success || addressData) !== null &&
                    updateMobileRes !== null &&
                    getMobileOTPRes_success === null && (
                      <CountDownTimer onTimerExpire={onTimerExpire} />
                    )}
                  {(detailsResponse_success || addressData) !== null &&
                    getMobileOTPRes_success !== null &&
                    updateMobileRes !== null && (
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
                  // separator={<span>&nbsp;&nbsp;</span>}
                />
              </div>
              {error && otp === "" && (
                <span className="error">Required fields</span>
              )}
              <div className="did_not_receive_otp_container">
                <p className="text">Didn’t receive the OTP?</p>
                {timerOut ? (
                  <a className="resend_btn" onClick={resendMobileOTPFunc}>
                    Resend &nbsp;
                    <img
                      src={resendIcon}
                      alt="resend-icon"
                      className="resend_icon"
                    />
                  </a>
                ) : (
                  <a
                    className="resend_btn disable-anchor"
                    onClick={resendMobileOTPFunc}
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

export default VerifyMobile;
