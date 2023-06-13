import React from "react";
import "./SuccessPayment.scss";
import successIcon from "./../../../assets/images/checked.png";
import Backdrop from "../../../components/Backdrop/Backdrop";

const RegistrationSuccessFull = (props) => {
  return (
    <Backdrop>
      <div className="section_modal">
        <div className="container_area">
          <div className="main_icon_container">
            <div className="icon_container">
              <img src={successIcon} alt="checked icon" />
            </div>
          </div>
          <h2 className="heading_reg">Registration Successful</h2>
          <p className="heading_text">
            You can earn rewards by sharing your referal code with others.
          </p>
          <p className="heading_text">
            Referal Code&nbsp;:&nbsp;
            <b>{localStorage.getItem("referalCode")}</b>
          </p>
          <div className="button_container_reg">
            <button className="button" onClick={props.redirect}>
              Finish
            </button>
          </div>
        </div>
      </div>
    </Backdrop>
  );
};

export default RegistrationSuccessFull;
