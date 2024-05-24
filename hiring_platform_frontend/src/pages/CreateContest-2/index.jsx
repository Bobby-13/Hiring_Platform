import React, { useEffect, useState } from "react";
import "./addpopupmcq.scss";
import Switching from "../../components/Switching/Switching";
import MCQContent from "../MCQContent";
import CodingContent from "../CodingContent";
// import Button from "../../components/Button";
import InputTag from "../../components/InputTag";

const Addpopupmcq = ({arrays,setPopup,arraylength}) => {
  const array = ["MCQ", "CODING"];
  const [state, setState] = useState("MCQ");
  const setMcqdata = useState([]);
  const [totalCountDisplay, setTotalCountDisplay] = useState(0);
  const [CategoryTitle, setCategoryTitle] = useState("");
  // const [totalcontestInfo,setTotalcontestInfo]=useState([])
  const [totalcounts, setTotalcounts] = useState({
    VERBAL: 0,
    APTITUDE: 0,
    LOGICAL: 0,
    TECHNICAL: 0,
    DSA: 0,
    MATHEMATICS: 0,
    STRINGS: 0,
    PATTERN: 0,
    ALGORITHMS: 0,
  });
  const handleAssignValue = () => {
    setTotalcounts((prevCounts) => ({
      ...prevCounts,
      [CategoryTitle]: totalCountDisplay,
    }));
  };
  const sum = Object.values(totalcounts).reduce((acc, curr) => acc + curr, 0);

  useEffect(() => {
    handleAssignValue();
  }, [totalCountDisplay]);

  const getinfovalue = (value,title,percentages) => {
    console.log(value)
    const nonNullUndefinedValues = value.filter(item => item !== null && item !== undefined);
    arrays(prevRoundinfo => [
        ...prevRoundinfo,
        {
            roundType:title,
            roundNumber:arraylength+1,
            PassPercentage: percentages.PassPercentage,
            startTime: percentages.StartDateTime,
            endTime: percentages.EndDateTime,
            parts: nonNullUndefinedValues
        }
    ]);
};
  return (
    <div className="CreateContest">
      <div className="Addrounds" style={{width:state!=="MCQ"?"85rem":"70rem"}}>
        <span className="AddRoundstitle"> Add Rounds</span>
        <Switching Arrayofvalue={array} renderContent={setState} />
        {state === "MCQ" ? (
          <MCQContent
            setTotalCountDisplay={setTotalCountDisplay}
            totalCountDisplay={totalCountDisplay}
            setCategoryTitle={setCategoryTitle}
            getinfovalue={getinfovalue}
            setPopup={setPopup}
          />
        ) : (
          <CodingContent
            constestinfo={setMcqdata}
            setTotalCountDisplay={setTotalCountDisplay}
            totalCountDisplay={totalCountDisplay}
            setCategoryTitle={setCategoryTitle}
            getinfovalue={getinfovalue}
            setPopup={setPopup}
          />
        )}
        <div className="TotalQuestionCount">
          <p>Total questions count:{sum}</p>
          {state === "MCQ" ? (
            <p>Total duration:</p>
          ) : (
            <InputTag
              type={"text"}
              placeholder={"mins"}
              label={"Total duration in mins:"}
              className={"min"}
            />
          )}
        </div>

      
      </div>
    </div>
  );
};

export default Addpopupmcq;
