import React from "react";
import "./Chat.scss";
import OpenChatImg from "../../../../assets/images/Icon open-chat.svg";
import ChatIcon from "../../../../assets/images/ChatIcon.svg";
import viewAttachementsImg from "../../../../assets/images/Icon metro-attachment.svg";

function Chat() {
  return (
    <div className="chat_container">
      <div className="container">
        <div>
          <h2>
            Dashboard Latest Requests <span className="editprofile"> Chat</span>
          </h2>
          <h1>Chat with vendors</h1>
        </div>
        <div className="Request_card_container">
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
                    <h3 className="card_heading">IMO :</h3>
                    <p className="cardSubHeading">Lorem Ipsum</p>
                  </div>
                  <div>
                    <h3 className="card_heading">ETA :</h3>
                    <p className="cardSubHeading">Lorem Ipsum</p>
                  </div>
                  <div>
                    <h3 className="card_heading">ETD :</h3>
                    <p className="cardSubHeading">Lorem Ipsum</p>
                  </div>
                  <div>
                    <h3 className="card_heading">Category :</h3>
                    <p className="cardSubHeading">Lorem Ipsum</p>
                  </div>
                </div>
                <div className="block_container">
                  <div>
                    <h3 className="card_heading">Issue Type :</h3>
                    <p className="cardSubHeading">Lorem Ipsum</p>
                  </div>
                  <div>
                    <h3 className="card_heading">Model :</h3>
                    <p className="cardSubHeading">Lorem Ipsum</p>
                  </div>
                  <div>
                    <h3 className="card_heading">Equipment Name :</h3>
                    <p className="cardSubHeading">Lorem Ipsum</p>
                  </div>
                  <div>
                    <h3 className="card_heading">Manufacturer/OEM :</h3>
                    <p className="cardSubHeading">Lorem Ipsum</p>
                  </div>
                </div>
                <div className="block_container">
                  <div className="time_container_wraper">
                    <div>
                      <div className="time_container">
                        <h3 className="time_HH_MM">HH </h3>
                        <h3 className="time_HH_MM">MM</h3>
                      </div>
                    </div>

                    <div>
                      <div className="label_container">
                        <span className="label">05 </span>
                        <h2>:</h2>
                        <span className="label">13</span>
                      </div>
                    </div>
                  </div>
                  <div className="view_attachements_container">
                    <img
                      src={viewAttachementsImg}
                      alt="view attachements"
                      className="viewAttachements_img"
                    />
                    <a href="/#" className="view_attachements">
                      View Attachements
                    </a>
                  </div>
                </div>
              </div>
            </div>
            {/*  */}
          </div>
          <div className="chat_container">
            <h1>Chat History</h1>
            <div className="chat_box_container">
              <div className="chat_msg_container">
                <div className="vendor_message_box">
                  <span className="vendor_msg">
                    Lorem ipsum dolor sit amet, consectetur adipisicing elit.
                    Quia, nemo.
                  </span>
                </div>

                <div className="receiver_message_box">
                  <div className="receiver_msg_container">
                    <span className="receiver_msg">
                      Lorem ipsum dolor sit amet, consectetur adipisicing elit.
                      Quia, nemo.
                    </span>
                  </div>
                </div>
              </div>
              <div className="input_container">
                <input
                  type="text"
                  className="form_control_input"
                  name="chat_msg"
                  placeholder="Type your Message"
                />
                <img src={ChatIcon} alt="ChatIcon" className="chat_icon" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Chat;
