import React from "react";
import "./RegistrationSuccessful.scss";
import successIcon from "../../../../../assets/images/checked.png";
import Backdrop from "../../../../../components/Backdrop/Backdrop";
import { getReferralCode } from "../../../../../config";

const RegistrationSuccessful = (props) => {
  return (
    <Backdrop>
      <div className="section_modal">
        <div className="a_container_area">
          <div className="main_icon_container">
            <div className="icon_container">
              <img src={successIcon} alt="checked icon" />
            </div>
          </div>
          <h2 className="heading">Registration Successful</h2>
          <p className="text">
            You can earn rewards by sharing your referral code with other's
            Referral code:
            <span className="bold_referral"> {getReferralCode()} </span>
          </p>
          <div className="button_container">
            <button className="button" onClick={props.redirect}>
              Finish
            </button>
          </div>
        </div>
        <div className="close_container" onClick={props.closeModal}>
          <span className="close_icon">&times;</span>
        </div>
      </div>
    </Backdrop>
  );
};

export default RegistrationSuccessful;
