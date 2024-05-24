
import React, { useState } from "react";
import Switching from "../../../../components/Switching/Switching";
import { viewComplteSwitch, Roundshow1, round3, participansts } from "../../../../helpers/RoundList";
import "../ViewCompletedContest/ViewCompleteContest.scss";
import DynamicToggle from "../../../../components/hr-components/dynamic-toggle";
import RoundDetails from "./RoundDetails";
import CompleteContest from "../CompleteContest";
import { useLocation } from "react-router";

const ViewCompleted = () => {
  const location = useLocation();
  // const result = location?.state?.contestData;
  // const contestId = location?.state?.contestId;
  
  const roundNumbers = Roundshow1.map((round) => round.Roundno);
  const roundTypes = Roundshow1.map((round) => round.roundType);
  const [pages, setPages] = useState("Round 1");
  const [pagesindex, setpagesindex] = useState(0);
  const pagechanges = (page) => {
    setPages(page);
    const pageNumber = page.replace(/\D/g, '');
    if(page==="Final Result"){
      setpagesindex(roundTypes.length.toString())
    }else{
      setpagesindex(pageNumber)
    }
  };

  const [stateSwitch, setStateSwitch] = useState("Contest Details");
  const rendervalues = (value) => {
    setStateSwitch(value)
  }
  console.log(roundNumbers)
  return (
    <div className="viewCompleted">
      <Switching
        Arrayofvalue={viewComplteSwitch}
        className={"viewContestSwitch"}
        renderContent={rendervalues}
      />
      {stateSwitch === "Contest Details" ?
        <>
          <div className="contestnameParticipant">
            <p>
              <span>Contest Name</span> : Divum Contest | Jan 01,2024 - Jan 03,2024
            </p>
            <p>
              <span>Participant count</span> : 180
            </p>
          </div>
          <div className="viewCompletetogglebutton">
            <DynamicToggle
              switchStates={roundNumbers.map((round) => roundNumbers.length==round?roundTypes[round-1]:`Round ${round}`)}
              page={pages}
              handleToggle={pagechanges}
            />
          </div>
          {roundTypes[pagesindex - 1] === "test" || pages === "Round 1" ?
            <RoundDetails />
            :
            <CompleteContest heading={pages} tableinfo={round3} 
            isview={pages===roundTypes[pagesindex-1] && true}/>}
        </>
        :
        <CompleteContest tableinfo={participansts} className={"participantspage"} isview={ true} />}
    </div>
  );
};

export default ViewCompleted;
