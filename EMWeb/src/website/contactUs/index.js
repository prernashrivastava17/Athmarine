import React, { useState, useEffect, useRef } from "react";
import "./index.scss";
import Header from "../header";
import Footer from "../footer";
import ReCAPTCHA from "react-google-recaptcha";

import { useDispatch, useSelector } from "react-redux";
import { postWebsiteQueries } from "../../store/website/contactUs";
import { ToastSuccess } from "../../components/Tostify";
import { emailValidation } from "../../utils/helpers";

const ContactUs = () => {
  const dispatch = useDispatch();
  const ref = useRef(null);

  const contactResponse = useSelector(
    (state) => state.postWebsiteQueries.state
  );

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [subject, setSubject] = useState("");
  const [message, setMessage] = useState("");
  const [isTermsAndConditionAgreed, setIsTermsAndConditionAgreed] =
    useState(false);
  const [error, setError] = useState(false);
  const [errMsg, setErrMsg] = useState(``);
  const [verfiRecaptcha, setVerfiRecaptcha] = useState(false);

  function onChange(value) {
    if (value !== null) {
      setVerfiRecaptcha(true);
    }
  }

  const submitQueryHandler = (e) => {
    e.preventDefault();

    setError(false);
    setErrMsg(``);

    if (name.trim() === ``) {
      setErrMsg("Please fill Proper name.");
      setError(true);
      return;
    } else if (email.trim() === ``) {
      setErrMsg(`Please fill email i'd.`);
      setError(true);
      return;
    } else if (!emailValidation(email.trim())) {
      setErrMsg(`Invalid email i'd.`);
      setError(true);
      return;
    } else if (subject.trim() === ``) {
      setErrMsg(`Subject can't be empty.`);
      setError(true);
      return;
    } else if (message.trim() === ``) {
      setErrMsg(`Message can't be empty.`);
      setError(true);
      return;
    } else if (verfiRecaptcha === false) {
      setErrMsg(`Verify recaptcha.`);
      setError(true);
      return;
    }
    // if (
    //   name.trim() === "" ||
    //   email.trim() === "" ||
    //   !emailValidation(email.trim()) ||
    //   subject.trim() === "" ||
    //   message.trim() === "" ||
    //   verfiRecaptcha === false
    // ) {
    //   setError(true);
    //   return false;
    // } else {
    //   setError(false);
    // }

    const data = {
      email: email,
      message: message,
      name: name,
      subject: subject,
      privacyPolicy: true,
    };

    dispatch(postWebsiteQueries(`contact/addContactUs`, data));
    setName("");
    setEmail("");
    setMessage("");
    setSubject("");
    setVerfiRecaptcha(false);
    ref.current.reset();
  };

  // useEffect(() => {
  //   if (contactResponse !== null) {
  //     ToastSuccess("We will reach you soon.");
  //   }
  // }, [contactResponse]);

  return (
    <>
      <Header />
      <main>
        <section className="section_contact_us">
          <div className="main_wrapper">
            <h1 className="main_heading">Contact Us</h1>
            <div className="main_area">
              {/* <h1 className="heading">Contact</h1> */}
              <hr />
              <div className="contact_area">
                <div className="contact_text_area">
                  <p>
                    Need to get in touch? You can use our contact form to send
                    us a message.
                  </p>
                  <div className="contact_support_options">
                    <ul>
                      <li>
                        We value the time taken for asking us questions and / or
                        giving us feedback.
                      </li>
                      <li>
                        We don’t guarantee immediate response but we assure that
                        each message will be read, contents would be noted and
                        feasible action will be taken.
                      </li>
                      <li>We thank you for getting in touch with us.</li>
                    </ul>
                  </div>
                </div>
                <div className="contact_form_area">
                  <form onSubmit={submitQueryHandler}>
                    <div className="input_container">
                      <input
                        type="text"
                        placeholder="Your Name*"
                        className="inner_input_area"
                        value={name}
                        onChange={(e) => {
                          if (!Number(e.nativeEvent.data))
                            setName(e.target.value);
                        }}
                      />
                      <input
                        type="text"
                        placeholder="Your Email*"
                        className="inner_input_area"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                      />
                    </div>
                    <input
                      type="text"
                      placeholder="Subject*"
                      className="outer_input_area"
                      value={subject}
                      onChange={(e) => setSubject(e.target.value)}
                    />

                    <textarea
                      rows="7"
                      placeholder="Your Message*"
                      value={message}
                      onChange={(e) => setMessage(e.target.value)}
                    ></textarea>

                    {error && <span className="error">{errMsg}</span>}

                    {/* {error &&
                      (name.trim() === "" ||
                        email.trim() === "" ||
                        !emailValidation(email.trim()) ||
                        subject.trim() === "" ||
                        message.trim() === "") && (
                        <span className="error">
                          Please fill all the fields.
                        </span>
                      )} */}

                    <ReCAPTCHA
                      className="outer_input_area1"
                      ref={ref}
                      /*-----localhost------*/
                      // sitekey="6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI"

                      /*-----staging && QA------*/
                      // sitekey="6LfhgOAeAAAAABZGNAJ93O38abFQdz-uy43AP1Zl"
                      /*-----production------*/
                      sitekey="6LeXdUIfAAAAAE6gh_lWDMaPIC5IQau2Lsz-S7Tf"
                      onChange={onChange}
                    />
                    {error && verfiRecaptcha === false && (
                      <span className="error">Please verify recaptcha.</span>
                    )}

                    {/* <div className="tacbox">
                      <input
                        id="checkbox"
                        type="checkbox"
                        checked={isTermsAndConditionAgreed}
                        onClick={(e) =>
                          setIsTermsAndConditionAgreed(e.target.checked)
                        }
                      />
                      <label for="checkbox">
                        I agree to these
                        <Link to="/tandc">Terms and Conditions</Link>.
                      </label>
                    </div> */}
                    <input
                      type="submit"
                      value="Send Message"
                      className="btn_submit_form"
                    />
                  </form>
                </div>
              </div>
            </div>
          </div>
        </section>
      </main>
      <Footer />
    </>
  );
};

export default ContactUs;
