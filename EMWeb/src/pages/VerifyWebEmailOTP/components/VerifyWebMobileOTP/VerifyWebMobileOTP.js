import React, { useState, useEffect } from "react";
import "./VerifyWebMobileOTP.scss";

import lock_icon from "./../../../../assets/images/lock-icon.png";
import OtpInput from "react-otp-input";
import refresh_icon from "./../../../../assets/images/refresh-icon.png";
import Header from "./../Header/Header";
import Loading from "../../../../components/Loading/Loading";
import { ToastError } from "../../../../components/Tostify";

import { useDispatch, useSelector } from "react-redux";
import { verifyMobileOTPFunc } from "../../../../store/vendorRegistration/verifyMobileOtp";
import CountDownTimer from "../../../../components/CountdownTimer/CountdownTimer";
import { getMobileOTPFunc } from "../../../../store/vendorRegistration/getMobileOtp";
import Footer from "../../../../components/Footer/Footer";

const VerifyWebMobileOTP = (props) => {
  const dispatch = useDispatch();
  const encodedUserInformation = useSelector(
    (state) => state.encodedUserReducer.userInformationRes
  );
  const isLoading_verifyMobileOTP = useSelector(
    (state) => state.verifyMobileOTP.isLoading
  );
  const verifyMobile = useSelector(
    (state) => state.verifyMobileOTP.verifyMobileRes
  );
  const isLoading_getMobileOTP = useSelector(
    (state) => state.mobileOTP.isLoading
  );
  const getMobileOTPRes_success = useSelector(
    (state) => state.mobileOTP.getMobileOTPRes
  );
  const mobileOTPError = useSelector((state) => state.mobileOTP.isError);
  const verifyMobileError = useSelector(
    (state) => state.verifyMobileOTP.isError
  );

  const [otp, setOtp] = useState("");
  const [timerOut, setTimerOut] = useState(false);

  useEffect(() => {
    if (mobileOTPError !== "") {
      ToastError(mobileOTPError);
    } else if (verifyMobileError !== "") {
      ToastError(verifyMobileError);
    }
  }, [mobileOTPError, verifyMobileError]);

  const onTimerExpire = () => {
    setTimerOut(true);
  };

  const resendMobileOTPFunc = () => {
    dispatch(
      getMobileOTPFunc({
        mobile: `${encodedUserInformation.phoneCode}${encodedUserInformation?.primaryPhone}`,
      })
    );
    setOtp("");
    setTimerOut(false);
  };

  useEffect(() => {
    if (verifyMobile === "Success") {
      props.verifiedMobileFunc();
    }
  }, [verifyMobile]);

  const handleFormSubmission = (event) => {
    event.preventDefault();

    if (otp.length !== 6) {
      ToastError("Please enter OTP!");
      return false;
    }

    let data = {
      phoneCode: encodedUserInformation?.phoneCode,
      otp,
      mobile: encodedUserInformation?.primaryPhone,
    };
    if (otp !== "") {
      dispatch(verifyMobileOTPFunc(data));
    }
  };

  return (
    <>
      {(isLoading_getMobileOTP || isLoading_verifyMobileOTP) && (
        <Loading text={"Loading ...."} />
      )}

      <Header />
      <section className="my_mobile_otp_container">
        <form onSubmit={handleFormSubmission}>
          <div className="login_wrapper">
            <div className="card_body">
              <h3 className="card_heading">Verify Mobile</h3>
              <div className="img_container">
                <img src={lock_icon} alt="lock-icon" />
              </div>
              <p className="card_text">
                An OTP has been sent to
                <span className="dynamic_email"> phone number</span> <br />
                {encodedUserInformation?.phoneCode}
                {encodedUserInformation?.primaryPhone}
              </p>
              <div className="text_with_timer">
                <p className="otp_text">Enter OTP</p>
                <span className="timer1" id="timer1">
                  {getMobileOTPRes_success !== null && (
                    <CountDownTimer onTimerExpire={onTimerExpire} />
                  )}
                  {getMobileOTPRes_success === null && (
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
                  <p className="resend " onClick={resendMobileOTPFunc}>
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
                    onClick={resendMobileOTPFunc}
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
          </div>
        </form>
      </section>
      <Footer />
    </>
  );
};

export default VerifyWebMobileOTP;
