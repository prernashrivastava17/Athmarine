import React from "react";

import "./Tooltip.scss";
import iIcon from "./../../assets/images/i-icon.png";

const Tooltip = (props) => {
  return (
    <span className="info_icon">
      <img src={iIcon} alt="i-icon" className="tooltip" />
      <span className="a_tooltiptext"> {props.text} </span>
    </span>
  );
};

export default Tooltip;
