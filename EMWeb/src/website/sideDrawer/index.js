import React, { useState, useEffect } from "react";
import "./index.scss";
import { Link } from "react-router-dom";
import Backdrop from "../../components/Backdrop/Backdrop";
import hamburgerIcon from "./../../assets/images/static_website/hamburger.png";

const SideDrawer = (props) => {
  const [isShowSideDrawer, setIsShowSideDrawer] = useState(false);

  useEffect(() => {
    setIsShowSideDrawer(props.show);
  }, [props.show]);

  return (
    // <Backdrop>
    <div
      className={`hambuger ${isShowSideDrawer ? "show_drawer" : "hide_drawer"}`}
    >
      <div className="section_hamburger">
        <span className="icon_inner">
          <img
            src={hamburgerIcon}
            alt="hamburger icon"
            onClick={() => setIsShowSideDrawer(!isShowSideDrawer)}
          />
        </span>
      </div>
      <div className="section_sidedrawer">
        <ul className="navigation_list">
          <li className="navigation_item">
            <Link to={`/home`} className="navigation_link">
              Home
            </Link>
          </li>
          <li className="navigation_item">
            <Link to={`/about-us`} className="navigation_link">
              About Us
            </Link>
          </li>
          <li className="navigation_item">
            <Link to={`/contact-us`} className="navigation_link">
              Contact Us
            </Link>
          </li>
          <li className="navigation_item">
            <Link to={`/faq`} className="navigation_link">
              FAQ
            </Link>
          </li>
          <li className="navigation_item">
            <Link to={`/our-solutions`} className="navigation_link">
              Our Solutions
            </Link>
          </li>
        </ul>
      </div>
    </div>
    // </Backdrop>
  );
};

export default SideDrawer;
