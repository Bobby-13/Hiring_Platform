import React, { useState } from "react";
import "./dateTime.scss";
const DateTime = () => {
  const [dateTime, setDateTime] = useState({ date: "", time: "" });

  const handleDateTimeChange = (event) => {
    const value = event.target.value;
    const dateValue = value.slice(0, 10);
    const timeValue = value.slice(11);
    setDateTime({ date: dateValue, time: timeValue });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    console.log("Date:", dateTime.date);
    console.log("Time:", dateTime.time);
  };

  return (
    <div className="DateTime">
      <input
        type="datetime-local"
        value={`${dateTime.date}T${dateTime.time}`}
        onChange={handleDateTimeChange}
      />
      <button onClick={handleSubmit}>Submit</button>
    </div>
  );
};

export default DateTime;
