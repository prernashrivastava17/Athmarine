import "./ProceedModal.scss";
import Backdrop from "../../../../../components/Backdrop/Backdrop";

const ProceedModal = (props) => {
  return (
    <Backdrop>
      <div className="ProceedModal">
        <div className="modal_text">
          <b> {props.message}</b>
        </div>
        <div className="button_container">
          <a className="btn btn_cancel" onClick={props.cancel}>
            {props.btn1}
          </a>
          <a className="btn btn_ok" onClick={props.ok}>
            {props.btn2}
          </a>
        </div>
      </div>
    </Backdrop>
  );
};

export default ProceedModal;
