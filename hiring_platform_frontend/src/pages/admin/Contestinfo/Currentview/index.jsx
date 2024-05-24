import React, { useEffect, useState } from "react";
import Switching from "../../../../components/Switching/Switching";
import { viewComplteSwitch, Roundshow1, round3, participansts } from "../../../../helpers/RoundList";
import "../ViewCompletedContest/ViewCompleteContest.scss";
import DynamicToggle from "../../../../components/hr-components/dynamic-toggle";

import CompleteContest from "../CompleteContest";
import RoundDetails from "../ViewCompletedContest/RoundDetails";
import { useLocation } from "react-router";
import { fetchFinalRoundApi, fetchRoundApi, partcipantsRoundApi } from "../../../../actions/API_actions";
import { round } from "face-api.js/build/commonjs/utils";

const Currentview = () => {
  const location = useLocation();
  const result = location?.state?.contestData;
  const contestIds = location?.state?.contestId;
  const roundTypes = result?.roundLists?.map((round) => round.roundType);
  const roundName = result?.roundLists?.map((round) => round.round);


  const [pages, setPages] = useState("Round 1");
  const [pagesindex, setpagesindex] = useState(0);
  const [roundDetails, setRoundDetails] = useState()
  const [roundId, setRoundId] = useState(result.roundLists[0].roundId);
  const [stateSwitch, setStateSwitch] = useState("Contest Details");
  const [tableDetails, setTableDetails] = useState()

  const pagechanges = async (page, index) => {
    setPages(page);
    setpagesindex(index)
    setRoundId(result.roundLists[index].roundId);
  };

  const fetchRoundData = async () => {
    if (stateSwitch === "Contest Details") {
      if (pages !== "Final Result") {
        try {
          const round = await fetchRoundApi(contestIds, roundId);
          setRoundDetails(round);
        } catch (err) {
          console.log("Error in fetching round details", err);
        }
      }
      else {
        try {
          const round = await fetchFinalRoundApi(contestIds, roundId);
          setRoundDetails(round);
          // console.log(round)
        } catch (err) {
          console.log("Error in fetching final round details", err);
        }
      }
    } else if (stateSwitch === "Participants") {
      try {
        const round = await partcipantsRoundApi(contestIds);
        console.log(round)
        setRoundDetails(round.map(({ name, email, collegeName }, index) =>
          ({ No: index + 1, "Name": name, "Email": email, "College Name": collegeName })))
      } catch (err) {
        console.log("Error in fetching final round details", err);
      }
    }
  };

  useEffect(() => {
    fetchRoundData();
  }, [roundId, stateSwitch, pages, contestIds]);

  // console.log(stateSwitch)
  const rendervalues = (value) => {
    setStateSwitch(value)
  }

  

  useEffect(() => {
    if (pages !== "Final Result") {
      setTableDetails(roundDetails?.interviewResults?.map(({ contestant, employee, collageName, email }, index) =>
        ({ No: index + 1, "Contestant Name": contestant, "Employee": employee, "Collage Name": collageName, "Email": email })))
    } else {
      console.log(pages)
      setTableDetails(roundDetails?.object?.map(({ contestant, email, college, userId }, index) =>
        ({ No: index + 1, "Contestant Name": contestant, "Email": email, "Collage Name": college, userId })))
    }
  }, [roundDetails, pages])

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
              <span>Contest Name</span> : {result.contestName} <span style={{ padding: "0rem 1rem" }}>|</span> {result.contestDate}
            </p>
            <p>
              <span>Participant count</span> : {result.participantCount}
            </p>
          </div>
          <div className="viewCompletetogglebutton">
            <DynamicToggle
              switchStates={roundName}
              page={pages}
              handleToggle={pagechanges}
            />
          </div>
          {roundTypes[pagesindex] === "Test" ?
            <RoundDetails roundDetails={roundDetails} />
            :
            <CompleteContest heading={pages} round={true} roundDetails={roundDetails} tableinfo={tableDetails} currentContest={true} contestIds={contestIds} />}
        </> :
        <CompleteContest participant={true}  tableinfo={roundDetails} className={"participantspage"} />}
    </div>
  );
};

export default Currentview;
