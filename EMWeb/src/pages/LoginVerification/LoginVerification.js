import React, { useState, useEffect } from "react";
import Footer from "../../components/Footer/Footer";
import Header from "../../components/Header/Header";
import "./LoginVerification.scss";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import { getAuthData } from "./../../config/index";

import blackLockIcon from "./../../assets/images/lock-ico-black.png";
import OtpInput from "react-otp-input";
import resendIcon from "./../../assets/images/refresh-icon.png";
import { ToastError } from "../../components/Tostify";
import CountDownTimer from "./../../components/CountdownTimer/CountdownTimer";
import Loading from "../../components/Loading/Loading";
import { getMobileOTPFunc } from "../../store/vendorRegistration/getMobileOtp";
import { verifyMobileOTPFunc } from "../../store/vendorRegistration/verifyMobileOtp";

const LoginVerification = (props) => {
  const dispatch = useDispatch();
  const history = useHistory();

  const [otp, setOtp] = useState("");
  const [timerOut, setTimerOut] = useState(false);

  const resendMobileOTPLoading = useSelector(
    (state) => state.mobileOTP.isLoading
  );
  const resendMobileOTPError = useSelector((state) => state.mobileOTP.isError);
  const resendMobileOTPRes = useSelector(
    (state) => state.mobileOTP.getMobileOTPRes
  );
  const verifyMobileError = useSelector(
    (state) => state.verifyMobileOTP.isError
  );
  const verifyMobileLoading = useSelector(
    (state) => state.verifyMobileOTP.isLoading
  );
  const verifyMobileRes = useSelector(
    (state) => state.verifyMobileOTP.verifyMobileRes
  );

  const handleFormSubmission = (e) => {
    e.preventDefault();
    if (otp.length !== 6) {
      ToastError(`Please enter OTP.`);
      return false;
    }
    const data = {
      otp: otp,
      phoneCode: `${getAuthData()?.phoneCode}`,
      mobile: `${getAuthData()?.primaryPhone}`,
    };
    dispatch(verifyMobileOTPFunc(data));
  };

  useEffect(() => {
    if (resendMobileOTPError !== ``) {
      ToastError(`${resendMobileOTPError}`);
    } else if (verifyMobileError !== ``) {
      ToastError(`${verifyMobileError}`);
    }
  }, [resendMobileOTPError, verifyMobileError]);

  useEffect(() => {
    if (resendMobileOTPRes) {
      setTimerOut(false);
    }
    if (verifyMobileRes !== null) {
      history.push(`/registration`);
    }
  }, [resendMobileOTPRes, verifyMobileRes, history]);

  const resendHandler = () => {
    dispatch(getMobileOTPFunc({
      mobile: `${getAuthData()?.phoneCode}${getAuthData()?.primaryPhone}`}));
    setOtp(``);
  };

  const onTimerExpire = () => {
    setTimerOut(true);
  };

  return (
    <>
      {(resendMobileOTPLoading || verifyMobileLoading) && (
        <Loading text={"Loading ...."} />
      )}
      <Header page={props.userType} userType={props.setUserType} />
      <section className="main_section">
        <div className="main_wrapper">
          <div className="login_otp_container">
            <div className="login_form_container">
              <form onSubmit={handleFormSubmission}>
                <div className="card_header">
                  <div className="lock_container">
                    <div className="lock_sub_container">
                      <img src={blackLockIcon} alt="lock-icon" />
                    </div>
                  </div>
                  <h2 className="card_heading">Verify</h2>
                </div>
                <div className="card_body">
                  <p className="body_main_text">
                    An OTP has been sent to your associated Mobile no. &nbsp;
                    {`${getAuthData()?.phoneCode}${getAuthData()?.primaryPhone
                      }`}
                  </p>
                  <div className="otp_text_container">
                    <p className="enter_otp_text">Enter OTP</p>
                    <p className="timer">
                      {!resendMobileOTPLoading && (
                        <CountDownTimer onTimerExpire={onTimerExpire} />
                      )}
                    </p>
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
                  <div className="did_not_receive_otp">
                    Didn’t receive the OTP?
                    <span
                      className={`resend_section ${!timerOut ? `disable-anchor` : ``
                        }`}
                      onClick={resendHandler}
                    >
                      Resend <img src={resendIcon} alt="resend-icon" />
                    </span>
                  </div>
                </div>
                <div className="card_footer">
                  <button type="submit" className="btn btn_submit">
                    Proceed
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </section>
      <Footer />
    </>
  );
};

export default LoginVerification;
