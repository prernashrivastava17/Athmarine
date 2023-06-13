import './transaction.scss';
import Backdrop from '../../../../../components/Backdrop/Backdrop';



const TransactionType = (props) => {
    return (
        <Backdrop>
            <div className="modal_confirmation_modal_invoice">
            <div className="form_control_spread">
                <label className="form_label123">
                  Pay via
                </label>
                <div className="radio_container">
                  <span className="a_control">
                    <input
                      type="radio"
                      name="sameAsAdmin"
                      className="form_control_radio_type"
                      id="a_yes"
                      value="yes"
                      onClick={props.onPayment}
                    />
                    <label className="form_label" htmlFor="a_yes">
                      Credit Card
                    </label>
                  </span>
                  <span className="a_control">
                    <input
                      type="radio"
                      name="sameAsAdmin"
                      className="form_control_radio_type"
                      id="a_no"
                      value="no"
                      onClick={props.onInvoice}
                    />
                    <label className="form_label" htmlFor="a_no">
                      Bank Transfer
                    </label>
                  </span>
                </div>
              </div>
              <div className="close_container" onClick={props.cancelTransactionType}>
                    <span className="close_icon">&times;</span>
                </div>
            
            </div>
        </Backdrop>
    );
}

export default TransactionType;