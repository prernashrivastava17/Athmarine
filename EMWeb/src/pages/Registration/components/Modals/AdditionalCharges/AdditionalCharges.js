import "./AdditionalCharges.scss";
import Backdrop from "./../../../../../components/Backdrop/Backdrop";

const AdditionalCharges = (props) => {
  return (
    <Backdrop>
      <div className="modal_addition_charges">
        <p className="info">
          {/* On basic registration fee, Vendor can onboard only&nbsp;
          {props.engineerCount === 10
            ? props.engineerCount
            : props.engineerCount === 15
            ? props.engineerCount
            : 10}
          &nbsp;engineers. In order to add more engineers , refer the table
          below:- */}
        In the basic registration fee of USD 100, up to 10 engineers can be registered free of cost. Nominal fee to add more engineers is stated in the table below:
        </p>
        <div className="engineers_pricing_container">
          <div className="engineers_head">
            <span className="heading">Engineers Registered</span>
            <span className="heading">Extra Amount</span>
          </div>
          <div className="engineers_body">
            <div className="engineers_row">
              <span className="text">Up to 10	</span>
              <span className="text_value">None</span>
            </div>
            <div className="engineers_row">
              <span className="text">From 11 up to 15</span>
              <span className="text_value">
                {props.additionPriceForMoreThanFifteen} USD/engineer
              </span>
            </div>
            <div className="engineers_row">
              <span className="text">From 16 up to 20</span>
              <span className="text_value">
                {props.additionPriceForMoreThanTwenty} USD/engineer
              </span>
            </div>
            <div className="engineers_row">
              <span className="text">More than 20	</span>
              <span className="text_value">
                {props.additionPriceForMoreThanTwentyFive} USD/engineer
              </span>
            </div>
          </div>
        </div>
        <div className="close_container_charges" onClick={props.closeModal}>
          <span className="close_icon">&times;</span>
        </div>
      </div>
    </Backdrop>
  );
};

export default AdditionalCharges;
