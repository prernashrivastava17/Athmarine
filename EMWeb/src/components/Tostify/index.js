import React from "react";
import { toast } from "react-toastify";

const Msg = ({ message, subtitle, icon }) => (
    <div style={{ display: 'flex' }}>
        <div style={{ marginLeft: '20px', marginRight: '20px' }}>
            {/* <FontAwesomeIcon icon={icon} className="font-size-xxl" /> */}
        </div>
        <div style={{ display: 'flex', flexDirection: "column", fontSize:"15px" }}>
            <div style={{ fontWeight: 'bold' }}>{subtitle}</div>
            <div className="message">{message}</div>
        </div>
    </div>
);

export function ToastSuccess(message) {
    toast.success(
        <Msg message={message} subtitle="Success!" />
    );
}

export function ToastError(message) {
    toast.error(
        <Msg message={message} subtitle="Error!" />
    );
}
