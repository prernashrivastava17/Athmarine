import React from "react";
import "./LatestRequest.scss";
import AwesomeSlider from "react-awesome-slider";
import OpenChatImg from "../../../../assets/images/Icon open-chat.svg";
function LatestRequest(props) {
  const { setCurrentScreen } = props;

  const openChatScreenFunc = () => {
    setCurrentScreen("Chat");
  };

  return (
    <div className="latest_request_container">
      <div className="bredcrum">
        &#x2B9E; Dashboard
        <span className="active_page"> &#x2B9E; Latest Requests</span>
      </div>
      <div className="main_heading">Latest Requests</div>

      <div className="mobile_responsive_view">
        <div className="latest_requests">
          <AwesomeSlider className="aws-btn">
            <div className="latest_request">
              <div className="col col_1">
                <div className="item">
                  <div className="heading">Vessel :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">Country :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">State :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">Port :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
              </div>
              <div className="col col_2">
                <div className="item">
                  <div className="heading">IMO :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">ETA :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">ETD :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">Category :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
              </div>
              <div className="col col_3">
                <div className="time_n_chat_container">
                  <div className="time_container">
                    <div className="time_area">
                      <div className="row_1">
                        <span className="hh">HH</span>
                        <span className="mm">MM</span>
                      </div>
                      <div className="row_2">
                        <span className="hours">05</span>
                        <span className="separator">:</span>
                        <span className="minutes">13</span>
                      </div>
                    </div>
                  </div>
                  <div className="Chat_btn_container">
                    <button
                      type="submit"
                      value="Chat"
                      className="btn update_btn"
                    >
                      <img className="chat_icon" src={OpenChatImg} alt="" />
                      <span className="chat_icon"> Chat</span>
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <div className="latest_request">
              <div className="col col_1">
                <div className="item">
                  <div className="heading">Vessel :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">Country :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">State :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">Port :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
              </div>
              <div className="col col_2">
                <div className="item">
                  <div className="heading">IMO :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">ETA :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">ETD :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">Category :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
              </div>
              <div className="col col_3">
                <div className="time_n_chat_container">
                  <div className="time_container">
                    <div className="time_area">
                      <div className="row_1">
                        <span className="hh">HH</span>
                        <span className="mm">MM</span>
                      </div>
                      <div className="row_2">
                        <span className="hours">05</span>
                        <span className="separator">:</span>
                        <span className="minutes">13</span>
                      </div>
                    </div>
                  </div>
                  <div className="Chat_btn_container">
                    <button
                      type="submit"
                      value="Chat"
                      className="btn update_btn"
                    >
                      <img className="chat_icon" src={OpenChatImg} alt="" />
                      <span className="chat_icon"> Chat</span>
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <div className="latest_request">
              <div className="col col_1">
                <div className="item">
                  <div className="heading">Vessel :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">Country :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">State :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">Port :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
              </div>
              <div className="col col_2">
                <div className="item">
                  <div className="heading">IMO :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">ETA :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">ETD :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">Category :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
              </div>
              <div className="col col_3">
                <div className="time_n_chat_container">
                  <div className="time_container">
                    <div className="time_area">
                      <div className="row_1">
                        <span className="hh">HH</span>
                        <span className="mm">MM</span>
                      </div>
                      <div className="row_2">
                        <span className="hours">05</span>
                        <span className="separator">:</span>
                        <span className="minutes">13</span>
                      </div>
                    </div>
                  </div>
                  <div className="Chat_btn_container">
                    <button
                      type="submit"
                      value="Chat"
                      className="btn update_btn"
                    >
                      <img className="chat_icon" src={OpenChatImg} alt="" />
                      <span className="chat_icon"> Chat</span>
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <div className="latest_request">
              <div className="col col_1">
                <div className="item">
                  <div className="heading">Vessel :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">Country :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">State :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">Port :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
              </div>
              <div className="col col_2">
                <div className="item">
                  <div className="heading">IMO :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">ETA :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">ETD :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">Category :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
              </div>
              <div className="col col_3">
                <div className="time_n_chat_container">
                  <div className="time_container">
                    <div className="time_area">
                      <div className="row_1">
                        <span className="hh">HH</span>
                        <span className="mm">MM</span>
                      </div>
                      <div className="row_2">
                        <span className="hours">05</span>
                        <span className="separator">:</span>
                        <span className="minutes">13</span>
                      </div>
                    </div>
                  </div>
                  <div className="Chat_btn_container">
                    <button
                      type="submit"
                      value="Chat"
                      className="btn update_btn"
                    >
                      <img className="chat_icon" src={OpenChatImg} alt="" />
                      <span className="chat_icon"> Chat</span>
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <div className="latest_request">
              <div className="col col_1">
                <div className="item">
                  <div className="heading">Vessel :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">Country :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">State :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">Port :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
              </div>
              <div className="col col_2">
                <div className="item">
                  <div className="heading">IMO :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">ETA :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">ETD :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
                <div className="item">
                  <div className="heading">Category :</div>
                  <div className="text">Lorem Ipsum</div>
                </div>
              </div>
              <div className="col col_3">
                <div className="time_n_chat_container">
                  <div className="time_container">
                    <div className="time_area">
                      <div className="row_1">
                        <span className="hh">HH</span>
                        <span className="mm">MM</span>
                      </div>
                      <div className="row_2">
                        <span className="hours">05</span>
                        <span className="separator">:</span>
                        <span className="minutes">13</span>
                      </div>
                    </div>
                  </div>
                  <div className="Chat_btn_container">
                    <button
                      type="submit"
                      value="Chat"
                      className="btn update_btn"
                    >
                      <img className="chat_icon" src={OpenChatImg} alt="" />
                      <span className="chat_icon"> Chat</span>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </AwesomeSlider>
        </div>
      </div>

      <div className="desktop_responsive_view">
        <div className="latest_requests">
          <div className="latest_request">
            <div className="col col_1">
              <div className="item">
                <div className="heading">Vessel :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">Country :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">State :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">Port :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
            </div>
            <div className="col col_2">
              <div className="item">
                <div className="heading">IMO :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">ETA :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">ETD :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">Category :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
            </div>
            <div className="col col_3">
              <div className="time_n_chat_container">
                <div className="time_container">
                  <div className="time_area">
                    <div className="row_1">
                      <span className="hh">HH</span>
                      <span className="mm">MM</span>
                    </div>
                    <div className="row_2">
                      <span className="hours">05</span>
                      <span className="separator">:</span>
                      <span className="minutes">13</span>
                    </div>
                  </div>
                </div>
                <div className="Chat_btn_container">
                  <button type="submit" value="Chat" className="btn update_btn">
                    <img className="chat_icon" src={OpenChatImg} alt="" />
                    <span className="chat_icon"> Chat</span>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div className="latest_request">
            <div className="col col_1">
              <div className="item">
                <div className="heading">Vessel :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">Country :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">State :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">Port :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
            </div>
            <div className="col col_2">
              <div className="item">
                <div className="heading">IMO :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">ETA :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">ETD :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">Category :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
            </div>
            <div className="col col_3">
              <div className="time_n_chat_container">
                <div className="time_container">
                  <div className="time_area">
                    <div className="row_1">
                      <span className="hh">HH</span>
                      <span className="mm">MM</span>
                    </div>
                    <div className="row_2">
                      <span className="hours">05</span>
                      <span className="separator">:</span>
                      <span className="minutes">13</span>
                    </div>
                  </div>
                </div>
                <div className="Chat_btn_container">
                  <button type="submit" value="Chat" className="btn update_btn">
                    <img className="chat_icon" src={OpenChatImg} alt="" />
                    <span className="chat_icon"> Chat</span>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div className="latest_request">
            <div className="col col_1">
              <div className="item">
                <div className="heading">Vessel :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">Country :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">State :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">Port :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
            </div>
            <div className="col col_2">
              <div className="item">
                <div className="heading">IMO :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">ETA :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">ETD :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">Category :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
            </div>
            <div className="col col_3">
              <div className="time_n_chat_container">
                <div className="time_container">
                  <div className="time_area">
                    <div className="row_1">
                      <span className="hh">HH</span>
                      <span className="mm">MM</span>
                    </div>
                    <div className="row_2">
                      <span className="hours">05</span>
                      <span className="separator">:</span>
                      <span className="minutes">13</span>
                    </div>
                  </div>
                </div>
                <div className="Chat_btn_container">
                  <button type="submit" value="Chat" className="btn update_btn">
                    <img className="chat_icon" src={OpenChatImg} alt="" />
                    <span className="chat_icon"> Chat</span>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div className="latest_request">
            <div className="col col_1">
              <div className="item">
                <div className="heading">Vessel :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">Country :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">State :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">Port :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
            </div>
            <div className="col col_2">
              <div className="item">
                <div className="heading">IMO :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">ETA :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">ETD :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">Category :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
            </div>
            <div className="col col_3">
              <div className="time_n_chat_container">
                <div className="time_container">
                  <div className="time_area">
                    <div className="row_1">
                      <span className="hh">HH</span>
                      <span className="mm">MM</span>
                    </div>
                    <div className="row_2">
                      <span className="hours">05</span>
                      <span className="separator">:</span>
                      <span className="minutes">13</span>
                    </div>
                  </div>
                </div>
                <div className="Chat_btn_container">
                  <button type="submit" value="Chat" className="btn update_btn">
                    <img className="chat_icon" src={OpenChatImg} alt="" />
                    <span className="chat_icon"> Chat</span>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div className="latest_request">
            <div className="col col_1">
              <div className="item">
                <div className="heading">Vessel :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">Country :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">State :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">Port :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
            </div>
            <div className="col col_2">
              <div className="item">
                <div className="heading">IMO :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">ETA :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">ETD :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
              <div className="item">
                <div className="heading">Category :</div>
                <div className="text">Lorem Ipsum</div>
              </div>
            </div>
            <div className="col col_3">
              <div className="time_n_chat_container">
                <div className="time_container">
                  <div className="time_area">
                    <div className="row_1">
                      <span className="hh">HH</span>
                      <span className="mm">MM</span>
                    </div>
                    <div className="row_2">
                      <span className="hours">05</span>
                      <span className="separator">:</span>
                      <span className="minutes">13</span>
                    </div>
                  </div>
                </div>
                <div className="Chat_btn_container">
                  <button type="submit" value="Chat" className="btn update_btn">
                    <img className="chat_icon" src={OpenChatImg} alt="" />
                    <span className="chat_icon"> Chat</span>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default LatestRequest;
