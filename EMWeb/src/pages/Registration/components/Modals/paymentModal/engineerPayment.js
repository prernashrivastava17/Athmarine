import './engineerPayment.scss';
import Backdrop from '../../../../../components/Backdrop/Backdrop';

const EngineerPayment = (props) => {
    return (
        <Backdrop>
            <div className="modal_confirmation_modal">
                <div className="modal_text"> you have selected <b>{props.totalEngineer}</b> engineers the total amount to be paid is <b>USD {props.totalPayment}</b></div>
                <div className="modal_text">
                  <b> Are you sure want to proceed ?</b> 
                </div>
                <div className="button_container">
                    <a className="btn btn_cancel_pay" onClick={props.cancel}>CANCEL</a>
                    <a className="btn btn_ok_pay" onClick={props.ok} >Proceed to Pay</a>
                </div>
            </div>
        </Backdrop>
    );
}

export default EngineerPayment;