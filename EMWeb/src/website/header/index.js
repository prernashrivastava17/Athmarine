import React, { useState } from "react";
import "./index.scss";

import { Link } from "react-router-dom";
import logo from "./../../assets/images/logo.png";
import hamburgerIcon from "./../../assets/images/static_website/hamburger.png";
import SideDrawer from "../sideDrawer";
const Header = () => {
  // const history = useHistory();

  const [isShowSideDrawer, setIsShowSideDrawer] = useState(false);

  return (
    <header>
      <SideDrawer show={isShowSideDrawer} />
      <div className="header_container_website">
        <div className="header_left_part">
          <div className="section_hamburger">
            <span className="icon_inner">
              <img
                src={hamburgerIcon}
                alt="hamburger icon"
                onClick={() => setIsShowSideDrawer(!isShowSideDrawer)}
              />
            </span>
          </div>
          <div className="logo_container">
            <Link to="/">
              <img src={logo} alt="logo" />
            </Link>
          </div>
          <div className="navigation_container">
            <ul className="nav_items">
              <li className="nav_item">
                <Link
                  to="/"
                  className={`nav_link ${
                    window.location.href.split("/").pop() === `` ? `active` : ``
                  }`}
                >
                  Home
                </Link>
              </li>
              <li className="nav_item">
                <Link
                  to="/about-us"
                  className={`nav_link ${
                    window.location.href.split("/").pop() === `about-us`
                      ? `active`
                      : ``
                  }`}
                >
                  About Us
                </Link>
              </li>
              <li className="nav_item">
                <Link
                  to="/our-solutions"
                  className={`nav_link ${
                    window.location.href.split("/").pop() === `our-solutions`
                      ? `active`
                      : ``
                  }`}
                >
                  Our Solutions
                </Link>
              </li>
              <li className="nav_item">
                <Link
                  to="/faq"
                  className={`nav_link ${
                    window.location.href.split("/").pop() === `faq`
                      ? `active`
                      : ``
                  }`}
                >
                  FAQs
                </Link>
              </li>
              <li className="nav_item ">
                <Link
                  to="/contact-us"
                  className={`nav_link ${
                    window.location.href.split("/").pop() === `contact-us`
                      ? `active`
                      : ``
                  }`}
                >
                  Contact Us
                </Link>
              </li>
            </ul>
          </div>
        </div>

        <div className="header_right_part">
          <button className="btn black">
            <Link to="/login"> Signup / Login </Link>
          </button>
        </div>

      </div>
    </header>
  );
};

export default Header;
