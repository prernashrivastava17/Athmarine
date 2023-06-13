import './IndividualPayment.scss';
import Backdrop from '../../../../../components/Backdrop/Backdrop';

const IndividualPayment = (props) => {
    return (
        <Backdrop>
            <div className="modal_confirmation_modal">
                <div className="modal_text"> You haven't selected any engineer, So the base Charges will be <b>USD 100</b> amount </div>
                <div className="modal_text">
                  <b> Are you sure want to proceed ?</b> 
                </div>
                <div className="button_container">
                    <a className="btn btn_cancel" onClick={props.cancel}>CANCEL</a>
                    <a className="btn btn_ok" onClick={props.ok} >Proceed</a>
                </div>
            </div>
        </Backdrop>
    );
}

export default IndividualPayment;