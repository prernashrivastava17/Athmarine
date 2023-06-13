import React, { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { useHistory } from "react-router-dom";

import "./Login.scss";
import userIcon from "./../../assets/images/filled_user_icon.png";
import ResetPassword from "./components/Modals/ResetPassword/ResetPassword";
import Loading from "./../../components/Loading/Loading";
import { userAuthentication } from "../../store/auth/auth";
import { commonGetApiActions } from "../../store/common/commonGetApi";
import eyeIconOn from "./../../assets/images/Icon-ionic-md-eye.png";
import eyeIconOff from "./../../assets/images/Icon-md-eye-off.png";
import vendor from "./../../assets/images/vendor.svg";
import customer from "./../../assets/images/customer.svg";
import { emailValidation } from "../../utils/helpers";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";
import { saveAddressActions } from "../../store/vendorRegistration/address";
import Maintenance from "../Registration/components/Modals/maintenance/maintenance";

const Login = (props) => {
  const dispatch = useDispatch();
  const history = useHistory();

  const isLoading = useSelector((state) => state.auth.isLoading);
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
  const authResponse = useSelector((state) => state.auth.authRes);

  const [resetPassword, setResetPassword] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState(false);
  const [isLogin, setIsLogin] = useState("");
  const [beforeSelect, setBeforeSelect] = useState(true);
  const [afterSelect, setAfterSelect] = useState(false);
  const [isConfirmationModal, setIsConfirmationModal] = useState(false);
  const [isMaintenance, setIsMaintenance] = useState(false);

  const onCancelHandler = () => {
    setIsConfirmationModal(!isConfirmationModal);
  };

  const validateForm = () => {
    if (username && password && emailValidation(username)) {
      setErrors(false);
      return true;
    } else {
      setErrors(true);
      return false;
    }
  };

  useEffect(() => {
    if (isAuthenticated) {
      if (authResponse?.registeredSuccessfully === false) {
        dispatch(
          commonGetApiActions.updateRegistrationKey(
            authResponse?.registrationStatusKey
          )
        );
        dispatch(saveAddressActions.addressNavigation(false));
        dispatch(commonGetApiActions.setAPIResponseToBlank());
        history.push("/verify");
      } else {
        dispatch(
          commonGetApiActions.updateRegistrationKey(
            authResponse?.registrationStatusKey
          )
        );
        // history.push("/dashboard");
        setIsMaintenance(true);
      }
    }
  }, [history, isAuthenticated]);

  useEffect(() => {
    if (authResponse != null) {
      if (authResponse?.type === null) {
        props.setUserType(`partner`);
      } else if (authResponse?.type === `Individual`) {
        props.setUserType(`partners-individual`);
      } else if (authResponse?.type === `Customer`) {
        props.setUserType(`customer`);
      }
    }
  }, [authResponse]);

  const submitFormHandler = (e) => {
    e.preventDefault();
    if (validateForm()) {
      if (username && password) {
        dispatch(userAuthentication({ username, password }));
      }
    }
  };

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

  const changeResetPasswordHandler = () => {
    setResetPassword(!resetPassword);
  };
  const closeModal = () => {
    setIsMaintenance(false);
    window.location.href = "/login";
  };

  const changeStateCustomer = () => {
    setAfterSelect(true);
    setBeforeSelect(false);
    setIsLogin("Customer");
  };
  const changeStateVendor = () => {
    setAfterSelect(true);
    setBeforeSelect(false);
    setIsLogin("Vendor");
  };

  return (
    <>
      {isLoading && <Loading text={"Loading..."} />}
      {resetPassword && <ResetPassword onClose={changeResetPasswordHandler} />}
      {isMaintenance && <Maintenance closeModal={closeModal} />}

      <Header page={`login`} userType={props.setUserType} />
      <section className="login_container">
        <div className="login_wrapper">
          <div className="card_header">
            <div className="display_user_icon_container">
              <img
                src={userIcon}
                className="display_user_icon"
                alt="user-icon"
              />
            </div>
            {beforeSelect && (
              <div className="login_info">Login to your account</div>
            )}
            {afterSelect && (
              <div className="login_info">Login as {isLogin}</div>
            )}
          </div>

          {beforeSelect && (
            <div className="card_body_suggest">
              <div className="both_same" onClick={changeStateCustomer}>
                <img src={customer} alt="customer icon" />
                <div style={{ width: "92px", textAlign: "left" }}>Customer</div>
              </div>

              <div className="both_same" onClick={changeStateVendor}>
                <img src={vendor} alt="customer icon" />
                <div style={{ width: "92px", textAlign: "left" }}>Partner</div>
              </div>
            </div>
          )}

          {afterSelect && (
            <div className="card_body">
              <form onSubmit={submitFormHandler}>
                <div className="form_control_fluid">
                  <label className="form_label">
                    Enter Username <span className="label_mandatory">*</span>
                  </label>
                  <input
                    type="text"
                    value={username}
                    className="form_control"
                    onChange={(e) => setUsername(e.target.value.trim())}
                  />
                  {errors &&
                    username.trim() === "" &&
                    !emailValidation(username.trim()) && (
                      <span className="error">Please enter an email</span>
                    )}
                  {errors &&
                    username.trim() !== "" &&
                    !emailValidation(username.trim()) && (
                      <span className="error">Invalid email</span>
                    )}
                </div>
                <div className="form_control_fluid">
                  <label className="form_label">
                    Password <span className="label_mandatory">*</span>
                  </label>
                  <input
                    type={inputType}
                    value={password}
                    className="form_control"
                    onChange={(e) => setPassword(e.target.value)}
                  />
                  <div className="eye_icon_login">
                    <img
                      src={passwordView}
                      onClick={changePasswordView}
                      alt="eye-icon"
                    />
                  </div>
                  {errors && password === "" && (
                    <span className="error">Please enter password</span>
                  )}
                </div>
                <div className="forget_container">
                  <p
                    className="forget_text"
                    onClick={changeResetPasswordHandler}
                  >
                    Forgot Password?
                  </p>
                </div>
                <div className="submit_form_container">
                  <input type="submit" value="Login" />
                </div>
              </form>
            </div>
          )}
        </div>
      </section>
      <Footer />
    </>
  );
};

export default Login;
