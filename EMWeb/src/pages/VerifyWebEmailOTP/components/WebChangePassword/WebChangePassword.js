import React, { useState, useEffect } from "react";
import "./WebChangePassword.scss";

import lock_icon from "./../../../../assets/images/lock-icon.png";
import { useHistory } from "react-router-dom";
import Header from "./../Header/Header";
import Loading from "../../../../components/Loading/Loading";
import { passwordValidation } from "../../../../utils/helpers";
import { ToastError } from "../../../../components/Tostify";
import eyeIconOn from "./../../../../assets/images/Icon-ionic-md-eye.png";
import eyeIconOff from "./../../../../assets/images/Icon-md-eye-off.png";

import { useDispatch, useSelector } from "react-redux";
import {
  updatePasswordActions,
  updatePasswordFunc,
} from "../../../../store/ResetPassword/updatePassword";
import Footer from "../../../../components/Footer/Footer";
import WebVerificationSuccess from "../Modal/WebVerificationSuccess";

const WebChangePassword = (props) => {
  const history = useHistory();
  const dispatch = useDispatch();
  const encodedUserInformation = useSelector(
    (state) => state.encodedUserReducer.userInformationRes
  );
  const isLoadingUpdatePasswordRes = useSelector(
    (state) => state.updatePasswordReducers.isLoading
  );
  const updatedPasswordRes = useSelector(
    (state) => state.updatePasswordReducers.updatePasswordRes
  );
  const IserrorUpdatedPasswordRes = useSelector(
    (state) => state.updatePasswordReducers.isError
  );

  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errors, setErrors] = useState(false);
  const [passwordView, setPasswordView] = useState(eyeIconOff);
  const [showPassword, setShowPassword] = useState(false);
  const [inputType, setInputType] = useState("password");
  const [reEnterPasswordView, setReEnterPasswordView] = useState(eyeIconOff);
  const [showReEnterPassword, setShowReEnterPassword] = useState(false);
  const [reEnterInputType, setReEnterInputType] = useState("password");
  const [showCompletedPopup, setShowCompletedPopup] = useState(false);

  const validateForm = () => {
    if (password && confirmPassword && passwordValidation(password)) {
      if (password === confirmPassword) {
        setErrors(false);
        return true;
      } else {
        setErrors(true);
        return false;
      }
    } else {
      setErrors(true);
      return false;
    }
  };

  const handleFormSubmission = (e) => {
    e.preventDefault();
    if (validateForm()) {
      dispatch(
        updatePasswordFunc({
          email: encodedUserInformation?.email,
          newPassword: password,
        })
      );
    }
  };

  useEffect(() => {
    if (updatedPasswordRes !== null) {
      console.log("password set successfully");
      setShowCompletedPopup(true);

      setTimeout(() => {
        history.push(`/login`);
      }, [5000]);
    }
  }, [updatedPasswordRes]);

  useEffect(() => {
    if (IserrorUpdatedPasswordRes !== "") {
      ToastError(IserrorUpdatedPasswordRes);
    }
  }, [IserrorUpdatedPasswordRes]);

  const changePasswordView = () => {
    if (showPassword === false) {
      setPasswordView(eyeIconOn);
      setShowPassword(true);
      setInputType("text");
    } else {
      setPasswordView(eyeIconOff);
      setShowPassword(false);
      setInputType("password");
    }
  };

  const changeReEnterPasswordView = () => {
    if (showReEnterPassword === false) {
      setReEnterPasswordView(eyeIconOn);
      setShowReEnterPassword(true);
      setReEnterInputType("text");
    } else {
      setReEnterPasswordView(eyeIconOff);
      setShowReEnterPassword(false);
      setReEnterInputType("password");
    }
  };

  return (
    <>
      {isLoadingUpdatePasswordRes && <Loading text="Loading..." />}
      {showCompletedPopup && <WebVerificationSuccess />}

      <Header />
      <section className="my_change_password_container">
        <div className="login_wrapper">
          <form onSubmit={handleFormSubmission}>
            <div className="card_body">
              <div className="lock_container">
                <img src={lock_icon} alt="lock-icon" className="lock_icon" />
                <p className="modal_heading">Create your own secure password</p>
              </div>

              {/* <div className="text_info">
                <p className="text">
                  Your new password must be different from the previous
                  password.
                </p>
              </div> */}

              <div className="form_container">
                <div className="form_control_area">
                  <label className="form_label">
                    Password
                    <span className="label_mandatory">*</span>
                  </label>
                  <input
                    type={inputType}
                    className="form_control"
                    name="Password"
                    onChange={(e) => setPassword(e.target.value)}
                  />
                  <div className="eye_icon">
                    <img
                      src={passwordView}
                      onClick={changePasswordView}
                      alt="eye-icon"
                    />
                  </div>
                  {errors &&
                    (password === "" || !passwordValidation(password)) && (
                      <span className="error">
                        {`Password must contain at least eight characters, at least one number and both lower and uppercase letters and special characters (!"#$%&'()*+,-./:;<=>?@[\]^_{|}~).`}
                      </span>
                    )}
                </div>
                <div className="form_control_area">
                  <label className="form_label">
                    Confirm Password
                    <span className="label_mandatory">*</span>
                  </label>
                  <input
                    type={reEnterInputType}
                    className="form_control"
                    name="confirmPassword"
                    onChange={(e) => setConfirmPassword(e.target.value)}
                  />
                  <div className="eye_icon">
                    <img
                      src={reEnterPasswordView}
                      onClick={changeReEnterPasswordView}
                      alt="eye-icon"
                    />
                  </div>
                  {errors &&
                    passwordValidation(password) &&
                    (confirmPassword === "" ||
                      !passwordValidation(confirmPassword) ||
                      confirmPassword !== password) && (
                      <span className="error">
                        Re-enter password should be same as create password
                      </span>
                    )}
                </div>
              </div>

              <div className="submit_container">
                <input type="submit" value="Verify" className="a_submit" />
              </div>
            </div>
          </form>
        </div>
      </section>
      <Footer />
    </>
  );
};

export default WebChangePassword;
