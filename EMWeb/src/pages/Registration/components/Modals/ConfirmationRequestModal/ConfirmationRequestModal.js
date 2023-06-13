import './ConfirmationRequestModal.scss';
import Backdrop from './../../../../../components/Backdrop/Backdrop';

const ConfirmationRequestModal = (props) => {
    return (
        <Backdrop>
            <div className="modal_addition_charges">
            <div className="modal_heading">You are opting for the emergency category,
                the number of bits you will get might be low and their
                price will be high as compared to normal and Urgent Category</div>
                <div className="modal_text">                
                Are you sure you want to proceed?
                </div>
                <div className="button_container">
                    <div className="btn btn_cancel" onClick={props.cancel}>CANCEL</div>
                    <div className="btn btn_ok" onClick={props.ok} >Sure</div>
                </div>
       
        <div className="close_container_charges" onClick={props.closeModal}>
          <span className="close_icon">&times;</span>
        </div>
      </div>
        </Backdrop>
    );
}

export default ConfirmationRequestModal;