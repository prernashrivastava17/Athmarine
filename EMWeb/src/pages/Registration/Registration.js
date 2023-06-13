import React, { useState, useEffect } from "react";
import { useHistory } from "react-router-dom";

import "./Registration.scss";
import moment from "moment";
import Marquee from "react-fast-marquee";
import { useDispatch, useSelector } from "react-redux";
import Sidebar from "./components/Sidebar/Sidebar";
import Details from "./components/Forms/Details/Details";
import VerifyEmail from "./components/Forms/VerifyEmail/VerifyEmail";
import VerifyMobile from "./components/Forms/VerifyMobile/VerifyMobile";
import Address from "./components/Forms/Address/Address";
import Bidder from "./components/Forms/Bidder/Bidder";
import Approver from "./components/Forms/Approver/Approver";
import Engineer from "./components/Forms/Engineer/Engineer";
import BusinessInformation from "./components/Forms/BusinessInformation/BusinessInformation";
import Services from "./components/Forms/Services/Services";
import Finance from "./components/Forms/Finance/Finance";
import CompanyHead from "./components/Forms/CompanyHead/CompanyHead";
import ManagingHead from "./components/Forms/ManagingHead/ManagingHead";
import Invoice from "./components/Forms/Invoice/invoice";
import { saveAddressActions } from "./../../store/vendorRegistration/address";
import { saveDetailActions } from "../../store/vendorRegistration/details";
import { saveCompanyHeadSlice as saveCompanyHeadActions } from "../../store/vendorRegistration/companyHead";
import { saveBusinessInformationActions } from "../../store/vendorRegistration/businessInformation";
import { saveBidderSlice } from "../../store/vendorRegistration/bidder";
import { saveApproverSlice } from "../../store/vendorRegistration/approver";
import createServiceSlice from "../../store/vendorRegistration/createService";
import { saveManagingHeadSlice } from "../../store/vendorRegistration/managingHead";
import { commonGetApiActions } from "./../../store/common/commonGetApi";
import { saveFinancerSlice } from "./../../store/vendorRegistration/assignedFinancer";
import { createServiceActions } from "./../../store/vendorRegistration/createService";
import { saveEngineerSlice } from "../../store/vendorRegistration/vendorEngineer";
import { getPromotionDetails } from "../../store/common/promotionsDetails";
import Payment from "./components/Forms/Payment/Payment";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const Registration = (props) => {
  const history = useHistory();
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

  const detailsResponse = useSelector((state) => state.detailsReducer.details);
  const companyHeadRes = useSelector(
    (state) => state.vendorCompanyHead.companyHeadObject
  );
  const businessInfoResponse = useSelector(
    (state) => state.businessInformation.businessInformationRes
  );
  const biddersResponseObject = useSelector(
    (state) => state.vendorBidder.bidder
  );
  const approverResponse = useSelector(
    (state) => state.vendorApprover.approverRes
  );
  const vendorEngineer_Response = useSelector(
    (state) => state.vendorEngineerRes.engineer
  );
  const managingHeadRes = useSelector(
    (state) => state.managingHeadReducer.managingHeadRes
  );
  const createServiceRes = useSelector(
    (state) => state.createServiceReducer.createServiceRes
  );
  const registrationStatusKey = useSelector(
    (state) => state.commonGetApiReducer.registrationStatusKey
  );
  const paymentRequestRes = useSelector(
    (state) => state.paymentRequestReducer.state
  );
  const authResponse = useSelector((state) => state.auth.authRes);

  if (props.userType === "") {
    history.push("/");
  }
  const [currentForm, setCurrentForm] = useState("address");
  const [currentActiveMenu, setCurrentActiveMenu] = useState(1);
  const [promotionFlag, setPromotionFlag] = useState(null);

  const updateFormStatus = (formLabel, count) => {
    setCurrentForm(formLabel);
    setCurrentActiveMenu(count);
  };

  useEffect(() => {
    dispatch(getPromotionDetails());
  }, [dispatch]);

  const navigationHandler = (props) => {
    const { currentForm } = props;

    if (registrationStatusKey !== null) {
      dispatch(commonGetApiActions.dispatchGetRequest());
    }
    if (currentForm === "address") {
      dispatch(commonGetApiActions.emptyError());
      dispatch(saveAddressActions.addressNavigation(false));
      setCurrentForm("address");
    }
    if (currentForm === "details") {
      if (detailsResponse) {
        setCurrentForm("details");
        dispatch(saveDetailActions.detailsNavigation(false));
      }

      if (registrationStatusKey - 1 >= 0) {
        setCurrentForm("details");
        dispatch(saveDetailActions.detailsNavigation(false));
      }
    }

    if (currentForm === "companyHead") {
      if (companyHeadRes) {
        setCurrentForm("companyHead");
        dispatch(saveCompanyHeadActions.companyHeadNavigationPass(false));
      }

      if (registrationStatusKey - 2 >= 0) {
        setCurrentForm("companyHead");
        dispatch(saveCompanyHeadActions.companyHeadNavigationPass(false));
      }
    }

    if (currentForm === "verifyEmail") {
      // do nothing
    }
    if (currentForm === "businessInformation") {
      if (authResponse?.type === "Individual") {
        setCurrentForm("businessInformation");
        dispatch(
          saveBusinessInformationActions.businessInformationNavigation(false)
        );
        dispatch(commonGetApiActions.dispatchGetRequest());
      }
      if (businessInfoResponse) {
        setCurrentForm("businessInformation");
        dispatch(
          saveBusinessInformationActions.businessInformationNavigation(false)
        );
      }
      if (
        registrationStatusKey - 3 >= 0 ||
        (registrationStatusKey - 2 >= 0 && authResponse?.type === "Customer")
      ) {
        setCurrentForm("businessInformation");
        dispatch(
          saveBusinessInformationActions.businessInformationNavigation(false)
        );
      }
    }
    if (currentForm === "bidder") {
      if (biddersResponseObject) {
        setCurrentForm("bidder");
        dispatch(commonGetApiActions.dispatchGetRequest());
        dispatch(saveBidderSlice.bidderNavigation(false));
      }
      if (
        registrationStatusKey - 4 >= 0 ||
        (registrationStatusKey - 4 >= 0 && authResponse?.type === "Customer")
      ) {
        setCurrentForm("bidder");
        dispatch(commonGetApiActions.dispatchGetRequest());
        dispatch(saveBidderSlice.bidderNavigation(false));
      }
    }
    if (currentForm === "approver") {
      if (approverResponse) {
        setCurrentForm("approver");
        dispatch(commonGetApiActions.dispatchGetRequest());
        dispatch(saveApproverSlice.approverNavigation(false));
      }
      if (
        registrationStatusKey - 5 >= 0 ||
        (registrationStatusKey - 5 >= 0 && authResponse?.type === "Customer")
      ) {
        setCurrentForm("approver");
        dispatch(commonGetApiActions.dispatchGetRequest());
        dispatch(saveApproverSlice.approverNavigation(false));
      }
    }
    if (currentForm === "engineer") {
      if (vendorEngineer_Response) {
        setCurrentForm("engineer");
        dispatch(commonGetApiActions.dispatchGetRequest());
        dispatch(saveEngineerSlice.engineerNavigation(false));
      }
      if (registrationStatusKey - 6 >= 0) {
        setCurrentForm("engineer");
        dispatch(commonGetApiActions.dispatchGetRequest());
        dispatch(saveEngineerSlice.engineerNavigation(false));
      }
    }

    if (currentForm === "services") {
      if (createServiceRes) {
        setCurrentForm("services");
        dispatch(commonGetApiActions.dispatchGetRequest());
        dispatch(createServiceActions.serviceNavigation(false));
      }
      if (registrationStatusKey - 7 >= 0) {
        setCurrentForm("services");
        dispatch(commonGetApiActions.dispatchGetRequest());
        dispatch(createServiceActions.serviceNavigation(false));
      }
    }

    if (currentForm === "finance") {
      if (createServiceRes) {
        setCurrentForm("finance");
        dispatch(commonGetApiActions.dispatchGetRequest());
        dispatch(saveFinancerSlice.financeNavigation(false));
      }
      if (registrationStatusKey - 8 >= 0) {
        setCurrentForm("finance");
        dispatch(commonGetApiActions.dispatchGetRequest());
        dispatch(saveFinancerSlice.financeNavigation(false));
      }
    }

    if (currentForm === "managingHead") {
      if (managingHeadRes) {
        setCurrentForm("managingHead");
        dispatch(saveManagingHeadSlice.managingHeadNavigation(false));
      }
    }
  };

  const sidebarHandler = (props) => {
    if (
      (props === "General" && currentForm === "address") ||
      (props === "Team" && currentForm === "bidder") ||
      (props === "Services" && currentForm === "services") ||
      (props === "Finance" && currentForm === "finance")
    ) {
      return false;
    }

    if (props === "General") {
      setCurrentForm("address");
      setCurrentActiveMenu(1);
      dispatch(commonGetApiActions.dispatchGetRequest());
      dispatch(saveAddressActions.addressNavigation(false));
    }
    if (props === "Team") {
      if (businessInfoResponse) {
        setCurrentForm("bidder");
        setCurrentActiveMenu(2);
        dispatch(commonGetApiActions.dispatchGetRequest());
        dispatch(saveBidderSlice.bidderNavigation(false));
      }
      if (registrationStatusKey - 3 > 0) {
        setCurrentForm("bidder");
        setCurrentActiveMenu(2);
        dispatch(commonGetApiActions.dispatchGetRequest());
        dispatch(saveBidderSlice.bidderNavigation(false));
      }
    }

    if (props === "Services") {
      if (managingHeadRes || vendorEngineer_Response) {
        setCurrentForm("services");
        setCurrentActiveMenu(3);
        dispatch(commonGetApiActions.dispatchGetRequest());
        dispatch(createServiceActions.serviceNavigation(false));
      }

      if (registrationStatusKey - 7 > 0) {
        setCurrentForm("services");
        setCurrentActiveMenu(3);
        dispatch(commonGetApiActions.dispatchGetRequest());
        dispatch(createServiceActions.serviceNavigation(false));
      }
    }
    if (props === "Finance") {
      if (createServiceRes || managingHeadRes) {
        setCurrentForm("finance");
        setCurrentActiveMenu(4);
        dispatch(commonGetApiActions.dispatchGetRequest());
        dispatch(saveFinancerSlice.financeNavigation(false));
      }

      if (registrationStatusKey - 8 > 0) {
        setCurrentForm("finance");
        setCurrentActiveMenu(4);
        dispatch(saveFinancerSlice.financeNavigation(false));
        dispatch(commonGetApiActions.dispatchGetRequest());
      }
    }
    if (props === "Payment") {
      if (paymentRequestRes) {
        setCurrentForm("payment");
        setCurrentActiveMenu(5);
        dispatch(saveFinancerSlice.financeNavigation(false));
        dispatch(commonGetApiActions.dispatchGetRequest());
      }
    }

    if (props === "Professional") {
      // Need to add condition on the basis on registeredStatusKey
      if (vendorEngineer_Response) {
        setCurrentActiveMenu(2);
        setCurrentForm("engineer");
        dispatch(commonGetApiActions.dispatchGetRequest());
        dispatch(saveEngineerSlice.engineerNavigation(false));
      }
    }
  };

  const selectFormHandler = () => {
    switch (currentForm) {
      case "details":
        return (
          <Details
            setFormLabel={updateFormStatus}
            selectedUser={props.userType}
            navigationHandler={navigationHandler}
          />
        );

      case "businessInformation":
        return (
          <BusinessInformation
            setFormLabel={updateFormStatus}
            selectedUser={props.userType}
            navigationHandler={navigationHandler}
          />
        );

      case "address":
        return (
          <Address
            setFormLabel={updateFormStatus}
            selectedUser={props.userType}
            navigationHandler={navigationHandler}
          />
        );

      case "verifyEmail":
        return (
          <VerifyEmail
            setFormLabel={updateFormStatus}
            selectedUser={props.userType}
            navigationHandler={navigationHandler}
          />
        );

      case "verifyMobile":
        return (
          <VerifyMobile
            setFormLabel={updateFormStatus}
            selectedUser={props.userType}
            navigationHandler={navigationHandler}
          />
        );

      case "bidder":
        return (
          <Bidder
            setFormLabel={updateFormStatus}
            selectedUser={props.userType}
            navigationHandler={navigationHandler}
          />
        );

      case "approver":
        return (
          <Approver
            setFormLabel={updateFormStatus}
            selectedUser={props.userType}
            navigationHandler={navigationHandler}
          />
        );

      case "engineer":
        return (
          <Engineer
            setFormLabel={updateFormStatus}
            selectedUser={props.userType}
            navigationHandler={navigationHandler}
          />
        );

      case "managingHead":
        return (
          <ManagingHead
            setFormLabel={updateFormStatus}
            selectedUser={props.userType}
            navigationHandler={navigationHandler}
          />
        );

      case "services":
        return (
          <Services
            setFormLabel={updateFormStatus}
            selectedUser={props.userType}
            navigationHandler={navigationHandler}
          />
        );

      case "finance":
        return (
          <Finance
            setFormLabel={updateFormStatus}
            selectedUser={props.userType}
            navigationHandler={navigationHandler}
          />
        );

      case "companyHead":
        return (
          <CompanyHead
            setFormLabel={updateFormStatus}
            selectedUser={props.userType}
            navigationHandler={navigationHandler}
          />
        );

      case "payment":
        return (
          <Payment
            setFormLabel={updateFormStatus}
            selectedUser={props.userType}
            navigationHandler={navigationHandler}
          />
        );
      case "invoice":
        return (
          <Invoice
            setFormLabel={updateFormStatus}
            selectedUser={props.userType}
            navigationHandler={navigationHandler}
          />
        );

      default:
        return null;
    }
  };

  useEffect(() => {
    let flag = null;
    if (promotionDetailsRes !== null) {
      if (
        new Date(`${promotionDetailsRes[0]?.startedDate}`).valueOf() <
          new Date(
            `${moment(new Date()).format("YYYY-MM-DD").toString()}`
          ).valueOf() &&
        new Date(
          `${moment(new Date()).format("YYYY-MM-DD").toString()}`
        ).valueOf() < new Date(`${promotionDetailsRes[0]?.endDate}`).valueOf()
      ) {
        flag = 1;
      } else if (
        new Date(`${promotionDetailsRes[1]?.startedDate}`).valueOf() <
          new Date(
            `${moment(new Date()).format("YYYY-MM-DD").toString()}`
          ).valueOf() &&
        new Date(
          `${moment(new Date()).format("YYYY-MM-DD").toString()}`
        ).valueOf() < new Date(`${promotionDetailsRes[1]?.endDate}`).valueOf()
      ) {
        flag = 2;
      } else if (
        new Date(`${promotionDetailsRes[2]?.startedDate}`).valueOf() <
          new Date(
            `${moment(new Date()).format("YYYY-MM-DD").toString()}`
          ).valueOf() &&
        new Date(
          `${moment(new Date()).format("YYYY-MM-DD").toString()}`
        ).valueOf() < new Date(`${promotionDetailsRes[2]?.endDate}`).valueOf()
      ) {
        flag = 3;
      }

      setPromotionFlag(flag);
    }
  }, [promotionDetailsRes]);

  return (
    <>
      <Header
        page={props.userType}
        userType={props.setUserType}
        pageName={currentForm}
      />
      <section className="main_container">
        <div className="main_wrapper">
          <Sidebar
            makeMenuActive={currentActiveMenu}
            selectedUser={props.userType}
            sidebarHandler={sidebarHandler}
          />
          {selectFormHandler()}
        </div>

        <div className="promotion_header">
          {promotionFlag === 1 && (
            <Marquee
              pauseOnHover={true}
              direction={"left"}
              speed={50}
              gradient={false}
            >
              <div className="a_marquee">
                One time registration fees is being waived off till&nbsp;
                {moment(promotionDetailsRes[0]["endDate"]).format("DD MMM YY")},
                after which a fee of minimum USD 100 will be applicable.
                Register now! &nbsp;
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              </div>
            </Marquee>
          )}
          {promotionFlag === 2 && (
            <Marquee
              pauseOnHover={true}
              direction={"left"}
              speed={20}
              gradient={false}
            >
              <div className="a_marquee">
                We are giving away vouchers worth USD 100 till&nbsp;
                {moment(promotionDetailsRes[1]["endDate"]).format("DD MMM YY")}
                for every successful registration. Register now!&nbsp;
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              </div>
            </Marquee>
          )}
          {promotionFlag === 3 && (
            <Marquee
              pauseOnHover={true}
              direction={"left"}
              speed={20}
              gradient={false}
            >
              <div className="a_marquee">
                We are giving away vouchers worth USD 50 till&nbsp;
                {moment(promotionDetailsRes[2]["endDate"]).format("DD MMM YY")}
                for every successful registration. Register now!&nbsp;
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              </div>
            </Marquee>
          )}
        </div>
      </section>
      <Footer />
    </>
  );
};

export default Registration;