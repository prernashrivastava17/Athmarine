import React, { useState } from "react";
import { ToastError } from "../../../components/Tostify";
import { emailValidation, passwordValidation } from "../../../utils/helpers";
import eyeIconOn from "./../../../assets/images/eye_show_1.png";
import eyeIconOff from "./../../../assets/images/Group 2110.png";
import userIcon from "./../../../assets/images/Profile.png";
import "./SignIn.scss";

const SignIn = ({ details }) => {
  const [passwordView, setPasswordView] = useState(eyeIconOff);
  const [showPassword, setShowPassword] = useState(false);
  const [inputType, setInputType] = useState("password");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const definedEmail = "rahul@athmarine.com";
  const definedPassword = "Rahul@123";

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

  const submitFormHandler = (e) => {
    e.preventDefault();

    if (!emailValidation(username)) {
      ToastError(`Enter proper email.`);
    } else if (!passwordValidation(password)) {
      ToastError(`Enter correct password.`);
    } else if (username === definedEmail && password === definedPassword) {
      details({ signIn: false, promotion: true });
    }
  };

  return (
    <>
      <section className="signin_container">
        <div className="login_wrapper">
          <img src={userIcon} className="display_user_icon" alt="user-icon" />
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
              </div>
              <div className="submit_form_container">
                <input type="submit" value="Log In" />
              </div>
            </form>
          </div>
        </div>
      </section>
    </>
  );
};

export default SignIn;
