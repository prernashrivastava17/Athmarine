import React from "react";
import { useHistory } from "react-router-dom";

import "./sidebar.scss";
import dashboardImg from "../../../../assets/images/dashboard.svg";
import mailImg from "../../../../assets/images/mail.svg";
import bidsImg from "../../../../assets/images/tick.svg";
import invoiceImg from "../../../../assets/images/Icon payment-invoice-sign-alt-o.svg";
import policyImg from "../../../../assets/images/policy.svg";
import helpSupportImg from "../../../../assets/images/Icon ionic-md-help-circle-outline.svg";
import caretWhiteImg from "../../../../assets/images/Icon ionic-ios-arrow-down.svg";

function Sidebar(props) {
  const { sidebarHandler } = props;
  const history = useHistory();

  const toggleBidsMenu = () => {
    var element = document.getElementById("bidsId");
    element.classList.toggle("sidebar_sublist_menu");
  };

  return (
    <div className="sidebar_container">
      <nav className="sidebar">
        <a href="#/" role="button" onClick={() => sidebarHandler("Dashboard")}>
          <img
            src={dashboardImg}
            alt="dashboard"
            className="sidebar_image_icon"
          />
          Dashboard
        </a>
        <a href="#/" onClick={() => sidebarHandler("LatestRequest")}>
          <img src={mailImg} alt="mail" className="sidebar_image_icon" />
          Requests
        </a>
        {/*  onClick={toggleBidsMenu} use tis to toggle right now just commenting to land bids component */}
        {/* onClick={()=>sidebarHandler("Bids")} use this to render bids screen  land bids component */}
        <a href="#clients" onClick={toggleBidsMenu}>
          <img src={bidsImg} alt="bids img" className="sidebar_image_icon" />
          Bids
          <img
            src={caretWhiteImg}
            alt="cartArrow"
            className="sidebar_image_icon_caret"
          />
          <ul className="sidebar_sublist_menu" id="bidsId">
            <li>
              <a href="#/" onClick={() => sidebarHandler("CompletedJobs")}>
                <i className="fa fa-fw fa-user"></i>
                <p>completed Jobs</p>
              </a>
            </li>
            <li>
              <a href="#/" onClick={() => sidebarHandler("PendingRequets")}>
                <i className="fa fa-fw fa-user"></i>
                <p> Pending Jobs</p>
              </a>
            </li>
            <li>
              <a href="#/" onClick={() => sidebarHandler("Bids")}>
                <i className="fa fa-fw fa-user"></i>
                <p> Bids </p>
              </a>
            </li>
            <li>
              <a href="#/" onClick={() => sidebarHandler("BidsReceived")}>
                <i className="fa fa-fw fa-user"></i>
                <p> Bids Received</p>
              </a>
            </li>
            <li>
              <a href="#/" onClick={() => sidebarHandler("BidsAccepted")}>
                <i className="fa fa-fw fa-user"></i>
                <p> Bids Accepted</p>
              </a>
            </li>
          </ul>
        </a>
        <a href="#/">
          <img src={invoiceImg} alt="invoice" className="sidebar_image_icon" />
          Invoice
        </a>
        <a href="#/">
          <img src={policyImg} alt="policy" className="sidebar_image_icon" />
          Policies
        </a>
        <a href="#/">
          <img
            src={helpSupportImg}
            alt="help&amp;suport"
            className="sidebar_image_icon"
          />
          Help &amp; support
        </a>
      </nav>
    </div>
  );
}

export default Sidebar;
