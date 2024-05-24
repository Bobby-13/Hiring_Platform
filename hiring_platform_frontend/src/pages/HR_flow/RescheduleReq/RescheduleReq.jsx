import React, { useState } from "react";
import "./RescheduleReq.scss";
import axios from "axios";
import { useParams } from "react-router";

const RescheduleReq = () => {
  const { interviewId } = useParams();
  const [isRescheduleSelected, setIsRescheduleSelected] = useState(true);
  const [reason, setReason] = useState('');
  const [preferredTime, setPreferredTime] = useState('');

  const handleRadioChange = (e) => {
    setIsRescheduleSelected(e.target.value === "eligible");
  };

  const reschedulereq = async () => {
    try {
      const interviewRequestType = isRescheduleSelected ? 'RESCHEDULE' : 'CANCEL';
      const data = {
        reason,
        preferredTime: convertToUTC(preferredTime) 
      };
      const response = await axios.post(
        `http://192.168.1.20:8081/api/v1/employee/${interviewId}/interviewRequestType/${interviewRequestType}`,
        data, {
          headers:{
            'Authorization':`${sessionStorage.getItem("token")}`
          }
        }
      );
      console.log("response.data", response.data);
    } catch (err) {
      console.log(err);
    }
  };

  const convertToUTC = (timeString) => {
    const date = new Date(timeString);
    return date.toISOString();
  };

  return (
    <div className="reschedule-container">
      RESCHEDULE REQUEST
      <div className="radio-container">
        <label>
          <input
            type="radio"
            name="feedback"
            value="eligible"
            checked={isRescheduleSelected}
            onChange={handleRadioChange}
          />
          Reschedule
        </label>
        <label>
          <input
            type="radio"
            name="feedback"
            value="considered"
            checked={!isRescheduleSelected}
            onChange={handleRadioChange}
          />
          Cancel Meet
        </label>
      </div>
      <div className="comment-container">
        REASON:
        <div>
          <input
            type="text"
            className="input1"
            value={reason}
            onChange={(e) => setReason(e.target.value)}
          />
        </div>
      </div>
      {isRescheduleSelected && (
        <div className="time-slot-container">
          <h3>PREFFERRED TIME </h3>
          <input
           className="datetime"
            type="datetime-local"
            value={preferredTime}
            onChange={(e) => setPreferredTime(e.target.value)}
          />
        </div>
      )}
      <button
        type="button"
        className="submit"
        onClick={reschedulereq}
      >
        SUBMIT
      </button>
    </div>
  );
};

export default RescheduleReq;
