import React from "react";
import "./SuccessPayment.scss";
import successIcon from "./../../../assets/images/checked.png";
import Backdrop from "../../../components/Backdrop/Backdrop";
import { useHistory } from "react-router-dom";

const RegistrationSuccessByInvoice = (props) => {
  const history = useHistory();

  const handleModal = () => {
    props.onConfirm();
    window.location.href = "/";
    localStorage.clear();
    sessionStorage.clear();
  };

  return (
    <Backdrop>
      <div className="section_modal">
        <div className="container_area">
          <div className="main_icon_container">
            <div className="icon_container">
              <img src={successIcon} alt="checked icon" />
            </div>
          </div>
          <h2 className="heading_reg">
            Your registration is successful please pay the due amount to start
            bidding.
          </h2>
          <div className="button_container_reg">
            <button className="button" onClick={handleModal}>
              Proceed
            </button>
          </div>
          <p className="suggestion_text">
            On clicking proceed button you will be redirected to home page
          </p>
        </div>
        <div className="close_container_charges" onClick={props.onConfirm}>
          <span className="close_icon">&times;</span>
        </div>
      </div>
    </Backdrop>
  );
};

export default RegistrationSuccessByInvoice;
