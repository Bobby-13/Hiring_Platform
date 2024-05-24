import React, { useState } from "react";
import { FaTimes } from "react-icons/fa";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./MeetingRoom.scss";
import Button from "../../../components/Button";

const MeetingRoom = () => {
  const [meetingId, setMeetingId] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const handleChangeMeetingId = (e) => {
    const value = e.target.value;
    const regex = /^[0-9]*$/;
    if (regex.test(value)) {
      setMeetingId(value);
      if (value.length > 11) {
        setErrorMessage("Meeting ID should not exceed 11 characters");
      } else {
        setErrorMessage("");
      }
    } else {
      setErrorMessage("Invalid input. Please enter numbers only.");
    }
  };

  const handleChangePassword = (e) => {
    setPassword(e.target.value);
  };

  const clearInput = () => {
    setMeetingId("");
    setPassword("");
    setErrorMessage("");
  };

  const handleJoinNow = () => {
    if (meetingId.length > 11) {
      toast.error("Meeting ID should not exceed 11 characters", {
        position: toast.POSITION.TOP_CENTER,
      });
    } else {
      // Construct the Zoom meeting join URL
      const zoomMeetingURL = `https://us05web.zoom.us/j/${meetingId}?pwd=${password}`;

      // Redirect the user to the Zoom meeting URL
      window.location.href = zoomMeetingURL;
    }
  };

  return (
    <div className="meeting-room">
      Join a meeting
      <div>
        <input
          className="inputField"
          value={meetingId}
          placeholder="Enter meeting ID"
          onChange={handleChangeMeetingId}
        />
        <FaTimes className="close-icon" onClick={clearInput} />

        <input
          className="inputField"
          type="password"
          value={password}
          placeholder="Enter password"
          onChange={handleChangePassword}
        />
        {errorMessage && <div className="error-message">{errorMessage}</div>}
      </div>
      <Button className="join-button" text="Join Now" onClick={handleJoinNow} />
    </div>
  );
};

export default MeetingRoom;
