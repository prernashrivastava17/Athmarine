import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import "./VerifyEmailModal.scss";
import Backdrop from "../../../../../components/Backdrop/Backdrop";
import lockIcon from "./../../../../../assets/images/lock-ico-black.png";
import {
  updateUserEmailActions,
  updateUserEmail_getOTPFunc,
} from "../../../../../store/vendorRegistration/updateEmail";
import { updateEmail } from "../../../../../store/vendorRegistration/saveDetailsForm";
import { emailValidation } from "../../../../../utils/helpers";
import { getCompanyId, getUserId } from "../../../../../config";

const VerifyEmailModal = (props) => {
  const dispatch = useDispatch();
  const updateEmailResLoading = useSelector(
    (state) => state.updateEmailAndgetOTPReducer.isLoading
  );
  const updatedEmailRes = useSelector(
    (state) => state.updateEmailAndgetOTPReducer.updatedEmailRes
  );
  const updatedEmailError = useSelector(
    (state) => state.updateEmailAndgetOTPReducer.isError
  );
  const DetailsFormEmail = useSelector(
    (state) => state.saveDetailsForm.DetailsForm
  );

  const [email, setEmail] = useState("");
  const [error, setError] = useState(false);

  const handleFormSubmission = (e) => {
    e.preventDefault();
    if (email !== "" && emailValidation(email)) {
      let data = {
        email,
        userId: getUserId() === null ? getCompanyId() : getUserId(),
      };
      // dispatch(updateEmail(email));
      dispatch(updateUserEmail_getOTPFunc(data));
    } else {
      setError(true);
    }
  };

  useEffect(() => {
    if (email !== "") {
      if (updatedEmailRes === "Success") {
        dispatch(updateEmail(email));
      }
    }
  }, [updatedEmailRes]);

  const closePopupHandler = () => {
    props.onClose();
  };

  return (
    <div className="verify_email_backdrop">
      <div className="modal_reset_password">
        <div className="lock_container">
          <img src={lockIcon} alt="lock-icon" className="lock_icon" />
          <p className="modal_heading"> Change Email </p>
        </div>

        <div className="text_info">
          <p className="text left_text">
            Please enter new email address, You will receive an OTP on your new
            email id for the verification.
          </p>
        </div>

        <div className="form_container">
          <form onSubmit={handleFormSubmission}>
            <div className="form_control_area left_text">
              <label className="form_label">
                Email Address
                <span className="label_mandatory">*</span>
              </label>
              <input
                type="text"
                className="form_control"
                name="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
              {error && (email === "" || !emailValidation(email)) && (
                <span className="error">please Enter Valid email </span>
              )}
            </div>

            <div className="submit_container">
              <button type="submit"> Send OTP </button>
            </div>
          </form>
        </div>

        <div className="close_container_email" onClick={closePopupHandler}>
          <span className="close_icon">&times;</span>
        </div>
      </div>
    </div>
  );
};

export default VerifyEmailModal;
