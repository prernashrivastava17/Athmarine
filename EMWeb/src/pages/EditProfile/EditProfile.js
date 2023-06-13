import React, { useEffect, useState } from "react";
import Sidebar from "../Dashboard/components/Sidebar/Sidebar";
import "./EditProfile.scss";
import profileImg from "../../assets/images/pngegg.png";
import editIcon from "./../../assets/images/edit-icon.png";
import showPasswordIcon from "./../../assets/images/show-password-icon.png";

import { useDispatch, useSelector } from "react-redux";
import { getProfile } from "../../store/dashboard/requestorDashboard/getProfile";
import Loading from "../../components/Loading/Loading";

const EditProfile = () => {
  const getProfileResponse = useSelector(
    (state) => state.getProfileReducer.state
  );
  const getProfileLoading = useSelector(
    (state) => state.getProfileReducer.isLoading
  );

  const dispatch = useDispatch();
  useEffect(() => {
    dispatch(getProfile({}, `customer/getRequesterProfile/10533`));
  }, [dispatch]);

  const initialState = {
    address: "",
    dob: "",
    email: "",
    id: null,
    imageUrl: "",
    name: "",
    password: "",
    phoneCode: "",
    primaryPhone: "",
  };
  const userProfileObject = useState(initialState);

  // const handleInputChange = (e) => {
  //   const { name, value } = e.target;

  //   switch (name) {
  //     case "name":
  //       if (!Number(e.nativeEvent.data)) setFormValues({ ...formValues, name: value });
  //       break;
  //   }
  // }

  const handleFormSubmission = () => {};

  return (
    <div className="editProfile_main_container">
      {getProfileLoading && <Loading text="Loading..." />}

      <Sidebar />
      <div className="editProfile_container">
        <div>
          <h2>
            Dashboard <span className="editprofile">Edit Profile</span>
          </h2>
          <h1>Edit Profile Details</h1>
        </div>
        <div className="card_container">
          <div className="inputBox_container">
            <div>
              <div className="input_container">
                <label htmlFor="" className="form_label">
                  Employee ID
                </label>
                <input
                  type="text"
                  className="form_control_input"
                  name="employee_id"
                  // onChange={(e) => handle}
                />
              </div>
              <div className="input_container">
                <label htmlFor="" className="form_label">
                  Name
                </label>
                <input
                  type="text"
                  className="form_control_input"
                  name="turnover"
                />
              </div>
              <div className="input_container">
                <label htmlFor="" className="form_label">
                  Email
                </label>
                <input
                  type="text"
                  className="form_control_input"
                  name="turnover"
                />
              </div>
              <div className="input_container">
                <label htmlFor="" className="form_label">
                  DOB
                </label>
                <input
                  type="text"
                  className="form_control_input"
                  name="turnover"
                />
              </div>
              <div className="input_container">
                <label htmlFor="" className="form_label">
                  Mobile No.
                </label>
                <input
                  type="text"
                  className="form_control_input"
                  name="turnover"
                />
              </div>
              <hr className="horizontal_line" />
              <div className="input_container">
                <label htmlFor="" className="form_label">
                  Address line 1
                </label>
                <input
                  type="text"
                  className="form_control_input"
                  name="turnover"
                />
              </div>
              <div className="input_container">
                <label htmlFor="" className="form_label">
                  Address line 2
                </label>
                <input
                  type="text"
                  className="form_control_input"
                  name="turnover"
                />
              </div>
              <div className="input_container">
                <label htmlFor="" className="form_label">
                  Address line 3
                </label>
                <input
                  type="text"
                  className="form_control_input"
                  name="turnover"
                />
              </div>
              <hr className="horizontal_line" />
              <div className="input_container">
                <label htmlFor="" className="form_label">
                  Change Password
                </label>
                <input
                  type="text"
                  className="form_control_input"
                  name="turnover"
                />
                <div className="show_password_container">
                  <img src={showPasswordIcon} alt="show password" />
                </div>
              </div>
              <div className="input_container">
                <label htmlFor="" className="form_label">
                  Confirm Password
                </label>
                <input
                  type="text"
                  className="form_control_input"
                  name="turnover"
                />
                <div className="show_password_container">
                  <img src={showPasswordIcon} alt="show password" />
                </div>
              </div>
            </div>
            <div className="change_profile_container">
              <div className="img_container">
                <img
                  src="https://raw.githubusercontent.com/flaatpixels/Notifications-card-animation/master/img/avatar_1.jpg"
                  alt="profile"
                />
                <div className="edit_profile_option">
                  <img className="edit_icon" src={editIcon} alt="edit" />
                </div>
              </div>
              <h2 className="profile_heading">Upload Image</h2>
            </div>
          </div>
        </div>
        <div className="form_submit_container">
          <input type="submit" value="Update" className="btn update_btn" />
          <input type="button" value="Cancel" className="btn btn_cancel_btn" />
        </div>
      </div>
    </div>
  );
};

export default EditProfile;
