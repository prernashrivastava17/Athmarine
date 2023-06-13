import React from "react";
import "./WebVerificationSuccess.scss";
import Backdrop from "../../../../components/Backdrop/Backdrop";
import right from "../../../../assets/images/right.png";
import { useHistory } from "react-router-dom";

const WebVerificationSuccess = (props) => {
  const history = useHistory();
  return (
    <Backdrop>
      <div className="modal_verification_success">
        <img src={right} alt="lock-icon" className="img" />
        <h3>Your account has been successfully setup.</h3>
        <p>Please log-in using your credentials.</p>
        <div className="submit_container">
          <button type="submit" onClick={() => history.push(`/login`)}>
            OK
          </button>
        </div>
      </div>
    </Backdrop>
  );
};

export default WebVerificationSuccess;
