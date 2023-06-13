import React from "react";

import "./VerifyOTP.scss";
import Backdrop from "./../../../../../components/Backdrop/Backdrop";
import lockIcon from "./../../../../../assets/images/lock-ico-black.png";

const VerifyOTP = () => {
  return (
    <Backdrop>
      <div className="modal">
        <div className="lock_container">
          <img src={lockIcon} alt="lock-icon" className="lock_icon" />
          <p className="modal_heading"> Verify </p>
        </div>

        <div className="text_info">
          <p className="text">
            An OTP has been sent to your associated Email account &amp; Mobile
            no.
          </p>
        </div>

        <div className="otp_container">
          <div className="otp_main_container">
            <span className="text">Enter OTP</span>
            <span className="timing">01:35</span>
          </div>
          <div className="enter_otp_container">
            <input type="text" />
            <input type="text" />
            <input type="text" />
            <input type="text" />
            <input type="text" />
            <input type="text" />
          </div>
          <div className="did_not_receive_otp_container">
            <p className="text">Didn’t receive the OTP?</p>
            <p className="resend_btn"> Resend </p>
          </div>
        </div>

        <div className="submit_container">
          <button type="submit"> Proceed </button>
        </div>

        <div className="close_container">
          <span className="close_icon">&times;</span>
        </div>
      </div>
    </Backdrop>
  );
};

export default VerifyOTP;
