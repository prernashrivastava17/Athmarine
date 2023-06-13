import React , {useState ,useEffect} from 'react';
import { useHistory } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { ToastError } from './../../../../../components/Tostify';

import {resetPassword , resetPasswordActions} from "../../../../../store/ResetPassword/resetPassword";


import './ResetPassword.scss';
import Backdrop from './../../../../../components/Backdrop/Backdrop';
import lockIcon from './../../../../../assets/images/lock-ico-black.png';
import Loading from '../../../../../components/Loading/Loading';
import { emailValidation } from '../../../../../utils/helpers';


const ResetPassword = (props) => {
    const history = useHistory();
    const dispatch = useDispatch();
    
    const isLoadingResetPasswordRes = useSelector(state=>state.resetPasswordReducer.isLoading);
    const resetPasswordRes = useSelector(state=>state.resetPasswordReducer.resetPassword);
    const IsErrorResetPasswordRes = useSelector(state=>state.resetPasswordReducer.isError);
    
    const [email, setEmail] = useState("")
    const [errors, setErrors] = useState(false);

    const validateForm = () => {
        if (!emailValidation(email.trim())) {
            setErrors(true);
            return false
        } else {
            setErrors(false);
            return true;
        }
    }



    useEffect(() => {
        if(resetPasswordRes){
            history.push('/otp');
            dispatch(resetPasswordActions.emptyError())
        }
    },[resetPasswordRes])
    
    useEffect(()=>{
        if(IsErrorResetPasswordRes!==''){
            ToastError(IsErrorResetPasswordRes)
        }
    },[IsErrorResetPasswordRes])

    const handleFormSubmission = (e) => {
        e.preventDefault();
        if(validateForm()){
            dispatch(resetPassword(`resetPassword?email=${email}`))
            dispatch(resetPasswordActions.saveResetEmail({email:email}))
        }
        // history.push('/otp'); // remove after no need 
    }

    const closePopupHandler = () => {
        props.onClose();
    }


    return (
        <Backdrop>
            {(isLoadingResetPasswordRes) && <Loading text="Loading..." />}
            <div className="modal_reset_password">

                <div className="lock_container">
                    <img src={lockIcon} alt="lock-icon" className="lock_icon" />
                    <p className="modal_heading"> Reset Password </p>
                </div>

                <div className="text_info">
                    <p className="text">
                        Enter the email associated with your account and we’ll send you the OTP for changing the password.
                    </p>
                </div>

                <div className="form_container">
                    <form onSubmit={handleFormSubmission}>
                        <div className="form_control_area">
                            <label className="form_label"> Email Address
                                <span className="label_mandatory">*</span>
                            </label>
                            <input type="text" name="email" className="form_control" onChange={e=>setEmail(e.target.value)} />
                            {
                               errors && (email.trim() === "" || !emailValidation(email.trim())) && <span className="error">Please enter valid email</span>
                            }
                        </div>

                        <div className="submit_container">
                            <button type="submit"> Send OTP </button>
                        </div>
                    </form>
                </div>

                <div className="close_container" onClick={closePopupHandler}>
                    <span className="close_icon">&times;</span>
                </div>
            </div>
        </Backdrop>
    )
}

export default ResetPassword;