import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router";
import VerifyWebMobileOTP from "./components/VerifyWebMobileOTP/VerifyWebMobileOTP";
import WebChangePassword from "./components/WebChangePassword/WebChangePassword";
import VerifyWebEmailOTP from "./VerifyWebEmailOTP";
import { getEncodedUserInformation } from "../../store/vendorRegistration/getUserByEncodedId";
import { verifyWebEmailOTP } from "../../store/vendorRegistration/verifyWebOTPEmail";
import { getMobileOTPFunc } from "../../store/vendorRegistration/getMobileOtp";
import AlreadyVerified from "./components/AlreadyVerified/AlreadyVerfied";

const WebVerification = () => {
  const history = useHistory();
  const dispatch = useDispatch();
  const [emailVerified, setEmailVerified] = useState(false);
  const [mobileVerified, setMobileVerified] = useState(false);
  const [passwordVerified, setPasswordVerified] = useState(false);
  const [isAlreadyVerified, setIsAlreadyVerified] = useState(false);
  const encodedUserInformation = useSelector(
    (state) => state?.encodedUserReducer?.userInformationRes
  );

  const verifyEmailInformation = useSelector(
    (state) => state?.verifyWebOTPReducer?.verifyEmailRes
  );

  const verifyMobileLoading = useSelector(
    (state) => state.verifyMobileOTP.isLoading
  );
  const verifyMobileRes = useSelector(
    (state) => state.verifyMobileOTP.verifyMobileRes
  );

  const verifiedEmail = () => {
    setEmailVerified(true);
  };
  const verifiedMobileFunc = () => {
    setMobileVerified(true);
  };

  useEffect(() => {
    const urlSearchParams = new URLSearchParams(window.location.search);
    const params = Object.fromEntries(urlSearchParams.entries());
    dispatch(getEncodedUserInformation(params));
  }, [dispatch]);

  useEffect(() => {
    if (encodedUserInformation !== null) {
      console.log({
        email: encodedUserInformation.emailVerified,
        phone: encodedUserInformation.phoneVerified,
        password: encodedUserInformation.isPasswordExist,
      });
      if (
        encodedUserInformation.emailVerified &&
        encodedUserInformation.phoneVerified &&
        encodedUserInformation.isPasswordExist !== null
      ) {
        console.log("iffffffffffffff");
        setIsAlreadyVerified(true);
      } else {
        console.log("elseeeeeeeeeeee");
        dispatch(
          verifyWebEmailOTP(
            { email: `${encodedUserInformation?.email}` },
            `user/emailVerification`
          )
        );
      }
    }
  }, [encodedUserInformation]);

  useEffect(() => {
    if (verifyEmailInformation !== null) {
      if (verifyEmailInformation?.emailVerified) {
        if (
          encodedUserInformation.phoneVerified === false &&
          encodedUserInformation.isPasswordExist == null
        ) {
          dispatch(
            getMobileOTPFunc({
              mobile: `${verifyEmailInformation.phoneCode}${verifyEmailInformation.primaryPhone}`,
            })
          );
          setMobileVerified(false);
          setPasswordVerified(false);
        } else if (
          encodedUserInformation.phoneVerified === true &&
          encodedUserInformation.isPasswordExist == null
        ) {
          setMobileVerified(true);
          setPasswordVerified(false);
        } else if (
          encodedUserInformation.phoneVerified === true &&
          encodedUserInformation.isPasswordExist != null
        ) {
          setMobileVerified(true);
          setPasswordVerified(true);
        }
      }
    }
  }, [verifyEmailInformation]);

  const verificationScreenRender = () => {
    if (!mobileVerified && !passwordVerified) {
      return <VerifyWebMobileOTP verifiedMobileFunc={verifiedMobileFunc} />;
    } else if (mobileVerified && !passwordVerified) {
      return <WebChangePassword />;
    }
  };
  return (
    <>
      {isAlreadyVerified && <AlreadyVerified />}

      {!isAlreadyVerified && verificationScreenRender()}
    </>
  );
};

export default WebVerification;
