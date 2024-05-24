import React, { useState } from "react";
import HRSidebar from "../../../components/HRSidebar/index";
import CompletedContestant from "./CompletedContestant";
import "./ContestLog.scss";

const ContestLog = () => {
  return (
    <div className="bg-container">
      <HRSidebar />
      <div className="LogContainer">
        <h1>CONTEST NAME</h1>
        <div>
           <h2 className="completed-header">COMPLETED CONTEST</h2>
            <CompletedContestant  />
        
        </div>
      </div>
    </div>
  );
};

export default ContestLog;
