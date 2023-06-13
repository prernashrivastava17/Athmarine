import React from "react";
import "./BidsAccepted.scss";
import OpenChatImg from "../../../../assets/images/Icon open-chat.svg";
import dummyImg from "../../../../assets/images/dummy.PNG";
import showPasswordIcon from "../../../../assets/images/show-password-icon.png";
import editIcon from "../../../../assets/images/edit-icon.png";
import { useState } from "react";

function BidsAccepted() {
  const [PORaise, setPORaise] = useState(false);

  return (
    <div className="bid_container">
      <div className="container">
        <div>
          <h2>
            Dashboard Latest Requests
            <span className="editprofile"> Bid Accepted</span>
          </h2>
          <h1>Bid Accepted</h1>
        </div>
        <div className="bid_card_container">
          <div>
            <div className="requestor_box_container">
              <div className="box_container">
                <div className="block_container">
                  <div>
                    <h3 className="card_heading">Vessel :</h3>
                    <p className="cardSubHeading">Lorem Ipsum</p>
                  </div>
                  <div>
                    <h3 className="card_heading">Country :</h3>
                    <p className="cardSubHeading">Lorem Ipsum</p>
                  </div>
                  <div>
                    <h3 className="card_heading">State :</h3>
                    <p className="cardSubHeading">Lorem Ipsum</p>
                  </div>
                  <div>
                    <h3 className="card_heading">PORT :</h3>
                    <p className="cardSubHeading">Lorem Ipsum</p>
                  </div>
                </div>
                <div className="block_container">
                  <div>
                    <h3 className="card_heading">ETA :</h3>
                    <p className="cardSubHeading pencilWraper">
                      Lorem Ipsum
                      {PORaise && <img src={editIcon} alt="" width={"25px"} />}
                    </p>
                  </div>
                  <div>
                    <h3 className="card_heading">ETD :</h3>
                    <p className="cardSubHeading pencilWraper">
                      Lorem Ipsum
                      {PORaise && <img src={editIcon} alt="" width={"25px"} />}
                    </p>
                  </div>
                  <div>
                    <h3 className="card_heading">Category :</h3>
                    <p className="cardSubHeading">Lorem Ipsum</p>
                  </div>
                </div>

                <div className="bids_button_container">
                  <div className="bid_accepted_btn_container">
                    {PORaise ? (
                      <button type="button" className="btn btn_cancel_btn">
                        P.O Raised
                      </button>
                    ) : (
                      <button type="button" className="btn btn_cancel_btn">
                        Bid Accepted
                      </button>
                    )}
                  </div>

                  <div className="raise_PO_container">
                    {!PORaise && (
                      <button
                        type="submit"
                        className="btn update_btn"
                        onClick={() => setPORaise(true)}
                      >
                        Raise P.O.
                      </button>
                    )}
                  </div>
                </div>
              </div>
            </div>
            {/*  */}
          </div>
          <div className="line_item_container_wraper">
            <div className="line_item_container">
              <div className="line_item_header_container">
                <div className="image_name_container">
                  <img
                    src={dummyImg}
                    alt=""
                    width={150}
                    className="image_vendor_name"
                  />
                  <label htmlFor="vendor_name" className="vendor_name">
                    Vendor Name
                  </label>
                </div>

                <button
                  type="submit"
                  value="Chat"
                  className="btn update_btn"
                  // onClick={openChatScreenFunc}
                >
                  <img
                    src={OpenChatImg}
                    alt="open Chat"
                    className="chat_icon"
                  />
                  <span className="chat_icon"> Chat</span>
                </button>
              </div>
              <div>
                <div className="para_container">
                  <p className="para">
                    There are many variations of passages of Lorem Ipsum
                    available, but the majority have suffered alteration in some
                    form, by injected humour, or randomised words which don't
                    look even slightly believable. If you are going to use a
                    passage of Lorem Ipsum, you need to be sure there isn't
                    anything embarrassing hidden in the middle of text. All the
                    Lorem Ipsum generators on the Internet tend to repeat
                    predefined chunks as necessary, making this the first true
                    generator on the Internet.
                  </p>
                </div>
                <div className="engineers_container">
                  <label htmlFor="engineers" className="engineers">
                    Engineers :
                  </label>

                  <button type="button" className="btn btn_engineers">
                    <label htmlFor="engineer" className="btn_label">
                      Engineers 1
                    </label>
                    <img src={showPasswordIcon} alt="" />
                  </button>
                  <button type="button" className="btn btn_engineers">
                    <label htmlFor="engineer" className="btn_label">
                      Engineers 2
                    </label>
                    <img src={showPasswordIcon} alt="" />
                  </button>
                  <button type="button" className="btn btn_engineers">
                    <label htmlFor="engineer" className="btn_label">
                      Engineers 3
                    </label>
                    <img src={showPasswordIcon} alt="" />
                  </button>
                </div>
                <hr className="horizontal_line" />
                <div className="quotation_container_wraper">
                  <label htmlFor="quotation" className="quotation">
                    Quotations :
                  </label>
                </div>
                <div className="quotation_container_items">
                  <div className="quotation_container">
                    <div className="quotation_name_container">
                      <h3 className="quotation_name"> Travel</h3>
                    </div>
                    <div className="quotation_description_container">
                      <p className="quotation_description">
                        Air Fare (Round Trip Total) :
                      </p>
                      <p className="quotation_description">
                        Transportation Total :
                      </p>
                      <p className="quotation_description">
                        Travel Time Cost :
                      </p>
                    </div>
                    <div className="quotation_price_details_container">
                      <p className="quotation_price">$ 200</p>
                      <p className="quotation_price">$ 200</p>
                      <p className="quotation_price">$ 200</p>
                    </div>
                    <div className="quotation_price_total_container">
                      <p className="quotation_price_total">$ 600</p>
                    </div>
                  </div>
                  <div className="quotation_container">
                    <div className="quotation_name_container">
                      <h3 className="quotation_name"> Working</h3>
                    </div>
                    <div className="quotation_description_container">
                      <div>
                        <h4 className="quotation_description_Heading">
                          Normal Days
                        </h4>
                        <div className="quotation_description_container_box">
                          <p className="quotation_description">
                            Working Rate - Normal Hours - (0900hrs-1800hrs) :
                          </p>
                          <p className="quotation_description">
                            Working Rate - Normal Hours - (0600hrs-0900hrs)
                            -(1800hrs-2400hrs) :
                          </p>
                          <p className="quotation_description">
                            Working Rate - Normal Hours - (0000hrs-0600hrs) :
                          </p>
                        </div>
                      </div>
                      <div>
                        <h4 className="quotation_description_Heading">
                          Saturday/Sunday/Public Holidays
                        </h4>
                        <div className="quotation_description_container_box">
                          <p className="quotation_description">
                            Working Rate - Normal Hours - (0900hrs-1800hrs) :
                          </p>
                          <p className="quotation_description">
                            Working Rate - Normal Hours - (0600hrs-0900hrs)
                            -(1800hrs-2400hrs) :
                          </p>
                          <p className="quotation_description">
                            Working Rate - Normal Hours - (0000hrs-0600hrs) :
                          </p>
                        </div>
                      </div>
                    </div>
                    <div className="quotation_price_details_container">
                      <div className="quotation_price_details_container_box">
                        <p className="quotation_price">&nbsp;</p>
                        <p className="quotation_price">$ 200</p>
                        <p className="quotation_price">$ 200</p>
                        <p className="quotation_price">$ 200</p>
                      </div>
                      <div className="quotation_price_details_container_box">
                        <p className="quotation_price">&nbsp;</p>
                        <p className="quotation_price">$ 200</p>
                        <p className="quotation_price">$ 200</p>
                        <p className="quotation_price">$ 200</p>
                      </div>
                    </div>
                    <div className="quotation_price_total_container">
                      <p className="quotation_price_total">$ 1000</p>
                    </div>
                  </div>
                  <div className="quotation_container">
                    <div className="quotation_name_container">
                      <h3 className="quotation_name"> Spares</h3>
                    </div>
                    <div className="quotation_description_container">
                      <p className="quotation_description">
                        Item 1 Description :
                      </p>
                      <p className="quotation_description">
                        Item 2 Descritpion :
                      </p>
                      <p className="quotation_description">
                        Item 3 Descritpion :
                      </p>
                    </div>
                    <div className="quotation_price_details_container">
                      <p className="quotation_price">$ 200</p>
                      <p className="quotation_price">$ 200</p>
                      <p className="quotation_price">$ 200</p>
                    </div>
                    <div className="quotation_price_total_container">
                      <p className="quotation_price_total">$ 600</p>
                    </div>
                  </div>
                  <div className="quotation_container">
                    <div className="quotation_name_container">
                      <h3 className="quotation_name"> Micellaneous</h3>
                    </div>
                    <div className="quotation_description_container">
                      <p className="quotation_description">Port Charges :</p>
                      <p className="quotation_description">COVID Test :</p>
                      <p className="quotation_description">
                        Shipyard Charges :
                      </p>
                      <p className="quotation_description">Others :</p>
                    </div>
                    <div className="quotation_price_details_container">
                      <p className="quotation_price">$ 200</p>
                      <p className="quotation_price">$ 200</p>
                      <p className="quotation_price">$ 200</p>
                      <p className="quotation_price">$ 200</p>
                    </div>
                    <div className="quotation_price_total_container">
                      <p className="quotation_price_total">$ 600</p>
                    </div>
                  </div>
                  <div className="quotation_container">
                    <div className="quotation_name_container">
                      <h3 className="quotation_name"> TOTAL COST</h3>
                    </div>
                    <div className="quotation_description_container"></div>
                    <div className="quotation_price_details_container"></div>
                    <div className="quotation_price_total_container">
                      <p className="quotation_price_total">$ 3000</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default BidsAccepted;
