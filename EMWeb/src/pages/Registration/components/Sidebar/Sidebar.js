import React from "react";

import "./Sidebar.scss";
import rightIcon from "./../../../../assets/images/right.svg";

const sidebarPartnerData = [
  { title: "General" },
  { title: "Team" },
  { title: "Services" },
  { title: "Finance" },
  { title: "Payment" },
];

const sidebarPartnersIndividualData = [
  { title: "General" },
  { title: "Professional" },
  { title: "Services" },
  { title: "Finance" },
  { title: "Payment" },
];

const sidebarCustomerData = [
  { title: "General" },
  { title: "Team" },
  { title: "Finance" },
];

const Menu = React.memo((props) => {
  let sidebarData = null;
  switch (props.selectedUser) {
    case "partner":
      sidebarData = sidebarPartnerData;
      break;
    case "partners-individual":
      sidebarData = sidebarPartnersIndividualData;
      break;
    case "customer":
      sidebarData = sidebarCustomerData;
      break;
    default:
      return null;
  }

  const { sidebarHandler } = props;

  return (
    <div className="menu_container">
      <div className="menu_wrapper">
        {sidebarData.map((curLabel, index) => {
          let contentToShow = "",
            active = "";
          if (props.makeMenuActive > index + 1) {
            contentToShow = (
              <span
                className={`serial_num`}
                style={{ backgroundColor: "#7EDCD9", borderColor: "#7EDCD9" }}
              >
                <img
                  src={rightIcon}
                  alt="right-tick-icon"
                  style={{ width: "60%" }}
                />
              </span>
            );
          } else if (props.makeMenuActive === index + 1) {
            active = "active";
            contentToShow = (
              <span className={`serial_num active`}> {index + 1} </span>
            );
          } else if (props.makeMenuActive < index + 1) {
            contentToShow = <span className={`serial_num`}> {index + 1} </span>;
          }

          return (
            <div className="menu" key={index}>
              <div className="menu_name">
                <span
                  className={active}
                  onClick={() => sidebarHandler(curLabel.title)}
                >
                  {curLabel.title}
                </span>
              </div>
              <div
                className="menu_serial_count"
                onClick={() => sidebarHandler(curLabel.title)}
              >
                {contentToShow}
              </div>
            </div>
          );
        })}
        <div className="main_horizontal_line">&nbsp;</div>
      </div>
    </div>
  );
});

export default Menu;
