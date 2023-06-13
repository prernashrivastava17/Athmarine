import React from "react";
import "./index.scss";
import { Link } from "react-router-dom";
import facebookIcon from "./../../assets/images/static_website/facebook.png";
import linkedInIcon from "./../../assets/images/static_website/linkedin.png";
import instagramIcon from "./../../assets/images/static_website/instagram.png";
import twitterIcon from "./../../assets/images/static_website/twitter.png";
import {
  emailSubscription,
  emailSubscriptionActions,
} from "../../store/vendorRegistration/emailSubscription";
import {
  phoneSubscription,
  phoneSubscriptionActions,
} from "../../store/vendorRegistration/phoneSubscription";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { emailValidation } from "../../utils/helpers";
import { numberValidation } from "../../utils/helpers";
import TermsAndConditions from "../../pages/Login/components/Modals/TermsAndConditions/TermsAndConditions";
import PrivacyPolicyAth from "../../pages/Login/components/Modals/PrivacyPolicy/PrivacyPolicyAth";

const Footer = () => {
  const dispatch = useDispatch();

  const [email, setEmail] = useState(``);
  const [phone, setPhone] = useState(``);
  const [emailError, setEmailError] = useState(false);
  const [phoneError, setPhoneError] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [modalOpenOnPhone, setModalOpenOnPhone] = useState(false);

  const emailValidation = () => {
    const regex =
      /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
    if (email === "") {
      setEmailError("Email is required");
      return false;
    } else if (regex.test(email) === false) {
      setEmailError("Email is not valid");
      return false;
    }

    setModalOpen(true);
  };

  const onOkHandler = () => {
    setModalOpen(false);
    const data = {
      email: email,
      id: 0,
      status: 0,
    };

    setEmailError(false);
    dispatch(
      emailSubscription(data, `emailSubscription/createEmailSubscription`)
    );
    setEmail("");
  };

  const onCancelHandler = () => {
    setModalOpen(false);
  };

  const phoneApi = () => {
    if (phone === "") {
      setPhoneError("Mobile Number is required");
      return false;
    } else if (!numberValidation(phone)) {
      setPhoneError("Mobile Number is invalid");
      return false;
    } else if (phone.trim() && phone.length > 5 && phone.length < 14) {
      setModalOpenOnPhone(true);
      setPhoneError(false);
    } else {
      setPhoneError(true);
    }
  };

  const onOkHandle = () => {
    setModalOpenOnPhone(false);
    const data = {
      id: 0,
      phoneNum: phone,
      status: 0,
    };

    setEmailError(false);
    dispatch(
      phoneSubscription(data, `phoneSubscription/createPhoneSubscription`)
    );
    setPhone("");
  };

  const onCancelHandle = () => {
    setModalOpenOnPhone(false);
  };

  const getYear = () => {
    return new Date().getFullYear();
  };

  return (
    <>
      {modalOpen && (
        <PrivacyPolicyAth cancel={onCancelHandler} ok={onOkHandler} />
      )}
      {modalOpenOnPhone && (
        <PrivacyPolicyAth cancel={onCancelHandle} ok={onOkHandle} />
      )}
      <footer>
        <section className="main_wrapper">
          <p className="main_heading">Want to learn more about Ath Marine?</p>
          <p className="sub_heading">
            We are always looking to talk to maritime professionals to
            understand the issues currently faced by our industry related to
            service requirements on vessels. We are keen to learn and strive to
            resolve the issues using all available technologies.
          </p>
          <p className="contact_info">
            <a className="phone" href="tel:+15859287030">
              1-585-928-7030
            </a>
            <a className="email" href="mailto:marketing@athmarine.com">
              marketing@athmarine.com
            </a>
          </p>
          <div className="newsletter_container">
            <p className="heading">For further updates</p>
            <div className="text_container">
              <input
                type="text"
                className="text_email"
                placeholder="E-mail"
                value={email}
                onChange={(e) => {
                  setEmail(e.target.value);
                }}
              />
              <button className="btn" onClick={emailValidation}>
                Subscribe
              </button>
              <label className="email_error_label">{emailError}</label>
            </div>
            <div className="text_container">
              <input
                type="number"
                className="text_mobile mobile_size"
                placeholder="Mobile-Number"
                value={phone}
                onChange={(e) => {
                  if (e.target.value.length < 14)
                    setPhone(e.target.value.trim());
                }}
              />

              <button className="btn" onClick={phoneApi}>
                WhatsApp Subscribe
              </button>
              <label className="phone_error_label">{phoneError}</label>
            </div>
          </div>
          <div className="social_media">
            <div className="media_section">
              <a className="redirection_link" href="/#">
                <span className="media_icon">
                  <img src={facebookIcon} alt="facebook icon" />
                </span>
                <span className="media_name">Facebook</span>
              </a>
            </div>
            <div className="media_section">
              <a className="redirection_link" href="/#">
                <span className="media_icon">
                  <img src={twitterIcon} alt="twitter icon" />
                </span>
                <span className="media_name">Twitter</span>
              </a>
            </div>
            <div className="media_section">
              <a className="redirection_link" href="/#">
                <span className="media_icon">
                  <img src={linkedInIcon} alt="linkedin icon" />
                </span>
                <span className="media_name">Linkedin</span>
              </a>
            </div>
            <div className="media_section">
              <a className="redirection_link" href="/#">
                <span className="media_icon">
                  <img src={instagramIcon} alt="instagram icon" />
                </span>
                <span className="media_name">Instagram</span>
              </a>
            </div>
          </div>
          <hr />
          <div className="footer_main">
            <div className="footer_inner">
              <div className="redirect_area">
                <Link to="/tandc" className="term_condition">
                  Terms of Service
                </Link>
                <span className="verticle_line"></span>
                <Link to="/privacyPolicy" className="faq">
                  Privacy Policy
                </Link>
                <span className="verticle_line"></span>
                <Link to="/cookies" className="cookie_preferences">
                  Rules and Guidelines
                </Link>
              </div>
              <div className="copyright_area">
                &#169; {`${getYear()}`} AthMarine. All right reserved
              </div>
              <div className="space_text"> &nbsp;</div>
            </div>
          </div>
        </section>
      </footer>
    </>
  );
};

export default Footer;
