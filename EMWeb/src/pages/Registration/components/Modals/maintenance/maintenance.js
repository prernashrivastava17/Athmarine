import "./maintenance.scss";
import logo from "../../../../../assets/images/logo.png"
import Backdrop from "./../../../../../components/Backdrop/Backdrop";

const Maintenance = (props) => {
  return (
    <Backdrop>
      <div className="modal_addition">
              <img
                src={logo}
                className="display_user"
                alt="user-icon"
                />
            <div className="login_info_maintenance">Work in progress.</div>
  

        <div className="close_container_charges" onClick={props.closeModal}>
          <span className="close_icon">&times;</span>
        </div>
      </div>
    </Backdrop>
  );
};

export default Maintenance;
