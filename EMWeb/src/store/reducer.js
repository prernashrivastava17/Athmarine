import { combineReducers } from "redux";
import authReducer from "./auth/auth";
import countryReducer from "./common/country";
import stateReducer from "./common/state";
import cityReducer from "./common/city";
import addressReducer from "./vendorRegistration/address";
import availableOnReducer from "./common/availableOn";
import referralCodeSlice from "./vendorRegistration/address/referralCode";

import detailsReducer from "./vendorRegistration/details";
import businessInformationReducer from "./vendorRegistration/businessInformation";
import vendorCompanyHeadReducer from "./vendorRegistration/companyHead";
import vendorBidderReducer from "./vendorRegistration/bidder";
import vendorApproverReducer from "./vendorRegistration/approver";
import vendorEngineerCategoryReducer from "./vendorRegistration/category";
import vendorEngineerEquipmentReducer from "./vendorRegistration/equipmentByCategory";
import vendorEngineerReducer from "./vendorRegistration/vendorEngineer";
import financeReducer from "./vendorRegistration/assignedFinancer";
import EngineerStatusReducer from "./vendorRegistration/engineerStatus";
import getEmailOTPReducer from "./vendorRegistration/getEmailOtp";
import verifyEmailOTPReducer from "./vendorRegistration/verifyEmail";
import getMobileOTPReducer from "./vendorRegistration/getMobileOtp";
import verifyMobileOTPReducer from "./vendorRegistration/verifyMobileOtp";
import saveDetailsFormReducer from "./vendorRegistration/saveDetailsForm";
import managingHeadReducer from "./vendorRegistration/managingHead";
import engineerMakeReducer from "./vendorRegistration/engineerMake";
import engineerChargesReducer from "./vendorRegistration/engineerCharges";
import currencyStatusReducer from "./vendorRegistration/currency";
import paymentDetailsReducer from "./vendorRegistration/paymentDetails";
import emailSubscriptionReducer from "./vendorRegistration/emailSubscription";
import phoneSubscriptionReducer from "./vendorRegistration/phoneSubscription";
import currencyCountryReducer from "./vendorRegistration/allCurrency";
import allUserDetailsReducer from "./common/allUserDetails";
import addressGetApiReducer from "./vendorRegistration/addressGetApi";
import verifyWebOTPReducer from "./vendorRegistration/verifyWebOTPEmail";

// Engineer
import dynamicEngineerPriceReducer from "./vendorRegistration/getDynamicEngineersPrice";

// useremail update and verify
import updateEmailAndgetOTPReducer from "./vendorRegistration/updateEmail";
import updateUserMobileOTPReducer from "./vendorRegistration/updateUserMobile";

// ports-by-Country-id
import CountryPortsReducer from "./common/getPortByCountry";
import StatePortReducer from "./common/getPortByStateId";

// service
import getAllDeptReducer from "./vendorRegistration/getAllDepartmentName";
import getAllEquipmentReducer from "./vendorRegistration/getAllEquipmentCategory"; //category reducer
import getAllEquipmentManufacturerReducer from "./vendorRegistration/getAllEquipmentManufacturer";
import getAllEquipmentNameReducer from "./vendorRegistration/getAllEquipmentName";
import createServiceReducer from "./vendorRegistration/createService"; //create service reducer

import commonGetApiReducer from "./common/commonGetApi";
import fileUploadReducer from "./common/fileUpload";
import documentRequestReducer from "./common/documentRequest";

//reset password reducers imports
import resetPasswordReducer from "./ResetPassword/resetPassword";
import updatePasswordReducers from "./ResetPassword/updatePassword";

// encoded userReduceer
import encodedUserReducer from "./vendorRegistration/getUserByEncodedId";

// Payment
import cardValidationReducer from "./payment/cardValidation";
import paymentRequestReducer from "./payment/paymentRequest";
import getAmountToPayReducer from "./payment/getAmountToPay";
import paymentSlipReducer from "./payment/paymentSlip";
import saveInvoiceReducer from "./payment/saveInvoice";
import financeEmailandPhoneReducer from "./payment/financeEmailandPhone";
import invoiceDownloadReducer from "./payment/invoiceDownload";

