import React from "react";
import "./SuccessPayment.scss";
import successIcon from "./../../../assets/images/checked.png";
import Backdrop from "../../../components/Backdrop/Backdrop";

const SuccessPayment = (props) => {
  return (
    <Backdrop>
      <div className="section_modal">
        <div className="container_area">
          <div className="main_icon_container">
            <div className="icon_container">
              <img src={successIcon} alt="checked icon" />
            </div>
          </div>
          <h2 className="heading">Payment Successful</h2>
          <div className="button_container">
            <button className="button" onClick={props.redirect}>
              Proceed
            </button>
          </div>
        </div>
      </div>
    </Backdrop>
  );
};

export default SuccessPayment;
