import React, { useState, useEffect } from "react";
import "./Payment.scss";
import { useHistory } from "react-router-dom";
import { useSelector } from "react-redux";
import RegistrationSuccessful from "../../Modals/RegistrationSuccessful/RegistrationSuccessful";

const Payment = () => {
  const history = useHistory();
  const paymentRequestRes = useSelector(
    (state) => state.paymentRequestReducer.state
  );
  const navigationResponse = useSelector(
    (state) => state.commonGetApiReducer.getApiRes
  );

  const [isRegistrationSuccessful, setIsRegistrationSuccessful] =
    useState(false);
  // const [navigationResponseDetails, setNavigationResponseDetails] = useState(null);
  const [navigationResponseDetails, setNavigationResponseDetails] =
    useState(null);

  useEffect(() => {
    if (navigationResponse !== null) {
      setNavigationResponseDetails(navigationResponse);
    } else {
      setNavigationResponseDetails(null);
    }
  }, [navigationResponse]);

  const registrationProcessCompleteHandler = () => {
    history.push(`/`);
  };

  const closeModalHandler = () => {
    setIsRegistrationSuccessful(false);
  };

  return (
    <div className="form_container_area">
      <div className="payment_form positioning_container">
        {isRegistrationSuccessful && (
          <RegistrationSuccessful
            redirect={registrationProcessCompleteHandler}
            closeModal={closeModalHandler}
          />
        )}

        {/* ---------------- Partner form ---------------- */}
        <div className="payment_outer_container">
          {navigationResponseDetails !== null && (
            <div className="payment_info_container">
              <div className="info_row">
                <span className="info_heading">Payment mode: </span>
                <span className="info_text">
                  {navigationResponseDetails.paymentMode}
                </span>
              </div>
              <div className="info_row">
                <span className="info_heading">Transaction id: </span>
                <span className="info_text">
                  {navigationResponseDetails.transactionId}
                </span>
              </div>
              <div className="info_row">
                <span className="info_heading">Email: </span>
                <span className="info_text">
                  {navigationResponseDetails.email}
                </span>
              </div>
              <div className="info_row">
                <span className="info_heading">Amount: </span>
                <span className="info_text">
                  {navigationResponseDetails.amount}
                </span>
              </div>
              <div className="info_row">
                <span className="info_heading">Currency: </span>
                <span className="info_text">
                  {navigationResponseDetails.currency}
                </span>
              </div>
            </div>
          )}
        </div>
        <div className="btn_area">
          {navigationResponseDetails !== null && (
            <button
              className="btn_next"
              onClick={() => setIsRegistrationSuccessful(true)}
            >
              Next
            </button>
          )}
        </div>
      </div>
    </div>
  );
};

export default Payment;
