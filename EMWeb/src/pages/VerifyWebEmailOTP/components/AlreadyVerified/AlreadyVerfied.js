import React from "react";
import "./AlreadyVerfied.scss";
import { useHistory } from "react-router-dom";

import Footer from "../../../../components/Footer/Footer";
import Header from "../../../../components/Header/Header";

const AlreadyVerfied = () => {
  const history = useHistory();
  return (
    <>
      <Header />
      <div className="modal_already">
        <div className="login_wrapper">
          <div className="modal_text">
            <h3> Your account is already exists</h3>

            <p>Please log-in using your credentials</p>
          </div>
          <div className="submit_container">
            <button
              className="btn"
              type="submit"
              onClick={() => history.push(`/`)}
            >
              Proceed
            </button>
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default AlreadyVerfied;
