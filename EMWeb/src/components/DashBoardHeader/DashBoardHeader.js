import React, { useState } from "react";
import { Link } from "react-router-dom";
import { useDispatch } from "react-redux";
import { useHistory } from "react-router-dom";

import "./DashBoardHeader.scss";
import logo from "./../../assets/images/logo.png";
import caretImg from "../../assets/images/Icon awesome-chevron-down.svg";
import notificationBellImg from "../../assets/images/Icon ionic-ios-notifications.svg";
import settingsIcon from "../../assets/images/Icon material-settings.svg";
import logoutImg from "../../assets/images/Icon feather-log-out.svg";
import { authActions } from "../../store/auth/auth";

const DashBoardHeader = () => {
  const history = useHistory();
  const dispatch = useDispatch();

  const [isToggled, setIsToggled] = React.useState(false);

  const toggle = React.useCallback(
    () => setIsToggled(!isToggled),
    [isToggled, setIsToggled]
  );

  const handleLogout = () => {
    localStorage.clear();
    dispatch(authActions.logoutAction());
    history.push("/");
  };

  return (
    <header>
      <div className="header_container">
        <div className="header_left_part">
          <div className="logo_container">
            <Link to="/">
              <img src={logo} alt="logo" />
            </Link>
          </div>
          <div className="navigation_container">
            {/* navigation list part  */}
          </div>
        </div>
        <div className="dashboard_header_right_part">
          <div className="header_right_container">
            <div></div>
            <div className="center">
              <div className="bell_container">
                <img src={notificationBellImg} alt="" />
              </div>
              <img
                src="https://images.unsplash.com/photo-1606787947360-4181fe0ab58c?ixid=MnwxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80"
                className="user"
                onClick={() => history.push("/profile")}
              />
              <div></div>

              <div className="dropdown">
                <label
                  style={{
                    fontSize: "20px",
                    position: "relative",
                    cursor: "pointer",
                  }}
                  onClick={toggle}
                >
                  <img src={caretImg} alt="" style={{ padding: "0 5px" }} />
                </label>
                {/* <button onclick="myFunction()" className="dropbtn">Dropdown</button> */}
                <div>
                  {isToggled && (
                    <div id="myDropdown" className="dropdown-content">
                      <div>
                        <a href="javascript:void(0)" role="button">
                          <img
                            src={settingsIcon}
                            alt=""
                            style={{ padding: "0px 10px" }}
                          />
                          Profile settings
                        </a>
                      </div>
                      <div>
                        <a
                          href="javascript:void(0)"
                          role="button"
                          onClick={handleLogout}
                        >
                          <img
                            src={logoutImg}
                            alt=""
                            style={{ padding: "0px 10px" }}
                          />
                          Log out
                        </a>
                      </div>
                    </div>
                  )}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
};

export default DashBoardHeader;
