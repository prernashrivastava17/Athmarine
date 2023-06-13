import React, { useState, useEffect } from "react";
import Sidebar from "../Sidebar/Sidebar";
import "./dashboard.scss";
import chatBotImage from "../../../../assets/images/undraw_Chat_bot_re_e2gj.svg";
import { useDispatch, useSelector } from "react-redux";

import addIcon from "../../../../assets/images/Icon feather-plus-circle.svg";
import gridViewImage from "../../../../assets/images/Icon feather-grid.svg";
import listViewImage from "../../../../assets/images/Icon awesome-list-ul.svg";
import locationImg from "../../../../assets/images/Icon material-location-on.svg";
import calendarImg from "../../../../assets/images/Icon awesome-calendar-alt.svg";
import { getRequestorMainFunc } from "../../../../store/dashboard/requestorDashboard/mainDashboard";
import { getRequestorId } from "../../../../config";

function Dashboard(props) {
  const dispatch = useDispatch();

  const dashboardResponse = useSelector(
    (state) => state.getRequestorMainReducer.state
  );
  const dashboardLoading = useSelector(
    (state) => state.getRequestorMainReducer.isLoading
  );

  useEffect(() => {
    dispatch(
      getRequestorMainFunc({}, `/dashboard/requester/${getRequestorId()}`)
    );
  }, [dispatch]);

  useEffect(() => {}, [dashboardResponse]);

  const { setCurrentScreen } = props;

  const openAddNewRequestScreen = () => {
    setCurrentScreen("AddNewRequest");
  };

  // for list view and grid view
  const [listView, setListView] = useState(true);
  const [gridView, setGridView] = useState(true);

  const handleView = () => {
    if (listView) {
      setGridView(true);
      setListView(false);
    } else {
      setGridView(false);
      setListView(true);
    }
  };

  return (
    <div className="main_container_area">
      <div className="dashboard_component">
        <h4>Dashboard</h4>
        <div className="dashboard_container " style={{ alignItems: "center" }}>
          <div>
            <h1 className="dashboard_header">Dashboard</h1>
          </div>
          <div>
            <button
              className="add_request_btn"
              onClick={openAddNewRequestScreen}
            >
              <img src={addIcon} alt="Add New Request" />
              Add New Request
            </button>
          </div>
        </div>
        <div className="dashboard_container">
          <div className="box_wraper">
            <div className="dashboard_container_wraper">
              <div className="box_container">
                <div className="box">
                  <h1 className="box_number">
                    {dashboardResponse?.openRequest}
                  </h1>
                  <h1 className="box_text">Open Request &amp; Status</h1>
                </div>
              </div>
              <div className="box_container">
                <div className="box">
                  <h1 className="box_number">
                    {dashboardResponse?.bidsReceived}
                  </h1>
                  <h1 className="box_text">
                    Bids <br /> Received
                  </h1>
                </div>
              </div>
              <div className="box_container">
                <div className="box">
                  <h1 className="box_number">
                    {dashboardResponse?.pendingForApprovalRequest}
                  </h1>
                  <h1 className="box_text">
                    Approved/ Pending for Approval Request
                  </h1>
                </div>
              </div>
              <div className="box_container">
                <div className="box">
                  <h1 className="box_number">
                    {dashboardResponse?.completedJobs}
                  </h1>
                  <h1 className="box_text">Completed Jobs</h1>
                </div>
              </div>
              <div className="chat-with-vendor-container">
                <div className="chat_bot_text_container">
                  <h1>
                    Chat With <br />
                    Vendor
                  </h1>
                </div>
                <div className="chat_bot_img_container">
                  <img
                    src={chatBotImage}
                    alt="chatBot"
                    className="chat_bot_img"
                  />
                </div>
              </div>
            </div>
          </div>
          <div className="notification_card">
            <div className="notification_heading">
              <h2>Notification</h2>
              <a href="#a" className="link">
                clear all
              </a>
            </div>
            <div className="wrapper">
              <div className="notifications">
                <div className="notifications__item">
                  <div className="notifications__item__avatar">
                    <img
                      src="https://github.com/Flat-Pixels/Notifications-card-animation/raw/master/img/avatar_1.jpg"
                      alt="notification icon"
                    />
                  </div>

                  <div className="notifications__item__content">
                    <span className="notifications__item__message">
                      Lorem ipsum dolor sit amet consectetur adipisicing elit.
                      Nihil minus, iste officia ullam vel .
                    </span>
                    <p className="notifications__item__message time_slot">
                      Time:11:20AM
                    </p>
                  </div>
                </div>
                <div className="notifications__item">
                  <div className="notifications__item__avatar">
                    <img
                      src="https://github.com/Flat-Pixels/Notifications-card-animation/raw/master/img/avatar_1.jpg"
                      alt="notification Icon"
                    />
                  </div>

                  <div className="notifications__item__content">
                    <span className="notifications__item__message">
                      Lorem ipsum dolor sit amet consectetur adipisicing elit.
                      Nihil minus, iste officia ullam vel .
                    </span>
                    <p className="notifications__item__message time_slot">
                      Time:11:20AM
                    </p>
                  </div>
                </div>
                <div className="notifications__item">
                  <div className="notifications__item__avatar">
                    <img
                      src="https://github.com/Flat-Pixels/Notifications-card-animation/raw/master/img/avatar_1.jpg"
                      alt="notification Icon"
                    />
                  </div>

                  <div className="notifications__item__content">
                    <span className="notifications__item__message">
                      Lorem ipsum dolor sit amet consectetur adipisicing elit.
                      Nihil minus, iste officia ullam vel .
                    </span>
                    <p className="notifications__item__message time_slot">
                      Time:11:20AM
                    </p>
                  </div>
                </div>
                <div className="notifications__item">
                  <div className="notifications__item__avatar">
                    <img
                      src="https://github.com/Flat-Pixels/Notifications-card-animation/raw/master/img/avatar_1.jpg"
                      alt="notification Icon"
                    />
                  </div>

                  <div className="notifications__item__content">
                    <span className="notifications__item__message">
                      Lorem ipsum dolor sit amet consectetur adipisicing elit.
                      Nihil minus, iste officia ullam vel .
                    </span>
                    <p className="notifications__item__message time_slot">
                      Time:11:20AM
                    </p>
                  </div>
                </div>
                <div className="notifications__item">
                  <div className="notifications__item__avatar">
                    <img
                      src="https://github.com/Flat-Pixels/Notifications-card-animation/raw/master/img/avatar_1.jpg"
                      alt="notification Icon"
                    />
                  </div>

                  <div className="notifications__item__content">
                    <span className="notifications__item__message">
                      Lorem ipsum dolor sit amet consectetur adipisicing elit.
                      Nihil minus, iste officia ullam vel .
                    </span>
                    <p className="notifications__item__message time_slot">
                      Time:11:20AM
                    </p>
                  </div>
                </div>
                <div className="notifications__item">
                  <div className="notifications__item__avatar">
                    <img
                      src="https://github.com/Flat-Pixels/Notifications-card-animation/raw/master/img/avatar_1.jpg"
                      alt="notification Icon"
                    />
                  </div>

                  <div className="notifications__item__content">
                    <span className="notifications__item__message">
                      Lorem ipsum dolor sit amet consectetur adipisicing elit.
                      Nihil minus, iste officia ullam vel .
                    </span>
                    <p className="notifications__item__message time_slot">
                      Time:11:20AM
                    </p>
                  </div>
                </div>
                <div className="notifications__item">
                  <div className="notifications__item__avatar">
                    <img
                      src="https://github.com/Flat-Pixels/Notifications-card-animation/raw/master/img/avatar_1.jpg"
                      alt="notification Icon"
                    />
                  </div>

                  <div className="notifications__item__content">
                    <span className="notifications__item__message">
                      Lorem ipsum dolor sit amet consectetur adipisicing elit.
                      Nihil minus, iste officia ullam vel .
                    </span>
                    <p className="notifications__item__message time_slot">
                      Time:11:20AM
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div>
          <div className="latest_request_container">
            <h1>Latest Requests</h1>
            <div className="request_container_box">
              <img
                src={gridViewImage}
                alt=""
                className="gridViewImgClass"
                onClick={handleView}
              />
              <img
                src={listViewImage}
                alt=""
                className="listViewImgClass"
                onClick={handleView}
              />
            </div>
          </div>
          {listView ? (
            <div>
              <table style={{ width: "100%" }}>
                <tr>
                  <th>S.No</th>
                  <th>Ship Name</th>
                  <th>Port</th>
                  <th>Date</th>
                  <th>Status</th>
                  <th>Issue</th>
                </tr>
                <tr>
                  <td>01</td>
                  <td>Black Pearl Pvt Ltd.</td>
                  <td>Kandla port</td>
                  <td>Thursday , 20-05-2021</td>
                  <td className="under_process">under Process</td>
                </tr>
                <tr>
                  <td>02</td>
                  <td>Black Pearl Pvt Ltd.</td>
                  <td>Kandla port</td>
                  <td>Thursday , 20-05-2021</td>
                  <td className="received">Received</td>
                </tr>
                <tr>
                  <td>03</td>
                  <td>Black Pearl Pvt Ltd.</td>
                  <td>Kandla port</td>
                  <td>Thursday , 20-05-2021</td>
                  <td className="unaccepted">Unaccepted</td>
                </tr>
                <tr>
                  <td>04</td>
                  <td>Black Pearl Pvt Ltd.</td>
                  <td>Kandla port</td>
                  <td>Thursday , 20-05-2021</td>
                  <td className="under_process">under Process</td>
                </tr>
              </table>
            </div>
          ) : (
            <div className="a_latest_request_container">
              {dashboardResponse?.latestRequest.map((request) => (
                <div className="a_latest_request">
                  <h1 className="request_heading">
                    {request?.vessel?.shipname}
                  </h1>
                  <div className="a_location">
                    <img src={locationImg} alt="" />
                    <p> {request?.port?.name} </p>
                  </div>
                  <div className="a_date">
                    <img src={calendarImg} alt="" />
                    <p> {request?.eta} </p>
                  </div>
                  <button className="request_status_btn under_process">
                    {request?.requestStatus}
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
