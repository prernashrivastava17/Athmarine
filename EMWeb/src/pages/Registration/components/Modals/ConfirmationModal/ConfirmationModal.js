import "./ConfirmationModal.scss";
import Backdrop from "./../../../../../components/Backdrop/Backdrop";

const ConfirmationModal = (props) => {
  return (
    <Backdrop>
      <div className="modal_confirmation_modal">
        <div className="modal_heading">Are you sure you want to go ahead?</div>
        <div className="modal_text">
          Once you will click on proceed, you will not be able to change
          previous data.
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

export default ConfirmationModal;
