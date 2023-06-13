import React, { Component } from "react";

import "./Footer.scss";
import logo from "./../../assets/images/logo.png";
import logo12 from "./../../assets/images/Transas_Logo.svg";
import emailIcon from "./../../assets/images/icon-email.png";
import phoneIcon from "./../../assets/images/icon-phone.png";
import locationIcon from "./../../assets/images/icon-location.png";
import policiesIcon from "./../../assets/images/footer_policies.png";
import assistanceIcon from "./../../assets/images/footer_assistance.png";
import helpIcon from "./../../assets/images/footer_help.png";

class Footer extends Component {
  state = {
    date: "",
  };

  componentDidMount() {
    this.getDate();
  }

  getDate = () => {
    var date = new Date().getFullYear();
    this.setState({ date });
  };

  render() {
    const { date } = this.state;
    return (
      <footer>
        <div className="footer_container">
          <div className="upper_section">
            <div className="left_section">
              <div className="logo_container">
                <img src={logo} alt="logo" />
              </div>
            </div>
            <div className="right_section">
              <div className="company_info_container">
                <div className="info">
                  <div className="info_part2">
                    <div className="row">
                      <a target="_blank" href="/tandc" className="contact_link">
                        Terms &amp; services
                      </a>
                    </div>

                    <div className="row">
                      <a
                        target="_blank"
                        href="/privacyPolicy"
                        className="contact_link"
                      >
                        Privacy policies
                      </a>
                    </div>

                    <div className="row">
                      <a
                        target="_blank"
                        href="/cookies"
                        className="contact_link"
                      >
                        Rules &amp; guidelines
                      </a>
                    </div>

                    <div className="row">
                      <a target="_blank" href="/faq" className="contact_link">
                        FAQ's
                      </a>
                    </div>

                    <div className="row">
                      <a
                        target="_blank"
                        href="/contact-us"
                        className="contact_links"
                      >
                        Contact Us
                      </a>
                    </div>
                  </div>
                </div>

                {/* <div className="info">
                  <div className="info_part1">
                    <img src={emailIcon} alt="email-icon" />
                  </div>
                  <div className="info_part2">
                    <a href="mailto:hello@email.com">hello@email.com</a>
                  </div>
                </div>
                <div className="info">
                  <div className="info_part1">
                    <img src={locationIcon} alt="location-icon" />
                  </div>
                  <div className="info_part2">
                    <p>772 Lyonwood Ave Walnut, CA 91789</p>
                  </div>
                </div>
                <div className="info">
                  <div className="info_part1">
                    <img src={phoneIcon} alt="phone-icon" />
                  </div>
                  <div className="info_part2">
                    <a href="tel:hello@email.com">+91 123 456 789</a>
                  </div>
                </div>*/}
              </div>
            </div>
          </div>
          <div className="bottom_section">
            <p className="copyright_text">
              © {date} AthMarine. All rights reserved
            </p>
          </div>
        </div>
      </footer>
    );
  }
}

export default Footer;