// Requestor Dashboard
import getRequestorMainReducer from "./dashboard/requestorDashboard/mainDashboard";
import getVesselReducer from "./dashboard/requestorDashboard/vessel";
import getProfileReducer from "./dashboard/requestorDashboard/getProfile";

// Website
import postWebsiteQueries from "./website/contactUs";

//promotions
import getPromotionDetailsReducer from "./common/promotionsDetails";
import PostpromotionDetailsReducer from "./common/promotionDetailsPost";

export default combineReducers({
  auth: authReducer,
  countries: countryReducer,
  states: stateReducer,
  cities: cityReducer,
  addressReducer: addressReducer,
  detailsReducer: detailsReducer,
  businessInformation: businessInformationReducer,
  vendorCompanyHead: vendorCompanyHeadReducer,

  vendorBidder: vendorBidderReducer,
  vendorApprover: vendorApproverReducer,
  managingHeadReducer: managingHeadReducer,
  referralCodeSlice: referralCodeSlice,

  engineerCategories: vendorEngineerCategoryReducer,
  engineerEquipments: vendorEngineerEquipmentReducer,
  vendorEngineerRes: vendorEngineerReducer,
  financerReducer: financeReducer,
  engineerStatus: EngineerStatusReducer,
  emailOTP: getEmailOTPReducer,
  verifyEmailOTP: verifyEmailOTPReducer,
  mobileOTP: getMobileOTPReducer,
  verifyMobileOTP: verifyMobileOTPReducer,
  saveDetailsForm: saveDetailsFormReducer,
  availableOnReducer: availableOnReducer,
  engineerMakeReducer: engineerMakeReducer,
  engineerChargesReducer: engineerChargesReducer,
  currencyStatusReducer: currencyStatusReducer,
  paymentDetailsReducer: paymentDetailsReducer,
  emailSubscriptionReducer: emailSubscriptionReducer,
  phoneSubscriptionReducer: phoneSubscriptionReducer,
  currencyCountryReducer: currencyCountryReducer,
  allUserDetailsReducer: allUserDetailsReducer,
  addressGetApiReducer: addressGetApiReducer,
  verifyWebOTPReducer: verifyWebOTPReducer,

  CountryPortsReducer: CountryPortsReducer,
  StatePortReducer: StatePortReducer,

  getAllDeptReducer: getAllDeptReducer,
  getAllEquipmentReducer: getAllEquipmentReducer,
  getAllEquipmentManufacturerReducer: getAllEquipmentManufacturerReducer,
  getAllEquipmentNameReducer: getAllEquipmentNameReducer,
  createServiceReducer: createServiceReducer,

  updateEmailAndgetOTPReducer: updateEmailAndgetOTPReducer,
  updateUserMobileOTPReducer: updateUserMobileOTPReducer,

  commonGetApiReducer: commonGetApiReducer,
  fileUploadReducer: fileUploadReducer,
  documentRequestReducer: documentRequestReducer,

  //ResetPassword reducers
  resetPasswordReducer: resetPasswordReducer,
  updatePasswordReducers: updatePasswordReducers,

  // EncodeduserInformation reducer
  encodedUserReducer: encodedUserReducer,

  // Engineer
  dynamicEngineerPriceReducer: dynamicEngineerPriceReducer,

  // Payment
  cardValidationReducer: cardValidationReducer,
  paymentRequestReducer: paymentRequestReducer,
  getAmountToPayReducer: getAmountToPayReducer,
  paymentSlipReducer: paymentSlipReducer,
  financeEmailandPhoneReducer: financeEmailandPhoneReducer,
  saveInvoiceReducer: saveInvoiceReducer,
  invoiceDownloadReducer: invoiceDownloadReducer,

  // Requestor Dashboard
  getRequestorMainReducer: getRequestorMainReducer,
  getVesselReducer: getVesselReducer,
  getProfileReducer: getProfileReducer,

  // Website
  postWebsiteQueries: postWebsiteQueries,

  //promotions
  getPromotionDetailsReducer: getPromotionDetailsReducer,
  PostpromotionDetailsReducer: PostpromotionDetailsReducer,
});
