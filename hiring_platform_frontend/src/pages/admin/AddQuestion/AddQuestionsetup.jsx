
import React, { useState } from "react";
import "./AddQuestionSetup.scss";
import CodingQuestion from "../../../components/Coding/Codingquestion";
import McqQuestion from "../../../components/MCQ/McqQuestion";
import Switching from "../../../components/Switching/Switching";

const AddQuestionsetup = () => {
  const data = ["MCQ", "CODING"];

  const [clickvalue, setClickvalue] = useState("MCQ");
  const [clickevent, setClickevent] = useState(false);
  
  const click = (value) => {
    console.log("---->>>>>>>>>>>>>>",value)

    setClickvalue(value);
    if (value === "MCQ") {
      window.location.href = "/addQuestion/addQuestionSetup/mcq";
    }
    else if (value === "CODING") {
      window.location.href = "/addQuestion/addQuestionSetup/coding";
    }
  };

  const getvaluesfun = (value) => {
    setClickevent(value);
    console.log(clickevent, "hello");
  };

  return (
    <div className="AddQuestion">
      <Switching Arrayofvalue={data} clickinfo={click} renderContent={setClickvalue} />
      {clickvalue === "MCQ" ? (
        <McqQuestion getvalue={getvaluesfun} />
      ) : (
        <CodingQuestion getvalue={getvaluesfun} />
      )}
    </div>
  );
};

export default AddQuestionsetup;