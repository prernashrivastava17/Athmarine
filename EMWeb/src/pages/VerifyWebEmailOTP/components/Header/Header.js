import React from 'react'
import Logo from './../../../../assets/images/logo.png';
import './Header.scss';
import { useHistory } from "react-router-dom";

function Header() {
    const history = useHistory();
    return (
        <div className="my_header_container">
            <div className="logo_container">
                <img src={Logo} alt="logo" onClick={() => history.push("/")} />
            </div>
        </div>
    )
}

export default Header
