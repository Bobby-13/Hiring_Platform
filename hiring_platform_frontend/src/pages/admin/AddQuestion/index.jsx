import React, { useState } from "react";
import "./addQuestion.scss";
import Switching from "../../../components/Switching/Switching";
import CodingAddQuestion from "./CodingAddQuestion";
import McqAddQuestion from "./McqAddQuestion";
const AddQuestion = () => {
  const arrayData = ["MCQ", "CODING"];

  const [clickvalue, setClickvalue] = useState("MCQ");
  const click = (value) => {
    setClickvalue(value);
  };

  return (
    <div className="AddQuestion">
      <Switching Arrayofvalue={arrayData} clickinfo={click} renderContent={setClickvalue}/>
      {clickvalue === "MCQ" ? <McqAddQuestion /> : <CodingAddQuestion />}
    </div>
  );
};

export default AddQuestion;
