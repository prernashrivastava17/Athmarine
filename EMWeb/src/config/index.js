// import DEV_VARS from "./env/dev";

export const apiUrls = {
  // baseUrl: process.env.REACT_APP_URL_STAGING,
  baseUrl: process.env.REACT_APP_URL_QA,
  path: {
    authentication: `authenticate`,
    getCountries: `master/getCountryList`,
  },
};

export const getToken = () => {
  if (JSON.parse(localStorage.getItem("authData"))?.token) {
    return JSON.parse(localStorage.getItem("authData")).token;
  }
  return JSON.parse(localStorage.getItem("token"));
};

export const getCompanyId = () => {
  if (JSON.parse(localStorage.getItem("authData"))?.companyId) {
    return JSON.parse(localStorage.getItem("authData")).companyId;
  }
  return JSON.parse(localStorage.getItem("companyId"));
};

export const getUserId = () => {
  return JSON.parse(localStorage.getItem("userId"));
};

export const getRequestorId = () => {
  return JSON.parse(localStorage.getItem("requestorId"));
};

export const getAuthToken = () => {
  return JSON.parse(localStorage.getItem("authData"))?.token;
};
export const getAuthData = () => {
  return JSON.parse(localStorage.getItem("authData"));
};

export const getReferralCode = () => {
  if (JSON.parse(localStorage.getItem("authData"))?.referralCode) {
    return JSON.parse(localStorage.getItem("authData"))?.referralCode;
  }
  return JSON.parse(localStorage.getItem("addressInfo"))?.referralCode;
};
