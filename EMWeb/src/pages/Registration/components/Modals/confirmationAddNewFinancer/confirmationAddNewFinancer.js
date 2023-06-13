import "./confirmationAddNewFinancer.scss";
import Backdrop from "./../../../../../components/Backdrop/Backdrop";

const ConfirmationAddNewFinancer = (props) => {
  return (
    <Backdrop>
      <div className="modal_confirmation_modal">
        <div className="modal_heading"> Are you Sure?</div>
        <div className="modal_text">
          You want to add a new member to the Finance team?
        </div>
        <div className="button_container">
          <a className="btn btn_cancel" onClick={props.cancel}>
            CANCEL
          </a>
          <a className="btn btn_ok" onClick={props.ok}>
            Sure
          </a>
        </div>
      </div>
    </Backdrop>
  );
};

export default ConfirmationAddNewFinancer;
