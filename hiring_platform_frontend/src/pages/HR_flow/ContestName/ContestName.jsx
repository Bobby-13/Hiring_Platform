import React, { useState } from "react";
import HRSidebar from "../../../components/HRSidebar/index";
import "./ContestName.scss";
import Switching from "../../../components/Switching/Switching";
import AssingnedContestant from "./AssingnedContestant";
import CompletedContestant from "./CompletedContestant";
import { useParams } from "react-router";

const ContestName = () => {
  const data = ["ASSIGNED CONTESTANTS", "COMPLETED CONTESTANTS"];
  const [clickvalue, setClickvalue] = useState("ASSIGNED CONTESTANTS");
  const [assignedContestantData, setAssignedContestantData] = useState([]); 
  const [clickevent, setClickevent] = useState(false);
  const { contestName } = useParams(); 
  const click = (value) => {
    setClickvalue(value);
  };
  
  const getvaluesfun = (value) => {
    setClickevent(value);
    console.log(clickevent, "hello");
  };

  return (
    <div className="bg-container">
      <HRSidebar />
      <div>
        <h1 style={{ marginTop: "4rem", marginLeft: "6rem", color: "#757575" }}>
          {contestName} 
        </h1>
        <div className="switching-container">
          <Switching
            className="swiching-content"
            Arrayofvalue={data}
            clickinfo={click}
            renderContent={setClickvalue}
          />
          {clickvalue === "ASSIGNED CONTESTANTS" ? (
            <AssingnedContestant
              getvalue={getvaluesfun}
              data={assignedContestantData}
            />
          ) : (
            <CompletedContestant getvalue={getvaluesfun} />
          )}
        </div>
      </div>
    </div>
  );
};

export default ContestName;
