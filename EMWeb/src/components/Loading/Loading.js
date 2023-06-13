import Backdrop from './../Backdrop/Backdrop';
import './Loading.scss';

const Loading = (props) => {
    return (
        <Backdrop>
            <div className="loading_spinner_container">
                <div className="loading_spinner">
                    <div className="lds-spinner"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>
                </div>
                <div className="loading_name">{props.text}</div>
            </div>
        </Backdrop>
    );
}

export default Loading;