import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import logo from "./../../../assets/images/logo.png";
import "./PromotionDetails.scss";
import Flatpickr from "react-flatpickr";
import "flatpickr/dist/themes/material_blue.css";
import { getPromotionDetails } from "../../../store/common/promotionsDetails";
import calendarIcon from "./../../../assets/images/Icon_calendar.png";
import logout_arrow from "./../../../assets/images/log_out.png";
import edit from "./../../../assets/images/edit.png";
import moment from "moment";
import {
  promotionDetailsPost,
  promotionDetailsPostActions,
} from "../../../store/common/promotionDetailsPost";

const PromotionDetails = ({ details }) => {
  const dispatch = useDispatch();

  const promotionDetailsRes = useSelector(
    (state) => state.getPromotionDetailsReducer.promotionDetailsRes
  );
  const promotionDetailsOnLoading = useSelector(
    (state) => state.getPromotionDetailsReducer.isLoading
  );
  const promotionDetailsOnError = useSelector(
    (state) => state.getPromotionDetailsReducer.isError
  );

  const [showCal, setShowCal] = useState(false);
  const [showEdit, setShowEdit] = useState(false);
  const [promotionData, setPromotionData] = useState("");

  useEffect(() => {
    dispatch(getPromotionDetails());
    setPromotionData(promotionDetailsRes);
  }, [dispatch]);

  useEffect(() => {
    if (promotionDetailsRes !== null) {
      setPromotionData(promotionDetailsRes);
    }
  }, [promotionDetailsRes]);

  const handleStartedDate = (index, date) => {
    let allPromotionData = [...promotionData];
    const desiredDate = moment(date[0]).format("YYYY-MM-DD").toString();
    const atDesiredIndex = index;

    if (atDesiredIndex === 0) {
      if (
        new Date(`${desiredDate}`) <
        new Date(`${allPromotionData[0][`endDate`]}`)
      ) {
        allPromotionData[0] = {
          ...promotionData[0],
          startedDate: moment(date[0]).format("YYYY-MM-DD").toString(),
        };
      } else {
        allPromotionData[0] = {
          ...promotionData[0],
          endDate: ``,
        };

        allPromotionData[1] = {
          ...promotionData[1],
          startedDate: ``,
          endDate: ``,
        };

        allPromotionData[2] = {
          ...promotionData[2],
          startedDate: ``,
          endDate: ``,
        };
      }
    }

    if (atDesiredIndex === 1) {
      if (
        new Date(`${desiredDate}`) <
        new Date(`${allPromotionData[1][`endDate`]}`)
      ) {
        allPromotionData[1] = {
          ...promotionData[1],
          startedDate: moment(date[0]).format("YYYY-MM-DD").toString(),
        };
      } else {
        allPromotionData[1] = {
          ...promotionData[1],
          endDate: ``,
        };

        allPromotionData[2] = {
          ...promotionData[2],
          startedDate: ``,
          endDate: ``,
        };
      }
    }

    setPromotionData(allPromotionData);
  };

  const handleEndedDate = (index, date) => {
    let allPromotionData = [...promotionData];
    const desiredDate = moment(date[0]).format("YYYY-MM-DD").toString();
    const atDesiredIndex = index;

    if (atDesiredIndex === 0) {
      if (
        new Date(`${allPromotionData[0][`startedDate`]}`) <
        new Date(`${desiredDate}`)
      ) {
        allPromotionData[0] = {
          ...promotionData[0],
          endDate: moment(date[0]).format("YYYY-MM-DD").toString(),
        };

        // add new date at below row started date with addition of next date
        allPromotionData[1] = {
          ...promotionData[1],
          startedDate: moment(date[0], "YYYY-MM-DD").add(1, "days"),
        };
        console.log(
          new Date(`${desiredDate}`),
          `${allPromotionData[1][`endDate`]}`,
          new Date(`${desiredDate}`) > `${allPromotionData[1][`endDate`]}`
        );

        if (
          new Date(`${desiredDate}`) >
          new Date(`${allPromotionData[1][`endDate`]}`)
        ) {
          allPromotionData[1] = {
            ...promotionData[1],
            endDate: ``,
          };

          allPromotionData[2] = {
            ...promotionData[2],
            startedDate: ``,
            endDate: ``,
          };
        }
      } else {
        allPromotionData[0] = {
          ...promotionData[0],
          endDate: moment(date[0]).format("YYYY-MM-DD").toString(),
          startedDate: ``,
        };
      }
    }

    if (atDesiredIndex === 1) {
      if (
        new Date(`${allPromotionData[1][`startedDate`]}`) <
        new Date(`${desiredDate}`)
      ) {
        allPromotionData[1] = {
          ...promotionData[1],
          endDate: moment(date[0]).format("YYYY-MM-DD").toString(),
        };

        // add new date at below row started date with addition of next date
        allPromotionData[2] = {
          ...promotionData[2],
          startedDate: moment(date[0], "YYYY-MM-DD").add(1, "days"),
        };

        if (
          new Date(`${desiredDate}`) >
          new Date(`${allPromotionData[2][`endDate`]}`)
        ) {
          allPromotionData[2] = {
            ...promotionData[2],
            endDate: ``,
          };
        }
      } else {
        allPromotionData[1] = {
          ...promotionData[1],
          endDate: moment(date[0]).format("YYYY-MM-DD").toString(),
          startedDate: ``,
        };
      }
    }

    if (atDesiredIndex === 2) {
      if (
        new Date(`${allPromotionData[2][`startedDate`]}`) <
        new Date(`${desiredDate}`)
      ) {
        allPromotionData[2] = {
          ...promotionData[2],
          endDate: moment(date[0]).format("YYYY-MM-DD").toString(),
        };
      } else {
        allPromotionData[2] = {
          ...promotionData[2],
          startedDate: ``,
        };
      }
    }

    setPromotionData(allPromotionData);
  };

  const savePromotionData = () => {
    setShowEdit(false);
    dispatch(
      promotionDetailsPost(
        promotionData,
        "masterPromotion/updateMasterPromotion"
      )
    );
  };

  return (
    <>
      <div className="container__promotion">
        <div className="header">
          <div className="header__left">
            <img className="logo" src={logo} alt="logo" />
          </div>
          <div className="header__right">
            <div
              className="logout"
              onClick={() => details({ signIn: true, promotion: false })}
            >
              <button className="btn__logout">
                <span className="logout_arrow">
                  <img src={logout_arrow} alt="calendar-icon" />
                </span>
                Logout
              </button>
            </div>
          </div>
        </div>
        <div className={showEdit ? "edit__active" : "edit"}>
          <button className="btn__edit" onClick={() => setShowEdit(true)}>
            Edit
            <span className="edit_arrow">
              <img src={edit} alt="calendar-icon" />
            </span>
          </button>
        </div>
        <div className="promotion__table">
          <table className="table">
            <thead>
              <td>S.no</td>
              <td>DATE</td>
              <td>PAY</td>
              <td>VOUCHER</td>
            </thead>
            <tbody>
              <tr>
                <td></td>
                <td width="500px">
                  <table width="75%">
                    <tbody>
                      <tr>
                        <td>From</td>
                        <td>To</td>
                      </tr>
                    </tbody>
                  </table>
                </td>

                <td></td>
                <td></td>
              </tr>
              {promotionData &&
                promotionData.map((item, index) => {
                  return [
                    <tr key={index}>
                      <td>{item?.id}</td>
                      <td>
                        <table className="inner_table">
                          <tr>
                            {showEdit ? (
                              <td>
                                <Flatpickr
                                  className="form_control_flatpickr"
                                  options={{
                                    // maxDate: "today",
                                    minDate: "1-1-1950",
                                    altInput: true,
                                    altFormat: "M j, Y",
                                    dateFormat: "Y-m-d",
                                  }}
                                  value={moment(item?.startedDate)
                                    .format("YYYY-MM-DD")
                                    .toString()}
                                  onChange={(date) =>
                                    handleStartedDate(index, date)
                                  }
                                />
                                {showEdit && (
                                  <span
                                    className="icon_container first"
                                    onClick={() => setShowCal(!showCal)}
                                  >
                                    <img
                                      src={calendarIcon}
                                      alt="calendar-icon"
                                    />
                                  </span>
                                )}
                              </td>
                            ) : (
                              <td className="form_control_startdate">
                                {moment(item?.startedDate).format(
                                  "DD MMM YYYY"
                                )}
                              </td>
                            )}

                            {showEdit ? (
                              <td>
                                <Flatpickr
                                  className="form_control_flatpickr"
                                  options={{
                                    // maxDate: "today",
                                    minDate: "1-1-1950",
                                    altInput: true,
                                    altFormat: "M j, Y",
                                    dateFormat: "Y-m-d",
                                  }}
                                  value={moment(item?.endDate)
                                    .format("YYYY-MM-DD")
                                    .toString()}
                                  onChange={(date) =>
                                    handleEndedDate(index, date)
                                  }
                                />
                                {showEdit && (
                                  <span
                                    className="icon_container second"
                                    onClick={() => setShowCal(!showCal)}
                                  >
                                    <img
                                      src={calendarIcon}
                                      alt="calendar-icon"
                                    />
                                  </span>
                                )}
                              </td>
                            ) : (
                              <td className="form_control_enddate">
                                {moment(item?.endDate).format("DD MMM YYYY")}
                              </td>
                            )}
                          </tr>
                        </table>
                      </td>
                      <td>{item?.payMoney}</td>
                      <td>{item?.voucher}</td>
                    </tr>,
                  ];
                })}
            </tbody>
          </table>
        </div>
        {showEdit && (
          <div className="save">
            <button className="btn__edit" onClick={() => savePromotionData()}>
              Save
            </button>
          </div>
        )}
      </div>
    </>
  );
};

export default PromotionDetails;
