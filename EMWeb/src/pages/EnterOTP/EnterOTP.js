import React, { useState, useEffect } from 'react';
import OtpInput from 'react-otp-input';
import { Redirect, useHistory } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { ToastError } from '../../components/Tostify';
import CountdownTimer from './../../components/CountdownTimer/CountdownTimer';

import { verifyEmailFun, emailVerifyActions } from "../../store/vendorRegistration/verifyEmail";
import { getEmailOTPFun } from "../../store/vendorRegistration/getEmailOtp";

import './EnterOTP.scss';
import lockIcon from './../../assets/images/lock-ico-black.png';
import Loading from '../../components/Loading/Loading';
import resendIcon from './../../assets/images/refresh-icon.png';
import Header from '../../components/Header/Header';
import Footer from '../../components/Footer/Footer';

const EnterOTP = (props) => {

    const history = useHistory();
    const dispatch = useDispatch();
    const email = useSelector(state => state.resetPasswordReducer.resetEmail);
    const isLoadingVerifyEmailRes = useSelector(state => state.verifyEmailOTP.isLoading);
    const verifyEmailRes = useSelector(state => state.verifyEmailOTP.verifyEmail);
    const IsErrorVerifyEmailRes = useSelector(state => state.verifyEmailOTP.isError);
    const getEmailOTPLoading = useSelector(state => state.emailOTP.isLoading);
    const IsErrorGetEmailOTPLoading = useSelector(state => state.emailOTP.isError);
    const resendOTPResponse = useSelector(state => state.emailOTP.verifyEmail);

    const [otp, setOtp] = useState("");
    const [timerOut, setTimerOut] = useState(false);

    useEffect(() => {
        if (verifyEmailRes) {
            history.push('/changePassword');
            dispatch(emailVerifyActions.emptyError());
        }
    });

    useEffect(() => {
        if (IsErrorVerifyEmailRes !== '') {
            ToastError(IsErrorVerifyEmailRes);
        } else if (IsErrorGetEmailOTPLoading !== '') {
            ToastError(IsErrorGetEmailOTPLoading);
        }
    }, [IsErrorVerifyEmailRes, IsErrorGetEmailOTPLoading]);

    const resendOTP = () => {
        dispatch(getEmailOTPFun({ email: email }));
    }

    const onTimerExpire = () => {
        setTimerOut(true);
    };

    const handleFormSubmission = (e) => {
        e.preventDefault()
        let data = { otp, email, };
        dispatch(verifyEmailFun(data));
        setTimerOut(false);
    }

    //  use this condition at last to redirect if email is null or falsy 
    // KEEP THIS IN LAST BEFORE JSX
    if (!email) {
        return <Redirect to='/' />
    }


    return (
        <>
            {(isLoadingVerifyEmailRes || getEmailOTPLoading) && <Loading text="Loading..." />}

            <Header page={`login`} userType={props.setUserType} />
            <section className="login_container">
                <form onSubmit={handleFormSubmission}>
                    <div className="a_opt_container">

                        <div className="lock_container">
                            <img src={lockIcon} alt="lock-icon" className="lock_icon" />
                            <p className="modal_heading"> Enter the OTP </p>
                        </div>

                        <div className="text_info">
                            <p className="text">
                                Please enter the code sent to you on your mail.
                            </p>
                        </div>

                        <div className="otp_container">
                            <div className="otp_main_container">
                                <span className="text">Enter OTP</span>
                                <span className="timer" id="timer">
                                    {resendOTPResponse == null && (<CountdownTimer onTimerExpire={onTimerExpire} />)}
                                    {resendOTPResponse !== null && (<CountdownTimer onTimerExpire={onTimerExpire} />)}
                                </span>
                            </div>
                            <div className="enter_otp_container">
                                <OtpInput
                                    className="otpInput"
                                    isInputNum={true}
                                    value={otp}
                                    onChange={otp => setOtp(otp)}
                                    numInputs={6}
                                    inputStyle={{ width: "45px" }}
                                    separator={<span>&nbsp;&nbsp;&nbsp;</span>}
                                />
                            </div>
                            <div className="did_not_receive_otp_container">
                                {timerOut ? (
                                    <p className="resend_btn " onClick={resendOTP}>
                                        Resend <img src={resendIcon} alt="resend-icon" className="resend_icon" />
                                    </p>
                                ) : (
                                    <p className="resend_btn disable-anchor" onClick={resendOTP}>
                                        Resend <img src={resendIcon} alt="resend-icon" className="resend_icon" />
                                    </p>
                                )}

                                {/* <p className="resend_btn" onClick={resendOTP}> Resend <img src={resendIcon} alt="resend-icon" className="resend_icon" /> </p> */}
                            </div>
                        </div>

                        <div className="submit_container">
                            <button type="submit"> Reset </button>
                        </div>
                    </div>
                </form>
            </section>
            <Footer />
        </>
    );
}

export default EnterOTP;