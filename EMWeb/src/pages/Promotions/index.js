import React, { useState } from "react";
import PromotionDetails from "./PromotionDetails/PromotionDetails";
import SignIn from "./SigninForPromotion/SignIn";

const PromotionDates = () => {
  const [isSignForm, setIsSignForm] = useState(true);
  const [isPromotionDate, setIsPromotionDate] = useState(false);

  const switchScreenHandler = (data) => {
    setIsSignForm(data.signIn);
    setIsPromotionDate(data.promotion);
  };

  return (
    <>
      {isSignForm && <SignIn details={switchScreenHandler} />}
      {isPromotionDate && <PromotionDetails details={switchScreenHandler} />}
    </>
  );
};

export default PromotionDates;
