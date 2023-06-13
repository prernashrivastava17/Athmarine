import React, { useState, useEffect } from "react";
import { useHistory, Redirect } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import Loading from "../../components/Loading/Loading";
import { ToastError } from "../../components/Tostify";
import eyeIconOn from "./../../assets/images/Icon-ionic-md-eye.png";
import eyeIconOff from "./../../assets/images/Icon-md-eye-off.png";
import SuccessfullyChangePassword from "./Components/Modals/SuccessfullyChangePassword/SuccessfullyChangePassword";

import {
  updatePasswordFunc,
  updatePasswordActions,
} from "../../store/ResetPassword/updatePassword";
import "./ChangePassword.scss";
import lockIcon from "./../../assets/images/lock-ico-black.png";
import { passwordValidation } from "../../utils/helpers";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const ChangePassword = (props) => {
  const history = useHistory();
  const dispatch = useDispatch();

  const email = useSelector((state) => state.resetPasswordReducer.resetEmail);
  const isLoadingUpdatePasswordRes = useSelector(
    (state) => state.updatePasswordReducers.isLoading
  );
  const updatedPasswordRes = useSelector(
    (state) => state.updatePasswordReducers.updatePasswordRes
  );
  const IserrorUpdatedPasswordRes = useSelector(
    (state) => state.updatePasswordReducers.isError
  );

  const [isPasswordChangeSuccessfully, setIsPasswordChangeSuccessfully] =
    useState(false);
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errors, setErrors] = useState(false);

  // useEffect(() => {
  //   if (updatedPasswordRes) {
  //     history.push("/");
  //     dispatch(updatePasswordActions.emptyError());
  //   }
  // });

  useEffect(() => {
    if (updatedPasswordRes) {
      setIsPasswordChangeSuccessfully(true);
    }
  }, [updatedPasswordRes]);

  useEffect(() => {
    if (IserrorUpdatedPasswordRes !== "") {
      ToastError(IserrorUpdatedPasswordRes);
    }
  }, [IserrorUpdatedPasswordRes]);

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
      dispatch(updatePasswordFunc({ email, newPassword: password }));
    }

    // history.push('/');
  };

  //  use this condition at last to redirect if email is null or falsy
  // KEEP THIS IN LAST BEFORE JSX
  if (!email) {
    // return <Redirect to="/" />;
  }

  const [passwordView, setPasswordView] = useState(eyeIconOff);
  const [showPassword, setShowPassword] = useState(false);
  const [inputType, setInputType] = useState("password");
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

  const [reEnterPasswordView, setReEnterPasswordView] = useState(eyeIconOff);
  const [showReEnterPassword, setShowReEnterPassword] = useState(false);
  const [reEnterInputType, setReEnterInputType] = useState("password");
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

  const redirectToHompageHandler = () => {
    history.push("/");
  };

  return (
    <>
      {isLoadingUpdatePasswordRes && <Loading text="Loading..." />}
      {isPasswordChangeSuccessfully && (
        <SuccessfullyChangePassword onClose={redirectToHompageHandler} />
      )}

      <Header page={`login`} userType={props.setUserType} />
      <section className="login_container">
        <div className="a_change_password">
          <div className="lock_container">
            <img src={lockIcon} alt="lock-icon" className="lock_icon" />
            <p className="modal_heading"> Change Password </p>
          </div>

          <div className="text_info">
            <p className="text">
              Your new password must be different from the previous password.
            </p>
          </div>

          <div className="form_container">
            <form onSubmit={handleFormSubmission}>
              <div className="form_control_area">
                <label className="form_label">
                  New Password &nbsp;
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
                  Confirm Password &nbsp;
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

              <div className="submit_container">
                <button type="submit"> Reset </button>
              </div>
            </form>
          </div>
        </div>
      </section>
      <Footer />
    </>
  );
};

export default ChangePassword;
