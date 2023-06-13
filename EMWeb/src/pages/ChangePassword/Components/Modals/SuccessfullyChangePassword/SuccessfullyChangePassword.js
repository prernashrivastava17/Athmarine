import React from 'react';
import './SuccessfullyChangePassword.scss';
import Backdrop from '../../../../../components/Backdrop/Backdrop';

 const SuccessfullyChangePassword = (props) => {
    return (
        <Backdrop>
        <div className="modal_successfuly_reset_password">
            <h3>Success</h3>
            <p>Password is reset successfully.</p>
            <div className="submit_container">
                <button type="submit" onClick={() => props.onClose()}> OK </button>
            </div>
        </div>
        </Backdrop>
    )
}

export default SuccessfullyChangePassword;
