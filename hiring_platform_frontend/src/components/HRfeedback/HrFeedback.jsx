import React, { useState, useEffect } from "react";
import "./HrFeedback.scss";
import { IoPersonSharp } from "react-icons/io5";
import axios from "axios";
import { useLocation, useParams } from "react-router";

const HrFeedback = () => {
  const location=useLocation();
  const data=location.state;
  const [name, setName] = useState(data.name ? data.name : "");
  const [scheduleDateTime, setScheduleDateTime] = useState(data.schedule ? data.schedule : ""); 
  const [feedback, setFeedback] = useState("");
  const [description, setDescription] = useState("");
  const { interviewId } = useParams();

 

  const handleSubmit = async () => {
    try {
      const response = await axios.put(
        `http://192.168.1.20:8081/api/v1/employee/interview/${interviewId}`,
        {
          feedback,
          description,
          
        }, {
          headers:{
            'Authorization':`${sessionStorage.getItem("token")}`
          }
        }
      );
      setFeedback("");
      setDescription("");
    } catch (error) {
      console.error("Error submitting feedback:", error);
    }
  };

  return (
    <div className="hr-container">
      <div className="feedback-header">
        <h2>FEEDBACK</h2>
      </div>

      <div className="name-container">
        NAME:
        <div>
          <input
            className="input"
            value={name}
            readOnly 
             />
        </div>
      </div>
      <div className="person-icon">
        <IoPersonSharp />
      </div>
      <div className="date-container">
        SCHEDULE DATE AND TIME:
        <div>
          <input
            type="text"
            className="input"
            value={new Date(scheduleDateTime).toLocaleString()}
            readOnly
          />
        </div>
      </div>
      <div className="feedback-containers">
        FEEDBACK:
        <div>
          <label>
            <input
              type="radio"
              name="feedback"
              value="SELECTED"
              checked={feedback === "SELECTED"}
              onChange={() => setFeedback("SELECTED")}
            />
            Eligible
          </label>
          <label>
            <input
              type="radio"
              name="feedback"
              value="CAN_BE_CONSIDERATE"
              checked={feedback === "CAN_BE_CONSIDERATE"}
              onChange={() => setFeedback("CAN_BE_CONSIDERATE")}
            />
            Can Be Considered
          </label>
          <label>
            <input
              type="radio"
              name="feedback"
              value="REJECTED"
              checked={feedback === "REJECTED"}
              onChange={() => setFeedback("REJECTED")}
            />
            Rejected
          </label>
        </div>
      </div>
      <div className="comment-container">
        DESCRIPTION:
        <div>
          <input
            type="text"
            className="input1"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </div>
      </div>
      <button type="button" className="submit" onClick={handleSubmit}>
        SUBMIT
      </button>
    </div>
  );
};

export default HrFeedback;
