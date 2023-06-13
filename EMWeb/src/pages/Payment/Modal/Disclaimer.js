import React from "react";
import "./Disclaimer.scss";
import Backdrop from "../../../components/Backdrop/Backdrop";

const Disclaimer = (props) => {
  return (
    <Backdrop>
      <div className="section_modal">
        <div className="disclaimercontainer_area">
          <div className="main_icon_disclaimercontainer">
            <div className="disclaimer_heading">Disclaimer</div>
          </div>
          {/* <div className="heading">
            <span>Note : </span>The credit/debit card details will not be saved.
          </div> */}
          <div className="note_text">
            <p className="a_note">
              Note<span className="mandantory">*</span>:
              <div className="on_quit">
                &nbsp; The credit/debit card details will not be saved.
              </div>
            </p>
          </div>
          <div className="button_container_disclaimer">
            <button className="button" onClick={props.redirect}>
              Proceed
            </button>
          </div>
        </div>
      </div>
    </Backdrop>
  );
};

export default Disclaimer;
