import React, { useEffect, useState } from "react";
import "./QuestionSet.scss";
import axios from "axios";

function QuestionSet() {
  const [completedContestantData, setCompletedContestantData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchCompletedContestantData = async () => {
      try {
        const response = await axios.get(
          `http://192.168.1.20:8081/api/v1/result/code/${sessionStorage.getItem("codingId")}`,
          {
            headers:{
              'Authorization':`${sessionStorage.getItem("token")}`
            }
          }
        );
        setCompletedContestantData(response.data.object);
      } catch (error) {
        console.error("Error fetching completed interview data:", error);
      } finally {
        setLoading(false); 
      }
    };
     
    fetchCompletedContestantData();
  }, []);

  return (
    <div>
      {completedContestantData.map((qa, index) => (
        <div className="question-set-container" key={index}>
          <h1 className="coding-heading">Coding Question {index + 1}</h1>
          <div className="question-wrapper">
            <h2 className="question-heading">Question</h2>
            <p className="question-input"> {qa.question} </p>
          </div>
          <div className="answer-wrapper">
            <h2 className="answer-heading">Answer</h2>
            <p className="test-case-pass-percentage">Test Cases: {qa.passCount}/{qa.testCaseCount}</p>
            <textarea className="answer-input" value={qa.code} readOnly></textarea>
          </div>
        </div>
      ))}
    </div>
  );
}

export default QuestionSet;
