import React, { useState } from "react";
import { useHistory } from "react-router-dom";
import Sidebar from "./components/Sidebar/Sidebar";
import Dashboard from "./components/Dashboard/Dashboard";
import LatestRequest from "./components/LatestRequest/LatestRequest";
import Bids from "./components/Bids/Bids";
import CompletedJobs from "./components/completedJobs/CompletedJobs";
import PendingRequets from "./components/PendingRequests/PendingRequets";
import Chat from "./components/Chat/Chat";
import AddNewRequest from "./components/AddNewRequest/AddNewRequest";
import BidsAccepted from "./components/BidsAccepted/BidsAccepted";
import BidsReceived from "./components/BidsReceived/BidsReceived";
import DashboardHeader from './../../components/DashBoardHeader/DashBoardHeader';
import Footer from "../../components/Footer/Footer";

function DashboardNavigation() {
  const [currentScreen, setCurrentScreen] = useState("Dashboard");

  const updateFormStatus = (formLabel) => {
    setCurrentScreen(formLabel);
  };

  const sidebarHandler = (props) => {
    if (props === "Dashboard") {
      setCurrentScreen("Dashboard");
    }
    if (props === "LatestRequest") {
      setCurrentScreen("LatestRequest");
    }
    if (props === "Bids") {
      setCurrentScreen("Bids");
    }
    if (props === "CompletedJobs") {
      setCurrentScreen("CompletedJobs");
    }
    if (props === "PendingRequets") {
      setCurrentScreen("PendingRequets");
    }
    if (props === "AddNewRequest") {
      setCurrentScreen("AddNewRequest");
    }
    if (props === "BidsAccepted") {
      setCurrentScreen("BidsAccepted");
    }
    if (props === "BidsReceived") {
      setCurrentScreen("BidsReceived");
    }
  };

  const screenHandler = () => {
    switch (currentScreen) {
      case "Dashboard":
        return <Dashboard setCurrentScreen={updateFormStatus} />;
      case "LatestRequest":
        return <LatestRequest setCurrentScreen={updateFormStatus} />;
      case "Bids":
        return <Bids />;
      case "CompletedJobs":
        return <CompletedJobs />;
      case "PendingRequets":
        return <PendingRequets />;
      case "Chat":
        return <Chat />;
      case "AddNewRequest":
        return <AddNewRequest />;
      case "BidsAccepted":
        return <BidsAccepted />;
      case "BidsReceived":
        return <BidsReceived />;
      default:
      // do nothing
    }
  };

  return (
    <>
      <DashboardHeader />
      <div style={{ display: "flex" }}>
        <Sidebar sidebarHandler={sidebarHandler} />
        {screenHandler()}
      </div>
      <Footer />
    </>
  );
}

export default DashboardNavigation;