import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import "./TermsAndConditions.scss";
import Backdrop from "../../../../../components/Backdrop/Backdrop";
import TermsAndConditionss from "../../../../TermsAndConditions/TermsAndConditions";
import DisagreeTermAndCondition from "../DisagreeTermAndCondition/disagreeTermAndCondition";

const TermsAndConditions = (props) => {
  const [isConfirmationModal, setIsConfirmationModal] = useState(false);

  const closePopupHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
  };

  const onOkHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
  }

  return (
    <>
      {isConfirmationModal && (
        <DisagreeTermAndCondition ok={onOkHandler} />
      )}
      <Backdrop>
        <div className="termsandConditons_modal">
          <div className="termsAndConditions_container">
            <p className="modal_heading"> Terms of Service </p>
          </div>

          <TermsAndConditionss />
          <div className="button_container">
            <div className="btn btn_cancel large_btn" onClick={closePopupHandler}>Disagree</div>
            <div className="btn btn_ok large_btn" onClick={props.ok} >Agree</div>
          </div>
        </div>

      </Backdrop>
    </>
  );
};

export default TermsAndConditions;
