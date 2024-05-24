// RoundFilerDetails.js

import React, { useState, useEffect } from "react";
import "./RoundFilerDetails.scss";
import Button from "../../../components/Button";
import Table from "../../../components/Table";
import axios from "axios";
import { useLocation } from "react-router-dom";
import {
  roundFilterOptions,
  topContainerContent,
} from "../../../helpers/roundFilterDetails";
// import {RoundFilterData} from "/home/divum/shobi/trail/hiring-platform-frontend/src/helpers/addQuestionsHelper.js"
const RoundFilerDetails = ({ onUpdateClick }) => {
  const [filterType, setFilterType] = useState("percentage");
  const [filterValue, setFilterValue] = useState(0);
  const [roundData, setRoundData] = useState(null);
  const [showFilter, setShowFilter] = useState(false);
  const [showTable, setShowTable] = useState(false);
  const [roundId, setRoundId] = useState(""); 
  const location = useLocation();
  const buttonText = location.state?.buttonText;
  const roundIdFromParent = location.state?.roundId; 
  console.log("-->>>>>>", location.state?.roundId)

  const handleFilterTypeChange = (e) => {
    setFilterType(e.target.value);
  };

  const handleValueChange = (e) => {
    setFilterValue(e.target.value);
  };

  const handleIncrement = () => {
    const numericValue = Number(filterValue);
    setFilterValue(numericValue + 1);
  };

  const handleDecrement = () => {
    const numericValue = Number(filterValue);
    if (numericValue > 0) {
      setFilterValue(numericValue - 1);
    }
  };

  const handleApply = async () => {
    try {
      const response = await axios.get(
        `http://192.168.1.20:8081/api/v1/contest/user?type=ROUND_FILTER&roundId=${roundId}&passMark=${filterValue}`,
        {
          headers:{
            'Authorization':`${sessionStorage.getItem("token")}`
          }
        }
      );
      const roundData = response.data.object;
      if (roundData.contestantLists.length==0)
      {console.log("eeee")
      setRoundData(null);
      setShowTable(false);}
      else{
      setRoundData(roundData);
      setShowTable(true);
      console.log(roundData)
    } }catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    if (buttonText && buttonText === "Pending") {
      setShowFilter(true);
    } else {
      setShowFilter(false);
    }
  }, [buttonText]);

  useEffect(() => {
    if (showFilter && roundIdFromParent) {
      setRoundId(roundIdFromParent);
    }
  }, [showFilter, roundIdFromParent]);

  return (
    <div className="RoundFilerDetails">
      <h1>Round Details</h1>
      <div className="RoundFilerDetailsTopContainer">
        {topContainerContent.map((field, index) => (
          <p key={index}>
            {field.label} : <span>{field.value} </span>
          </p>
        ))}
      </div>
      {showFilter && (
        <div className="RoundFilerDetailsTopContentRight">
          <p>Filter Type :</p>
          <div className="RoundFilterRadioButtons">
            <input
              type="radio"
              id="percentage"
              value="percentage"
              checked={filterType === "percentage"}
              onChange={handleFilterTypeChange}
            />
            <label htmlFor="percentage">Percentage</label>
          </div>
          <div className="RoundFilterInputField">
            <input
              type="text"
              value={filterValue}
              onChange={handleValueChange}
            />
            <Button text="+" onClick={handleIncrement} className="plus" />
            <Button text="-" onClick={handleDecrement} className="minus" />
          </div>
          <Button text="Apply" onClick={handleApply} />
        </div>
      )}
      <div className="tableContainer">
        <h1>Contestant List</h1>
        {showTable && roundData && <Table data={roundData.contestantLists} />}
        {!showTable && <p>No data available.</p>}
      </div>
    </div>
  );
};

export default RoundFilerDetails;
