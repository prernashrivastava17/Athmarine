import React, { useEffect, useState } from "react";
import { Link, useHistory } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";

import "./Header.scss";
import logo from "./../../assets/images/logo.png";
import { authActions } from "../../store/auth/auth";
import Maintenance from "../../pages/Registration/components/Modals/maintenance/maintenance";
import Loading from "../../components/Loading/Loading";
import ProceedModal from "../../pages/Registration/components/Modals/ProceedModal/ProceedModal";

const Header = (props) => {
  const history = useHistory();
  const dispatch = useDispatch();

  const setUserType = (type) => {
    props.userType(type);
  };

  useEffect(() => {
    if (props.screenNo > 1) {
      history.push("/registration");
      setUserType("partner");
    }
  }, [props.screenNo]);

  useEffect(() => {
    if (props.pageName === "verifyEmail") {
      setLogoutBtn(true);
    }
  }, [props.pageName]);

  let isPageLogin = props.page === "login" ? true : false;
  let isPagePartner = props.page === "partner" ? true : false;
  let isPageCustomer = props.page === "customer" ? true : false;
  let isPagePartnersIndividual =
    props.page === "partners-individual" ? true : false;

  const [isToggled, setIsToggled] = useState(false);
  const [toggleDropdown, setToggleDrodpwon] = useState(false);
  const [isMaintenance, setIsMaintenance] = useState(false);
  const [logoutBtn, setLogoutBtn] = useState(false);
  const [logoutModal, setLogoutModal] = useState(false);
  const [loader, setLoader] = useState(false);

  const toggleDropdownHandler = () => {
    setToggleDrodpwon(!toggleDropdown);
  };

  const closeModal = () => {
    setIsMaintenance(false);
  };

  const toggle = React.useCallback(
    () => setIsToggled(!isToggled),
    [isToggled, setIsToggled]
  );

  const returnToLoginHandler = () => {
    setUserType("");
    dispatch(authActions.logoutAction());
  };

  const returnToHomePage = () => {
    setLogoutModal(false);
    setLoader(true);
    dispatch(authActions.logoutAction());
    window.location.href = "/";
    setTimeout(() => {
      setLoader(false);
    }, 1000);
  };

  return (
    <header>
      {isMaintenance && <Maintenance closeModal={closeModal} />}
      <div className="header_container">
        <div className="header_left_part">
          <div className="logo_container" onClick={() => setUserType("")}>
            <Link to="/">
              <img src={logo} alt="logo" />
            </Link>
          </div>
          <div className="navigation_container">
            <ul className="nav_items">
              <li className="nav_item">
                <a href="/#" className="nav_link">
                  Home
                </a>
              </li>
              <li className="nav_item">
                <a href="/#" className="nav_link">
                  Product
                </a>
              </li>
              <li className="nav_item">
                <a href="/#" className="nav_link">
                  About
                </a>
              </li>
              <li className="nav_item">
                <a href="/#" className="nav_link">
                  Contact
                </a>
              </li>
            </ul>
          </div>
        </div>

        <div className="header_right_part">
          {isPageLogin && (
            <>
              <div
                className="dropdown_container desktop_view"
                onClick={toggleDropdownHandler}
              >
                <nav>
                  <ul className="menu">
                    <li className="dropdown">
                      Register as a Partner
                      <ul
                        className={`dropdown_menu ${
                          toggleDropdown === true
                            ? "animated_dropdown_show"
                            : "animated_dropdown_hide"
                        }`}
                      >
                        <Link
                          to="/registration"
                          onClick={() => setUserType("partners-individual")}
                        >
                          <li className="dropdown_item">
                            Register as an Individual
                          </li>
                        </Link>
                        <Link
                          to="/registration"
                          onClick={() => setUserType("partner")}
                        >
                          <li className="dropdown_item">
                            Register as a Team/Company
                          </li>
                        </Link>
                      </ul>
                    </li>
                  </ul>
                </nav>
              </div>

              <button
                className="btn black desktop_view"
                // onClick={() => setUserType("customer")}
                onClick={() => setIsMaintenance(true)}
              >
                <Link
                // to="/registration"
                >
                  Register as a Customer{" "}
                </Link>
              </button>
            </>
          )}

          {isPageLogin && (
            <>
              <div
                className="dropdown_container mobile_view"
                onClick={toggleDropdownHandler}
              >
                <nav>
                  <ul className="menu">
                    <li className="dropdown">
                      Register
                      <ul
                        className={`dropdown_menu ${
                          toggleDropdown === true
                            ? "animated_dropdown_show"
                            : "animated_dropdown_hide"
                        }`}
                      >
                        <li className="dropdown_item">
                          <Link
                            to="/registration"
                            onClick={() => setUserType("partners-individual")}
                          >
                            Register as an Individual
                          </Link>
                        </li>
                        <li className="dropdown_item">
                          <Link
                            to="/registration"
                            onClick={() => setUserType("partner")}
                          >
                            Register as a Team/Company
                          </Link>
                        </li>
                        <li
                          className="dropdown_item"
                          onClick={() => setIsMaintenance(true)}
                        >
                          <Link
                          // to="/registration"
                          // onClick={() => setUserType("customer")}
                          >
                            Register as a Customer
                          </Link>
                        </li>
                      </ul>
                    </li>
                  </ul>
                </nav>
              </div>
            </>
          )}

          {(isPagePartner || isPagePartnersIndividual) &&
            (logoutBtn ? (
              <button
                className="btn blackBtn"
                onClick={() => setLogoutModal(true)}
              >
                Logout
              </button>
            ) : (
              <button className="btn black" onClick={returnToLoginHandler}>
                <Link to="/login"> Login </Link>
              </button>
            ))}

          {isPageCustomer && (
            <>
              <button className="btn white" onClick={() => setUserType("")}>
                <Link to="/"> Continue As Guest </Link>
              </button>
              <button className="btn black" onClick={() => setUserType("")}>
                <Link to="/login"> Login </Link>
              </button>
            </>
          )}
        </div>
      </div>
      {logoutModal && (
        <ProceedModal
          ok={returnToHomePage}
          cancel={() => setLogoutModal(false)}
          message="Are you sure you want to logout?"
          btn1="Cancel"
          btn2="Ok"
        />
      )}
      {loader && <Loading text="Loading..." />}
    </header>
  );
};

export default Header;
