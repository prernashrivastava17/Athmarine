import React, { useState } from "react";
import { Switch, Route } from "react-router-dom";

import Login from "../pages/Login/Login";
import Registration from "../pages/Registration/Registration";
import ChangePassword from "../pages/ChangePassword/ChangePassword";
import EnterOTP from "../pages/EnterOTP/EnterOTP";
import ProtectedRoute from "./ProtectedRoute";
import EditProfile from "../pages/EditProfile/EditProfile";
import DashboardNavigation from "../pages/Dashboard/DashboardNavigation";
import TermsAndConditions from "../pages/TermsAndConditions/TermsAndConditions";
import WebVerification from "../pages/VerifyWebEmailOTP/WebVerification";
import Payment from "./../pages/Payment/Payment";
import LoginVerification from "../pages/LoginVerification/LoginVerification";
import HomePage from "../website/homepage/index";
import TermsAndConditionsWebsite from "../website/termsAndCondition/index";
import TermsOfService from "../website/TermsAndservice/TermsOfService";
import AboutUs from "../website/aboutUs";
import ContactUs from "../website/contactUs";
import OurSolutions from "../website/ourSolution";
import FAQ from "../website/FAQ";
import CookiesAndPreferences from "../website/cookiesAndPreferences";
import PaymentSlip from "../pages/Payment/PaymentSlip/PaymentSlip";
import PromotionDetails from "../pages/Promotions/PromotionDetails/PromotionDetails";
import SignIn from "../pages/Promotions/SigninForPromotion/SignIn";
import PromotionDates from "../pages/Promotions";


const Routes = () => {
  const [userRegistrationType, setUserRegistrationType] = useState("");

  const registrationTypeHandler = (type) => {
    setUserRegistrationType(type);
  };

  return (
    <>
      <Switch>
        <Route path="/login" exact>
          <Login setUserType={registrationTypeHandler} />
        </Route>

        {/* ************************* Static Website Starting *************************  */}
        <Route path="/" exact>
          <HomePage />
        </Route>
        <Route path="/tandc" exact>
          <TermsOfService />
        </Route>
        <Route path="/privacyPolicy" exact>
          <TermsAndConditionsWebsite />
        </Route>
        <Route path="/about-us" exact>
          <AboutUs />
        </Route>
        <Route path="/contact-us" exact>
          <ContactUs />
        </Route>
        <Route path="/faq" exact>
          <FAQ />
        </Route>
        <Route path="/cookies" exact>
          <CookiesAndPreferences />
        </Route>

        <Route path="/our-solutions" exact>
          <OurSolutions />
        </Route>
        {/* ************************* Static Website Ending *************************  */}

        <Route path="/registration">
          <Registration
            userType={userRegistrationType}
            setUserType={registrationTypeHandler}
          />
        </Route>
        <Route path="/otp">
          <EnterOTP setUserType={registrationTypeHandler} />
        </Route>
        <Route path="/changePassword">
          <ChangePassword setUserType={registrationTypeHandler} />
        </Route>
        <Route path="/verify-web-otp">
          <WebVerification />
        </Route>
        <Route path="/terms-and-conditions">
          <TermsAndConditions />
        </Route>
        <Route path="/payment">
          <Payment />
        </Route>

        <Route path="/paymentSlip">
          <PaymentSlip />
        </Route>

        <Route path="/promotionDetails">
          <PromotionDates />
        </Route>

        <Route path="/verify">
          <LoginVerification
            userType={userRegistrationType}
            setUserType={registrationTypeHandler}
          />
        </Route>

        <ProtectedRoute
          path="/dashboard"
          component={DashboardNavigation}
        ></ProtectedRoute>
        <ProtectedRoute
          path="/profile"
          component={EditProfile}
        ></ProtectedRoute>
      </Switch>
    </>
  );
};

export default Routes;
