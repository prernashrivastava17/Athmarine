import React, { useState, useEffect } from "react";
import "./VerifyWebEmailOTP.scss";

import lock_icon from "./../../assets/images/lock-icon.png";
import OtpInput from "react-otp-input";
import refresh_icon from "./../../assets/images/refresh-icon.png";
import Header from "./components/Header/Header";
import CountDownTimer from "../../components/CountdownTimer/CountdownTimer";
import Loading from "../../components/Loading/Loading";
import { ToastError } from "../../components/Tostify";

import { useDispatch, useSelector } from "react-redux";
import { getEncodedUserInformation } from "../../store/vendorRegistration/getUserByEncodedId";
import { verifyEmailFun } from "../../store/vendorRegistration/verifyEmail";
import { getEmailOTPFun } from "../../store/vendorRegistration/getEmailOtp";
import Footer from "../../components/Footer/Footer";

const VerifyWebEmailOTP = (props) => {
  const dispatch = useDispatch();
  const encodedUserInformation = useSelector(
    (state) => state.encodedUserReducer.userInformationRes
  );
  const Loading_encodedUserInformation = useSelector(
    (state) => state.encodedUserReducer.isLoading
  );
  const getEmailOTPRes = useSelector((state) => state.emailOTP.verifyEmail);
  const emailOTPLoading = useSelector((state) => state.emailOTP.isLoading);
  const emailOTPError = useSelector((state) => state.emailOTP.isError);
  const verifyEmailLoading = useSelector(
    (state) => state.verifyEmailOTP.isLoading
  );
  const verifyEmail = useSelector((state) => state.verifyEmailOTP.verifyEmail);
  const verifyEmailError = useSelector((state) => state.verifyEmailOTP.isError);

  const [otp, setOtp] = useState("");
  const [timerOut, setTimerOut] = useState(false);

  useEffect(() => {
    if (emailOTPError !== "") {
      ToastError(emailOTPError);
    } else if (verifyEmailError !== "") {
      ToastError(verifyEmailError);
    }
  }, [emailOTPError, verifyEmailError]);

  useEffect(() => {
    if (verifyEmail === "Success") {
      props.verifiedEmail();
    }
  }, [verifyEmail]);

  const resendEmailFunc = () => {
    dispatch(getEmailOTPFun({ email: encodedUserInformation?.email }));
    setOtp("");
    setTimerOut(false);
  };

  const onTimerExpire = () => {
    setTimerOut(true);
  };

  const handleFormSubmission = (event) => {
    event.preventDefault();
    if (otp.length !== 6) {
      ToastError("Please enter proper OTP.");
      return false;
    }

    let data = { otp, email: encodedUserInformation?.email };
    if (otp !== "") {
      dispatch(verifyEmailFun(data));
    } else {
      ToastError("Please enter OTP!");
    }
  };

  return (
    <>
      <Header />
      {(emailOTPLoading ||
        verifyEmailLoading ||
        Loading_encodedUserInformation) && <Loading text={"Loading ...."} />}

      <section className="my_email_otp_container">
        <div className="login_wrapper">
          <form onSubmit={handleFormSubmission}>
            <div className="card_body">
              <h3 className="card_heading">Verify Email</h3>
              <div className="img_container">
                <img src={lock_icon} alt="lock-icon" />
              </div>
              <p className="card_text">
                An OTP has been sent to
                <span className="dynamic_email">email id</span>
                {encodedUserInformation?.email}
              </p>
              <div className="text_with_timer">
                <p className="otp_text">Enter OTP</p>
                <span className="timer1" id="timer1">
                  {getEmailOTPRes == null && (
                    <CountDownTimer onTimerExpire={onTimerExpire} />
                  )}
                  {getEmailOTPRes !== null && (
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
                />
              </div>
              <div className="secondary_text">
                <p className="first_text">Didn't receive the OTP?</p>
                {timerOut ? (
                  <p className="resend " onClick={resendEmailFunc}>
                    Resend
                    <img
                      src={refresh_icon}
                      alt="resend-icon"
                      className="resend_icon"
                    />
                  </p>
                ) : (
                  <p
                    className="resend disable-anchor"
                    onClick={resendEmailFunc}
                  >
                    Resend
                    <img
                      src={refresh_icon}
                      alt="resend-icon"
                      className="resend_icon"
                    />
                  </p>
                )}
              </div>
              <div className="submit_container">
                <input type="submit" value="Verify" className="a_submit" />
              </div>
            </div>
          </form>
        </div>
      </section>
      <Footer />
    </>
  );
};

export default VerifyWebEmailOTP;
