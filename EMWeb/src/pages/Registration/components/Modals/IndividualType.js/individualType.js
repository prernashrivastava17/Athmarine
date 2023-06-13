import React from 'react';
import Backdrop from '../../../../../components/Backdrop/Backdrop';
import './individualType.scss';

const IndividualType = (props) => {
  return (
    <>
            <Backdrop>
            <div className="modal_individual_type">
            <div className="form_control_spread">
                <label className="form_label123">
                  Do you have a company?
                </label>
                <div className="radio_container_type">
                  <span className="a_control">
                    <input
                      type="radio"
                      name="sameAsAdmin"
                      className="form_control_radio_type"
                      id="a_yes"
                      value="yes"
                      onClick={props.haveCompany}
                    />
                    <label className="form_label" htmlFor="a_yes">
                      Yes
                    </label>
                  </span>
                  <span className="a_control">
                    <input
                      type="radio"
                      name="sameAsAdmin"
                      className="form_control_radio_type"
                      id="a_no"
                      value="no"
                      onClick={props.haveNotCompany}
                    />
                    <label className="form_label" htmlFor="a_no">
                      No
                    </label>
                  </span>
                </div>
              </div>            
            </div>
        </Backdrop>
    </>
  )
}

export default IndividualType