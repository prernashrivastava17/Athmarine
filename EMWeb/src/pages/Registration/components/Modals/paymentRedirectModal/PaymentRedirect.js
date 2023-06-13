import "./PaymentRedirect.scss";
import Backdrop from "./../../../../../components/Backdrop/Backdrop";
import boat from "./../../../../../assets/images/boat.png";
import wave from "./../../../../../assets/images/wave.png";

const PaymentRedirect = (props) => {
  return (
    <Backdrop>
      <div className="payment_confirmation_modal">
        <img src={boat} alt="boat" className="boat" />
        <img src={wave} alt="wave" className="wave" />
        <div className="text">
          <p>Hang tight!</p>
          <p>
            You are being redirected to the payment page, It may takes upto few
            seconds...
          </p>
        </div>
      </div>
    </Backdrop>
  );
};

export default PaymentRedirect;
