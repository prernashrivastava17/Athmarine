import React from "react";
import Backdrop from "../../../../../components/Backdrop/Backdrop";
import "./disagreeTermAndCondition.scss";
import { useHistory } from "react-router-dom";

const DisagreeTermAndCondition = (props) => {
  const history = useHistory();

  const handleModal = () => {
    history.push("/");
    window.location.reload(false);
  };
  return (
    // <Backdrop>
    <div className="custom_backdrop">
      <div className="modal_confirmation_modal_disagree container_model">
        <div className="modal_text1">
          <b>Are you sure? </b>
        </div>
        <div className="modal_text">
          To proceed further, kindly accept the terms and conditions.
        </div>
        <div className="note_text">
          <p className="a_note">
            Note<span className="mandantory">*</span>:
            <div className="on_quit">
              &nbsp; If you click on the Quit button you will be redirected to
              the home screen.
            </div>
          </p>
        </div>
        <div className="button_container">
          <div className="btn btn_cancel" onClick={handleModal}>
            Quit
          </div>
          <div className="btn btn_ok" onClick={props.ok}>
            Proceed
          </div>
        </div>
      </div>
    </div>
    //   </Backdrop>
  );
};

export default DisagreeTermAndCondition;
