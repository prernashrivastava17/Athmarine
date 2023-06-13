import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import "./PrivacyPolicyAth.scss";
import Backdrop from "../../../../../components/Backdrop/Backdrop";
import PrivacyPolicyAthMerine from "../../../../TermsAndConditions/PrivacyPolicyAthMerine";

const PrivacyPolicyAth = (props) => {
  const closePopupHandler = () => {
    props.onClose();
  };

  return (
    <Backdrop>
      <div className="termsandConditons_modal">
        <div className="termsAndConditions_container">
          <p className="modal_heading"> Privacy Policy </p>
        </div>

      <PrivacyPolicyAthMerine/>
        <div className="button_container">
                    <a className="btn btn_cancel large_btn" onClick={props.cancel}>Disagree</a>
                    <a className="btn btn_ok large_btn" onClick={props.ok} >Agree</a>
                </div>
      </div>
     
      <div className="close_container" onClick={props.cancel}>
        <span className="close_icon">&times;</span>
      </div>
    </Backdrop>
  );
};

export default PrivacyPolicyAth;
