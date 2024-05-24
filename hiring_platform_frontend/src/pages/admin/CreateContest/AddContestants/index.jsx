import React, { useState } from "react";
import Switching from "../../../../components/Switching/Switching";
import'./AddContestant.scss';
import ManualUpload from "./modalChildren/ManualUpload/index";
import FileUpload from "./modalChildren/FileUpload/index";

const AddContestants = ({setMainContestantAdd}) => {
    const array = ["Manual", "File Upload"];
    const [state, setState] = useState("Manual");
    // const [manualdata, setmanualdata] = useState([]);
     const rendervalue=(value)=>{
              setState(value)
     }

  return (
    <div className="AddContestants">
        <div className="Upload">
          <h1 className="addcontestantHeading"> Add Contestants</h1>
          <Switching Arrayofvalue={array} renderContent={rendervalue} className={"switchingfont"} />
          {state === "Manual" ? <ManualUpload setMainContestantAdd={setMainContestantAdd}/>: <FileUpload />}
        </div>
    </div>
  );
};

export default AddContestants