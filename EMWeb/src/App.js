import React, { } from "react";
import { BrowserRouter } from 'react-router-dom';
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";

import "./App.scss";
import Routes from './routers/Routes';

const App = () => {
  return (
    <>
      <BrowserRouter>
        <ToastContainer
          draggable
          pauseOnHover
          hideProgressBar={true}
          autoClose={5000}
          position="top-right"
        />
        <Routes />
      </BrowserRouter>
    </>
  );
}

export default App;
